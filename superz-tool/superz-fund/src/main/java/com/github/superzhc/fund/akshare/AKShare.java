package com.github.superzhc.fund.akshare;

import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/3/29 9:31
 **/
public class AKShare {
    private static final String TUSHARE_TOKEN_PARAM_NAME = "TUSHARE_TOKEN";

    /**
     * @return Structure of
     * Index  |  Column Name  |  Column Type  |
     * -----------------------------------------
     * 0  |      ts_code  |       STRING  |
     * 1  |         name  |       STRING  |
     * 2  |     fullname  |       STRING  |
     * 3  |       market  |       STRING  |
     * 4  |    publisher  |       STRING  |
     * 5  |   index_type  |       STRING  |
     * 6  |     category  |       STRING  |
     * 7  |    base_date  |   LOCAL_DATE  |
     * 8  |   base_point  |       DOUBLE  |
     * 9  |    list_date  |   LOCAL_DATE  |
     * 10  |  weight_rule  |       STRING  |
     * 11  |         desc  |       STRING  |
     * 12  |     exp_date  |   LOCAL_DATE  |
     */
    public static Table indics() {
        return indics(null);
    }

    public static Table indics(String name) {
        String token = System.getProperty(TUSHARE_TOKEN_PARAM_NAME);
        if (null == token || token.trim().length() == 0) {
            token = System.getenv(TUSHARE_TOKEN_PARAM_NAME);
        }
        if (null == token || token.trim().length() == 0) {
            throw new RuntimeException("环境变量 " + TUSHARE_TOKEN_PARAM_NAME + " 未配置");
        }

        TusharePro ts = new TusharePro(token);
        Table table = ts.indexBasic(null, name, null, null, null);
        table = table.where(
                table.stringColumn("market")
                        //.isNotIn("OTH", "MSCI", "CICC", "SW", "NH", "CNI")
                        .isIn("SSE", "SZSE", "CSI")
        );
        return table;
    }

    public static Table index(String symbol) {
        Table table = JiuCaiShuo.indexInfo(symbol);
        return table;
    }

    public static Table trackIndex(String symbol) {
        Table table = JiuCaiShuo.indexTrack(symbol);
        return table;
    }

    public static Table fund(String symbol) {
        Table table = EastMoney.fund(symbol);
        return table;
    }

    public static void main(String[] args) {
        Table table = fund("501009");
        String indexName = table.getString(0, "track_index");
        Table t2 = indics(indexName);
        String symbol = t2.getString(0, "ts_code");
        Table t3 = index(symbol);

//        System.out.println(table.printAll());
//        System.out.println(table.structure().printAll());
//        System.out.println(t2.printAll());
        System.out.println(t3.printAll());
    }
}