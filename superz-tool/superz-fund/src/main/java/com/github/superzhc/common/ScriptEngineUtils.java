package com.github.superzhc.common;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.*;

/**
 * @author superz
 * @create 2022/5/10 0:41
 */
public class ScriptEngineUtils {
    public static String string(Object obj) {
        String value = (String) obj;
        return value;
    }

    public static Object object(Object value) {
        if (value instanceof ScriptObjectMirror) {
            ScriptObjectMirror v2 = (ScriptObjectMirror) value;
            if (v2.isArray()) {
                return getArray(v2);
            } else {
                return getObject(v2);
            }
        } else {
            return value;
        }
    }

    public static List getArray(ScriptObjectMirror array) {
        List lst = new ArrayList<>();
        for (int i = 0, len = array.size(); i < len; i++) {
            Object value = array.getSlot(i);
            if (value instanceof ScriptObjectMirror) {
                ScriptObjectMirror v2 = (ScriptObjectMirror) value;
                if (v2.isArray()) {
                    lst.add(getArray(v2));
                } else {
                    lst.add(getObject(v2));
                }
            } else {
                lst.add(value);
            }
        }
        return lst;
    }

    public static Map<String, Object> getObject(ScriptObjectMirror obj) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> item : obj.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            if (value instanceof ScriptObjectMirror) {
                ScriptObjectMirror v2 = (ScriptObjectMirror) value;
                if (v2.isArray()) {
                    map.put(key, getArray(v2));
                } else {
                    map.put(key, getObject(v2));
                }
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
}
