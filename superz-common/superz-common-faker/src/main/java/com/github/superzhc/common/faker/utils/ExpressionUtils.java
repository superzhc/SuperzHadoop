package com.github.superzhc.common.faker.utils;

import com.github.javafaker.Faker;
import com.github.superzhc.common.format.PlaceholderResolver;

import java.util.*;

/**
 * @author superz
 * @create 2022/7/29 11:33
 **/
public class ExpressionUtils {
    private static final String EXPRESSION_PREFIX = "#{";
    private static final String EXPRESSION_SUFFIX = "}";

    private static Faker faker = Faker.instance(new Locale("zh-CN"));

    public static List<String> extractExpressions(String str, boolean standard) {
        List<String> expressions = new ArrayList<>();
        int start = str.indexOf("#{");
        if (start > -1) {
            while (start != -1) {
                int end = str.indexOf("}", start);
                String expression = str.substring(start + 2, end).trim();
                int expressionStart = expression.indexOf("{");
                while (expressionStart != -1) {
                    end = str.indexOf("}", end + 1);
                    expressionStart = expression.indexOf("{", expressionStart + 1);
                }
                expression = str.substring(start + 2, end).trim();
                // 验证表达式是否有效
                try {
                    String expression2 = String.format("%s%s%s", EXPRESSION_PREFIX, expression, EXPRESSION_SUFFIX);
                    faker.expression(expression2);
                    expressions.add(standard ? expression2 : expression);
                } catch (Exception e) {
                    //ignore
                }
                start = str.indexOf("#{", end);
            }
        }
        return expressions;
    }

    public static Map<String, String> build(List<String> expressions, boolean standard) {
        Map<String, String> values = new HashMap<>();
        for (String expression : expressions) {
            if (standard) {
                values.put(expression, faker.expression(expression));
            } else {
                String standardExpression = String.format("%s%s%s", EXPRESSION_PREFIX, expression, EXPRESSION_SUFFIX);
                values.put(expression, faker.expression(standardExpression));
            }
        }
        return values;
    }

    public static String convert(String str, List<String> expressions) {
        Map<String, String> values = new HashMap<>();
        for (String expression : expressions) {
            if (expression.startsWith(EXPRESSION_PREFIX) && expression.endsWith(EXPRESSION_SUFFIX)) {
                values.put(expression.substring(EXPRESSION_PREFIX.length(), expression.length() - EXPRESSION_SUFFIX.length()), faker.expression(expression));
            } else {
                values.put(expression, faker.expression(String.format("%s%s%s", EXPRESSION_PREFIX, expression, EXPRESSION_SUFFIX)));
            }
        }

        PlaceholderResolver resolver = PlaceholderResolver.getResolver(EXPRESSION_PREFIX, EXPRESSION_SUFFIX);
        return resolver.resolveByMap(str, values);
    }

    public static String convert(String str) {
        List<String> expressions = extractExpressions(str, false);
        Map<String, String> values = build(expressions, false);

        PlaceholderResolver resolver = PlaceholderResolver.getResolver(EXPRESSION_PREFIX, EXPRESSION_SUFFIX);
        return resolver.resolveByMap(str, values);
    }
}
