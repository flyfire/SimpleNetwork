package com.solarexsoft.solarexnetwork.requests;

import com.solarexsoft.solarexnetwork.base.Request;
import com.solarexsoft.solarexnetwork.base.RequestListener;
import com.solarexsoft.solarexnetwork.base.Response;
import com.solarexsoft.solarexnetwork.entity.MultiPartEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 12/06/2017
 *    Desc:
 * </pre>
 */

public class MultiPartRequest extends Request<String> {
    MultiPartEntity mMultiPartEntity = new MultiPartEntity();

    public MultiPartRequest(int method, String url, RequestListener<String> listener) {
        super(method, url, listener);
    }

    public MultiPartEntity getMultiPartEntity() {
        return mMultiPartEntity;
    }

    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }

    @Override
    public byte[] getRequestBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    public String parseResonse(Response response) {
        if (response != null && response.getRawData() != null) {
            return new String(response.getRawData());
        }
        return null;
    }
}
