package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import com.github.javafaker.Faker;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 带分区的 javafaker 数据源
 *
 * @author superz
 * @create 2021/11/4 14:59
 */
public class JavaFakerParallelSource extends RichParallelSourceFunction<String> {
    private static final String DEFAULT_LOCALE = "zh-CN";
    private static final Long DEFAULT_ROWS_PER_SECOND = 8L;
    private static final Long DEFAULT_NUMBER_OF_ROWS = -1L;
    /* 2021年11月2日 superz add 新增数据所在分区字段 */
    private static final String DEFAULT_PARTITION_NAME = "partition";
    private static final String PREFIX = "fields";
    private static final String SUFFIX = "expression";

    private volatile boolean cancelled = false;

    private Map<String, String> expressions;
    private String locale;
    private Long rowsPerSecond;
    private Long numberOfRows;

    Faker faker;
    ObjectMapper mapper;
    private String[] fields;
    private String[] expressionArr;

    public JavaFakerParallelSource(Map<String, String> expressions) {
        this(expressions, DEFAULT_LOCALE);
    }

    public JavaFakerParallelSource(Map<String, String> expressions, String locale) {
        this(expressions, locale, DEFAULT_ROWS_PER_SECOND);
    }

    public JavaFakerParallelSource(Map<String, String> expressions, Long rowsPerSecond) {
        this(expressions, DEFAULT_LOCALE, rowsPerSecond);
    }

    public JavaFakerParallelSource(Map<String, String> expressions, String locale, Long rowsPerSecond) {
        this(expressions, locale, rowsPerSecond, DEFAULT_NUMBER_OF_ROWS);
    }

    public JavaFakerParallelSource(Map<String, String> expressions, String locale, Long rowsPerSecond, Long numberOfRows) {
        this.locale = locale;
        this.expressions = expressions;
        this.rowsPerSecond = rowsPerSecond;
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

        Long numberOfRowsForSubTask = getRowsForThisSubTask();
        Long rowsPerSecondForSubTask = getRowsPerSecondForSubTask();
        while (!cancelled && (numberOfRowsForSubTask == -1L || rowsSoFar < numberOfRowsForSubTask)) {
            for (int j = 0; j < rowsPerSecondForSubTask; j++) {
                if (!cancelled && (numberOfRowsForSubTask == -1L || rowsSoFar < numberOfRowsForSubTask)) {
                    ObjectNode objectNode = mapper.createObjectNode();

                    /* 分区字段，分区是从 0 开始的，加 1 是为了个直观的查看 */
                    objectNode.put(DEFAULT_PARTITION_NAME, getRuntimeContext().getIndexOfThisSubtask() + 1);

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

    private long getRowsPerSecondForSubTask() {
        /* 获取子任务的分区数 */
        int numSubtasks = getRuntimeContext().getNumberOfParallelSubtasks();
        int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
        /* 每个任务每秒产生的数据数 */
        long baseRowsPerSecondPerSubtask = rowsPerSecond / numSubtasks;
        return (rowsPerSecond % numSubtasks > indexOfThisSubtask)
                ? baseRowsPerSecondPerSubtask + 1
                : baseRowsPerSecondPerSubtask;
    }

    private long getRowsForThisSubTask() {
        if (numberOfRows == -1) {
            return numberOfRows;
        } else {
            int numSubtasks = getRuntimeContext().getNumberOfParallelSubtasks();
            int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
            final long baseNumOfRowsPerSubtask = numberOfRows / numSubtasks;
            return (numberOfRows % numSubtasks > indexOfThisSubtask)
                    ? baseNumOfRowsPerSubtask + 1
                    : baseNumOfRowsPerSubtask;
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Map<String, String> map = new HashMap<>();
        map.put("fields.name.expression", "#{Name.name}");
        map.put("fields.age.expression", "#{number.number_between '1','80'}");
        map.put("fields.date.expression", "#{date.past '5','SECONDS'}");
        env.addSource(new JavaFakerParallelSource(map,2L)).print();
        env.execute("javafaker parallel source");
    }
}
