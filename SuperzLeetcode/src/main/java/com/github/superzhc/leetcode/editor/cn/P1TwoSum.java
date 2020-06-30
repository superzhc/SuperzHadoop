// 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
//
// 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
//
//
//
// 示例:
//
// 给定 nums = [2, 7, 11, 15], target = 9
//
// 因为 nums[0] + nums[1] = 2 + 7 = 9
// 所以返回 [0, 1]
//
// Related Topics 数组 哈希表

package com.github.superzhc.leetcode.editor.cn;

// Java：两数之和
public class P1TwoSum
{
    public static void main(String[] args) {
        Solution solution = new P1TwoSum().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {
        public int[] twoSum(int[] nums, int target) {
            for (int i = 0, len = nums.length; i < len; i++) {
                int need = target - nums[i];
                for (int j = (i + 1); j < len; j++) {
                    if (need == nums[j])
                        return new int[] {i, j };
                }
            }
            return null;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
