package com.github.knaufk.flink.faker;

import static org.apache.flink.configuration.ConfigOptions.key;

import com.github.javafaker.Faker;

import java.util.*;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.api.ValidationException;
import org.apache.flink.table.catalog.CatalogTable;
import org.apache.flink.table.factories.DynamicTableSourceFactory;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.LogicalTypeRoot;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.table.utils.TableSchemaUtils;

/**
 * Flink SQL采用Java SPI机制来发现与加载表工厂类，需要在classpath的META-INF/services目录下创建一个名为org.apache.flink.table.factories.Factory的文件，并写入自定义的工厂类的全限定名
 */
public class FlinkFakerTableSourceFactory implements DynamicTableSourceFactory {

  public static final String IDENTIFIER = "faker";

  public static final String FIELDS = "fields";
  public static final String EXPRESSION = "expression";
  public static final String NULL_RATE = "null-rate";
  public static final String COLLECTION_LENGTH = "length";

  public static final Long ROWS_PER_SECOND_DEFAULT_VALUE = 10000L;
  public static final Long UNLIMITED_ROWS = -1L;
  public static final String DEFAULT_LOCALE="zh-CN";

  /**
   * 定义 Connector 需要的各项参数，利用内置的 ConfigOption/ConfigOptions 类来定义
   */
  /* 每秒产生数据的条数 */
  public static final ConfigOption<Long> ROWS_PER_SECOND =
      key("rows-per-second")
          .longType()
          .defaultValue(ROWS_PER_SECOND_DEFAULT_VALUE)
          .withDescription("Rows per second to control the emit rate.");

  /* 要产生的数据数量，若该参数被设置则代表是数据有界，否则则代表是无解的流式数据 */
  public static final ConfigOption<Long> NUMBER_OF_ROWS =
      key("number-of-rows")
          .longType()
          .defaultValue(UNLIMITED_ROWS)
          .withDescription("Total number of rows to emit. By default, the source is unbounded.");

  /* Faker 使用的国际化 */
  public static final ConfigOption<String> LOCALE =
          key("locale")
                  .stringType()
                  .defaultValue(DEFAULT_LOCALE)
                  .withDescription("Faker's Usage with Locales ");

  public static final List<LogicalTypeRoot> SUPPORTED_ROOT_TYPES =
      Arrays.asList(
          LogicalTypeRoot.DOUBLE,
          LogicalTypeRoot.FLOAT,
          LogicalTypeRoot.DECIMAL,
          LogicalTypeRoot.TINYINT,
          LogicalTypeRoot.SMALLINT,
          LogicalTypeRoot.INTEGER,
          LogicalTypeRoot.BIGINT,
          LogicalTypeRoot.CHAR,
          LogicalTypeRoot.VARCHAR,
          LogicalTypeRoot.BOOLEAN,
          LogicalTypeRoot.ARRAY,
          LogicalTypeRoot.MAP,
          LogicalTypeRoot.ROW,
          LogicalTypeRoot.MULTISET,
          LogicalTypeRoot.TIMESTAMP_WITHOUT_TIME_ZONE,
          LogicalTypeRoot.TIMESTAMP_WITH_LOCAL_TIME_ZONE,
          LogicalTypeRoot.TIMESTAMP_WITH_TIME_ZONE);

  public static final List<LogicalTypeRoot> COLLECTION_ROOT_TYPES =
      Arrays.asList(LogicalTypeRoot.ARRAY, LogicalTypeRoot.MAP, LogicalTypeRoot.MULTISET);

  @Override
  public FlinkFakerTableSource createDynamicTableSource(final Context context) {

    CatalogTable catalogTable = context.getCatalogTable();

    Configuration options = new Configuration();
    context.getCatalogTable().getOptions().forEach(options::setString);

    /* 获取定义的表结构信息 */
    TableSchema schema = TableSchemaUtils.getPhysicalSchema(catalogTable.getSchema());
    Float[] fieldNullRates = new Float[schema.getFieldCount()];
    String[][] fieldExpressions = new String[schema.getFieldCount()][];
    Integer[] fieldCollectionLengths = new Integer[schema.getFieldCount()];

    /* 初始化 faker 实例，验证表达式时使用 */
    Faker faker = Faker.instance(new Locale(options.get(LOCALE)));

    for (int i = 0; i < fieldExpressions.length; i++) {
      /* 获取字段名和字段类型，并且验证字段类型 */
      String fieldName = schema.getFieldName(i).get();
      DataType dataType = schema.getFieldDataType(i).get();
      validateDataType(fieldName, dataType);

      fieldExpressions[i] = readAndValidateFieldExpression(options, fieldName, dataType,faker);
      fieldNullRates[i] = readAndValidateNullRate(options, fieldName);
      fieldCollectionLengths[i] = readAndValidateCollectionLength(options, fieldName, dataType);
    }
    return new FlinkFakerTableSource(
        fieldExpressions,
        fieldNullRates,
        fieldCollectionLengths,
        schema,
        options.get(ROWS_PER_SECOND),
        options.get(NUMBER_OF_ROWS),
        options.get(LOCALE));
  }

  private Integer readAndValidateCollectionLength(
      Configuration options, String fieldName, DataType dataType) {
    ConfigOption<Integer> collectionLength =
        key(FIELDS + "." + fieldName + "." + COLLECTION_LENGTH).intType().defaultValue(1);
    Integer fieldCollectionLength = options.get(collectionLength);

    if (fieldCollectionLength != 1
        && !COLLECTION_ROOT_TYPES.contains(dataType.getLogicalType().getTypeRoot())) {
      throw new ValidationException(
          "Collection length may not be set for  "
              + fieldName
              + " with type "
              + dataType.getLogicalType().getTypeRoot().toString());
    }

    if (fieldCollectionLength <= 0
        && COLLECTION_ROOT_TYPES.contains(dataType.getLogicalType().getTypeRoot())) {
      throw new ValidationException(
          "Collection length needs to be positive. Collection length of "
              + fieldName
              + " is "
              + fieldCollectionLength);
    }
    return fieldCollectionLength;
  }

  private Float readAndValidateNullRate(Configuration options, String fieldName) {
    ConfigOption<Float> nullRate =
        key(FIELDS + "." + fieldName + "." + NULL_RATE).floatType().defaultValue(0.0f);

    Float fieldNullRate = options.get(nullRate);

    if (fieldNullRate > 1.0f || fieldNullRate < 0.0f) {
      throw new ValidationException(
          "Null rate needs to be in [0,1]. Null rate of " + fieldName + " is " + fieldNullRate);
    }
    return fieldNullRate;
  }

  private String[] readAndValidateFieldExpression(
      Configuration options, String fieldName, DataType dataType,Faker faker) {
    String[] fieldExpression;

    if (dataType.getLogicalType().getTypeRoot() == LogicalTypeRoot.MAP) {
      // expression is given with key and value
      ConfigOption<String> keyExpression =
          key(FIELDS + "." + fieldName + ".key." + EXPRESSION).stringType().noDefaultValue();
      ConfigOption<String> valueExpression =
          key(FIELDS + "." + fieldName + ".value." + EXPRESSION).stringType().noDefaultValue();
      fieldExpression = new String[] {options.get(keyExpression), options.get(valueExpression)};

    } else if (dataType.getLogicalType().getTypeRoot() == LogicalTypeRoot.ROW) {
      StringBuilder stringBuilder = new StringBuilder();
      List<RowType.RowField> rowFields = ((RowType) dataType.getLogicalType()).getFields();
      fieldExpression = new String[rowFields.size()];
      // expression is given element by element
      for (int i = 0; i < rowFields.size(); i++) {
        ConfigOption<String> rowExpression =
            key(FIELDS + "." + fieldName + "." + rowFields.get(i).getName() + "." + EXPRESSION)
                .stringType()
                .noDefaultValue();
        fieldExpression[i] = options.get(rowExpression);
      }

    } else {
      fieldExpression =
          new String[] {
            options.get(
                key(FIELDS + "." + fieldName + "." + EXPRESSION).stringType().noDefaultValue())
          };
    }

    if (Arrays.asList(fieldExpression).contains(null)) {
      throw new ValidationException(
          "Every column needs a corresponding expression. No expression found for "
              + fieldName
              + ".");
    }

    /**
     * 验证表达式是否符合标准
     */
    try {
      /*Faker faker = new Faker();*/
      for (String expression : fieldExpression) {
        faker.expression(expression);
      }
    } catch (RuntimeException e) {
      throw new ValidationException("Invalid expression for column \"" + fieldName + "\".", e);
    }
    return fieldExpression;
  }

  /**
   * 验证数据类型是否是支持的类型
   * @param fieldName
   * @param dataType
   */
  private void validateDataType(String fieldName, DataType dataType) {
    if (!SUPPORTED_ROOT_TYPES.contains(dataType.getLogicalType().getTypeRoot())) {
      throw new ValidationException(
          "Only "
              + SUPPORTED_ROOT_TYPES
              + " columns are supported by Faker TableSource. "
              + fieldName
              + " is "
              + dataType.getLogicalType().getTypeRoot()
              + ".");
    }
  }

  /**
   * 指定工厂类标识符，该标识符是建表时必须填写 connector 参数的值
   * @return
   */
  @Override
  public String factoryIdentifier() {
    return IDENTIFIER;
  }

  /**
   * 必须参数集合
   * @return
   */
  @Override
  public Set<ConfigOption<?>> requiredOptions() {
    return new HashSet<>();
  }

  /**
   * 可选参数集合
   * @return
   */
  @Override
  public Set<ConfigOption<?>> optionalOptions() {
    Set<ConfigOption<?>> options = new HashSet<>();
    options.add(ROWS_PER_SECOND);
    options.add(NUMBER_OF_ROWS);
    options.add(LOCALE);
    return options;
  }
}
