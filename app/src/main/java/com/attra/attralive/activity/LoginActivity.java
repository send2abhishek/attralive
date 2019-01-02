package com.attra.attralive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;

import javax.annotation.Nonnull;

import graphqlandroid.UserLoginAuth;

public class LoginActivity extends AppCompatActivity {
    private String username,password;
CardView loginbutton;
EditText userName,userPassword;
TextView attraemail,forgotpswd,registerHere;
TextInputLayout passwordtil,usernametil;
CheckBox saveLoginCheckBox;
String status,message;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        loginbutton=findViewById(R.id.crd_loginbutton);
        loginbutton.setBackgroundResource(R.drawable.color_gradient_login_btn);
        userName=findViewById(R.id.et_username);
        userPassword=findViewById(R.id.et_password);
        attraemail=findViewById(R.id.tv_attraemail);
        passwordtil=findViewById(R.id.testtil);
        usernametil=findViewById(R.id.usernametil);
        forgotpswd=findViewById(R.id.tv_forgotpassword);
        saveLoginCheckBox = findViewById(R.id.chk_remmebrme);
        registerHere = findViewById(R.id.tv_registerHere);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            userName.setText(loginPreferences.getString("username", ""));
            userPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(intent);
            }
        });


        forgotpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordtil.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // passwopard.getBackground().setColorFilter(getResources().getColor(R.color.text_coloring_login), PorterDuff.Mode.SRC_ATOP);
       // password.getBackground().clearColorFilter();
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            usernametil.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if(hasFocus==false)
                if(userName.getText().toString().trim().isEmpty()) {
                    usernametil.setError(getString(R.string.emptyusername_text));

                }
            }
        });
    }


    public void login(View view) {
        if(userName.getText().toString().trim().isEmpty()) {
            usernametil.setError(getString(R.string.emptyusername_text));
            userName.requestFocus();

        }
            else if(!Patterns.EMAIL_ADDRESS.matcher((userName.getText().toString()+attraemail.getText().toString()).trim()).matches())
        {
            usernametil.setError(getString(R.string.usernameerror_text));
            userName.requestFocus();

        }else if (userPassword.getText().toString().trim().isEmpty()) {
            passwordtil.setError(getString(R.string.passworderror_text));
            //password.setError();
            //password.setText("");
            userPassword.requestFocus();

        }
       else {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);

            username = userName.getText().toString();
            password = userPassword.getText().toString();

            if (saveLoginCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            Intent i=new Intent(LoginActivity.this,DashboardActivity.class);
            startActivity(i);
            passwordtil.setError(null);
            /*
            MyAppolloClient.getMyAppolloClient("").query(UserLoginAuth.builder().username(username.getText().toString()+attraemail.getText().toString().trim()).
                    password(password.getText().toString()).build()).enqueue(new ApolloCall.Callback<UserLoginAuth.Data>() {
                @Override
                public void onResponse(@Nonnull Response<UserLoginAuth.Data> response) {
                    status = response.data().userLoginAuth_Q().status().toString();
                    message = response.data().userLoginAuth_Q().message();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status.equals("Success")) {
                                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(LoginActivity.this,DashboardActivity.class);
                                startActivity(i);
                            } else if ((status.equals("Failure")) && (message.equals("Invalid Username or Password"))) {
                                Toast.makeText(LoginActivity.this,"Username or password is incorrect",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {

                }
            });*/
        }



    }
}
