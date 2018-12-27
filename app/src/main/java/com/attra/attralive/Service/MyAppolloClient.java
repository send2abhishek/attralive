package com.attra.attralive.Service;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyAppolloClient {


  //  private static  final String BASE_URL = "http://10.200.44.23:4000/graphql";


    private static  final String BASE_URL = "http://10.200.44.23:4001/graphql";

   // private static  final String BASE_URL = "http://10.200.44.21:4000/graphql";


   // private static  final String BASE_URL = "http://10.200.42.176:4000/graphql";

 //   private static  final String BASE_URL = "http://10.200.44.23:4000/graphql";
    private static ApolloClient myAppolloClient;

    public static ApolloClient getMyAppolloClient(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        myAppolloClient = ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();

        return  myAppolloClient;
    }

}
