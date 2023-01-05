package com.github.superzhc.spring.data.neo4j.controller;

import com.github.superzhc.spring.data.neo4j.dao.SchoolRepository;
import com.github.superzhc.spring.data.neo4j.entity.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author superz
 * @create 2023/1/5 15:38
 **/
@RestController
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    private SchoolRepository schoolRepository;

    @PutMapping
    public Mono<School> createOrUpdate(@RequestBody School school){
        return schoolRepository.save(school);
    }
}
