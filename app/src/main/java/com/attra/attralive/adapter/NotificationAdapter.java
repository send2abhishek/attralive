package com.attra.attralive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.Notification;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.productViewHolder>
{
    private Context mcntx;
    private List<Notification> notiList;
    ImageView commentnotifyDp;



    public NotificationAdapter(Context mcntx, List<Notification> notifyList) {
        this.mcntx = mcntx;
        this.notiList = notifyList;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mcntx);
        View view=inflater.inflate(R.layout.notification_item,null);
        productViewHolder productViewHolder=new productViewHolder(view);
        return productViewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder productViewHolder, int i) {
        Notification notification = notiList.get(i);
        String str = "<b>"+notification.getUserName()+"</b> has"+notification.getAction()+"on your post";
        productViewHolder.textViewTitle.setText(Html.fromHtml(str));
        productViewHolder.textViewShortDesc.setText("2 days ago");
        Picasso.with(mcntx).load("https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546592733408-29a82067b71bd9e3df95e1c0ba5c4daf--fantasy-art-avatar-jake-sully.jpg").fit().into(commentnotifyDp);

    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    class productViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewTitle, textViewShortDesc;


        public productViewHolder(@NonNull View itemView) {
            super(itemView);

            commentnotifyDp = itemView.findViewById(R.id.commentNotifyDp);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);

        }
    }
}
