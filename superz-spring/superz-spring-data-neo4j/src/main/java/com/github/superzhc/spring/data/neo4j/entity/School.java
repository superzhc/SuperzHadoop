package com.github.superzhc.spring.data.neo4j.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

/**
 * @author superz
 * @create 2023/1/5 15:34
 **/
@Node("School")
@Data
public class School {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String tel;

    @Relationship(type = "学校",direction = Relationship.Direction.INCOMING)
    private List<SchoolRelation> schoolRelations;
}
