package com.github.superzhc.fund.tablesaw.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author superz
 * @create 2022/4/2 10:05
 **/
public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        //允许使用未带引号的字段名
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许使用单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String string(JsonNode json) {
        try {
            return mapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String string(Map<?, ?> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(JsonNode json) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(Map<?, ?> map) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, ?> map(String json, String... path) {
        JsonNode node = json(json, path);
        return map(node);
    }

    public static Map<String, ?> map(JsonNode json) {
        return mapper.convertValue(json, Map.class);
    }

    public static JsonNode json(String json, String... paths) {
        try {
            JsonNode node = mapper.readTree(json);
            return json(node, paths);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode json(JsonNode json, String... paths) {
        JsonNode node = json;
        for (String path : paths) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            node = node.at(path);
        }
        return node;
    }

    public static List<String[]> extractArrayData(JsonNode datas, int... excludes) {
        List<String[]> rows = new ArrayList<>();
        skip:
        for (int j = 0, len2 = datas.size(); j < len2; j++) {

            for (int exclude : excludes) {
                if (j == exclude) {
                    continue skip;
                }
            }

            JsonNode data = datas.get(j);

            String[] row = array(data);
            rows.add(row);
        }
        return rows;
    }

    public static String[] array(JsonNode node) {
        String[] arr = new String[node.size()];
        for (int i = 0, len = node.size(); i < len; i++) {
            arr[i] = null == node.get(i) ? null : node.get(i).asText();
        }
        return arr;
    }

    public static List<String> extractObjectColumnName(JsonNode datas, String... paths) {
        // 保证列的顺序
        Set<String> columnNames = new LinkedHashSet<>();
        for (JsonNode data : datas) {
            JsonNode item = data;
            for (String path : paths) {
                item = item.get(path);
            }
            Iterator<String> fieldNames = item.fieldNames();
            while (fieldNames.hasNext()) {
                columnNames.add(fieldNames.next());
            }
        }
        return new ArrayList<>(columnNames);
    }

    public static List<String[]> extractObjectData(JsonNode datas, String... paths) {
        List<String> columnNames = extractObjectColumnName(datas, paths);
        return extractObjectData(datas, columnNames, paths);
    }

    public static List<String[]> extractObjectData(JsonNode datas, List<String> columnNames, String... paths) {
        List<String[]> rows = new ArrayList<>();
        try {
            for (JsonNode data : datas) {
                JsonNode item = data;
                for (String path : paths) {
                    item = item.get(path);
                }

                String[] row = new String[columnNames.size()];
                for (int i = 0, len = columnNames.size(); i < len; i++) {
                    String columnName = columnNames.get(i);
                    if (!item.has(columnName)) {
                        row[i] = null;
                    } else if (null == item.get(columnName)) {
                        row[i] = null;
                    } else if (item.get(columnName).isObject()) {
                        row[i] = mapper.writeValueAsString(item.get(columnName));
                    } else {
                        row[i] = item.get(columnName).asText();
                    }
                }
                rows.add(row);
            }
        } catch (Exception e) {
            log.error("抽取数据异常", e);
        }
        return rows;
    }
}
