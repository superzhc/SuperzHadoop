package com.github.superzhc.data.tushare;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.github.superzhc.common.utils.PropertiesUtils;
import com.google.common.base.CaseFormat;
import okhttp3.*;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author superz
 * @create 2021/9/25 10:24
 */
public class TusharePro {
    private static final String URL = "http://api.waditu.com";

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    private String token;

    public TusharePro(String token) {
        this.token = token;
    }

    public List<Map<String, Object>> execute(String apiName, Map<String, String> params, String[] fields) {
        return execute(apiName, params, String.join(",", fields));
    }

    public List<Map<String, Object>> execute(String apiName, Map<String, String> params, String fields) {

        return execute0(apiName, params, fields, new Function<TushareResponse.Data, List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> apply(TushareResponse.Data data) {
                if (null == data.getItems() || data.getItems().length == 0) {
                    return null;
                }

                List<Map<String, Object>> result = new ArrayList<>();

                int fieldSize = data.getFields().length;
                for (int i = 0, len = data.getItems().length; i < len; i++) {
                    Map<String, Object> map = new HashMap<>();
                    for (int j = 0; j < fieldSize; j++) {
                        map.put(data.getFields()[j], data.getItems()[i][j]);
                    }
                    result.add(map);
                }

                return result;
            }
        });

    }

    public Table execute2(String apiName, Map<String, String> params, String[] fields) {
        return execute2(apiName, params, String.join(",", fields));
    }

    public Table execute2(String apiName, Map<String, String> params, String fields) {
        return execute0(apiName, params, fields, new Function<TushareResponse.Data, Table>() {
            @Override
            public Table apply(TushareResponse.Data data) {
                Table table = Table.create();

                /**
                 * 表格的列暂未定义类型，统一使用String类型，待修复
                 */

                // 获取列名
                Integer fieldSize = data.getFields().length;
                for (int i = 0; i < fieldSize; i++) {
                    table.addColumns(StringColumn.create(data.getFields()[i]));
                }

                // 获取数据
                if (null != data.getItems() && data.getItems().length > 0) {
                    for (int i = 0, len = data.getItems().length; i < len; i++) {
                        for (int j = 0; j < fieldSize; j++) {
                            Column<?> column = table.column(j);
                            column.appendObj(TypeUtils.castToString(data.getItems()[i][j]));
                        }
                    }
                }
                return table;
            }
        });
    }

    public <R> List<R> execute3(String apiName, Map<String, String> params, String[] fields, Class<R> clazz) {
        return execute3(apiName, params, String.join(",", fields), clazz);
    }

    public <R> List<R> execute3(String apiName, Map<String, String> params, String fields, Class<R> clazz) {
        return execute0(apiName, params, fields, new Function<TushareResponse.Data, List<R>>() {
            @Override
            public List<R> apply(TushareResponse.Data data) {
                if (null == data.getItems() || data.getItems().length == 0) {
                    return null;
                }

                List<R> result = new ArrayList<>();

                int fieldSize = data.getFields().length;
                try {
                    for (int i = 0, len = data.getItems().length; i < len; i++) {
                        R instance = clazz.newInstance();
                        for (int j = 0; j < fieldSize; j++) {
                            // Field field = clazz.getDeclaredField(data.getFields()[j]);
                            // 统一转实体使用驼峰命名法
                            Field field = clazz.getDeclaredField(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, data.getFields()[j]));
                            field.setAccessible(true);
                            field.set(instance, data.getItems()[i][j]);
                        }
                        result.add(instance);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return result;
            }
        });
    }

//    public <R> R execute0(String apiName, Map<String, String> params, String[] fields, Function<TushareResponse.Data, R> function) {
//        return execute0(apiName, params, String.join(",", fields), function);
//    }

    public <R> R execute0(String apiName, Map<String, String> params, String fields, Function<TushareResponse.Data, R> function) {
        Map<String, Object> body = new HashMap<>();
        body.put("api_name", apiName);
        body.put("token", token);
        body.put("params", params);
        body.put("fields", fields);
        RequestBody requestBody = RequestBody.create(JSON.toJSONString(body), MediaType.parse("application/json"));

        Request request = new Request.Builder().url(URL).post(requestBody).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("请求异常:code={" + response.code() + "}\n异常信息:" + response.body().string());
            }

            TushareResponse tr = JSON.parseObject(response.body().string(), TushareResponse.class);
            if (null == tr.getCode() || tr.getCode() != 0) {
                throw new RuntimeException("请求失败:code={" + tr.getCode() + "}\n失败信息:" + tr.getMsg());
            }

            return function.apply(tr.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        PropertiesUtils.read("application.properties");
        TusharePro pro = new TusharePro(PropertiesUtils.get("tushare.token"));

        List<Map<String, Object>> datas=pro.execute("index_basic", null, new String[]{
                "ts_code",
                "name",
                "market",
                "publisher",
                "category",
                "base_date",
                "base_point",
                "list_date"
        });

        for(Map<String,Object> data:datas){
            System.out.println(JSON.toJSONString(data));
        }

//        Table table = pro.execute2("index_basic", null, new String[]{
//                "ts_code",
//                "name",
//                "market",
//                "publisher",
//                "category",
//                "base_date",
//                "base_point",
//                "list_date"
//        });
//        System.out.println(table.structure());
//        System.out.println("------------------------华丽的分割线-----------------------------");
//        System.out.println(table.shape());
//        System.out.println("------------------------华丽的分割线-----------------------------");
//        System.out.println(table.printAll());
    }
}
