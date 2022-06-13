package com.github.superzhc.common.file.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.github.superzhc.common.file.setting.FileReaderSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/6/13 15:24
 **/
public class ExcelReader {
    private static final Logger log = LoggerFactory.getLogger(ExcelReader.class);

    public static class ExcelReaderSetting extends FileReaderSetting<Map<Integer, String>> {
        private Integer sheetNumber;
        private String sheetName;
        /* 标题头，默认有且为第一行，若无标题头，需将该参数设置为 0 */
        private Integer headRowNumber = 1;
        private Function<List<Map<Integer, String>>, Object> headFunction = null;
        /* 指定结束的行号，默认为0，即读取全部内容完成后结束 */
        private Integer endRowNumber = 0;

        public Integer getSheetNumber() {
            return sheetNumber;
        }

        public ExcelReaderSetting setSheetNumber(Integer sheetNumber) {
            this.sheetNumber = sheetNumber;
            return this;
        }

        public String getSheetName() {
            return sheetName;
        }

        public ExcelReaderSetting setSheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public Integer getHeadRowNumber() {
            return headRowNumber;
        }

        public ExcelReaderSetting setHeadRowNumber(Integer headRowNumber) {
            this.headRowNumber = headRowNumber;
            return this;
        }

        public Function<List<Map<Integer, String>>, Object> getHeadFunction() {
            return headFunction;
        }

        public ExcelReaderSetting setHeadFunction(Function<List<Map<Integer, String>>, Object> headFunction) {
            this.headFunction = headFunction;
            return this;
        }

        public Integer getEndRowNumber() {
            return endRowNumber;
        }

        public ExcelReaderSetting setEndRowNumber(Integer endRowNumber) {
            this.endRowNumber = endRowNumber;
            return this;
        }

        @Override
        public ExcelReaderSetting setPath(String path) {
            super.setPath(path);
            return this;
        }

        @Override
        public ExcelReaderSetting setCharset(String charset) {
            super.setCharset(charset);
            return this;
        }

        @Override
        public ExcelReaderSetting setBatchSize(Integer batchSize) {
            super.setBatchSize(batchSize);
            return this;
        }

        @Override
        public ExcelReaderSetting setDataFunction(Function<List<Map<Integer, String>>, Object> dataFunction) {
            super.setDataFunction(dataFunction);
            return this;
        }
    }

    public static void preview(String path, Integer sheetNumber) {
        preview(path, sheetNumber, 20);
    }

    public static void preview(String path, Integer sheetNumber, Integer number) {
        Function<List<Map<Integer, String>>, Object> printFun = new Function<List<Map<Integer, String>>, Object>() {
            @Override
            public Object apply(List<Map<Integer, String>> maps) {
                for (Map<Integer, String> map : maps) {
                    StringBuilder sb = new StringBuilder("|");
                    for (Map.Entry<Integer, String> entry : map.entrySet()) {
                        sb.append(entry.getValue()).append("|");
                    }
                    System.out.println(sb);
                }
                return null;
            }
        };

        ExcelReaderSetting setting = new ExcelReaderSetting();
        setting.setPath(path).setSheetNumber(sheetNumber)
                .setBatchSize(number).setEndRowNumber(number + setting.getHeadRowNumber())
                .setHeadFunction(printFun)
                .setDataFunction(printFun)
        ;

        read(setting);
    }

    public static List<Map<Integer, String>> read(String path, String sheetName) {
        ExcelReaderSetting setting = new ExcelReaderSetting();
        setting.setPath(path).setSheetName(sheetName).setBatchSize(1000);

        final List<Map<Integer, String>> content = new ArrayList<>();
        setting.setDataFunction(new Function<List<Map<Integer, String>>, Object>() {
            @Override
            public Object apply(List<Map<Integer, String>> maps) {
                content.addAll(maps);
                return null;
            }
        });

        read(setting);

        return content;
    }

    public static List<Map<Integer, String>> read(String path, Integer sheetNumber) {
        ExcelReaderSetting setting = new ExcelReaderSetting();
        setting.setPath(path).setSheetNumber(sheetNumber);

        final List<Map<Integer, String>> content = new ArrayList<>();
        setting.setDataFunction(new Function<List<Map<Integer, String>>, Object>() {
            @Override
            public Object apply(List<Map<Integer, String>> maps) {
                content.addAll(maps);
                return null;
            }
        });

        read(setting);

        return content;
    }

    public static void read(final ExcelReaderSetting setting) {
        if (null == setting.getSheetNumber() && (null == setting.getSheetName() || setting.getSheetName().trim().length() == 0)) {
            throw new IllegalArgumentException("请设置读取的 sheet 的编号或者名称");
        }

        EasyExcel.read(setting.getPath(), new ReadListener<Map<Integer, String>>() {
                    private List<Map<Integer, String>> headContainer = new ArrayList<>(0 == setting.getHeadRowNumber() ? 1 : setting.getHeadRowNumber());
                    private List<Map<Integer, String>> dataContainer = new ArrayList<>(setting.getBatchSize());
                    private Long currentRowNum = 0L;
                    private Integer currentBatchNumer = 0;
                    private Long start0;
                    private Long start = null;

                    @Override
                    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
                        if (currentBatchNumer == 0L) {
                            start = System.currentTimeMillis();
                            start0 = start;
                        }
                        currentRowNum++;

                        Map<Integer, String> headMap2 = new HashMap<>();
                        for (Map.Entry<Integer, ReadCellData<?>> entry : headMap.entrySet()) {
                            headMap2.put(entry.getKey(), entry.getValue().getStringValue());
                        }
                        headContainer.add(headMap2);

                        if (headContainer.size() == setting.getHeadRowNumber()) {
                            if (null != setting.getHeadFunction()) {
                                Object result = setting.getHeadFunction().apply(headContainer);

                                Long end = System.currentTimeMillis();
                                log.debug("Excel 文件处理中，Sheet[{}] 的 Header 处理耗时：{}s，处理行数：{}，处理结果：{}"
                                        , context.readSheetHolder().getSheetName()
                                        , (end - start) / 1000.0
                                        , headContainer.size(), result);
                            } else {
                                log.debug("Excel 文件处理中，Sheet[{}] 的 Header 行数：{}，未处理数据：{}"
                                        , context.readSheetHolder().getSheetName()
                                        , headContainer.size(), headContainer);
                            }
                            start = null;
                        }
                    }

                    @Override
                    public void invoke(Map<Integer, String> data, AnalysisContext context) {
                        if (null == start) {
                            start = System.currentTimeMillis();
                        }

                        dataContainer.add(data);
                        currentRowNum++;
                        currentBatchNumer++;

                        if (currentBatchNumer >= setting.getBatchSize()) {
                            Object result = setting.getDataFunction().apply(dataContainer);
                            currentBatchNumer = 0;
                            dataContainer.clear();

                            Long end = System.currentTimeMillis();
                            log.debug("Excel 文件处理中，Sheet[{}] 处理行[{} ~ {}]，处理耗时：{}s，处理结果：{}"
                                    , context.readSheetHolder().getSheetName()
                                    , (currentRowNum - setting.getBatchSize() + 1), currentRowNum
                                    , (end - start) / 1000.0
                                    , result);
                            start = end;
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        if (currentBatchNumer > 0) {
                            Object result = setting.getDataFunction().apply(dataContainer);
                            Long end = System.currentTimeMillis();
                            log.debug("Excel 文件处理中，Sheet[{}] 处理行[{} ~ {}]，处理耗时：{}s，处理结果：{}"
                                    , context.readSheetHolder().getSheetName()
                                    , (currentRowNum - currentBatchNumer + 1), currentRowNum
                                    , (end - start) / 1000.0
                                    , result);
                            start = end;
                        }

                        Long end = System.currentTimeMillis();
                        log.debug("Excel 文件处理完成，Sheet[{}] 处理总行数：{}，处理总耗时：{}s"
                                , context.readSheetHolder().getSheetName()
                                , currentRowNum
                                , (end - start0) / 1000.0);
                    }

                    @Override
                    public boolean hasNext(AnalysisContext context) {
                        return setting.getEndRowNumber() < 1 || context.readRowHolder().getRowIndex() <= setting.getEndRowNumber();
                    }
                })
                .headRowNumber(setting.getHeadRowNumber())
                .sheet(setting.getSheetNumber(), setting.getSheetName())
                .doRead();
    }

    public static void main(String[] args) {
        String path = "E:\\downloads\\累计入园人数_2022-03-24_10-37-45-104.xlsx";

        preview(path, 0);
    }
}
