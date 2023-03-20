package com.github.superzhc.data.other;

import com.github.superzhc.common.utils.MapUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AKToolsTest {

    private AKTools instance;

    @Before
    public void setUp() throws Exception {
        instance = new AKTools("127.0.0.1");
    }

    @Test
    public void testGet() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "20230216");

        List<Map<String, Object>> data = instance.get("fund_etf_spot_em");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void stock_individual_info_em() {
        String api = "stock_individual_info_em";

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", "000001");

        List<Map<String, Object>> data = instance.get(api, params);
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void stock_new_a_spot_em(){
        List<Map<String,Object>> data=instance.get("stock_new_a_spot_em");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void stock_kc_a_spot_em(){
        List<Map<String,Object>> data=instance.get("stock_kc_a_spot_em");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void testBondInfoCm() {
        String api = "bond_info_cm";

        Map<String, Object> params = new HashMap<>();
        params.put("bond_name", "");
        params.put("bond_code", "");
        params.put("bond_issue", "");
        params.put("bond_type", "");
        params.put("coupon_type", "");
        params.put("issue_year", "");
        params.put("underwriter", "");
        params.put("grade", "");

        List<Map<String, Object>> data = instance.get(api, params);
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void macro_bank_china_interest_rate() {
        List<Map<String, Object>> data = instance.get("macro_bank_china_interest_rate");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_qyspjg() {
        List<Map<String, Object>> data = instance.get("macro_china_qyspjg");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_fdi() {
        List<Map<String, Object>> data = instance.get("macro_china_fdi");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_cpi_yearly() {
        List<Map<String, Object>> data = instance.get("macro_china_cpi_yearly");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_cpi_monthly() {
        List<Map<String, Object>> data = instance.get("macro_china_cpi_monthly");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_ppi_yearly() {
        List<Map<String, Object>> data = instance.get("macro_china_ppi_yearly");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void macro_china_fx_reserves_yearly() {
        List<Map<String, Object>> data = instance.get("macro_china_fx_reserves_yearly");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_m2_yearly() {
        List<Map<String, Object>> data = instance.get("macro_china_m2_yearly");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void macro_china_new_financial_credit() {
        List<Map<String, Object>> data = instance.get("macro_china_new_financial_credit");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_cpi() {
        List<Map<String, Object>> data = instance.get("macro_china_cpi");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_gdp() {
        List<Map<String, Object>> data = instance.get("macro_china_gdp");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_ppi() {
        List<Map<String, Object>> data = instance.get("macro_china_ppi");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_pmi() {
        List<Map<String, Object>> data = instance.get("macro_china_pmi");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_gdzctz() {
        List<Map<String, Object>> data = instance.get("macro_china_gdzctz");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_czsr() {
        List<Map<String, Object>> data = instance.get("macro_china_czsr");
        System.out.println(MapUtils.print(data));
    }

    public void macro_china_whxd() {
        List<Map<String, Object>> data = instance.get("macro_china_whxd");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void sw_index_first_info() {
        List<Map<String, Object>> data = instance.get("sw_index_first_info");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void sw_index_second_info() {
        List<Map<String, Object>> data = instance.get("sw_index_second_info");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void sw_index_third_info() {
        List<Map<String, Object>> data = instance.get("sw_index_third_info");
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void fund_rating_all() {
        List<Map<String, Object>> data = instance.get("fund_rating_all");
        System.out.println(MapUtils.print(data, 100));
    }
}