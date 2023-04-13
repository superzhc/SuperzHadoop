package com.github.superzhc.hadoop.datahub.api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author superz
 * @create 2023/4/13 17:42
 **/
public class GraphQLAPITest {

    GraphQLAPI api;
    @Before
    public void setUp() throws Exception {
        api=new GraphQLAPI("10.90.18.76",58080);
    }

    @Test
    public void testAppConfig(){
        System.out.println(api.execute("appConfig"));
    }
}