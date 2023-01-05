package com.github.superzhc.spring.data.neo4j.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author superz
 * @create 2023/1/4 18:07
 **/
@Node("SpringPerson")
@Data
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int age;

    /*Property用于定义别名*/
    @Property("mark")
    private String note;

    @Relationship(type = "friend", direction = Relationship.Direction.INCOMING)
    private Set<Person> friends = new HashSet<>();


}
