package com.attra.attralive.Service;

import android.util.Log;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyAppolloClient {


    //  private static  final String BASE_URL = "http://10.200.44.23:4000/graphql";




   // private static  final String URL = "http://10.200.44.25:4001/graphql";


    private static  final String URL = "http://10.200.44.20:4001/graphql";


    // private static  final String BASE_URL = "http://10.200.44.21:4000/graphql";


    // private static  final String BASE_URL = "http://10.200.42.176:4000/graphql";

    //   private static  final String BASE_URL = "http://10.200.44.23:4000/graphql";
    //private static final String URL = "http://192.168.1.100/graphql/";
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


