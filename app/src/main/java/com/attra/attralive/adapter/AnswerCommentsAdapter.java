package com.attra.attralive.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.model.AnswerComments;
import com.attra.attralive.model.RepliesForAnswerComments;


import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.GetRepliesForAnswerComments;

public class AnswerCommentsAdapter extends RecyclerView.Adapter<AnswerCommentsAdapter.AnswerViewHolder> {
    Context context;
    ArrayList<AnswerComments> list;
    AnswerComments answerComments;
    RecyclerView replyanscomments;
    LayoutAnimationController controller=null;
    ReplayAdapter adapter;
    RepliesForAnswerComments repliesForAnswersCommentsPojo;

    LinearLayoutManager linearLayoutManager;
    ReplyForAnswersCommentsAdapter replyForAnswersAdapter;
    ArrayList<RepliesForAnswerComments> list1;
    public AnswerCommentsAdapter(Context context, ArrayList<AnswerComments> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forum_answer_reply_comments_list, viewGroup, false);
        AnswerViewHolder questionViewHolder = new AnswerViewHolder(view);
        return questionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder questionViewHolder, int i) {
        answerComments = list.get(i);
        questionViewHolder.tvAnswerComments.setText(answerComments.getComments());
        questionViewHolder.createdby.setText(answerComments.getUser() + ", " + answerComments.getLocation());
       // getReplyAnswerComments();

        GetRepliesForAnswers();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView tvAnswerComments, createdby;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnswerComments = itemView.findViewById(R.id.tv_questioncomments);
            createdby = itemView.findViewById(R.id.tv_usercreatedcomments);


           // replies = new ArrayList<>();
            replyanscomments = itemView.findViewById(R.id.rv_replayanscomments);
            linearLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);



        }

    }

    private void getReplyAnswerComments() {
        ArrayList<RepliesForAnswerComments> replies = new ArrayList<>();
        repliesForAnswersCommentsPojo = new RepliesForAnswerComments("Hello, this is a reply","Mohseen","Today 7:15pm");
        replies.add(repliesForAnswersCommentsPojo);
        repliesForAnswersCommentsPojo = new RepliesForAnswerComments("This is also a reply to comment","Mohseen","Today 8:17pm");
        replies.add(repliesForAnswersCommentsPojo);
        replyForAnswersAdapter = new ReplyForAnswersCommentsAdapter(context,replies);
        replyanscomments.setLayoutManager(linearLayoutManager);
        replyanscomments.setAdapter(replyForAnswersAdapter);
//        controller=AnimationUtils.loadLayoutAnimation(replyanscomments.getContext(),R.anim.layout_file);
//        replyanscomments.setLayoutAnimation(controller);
//        replyanscomments.scheduleLayoutAnimation();

    }

//    public void AddReplyToComments(View v)
//    {
//        TextView tvAddReply = v.findViewById(R.id.et_postComment);
//        tvAddReply.setText("Add comment");
//    }

    private void GetRepliesForAnswers()
    {
       final ArrayList<RepliesForAnswerComments> replies = new ArrayList<>();

        MyAppolloClient.getMyAppolloClient().query(graphqlandroid.GetRepliesForAnswerComments.builder().commId("5bf7c6ae069ec25614cba9c0").build())
                .enqueue(new ApolloCall.Callback<GetRepliesForAnswerComments.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetRepliesForAnswerComments.Data> response) {
                        if(response.data().Replies().get(0).replyMsg()==null){

                        }
                        else
                        {
                            for(int i =0;i<response.data().Replies().size();i++)
                            {
                                repliesForAnswersCommentsPojo = new RepliesForAnswerComments(
                                        response.data().Replies().get(i).replyMsg(),"Mohseen","7:10pm");
                                replies.add(repliesForAnswersCommentsPojo);
                            }
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    replyForAnswersAdapter = new ReplyForAnswersCommentsAdapter(context,replies);
                                    replyanscomments.setLayoutManager(linearLayoutManager);
                                    replyanscomments.setAdapter(replyForAnswersAdapter);
                                }
                            });


                            }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }
}
