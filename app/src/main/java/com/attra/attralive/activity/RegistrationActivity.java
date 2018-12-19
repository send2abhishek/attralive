package com.attra.attralive.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import com.attra.attralive.R;

public class RegistrationActivity extends AppCompatActivity {
EditText fullname,email,password,confirmpassword;
TextInputLayout fullnametil,emailtil,passwodtil,confirmpasswordtil;
TextView passworderror,emailerror,fullnameerror,confrmpswderror,attraEmail;
    String numRegex   = ".*[0-9].*";
    String alphaRegex = ".*[a-zA-Z].*";
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fullname=findViewById(R.id.et_fullname);
        email=findViewById(R.id.et_email);
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
        fullnameerror.setTextColor(getResources().getColor(R.color.text_coloring_login));

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
    }
}
