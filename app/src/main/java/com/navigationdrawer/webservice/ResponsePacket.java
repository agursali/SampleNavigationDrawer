package com.navigationdrawer.webservice;

/**
 * Created by AmolGursali on 9/12/2018.
 */

public class ResponsePacket
{
    private String responsePacket,status;
    int errorCode;

    public String getResponsePacket() {
        return responsePacket;
    }

    public void setResponsePacket(String responsePacket) {
        this.responsePacket = responsePacket;
    }

    public ResponsePacket(String status, int errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }



}
