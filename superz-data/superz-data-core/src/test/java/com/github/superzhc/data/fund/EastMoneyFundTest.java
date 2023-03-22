package com.github.superzhc.data.fund;

import com.github.superzhc.common.utils.MapUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EastMoneyFundTest {

    @Test
    public void fundRealNet() {
        List<Map<String, Object>> data = EastMoneyFund.fundRealNet("164402", "001594");
        System.out.println(MapUtils.print(data));
    }
}