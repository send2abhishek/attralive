package com.attra.attralive.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.activity.PostDetailsActivity;
import com.attra.attralive.model.Notification;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.productViewHolder>
{
    private Context mcntx;
    private List<Notification> notiList;




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
        String str = "<b>"+notification.getUserName()+"</b> has "+notification.getAction()+" on your post";
        productViewHolder.textViewTitle.setText(Html.fromHtml(str));
        productViewHolder.textViewShortDesc.setText(notification.getTime());
        productViewHolder.textpostID.setText(notification.getPostId());
        productViewHolder.textimagepath.setText(notification.getUserImage());
        Picasso.with(mcntx).load(notification.getUserImage()).fit().into(productViewHolder.commentnotifyDp);
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    class productViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewTitle, textViewShortDesc,textpostID,textimagepath;
        ImageView commentnotifyDp;

//AppCompatActivity activity;
        public productViewHolder(@NonNull View itemView) {
            super(itemView);

            commentnotifyDp = itemView.findViewById(R.id.commentNotifyDp);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textpostID=itemView.findViewById(R.id.tv_notificationid);
            textimagepath=itemView.findViewById(R.id.tv_imagepath);
            //activity= (AppCompatActivity) itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mcntx,PostDetailsActivity.class);
                    intent.putExtra("postId",textpostID.getText().toString());
                    intent.putExtra("imagepath",textimagepath.getText().toString());
                    mcntx.startActivity(intent);
                }
            });

        }
    }
}
