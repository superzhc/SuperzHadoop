package com.github.superzhc.data.other;

import com.github.superzhc.common.utils.MapUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/23 0:42
 */
public class AKShareTest {
    AKTools api;

    @Before
    public void setUp() {
        api = new AKTools("127.0.0.1");
    }

    @Test
    //单次返回所有沪深京 A 股上市公司的实时行情数据[实时]
    public void stock_zh_a_spot_em() {
        List<Map<String, Object>> data = api.get("stock_zh_a_spot_em");
        MapUtils.show(data, 100);
    }

    @Test
    // 单次返回所有沪 A 股上市公司的实时行情数据[实时]
    public void stock_sh_a_spot_em() {
        List<Map<String, Object>> data = api.get("stock_sh_a_spot_em");
        MapUtils.show(data, 100);
    }

    @Test
    // 单次返回所有深 A 股上市公司的实时行情数据[实时]
    public void stock_sz_a_spot_em() {
        List<Map<String, Object>> data = api.get("stock_sz_a_spot_em");
        MapUtils.show(data, 100);
    }

    @Test
    // 单次返回所有新股上市公司的实时行情数据
    public void stock_new_a_spot_em() {
        List<Map<String, Object>> data = api.get("stock_new_a_spot_em");
        MapUtils.show(data, 100);
    }

    // region 指数

    @Test
    // 中国股票指数数据, 注意该股票指数指新浪提供的国内股票指数
    public void stock_zh_index_spot() {
        List<Map<String, Object>> data = api.get("stock_zh_index_spot");
        MapUtils.show(data);
    }

    @Test
    // 股票指数数据是从新浪财经获取的数据, 历史数据按日频率更新
    // 注意：腾讯的数据开始时间, 不是证券上市时间
    public void stock_zh_index_daily_tx() {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", "sh000300");

        List<Map<String, Object>> data = api.get("stock_zh_index_daily_tx", params);
        MapUtils.show(data);
    }

    // endregion 指数

}
