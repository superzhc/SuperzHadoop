package com.github.superzhc.fund.util;

/**
 * @author superz
 * @create 2021/12/31 14:06
 */
public class FundUtils {
    /**
     * first*(1+x)^n=last
     * <p>
     * 通过上面的公式计算收益率 x 的值
     *
     * @param first  期初资金
     * @param last   期末资金
     * @param period 周期
     */
    public static void shouYiLv(Double first, Double last, Integer period) {

    }

    /**
     * netPurchaseAmount + rate * netPurchaseAmount = amount
     * <p>
     * 即：
     * <p>
     * netPurchaseAmount = amount /(1+rate)
     * <p>
     * 申购费用是按照净申购份额来收取的
     *
     * @param amount 申购金额
     * @param rate   费率（注意：这个是带百分比的）
     * @return
     */
    public static Double netPurchaseAmount(Double amount, Double rate) {
        return amount / (1 + 0.01 * rate);
    }

    public static void main(String[] args) {
        System.out.println(netPurchaseAmount(100000.0,0.15));
    }
}
