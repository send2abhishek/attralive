package com.attra.attralive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.adapter.QuestionCommentsAdapter;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.PostQuestionComment;
import graphqlandroid.QuestionComment;

public class QuestionComments extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager1;
    TextView allcomments;
    ArrayList<com.attra.attralive.model.QuestionComments> questionCommentsArrayList;
    QuestionCommentsAdapter questionCommentsAdapter;
    com.attra.attralive.model.QuestionComments questionComments;
    RecyclerView questioncommentsrecyclerview;
    EditText commentText;
    Intent intent;
    Button button,comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_comments);
        questioncommentsrecyclerview=findViewById(R.id.rv_questionComments);
       // allcomments=findViewById(R.id.tv_allcomments);
        commentText=findViewById(R.id.et_Comment);
        button=findViewById(R.id.bt_post);
        comment = findViewById(R.id.bt_post);

        ActionBar actionBar=getSupportActionBar();

        actionBar.setTitle("Comments");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);


        linearLayoutManager1=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        questionCommentsArrayList=new ArrayList<com.attra.attralive.model.QuestionComments>();
        getQuestionComment();

        questionCommentsAdapter=new QuestionCommentsAdapter(this,questionCommentsArrayList);
        questioncommentsrecyclerview.addItemDecoration(new DividerItemDecoration(questioncommentsrecyclerview.getContext(),DividerItemDecoration.VERTICAL));
        questioncommentsrecyclerview.setLayoutManager(linearLayoutManager1);
        questioncommentsrecyclerview.setAdapter(questionCommentsAdapter);

        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                button.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(commentText.length()==0)
                    button.setVisibility(View.GONE);

            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionComments=new com.attra.attralive.model.QuestionComments(commentText.getText().toString(),"Awnish","");
                questionCommentsArrayList.add(0,questionComments);
  //              questionCommentsArrayList.remove(2);
                questionCommentsArrayList.add(questionComments);

                questionCommentsAdapter.notifyDataSetChanged();
           //     postCommentsToDB();
                commentText.setText("");
                v.setVisibility(View.GONE);
            }
        });

    }

 /*   public void replyComment(View view){


    }


    public void postComments(View view) {


    }
*/



    private void getQuestionComment(){
        MyAppolloClient.getMyAppolloClient().query(graphqlandroid.QuestionComment.builder().id("5bf3caaa1b50914658c1648a").
                build()).enqueue(new ApolloCall.Callback<QuestionComment.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<QuestionComment.Data> response) {
                if(response.data().Question().comments().size()==0){
                    questionComments= new com.attra.attralive.model.QuestionComments("","","");
                    questionCommentsArrayList.add(questionComments);
                }
                else {
                    for(int loopVar=0;loopVar<response.data().Question().comments().size();loopVar++){
                        if(response.data().Question().comments().get(loopVar).replies().get(loopVar).replyMsg().equals(null)) {

                        }
                        questionComments= new com.attra.attralive.model.QuestionComments(response.data().Question().comments().get(loopVar).comment(),response.data().Question().comments().get(loopVar).user().get(loopVar).name(),"");
                        questionCommentsArrayList.add(questionComments);
                    }
                }


            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });


    }

    public void postCommentsToDB()
    {
        MyAppolloClient.getMyAppolloClient().mutate(PostQuestionComment.builder().userId("5bcd72dce814cc4b8cca8fad").questionId("5bf3caaa1b50914658c1648a").comment(commentText.getText().toString()).build()).
                enqueue(new ApolloCall.Callback<PostQuestionComment.Data>() {
                    @Override
                    public void onResponse(@Nonnull final Response<PostQuestionComment.Data> response) {
                        QuestionComments.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(QuestionComments.this,"added comments to DB"+response.data().addQuesComment(),Toast.LENGTH_LONG).show();
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

}
