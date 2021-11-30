package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import com.github.javafaker.Faker;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author superz
 * @create 2021/10/25 18:22
 */
public class JavaFakerSource extends RichSourceFunction<String> implements CheckpointedFunction {
    private static final String DEFAULT_LOCALE = "zh-CN";
    private static final Long DEFAULT_ROWS_PER_SECOND = 1L;
    private static final Long DEFAULT_NUMBER_OF_ROWS = -1L;
    /* 2021年11月2日 superz add 新增数据序号字段 */
    private static final String DEFAULT_ID_NAME = "id";
    private static final String PREFIX = "fields";
    private static final String SUFFIX = "expression";

    private volatile boolean cancelled = false;

    private transient ListState<Long> idState;
    private Long idCache;

    private Map<String, String> expressions;
    private String locale;
    private Long rowsPerSecond;
    private Long numberOfRows;

    Faker faker;
    ObjectMapper mapper;
    private String[] fields;
    private String[] expressionArr;

    public JavaFakerSource(Map<String, String> expressions) {
        this(expressions, DEFAULT_LOCALE);
    }

    public JavaFakerSource(Map<String, String> expressions, String locale) {
        this(expressions, locale, DEFAULT_ROWS_PER_SECOND);
    }

    public JavaFakerSource(Map<String, String> expressions, Long rowsPerSecond) {
        this(expressions, DEFAULT_LOCALE, rowsPerSecond);
    }

    public JavaFakerSource(Map<String, String> expressions, String locale, Long rowsPerSecond) {
        this(expressions, locale, rowsPerSecond, DEFAULT_NUMBER_OF_ROWS);
    }

    public JavaFakerSource(Map<String, String> expressions, String locale, Long rowsPerSecond, Long numberOfRows) {
        this.locale = locale;
        this.expressions = expressions;
        this.rowsPerSecond = rowsPerSecond;
        this.numberOfRows = numberOfRows;
        this.idCache = 0L;
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
            for (int j = 0; j < rowsPerSecond; j++) {
                if (!cancelled && (numberOfRows == -1L || rowsSoFar < numberOfRows)) {
                    ObjectNode objectNode = mapper.createObjectNode();

                    // 用户自定义的 id 字段的优先级过于默认的 id 生成
                    objectNode.put(DEFAULT_ID_NAME, ++idCache);

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

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        idState.clear();
        idState.add(idCache);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<Long> idStateDescriptior = new ListStateDescriptor<Long>(DEFAULT_ID_NAME, Long.class);
        idState = context.getOperatorStateStore().getListState(idStateDescriptior);
        for (Long item : idState.get()) {
            this.idCache = item;
        }
    }

    public static String field(String fieldName) {
        return String.format("%s.%s.%s", PREFIX, fieldName, SUFFIX);
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        Map<String, String> fakerConfigs = new HashMap<>();
        //fakerConfigs.put("fields..expression",FakerUtils.Expression);
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.NAME);
        fakerConfigs.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        fakerConfigs.put("fields.id_card.expression",FakerUtils.Expression.ID_CARD);
        fakerConfigs.put("fields.qq.expression",FakerUtils.Expression.QQ);
        fakerConfigs.put("fields.ip.expression",FakerUtils.Expression.IP);
        fakerConfigs.put("fields.plate_number.expression",FakerUtils.Expression.Car.LICESE_PLATE);
        fakerConfigs.put("fields.date.expression", FakerUtils.Expression.pastDate(5));
        env.addSource(new JavaFakerSource(fakerConfigs)).print();
        env.execute("javafaker source");
    }
}
