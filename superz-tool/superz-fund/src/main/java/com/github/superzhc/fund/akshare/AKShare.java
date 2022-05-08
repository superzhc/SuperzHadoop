package com.github.superzhc.fund.akshare;

import com.github.superzhc.fund.data.fund.EastMoneyFund;
import tech.tablesaw.api.Table;
import tech.tablesaw.selection.Selection;

import static com.github.superzhc.fund.akshare.TusharePro.TUSHARE_TOKEN_PARAM_NAME;

/**
 * @author superz
 * @create 2022/3/29 9:31
 **/
public class AKShare {
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
     * @param text 目前支持名称、代码的模糊查询
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
    public static Table indics(String text) {
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

        if (null != text && text.trim().length() > 0) {
            // 名称模糊查询
            Selection childCondition = table.stringColumn("fullname").containsString(text);
            childCondition.or(table.stringColumn("name").containsString(text));

            // 代码模糊查询
            childCondition.or(table.stringColumn("ts_code").containsString(text));
            where.and(childCondition);
        }

        table = table.where(where);

        return table;
    }

    /**
     * @param symbol
     * @return
     */
    public static Table index(String symbol) {
        // 某些指数获取不到，不好用
        // Table table = JiuCaiShuo.indexInfo(symbol);

        String token = System.getProperty(TUSHARE_TOKEN_PARAM_NAME);
        if (null == token || token.trim().length() == 0) {
            token = System.getenv(TUSHARE_TOKEN_PARAM_NAME);
        }
        if (null == token || token.trim().length() == 0) {
            throw new RuntimeException("环境变量 " + TUSHARE_TOKEN_PARAM_NAME + " 未配置");
        }

        TusharePro ts = new TusharePro(token);
        Table table = ts.indexBasic(symbol, null, null, null, null);
        return table;
    }

    /**
     * @param symbol 示例 000001.SH
     * @return
     */
    public static Table indexTrack(String symbol) {
        Table table = JiuCaiShuo.indexTrack(symbol);
        return table;
    }

    /**
     * @param symbol 示例 000001.SH
     * @return
     */
    public static Table indexHistroy(String symbol) {
        return Table.create();
    }

    /**
     * @param symbol
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
        Table table = EastMoneyFund.fundNew(symbol);//EastMoney.fund(symbol);
        return table;
    }

    /**
     * @return Structure of
     * Index  |   Column Name   |  Column Type  |
     * -------------------------------------------
     * 0  |         symbol  |       STRING  |
     * 1  |           name  |       STRING  |
     * 2  |          trade  |       DOUBLE  |
     * 3  |    pricechange  |      INTEGER  |
     * 4  |  changepercent  |      INTEGER  |
     * 5  |            buy  |       DOUBLE  |
     * 6  |           sell  |       DOUBLE  |
     * 7  |     settlement  |       DOUBLE  |
     * 8  |           open  |      INTEGER  |
     * 9  |           high  |      INTEGER  |
     * 10  |            low  |      INTEGER  |
     * 11  |         volume  |      INTEGER  |
     * 12  |         amount  |      INTEGER  |
     * 13  |           code  |       STRING  |
     * 14  |       ticktime  |   LOCAL_TIME  |
     * 15  |          state  |      INTEGER  |
     * 16  |       statetxt  |       STRING  |
     */
    public static Table etf() {
        Table table = Sina.etf();
        return table;
    }

    public static Table lof() {
        Table table = Sina.lof();
        return table;
    }

    public static void main(String[] args) {
//        Table table = fund("012820");
//        System.out.println(table.print());
//
//        String indexName = table.getString(0, "track_index");
//
//        Table t2 = indics(indexName);
//        System.out.println(t2.print());

//        String symbol = t2.getString(0, "ts_code");
//        String symbol="000300.SH";
//        Table t3 = index(symbol);
//        System.out.println(t3.print());

        Table table = Table.create();
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}