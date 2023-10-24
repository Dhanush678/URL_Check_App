package com.example.myapplication;

import com.example.myapplication.NetworkUtility;

import okhttp3.Callback;

public class IPQualityScoreApi {
    private static final String API_KEY = "XtT5ih91I4xxA6Z67Desd6oBacO25451";
    private static final String API_BASE_URL = "https://ipqualityscore.com/api/json/url/";

    public static void checkMaliciousURL(String url, Callback callback) {
        String apiUrl = API_BASE_URL + API_KEY + "/" + url;
        NetworkUtility.get(apiUrl, callback);
    }
}
