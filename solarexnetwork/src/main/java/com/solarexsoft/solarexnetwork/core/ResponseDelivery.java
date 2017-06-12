package com.solarexsoft.solarexnetwork.core;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.base.Response;

import java.util.concurrent.Executor;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public class ResponseDelivery implements Executor {
    Handler mResponseHandler = new Handler(Looper.getMainLooper());

    public void deliveryResponse(final Request<?> request, final Response response) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                request.deliveryResponse(response);
            }
        };
        execute(runnable);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mResponseHandler.post(command);
    }
}
