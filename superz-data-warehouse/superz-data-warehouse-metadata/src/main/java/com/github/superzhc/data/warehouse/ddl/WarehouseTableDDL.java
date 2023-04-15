package com.github.superzhc.data.warehouse.ddl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.base.Preconditions;
import com.github.superzhc.common.format.PlaceholderResolver;
import com.github.superzhc.common.jackson.JsonUtils;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

/**
 * @author superz
 * @create 2023/4/15 15:00
 **/
public abstract class WarehouseTableDDL implements Function<String, String> {
    private static final Logger LOG = LoggerFactory.getLogger(WarehouseTableDDL.class);

    private static final String DEFAULT_DDL_SCHEMA_PATH = "/ddl_table_schema.json";

    public final String convert(JsonNode json) {
        // 加载Schema文件
        InputStream in = this.getClass().getResourceAsStream(DEFAULT_DDL_SCHEMA_PATH);
        JsonSchemaFactory factory = JsonSchemaFactory.builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)).objectMapper(JsonUtils.mapper()).build();
        JsonSchema jsonSchema = factory.getSchema(in);

        // 1. 验证metadata是否符合标准
        Set<ValidationMessage> errors = jsonSchema.validate(json);
        if (errors.size() > 0) {
            throw new RuntimeException("元数据配置不符合规范！\n" + errors);
        }

        // 处理metadata
        Map<String, Object> metadata = new LinkedHashMap<>();
        String tableName = convertTableName(JsonUtils.string(json, "name"));
        List<Map<String, Object>> columns = convertColumns(JsonUtils.newObjectArray(json, "fields"));
        String comment = convertComment(JsonUtils.string(json, "comment"));

        String sql = PlaceholderResolver.getResolver("${", "}")
                .resolveByRule(ddlTemplate(), this);

        return sql;
    }

    @Override
    public final String apply(String param) {
        return null;
    }

    public abstract String ddlTemplate();

    public String convertTableName(String originTableName) {
        return originTableName;
    }

    public final List<Map<String, Object>> convertColumns(Map<String, Object>[] originColumns) {
        Preconditions.checkNotNull(originColumns);
        beforeConvertColumns(originColumns);
        List<Map<String, Object>> columns = new ArrayList<>(originColumns.length);
        for (Map<String, Object> originColumn : originColumns) {
            Map<String, Object> column = beforeConvertColumn(originColumn);
            column = convertColumn(column);
            column = afterConvertColumn(column);
            if (null != column) {
                columns.add(column);
            }
        }
        columns = afterConvertColumns(columns);
        return columns;
    }

    public Map<String, Object>[] beforeConvertColumns(Map<String, Object>[] originColumns) {
        return originColumns;
    }

    public Map<String, Object> beforeConvertColumn(Map<String, Object> originColumn) {
        return originColumn;
    }

    public final Map<String, Object> convertColumn(Map<String, Object> originColumn) {
        Map<String, Object> column = new HashMap<>(originColumn.size());
        return column;
    }

    public Map<String, Object> afterConvertColumn(Map<String, Object> originColumn) {
        return originColumn;
    }

    public List<Map<String, Object>> afterConvertColumns(List<Map<String, Object>> originColumns) {
        return originColumns;
    }

    public String convertComment(String originComment) {
        return originComment;
    }
}
