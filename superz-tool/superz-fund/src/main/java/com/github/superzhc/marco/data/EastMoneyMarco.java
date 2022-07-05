package com.github.superzhc.marco.data;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/2 16:59
 **/
public class EastMoneyMarco {
    public static Table qyspjg() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("mkt", "9");
        params.put("p", 1);
        params.put("ps", 2000);
        params.put("_", System.currentTimeMillis());

        String[] columnNames = new String[]{
                "月份",
                "总指数-指数值",
                "总指数-同比增长",
                "总指数-环比增长",
                "农产品-指数值",
                "农产品-同比增长",
                "农产品-环比增长",
                "矿产品-指数值",
                "矿产品-同比增长",
                "矿产品-环比增长",
                "煤油电-指数值",
                "煤油电-同比增长",
                "煤油电-环比增长"
        };

        return EMDataCenter(params, "企业商品价格指数", columnNames);
    }

    /**
     * 获取季度国内生产总值数据
     *
     * @return
     */
    public static Table GDPQuarter() {
        String[] columns = new String[]{"季度", "国内生产总值 绝对值(亿元)", "国内生产总值 同比增长", "第一产业 绝对值(亿元)", "第一产业 同比增长", "第二产业 绝对值(亿元)", "第二产业 同比增长", "第三产业 绝对值(亿元)", "第三产业 同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "20");
        return EMDataCenter(params, "国内生产总值（季度）", columns);
    }

    /**
     * 获取居民消费价格指数数据（CPI）
     * <p>
     * 包括食品烟酒、衣着、生活永平和服务、医疗保健及个人用品、交通和通讯、娱乐和文化和居住等8大类262个子项目，但不包括股票、房产投资
     * <p>
     * CPI 上涨说明物价上涨，购买力下降
     *
     * @return
     */
    public static Table CPI() {
        String[] columns = new String[]{"月份", "全国当月", "全国同比增长", "全国环比增长", "全国累计", "城市当月", "城市同比增长", "城市环比增长", "城市累计", "农村当月", "农村同比增长", "农村环比增长", "农村累计"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "19");
        return EMDataCenter(params, "居民消费价格指数", columns);
    }

    /**
     * 获取工业品出厂价格指数数据
     * <p>
     * PPI反映的是生产环节的价格水平
     *
     * @return
     */
    public static Table PPI() {
        String[] columns = new String[]{"月份", "当月", "当月同比增长", "累计"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "22");
        return EMDataCenter(params, "工业品出厂价格指数", columns);
    }

    /**
     * 获取采购经理人指数(PMI)
     *
     * @return
     */
    public static Table PMI() {
        String[] columns = new String[]{"月份", "制造业指数", "制造业同比增长", "非制造业指数", "非制造业同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "21");
        return EMDataCenter(params, "PMI", columns);
    }

    /**
     * 获取存款准备金率数据
     *
     * @return
     */
    public static Table RRR() {
        String[] columns = new String[]{"公布时间", "生效时间", "大型金融机构 调整前", "大型金融机构 调整后", "大型金融机构 调整幅度", "中小型金融机构 调整前", "中小型金融机构 调整后", "中小型金融机构 调整幅度", "备注", "消息公布次日指数涨跌 上证", "消息公布次日指数涨跌 深证"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "23");
        return EMDataCenter(params, "存款准备金率", columns);
    }

    /**
     * 获取货币供应量数据
     *
     * @return
     */
    public static Table moneySupply() {
        String[] columns = new String[]{"月份", "货币和准货币(M2) 数量(亿元)", "货币和准货币(M2) 同比增长", "货币和准货币(M2) 环比增长", "货币(M1) 数量(亿元)", "货币(M1) 同比增长", "货币(M1) 环比增长", "流通中的现金(M0) 数量(亿元)", "流通中的现金(M0) 同比增长", "流通中的现金(M0) 环比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "11");
        return EMDataCenter(params, "货币供应量", columns);
    }

    /**
     * 获取外汇储备
     *
     * @return
     */
    public static Table goldAndForeignReserve() {
        String[] columns = new String[]{"月份", "国家外汇储备(亿美元) 数值", "国家外汇储备(亿美元) 同比", "国家外汇储备(亿美元) 环比", "黄金储备(万盎司) 数值", "黄金储备(万盎司) 同比", "黄金储备(万盎司) 环比"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "16");
        return EMDataCenter(params, "外汇储备", columns);
    }

    /**
     * 获取工业增加值增长
     *
     * @return
     */
    public static Table industrialGrowth() {
        String[] columns = new String[]{"月份", "_", "_"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "0");
        return EMDataCenter(params, "工业增加值增长", columns);
    }

    /**
     * 获取财政收入
     *
     * @return
     */
    public static Table fiscalRevenue() {
        String[] columns = new String[]{"月份", "当月(亿元)", "同比增长", "环比增长", "累计(亿元)", "同比增长2"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "14");
        return EMDataCenter(params, "财政收入", columns);
    }

    /**
     * 获取社会消费品零售总额
     *
     * @return
     */
    public static Table consumerTotal() {
        String[] columns = new String[]{"月份", "当月(亿元)", "同比增长", "环比增长", "累计(亿元)", "同比增长2"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "5");
        return EMDataCenter(params, "社会消费品零售总额", columns);
    }

    /**
     * 获取信贷数据
     *
     * @return
     */
    public static Table creditData() {
        String[] columns = new String[]{"月份", "当月(亿元)", "同比增长", "环比增长", "累计(亿元)", "同比增长2"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "7");
        return EMDataCenter(params, "信贷", columns);
    }

    /**
     * 获取外商直接投资数据(FDI)
     *
     * @return
     */
    public static Table fdiData() {
        String[] columns = new String[]{"月份", "当月(十万元)", "同比增长", "环比增长", "累计(十万元)", "同比增长2"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("mkt", "15");
        params.put("p", "1");
        params.put("ps", "2000");
        return EMDataCenter(params, "FDI", columns);
    }

    private static Table EMDataCenter(Map<String, ?> params, String tableName, String[] columns) {
        String url = "http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx";
        try {
            String result = HttpRequest.get(url, params).body();
            result = result.substring(1, result.length() - 1);
            String[] arr = JsonUtils.array(result);

            List<String[]> dataRows = new ArrayList<>();
            for (String row : arr) {
                String[] item = row.split(",", -1);
                dataRows.add(item);
            }

            Table table = TableUtils.build(columns, dataRows);
            table.setName(tableName);
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Table table = PPI();
        System.out.println(table.print());
    }
}
