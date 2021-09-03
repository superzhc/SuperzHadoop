package com.github.superzhc.kafka.jsdz.entity.api;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class StatisticsVehiclemodelDO {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 统计日期
     */
    private LocalDate statisticsDate;
    /**
     * 统计时段开始时间
     */
    private Date startTime;
    /**
     * 统计时段结束时间
     */
    private Date endTime;
    /**
     * 车辆类型
     */
    private Integer modelType;
    /**
     * 车辆总数
     */
    private Integer vehicleNum;
    /**
     * 删除标识。0：未删除；1：已删除
     */
    private Object delFlag;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;
}
