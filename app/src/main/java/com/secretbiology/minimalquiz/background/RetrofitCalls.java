package com.secretbiology.minimalquiz.background;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rohit Suratekar on 29-11-17 for Quiz.
 * All code is released under MIT License.
 */

public class RetrofitCalls {

    private static final String OPEN_DB_URL = "https://opentdb.com/";

    private Retrofit builder(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpClient.addInterceptor(interceptor).build();
    }

    public Call<ResponseData> getQuestion(int amount, OpenDBCategory category) {
        return builder(OPEN_DB_URL).create(RetroServices.class).
                getLatestExchangeRates(amount, category.getCategory());
    }

}
