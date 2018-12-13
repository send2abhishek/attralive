package com.attra.attralive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.activity.BlogDetails;
import com.attra.attralive.activity.DetailedForumScreen;
import com.attra.attralive.activity.UpdateBlogActivity;
import com.attra.attralive.model.BlogListModel;


import java.util.ArrayList;

public class BlogListAdapter extends RecyclerView.Adapter<BlogListAdapter.MyViewHolder>
{
    Context mcontext;
    ArrayList<BlogListModel> blogListModels;

    BlogListModel blogListModel;
    public RelativeLayout relativeLayout;
    public BlogListAdapter(Context context, ArrayList<BlogListModel> blogArrayList)
    {
        mcontext=context;
        blogListModels =blogArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_row,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        blogListModel = blogListModels.get(position);
        holder.title_tv.setText(blogListModel.getTitle());
        holder.subtitle_tv.setText(blogListModel.getSubTitle());
        //Date date=blogListModel.getPublishTime();
       // DateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
      //  final String dateString=dateFormat.format(date);
        holder.time_tv.setText("Created On "+blogListModel.getPublishTime());
        holder.ratingBar.setRating(blogListModel.getRating());
        holder.technology_im.setImageResource(blogListModel.getImageId());
        holder.blogid.setText(blogListModel.getBlogId());
        holder.optionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup =new PopupMenu(mcontext,holder.optionsView);
//inflating menu from xml resource
                popup.inflate(R.menu.blog_action_option);
//adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                    case R.id.edit:
                        Intent intent =new Intent(mcontext,UpdateBlogActivity.class);
                        intent.putExtra("title",holder.title_tv.getText().toString());
                        intent.putExtra("blogId",holder.blogid.getText().toString());

                        mcontext.startActivity(intent);
                        return true;
                    case R.id.delete:
                        Intent intent1 =new Intent(mcontext,DetailedForumScreen.class);
                        mcontext.startActivity(intent1);
                        return true;
                    case R.id.block:

                        return true;
                    default:
                        return false;
}
                    }
                });
                popup.show();
            }
        });
        }


    @Override
    public int getItemCount() {
        return blogListModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv,subtitle_tv,time_tv,blogid;
        ImageView technology_im,optionsView;
        RatingBar ratingBar;
        LinearLayout relativeLayout;
        AppCompatActivity activity;
        private AdapterView.OnItemClickListener listener;
        public MyViewHolder(View itemView) {
            super(itemView);
            blogid=itemView.findViewById(R.id.tv_blogid);
            title_tv=itemView.findViewById(R.id.tv_title);
            subtitle_tv=itemView.findViewById(R.id.tv_subtitle);
            time_tv=itemView.findViewById(R.id.tv_time);
            technology_im=itemView.findViewById(R.id.im_blogicon);
            ratingBar=itemView.findViewById(R.id.ratingbar);
            optionsView=itemView.findViewById(R.id.textViewOptions);
            activity= (AppCompatActivity) itemView.getContext();
            relativeLayout = itemView.findViewById(R.id.relativelayout_animation);
            Fade fade=new Fade();
            Explode explode=new Explode();
            View decor=activity.getWindow().getDecorView();
            explode.excludeTarget(decor.findViewById(R.id.relativelayout_animation),true);
            explode.excludeTarget(android.R.id.statusBarBackground,true);
            explode.excludeTarget(android.R.id.navigationBarBackground,true);
            activity.getWindow().setEnterTransition(explode);
            activity.getWindow().setExitTransition(explode);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Fragment fragment=new BlogReadFragment();

                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_right_exit,
                            R.anim.fragment_slide_right_enter,R.anim.fragment_slide_left_exit).
                            add(R.id.container1,fragment).addToBackStack("GoBack").commit();*/

                    Intent intent=new Intent(activity,BlogDetails.class);
                    intent.putExtra("title",title_tv.getText().toString());
                    intent.putExtra("user",subtitle_tv.getText().toString());
                    intent.putExtra("blogId",blogid.getText().toString());
                    intent.putExtra("publishingTime",time_tv.getText().toString());
                    ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(activity,relativeLayout,ViewCompat.getTransitionName(relativeLayout));
                    activity.startActivity(intent,compat.toBundle());
                }
            });
        }
    }
}
