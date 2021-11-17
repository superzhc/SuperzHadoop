package com.github.superzhc.hadoop.hbase;

import com.github.superzhc.hadoop.hbase.source.HBaseConnection;
import com.github.superzhc.hadoop.hbase.source.impl.Cloud4ControlConfig;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @author superz
 * @create 2021/11/16 16:59
 */
public class HBaseAdmin {

    private HBaseConnection connection;

    public HBaseAdmin(HBaseConnection connection) {
        this.connection = connection;
    }

    public void create(String tableName, String... families) {
        try (Admin admin = connection.getAdmin()) {
            TableDescriptorBuilder builder = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

            // 设置列簇，TODO

            admin.createTable(builder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void list(String tableName) {
        try (Admin admin = connection.getAdmin()) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void describe(String tableName) {
        try (Admin admin = connection.getAdmin()) {
            TableDescriptor descriptor = admin.getDescriptor(TableName.valueOf(tableName));
            System.out.println(descriptor.toStringCustomizedValues());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disable(String tableName) {
        try (Admin admin = connection.getAdmin()) {
            disable(admin, TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disable(Admin admin, TableName tableName) throws IOException {
        admin.disableTable(tableName);
    }

    /**
     * 删除表之前需要先禁用表
     *
     * @param tableName
     */
    public void delete(String tableName) {
        try (Admin admin = connection.getAdmin()) {
            TableName tableName1 = TableName.valueOf(tableName);
            disable(admin, tableName1);
            admin.deleteTable(tableName1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (HBaseConnection hBaseConnection = new HBaseConnection(new Cloud4ControlConfig())) {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(hBaseConnection);
            hBaseAdmin.describe("hbase:meta");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
