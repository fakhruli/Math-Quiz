package com.app.mathquiz;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by islam on 21/08/17.
 */

public class HttpClient {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String TAG = HttpClient.class.getSimpleName();

    public static String run(String url) throws IOException {

        final String[] msg = {""};

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
                msg[0] = String.valueOf(response.body().string());
            }
        });

        return msg[0];
    }

}
