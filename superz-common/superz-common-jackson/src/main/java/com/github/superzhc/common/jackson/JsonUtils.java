package com.github.superzhc.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public static ObjectMapper mapper() {
        return mapper;
    }

    public static String asString(JsonNode json) {
        try {
            return mapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String asString(Map<?, ?> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String asString(T[] objs) {
        try {
            return mapper.writeValueAsString(objs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String asString(List<T> lst) {
        try {
            return mapper.writeValueAsString(lst);
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

    public static Map<String, Object> map(String json, String... path) {
        JsonNode node = json(json, path);
        return map(node);
    }

    public static Map<String, Object> map(JsonNode json) {
        return mapper.convertValue(json, LinkedHashMap.class);
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

    public static String string(String json, String... paths) {
        JsonNode childNode = json(json, paths);
        return string(childNode);
    }

    public static String string(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        return null == childNode ? null : childNode.asText();
    }

    public static String text(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        if (null == childNode) {
            return null;
        } else if (childNode.isObject() || childNode.isArray()) {
            return asString(childNode);
        } else {
            return string(childNode);
        }
    }

    public static Integer integer(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        return null == childNode ? null : childNode.asInt();
    }

    public static Double aDouble(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        return null == childNode ? null : childNode.asDouble();
    }

    public static Long aLong(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        return null == childNode ? null : childNode.asLong();
    }

    public static Boolean bool(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        return null == childNode ? Boolean.FALSE : childNode.asBoolean();
    }

    public static List<String> list(JsonNode node, String... paths) {
        return Arrays.asList(array(node, paths));
    }

    public static String[] array(String json, String... paths) {
        return array(json(json), paths);
    }

    public static String[] array(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        String[] arr = new String[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : childNode.get(i).asText();
        }
        return arr;
    }

    public static int[] intArray(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        int[] arr = new int[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : childNode.get(i).asInt();
        }
        return arr;
    }


    public static long[] longArray(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        long[] arr = new long[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : childNode.get(i).asLong();
        }
        return arr;
    }

    public static LocalDateTime[] long2DateTimeArray(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        LocalDateTime[] arr = new LocalDateTime[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(childNode.get(i).asLong()), ZoneId.systemDefault());
        }
        return arr;
    }

    public static double[] doubleArray(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);
        double[] arr = new double[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : childNode.get(i).asDouble();
        }
        return arr;
    }

    public static String[] objectArrayKeys(JsonNode node, String... childPaths) {
        return objectArrayKeys(node, null, childPaths);
    }

    public static String[] objectArrayKeys(JsonNode node, String[] paths, String[] childPaths) {
        JsonNode childNode = node;
        if (null != paths) {
            childNode = json(node, paths);
        }

        // 保证列的顺序
        Set<String> columnNames = new LinkedHashSet<>();
//        if (childNode.isArray()) {
        for (JsonNode item : childNode) {
            JsonNode childItem = item;
            if (null != childPaths) {
                childItem = json(item, childPaths);
            }
            Iterator<String> fieldNames = childItem.fieldNames();
            while (fieldNames.hasNext()) {
                columnNames.add(fieldNames.next());
            }
        }
//        } else if (childNode.isObject()) {
//            JsonNode childNode2 = childNode;
//            if (null != childPaths) {
//                childNode2 = json(childNode, childPaths);
//            }
//            Iterator<String> fieldNames = childNode2.fieldNames();
//            while (fieldNames.hasNext()) {
//                columnNames.add(fieldNames.next());
//            }
//        } else {
//            return null;
//        }
        return columnNames.toArray(new String[columnNames.size()]);
    }

    public static List<String[]> objectArray(JsonNode node, String... childPaths) {
        return objectArray(node, null, childPaths);
    }

    public static List<String[]> objectArray(JsonNode node, String[] paths, String[] childPaths) {
        JsonNode childNode = node;
        if (null != paths) {
            childNode = json(node, paths);
        }

        List<String[]> table = new LinkedList<>();

        String[] columnNames = objectArrayKeys(childNode, childPaths);
        // 第一行是列的元数据信息
        table.add(columnNames);

//        if (childNode.isArray()) {
        int columnLength = columnNames.length;
        for (JsonNode item : childNode) {
            JsonNode childItem = item;
            if (null != childPaths) {
                childItem = json(item, childPaths);
            }
            String[] dataRow = new String[columnLength];
            for (int i = 0; i < columnLength; i++) {
                String columnName = columnNames[i];
                JsonNode dataCell = json(childItem, columnName);
                dataRow[i] = text(dataCell);
            }
            table.add(dataRow);
        }
//        } else if (childNode.isObject()) {
//            int columnLength = columnNames.length;
//              JsonNode childNode2=childNode;
//              if(null!=childPaths){
//                  childNode2=json(childNode,childPaths);
//              }
//            String[] dataRow = new String[columnLength];
//            for (int i = 0; i < columnLength; i++) {
//                String columnName = columnNames[i];
//                JsonNode dataCell = json(childNode, columnName);
//                dataRow[i] = text(dataCell);
//            }
//            table.add(dataRow);
//        } else {
//            return null;
//        }
        return table;
    }

    public static List<String[]> objectArrayWithKeys(JsonNode node, List<String> columnNames, String... childPaths) {
        return objectArrayWithKeys(node, null, childPaths, columnNames.toArray(new String[columnNames.size()]));
    }

    public static List<String[]> objectArrayWithKeys(JsonNode node, String[] columnNames, String... childPaths) {
        return objectArrayWithKeys(node, null, childPaths, columnNames);
    }

    public static List<String[]> objectArrayWithKeys(JsonNode node, String[] paths, String[] childPaths, String[] columnNames) {
        JsonNode childNode = node;
        if (null != paths) {
            childNode = json(node, paths);
        }

        List<String[]> table = new LinkedList<>();
        table.add(columnNames);
        int columnLength = columnNames.length;
        for (JsonNode item : childNode) {
            JsonNode childItem = item;
            if (null != childPaths) {
                childItem = json(item, childPaths);
            }
            String[] dataRow = new String[columnLength];
            for (int i = 0; i < columnLength; i++) {
                String columnName = columnNames[i];
                JsonNode dataCell = json(childItem, columnName);
                dataRow[i] = text(dataCell);
            }
            table.add(dataRow);
        }
        return table;
    }

    public static List<String[]> arrayArray(JsonNode node, String... paths) {
        JsonNode childNode = json(node, paths);

        List<String[]> dataRows = new ArrayList<>(childNode.size());
        for (JsonNode item : childNode) {
            String[] dataRow = array(item);
            dataRows.add(dataRow);
        }
        return dataRows;
    }

    /**
     * 推荐使用 objectArrayKeys
     *
     * @param datas
     * @param childPaths
     * @return
     */
    @Deprecated
    public static List<String> extractObjectColumnName(JsonNode datas, String... childPaths) {
        return Arrays.asList(objectArrayKeys(datas, childPaths));
    }

    /**
     * 推荐使用 objectArray
     *
     * @param datas
     * @param childPaths
     * @return
     */
    @Deprecated
    public static List<String[]> extractObjectData(JsonNode datas, String... childPaths) {
        List<String[]> table = objectArray(datas, null, childPaths);
        return table;
    }

    /**
     * 推荐使用 objectArrayWithKeys
     *
     * @param datas
     * @param columnNames
     * @param childPaths
     * @return
     */
    @Deprecated
    public static List<String[]> extractObjectData(JsonNode datas, List<String> columnNames, String... childPaths) {
        return objectArrayWithKeys(datas, columnNames.toArray(new String[columnNames.size()]), childPaths);
    }

    public static void main(String[] args) {
        Object[] objs = new Object[]{"Hello", "en", "zh-cn", true};
        Object[] objs2 = new Object[]{objs, new Object[]{null}};
        Object[] objs3 = new Object[]{"MkEWBc", JsonUtils.asString(objs2), null, "generic"};
        Object[] objs4 = new Object[]{objs3};
        Object[] objs5 = new Object[]{objs4};
        System.out.println(JsonUtils.asString(objs5));
    }
}
