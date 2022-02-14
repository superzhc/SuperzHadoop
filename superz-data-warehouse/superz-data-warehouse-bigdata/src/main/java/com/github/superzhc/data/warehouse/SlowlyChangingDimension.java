package com.github.superzhc.data.warehouse;

/**
 * SCD（缓慢变化维度）
 *
 * @author superz
 * @create 2021/12/10 14:40
 */
public class SlowlyChangingDimension {
    /**
     * 类型0：保留原始值
     *
     * 标记为类型0，维度属性值是不发生变化，因此事实始终按照该原始值分组
     */

    /**
     * 类型1：重写
     *
     * 重写维度行中的旧值，以当前值替换。属性始终反映最近的情况。
     *
     * 类型1存在的问题是将会市区属性变化的所有历史记录，因为重写删除了历史属性值，仅保存了当前最新的属性值。
     */

    /**
     * 类型2：增加新行
     */

    /**
     * 类型3：增加新属性
     */

    /**
     * 类型4：增加微型维度
     */

    /**
     * 类型5：微型维度于类型1支架表
     */

    /**
     * 类型6：将类型1属性增加到类型2维度
     */

    /**
     * 类型7：双重类型1与类型2维度
     */
}
