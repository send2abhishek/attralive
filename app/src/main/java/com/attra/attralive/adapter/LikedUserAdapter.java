package com.attra.attralive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.attra.attralive.R;
import com.attra.attralive.model.AllPostLikes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikedUserAdapter extends RecyclerView.Adapter<LikedUserAdapter.MyViewHolder> {
    Context context;
    ArrayList<AllPostLikes> arrayList;
    AllPostLikes allPostLikes;
    public LikedUserAdapter(Context context, ArrayList<AllPostLikes> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_likes_row,viewGroup,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
    allPostLikes=arrayList.get(i);
        Picasso.with(context).load(allPostLikes.getLikeduserimagepath()).into(myViewHolder.userprofile);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView userprofile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userprofile=itemView.findViewById(R.id.im_likeuserimage);
        }
    }
}
