package com.github.superzhc.hive.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 2020年09月28日 superz add
 */
@Description(name = "zodiac", value = "_FUNC_(date) - from the input date string or separate month and day arguments, return the sign of the Zodiac.", extended = "Example:\n > SELECT _FUNC_(date_string) FROM src;\n > SELECT _FUNC_(month,day) FROM src;")
public class UDFZodiacSign extends UDF
{
    private SimpleDateFormat df;

    public UDFZodiacSign() {
        df = new SimpleDateFormat("MM-dd-yyyy");
    }

    public String evaluate(Date bday) {
        return evaluate(bday.getMonth() + 1, bday.getDay());
    }

    public String evaluate(String bday) {
        Date date = null;
        try {
            date = df.parse(bday);
        }
        catch (Exception ex) {
            return null;
        }
        return evaluate(date.getMonth() + 1, date.getDay());
    }

    public String evaluate(Integer month, Integer day) {
        switch (month) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return day < 20 ? "XX" : "YY";
            default:
                return null;
        }
    }
}
