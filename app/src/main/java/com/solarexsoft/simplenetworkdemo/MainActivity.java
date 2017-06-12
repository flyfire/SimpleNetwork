package com.solarexsoft.simplenetworkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.solarexsoft.solarexnetwork.base.RequestListener;
import com.solarexsoft.solarexnetwork.core.RequestQueue;
import com.solarexsoft.solarexnetwork.core.SolarexNetwork;
import com.solarexsoft.solarexnetwork.requests.StringRequest;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue = SolarexNetwork.newRequestQueue();
    private TextView mTextResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextResult = (TextView) findViewById(R.id.tv_result);
        StringRequest request = new StringRequest(0, "http://baidu.com", new RequestListener<String>() {


            @Override
            public void onComplete(int statusCode, String response, String errorMsg) {
                mTextResult.setText(response);
            }
        });
        mRequestQueue.addRequest(request);
    }
}
