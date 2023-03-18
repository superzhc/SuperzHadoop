package com.github.superzhc.common.dom4j;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author superz
 * @create 2023/3/17 14:30
 **/
public class XmlUtils {
    private static final Logger LOG = LoggerFactory.getLogger(XmlUtils.class);

    private XmlUtils() {
    }

    public static Element load(String content) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new StringReader(content));
            return doc.getRootElement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Element load(InputStream in) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            return doc.getRootElement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Element element(Element element, Object... tags) {
        List<Element> elements = elements(element, tags);
        return (null == elements || elements.size() == 0) ? null : elements.get(0);
    }

    public static List<Element> elements(Element element, Object... tags) {
        if (null == element) {
            return null;
        }

        List<Element> childElements = Arrays.asList(element);

        for (Object tag : tags) {
            if (null == tag) {
                continue;
            }

            if (tag.getClass() == String.class) {
                childElements = childElements.get(0).elements((String) tag);
            } else if (tag.getClass() == Integer.TYPE || tag.getClass() == Integer.class) {
                int index = (int) tag;
                if (index >= childElements.size()) {
                    throw new RuntimeException("Index 超过节点数");
                }

                Element innerElement = childElements.get(index);
                if (null == innerElement) {
                    return null;
                }

                childElements = Arrays.asList(innerElement);
            } else {
                throw new RuntimeException("XML 子节点的获取仅支持字符串字段和整型index序号");
            }
        }

        return childElements;
    }

    public static Map<String, Object> map(Element element, Object... tags) {
        Element childElement = element(element, tags);
        if (null == childElement) {
            return null;
        }

        // TODO:属性是否考虑？？？

        Object value = object(childElement);
        if (null == value) {
            return null;
        }

        if (value.getClass() == String.class) {
            throw new RuntimeException("当前节点【" + childElement.getName() + "】无任何子节点，无法转换成 Map");
        }

        return (Map<String, Object>) value;
    }

    public static Map<String, Object>[] maps(Element element, Object... tags) {
        List<Element> childElements = elements(element, tags);
        if (null == childElements || childElements.size() == 0) {
            return null;
        }

        Map<String, Object>[] array = new Map[childElements.size()];
        for (int i = 0, len = childElements.size(); i < len; i++) {
            Element childElement = childElements.get(i);
            Object value = object(childElement);
            if (null == value) {
                array[i] = null;
            } else if (value.getClass() == String.class) {
                array[i] = null;
                LOG.debug("当前节点【{}】无任何子节点，无法转换成 Map，置为 null", childElement.getName());
            } else {
                array[i] = (Map<String, Object>) value;
            }
        }
        return array;
    }

    public static Object object(Element element, Object... tags) {
        Element childElement = element(element, tags);
        if (null == childElement) {
            return null;
        }

        List<Element> subChildElements = childElement.elements();
        if (null == subChildElements || subChildElements.size() == 0) {
            return childElement.getTextTrim();
        }

        Map<String, List<Object>> map = new LinkedHashMap<>();
        for (Element subChildElement : subChildElements) {
            String name = subChildElement.getName();

            List<Object> data;
            if (!map.containsKey(name)) {
                data = new ArrayList<>();
                map.put(name, data);
            } else {
                data = map.get(name);
            }

            data.add(object(subChildElement));
        }

        // map 中的值优化
        Map<String, Object> finalMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
            List<Object> data = entry.getValue();
            if (null == data || data.size() == 0) {
                finalMap.put(entry.getKey(), null);
            } else if (data.size() == 1) {
                finalMap.put(entry.getKey(), data.get(0));
            } else {
                finalMap.put(entry.getKey(), data);
            }
        }
        return finalMap;
    }

    public static String text(Element element, Object... tags) {
        Element innerElement = element(element, tags);
        return null == innerElement ? null : innerElement.getTextTrim();
    }

    public static Integer integer(Element element, Object... tags) {
        String text = text(element, tags);
        return (null == text || text.trim().length() == 0) ? null : Integer.valueOf(text);
    }

    public static Long aLong(Element element, Object... tags) {
        String text = text(element, tags);
        return (null == text || text.trim().length() == 0) ? null : Long.valueOf(text);
    }

    public static Float aFloat(Element element, Object... tags) {
        String text = text(element, tags);
        return (null == text || text.trim().length() == 0) ? null : Float.valueOf(text);
    }

    public static Double aDouble(Element element, Object... tags) {
        String text = text(element, tags);
        return (null == text || text.trim().length() == 0) ? null : Double.valueOf(text);
    }

    public static Boolean bool(Element element, Object... tags) {
        String text = text(element, tags);
        return (null == text || text.trim().length() == 0) ? null : Boolean.valueOf(text);
    }

    public static Date date(Element element, String pattern, Object... tags) {
        return date(element, new SimpleDateFormat(pattern), tags);
    }

    public static Date date(Element element, SimpleDateFormat format, Object... tags) {
        String text = text(element, tags);
        if (null == text || text.trim().length() == 0) {
            return null;
        }

        try {
            return format.parse(text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static LocalDateTime localDateTime(Element element, String pattern, Object... tags) {
        return localDateTime(element, DateTimeFormatter.ofPattern(pattern), tags);
    }

    public static LocalDateTime localDateTime(Element element, DateTimeFormatter format, Object... tags) {
        String text = text(element, tags);
        if (null == text || text.trim().length() == 0) {
            return null;
        }

        return LocalDateTime.parse(text, format);
    }

    public static String[] texts(Element element, Object... tags) {
        List<Element> childElements = elements(element, tags);
        if (null == childElements || childElements.size() == 0) {
            return null;
        }

        String[] arr = new String[childElements.size()];
        for (int i = 0, len = childElements.size(); i < len; i++) {
            arr[i] = childElements.get(i).getTextTrim();
        }
        return arr;
    }

    public static Integer[] integers(Element element, Object... tags) {
        String[] array = texts(element, tags);
        Integer[] resultArray = new Integer[array.length];
        for (int i = 0, len = array.length; i < len; i++) {
            resultArray[i] = (null == array[i] || array[i].trim().length() == 0) ? null : Integer.valueOf(array[i]);
        }
        return resultArray;
    }

    public static Long[] longs(Element element, Object... tags) {
        String[] array = texts(element, tags);
        Long[] resultArray = new Long[array.length];
        for (int i = 0, len = array.length; i < len; i++) {
            resultArray[i] = (null == array[i] || array[i].trim().length() == 0) ? null : Long.valueOf(array[i]);
        }
        return resultArray;
    }

    public static Float[] floats(Element element, Object... tags) {
        String[] array = texts(element, tags);
        Float[] resultArray = new Float[array.length];
        for (int i = 0, len = array.length; i < len; i++) {
            resultArray[i] = (null == array[i] || array[i].trim().length() == 0) ? null : Float.valueOf(array[i]);
        }
        return resultArray;
    }

    public static Double[] doubles(Element element, Object... tags) {
        String[] array = texts(element, tags);
        Double[] resultArray = new Double[array.length];
        for (int i = 0, len = array.length; i < len; i++) {
            resultArray[i] = (null == array[i] || array[i].trim().length() == 0) ? null : Double.valueOf(array[i]);
        }
        return resultArray;
    }

    public static Boolean[] bools(Element element, Object... tags) {
        String[] array = texts(element, tags);
        Boolean[] resultArray = new Boolean[array.length];
        for (int i = 0, len = array.length; i < len; i++) {
            resultArray[i] = (null == array[i] || array[i].trim().length() == 0) ? null : Boolean.valueOf(array[i]);
        }
        return resultArray;
    }

    public static Date[] GMTDates(Element element, Object... tags) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        return dates(element, format, tags);
    }

    public static Date[] dates(Element element, String pattern, Object... tags) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return dates(element, format, tags);
    }

    public static Date[] dates(Element element, SimpleDateFormat format, Object... tags) {
        String[] array = texts(element, tags);
        Date[] resultArray = new Date[array.length];
        try {
            for (int i = 0, len = array.length; i < len; i++) {
                if (null == array[i] || array[i].trim().length() == 0) {
                    resultArray[i] = null;
                } else {
                    resultArray[i] = format.parse(array[i]);
                }
            }
            return resultArray;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDateTime[] localDateTimes(Element element, String pattern, Object... tags) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return localDateTimes(element, format, tags);
    }

    public static LocalDateTime[] localDateTimes(Element element, DateTimeFormatter format, Object... tags) {
        String[] array = texts(element, tags);
        LocalDateTime[] resultArray = new LocalDateTime[array.length];
        try {
            for (int i = 0, len = array.length; i < len; i++) {
                if (null == array[i] || array[i].trim().length() == 0) {
                    resultArray[i] = null;
                } else {
                    resultArray[i] = LocalDateTime.parse(array[i], format)
                    ;
                }
            }
            return resultArray;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class XPath {
        private XPath() {
        }

        public static Node node(Element element, String xpath) {
            return element.selectSingleNode(xpath);
        }

        public static List<Node> nodes(Element element, String xpath) {
            return element.selectNodes(xpath);
        }
    }
}
