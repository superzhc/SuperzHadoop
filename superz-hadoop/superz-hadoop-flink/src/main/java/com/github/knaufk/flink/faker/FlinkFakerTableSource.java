package com.github.knaufk.flink.faker;

import static com.github.knaufk.flink.faker.FlinkFakerTableSourceFactory.UNLIMITED_ROWS;

import java.util.Arrays;

import com.github.javafaker.Faker;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.source.*;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.LogicalType;

public class FlinkFakerTableSource implements ScanTableSource, LookupTableSource {

  private String[][] fieldExpressions;
  private Float[] fieldNullRates;
  private Integer[] fieldCollectionLengths;
  private TableSchema schema;
  private final LogicalType[] types;
  private long rowsPerSecond;
  private long numberOfRows;
  private String locale;

  public FlinkFakerTableSource(
          String[][] fieldExpressions,
          Float[] fieldNullRates,
          Integer[] fieldCollectionLengths,
          TableSchema schema,
          long rowsPerSecond,
          long numberOfRows,
          String locale) {
    this.fieldExpressions = fieldExpressions;
    this.fieldNullRates = fieldNullRates;
    this.fieldCollectionLengths = fieldCollectionLengths;
    this.schema = schema;
    types =
        Arrays.stream(schema.getFieldDataTypes())
            .map(DataType::getLogicalType)
            .toArray(LogicalType[]::new);
    this.rowsPerSecond = rowsPerSecond;
    this.numberOfRows = numberOfRows;
    this.locale=locale;
  }

  @Override
  public ChangelogMode getChangelogMode() {
    return ChangelogMode.insertOnly();
  }

  /**
   * ScanTableSource 能够扫描外部系统中的所有或部分数据，并且支持谓词下推、分区下推之类的特性
   * @param scanContext
   * @return
   */
  @Override
  public ScanRuntimeProvider getScanRuntimeProvider(final ScanContext scanContext) {
    boolean isBounded = numberOfRows != UNLIMITED_ROWS;
    return SourceFunctionProvider.of(
        new FlinkFakerSourceFunction(
            locale,
            fieldExpressions,
            fieldNullRates,
            fieldCollectionLengths,
            types,
            rowsPerSecond,
            numberOfRows),
        isBounded);
  }

  @Override
  public DynamicTableSource copy() {
    return new FlinkFakerTableSource(
        fieldExpressions,
        fieldNullRates,
        fieldCollectionLengths,
        schema,
        rowsPerSecond,
        numberOfRows,
        locale);
  }

  @Override
  public String asSummaryString() {
    return "FlinkFakerSource";
  }

  /**
   * LookupTableSource 不会感知到外部系统中数据的全貌，而是根据一个或者多个key去执行点查询并返回结果
   * @param context
   * @return
   */
  @Override
  public LookupRuntimeProvider getLookupRuntimeProvider(LookupContext context) {
    return TableFunctionProvider.of(
        new FlinkFakerLookupFunction(
                locale,fieldExpressions, fieldNullRates, fieldCollectionLengths, types, context.getKeys()));
  }
}
