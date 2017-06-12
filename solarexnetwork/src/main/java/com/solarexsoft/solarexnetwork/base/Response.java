package com.solarexsoft.solarexnetwork.base;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 09/06/2017
 *    Desc:
 * </pre>
 */

public class Response extends BasicHttpResponse {
    public byte[] rawData = new byte[0];

    public Response(StatusLine statusline) {
        super(statusline);
    }

    public Response(ProtocolVersion ver, int code, String reason) {
        super(ver, code, reason);
    }

    @Override
    public void setEntity(HttpEntity entity) {
        super.setEntity(entity);
        rawData = entityToBytes(this.getEntity());
    }

    public byte[] getRawData() {
        return rawData;
    }

    public int getStatusCode() {
        return this.getStatusLine().getStatusCode();
    }

    public String getMessage() {
        return this.getStatusLine().getReasonPhrase();
    }


    private byte[] entityToBytes(HttpEntity entity) {
        try {
            return EntityUtils.toByteArray(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
