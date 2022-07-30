package com.github.superzhc.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author superz
 * @create 2022/7/30 11:11
 **/
public class OCRMain {
    public static void main(String[] args) throws Exception {
        ITesseract instance=new Tesseract();
        instance.setDatapath("E:\\SuperzHadoop\\superz-tool\\superz-ocr\\src\\main\\resources"/*OCRMain.class.getResource("/").getPath()*/);
        // instance.setLanguage("chi_sim");
        String result=instance.doOCR(new File("E:\\downloads\\验证码图片\\code20220723003.jpg"));

        System.out.println(result);
    }
}
