package com.attra.attralive.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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

import graphqlandroid.ForgotPassword;
import graphqlandroid.ForgotPasswordValidation;

public class PasswordRecover extends AppCompatActivity {

    EditText otp,password,cnfPassword;
    String receivedOtp,enteredPassword,confirmedPassword,emailId;
    Button save,cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);
        otp = findViewById(R.id.et_otp);
        password = findViewById(R.id.et_newPassword);
        cnfPassword = findViewById(R.id.et_confirmPassword);
        save = findViewById(R.id.btn_save);
        receivedOtp = otp.getText().toString();
        enteredPassword = password.getText().toString();
        confirmedPassword = cnfPassword.getText().toString();
        cancel = findViewById(R.id.btn_resetCancel);
        ProgressDialog progressdialog = new ProgressDialog(PasswordRecover.this);
        progressdialog.setMessage("Please Wait....");
        String numRegex   = ".*[0-9].*";
        String alphaRegex = ".*[a-zA-Z].*";

        emailId = getIntent().getStringExtra("emailId");



      /*  otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((otp.getText().toString().trim().length()<4))
                {
                    otp.setText("Enter valid OTP");
                    otp.requestFocus();
                }
                else {
                    otp.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!((password.getText().toString().trim().matches(numRegex))&&
                        ( password.getText().toString().trim().matches(alphaRegex))) ) {
                    // passworderror.setText("");
                    password.setError("Password should contain alphanumeric");
                }
                else
                    password.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if((otp.getText().toString().trim().isEmpty()))
                {
                    otp.setText("OTP cannot be empty");
                    otp.requestFocus();
                }

            }


        });

        cnfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((otp.getText().toString().trim().isEmpty()))
                {
                    otp.setTextColor(getResources().getColor(R.color.redcolor));
                    otp.setText("OTP cannot be empty");
                    otp.requestFocus();
                }

                else if((password.getText().toString().trim().isEmpty()))
                {
                    password.setError("Password cannot be empty");
                    password.requestFocus();
                }
                cnfPassword.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(password.getText().toString().trim().equals(s.toString())))
                {
                    cnfPassword.setText("Password not matching");
                }
            }
        });
*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().trim().isEmpty()){
                    otp.setError("Please enter OTP");
                    otp.requestFocus();
                }else if((otp.getText().toString().trim()).length()<4){
                    otp.setError("Please enter valid OTP");
                    otp.requestFocus();
                }else if(password.getText().toString().trim().isEmpty()){
                    password.setError("Please enter Password");
                    password.requestFocus();
                }else if(cnfPassword.getText().toString().trim().isEmpty()){
                    cnfPassword.setError("Please enter confirm password");
                    cnfPassword.requestFocus();
                } else if (!((password.getText().toString().trim().matches(numRegex))&&
                        ( password.getText().toString().trim().matches(alphaRegex))) ) {
                    password.setError("Password should contain alphanumeric");
                    password.requestFocus();
                } else if((password.getText().toString().trim()).length()<6){
                    password.setError("Password should contain minimum 6 character ");
                    password.requestFocus();

                }else if(!(password.getText().toString().trim()).equals(cnfPassword.getText().toString().trim())){
                    cnfPassword.setError("Password doesn't match");
                    cnfPassword.requestFocus();

                }else
                    Log.i("email in forgot pass",emailId);
                Log.i("OTP in forgot pass", otp.getText().toString());
                Log.i("pass in forgot pass",password.getText().toString());

                progressdialog.show();
                MyAppolloClient.getMyAppolloClient("").mutate(ForgotPasswordValidation.builder().email(emailId).oTP(otp.getText().toString()).newPassword(password.getText().toString()).confirmPassword(cnfPassword.getText().toString()).build()).enqueue(new ApolloCall.Callback<ForgotPasswordValidation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ForgotPasswordValidation.Data> response) {
                        String status = response.data().forgotPasswordValidation_M().status();
                        String message = response.data().forgotPasswordValidation_M().message();
                        Log.i("forgot password ",status);
                        Log.i("message",message);
                        PasswordRecover.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status.equals("Success")) {
                                    progressdialog.dismiss();
                                    Toast.makeText(PasswordRecover.this, "Password Successfully Changed", Toast.LENGTH_LONG).show();
                                    Intent i=new Intent(PasswordRecover.this,PasswordRecover.class);
                                    startActivity(i);
                                } else if ((status.equals("Failure"))) {
                                    if (message.equals("Invalid OTP")) {
                                        progressdialog.dismiss();
                                        Toast.makeText(PasswordRecover.this, "OTP is incorrect", Toast.LENGTH_LONG).show();
                                    }
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
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }




}
