package com.github.superzhc.generate;

import java.util.function.Supplier;

/**
 * 生成测试数据
 */
public interface GenerateTestData
{
    public String target();

    /**
     * 生成操作
     * @param supplier 工厂
     * @param quantity 数量
     */
    public void generate(Supplier<String> supplier, long quantity);
}
