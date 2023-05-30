package com.github.superzhc.hadoop.nifi.api;

import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NiFiApiClient {
    private static final Logger LOG = LoggerFactory.getLogger(NiFiApiClient.class);

    private static final String NO_AUTH = "no auth";

    /**
     * 要包含协议
     */
    private String host;

    private String username = null;
    private String password = null;
    private String token = null;

    public NiFiApiClient(String host) {
        this.host = host;
    }

    public NiFiApiClient(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public NiFiApiClient(String host, String token) {
        this.host = host;
        this.token = token;
    }

    public String getToken() {
        if (null == token || token.trim().length() == 0) {
            synchronized (this) {
                if (null == token || token.trim().length() == 0) {
                    if (username != null && username.trim().length() > 0 && password != null && password.trim().length() > 0) {
                        // nifi 要用用户名/密码方式必须是https协议
                        String url = String.format("%s/nifi-api/access/token", host);

                        Map<String, Object> formData = new HashMap<>();
                        formData.put("username", this.username);
                        formData.put("password", this.password);

                        token = HttpRequest.post(url).trustAllCerts().trustAllHosts().form(formData).body();
                    } else {
                        token = NO_AUTH;
                    }
                }
            }
        }
        return token;
    }

    public String get(String path) {
        return get(path, null);
    }

    public String get(String path, Map<String, Object> params) {
        String url = String.format("%s/nifi-api%s", host, path);
        HttpRequest request = HttpRequest.get(url, params);

        if ("https".equalsIgnoreCase(request.url().getProtocol())) {
            request.trustAllCerts().trustAllHosts();
        }

        if (!NO_AUTH.equalsIgnoreCase(getToken())) {
            request.bearer(token);
        }

        String result = request.body();
        return result;
    }

    public String post(String path) {
        return post(path, null);
    }

    public String post(String path, Map<String, Object> params) {
        String url = String.format("%s/nifi-api%s", host, path);
        HttpRequest request = HttpRequest.post(url);

        if ("https".equalsIgnoreCase(request.url().getProtocol())) {
            request.trustAllCerts().trustAllHosts();
        }

        if (!NO_AUTH.equalsIgnoreCase(getToken())) {
            request.bearer(token);
        }
        request.form(params);

        String result = request.body();
        return result;
    }

    public static void main(String[] args) {
//        String token = "eyJraWQiOiI4M2NjOTk5ZS05Y2YxLTRjYmEtOTFkNS0yYmZhYmM0Mzg0ZWYiLCJhbGciOiJQUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6IlNpbmdsZVVzZXJMb2dpbklkZW50aXR5UHJvdmlkZXIiLCJuYmYiOjE2ODU0NDAzODQsImlzcyI6IlNpbmdsZVVzZXJMb2dpbklkZW50aXR5UHJvdmlkZXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTY4NTQ2OTE4NCwiaWF0IjoxNjg1NDQwMzg0LCJqdGkiOiJmMDQ5ZGJiYy0yYjJhLTQyYWQtOWI3NS1jM2U3ZDU5MWFiMTIifQ.OpApiPrIkWeAbL8UZvHEyK8xfP-kcfDedH1saJPum2QpCJz4vIZLmnwCK9Rsg4MCTp8rpXZ5TXPUScK5SG48_sWbc5XoOJvZcd9XN077RMrFeug1ae7TGB5cq6q2tUF1LSFj9M1ZCFkOuhykdAZQ0X0RN9IOI3KJs2cd64xH46NqjjUbmTocAt-eaB0G7GvcZsS8hFFU9rRvdt_pSpvIm9-9y0glac1sIlkmreMog5m70vB4Fj38gtR8HiZGyXgBzgnGc448LDgb2-rI4QnCK7BtzXk1YFft1NsfRHW2jxbdtcEXFCVcs0DjTg-2ZP1hBApJatzAm4JNBN10wPJY21jNJ97YmgS7JUF6b936us344aTZuGFvC53UonUDzWKxDYGr0BmBUCg10wqzrz-8CrgShQWEi3YdrswJi0cbUPvibPwYrZsDQQc1rLFDdwHS9uvtPm-FqM99nba64WE3p7mxeievwPtW-2ChK-OOYjwkJoChMeo4fF5sx0r7Tn_DDBNqqsoQHUvYx8cIDeNK0T0JiZHRAYwyWgJ4XWBi722_zEHB5KwRAHKkL7n20OQ28s-bPMC-nBK_6066K9RBOBMNGUoOcDtIakTokI9zBchrNZO1U2Pfy3R60y_uUqEVjCpsXFtpaq12BGIlFmRrVytZy_1BWcTaqPMhznZrryE";
//        NiFiApiClient client = new NiFiApiClient("https://127.0.0.1:8443", token);
//
//        System.out.println(client.get("/resources"));

        NiFiApiClient client2=new NiFiApiClient("http://10.90.18.151:18443");
        System.out.println(client2.get("/resources"));
    }
}
