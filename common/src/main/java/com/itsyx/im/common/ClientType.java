package com.itsyx.im.common;

/**
 * @author: syx
 * @description:
 **/
public enum ClientType {

    WEBAPI(0,"webApi"),
    WEB(1,"web"),
    IOS(2,"ios"),
    ANDROID(3,"android"),
    WINDOWS(4,"windows"),
    MAC(5,"mac"),
            ;

    private int code;
    private String error;

    ClientType(int code, String error){
        this.code = code;
        this.error = error;
    }
    public int getCode() {
        return this.code;
    }

    public String getError() {
        return this.error;
    }



}
