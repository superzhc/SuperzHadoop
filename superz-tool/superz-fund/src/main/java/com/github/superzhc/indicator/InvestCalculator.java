package com.github.superzhc.indicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 投入成本计算
 *
 * @author superz
 * @create 2022/5/19 22:42
 */
public class InvestCalculator {
    private static final Logger log = LoggerFactory.getLogger(InvestCalculator.class);

    public static double realInvest(double invest, double fee) {
        return invest / (1 + fee);
    }

    public static double totalInvest(double invest, double fee) {
        return (1.0 + fee) * invest;
    }

    /**
     * 通过计算成本涨跌幅来计算出投入
     *
     * @param oldCost
     * @param share
     * @param currentNetWorth
     * @param change
     * @return
     */
    public static double investByChange(double oldCost, double share, double currentNetWorth, double change) {
        // 最大幅度变化
        double maxIncrease = (currentNetWorth - oldCost) / oldCost;
        if (currentNetWorth >= oldCost) {
            log.info("成本增长区间在(0,{})", maxIncrease);
            // 增幅在 0~(currentNetWorth-avgCost)/avgCost之间
            if (change <= 0 || change >= maxIncrease) {
                throw new RuntimeException(String.format("成本价：%.4f，当前估值：%.4f，成本增长区间须在(0,%.4f)", oldCost, currentNetWorth, maxIncrease));
            }
        } else {
            log.info("成本减少区间在({},0)", maxIncrease);
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
     * @param currentNetWorth
     * @param invest
     * @return
     */
    public static double costByInvest(double oldCost, double share, double currentNetWorth, double invest) {
        double share2 = invest / currentNetWorth;
        return (oldCost * share + invest) / (share + share2);
    }

    /**
     * 通过计算最新成本来获取投入总额
     *
     * @param oldCost
     * @param share
     * @param currentNetWorth
     * @param newCost
     * @return
     */
    public static double investByCost(double oldCost, double share, double currentNetWorth, double newCost) {
        // 是否降本
        boolean b = currentNetWorth - oldCost < 0;
        if (b) {
            if (newCost < currentNetWorth || newCost > oldCost) {
                throw new RuntimeException(String.format("当前成本：%.4f，当前估值：%.4f，预期成本区间在(%.4f,%.4f)，设定成本[%.4f]不符合规定", oldCost, currentNetWorth, currentNetWorth, oldCost, newCost));
            }
        } else {
            if (newCost < oldCost || newCost > currentNetWorth) {
                throw new RuntimeException(String.format("当前成本：%.4f，当前估值：%.4f，预期成本区间在(%.4f,%.4f)，设定成本[%.4f]不符合规定", oldCost, currentNetWorth, oldCost, currentNetWorth, newCost));
            }
        }
        // 在当前净值下买入多少份额可获取到新成本
        double share2 = (oldCost - newCost) * share / (newCost - currentNetWorth);
        return currentNetWorth * share2;
    }
}
