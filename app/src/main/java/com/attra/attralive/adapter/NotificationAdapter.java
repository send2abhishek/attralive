package com.attra.attralive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.Notification;

import java.util.List;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.productViewHolder>
{
    private Context mcntx;
    private List<Notification> productList;



    public NotificationAdapter(Context mcntx, List<Notification> productList) {
        this.mcntx = mcntx;
        this.productList = productList;
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
        Notification notification = productList.get(i);

        //binding the data with the viewholder views
        productViewHolder.textViewTitle.setText(notification.getTitle());
        productViewHolder.textViewShortDesc.setText(notification.getShortdesc());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class productViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);

        }
    }
}
