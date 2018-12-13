package com.attra.attralive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.activity.ReplayActivity;
import com.attra.attralive.model.Comments;
import com.attra.attralive.model.ReplyBlogComments;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    ArrayList<Comments> mlist;
   public  ArrayList<ReplyBlogComments> replayList;
    Context mcontext;
    Comments comments;
    ReplyBlogComments replyBlogComments;
    ReplayAdapter myAdapter1;
    LinearLayoutManager linearLayoutManager1 ;
    public CommentsAdapter(Context context, ArrayList<Comments> list)
    {
        mlist=list;
        mcontext=context;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_comments_row,viewGroup,false);
       MyViewHolder myViewHolder=new MyViewHolder(view);
       return myViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i)
    {
    comments=mlist.get(i);
    myViewHolder.im_profile.setImageResource(comments.getImageID());
    myViewHolder.im_profile.setTag(comments.getImageID());
    myViewHolder.tv_name.setText(comments.getName());
    myViewHolder.tv_comments.setText(comments.getComments());
        prepareReplayData();
        if(linearLayoutManager1!=null)
            callLayout();
        myAdapter1=new ReplayAdapter(mcontext,replayList);
        myViewHolder.replay_rv.setAdapter(myAdapter1);
        myViewHolder.replay_rv.setLayoutManager(linearLayoutManager1);
        myViewHolder.replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(mcontext,ReplayActivity.class);
                in.putExtra("image",myViewHolder.im_profile.getTag().toString());
                in.putExtra("name",myViewHolder.tv_name.getText().toString());
                in.putExtra("comments",myViewHolder.tv_comments.getText().toString());
               in.putExtra("list",replayList);
                mcontext.startActivity(in);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name,tv_comments,replay;
        ImageView im_profile;
        RecyclerView replay_rv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.name);
            tv_comments=itemView.findViewById(R.id.comment);
            replay=itemView.findViewById(R.id.tv_replay);
            im_profile=itemView.findViewById(R.id.im_profile);
            replay_rv=itemView.findViewById(R.id.rv_reply);
            replayList=new ArrayList<ReplyBlogComments>();
            callLayout();

        }
    }

    private void prepareReplayData()
    {
        if(replayList!=null)
            replayList.clear();

        replyBlogComments=new ReplyBlogComments("gayathry","hai hello",R.drawable.ic_foursquare);
        replayList.add(replyBlogComments);
        replyBlogComments=new ReplyBlogComments("Awnish","welcome good work",R.drawable.ic_dropbox);
        replayList.add(replyBlogComments);

    }
    private void callLayout()
    {
        linearLayoutManager1 =new LinearLayoutManager(mcontext,LinearLayoutManager.VERTICAL,false);
    }
}
