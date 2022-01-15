package com.github.superzhc.data.oick;

import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.utils.ResultT;

import java.io.IOException;

/**
 * @author superz
 * @create 2021/12/9 10:48
 */
public class API extends HttpData {
    private static final String URL = "https://api.oick.cn/";

    public ResultT ICP(String param) throws IOException {
        String url = URL + "icp/api.php?url=" + param;
        ResultT response = get(url);
        return response;
    }

    public static void main(String[] args) throws Exception {
        API api = new API();
        System.out.println(api.ICP("weixin.com"));
    }
}
