package com.attra.attralive.activity;

import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.provider.FontRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.attra.attralive.R;

public class NewsFeedPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_post);
        /*FontRequest fontRequest = new FontRequest(
                "com.example.fontprovider",
                "com.example",
                "emoji compat Font Query",
                CERTIFICATES);
        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest);
        EmojiCompat.init(config);*/
    }
}
