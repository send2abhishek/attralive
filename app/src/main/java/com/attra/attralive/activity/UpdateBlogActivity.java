package com.attra.attralive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;

import javax.annotation.Nonnull;

import graphqlandroid.GetDescription;
import graphqlandroid.UpdateBlog;

public class UpdateBlogActivity extends AppCompatActivity {

    EditText blogTitle, blogDescription;
    Button createBlog;
    String titleBlog,blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_blog);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Update Blog");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        blogTitle = findViewById(R.id.et_blogTitle);
       // blogKeyWords = findViewById(R.id.et_keywords);
        blogDescription = findViewById(R.id.et_description);
        createBlog = findViewById(R.id.btn_createBlog);

        Intent in=getIntent();
        titleBlog=in.getStringExtra("title");
        blogId=in.getStringExtra("blogId");
        overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
        blogTitle.setText(titleBlog);
        getDescriptionfromDB();
        createBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = blogTitle.getText().toString();
               // String keywords = blogKeyWords.getText().toString();
                String description = blogDescription.getText().toString();
                if(title.trim().equals("")){
                    blogTitle.setError("Title is required");
                    blogTitle.requestFocus();
                }
               /* else if(keywords.trim().equals("")) {
                    blogKeyWords.setError("Keywords is required");
                    blogKeyWords.requestFocus();
                }*/
                else if(description.trim().equals("")) {
                    blogDescription.setError("Description is required");
                } else
                {
                    updatePost(v);
                }


            }
        });

    }
    public void getDescriptionfromDB()
    {
        MyAppolloClient.getMyAppolloClient().query(GetDescription.builder().id(blogId).build()).enqueue(new ApolloCall.Callback<GetDescription.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<GetDescription.Data> response) {
                UpdateBlogActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        blogDescription.setText(response.data().Blog().description());
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }


    public void updatePost(View v){
        MyAppolloClient.getMyAppolloClient().mutate(UpdateBlog.builder().blogid(blogId).description(blogDescription.getText().toString())
                        .build()).enqueue(new ApolloCall.Callback<UpdateBlog.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateBlog.Data> response) {
                UpdateBlogActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateBlogActivity.this,"updated",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }


    });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.createblog_toolbar, menu);
        return true;
    }
}
