package com.attra.attralive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.adapter.AnswerCommentsAdapter;
import com.attra.attralive.adapter.CommentsAdapter;
import com.attra.attralive.model.AnswerComments;
import com.attra.attralive.model.RepliesForAnswerComments;
import com.attra.attralive.model.ReplyBlogComments;


import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.AddCommentsForAnswers;
import graphqlandroid.GetCommentsForAnswer;

public class AddAnswerComments extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager1;
    LinearLayoutManager linearLayoutManager;
    EditText reply;
    Button replyPost;
    AnswerComments comments;
    RecyclerView answerComments;
    CommentsAdapter myAdapter;
    ArrayList<ReplyBlogComments> replyList;
    ReplyBlogComments objReplyBlogComments;
    RepliesForAnswerComments repliesForAnswersCommentsPojo;
    ArrayList<RepliesForAnswerComments> replies;
    AnswerComments answerCommentsPojo;
    LayoutAnimationController controller=null;
    Intent intent;
    ArrayList<AnswerComments> answerCommentsArrayList;
    AnswerCommentsAdapter answerCommentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer_comments);
        answerComments = findViewById(R.id.answerComments);
        reply=findViewById(R.id.et_postComment);
        replyPost=findViewById(R.id.bt_replypost);
        linearLayoutManager1=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        answerCommentsArrayList= new ArrayList<AnswerComments>();
        replies=new ArrayList<>();
        getAnswerComments();



        reply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                replyPost.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(reply.length()==0)
                    replyPost.setVisibility(View.GONE);
            }
        });

    }

    public void AddReplyToComments()
    {
//        TextView tv = findViewById(R.id.et_postComment);
//        tv.setText("Welcome to android");
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View vi = inflater.inflate(R.layout.activity_add_answer_comments, null); //log.xml is your file.
//        TextView tv = (TextView)vi.findViewById(R.id.et_postComment);
//        tv.setText("HI");
    }

    public void PostAnswerComments(View view) {
        comments=new AnswerComments(reply.getText().toString(),"Mohseen","");


        answerCommentsArrayList.add(comments);
        answerCommentsAdapter.notifyDataSetChanged();
        AddAnsCommentsToDB();
        reply.setText("");
        view.setVisibility(View.GONE);

    }

    public void AddAnsCommentsToDB()
    {
        MyAppolloClient.getMyAppolloClient().mutate(AddCommentsForAnswers.builder()
                .userId("ddd").answerId("5bf7c62f069ec25614cba9bf")
        .comment(reply.getText().toString()).build())
                .enqueue(new ApolloCall.Callback<AddCommentsForAnswers.Data>() {
                                                                        @Override
                                                                        public void onResponse(@Nonnull Response<AddCommentsForAnswers.Data> response) {
                                                                            AddAnswerComments.this.runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    Toast.makeText(AddAnswerComments.this, "comment added successfully", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                        }

                                                                        @Override
                                                                        public void onFailure(@Nonnull ApolloException e) {

                                                                        }
                                                                    }
        );
    }



    private void getAnswerComments(){
        MyAppolloClient.getMyAppolloClient().query(graphqlandroid.GetCommentsForAnswer.builder().quesId("5bf7c54e069ec25614cba9be").build())
                .enqueue(new ApolloCall.Callback<GetCommentsForAnswer.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetCommentsForAnswer.Data> response) {
                        if(response.data().Answers().get(0).comments().size()==0){


                        }
                        else {
                            for(int loopVar=0;loopVar<response.data().Answers().get(0).comments().size();loopVar++){
                                answerCommentsPojo = new AnswerComments(response.data().Answers().get(0).comments()
                                .get(loopVar).comment(),"Mohseen","");
                                answerCommentsArrayList.add(answerCommentsPojo);
                                for(int loopRep=0;loopRep<response.data().Answers().get(0).comments().get(0).replies().size();loopRep++)
                                {
                                    repliesForAnswersCommentsPojo=new RepliesForAnswerComments("gayathry",response.data().Answers().get(0)
                                    .comments().get(0).replies().get(loopRep).replyMsg(),"hgdhga");
                                    replies.add(repliesForAnswersCommentsPojo);

                                }
                                AddAnswerComments.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        answerCommentsAdapter=new AnswerCommentsAdapter(AddAnswerComments.this,answerCommentsArrayList);
                                        //  answerComments.addItemDecoration(new DividerItemDecoration(answerComments.getContext(),DividerItemDecoration.VERTICAL));
                                        answerComments.setAdapter(answerCommentsAdapter);
                                        answerComments.setLayoutManager(linearLayoutManager1);


                                    }
                                });
                               // replies.clear();
                               // answerComments.setLayoutAnimation(controller);
                               // answerComments.scheduleLayoutAnimation();
                            }
                        }

                        controller=AnimationUtils.loadLayoutAnimation(answerComments.getContext(),R.anim.layout_file);



                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });

    }

}
