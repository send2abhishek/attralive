package com.attra.attralive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import javax.annotation.Nonnull;

import graphqlandroid.ChangePassword;
import graphqlandroid.ForgotPasswordValidation;

public class ResetPassword extends AppCompatActivity {
    EditText currentPassword,newPassword,confirmPassword;
    Button savePassword,cancel;
    String password,newPass,confPassword;
    public static final String PREFS_AUTH = "my_auth";
    private SharedPreferences sharedPreferences;
    String status, message,myToken,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        currentPassword = findViewById(R.id.et_currentPassword);
        newPassword = findViewById(R.id.et_newPassword);
        confirmPassword = findViewById(R.id.et_confirmPassword);
        savePassword = findViewById(R.id.btn_save);
        cancel = findViewById(R.id.btn_resetCancel);
        sharedPreferences = getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");

            userId = sharedPreferences.getString("userId", "");
            Log.i("user id in userDtail", userId);
            Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

            String username = sharedPreferences.getString("userName", "");
            //      Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

        }


        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAppolloClient.getMyAppolloClient(myToken).mutate(ChangePassword.builder().userId(userId).currentPassword(password).newPassword(newPass).build()).enqueue(new ApolloCall.Callback<ChangePassword.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ChangePassword.Data> response) {
                         status = response.data().changePassword_M().status();
                         message = response.data().changePassword_M().message();
                        Log.i("reset_password ",status);
                        Log.i("message",message);
                        ResetPassword.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status.equals("Success")) {
                                    Toast.makeText(ResetPassword.this, "Password Successfully Changed", Toast.LENGTH_LONG).show();

                                } else if ((status.equals("Failure")) && (message.equals("Invalid Username or Password"))) {
                                    Toast.makeText(ResetPassword.this,"emial id is incorrect",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
