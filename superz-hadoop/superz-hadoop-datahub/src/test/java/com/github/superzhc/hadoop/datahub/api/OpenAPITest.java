package com.github.superzhc.hadoop.datahub.api;

import com.github.superzhc.hadoop.datahub.utils.URNHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author superz
 * @create 2023/4/14 11:03
 **/
public class OpenAPITest {
    static URNHelper helper;

    OpenAPI api;

    @BeforeClass
    public static void init() {
        helper = URNHelper.create();
    }

    @Before
    public void setUp() {
        api = new OpenAPI("10.90.18.76", 58080);
    }

    @Test
    public void testEntities() {
        String urn1 = helper.table("clickhouse", "my_dw", "t_202304111455");
        String urn2 = helper.table("clickhouse", "my_dw", "t_202304111608");
        System.out.println(api.entities(urn1, urn2));
    }

    @Test
    public void testDataset() {
        String urn = helper.dataset("clickhouse", "my_dw.t_202304111455", "PROD");
        System.out.println(api.entities(urn));
    }

    @Test
    public void testDatasource() {
        String urn = helper.dataPlatform("clickhouse");
        System.out.println(api.entities(urn));
    }
}