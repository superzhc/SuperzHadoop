package com.github.superzhc.hadoop.hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Map;

/**
 * @author superz
 * @create 2021/11/16 17:01
 */
public class HBaseCRUD {


    /**
     * 一般情况新增数据是向同一个列簇中添加数据
     * @param table
     * @param rowKey
     * @param columnFamily
     * @param qualifiersAndValues
     */
    public void put(String table, String rowKey, String columnFamily, Map<String,String> qualifiersAndValues /*String qualifier, String value*/) {

        // 构建 Put 对象
        Put put = new Put(Bytes.toBytes(rowKey));
        for(Map.Entry<String,String> qualifierAndValue :qualifiersAndValues.entrySet()){
            put=addColumn(put,columnFamily,qualifierAndValue.getKey(), qualifierAndValue.getValue());
        }
    }

    private Put addColumn(Put put, String columnFamily, String qualifier, String value) {
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        return put;
    }

    public void get(String table, String rowKey) {
    }

    public void scan(String table) {
    }

    public void delete(String table, String rowKey, String familyAndQualifier) {
    }
}
