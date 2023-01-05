package com.github.superzhc.spring.data.neo4j.dao;

import com.github.superzhc.spring.data.neo4j.entity.School;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends ReactiveNeo4jRepository<School, Long> {
}
