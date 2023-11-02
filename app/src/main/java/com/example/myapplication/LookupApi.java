package com.example.myapplication;

import retrofit2.Callback;

public class LookupApi {
    private static final String API_KEY = "bWFsd2FyZV90aHJlYXRfdHlwZQ==";
    private static final String BASE_URL = "https://api.provider.com/lookup/v4/";

    public static void performLookup(String query, Callback callback) {
        String apiUrl = BASE_URL + "?api_key=" + API_KEY + "&query=" + query;
//        NetworkUtility.get(apiUrl, callback);
    }
}
