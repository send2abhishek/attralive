package com.attra.attralive.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.annotation.Nonnull;

import graphqlandroid.ForgotPassword;
import graphqlandroid.UserLoginAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    LinearLayout backtologinbtn;
    Button resetPassword;
    EditText emailId;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        actionBar.hide();
        backtologinbtn=findViewById(R.id.back_to_login_layout);
        resetPassword = findViewById(R.id.btn_resetPassword);
        emailId = findViewById(R.id.et_emailId);
        email = emailId.getText().toString();
        Log.i("email in forgot ",email);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        ProgressDialog progressdialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressdialog.setMessage("Please Wait....");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.i("ref token",refreshedToken);
        emailId.setText(refreshedToken);
        backtologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailId.requestFocus();

                if((emailId.getText().toString()).equals("")){
                    emailId.setError("Please enter the email id");
                    emailId.requestFocus();
                }else {
                    progressdialog.show();
                    Log.i("Forgot password click", email);

                    MyAppolloClient.getMyAppolloClient("").mutate(ForgotPassword.builder().email(emailId.getText().toString()).build()).enqueue(new ApolloCall.Callback<ForgotPassword.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<ForgotPassword.Data> response) {
                            String status = response.data().forgotPassword_M().status();
                            String message = response.data().forgotPassword_M().message();
                            Log.i("forgot password ", status);
                            Log.i("message", message);
                            ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (status.equals("Success")) {
                                        progressdialog.dismiss();
                                        if(message.equals("OTP Sent"))
                                        Toast.makeText(ForgotPasswordActivity.this, "OTP has been sent to registered email id", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(ForgotPasswordActivity.this, PasswordRecover.class);
                                        i.putExtra("emailId",emailId.getText().toString());
                                        startActivity(i);
                                    } else if ((status.equals("Failure")) && (message.equals("Email Not Found"))) {
                                        progressdialog.dismiss();
                                        Toast.makeText(ForgotPasswordActivity.this, "Please enter correct email id", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                        }
                    });
                }

            }
        });
    }




}
