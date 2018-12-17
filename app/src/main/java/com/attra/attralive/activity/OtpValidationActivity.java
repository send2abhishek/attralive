package com.attra.attralive.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.attra.attralive.R;

public class OtpValidationActivity extends AppCompatActivity {
    TextView validateOTP;
    EditText motpNumber1, motpNumber2, motpNumber3, motpNumber4;
    ProgressDialog prgDialog;
    TextView resendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validation);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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

        validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CardNumber1 = motpNumber1.getText().toString();
                String CardNumber2 = motpNumber2.getText().toString();
                String CardNumber3 = motpNumber3.getText().toString();
                String CardNumber4 = motpNumber4.getText().toString();

                final String otpNumber = CardNumber1+""+CardNumber2+CardNumber3+CardNumber4;

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

                    } else if (!otpNumber.equals("1234")) {
                        final ProgressDialog loading = ProgressDialog.show(OtpValidationActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                loading.dismiss();
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
                        }, 4000);
                    }else{
                        Intent intent = new Intent(getApplicationContext(),UserDetailsActivity.class);
                        startActivity(intent);

                    }
                    //Hiding the alert dialog

                    //Displaying a progressbar


                }

            }
        });

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

