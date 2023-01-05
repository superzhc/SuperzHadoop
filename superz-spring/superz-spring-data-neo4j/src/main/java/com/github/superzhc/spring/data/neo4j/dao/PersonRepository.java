package com.github.superzhc.spring.data.neo4j.dao;

import com.github.superzhc.spring.data.neo4j.entity.Person;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PersonRepository extends ReactiveNeo4jRepository<Person, Long> {

    @Query("match(n:SpringPerson) where n.name = $name return n")
    Flux<Person> getByName(@Param("name") String name);

    Flux<Person> findByName(String name);
}
