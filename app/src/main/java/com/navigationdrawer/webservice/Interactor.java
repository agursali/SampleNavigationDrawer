package com.navigationdrawer.webservice;

import org.json.JSONObject;

/**
 * Created by AmolGursali on 9/12/2018.
 */

public interface Interactor
{
    String BASE_API_URL = ""; // Test URL
    String BASE_URL = BASE_API_URL + "";



    String Method_Login = BASE_URL + "Login";
    int RequestCode_Login = 1;
    String Tag_Login = "TAG_Login";

    void makeStringGetRequest(String url, boolean showDialog);

    void makeJsonPostRequest(String url, JSONObject jsonRequest, boolean showDialog);
}
