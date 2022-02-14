package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.FileData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/1/17 11:41
 */
public class FileDataMain {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        String path = "D:\\downloads\\tg\\";

        String fileName = "chinese_patients.csv";//"副本（1）70万.xlsx";//"副本70万.xlsx";

        path = path + fileName;

        FileData fileData = FileData.file(path);
        fileData.preview();
        fileData.count();

//        final List<Integer> errorLineNumbers = new ArrayList<>();
//        FileData.FileReadSetting errorLinesSettings = new FileData.FileReadSetting(100000, new Function<List<Object>, Boolean>() {
//            @Override
//            public Boolean apply(List<Object> objects) {
//                for (Object obj : objects) {
//                    String str = (String) obj;
//                    String[] ss = str.split(":");
//                    errorLineNumbers.add(Integer.parseInt(ss[0]));
//                }
//                return true;
//            }
//        });
//        fileData.read(errorLinesSettings);
//
//        try (final JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
//            final ErrorData errorData = new ErrorData("jd");
//
//            String path2 = "D:\\downloads\\tg\\jd.txt";
//            FileData fileData2 = FileData.file(path2);
//            FileData.FileReadSetting originSettings = new FileData.FileReadSetting(100000, new Function<List<Object>, Boolean>() {
//                int cursor = 1;
//
//                @Override
//                public Boolean apply(List<Object> objects) {
//                    for (Object data : objects) {
//                        if (errorLineNumbers.contains(cursor)) {
//                            errorData.add((String) data);
//                        }
//                        cursor++;
//                    }
//                    errorData.write2db(jdbc);
//                    return true;
//                }
//            });
//            fileData2.read(originSettings);
//        }
    }
}
