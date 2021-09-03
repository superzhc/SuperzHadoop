package com.github.superzhc.db.transform;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author superz
 * @create 2021/8/5 9:27
 */
public abstract class Transform {
    public static class DataMetric {
        private Long total = 0L;
        private Long success = 0L;
        private Long fail = 0L;

        public void add() {
            total++;
        }

        public void success() {
            success++;
        }

        public void fail() {
            fail++;
        }

        public Double successRate() {
            if (total == 0) {
                return 100.0;
            }
            return Double.longBitsToDouble(success) / total;
        }

        public Double failRate() {
            if (total == 0) {
                return 100.0;
            }
            return Double.longBitsToDouble(fail) / total;
        }

        public Long getTotal() {
            return total;
        }

        public Long getSuccess() {
            return success;
        }

        public Long getFail() {
            return fail;
        }
    }

    public void dealLine(String path) throws Exception {
        dealLine(path, "UTF-8");
    }

    public void dealLine(String path, String encoding) throws Exception {
        // 对于常规的reader方式，在大文件的情况下会报堆内存溢出；使用如下的方式可以避免一次性将文件全部读到内存中
        InputStream in = new FileInputStream(path);
        Scanner sc = new Scanner(in, encoding);

        DataMetric metric = new DataMetric();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            // System.out.println(line);
            metric.add();
            int result = transform(line);
            if (result > 0) {
                metric.success();
            } else {
                metric.fail();
            }

            if (metric.getTotal() % 100 == 0) {
                System.out.println("<"+metric.getTotal()+">");
            }
            System.out.print(".");
        }

        System.out.println();
        System.out.print("数据总条数：" + metric.getTotal());
        System.out.print("成功率：" + metric.successRate() + "，失败率：" + metric.failRate());
    }

    public void dealLineBatch(String path, String encoding, Integer number) throws Exception {
        // 对于常规的reader方式，在大文件的情况下会报堆内存溢出；使用如下的方式可以避免一次性将文件全部读到内存中
        InputStream in = new FileInputStream(path);
        Scanner sc = new Scanner(in, encoding);

        DataMetric metric = new DataMetric();
        String[] datas = new String[number];
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            metric.add();
            datas[(int) (metric.getTotal() % number)] = line;
            if (metric.getTotal() % number == 0) {
                int[] result = transformBatch(datas);
                for (int i : result) {
                    if (i > 0) {
                        metric.success();
                    } else {
                        metric.fail();
                    }
                }

                System.out.println("批次" + metric.getTotal() / number + "完成");
            }
        }

        System.out.println();
        System.out.print("数据总条数：" + metric.getTotal());
        System.out.print("成功率：" + metric.successRate() + "，失败率：" + metric.failRate());
    }

    public abstract int transform(String data);

    /**
     * 批处理，默认直接使用transform，如果有公用连接，建议重写
     * @param datas
     * @return
     */
    public int[] transformBatch(String[] datas) {
        int[] result = new int[datas.length];
        for (int i = 0, len = datas.length; i < len; i++) {
            result[i] = transform(datas[i]);
        }
        return result;
    }
}
