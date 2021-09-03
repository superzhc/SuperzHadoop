// 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
//
// 如果你最多只允许完成一笔交易（即买入和卖出一支股票一次），设计一个算法来计算你所能获取的最大利润。
//
// 注意：你不能在买入股票前卖出股票。
//
//
//
// 示例 1:
//
// 输入: [7,1,5,3,6,4]
// 输出: 5
// 解释: 在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
// 注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
//
//
// 示例 2:
//
// 输入: [7,6,4,3,1]
// 输出: 0
// 解释: 在这种情况下, 没有交易完成, 所以最大利润为 0。
//
// Related Topics 数组 动态规划

package com.github.superzhc.leetcode.editor.cn;

// Java：买卖股票的最佳时机
public class P121BestTimeToBuyAndSellStock
{
    public static void main(String[] args) {
        Solution solution = new P121BestTimeToBuyAndSellStock().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {
        public int maxProfit(int[] prices) {
            int days = prices.length;

            if (days == 0)
                return 0;
            else if (days == 1)
                return 0;

            /**
             * dp:dayprice
             * 
             * 示例1：[7,1,5,3,6,4]
             * 0. dp=7,min=7,max=7,price=0,last_price=0
             * 1. dp=1,min=1,max=1,price=0,last_price=0
             * 2. dp=5,min=1,max=5,price=4,last_price=4
             * 3. dp=3,min=1,max=5,price=4,last_price=4
             * 4. dp=6,min=1,max=6,price=5,last_price=5
             * 5. dp=4,min=1,max=6,price=5,last_price=5
             * 
             * 示例2：[2,4,1]
             * 0. dp=2,min=2,max=2,price=0,last_price=0
             * 1. dp=4,min=2,max=4,price=2,last_price=2
             * 2. dp=1,min=1,max=1,price=0,last_price=2
             */
            int min = prices[0], max = prices[0], price = 0, last_price = 0;
            for (int day = 1; day < days; day++) {
                int p = prices[day];// 当天的价格
                if (min > p) {
                    min = p;
                    max = p;
                    price = 0;
                }
                else if (max < p) {
                    max = p;
                    int i = max - min;
                    price = price > i ? price : i;
                    last_price = last_price > price ? last_price : price;
                }
            }

            return last_price > 0 ? last_price : 0;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
