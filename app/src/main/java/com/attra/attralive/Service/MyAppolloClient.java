package com.attra.attralive.Service;

import android.util.Log;

import com.apollographql.apollo.ApolloClient;
import com.attra.attralive.util.GetURLs;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyAppolloClient {




   // private static final String URL = "http://13.232.225.201:80/graphql";
       private static  final String URL = GetURLs.BaseUrl; // "http://13.232.225.201:80/graphql";


    private static ApolloClient apolloClient;

    public static ApolloClient getMyAppolloClient(String Authorization) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder().method(original.method(), original.body());
                    builder.header("Authorization", Authorization);
                    Log.d("AUTH_TAG", Authorization);
                    return chain.proceed(builder.build());
                })
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl(URL)
                .okHttpClient(okHttpClient)
                .build();

        return apolloClient;
    }


}


