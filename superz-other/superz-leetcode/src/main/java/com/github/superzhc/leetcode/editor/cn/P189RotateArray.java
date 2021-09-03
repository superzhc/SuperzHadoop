// 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
//
// 示例 1:
//
// 输入: [1,2,3,4,5,6,7] 和 k = 3
// 输出: [5,6,7,1,2,3,4]
// 解释:
// 向右旋转 1 步: [7,1,2,3,4,5,6]
// 向右旋转 2 步: [6,7,1,2,3,4,5]
// 向右旋转 3 步: [5,6,7,1,2,3,4]
//
//
// 示例 2:
//
// 输入: [-1,-100,3,99] 和 k = 2
// 输出: [3,99,-1,-100]
// 解释:
// 向右旋转 1 步: [99,-1,-100,3]
// 向右旋转 2 步: [3,99,-1,-100]
//
// 说明:
//
//
// 尽可能想出更多的解决方案，至少有三种不同的方法可以解决这个问题。
// 要求使用空间复杂度为 O(1) 的 原地 算法。
//
// Related Topics 数组

package com.github.superzhc.leetcode.editor.cn;

// Java：旋转数组
public class P189RotateArray
{
    public static void main(String[] args) {
        Solution solution = new P189RotateArray().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {
        public void rotate(int[] nums, int k) {
            int len=nums.length;

            // 方案一：形成环移动
//            for(int i=1;i<=k;i++){
//                int tail=nums[len-1];
//                for(int j=len-2;j>-1;j--){
//                    nums[j+1]=nums[j];
//                }
//                nums[0]=tail;
//            }

            // 方案二：减少缩短的次数
//            int times = k % len;
//            for (int i = 1; i <= times; i++) {
//                int tail = nums[len - 1];
//                for (int j = len - 2; j > -1; j--) {
//                    nums[j + 1] = nums[j];
//                }
//                nums[0] = tail;
//            }

            // 方案三：
            int times = k % len;

        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
