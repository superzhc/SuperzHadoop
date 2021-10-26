package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import com.github.javafaker.Faker;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author superz
 * @create 2021/10/25 18:22
 */
public class JavaFakerSource extends RichSourceFunction<String> {
    private static final String DEFAULT_LOCALE = "zh-CN";
    private static final Long DEFAULT_ROW_PER_SECOND = 1L;
    private static final Long DEFAULT_NUMBER_OF_ROWS = -1L;
    private static final String PREFIX = "fields";
    private static final String SUFFIX = "expression";

    private volatile boolean cancelled = false;

    private Map<String, String> expressions;
    private String locale;
    private Long rowPerSecond;
    private Long numberOfRows;

    Faker faker;
    ObjectMapper mapper;
    private String[] fields;
    private String[] expressionArr;

    public JavaFakerSource(Map<String, String> expressions) {
        this(expressions, DEFAULT_LOCALE);
    }

    public JavaFakerSource(Map<String, String> expressions, String locale) {
        this(expressions, locale, DEFAULT_ROW_PER_SECOND);
    }

    public JavaFakerSource(Map<String, String> expressions, Long rowPerSecond) {
        this(expressions, DEFAULT_LOCALE, DEFAULT_ROW_PER_SECOND);
    }

    public JavaFakerSource(Map<String, String> expressions, String locale, Long rowPerSecond) {
        this(expressions, locale, rowPerSecond, DEFAULT_NUMBER_OF_ROWS);
    }

    public JavaFakerSource(Map<String, String> expressions, String locale, Long rowsPerSecond, Long numberOfRows) {
        this.locale = locale;
        this.expressions = expressions;
        this.rowPerSecond = rowsPerSecond;
        this.numberOfRows = numberOfRows;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        this.faker = new Faker(new Locale(locale));
        this.mapper = new ObjectMapper();
        /* 解析 expressions 并验证表达式 */
        fields = new String[expressions.size()];
        expressionArr = new String[expressions.size()];

        //valid
        int i = 0;
        for (Map.Entry<String, String> entry : expressions.entrySet()) {
            // fields.<column_name>.expression
            String field = entry.getKey().substring(PREFIX.length() + 1).substring(0, entry.getKey().length() - (PREFIX.length() + 1) - (SUFFIX.length() + 1));
            fields[i] = field;
            String expression = entry.getValue();
            try {
                faker.expression(expression);
            } catch (Exception e) {
                throw new IllegalArgumentException("field " + field + " expression must config");
            }
            expressionArr[i] = expression;
            i++;
        }
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        long rowsSoFar = 0;
        long nextReadTime = System.currentTimeMillis();
        while (!cancelled && (numberOfRows == -1L || rowsSoFar < numberOfRows)) {
            for (int j = 0; j < rowPerSecond; j++) {
                if (!cancelled && (numberOfRows == -1L || rowsSoFar < numberOfRows)) {
                    ObjectNode objectNode = mapper.createObjectNode();
                    for (int i = 0, len = fields.length; i < len; i++) {
                        objectNode.put(fields[i], faker.expression(expressionArr[i]));
                    }
                    ctx.collect(mapper.writeValueAsString(objectNode));
                    rowsSoFar++;
                }
            }

            nextReadTime += 1000;
            long toWaitMs = Math.max(0, nextReadTime - System.currentTimeMillis());
            Thread.sleep(toWaitMs);
        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        Map<String, String> map = new HashMap<>();
        map.put("fields.name.expression", "#{Name.name}");
        map.put("fields.age.expression", "#{number.number_between '1','80'}");
        map.put("fields.date.expression", "#{date.past '5','SECONDS'}");
        env.addSource(new JavaFakerSource(map)).print();
        env.execute("javafaker source");
    }
}
