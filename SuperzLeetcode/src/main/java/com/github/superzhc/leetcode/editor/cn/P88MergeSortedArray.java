// 给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
//
//
//
// 说明:
//
//
// 初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。
// 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
//
//
//
//
// 示例:
//
// 输入:
// nums1 = [1,2,3,0,0,0], m = 3
// nums2 = [2,5,6], n = 3
//
// 输出: [1,2,2,3,5,6]
// Related Topics 数组 双指针

package com.github.superzhc.leetcode.editor.cn;

// Java：合并两个有序数组
public class P88MergeSortedArray
{
    public static void main(String[] args) {
        Solution solution = new P88MergeSortedArray().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {
        public void merge(int[] nums1, int m, int[] nums2, int n) {
            int cursor = m + n;
            int flag = 0;

            /**
             * 初始化条件
             * nums1 = [1,2,3,0,0,0], m = 3
             * nums2 = [2,5,6], n = 3
             * flag=0,cursor=6
             *
             * 遍历：
             * 1. [1,2,3,0,0,6],cursor=5,flag=1
             * 2. [1,2,3,0,5,6],cursor=4,flag=2
             * 3. [1,2,3,3,5,6],cursor=3,flag=2
             * 4. [1,2,2,3,5,6],cursor=2,flag=3
             * 5. [1,2,2,3,5,6],cursor=2,flag=3
             * 6. [1,2,2,3,5,6],cursor=2,flag=3
             *
             * 测试示例2
             * nums1=[2,0], m=1
             * nums2=[1], n=1
             * flag=0,cursor=2
             *
             * 1. [2,2],flag=0,cursor=1
             * 2. [1,2],flag=1,cursor=0
             */
            for (int i = m - 1; (i >= 0 || cursor > 0); i--) {
                for (int j = n - 1 - flag; j >= 0; j--) {
                    if (i > -1 && nums1[i] > nums2[j])
                        break;

                    nums1[cursor - 1] = nums2[j];
                    cursor--;
                    flag++;
                }

                if (i>-1) {
                    nums1[cursor - 1] = nums1[i];
                    cursor--;
                }
            }
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
