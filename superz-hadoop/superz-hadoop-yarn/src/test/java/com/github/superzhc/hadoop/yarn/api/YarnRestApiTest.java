package com.github.superzhc.hadoop.yarn.api;

import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/2/8 11:36
 **/
public class YarnRestApiTest {

    private static final String HOST="10.90.18.80";
    private static final Integer PORT=8088;

    private YarnRestApi api=null;

    @Before
    public void setUp() throws Exception {
        api=new YarnRestApi(HOST,PORT);
    }

    @Test
    public void cluster() {
        System.out.println(api.cluster());
    }

    @Test
    public void nodes() {
    }

    @Test
    public void applications() {
        String result=api.applications();
        System.out.println(result);
    }

    @Test
    public void application() {
        String applicationId="application_1675676587232_0001";
        String result=api.application(applicationId);
        System.out.println(result);
    }

    @Test
    public void applicationState() {
    }

    @Test
    public void updateApplicationState() {
    }

    @Test
    public void appAttempts() {
    }

    @Test
    public void logs() {
    }
}
