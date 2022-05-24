package com.github.superzhc.fund.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.ScriptUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.tablesaw.read.EmptyReadOptions;
import com.github.superzhc.tablesaw.utils.ColumnUtils;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.ReadOptionsUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import javax.script.ScriptEngine;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * @author superz
 * @create 2022/3/29 10:04
 **/
public class EastMoneyFund {
    private static final Logger log = LoggerFactory.getLogger(EastMoneyFund.class);

    public static Table funds() {
        String url = "http://fund.eastmoney.com/js/fundcode_search.js";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", UA);

        try {
            String result = HttpRequest.get(url).headers(headers).body();
            String json = result.substring("var r = ".length(), result.length() - 1);
            JsonNode nodes = JsonUtils.json(json);

            List<String> columnNames = Arrays.asList(
                    "code",
                    "pinyin",
                    "name",
                    "type",
                    "full_pinyin"
            );

            List<String[]> dataRows = JsonUtils.extractArrayData(nodes);

            Table table = TableUtils.build(columnNames, dataRows);
            return table;
        } catch (Exception e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

    /*
    public static Table openFundDaily() {
        String url = "http://fund.eastmoney.com/Data/Fund_JJJZ_Data.aspx";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", UA);

        Map<String, String> params = new HashMap<>();
        params.put("t", "1");
        params.put("lx", "1");
        params.put("letter", "");
        params.put("gsid", "");
        params.put("text", "");
        params.put("sort", "zdf,desc");
        params.put("page", "1,20000");
        params.put("dt", "1580914040623");
        params.put("atfc", "");
        params.put("onlySale", "0");

        try {
            String result = HttpRequest.get(url, params, true).headers(headers).body();
            String json = result.substring("var db=".length());
            // 坑~~~
            json = json
                    .replace("[ ,", "[ \"\",").replace("[,", "[ \"\",")
                    .replace(",,", ",\"\",").replace(", ,", ", \"\",")
                    .replace(", ]", "]").replace(",]", "]");
            JsonNode jsonNode = JsonUtils.json(json);//mapper.readTree(json);
            JsonNode datas = jsonNode.get("datas");
            JsonNode showDay = jsonNode.get("showday");

            List<String> columnNames = Arrays.asList(
                    "基金代码",
                    "基金简称",
                    "c1",
                    showDay.get(0).asText() + "-单位净值",
                    showDay.get(0).asText() + "-累计净值",
                    showDay.get(1).asText() + "-单位净值",
                    showDay.get(1).asText() + "-累计净值",
                    "日增长值",
                    "日增长率",
                    "申购状态",
                    "赎回状态",
                    "c2",
                    "c3",
                    "c4",
                    "c5",
                    "c6",
                    "c7",
                    "手续费",
                    "c8",
                    "c9",
                    "c10");

            List<String[]> dataRows = new ArrayList<>();
            for (JsonNode node : datas) {
                String[] row = new String[node.size()];
                for (int i = 0, len = node.size(); i < len; i++) {
                    row[i] = node.get(i).asText();
                }
                dataRows.add(row);
            }

            Table table = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().build());
            table = table.select(
                    "基金代码",
                    "基金简称",
                    showDay.get(0).asText() + "-单位净值",
                    showDay.get(0).asText() + "-累计净值",
                    showDay.get(1).asText() + "-单位净值",
                    showDay.get(1).asText() + "-累计净值",
                    "日增长值",
                    "日增长率",
                    "申购状态",
                    "赎回状态",
                    "手续费"
            );
            return table;
        } catch (Exception e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }
*/

    /**
     * 推荐使用 fundNew 方法
     *
     * @param symbol
     * @return
     */
    @Deprecated
    public static Table fund(String symbol) {
        String url = String.format("http://fundf10.eastmoney.com/jbgk_%s.html", symbol);

        try {
            String result = HttpRequest.get(url).body();
            Document doc = Jsoup.parse(result);
            JXDocument jxDoc = JXDocument.create(doc);

            // 名称
            String fullName = jxDoc.selNOne("//div/div/table/tbody/tr[1]/td[1]/text()").asString();
            // 简称
            String simpleName = jxDoc.selNOne("//div/div/table/tbody/tr[1]/td[2]/text()").asString();
            // 代码
            String code = jxDoc.selNOne("//div/div/table/tbody/tr[2]/td[1]/text()").asString();
            // 类型
            String type = jxDoc.selNOne("//div/div/table/tbody/tr[2]/td[2]/text()").asString();
            // 发行时间
            String issueDate = jxDoc.selNOne("//div/div/table/tbody/tr[3]/td[1]/text()").asString();
            // 成立日期
            String foundDate = jxDoc.selNOne("//div/div/table/tbody/tr[3]/td[2]/text()").asString();
            // 资产规模
            String assetSize = jxDoc.selNOne("//div/div/table/tbody/tr[4]/td[1]/text()").asString();
            // 份额规模
            String shareSize = jxDoc.selNOne("//div/div/table/tbody/tr[4]/td[2]/allText()").asString();
            // 管理人
            String keeper = jxDoc.selNOne("//div/div/table/tbody/tr[5]/td[1]/a/text()").asString();
            // 托管人
            String custodian = jxDoc.selNOne("//div/div/table/tbody/tr[5]/td[2]/a/text()").asString();
            // 经理人
            String manager = jxDoc.selNOne("//div/div/table/tbody/tr[6]/td[1]/a/text()").asString();
            // 分红
            String dividend = jxDoc.selNOne("//div/div/table/tbody/tr[6]/td[2]/a/text()").asString();
            // 追踪指数
            String trackIndex = jxDoc.selNOne("//div/div/table/tbody/tr[10]/td[2]/text()").asString();
            // 目标
            JXNode investmentObjective = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[2]/div/p/text()");
            // 理念
            String investmentConcept = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[3]/div/p/text()").asString();
            // 范围
            String investmentScope = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[4]/div/p/text()").asString();
            // 策略
            String investmentTatics = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[5]/div/p/text()").asString();
            // 分红策略
            String dividendPolicy = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[6]/div/p/text()").asString();
            // 风险收益特征
            String riskIncomeCharacteristics = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[7]/div/p/text()").asString();

            Map<String, String> map = new LinkedHashMap<>();
            map.put("code", code);
            map.put("full_name", fullName);
            map.put("name", simpleName);
            map.put("type", type);
            map.put("issue_date", issueDate);
            map.put("found_date", foundDate);
            map.put("asset_size", assetSize);
            map.put("share_size", shareSize);
            map.put("manager", manager);
            map.put("dividend", dividend);
            map.put("track_index", trackIndex);
            map.put("investment_objective", null == investmentObjective ? null : investmentObjective.asString());
            map.put("investment_concept", investmentConcept);
            map.put("investment_scope", investmentScope);
            map.put("investment_tatics", investmentTatics);
            map.put("dividend_policy", dividendPolicy);
            map.put("risk_income_characteristics", riskIncomeCharacteristics);

            return TableUtils.map2Table(map);
        } catch (Exception e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param symbol
     * @return Structure of null
     * Index  |  Column Name  |  Column Type  |
     * -----------------------------------------
     * 0  |         code  |       STRING  |
     * 1  |         name  |       STRING  |
     * 2  |         type  |       STRING  |
     * 3  |  established  |       STRING  |
     * 4  |   index_code  |       STRING  |
     * 5  |   index_name  |       STRING  |
     * 6  |         rate  |       STRING  |
     * 7  |   risk_level  |       STRING  |
     * 8  |        bench  |       STRING  |
     */
    public static Table fundNew(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNNBasicInformation";

        Map<String, Object> params = new HashMap<>();
        params.put("FCODE", symbol);
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.3.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode jjxq = JsonUtils.json(result, "Datas");

        Map<String, Object> map = new LinkedHashMap<>();

        map.put("code", jjxq.get("FCODE").asText());
        map.put("name", jjxq.get("SHORTNAME").asText());
        map.put("type", jjxq.get("FTYPE").asText());
        map.put("established", jjxq.get("ESTABDATE").asText());
        map.put("index_code", jjxq.get("INDEXCODE").asText());
        map.put("index_name", jjxq.get("INDEXNAME").asText());
        map.put("rate", jjxq.get("RLEVEL_SZ").asText());
        map.put("risk_level", jjxq.get("RISKLEVEL").asText());
//        map.put("bench", jjxq.get("BENCH").asText());
        map.put("scale", jjxq.get("ENDNAV").asText());
        map.put("scale_date", jjxq.get("FEGMRQ").asText());
//        map.put("日涨幅 (%)", jjxq.get("RZDF").asText());
        map.put("net_worth"/*"单位净值"*/, jjxq.get("DWJZ").asText());
        map.put("accumulated_net_worth"/*"累计净值"*/, jjxq.get("LJJZ").asText());
//        map.put("当日确认份额时间点", jjxq.get("CURRENTDAYMARK").asText());
//        map.put("购买起点（元）", jjxq.get("MINSG").asText());
//        map.put("首次购买（元）", jjxq.get("MINSBRG").asText());
//        map.put("追加购买（元）", jjxq.get("MINSBSG").asText());
//        map.put("定投起点（元）", jjxq.get("MINDT").asText());
//        map.put("单日累计购买上限（元）", jjxq.get("MAXSG").asText());
        map.put("purchase_status", jjxq.get("SGZT").asText());
//        map.put("卖出状态", jjxq.get("SHZT").asText());
//        map.put("定投状态", jjxq.get("DTZT").asText());
        map.put("origin_rate"/*"原始购买费率"*/, jjxq.get("SOURCERATE").asText());
        map.put("real_rate"/*"实际购买费率"*/, jjxq.get("RATE").asText());
//        map.put("近1年波动率", jjxq.get("STDDEV1").asText());
//        map.put("近2年波动率", jjxq.get("STDDEV2").asText());
//        map.put("近3年波动率", jjxq.get("STDDEV3").asText());
//        map.put("近1年夏普比率", jjxq.get("SHARP1").asText());
//        map.put("近2年夏普比率", jjxq.get("SHARP2").asText());
//        map.put("近3年夏普比率", jjxq.get("SHARP3").asText());
//        map.put("近1年最大回撤", jjxq.get("MAXRETRA1").asText());
//        map.put("买入确认日", jjxq.get("SSBCFDAY").asText());

        Table table = TableUtils.map2Table(map);

        return table;
    }

    public static Table fundPeriodRank(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNPeriodIncrease";

        Map<String, Object> params = new HashMap<>();
        params.put("FCODE", symbol);
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.3.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode jdzf = JsonUtils.json(result, "Datas");

        // JsonNode json = fundBasic(symbol);
        // JsonNode jdzf = json.get("JDZF").get("Datas");

        List<String> columnNames = Arrays.asList(
                "title",
                "syl",// 收益率
                "avg",// 涨跌幅
                "hs300",// 同类平均
                "rank",// 同类排名
                "sc"// 排名总数
                // ,"diff"// 该字段一样不明，去掉
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(jdzf, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        StringColumn titleColumn = table.stringColumn("title").map(d -> {
            switch (d) {
                case "Z":
                    return "近1周";
                case "Y":
                    return "近1月";
                case "3Y":
                    return "近3月";
                case "6Y":
                    return "近6月";
                case "1N":
                    return "近1年";
                case "2N":
                    return "近2年";
                case "3N":
                    return "近3年";
                case "5N":
                    return "近5年";
                case "JN":
                    return "今年";
                case "LN":
                    return "成立以来";
                default:
                    return d;
            }
        });
        table.replaceColumn("title", titleColumn);

        return table;
    }

    public static Table fundSummarize2(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode tssj = json.get("TSSJ").get("Datas");

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("SHARP1", "近1年夏普比率");
        fields.put("SHARP_1NRANK", "近1年夏普比率排名");
        fields.put("SHARP_1NFSC", "近1年夏普比率排名总数");
        fields.put("SHARP3", "近3年夏普比率");
        fields.put("SHARP_3NRANK", "近3年夏普比率排名");
        fields.put("SHARP_3NFSC", "近3年夏普比率排名总数");
        fields.put("SHARP5", "近5年夏普比率");
        fields.put("SHARP_5NRANK", "近5年夏普比率排名");
        fields.put("SHARP_5NFSC", "近5年夏普比率排名总数");
        fields.put("SYL_1N", "近1年收益率");
        fields.put("SYL_LN", "成立来收益率");
        fields.put("MAXRETRA1", "近1年最大回撤（%）");
        fields.put("MAXRETRA_1NRANK", "近1年最大回撤（%）排名");
        fields.put("MAXRETRA_1NFSC", "近1年最大回撤（%）排名总数");
        fields.put("MAXRETRA3", "近3年最大回撤（%）");
        fields.put("MAXRETRA_3NRANK", "近3年最大回撤（%）排名");
        fields.put("MAXRETRA_3NFSC", "近3年最大回撤（%）排名总数");
        fields.put("MAXRETRA5", "近5年最大回撤（%）");
        fields.put("MAXRETRA_5NRANK", "近5年最大回撤（%）排名");
        fields.put("MAXRETRA_5NFSC", "近5年最大回撤（%）排名总数");
        fields.put("STDDEV1", "近1年波动率（%）");
        fields.put("STDDEV_1NRANK", "近1年波动率（%）排名");
        fields.put("STDDEV_1NFSC", "近1年波动率（%）排名总数");
        fields.put("STDDEV3", "近3年波动率（%）");
        fields.put("STDDEV_3NRANK", "近3年波动率（%）排名");
        fields.put("STDDEV_3NFSC", "近3年波动率（%）排名总数");
        fields.put("STDDEV5", "近5年波动率（%）");
        fields.put("STDDEV_5NRANK", "近5年波动率（%）排名");
        fields.put("STDDEV_5NFSC", "近5年波动率（%）排名总数");
        fields.put("PROFIT_Z", "持有1周盈利概率");
        fields.put("PROFIT_Y", "持有1月盈利概率");
        fields.put("PROFIT_3Y", "持有3月盈利概率");
        fields.put("PROFIT_6Y", "持有6月盈利概率");
        fields.put("PROFIT_1N", "持有1年盈利概率");
        fields.put("PV_Y", "近1月访问量");
        fields.put("DTCOUNT_Y", "近1月定投人数");
        fields.put("FFAVORCOUNT", "加自选人数");
        fields.put("EARN_1N", "EARN_1N");
        fields.put("AVGHOLD", "用户平均持有时长（天）");
        fields.put("BROKENTIMES", "BROKENTIMES");
        fields.put("ISEXCHG", "ISEXCHG");

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("code", symbol);
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            map.put(entry.getValue(), tssj.get(entry.getKey()).asText());
        }

        Table table = TableUtils.map2Table(map);

        return table;
    }

    public static Table fundManager(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode jjjl = json.get("JJJLNEW").get("Datas").get(0).get("MANGER");

        List<String> columnNames = Arrays.asList(
                "MGRID",
                "NEWPHOTOURL",
                "ISINOFFICE",
                "YIELDSE",//年均回报（%）
                "TOTALDAYS",// 从业天数
                "INVESTMENTIDEAR",
                "HJ_JN",// 金牛奖获奖次数
                "DAYS",// 任职该基金天数
                "FEMPDATE",// 任职该基金开始时间
                "LEMPDATE",
                "PENAVGROWTH",// 任职回报（%）
                "MGRNAME"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(jjjl, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table fundScale(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode jjgm = json.get("JJGM").get("Datas");

        List<String> columnNames = Arrays.asList(
                "FSRQ",// 日期
                "NETNAV",// 净资产（元）
                "CHANGE",// 净资产变动率（%）
                "ISSUM"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(jjgm, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table.column("FSRQ").setName("date");

        return table;

    }

    public static Table fundUpdatePosition(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNIVInfoMultiple";

        Map<String, Object> params = new HashMap<>();
        params.put("FCODE", symbol);
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.3.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();

        JsonNode datas = JsonUtils.json(result, "Datas");

        List<String> columnNames = Arrays.asList(
                "title",
                "date"
        );

        List<String[]> dataRows = new ArrayList<>();

        int size = datas.size();
        for (int i = 0; i < size; i++) {
            String[] row = new String[2];
            row[0] = String.format("第%d次更新日期", size - i);
            row[1] = datas.get(i).asText();

            dataRows.add(row);
        }

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table fundFenHong(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode fhsp = json.get("FHSP").get("Datas").get("FHINFO");

        List<String> columnNames = Arrays.asList(
                "FSRQ",
                "DJR",// 权益登记日
                "FHFCZ",// 每份分红（元）
                "CFBL",
                "FHFCBZ",
                "CFLX",
                "FFR",// 分红发放日
                "FH",
                "DTYPE"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(fhsp, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table.column("FSRQ").setName("date");

        return table;
    }

    public static Table fundStocks(String symbol) {
        // String url="https://fundmobapi.eastmoney.com/FundMNewApi/FundMNInverstPosition";

        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode stocks = jjcc.get("InverstPosition").get("fundStocks");

        List<String> columnNames = Arrays.asList(
                "GPDM",// 股票代码
                "GPJC",// 股票名称
                "JZBL",// 持仓占比(%)
                "TEXCH",
                "ISINVISBL",
                "PCTNVCHGTYPE",// 增持｜减持｜新增
                "PCTNVCHG",// 较上期增减比例（%）
                "NEWTEXCH",
                "INDEXCODE",
                "INDEXNAME"// 股票行业
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(stocks, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table fundBoods(String symbol) {
        // String url="https://fundmobapi.eastmoney.com/FundMNewApi/FundMNInverstPosition";

        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode boods = jjcc.get("InverstPosition").get("fundboods");

        List<String> columnNames = Arrays.asList(
                "ZQDM",// 债券代码
                "ZQMC",// 债券名称
                "ZJZBL",// 持仓占比（%）
                "ISBROKEN"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(boods, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table fundFofs(String symbol) {
        // String url="https://fundmobapi.eastmoney.com/FundMNewApi/FundMNInverstPosition";

        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode fofs = jjcc.get("InverstPosition").get("fundfofs");

        System.out.println(JsonUtils.format(fofs));

        // List<String> columnNames = Arrays.asList(
        //
        // );
        //
        // List<String[]> dataRows = JsonUtils.extractObjectData(fofs, columnNames);
        //
        // Table table = TableUtils.build(columnNames, dataRows);
        //
        // return table;
        return Table.create();
    }

    public static Table fundETF(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode inverstPosition = jjcc.get("InverstPosition");

        Map<String, Object> map = new HashMap<>();
        map.put("etf_code", inverstPosition.get("ETFCODE").asText());
        map.put("etf_name", inverstPosition.get("ETFSHORTNAME").asText());

        Table table = TableUtils.map2Table(map);
        return table;
    }

    public static Table fundPositionRatio(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNAssetAllocationNew";

        Map<String, Object> params = new HashMap<>();
        params.put("FCODE", symbol);
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.3.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode assetAllocation = JsonUtils.json(result, "Datas");

        List<String> columnNames = Arrays.asList(
                "FSRQ",//日期
                "JJ",//基金
                "GP",//股票占比（%）
                "ZQ",//债券占比（%）
                "HB",//现金占比（%）
                "QT",//其他
                "JZC"//净资产（亿）
        );

        Table table = null;

        List<String[]> dataRows = JsonUtils.extractObjectData(assetAllocation, columnNames);

        table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table fundIndustryComponent(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNSectorAllocation";

        Map<String, Object> params = new HashMap<>();
        params.put("FCODE", symbol);
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.3.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode sectorAllocation = JsonUtils.json(result, "Datas");

        List<String> columnNames = Arrays.asList(
                "HYMC",//行业名称
                "SZ",
                "ZJZBL",//占比（%）
                "FSRQ"//日期
        );

        Table table = null;

        List<String[]> dataRows = JsonUtils.extractObjectData(sectorAllocation, columnNames);

        table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    private static JsonNode fundBasic(String symbol) {
        String url = String.format("http://j5.dfcfw.com/sc/tfs/qt/v2.0.1/%s.json", symbol);

        Map<String, Object> params = new HashMap<>();
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode json = JsonUtils.json(result);
        return json;
    }

    public static Table etf() {
        String url = "http://fund.eastmoney.com/cnjy_dwjz.html";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", UA);

        try {
            Document doc = Jsoup.connect(url).headers(headers).get();

            int cursor = 0;
            List<String> columnNames = new ArrayList<>();
            List<String[]> dataRows = new ArrayList<>();

            Elements tables = doc.select("table");
            Element table = tables.get(1);
            Elements rows = table.select("tr");

            // header
            /* 这个自动类型推到列存在问题，手动指明此列的类型 */
            String codeColumnName = rows.get(0).select("td").get(3).text();
            columnNames.add(codeColumnName);
            columnNames.add(rows.get(0).select("td").get(4).text());
            columnNames.add(rows.get(0).select("td").get(5).text());
            String today = rows.get(0).select("td").get(6).text();
            columnNames.add(today + rows.get(1).select("td").get(0).text());
            columnNames.add(today + rows.get(1).select("td").get(1).text());
            String yesterday = rows.get(0).select("td").get(7).text();
            columnNames.add(yesterday + rows.get(1).select("td").get(2).text());
            columnNames.add(yesterday + rows.get(1).select("td").get(3).text());
            columnNames.add(rows.get(0).select("td").get(8).text());
            columnNames.add(rows.get(0).select("td").get(9).text());
            columnNames.add(rows.get(0).select("td").get(10).text());
            columnNames.add(rows.get(0).select("td").get(11).text());

            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cursor > 1) {
                    String[] dataRow = new String[13];
                    for (int i = 0; i < 11; i++) {
                        if (i + 3 == 4) {
                            dataRow[i] = cells.get(i + 3).selectFirst("a").text();
                        } else {
                            dataRow[i] = cells.get(i + 3).text();
                        }
                    }
                    dataRows.add(dataRow);
                }
                cursor++;
            }

            Map<String, ColumnType> columnTypeByName = new HashMap<>();
            columnTypeByName.put(codeColumnName, ColumnType.STRING);
            Table t = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().columnTypesPartial(columnTypeByName).build());
            return t;
        } catch (IOException e) {
            log.error("解析异常", e);
            throw new RuntimeException(e);
        }
    }

    // 估值
    public static Table estimation() {
        String url = "http://api.fund.eastmoney.com/FundGuZhi/GetFundGZList";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", UA);
        headers.put("Referer", "http://fund.eastmoney.com/");

//        Map<String, Integer> symbolMap = new HashMap<>();
//        symbolMap.put("全部", 1);
//        symbolMap.put("股票型", 2);
//        symbolMap.put("混合型", 3);
//        symbolMap.put("债券型", 4);
//        symbolMap.put("指数型", 5);
//        symbolMap.put("QDII", 6);
//        symbolMap.put("ETF联接", 7);
//        symbolMap.put("LOF", 8);
//        symbolMap.put("场内交易基金", 9);

        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        params.put("sort", "3");
        params.put("orderType", "desc");
        params.put("canbuy", "0");
        params.put("pageIndex", "1");
        params.put("pageSize", "20000");
        params.put("_", String.valueOf(System.currentTimeMillis()));

        try {
            String result = HttpRequest.get(url, params).headers(headers).body();
            JsonNode json = JsonUtils.json(result);

            String valueDay = json.get("Data").get("gzrq").asText();
            String calDay = json.get("Data").get("gxrq").asText();

            JsonNode data = json.get("Data").get("list");
            List<String[]> dataRows = new ArrayList<>();
            for (JsonNode item : data) {
                String[] row = new String[item.size()];

                if (item.isArray()) {
                    for (int i = 0, len = item.size(); i < len; i++) {
                        JsonNode e = item.get(i);
                        String value = null;
                        if (e != null && !"---".equals(e.asText())) {
                            value = e.asText();
                        }
                        row[i] = value;
                    }
                } else {
                    int cursor = 0;
                    Iterator<JsonNode> iterator = item.elements();
                    while (iterator.hasNext()) {
                        JsonNode e = iterator.next();
                        String value = null;
                        if (e != null && !"---".equals(e.asText())) {
                            value = e.asText();
                        }
                        row[cursor++] = value;
                    }
                }
                dataRows.add(row);
            }

            List<String> columnNames = Arrays.asList(
                    "code",
                    "c1",
                    "c2",
                    "c3",
                    "c4",
                    "c5",
                    "type",
                    "c6",
                    "c7",
                    "c8",
                    "c9",
                    "date",
                    "c10",
                    "c11",
                    "c12",
                    "c13",
                    "c14",
                    "c15",
                    "c16",
                    "估算偏差",
                    calDay + "-估算数据-估算值",
                    calDay + "-估算数据-估算增长率",
                    calDay + "-公布数据-日增长率",
                    valueDay + "-单位净值",
                    calDay + "-公布数据-单位净值",
                    "c17",
                    "name",
                    "c18",
                    "c19",
                    "c20"
            );

            Table table = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().columnTypesPartial(new TableUtils.FundColumnType()).build());
            table = table.select(
                    "code",
                    "name",
                    "type",
                    "date",
                    "估算偏差",
                    calDay + "-估算数据-估算值",
                    calDay + "-估算数据-估算增长率",
                    calDay + "-公布数据-日增长率",
                    valueDay + "-单位净值",
                    calDay + "-公布数据-单位净值"
            );
            return table;
        } catch (Exception e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

    public static Table companies() {
        String url = "http://fund.eastmoney.com/Data/FundRankScale.aspx";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", UA);

        Map<String, String> params = new HashMap<>();
        params.put("_", String.valueOf(System.currentTimeMillis()));

        try {
            String result = HttpRequest.get(url, params).headers(headers).body();
            String json = result.substring("var json=".length());

            List<String> columnNames = ColumnUtils.transform(
                    "uid,company_name,company_fund_date,manager_crew_num,company_boss_name,company_pinyin,others1,manage_scale,company_rank,company_short_name,others2,update_time".split(",")
            );

            JsonNode node = JsonUtils.json(json, "datas");
            List<String[]> dataRows = JsonUtils.extractArrayData(node);

            Table table = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.empty());
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    /**
//     * 不可用
//     *
//     * @param symbol
//     * @return
//     */
//    @Deprecated
//    public static Table historyIndex(String symbol) {
//        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get";
//
//        String symbol2;
//        if (symbol.startsWith("sz")) {
//            symbol2 = "0" + symbol.substring(2);
//        } else if (symbol.startsWith("sh")) {
//            symbol2 = "1" + symbol.substring(2);
//        } else {
//            symbol2 = "1" + symbol;
//        }
//
//        Map<String, String> params = new HashMap<>();
//        params.put("cb", "jQuery1124033485574041163946_1596700547000");
//        params.put("secid", symbol2);
//        params.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
//        params.put("fields1", "f1,f2,f3,f4,f5");
//        params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58");
//        params.put("klt", "101");
//        params.put("fqt", "0");
//        params.put("beg", "19900101");
//        params.put("end", "20991231");
//        params.put("_", "1596700547039"/*String.valueOf(System.currentTimeMillis())*/);
//
//        String result = HttpRequest.get(url, params).body();
//        System.out.println(result);
//
//        return Table.create();
//    }

    /**
     * @param symbols 例如：1.000300
     * @return
     */
    public static Table test(String... symbols) {
        String url = "https://push2.eastmoney.com/api/qt/ulist.np/get";

        Map<String, String> params = new HashMap<>();
        params.put("fltt", "2");
        // 指定字段
        // params.put("fields","f3");
        params.put("secids", String.join(",", symbols));
        params.put("_", String.valueOf(System.currentTimeMillis()));

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        columnNames.set(columnNames.indexOf("f12"), "code");

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

//    /**
//     * 看数据计算到 2020
//     * @param symbol 示例：000300
//     * @return
//     */
//    @Deprecated
//    public static Table test3(String symbol) {
//        String url = "https://fundmobapi.eastmoney.com/FundMApi/FundYieldDiagramNew.ashx";
//
//        Map<String, Object> params = new LinkedHashMap<>();
//        params.put("FCODE", symbol);
//        // y：月；3y：季；6y：半年；n：一年；3n：三年；5n：五年
//        params.put("RANGE", "5n");
//        params.put("deviceid", "Wap");
//        params.put("plat", "Wap");
//        params.put("product", "EFund");
//        params.put("version", "2.0.0");
//        params.put("_", System.currentTimeMillis());
//
//        String result = HttpRequest.get(url, params).body();
//        JsonNode json = JsonUtils.json(result, "Datas");
//
//        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
//        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
//
//        Table table = TableUtils.build(columnNames, dataRows);
//        return table;
//    }

//    /**
//     * 同 fundNetHistory
//     *
//     * @param symbol
//     * @return
//     */
//    @Deprecated
//    public static Table fundNetHistoryOld(String symbol) {
//        String url = "https://fundmobapi.eastmoney.com/FundMApi/FundNetDiagram.ashx";
//
//        Map<String, Object> params = new LinkedHashMap<>();
//        params.put("FCODE", symbol);
//        // y：月；3y：季；6y：半年；n：一年；3n：三年；5n：五年
//        params.put("RANGE", "5n");
//        params.put("deviceid", "Wap");
//        params.put("plat", "Wap");
//        params.put("product", "EFund");
//        params.put("version", "2.0.0");
//        params.put("_", System.currentTimeMillis());
//
//        String result = HttpRequest.get(url, params).body();
//        JsonNode json = JsonUtils.json(result, "Datas");
//
//        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
//        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
//
//        Table table = TableUtils.build(columnNames, dataRows);
//        return table;
//    }

//    /**
//     * 未完全解析，推荐使用 fundNew
//     *
//     * @param symbol
//     *
//     * @return
//     */
//    public static Table fundInfo(String symbol) {
//        String url = "https://fundmobapi.eastmoney.com/FundMApi/FundBaseTypeInformation.ashx";
//
//        Map<String, Object> params = new LinkedHashMap<>();
//        params.put("FCODE", symbol);
//        params.put("deviceid", "Wap");
//        params.put("plat", "Wap");
//        params.put("product", "EFund");
//        params.put("version", "2.0.0");
//        params.put("_", System.currentTimeMillis());
//
//        String result = HttpRequest.get(url, params).body();
//        JsonNode json = JsonUtils.json(result, "Datas");
//
//        Map<String, ?> map = JsonUtils.map(json);
//
//        Table table = TableUtils.map2Table(map);
//
//        return table;
//    }

    public static Table test6(String symbol) {
        String url = "https://push2.eastmoney.com/api/qt/stock/trends2/get";

        Map<String, Object> params = new HashMap<>();
        // 1.000300
        params.put("secid", symbol);
        params.put("fields1", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13");
        params.put("fields2", "f51,f53,f56,f58");
        params.put("iscr", 0);
        params.put("iscca", 0);
        params.put("ndays", 1);
        params.put("forcect", 1);

        String result = HttpRequest.get(url, params).body();
        System.out.println(result);

        return Table.create();
    }

    /**
     * @param symbol
     * @return Structure of
     * Index  |       Column Name       |  Column Type  |
     * ---------------------------------------------------
     * 0  |                   date  |   LOCAL_DATE  |
     * 1  |              net_worth  |       DOUBLE  |
     * 2  |  accumulated_net_worth  |       DOUBLE  |
     * 3  |                 change  |       STRING  |
     */
    public static Table fundNetHistory(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNHisNetList";

        Map<String, Object> params = new HashMap<>();
        params.put("FCODE", symbol);
        params.put("IsShareNet", true);
        params.put("pageIndex", 1);
        params.put("pageSize", LocalDate.of(1990, 1, 1).until(LocalDate.now(), ChronoUnit.DAYS));
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.2.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params)
                .userAgent(UA)
                //.accept("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                //.acceptEncoding("gzip, deflate, br")
                //.header("Host", "fundmobapi.eastmoney.com")
                .body();

        JsonNode json = JsonUtils.json(result).get("Datas");

        List<String> columnNames = Arrays.asList(
                "FSRQ",
                "DWJZ",
                "LJJZ",
                "JZZZL"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table.column("FSRQ").setName("date");
        table.column("DWJZ").setName("net_worth");
        table.column("LJJZ").setName("accumulated_net_worth");
        table.column("JZZZL").setName("change");

        return table;
    }

    public static Table fundRealNet(String... symbols) {
        if (null == symbols || symbols.length < 1) {
            throw new IllegalArgumentException("at least one fund");
        }

        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNFInfo";

        Map<String, Object> params = new HashMap<>();
        params.put("Fcodes", String.join(",", symbols));
        params.put("pageIndex", 1);
        params.put("pageSize", symbols.length);
        // params.put("Sort", "");
        // params.put("SortColumn", "");
        params.put("IsShowSE", false);
        // params.put("P", "F");
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.2.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode json = JsonUtils.json(result, "Datas");

        List<String> columnNames = Arrays.asList(
                "FCODE",
                "SHORTNAME",
                "PDATE",
                "NAV",
                "ACCNAV",
                "NAVCHGRT",
                "GSZ",
                "GSZZL",
                "GZTIME"
//                ,
//                "NEWPRICE",
//                "CHANGERATIO",
//                "ZJL",
//                "HQDATE",
//                "ISHAVEREDPACKET"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table.column("FCODE").setName("code");
        table.column("SHORTNAME").setName("name");
        // 上一个交易日的值
        table.column("PDATE").setName("latest_date");
        table.column("NAV").setName("latest_net_worth");
        table.column("ACCNAV").setName("latest_accumulated_net_worth");
        table.column("NAVCHGRT").setName("latest_change");
        // 预估值
        table.column("GSZ").setName("estimate_net_worth");
        table.column("GSZZL").setName("estimate_change");
        table.column("GZTIME").setName("estimate_date");
        //table.column("").setName("");

        return table;
    }

    public static Table industry() {
        String url = "http://push2.eastmoney.com/api/qt/clist/get";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("pn", 1);
        params.put("pz", 500);
        params.put("po", 1);
        params.put("np", 1);
        params.put("fields", "f12,f13,f14,f62");
        params.put("fid", "f62");
        params.put("fs", "m:90+t:2");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table test11(String symbol) {
        try {
            String url = String.format("http://fund.eastmoney.com/pingzhongdata/%s.js", symbol);
            String result = HttpRequest.get(url).userAgent(UA).body();

            ScriptEngine engine = ScriptUtils.JSEngine();
            engine.eval(result);

            Map<String, Object> map = new HashMap<>();
            map.put("code", engine.get("fS_code"));
            map.put("name", engine.get("fS_name"));
            map.put("original_rate", engine.get("fund_sourceRate"));
            map.put("rate", engine.get("fund_Rate"));
            map.put("min_purchase_amount", engine.get("fund_minsg"));

            List stockCodes = ScriptUtils.array("stockCodes");
            map.put("stocks", stockCodes);

            // ScriptObjectMirror var2=(ScriptObjectMirror) engine.get("Data_performanceEvaluation");
            // map.put("var",ScriptEngineUtils.getObject(var2));

            return TableUtils.map2Table(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Table fundsFenHong() {
        ScriptEngine engine = ScriptUtils.JSEngine();

        List<String> columnNames = Arrays.asList(
                "code",
                "name",
                "quanyidengjiri",
                "chuxiriqi",
                "fenhong",
                "fenhongfafangri",
                "c1"
        );

        try {
            String url = "http://fund.eastmoney.com/Data/funddataIndex_Interface.aspx";

            Map<String, Object> params = new HashMap<>();
            params.put("dt", "8");
            params.put("page", "1");
            params.put("rank", "DJR");
            params.put("sort", "desc");
            params.put("gs", "");
            params.put("ftype", "");
            params.put("year", "");

            String result = HttpRequest.get(url, params).body();
            engine.eval(result);

            List pageinfo = ScriptUtils.array(engine.get("pageinfo"));
            int totalPage = (int) pageinfo.get(0);

            Table table = null;
            for (int j = 1; j < totalPage; j++) {
                params.put("page", j);

                result = HttpRequest.get(url, params).body();
                engine.eval(result);

                List<String[]> dataRows = new ArrayList<>();
                List jjfh = ScriptUtils.array(engine.get("jjfh_data"));
                for (Object item : jjfh) {
                    List lst = (List) item;
                    String[] row = new String[lst.size()];
                    for (int i = 0, len = lst.size(); i < len; i++) {
                        row[i] = (String) lst.get(i);
                    }
                    dataRows.add(row);
                }
                Table t2 = TableUtils.build(columnNames, dataRows);

                if (null == table) {
                    table = t2;
                } else {
                    table = table.append(t2);
                }
            }
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Table test13() {
        ScriptEngine engine = ScriptUtils.JSEngine();

        List<String> columnNames = ColumnUtils.transform(
                "code",
                "name",
                "chaifenzhesuanri",
                "chaifenleixing",
                "chaifenzhesuan",
                "_"
        );

        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put("code", ColumnType.STRING);
        columnTypeMap.put("name", ColumnType.STRING);
        columnTypeMap.put("chaifenzhesuanri", ColumnType.LOCAL_DATE);
        columnTypeMap.put("chaifenleixing", ColumnType.STRING);
        columnTypeMap.put("chaifenzhesuan", ColumnType.STRING);

        try {
            String url = "http://fund.eastmoney.com/Data/funddataIndex_Interface.aspx";

            Map<String, Object> params = new HashMap<>();
            params.put("dt", "9");
            params.put("page", "1");
            params.put("rank", "FSRQ");
            params.put("sort", "desc");
            params.put("gs", "");
            params.put("ftype", "");
            params.put("year", "");

            String result = HttpRequest.get(url, params).body();
            engine.eval(result);

            List pageinfo = ScriptUtils.array(engine.get("pageinfo"));
            int totalPage = (int) pageinfo.get(0);

            Table table = null;
            for (int j = 1; j < totalPage; j++) {
                params.put("page", j);

                result = HttpRequest.get(url, params).body();
                engine.eval(result);

                List<String[]> dataRows = new ArrayList<>();
                List jjfh = ScriptUtils.array(engine.get("jjcf_data"));
                for (Object item : jjfh) {
                    List lst = (List) item;
                    String[] row = new String[lst.size()];
                    for (int i = 0, len = lst.size(); i < len; i++) {
                        row[i] = String.valueOf(lst.get(i));
                    }
                    dataRows.add(row);
                }

                Table t2 = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.columnTypeByName(columnTypeMap));

                if (null == table) {
                    table = t2;
                } else {
                    table = table.append(t2);
                }
            }
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Table test14() {
        ScriptEngine engine = ScriptUtils.JSEngine();

        List<String> columnNames = ColumnUtils.transform(
                "code",
                "name",
                "leijifenhong",
                "leijicishu",
                "chengliriqi",
                "_"
        );

        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put("code", ColumnType.STRING);
        columnTypeMap.put("name", ColumnType.STRING);
//        columnTypeMap.put("chaifenzhesuanri", ColumnType.LOCAL_DATE);
//        columnTypeMap.put("chaifenleixing", ColumnType.STRING);
//        columnTypeMap.put("chaifenzhesuan", ColumnType.STRING);

        try {
            String url = "http://fund.eastmoney.com/Data/funddataIndex_Interface.aspx";

            Map<String, Object> params = new HashMap<>();
            params.put("dt", "10");
            params.put("page", "1");
            params.put("rank", "FHFCZ");
            params.put("sort", "desc");
            params.put("gs", "");
            params.put("ftype", "");
            params.put("year", "");

            String result = HttpRequest.get(url, params).body();
            engine.eval(result);

            List pageinfo = ScriptUtils.array(engine.get("pageinfo"));
            int totalPage = (int) pageinfo.get(0);

            Table table = null;
            for (int j = 1; j < totalPage; j++) {
                params.put("page", j);

                result = HttpRequest.get(url, params).body();
                engine.eval(result);

                List<String[]> dataRows = new ArrayList<>();
                List jjfh = ScriptUtils.array(engine.get("fhph_data"));
                for (Object item : jjfh) {
                    List lst = (List) item;
                    String[] row = new String[lst.size()];
                    for (int i = 0, len = lst.size(); i < len; i++) {
                        row[i] = String.valueOf(lst.get(i));
                    }
                    dataRows.add(row);
                }

                Table t2 = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.columnTypeByName(columnTypeMap));

                if (null == table) {
                    table = t2;
                } else {
                    table = table.append(t2);
                }
            }
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Table realNet(String symbol) {
        String url = String.format("http://fundgz.1234567.com.cn/js/%s.js", symbol);

        Map<String, Object> params = new HashMap<>();
        params.put("rt", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        result = result.substring("jsonpgz".length() + 1, result.length() - 2);
        JsonNode json = JsonUtils.json(result);

        Table table = TableUtils.json2Table(json);
        return table;
    }

    public static void main(String[] args) throws Exception {
        Table t = industry();
        System.out.println(t.print());
        System.out.println(t.structure().printAll());
        System.out.println(t.shape());

    }
}
