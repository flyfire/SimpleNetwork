package com.solarexsoft.solarexnetwork.core;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.httpstacks.HttpStack;
import com.solarexsoft.solarexnetwork.utils.L;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public final class RequestQueue {
    private BlockingQueue<Request<?>> mRequestBlockingQueue = new PriorityBlockingQueue<>();
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    public static int DEFAULT_THREAD_NUMS = Runtime.getRuntime().availableProcessors() + 1;
    private int mDispatcherNums = DEFAULT_THREAD_NUMS;
    private NetworkExecutor[] mNetworkExecutors;
    private HttpStack mHttpStack;

    public RequestQueue(int threadNums, HttpStack httpStack) {
        mDispatcherNums = threadNums;
        mHttpStack = httpStack;
    }

    private final void startExecutors() {
        mNetworkExecutors = new NetworkExecutor[mDispatcherNums];
        for (int i = 0; i < mDispatcherNums; i++) {
            mNetworkExecutors[i] = new NetworkExecutor(mRequestBlockingQueue, mHttpStack);
            mNetworkExecutors[i].start();
        }
    }

    public void stop() {
        if (mNetworkExecutors != null && mNetworkExecutors.length > 0) {
            for (int i = 0; i < mDispatcherNums; i++) {
                mNetworkExecutors[i].quit();
            }
        }
    }

    public void start() {
        stop();
        startExecutors();
    }

    public void addRequest(Request<?> request) {
        if (mRequestBlockingQueue.contains(request)) {
            L.d("RequestQueue", "request: " + request + " already added.");
        } else {
            request.setSerialNum(this.mSerialNumGenerator.getAndIncrement());
            mRequestBlockingQueue.add(request);
        }
    }

    public void clearRequests() {
        mRequestBlockingQueue.clear();
    }

    public BlockingQueue<Request<?>> getAllRequests() {
        return mRequestBlockingQueue;
    }
}
