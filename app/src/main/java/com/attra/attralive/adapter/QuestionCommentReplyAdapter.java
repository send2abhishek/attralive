package com.attra.attralive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.ReplyQuestionComment;

import java.util.ArrayList;


public class QuestionCommentReplyAdapter extends RecyclerView.Adapter<QuestionCommentReplyAdapter.MyViewHolder> {
    Context context;
    ReplyQuestionComment replaycomments;
    ArrayList<ReplyQuestionComment> questionCommentsArrayList;


    public QuestionCommentReplyAdapter(Context mcontext, ArrayList<ReplyQuestionComment> replayList) {
        context=mcontext;
        questionCommentsArrayList=replayList;
    }

    @NonNull
    @Override
    public QuestionCommentReplyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_comment_reply_row,viewGroup,false);
        QuestionCommentReplyAdapter.MyViewHolder myViewHolder=new QuestionCommentReplyAdapter.MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull QuestionCommentReplyAdapter.MyViewHolder myViewHolder, int i) {
        replaycomments=questionCommentsArrayList.get(i);
        myViewHolder.name.setText(replaycomments.getName());
        myViewHolder.replay.setText(replaycomments.getReply());
        myViewHolder.replyTime.setText(replaycomments.getImageId());

    }

    @Override
    public int getItemCount() {
        return questionCommentsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,replay,replyTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameR);
            replay=itemView.findViewById(R.id.reply);
            replyTime=itemView.findViewById(R.id.tv_replyTime);
        }
    }
}

