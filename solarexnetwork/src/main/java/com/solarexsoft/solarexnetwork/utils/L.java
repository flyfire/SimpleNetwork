package com.solarexsoft.solarexnetwork.utils;

import android.util.Log;

import com.solarexsoft.solarexnetwork.BuildConfig;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 09/06/2017
 *    Desc:
 * </pre>
 */

public class L {

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }
}
