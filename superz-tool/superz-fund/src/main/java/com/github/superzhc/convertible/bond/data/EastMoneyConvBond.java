package com.github.superzhc.convertible.bond.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.fund.common.HttpConstant;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.ReadOptionsUtils;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/4/28 19:13
 **/
public class EastMoneyConvBond {
    private static final Logger log = LoggerFactory.getLogger(EastMoneyConvBond.class);

    public static Table convertibleBond(String symbol) {
        String url = String.format("https://data.eastmoney.com/kzz/detail/%s.html", symbol);

        String result = HttpRequest.get(url).userAgent(HttpConstant.UA).body();

        Document doc = Jsoup.parse(result);
        /**
         * soup = BeautifulSoup(r.text, "lxml")
         *         data_text = soup.find("script").string.strip()
         *         data_json = json.loads(re.findall("var info= (.*)", data_text)[0][:-1])
         *         temp_df = pd.json_normalize(data_json)
         */
        String text = doc.select("script").html().trim();
        Pattern pattern = Pattern.compile("var info= (.*);");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String data = matcher.group(1);
            JsonNode json = JsonUtils.json(data);
            Table table = TableUtils.json2Table(json);
            return table;
        }

        return Table.create();
    }

    public static Table convertibleBonds() {
        String url = "https://datacenter-web.eastmoney.com/api/data/v1/get";

        Map<String, Object> params = new HashMap<>();
        params.put("sortColumns", "PUBLIC_START_DATE");
        params.put("sortTypes", -1);
        params.put("pageSize", 1000);
        params.put("pageNumber", 1);
        params.put("reportName", "RPT_BOND_CB_LIST");
        params.put("columns", "ALL");
        params.put("quoteColumns", "f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE,f235~10~SECURITY_CODE~TRANSFER_PRICE,f236~10~SECURITY_CODE~TRANSFER_VALUE,f2~10~SECURITY_CODE~CURRENT_BOND_PRICE,f237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO,f239~10~SECURITY_CODE~RESALE_TRIG_PRICE,f240~10~SECURITY_CODE~REDEEM_TRIG_PRICE,f23~01~CONVERT_STOCK_CODE~PBV_RATIO");
        params.put("source", "WEB");
        params.put("client", "WEB");

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "result");
        int total = json.get("pages").asInt(1);

        JsonNode data = json.get("data");
        List<String> columnNames = JsonUtils.extractObjectColumnName(data);
        Map<String, ColumnType> cts = new HashMap<>();
        cts.put("PBV_RATIO", ColumnType.STRING);

        List<String[]> dataRows = JsonUtils.extractObjectData(data, columnNames);

        Table table = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.columnTypeByName(cts));

        Map<String, ColumnType> columnNamesWithType = new HashMap<>();
        for (String columnName : columnNames) {
            columnNamesWithType.put(columnName, table.column(columnName).type());
        }

        int page = 2;
        while (page <= total) {
            params.put("pageNumber", page);
            String str = HttpRequest.get(url, params).body();
            JsonNode dataJson = JsonUtils.json(str, "result", "data");
            List<String[]> drs = JsonUtils.extractObjectData(dataJson, columnNames);

            Table t2 = TableBuildingUtils.build(columnNames, drs, ReadOptionsUtils.columnTypeByName(columnNamesWithType));
            table.append(t2);

            page++;
        }

        return table;
    }

    public static Table convertibleBondsComparison() {
        String url = "http://16.push2.eastmoney.com/api/qt/clist/get";

        Map<String, Object> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "5000");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f243");
        params.put("fs", "b:MK0354");
        params.put("fields", "f1,f152,f2,f3,f12,f13,f14,f227,f228,f229,f230,f231,f232,f233,f234,f235,f236,f237,f238,f239,f240,f241,f242,f26,f243");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table;

//        table = convertibleBonds();

//        table = convertibleBondsComparison();

        table = convertibleBond("110059");

        System.out.println(table.print());
        System.out.println(table.structure().printAll());
        TableUtils.write2Html(table);
    }
}
