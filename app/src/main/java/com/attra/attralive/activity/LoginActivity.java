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
import android.util.Log;
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
import com.attra.attralive.util.GetNewRefreshToken;


import javax.annotation.Nonnull;

import graphqlandroid.UserLoginAuth;

public class LoginActivity extends AppCompatActivity {
    private String username, password;
    CardView loginbutton;
    EditText etusername, userPassword;
    TextView attraemail, forgotpswd, registerHere;
    TextInputLayout passwordtil, usernametil;
    CheckBox saveLoginCheckBox;
    String status, message, accessToken, authToken;
    String myToken, refreshToken, name, userId, userName;
    private SharedPreferences loginPreferences, sharedPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        loginbutton = findViewById(R.id.crd_loginbutton);
        loginbutton.setBackgroundResource(R.drawable.color_gradient_login_btn);
        etusername = findViewById(R.id.et_username);
        userPassword = findViewById(R.id.et_password);
        attraemail = findViewById(R.id.tv_attraemail);
        passwordtil = findViewById(R.id.testtil);
        usernametil = findViewById(R.id.usernametil);
        forgotpswd = findViewById(R.id.tv_forgotpassword);
        saveLoginCheckBox = findViewById(R.id.chk_remmebrme);
        registerHere = findViewById(R.id.tv_registerHere);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.i("refresh token....",refreshedToken);
        if (saveLogin == true) {
            etusername.setText(loginPreferences.getString("username", ""));
            userPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            refreshToken = sharedPreferences.getString("refreshToken", "");
            userId = sharedPreferences.getString("userId", "");
            name = sharedPreferences.getString("userName", "");
            Log.i("user id in userDtail", userId);
            Log.i("refresh id in userDtail", refreshToken);

        }


        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);

            }
        });

        forgotpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
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
        etusername.addTextChangedListener(new TextWatcher() {
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


        etusername.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
        @Override
        public void onFocusChange (View v,boolean hasFocus){
        if (!hasFocus)
            if (etusername.getText().toString().trim().isEmpty()) {
                usernametil.setError(getString(R.string.emptyusername_text));

            }
    }
    });
}



    public void login(View view) {
        if (etusername.getText().toString().trim().isEmpty()) {
            usernametil.setError(getString(R.string.emptyusername_text));
            etusername.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher((etusername.getText().toString() + attraemail.getText().toString()).trim()).matches()) {
            usernametil.setError(getString(R.string.usernameerror_text));
            etusername.requestFocus();

        } else if (userPassword.getText().toString().trim().isEmpty()) {
            passwordtil.setError(getString(R.string.passworderror_text));
            //password.setError();
            //password.setText("");
            userPassword.requestFocus();

        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etusername.getWindowToken(), 0);

            username = etusername.getText().toString();
            password = userPassword.getText().toString();

            if (saveLoginCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.putString("userId", userId);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            /*Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(i);*/
            passwordtil.setError(null);
            callLoginservice(myToken);
        }


    }

    private void callLoginservice(String accesstoken) {

      //  Log.d("accesstoken", accesstoken);


      // Log.d("accesstoken", accesstoken);

        MyAppolloClient.getMyAppolloClient(GetNewRefreshToken.Authorization).query(UserLoginAuth.builder().username(username + attraemail.getText().toString().trim()).
                password(password).build()).enqueue(new ApolloCall.Callback<UserLoginAuth.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UserLoginAuth.Data> response) {
                if (response.data().userLoginAuth_Q() != null) {
                    status = response.data().userLoginAuth_Q().status();
                    Log.d("status", status);
                    message = response.data().userLoginAuth_Q().message();
                    //Log.d("status", message);
                    if (status.equals("Success")) {
                        accessToken = response.data().userLoginAuth_Q().accessToken();
                        String tokenExpiry = response.data().userLoginAuth_Q().accessTokenExpiresAt();
                        refreshToken = response.data().userLoginAuth_Q().RefreshToken();
                        String refreshTokenExpiry = response.data().userLoginAuth_Q().accessTokenExpiresAt();
                        String user = response.data().userLoginAuth_Q().user();
                        String message = response.data().userLoginAuth_Q().message();

                        userId = response.data().userLoginAuth_Q().user_id();
                        String status = response.data().userLoginAuth_Q().status();
                        Log.i("access Token", accessToken);
                        authToken = "Bearer" + " " + accessToken;
                        //refreshToken="Bearer"+" "+RefreshToken;
                        Log.i("brarer token", authToken);
                       userName=username.replace(".","");
                       Log.d("name",userName);
                       Log.d("username",username);
                    }
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status.equals("Success")) {
                                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
                                SharedPreferences preferences = getApplicationContext().getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("authToken", authToken);
                                editor.putString("refreshToken", refreshToken);
                                // editor.putString("emailId",emailId);
                                editor.putString("userId", userId);
                                editor.putString("userName", userName);
                                editor.apply();
                                //   editor.commit();
                                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(i);
                            } else if ((status.equals("Failure"))) {
                                if (message.equals("Username or password is incorrect")) {
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                } else if (message.equals("Invalid Username or Password")) {
                                    Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                } else {

                                    if (message.equals("Invalid token: access token is invalid")) {

                                        GetNewRefreshToken.getRefreshtoken(refreshToken, LoginActivity.this);
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                                if (sharedPreferences.contains("authToken")) {
                                                    String myToken = sharedPreferences.getString("authToken", "");
                                                    callLoginservice(myToken);
                                                    //Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                    }

                                }
                            }
                        }

                    });
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
            }
        });
    }


}









