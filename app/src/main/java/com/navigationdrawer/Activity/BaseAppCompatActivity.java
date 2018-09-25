package com.navigationdrawer.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.navigationdrawer.webservice.ErrorType;
import com.navigationdrawer.webservice.OnResponseListener;
import com.navigationdrawer.webservice.ResponsePacket;

public class BaseAppCompatActivity extends AppCompatActivity implements OnResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {

    }

    @Override
    public void onError(int requestCode, ErrorType errorType) {

    }
}
