package com.github.superzhc.hadoop.hbase;

import com.github.superzhc.hadoop.hbase.source.HBaseConnection;
import com.github.superzhc.hadoop.hbase.source.impl.Cloud4ControlConfig;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Map;

/**
 * @author superz
 * @create 2021/11/16 17:01
 */
public class HBaseCRUD {

    private HBaseConnection connection;

    public HBaseCRUD(HBaseConnection connection) {
        this.connection = connection;
    }

    public void put(String tableName, String rowKey, Map<String, Map<String, String>> familiesAndQualifiersAndValues) {
        try (Table table = connection.getTable(tableName)) {
            Put put = new Put(Bytes.toBytes(rowKey));
            for (Map.Entry<String, Map<String, String>> familyAndQualifiersAndValues : familiesAndQualifiersAndValues.entrySet()) {
                String columnFamily = familyAndQualifiersAndValues.getKey();
                Map<String, String> qualifiersAndValues = familyAndQualifiersAndValues.getValue();
                for (Map.Entry<String, String> qualifierAndValue : qualifiersAndValues.entrySet()) {
                    put = addColumn(put, columnFamily, qualifierAndValue.getKey(), qualifierAndValue.getValue());
                }
            }

            table.put(put);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 一般情况新增数据是向同一个列簇中添加数据
     *
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @param qualifiersAndValues
     */
    public void put(String tableName, String rowKey, String columnFamily, Map<String, String> qualifiersAndValues /*String qualifier, String value*/) {
        try (Table table = connection.getTable(tableName)) {
            // 构建 Put 对象
            Put put = new Put(Bytes.toBytes(rowKey));
            for (Map.Entry<String, String> qualifierAndValue : qualifiersAndValues.entrySet()) {
                put = addColumn(put, columnFamily, qualifierAndValue.getKey(), qualifierAndValue.getValue());
            }

            table.put(put);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Put addColumn(Put put, String columnFamily, String qualifier, String value) {
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        return put;
    }

    public void get(String tableName, String rowKey) {
        try (Table table = connection.getTable(tableName)) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void scan(String tableName) {
        scan(tableName, new Scan());
    }

    public void scan(String tableName, Scan scan) {
        try (Table table = connection.getTable(tableName)) {

            //Scan scan = new Scan().setLimit(1);

            try (ResultScanner scanner = table.getScanner(scan)) {
                for (Result result : scanner) {
                    System.out.println(result);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String tableName, String rowKey, String familyAndQualifier) {
        try (Table table = connection.getTable(tableName)) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try (HBaseConnection hBaseConnection = new HBaseConnection(new Cloud4ControlConfig())) {
            HBaseCRUD hBaseCRUD = new HBaseCRUD(hBaseConnection);
            hBaseCRUD.scan("hbase:meta");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
