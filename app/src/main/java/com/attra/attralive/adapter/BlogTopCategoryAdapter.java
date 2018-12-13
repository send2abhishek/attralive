package com.attra.attralive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.attra.attralive.R;
import com.attra.attralive.model.BlogTopCategories;

import java.util.ArrayList;


public class BlogTopCategoryAdapter extends RecyclerView.Adapter<BlogTopCategoryAdapter.MyViewHolder> {
    Context tcontext;
    ArrayList<BlogTopCategories> tlist;
    BlogTopCategories blogTopCategories;
    public BlogTopCategoryAdapter(Context context, ArrayList<BlogTopCategories> t_List) {
        tcontext=context;
        tlist=t_List;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_category_list,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
    blogTopCategories=tlist.get(i);
    myViewHolder.topImages.setImageResource(blogTopCategories.getImageId());
    }

    @Override
    public int getItemCount() {
        return tlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView topImages;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            topImages=itemView.findViewById(R.id.im_topimages);

        }
    }
}
