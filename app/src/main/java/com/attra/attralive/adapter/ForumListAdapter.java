package com.attra.attralive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.activity.DetailedForumScreen;
import com.attra.attralive.model.ForumQuestionListModel;

import java.util.ArrayList;


public class ForumListAdapter extends RecyclerView.Adapter<ForumListAdapter.MyViewHolder>{
    Context mcontext;
    ArrayList<ForumQuestionListModel> forumQuestionListModels;
    ForumQuestionListModel model;
    public ForumListAdapter(Context mcontext, ArrayList<ForumQuestionListModel> forumQuestionListModels) {
        this.mcontext = mcontext;
        this.forumQuestionListModels = forumQuestionListModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_list_row,parent,false);
        ForumListAdapter.MyViewHolder viewHolder=new ForumListAdapter.MyViewHolder(view);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        model =   forumQuestionListModels.get(position);

        holder.question.setText(model.getTopic());
        holder.answers_count.setText("0");
        //Date date=blogListModel.getPublishTime();
        // DateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
        //  final String dateString=dateFormat.format(date);
        holder.votes_count.setText(""+5);
        holder.watch_count.setText(""+model.getWatch_count());
        holder.tv_time.setText("1 hour ago");
        holder.quesId.setText(model.getId());

    }

    @Override
    public int getItemCount() {
        return forumQuestionListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView question,tv_time,votes_count,votes,answers_count,answers,watch_count,watch,quesId;
        ImageView im_blogicon,iv_clock;
        LinearLayout relativeLayout;
        AppCompatActivity activity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            im_blogicon = itemView.findViewById(R.id.im_blogicon);
            question = itemView.findViewById(R.id.question);
            tv_time = itemView.findViewById(R.id.tv_time);
            votes_count = itemView.findViewById(R.id.votes_count);
            votes = itemView.findViewById(R.id.votes);
            answers_count = itemView.findViewById(R.id.answers_count);
            answers = itemView.findViewById(R.id.answers);
            watch_count = itemView.findViewById(R.id.watch_count);
            watch = itemView.findViewById(R.id.watch);
            quesId=itemView.findViewById(R.id.tv_quesid);
            relativeLayout = itemView.findViewById(R.id.relativelayout_animation);
            final Activity activity=(AppCompatActivity) itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(mcontext,DetailedForumScreen.class);
                    in.putExtra("quesid",quesId.getText().toString());
                    activity.startActivity(in);

                }
            });

        }
    }
}
