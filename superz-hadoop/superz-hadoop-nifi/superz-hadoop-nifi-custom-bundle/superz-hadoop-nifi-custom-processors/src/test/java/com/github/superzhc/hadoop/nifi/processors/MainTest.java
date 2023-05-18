package com.github.superzhc.hadoop.nifi.processors;

import org.junit.Test;

/**
 * @author superz
 * @create 2023/5/16 14:38
 **/
public class MainTest {
    @Test
    public void test() {
        String url = "http://10.90.15.222:18089/nifi-registry-api/buckets/2320418f-dc67-4049-8e72-f2d6fe0dc5e6/flows/f15bb7a0-a266-4e15-9d25-35fbfd07ac15/versions/1";
        String storageLocation = url;

        String protocol = "";
        if (storageLocation.startsWith("https://")) {
            protocol = storageLocation.substring(0, 8);
            storageLocation = storageLocation.substring(8);
        } else if (storageLocation.startsWith("http://")) {
            protocol = storageLocation.substring(0, 7);
            storageLocation=storageLocation.substring(7);
        }

        int cursor = storageLocation.indexOf("/");
        if (cursor == -1) {
            return;
        }
        String host = storageLocation.substring(0, cursor);
        storageLocation = storageLocation.substring(cursor+1);

        cursor = storageLocation.indexOf("/");
        String serverName = storageLocation.substring(0, cursor);
        storageLocation = storageLocation.substring(cursor+1);

        String path = storageLocation;
    }
}
