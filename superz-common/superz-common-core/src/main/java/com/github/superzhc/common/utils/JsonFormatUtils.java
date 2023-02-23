package com.github.superzhc.common.utils;

/**
 * @author superz
 * @create 2022/3/16 10:42
 **/
@Deprecated
public class JsonFormatUtils {
    private static final int INDENT_SPACE = 2;

    private JsonFormatUtils() {
    }

    /**
     * 默认使用 2 个空格缩进符进行格式化
     *
     * @param text
     * @return
     */
    public static String format(String text) {
        return format(text, INDENT_SPACE);
    }

    /**
     * 使用空格进行Json格式化
     *
     * @param text
     * @param space 空格数
     * @return
     */
    public static String format(String text, int space) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < space; i++) {
            sb.append(' ');
        }
        return format(text, sb.toString());
    }

    /**
     * 格式化 Json
     *
     * @param text
     * @param indentation 缩进符
     * @return
     */
    public static String format(String text, String indentation) {
        StringBuilder formattedText = new StringBuilder();
        int indentCount = 0;

        for (char c : text.toCharArray()) {
            if (c == '[' || c == '{') {
                formattedText.append(c);
                formattedText.append('\n');
                indentCount += 1;
                appendIndent(formattedText, indentation, indentCount);
            } else if (c == ',') {
                formattedText.append(c);
                formattedText.append('\n');
                appendIndent(formattedText, indentation, indentCount);
            } else if (c == ']' || c == '}') {
                formattedText.append('\n');
                indentCount -= 1;
                appendIndent(formattedText, indentation, indentCount);
                formattedText.append(c);
            } else if (c == '\n' || c == '\t') {
                // 不做处理
            } else {
                formattedText.append(c);
            }
        }

        return formattedText.toString();
    }

    private static void appendIndent(StringBuilder text, String indentation, int indentCount) {
        for (int i = 0; i < indentCount; ++i) {
            text.append(indentation);
        }
    }
}
