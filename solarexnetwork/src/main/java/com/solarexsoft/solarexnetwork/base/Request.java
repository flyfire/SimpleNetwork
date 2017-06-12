package com.solarexsoft.solarexnetwork.base;

import android.support.annotation.NonNull;

import com.solarexsoft.solarexnetwork.utils.L;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 09/06/2017
 *    Desc:
 * </pre>
 */

public abstract class Request<T> implements Comparable<Request<T>> {

    public interface HttpMethod {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
    }

    public interface Priority {
        int LOW = 0;
        int NORMAL = 1;
        int HIGH = 2;
        int IMMEDIATE = 3;
    }

    public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    protected int mSerialNum = 0;

    protected int mPriority = Priority.NORMAL;

    protected boolean isCancel = false;

    private boolean isShouldCache = true;

    protected RequestListener<T> mRequestListener;

    private String mUrl = "";

    protected int mMethod = HttpMethod.GET;

    private Map<String, String> mHeaders = new HashMap<>();

    private Map<String, String> mBodyParams = new HashMap<>();

    public Request(int method, String url, RequestListener<T> listener) {
        mMethod = method;
        mUrl = url;
        mRequestListener = listener;
    }


    public void addHeader(String name, String value) {
        mHeaders.put(name, value);
    }

    public final void deliveryResponse(Response response) {
        T result = parseResonse(response);
        if (mRequestListener != null) {
            int statusCode = response != null ? response.getStatusCode() : -1;
            String errorMsg = response != null ? response.getMessage() : "unknown error";
            L.d("Request", "Callback: statusCode = " + statusCode + ",result = " + result + ", " +
                    "errorMsg = " + errorMsg);
            mRequestListener.onComplete(statusCode, result, errorMsg);
        }
    }


    public abstract T parseResonse(Response response);


    @Override
    public int compareTo(@NonNull Request<T> o) {
        int priority = this.getPriority();
        int anotherPriority = o.getPriority();
        return priority == anotherPriority ? this.getSerialNum() - o.getSerialNum() : priority -
                anotherPriority;
    }


    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    public boolean isHttps() {
        return mUrl.startsWith("https");
    }

    public byte[] getRequestBody() {
        Map<String, String> params = getBodyParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                sb.append("&");
            }
            return sb.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Encoding not supported : " + paramsEncoding, e);
        }
    }

    public int getSerialNum() {
        return mSerialNum;
    }

    public void setSerialNum(int serialNum) {
        mSerialNum = serialNum;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public boolean isShouldCache() {
        return isShouldCache;
    }

    public void setShouldCache(boolean shouldCache) {
        isShouldCache = shouldCache;
    }

    public RequestListener<T> getRequestListener() {
        return mRequestListener;
    }

    public void setRequestListener(RequestListener<T> requestListener) {
        mRequestListener = requestListener;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getMethod() {
        return mMethod;
    }

    public void setMethod(int method) {
        mMethod = method;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    public Map<String, String> getBodyParams() {
        return mBodyParams;
    }

    public void setBodyParams(Map<String, String> bodyParams) {
        mBodyParams = bodyParams;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mHeaders == null ? 0 : mHeaders.hashCode());
        result = prime * result + (mBodyParams == null ? 0 : mBodyParams.hashCode());
        result = prime * result + (mUrl == null ? 0 : mUrl.hashCode());
        result = prime * result + (mMethod == -1 ? 0 : 31);
        result = prime * result + (mPriority == -1 ? 0 : 37);
        result = prime * result + (isShouldCache ? 31 : 37);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Request<?> other = (Request<?>) obj;
        if (this.getUrl() == null) {
            if (other.getUrl() != null) {
                return false;
            }
        } else if (!this.getUrl().equals(other.getUrl())) {
            return false;
        }
        if (this.getHeaders() == null) {
            if (other.getHeaders() != null) {
                return false;
            }
        } else if (!this.getHeaders().equals(other.getHeaders())) {
            return false;
        }
        if (this.getBodyParams() == null) {
            if (other.getBodyParams() != null) {
                return false;
            }
        } else if (!this.getBodyParams().equals(other.getBodyParams())) {
            return false;
        }
        if (this.getMethod() != other.getMethod()) {
            return false;
        }
        if (this.getPriority() != other.getPriority()) {
            return false;
        }
        if (this.isShouldCache() != other.isShouldCache()) {
            return false;
        }

        return true;

    }
}
