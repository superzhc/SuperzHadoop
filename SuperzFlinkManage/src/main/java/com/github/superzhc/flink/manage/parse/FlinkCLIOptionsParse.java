package com.github.superzhc.flink.manage.parse;

import com.alibaba.fastjson.JSON;
import com.github.superzhc.flink.manage.util.ReflectionUtil;
import com.github.superzhc.flink.manage.annotation.CLIOption;
import com.github.superzhc.flink.manage.model.FlinkCLIOptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/4/10 14:30
 */
public class FlinkCLIOptionsParse {
    private FlinkCLIOptions flinkCLIOptions;

    public FlinkCLIOptionsParse(FlinkCLIOptions flinkCLIOptions) {
        this.flinkCLIOptions = flinkCLIOptions;
    }

    public List<String> parse() {
        List<String> result = new ArrayList<>();
        // 获取所有属性
//        Field[] fields = ReflectionUtil.getDeclaredFields(flinkCLIOptions.getClass());
        Field[] fields = flinkCLIOptions.options().toArray(new Field[0]);
        if (null != fields && fields.length > 0) {
            try {
                for (Field field : fields) {
//                    // 判断类是否存在CLIOption注解，若为该注解，代表非命令行的参数
//                    if (!field.isAnnotationPresent(CLIOption.class)) {
//                        continue;
//                    }

                    CLIOption cliOption = field.getAnnotation(CLIOption.class);

                    // 获取参数名称
                    String param;
                    if (null != cliOption.shortening() && cliOption.shortening().trim().length() > 0) {
                        param = "-" + cliOption.shortening();
                    } else {
                        param = "--" + ("".equals(cliOption.name()) ? field.getName() : cliOption.name());
                    }

                    // 获取参数值
                    field.setAccessible(true);
                    Object value = field.get(flinkCLIOptions);

                    // 判断参数值是否设置，若未设置，不返回该参数
                    if (null == value) {
                        continue;
                    }

                    // 判断是否是属性参数
                    if (cliOption.isProperty()) {
                        // 属性参数已改为保存为json字符串，这里添加一步解析
                        Map<String, Object> properties = JSON.parseObject((String) value).getInnerMap();
                        for (Map.Entry<String, Object> property : properties.entrySet()) {
                            result.add(String.format("%s%s=%s", param, property.getKey(), property.getValue()));
                        }
                    }
                    // 判断值是否是布尔类型，如果是布尔类型，则只要有参数就ok
                    else if (Boolean.class == value.getClass()
                            || (value.getClass().isPrimitive() && Boolean.TYPE == value.getClass())) {
                        Boolean b = (Boolean) value;
                        if (b) {
                            result.add(param);
                        }
                    } else {
                        result.add(param);
                        result.add(String.valueOf(value));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
