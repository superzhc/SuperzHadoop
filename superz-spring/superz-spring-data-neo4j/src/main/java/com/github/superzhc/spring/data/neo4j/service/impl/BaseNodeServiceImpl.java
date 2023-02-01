package com.github.superzhc.spring.data.neo4j.service.impl;

import com.github.superzhc.spring.data.neo4j.entity.BaseNode;
import com.github.superzhc.spring.data.neo4j.service.BaseNodeService;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

/**
 * @author superz
 * @create 2023/2/1 17:22
 **/
@Service
@AllArgsConstructor
public class BaseNodeServiceImpl<T extends BaseNode> implements BaseNodeService<T> {
    private final Neo4jTemplate neo4jTemplate;


//    @Override
//    public <T extends BaseNode> T save(T entity) {
//        return neo4jTemplate.save(entity);
//    }
//
//    @Override
//    public <T extends BaseNode> T getById(Long id) {
////        return neo4jTemplate.findById(id,);
//        return null;
//    }
//
//    @Override
//    public <T extends BaseNode> T deleteById(Long id) {
//        return null;
//    }
}
