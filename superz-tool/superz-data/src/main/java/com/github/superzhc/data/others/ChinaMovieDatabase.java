package com.github.superzhc.data.others;

import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.utils.ResultT;

/**
 * 中国电影数据信息网
 * 网址：https://www.zgdypw.cn/
 *
 * @author superz
 * @create 2022/2/17 10:01
 */
public class ChinaMovieDatabase extends HttpData {
    public ResultT searchDayBoxOffice() {
        String url = String.format("https://www.zgdypw.cn/data/searchDayBoxOffice.json?timestamp=%d", System.currentTimeMillis());
        return get(url);
    }

    public ResultT searchSevenDaysBoxOffice() {
        String url = String.format("https://www.zgdypw.cn/data/searchSevenDaysBoxOffice.json?timestamp=%d", System.currentTimeMillis());
        return get(url);
    }

    public static void main(String[] args) {
        new ChinaMovieDatabase().searchDayBoxOffice();
    }
}
