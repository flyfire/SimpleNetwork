package com.solarexsoft.solarexnetwork.requests;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.base.RequestListener;
import com.solarexsoft.solarexnetwork.base.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 12/06/2017
 *    Desc:
 * </pre>
 */

public class JsonRequest extends Request<JSONObject> {

    public JsonRequest(int method, String url, RequestListener<JSONObject> listener) {
        super(method, url, listener);
    }

    @Override
    public JSONObject parseResonse(Response response) {
        String jsonString = new String(response.getRawData());
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}