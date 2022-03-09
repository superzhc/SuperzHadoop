package com.github.superzhc.common.file.avro;

import com.github.superzhc.common.file.avro.generate.User;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;

public class FileAvroMain {
    public static void main(String[] args) throws Exception {
        // 新建user三种方式
        // 方式1
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // 方式2
        User user2 = new User("Ben", 7, "red");
        // 方式3
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();

        // 文件地址
        String path = "E:\\data\\users.avro";

        // 持久化数据到磁盘
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), new File(path));
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();

        // 从User反序列化数据
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<>(new File(path), userDatumReader);
        User user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }

        /* 在不生成 User 类的情况下直接进行序列化和反序列化操作 */
        Schema schema = new Schema.Parser().parse(new File("superz-common-file/src/main/avro/com/github/superzhc/avro/generate/user.avsc"));
        GenericRecord user4 = new GenericData.Record(schema);
        user4.put("name", "Alyssa");
        user4.put("favorite_number", 256);

        GenericRecord user5 = new GenericData.Record(schema);
        user5.put("name", "Ben");
        user5.put("favorite_number", 7);
        user5.put("favorite_color", "red");

        File file = new File("users2.avro");
        DatumWriter<GenericRecord> datumWriter2 = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter2 = new DataFileWriter<>(datumWriter2);
        dataFileWriter2.create(schema, file);
        dataFileWriter2.append(user4);
        dataFileWriter2.append(user5);
        dataFileWriter2.close();

        DatumReader<GenericRecord> datumReader2 = new GenericDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader2 = new DataFileReader<>(file, datumReader2);
        GenericRecord otherUser = null;
        while (dataFileReader2.hasNext()) {
            otherUser = dataFileReader2.next(otherUser);
            System.out.println(otherUser);
        }
    }
}
