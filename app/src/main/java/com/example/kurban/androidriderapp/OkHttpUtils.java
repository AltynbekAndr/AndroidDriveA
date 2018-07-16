package com.example.kurban.androidriderapp;

        import okhttp3.OkHttpClient;


public class OkHttpUtils {
    public static OkHttpClient okHttpClient = new OkHttpClient();

    public static OkHttpClient getInstance() {
        return okHttpClient;
    }
}
