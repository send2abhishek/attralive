package com.attra.attralive.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.adapter.NotificationAdapter;
import com.attra.attralive.model.Notification;
import com.attra.attralive.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class notificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter productAdapter;
    List<Notification> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        productList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        productList.add(
                new Notification(1,
                        "Sagar commented on your post",
                        "2 days ago"));

        productList.add(
                new Notification(
                        1,
                        "Yadav liked the post your are tagged in",
                        "14 mins ago"));

        productList.add(
                new Notification(
                        1,
                        "Sachin commented on your post1111111112",
                        "now"));
        productList.add(
                new Notification(
                        1,
                        "Sanagaraj replied on this post comment",
                        "minute ago "));


        NotificationAdapter adapter = new NotificationAdapter(this, productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
