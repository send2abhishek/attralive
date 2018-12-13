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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;

import java.util.Arrays;

import javax.annotation.Nonnull;

import graphqlandroid.AddQuestionForum;

public class CreateForumActivity extends AppCompatActivity {
    EditText forumTitle,forumKeywords,forumDescription;
    Button createForumButton;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_forum);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("ASK QUESTION");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        forumTitle = findViewById(R.id.et_ForumTitle);
        forumKeywords = findViewById(R.id.et_Forumkeywords);
        forumDescription = findViewById(R.id.et_ForumDescription);
        createForumButton = findViewById(R.id.btn_createForum);
        linearLayout = findViewById(R.id.createForumMainLayout);
        createForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = forumTitle.getText().toString();
                String keywords = forumKeywords.getText().toString();
                String description = forumDescription.getText().toString();
                if(title.trim().equals("")){
                    forumTitle.setError("Title is required");
                    forumTitle.requestFocus();
                }
                else if(keywords.trim().equals("")) {
                    forumKeywords.setError("Keywords is required");
                    forumKeywords.requestFocus();
                }
                else if(description.trim().equals("")) {
                    forumDescription.setError("Description is required");
                } else
                {


                    loadFragment();

                   submitPost(v);
                }



            }
        });

    }

    private void loadFragment() {
        // load fragment
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("FORUM","LOAD_FORUM");
        startActivity(intent);
      /* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace((FrameLayout)DashboardActivity.(R.id.frame_container),  new ForumFragment());
        transaction.addToBackStack(null);
        transaction.commit();*/
    }

    public void submitPost(View v){
        MyAppolloClient.getMyAppolloClient().mutate(
                AddQuestionForum.builder().topicVal(forumTitle.getText().toString())
                        .keywordsVal(Arrays.asList(forumKeywords.getText().toString().split(",")))
                        .descriptionVal(forumDescription.getText().toString())
                        .userVal("5bdc1f635d3211192f245db9")
                        .build()).enqueue(
                new ApolloCall.Callback<AddQuestionForum.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<AddQuestionForum.Data> response) {
                        CreateForumActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             //   Snackbar.make(linearLayout,"added",Snackbar.LENGTH_LONG).show();
                               // Toast.makeText(CreateForumActivity.this,"Added successfully!!!",Toast.LENGTH_SHORT).show();


                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Toast.makeText(CreateForumActivity.this,"Error!!!",Toast.LENGTH_SHORT).show();
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
