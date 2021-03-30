package com.github.superzhc.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author superz
 * @create 2021/3/30 11:09
 */
public class DateUtils {
    /**
     * 获取下一个小时的整点数
     *
     * @return
     */
    public static Date getNextHourTime() {
        return getHourTime(1);
    }

    /**
     * 获取当前时间的整点时间
     *
     * @return
     */
    public static Date getCurrentHourTime() {
        // Calendar ca = Calendar.getInstance();
        // ca.set(Calendar.MINUTE, 0);
        // ca.set(Calendar.SECOND, 0);
        // Date date = ca.getTime();
        // return date;
        return getHourTime(0);
    }

    /**
     * 获取相对当前时间的n个小时的整点数
     *
     * @param n
     * @return
     */
    public static Date getHourTime(int n) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY) + n);
        Date date = ca.getTime();
        return date;
    }

    /**
     * 判断当前时间是否是整点
     *
     * @return
     */
    public static boolean isCurrentHourTime() {
        if (System.currentTimeMillis() % 3600000 < 60000) {
            return true;
        } else {
            return false;
        }
    }
}
