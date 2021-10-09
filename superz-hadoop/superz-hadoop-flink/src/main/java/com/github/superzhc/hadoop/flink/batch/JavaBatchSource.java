package com.github.superzhc.hadoop.flink.batch;

import org.apache.flink.api.common.io.FileInputFormat;
import org.apache.flink.api.common.io.InputFormat;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.CsvReader;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.types.StringValue;

import java.util.Collection;
import java.util.Iterator;

/**
 * 批处理数据源
 *
 * @author superz
 * @create 2021/10/9 11:23
 */
public class JavaBatchSource {
    public static void main(String[] args) {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> ds = new JavaBatchCollectionSource(env).fromElements("张三", "李四", "赵六", "鬼脚七", "王八");
    }

    /**
     * 基于文件
     * 1. 读取本地文件：file://path/to/my/textfile
     * 2. 读取 HDFS 文件：hdfs://nnHost:nnPort/path/to/my/textfile
     */
    public static class JavaBatchFileSource {
        private ExecutionEnvironment env;

        public JavaBatchFileSource(ExecutionEnvironment env) {
            this.env = env;
        }

        /**
         * 按行读取文件并将其作为字符串返回
         *
         * @param path
         * @return
         */
        public DataSource<String> readTextFile(String path) {
            return env.readTextFile(path);
        }

        public DataSource<StringValue> readTextFileWithValue(String path) {
            return env.readTextFileWithValue(path);
        }

        public CsvReader readCsvFile(String path) {
            return env.readCsvFile(path);
        }

        /**
         * 读取Csv文件，并将结果转换成元组，此处只列出了3元组，可类似这种进行转换
         */
        public <T1, T2, T3> DataSource<Tuple3<T1, T2, T3>> readCsvFileAndConvertTuple(String path, Class<T1> t1, Class<T2> t2, Class<T3> t3) {
            return env.readCsvFile(path).types(t1, t2, t3);
        }

        // 转换成 Java 实体
//        public void readCsvFileAndConvertPojo(String path){
//            env.readCsvFile(path).pojoType(???);
//        }

        public CsvReader readCsvFileWithIncludeFields(String path, String fields) {
            return env.readCsvFile(path).includeFields(fields);
        }

//        public void readFileOfPrimitives(String path){
//            /*???*/
//            env.readFileOfPrimitives(path,String.class);
//        }
    }

    /**
     * 基于集合
     */
    public static class JavaBatchCollectionSource {
        private ExecutionEnvironment env;

        public JavaBatchCollectionSource(ExecutionEnvironment env) {
            this.env = env;
        }

        public <E> DataSource<E> fromCollection(Collection<E> collection) {
            return env.fromCollection(collection);
        }

//        public <E> DataSource<E> fromCollection(Iterator<E> iter){
//            return env.fromCollection(iter,???);
//        }

        public <E> DataSource<E> fromElements(E... elements) {
            return env.fromElements(elements);
        }

//        public void fromParallelCollection(){
//            env.fromParallelCollection(?,?)
//        }

        public DataSource<Long> generateSequence(long from, long to) {
            return env.generateSequence(from, to);
        }
    }

    /**
     * 通用方法
     */
    public static class JavaBatchCommonSource {
        private ExecutionEnvironment env;

        public JavaBatchCommonSource(ExecutionEnvironment env) {
            this.env = env;
        }

        public <E> DataSource<E> readFile(FileInputFormat<E> fif, String path) {
            return env.readFile(fif, path);
        }

//        public <E> DataSource<E> createInput(InputFormat<E> inputFormat){
//            env.createInput(inputFormat);
//        }
    }
}
