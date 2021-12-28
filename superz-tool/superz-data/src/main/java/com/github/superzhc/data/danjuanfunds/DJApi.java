package com.github.superzhc.data.danjuanfunds;

import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.common.ResultT;
import okhttp3.Request;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 蛋卷 API
 *
 * @author superz
 * @create 2021/12/28 16:33
 */
public class DJApi extends HttpData {
    private static final String URL = "https://danjuanfunds.com/djapi";

    public DJApi() {
    }

    /**
     * 股票型排行
     *
     * @param orderBy
     * @param page
     * @param size
     * @return
     */
    public ResultT stockRanking(String orderBy, Integer page, Integer size) {
        return ranking("1", orderBy, page, size);
    }

    /**
     * 混合型
     *
     * @param orderBy
     * @param page
     * @param size
     * @return
     */
    public ResultT hybridRanking(String orderBy, Integer page, Integer size) {
        return ranking("3", orderBy, page, size);
    }

    /**
     * 债券型
     *
     * @param orderBy
     * @param page
     * @param size
     * @return
     */
    public ResultT bondRanking(String orderBy, Integer page, Integer size) {
        return ranking("2", orderBy, page, size);
    }

    /**
     * 指数型
     *
     * @param orderBy
     * @param page
     * @param size
     * @return
     */
    public ResultT ETFRanking(String orderBy, Integer page, Integer size) {
        return ranking("5", orderBy, page, size);
    }

    /**
     * QDII型
     *
     * @param orderBy
     * @param page
     * @param size
     * @return
     */
    public ResultT QDIIRanking(String orderBy, Integer page, Integer size) {
        return ranking("11", orderBy, page, size);
    }

    /**
     * 业绩排行
     *
     * @param type    类型
     *                股票型（1）、混合型（3）、债券型（2）、指数型（5）、QDII型（11）
     * @param orderBy 排名方式
     *                日涨幅（td）、近一周(1w)、近一月（1m）、近三月（3m）、近六月（6m）、今年以来（ty）、近一年（1y）、近两年（2y）、近三年（3y）、近五年（5y）、成立以来(base)
     * @param page
     * @param size
     * @return
     */
    public ResultT ranking(String type, String orderBy, Integer page, Integer size) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("type", type);
        params.put("order_by", orderBy);
        params.put("size", String.valueOf(size));
        params.put("page", String.valueOf(page));
        return get("/v3/filter/fund", null, params);
    }

    public ResultT fund(String code) {
        return get("/fund/" + code);
    }

    public ResultT detail(String code) {
        return get("/fund/detail/" + code);
    }

    public ResultT history(String code, Integer days) {
        Map<String, String> params = new HashMap<>();
        params.put("size", String.valueOf(days));
        params.put("page", "1");
        return get("/fund/nav/history/" + code, null, params);
    }

    @Override
    protected Request.Builder commonBuilder(String url, Map<String, String> headers) {
        return super.commonBuilder(URL + url, headers);
    }

    public static void main(String[] args) {
        DJApi api = new DJApi();
        ResultT resultT = api.history("501057", 100);
        System.out.println(resultT);
    }
}
