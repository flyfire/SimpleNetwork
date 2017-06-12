package com.solarexsoft.solarexnetwork.requests;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.base.RequestListener;
import com.solarexsoft.solarexnetwork.base.Response;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 12/06/2017
 *    Desc:
 * </pre>
 */

public class StringRequest extends Request<String> {
    public StringRequest(int method, String url, RequestListener<String> listener) {
        super(method, url, listener);
    }

    @Override
    public String parseResonse(Response response) {
        if (response != null && response.getRawData() != null) {
            return new String(response.getRawData());
        }
        return null;
    }
}
