package com.github.superzhc.common.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;

/**
 * @author 小帅丶
 * @className ImageWaterMarkUtil
 * @Description 图片增加水印
 * @Date 2020/8/27-15:30
 **/
public class ImageWaterMarkUtil {
    //默认倾斜角度
    private static Integer degree = -45;
    //透明度
    private static float alpha = 1.0f;
    //默认保存格式
    private static String suffix = "jpg";

    /**
     * 给图片添加图片水印、可设置水印图片位置与旋转角
     * @param iconURL - 水印图片URL路径
     * @param srcURL - 源图片URL路径
     * @return ByteArrayOutputStream 处理后的图片
     */
    public static BufferedImage addImgWaterMarkByURL(String iconURL, String srcURL) throws Exception{
        Image imageIcon = Toolkit.getDefaultToolkit().getImage(iconURL);
        ImageIcon imgIcon = new ImageIcon(imageIcon);
        Image srcImg = Toolkit.getDefaultToolkit().getImage(srcURL);
        ByteArrayOutputStream byteArrayOutputStream = markImageByIconBAO(imgIcon, srcImg, 4, 0);
        BufferedImage bufferedImage = ImageIO.read( new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        return bufferedImage;
    }

    /**
     * 给图片添加图片水印、可设置水印图片位置与旋转角
     * @param iconURL - 水印图片URL路径
     * @param srcURL - 源图片URL路径
     * @param location - 水印位置：1、左上角，2、右上角，3、左下角，4、右下角，5、中间
     * @param degree - 旋转角：顺时针角度
     * @return ByteArrayOutputStream 处理后的图片
     */
    public static BufferedImage addImgWaterMarkByURL(String iconURL, String srcURL,Integer location, Integer degree) throws Exception{
        Image imageIcon = Toolkit.getDefaultToolkit().getImage(iconURL);
        ImageIcon imgIcon = new ImageIcon(imageIcon);
        Image srcImg = Toolkit.getDefaultToolkit().getImage(srcURL);
        ByteArrayOutputStream byteArrayOutputStream = markImageByIconBAO(imgIcon, srcImg, location, degree);
        BufferedImage bufferedImage = ImageIO.read( new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        return bufferedImage;
    }

    /**
     * 给图片添加图片水印、可设置水印图片位置与旋转角
     * @param iconPath - 水印图片本地路径
     * @param srcPath - 源图片本地路径
     * @return ByteArrayOutputStream 处理后的图片
     */
    public static BufferedImage addImgWaterMark(String iconPath, String srcPath) throws Exception{
        Image srcImg = ImageIO.read(new File(srcPath));
        ImageIcon imgIcon = new ImageIcon(iconPath);
        ByteArrayOutputStream byteArrayOutputStream = markImageByIconBAO(imgIcon, srcImg, 4, 0);
        BufferedImage bufferedImage = ImageIO.read( new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        return bufferedImage;
    }
    /**
     * 给图片添加图片水印、可设置水印图片位置与旋转角
     * @param iconPath  - 水印图片本地路径
     * @param srcPath - 源图片本地路径
     * @param location - 水印位置：1、左上角，2、右上角，3、左下角，4、右下角，5、中间
     * @return ByteArrayOutputStream 处理后的图片
     */
    public static BufferedImage addImgWaterMark(String iconPath, String srcPath,Integer location) throws Exception{
        Image srcImg = ImageIO.read(new File(srcPath));
        ImageIcon imgIcon = new ImageIcon(iconPath);
        ByteArrayOutputStream byteArrayOutputStream = markImageByIconBAO(imgIcon, srcImg, location, 0);
        BufferedImage bufferedImage = ImageIO.read( new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        return bufferedImage;
    }
    /**
     * 给图片添加图片水印、可设置水印图片位置与旋转角
     * @param iconPath - 水印图片本地路径
     * @param srcPath - 源图片本地路径
     * @param degree - 旋转角：顺时针角度
     * @return ByteArrayOutputStream 处理后的图片
     */
    public static BufferedImage addImgWaterMark(String iconPath, String srcPath,Integer location, Integer degree) throws Exception{
        Image srcImg = ImageIO.read(new File(srcPath));
        ImageIcon imgIcon = new ImageIcon(iconPath);
        ByteArrayOutputStream byteArrayOutputStream = markImageByIconBAO(imgIcon, srcImg, location, degree);
        BufferedImage bufferedImage = ImageIO.read( new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        return bufferedImage;
    }
    /**
     * 给图片添加图片水印、可设置水印图片位置与旋转角
     * @param imgIcon - 水印图片
     * @param srcImg - 源图片
     * @param location - 水印位置：1、左上角，2、右上角，3、左下角，4、右下角，5、中间
     * @param degree - 旋转角：顺时针角度
     * @return ByteArrayOutputStream 处理后的图片
     */
    private static ByteArrayOutputStream markImageByIconBAO(ImageIcon imgIcon, Image srcImg, Integer location, Integer degree) {
        long startTime = System.currentTimeMillis();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            int width = srcImg.getWidth(null);
            int height = srcImg.getHeight(null);

            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg, 0, 0, width, height, null);
            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
            }
            // 得到Image对象。
            Image syImg = imgIcon.getImage();
            // 透明度
            float alpha = 1.0f;
            int syWidth = syImg.getWidth(null);
            int syHeight = syImg.getHeight(null);
            // 如果水印图片高或宽大于目标图片是做的处理，使水印宽或高等于目标图片的宽高，并且等比例缩放
            int newSyWidth = syWidth;
            int newSyHeight = syHeight;
            if (syWidth > width) {
                //对水印图片进行缩放
                Integer resizeWidth = width;
                double ratioHeight = new BigDecimal((float)width/syWidth).setScale(4,
                        BigDecimal.ROUND_HALF_UP).doubleValue()*syHeight;
                Integer resizeHeight = (int)ratioHeight;
                BufferedImage bufferedImage = zoomImage(imgIcon,resizeWidth,resizeHeight);
                newSyWidth = bufferedImage.getWidth();
                newSyHeight = bufferedImage.getHeight();
                syImg = bufferedImage;
            }
            // 根据位置参数确定坐标位置
            int x = 0, y = 0;
            // location 位置： 1、左上角，2、右上角，3、左下角，4、右下角，5、中间
            switch (location) {
                case 1: break;
                case 2:
                    x = width - newSyWidth;
                    break;
                case 3:
                    y = height - newSyHeight;
                    break;
                case 4:
                    x = width - newSyWidth;
                    y = height - newSyHeight;
                    break;
                case 5:
                    x = (width - newSyWidth) / 2;
                    y = (height - newSyHeight) / 2;
                    break;
                default: break;
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 表示水印图片的位置
            g.drawImage(syImg, x, y, newSyWidth, newSyHeight, null);
            // 水印文件结束
            g.dispose();
            // 生成图片
            ImageIO.write(buffImg, "JPG",outputStream);
            long endTime = System.currentTimeMillis();
            System.out.println("指定水印图片位置图片添加水印...完成，耗时:"+(endTime-startTime)+"ms");
        } catch (Exception e) {
            System.out.println("图片添加水印异常！"+e.getMessage());
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (Exception e) {
                System.out.println("关闭流异常！"+e.getMessage());
            }
        }
        return outputStream;
    }

    /**
     * 图片缩放,w，h为缩放的目标宽度和高度
     * @param imageIcon -ImageIcon
     * @param w - 目标宽度
     * @param h - 目标高度
     */
    private static BufferedImage zoomImage(ImageIcon imageIcon,int w,int h) throws Exception {
        BufferedImage bufImg = new BufferedImage(imageIcon.getIconWidth(),imageIcon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        return zoomImage(bufImg,w,h);
    }

    /**
     * 图片缩放,w，h为缩放的目标宽度和高度
     * @param src - 为源文件目录
     * @param w - 目标宽度
     * @param h - 目标高度
     */
    private static BufferedImage zoomImage(String src,int w,int h) throws Exception {
        File srcFile = new File(src);
        //读取图片
        BufferedImage bufImg = ImageIO.read(srcFile);
        return zoomImage(bufImg,w,h);
    }

    /**
     * 图片缩放,w，h为缩放的目标宽度和高度
     * @param bufImg - BufferedImage
     * @param w -目标宽度
     * @param h - 目标高度
     */
    private static BufferedImage zoomImage(BufferedImage bufImg,int w,int h) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        double wr=0,hr=0;
        BufferedImage bufferedImage = null;
        //设置缩放目标图片模板
        Image Itemp = bufImg.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
        //获取缩放比例
        wr=w*1.0/bufImg.getWidth();
        hr=h*1.0/bufImg.getHeight();

        System.out.println("缩放后的宽"+wr);
        System.out.println("缩放后的高"+hr);

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);
        try {
            //写入缩减后的图片
            ImageIO.write((BufferedImage) Itemp,"png", outputStream);
            ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());
            bufferedImage = ImageIO.read(in);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferedImage;
    }

    /**
     * @description 给图片增加文本水印铺满
     * @param sourceImgPath 源图片路径
     * @param waterMarkContent 水印内容
     * @return void
     */
    public static BufferedImage addFullTextWaterMark(String sourceImgPath, String waterMarkContent){
        return addWaterMark(sourceImgPath, waterMarkContent, null,suffix, degree, alpha,true);
    }
    /**
     * @description 给图片增加文本水印铺满
     * @param sourceImgPath 源图片路径
     * @param waterMarkContent 水印内容
     * @param fileExt 图片格式
     * @return void
     */
    public static BufferedImage addFullTextWaterMark(String sourceImgPath, String waterMarkContent,String fileExt){
        return addWaterMark(sourceImgPath, waterMarkContent, null,fileExt, degree, alpha,true);
    }
    /**
     * @description 给图片增加图片水印铺满
     * @param sourceImgPath 源图片路径
     * @param watermarkPath 水印图片
     * @return void
     */
    public static BufferedImage addFullImgWaterMark(String sourceImgPath, String watermarkPath){
        return addWaterMark(sourceImgPath, null, watermarkPath,suffix, degree, alpha,false);
    }
    /**
     * @description 给图片增加图片水印铺满
     * @param sourceImgPath 源图片路径
     * @param watermarkPath 水印图片
     * @param fileExt 图片格式
     * @return void
     */
    public static BufferedImage addFullImgWaterMark(String sourceImgPath, String watermarkPath,String fileExt){
        return addWaterMark(sourceImgPath, null, watermarkPath,fileExt, degree, alpha,false);
    }
    /**
     * @description 给图片增加文本水印
     * @param sourceImgPath 源图片路径
     * @param waterMarkContent 水印内容
     * @param watermarkPath  水印图片
     * @param fileExt 图片格式
     * @param degreeAngle  设置水印文字的旋转角度 默认-45
     * @param transparency 设置水印透明度 默认为1.0  值越小颜色越浅
     * @return void
     */
    private static BufferedImage addWaterMark(String sourceImgPath,String waterMarkContent,String watermarkPath,String fileExt,Integer degreeAngle,float transparency,boolean isText){
        long startTime = System.currentTimeMillis();
        BufferedImage bufferedImage = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //水印字体，大小
        Font font = new Font("宋体", Font.BOLD, 24);
        //水印颜色
        Color markContentColor = Color.white;
        //设置水印文字的旋转角度
        Integer degree = degreeAngle;
        //设置水印透明度 默认为1.0  值越小颜色越浅
        float alpha = transparency;
        try {
            //得到文件
            File srcImgFile = new File(sourceImgPath);
            //文件转化为图片
            Image srcImg = ImageIO.read(srcImgFile);
            //获取图片的宽
            int srcImgWidth = srcImg.getWidth(null);
            //获取图片的高
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            //得到画笔
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //设置水印颜色
            g.setColor(markContentColor);
            //设置字体
            g.setFont(font);
            //设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            if (null != degree) {
                //设置水印旋转
                g.rotate(Math.toRadians(degree),(double)bufImg.getWidth(),(double)bufImg.getHeight());
            }
            if(isText){
                addTextWaterMarkMethod(g,font,waterMarkContent,srcImgWidth,srcImgHeight);
            } else {
                addFullImgWaterMarkMethod(g,watermarkPath,srcImgWidth,srcImgHeight);
            }
            // 释放资源
            g.dispose();
            // 输出图片
            ImageIO.write(bufImg, fileExt, outputStream);
            bufferedImage = ImageIO.read( new ByteArrayInputStream(outputStream.toByteArray()));
            long endTime = System.currentTimeMillis();
            if(isText){
                System.out.println("文本内容铺满图片...完成,耗时"+(endTime-startTime)+"ms");
            }else{
                System.out.println("水印图片铺满图片...完成,耗时"+(endTime-startTime)+"ms");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        } finally{
            try {
                if(outputStream != null){
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
        return bufferedImage;
    }

    private static void addFullImgWaterMarkMethod(Graphics2D g,String watermarkPath, int srcImgWidth, int srcImgHeight) throws Exception{
        //得到水印文件
        File watermarkImgFile = new File(watermarkPath);
        //文件转化为图片-水印图片
        Image watermarkImg = ImageIO.read(watermarkImgFile);
        //文字水印的宽
        int width = watermarkImg.getWidth(null)/2;
        int rowsNumber = getRowNumber(srcImgHeight,width);
        int columnsNumber =getColumnsNumber(srcImgWidth,width);
        for(int j=0;j<rowsNumber;j++){
            for(int i=0;i<columnsNumber;i++){
                int x = i*width + j*width;
                int y = -i*width + j*width;
                // 表示水印图片的位置
                if(i==0){
                    g.drawImage(watermarkImg, x, y,null);
                }else{
                    g.drawImage(watermarkImg, x+20, y,null);
                }
            }
        }
    }

    private static void addTextWaterMarkMethod(Graphics2D g,Font font,String waterMarkContent,int srcImgWidth,int srcImgHeight) {
        JLabel label = new JLabel(waterMarkContent);
        FontMetrics metrics = label.getFontMetrics(font);
        //文字水印的宽
        int width = metrics.stringWidth(label.getText());
        int rowsNumber = getRowNumber(srcImgHeight,width);
        int columnsNumber =getColumnsNumber(srcImgWidth,width);
        for(int j=0;j<rowsNumber;j++){
            for(int i=0;i<columnsNumber;i++){
                //画出水印,并设置水印位置
                g.drawString(waterMarkContent, i*width + j*width, -i*width + j*width);
            }
        }
    }
    /**
     * @Author 小帅丶
     * @Description 获取行
     * @Date  2020/8/27 17:07
     * @param srcImgHeight 原始的高度
     * @param width 缩放后的宽度
     * @return int
     **/
    private static int getRowNumber(int srcImgHeight, int width) {
        //图片的高  除以  文字水印的宽  打印的行数(以文字水印的宽为间隔)
        int rowsNumber = srcImgHeight/width+srcImgHeight%width;
        //防止图片太小而文字水印太长，所以至少打印一次
        if(rowsNumber < 1){
            rowsNumber = 1;
        }
        return rowsNumber;
    }
    /**
     * @Author 小帅丶
     * @Description 获取列
     * @Date  2020/8/27 17:07
     * @param srcImgWidth 原始的宽度
     * @param width 缩放后的宽度
     * @return int
     **/
    private static int getColumnsNumber(int srcImgWidth, int width) {
        //图片的宽 除以 文字水印的宽  每行打印的列数(以文字水印的宽为间隔)
        int columnsNumber = srcImgWidth/width+srcImgWidth%width;
        //防止图片太小而文字水印太长，所以至少打印一次
        if(columnsNumber < 1){
            columnsNumber = 1;
        }
        return columnsNumber;
    }
}
