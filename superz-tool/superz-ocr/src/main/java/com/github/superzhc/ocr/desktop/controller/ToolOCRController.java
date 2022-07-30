package com.github.superzhc.ocr.desktop.controller;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.javafx.DialogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author superz
 * @create 2022/7/30 14:14
 **/
public class ToolOCRController implements Initializable {
    public static final String FXML_PATH = "../tool_ocr.fxml";

    public static URL getFxmlPath() {
        return ToolOCRController.class.getResource(FXML_PATH);
    }

    private ITesseract instance = null;
    private byte[] bytes = null;

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = new Tesseract();
        instance.setDatapath("E:\\SuperzHadoop\\superz-tool\\superz-ocr\\src\\main\\resources"/*OCRMain.class.getResource("/").getPath()*/);
    }

    public void btnLoadAction(ActionEvent actionEvent) {
        // http://10.90.12.68:4200/hanyun/auth-server/validata/code?deviceId=0.12850592399947147
        String url = "http://10.90.12.68:4200/hanyun/auth-server/validata/code";

        Map<String, String> params = new HashMap<>();
        params.put("deviceId", "0.12850592399947147");

        bytes = HttpRequest.get(url, params).bytes();

        Image image = new Image(new ByteArrayInputStream(bytes));
        imageView.setImage(image);
    }

    public void btnIdentityAction(ActionEvent actionEvent) {
        if (null == bytes) {
            DialogUtils.error("消息", "请先加载图片");
            return;
        }

        try {
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));
            String result = instance.doOCR(bi);
            DialogUtils.info("消息", result);
        } catch (Exception e) {
            DialogUtils.error("消息", "识别失败：" + e.getMessage());
            return;
        }
    }

    public void btnTestAction(ActionEvent actionEvent) {
        String url = "http://10.90.12.68:4200/hanyun/auth-server/oauth/token";

        if (null == bytes) {
            DialogUtils.error("消息", "请先加载图片");
            return;
        }

        String validCode;
        try {
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));
            String result = instance.doOCR(bi);
            validCode = DialogUtils.prompt("消息", "请输入验证码", result);
        } catch (Exception e) {
            validCode = DialogUtils.prompt("消息", "请输入验证码");
        }

        Map<String, String> forms = new HashMap<>();


        String result = HttpRequest.post(url).form(forms).body();
        DialogUtils.prompt("消息", "响应信息", result);
    }
}
