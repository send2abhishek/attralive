package com.attra.attralive.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;

import javax.annotation.Nonnull;

import graphqlandroid.OtpValidation;
import graphqlandroid.UserRegistration;

public class RegistrationActivity extends AppCompatActivity {
    EditText fullname,email,password,confirmpassword;
    CardView regbutton;
    LinearLayout linearLayout1,linearLayout2;
    TextInputLayout fullnametil,emailtil,passwodtil,confirmpasswordtil;
    TextView passworderror,emailerror,fullnameerror,confrmpswderror,attraEmail;
    public static String MY_PREFS_NAME = "MyPrefsFile";
    String token="";
    String emailId,pwd;
    String status,message;
    String numRegex   = ".*[0-9].*";
    String alphaRegex = ".*[a-zA-Z].*";
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fullname=findViewById(R.id.et_fullname);
        email=findViewById(R.id.et_email);
        linearLayout1=findViewById(R.id.Ll_layout1);
        linearLayout2=findViewById(R.id.Ll_layout2);
        password=findViewById(R.id.et_regpassword);
        confirmpassword=findViewById(R.id.et_confirmpassword);
        fullnametil=findViewById(R.id.til_regusername);
        emailtil=findViewById(R.id.til_regemail);
        passwodtil=findViewById(R.id.til_regpswd);
        passworderror=findViewById(R.id.tv_passworderror);
        fullnameerror=findViewById(R.id.tv_usernameerror);
        emailerror=findViewById(R.id.tv_emailerror);
        confrmpswderror=findViewById(R.id.tv_confrmpswderror);
        attraEmail=findViewById(R.id.tv_attraemail);
        //fullname.requestFocus();
        regbutton=findViewById(R.id.crd_regbutton);
        fullnameerror.setTextColor(getResources().getColor(R.color.text_coloring_login));
        linearLayout2.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);

        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        //   SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        // 0 - for private mode`

       /* password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false) {

                     if (!((password.getText().toString().trim().matches(numRegex))&&
                            ( password.getText().toString().trim().matches(alphaRegex))) ) {
                       // passworderror.setText("");
                        passworderror.setTextColor(getResources().getColor(R.color.redcolor));
                        passworderror.setText("Password should contain alphanumeric");
                    }

                }
            }
        });
        fullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false) {
                    if (fullname.getText().toString().trim().length()<8 ) {
                        fullnameerror.setTextColor(getResources().getColor(R.color.redcolor));
                        fullnameerror.setError("Username should be valid");

                    }
                }
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false) {
                    if (Patterns.EMAIL_ADDRESS.matcher((email.getText().toString()+attraEmail.getText().toString()).trim()).matches() ) {
                        emailerror.setTextColor(getResources().getColor(R.color.redcolor));
                        emailerror.setError("Email should be valid emailID");

                    }
                }
            }
        });*/
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((fullname.getText().toString().trim().length()<6))
                {
                    fullnameerror.setTextColor(getResources().getColor(R.color.redcolor));
                    fullnameerror.setText("Enter valid username");
                    fullname.requestFocus();
                }
                else {
                    fullnameerror.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(Patterns.EMAIL_ADDRESS.matcher((email.getText().toString()+attraEmail.getText().toString()).trim()).matches() ) ){
                    emailerror.setTextColor(getResources().getColor(R.color.redcolor));
                    emailerror.setText("Email should be valid emailID");

                }
                else {
                    emailerror.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if((fullname.getText().toString().trim().isEmpty()))
                {
                    fullnameerror.setTextColor(getResources().getColor(R.color.redcolor));
                    fullnameerror.setText("username cannot be empty");
                    fullname.requestFocus();
                }
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
                    passworderror.setTextColor(getResources().getColor(R.color.redcolor));
                    passworderror.setText("Password should contain alphanumeric");
                }
                else
                    passworderror.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if((fullname.getText().toString().trim().isEmpty()))
                {
                    fullnameerror.setTextColor(getResources().getColor(R.color.redcolor));
                    fullnameerror.setText("username cannot be empty");
                    fullname.requestFocus();
                }
                else if(email.getText().toString().trim().isEmpty())
                {
                    emailerror.setTextColor(getResources().getColor(R.color.redcolor));
                    emailerror.setText("email cannot be empty");
                    email.requestFocus();
                }

            }
        });

        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((fullname.getText().toString().trim().isEmpty()))
                {
                    fullnameerror.setTextColor(getResources().getColor(R.color.redcolor));
                    fullnameerror.setText("username cannot be empty");
                    fullname.requestFocus();
                }
                else if(email.getText().toString().trim().isEmpty())
                {
                    emailerror.setTextColor(getResources().getColor(R.color.redcolor));
                    emailerror.setText("email cannot be empty");
                    email.requestFocus();
                }
                else if((password.getText().toString().trim().isEmpty()))
                {
                    passworderror.setTextColor(getResources().getColor(R.color.redcolor));
                    passworderror.setText("Password cannot be empty");
                    password.requestFocus();
                }
                confrmpswderror.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(password.getText().toString().trim().equals(s.toString())))
                {
                    confrmpswderror.setTextColor(getResources().getColor(R.color.redcolor));
                    confrmpswderror.setText("Password not matching");
                }
            }
        });
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loading = ProgressDialog.show(RegistrationActivity.this, "Registering", "Please wait ", false, false);
                Handler handler = new Handler();
                pwd= password.getText().toString();
                /*Intent intent = new Intent(RegistrationActivity.this, OtpValidationActivity.class);*/

                handler.postDelayed(new Runnable() {
                    public void run() {
                        loading.dismiss();
                        emailId= email.getText().toString()+attraEmail.getText().toString();
                        MyAppolloClient.getMyAppolloClient(token).mutate(UserRegistration.builder().name(fullname.getText().toString()).
                                email((email.getText().toString()+attraEmail.getText().toString())).password(password.getText().toString()).build()).
                                enqueue(new ApolloCall.Callback<UserRegistration.Data>() {
                                    @Override
                                    public void onResponse(@Nonnull Response<UserRegistration.Data> response) {

                                        status = response.data().addUser_M().status();
                                        message = response.data().addUser_M().message();
                                        RegistrationActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (status.equals("Success")) {
                                                    linearLayout1.setVisibility(View.GONE);
                                                    linearLayout2.setVisibility(View.GONE);
                                                    Toast.makeText(RegistrationActivity.this, "otp sent to your registered emailid", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(RegistrationActivity.this, OtpValidationActivity.class);
                                                    intent.putExtra("emailId",emailId );
                                                    intent.putExtra("pass",pwd);
                                                    Log.i("email in reg",emailId);
                                                    Log.i("pass in Reg",pwd);
                                                    startActivity(intent);
                                                }
                                                else if ((status.equals("Failure"))&&(message.equals("User already exists"))) {
                                                    linearLayout1.setVisibility(View.GONE);
                                                    linearLayout2.setVisibility(View.GONE);
                                                    Toast.makeText(RegistrationActivity.this, "Already registered. Please Login", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                                    intent.putExtra("emailId",emailId );
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(@Nonnull ApolloException e) {

                                    }
                                });


                    }
                }, 4000);



            }
        });
    }
}
