package com.github.superzhc.fund.data.index;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.ColumnUtils;
import com.github.superzhc.tablesaw.utils.ReadOptionsUtils;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;
import tech.tablesaw.io.json.JsonReadOptions;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.util.*;

/**
 * @author superz
 * @create 2022/5/6 19:14
 **/
public class SinaIndex {
    public static Table indices() {
        // 获取总数
        String countUrl = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeStockCountSimple?node=hs_s";
        String cResult = HttpRequest.get(countUrl).body();
        //int total=Integer.valueOf(cResult.substring(1,cResult.length()-1));
        String total = cResult.substring(1, cResult.length() - 1);

        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeDataSimple";

        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        // 直接一次性将所有数据给读取出来
        params.put("num", /*"80"*/total);
        params.put("sort", "symbol");
        params.put("asc", "1");
        params.put("node", "hs_s");
        params.put("_s_r_a", "page");

        String result = HttpRequest.get(url, params).body();
        try {
            JsonReadOptions options = JsonReadOptions.builderFromString(result).columnTypesPartial(name -> Optional.ofNullable("code".equals(name) ? ColumnType.STRING : null)).build();
            Table table = Table.read().usingOptions(options);
            table = table.select("symbol", "name", "trade", "pricechange", "changepercent", "settlement", "open", "high", "low", "volume", "amount");
//            table.column("symbol").setName("代码");
//            table.column("name").setName("名称");
//            table.column("trade").setName("最新价");
//            table.column("pricechange").setName("涨跌额");
//            table.column("changepercent").setName("涨跌幅");
//            table.column("settlement").setName("昨开");
//            table.column("open").setName("今开");
//            table.column("high").setName("最高");
//            table.column("low").setName("最低");
//            table.column("volume").setName("成交量");
//            table.column("amount").setName("成交额");

            // 统一编码的规则
            StringColumn column = table.stringColumn("symbol").map(d -> {
                int length = d.length();
                String market = d.substring(0, length - 6).toUpperCase();
                String code = d.substring(length - 6);
                return String.format("%s.%s", code, market);
            });

            table.replaceColumn("symbol", column);

            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Table history(String symbol) {
        String[] ss = symbol.split("\\.");
        String sinaCode = ss[1].toLowerCase() + ss[0];

        String url = String.format("https://finance.sina.com.cn/realstock/company/%s/hisdata/klc_kl.js", sinaCode);

        try {
            String result = HttpRequest.get(url).body();
            String text = result.split("=")[1].split(";")[0].replace("\"", "");

            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval(new FileReader(SinaIndex.class.getClassLoader().getResource("js/hk_js_decode.js").getPath()));
            // 将引擎转换为Invocable，这样才可以调用js的方法
            Invocable invocable = (Invocable) engine;
            // 用 invocable.invokeFunction调用js脚本里的方法，第一個参数为方法名，后面的参数为被调用的js方法的入参
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) invocable.invokeFunction("d", text);

            List<String> columnNames = ColumnUtils.transform("date", "open", "high", "low", "close", "volume");
            List<String[]> dataRows = new ArrayList<>();
            if (scriptObjectMirror.isArray()) {
                for (Object item : scriptObjectMirror.values()) {
                    ScriptObjectMirror obj = (ScriptObjectMirror) item;

                    String[] row = new String[columnNames.size()];
                    for (int i = 0, len = columnNames.size(); i < len; i++) {
                        String columnName = columnNames.get(i);
                        if ("date".equals(columnName)) {
                            // 不能直接进行转换
//                            row[i] = ((ScriptObjectMirror) obj.get(columnName)).to(LocalDate.class).format(DEFAULT_DATETIMEFORMATTER);
                            Object o = ((ScriptObjectMirror) obj.get(columnName)).callMember("toLocaleDateString");
                            row[i] = String.valueOf(o);
                        } else {
                            row[i] = String.valueOf(obj.get(columnName));
                        }
                    }
                    dataRows.add(row);
                }
            }

            return TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.empty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
