package com.attra.attralive.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.adapter.ReplayAdapter;
import com.attra.attralive.model.ReplyBlogComments;


import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.ReplayToComments;

public class ReplayActivity extends AppCompatActivity {
TextView tv1,tv2,tv3;
ImageView im;
ArrayList<ReplyBlogComments> replyList;
ReplyBlogComments obj;
RecyclerView recyclerView;
ReplayAdapter myAdapter;
LinearLayoutManager linearLayoutManager;
EditText replay;
Button replayPost;
String Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_screen);
        tv1=findViewById(R.id.tv_name);
        tv2=findViewById(R.id.tv_comments);
        tv3=findViewById(R.id.tv_mainheading);
        im=findViewById(R.id.im_image);
        recyclerView=findViewById(R.id.rv_replaylist);
        replay=findViewById(R.id.et_Replay);
        replayPost=findViewById(R.id.bt_replaypost);
        replyList=new ArrayList<ReplyBlogComments>();
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        Intent in=getIntent();
        Name=in.getStringExtra("name");
        String str1="Replay's for ";
        SpannableStringBuilder str=new SpannableStringBuilder(str1+Name+" post");
        str.setSpan(new StyleSpan(Typeface.BOLD),str1.length(),str1.length()+Name.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       // tv3.setTypeface(null,Typeface.BOLD);
        tv3.setText(str);
        tv1.setText(in.getStringExtra("name"));
        tv2.setText(in.getStringExtra("comments"));
       String image=in.getStringExtra("image");
       Integer intgr=Integer.parseInt(image);
       im.setImageResource(intgr);
       replyList=in.getParcelableArrayListExtra("list");
       /*obj=replyList.get(0);
        Toast.makeText(this,"list is"+obj.getName(),Toast.LENGTH_LONG).show();*/
        myAdapter=new ReplayAdapter(this,replyList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        replay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            replayPost.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            if(replay.length()==0)
                replayPost.setVisibility(View.GONE);
            }
        });
    }

    public void replayCommentsBlog(View view) {
        obj=new ReplyBlogComments("gayathry",replay.getText().toString(),R.drawable.ic_dropbox);
        replyList.add(obj);
        myAdapter.notifyDataSetChanged();
        saveReplayToDB();
        replay.setText("");
        replayPost.setVisibility(View.GONE);
    }
    public void saveReplayToDB()
    {
        MyAppolloClient.getMyAppolloClient().mutate(ReplayToComments.builder().replyMsg(replay.getText().toString()).
                build()).enqueue(new ApolloCall.Callback<ReplayToComments.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<ReplayToComments.Data> response)
            {
             ReplayActivity.this.runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     Toast.makeText(ReplayActivity.this, "successfully added reply"+response.data().addReply().replyMsg(), Toast.LENGTH_SHORT).show();
                 }
             });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }
}
