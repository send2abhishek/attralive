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
    List<Notification> NotificationList;
    int notify=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotificationList=new ArrayList<Notification>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        Log.i("List-size-before", NotificationList.size()+"");
        MyAppolloClient.getMyAppolloClient("Bearer dff60b491273948153a7e0940950c451303a032e").query(
                GetNotificationList.builder().userId("5c2e46f9fb15963434c755f4")
                        .build()).enqueue(
                new ApolloCall.Callback<GetNotificationList.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetNotificationList.Data> response) {
                        notificationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(response.data()!=null &&response.data().getUserNotification_Q()!=null && response.data().getUserNotification_Q().notifications()!=null)
                                {
                                    Log.i("Run method notification", "Run method notification");
                                    notify = response.data().getUserNotification_Q().notifications().size();
                                    Log.i("notification-size ", notify+"");
                                   if(notify>0)
                                   {
                                       Log.i("status...", response.data().getUserNotification_Q().status());
                                       Log.i("message", response.data().getUserNotification_Q().message());
                                       for(GetNotificationList.Notification noti :response.data().getUserNotification_Q().notifications()) {
                                           NotificationList.add(new Notification(noti.postType(),"","","",noti.action(),"",noti.userName(),"",noti.postMessage(),"",""));

                                           Log.i("notifications", noti.action());

                                       }
                                       NotificationAdapter adapter = new NotificationAdapter(getApplicationContext(), NotificationList);
                                       recyclerView.setAdapter(adapter);
                                   }
                                }
                                else
                                {
                                    Log.i("else responsedata", "inside else of  respnse");

                                }

                            }
                        });

                    }
                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.i("Failure", "OnFailure method   "+e.getMessage());
                    }
                }
        );


    }
}
