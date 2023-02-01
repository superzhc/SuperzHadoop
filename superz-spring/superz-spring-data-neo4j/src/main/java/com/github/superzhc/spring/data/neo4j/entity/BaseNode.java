package com.github.superzhc.spring.data.neo4j.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * @author superz
 * @create 2023/2/1 17:19
 **/
@Node
@Data
public class BaseNode {
    @Id
    @GeneratedValue
    private Long id;

    @CreatedBy
    private String creator;
}
