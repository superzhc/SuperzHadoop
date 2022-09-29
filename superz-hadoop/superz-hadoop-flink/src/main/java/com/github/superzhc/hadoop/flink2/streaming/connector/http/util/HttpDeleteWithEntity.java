package com.github.superzhc.hadoop.flink2.streaming.connector.http.util;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * Allows to send DELETE requests providing a body (not supported out of the box)
 */
public final class HttpDeleteWithEntity extends HttpEntityEnclosingRequestBase {

    static final String METHOD_NAME = HttpDelete.METHOD_NAME;

    public HttpDeleteWithEntity(final URI uri) {
        setURI(uri);
    }

    public HttpDeleteWithEntity(String uri) {
        this.setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
