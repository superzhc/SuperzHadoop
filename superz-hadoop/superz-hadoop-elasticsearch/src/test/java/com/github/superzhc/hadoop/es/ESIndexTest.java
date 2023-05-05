package com.github.superzhc.hadoop.es;

import com.github.superzhc.hadoop.es.index.ESIndex;
import org.junit.Test;

import java.util.List;

/**
 * @author superz
 * @create 2023/4/12 11:49
 **/
public class ESIndexTest extends ESClientTest {
    ESIndex indexClient;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        indexClient = new ESIndex(client);
    }

    @Test
    public void testIndices() {
        List<String> indices = indexClient.indices();
        System.out.println(indices);
    }

    @Test
    public void testCreateIndex() {
        String result = indexClient.create("my_test");
        System.out.println(result);
    }

    @Test
    public void testMapping(){
        String indexPattern="prometheusbeat-7.3.1-*";
        String result=indexClient.mapping(indexPattern);
        System.out.println(result);
    }
}
