package com.navigationdrawer.webservice;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.navigationdrawer.ApplicationContext;
import com.navigationdrawer.util.Utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AmolGursali on 9/12/2018.
 */

public class InteractorImpl implements Interactor
{
    Context context;
    OnResponseListener callBack;
    int requestCode;
    private String headerKey;
    String tag;
    private int socketTimeout = 10000;

    public InteractorImpl(Context context, OnResponseListener callBack, int requestCode, String tag)
    {
        this.context=context;
        this.callBack=callBack;
        this.requestCode=requestCode;
        this.tag=tag;

    }

    @Override
    public void makeStringGetRequest(String url, boolean showDialog)
    {
        System.out.println("--------------------------------------------------------");
        System.out.println("------" + "url= " +  url + " --------");
        System.out.println("--------------------------------------------------------");
        if (Utilities.getInstance().isOnline(context)) {
            if (showDialog) {
                //  showDialog(null);
            }
            makeGetStringRequest(url);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET);
        }
    }

    private void makeGetStringRequest(String url) {
        url = url + "?v=" + System.currentTimeMillis();
        if (Utilities.getInstance().isOnline(context)) {
            StringRequest sr = new StringRequest(Request.Method.GET, url, successStringListener, errorListener);
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            sr.setRetryPolicy(policy);
            ApplicationContext.getInstance().addToRequestQueue(sr, tag);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET);
        }
    }

    Response.Listener successStringListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                System.out.println("-------------------------successStringListener-------------------------------");
                System.out.println("---------" + s);
                System.out.println("----------------------------successStringListener----------------------------");
                ResponsePacket response = new Gson().fromJson(s.toString(), ResponsePacket.class);
                response.setResponsePacket(s);
                callBack.onSuccess(requestCode, response);
            } catch (Exception e) {
                callBack.onSuccess(requestCode, new ResponsePacket("success", 0));
                e.printStackTrace();
            }
        }
    };


    @Override
    public void makeJsonPostRequest(String url, JSONObject jsonRequest, boolean showDialog)
    {
        System.out.print("---------------------------------------------------------");
        System.out.print("----"+url+"-----");
        System.out.print("----"+jsonRequest+"-----");
        System.out.println("---------------" + "headerKey = "+ headerKey + " -----------------");

        if(Utilities.getInstance().isOnline(context))
        {
            makeJsonPostRequest(url,jsonRequest);
        }
        else
        {
            showNoInternetConnection();
            callBack.onError(requestCode,ErrorType.NO_INTERNET);
        }


    }

    private void showNoInternetConnection() {
//        Utilities.getInstance().showDialog(context, context.getString(R.string.alert), context.getString(R.string.noInternetAccess)).show();
        Toast.makeText(context, "noInternetAccess", Toast.LENGTH_SHORT).show();
    }



    private void makeJsonPostRequest(String url, JSONObject jsonRequest)
    {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,url,jsonRequest,successJsonListener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String,String> mHeaders=new HashMap<>();
                try
                {
                    for (String key : super.getHeaders().keySet()) {
                        mHeaders.put(key, super.getHeaders().get(key));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                mHeaders.put("Content-Type", "application/json; charset=utf-8");
                mHeaders.put("splalgoval", headerKey);

                return mHeaders;
            }
        };
        RetryPolicy retryPolicy=new DefaultRetryPolicy(socketTimeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        jsonObjectRequest.setTag(tag);
        ApplicationContext.getInstance().addToRequestQueue(jsonObjectRequest, tag);
    }

    Response.Listener successJsonListener=new Response.Listener<JSONObject>()
    {
        @Override
        public void onResponse(JSONObject s)
        {
            try
            {
                System.out.println("--------------------------successJsonListener------------------------------");
                System.out.println("---------" + s);
                System.out.println("--------------------------successJsonListener------------------------------");
                ResponsePacket responsePacket=new Gson().fromJson(s.toString(),ResponsePacket.class);
                responsePacket.setResponsePacket(s.toString());
                callBack.onSuccess(requestCode,responsePacket);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                callBack.onSuccess(requestCode,new ResponsePacket("success",0));
            }

        }
    };

    Response.ErrorListener errorListener=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError)
        {
            System.out.println("------------------------errorListener--------------------------------");
            String err=null;
            try
            {
                if(volleyError!=null)
                {
                    if (volleyError.getCause() != null && volleyError.getCause().getMessage().equalsIgnoreCase("End of input at character 0 of ")) {
                        callBack.onSuccess(requestCode, new ResponsePacket("success", 0));
                        return;
                    }
                    else {
                        try {
                            if (volleyError.networkResponse.statusCode == 500)
                            {
                                callBack.onError(requestCode, ErrorType.ERROR500);
                                return;
                            } else {
                                err = new String(volleyError.networkResponse.data);
                            }
                        } catch (Exception e) {
//                          Crashlytics.logException(e);
                        }
                        if (err != null) {
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            callBack.onError(requestCode, ErrorType.ERROR);
            System.out.println("------------------------errorListener--------------------------------");
        }
    };

}
