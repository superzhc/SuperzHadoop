package com.github.superzhc.translate.translator;

public interface BaseTranslator {
    default String translate(String text, String source, String destination) {
        return translate0(text, supportLang(source), supportLang(destination));
    }

    String translate0(String text, String source, String destination);

    String supportLang(String lang);
}
