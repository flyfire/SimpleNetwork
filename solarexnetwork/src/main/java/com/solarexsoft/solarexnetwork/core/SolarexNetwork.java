package com.solarexsoft.solarexnetwork.core;

import com.solarexsoft.solarexnetwork.httpstacks.HttpStack;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public final class SolarexNetwork {
    public static RequestQueue newRequestQueue() {
        return newRequestQueue(RequestQueue.DEFAULT_THREAD_NUMS);
    }

    private static RequestQueue newRequestQueue(int defaultThreadNums) {
        return newRequestQueue(defaultThreadNums, null);
    }

    private static RequestQueue newRequestQueue(int defaultThreadNums, HttpStack httpStack) {
        RequestQueue requestQueue = new RequestQueue(Math.max(1, defaultThreadNums), httpStack);
        requestQueue.start();
        return requestQueue;
    }
}
