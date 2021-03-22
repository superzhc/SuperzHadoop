package com.github.superzhc.data.jsdz.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 代码描述
 *
 * @author baosulei
 * @since 2020/2/25 19:07
 */
@Data
public class EventDto<T> {
    @JSONField(name = "EventID")
    private String eventId;
    /**
     * 设备ID
     */
    @JSONField(name = "DeviceID")
    private String deviceId;
    /**
     * 设备类型
     */
    @JSONField(name = "DeviceType")
    private String deviceType;
    /**
     * 场景ID，如果是设备驱动事件，则同设备ID，可忽略
     */
    @JSONField(name = "SceneID")
    private String sceneId;
    /**
     * 场景类型，如果是设备驱动事件，则同设备类型，可忽略
     */
    @JSONField(name = "SceneType")
    private String sceneType;
    @JSONField(name = "RegionID")
    private Integer regionId;
    @JSONField(name = "Timestamp")
    private Long timestamp;
    @JSONField(name = "Longitude")
    private Double longitude;
    @JSONField(name = "Latitude")
    private Double latitude;
    @JSONField(name = "AttachCount")
    private Integer attachCount;
    @JSONField(name = "EventDetail")
    private T eventDetail;
}
