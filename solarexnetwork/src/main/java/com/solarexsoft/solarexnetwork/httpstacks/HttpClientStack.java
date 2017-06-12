package com.solarexsoft.solarexnetwork.httpstacks;

import android.net.http.AndroidHttpClient;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.base.Response;
import com.solarexsoft.solarexnetwork.config.HttpClientConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public class HttpClientStack implements HttpStack {
    HttpClientConfig mConfig = HttpClientConfig.getInstance();
    HttpClient mHttpClient = AndroidHttpClient.newInstance(mConfig.userAgent);

    @Override
    public Response performRequest(Request<?> request) {
        try {
            HttpUriRequest httpRequest = createHttpRequest(request);
            setConnectionParams(httpRequest);
            addHeaders(httpRequest, request.getHeaders());
            configHttps(request);
            HttpResponse httpResponse = mHttpClient.execute(httpRequest);
            Response response = new Response(httpResponse.getStatusLine());
            response.setEntity(httpResponse.getEntity());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpUriRequest createHttpRequest(Request<?> request) {
        HttpUriRequest httpUriRequest = null;
        switch (request.getMethod()) {
            case 0://get
                httpUriRequest = new HttpGet(request.getUrl());
                break;
            case 1://post
                httpUriRequest = new HttpPost(request.getUrl());
                httpUriRequest.addHeader(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());
                setRequestBodyIfNonEmpty((HttpPost) httpUriRequest, request);
                break;
            case 2://put
                httpUriRequest = new HttpPut(request.getUrl());
                httpUriRequest.addHeader(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());
                setRequestBodyIfNonEmpty((HttpPut) httpUriRequest, request);
                break;
            case 3://delete
                httpUriRequest = new HttpDelete(request.getUrl());
                break;
            default:
                throw new IllegalStateException("Unknown request method!");
        }
        return httpUriRequest;
    }

    private void setConnectionParams(HttpUriRequest httpRequest) {
        HttpParams httpParams = httpRequest.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, mConfig.connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParams, mConfig.socketTimeout);
    }

    private void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpRequest.addHeader(entry.getKey(), entry.getValue());
        }
    }

    private void configHttps(Request<?> request) {
        org.apache.http.conn.ssl.SSLSocketFactory sslSocketFactory = mConfig.getSSLSocketFactory();
        if (request.isHttps() && sslSocketFactory != null) {
            Scheme scheme = new Scheme("https", sslSocketFactory, 443);
            mHttpClient.getConnectionManager().getSchemeRegistry().register(scheme);
        }
    }

    private static void setRequestBodyIfNonEmpty(HttpEntityEnclosingRequestBase
                                                               httpRequest, Request<?> request) {
        byte[] body = request.getRequestBody();
        if (body != null) {
            HttpEntity entity = new ByteArrayEntity(body);
            httpRequest.setEntity(entity);
        }
    }

}
