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
import com.attra.attralive.model.AllComments;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.MyViewHolder> {
    Context context;
    ArrayList<AllComments> arrayList;
    AllComments allComments;
    public PostCommentsAdapter(Context context, ArrayList<AllComments> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_comments_row,viewGroup,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
    allComments=arrayList.get(i);
    myViewHolder.username.setText(allComments.getCommentdby());
        myViewHolder.location.setText(allComments.getUserlocation());
        myViewHolder.time.setText(allComments.getCommenttime());
        myViewHolder.comment.setText(allComments.getCommentmsg());
//        Picasso.with(context).load(allComments.getUserimagepath()).into(myViewHolder.userimage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username,location,time,comment;
        ImageView userimage;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            username=itemView.findViewById(R.id.tv_userName);
            location=itemView.findViewById(R.id.tv_location);
            time=itemView.findViewById(R.id.tv_timeago);
            comment=itemView.findViewById(R.id.tv_commentmsg);
            userimage=itemView.findViewById(R.id.im_profile);
        }
    }
}
