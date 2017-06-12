package com.solarexsoft.solarexnetwork.httpstacks;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.base.Response;
import com.solarexsoft.solarexnetwork.config.HttpUrlConnectionConfig;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public class HttpUrlConnectionStack implements HttpStack {
    HttpUrlConnectionConfig mConfig = HttpUrlConnectionConfig.getInstance();

    @Override
    public Response performRequest(Request<?> request) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = createUrlConnection(request.getUrl());
            setRequestHeaders(httpURLConnection, request);
            setRequestParams(httpURLConnection, request);
            configHttps(request);
            return fetchResponse(httpURLConnection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

    private HttpURLConnection createUrlConnection(String url) throws IOException {
        URL tmpUrl = new URL(url);
        URLConnection urlConnection = tmpUrl.openConnection();
        urlConnection.setConnectTimeout(mConfig.connectionTimeout);
        urlConnection.setReadTimeout(mConfig.socketTimeout);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(true);
        return (HttpURLConnection) urlConnection;
    }

    private void setRequestHeaders(HttpURLConnection httpURLConnection, Request<?> request) {
        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
        }
    }

    private void setRequestParams(HttpURLConnection httpURLConnection, Request<?> request) throws
            Exception {
        int method = request.getMethod();
        switch (method) {
            case 0:
                httpURLConnection.setRequestMethod("GET");
                break;
            case 1:
                httpURLConnection.setRequestMethod("POST");
                break;
            case 2:
                httpURLConnection.setRequestMethod("PUT");
                break;
            case 3:
                httpURLConnection.setRequestMethod("DELETE");
                break;
        }
        byte[] body = request.getRequestBody();
        if (body != null) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty(Request.HEADER_CONTENT_TYPE, request
                    .getBodyContentType());
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.write(body);
            dos.flush();
            dos.close();
        }
    }

    private void configHttps(Request<?> request) {
        if (request.isHttps()) {
            SSLSocketFactory sslSocketFactory = mConfig.getSSLSocketFactory();
            if (sslSocketFactory != null) {
                HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
                HttpsURLConnection.setDefaultHostnameVerifier(mConfig.getHostnameVerifier());
            }
        }
    }

    private Response fetchResponse(HttpURLConnection httpURLConnection) throws IOException {
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == -1) {
            throw new IOException("Couldnt retrive response from HttpUrlConnection!");
        }
        StatusLine responseStatus = new BasicStatusLine(protocolVersion, httpURLConnection
                .getResponseCode(), httpURLConnection.getResponseMessage());
        Response response = new Response(responseStatus);
        response.setEntity(entityFromUrlConnection(httpURLConnection));
        addHeadersToResponse(response, httpURLConnection);
        return response;
    }

    private HttpEntity entityFromUrlConnection(HttpURLConnection httpURLConnection) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream = null;
        try {
            inputStream = httpURLConnection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            inputStream = httpURLConnection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(httpURLConnection.getContentLength());
        entity.setContentEncoding(httpURLConnection.getContentEncoding());
        entity.setContentType(httpURLConnection.getContentType());
        return entity;
    }

    private void addHeadersToResponse(Response response, HttpURLConnection httpURLConnection) {
        for (Map.Entry<String, List<String>> entry : httpURLConnection.getHeaderFields
                ().entrySet()) {
            if (entry.getKey() != null) {
                Header header = new BasicHeader(entry.getKey(), entry.getValue().get(0));
                response.addHeader(header);
            }
        }
    }


}
