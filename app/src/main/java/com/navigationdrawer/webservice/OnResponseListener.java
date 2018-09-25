package com.navigationdrawer.webservice;

/**
 * Created by AmolGursali on 9/12/2018.
 */

public interface OnResponseListener
{
    void onSuccess(int requestCode,ResponsePacket responsePacket);
    void onError(int requestCode,ErrorType errorType);
}
