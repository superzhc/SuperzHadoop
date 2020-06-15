package com.github.superzhc.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 2020年04月18日 superz add
 */
public class FastJsonHelper
{
    private JSONObject obj;

    public FastJsonHelper(JSONObject obj) {
        this.obj = obj;
    }

    /**
     * 嵌套获取某个key的值，每一层嵌套使用 . 分割
     * @param keys obj1.obj2.key 或 obj1.obj2[2].key
     * @return
     */
    public Object deepGet(String keys) {
        JSONObject obj1 = obj;

        String[] arr = keys.split("\\.");
        for (int i = 0, len = arr.length - 1; i < len; i++) {
            String key = arr[i];
            if (key.lastIndexOf("[") != -1 && key.lastIndexOf("]") != -1) {// 2020年6月11日 提供数组类型解析
                int num = Integer.valueOf(key.substring(key.lastIndexOf("[") + 1, key.lastIndexOf("]")));
                String actualKey = key.substring(0, key.lastIndexOf("["));
                obj1 = obj1.getJSONArray(actualKey).getJSONObject(num);
            }
            else {
                obj1 = obj1.getJSONObject(key);
            }

            // 2020年4月24日 如果子Json对象为空，没必须继续向下找了，同时解决obj1报空指针的问题
            if (null == obj1)
                return null;
        }

        return obj1.get(arr[arr.length - 1]);
    }
}
