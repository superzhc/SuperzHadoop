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
public class PointData2 {
    @ExcelProperty(index = 0)
    private Integer id;
    @ExcelProperty(index = 1)
    private String location;
    @ExcelProperty(index = 2)
    private String scene;
    @ExcelProperty(index = 7)
    private String jixiang;
    @ExcelProperty(index = 8)
    private String baoganxiang;
    @ExcelProperty(index = 9)
    private String ganjian;
    @ExcelProperty(index = 10)
    private Integer number;
    @ExcelProperty(index = 11)
    private String unit;
    @ExcelProperty(index = 12)
    private String ip;
    @ExcelProperty(index = 13)
    private String mask;
    @ExcelProperty(index = 14)
    private String gateway;
    @ExcelProperty(index = 15)
    private String newIp;
    @ExcelProperty(index = 16)
    private String newMask;
    @ExcelProperty(index = 17)
    private String newGateway;
}
