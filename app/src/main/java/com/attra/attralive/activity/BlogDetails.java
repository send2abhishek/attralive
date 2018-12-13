package com.attra.attralive.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.adapter.CommentsAdapter;
import com.attra.attralive.model.Comments;
import com.attra.attralive.model.ReplyBlogComments;


import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.BlockBlog;
import graphqlandroid.DeleteBlog;
import graphqlandroid.LikeDislikeBlog;
import graphqlandroid.RateBlog;

public class BlogDetails extends AppCompatActivity {
    TextView tv,ratingtv,profileName,blogDescription,likes,dislikes,allcomments,uploadedTime,likedUsers;
    RatingBar rating;
    LayoutAnimationController controller=null;
    ScrollView scrollView;
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    //RecyclerView.ItemAnimator animator=new
    ArrayList<Comments> list;
    LinearLayoutManager linearLayoutManager;
    CommentsAdapter myAdapter;
    Comments comments;
    EditText et_comment;
    Button button;
    Intent intent;
    int likesCount,dislikesCount;
    LinearLayout deleteBlog,reportBlog,blockBlog;
    ImageView blogDescriptionImage,imgLike,imgDislike;
    TextView noOfLikes,noOfDislike;
    public  ArrayList<ReplyBlogComments> replayList;
    ReplyBlogComments replyBlogComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_details);
        tv=findViewById(R.id.tv_blogDescription);
        blogDescriptionImage=findViewById(R.id.im_blogDescription);
        uploadedTime=findViewById(R.id.tv_uploadedTime);
        ratingtv=findViewById(R.id.tv_rating);
        profileName=findViewById(R.id.tv_profileName);
        scrollView=findViewById(R.id.scrollView1);
        recyclerView=findViewById(R.id.rv_comments);
        likes=findViewById(R.id.tv_like);
        dislikes=findViewById(R.id.tv_dislike);
        imgLike= findViewById(R.id.img_like);
        imgDislike = findViewById(R.id.img_dislike);
        likedUsers = findViewById(R.id.tv_likedUsers);
        noOfLikes = findViewById(R.id.tv_like);
        noOfDislike = findViewById(R.id.tv_dislike);
        allcomments=findViewById(R.id.tv_allcomments);
        et_comment=findViewById(R.id.et_Comment);
        blogDescription=findViewById(R.id.tv_blogDescription);

        imgLike.setTag(R.drawable.ic_thumb_up_grey_24dp);
        //  likeques.setTag(R.drawable.ic_thumb_up_color_24dp);
        imgDislike.setTag(R.drawable.ic_thumb_down_grey_24dp);

        //blogDescription.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

       // blogDescription.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        ratingtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        button=findViewById(R.id.bt_post);
         intent=getIntent();
         profileName.setText(intent.getStringExtra("user"));
         uploadedTime.setText(intent.getStringExtra("publishingTime"));
        list=new ArrayList<Comments>();
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        prepareCommentsdata();


        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) (imgLike.getTag()) == R.drawable.ic_thumb_up_grey_24dp) {
                    if ((int) (imgDislike.getTag()) == R.drawable.ic_thumb_down_grey_24dp) {
                        likeDislike("like");
                        // System.out.println("likescountgayathry" + likesCount);
                        imgLike.setImageResource(R.drawable.ic_thumb_up_color_24dp);
                        imgDislike.setTag(R.drawable.ic_thumb_up_color_24dp);
                        String likeno = noOfLikes.getText().toString();
                        int likec = Integer.parseInt(likeno);
                        noOfLikes.setText("" + (likec + 1));
                    } else {
                        likeDislike("like");
                        // System.out.println("likescountgayathry" + likesCount);
                        imgLike.setImageResource(R.drawable.ic_thumb_up_color_24dp);
                        imgLike.setTag(R.drawable.ic_thumb_up_color_24dp);
                        String likeno = noOfLikes.getText().toString();
                        int likec = Integer.parseInt(likeno);
                        noOfLikes.setText("" + (likec + 1));
                        imgDislike.setImageResource(R.drawable.ic_thumb_down_grey_24dp);
                        imgDislike.setTag(R.drawable.ic_thumb_down_grey_24dp);
                        String dlikeno = noOfDislike.getText().toString();
                        int dlikec = Integer.parseInt(dlikeno);
                        if (dlikec != 0)
                            noOfDislike.setText((dlikec - 1) + "");

                    }
                } else {

                }
             String   likeDislikes= "like";
                likeDislike(likeDislikes);
                MyAppolloClient.getMyAppolloClient().query(graphqlandroid.BlogDetails.builder().id(intent.getStringExtra("blogId")).
                        build()).enqueue(new ApolloCall.Callback<graphqlandroid.BlogDetails.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<graphqlandroid.BlogDetails.Data> response) {

                        likesCount=response.data().Blog().likes_count();
                        dislikesCount=response.data().Blog().unlikes_count();

                        BlogDetails.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                likes.setText(""+likesCount);
                                dislikes.setText(""+dislikesCount);
                                if(likesCount>1){
                                    likedUsers.setText("You,"+ (likesCount-1)+"others");
                                }else if(likesCount==1){
                                    likedUsers.setText("You");
                                }else{
                                    likedUsers.setVisibility(View.GONE);
                                    noOfLikes.setVisibility(View.GONE);
                                }


                            }
                        });
                    }
                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
            }
        });



        imgDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((int) (imgDislike.getTag()) == R.drawable.ic_thumb_down_grey_24dp) {
                    if ((int) (imgLike.getTag()) == R.drawable.ic_thumb_up_grey_24dp) {
                        String   likeDislikes= "dislike";
                        likeDislike(likeDislikes);
                        imgDislike.setImageResource(R.drawable.ic_thumb_down_color_24dp);
                        imgDislike.setTag(R.drawable.ic_thumb_down_color_24dp);
                        String dlikeno = noOfDislike.getText().toString();
                        int dlikec = Integer.parseInt(dlikeno);
                        noOfDislike.setText("" + (dlikec + 1));
                        // Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                    } else {
                        String   likeDislikes= "dislike";
                        likeDislike(likeDislikes);
                        // System.out.println("likescountgayathry"+dislikeCount);
                        imgDislike.setImageResource(R.drawable.ic_thumb_down_color_24dp);
                        imgDislike.setTag(R.drawable.ic_thumb_down_color_24dp);
                        String dlikeno = noOfDislike.getText().toString();
                        int dlikec = Integer.parseInt(dlikeno);

                        noOfDislike.setText("" + (dlikec + 1));
                        // Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                        imgLike.setImageResource(R.drawable.ic_thumb_up_grey_24dp);
                        imgLike.setTag(R.drawable.ic_thumb_up_grey_24dp);
                        String likeno = noOfLikes.getText().toString();
                        int likec = Integer.parseInt(likeno);
                        if (likec != 0)
                            noOfLikes.setText((likec - 1) + "");
                    }
                } else {

                }

                MyAppolloClient.getMyAppolloClient().query(graphqlandroid.BlogDetails.builder().id(intent.getStringExtra("blogId")).
                        build()).enqueue(new ApolloCall.Callback<graphqlandroid.BlogDetails.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<graphqlandroid.BlogDetails.Data> response) {

                       /* likesCount=response.data().Blog().likes_count();
                        dislikesCount=response.data().Blog().unlikes_count();

                        BlogDetails.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                likes.setText(""+likesCount);
                                dislikes.setText(""+dislikesCount);

                            }
                        });*/
                    }
                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
            }
        });

        allcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayAllComments();

            }
        });
        //toolbar=findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();

        actionBar.setTitle(intent.getStringExtra("title"));
        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Fade fade=new Fade();
        View decor=getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.relativelayout_animation),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        /*tv.setMovementMethod(new ScrollingMovementMethod());
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
    et_comment.addTextChangedListener(new TextWatcher() {
         @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    button.setVisibility(View.VISIBLE);

    }

    @Override
    public void afterTextChanged(Editable s) {
             if(et_comment.length()==0)
                 button.setVisibility(View.GONE);

    }
});

    }
private void ShowDialog()
{
    System.out.println("hitting dialog");

    final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
    //final RatingBar rating = new RatingBar(this);
    View view = getLayoutInflater().inflate(R.layout.rating_dialog, null);
    rating = view.findViewById(R.id.ratingBar);

    popDialog.setTitle("How is this blog?");
    //popDialog.setView(rating);
// Button OK
    popDialog.setNeutralButton("Submit", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            rateBlog((int) rating.getRating());
            dialog.dismiss();
        }
    });

    popDialog.create();
    popDialog.setView(view);
    popDialog.show();

}


    private void likeDislike(String likeDislike){
        MyAppolloClient.getMyAppolloClient().mutate(
                LikeDislikeBlog.builder().blogId(intent.getStringExtra("blogId")).user_id("5bf27fe2bbfa3185af136dcc").likeDislike(likeDislike).build()).enqueue(
                new ApolloCall.Callback<LikeDislikeBlog.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<LikeDislikeBlog.Data> response) {
                        BlogDetails.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BlogDetails.this,"Liked successfully!!!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });

    }

    private void rateBlog(Integer rate)

{
    MyAppolloClient.getMyAppolloClient().mutate(
            RateBlog.builder().id(intent.getStringExtra("blogId")).userId("abcd").rating(rate).build()).enqueue(
            new ApolloCall.Callback<RateBlog.Data>() {
                @Override
                public void onResponse(@Nonnull Response<RateBlog.Data> response) {
                    BlogDetails.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BlogDetails.this,"Rated successfully!!!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {

                }
            }
    );


}
private void displayAllComments() {

    MyAppolloClient.getMyAppolloClient().query(graphqlandroid.BlogDetails.builder().id(intent.getStringExtra("blogId")).
            build()).enqueue(new ApolloCall.Callback<graphqlandroid.BlogDetails.Data>() {
        @Override
        public void onResponse(@Nonnull Response<graphqlandroid.BlogDetails.Data> response) {
            list.clear();

            for (int loopVar=0;loopVar<response.data().Blog().comments().size();loopVar++)
            {
                comments = new Comments(R.drawable.ic_dropbox, response.data().Blog().comments().get(loopVar).comment(), "Gayathry");
                list.add(comments);

            }
            controller=AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),R.anim.layout_file);
            BlogDetails.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myAdapter=new CommentsAdapter(BlogDetails.this,list);
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setLayoutAnimation(controller);
                    recyclerView.scheduleLayoutAnimation();


                }
            });




        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {

        }
    });
}
private void prepareCommentsdata() {
        MyAppolloClient.getMyAppolloClient().query(graphqlandroid.BlogDetails.builder().id(intent.getStringExtra("blogId")).
                build()).enqueue(new ApolloCall.Callback<graphqlandroid.BlogDetails.Data>() {
            @Override
            public void onResponse(@Nonnull Response<graphqlandroid.BlogDetails.Data> response) {


                if(response.data().Blog().comments().size()==0)
                {
                    try
                    {

                    }
                    catch (Exception e)
                    {

                    }
                }
                else{
                for (int loopVar=0;loopVar<2;loopVar++)
                {
                    comments = new Comments(R.drawable.ic_dropbox, response.data().Blog().comments().get(loopVar).comment(), "Gayathry");
                    list.add(comments);

                }}

                myAdapter=new CommentsAdapter(BlogDetails.this,list);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);

                        //response.data().Blog().comments().get(0).comment();
                final String descrption=response.data().Blog().description();
                final int commentsCount=response.data().Blog().comments().size();
                 likesCount=response.data().Blog().likes_count();
                 dislikesCount=response.data().Blog().unlikes_count();

                BlogDetails.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(likesCount==1){
                            blogDescriptionImage.setVisibility(View.VISIBLE);
                        blogDescriptionImage.setImageResource(R.drawable.blogreadimage);}
                        blogDescription.setText(descrption);
                        likes.setText(""+likesCount);
                        dislikes.setText(""+dislikesCount);
                        allcomments.setText(""+commentsCount+" Comments");
                        if(likesCount>1){
                            likedUsers.setText("You,"+ (likesCount-1)+"others");
                        }else if(likesCount==1){
                            likedUsers.setText("You");
                        }else{
                            likedUsers.setVisibility(View.GONE);
                            noOfLikes.setVisibility(View.GONE);
                        }

                    }
                });

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    /*comments = new Comments(R.drawable.ic_dropbox, "hai hello hai", "Gayathry");
    list.add(comments);
    comments = new Comments(R.drawable.ic_foursquare, "welcome hai hello hai", "Awnish");
    list.add(comments);
    comments=new Comments(R.drawable.ic_behance,"good work","Sangaraj");
    list.add(comments);
    comments=new Comments(R.drawable.ic_man,"can you please modify the version number","Monika");
    list.add(comments);*/

}
/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                showButtonSheetDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_action_items_blog_details, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    public void postComments(View view) {
        comments=new Comments(R.drawable.ic_man,et_comment.getText().toString(),"Renuka");
        list.add(0,comments);
        list.remove(2);
       // list.add(comments);

        myAdapter.notifyDataSetChanged();
        //postCommentsToDB();
        et_comment.setText("");
        view.setVisibility(View.GONE);

    }
public void showButtonSheetDialog()
{
    View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet,null);
    deleteBlog = view.findViewById(R.id.delete_Blog);
    reportBlog = view.findViewById(R.id.report_Blog);
    blockBlog = view.findViewById(R.id.block_Blog);
    deleteBlog.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteBlog(v);
        }
    });

   reportBlog.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Toast.makeText(BlogDetails.this,"Yet to implement!!!",Toast.LENGTH_SHORT).show();
       }
   });

   blockBlog.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           blockBlog(v);
       }
   });



    BottomSheetDialog dialog =new BottomSheetDialog(this);
    dialog.setContentView(view);
    dialog.show();
}
    public void blockBlog(View v)
    {

        MyAppolloClient.getMyAppolloClient().mutate(
                BlockBlog.builder().id(intent.getStringExtra("blogId")).build()).enqueue(
                new ApolloCall.Callback<BlockBlog.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<BlockBlog.Data> response) {
                        BlogDetails.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(BlogDetails.this,"Blocked successfully!!!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                }
        );

    }
    public void deleteBlog(View v)
    {
        MyAppolloClient.getMyAppolloClient().mutate(
                DeleteBlog.builder().id(intent.getStringExtra("blogId")).build()).enqueue(
                new ApolloCall.Callback<DeleteBlog.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<DeleteBlog.Data> response) {
                        BlogDetails.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BlogDetails.this,"Deleted successfully!!!",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                }
        );

    }
  /*  public void postCommentsToDB()
    {
        MyAppolloClient.getMyAppolloClient().mutate(PostComment.builder().userId("5bf27fe2bbfa3185af136dcc").blogId(intent.getStringExtra("blogId")).comments(et_comment.getText().toString()).build()).
                enqueue(new ApolloCall.Callback<PostComment.Data>() {
                    @Override
                    public void onResponse(@Nonnull final Response<PostComment.Data> response) {
                        BlogDetails.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BlogDetails.this,"added comments to DB"+response.data().addComment().comment(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }
*/
}
