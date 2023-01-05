package com.github.superzhc.spring.data.neo4j.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

/**
 * @author superz
 * @create 2023/1/5 16:08
 **/
@RelationshipProperties
@Data
public class SchoolRelation {
    @RelationshipId
    private Long id;

    @TargetNode
    private Person person;

    private String level;
}
