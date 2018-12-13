package com.attra.attralive.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.model.QuestionComments;
import com.attra.attralive.model.ReplyQuestionComment;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class QuestionCommentsAdapter extends RecyclerView.Adapter<QuestionCommentsAdapter.QuestionViewHolder> {
    Context context;
    ArrayList<QuestionComments> list;
    QuestionComments questionComments;
    LinearLayoutManager linearLayoutManager2;
    RecyclerView replyCommentsRecyclerview;
    QuestionCommentReplyAdapter questionCommentReplyAdapter;
    ReplyQuestionComment replaycomments;
    ArrayList<ReplyQuestionComment> questionCommentsArrayList;
    com.attra.attralive.activity.QuestionComments questionCommentsActivity;
    private Handler mImagesProgressHandler;
    public QuestionCommentsAdapter(Context context,ArrayList<QuestionComments> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_comments_row,viewGroup,false);
        QuestionViewHolder questionViewHolder=new QuestionViewHolder(view);
        return questionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder questionViewHolder, int i) {
        questionComments=list.get(i);
        questionViewHolder.questioncomments.setText(questionComments.getComments());
        questionViewHolder.createdby.setText(questionComments.getUser()+", "+questionComments.getLocation());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questioncomments,createdby,noOfReply;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questioncomments=itemView.findViewById(R.id.tv_questioncomments);
            createdby=itemView.findViewById(R.id.tv_usercreatedcomments);
            noOfReply = itemView.findViewById(R.id.tv_noofreplay);
            replyCommentsRecyclerview = itemView.findViewById(R.id.rv_questionCommentReply);

            replyCommentsRecyclerview.setVisibility(View.VISIBLE);
            linearLayoutManager2=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            questionCommentsArrayList = new ArrayList<ReplyQuestionComment>();
           /* getReplyQuestionComments();*/
            replyQuestionComment();

        }

    }

    private  void replyQuestionComment(){
        MyAppolloClient.getMyAppolloClient().query(graphqlandroid.ReplyQuestionComment.builder().id("5bf3caaa1b50914658c1648a").
                build()).enqueue(new ApolloCall.Callback<graphqlandroid.ReplyQuestionComment.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<graphqlandroid.ReplyQuestionComment.Data> response) {

                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if(response.data().Question().comments().size()==0){

                        }
                        else {
                            for(int loopVar=0;loopVar<response.data().Question().comments().size();loopVar++){
                                if(response.data().Question().comments().get(loopVar).replies().get(loopVar).replyMsg().equals(null)){
                                    replaycomments=null;
                                }else {

                                    replaycomments= new com.attra.attralive.model.ReplyQuestionComment(response.data().Question().comments().get(loopVar).replies().get(loopVar).user().get(loopVar).name(),response.data().Question().comments().get(loopVar).replies().get(loopVar).replyMsg(),response.data().Question().comments().get(loopVar).replies().get(loopVar).timestamp());
                                    questionCommentsArrayList.add(replaycomments);
                                }

                            }
                            questionCommentReplyAdapter = new QuestionCommentReplyAdapter(context,questionCommentsArrayList);
                            replyCommentsRecyclerview.addItemDecoration(new DividerItemDecoration(replyCommentsRecyclerview.getContext(),DividerItemDecoration.VERTICAL));
                            replyCommentsRecyclerview.setLayoutManager(linearLayoutManager2);
                            replyCommentsRecyclerview.setAdapter(questionCommentReplyAdapter);

                        }

                    }
                });




            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
        }



    private void getReplyQuestionComments()
    {
        replaycomments=new ReplyQuestionComment("hello","jasgdkas","today");
        questionCommentsArrayList.add(replaycomments);
        replaycomments=new ReplyQuestionComment("hello","jasgdkas","today");
        questionCommentsArrayList.add(replaycomments);
        replaycomments=new ReplyQuestionComment("hello","jasgdkas","yesterday");
        questionCommentsArrayList.add(replaycomments);

    }

}
