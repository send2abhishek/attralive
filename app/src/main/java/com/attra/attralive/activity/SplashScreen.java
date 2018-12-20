package com.attra.attralive.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.attra.attralive.R;

import static java.lang.System.in;

public class SplashScreen extends AppCompatActivity {

    private static int splashTimeOut = 1000;
    TextView tv_attralive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        tv_attralive = findViewById(R.id.attralive);



        String text = "<font color=#707070>Attra</font><font color=#f44f4b>LIVE</font>";
        tv_attralive.setText(Html.fromHtml(text));

     //  tv_attralive.startAnimation(AnimationUtils.loadAnimation(this,R.anim.textview_fade_animation));



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,DashboardActivity.class);
                startActivity(i);
            }
        }, splashTimeOut);
    }

}
