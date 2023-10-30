package com.lml.repository;

import com.lml.pojo.PersonEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface PersonRepository extends ReactiveNeo4jRepository<PersonEntity, Long> {
}

