package com.github.superzhc.fund.akshare;

import tech.tablesaw.api.Table;
import tech.tablesaw.selection.Selection;

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

    /**
     * @param name
     *
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
    public static Table indics(String name) {
        String token = System.getProperty(TUSHARE_TOKEN_PARAM_NAME);
        if (null == token || token.trim().length() == 0) {
            token = System.getenv(TUSHARE_TOKEN_PARAM_NAME);
        }
        if (null == token || token.trim().length() == 0) {
            throw new RuntimeException("环境变量 " + TUSHARE_TOKEN_PARAM_NAME + " 未配置");
        }

        TusharePro ts = new TusharePro(token);
        Table table = ts.indexBasic(null, null, null, null, null);
        Selection where = table.stringColumn("market")
                //.isNotIn("OTH", "MSCI", "CICC", "SW", "NH", "CNI")
                .isIn("SSE", "SZSE", "CSI");

        if (null != name && name.trim().length() > 0) {
            where.and(table.stringColumn("fullname").containsString(name));
        }

        table = table.where(where);

        return table;
    }

    /**
     * @param symbol
     *
     * @return
     */
    public static Table index(String symbol) {
        Table table = JiuCaiShuo.indexInfo(symbol);
        return table;
    }

    public static Table trackIndex(String symbol) {
        Table table = JiuCaiShuo.indexTrack(symbol);
        return table;
    }

    /**
     * 基金信息
     *
     * @param symbol
     *
     * @return Structure of null
     * Index  |          Column Name          |  Column Type  |
     * ---------------------------------------------------------
     * 0  |                         code  |       STRING  |
     * 1  |                    full_name  |       STRING  |
     * 2  |                         name  |       STRING  |
     * 3  |                         type  |       STRING  |
     * 4  |                   issue_date  |       STRING  |
     * 5  |                   found_date  |       STRING  |
     * 6  |                   asset_size  |       STRING  |
     * 7  |                   share_size  |       STRING  |
     * 8  |                      manager  |       STRING  |
     * 9  |                     dividend  |       STRING  |
     * 10  |                  track_index  |       STRING  |
     * 11  |         investment_objective  |       STRING  |
     * 12  |           investment_concept  |       STRING  |
     * 13  |             investment_scope  |       STRING  |
     * 14  |            investment_tatics  |       STRING  |
     * 15  |              dividend_policy  |       STRING  |
     * 16  |  risk_income_characteristics  |       STRING  |
     */
    public static Table fund(String symbol) {
        Table table = EastMoney.fund(symbol);
        return table;
    }

    public static void main(String[] args) {
        Table table = fund("501009");
        System.out.println(table.print());

        String indexName = table.getString(0, "track_index");

        Table t2 = indics(indexName);
        System.out.println(t2.print());

        String symbol = t2.getString(0, "ts_code");

        Table t3 = index(symbol);
        System.out.println(t3.print());
    }
}