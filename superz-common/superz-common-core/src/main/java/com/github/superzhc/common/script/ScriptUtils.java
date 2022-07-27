package com.github.superzhc.common.script;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * @author superz
 * @create 2022/5/10 0:41
 */
public class ScriptUtils {
//    private static final Logger log = LoggerFactory.getLogger(ScriptUtils.class);

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();

    public static ScriptEngine JSEngine() {
        return SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn");
    }

    public static ScriptEngine getEngine(String engineName) {
        return SCRIPT_ENGINE_MANAGER.getEngineByName(engineName);
    }

    /**
     * 加载资源
     *
     * @param path
     */
    public static void load(ScriptEngine engine, String path) {
        try {
            engine.eval(new FileReader(path));
        } catch (ScriptException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void eval(ScriptEngine engine, String str) {
        try {
            engine.eval(str);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输入方法体内容，并进行调用
     *
     * @param engine
     * @param func
     * @param funcName
     * @param params
     * @return
     */
    public static <T> T function(ScriptEngine engine, String func, String funcName, Object... params) {
        try {
            engine.eval(func);
//            Invocable invocable = (Invocable) engine;
//            Object value = invocable.invokeFunction(funcName, params);
//            return value;
            return call(engine, funcName, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用引擎中的方法
     *
     * @param engine
     * @param funcName
     * @param params
     * @return
     */
    public static <T> T call(ScriptEngine engine, String funcName, Object... params) {
        try {
            Invocable invocable = (Invocable) engine;
            Object value = invocable.invokeFunction(funcName, params);
            return (T) value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取变量
     *
     * @param engine
     * @param varName
     * @return
     */
    public static Object variable(ScriptEngine engine, String varName) {
        return engine.get(varName);
    }

    public static String string(ScriptEngine engine, String varName) {
        Object obj = variable(engine, varName);
        return string(obj);
    }

    public static String string(Object obj) {
        if (null == obj) {
            return null;
        }

        String value;
        if (obj instanceof ScriptObjectMirror) {
            ScriptObjectMirror obj2 = (ScriptObjectMirror) ((ScriptObjectMirror) obj).eval("JSON");
            Object stringify = obj2.callMember("stringify", obj);
            value = (String) stringify;
        } else {
            value = (String) obj;
        }
        return value;
    }

    public static Map<String, Object> object(ScriptEngine engine, String varName) {
        Object obj = variable(engine, varName);
        return object(obj);
    }

    public static Map<String, Object> object(Object value) {
        /*if (value instanceof ScriptObjectMirror) {
            ScriptObjectMirror v2 = (ScriptObjectMirror) value;
            if (v2.isArray()) {
                return getArray(v2);
            } else {
                return getObject(v2);
            }
        } else {
            return value;
        }*/
        ScriptObjectMirror v2 = (ScriptObjectMirror) value;
        return getObject(v2);
    }

    public static List array(ScriptEngine engine, String varName) {
        Object obj = variable(engine, varName);
        return array(obj);
    }

    public static List array(Object value) {
        ScriptObjectMirror v2 = (ScriptObjectMirror) value;
        return getArray(v2);
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
