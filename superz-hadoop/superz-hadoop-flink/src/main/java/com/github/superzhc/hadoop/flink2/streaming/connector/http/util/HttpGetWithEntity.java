package com.github.superzhc.hadoop.flink2.streaming.connector.http.util;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;

/**
 * Allows to send GET requests providing a body (not supported out of the box)
 */
public final class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {

    static final String METHOD_NAME = HttpGet.METHOD_NAME;

    public HttpGetWithEntity(final URI uri) {
        setURI(uri);
    }

    public HttpGetWithEntity(String uri){
        this.setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
