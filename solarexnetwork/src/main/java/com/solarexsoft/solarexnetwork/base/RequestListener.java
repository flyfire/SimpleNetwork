package com.solarexsoft.solarexnetwork.base;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 09/06/2017
 *    Desc:
 * </pre>
 */

public interface RequestListener<T> {
    void onComplete(int statusCode, T response, String errorMsg);
}
