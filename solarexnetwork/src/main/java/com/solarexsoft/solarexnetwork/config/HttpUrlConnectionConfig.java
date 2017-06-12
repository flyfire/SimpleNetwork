package com.solarexsoft.solarexnetwork.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public class HttpUrlConnectionConfig extends HttpConfig {
    private static volatile HttpUrlConnectionConfig sHttpUrlConnectionConfig;

    private SSLSocketFactory mSSLSocketFactory = null;
    private HostnameVerifier mHostnameVerifier = null;

    private HttpUrlConnectionConfig() {
        if (sHttpUrlConnectionConfig != null) {
            throw new RuntimeException("already initialized!");
        }
    }

    public static HttpUrlConnectionConfig getInstance() {
        if (sHttpUrlConnectionConfig == null) {
            synchronized (HttpUrlConnectionConfig.class) {
                if (sHttpUrlConnectionConfig == null) {
                    sHttpUrlConnectionConfig = new HttpUrlConnectionConfig();
                }
            }
        }
        return sHttpUrlConnectionConfig;
    }

    public void setHttpsConfig(SSLSocketFactory sslSocketFactory, HostnameVerifier
            hostnameVerifier) {
        mSSLSocketFactory = sslSocketFactory;
        mHostnameVerifier = hostnameVerifier;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return mSSLSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }
}
