package com.github.superzhc.neo4j;

import org.neo4j.driver.*;

/**
 * @author superz
 * @create 2023/1/4 11:20
 **/
public class HelloWorldMain {
    public static void main(String[] args) {
        String uri = "bolt://localhost:7687";
        String username = "neo4j";
        String password = "password";

        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password))) {
            try (Session session = driver.session()) {
//                String str = session.writeTransaction(tx -> {
//                    Result result = tx.run("CREATE(n:Record{k1:\"v1\",k2:10,k3:1.0}) RETURN n.k1+' , from code '+id(n)");
//                    return result.single().get(0).asString();
//                });
//                System.out.println(str);

                String str="superz-neo4j";


            }
        }
    }
}
