package com.github.superzhc.flink.manage.job.cli;

import cn.hutool.core.util.ReflectUtil;
import com.github.superzhc.flink.manage.job.cli.annotation.CLIOption;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * flink脚本参数的统一父类
 * 注：
 * 1.不抽取公用参数，虽然有些参数相似度很高、甚至一样，但是非常容易出现混乱，每个Action自行管理参数
 * 2021年4月12日 add 命令行参数属性的抽取
 *
 * @author superz
 * @create 2021/4/10 14:25
 */
public abstract class FlinkCLIOptions {
    /**
     * 类属性仅读取一次就够了，没必要每次实例化都要反射获取Field
     */
    private static Map<Class<? extends FlinkCLIOptions>, Map<String, Field>> clazzOptions = new HashMap<>();
    private Map<String, Field> map;

    public FlinkCLIOptions() {
        if (clazzOptions.containsKey(this.getClass())) {
            map = clazzOptions.get(this.getClass());
        } else {
            map = new HashMap<>();
            Field[] fields = ReflectUtil.getFields(this.getClass());
            for (Field field : fields) {
                if (!field.isAnnotationPresent(CLIOption.class)) {
                    continue;
                }

                // 将简写和完整的写法都放入一个容器中
                CLIOption cliOption = field.getAnnotation(CLIOption.class);
                if (null != cliOption.shortening() && cliOption.shortening().trim().length() > 0) {
                    map.put(cliOption.shortening(), field);
                }
                map.put(("".equals(cliOption.name()) ? field.getName() : cliOption.name()), field);
            }

            clazzOptions.put(this.getClass(), map);
        }
    }

    public Set<Field> options() {
        Set<Field> set = new HashSet<>();
        for (Field field : map.values()) {
            set.add(field);
        }
        return set;
    }

    public Field option(String str) {
        return map.get(str);
    }

    public boolean containOption(String str) {
        return map.containsKey(str);
    }
}
