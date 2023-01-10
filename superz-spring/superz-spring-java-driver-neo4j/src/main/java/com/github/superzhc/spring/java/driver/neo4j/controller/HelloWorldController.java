package com.github.superzhc.spring.java.driver.neo4j.controller;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/1/9 10:20
 **/
@RestController
@RequestMapping("/helloworld")
public class HelloWorldController {

    @Autowired
    private Driver driver;

    @GetMapping(path = "/schools", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getSchools(){
        try(Session session= driver.session()){
            return session.run("MATCH(s:School) RETURN s")
                    .list(r->r.get("s").asNode().get("name").asString());
        }
    }
}
