package com.github.superzhc.db.influxdb;

import com.github.superzhc.common.utils.TypeUtils;

import java.time.*;
import java.util.List;
import java.util.Map;

/**
 * 行协议
 * InfluxDB的行协议是一种写入数据点到InfluxDB的文本格式。必须要是这样的格式的数据点才能被Influxdb解析和写入成功，当然除非你使用一些其他服务插件。
 * <p>
 * 语法：
 * 一行Line Protocol表示InfluxDB中的一个数据点。它向InfluxDB通知点的measurement，tag set，field set和timestamp。以下代码块显示了行协议的示例，并将其分解为其各个组件：
 * weather,location=us-midwest temperature=82 1465839830100400200
 * |    -------------------- --------------  |
 * |             |             |             |
 * |             |             |             |
 * +-----------+--------+-+---------+-+---------+
 * |measurement|,tag_set| |field_set| |timestamp|
 * +-----------+--------+-+---------+-+---------+
 * <p>
 * - measurement【必填项】，示例的measurement是weater
 * - Tag set【可选项】，注意measurement和tag set是用不带空格的逗号分开的。
 * 用不带空格的=来分割一组tag的键值：<tag_key>=<tag_value>
 * 多组tag直接用不带空格的逗号分开：<tag_key>=<tag_value>,<tag_key>=<tag_value>
 * - Field set【必填项】，每个数据点在行协议中至少需要一个field。
 * 使用无空格的=分隔field的键值对：<field_key>=<field_value>
 * 多组field直接用不带空格的逗号分开：<field_key>=<field_value>,<field_key>=<field_value>
 * - Timestamp【可选项】数据点的时间戳记以纳秒精度Unix时间。行协议中的时间戳是可选的。 如果没有为数据点指定时间戳，InfluxDB会使用服务器的本地纳秒时间戳。
 * - 空格【必填项】，主要在一下两处
 * 分离measurement和field set，或者如果使用数据点包含tag set，则使用空格分隔tag set和field set。行协议中空格是必需的。
 * 使用空格分隔field set和可选的时间戳。如果你包含时间戳，则行协议中需要空格
 *
 * @author superz
 * @create 2023/2/23 10:15
 **/
public class LineProtocol {
    private String measurement;
    private Map<String, String> tagSet;
    private Map<String, Object> fieldSet;

    /**
     * 时间戳记以纳秒精度Unix时间。行协议中的时间戳是可选的。 如果没有为数据点指定时间戳，InfluxDB会使用服务器的本地纳秒时间戳。
     */
    private Long timestamp;

    public LineProtocol() {
    }

    public LineProtocol(String measurement, Map<String, String> tagSet, Map<String, Object> fieldSet, Long timestamp) {
        this.measurement = measurement;
        this.tagSet = tagSet;
        this.fieldSet = fieldSet;
        this.timestamp = timestamp;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public Map<String, String> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Map<String, String> tagSet) {
        this.tagSet = tagSet;
    }

    public Map<String, Object> getFieldSet() {
        return fieldSet;
    }

    public void setFieldSet(Map<String, Object> fieldSet) {
        this.fieldSet = fieldSet;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(LocalDateTime dateTime) {
        /*计算出纳米级时间戳*/
        ZonedDateTime dt = dateTime.atZone(ZoneOffset.systemDefault());
        Instant instant=dt.toInstant();
        long ns = instant.getEpochSecond() * 1000000000 + instant.getNano();
        setTimestamp(ns);
    }

    @Override
    public String toString() {
        StringBuilder lineProtocol = new StringBuilder();

        // measurement
        lineProtocol.append(escape(this.measurement));

        // tag set
        if (null != this.tagSet && this.tagSet.size() > 0) {
            for (Map.Entry<String, String> tag : this.tagSet.entrySet()) {
                lineProtocol.append(',').append(escape(tag.getKey())).append('=').append(escape(tag.getValue()));
            }
        }

        // blank，此处空格必存在
        lineProtocol.append(' ');

        // field set
        if (this.fieldSet == null || this.fieldSet.size() == 0) {
            throw new RuntimeException("At least one field!");
        }


        StringBuilder fieldsSb = new StringBuilder();
        /**
         * Field value可以是整数、浮点数、字符串和布尔值
         */
        for (Map.Entry<String, Object> field : this.fieldSet.entrySet()) {
            Object value = field.getValue();
            if (null == value) {
                continue;
            }

            /**
             * 1. field value不要单引号，即使是字符串类型
             * 2. measurement，tag keys，tag value和field key不用单双引号。InfluxDB会假定引号是名称的一部分。
             * 3. 当field value是整数，浮点数或是布尔型时，不要使用双引号，不然InfluxDB会假定值是字符串类型
             * 4. 当Field value是字符串时，使用双引号
             */
            if (TypeUtils.isInt(value)
                    || TypeUtils.isLong(value)
                    || TypeUtils.isFloat(value)
                    || TypeUtils.isDouble(value)
                    || TypeUtils.isBool(value)) {
                fieldsSb.append(',').append(escape(field.getKey())).append('=').append(value);
            } else {
                fieldsSb.append(',').append(escape(field.getKey())).append('=').append('"').append(escape(value.toString())).append('"');
            }
        }
        lineProtocol.append(fieldsSb.substring(1));


        if (null != this.timestamp) {
            // blank，此处空格在timestamp不存在的时候不需要填写
            lineProtocol.append(' ');

            // timestamp
            lineProtocol.append(this.timestamp);
        }

        return lineProtocol.toString();
    }

    /**
     * 特殊字符转义
     * 对于tag key，tag value和field key，始终使用反斜杠字符\来进行转义
     *
     * @param value
     * @return
     */
    public String escape(String value) {
        return value.replaceAll(",", "\\,")
                .replaceAll("=", "\\=")
                .replaceAll("\"", "\\\"")
                .replaceAll(" ", "\\ ")
                ;
    }

    public static String build(LineProtocol protocol) {
        return protocol.toString();
    }

    public static String build(String measurement, Map<String, Object> fieldSet) {
        return build(measurement, null, fieldSet, null);
    }

    public static String build(String measurement, Map<String, Object> fieldSet, Long timestamp) {
        return build(measurement, null, fieldSet, timestamp);
    }

    public static String build(String measurement, Map<String, String> tagSet, Map<String, Object> fieldSet) {
        return build(measurement, tagSet, fieldSet, null);
    }

    public static String build(String measurement, Map<String, String> tagSet, Map<String, Object> fieldSet, Long timestamp) {
        LineProtocol lineProtocol = new LineProtocol(measurement, tagSet, fieldSet, timestamp);
        return build(lineProtocol);
    }

    public static String buildBatch(List<LineProtocol> lineProtocols) {
        if (null == lineProtocols || lineProtocols.size() == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (LineProtocol lineProtocol : lineProtocols) {
            sb.append('\n').append(lineProtocol.toString());
        }
        return sb.substring(1);
    }
}
