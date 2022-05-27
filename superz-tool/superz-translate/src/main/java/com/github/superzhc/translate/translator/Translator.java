package com.github.superzhc.translate.translator;

/**
 * @author superz
 * @create 2022/5/27 14:14
 **/
public class Translator {
    private static YandexTranslator yandexTranslator = null;

    public static YandexTranslator yandex() {
        if (null == yandexTranslator) {
            yandexTranslator = new YandexTranslator();
        }
        return yandexTranslator;
    }

    public static YandexTranslator yandex(String proxyHost, Integer proxyPort) {
        if (null == yandexTranslator) {
            yandexTranslator = new YandexTranslator(proxyHost, proxyPort);
        }
        return yandexTranslator;
    }
}
