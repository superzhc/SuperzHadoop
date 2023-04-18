package com.github.superzhc.data.warehouse.ddl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.format.PlaceholderResolver;
import com.github.superzhc.common.jackson.JsonSchemaUtils;
import com.github.superzhc.common.jackson.JsonUtils;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Set;
import java.util.function.Function;

/**
 * @author superz
 * @create 2023/4/15 17:28
 **/
public final class WarehouseDDL {
    private static final Logger LOG = LoggerFactory.getLogger(WarehouseDDL.class);
    private static final String DEFAULT_DDL_TABLE_SCHEMA_PATH = "/ddl_table_schema.json";

    public static String convert(final JsonNode json, final WarehouseDDLDialect dialect) {
        // 校验json是否合法
        InputStream in = WarehouseDDL.class.getResourceAsStream(DEFAULT_DDL_TABLE_SCHEMA_PATH);
        JsonSchema schema = JsonUtils.jsonSchema(in);
        return convert(schema, json, dialect);
    }

    public static String convert(final JsonNode jsonSchema, final JsonNode json, final WarehouseDDLDialect dialect) {
        JsonSchemaFactory factory = JsonSchemaUtils.getFactory();
        JsonSchema schema = factory.getSchema(jsonSchema);
        return convert(schema, json, dialect);
    }

    public static String convert(final JsonSchema schema, final JsonNode json, final WarehouseDDLDialect dialect) {
        Set<ValidationMessage> errors = schema.validate(json);
        if (errors.size() > 0) {
            throw new RuntimeException("元数据配置不符合规范！\n" + errors);
        }

        String sqlTemplate = dialect.ddlTemplate();
        LOG.info("[SQL Template] {}", sqlTemplate);
        String sql = PlaceholderResolver.getResolver("${", "}")
                .resolveByRule(sqlTemplate, new Function<String, String>() {
                    @Override
                    public String apply(String param) {
                        if (null == param) {
                            return "";
                        }

                        Object[] jsonPaths = JsonUtils.convertPaths(param);
                        Object value = JsonUtils.objectValue(json, jsonPaths);
                        String dialectValue = dialect.convertParam(param, value);
                        return null == dialectValue ? param : dialectValue;
                    }
                });
        LOG.info("[SQL] {}", sql);
        return sql;
    }
}
