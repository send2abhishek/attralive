package com.attra.attralive.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;

import javax.annotation.Nonnull;

import graphqlandroid.CreateNewBlog;

//import graphqlandroid.CreateNewBlog;

public class CreateBlogActivity extends AppCompatActivity {
    EditText blogTitle, blogKeyWords, blogDescription;
    Button createBlog;
    LinearLayout  linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Create Blog");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        blogTitle = findViewById(R.id.et_blogTitle);
        blogKeyWords = findViewById(R.id.et_keywords);
        blogDescription = findViewById(R.id.et_description);
        createBlog = findViewById(R.id.btn_createBlog);
        linearLayout=findViewById(R.id.mainlinearlayout);
        createBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = blogTitle.getText().toString();
                String keywords = blogKeyWords.getText().toString();
                String description = blogDescription.getText().toString();
                if(title.trim().equals("")){
                    blogTitle.setError("Title is required");
                    blogTitle.requestFocus();
                }
                else if(keywords.trim().equals("")) {
                    blogKeyWords.setError("Keywords is required");
                    blogKeyWords.requestFocus();
                }
                else if(description.trim().equals("")) {
                    blogDescription.setError("Description is required");
                } else
                 {
                    submitPost(v);
                }


            }
        });

    }


    public void submitPost(View v){
        MyAppolloClient.getMyAppolloClient().mutate(
                CreateNewBlog.builder().blogTitle(blogTitle.getText().toString())
                        .blogCategory(blogKeyWords.getText().toString())
                        .blogDescription(blogDescription.getText().toString())
                        .blogStatus("Sachin").
                        user("Awnish").subCategory("Java")
                .build()).enqueue(
                new ApolloCall.Callback<CreateNewBlog.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<CreateNewBlog.Data> response) {
                        CreateBlogActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(linearLayout,"added",Snackbar.LENGTH_LONG).show();
                                //Toast.makeText(CreateBlogActivity.this,"Added successfully!!!",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );
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
