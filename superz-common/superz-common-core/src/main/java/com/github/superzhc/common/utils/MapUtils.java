package com.github.superzhc.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * 2020年11月23日 superz add
 */
public class MapUtils {
    public static <T> T mapToBean(Map<String, ?> map, Class<T> beanClass) {
        if (null == map)
            return null;

        try {
            boolean emptyConstructor = false;
            Constructor[] constructors = beanClass.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    emptyConstructor = true;
                    break;
                }
            }
            if (!emptyConstructor)
                throw new RuntimeException("无空构造函数，Map无法转" + beanClass.getName());

            T obj = beanClass.newInstance();

            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                // 静态变量不做处理，一般Bean中不存在静态变量
                if (Modifier.isStatic(mod))
                    continue;

                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> beanToMap(Object obj) {
        if (null == obj) return null;

        Map<String, Object> map = new HashMap<>();

        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                // 静态变量不做处理，一般Bean中不存在静态变量
                if (Modifier.isStatic(mod))
                    continue;

                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }

    public static Map<String, Object> caseInsensitiveMap(Map<String, Object> map) {
        if (null == map || map.size() == 0) {
            return null;
        }

        // 忽略大小写
//        Map<String, Object> newMap = new HashMap<>(map.size());
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            newMap.put(key.toLowerCase(), value);
//        }

        Map<String, Object> newMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        newMap.putAll(map);
        return newMap;
    }

    public static <T> String[] keys(Map<String, T>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }

        return keys(Arrays.asList(maps));
    }

    public static <T> T getIgnoreCase(Map<String, T> map, String key) {
        return get(map, key, true);
    }

    public static <T> T get(Map<String, T> map, String key, boolean ignoreCase) {
        if (null == map) {
            return null;
        }

        if (!ignoreCase) {
            return map.get(key);
        } else {
            for (String k : map.keySet()) {
                if (key.equalsIgnoreCase(k)) {
                    return map.get(key);
                }
            }

            return null;
        }
    }

    public static <T> String[] keys(List<Map<String, T>> maps) {
        Set<String> set = new HashSet<>();
        for (Map<String, T> map : maps) {
            set.addAll(map.keySet());
        }

        return set.toArray(new String[set.size()]);
    }

    public static Map<String, Class> types(List<Map<String, Object>> maps) {
        //不添加枚举了
        Map<Class, Integer> typesOrder = new HashMap<>();
        typesOrder.put(null, 0);
        typesOrder.put(Byte.class, 1);
        typesOrder.put(Short.class, 2);
        typesOrder.put(Boolean.class, 3);
        typesOrder.put(Integer.class, 4);
        typesOrder.put(BigInteger.class, 5);
        typesOrder.put(Long.class, 5);
        typesOrder.put(Float.class, 6);
        typesOrder.put(Double.class, 7);
        typesOrder.put(BigDecimal.class, 8);
        typesOrder.put(String.class, 10);


        Map<String, Class> valueTypes = new LinkedHashMap<>();
        for (Map<String, Object> map : maps) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Class valueClass = null == value ? null : TypeUtils.removePrimitive(value);
                if (!valueTypes.containsKey(key)) {
                    valueTypes.put(key, typesOrder.containsKey(valueClass) ? valueClass : String.class);
                } else {
                    Class oldClass = valueTypes.get(key);
                    Class newClass = typesOrder.containsKey(valueClass) ? valueClass : String.class;
                    valueTypes.put(key, typesOrder.get(newClass) > typesOrder.get(oldClass) ? newClass : oldClass);
                }
            }
        }

        return valueTypes;
    }

    public static <T> List<List<T>> values(List<Map<String, T>> maps, String... keys) {
        if (null == maps) {
            return null;
        }

        if (null == keys || keys.length == 0) {
            return null;
        }

        List<List<T>> data = new ArrayList<>(maps.size());
        for (Map<String, T> map : maps) {
            List<T> item = new ArrayList<>();
            for (String key : keys) {
                item.add(map.get(key));
            }
            data.add(item);
        }
        return data;
    }

    public static <K, V> Map<K, V> removeKeys(final Map<K, V> map, K... keys) {
        if (null == keys || keys.length == 0) {
            return map;
        }

        for (K key : keys) {
            map.remove(key);
        }
        return map;
    }

    public static <T> Map<String, T> replaceKey(Map<String, T> map, String key, String newKey) {
        map.put(newKey, map.get(key));
        map.remove(key);
        return map;
    }

    public static <K, V> Map<K, V>[] constant(K k, V v, Map<K, V>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }

        for (Map<K, V> map : maps) {
            map.put(k, v);
        }

        return maps;
    }

    public static <K, V> List<Map<K, V>> constant(K k, V v, List<Map<K, V>> maps) {
        if (null == maps || maps.size() == 0) {
            return null;
        }

        for (Map<K, V> map : maps) {
            map.put(k, v);
        }

        return maps;
    }

    public static <T> String html(Map<String, T>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }

        List<Map<String, T>> lst = new ArrayList<>();
        for (Map<String, T> map : maps) {
            lst.add(map);
        }
        return html(lst);
    }

    public static <T> String html(List<Map<String, T>> maps) {
        if (null == maps || maps.size() == 0) {
            return "<p>暂无数据</p>";
        }

        // 获取所有的key
        Set<String> keys = new LinkedHashSet<>();
        for (Map<String, ?> map : maps) {
            keys.addAll(map.keySet());
        }

        StringBuilder sb = new StringBuilder("<table>");
        String rowTemplate = "<tr>%s</tr>";

        StringBuilder header = new StringBuilder();
        for (String key : keys) {
            header.append("<th>").append(key).append("</th>");
        }
        sb.append(String.format(rowTemplate, header));

        for (Map<String, T> map : maps) {
            StringBuilder row = new StringBuilder();
            for (String key : keys) {
                String value = (null == map || !map.containsKey(key) || null == map.get(key)) ? null : String.valueOf(map.get(key)).trim();
                if (null != value) {
                    if (value.startsWith("http://") || value.startsWith("https://")) {
                        value = String.format("<a href='%s'>链接</a>", value);
                    }
                }
                row.append("<td>").append(value).append("</td>");
            }
            sb.append(String.format(rowTemplate, row));
        }

        sb.append("</table>");
        return sb.toString();
    }

    public static <T> void show(Map<String, T>... maps) {
        System.out.println(print(maps));
    }

    public static <T> void show(List<Map<String, T>> maps) {
        System.out.println(print(maps));
    }

    public static <T> void show(List<Map<String, T>> maps, int num) {
        System.out.println(print(maps, num));
    }

    public static <T> String print(Map<String, T>... maps) {
        if (null == maps || maps.length == 0) {
            return null;
        }

        List<Map<String, T>> lst = new ArrayList<>();
        for (Map<String, T> map : maps) {
            lst.add(map);
        }
        return print(lst);
    }

    /**
     * 打印所有数据
     *
     * @param maps
     * @param <T>
     * @return
     */
    public static <T> String print(List<Map<String, T>> maps) {
        return print(maps, maps.size());
    }

    /**
     * 打印指定条数的数据
     *
     * @param maps
     * @param num
     * @param <T>
     * @return
     */
    public static <T> String print(List<Map<String, T>> maps, int num) {
        if (null == maps) {
            return "暂无数据";
        }

        // 获取所有的key
        Set<String> keys = new LinkedHashSet<>();
        for (Map<String, ?> map : maps) {
            keys.addAll(map.keySet());
        }

        int[] columnMaxLengths = new int[keys.size()];
        String[] headerRow = new String[keys.size()];
        int i = 0;
        for (String key : keys) {
            columnMaxLengths[i] = stringLength(key);
            headerRow[i] = key;
            i++;
        }

        List<String[]> rows = new ArrayList<>();
        for (int k = 0, mapsLength = maps.size(); k < mapsLength; k++) {
            if (k >= num) {
                break;
            }

            Map<String, ?> map = maps.get(k);
            String[] row = new String[keys.size()];
            int j = 0;
            for (String key : keys) {
                String value = (null == map || !map.containsKey(key) || null == map.get(key)) ? null : String.valueOf(map.get(key));
                row[j] = value;
                columnMaxLengths[j] = Math.max(columnMaxLengths[j], (null == value ? 0 : stringLength(value)));
                j++;
            }
            rows.add(row);
        }

        StringBuilder result = new StringBuilder();
        result.append(printSeparator(columnMaxLengths)).append("\n");
        result.append(printRow(headerRow, columnMaxLengths)).append("\n");
        result.append(printSeparator(columnMaxLengths)).append("\n");
        for (String[] row : rows) {
            result.append(printRow(row, columnMaxLengths)).append("\n");
        }
        result.append(printSeparator(columnMaxLengths)).append("\n");

        result.append("展示数据条数：" + num + "，数据总数：").append(maps.size()).append("\n");

        return result.toString();
    }

    private static String printRow(String[] row, int[] columnMaxLengths) {
        StringBuilder sb = new StringBuilder();
        int columnCount = row.length;
        for (int i = 0; i < columnCount; i++) {
            sb.append("|");
            sb.append(rightPad(row[i], columnMaxLengths[i]));
        }
        sb.append("|");
        return sb.toString();
    }

    private static String printSeparator(int[] columnMaxLengths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnMaxLengths.length; i++) {
            sb.append("+");
            for (int j = 0; j < columnMaxLengths[i] + 1; j++) {
                sb.append("-");
            }
        }
        sb.append("+");
        return sb.toString();
    }

    private static String rightPad(String str, int maxLength) {
        int len = stringLength(str);
        StringBuilder sb = new StringBuilder(null == str ? "" : str);
        for (int i = 0; i < ((maxLength - len) + 1); i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static int stringLength(String str) {
        if (null == str) {
            return 0;
        }

        // String chinese = "[\u0391-\uFFE5]";//匹配中文字符的正则表达式： [\u4e00-\u9fa5]
        // String doubleChar = "[^\\x00-\\xff]";// 匹配双字节字符(包括汉字在内)：[^\x00-\xff]
        String doubleChar = "[" +
                "\u1100-\u115F" +
                "\u2E80-\uA4CF" +
                "\uAC00-\uD7A3" +
                "\uF900-\uFAFF" +
                "\uFE10-\uFE19" +
                "\uFE30-\uFE6F" +
                "\uFF00-\uFF60" +
                "\uFFE0-\uFFE6" +
                "]";

        int valueLength = 0;
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < str.length(); i++) {
            /* 获取一个字符 */
            String temp = str.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(doubleChar)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
