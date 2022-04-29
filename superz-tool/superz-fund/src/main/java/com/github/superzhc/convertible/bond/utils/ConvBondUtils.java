package com.github.superzhc.convertible.bond.utils;

/**
 * @author superz
 * @create 2022/4/28 20:21
 */
public class ConvBondUtils {
    /**
     * 可转债所属的市场
     * <p>
     * - 110开头的是沪市600开头股票对应的可转债
     * - 113开头的是沪市601和603开头股票对应的可转债
     * - 123开头的是深圳创业板以300开头股票对应的可转债
     * - 127开头的是深圳主板以000开头股票对应的可转债
     * - 128开头的是深证中小板以002开头股票对应的可转债
     * - 132开头的是在沪市发行的可转债
     * - 120开头的是在深市发行的可转债
     *
     * @param symbol
     *
     * @return
     */
    public static String market(String symbol) {
        String str = symbol.substring(0, 3);

        switch (str) {
            case "110":
            case "113":
            case "132":
                return "SH";
            case "120":
            case "123":
            case "127":
            case "128":
                return "SZ";
        }

        return null;
    }

    /**
     * 转股价值是指每张可转债转换成正股并卖出可以得到的金额，其单位为元
     * <p>
     * 转股价值的计算公式为
     * <p>
     * 转股价值=正股价格*100/转股价格
     *
     * @param zhengGuJiaGe 正股价格
     * @param zhuanGuJiaGe 转股价格
     *
     * @return
     */
    public static double zhuanGuJiaZhi(double zhengGuJiaGe, double zhuanGuJiaGe) {
        /**
         * 100/转股价格：理论上一张可转债可以转换成股票的数量
         */
        double zgjz = zhengGuJiaGe * (100.0 / zhuanGuJiaGe);
        return zgjz;
    }

    /**
     * 溢价率
     * <p>
     * 可转债溢价率是指可转债市场成交价格相对其转股价值高出的百分比
     * <p>
     * 溢价率的计算公式为：
     * <p>
     * 溢价率=（转债价格/转债价值-1）*100%
     *
     * @param zhuanZhaiJiaGe
     * @param zhuanGuJiaZhi
     *
     * @return
     */
    public static double premium(double zhuanZhaiJiaGe, double zhuanGuJiaZhi) {
        double d = zhuanZhaiJiaGe / zhuanGuJiaZhi - 1;
        return d * 100;
    }
}
