package com.github.superzhc.indicator;

import tech.tablesaw.api.Table;

import java.time.LocalDate;

/**
 * 投入成本计算
 *
 * @author superz
 * @create 2022/5/19 22:42
 */
public class InvestCalculator {
    /**
     * 通过计算成本涨跌幅来计算出投入
     *
     * @param oldCost
     * @param share
     * @param change
     * @param currentNetWorth
     *
     * @return
     */
    public static double costByChange(double oldCost, double share, double change, double currentNetWorth) {
        // 最大幅度变化
        double maxIncrease = (currentNetWorth - oldCost) / oldCost;
        if (currentNetWorth >= oldCost) {
            // 增幅在 0~(currentNetWorth-avgCost)/avgCost之间
            if (change <= 0 || change >= maxIncrease) {
                throw new RuntimeException(String.format("成本价：%.4f，当前估值：%.4f，成本增长区间须在(0,%.4f)", oldCost, currentNetWorth, maxIncrease));
            }
        } else {
            if (change >= 0 || change <= maxIncrease) {
                throw new RuntimeException(String.format("成本价：%.4f，当前估值：%.4f，成本减少区间须在(%.4f,0)", oldCost, currentNetWorth, maxIncrease));
            }
        }
        double x = (change * oldCost * share) / (1 - oldCost * (1 + change) / currentNetWorth);
        return x;
    }

    /**
     * 通过计算投入成本来获取最新的平均成本
     *
     * @param oldCost
     * @param share
     * @param invest
     * @param currentNetWorth
     *
     * @return
     */
    public static double costByInvest(double oldCost, double share, double invest, double currentNetWorth) {
        double share2 = invest / currentNetWorth;
        return (oldCost * share + invest) / (share + share2);
    }
}
