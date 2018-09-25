package com.navigationdrawer;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by AmolGursali on 9/11/2018.
 */

public class ApplicationContext extends Application
{
    private static ApplicationContext mInstance;
    private RequestQueue mRequestQueue;
    public static final String TAG = ApplicationContext.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static synchronized ApplicationContext getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }


}
