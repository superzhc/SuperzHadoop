package com.github.superzhc.excel.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * @author superz
 * @create 2021/8/13 10:17
 */
@Data
public class TDeviceInfo {
    @CsvBindByName(column = "device_id")
    private String id;

    @CsvBindByName(column = "device_type")
    private String type;

    @CsvBindByName(column = "device_name")
    private String name;

    @CsvBindByName(column = "device_desc")
    private String desc;

    @CsvBindByName(column = "parent_device_id")
    private String parent;

    @CsvBindByName(column = "regist_type")
    private String registType;

    @CsvBindByName(column = "longitude")
    private Double longitude;

    @CsvBindByName(column = "latitude")
    private Double latitude;

    @CsvBindByName(column = "")
    private String locationAlias;

    @CsvBindByName(column = "regist_time")
    private String registTime;

    @CsvBindByName(column = "device_status")
    private String status;

    @CsvBindByName(column = "cycle")
    private String cycle;

    @CsvBindByName(column = "tags")
    private String tags;

    @CsvBindByName(column = "ota_version")
    private String otaVersion;

    @CsvBindByName(column = "ota_version_description")
    private String otaVersionDescription;

    @CsvBindByName(column = "rsi_id")
    private String rsiId;

    @CsvBindByName(column = "software_version")
    private String softwareVersion;

    @CsvBindByName(column = "hardware_version")
    private String hardwareVersion;

    @CsvBindByName(column = "region_id")
    private String regionId;

    @CsvBindByName(column = "its_region_ids")
    private String itsRegionIds;

    @CsvBindByName(column = "create_time")
    private String createTime;

    @CsvBindByName(column = "update_time")
    private String updateTime;

    @CsvBindByName(column = "create_id")
    private String createId;

    @CsvBindByName(column = "update_id")
    private String updateId;

    @CsvBindByName(column = "del_flag")
    private String delFlag;

    @CsvBindByName(column = "pole_no")
    private String poleNo;
}
