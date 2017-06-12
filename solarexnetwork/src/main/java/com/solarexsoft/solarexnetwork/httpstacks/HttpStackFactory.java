package com.solarexsoft.solarexnetwork.httpstacks;

import android.os.Build;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 12/06/2017
 *    Desc:
 * </pre>
 */

public class HttpStackFactory {

    public static HttpStack createHttpStack() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
            return new HttpUrlConnectionStack();
        }
        return new HttpClientStack();
    }
}
