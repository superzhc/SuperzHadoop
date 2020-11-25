package com.github.superzhc.flink.streaming;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.Serializable;

/**
 * 2020年11月20日 superz add
 */
public class JavaReduceOperatorDemo
{
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> text=env.fromElements("1 张三 20","2 李四 21","3 王五 22","1 zhangsan 22","3 wangwu 24","4 zhaoliu 22");
        SingleOutputStreamOperator<Student> students=text.map(new MapFunction<String, Student>()
        {
            @Override public Student map(String value) throws Exception {
                String[] ss=value.split(" ");
                return new Student(Integer.parseInt(ss[0]),ss[1],Integer.parseInt(ss[2]));
            }
        });
        students.print();

        students.keyBy("id")//
                .reduce(new ReduceFunction<Student>()
                {
                    @Override public Student reduce(Student value1, Student value2) throws Exception {
                        int id= (value1.getId()+ value1.getId())/2;
                        String name= value1.getName()+value2.getName();
                        int age=(value1.getAge()+ value2.getAge())/2;
                        return new Student(id,name,age);
                    }
                }).print();

        env.execute("reduce operator demo");
    }

    public static class Student implements Serializable
    {
        private int id;
        private String name;
        private int age;

        /**
         * 默认的无参构造函数必须存在，不然keyBy直接使用属性会报错
         * 报错如下：
         * Exception in thread "main" org.apache.flink.api.common.InvalidProgramException: This type (GenericType<...>) cannot be used as key.
         * 	at org.apache.flink.api.common.operators.Keys$ExpressionKeys.<init>(Keys.java:330)
         */
        public Student(){}

        public Student(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override public String toString() {
            return "Student{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
        }
    }
}
