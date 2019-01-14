package com.attra.attralive.activity;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.attra.attralive.R;
import com.attra.attralive.adapter.NotificationAdapter;
import com.attra.attralive.model.Notification;
import com.attra.attralive.util.SimpleDividerItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter productAdapter;
    ArrayList<Notification> NotificationList=new ArrayList<Notification>();
    int notify=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.notifications);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return true;

        //return super.onOptionsItemSelected(item);
    }
}
