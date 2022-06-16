package com.github.superzhc.common.format;

/**
 * @author superz
 * @create 2022/6/16 0:53
 */
public class LogFormat {
    private static final PlaceholderResolver resolver = PlaceholderResolver.getResolver("{", "}");

    public static String format(String content, Object... params) {
        if (null == params || params.length == 0) {
            return content;
        }

        return resolver.resolve(content, params);
    }
}
