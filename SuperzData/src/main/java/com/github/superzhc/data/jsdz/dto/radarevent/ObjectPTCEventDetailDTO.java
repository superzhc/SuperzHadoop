package com.github.superzhc.data.jsdz.dto.radarevent;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author miaoc
 * @since 2020/1/14 13:58
 */
@Data
public class ObjectPTCEventDetailDTO {
    /**
     * 参与者类型
     */
    @JSONField(name = "PtcType")
    private Integer ptcType;
    /**
     * 参与者ID
     */
    @JSONField(name = "PtcID")
    private Integer ptcId;
    /**
     * 数据来源（0:未知、1: 自身信息、2:V2X、3:视频、4:微波雷 达、5:线圈）
     */
    @JSONField(name = "SourceType")
    private Integer sourceType;
    /**
     * 速度
     */
    @JSONField(name = "Speed")
    private Float speed;
    /**
     * 行驶方向
     */
    @JSONField(name = "Heading")
    private Float heading;
    /**
     * 纵向加速度
     */
    @JSONField(name = "LongAccele")
    private Float longAccele;
    /**
     * 横向加速度
     */
    @JSONField(name = "LatAccele")
    private Float latAccele;
    /**
     * 垂直加速度
     */
    @JSONField(name = "VertAccele")
    private Float vertAccele;
    /**
     * 长度
     */
    @JSONField(name = "Length")
    private Double length;
    /**
     * 宽度
     */
    @JSONField(name = "Width")
    private Double width;
    /**
     * 时间戳
     */
    @JSONField(name = "Timestamp")
    private Long timestamp;
    /**
     * 经度
     */
    @JSONField(name = "Longitude")
    private Double longitude;
    /**
     * 纬度
     */
    @JSONField(name = "Latitude")
    private Double latitude;
}
