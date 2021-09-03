package com.github.superzhc.geo.geomesa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2021/8/11 21:59
 */
public class QueryWrapper {
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String EQ = "=";
    private static final String NE = "!=";
    private static final String GT = ">";
    private static final String GE = ">=";
    private static final String LT = "<";
    private static final String LE = "<=";

    private StringBuilder query;
    private boolean hasCondition = false;
    private boolean isAnd = true;

    public QueryWrapper() {
        query = new StringBuilder();
    }

    public QueryWrapper eq(String field, Object value) {
        return compareCondition(field, EQ, value);
    }

    public QueryWrapper ne(String field, Object value) {
        return compareCondition(field, NE, value);
    }

    public QueryWrapper gt(String field, Object value) {
        return compareCondition(field, GT, value);
    }

    public QueryWrapper ge(String field, Object value) {
        return compareCondition(field, GE, value);
    }

    public QueryWrapper lt(String field, Object value) {
        return compareCondition(field, LT, value);
    }

    public QueryWrapper le(String field, Object value) {
        return compareCondition(field, LE, value);
    }

    private QueryWrapper compareCondition(String field, String operate, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append(operate).append(SPACE).append(dealValue(value));
        return express(sb.toString());
    }

    public QueryWrapper between(String field, Object value1, Object value2) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("BETWEEN").append(SPACE).append(dealValue(value1)).append(SPACE).append("AND").append(SPACE).append(dealValue(value2));
        return express(sb.toString());
    }

    public QueryWrapper notBetween(String field, Object value1, Object value2) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("NOT BETWEEN").append(SPACE).append(dealValue(value1)).append(SPACE).append("AND").append(SPACE).append(dealValue(value2));
        return express(sb.toString());
    }

    public QueryWrapper like(String field, String pattern) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("LIKE").append(SPACE).append("'%").append(pattern).append("%'");
        return express(sb.toString());
    }

    public QueryWrapper likeLeft(String field, String pattern) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("LIKE").append(SPACE).append("'%").append(pattern).append("'");
        return express(sb.toString());
    }

    public QueryWrapper likeRight(String field, String pattern) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("LIKE").append(SPACE).append("'").append(pattern).append("%'");
        return express(sb.toString());
    }

    public QueryWrapper notlike(String field, String pattern) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("NOT LIKE").append(SPACE).append("'%").append(pattern).append("%'");
        return express(sb.toString());
    }

    public QueryWrapper in(String field, Object... values) {
        if (null == values || values.length == 0) {
            return this;
        }

        StringBuilder inSB = new StringBuilder();
        for (Object value : values) {
            inSB.append(",").append(dealValue(value));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("IN").append(SPACE).append("(").append(inSB.substring(1)).append(")");
        return express(sb.toString());
    }

    public QueryWrapper notIn(String field, Object... values) {
        if (null == values || values.length == 0) {
            return this;
        }

        StringBuilder inSB = new StringBuilder();
        for (Object value : values) {
            inSB.append(",").append(dealValue(value));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("NOT IN").append(SPACE).append("(").append(inSB.substring(1)).append(")");
        return express(sb.toString());
    }

    public QueryWrapper isNull(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("IS NULL");
        return express(sb.toString());
    }

    public QueryWrapper isNotNull(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("IS NOT NULL");
        return express(sb.toString());
    }

    /* 时间判定 */
    public QueryWrapper before(String field, LocalDateTime value) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("BEFORE").append(SPACE).append(dealValue(value));
        return express(sb.toString());
    }

    public QueryWrapper after(String field, LocalDateTime value) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("AFTER").append(SPACE).append(dealValue(value));
        return express(sb.toString());
    }

    public QueryWrapper during(String field, LocalDateTime value1, LocalDateTime value2) {
        StringBuilder sb = new StringBuilder();
        sb.append(field).append(SPACE).append("DURING").append(SPACE).append(dealValue(value1)).append("/").append(dealValue(value2));
        return express(sb.toString());
    }

    /* 空间判定 */
    public QueryWrapper st_bbox(String column, Double xmin, Double xmax, Double ymin, Double ymax) {
        String condition = String.format("BBOX(%s,%f,%f,%f,%f)", column, xmin, xmax, ymin, ymax);
        return express(condition);
    }

    public QueryWrapper express(String expression) {
        if (hasCondition) {
            if (isAnd) {
                query.append(SPACE).append("AND").append(SPACE);
            } else {
                query.append(SPACE).append("OR").append(SPACE);
            }
        } else {
            hasCondition = true;
        }

        query.append(expression);
        /* 默认都是 and 条件，or 条件仅保证后面添加的第一条条件 */
        if (!isAnd) {
            isAnd = true;
        }
        return this;
    }

    public String getECQL() {
        if (hasCondition) {
            return query.toString();
        } else {
            return "";
        }
    }

    /**
     * 仅在之后跟着的条件有效
     */
    public QueryWrapper or() {
        isAnd = false;
        return this;
    }

    /**
     * 嵌套条件
     *
     * @param child
     * @return
     */
    public QueryWrapper nested(QueryWrapper child) {
        return express("(" + child.getECQL() + ")");
    }

    private Object dealValue(Object val) {
        if (val instanceof String) {
            return String.format("'%s'", val);
        } else if (val instanceof LocalDateTime) {
            DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            String strDate = ((LocalDateTime) val).format(dtf);
            return strDate;
        }
        return val;
    }
}
