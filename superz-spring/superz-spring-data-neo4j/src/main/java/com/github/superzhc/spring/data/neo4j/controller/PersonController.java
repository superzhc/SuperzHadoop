package com.github.superzhc.spring.data.neo4j.controller;

import com.github.superzhc.spring.data.neo4j.dao.PersonRepository;
import com.github.superzhc.spring.data.neo4j.entity.Person;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author superz
 * @create 2023/1/5 10:07
 **/
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @PutMapping
    public Mono<Person> createOrUpdatePerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @GetMapping(value = {"", "/"})
    public Flux<Person> getPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/queryByName")
    public Flux<Person> getPersonsByName(@RequestParam String name) {
        return personRepository.findByName(name);
    }

    @GetMapping("/queryByName2")
    public Flux<Person> getPersonsByName2(@RequestParam String name) {
        return personRepository.getByName(name);
    }
}
