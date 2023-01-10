package com.github.superzhc.spring.data.neo4j.dao;

import com.github.superzhc.spring.data.neo4j.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PersonRepository2 extends Neo4jRepository<Person, Long> {
}
