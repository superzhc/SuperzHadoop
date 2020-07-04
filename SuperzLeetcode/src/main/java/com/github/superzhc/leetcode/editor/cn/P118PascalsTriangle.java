// 给定一个非负整数 numRows，生成杨辉三角的前 numRows 行。
//
//
//
// 在杨辉三角中，每个数是它左上方和右上方的数的和。
//
// 示例:
//
// 输入: 5
// 输出:
// [
// [1],
// [1,1],
// [1,2,1],
// [1,3,3,1],
// [1,4,6,4,1]
// ]
// Related Topics 数组

package com.github.superzhc.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Java：杨辉三角
public class P118PascalsTriangle
{
    public static void main(String[] args) {
        Solution solution = new P118PascalsTriangle().new Solution();
        System.out.println(solution.generate(5));
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {
        public List<List<Integer>> generate(int numRows) {
            List<List<Integer>> result = new ArrayList<>();
            for (int i = 0; i < numRows; i++) {
                List<Integer> innerResult = new ArrayList<>();
                for (int j = 0; j < i + 1; j++) {
                    if (j == 0) {
                        innerResult.add(1);
                    }
                    else if (j == i) {
                        innerResult.add(1);
                    }
                    else {
                        List<Integer> last = result.get(i - 1);
                        innerResult.add(last.get(j - 1) + last.get(j));
                    }
                }
                result.add(innerResult);
            }
            return result;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
