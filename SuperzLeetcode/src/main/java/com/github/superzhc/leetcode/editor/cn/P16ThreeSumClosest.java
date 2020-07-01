// 给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target
// 最接近。返回这三个数的和。假定每组输入只存在唯一答案。
//
//
//
// 示例：
//
// 输入：nums = [-1,2,1,-4], target = 1
// 输出：2
// 解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2) 。
//
//
//
//
// 提示：
//
//
// 3 <= nums.length <= 10^3
// -10^3 <= nums[i] <= 10^3
// -10^4 <= target <= 10^4
//
// Related Topics 数组 双指针

package com.github.superzhc.leetcode.editor.cn;

// Java：最接近的三数之和
public class P16ThreeSumClosest
{
    public static void main(String[] args) {
        Solution solution = new P16ThreeSumClosest().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {
        public int threeSumClosest(int[] nums, int target) {
            int len = nums.length;
            Integer min = null, result = null;

            for (int i = 0; i < len; i++) {
                for (int j = i + 1; j < len; j++) {
                    for (int k = j + 1; k < len; k++) {
                        int sum = nums[i] + nums[j] + nums[k];
                        int x = Math.abs(sum - target);
                        if ((null == min && result == null) || min > x) {
                            min = x;
                            result = sum;
                        }
                    }
                }
            }
            return result;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
