package com.reactive.repository.jpa;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;
import java.util.function.Supplier;

import static reactor.core.publisher.Flux.defer;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.fromSupplier;

public abstract class AdapterOperations<E, D, I, R extends CrudRepository<D, I> & QueryByExampleExecutor<D>> {

    protected R repository;
    protected ObjectMapper mapper;
    private Class<D> dataClass;
    private Function<D, E> toEntityFn;

    @SuppressWarnings("unchecked")
    public AdapterOperations(R repository, ObjectMapper mapper, Function<D, E> toEntityFn) {
        this.repository = repository;
        this.mapper = mapper;
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.dataClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
        this.toEntityFn = toEntityFn;
    }

    public Mono<E> save(E entity) {
        return Mono.just(entity)
            .map(this::toData)
            .flatMap(this::saveData)
            .thenReturn(entity);
    }

    protected Flux<E> saveAllEntities(Flux<E> entities) {
        return entities.map(this::toData).collectList()
            .flatMapMany(this::saveData).map(this::toEntity);
    }

    private Mono<E> doQuery(Mono<D> query) {
        return query.map(this::toEntity);
    }

    public Mono<E> findById(I id) {
        return doQuery(fromSupplier(() -> repository.findById(id)).subscribeOn(Schedulers.boundedElastic()).flatMap(Mono::justOrEmpty));
    }

    public Flux<E> findByExample(E entity) {
        return doQueryMany(() -> repository.findAll(Example.of(toData(entity))));
    }

    protected Flux<E> doQueryMany(Supplier<Iterable<D>> query) {
        return fromSupplier(query).subscribeOn(Schedulers.boundedElastic())
            .flatMapMany(Flux::fromIterable)
            .map(this::toEntity);
    }

    protected D toData(E entity) {
        return mapper.map(entity, dataClass);
    }

    protected E toEntity(D data) {
        return toEntityFn.apply(data);
    }

    protected Mono<D> saveData(D data) {
        return fromSupplier(() -> repository.save(data))
            .subscribeOn(Schedulers.boundedElastic());
    }

    protected Flux<D> saveData(Iterable<D> data) {
        return defer(() -> fromIterable(repository.saveAll(data)))
            .subscribeOn(Schedulers.boundedElastic());
    }

}
