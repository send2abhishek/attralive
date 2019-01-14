package com.attra.attralive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.adapter.LikedUserAdapter;
import com.attra.attralive.adapter.PostCommentsAdapter;
import com.attra.attralive.model.AllComments;
import com.attra.attralive.model.AllPostLikes;
import com.attra.attralive.util.GetNewRefreshToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.GetPostDetails;
import graphqlandroid.LikePost;
import graphqlandroid.PostComments;

public class PostDetailsActivity extends AppCompatActivity {
ArrayList<AllComments> allpostcomments;
ArrayList<AllPostLikes> allpostlikeslist;
RecyclerView rvcomments;
RecyclerView likes;
LikedUserAdapter likedUserAdapter;
PostCommentsAdapter postCommentsAdapter;
LinearLayoutManager linearLayoutManager1,linearLayoutManager2;
EditText addcomments;
ImageView userprofile,postprofile,postimage,likeimage;
TextView postdescription,postedby,posttime,userlocation,likescount,commentscount,postidsaved;
SharedPreferences sharedPreferences;
String myToken,username,userId,refreshToken;
Button post;
Boolean isLiked=false;
AllPostLikes allPostLikes;
AllComments allComments;
String postIdinput;
String postId,postuserId,location,filePath,profileImagePath,likedUserId,likedTimeago,likedUserName,likedDateAndTime,
            likedUserLocation,likedUserProfilePath,name,description,dateAndTime,timeago,commentedUserId,
        commentedUserName, commentedUserLocation,commentedUserProfilePath,commentedDateAndTime,commentedTimeago,commentMsg,commentId;
int likesCount,commentsCount,updatelikescount;
Intent intent;
String worklocation,profileimage,noficimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        //postidsaved=findViewById(R.id.tv_postid);
        rvcomments=findViewById(R.id.rv_comments);
        likes=findViewById(R.id.rv_likes);
      addcomments=findViewById(R.id.et_Comment);
        userprofile=findViewById(R.id.img_userImage);
        postimage=findViewById(R.id.img_descImage);
       postprofile=findViewById(R.id.im_commentProfilePic);
        postdescription=findViewById(R.id.tv_newsfeedDesc);
        postedby=findViewById(R.id.tv_username);
        posttime=findViewById(R.id.tv_time);
        userlocation=findViewById(R.id.tv_title);
        likescount=findViewById(R.id.tv_noOfLikes);
        commentscount=findViewById(R.id.tv_noOfComments);
      post=findViewById(R.id.bt_post);
        likeimage=findViewById(R.id.img_like);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Post Details");
        allpostcomments=new ArrayList<AllComments>();
        allpostlikeslist=new ArrayList<AllPostLikes>();
        intent=getIntent();
        postIdinput=intent.getStringExtra("postId");
         noficimage=intent.getStringExtra("imagepath");
        if(noficimage==null)
        {
            Picasso.with(PostDetailsActivity.this).load("https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1547216418003-image.jpeg").into(postprofile);
        }
        else
        Picasso.with(PostDetailsActivity.this).load(noficimage).into(postprofile);

        linearLayoutManager1=new LinearLayoutManager(PostDetailsActivity.this,LinearLayoutManager.VERTICAL,false);
       linearLayoutManager2=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
       /* allComments = new AllComments("q","q","q","q","q");
        allpostcomments.add(allComments);*/
        postCommentsAdapter = new PostCommentsAdapter(PostDetailsActivity.this, allpostcomments);
        rvcomments.addItemDecoration(new DividerItemDecoration(rvcomments.getContext(),DividerItemDecoration.VERTICAL));
        rvcomments.setLayoutManager(linearLayoutManager1);
        rvcomments.setAdapter(postCommentsAdapter);
        likedUserAdapter = new LikedUserAdapter(PostDetailsActivity.this, allpostlikeslist);
        likes.setLayoutManager(linearLayoutManager2);
        likes.setAdapter(likedUserAdapter);
        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);

        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            username = sharedPreferences.getString("userName","");
            userId = sharedPreferences.getString("userId","");
            refreshToken=sharedPreferences.getString("refreshToken","");
            worklocation=sharedPreferences.getString("location","");
            profileimage=sharedPreferences.getString("profileImagePath","");
        }
       getPostDetails(myToken);
likeimage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if((int)(likeimage.getTag())==R.drawable.ic_thumb_up_grey_24dp)
        {
            updatelikescount= Integer.parseInt(likescount.getText().toString());
            likescount.setText(""+(updatelikescount-1));
            likeimage.setImageResource(R.drawable.ic_thumb_up_greyempty_24dp);
            likeimage.setTag(R.drawable.ic_thumb_up_greyempty_24dp);
        }
        else if((int)(likeimage.getTag())==R.drawable.ic_thumb_up_greyempty_24dp)
        {
            updatelikescount= Integer.parseInt(likescount.getText().toString());
            likescount.setText(""+(updatelikescount+1));
            likeimage.setImageResource(R.drawable.ic_thumb_up_grey_24dp);
            likeimage.setTag(R.drawable.ic_thumb_up_grey_24dp);
        }
        calllikepost(myToken);
    }
});
   post.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        allComments = new AllComments(username, addcomments.getText().toString(), worklocation, profileimage, "just now");
        allpostcomments.add(allComments);
        postCommentsAdapter.notifyDataSetChanged();
        callPostComments(myToken);
        addcomments.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        v.setVisibility(View.GONE);

    }
});

        addcomments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                post.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(addcomments.length()==0){
                    post.setVisibility(View.GONE);

            }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            finish();
        }
        return true;

        //return super.onOptionsItemSelected(item);
    }
    private void calllikepost(String accesstoken)
    {
        MyAppolloClient.getMyAppolloClient(myToken).mutate(LikePost.builder().postId(postIdinput).userId(userId).build()).enqueue(new ApolloCall.Callback<LikePost.Data>() {
            @Override
            public void onResponse(@Nonnull Response<LikePost.Data> response) {
                String status = response.data().postLike_M().status();
                String message = response.data().postLike_M().message();
                Log.i("like post ",status);
                Log.i("message",message);
                if(status.equals("Success")){
                    if (message.equals("User Liked this Post")){
                        //holder.likeImg.setImageResource(R.drawable.ic_thumb_up_color_24dp);

                    }
                }else if(status.equals("Failure")){
                    if(message.equals("User Disliked this Post")){
                      //  holder.likeImg.setImageResource(R.drawable.ic_thumb_up_grey_24dp);
                    }
                }

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });

    }
    private void callPostComments(String accesstoken)
    {
        MyAppolloClient.getMyAppolloClient(accesstoken).mutate
                (PostComments.builder().postId(postIdinput).userId(userId).
                        comment(addcomments.getText().toString()).build()).
                enqueue(new ApolloCall.Callback<PostComments.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<PostComments.Data> response) {
                        String status=response.data().postComment_M().status();
                        String message=response.data().postComment_M().message();
                        if(status.equals("Success"))
                        {
                            Log.d("aaa",status);

                        }
                        if(status.equals("Failure"))
                        {
                            Log.d("aaa",status);
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }
    private void getPostDetails(String accesstoken)
    {
        System.out.println("accesstoken"+accesstoken);
        Log.d("postid",postIdinput);
        MyAppolloClient.getMyAppolloClient(accesstoken).query(GetPostDetails.builder().postId(postIdinput).build()).enqueue(new ApolloCall.Callback<GetPostDetails.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetPostDetails.Data> response) {
                System.out.println("hhh"+response.data().getPostDetails_Q().postDetails().get(0).commentsCount());
                if (response.data().getPostDetails_Q() != null) {
                    postId = response.data().getPostDetails_Q().postDetails().get(0).postId();
                    postuserId = response.data().getPostDetails_Q().postDetails().get(0).userId();
                    location = response.data().getPostDetails_Q().postDetails().get(0).location();
                    likesCount = response.data().getPostDetails_Q().postDetails().get(0).likesCount();
                    commentsCount = response.data().getPostDetails_Q().postDetails().get(0).comments().size();
                    name = response.data().getPostDetails_Q().postDetails().get(0).name();
                    description = response.data().getPostDetails_Q().postDetails().get(0).description();
                    filePath = response.data().getPostDetails_Q().postDetails().get(0).filePath();
                    profileImagePath = response.data().getPostDetails_Q().postDetails().get(0).filePath();
                    //dateAndTime=response.data().getPostDetails_Q().postDetails().get(0).dateAndTime();
                    //timeago=response.data().getPostDetails_Q().postDetails().get(0).timeago();
                    if (response.data().getPostDetails_Q().postDetails().get(0).comments().size() > 0) {
                        for (int i = 0; i < response.data().getPostDetails_Q().postDetails().get(0).comments().size(); i++) {
                            commentedUserProfilePath = response.data().getPostDetails_Q().postDetails().get(0).comments().get(i).commentedUserProfilePath();
                            commentedUserLocation = response.data().getPostDetails_Q().postDetails().get(0).comments().get(i).commentedUserLocation();
                            commentedUserName = response.data().getPostDetails_Q().postDetails().get(0).comments().get(i).commentedUserName();
                            commentedTimeago = response.data().getPostDetails_Q().postDetails().get(0).comments().get(i).commentedTimeago();
                            commentMsg = response.data().getPostDetails_Q().postDetails().get(0).comments().get(i).commentMsg();
                            allComments = new AllComments(commentedUserName, commentMsg, commentedUserLocation, commentedUserProfilePath, commentedTimeago);
                            allpostcomments.add(allComments);

                        }
                    }
                    if (response.data().getPostDetails_Q().postDetails().get(0).likes().size() > 0) {
                        for (int k = 0; k < response.data().getPostDetails_Q().postDetails().get(0).likes().size(); k++) {

                            likedUserId = response.data().getPostDetails_Q().postDetails().get(0).likes().get(k).likedUserId();
                            likedUserProfilePath = response.data().getPostDetails_Q().postDetails().get(0).likes().get(k).likedUserProfilePath();
                            if (userId.equals(likedUserId)) {
                                isLiked = true;
                            }
                            allPostLikes = new AllPostLikes(likedUserProfilePath);
                            allpostlikeslist.add(allPostLikes);
                        }
                    }
                    PostDetailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // postidsaved.setText(postId);
                            Picasso.with(PostDetailsActivity.this).load(profileImagePath).into(userprofile);
                            postdescription.setText(description);
                            Picasso.with(PostDetailsActivity.this).load(filePath).into(postimage);
                            posttime.setText(timeago);
                            postedby.setText(name);
                            likescount.setText(likesCount + "");
                            commentscount.setText("" + commentsCount);
                            userlocation.setText(location);
                            if (isLiked == true) {
                                likeimage.setImageResource(R.drawable.ic_thumb_up_grey_24dp);
                                likeimage.setTag(R.drawable.ic_thumb_up_grey_24dp);
                            }
                            else
                            {
                                likeimage.setImageResource(R.drawable.ic_thumb_up_greyempty_24dp);
                                likeimage.setTag(R.drawable.ic_thumb_up_greyempty_24dp);
                            }
                            postCommentsAdapter.notifyDataSetChanged();
                            likedUserAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }
}
