package com.attra.attralive.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.util.GetNewRefreshToken;
import com.attra.attralive.util.NetworkUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import javax.annotation.Nonnull;
import graphqlandroid.OtpValidation;
import graphqlandroid.ResendOtp;
import graphqlandroid.SendDeviceToken;
import graphqlandroid.UserLoginAuth;


public class OtpValidationActivity extends AppCompatActivity {
    TextView validateOTP;
    EditText motpNumber1, motpNumber2, motpNumber3, motpNumber4;
    TextView resendOtp;
    ImageView closeOtp;
    NetworkUtil networkUtil= null;
    String emailId,password;
    String otpNumber;
    String token="";
    String opt;
    String refreshToken,RefreshToken;
    SharedPreferences sharedPreferences;
    private static String accessToken,authToken;
    public static String  Authorization= "Basic YXBwbGljYXRpb246c2VjcmV0";
   // public static final String PREFS_AUTH ="my_auth";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validation);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);

        closeOtp = findViewById(R.id.iv_close_otp);

        Intent intent=this.getIntent();
        if(intent !=null) {
            emailId = intent.getStringExtra("emailId");
            password = intent.getStringExtra("pass");
            Log.i("email id",emailId);
        }


        validateOTP =  findViewById(R.id.validate);
        motpNumber1 = (EditText) findViewById(R.id.otp_num1);
        motpNumber2 = (EditText) findViewById(R.id.otp_num2);
        motpNumber3 = (EditText) findViewById(R.id.otp_num3);
        motpNumber4 = (EditText) findViewById(R.id.otp_num4);
        resendOtp = findViewById(R.id.tv_resendOtp);

        motpNumber1.addTextChangedListener(CardNum1EntryWatcher);
        motpNumber2.addTextChangedListener(CardNum2EntryWatcher);
        motpNumber3.addTextChangedListener(CardNum3EntryWatcher);
        motpNumber4.addTextChangedListener(CardNum4EntryWatcher);
        motpNumber1.requestFocus();

        networkUtil = new NetworkUtil();

        closeOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(intent1);
            }
        });



        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motpNumber1.setText("");
                motpNumber2.setText("");
                motpNumber3.setText("");
                motpNumber4.setText("");
                motpNumber1.requestFocus();

                MyAppolloClient.getMyAppolloClient(token).mutate(
                        ResendOtp.builder().email(emailId)
                                .build()).enqueue(
                        new ApolloCall.Callback<ResendOtp.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<ResendOtp.Data> response) {
                                final String otpStatus = response.data().resendOTP_M().otpstatus();
                                String message = response.data().resendOTP_M().message();
                                Log.i("res_message",otpStatus);
                                OtpValidationActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(otpStatus.equals("Success")){

                                            Toast.makeText(OtpValidationActivity.this, "OTP has been sent successfully to you registered email id", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {
                            }
                        }
                );

            }
        });
        validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CardNumber1 = motpNumber1.getText().toString();
                String CardNumber2 = motpNumber2.getText().toString();
                String CardNumber3 = motpNumber3.getText().toString();
                String CardNumber4 = motpNumber4.getText().toString();

                otpNumber = CardNumber1+""+CardNumber2+CardNumber3+CardNumber4;
                Log.i("otp nuber",otpNumber);

                if(otpNumber.equals("")){
                    motpNumber1.setError("Enter OTP number");
                    motpNumber1.requestFocus();
                } else if(otpNumber !=""){
                    if (otpNumber.length() != 4) {
                        if (CardNumber1.equals("")) {
                            motpNumber1.setError("Enter OTP number");
                            motpNumber1.requestFocus();
                        } else if (CardNumber2.equals("")) {
                            motpNumber2.setError("Enter OTP number");
                            motpNumber2.requestFocus();
                        } else if (CardNumber3.equals("")) {
                            motpNumber3.setError("Enter OTP number");
                            motpNumber3.requestFocus();
                        } else if (CardNumber4.equals("")) {
                            motpNumber4.setError("Enter OTP number");
                            motpNumber4.requestFocus();
                        }

                    }else{
                        opt= otpNumber;
                        final ProgressDialog loading = ProgressDialog.show(OtpValidationActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callservice();
                                loading.dismiss();
                            }
                        }, 4000);
                    }
                }

            }
        });

    }
private void callservice()
{
    MyAppolloClient.getMyAppolloClient(token).mutate(
            OtpValidation.builder().email(emailId)
                    .otp(opt)
                    .build()).enqueue(
            new ApolloCall.Callback<OtpValidation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<OtpValidation.Data> response) {

                    String message= response.data().otpValidation_M().message();
                    final String otpStatus = response.data().otpValidation_M().otpstatus();
                                                Log.i("res_message",message);
                    OtpValidationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(otpStatus.equals("Success")){
                                getToken();
                                sendDeviceToken();

                            }
                            else if( (otpStatus.equals("Failure"))) {

                                if (message.equals("Invalid token: access token has expired")) {
                                    GetNewRefreshToken.getRefreshtoken(refreshToken, OtpValidationActivity.this);
                                    sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                    if (sharedPreferences.contains("authToken")) {
                                        String myToken = sharedPreferences.getString("authToken", "");
                                        callservice();
                                        Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                            else {
                                new AlertDialog.Builder(OtpValidationActivity.this)
                                        .setTitle("Wrong OTP")
                                        .setMessage("The OTP you entered doesn't match. Please re-enter the correct OTP")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }).show();
                                motpNumber1.setText("");
                                motpNumber2.setText("");
                                motpNumber3.setText("");
                                motpNumber4.setText("");
                                motpNumber1.requestFocus();
                            }


                        }
                    });
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                }
            }
    );
}

    private void sendDeviceToken(){

        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        MyAppolloClient.getMyAppolloClient(token).mutate(
                SendDeviceToken.builder().userId(emailId)
                        .deviceId(deviceToken)
                        .build()).enqueue(
                new ApolloCall.Callback<SendDeviceToken.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<SendDeviceToken.Data> response) {
                        String message= response.data().registerDeviceId_M().message();
                        final String status = response.data().registerDeviceId_M().status();
                        Log.i("res_message",message);

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }

    public void getToken(){
        MyAppolloClient.getMyAppolloClient(Authorization).query(
                UserLoginAuth.builder().username(emailId).password(password)
                        .build()).enqueue(
                new ApolloCall.Callback<UserLoginAuth.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<UserLoginAuth.Data> response) {
                        accessToken= response.data().userLoginAuth_Q().accessToken();
                        String tokenExpiry = response.data().userLoginAuth_Q().accessTokenExpiresAt();
                        refreshToken = response.data().userLoginAuth_Q().RefreshToken();
                        String refreshTokenExpiry = response.data().userLoginAuth_Q().accessTokenExpiresAt();
                        String user = response.data().userLoginAuth_Q().user();
                        String message = response.data().userLoginAuth_Q().message();
                        String userName = response.data().userLoginAuth_Q().name();
                        String userId = response.data().userLoginAuth_Q().user_id();
                        String status = response.data().userLoginAuth_Q().status();
                        Log.i("access Token",accessToken);
                        authToken="Bearer"+" "+accessToken;
                        Log.i("brarer token",authToken);
                        if(status.equals("Success")){

                          SharedPreferences  preferences = getApplicationContext().getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("authToken",authToken);
                            editor.putString("refreshToken",refreshToken);
                            editor.putString("emailId",emailId);
                            editor.putString("userId",userId);
                            editor.putString("userName",userName);
                            editor.commit();

                        }
                        OtpValidationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1 = new Intent(getApplicationContext(),UserDetailsActivity.class);
                                intent1.putExtra("emailId",emailId);
                                intent1.putExtra("password",password);
                                startActivity(intent1);
                            }
                        });/*else if(status.equals("Failure")){
                            if(message.equals("Invalid token: access token has expired")){

                                GetNewRefreshToken.getRefreshtoken(refreshToken,OtpValidationActivity.this);
                                sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                if (sharedPreferences.contains("authToken")) {
                                    String myToken = sharedPreferences.getString("authToken", "");
                                    getToken();
                                    Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                                }
                               // getNewRefreshToken(refreshToken);

                                *//*getNewRefreshToken(refreshToken);*//*

                            }

                        }*/

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }

    private TextWatcher CardNum1EntryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (motpNumber1.length() == 1){
                motpNumber2.requestFocus();
            }
        }
    };
    private TextWatcher CardNum2EntryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (motpNumber2.length() == 1){
                motpNumber3.requestFocus();
            }
        }
    };
    private TextWatcher CardNum3EntryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (motpNumber3.length() == 1){
                motpNumber4.requestFocus();
            }
        }
    };
    private TextWatcher CardNum4EntryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (motpNumber4.length() == 1){
                validateOTP.requestFocus();
            }
        }
    };
}

