package com.github.superzhc.db.neo4j;

import org.junit.Test;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

public class Neo4jHelperTest {

    private Neo4jHelper helper;

    @org.junit.Before
    public void setUp() throws Exception {
        helper = Neo4jHelper.connect("bolt://10.90.18.146:7687", "neo4j", "password");
    }

    @Test
    public void createNodes() {
        String cql = "CREATE(n:%s $properties) RETURN n";

        String batchFlag = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        for (int i = 0; i < 100; i++) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("name", String.format("%s-%d", batchFlag, i));
            properties.put("flag", batchFlag);

            Map<String, Object> params = new HashMap<>();
            params.put("properties", properties);
            helper.write(String.format(cql, "n" + i), params);
        }
    }

    @Test
    public void buildRelationships() {
        String cql = "MATCH (source),(target) WHERE id(source)=$start AND id(target)=$end CREATE (source)-[r:friend $properties]->(target) RETURN r";

        Random rand = new Random();
        String batchFlag = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        for (int i = 0; i < 50; i++) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("flag", batchFlag);

            int start = rand.nextInt(200);
            int end = rand.nextInt(200);
            Map<String, Object> params = new HashMap<>();
            params.put("start", start);
            params.put("end", start == end ? end + 1 : end);
            params.put("properties", properties);
            helper.write(cql, params);
        }
    }

    @Test
    public void graph() {
        String cql = "MATCH (n) where id(n)>55 OPTIONAL MATCH (n)-[r]-(m) RETURN n,r,m LIMIT $limit";

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 10);

        List<Record> records = helper.read(cql, params);
        if (null != records) {
            for (Record record : records) {
                Map<String, Object> data = new HashMap<>();

                Node start = record.get("n").asNode();
                data.put("start", Neo4jHelper.node2map(start));

                Value endNode = record.get("m");
                if (!endNode.isNull()) {
                    Node end = endNode.asNode();
                    data.put("end", Neo4jHelper.node2map(end));
                }

                Value relationshipObj = record.get("r");
                if (!relationshipObj.isNull()) {
                    Relationship relationship = relationshipObj.asRelationship();
                    data.put("relationship", Neo4jHelper.relationship2map(relationship));
                }

                System.out.println(data);
            }
        }
    }

    @Test
    public void nodeDirectRelationshipNode() {
        String cql = "MATCH(n)-[r]-(m) WHERE id(n)=$id RETURN r,m";

        Map<String, Object> params = new HashMap<>();
        params.put("id", 21);

        List<Record> records = helper.read(cql, params);
        if (null != records) {
            for (Record record : records) {
                Map<String, Object> data = new HashMap<>();

//                Node start = record.get("n").asNode();
//                data.put("start", Neo4jHelper.node2map(start));

                Value endNode = record.get("m");
                if (!endNode.isNull()) {
                    Node end = endNode.asNode();
                    data.put("end", Neo4jHelper.node2map(end));
                }

                Value relationshipObj = record.get("r");
                if (!relationshipObj.isNull()) {
                    Relationship relationship = relationshipObj.asRelationship();
                    data.put("relationship", Neo4jHelper.relationship2map(relationship));
                }

                System.out.println(data);
            }
        }
    }
}