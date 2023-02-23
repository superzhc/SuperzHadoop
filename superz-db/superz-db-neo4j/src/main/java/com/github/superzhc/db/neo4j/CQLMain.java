package com.github.superzhc.db.neo4j;

/**
 * @author superz
 * @create 2023/1/5 16:12
 **/
public class CQLMain {
    /**
     * 创建节点
     * 示例：CREATE (n:Person {name:'John'}) RETURN n
     * CREATE是创建操作，Person是<b>标签</b>，代表节点的类型。花括号{}代表节点的<b>属性</b>
     */

    /**
     * 增加/修改节点属性
     * 示例：MATCH (a:Person {name:'Liz'}) SET a.age=34
     *
     * 删除节点属性
     * 示例：MATCH (a:Person {name:'Mike'}) REMOVE a.test
     */

    /**
     * 删除节点
     * 示例1：MATCH (a:Location {city:'Portland'}) DELETE a
     * 示例2：MATCH (p:People) WHERE p.id=2 DELETE p
     */

    /**
     * 创建关系
     * 示例：
     * MATCH (a:Person {name:'Liz'}),
     *       (b:Person {name:'Mike'})
     * MERGE (a)-[:FRIENDS]->(b)
     * 这里的方括号[]即为关系，FRIENDS为关系的类型。注意这里的箭头-->是有方向的，表示是从a到b的关系。
     *
     * 关系也可以添加属性，示例如下：
     * MATCH (a:Person {name:'Shawn'}),
     *       (b:Person {name:'Sally'})
     * MERGE (a)-[:FRIENDS {since:2001}]->(b)
     */

    /**
     * 删除有关系的节点
     * 示例：MATCH (a:Person {name:'Todd'})-[rel]-(b:Person) DELETE a,b,rel
     */

    /**
     * 查询所有对外有关系的节点：<code>MATCH (a)-->() RETURN a</code>
     * 查询所有有关系的节点:<code>MATCH (a)--() RETURN a</code>
     */
}
