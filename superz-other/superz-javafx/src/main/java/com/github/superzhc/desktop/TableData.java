package com.github.superzhc.desktop;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author superz
 * @create 2022/7/14 1:28
 */
public class TableData {
    private SimpleStringProperty field1=new SimpleStringProperty();
    private SimpleStringProperty field2=new SimpleStringProperty();

    public TableData(String field1,String field2){
        setField1(field1);
        setField2(field2);
    }

    public String getField1() {
        return field1.get();
    }

    public SimpleStringProperty field1Property() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1.set(field1);
    }

    public String getField2() {
        return field2.get();
    }

    public SimpleStringProperty field2Property() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2.set(field2);
    }
}
