package com.attra.attralive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.text.style.LeadingMarginSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.ForgotPasswordActivity;
import com.attra.attralive.activity.NewsFeedPostActivity;
import com.attra.attralive.activity.PasswordRecover;
import com.attra.attralive.activity.PostDetailsActivity;
import com.attra.attralive.model.NewsFeed;

import com.attra.attralive.model.NewsFeedNew;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.ForgotPassword;
import graphqlandroid.LikePost;


public class NewsFeedListAdapter extends RecyclerView.Adapter<NewsFeedListAdapter.MyViewHolder>
{
    Context mcontext;
    ArrayList<NewsFeed>newsFeeds;
    public static final String PREFS_AUTH = "my_auth";
    private SharedPreferences sharedPreferences;

    NewsFeed newsFeed;
    String status, message,myToken,postId,userId,username;
    int updatedlikes;

    public NewsFeedListAdapter(Context context, ArrayList<NewsFeed> notificationArrayList) {
        mcontext = context;
        newsFeeds = notificationArrayList;
        sharedPreferences =context.getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            userId = sharedPreferences.getString("userId", "");
            username = sharedPreferences.getString("userName","");

            //      Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_LONG).show();
            Log.i("token in dashboard",myToken);
            Log.i("user id in dashboard",userId);

        }
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
        //postId = newsFeed.getPostId();
      //  Log.i("post id",postId);
          // holder.userImage.setImageResource(newsFeed.getImageId());



        holder.likeImg.setOnClickListener(v -> {

            if ((int) (holder.likeImg.getTag()) == R.drawable.ic_thumb_up_greyempty_24dp) {
                updatedlikes = Integer.parseInt(holder.noOfLikes.getText().toString());
                holder.noOfLikes.setText(""+(updatedlikes+1));
                holder.likeImg.setImageResource(R.drawable.ic_thumb_up_grey_24dp);
                holder.likeImg.setTag(R.drawable.ic_thumb_up_grey_24dp);


            } else if ((int) (holder.likeImg.getTag()) == R.drawable.ic_thumb_up_grey_24dp) {
                updatedlikes = Integer.parseInt(holder.noOfLikes.getText().toString());
                holder.noOfLikes.setText(""+(updatedlikes-1));
                holder.likeImg.setImageResource(R.drawable.ic_thumb_up_greyempty_24dp);
                holder.likeImg.setTag(R.drawable.ic_thumb_up_greyempty_24dp);

            }

            MyAppolloClient.getMyAppolloClient(myToken).mutate(LikePost.builder().postId(holder.postId.getText().toString()).userId(userId).build()).enqueue(new ApolloCall.Callback<LikePost.Data>() {
                @Override
                public void onResponse(@Nonnull Response<LikePost.Data> response) {
                    status = response.data().postLike_M().status();
                    message = response.data().postLike_M().message();
                    Log.i("like post ",status);
                    Log.i("message",message);
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {

                }
            });

        });

       holder.userName.setText(newsFeed.getUserName());
       Picasso.with(mcontext)
               .load(newsFeed.getImageId())
               .into(holder.userImage);
       //holder.userImage.setImageResource(newsFeed.getImageId());
        holder.title.setText(newsFeed.getLocation());
        holder.time.setText(newsFeed.getFeedTime());
        holder.description.setText(newsFeed.getFeedDescription());
        holder.noOfLikes.setText(String.valueOf(newsFeed.getNoOfLikes()));
        holder.noofComments.setText(String.valueOf(newsFeed.getNoOfCommenst()));
        holder.postId.setText(newsFeed.getPostId());
        Picasso.with(mcontext)
                .load(newsFeed.getNewsFeedImage())
                .into(holder.descriptionImage);
       // holder.like.setText((newsFeed.getlike()));

        if(newsFeed.getlike())
        {
            holder.likeImg.setImageResource(R.drawable.ic_thumb_up_grey_24dp);
            holder.likeImg.setTag(R.drawable.ic_thumb_up_grey_24dp);
        }
        else
        {
            holder.likeImg.setImageResource(R.drawable.ic_thumb_up_greyempty_24dp);
            holder.likeImg.setTag(R.drawable.ic_thumb_up_greyempty_24dp);
        }

        holder.optionmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postid = newsFeed.getPostId();
                String postedUserId = newsFeed.getUserid();

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mcontext,view);
                //inflating menu from xml resource
                popup.inflate(R.menu.newsfeed_options);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {


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
                });
                //displaying the popup
                Log.i("loggeduserId",userId);
                Log.i("likeduserID",postedUserId);

                if(userId.equals(postedUserId))
                {
                    popup.show();
                }
                else
                {
                    popup.dismiss();
                    Toast.makeText(mcontext,"Disabled",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }


    @Override
    public int getItemCount() {
        return newsFeeds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName,title,time,description,noOfLikes,noofComments,like,comment,postId;
        ImageView userImage,descriptionImage,likeImg;
        ImageButton optionmenu;
        AppCompatActivity activity;
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
            likeImg = itemView.findViewById(R.id.img_like);
            postId = itemView.findViewById(R.id.tv_postId);
            activity= (AppCompatActivity) itemView.getContext();
            System.out.println("adpter"+postId.getText().toString());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
Intent intent=new Intent(activity,PostDetailsActivity.class);
intent.putExtra("postId",postId.getText().toString());
activity.startActivity(intent);
                }
            });
        }
    }


}


