package com.github.superzhc.neo4j;

import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/2/2 15:19
 **/
public class Neo4jHelper implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(Neo4jHelper.class);

    private Driver driver;

    public Neo4jHelper(Driver driver) {
        this.driver = driver;
    }

    public static Neo4jHelper connect(String uri, String username, String password) {
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        return create(driver);
    }

    public static Neo4jHelper create(Driver driver) {
        return new Neo4jHelper(driver);
    }

    public List<Record> read(String cql) {
        return read(cql, null);
    }

    public List<Record> read(String cql, Map<String, Object> params) {
        try (Session session = driver.session()) {
            LOG.info("CQL:{};\n\tparams:{}", cql, params);
            return session.readTransaction(tx -> {
                Result result = tx.run(cql, params);
                if (result.hasNext()) {
                    return result.list();
                }
                return null;
            });
        }
    }

    public List<Record> write(String cql) {
        return write(cql, null);
    }

    public List<Record> write(String cql, Map<String, Object> params) {
        try (Session session = driver.session()) {
            LOG.info("CQL:{};\n\tparams:{}", cql, params);
            return session.writeTransaction(tx -> {
                Result result = tx.run(cql, params);
                if (result.hasNext()) {
                    return result.list();
                }
                return null;
            });
        }
    }

    public static Map<String, Object> node2map(Node node) {
        if (null == node) {
            return null;
        }

        Long __id = node.id();

        List<String> labels = new ArrayList<>();
        for (String label : node.labels()) {
            labels.add(label);
        }

        Map<String, Object> properties = node.asMap();

        Map<String, Object> data = new HashMap<>();
        data.put("__id", __id);
        /*第一个标签作为主标签，默认规则，不单独创建primaryLabel属性了*/
        data.put("labels", labels);
        data.put("properties", properties);
        return data;
    }

    public static Map<String, Object> relationship2map(Relationship relationship) {
        if (null == relationship) {
            return null;
        }

        Long __id = relationship.id();

        /*开始节点id*/
        Long start = relationship.startNodeId();

        /*结束节点id*/
        Long end = relationship.endNodeId();

        String type = relationship.type();

        Map<String, Object> properties = relationship.asMap();

        Map<String, Object> data = new HashMap<>();
        data.put("__id", __id);
        data.put("start", start);
        data.put("end", end);
        data.put("type", type);
        data.put("properties", properties);
        return data;
    }

    public Driver getDriver() {
        return driver;
    }

    @Override
    public void close() throws IOException {
        if (null != driver) {
            driver.close();
        }
    }
}
