package com.solarexsoft.solarexnetwork.config;

import org.apache.http.conn.ssl.SSLSocketFactory;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public class HttpClientConfig extends HttpConfig {
    private static volatile HttpClientConfig sHttpClientConfig;
    private SSLSocketFactory mSSLSocketFactory;

    private HttpClientConfig() {
        if (sHttpClientConfig != null) {
            throw new RuntimeException("already initialized!");
        }
    }

    public static HttpClientConfig getInstance() {
        if (sHttpClientConfig == null) {
            synchronized (HttpClientConfig.class) {
                if (sHttpClientConfig == null) {
                    sHttpClientConfig = new HttpClientConfig();
                }
            }
        }
        return sHttpClientConfig;
    }

    public void setHttpsConfig(SSLSocketFactory sslSocketFactory) {
        mSSLSocketFactory = sslSocketFactory;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return mSSLSocketFactory;
    }
}
