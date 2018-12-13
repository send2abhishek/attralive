package com.attra.attralive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.activity.CreateBlogActivity;
import com.attra.attralive.activity.MainBlogCategoryActivity;
import com.attra.attralive.model.AnimationInterface;
import com.attra.attralive.model.BlogAllCategories;


import java.util.ArrayList;

public class BlogMainCategoryAdapter extends RecyclerView.Adapter<BlogMainCategoryAdapter.MyViewHolder>  {
    Context tcontext;
    /*AnimationInterface animationInterface=new MainBlogCategoryActivity();*/
    ArrayList<BlogAllCategories> tlist;

    BlogAllCategories blogAllCategories;
    public BlogMainCategoryAdapter(Context context,ArrayList<BlogAllCategories> alllist) {
        tcontext=context;
        tlist=alllist;

    }

    @NonNull
    @Override
    public BlogMainCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_category_list,viewGroup,false);
        BlogMainCategoryAdapter.MyViewHolder myViewHolder=new BlogMainCategoryAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlogMainCategoryAdapter.MyViewHolder myViewHolder, final int i) {
    blogAllCategories=tlist.get(i);
    myViewHolder.allcategorytitle.setText(blogAllCategories.getCategories());
    myViewHolder.allimages.setImageResource(blogAllCategories.getImageId());
    myViewHolder.arrowimages.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           // animationInterface=new MainBlogCategoryActivity();
            /*Log.d("higayathry" ,"hello"+animationInterface);*/
            Intent in=new Intent(tcontext,CreateBlogActivity.class);
            //Intent intent =new Intent(this,CreateBlogActivity.class);
       /* intent.putExtra("title",titleBlog);
        intent.putExtra("blogId",blogId);*/
            Log.d("karthika","kjkj");
            tcontext.startActivity(in);
            ((Activity) tcontext).overridePendingTransition(R.anim.slide_from_right,R.anim.slide_left_out);
            //animationInterface.callupdateActivity();

        }
    });

    }

    @Override
    public int getItemCount() {
        return tlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView allcategorytitle;
        ImageView allimages,arrowimages;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            allcategorytitle=itemView.findViewById(R.id.tv_allcategorytitle);
            allimages=itemView.findViewById(R.id.im_allimages);
            arrowimages=itemView.findViewById(R.id.arrowimage);

        }
    }
}
