package cleanarchitecture.domain.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class UniqueIDGeneratorTest {

    @Test
    public void uuid() {
        StepVerifier.create(UniqueIDGenerator.uuid())
            .assertNext(uuid -> Assertions.assertThat(uuid).isNotEmpty())
            .verifyComplete();
    }

    @Test
    public void uuids() {
        final Mono<HashSet> ids = UniqueIDGenerator.uuids()
            .take(10000).collectList()
            .map(HashSet::new);

        StepVerifier.create(ids).assertNext(idsSet -> assertThat(idsSet).hasSize(10000))
            .verifyComplete();

    }

    @Test
    public void now() {
        StepVerifier.create(UniqueIDGenerator.now())
            .assertNext(date -> Assertions.assertThat(date).isCloseTo(new Date(), 500))
            .verifyComplete();
    }
}