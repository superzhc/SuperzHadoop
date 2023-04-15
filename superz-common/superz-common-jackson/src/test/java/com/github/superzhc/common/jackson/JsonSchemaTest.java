package com.github.superzhc.common.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

/**
 * @author superz
 * @create 2023/4/14 17:26
 **/
public class JsonSchemaTest {
    @Test
    public void test() {
        // ObjectMapper mapper=JsonUtils.mapper();

        String basePath = "E:\\SuperzHadoop\\superz-data-warehouse\\superz-data-warehouse-metadata\\src\\main\\resources";
        JsonSchemaFactory factory = JsonSchemaFactory.builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)).objectMapper(JsonUtils.mapper()).build();

        File file = new File(basePath + "\\ddl_json_schema.json");
        JsonSchema jsonSchema = factory.getSchema(file.toURI());

        File dataFile = new File(basePath + "\\finance\\ddl_table_index.json");
        JsonNode json = JsonUtils.loads(dataFile);

        Set<ValidationMessage> errors = jsonSchema.validate(json);
        System.out.println(errors);
    }
}
