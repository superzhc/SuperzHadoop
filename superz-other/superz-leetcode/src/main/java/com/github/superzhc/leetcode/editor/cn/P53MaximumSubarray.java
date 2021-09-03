// 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
//
// 示例:
//
// 输入: [-2,1,-3,4,-1,2,1,-5,4],
// 输出: 6
// 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
//
//
// 进阶:
//
// 如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的分治法求解。
// Related Topics 数组 分治算法 动态规划

package com.github.superzhc.leetcode.editor.cn;

// Java：最大子序和
public class P53MaximumSubarray
{
    public static void main(String[] args) {
        Solution solution = new P53MaximumSubarray().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {

        public int maxSubArray(int[] nums) {
            int len = nums.length;
            /**
             * [-2,1,-3,4,-1,2,1,-5,4]
             * 1. 第一个值为-2，因为小于0，直接过滤掉，即：max=0，oldmax=0
             * 2. 第二个值为1，因为大于0，即：max=1，oldmax=1
             * 3. 第三个值为-3，oldmax不做追加，max继续添加，即max=0，oldmax=1
             * 4. 第四个值为4，即max=4，oldmax=4
             * 5. 第五个值为-1，即max=3，oldmax=4
             * 6. 第六个值为2，即max=5，oldmax=5
             * 7. 第七个值为1，即max=6，oldmax=6
             * 8. 第八个值为-5，即max=1，oldmax=6
             * 9. 第九个值为4，即max=5，oldmax=6
             */
            int oldmax = 0, max = 0, single_max = len > 0 ? nums[0] : 0;
            for (int i = 0; i < len; i++) {
                int num = nums[i];
                if (num > single_max) {
                    single_max = num;
                }

                int temp = max + num;
                if (temp < 0) {
                    max = 0;
                }
                else {
                    max = temp;
                }

                if (max > oldmax)
                    oldmax = max;

            }
            return (oldmax > 0 && oldmax > single_max) ? oldmax : single_max;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
