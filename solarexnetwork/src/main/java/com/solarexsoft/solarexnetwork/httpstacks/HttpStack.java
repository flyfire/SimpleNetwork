package com.solarexsoft.solarexnetwork.httpstacks;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.base.Response;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 11/06/2017
 *    Desc:
 * </pre>
 */

public interface HttpStack {
    public Response performRequest(Request<?> request);
}
