package com.github.knaufk.flink.faker;

import static com.github.knaufk.flink.faker.FlinkFakerTableSourceFactory.UNLIMITED_ROWS;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.logical.LogicalType;

public class FlinkFakerSourceFunction extends RichParallelSourceFunction<RowData> {

  private volatile boolean cancelled;
  private Faker faker;
  private Random rand;

  private String[][] fieldExpressions;
  private Float[] fieldNullRates;
  private Integer[] fieldCollectionLengths;
  private LogicalType[] types;
  private long rowsPerSecond;
  private long numberOfRows;

  public FlinkFakerSourceFunction(
      String[][] fieldExpressions,
      Float[] fieldNullRates,
      Integer[] fieldCollectionLengths,
      LogicalType[] types,
      long rowsPerSecond,
      long numberOfRows) {
    this.fieldExpressions = fieldExpressions;
    this.fieldNullRates = fieldNullRates;
    this.fieldCollectionLengths = fieldCollectionLengths;
    this.types = types;
    this.rowsPerSecond = rowsPerSecond;
    this.numberOfRows = numberOfRows;
  }

  @Override
  public void open(final Configuration parameters) throws Exception {
    super.open(parameters);
    faker = new Faker(new Locale("zh-CN"));
    rand = new Random();
  }

  @Override
  public void run(final SourceContext<RowData> sourceContext) throws Exception {

    final long rowsForSubtask = getRowsForThisSubTask();
    final long rowsPerSecondForSubtask = getRowsPerSecondForSubTask();
    long rowsSoFar = 0;

    long nextReadTime = System.currentTimeMillis();
    while (!cancelled && rowsSoFar < rowsForSubtask) {
      for (long i = 0; i < rowsPerSecondForSubtask; i++) {
        if (!cancelled && rowsSoFar < rowsForSubtask) {
          RowData row = generateNextRow();
          sourceContext.collect(row);
          rowsSoFar++;
        }
      }
      nextReadTime += 1000;
      long toWaitMs = Math.max(0, nextReadTime - System.currentTimeMillis());
      Thread.sleep(toWaitMs);
    }
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
    if (numberOfRows == UNLIMITED_ROWS) {
      return Long.MAX_VALUE;
    } else {
      int numSubtasks = getRuntimeContext().getNumberOfParallelSubtasks();
      int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
      final long baseNumOfRowsPerSubtask = numberOfRows / numSubtasks;
      return (numberOfRows % numSubtasks > indexOfThisSubtask)
          ? baseNumOfRowsPerSubtask + 1
          : baseNumOfRowsPerSubtask;
    }
  }

  @Override
  public void cancel() {
    cancelled = true;
  }

  @VisibleForTesting
  RowData generateNextRow() {
    GenericRowData row = new GenericRowData(fieldExpressions.length);
    for (int i = 0; i < fieldExpressions.length; i++) {

      float fieldNullRate = fieldNullRates[i];
      if (rand.nextFloat() >= fieldNullRate) {
        List<String> values = new ArrayList<String>();
        for (int j = 0; j < fieldCollectionLengths[i]; j++) {
          for (int k = 0; k < fieldExpressions[i].length; k++) {
            // loop for multiple expressions of one field (like map, row fields)
            values.add(faker.expression(fieldExpressions[i][k]));
          }
        }

        row.setField(
            i, FakerUtils.stringValueToType(values.toArray(new String[values.size()]), types[i]));
      } else {
        row.setField(i, null);
      }
    }
    return row;
  }
}
