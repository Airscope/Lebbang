package com.example.dlen.lebbang;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ASUS on 2017/12/28.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String session, String address, int method, String data, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        if (method == 0) {
            request = new Request.Builder().url(address).addHeader("cookie", session).build();
        }
        else if (method == 1) {
            //Log.d("sid", session);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, data);
            request = new Request.Builder()
                    //.header("Cookie", session)
                    .url(address)
                    .addHeader("cookie", session)
                    .post(requestBody)
                    .build();
        }
        client.newCall(request).enqueue(callback);
    }
}
