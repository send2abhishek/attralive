package com.attra.attralive.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.NewsFeed;

import java.util.ArrayList;

public class NewsFeedListAdapter extends RecyclerView.Adapter<NewsFeedListAdapter.MyViewHolder>
{
    Context mcontext;
    ArrayList<NewsFeed> newsFeeds;

    NewsFeed newsFeed;
    public NewsFeedListAdapter(Context context, ArrayList<NewsFeed> notificationArrayList) {
        mcontext = context;
        newsFeeds = notificationArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_row,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        newsFeed = newsFeeds.get(position);
        holder.userName.setText(newsFeed.getUserName());
       // holder.userImage.setImageResource(newsFeed.getImageId());
        holder.title.setText(newsFeed.getTitle());
        holder.time.setText(newsFeed.getFeedTime());
        holder.description.setText(newsFeed.getFeedDescription());
        holder.noOfLikes.setText(newsFeed.getNoOfLikes());
        holder.noofComments.setText(newsFeed.getNoOfCommenst());
       // holder.descriptionImage.setImageResource(newsFeed.getNewsFeedImage());

        holder.optionmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mcontext,view);
                //inflating menu from xml resource
                popup.inflate(R.menu.newsfeed_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editFeed:
                                //handle menu1 click
                                return true;
                            case R.id.deleteFeed:
                                //handle menu2 click
                                return true;
                            case R.id.reportFeed:
                                //handle menu3 click
                                return true;
                            case R.id.disableFeed:
                                //handle menu3 click
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return newsFeeds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName,title,time,description,noOfLikes,noofComments,like,comment;
        ImageView userImage,descriptionImage;
        ImageButton optionmenu;
        public MyViewHolder(View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.tv_username);
            title=itemView.findViewById(R.id.tv_title);
            time=itemView.findViewById(R.id.tv_time);
            userImage=itemView.findViewById(R.id.img_userImage);
            description = itemView.findViewById(R.id.tv_newsfeedDesc);
            noOfLikes= itemView.findViewById(R.id.tv_noOfLikes);
            noofComments = itemView.findViewById(R.id.tv_noOfComments);
            like= itemView.findViewById(R.id.tv_like);
            comment= itemView.findViewById(R.id.tv_comment);
            descriptionImage = itemView.findViewById(R.id.img_descImage);
            optionmenu= itemView.findViewById(R.id.ib_popup_menu);
        }
    }


}


