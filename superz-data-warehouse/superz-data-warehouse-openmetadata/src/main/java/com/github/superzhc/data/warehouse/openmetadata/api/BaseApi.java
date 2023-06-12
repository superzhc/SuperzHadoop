package com.github.superzhc.data.warehouse.openmetadata.api;

import com.github.superzhc.common.http.HttpRequest;

public class BaseApi {
    private String host;
    private int port;

    public BaseApi(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void getToken(){
        String url=String.format("http://%s:%d/api/v1/system/config/jwks",host,port);
    }

    public void bots() {
        String url = String.format("http://%s:%d/api/v1/bots", host, port);

        String result = HttpRequest.get(url).bearer("eyJraWQiOiJHYjM4OWEtOWY3Ni1nZGpzLWE5MmotMDI0MmJrOTQzNTYiLCJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJvcGVuLW1ldGFkYXRhLm9yZyIsInN1YiI6ImFkbWluIiwiZW1haWwiOiJhZG1pbkBvcGVubWV0YWRhdGEub3JnIiwiaXNCb3QiOmZhbHNlLCJ0b2tlblR5cGUiOiJPTV9VU0VSIiwiaWF0IjoxNjg2MzAyNDgyLCJleHAiOjE2ODYzMDYwODJ9.ZtR3V7gRdUdJh1WDzINFX3AVAvgMAsBLlEAh1Dso00WCkTF0RultNIw5K5AG9Cizsi-pGcHgjDrqr41ej7Rz6cUOWIxYdlJxzrBquLE13KDmcRQ-t1vBz4QvtH3E49zz023s8KKtllGxlDmawnOt0Ho-Ad-v4-nrJY2bKvr3V4I2XSclpKVsgPTbrUlQKXFczA3_Th8ICMHptssOGy8KHKnZKQ5wsydRuXOHOGR_7el9D256qSDTNJSJ_zGTVnW6v3uBtAh1qEYB2fSfQIa0kqNGD6GhN9PqmZasFDg9-VH8la9Vo5HIrqGVFIeArGC6V7Bs1_5QHBEEikkbBAtCcg").body();
        System.out.println(result);
    }

    public static void main(String[] args) {
        BaseApi api = new BaseApi("10.90.18.142", 8585);
        api.bots();
    }
}
