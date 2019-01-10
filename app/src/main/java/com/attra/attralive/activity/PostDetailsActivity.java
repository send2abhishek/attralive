package com.attra.attralive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import graphqlandroid.PostComments;

public class PostDetailsActivity extends AppCompatActivity {
ArrayList<AllComments> allpostcomments;
ArrayList<AllPostLikes> allpostlikeslist;
RecyclerView comments;
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
String postId,postuserId,location,filePath,profileImagePath,likedUserId,likedTimeago,likedUserName,likedDateAndTime,
            likedUserLocation,likedUserProfilePath,name,description,dateAndTime,timeago,commentedUserId,
        commentedUserName, commentedUserLocation,commentedUserProfilePath,commentedDateAndTime,commentedTimeago,commentMsg,commentId;
int likesCount,commentsCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        postidsaved=findViewById(R.id.tv_postid);
        comments=findViewById(R.id.rv_comments);
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
        allpostcomments=new ArrayList<AllComments>();
        allpostlikeslist=new ArrayList<AllPostLikes>();
        linearLayoutManager1=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager2=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            username = sharedPreferences.getString("userName","");
            userId = sharedPreferences.getString("userId","");
            refreshToken=sharedPreferences.getString("refreshToken","");
        }
post.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        allComments = new AllComments("gayathry", commentMsg, "bangalore", "ff", "1 sec");
        allpostcomments.add(allComments);
        postCommentsAdapter.notifyDataSetChanged();
        callPostComments(myToken);
        addcomments.setText("");
        v.setVisibility(View.GONE);

    }
});
        getPostDetails(myToken);
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
    private void callPostComments(String accesstoken)
    {
        MyAppolloClient.getMyAppolloClient("Bearer 0b952f1e04572a8b0af6d577878f2b62b91cec94").mutate
                (PostComments.builder().postId(postidsaved.getText().toString()).userId("5c370261e716cb72105e5fa1").
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
        MyAppolloClient.getMyAppolloClient("Bearer 0b952f1e04572a8b0af6d577878f2b62b91cec94").query(GetPostDetails.builder().postId("5c2f09ccb933eb5114649767").build()).enqueue(new ApolloCall.Callback<GetPostDetails.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetPostDetails.Data> response) {
                if (response.data().getPostDetails_Q() != null) {
                    postId = response.data().getPostDetails_Q().postDetails().get(0).postId();
                    postuserId = response.data().getPostDetails_Q().postDetails().get(0).userId();
                    location = response.data().getPostDetails_Q().postDetails().get(0).location();
                    likesCount = response.data().getPostDetails_Q().postDetails().get(0).likesCount();
                    commentsCount = response.data().getPostDetails_Q().postDetails().get(0).likesCount();
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
                            likedUserProfilePath = response.data().getPostDetails_Q().postDetails().get(0).likes().get(k).likedUserName();
                            if (postuserId.equals(likedUserId))
                            {
                                isLiked = true;
                            }
                            allPostLikes = new AllPostLikes(likedUserProfilePath);
                            allpostlikeslist.add(allPostLikes);
                        }
                    }
                    PostDetailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postidsaved.setText(postId);
                            Picasso.with(PostDetailsActivity.this).load(profileImagePath).into(userprofile);
                            postdescription.setText(description);
                            Picasso.with(PostDetailsActivity.this).load(filePath).into(postimage);
                            posttime.setText(timeago);
                            postedby.setText(name);
                            likescount.setText(likesCount + "");
                            commentscount.setText("" + likesCount);
                            userlocation.setText(location);
                            if (isLiked == true) {
                                likeimage.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                            }
                            likedUserAdapter = new LikedUserAdapter(PostDetailsActivity.this, allpostlikeslist);
                            likes.setLayoutManager(linearLayoutManager2);
                            likes.setAdapter(likedUserAdapter);
                            postCommentsAdapter = new PostCommentsAdapter(PostDetailsActivity.this, allpostcomments);
                            comments.setLayoutManager(linearLayoutManager1);
                            comments.setAdapter(postCommentsAdapter);
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
