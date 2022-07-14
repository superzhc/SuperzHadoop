package com.github.superzhc.common.format;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 占位符解析器
 * 2020年4月28日 superz 二次开发
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-13 1:42 PM
 */
public class PlaceholderResolver {
    /**
     * 默认前缀占位符
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * 默认后缀占位符
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * 默认单例解析器
     */
    private static PlaceholderResolver defaultResolver = new PlaceholderResolver();

    /**
     * 占位符前缀
     */
    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;

    /**
     * 占位符后缀
     */
    private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;

    private PlaceholderResolver() {
    }

    private PlaceholderResolver(String placeholderPrefix, String placeholderSuffix) {
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
    }

    /**
     * 获取默认的占位符解析器，即占位符前缀为"${", 后缀为"}"
     *
     * @return
     */
    public static PlaceholderResolver getDefaultResolver() {
        return defaultResolver;
    }

    public static PlaceholderResolver getResolver(String placeholderPrefix, String placeholderSuffix) {
        return new PlaceholderResolver(placeholderPrefix, placeholderSuffix);
    }

    /**
     * 解析带有指定占位符的模板字符串，默认占位符为前缀：${  后缀：}<br/><br/>
     * 如：template = category:${}:product:${}<br/>
     * values = {"1", "2"}<br/>
     * 返回 category:1:product:2<br/>
     *
     * @param content 要解析的带有占位符的模板字符串
     * @param values 按照模板占位符索引位置设置对应的值
     *
     * @return
     */
    public String resolve(String content, String... values) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        }
        // 值索引
        int valueIndex = 0;
        StringBuilder result = new StringBuilder(content);
        // 2021年8月17日 superz modify 填充的值用完了就不管后续的占位符了
        while (start != -1 && valueIndex < values.length) {
            int end = result.indexOf(this.placeholderSuffix);
            String replaceContent = values[valueIndex++];
            result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
            start = result.indexOf(this.placeholderPrefix, start + replaceContent.length());
        }
        return result.toString();
    }

    /**
     * 解析带有指定占位符的模板字符串，默认占位符为前缀：${  后缀：}<br/><br/>
     * 如：template = category:${}:product:${}<br/>
     * values = {"1", "2"}<br/>
     * 返回 category:1:product:2<br/>
     *
     * @param content 要解析的带有占位符的模板字符串
     * @param values 按照模板占位符索引位置设置对应的值
     *
     * @return
     */
    public String resolve(String content, Object[] values) {
        return resolve(content, Stream.of(values).map(String::valueOf).toArray(String[]::new));
    }

    /**
     * 2021年8月17日 superz add
     * 解析带有指定占位符的模板字符串，默认占位符为前缀：${  后缀：}<br/><br/>
     * 如：template = category:${0}:product:${1}:category:${0}<br/>
     * values = {"1", "2"}<br/>
     * 返回 category:1:product:2:category:1<br/>
     * 注意：索引从0开始
     *
     * @param content 要解析的带有占位符的模板字符串
     * @param values 按照模板占位符索引位置设置对应的值
     *
     * @return
     */
    public String resolveByIndex(String content, String... values) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        }

        int len = values.length;
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            int end = result.indexOf(this.placeholderSuffix, start);
            if (end == -1) {
                break;
            }
            // 获取占位符属性值，如${index}, 即获取index
            String placeholder = result.substring(start + this.placeholderPrefix.length(), end).trim();
            // 替换整个占位符内容，即将${index}值替换为替换规则回调中的内容
            if (placeholder.isEmpty() || Integer.parseInt(placeholder) >= len) {
                start = result.indexOf(this.placeholderPrefix, end + this.placeholderSuffix.length());
            } else {
                String replaceContent = values[Integer.parseInt(placeholder)];
                result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
                start = result.indexOf(this.placeholderPrefix, start + replaceContent.length());
            }
        }
        return result.toString();
    }

    /**
     * 根据替换规则来替换指定模板中的占位符值
     *
     * @param content 要解析的字符串
     * @param rule 解析规则回调
     *
     * @return
     */
    public String resolveByRule(String content, Function<String, String> rule) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        }
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            int end = result.indexOf(this.placeholderSuffix, start);
            // 获取占位符属性值，如${id}, 即获取id
            String placeholder = result.substring(start + this.placeholderPrefix.length(), end).trim();
            // 替换整个占位符内容，即将${id}值替换为替换规则回调中的内容
            String replaceContent;
            if (placeholder.isEmpty()) {
                replaceContent = "";
            } else {
                replaceContent = rule.apply(placeholder);
            }
            result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
            start = result.indexOf(this.placeholderPrefix, start + replaceContent.length());
        }
        return result.toString();
    }

    /**
     * 替换模板中占位符内容，占位符的内容即为map key对应的值，key为占位符中的内容。<br/><br/>
     * 如：content = product:${id}:detail:${did}<br/>
     * valueMap = id -> 1; pid -> 2<br/>
     * 经过解析返回 product:1:detail:2<br/>
     *
     * @param content 模板内容。
     * @param valueMap 值映射
     *
     * @return 替换完成后的字符串。
     */
    public String resolveByMap(String content, final Map<String, ?> valueMap) {
        if (null == valueMap || valueMap.size() == 0) {
            return content;
        }

        return resolveByRule(content, placeholderValue -> {
            /* 2021年8月17日 superz add 支持时间格式化，形如 date#YYYY-MM-dd HH:mm:ss*/
            if (!placeholderValue.startsWith("#") && placeholderValue.contains("#")) {
                String[] ss = placeholderValue.split("#");
                // 如果字段不存在，直接返回null
                if (!valueMap.containsKey(ss[0]) || null == valueMap.get(ss[0])) {
                    return (String) null;
                }
                Object originalValue = valueMap.get(ss[0]);
                if (originalValue instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat(ss[1]);
                    return sdf.format(originalValue);
                } else if (originalValue instanceof LocalDate) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ss[1]);
                    return ((LocalDate) originalValue).format(dtf);
                } else if (originalValue instanceof LocalTime) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ss[1]);
                    return ((LocalTime) originalValue).format(dtf);
                } else if (originalValue instanceof LocalDateTime) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ss[1]);
                    return ((LocalDateTime) originalValue).format(dtf);
                }
                /* 2021年8月23日 add 新增时间戳判定 */
                else if (originalValue instanceof String || originalValue instanceof Long || originalValue.getClass() == long.class) {
                    Long originalValue2;
                    if (originalValue instanceof String) {
                        originalValue2 = Long.parseLong(originalValue.toString());
                    } else {
                        originalValue2 = (Long) originalValue;
                    }
                    LocalDateTime ldt;
                    // 946656000000 毫秒时间戳对应的时间是 2000-01-01 00:00:00
                    // 4102415999 秒时间戳对应的时间是 2099-12-31 23:59:59
                    if (originalValue2 < 946656000000L && originalValue2 < 4102415999L) {
                        ldt = LocalDateTime.ofEpochSecond(originalValue2, 0, ZoneOffset.ofHours(8));
                    } else {
                        ldt = LocalDateTime.ofEpochSecond(originalValue2 / 1000, 0, ZoneOffset.ofHours(8));
                    }
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ss[1]);
                    return ldt.format(dtf);
                }
            }
            /* 2021年8月17日 superz add 支持 string.format，形如 id:%s 占位符的数据*/
            else if (!placeholderValue.startsWith(":") && placeholderValue.contains(":")) {
                String[] ss = placeholderValue.split(":");
                return String.format(ss[1], valueMap.get(ss[0]));
            }
            return String.valueOf(valueMap.get(placeholderValue));
        });
    }

    /**
     * 根据properties文件替换占位符内容
     *
     * @param content
     * @param properties
     *
     * @return
     */
    public String resolveByProperties(String content, final Properties properties) {
        return resolveByRule(content, placeholderValue -> properties.getProperty(placeholderValue));
    }

    /* sql语句自助使用引号来包裹，不使用这个玩意 */
    @Deprecated
    public String resolveSQLByMap(String content, final Map<String, Object> valueMap) {
        return resolveByRule(content, placeholderValue -> {
            Object originalValue = valueMap.get(placeholderValue);
            if (null == originalValue) {
                return null;
            } else if (originalValue.getClass() == int.class || originalValue.getClass() == Integer.class
                    || originalValue.getClass() == short.class || originalValue.getClass() == Short.class
                    || originalValue.getClass() == long.class || originalValue.getClass() == Long.class
                    || originalValue.getClass() == float.class || originalValue.getClass() == Float.class
                    || originalValue.getClass() == double.class || originalValue.getClass() == Double.class
            ) {
                return String.valueOf(originalValue);
            } else if (originalValue.getClass() == Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return String.format("'%s'", sdf.format(originalValue));
            } else if (originalValue.getClass() == LocalDate.class) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return String.format("'%s'", ((LocalDate) originalValue).format(dtf));
            } else if (originalValue.getClass() == LocalTime.class) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return String.format("'%s'", ((LocalTime) originalValue).format(dtf));
            } else if (originalValue.getClass() == LocalDateTime.class) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return String.format("'%s'", ((LocalDateTime) originalValue).format(dtf));
            } else {
                return String.format("'%s'", originalValue);
            }
        });
    }

    /**
     * 根据对象中字段路径(即类似js访问对象属性值)替换模板中的占位符 <br/><br/>
     * 如 content = product:${id}:detail:${detail.id} <br/>
     *    obj = Product.builder().id(1).detail(Detail.builder().id(2).build()).build(); <br/>
     *    经过解析返回 product:1:detail:2 <br/>
     *
     * @param content  要解析的内容
     * @param obj   填充解析内容的对象(如果是基本类型，则所有占位符替换为相同的值)
     * @return
     */
    // public String resolveByObject(String content, final Object obj) {
    // if (obj instanceof Map) {
    // return resolveByMap(content, (Map)obj);
    // }
    // return resolveByRule(content, placeholderValue ->
    // String.valueOf(ReflectionUtils.getValueByFieldPath(obj, placeholderValue)));
    // }
}
