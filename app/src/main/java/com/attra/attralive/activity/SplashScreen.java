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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.attra.attralive.R;
import com.google.firebase.iid.FirebaseInstanceId;

import static java.lang.System.in;

public class SplashScreen extends AppCompatActivity {

    private static int splashTimeOut = 3500;
    TextView tv_attralive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        tv_attralive = findViewById(R.id.attralive);

//        Log.i("device token",FirebaseInstanceId.getInstance().getToken());


        String text = "<font color=#707070>Attra</font><font color=#f44f4b>LIVE</font>";
        tv_attralive.setText(Html.fromHtml(text));

        //  tv_attralive.startAnimation(AnimationUtils.loadAnimation(this,R.anim.textview_fade_animation));

        try {
            RunAnimation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(i);
            }
        }, splashTimeOut);
    }

    private void RunAnimation() throws InterruptedException {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

        a.reset();
        TextView tv = (TextView) findViewById(R.id.presents);
        tv.clearAnimation();
        tv.startAnimation(a);
        a.reset();
        TextView tv1= findViewById(R.id.attralive);
        tv1.clearAnimation();
        tv1.startAnimation(a);

    }
}
