package com.github.superzhc.common.jdbc.generator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author superz
 * @create 2022/7/8 11:23
 **/
public class MySQLGenerator extends BaseGenerator {

    public static enum MySQLDataType {
        /*数值类型*/
        TINYINT, SMALLINT, MEDIUMINT //
        , INT(false, false)       //
        , INTEGER(false, false) //
        , BIGINT(true, false)       //
        , FLOAT(false, true)       //
        , DOUBLE(false, true)     //
        , DECIMAL(false, true),
        /*日期和时间类型*/
        DATE(false, false)            //
        , TIME, YEAR, DATETIME, TIMESTAMP,
        /*字符串类型*/
        CHAR //
        , VARCHAR(true, false)    //
        , TINYBLOB, TINYTEXT, BLOB, TEXT, MEDIUMBLOB, MEDIUMTEXT, LONGBLOB, LONGTEXT;

        private boolean isSupportLength;
        private boolean isSupportPrecision;
//        private Class javaClass = null;
//        private boolean isJavaDefault = false;

        MySQLDataType() {
            this(false, false);
        }

        MySQLDataType(boolean isSupportLength, boolean isSupportPrecision) {
            this.isSupportLength = isSupportLength;
            this.isSupportPrecision = isSupportPrecision;
//            this.javaClass = javaClass;
//            this.isJavaDefault = isJavaDefault;
        }

        public boolean isSupportLength() {
            return isSupportLength;
        }

        public boolean isSupportPrecision() {
            return isSupportPrecision;
        }

        public static MySQLDataType fromJavaClass(Class clazz) {
            if (Integer.class == clazz) {
                return INT;
            } else if (Long.class == clazz) {
                return BIGINT;
            } else if (String.class == clazz) {
                return VARCHAR;
            } else if (Float.class == clazz) {
                return FLOAT;
            } else if (Double.class == clazz) {
                return DOUBLE;
            } else if (BigDecimal.class == clazz) {
                return DECIMAL;
            } else if (Date.class == clazz || LocalDate.class == clazz) {
                return DATE;
            } else if (LocalDateTime.class == clazz) {
                return DATETIME;
            } else if (Timestamp.class == clazz) {
                return TIMESTAMP;
            } else {
                return TEXT;
            }
        }
    }

    public MySQLGenerator(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected String handleJavaClass2DBType(Class javaClass) {
        return MySQLDataType.fromJavaClass(javaClass).name();
    }

    @Override
    protected String handleColumnType(String columnName, String columnType, int length, int precision, int scale) {
        MySQLDataType dataType = MySQLDataType.valueOf(columnType.toUpperCase());

        if (dataType.isSupportLength() && length > 0) {
            return String.format("%s(%d)", dataType.name(), length);
        }

        if (dataType.isSupportPrecision() && precision > 0 && scale > 0) {
            return String.format("%s(%d,%d)", dataType.name(), precision, scale);
        }

        return dataType.name();
    }

    @Override
    protected String handleColumnDefaultValue(String columnName, String columnFullType, String defaultValue) {
        return super.handleColumnDefaultValue(columnName, columnFullType, defaultValue);
    }
}
