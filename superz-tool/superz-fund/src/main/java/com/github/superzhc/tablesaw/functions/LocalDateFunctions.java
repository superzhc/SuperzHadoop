package com.github.superzhc.tablesaw.functions;

import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.LongColumn;

import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * @author superz
 * @create 2022/5/31 15:32
 **/
public class LocalDateFunctions {
    public static LongColumn convert2Long(DateColumn dates) {
        LongColumn longs = LongColumn.create(dates.name() + "[Long]", dates.size());
        for (int i = 0, size = dates.size(); i < size; i++) {
            LocalDate date = dates.get(i);
            long timstamp = date.atStartOfDay(ZoneOffset.ofHours(+8)).toInstant().toEpochMilli();
            longs.set(i, timstamp);
        }
        return longs;
    }
}
