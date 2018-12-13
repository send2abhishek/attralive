package com.attra.attralive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.RepliesForAnswerComments;

import java.util.ArrayList;

public class ReplyForAnswersCommentsAdapter extends RecyclerView.Adapter<ReplyForAnswersCommentsAdapter.MyViewHolder> {
Context context;
    @Override
    public int getItemCount() {
        return list.size();
    }
    ArrayList<RepliesForAnswerComments> list;
    RepliesForAnswerComments RepliesForAnswerCommentsPojo;

    public ReplyForAnswersCommentsAdapter(Context mcontext, ArrayList<RepliesForAnswerComments> replyForAnswersList){
        context = mcontext;
        list = replyForAnswersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.replies_for_answers_comments_list,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i){
        RepliesForAnswerCommentsPojo=list.get(i);
        myViewHolder.name.setText(RepliesForAnswerCommentsPojo.getComments());
        myViewHolder.reply.setText(RepliesForAnswerCommentsPojo.getUser());
        myViewHolder.time.setText(RepliesForAnswerCommentsPojo.getTimeStamp());
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,reply;
        TextView time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userName);
            reply=itemView.findViewById(R.id.replyForComments);
            time=itemView.findViewById(R.id.timeWhenReplied);
        }
    }


}
