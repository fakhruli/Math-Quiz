package com.app.mathquiz;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by islam on 13/08/17.
 */

public class RetrofitClient {
    public Retrofit getClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
