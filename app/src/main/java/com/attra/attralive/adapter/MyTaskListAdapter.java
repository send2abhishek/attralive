package com.attra.attralive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.MyTaskList;

import java.util.ArrayList;

public class MyTaskListAdapter extends RecyclerView.Adapter<MyTaskListAdapter.MyViewHolder>
{
    Context mcontext;
    ArrayList<MyTaskList> myTaskLists;

    MyTaskList myTaskList;
    public MyTaskListAdapter(Context context, ArrayList<MyTaskList> myTaskArrayList) {
        mcontext = context;
        myTaskLists = myTaskArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mytask_list_row,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        myTaskList=myTaskLists.get(position);
        holder.title_tv.setText(myTaskList.getNoTitle());
        holder.subtitle_tv.setText(myTaskList.getNoSubTitle());
        holder.time_tv.setText(myTaskList.getNoTime());
        holder.technology_im.setImageResource(myTaskList.getImageId());

    }

    @Override
    public int getItemCount() {
        return myTaskLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv,subtitle_tv,time_tv;
        ImageView technology_im;
        public MyViewHolder(View itemView) {
            super(itemView);
            title_tv=itemView.findViewById(R.id.notification_title);
            subtitle_tv=itemView.findViewById(R.id.tv_notSubTitle);
            time_tv=itemView.findViewById(R.id.tv_time);
            technology_im=itemView.findViewById(R.id.im_blogicon);
        }
    }


}

