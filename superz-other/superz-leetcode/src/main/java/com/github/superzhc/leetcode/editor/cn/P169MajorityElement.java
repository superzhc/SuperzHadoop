// 给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数大于 ⌊ n/2 ⌋ 的元素。
//
// 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
//
//
//
// 示例 1:
//
// 输入: [3,2,3]
// 输出: 3
//
// 示例 2:
//
// 输入: [2,2,1,1,1,2,2]
// 输出: 2
//
// Related Topics 位运算 数组 分治算法
// 👍 647 👎 0

package com.github.superzhc.leetcode.editor.cn;

import java.util.HashMap;
import java.util.Map;

// Java：多数元素
public class P169MajorityElement
{
    public static void main(String[] args) {
        Solution solution = new P169MajorityElement().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution
    {
        public int majorityElement(int[] nums) {
            Map<Integer, Integer> map = new HashMap<>();
            int len = nums.length;
            for (int i = 0; i < len; i++) {
                int num = nums[i];
                int n = map.getOrDefault(num, 0);
                map.put(num, n + 1);
            }

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                int num = entry.getKey();
                int f = entry.getValue();
                if (f > len / 2) {
                    return num;
                }
            }
            return 0;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
