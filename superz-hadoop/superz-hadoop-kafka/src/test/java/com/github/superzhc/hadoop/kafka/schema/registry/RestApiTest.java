package com.github.superzhc.hadoop.kafka.schema.registry;

import com.github.superzhc.common.http.HttpRequest;
import org.junit.Test;

public class RestApiTest {
    private static final String SCHEMA_REGISTRY_URL = "http://127.0.0.1:8081";

    @Test
    public void subjects() {
        String url = String.format("%s/subjects", SCHEMA_REGISTRY_URL);
        String result = HttpRequest.get(url).body();
        System.out.println(result);
    }

    @Test
    public void register() {
        String schema = "test";
        String url = String.format("%s/subjects/%s/versions", SCHEMA_REGISTRY_URL, schema);
        String result = HttpRequest.post(url).header("Content-Type", "application/vnd.schemaregistry.v1+json")
                .json("{\"schema\": \"{\\\"type\\\": \\\"string\\\"}\"}")
                .body();
        System.out.println(result);
    }

    @Test
    public void versions(){
        String schema = "test";
        String url = String.format("%s/subjects/%s/versions", SCHEMA_REGISTRY_URL, schema);
        String result=HttpRequest.get(url).body();
        System.out.println(result);
    }
}
