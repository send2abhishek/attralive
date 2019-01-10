package com.attra.attralive.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.adapter.NotificationAdapter;
import com.attra.attralive.model.Notification;
import com.attra.attralive.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import graphqlandroid.GetNotificationList;

public class notificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter productAdapter;
    ArrayList<Notification> NotificationList=new ArrayList<Notification>();
    int notify=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getSerializableExtra("NOTIFICATION_LIST")!=null) {
            NotificationList = (ArrayList<Notification>)getIntent().getSerializableExtra("NOTIFICATION_LIST");
        }
        setContentView(R.layout.activity_notification);
        NotificationList=new ArrayList<Notification>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        NotificationAdapter adapter = new NotificationAdapter(getApplicationContext(), NotificationList);
        recyclerView.setAdapter(adapter);
/*
        adapter.setItemOnClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Log.i(" clcik of notti","Item clicked");

            }
        });
*/

    }
}
