package com.hewei.netsdk;

/**
 * Created by fengyinpeng on 2018/2/11.
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 处理 okhttp 的 session
 * Created by liujiane on 16/7/21.
 */
public class CookieJar implements okhttp3.CookieJar {

    public static final String TAG = "Cookie";

    public static final String UFO_COOKIE_ID = "UFO-SESSION-ID";

    private final static CookieJar INS = new CookieJar();

    private CookieJar(){

    }

    public static CookieJar getIns(){
        return INS;
    }

    private Cookie cookie = null;

    public Cookie getCookie(){
        return cookie;
    }

    public void setCookie(Cookie cookie){
        this.cookie = cookie;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (UFO_COOKIE_ID.equals(cookie.name())) {
                    this.cookie = cookie;
                    break;
                }
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookie != null ? Collections.singletonList(cookie) : new ArrayList<Cookie>();
    }
}