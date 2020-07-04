// 给定一个由整数组成的非空数组所表示的非负整数，在该数的基础上加一。
//
// 最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。
//
// 你可以假设除了整数 0 之外，这个整数不会以零开头。
//
// 示例 1:
//
// 输入: [1,2,3]
// 输出: [1,2,4]
// 解释: 输入数组表示数字 123。
//
//
// 示例 2:
//
// 输入: [4,3,2,1]
// 输出: [4,3,2,2]
// 解释: 输入数组表示数字 4321。
//
// Related Topics 数组

package com.github.superzhc.leetcode.editor.cn;

import java.util.Arrays;

// Java：加一
public class P66PlusOne
{
    public static void main(String[] args) {
        Solution solution = new P66PlusOne().new Solution();
        int[] digits = {1, 2, 3 };
        solution.plusOne(digits);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {
        public int[] plusOne(int[] digits) {
            int len = digits.length;
            digits[len - 1] = digits[len - 1] + 1;

            boolean carry = false;
            int len2 = len + 1;
            int[] ii = new int[len2];
            for (int i = 0; i < len2; i++) {
                int oriIndex = len - 1 - i;
                int i1 = oriIndex > -1 ? digits[oriIndex] : 0;
                if (carry) {
                    carry = false;
                    i1 = i1 + 1;
                }

                if (i1 > 9) {
                    carry = true;
                    i1 = i1 - 10;
                }

                ii[i] = i1;
            }

            int len3 = ii[len2 - 1] == 0 ? len : len2;
            int[] result = new int[len3];
            for (int i = len3 - 1; i > -1; i--) {
                result[i] = ii[len3 - 1 - i];
            }

            return result;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
