package com.attra.attralive.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.model.AllAnswers;


import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.LikeDislikeAnswer;

public class AllAnswersAdapter extends RecyclerView.Adapter<AllAnswersAdapter.ViewHolderAnswer> {
    Context context;
    ArrayList<AllAnswers> list;
    ArrayList<String> idlist;
    AllAnswers allAnswers;
    int lastPostion=-1;
    int likesCount=0,dislikeCount=0;
    public AllAnswersAdapter(Context context, ArrayList<AllAnswers> list,ArrayList<String> ansidlist) {
        this.context = context;
        this.list = list;
        idlist=ansidlist;
    }

    @NonNull
    @Override
    public AllAnswersAdapter.ViewHolderAnswer onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.answer_list_row,viewGroup,false);
        ViewHolderAnswer viewHolderAnswer=new ViewHolderAnswer(view);
        return viewHolderAnswer;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolderAnswer holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(@NonNull final AllAnswersAdapter.ViewHolderAnswer viewHolderAnswer, final int i) {
    allAnswers=list.get(i);
    viewHolderAnswer.votes.setText(""+allAnswers.getVotescount());
    viewHolderAnswer.watch.setText(""+allAnswers.getWatchcount());
    viewHolderAnswer.answer.setText(""+allAnswers.getAnswer());
    viewHolderAnswer.likes.setText(""+allAnswers.getLikescount());
    viewHolderAnswer.dislikes.setText(""+allAnswers.getDislikecount());
    viewHolderAnswer.ratingBar.setStepSize(Float.parseFloat(""+allAnswers.getRating()));
    viewHolderAnswer.createdby.setText(allAnswers.getUsercreatedans()+" "+allAnswers.getUserlocation());
    viewHolderAnswer.createdate.setText(""+allAnswers.getAnscreatedate());
    viewHolderAnswer.ansid.setText(allAnswers.getAnsId());
   // idlist.contains("5bdc1f635d3211192f245db9");
    viewHolderAnswer.likeimage.setTag(R.drawable.ic_thumb_up_grey_24dp);
   // viewHolderAnswer.dislikeimage.setTag(R.drawable.ic_thumb_down_grey_24dp);
        viewHolderAnswer.dislikeimage.setTag(R.drawable.ic_thumb_down_grey_24dp);
       // viewHolderAnswer.likeimage.setTag(R.drawable.ic_thumb_down_grey_24dp);
        Animation animation=AnimationUtils.loadAnimation(context,(i>lastPostion)?R.anim.rv_up_from_bottom:R.anim.rv_down_from_up);
        viewHolderAnswer.itemView.startAnimation(animation);
        lastPostion=i;
        viewHolderAnswer.dislikeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)(viewHolderAnswer.dislikeimage.getTag())==R.drawable.ic_thumb_down_grey_24dp) {
                    if((int)(viewHolderAnswer.likeimage.getTag())==R.drawable.ic_thumb_up_grey_24dp) {
                        dislikeCount=getLikeDislikeService("dislike",viewHolderAnswer.ansid.getText().toString());
                        System.out.println("likescountgayathry"+dislikeCount);
                        viewHolderAnswer.dislikeimage.setImageResource(R.drawable.ic_thumb_down_color_24dp);
                        viewHolderAnswer.dislikeimage.setTag(R.drawable.ic_thumb_down_color_24dp);

                        viewHolderAnswer.dislikes.setText("" + (dislikeCount + 1));
                        Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                    }
                    else {
                       dislikeCount= getLikeDislikeService("dislike",viewHolderAnswer.ansid.getText().toString());
                        System.out.println("likescountgayathry"+dislikeCount);
                        viewHolderAnswer.dislikeimage.setImageResource(R.drawable.ic_thumb_down_color_24dp);
                        viewHolderAnswer.dislikeimage.setTag(R.drawable.ic_thumb_down_color_24dp);
                        String dlikeno=viewHolderAnswer.dislikes.getText().toString();
                        int dlikec=Integer.parseInt(dlikeno);

                        viewHolderAnswer.dislikes.setText("" + ( dlikec+ 1));
                        Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                        viewHolderAnswer.likeimage.setImageResource(R.drawable.ic_thumb_up_grey_24dp);
                        viewHolderAnswer.likeimage.setTag(R.drawable.ic_thumb_up_grey_24dp);
                        String likeno=viewHolderAnswer.likes.getText().toString();
                        int likec=Integer.parseInt(likeno);
                        if(likec!=0)
                        viewHolderAnswer.likes.setText((likec-1)+"");
                    }
                }
                else {
                    Toast.makeText(context,"not",Toast.LENGTH_LONG).show();
                }
            }

        });
    viewHolderAnswer.likeimage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if((int)(viewHolderAnswer.likeimage.getTag())==R.drawable.ic_thumb_up_grey_24dp) {
                if((int)(viewHolderAnswer.dislikeimage.getTag())==R.drawable.ic_thumb_down_grey_24dp) {
                    likesCount=getLikeDislikeService("like",viewHolderAnswer.ansid.getText().toString());
                    System.out.println("likescountgayathry"+likesCount);
                    viewHolderAnswer.likeimage.setImageResource(R.drawable.ic_thumb_up_color_24dp);
                    viewHolderAnswer.likeimage.setTag(R.drawable.ic_thumb_up_color_24dp);
                    String likeno=viewHolderAnswer.likes.getText().toString();
                    int likec=Integer.parseInt(likeno);
                    viewHolderAnswer.likes.setText("" + (likec + 1));
                    Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                }
                else {
                    likesCount=getLikeDislikeService("like",viewHolderAnswer.ansid.getText().toString());
                    System.out.println("likescountgayathry"+likesCount);
                    viewHolderAnswer.likeimage.setImageResource(R.drawable.ic_thumb_up_color_24dp);
                    viewHolderAnswer.likeimage.setTag(R.drawable.ic_thumb_up_color_24dp);
                    String likeno=viewHolderAnswer.likes.getText().toString();
                    int likec=Integer.parseInt(likeno);
                    viewHolderAnswer.likes.setText("" + (likec + 1));
                    Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                    viewHolderAnswer.dislikeimage.setImageResource(R.drawable.ic_thumb_down_grey_24dp);
                    viewHolderAnswer.dislikeimage.setTag(R.drawable.ic_thumb_down_grey_24dp);
                    String dlikeno=viewHolderAnswer.dislikes.getText().toString();
                    int dlikec=Integer.parseInt(dlikeno);
                    if(dlikec!=0)
                    viewHolderAnswer.dislikes.setText((dlikec-1)+"");

                }
                            }
            else {
                Toast.makeText(context,"not",Toast.LENGTH_LONG).show();
            }
            }
    });
    }
    private int getLikeDislikeService(final String like,String ansid)
    {
        MyAppolloClient.getMyAppolloClient().mutate(LikeDislikeAnswer.builder().answerId(ansid).userId("x").like_dislike(like).build()).enqueue(new ApolloCall.Callback<LikeDislikeAnswer.Data>() {
            @Override

            public void onResponse(@Nonnull Response<LikeDislikeAnswer.Data> response) {
                likesCount=response.data().like_dislike_answer().likes_count();
                dislikeCount=response.data().like_dislike_answer().unlikes_count();
               if(like.equals("like"))
                    Log.d("abcxyz",""+likesCount);
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }


            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }

        });
        if(like.equals("like"))
        {
            Log.d("abcxyzaa",""+likesCount);
            return likesCount;
        }
        else

            return dislikeCount;

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderAnswer extends RecyclerView.ViewHolder {
        TextView votes,watch,answer,likes,dislikes,createdby,createdate,ansshowcomments,ansid;
        ImageView showallcomments,likeimage,dislikeimage;
        RatingBar ratingBar;
        public ViewHolderAnswer(@NonNull View itemView) {
            super(itemView);
            votes=itemView.findViewById(R.id.tv_votecount);
            watch=itemView.findViewById(R.id.tv_watchcount);
            answer=itemView.findViewById(R.id.answer);
            likes=itemView.findViewById(R.id.tv_answerlikescount);
            dislikes=itemView.findViewById(R.id.tv_answerdislikecount);
            ratingBar=itemView.findViewById(R.id.ratingbar);
            createdby=itemView.findViewById(R.id.tv_createdby);
            createdate=itemView.findViewById(R.id.tv_anscreatetime);
            ansshowcomments=itemView.findViewById(R.id.tv_showcomments);
            //showallcomments=itemView.findViewById(R.id.im_showcomments);
            likeimage=itemView.findViewById(R.id.im_answerlikes);
            dislikeimage=itemView.findViewById(R.id.im_answerdislike);
            ansid=itemView.findViewById(R.id.ansid);

        }
    }
}
