// 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
//
// 你可以假设数组中无重复元素。
//
// 示例 1:
//
// 输入: [1,3,5,6], 5
// 输出: 2
//
//
// 示例 2:
//
// 输入: [1,3,5,6], 2
// 输出: 1
//
//
// 示例 3:
//
// 输入: [1,3,5,6], 7
// 输出: 4
//
//
// 示例 4:
//
// 输入: [1,3,5,6], 0
// 输出: 0
//
// Related Topics 数组 二分查找

package com.github.superzhc.leetcode.editor.cn;

// Java：搜索插入位置
public class P35SearchInsertPosition
{
    public static void main(String[] args) {
        Solution solution = new P35SearchInsertPosition().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {

        public int searchInsert(int[] nums, int target) {
            int start = 0, end = nums.length;
            while (true) {
                if ((end - start) == 1) {
                    if (nums[start] >= target)
                        return start;
                    else
                        return end;
                }

                int index = (start + end) / 2;
                if (nums[index] == target) {
                    return index;
                }
                else if (nums[index] > target) {
                    end = index;
                }
                else if (nums[index] < target) {
                    start = index;
                }
            }
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
