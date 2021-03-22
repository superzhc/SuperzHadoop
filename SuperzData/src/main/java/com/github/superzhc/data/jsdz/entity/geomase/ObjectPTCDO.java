package com.github.superzhc.data.jsdz.entity.geomase;

import lombok.Data;

import java.util.Date;

/**
 * @author yaoyujing
 * @since 2020/8/27 14:45
 */
@Data
public class ObjectPTCDO {
    /**
     * 时间戳
     */
    private Date timestamp;
    /**
     * 坐标点，Point(lon lat)
     */
    private String locat;

    private String eventId;
    private String deviceId;

    private Double longitude;
    private Double latitude;
    /**
     * 区域id
     */
    private Integer regionId;
    private String deviceType;
    private Integer attachCount;

    private Integer ptcType;
    private Integer ptcId;
    private Integer sourceType;
    private Float speed;
    private Float heading;
    private Float longAccele;
    private Float latAccele;
    private Float vertAccele;
    private Double length;
    private Double width;
    private Double ptcLongitude;
    private Double ptcLatitude;

    /**
     * 车型--检测配置计算结果
     */
    private Integer modelType;
}
