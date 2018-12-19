package com.attra.attralive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.attra.attralive.R;

public class LoginActivity extends AppCompatActivity {
CardView loginbutton;
EditText username,password;
TextView attraemail,forgotpswd;
TextInputLayout passwordtil,usernametil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbutton=findViewById(R.id.crd_loginbutton);
        loginbutton.setBackgroundResource(R.drawable.color_gradient_login_btn);
        username=findViewById(R.id.et_username);
        password=findViewById(R.id.et_password);
        attraemail=findViewById(R.id.tv_attraemail);
        passwordtil=findViewById(R.id.testtil);
        usernametil=findViewById(R.id.usernametil);
        forgotpswd=findViewById(R.id.tv_forgotpassword);
        forgotpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        password.addTextChangedListener(new TextWatcher() {
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
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            usernametil.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(LoginActivity.this,"passwrd"+s,Toast.LENGTH_LONG).show();
            }
        });
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if(hasFocus==false)
                if(username.getText().toString().trim().isEmpty()) {
                    usernametil.setError(getString(R.string.emptyusername_text));

                }
            }
        });
    }

    public void login(View view) {
        if(username.getText().toString().trim().isEmpty()) {
            usernametil.setError(getString(R.string.emptyusername_text));
            username.requestFocus();

        }
            else if(!Patterns.EMAIL_ADDRESS.matcher((username.getText().toString()+attraemail.getText().toString()).trim()).matches())
        {
            usernametil.setError(getString(R.string.usernameerror_text));
            username.requestFocus();

        }else if (password.getText().toString().trim().isEmpty()) {
            passwordtil.setError(getString(R.string.passworderror_text));
            //password.setError();
            //password.setText("");
            password.requestFocus();

        }
       else {
            passwordtil.setError(null);
            Intent i=new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(i);
        }


    }
}
