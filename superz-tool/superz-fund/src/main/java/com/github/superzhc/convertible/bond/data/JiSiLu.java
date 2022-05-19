package com.github.superzhc.convertible.bond.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.HttpConstant;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.html.HtmlReadOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/27 19:10
 **/
public class JiSiLu {

    private static final Logger log = LoggerFactory.getLogger(JiSiLu.class);

    public static Table convertibleBond() {
        return convertibleBond(null);
    }

    public static Table convertibleBond(String cookie) {
        String url = "https://app.jisilu.cn/data/cbnew/cb_list_new/";

        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent", HttpConstant.UA);
        // 需要cookie，不然只显示一部分数据
        if (null != cookie && cookie.trim().length() > 0) {
            headers.put("Cookie", cookie);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%d", System.currentTimeMillis()));


        Map<String, Object> json = new HashMap<>();
        json.put("fprice", "");
        json.put("tprice", "");
        json.put("curr_iss_amt", "");
        json.put("volume", "");
        json.put("svolume", "");
        json.put("premium_rt", "");
        json.put("ytm_rt", "");
        json.put("market", "");
        json.put("rating_cd", "");
        json.put("is_search", "N");
        json.put("market_cd", new String[]{"shmb", "shkc", "szmb", "szcy"});
        json.put("btype", "");
        json.put("listed", "Y");
        json.put("qflag", "N");
        json.put("sw_cd", "");
        json.put("bond_ids", "");
        json.put("rp", "50");

        String result = HttpRequest.post(url, params).headers(headers).json(json).body();
        JsonNode node = JsonUtils.json(result, "rows");

        List<String> columnNames = JsonUtils.extractObjectColumnName(node, "cell");
        List<String[]> dataRows = JsonUtils.extractObjectData(node, columnNames, "cell");
        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    /**
     * @return Structure of
     * Index  |     Column Name      |  Column Type  |
     * ------------------------------------------------
     * 0  |               price  |       DOUBLE  |
     * 1  |         increase_rt  |       DOUBLE  |
     * 2  |              pma_rt  |       DOUBLE  |
     * 3  |                  pb  |       DOUBLE  |
     * 4  |          margin_flg  |       STRING  |
     * 5  |                 rid  |      INTEGER  |
     * 6  |            stock_id  |      INTEGER  |
     * 7  |            stock_nm  |       STRING  |
     * 8  |             bond_id  |      INTEGER  |
     * 9  |             bond_nm  |       STRING  |
     * 10  |              amount  |       DOUBLE  |
     * 11  |            b_shares  |      INTEGER  |
     * 12  |           pg_shares  |         TEXT  |
     * 13  |           cb_amount  |       DOUBLE  |
     * 14  |          ma20_price  |       DOUBLE  |
     * 15  |                naps  |       DOUBLE  |
     * 16  |       convert_price  |       DOUBLE  |
     * 17  |          apply_date  |   LOCAL_DATE  |
     * 18  |            apply_cd  |      INTEGER  |
     * 19  |           ration_cd  |      INTEGER  |
     * 20  |           record_dt  |   LOCAL_DATE  |
     * 21  |        record_price  |       DOUBLE  |
     * 22  |              ration  |       DOUBLE  |
     * 23  |           list_date  |   LOCAL_DATE  |
     * 24  |          list_price  |         TEXT  |
     * 25  |           status_cd  |       STRING  |
     * 26  |           ration_rt  |       DOUBLE  |
     * 27  |       online_amount  |       DOUBLE  |
     * 28  |       lucky_draw_rt  |       DOUBLE  |
     * 29  |    individual_limit  |      INTEGER  |
     * 30  |      underwriter_rt  |       DOUBLE  |
     * 31  |           rating_cd  |       STRING  |
     * 32  |       offline_limit  |         TEXT  |
     * 33  |    offline_accounts  |         TEXT  |
     * 34  |        offline_draw  |         TEXT  |
     * 35  |     valid_apply_raw  |         TEXT  |
     * 36  |     jsl_advise_text  |         TEXT  |
     * 37  |             apply10  |      INTEGER  |
     * 38  |         progress_nm  |       STRING  |
     * 39  |             cb_type  |       STRING  |
     * 40  |         progress_dt  |   LOCAL_DATE  |
     * 41  |       progress_full  |       STRING  |
     * 42  |             cp_flag  |      BOOLEAN  |
     * 43  |  convert_price_tips  |         TEXT  |
     * 44  |          apply_tips  |       STRING  |
     * 45  |             ap_flag  |       STRING  |
     * 46  |         single_draw  |       DOUBLE  |
     * 47  |         valid_apply  |       DOUBLE  |
     */
    public static Table convertibleBondToIssue() {
        String url = String.format("https://www.jisilu.cn/data/cbnew/pre_list/");

        Map<String, String> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%s", System.currentTimeMillis()));

        Map<String, Object> form = new HashMap<>();
        form.put("progress", "");
        form.put("rp", 22);

        String result = HttpRequest.post(url, params).userAgent(HttpConstant.UA).form(form).body();
        JsonNode json = JsonUtils.json(result, "rows");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json, "cell");

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames, "cell");

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table convertibleBondRedeem() {
        String url = "https://www.jisilu.cn/data/cbnew/redeem_list/";

        Map<String, String> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%s", System.currentTimeMillis()));

        Map<String, Object> form = new HashMap<>();
        form.put("page", 1);
        form.put("rp", 50);

        String result = HttpRequest.post(url, params).userAgent(HttpConstant.UA).form(form).body();
        JsonNode json = JsonUtils.json(result, "rows");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json, "cell");

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames, "cell");

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    /**
     * 回售
     *
     * @return Structure of
     * Index  |        Column Name        |  Column Type  |
     * -----------------------------------------------------
     * 0  |                  bond_id  |      INTEGER  |
     * 1  |                  bond_nm  |       STRING  |
     * 2  |               full_price  |       DOUBLE  |
     * 3  |                    price  |       DOUBLE  |
     * 4  |                  last_dt  |   LOCAL_DATE  |
     * 5  |                last_time  |   LOCAL_TIME  |
     * 6  |                 stock_id  |      INTEGER  |
     * 7  |                 stock_nm  |       STRING  |
     * 8  |               margin_flg  |       STRING  |
     * 9  |             orig_iss_amt  |       DOUBLE  |
     * 10  |                    btype  |       STRING  |
     * 11  |             curr_iss_amt  |       DOUBLE  |
     * 12  |              next_put_dt  |       STRING  |
     * 13  |            convert_price  |       DOUBLE  |
     * 14  |  put_convert_price_ratio  |       STRING  |
     * 15  |                put_price  |       STRING  |
     * 16  |                   put_tc  |       STRING  |
     * 17  |                     time  |       STRING  |
     * 18  |                   sprice  |       DOUBLE  |
     * 19  |               price_tips  |       STRING  |
     * 20  |        put_convert_price  |       STRING  |
     */
    public static Table convertibleBondBack() {
        String url = "https://www.jisilu.cn/data/cbnew/huishou_list/";

        Map<String, String> params = new HashMap<>();
        params.put("___jsl", String.format("LST___t=%s", System.currentTimeMillis()));

        Map<String, Object> form = new HashMap<>();
        form.put("page", 1);
        form.put("rp", 50);

        String result = HttpRequest.post(url, params).userAgent(HttpConstant.UA).form(form).body();
        JsonNode json = JsonUtils.json(result, "rows");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json, "cell");

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames, "cell");

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table convertibleBondAdjustment(String symbol) {
        String url = String.format("https://www.jisilu.cn/data/cbnew/adj_logs/?bond_id=%s", symbol);
        String result = HttpRequest.get(url).userAgent(HttpConstant.UA).body();

        Table table;
        try {
            /**
             * 返回值：
             * 1. 该可转债没有转股价调整记录，服务端返回文本 '暂无数据'
             * 2. 无效可转债代码，服务端返回 {"timestamp":1639565628,"isError":1,"msg":"无效代码格式"}
             * 以上两种情况，返回空的 DataFrame
             */
            if (!result.contains("</table>")) {
                table = Table.create();
            } else {
                HtmlReadOptions options = HtmlReadOptions.builderFromString(result).tableIndex(0).build();
                table = Table.read().usingOptions(options);
            }
        } catch (Exception e) {
            log.error("解析失败", e);
            table = Table.create();
        }
        return table;
    }

    public static void main(String[] args) {
        Table table = convertibleBondAdjustment("110059");


        System.out.println(table.printAll());
        System.out.println(table.structure().printAll());
    }
}
