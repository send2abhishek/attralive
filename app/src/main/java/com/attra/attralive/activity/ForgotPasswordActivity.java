package com.attra.attralive.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.attra.attralive.R;

public class ForgotPasswordActivity extends AppCompatActivity {
LinearLayout backtologinbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        actionBar.hide();
        backtologinbtn=findViewById(R.id.back_to_login_layout);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        backtologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
