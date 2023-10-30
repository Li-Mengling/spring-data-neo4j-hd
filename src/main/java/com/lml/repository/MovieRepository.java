package com.lml.repository;

import com.lml.pojo.MovieEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository
public interface MovieRepository extends ReactiveNeo4jRepository<MovieEntity, String> {
    //To get that 0 or 1 return result, we can use the reactive return type of Mono<MovieEntity>
    Mono<MovieEntity> findOneByTitle(String title);
}