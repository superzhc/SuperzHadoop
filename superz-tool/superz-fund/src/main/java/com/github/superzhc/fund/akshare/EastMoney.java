package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.tablesaw.read.EmptyReadOptions;
import com.github.superzhc.fund.tablesaw.utils.ColumnUtils;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.ReadOptionsUtils;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
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

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/3/29 10:04
 **/
public class EastMoney {
    private static final Logger log = LoggerFactory.getLogger(EastMoney.class);

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36";

    public static Table funds() {
        String url = "http://fund.eastmoney.com/js/fundcode_search.js";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);

        try {
            String result = HttpRequest.get(url).headers(headers).body();
            String json = result.substring("var r = ".length(), result.length() - 1);
            JsonNode nodes = JsonUtils.json(json);//mapper.readTree(json);

            List<String> columnNames = Arrays.asList("基金代码", "拼音缩写", "基金简称", "基金类型", "拼音全称");

            List<String[]> dataRows = new ArrayList<>();
            for (JsonNode node : nodes) {
                String[] row = new String[node.size()];
                for (int i = 0, len = node.size(); i < len; i++) {
                    row[i] = node.get(i).asText();
                }
                dataRows.add(row);
            }

            Table table = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().build());
            return table;
        } catch (Exception e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

    public static Table openFundDaily() {
        String url = "http://fund.eastmoney.com/Data/Fund_JJJZ_Data.aspx";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);

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

//    // 接口已不存在
//    public static Table openFundInfo(String fundCode){
//        String url=String.format("http://fund.eastmoney.com/pingzhongdata/%s.js",fundCode);
//
//        Map<String,String> headers=new HashMap<>();
//        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
//
//        String result=HttpRequest.get(url).headers(headers).body();
//        System.out.println(result);
//
//        return Table.create();
//    }

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

    public static Table fundNew(String symbol) {
        Map<String, Object> map = new LinkedHashMap<>();

        JsonNode json = fundBasic(symbol);

        JsonNode jjxq = json.get("JJXQ").get("Datas");
        map.put("code", jjxq.get("FCODE").asText());
        map.put("name", jjxq.get("SHORTNAME").asText());
        map.put("type", jjxq.get("FTYPE").asText());
        map.put("established", jjxq.get("ESTABDATE").asText());
        map.put("index_code", jjxq.get("INDEXCODE").asText());
        map.put("index_name", jjxq.get("INDEXNAME").asText());
        map.put("rate", jjxq.get("RLEVEL_SZ").asText());
        map.put("risk_level", jjxq.get("RISKLEVEL").asText());
        map.put("bench", jjxq.get("BENCH").asText());
        map.put("资产规模（元）", jjxq.get("ENDNAV").asText());
        map.put("规模截止日期", jjxq.get("FEGMRQ").asText());
//        map.put("日涨幅 (%)", jjxq.get("RZDF").asText());
        map.put("单位净值", jjxq.get("DWJZ").asText());
        map.put("累计净值", jjxq.get("LJJZ").asText());
//        map.put("当日确认份额时间点", jjxq.get("CURRENTDAYMARK").asText());
//        map.put("购买起点（元）", jjxq.get("MINSG").asText());
//        map.put("首次购买（元）", jjxq.get("MINSBRG").asText());
//        map.put("追加购买（元）", jjxq.get("MINSBSG").asText());
//        map.put("定投起点（元）", jjxq.get("MINDT").asText());
//        map.put("单日累计购买上限（元）", jjxq.get("MAXSG").asText());
        map.put("申购状态", jjxq.get("SGZT").asText());
//        map.put("卖出状态", jjxq.get("SHZT").asText());
//        map.put("定投状态", jjxq.get("DTZT").asText());
//        map.put("原始购买费率", jjxq.get("SOURCERATE").asText());
//        map.put("实际购买费率", jjxq.get("RATE").asText());
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

    public static Table fundSummarize(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode jdzf = json.get("JDZF").get("Datas");

        List<String> columnNames = Arrays.asList(
                "title",
                "syl",// 收益率
                "avg",// 涨跌幅
                "hs300",// 同类平均
                "rank",// 同类排名
                "sc",// 排名总数
                "diff"
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
        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode stocks = jjcc.get("InverstPosition").get("fundStocks");

        List<String> columnNames = Arrays.asList(
                "GPDM",
                "GPJC",
                "JZBL",
                "TEXCH",
                "ISINVISBL",
                "PCTNVCHGTYPE",
                "PCTNVCHG",
                "NEWTEXCH",
                "INDEXCODE",
                "INDEXNAME"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(stocks, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table fundBoods(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode boods = jjcc.get("InverstPosition").get("fundboods");

        List<String> columnNames = Arrays.asList(
                "ZQDM",
                "ZQMC",
                "ZJZBL",
                "ISBROKEN"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(boods, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table fundFofs(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode fofs = jjcc.get("InverstPosition").get("fundfofs");

        List<String> columnNames = Arrays.asList(

        );

        List<String[]> dataRows = JsonUtils.extractObjectData(fofs, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
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
        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode assetAllocation = jjcc.get("AssetAllocation");

        System.out.println(JsonUtils.format(assetAllocation));

        List<String> columnNames = Arrays.asList(
                "FSRQ",
                "JJ",
                "GP",
                "ZQ",
                "HB",
                "QT",
                "JZC"
        );

        Table table = null;

        Iterator<String> ss = assetAllocation.fieldNames();
        while (ss.hasNext()) {
            String s = ss.next();
            JsonNode node = assetAllocation.get(s);

            List<String[]> dataRows = JsonUtils.extractObjectData(node, columnNames);

            Table t = TableUtils.build(columnNames, dataRows);
            if (null == table) {
                table = t;
            } else {
                table = table.append(t);
            }
        }

        return table;
    }

    public static Table fundIndustryComponent(String symbol) {
        JsonNode json = fundBasic(symbol);
        JsonNode jjcc = json.get("JJCC").get("Datas");
        JsonNode sectorAllocation = jjcc.get("SectorAllocation");

        List<String> columnNames = Arrays.asList(
                "HYMC",
                "SZ",
                "ZJZBL",
                "FSRQ"
        );

        Table table = null;

        Iterator<String> ss = sectorAllocation.fieldNames();
        while (ss.hasNext()) {
            String s = ss.next();
            JsonNode node = sectorAllocation.get(s);

            List<String[]> dataRows = JsonUtils.extractObjectData(node, columnNames);

            Table t = TableUtils.build(columnNames, dataRows);
            if (null == table) {
                table = t;
            } else {
                table = table.append(t);
            }
        }

        return table;
    }

    private static JsonNode fundBasic(String symbol) {
        String url = String.format("http://j5.dfcfw.com/sc/tfs/qt/v2.0.1/%s.json", symbol);

        Map<String, Object> params = new HashMap<>();
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(USER_AGENT).body();
        JsonNode json = JsonUtils.json(result);
        return json;
    }

    public static Table etf() {
        String url = "http://fund.eastmoney.com/cnjy_dwjz.html";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);

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
        headers.put("User-Agent", USER_AGENT);
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
            JsonNode json = JsonUtils.json(result);//mapper.readTree(result);

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
                    "基金代码",
                    "c1",
                    "c2",
                    "c3",
                    "c4",
                    "c5",
                    "基金类型",
                    "c6",
                    "c7",
                    "c8",
                    "c9",
                    "估算日期",
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
                    "基金名称",
                    "c18",
                    "c19",
                    "c20"
            );

            Table table = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().columnTypesPartial(new TableUtils.FundColumnType()).build());
            table = table.select(
                    "基金代码",
                    "基金类型",
                    "估算日期",
                    "估算偏差",
                    calDay + "-估算数据-估算值",
                    calDay + "-估算数据-估算增长率",
                    calDay + "-公布数据-日增长率",
                    valueDay + "-单位净值",
                    calDay + "-公布数据-单位净值",
                    "基金名称"
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
        headers.put("User-Agent", USER_AGENT);

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

    public static Table test2(String... symbols) {
        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNFInfo";

        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", 1);
        params.put("pageSize", 200);
        params.put("plat", "Android");
        params.put("appType", "ttjj");
        params.put("product", "EFund");
        params.put("Version", "1");
        params.put("deviceid", generator("xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx"));
        params.put("Fcodes", String.join(",", symbols));

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "Datas");
        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    private static String generator(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            int r = ((int) (Math.random() * 16)) | 0;
            if (c == 'x') {
                sb.append(Integer.toHexString(r));
            } else if (c == 'y') {
                int r2 = (r & 0x3) | 0x8;
                sb.append(Integer.toHexString(r2));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * @param symbol 示例：000300
     * @return
     */
    public static Table test3(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMApi/FundYieldDiagramNew.ashx";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("FCODE", symbol);
        // y：月；3y：季；6y：半年；n：一年；3n：三年；5n：五年
        params.put("RANGE", "5n");
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "2.0.0");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "Datas");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static Table test4(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMApi/FundNetDiagram.ashx";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("FCODE", symbol);
        // y：月；3y：季；6y：半年；n：一年；3n：三年；5n：五年
        params.put("RANGE", "5n");
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "2.0.0");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "Datas");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static Table test5(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMApi/FundBaseTypeInformation.ashx";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("FCODE", symbol);
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "2.0.0");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "Datas");

        Map<String, ?> map = JsonUtils.map(json);
        Table table = TableUtils.map2Table(map);
        return table;
    }

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

    public static void main(String[] args) throws Exception {
//        Table table = fund("000001");//companies();

//        Table table = test("1.000300");

//        Table table=test2("000300","000905");

//        Table table = Table.create();
//
//        table = test6("1.000300");
//
//        System.out.println(table.print());
//        System.out.println(table.structure().printAll());

        Table t = fundETF("160119");
        System.out.println(t.printAll());
        System.out.println(t.shape());

    }
}
