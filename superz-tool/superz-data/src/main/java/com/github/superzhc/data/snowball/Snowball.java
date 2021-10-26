package com.github.superzhc.data.snowball;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.superzhc.data.snowball.entity.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2021/8/2 17:34
 */
public class Snowball {
    private static final String BASE_URL = "https://stock.xueqiu.com";

    // finance
    private static final String FINANCE_CASH_FLOW_URL = BASE_URL + "/v5/stock/finance/cn/cash_flow.json?symbol=";
    private static final String FINANCE_INDICATOR_URL = BASE_URL + "/v5/stock/finance/cn/indicator.json?symbol=";
    private static final String FINANCE_BALANCE_URL = BASE_URL + "/v5/stock/finance/cn/balance.json?symbol=";
    private static final String FINANCE_INCOME_URL = BASE_URL + "/v5/stock/finance/cn/income.json?symbol=";
    private static final String FINANCE_BUSINESS_URL = BASE_URL + "/v5/stock/finance/cn/business.json?symbol=";

    // report
    private static final String REPORT_LATEST_URL = BASE_URL + "/stock/report/latest.json?symbol=";
    private static final String REPORT_EARNINGFORECAST_URL = BASE_URL + "/stock/report/earningforecast.json?symbol=";

    // capital
    private static final String CAPITAL_MARGIN_URL = BASE_URL + "/v5/stock/capital/margin.json?symbol=";
    private static final String CAPITAL_BLOCKTRANS_URL = BASE_URL + "/v5/stock/capital/blocktrans.json?symbol=";
    private static final String CAPITAL_ASSORT_URL = BASE_URL + "/v5/stock/capital/assort.json?symbol=";
    private static final String CAPITAL_HISTORY_URL = BASE_URL + "/v5/stock/capital/history.json?symbol=";
    private static final String CAPITAL_FLOW_URL = BASE_URL + "/v5/stock/capital/flow.json?symbol=";

    // f10
    private static final String F10_SKHOLDERCHG_URL = BASE_URL + "/v5/stock/f10/cn/skholderchg.json?symbol=";
    private static final String F10_SKHOLDER_URL = BASE_URL + "/v5/stock/f10/cn/skholder.json?symbol=";
    private static final String F10_INDUSTRY_URL = BASE_URL + "/v5/stock/f10/cn/industry.json?symbol=";
    private static final String F10_HOLDERS_URL = BASE_URL + "/v5/stock/f10/cn/holders.json?&symbol=";
    private static final String F10_BONUS_URL = BASE_URL + "/v5/stock/f10/cn/bonus.json?&symbol=";
    private static final String F10_ORG_HOLDING_CHANGE_URL = BASE_URL + "/v5/stock/f10/cn/org_holding/change.json?symbol=";
    private static final String F10_INDUSTRY_COMPARE_URL = BASE_URL + "/v5/stock/f10/cn/industry/compare.json?type=single&symbol=";
    private static final String F10_BUSINESS_ANALYSIS_URL = BASE_URL + "/v5/stock/f10/cn/business_analysis.json?symbol=";
    private static final String F10_SHARESCHG_URL = BASE_URL + "/v5/stock/f10/cn/business_analysis.json?symbol=";
    private static final String F10_TOP_HOLDERS_URL = BASE_URL + "/v5/stock/f10/cn/top_holders.json?&symbol=";
    private static final String F10_INDICATOR_URL = BASE_URL + "/v5/stock/f10/cn/indicator.json?symbol=";

    // real time
    private static final String REALTIME_QUOTEC_URL = BASE_URL + "/v5/stock/realtime/quotec.json?symbol=";
    private static final String REALTIME_PANKOU_URL = BASE_URL + "/v5/stock/realtime/pankou.json?symbol=";

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    private static final Map<String, String> HEADERS = new HashMap<>();

    static {
        /*
        {'Host': 'stock.xueqiu.com',
               'Accept': 'application/json',
               'Cookie': token.get_token(),
               'User-Agent': 'Xueqiu iPhone 11.8',
               'Accept-Language': 'zh-Hans-CN;q=1, ja-JP;q=0.9',
               'Accept-Encoding': 'br, gzip, deflate',
               'Connection': 'keep-alive'}
         */
        HEADERS.put("Host", "stock.xueqiu.com");
        HEADERS.put("Accept", "application/json");
        HEADERS.put("User-Agent", "Xueqiu iPhone 11.8");
        HEADERS.put("Accept-Language", "zh-Hans-CN;q=1, ja-JP;q=0.9");
        HEADERS.put("Accept-Encoding", "br, gzip, deflate");
        HEADERS.put("Connection", "keep-alive");
    }

    private String token;

    public Snowball(String token) {
        this.token = token;
        HEADERS.put("Cookie", this.token);
    }


    public FinanceCashFlow cashFlow(String symbol, String type) {
        return cashFlow(symbol, type, 10);
    }

    /**
     * 现金流量表
     *
     * @param symbol
     * @param type   Q4
     * @param count
     * @return
     */
    public FinanceCashFlow cashFlow(String symbol, String type, Integer count) {
        String url = new StringBuilder(FINANCE_CASH_FLOW_URL).append(symbol).append("&type=").append(type).append("&count=").append(count).toString();
        return fetch(url, FinanceCashFlow.class);
    }

    /**
     * 业绩指标
     * <p>
     * 按年度、季度获取业绩报表数据
     *
     * @param symbol
     * @param type
     * @param count
     * @return
     */
    public FinanceIndicator indicator(String symbol, String type, Integer count) {
        String url = new StringBuilder(FINANCE_INDICATOR_URL).append(symbol).append("&type=").append(type).append("&count=").append(count).toString();
        return fetch(url, FinanceIndicator.class);
    }

    /**
     * 资产负债表
     *
     * @param symbol
     * @param type
     * @param count
     * @return
     */
    public FinanceBalance balance(String symbol, String type, Integer count) {
        String url = new StringBuilder(FINANCE_BALANCE_URL).append(symbol).append("&type=").append(type).append("&count=").append(count).toString();
        return fetch(url, FinanceBalance.class);
    }

    /**
     * 利润表
     *
     * @param symbol
     * @param type
     * @param count
     * @return
     */
    public FinanceIncome income(String symbol, String type, Integer count) {
        String url = new StringBuilder(FINANCE_INCOME_URL).append(symbol).append("&type=").append(type).append("&count=").append(count).toString();
        return fetch(url, FinanceIncome.class);
    }

    /**
     * 主营业务构成
     *
     * @param symbol
     * @param type
     * @param count
     * @return
     */
    public FinanceBusiness business(String symbol, String type, Integer count) {
        String url = new StringBuilder(FINANCE_BUSINESS_URL).append(symbol).append("&type=").append(type).append("&count=").append(count).toString();
        return fetch(url, FinanceBusiness.class);
    }

    public List<CapitalMargin> margin(String symbol) {
        return margin(symbol, 1, 180);
    }

    /**
     * 融资融券
     * <p>
     * 融资融券数据
     *
     * @param symbol
     * @param page
     * @param size
     * @return
     */
    public List<CapitalMargin> margin(String symbol, Integer page, Integer size) {
        String url = new StringBuilder(CAPITAL_MARGIN_URL).append(symbol).append("&page=").append(page).append("&size=").append(size).toString();
        JSONObject json = fetch(url);
        return json.getJSONObject("data").getJSONArray("items").toJavaList(CapitalMargin.class);
    }

    public List<CapitalBlocktrans> blocktrans(String symbol) {
        return blocktrans(symbol, 1, 30);
    }

    /**
     * 大宗交易
     * <p>
     * 大宗交易数据
     *
     * @param symbol
     * @param page
     * @param size
     * @return
     */
    public List<CapitalBlocktrans> blocktrans(String symbol, Integer page, Integer size) {
        String url = new StringBuilder(CAPITAL_BLOCKTRANS_URL).append(symbol).append("&page=").append(page).append("&size=").append(size).toString();
        JSONObject json = fetch(url);
        return json.getJSONObject("data").getJSONArray("items").toJavaList(CapitalBlocktrans.class);
    }

    /**
     * 资金成交分布
     * <p>
     * 获取资金成交分布数据
     *
     * @param symbol
     * @return
     */
    public CapitalAssort assort(String symbol) {
        String url = CAPITAL_ASSORT_URL + symbol;
        return fetch(url, CapitalAssort.class);
    }

    /**
     * 资金流向趋势
     * <p>
     * 获取当日资金流如流出数据，每分钟数据
     *
     * @param symbol
     * @return
     */
    public CapitalFlow flow(String symbol) {
        String url = CAPITAL_FLOW_URL + symbol;
        return fetch(url, CapitalFlow.class);
    }

    public CapitalHistory history(String symbol) {
        return history(symbol, 20);
    }

    /**
     * 资金流向历史
     * <p>
     * 获取历史资金流如流出数据，每日数据
     *
     * @param symbol
     * @param count
     * @return
     */
    public CapitalHistory history(String symbol, Integer count) {
        String url = new StringBuilder(CAPITAL_HISTORY_URL).append(symbol).append("&count=").append(count).toString();
        return fetch(url, CapitalHistory.class);
    }

    public F10Skholderchg skholderchg(String symbol) {
        String url = F10_SKHOLDERCHG_URL + symbol;
        return fetch(url, F10Skholderchg.class);
    }

    public F10Skholder skholder(String symbol) {
        String url = F10_SKHOLDER_URL + symbol;
        return fetch(url, F10Skholder.class);
    }

    public F10Industry industry(String symbol) {
        String url = F10_INDUSTRY_URL + symbol;
        return fetch(url, F10Industry.class);
    }

    /**
     * 股东人数
     *
     * @param symbol
     * @return
     */
    public List<F10Holders> holders(String symbol) {
        String url = F10_HOLDERS_URL + symbol;
        JSONObject json = fetch(url);
        return json.getJSONObject("data").getJSONArray("items").toJavaList(F10Holders.class);
    }

    public F10Bonus bonus(String symbol) {
        return bonus(symbol, 1, 10);
    }

    /**
     * 分红融资
     *
     * @param symbol
     * @param page
     * @param size
     * @return
     */
    public F10Bonus bonus(String symbol, Integer page, Integer size) {
        String url = new StringBuilder(F10_BONUS_URL).append(symbol).append("&page=").append(page).append("&size=").append(size).toString();
        return fetch(url, F10Bonus.class);
    }

    /**
     * 机构持仓
     *
     * @param symbol
     * @return
     */
    public List<F10OrgHoldingChange> orgHoldingChange(String symbol) {
        String url = F10_ORG_HOLDING_CHANGE_URL + symbol;
        JSONObject json = fetch(url);
        return json.getJSONObject("data").getJSONArray("items").toJavaList(F10OrgHoldingChange.class);
    }

    /**
     * 行业对比
     *
     * @param symbol
     * @return
     */
    public F10IndustryCompare industryCompare(String symbol) {
        String url = F10_INDUSTRY_COMPARE_URL + symbol;
        return fetch(url, F10IndustryCompare.class);
    }

    public F10BusinessAnalysis businessAnalysis(String symbol) {
        String url = F10_BUSINESS_ANALYSIS_URL + symbol;
        return fetch(url, F10BusinessAnalysis.class);
    }

    public F10Shareschg shareschg(String symbol) {
        return shareschg(symbol, 5);
    }

    public F10Shareschg shareschg(String symbol, Integer count) {
        String url = new StringBuilder(F10_SHARESCHG_URL).append(symbol).append("&count=").append(count).toString();
        return fetch(url, F10Shareschg.class);
    }


    public F10TopHolders topHolders(String symbol) {
        return topHolders(symbol, 1);
    }

    /**
     * 十大股东
     *
     * @param symbol
     * @param circula
     * @return
     */
    public F10TopHolders topHolders(String symbol, Integer circula) {
        String url = new StringBuilder(F10_TOP_HOLDERS_URL).append(symbol).append("&circula=").append(circula).toString();
        return fetch(url, F10TopHolders.class);
    }

    /**
     * 主要指标
     *
     * @param symbol
     * @return
     */
    public List<F10Indicator> mainIndicator(String symbol) {
        String url = F10_INDICATOR_URL + symbol;
        JSONObject json = fetch(url);
        return json.getJSONObject("data").getJSONArray("items").toJavaList(F10Indicator.class);
    }

    /**
     * 实时行情
     * <p>
     * 获取某支股票的行情数据
     *
     * @param symbol
     * @return
     */
    public RealtimeQuotec quotec(String symbol) {
        String url = REALTIME_QUOTEC_URL + symbol;
        return fetch(url, RealtimeQuotec.class);
    }

    /**
     * 实时分笔
     * <p>
     * 获取实时分笔数据，可以实时取得股票当前报价和成交信息
     *
     * @param symbol
     * @return
     */
    public RealtimePankou pankou(String symbol) {
        String url = REALTIME_PANKOU_URL + symbol;
        return fetch(url, RealtimePankou.class);
    }

    /**
     * 机构评级
     * <p>
     * 获取机构评级数据
     *
     * @param symbol
     * @return
     */
    public List<ReportLatest> report(String symbol) {
        String url = REPORT_LATEST_URL + symbol;
        return fetchList(url, ReportLatest.class);
    }

    /**
     * 业绩预告
     * <p>
     * 按年度获取业绩预告数据
     *
     * @param symbol
     * @return
     */
    public List<ReportEarningforecast> earningforecast(String symbol) {
        String url = REPORT_EARNINGFORECAST_URL + symbol;
        return fetchList(url, ReportEarningforecast.class);
    }

    private JSONObject fetch(String url) {
        Request.Builder builder = new Request.Builder().url(url);
        for (Map.Entry<String, String> header : HEADERS.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.get().build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("请求异常:code={" + response.code() + "}\n异常信息:" + response.body().string());
            }
            JSONObject json = JSON.parseObject(response.body().string());
            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fetch(String url, Class<T> clazz) {
        JSONObject json = fetch(url);
        return json.getObject("data", clazz);
    }

    private <T> List<T> fetchList(String url, Class<T> clazz) {
        JSONObject json = fetch(url);
        return json.getJSONArray("list").toJavaList(clazz);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
