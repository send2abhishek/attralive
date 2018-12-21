package com.attra.attralive.activity;

import android.content.Intent;
import android.graphics.Color;
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
        backtologinbtn=findViewById(R.id.backtologin);
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
