package com.github.superzhc.data.endata;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * https://github.com/justinzm/gopup/blob/a66e9c44ad255cb05b90fcc68d19fee8c7d95eea/gopup/movie/movie.py
 * @author superz
 * @create 2021/12/3 17:13
 */
public class BoxOfficeMovie {
    private static final Map<String, String> HEADERS = new HashMap<>();

    static {
        HEADERS.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
    }

    /**
     * BOX = 'boxOffice'
     * <p>
     * MOVIE_BOX = 'http://www.cbooo.cn/%s/GetHourBoxOffice?d=%s'
     */

    public String webDES(String text) {
        try {
            // 得到脚本引擎
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            // 引擎读取脚本
            InputStream in = BoxOfficeMovie.class.getClassLoader().getResourceAsStream("./webDES.js");
            engine.eval(new InputStreamReader(in));
            // 获取对象
            Object webInstance = engine.get("webInstace");
            // 调用js脚本中的方法
            Invocable invocable = (Invocable) engine;
            String result = (String) invocable.invokeMethod(webInstance, "shell", text);
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    public static void main(String[] args) throws Exception {

    }
}
