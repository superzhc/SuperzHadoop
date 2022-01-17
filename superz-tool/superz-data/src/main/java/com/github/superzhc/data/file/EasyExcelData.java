package com.github.superzhc.data.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.FileData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/1/17 13:55
 */
public class EasyExcelData implements FileData {
    private static final Logger log = LoggerFactory.getLogger(EasyExcelData.class);

    private static class PrintReadListener implements ReadListener<Map<Integer, String>> {
        private Integer end = -1;
        private boolean isStart = true;

        public PrintReadListener() {
        }

        public PrintReadListener(Integer end) {
            this.end = end;
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            if (isStart) {
                ReadSheetHolder sheetHolder = context.readSheetHolder();
                System.out.println("Sheet[" + sheetHolder.getSheetName() + "]:");
                isStart = false;
            }

            StringBuilder sb = new StringBuilder("|");
            for (String value : data.values()) {
                sb.append(value).append("|");
            }
            System.out.println(sb);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            isStart = true;
        }

        @Override
        public boolean hasNext(AnalysisContext context) {
            return -1 == end || 0 == end || context.readRowHolder().getRowIndex() <= end;
        }
    }

    private static class CountReadListener implements ReadListener<Map<Integer, String>> {

        private Map<String, Long> counter;

        public CountReadListener() {
            counter = new LinkedHashMap<>();
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            String sheetName = context.readSheetHolder().getSheetName();

            Long number = !counter.containsKey(sheetName) ? 0L : counter.get(sheetName);
            counter.put(sheetName, number + 1);
        }

        /**
         * 注意：每个sheet结束都会调用一次这个方法
         *
         * @param context
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            String sheetName = context.readSheetHolder().getSheetName();
            Long number = counter.get(sheetName);
            System.out.printf("Sheet[%s] count : %d\n", sheetName, number);
        }
    }

    public static class JDBCReadListener implements ReadListener<Map<Integer, String>>, Closeable {

        private String url;
        private String username;
        private String password;
        private String table;
        private String[] columns;

        private JdbcHelper jdbc = null;
        private List<List<Object>> params = null;

        private Integer cursor = 0;

        public JDBCReadListener(String url, String username, String password, String table, String[] columns) {
            this.url = url;
            this.username = username;
            this.password = password;
            this.table = table;
            this.columns = columns;

            init();
        }

        private void init() {
            jdbc = new JdbcHelper(url, username, password);
            params = new ArrayList<>();
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            List<Object> row = new ArrayList<>();
            for (int i = 0, len = data.size(); i < len; i++) {
                row.add(data.get(i));
            }
            params.add(row);
            cursor++;

            if (cursor >= 10000) {
                jdbc.batchUpdate(table, columns, params, 1000);
                cursor = 0;
                params.clear();
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            if (cursor > 0) {
                jdbc.batchUpdate(table, columns, params, 1000);
                cursor = 0;
                params.clear();
            }
        }

        @Override
        public void close() throws IOException {
            if (null != jdbc) {
                jdbc.close();
            }
        }
    }

    private static final Integer DEFAULT_HEADERS = 0;

    private String path;
    private Integer headers;

    public EasyExcelData(String path) {
        this(path, DEFAULT_HEADERS);
    }

    public EasyExcelData(String path, Integer headers) {
        this.path = path;
        this.headers = headers;
    }

    @Override
    public void preview(Integer number) {
        EasyExcel.read(path, new PrintReadListener(number)).headRowNumber(0).doReadAll();
    }

    @Override
    public void count() {
        EasyExcel.read(path, new CountReadListener()).headRowNumber(headers).doReadAll();
    }

    public static void main(String[] args) {
        String path = "D:\\downloads\\tg\\";

        String fileName = "1.xlsx";

        for (int i = 0; i <= 51; i++) {
            if (i == -1) {
                fileName = "1.xlsx";
            } else if (i == 0) {
                fileName = "1 - 副本.xlsx";
            } else {
                fileName = String.format("1 - 副本 (%d).xlsx", i);
            }

            // EasyExcelData fileData = new EasyExcelData(path + fileName);
            // fileData.preview(5);
            // fileData.count();

            String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
            String username = "root";
            String password = "123456";
            String table = "user_info2";
            String[] columns = "ext1,create_time,ext2,ext3,birthday,sex,id_card,username,mobile,address,ext4,ext5".split(",");

            EasyExcel.read(path + fileName, new JDBCReadListener(url, username, password, table, columns)).headRowNumber(1).doReadAll();
        }
    }
}
