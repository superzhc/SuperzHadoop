package com.github.superzhc.common.jdbc.generator.demo;

import com.github.superzhc.common.jdbc.Column;
import com.github.superzhc.common.jdbc.Table;
import com.github.superzhc.common.jdbc.generator.MySQLGenerator;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author superz
 * @create 2022/7/8 15:46
 **/
@Table(name = "data_struct")
public class DataStruct {
    @Column(nullable = false, primaryKey = true, autoIncrement = true)
    private int id;

    @Column(comment = "姓名")
    private String name;

    @Column
    private int age;

    @Column
    private double high;

    @Column(comment = "生日")
    private Date birthday;

    @Column
    private LocalDate createTime;

    @Column
    public String getCol() {
        return null;
    }

    public static void main(String[] args) {
        MySQLGenerator generator = new MySQLGenerator(DataStruct.class);
        System.out.println(generator.generate());
    }
}
