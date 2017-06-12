package com.solarexsoft.solarexnetwork.cache;

import android.util.LruCache;

import com.solarexsoft.solarexnetwork.base.Response;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public class MemoryCache implements Cache<String, Response> {
    private LruCache<String, Response> mResponseCache;

    public MemoryCache() {
        final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / (8 * 1024));
        mResponseCache = new LruCache<String, Response>(cacheSize) {
            @Override
            protected int sizeOf(String key, Response value) {
                return value.getRawData().length / 1024;
            }
        };
    }

    @Override
    public Response get(String key) {
        return mResponseCache.get(key);
    }

    @Override
    public void put(String key, Response value) {
        mResponseCache.put(key, value);
    }

    @Override
    public void remove(String key) {
        mResponseCache.remove(key);
    }
}
