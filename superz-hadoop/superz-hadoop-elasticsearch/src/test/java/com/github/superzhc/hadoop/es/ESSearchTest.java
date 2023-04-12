package com.github.superzhc.hadoop.es;

import com.github.superzhc.hadoop.es.search.ESNewSearch;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/4/12 11:28
 **/
public class ESSearchTest extends ESClientTest {

    ESNewSearch search;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        search = new ESNewSearch(client);
    }

    @Test
    public void testQueryAll() {
        String result = search.queryAll();
        System.out.println(result);
    }
}
