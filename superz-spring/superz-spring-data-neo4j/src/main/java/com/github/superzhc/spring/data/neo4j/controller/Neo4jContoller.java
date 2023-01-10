package com.github.superzhc.spring.data.neo4j.controller;

import com.github.superzhc.spring.data.neo4j.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/1/10 9:27
 **/
@RestController
@RequestMapping("/neo4j")
@Slf4j
public class Neo4jContoller {

    @Autowired
    private Driver driver;

    @Autowired
    private Neo4jClient neo4jClient;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @GetMapping("/demo1")
    public List<String> demo1() {
        try (Session session = driver.session()) {
            return session.run("MATCH(s:School) RETURN s")
                    .list(r -> r.get("s").asNode().get("name").asString());
        }
    }

    @GetMapping("/demo2")
    public Collection<String> demo2() {
        return neo4jClient.query("MATCH(s:SpringPerson) RETURN s")
                .fetchAs(String.class)
                .mappedBy((types, records) -> records.get("s").get("name").asString())
                .all();
    }

    @GetMapping("/demo3")
    public List<Person> demo3(@RequestParam("name") String name){
        Map<String,Object> params=new HashMap<>();
        params.put("name",name);

        return neo4jTemplate.findAll("MATCH(s:SpringPerson) WHERE s.name=$name RETURN s",params, Person.class);
    }
}
