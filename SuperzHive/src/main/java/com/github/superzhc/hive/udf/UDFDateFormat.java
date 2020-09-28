package com.github.superzhc.hive.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 2020年09月28日 superz add
 */
@Description(name = "date_format", value = "_FUNC_(date,format) - 日期格式化，支持时间类型、字符串类型")
public class UDFDateFormat extends UDF
{
    public String evaluate(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public String evaluate(String sdate, String f1, String format) {
        SimpleDateFormat df = new SimpleDateFormat(f1);
        try {
            Date date = df.parse(sdate);
            return evaluate(date, format);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
