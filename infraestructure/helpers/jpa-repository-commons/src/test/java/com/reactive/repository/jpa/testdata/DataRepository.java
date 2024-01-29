package com.reactive.repository.jpa.testdata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

interface DataRepository extends CrudRepository<DataEntity, String>, QueryByExampleExecutor<DataEntity> {
}
