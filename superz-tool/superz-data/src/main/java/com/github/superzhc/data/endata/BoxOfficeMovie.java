package com.github.superzhc.data.endata;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.data.tushare.TushareResponse;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 影视数据
 * <p>
 * EBOT艺恩票房智库 https://www.endata.com.cn
 * 参考：https://github.com/justinzm/gopup/blob/a66e9c44ad255cb05b90fcc68d19fee8c7d95eea/gopup/movie/movie.py
 * <p>
 * 2021年12月4日 10点23分 经测试几个接口，已无法获取数据
 *
 * @author superz
 * @create 2021/12/3 17:13
 */
@Deprecated
public class BoxOfficeMovie {
    private static final Logger log = LoggerFactory.getLogger(BoxOfficeMovie.class);
    private static final Map<String, String> HEADERS = new HashMap<>();

    static {
        HEADERS.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
    }

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取实时电影票房数据
     * 数据来源：EBOT艺恩票房智库 https://www.endata.com.cn/BoxOffice
     * <p>
     * 2021年12月4日 10点15分 获取不到数据
     *
     * @return
     */
    @Deprecated
    public String realTimeBoxOffice() {
        String url = "https://www.endata.com.cn/API/GetData.ashx";
        Map<String, Object> data = new HashMap<>();
        data.put("tdate", DTF.format(LocalDate.now()));
        data.put("MethodName", "BoxOffice_GetHourBoxOffice");
        return execute(url, data);
    }

    /**
     * 获取单日电影票房数据
     * 数据来源：EBOT艺恩票房智库 https://www.endata.com.cn/BoxOffice
     *
     * @return
     */
    @Deprecated
    public String dayBoxOffice() {
        String url = "https://www.endata.com.cn/API/GetData.ashx";

        Map<String, Object> data = new HashMap<>();
        data.put("sdate", DTF.format(LocalDate.now().minusDays(1)));
        data.put("edate", DTF.format(LocalDate.now()));
        data.put("MethodName", "BoxOffice_GetDayBoxOffice");

        return execute(url, data);
    }

    public String dayCinema() {
        String url = "https://www.endata.com.cn/API/GetData.ashx";

        Map<String, Object> data = new HashMap<>();
        // data.put("date",null);
        data.put("rowNum1", 1);
        data.put("rowNum2", 100);
        data.put("MethodName", "BoxOffice_GetCinemaDayBoxOffice");

        return execute(url, data);
    }

    private String execute(String url, Map<String, Object> data) {
        return execute(url, HEADERS, data);
    }

    private String execute(String url, Map<String, String> headers, Map<String, Object> data) {
        try {
            RequestBody requestBody = RequestBody.create(mapper.writeValueAsString(data), MediaType.parse("application/json"));

            Request.Builder builder = new Request.Builder();
            log.debug("url : " + url);
            builder.url(url);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
                log.debug("header : [" + header.getKey() + ":" + header.getValue() + "]");
            }
            builder.post(requestBody);
            log.debug("data : " + mapper.writeValueAsString(data));
            Request request = builder.build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("请求异常:code={" + response.code() + "}\n异常信息:" + response.body().string());
                }

                // 数据需要进行解密
                String resResponse = webDES(response.body().string());
                log.debug("response : " + resResponse);
                return resResponse;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String webDES(String text) {
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
        String result = new BoxOfficeMovie().dayCinema();
        // System.out.println(result);
    }
}
