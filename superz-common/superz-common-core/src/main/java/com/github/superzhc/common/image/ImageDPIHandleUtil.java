package com.github.superzhc.common.image;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description 图片DPI修改工具类
 * @acknowledge 感谢各位网上大佬分享的代码
 * ProjectName imagetool
 * Created by 小帅丶 on 2022-04-21
 * Version 1.0
 */

public class ImageDPIHandleUtil {
    /*// 默认密度单位 方式
    private static int DEFAULT_DENSITY_UNIT = JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH;
    // 默认质量
    private static float DEFAULT_QUALITY = 1f;


    *//************************* com.sun.image.codec 处理DPI *************************//*
    *//**
     * 改变JPG图片DPI
     * @param file - 原图
     * @param xDensity - 水平分辨率
     * @param yDensity - 垂直分辨率
     *//*
    public static void handleDpiJPG(File file,int xDensity, int yDensity){
        handleDpiJPG(file,DEFAULT_DENSITY_UNIT,xDensity,yDensity,DEFAULT_QUALITY);
    }

    *//**
     * 改变JPG图片DPI
     * @param file - 原图
     * @param density - 密度单位
     * @param xDensity - 水平分辨率
     * @param yDensity - 垂直分辨率
     * @param quality  - 质量水平
     *//*
    public static void handleDpiJPG(File file,int density, int xDensity, int yDensity, float quality) {
        try {
            BufferedImage image = ImageIO.read(file);
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(new FileOutputStream(file));
            JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image);
            jpegEncodeParam.setDensityUnit(density);
            jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
            jpegEncodeParam.setQuality(quality, false);
            jpegEncodeParam.setXDensity(xDensity);
            jpegEncodeParam.setYDensity(yDensity);
            jpegEncoder.encode(image, jpegEncodeParam);
            image.flush();
        } catch (IOException e) {
            System.out.println("handleDpiJPG---->异常:"+e.getMessage());
            e.printStackTrace();
        }
    }
    *//************************* com.sun.image.codec 处理DPI *************************/
}
