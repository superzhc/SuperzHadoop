package com.github.superzhc.neo4j;

import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.github.superzhc.neo4j.Neo4jHelper.node2map;
import static com.github.superzhc.neo4j.Neo4jHelper.relationship2map;

/**
 * @author superz
 * @create 2023/2/2 15:33
 **/
public class Neo4jCQL {
    private static final Logger LOG = LoggerFactory.getLogger(Neo4jCQL.class);

    private Neo4jHelper helper;

    public Neo4jCQL(Neo4jHelper helper) {
        this.helper = helper;
    }

    /**
     * 获取节点的数量
     *
     * @return
     */
    public Long nodesSize() {
        String cql = "MATCH(n) RETURN COUNT(n)";
        return helper.read(cql).get(0).get(0).asLong();
    }

    /**
     * 通过内置过程获取所有节点标签
     * <p>
     * 注意：不知道如果给这内置过程的结果添加过滤条件
     *
     * @return
     */
    public List<String> labelsByFunction() {
        String cql = "call db.labels";
        Set<String> set = new HashSet<>();
        List<Record> records = helper.read(cql);
        for (Record record : records) {
            String label = record.get("label", (String) null);
            if (null != label) {
                set.add(label);
            }
        }

        if (set.size() == 0) {
            return null;
        }

        return new ArrayList<>(set);
    }

    /**
     * 通过CQL语句获取节点标签，此方式可对节点进行过滤条件设置，性能未测试过
     *
     * @return
     */
    public List<String> labels() {
        String cql = "MATCH(n) RETURN DISTINCT [label IN LABELS(n)][0]";
        Set<String> set = new HashSet<>();

        List<Record> records = helper.read(cql);
        for (Record record : records) {
            String sigleNodeLabel = record.get(0).asString(null);
            if (null != sigleNodeLabel) {
                set.add(sigleNodeLabel);
            }
        }

        if (set.size() == 0) {
            return null;
        }

        return new ArrayList<>(set);
    }

    /**
     * 判断标签是否存在
     *
     * @param label 标签名称
     * @return
     */
    public boolean existLabel(String label) {
        String cql = "MATCH (n) WHERE ANY(label IN LABELS(n) WHERE label=$label) RETURN count(n)";

        Map<String, Object> params = new HashMap<>();
        params.put("label", label);

        List<Record> records = helper.read(cql, params);
        if (null == records) {
            return false;
        }

        int nodeNum = records.get(0).get(0).asInt();
        return nodeNum > 0;
    }

    public Map<String, Object> createNode(String label) {
        return createNode(label, new HashMap<>());
    }

    public Map<String, Object> createNode(String label, Map<String, Object> properties) {
        String cqlTemplate = "CREATE(n:%s $properties) RETURN n";
        String cql = String.format(cqlTemplate, label);

        Map<String, Object> params = new HashMap<>();
        params.put("properties", properties);

        List<Record> records = helper.write(cql, params);
        Node node = records.get(0).get("n").asNode();

        return node2map(node);
    }

    public void deleteNodeProperties(Long id, String... propertyNames) {
        if (null == propertyNames || propertyNames.length == 0) {
            return;
        }

        String cqlTemplate = "MATCH(n) WHERE id(n)=$id REMOVE %s";

        StringBuilder propertySb = new StringBuilder();
        for (String propertyName : propertyNames) {
            propertySb.append(",").append("n.").append(propertyName);
        }
        String cql = String.format(cqlTemplate, propertySb.substring(1));

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        helper.write(cql, params);
    }

    public Map<String, Object> createRelationship(Long start, Long end, String type) {
        return createRelationship(start, end, type, new HashMap<>());
    }

    public Map<String, Object> createRelationship(Long start, Long end, String type, Map<String, Object> properties) {
        String cqlTemplate = "MATCH (source),(target) WHERE id(source)=$start AND id(target)=$end CREATE (source)-[r:%s $properties]->(target) RETURN r";
        String cql = String.format(cqlTemplate, type);

        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("properties", properties);

        List<Record> records = helper.write(cql, params);
        Relationship relationship = records.get(0).get("r").asRelationship();

        return relationship2map(relationship);
    }

    public void deleteRelationProperties(Long id, String... propertyNames) {
        if (null == propertyNames || propertyNames.length == 0) {
            return;
        }

        String cqlTemplate = "MATCH()-[r]->() WHERE id(r)=$id REMOVE %s";

        StringBuilder propertySb = new StringBuilder();
        for (String propertyName : propertyNames) {
            propertySb.append(",").append("r.").append(propertyName);
        }
        String cql = String.format(cqlTemplate, propertySb.substring(1));

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        helper.write(cql, params);
    }

    public static void main(String[] args) throws Exception {
        String uri = "bolt://localhost:7687";
        String username = "neo4j";
        String password = "password";
        try (Neo4jHelper neo4jHelper = Neo4jHelper.connect(uri, username, password)) {
            Neo4jCQL operator = new Neo4jCQL(neo4jHelper);

            Map<String, Object> properties = new LinkedHashMap<>();
            properties.put("creator", "superz");
            properties.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));

            //System.out.println(operator.existLabel("Person"));
//            System.out.println(operator.createNode("Boat"));
//            System.out.println(operator.createNode("Car", properties));
//            System.out.println(operator.createRelationship(17l, 20l, "拥有", properties));
            System.out.println(operator.nodesSize());
        }
    }
}
