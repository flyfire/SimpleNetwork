package com.solarexsoft.solarexnetwork.core;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.base.Response;
import com.solarexsoft.solarexnetwork.cache.Cache;
import com.solarexsoft.solarexnetwork.cache.MemoryCache;
import com.solarexsoft.solarexnetwork.httpstacks.HttpStack;
import com.solarexsoft.solarexnetwork.httpstacks.HttpStackFactory;
import com.solarexsoft.solarexnetwork.utils.L;

import java.util.concurrent.BlockingQueue;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public class NetworkExecutor extends Thread {
    private BlockingQueue<Request<?>> mRequestQueue;
    private HttpStack mHttpStack;
    private static ResponseDelivery mResponseDelivery = new ResponseDelivery();
    private static Cache<String, Response> mReqCache = new MemoryCache();

    private boolean isShouldStop = false;

    public NetworkExecutor(BlockingQueue<Request<?>> requestQueue, HttpStack httpStack) {
        mRequestQueue = requestQueue;
        mHttpStack = httpStack!=null?httpStack: HttpStackFactory.createHttpStack();
    }

    @Override
    public void run() {
        try {
            while (!isShouldStop && !isInterrupted()) {
                final Request<?> request = mRequestQueue.take();
                if (request.isCancel()) {
                    L.d("NetworkExecutor", "request: " + request + " canceled");
                    continue;
                }
                Response response = null;
                if (isUseCache(request)) {
                    response = mReqCache.get(request.getUrl());
                } else {
                    response = mHttpStack.performRequest(request);
                    if (request.isShouldCache() && isSuccess(response)) {
                        mReqCache.put(request.getUrl(), response);
                    }
                }
                mResponseDelivery.deliveryResponse(request, response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isUseCache(Request<?> request) {
        boolean isShouldUseCache = request.isShouldCache() &&
                mReqCache.get(request.getUrl()) != null;
        L.d("NetworkExecutor", "isUseCache: " + isShouldUseCache);
        return isShouldUseCache;
    }

    private boolean isSuccess(Response response) {
        int statusCode = 0;
        if (response != null) {
            statusCode = response.getStatusCode();
        }
        boolean isSuccess = statusCode >= 200 && statusCode < 300;
        L.d("NetworkExecutor", "isSuccess: " + isSuccess);
        return isSuccess;
    }

    public void quit() {
        isShouldStop = true;
        interrupt();
    }
}
