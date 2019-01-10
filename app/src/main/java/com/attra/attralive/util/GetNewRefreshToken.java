package com.attra.attralive.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.Service.MyAppolloClient;

import javax.annotation.Nonnull;

import graphqlandroid.GetRefreshToken;

public class GetNewRefreshToken {
    public static String  Authorization= "Basic YXBwbGljYXRpb246c2VjcmV0";
    public static final String PREFS_AUTH ="my_auth";
    public static void getRefreshtoken(String refreshtoken, Context context) {
        MyAppolloClient.getMyAppolloClient(Authorization).query(
                GetRefreshToken.builder().refreshToken(refreshtoken).grant_type("refresh_token")
                        .build()).enqueue(
                new ApolloCall.Callback<GetRefreshToken.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetRefreshToken.Data> response) {
                        String message = response.data().getRefreshToken_Q().message();
                        String status = response.data().getRefreshToken_Q().status();
                        if(status.equals("Success")){
                            String accessToken= response.data().getRefreshToken_Q().accessToken();
                            String tokenExpiry = response.data().getRefreshToken_Q().accessTokenExpiresAt();
                            String newRefreshToken = response.data().getRefreshToken_Q().RefreshToken();
                            String refreshTokenExpiry = response.data().getRefreshToken_Q().accessTokenExpiresAt();
                            String user = response.data().getRefreshToken_Q().user();
                            String userName = response.data().getRefreshToken_Q().name();
                            Log.i("access Token",accessToken);
                            String authToken="Bearer"+" "+accessToken;
                            Log.i("brarer token",authToken);
                            SharedPreferences  preferences = context.getSharedPreferences(PREFS_AUTH, 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("authToken",authToken);
                            editor.putString("refreshToken",newRefreshToken);
                            editor.commit();
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );
    }


}
