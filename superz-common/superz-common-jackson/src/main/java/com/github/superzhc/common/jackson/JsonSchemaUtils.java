package com.github.superzhc.common.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * @author superz
 * @create 2023/4/15 15:44
 **/
public class JsonSchemaUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonSchemaUtils.class);

    public static SpecVersion.VersionFlag DEFAULT_SPEC_VERSION = SpecVersion.VersionFlag.V7;

    public static JsonSchemaFactory getFactory() {
        return getFactory(DEFAULT_SPEC_VERSION);
    }

    public static JsonSchemaFactory getFactory(SpecVersion.VersionFlag version) {
        JsonSchemaFactory factory = JsonSchemaFactory
                .builder(JsonSchemaFactory.getInstance(version))
                .objectMapper(JsonUtils.mapper())
                .build();
        return factory;
    }

    public static JsonSchema loads(JsonSchemaFactory factory, String schemaString) {
        return factory.getSchema(schemaString);
    }

    public static JsonSchema loads(JsonSchemaFactory factory, InputStream in) {
        return factory.getSchema(in);
    }

    public static JsonSchema loads(JsonSchemaFactory factory, File file) {
        return factory.getSchema(file.toURI());
    }

    public static JsonSchema loads(JsonSchemaFactory factory, JsonNode jsonSchema) {
        return factory.getSchema(jsonSchema);
    }
}
