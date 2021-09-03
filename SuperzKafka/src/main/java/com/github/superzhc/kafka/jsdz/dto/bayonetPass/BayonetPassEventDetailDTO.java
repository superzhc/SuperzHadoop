package com.github.superzhc.kafka.jsdz.dto.bayonetPass;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 卡口过车数据结构
 *
 * @author guomingliang
 */
@Data
public class BayonetPassEventDetailDTO {

    /**
     * 车道编号
     */
    @JSONField(name = "LaneID")
    private Integer laneId;

    /**
     * 车牌号
     */
    @JSONField(name = "VehicleLicense")
    private String plateNumber;

    /**
     * 车牌颜色 蓝-1;黄-2;白-3;绿-4;黑-5
     */
    @JSONField(name = "VehicleLicenseColor")
    private Integer plateColor;

    /**
     * 车速，单位m/s
     */
    @JSONField(name = "VehicleSpeed")
    private Integer vehicleSpeed;

    /**
     * 车速 千米每小时
     */
    @JSONField(name = "VehicleSpeedKM")
    private Integer vehicleSpeedKM;
    /**
     * 车辆类型
     */
    @JSONField(name = "VehicleType")
    private Integer vehicleType;
    /**
     * 车辆类型说明
     */
    @JSONField(name = "VehicleTypeDesc")
    private String vehicleTypeDesc;

    /**
     * 车身颜色
     */
    @JSONField(name = "VehicleColor")
    private Integer vehicleColor;
}
