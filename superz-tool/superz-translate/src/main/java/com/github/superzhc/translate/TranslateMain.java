package com.github.superzhc.translate;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.translate.translator.DeepLDockerTranslator;
import com.github.superzhc.translate.translator.GoogleTranslator;
import com.github.superzhc.translate.translator.MicrosoftTranslator;

import java.io.*;
import java.util.*;

/**
 * @author superz
 * @create 2022/5/27 9:09
 **/
public class TranslateMain {
    public static void main(String[] args) {
        List<Map<String, String>> datas = new ArrayList<>();

        MicrosoftTranslator translator=new MicrosoftTranslator();
//        GoogleTranslator translator=new GoogleTranslator();
//         DeepLDockerTranslator translator = new DeepLDockerTranslator();
//        YandexTranslate translator=new YandexTranslate();
        try (InputStream in = TranslateMain.class.getResourceAsStream("/data.txt")) {
            try (InputStreamReader reader = new InputStreamReader(in)) {
                try (BufferedReader buffer = new BufferedReader(reader)) {
                    String line = null;
                    while ((line = buffer.readLine()) != null) {
                        String text = translator.translate(line, "ZH", "EN");

                        Map<String, String> data = new LinkedHashMap<>();
                        data.put("key", "");
                        data.put("zh", line);
                        data.put("en", text);
                        datas.add(data);

                        Thread.sleep(1000 * 3);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n\n\n");

        System.out.println(JsonUtils.asString(datas));
    }
}
