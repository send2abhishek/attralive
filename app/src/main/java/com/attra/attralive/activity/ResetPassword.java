package com.attra.attralive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.util.GetNewRefreshToken;

import javax.annotation.Nonnull;

import graphqlandroid.ChangePassword;
import graphqlandroid.GetRefreshToken;

public class ResetPassword extends AppCompatActivity {
    EditText currentPassword, newPassword, confirmPassword;
    Button savePassword, cancel;
    String password, confPassword;
    public static final String PREFS_AUTH = "my_auth";
    private SharedPreferences sharedPreferences;
    String status, message, myToken, userId, refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        currentPassword = findViewById(R.id.et_currentPassword);
        newPassword = findViewById(R.id.et_newPassword);
        confirmPassword = findViewById(R.id.et_confirmPassword);
        savePassword = findViewById(R.id.btn_save);
        cancel = findViewById(R.id.btn_cancel);

        sharedPreferences = getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            refreshToken = sharedPreferences.getString("refreshToken", "");
            userId = sharedPreferences.getString("userId", "");
            Log.i("user id in userDtail", userId);
            Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

            String username = sharedPreferences.getString("userName", "");

        }


        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(myToken);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    public void resetPassword(String accesstoken) {

        MyAppolloClient.getMyAppolloClient(accesstoken).mutate(ChangePassword.builder().userId(userId).currentPassword(currentPassword.getText().toString()).newPassword(newPassword.getText().toString()).build()).enqueue(new ApolloCall.Callback<ChangePassword.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ChangePassword.Data> response) {
                status = response.data().changePassword_M().status();
                message = response.data().changePassword_M().message();
                Log.i("reset_password ", status);
                Log.i("message", message);
                if (status.equals("Success")) {
                    ResetPassword.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            {
                                Toast.makeText(ResetPassword.this, "Password Successfully Changed", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(intent);
                            }
                            if ((status.equals("Failure")) && (message.equals("Incorrect Current Password"))) {
                                Toast.makeText(ResetPassword.this, "Incorrect Current Password", Toast.LENGTH_LONG).show();
                            }
                        }


                    });
                }
              else  if (status.equals("Failure"))
                    if (message.equals("Invalid token: access token is invalid")) {

                      //  GetNewRefreshToken.getRefreshtoken(refreshToken, ResetPassword.this);
                        MyAppolloClient.getMyAppolloClient(GetNewRefreshToken.Authorization).query(
                                GetRefreshToken.builder().refreshToken(refreshToken).grant_type("refresh_token")
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
                                            SharedPreferences  preferences = getApplicationContext().getSharedPreferences(PREFS_AUTH, 0);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("authToken",authToken);
                                            editor.putString("refreshToken",newRefreshToken);
                                            editor.commit();
                                            ResetPassword.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                                    if (sharedPreferences.contains("authToken")) {
                                                        String myToken = sharedPreferences.getString("authToken", "");
                                                        resetPassword(myToken);
                                                        //Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(@Nonnull ApolloException e) {
                                    }
                                }
                        );

                    }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }
}
