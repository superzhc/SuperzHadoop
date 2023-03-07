package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Snapshot;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/7 15:18
 **/
public class JavaIcebergMain {
    public static void main(String[] args) {
        Catalog catalog = new IcebergHadoopS3Catalog("s3a://superz/iceberg", "http://127.0.0.1:9000", "admin", "admin123456")
                .catalog("hadoop");

        TableIdentifier tableIdentifier = TableIdentifier.of("akshare", "fund_name_em");
        Table table = catalog.loadTable(tableIdentifier);

//        Map<String, String> properties = table.properties();
//        System.out.println(properties);

        Schema schema = table.schema();
        System.out.println(schema);

        System.out.println("========================华丽分割线========================");

        Snapshot currentSnapshot=table.currentSnapshot();
        System.out.println(currentSnapshot);

        System.out.println("========================华丽分割线========================");

//        List<Snapshot> snapshots=new ArrayList<>(table.snapshots());

        System.out.println(table.location());
    }
}
