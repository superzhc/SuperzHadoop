package com.github.superzhc.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * @author superz
 * @create 2022/5/19 14:30
 **/
public class DateUtils {
    private DateUtils() {
    }

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static Map<Integer, List<String>> holidayByYear = new ConcurrentHashMap<>();
    /* 非工作日 */
    private static Map<Integer, List<String>> offDayByYear = new ConcurrentHashMap<>();
    /* 交易日 */
    private static Map<Integer, List<String>> nonTradingDayByYear = new ConcurrentHashMap<>();

    public static boolean isHoliday(LocalDate date) {
        int year = date.getYear();
        if (!holidayByYear.containsKey(year)) {
            String url = String.format("https://natescarlet.coding.net/p/github/d/holiday-cn/git/raw/master/%d.json", year);
            String result = HttpRequest.get(url).userAgent(UA).body();
            JsonNode days = JsonUtils.json(result, "days");
            List<String> holidays = new ArrayList<>();
            for (JsonNode day : days) {
                if (JsonUtils.bool(day, "isOffDay")) {
                    holidays.add(JsonUtils.string(day, "date"));
                }
            }
            holidayByYear.put(year, holidays);
        }

        return holidayByYear.get(year).contains(date.format(DEFAULT_FORMATTER));
    }

    /**
     * 是否是非工作日
     *
     * @param date
     * @return
     */
    public static boolean isOffDay(LocalDate date) {
        int year = date.getYear();
        if (!offDayByYear.containsKey(year)) {
            String url = String.format("https://natescarlet.coding.net/p/github/d/holiday-cn/git/raw/master/%d.json", year);
            String result = HttpRequest.get(url).userAgent(UA).body();
            JsonNode days = JsonUtils.json(result, "days");
            // 周末补节假日的调休
            List<String> nonOffDays = new ArrayList<>();
            List<String> offDays = new ArrayList<>();
            for (JsonNode day : days) {
                if (JsonUtils.bool(day, "isOffDay")) {
                    offDays.add(JsonUtils.string(day, "date"));
                } else {
                    nonOffDays.add(JsonUtils.string(day, "date"));
                }
            }

            LocalDate start = LocalDate.of(year, 1, 1);
            LocalDate end = start.plusYears(1);

            LocalDate d = start;
            while (d.isBefore(end)) {
                if (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    String dStr = d.format(DEFAULT_FORMATTER);
                    if (!nonOffDays.contains(dStr)) {
                        offDays.add(dStr);
                    }
                }
                d = d.plusDays(1);
            }

            offDayByYear.put(year, offDays);
        }

        return offDayByYear.get(year).contains(date.format(DEFAULT_FORMATTER));
    }

    public static boolean isNonTradingDay(LocalDate date) {
        int year = date.getYear();
        if (!nonTradingDayByYear.containsKey(year)) {
            String url = String.format("https://natescarlet.coding.net/p/github/d/holiday-cn/git/raw/master/%d.json", year);
            String result = HttpRequest.get(url).userAgent(UA).body();
            JsonNode days = JsonUtils.json(result, "days");
            // 周末补节假日的调休
            List<String> nonTradingDays = new ArrayList<>();
            for (JsonNode day : days) {
                if (JsonUtils.bool(day, "isOffDay")) {
                    nonTradingDays.add(JsonUtils.string(day, "date"));
                }
            }

            LocalDate start = LocalDate.of(year, 1, 1);
            LocalDate end = start.plusYears(1);

            LocalDate d = start;
            while (d.isBefore(end)) {
                if (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    String dStr = d.format(DEFAULT_FORMATTER);
                    nonTradingDays.add(dStr);
                }
                d = d.plusDays(1);
            }

            nonTradingDayByYear.put(year, nonTradingDays);
        }

        return nonTradingDayByYear.get(year).contains(date.format(DEFAULT_FORMATTER));
    }


    public static void main(String[] args) {
        System.out.println(isHoliday(LocalDate.of(2022, 5, 4)));

    }
}
