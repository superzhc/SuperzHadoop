package com.github.superzhc.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 点位数据
 * @author superz
 * @create 2021/8/12 17:28
 */
@Data
@ToString
public class PointData {
    @ExcelProperty("序号")
    private Integer id;
    @ExcelProperty("点位位置")
    private String location;
    @ExcelProperty("场景")
    private String scene;
    @ExcelProperty("机箱内设备")
    private String jixiang;
    @ExcelProperty("抱杆箱内设备")
    private String baoganxiang;
    @ExcelProperty("杆件上设备")
    private String ganjian;
    @ExcelProperty("数量")
    private Integer number;
    @ExcelProperty("单位")
    private String unit;
    @ExcelProperty("IP地址")
    private String ip;
    @ExcelProperty("掩码")
    private String mask;
    @ExcelProperty("网关")
    private String gateway;
    @ExcelProperty("新IP地址")
    private String newIp;
    @ExcelProperty("新掩码")
    private String newMask;
    @ExcelProperty("新网关")
    private String newGateway;
}
