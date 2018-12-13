package com.attra.attralive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.ReplyBlogComments;

import java.util.ArrayList;

public class ReplayAdapter extends RecyclerView.Adapter<ReplayAdapter.MyViewHolder> {
    Context context;
    ArrayList<ReplyBlogComments> list;
    ReplyBlogComments replaycomments;
    public ReplayAdapter(Context mcontext, ArrayList<ReplyBlogComments> replayList) {
        context=mcontext;
        list=replayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_reply_row,viewGroup,false);
       MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        replaycomments=list.get(i);
        myViewHolder.name.setText(replaycomments.getName());
        myViewHolder.replay.setText(replaycomments.getReply());
        myViewHolder.profile.setImageResource(replaycomments.getImageId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,replay;
        ImageView profile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameR);
            replay=itemView.findViewById(R.id.reply);
            profile=itemView.findViewById(R.id.im_profileR);
        }
    }
}
