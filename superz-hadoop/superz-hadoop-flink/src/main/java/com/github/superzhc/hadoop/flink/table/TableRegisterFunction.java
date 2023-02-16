package com.github.superzhc.hadoop.flink.table;

import com.github.superzhc.hadoop.flink.table.fun.SubstringFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.call;

/**
 * @author superz
 * @create 2023/2/16 17:46
 **/
public class TableRegisterFunction {
    /**
     * 注意：在聚合函数使用新的类型系统前，本节仅适用于标量和表值函数。
     *
     * 函数类
     * 1. 实现类必须继承自合适的基类之一（例如 org.apache.flink.table.functions.ScalarFunction）。
     * 2. 该类必须声明为 public ，而不是 abstract ，并且可以被全局访问。不允许使用非静态内部类或匿名类。
     * 3. 为了将自定义函数存储在持久化的 catalog 中，该类必须具有默认构造器，且在运行时可实例化。
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        TableEnvironment tEnv = StreamTableEnvironment.create(env);

        Table t1 = tEnv.from("MyTable");

        // 方式1：在 Table API 里不经注册直接“内联”调用函数
        t1.select(call(SubstringFunction.class, $("f1"), 5, 10));

        // 方式2：注册函数
        tEnv.createTemporarySystemFunction("SubStringFunction", SubstringFunction.class);
        t1.select(call("SubstringFunction", $("f1"), 5, 10));
        tEnv.sqlQuery("SELECT SubstringFunction(f1, 5, 10) FROM MyTable");
    }
}
