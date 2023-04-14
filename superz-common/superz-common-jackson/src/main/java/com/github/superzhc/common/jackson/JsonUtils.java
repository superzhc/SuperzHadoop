package com.github.superzhc.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author superz
 * @create 2022/4/2 10:05
 **/
public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        //2022年12月6日 提供对Java8 LocalDate、LocalTime、LocalDateTime支持
        // 日期和时间格式化
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        mapper.registerModule(javaTimeModule);

        //允许使用未带引号的字段名
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许使用单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 忽略json字符串中不识别的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 日期类型字符串处理
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

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

    public static String format(String str) {
        JsonNode json = json(str);
        return format(json);
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

    public static String format(List<?> lst) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(lst);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> map(String json, Object... paths) {
        JsonNode node = loads(json, paths);
        return map(node);
    }

    public static Map<String, Object> map(JsonNode json, Object... paths) {
        JsonNode childNode = object(json, paths);
        return mapper.convertValue(childNode, LinkedHashMap.class);
    }

    /**
     * 推荐使用{@method loads}
     *
     * @param path
     * @param paths
     * @return
     */
    @Deprecated
    public static JsonNode file(String path, Object... paths) {
        return loads(new File(path), paths);
    }

    public static JsonNode loads(InputStream in, Object... paths) {
        try {
            JsonNode node = mapper.readTree(in);
            return object(node, paths);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode loads(File file, Object... paths) {
        try {
            JsonNode node = mapper.readTree(file);
            return object(node, paths);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode loads(String json, Object... paths) {
        try {
            JsonNode node = mapper.readTree(json);
            return object(node, paths);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 推荐使用{@method loads}
     *
     * @param json
     * @param paths
     * @return
     */
    @Deprecated
    public static JsonNode json(String json, Object... paths) {
        return loads(json, paths);
    }

    public static String simpleString(String json, Object... paths) {
        JsonNode childNode = loads(json, paths);
        return text(childNode);
    }

    public static JsonNode object(JsonNode json, Object... paths) {
        JsonNode node = json;
//        for (String path : paths) {
//            if (!path.startsWith("/")) {
//                path = "/" + path;
//            }
//            node = node.at(path);
//        }

        if (null == node) {
            return null;
        }

        for (Object path : paths) {
            if (null == path) {
                continue;
            }

            if (path.getClass() == String.class) {
                String str = (String) path;
                if (!str.startsWith("/")) {
                    str = "/" + str;
                }
                node = node.at(str);
            } else if (path.getClass() == int.class || path.getClass() == Integer.class) {
                node = node.get((int) path);
            } else {
                throw new RuntimeException("json 子节点的获取仅支持字符串字段和整型index序号");
            }
        }
        return node;
    }

    public static ArrayNode array(JsonNode json, Object... paths) {
        JsonNode childNode = object(json, paths);
        return (ArrayNode) childNode;
    }

    public static Object object2(JsonNode json, Object... paths) {
        JsonNode childNode = object(json, paths);
        if (null == childNode) {
            return null;
        }

        if (childNode.isMissingNode()) {
            return null;
        } else if (childNode.isObject()) {
            return map(childNode);
        } else if (childNode.isArray()) {
            List<Object> lst = list(childNode);
            return lst;
        } else if (childNode.isShort()) {
            return childNode.shortValue();
        } else if (childNode.isInt()) {
            return childNode.intValue();
        } else if (childNode.isLong()) {
            return childNode.longValue();
        } else if (childNode.isFloat()) {
            return childNode.floatValue();
        } else if (childNode.isDouble()) {
            return childNode.doubleValue();
        } else if (childNode.isBigDecimal()) {
            return childNode.decimalValue();
        } else if (childNode.isBigInteger()) {
            return childNode.bigIntegerValue();
        } else if (childNode.isBoolean()) {
            return childNode.booleanValue();
        } else if (childNode.isBinary()) {
            try {
                return childNode.binaryValue();
            } catch (Exception e) {
                log.error("转换binary异常！", e);
                return null;
            }
        } else if (childNode.isTextual()) {
            return childNode.textValue();
        } else if (childNode.isNull()) {
            return null;
        } else {
            log.debug("数据【{}】未知类型", asString(childNode));
            return null;
        }
    }

    /**
     * 见 simpleString 函数
     *
     * @param json
     * @param paths
     * @return
     */
    @Deprecated
    public static String string(String json, Object... paths) {
        return simpleString(json, paths);
    }

    public static String string(JsonNode node, Object... paths) {
        JsonNode childNode = object(node, paths);
        return null == childNode ? null : childNode.asText();
    }

    public static String text(JsonNode node, Object... paths) {
        JsonNode childNode = object(node, paths);
        if (null == childNode) {
            return null;
        } else if (childNode.isObject() || childNode.isArray()) {
            return asString(childNode);
        } else {
            return string(childNode);
        }
    }

    public static Integer integer(JsonNode node, Object... paths) {
        JsonNode childNode = object(node, paths);
        return null == childNode ? null : childNode.asInt();
    }

    public static Double aDouble(JsonNode node, Object... paths) {
        JsonNode childNode = object(node, paths);
        return null == childNode ? null : childNode.asDouble();
    }

    public static Long aLong(JsonNode node, Object... paths) {
        JsonNode childNode = object(node, paths);
        return null == childNode ? null : childNode.asLong();
    }

    public static Boolean bool(JsonNode node, Object... paths) {
        JsonNode childNode = object(node, paths);
        return null == childNode ? Boolean.FALSE : childNode.asBoolean();
    }

    public static <T> List<T> list(JsonNode node, Object... paths) {
        ArrayNode arrayNode = (ArrayNode) node;
        List<T> lst = new ArrayList<>(arrayNode.size());
        for (int i = 0, len = arrayNode.size(); i < len; i++) {
            JsonNode arrayChildNode = node.get(i);
            T value = (T) object2(arrayChildNode);
            lst.add(value);
        }
        return lst;
    }

    public static List<String> stringArray2List(JsonNode node, Object... paths) {
        return Arrays.asList(stringArray(node, paths));
    }

    public static String[] stringArray(String json, Object... paths) {
        return stringArray(json(json), paths);
    }

    public static String[] stringArray(JsonNode node, Object... paths) {
        ArrayNode childNode = array(node, paths);
        String[] arr = new String[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : childNode.get(i).asText();
        }
        return arr;
    }

    public static int[] intArray(JsonNode node, Object... paths) {
        ArrayNode childNode = array(node, paths);
        int[] arr = new int[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : childNode.get(i).asInt();
        }
        return arr;
    }


    public static long[] longArray(JsonNode node, Object... paths) {
        ArrayNode childNode = array(node, paths);
        long[] arr = new long[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : childNode.get(i).asLong();
        }
        return arr;
    }

    public static LocalDateTime[] long2DateTimeArray(JsonNode node, Object... paths) {
        ArrayNode childNode = array(node, paths);
        LocalDateTime[] arr = new LocalDateTime[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(childNode.get(i).asLong()), ZoneId.systemDefault());
        }
        return arr;
    }

    public static double[] doubleArray(JsonNode node, Object... paths) {
        ArrayNode childNode = array(node, paths);
        double[] arr = new double[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            arr[i] = null == childNode.get(i) ? null : childNode.get(i).asDouble();
        }
        return arr;
    }

    public static Map<String, Object>[] newObjectArray(JsonNode node, Object... paths) {
        return newObjectArray(node, paths, (List<String>) null);
    }

    public static Map<String, Object>[] newObjectArray4Keys(JsonNode node, List<String> keys) {
        return newObjectArray(node, null, keys);
    }

    public static Map<String, Object>[] newObjectArray4Keys(JsonNode node, String... keys) {
        return newObjectArray(node, null, keys);
    }

    public static Map<String, Object>[] newObjectArray(JsonNode node, Object[] paths, String... keys) {
        return newObjectArray(node, paths, Arrays.asList(keys));
    }

    public static Map<String, Object>[] newObjectArray(JsonNode node, Object[] paths, List<String> keys) {
        JsonNode childNode = node;
        if (null != paths) {
            childNode = object(node, paths);
        }

        Map<String, Object>[] arr = new Map[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            JsonNode item = childNode.get(i);
            if (null == item) {
                continue;
            }

            Map<String, Object> map = new LinkedHashMap<>();
            if (null == keys || keys.size() == 0) {
                keys = null == keys ? new ArrayList<>() : keys;
                Iterator<String> fieldNames = item.fieldNames();
                while (fieldNames.hasNext()) {
                    keys.add(fieldNames.next());
                }
            }

            for (String key : keys) {
                map.put(key, object2(item, key));
            }

            arr[i] = map;
        }
        return arr;
    }

    public static String[] mapOneArray(JsonNode node, String key, String... paths) {
        ArrayNode childNode = array(node, paths);
        String[] arr = new String[childNode.size()];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            JsonNode item = childNode.get(i);
            if (null == item) {
                continue;
            }

            arr[i] = text(item, key);
        }
        return arr;
    }

    /**
     * Deprecated，推荐使用 newObjectArray
     *
     * @param node
     * @param keys
     * @param paths
     * @return
     */
    @Deprecated
    public static Map<String, String>[] objectArray2Map(JsonNode node, String[] keys, String... paths) {
        return objectArray2Map(node, Arrays.asList(keys), paths);
    }

    /**
     * Deprecated，推荐使用 newObjectArray
     *
     * @param node
     * @param keys
     * @param paths
     * @return
     */
    @Deprecated
    public static Map<String, String>[] objectArray2Map(JsonNode node, List<String> keys, String... paths) {
        Map<String, Object>[] originData = newObjectArray(node, paths, keys);
        Map<String, String>[] data = new Map[originData.length];
        for (int i = 0, len = originData.length; i < len; i++) {
            Map<String, Object> originItem = originData[i];
            Map<String, String> item = new LinkedHashMap<>();
            for (Map.Entry<String, Object> originEntry : originItem.entrySet()) {
                item.put(originEntry.getKey(), String.valueOf(originEntry.getValue()));
            }
            data[i] = item;
        }
        return data;
    }

    /**
     * Deprecated，推荐使用 newObjectArray
     *
     * @param node
     * @param paths
     * @return
     */
    @Deprecated
    public static Map<String, String>[] objectArray2Map(JsonNode node, String... paths) {
        return objectArray2Map(node, (List<String>) null, paths);
    }

    public static Object[][] newArrayArray(JsonNode node, String... paths) {
        JsonNode childNode = node;
        if (null != paths) {
            childNode = object(node, paths);
        }

        Object[][] arr = new Object[childNode.size()][];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            JsonNode item = childNode.get(i);
            if (null == item) {
                continue;
            }

            ArrayNode arrayNode = array(item);
            Object[] objArr = new Object[arrayNode.size()];
            for (int j = 0, arrLen = arrayNode.size(); j < arrLen; j++) {
                objArr[j] = object2(arrayNode.get(j));
            }

            arr[i] = objArr;
        }
        return arr;
    }

    public static Map<String, Object>[] arrayArray2Map(JsonNode node, String... keys) {
        return arrayArray2Map(node, null, keys);
    }

    public static Map<String, Object>[] arrayArray2Map(JsonNode node, String[] paths, String... keys) {
        Object[][] originData = newArrayArray(node, paths);
        Map<String, Object>[] data = new Map[originData.length];
        for (int i = 0, len = originData.length; i < len; i++) {
            Object[] originItem = originData[i];
            if (null == originItem) {
                data[i] = null;
                continue;
            }

            Map<String, Object> item = new LinkedHashMap<>();
            int indexCursor = 1;
            int keySize = null == keys ? 0 : keys.length;
            for (int j = 0, itemLen = originItem.length; j < itemLen; j++) {
                Object value = originItem[j];
                String key;
                if (j < keySize) {
                    key = keys[j];
                } else {
                    key = String.format("_%d", indexCursor);
                    indexCursor++;
                }
                item.put(key, value);
            }
            data[i] = item;
        }
        return data;
    }

    public static String[][] arrayArray(JsonNode node, String... paths) {
        JsonNode childNode = node;
        if (null != paths) {
            childNode = object(node, paths);
        }

        String[][] arr = new String[childNode.size()][];
        for (int i = 0, len = childNode.size(); i < len; i++) {
            JsonNode item = childNode.get(i);
            if (null == item) {
                continue;
            }

            arr[i] = stringArray(item);
        }
        return arr;
    }

    public static String[] objectOneArray(JsonNode node, String key, String... paths) {
        return mapOneArray(node, key, paths);
    }

    public static String[] objectKeys(JsonNode node, String... paths) {
        JsonNode childNode = node;
        if (null != paths) {
            childNode = object(node, paths);
        }

        List<String> keys = new ArrayList<>();
        Iterator<String> fieldNames = childNode.fieldNames();
        while (fieldNames.hasNext()) {
            keys.add(fieldNames.next());
        }
        return keys.toArray(new String[keys.size()]);
    }

    public static String[] objectArrayKeys(JsonNode node, String... childPaths) {
        return objectArrayKeys(node, null, childPaths);
    }

    public static String[] objectArrayKeys(JsonNode node, String[] paths, String[] childPaths) {
        JsonNode childNode = node;
        if (null != paths) {
            childNode = object(node, paths);
        }

        // 保证列的顺序
        Set<String> columnNames = new LinkedHashSet<>();
//        if (childNode.isArray()) {
        for (JsonNode item : childNode) {
            JsonNode childItem = item;
            if (null != childPaths) {
                childItem = object(item, childPaths);
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
            childNode = object(node, paths);
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
                childItem = object(item, childPaths);
            }
            String[] dataRow = new String[columnLength];
            for (int i = 0; i < columnLength; i++) {
                String columnName = columnNames[i];
                JsonNode dataCell = object(childItem, columnName);
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
            childNode = object(node, paths);
        }

        List<String[]> table = new LinkedList<>();
        table.add(columnNames);
        int columnLength = columnNames.length;
        for (JsonNode item : childNode) {
            JsonNode childItem = item;
            if (null != childPaths) {
                childItem = object(item, childPaths);
            }
            String[] dataRow = new String[columnLength];
            for (int i = 0; i < columnLength; i++) {
                String columnName = columnNames[i];
                JsonNode dataCell = object(childItem, columnName);
                dataRow[i] = text(dataCell);
            }
            table.add(dataRow);
        }
        return table;
    }

    public static List<String[]> arrayArray2(JsonNode node, String... paths) {
        return Arrays.asList(arrayArray(node, paths));
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

    //=============================【实验性】JsonPath支持，若未引入jsonpath依赖包，无法使用如下方法===========================
    static {
        Configuration.setDefaults(new Configuration.Defaults() {
            private final JsonProvider jsonProvider = new JacksonJsonNodeJsonProvider(mapper);
            private final MappingProvider mappingProvider = new JacksonMappingProvider(mapper);

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

    public static JsonNode jsonpath(JsonNode node, String path) {
        return JsonPath.read(node, path);
    }
    //=============================【实验性】JsonPath支持===================================================================

    public static void main(String[] args) {
        Object[] objs = new Object[]{"Hello", "en", "zh-cn", true};
        Object[] objs2 = new Object[]{objs, new Object[]{null}};
        Object[] objs3 = new Object[]{"MkEWBc", JsonUtils.asString(objs2), null, "generic"};
        Object[] objs4 = new Object[]{objs3};
        Object[] objs5 = new Object[]{objs4};

        String str = "{\"cmdID\":0,\"productID\":\"2181457504\",\"offset\":42399409,\"payload\":\"{\\\"realLoadingWeight\\\":0.77,\\\"GPSCell\\\":\\\"0\\\",\\\"ShenBiSuoFaSignal\\\":0,\\\"FuJuanYangXiaLuoDianCiFa\\\":0,\\\"callType\\\":\\\"00\\\",\\\"currentWaterTemp\\\":79,\\\"ShenBiQieHuanFa\\\":0,\\\"password\\\":\\\"Qh\\\",\\\"pedalPosition\\\":0.0,\\\"enterAreaAlarmFunc\\\":\\\"0\\\",\\\"leaveAreaAlarmFunc\\\":\\\"0\\\",\\\"Distance\\\":380222,\\\"lng\\\":46.756175,\\\"BianFuYouGangDaQiangYaLi\\\":0,\\\"GaoDuXianWeiKaiGuanStatus\\\":\\\"0000\\\",\\\"encryptLat\\\":24.86716,\\\"gpsInfoNumber\\\":1,\\\"XianDaoFa\\\":0,\\\"interAreaAlarm\\\":\\\"0\\\",\\\"locationStatusOriginal\\\":\\\"A1\\\",\\\"mainLossOfElectric\\\":\\\"0\\\",\\\"standbyLossOfElectricFunc\\\":\\\"0\\\",\\\"ZhuJuanYangXiaLuoDianCiFa\\\":0,\\\"onOrOffStatusResponseFunc\\\":\\\"1\\\",\\\"forceSwitch\\\":0,\\\"mainBreakElectric\\\":\\\"0\\\",\\\"lockReason4\\\":\\\"0\\\",\\\"lockReason3\\\":\\\"0\\\",\\\"cmdReceiveTime\\\":\\\"2022-11-22 20:34:34\\\",\\\"terminalId\\\":\\\"2181457504\\\",\\\"currentOilConsumption\\\":0,\\\"deadZoneCompensation\\\":\\\"1\\\",\\\"lockReason2\\\":\\\"0\\\",\\\"gprsManufacturerCode\\\":\\\"68\\\",\\\"lockReason1\\\":\\\"0\\\",\\\"ErJieBiChang\\\":0,\\\"amplitude\\\":16.19,\\\"encryptLng\\\":46.756175,\\\"ZiYouHuaZhuanFa\\\":0,\\\"mainBreakElectricFunc\\\":\\\"1\\\",\\\"angle\\\":55.88,\\\"commandSequenceID\\\":\\\"E140\\\",\\\"originalGpsTime\\\":\\\"2022-11-22 12:34:33\\\",\\\"returnInfoId\\\":\\\"XXX8\\\",\\\"forceLimitFaultCode\\\":\\\"0000\\\",\\\"longitudeMarkOriginal\\\":\\\"A6\\\",\\\"torquePercentage\\\":25,\\\"overSpeedAlarm\\\":\\\"0\\\",\\\"protocolType\\\":\\\"gprsMediumSmallGps\\\",\\\"PositioningState\\\":1,\\\"vehicle_acc\\\":\\\"0\\\",\\\"leaveAreaAlarm\\\":\\\"0\\\",\\\"overSpeedAlarmFunc\\\":\\\"0\\\",\\\"bindingStatus\\\":\\\"0\\\",\\\"HuiZhuanZhiDongFa\\\":0,\\\"ratio\\\":4,\\\"FuJuanYangQiShengDianCiFa\\\":0,\\\"latitudeMarkOriginal\\\":\\\"A5\\\",\\\"ZhuSanQuanBaoHuQi\\\":0,\\\"onStatusResponse\\\":\\\"1\\\",\\\"totalMileage\\\":380222,\\\"emergencyAlarm\\\":\\\"0\\\",\\\"BianFuLuoDianCiFa\\\":0,\\\"parseTime\\\":\\\"2022-11-22 22:46:26\\\",\\\"lat\\\":24.86716,\\\"controllerFaultCode\\\":\\\"00\\\",\\\"offset\\\":42399409,\\\"dataType\\\":0,\\\"responseCommandID\\\":\\\"05\\\",\\\"workingCondition\\\":\\\"0001\\\",\\\"gpsTime\\\":\\\"2022-11-22 20:34:33\\\",\\\"terminalType\\\":\\\"\\\",\\\"YunXingShiJian\\\":2105,\\\"shortMessageManufacturerCode\\\":\\\"28\\\",\\\"FuSanQuanBaoHuQi\\\":0,\\\"gpsTerminalSn\\\":2181457504,\\\"lockVehicleStatus\\\":\\\"0\\\",\\\"oilPressure\\\":292,\\\"ZhuJuanYangQiShengDianCiFa\\\":0,\\\"speed\\\":0.5,\\\"ShenSuoFuJuanTaBanSignal\\\":0,\\\"locationStatus\\\":1,\\\"bindingSuccessStatus\\\":\\\"0\\\",\\\"latitudeMark\\\":\\\"N\\\",\\\"longitudeMark\\\":\\\"E\\\",\\\"ShenSuoFuJuanQieHuanKaiGuanSignal\\\":0,\\\"mainLossOfElectricFunc\\\":\\\"1\\\",\\\"rotateSpeed\\\":873,\\\"vehicleId\\\":\\\"1495240153358848002\\\",\\\"direction\\\":198,\\\"ShenBiShenFaSignal\\\":0,\\\"forceCall\\\":\\\"0\\\",\\\"dataFormat\\\":\\\"B\\\",\\\"length\\\":32.19,\\\"deviceCode\\\":\\\"501X\\\",\\\"maxLoadingWeight\\\":14.75,\\\"inputSignalAlarmFunc\\\":\\\"0\\\",\\\"engineWorkStatus\\\":\\\"1\\\"}\",\"vehiclePrefix\":22188,\"datePartition\":\"20221122\",\"time\":9223372035185655334,\"vehicle_id\":\"1495240153358848002\",\"productPrefix\":10971}";
        JsonNode json = json(str);
        long offset = aLong(jsonpath(json, "$.offset"));
        System.out.println(offset);
    }
}
