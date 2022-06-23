package com.github.superzhc.hadoop.es;

import com.github.superzhc.hadoop.es.util.ResponseUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.bouncycastle.util.io.pem.PemReader;
import org.elasticsearch.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

/**
 * 2020年04月21日 superz add
 * 2022年04月26日 superz modify 基于 7.13.3 版本的 Elasticsearch 新增 user/password 验证方式
 */
public class ESClient implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(ESClient.class);
    private static final String DEFAULT_PROTOCOL = "http";

    private HttpHost[] httpHosts;
    private RestClient client;
    /* Elasticsearch高级别客户端 */
    private RestHighLevelClient highLevelClient;

    public static ESClient create(String protocol, String host, Integer port) {
        return create(protocol, host, port, null, null);
    }

    public static ESClient create(String protocol, String host, Integer port, String username, String password) {
        return new ESClient(username, password, new HttpHost[]{new HttpHost(host, port, protocol)});
    }

    public static ESClient create(String host, Integer port, String username, String password) {
        return create(DEFAULT_PROTOCOL, host, port, username, password);
    }

    public static ESClient create(String host, Integer port) {
        return create(DEFAULT_PROTOCOL, host, port);
    }

    public ESClient(HttpHost... httpHosts) {
        this(null, null, httpHosts);
    }

    public ESClient(String username, String password, HttpHost... httpHosts) {
        this.httpHosts = httpHosts;
        RestClientBuilder builder = RestClient.builder(httpHosts);
        if (null != username && username.trim().length() > 0) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

            builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            });
        }
        this.highLevelClient = new RestHighLevelClient(builder);
        this.client = builder.build()/*highLevelClient.getLowLevelClient()*/;
    }

    public ESClient(HttpHost[] httpHosts, String pemPath) {
        this.httpHosts = httpHosts;
        RestClientBuilder builder = RestClient.builder(httpHosts);

        builder.setHttpClientConfigCallback(
                new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                        try {
//                            KeyStore truststore = KeyStore.getInstance("PKCS12");
//
//                            // String path = "C:\\certs\\elastic-certificates.p12";
//                            String path = pemPath;
//                            InputStream in =
//                                    new FileInputStream(path);
//                            //this.getClass().getResourceAsStream(pemPath);
//                            //Files.newInputStream(Paths.get(path));
//                            truststore.load(in, "".toCharArray()/*密码，此处设置的是无密码*/);
//
//                            SSLContextBuilder sslBuilder = SSLContexts.custom()
//                                    .loadTrustMaterial(truststore, new TrustSelfSignedStrategy());
//
//                            // 获取证书
//                            SSLContext sslcontext = sslBuilder.build();

                            // Load the self-certificate that is bundled with the JAR (see pom.xml)
//                            InputStream ksStream = this.getClass().getResourceAsStream(pemPath);//Files.newInputStream(Paths.get(pemPath))
//                            truststore.load(ksStream, "".toCharArray()); // Exception here
//
//                            // Rest of the code (only for context purpose)
//
//                            // setup the key manager factory
//                            String defaultKeyManagerAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
//                            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(defaultKeyManagerAlgorithm);
//                            keyManagerFactory.init(truststore, "".toCharArray());
//
//                            // setup the trust manager factory
//                            String defaultTrustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//                            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultTrustManagerAlgorithm);
//                            trustManagerFactory.init(truststore);
//
//                            // setup the HTTPS context and parameters
//                            SSLContext sslcontext = SSLContext.getInstance("TLS");
//                            sslcontext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

//                            httpAsyncClientBuilder.setSSLContext(sslcontext);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return httpAsyncClientBuilder;
                    }
                }
        );

        this.highLevelClient = new RestHighLevelClient(builder);
        this.client = builder.build()/*highLevelClient.getLowLevelClient()*/;
    }

    @Override
    public void close() throws IOException {
        if (null != client) {
            client.close();
        }
        if (null != highLevelClient) {
            highLevelClient.close();
        }
    }

    public String ping() {
        Response response = get("/");
        return ResponseUtils.getEntity(response);
    }

    public Response get(String url) {
        return execute("GET", url, null);
    }

    public Response get(String url, String json) {
        return execute("GET", url, json);
    }

    public Response post(String url, String json) {
        return execute("POST", url, json);
    }

    public Response put(String url, String json) {
        return execute("PUT", url, json);
    }

    public Response head(String url) {
        return execute("HEAD", url, null);
    }

    public Response delete(String url) {
        return delete(url, null);
    }

    public Response delete(String url, String json) {
        return execute("DELETE", url, json);
    }

    public Response execute(String method, String url, String json) {
        try {
            // 请求参数不允许为空
            // 6.3.2 版本
            // Map<String, String> params = Collections.emptyMap();
            // HttpEntity entity = null;
            // if (null != json) {
            // entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
            // }
            // return client.performRequest(method, url, params, entity);

            // 7.1.1 版本
            Request request = new Request(method, url);
            if (null != json) {
                request.setJsonEntity(json);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(request.toString() + (null == json ? "" : ",请求体内容：" + json));
            }

            Response response = client.performRequest(request);

            if (logger.isDebugEnabled()) {
                logger.debug(response.toString());
            }

            return response;
        } catch (Exception e) {
            logger.error("执行Elasticsearch的请求异常！", e);
            throw new RuntimeException(e);
        }
    }

    public RestClient getRestClient() {
        return client;
    }

    public RestHighLevelClient getHighLevelClient() {
        return highLevelClient;
    }
}
