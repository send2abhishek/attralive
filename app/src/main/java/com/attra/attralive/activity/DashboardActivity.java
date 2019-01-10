package com.attra.attralive.activity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.fragment.Profile;
import com.attra.attralive.R;
import com.attra.attralive.fragment.AboutUsFragment;
import com.attra.attralive.fragment.Gallery;
import com.attra.attralive.fragment.HolidayCalender;
import com.attra.attralive.fragment.HomeFragment;
import com.attra.attralive.fragment.LearningD;
import com.attra.attralive.model.NewsFeed;
import com.attra.attralive.util.GetNewRefreshToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import javax.annotation.Nonnull;

import graphqlandroid.GetNotificationList;
import graphqlandroid.GetProfileDetails;
import graphqlandroid.GetRefreshToken;

//import static com.attra.attralive.activity.OtpValidationActivity.PREFS_AUTH;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    ArrayList<NewsFeed> notificationArrayList;
    LinearLayoutManager linearLayoutManager;
    ImageView profileImage,profileNav;
    TextView userName,userEmail;
    String userId1,username,location;
    String myToken,refreshToken;
    int notificationSize=0;
    private static final String TAG = "DashboardActivity";
Intent intent;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        intent=getIntent();
        location=intent.getStringExtra("location");
        subscribeToTopic();


        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("goBack").commit();
        notificationArrayList=new ArrayList<NewsFeed>();
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.tv_username);
        userEmail = headerView.findViewById(R.id.tv_email);
        profileImage = headerView.findViewById(R.id.civ_profilePic);

        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            userId1 = sharedPreferences.getString("userId", "");
            username = sharedPreferences.getString("userName","");
            refreshToken=sharedPreferences.getString("refreshToken","");
      //      Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_LONG).show();
            Log.i("token in dashboard",myToken);
            Log.i("user id in dashboard",userId1);

        }
        getProfileDetail(myToken);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }


        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.

    }

/*    private void getRegistrationToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(DashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    private void getProfileDetail(String accesstoken){

        MyAppolloClient.getMyAppolloClient(accesstoken).query(
                GetProfileDetails.builder().userId(userId1)
                        .build()).enqueue(
                new ApolloCall.Callback<GetProfileDetails.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetProfileDetails.Data> response) {
                        if (response.data().getProfileDetails_Q() != null) {
                            Log.i("res", String.valueOf(response));
                            String message = response.data().getProfileDetails_Q().message();
                            String status = response.data().getProfileDetails_Q().status();
                            Log.i("message in dashboard", message);
                            Log.i("mstatus in dashboard", status);
                            if (response.data().getProfileDetails_Q().name() != null) {
                           /* String message = response.data().getProfileDetails_Q().message();
                            String status = response.data().getProfileDetails_Q().status();*/
                                if (status.equals("Success")) {
                                    String username = response.data().getProfileDetails_Q().name();
                                    String emaiId = response.data().getProfileDetails_Q().email();
                                    String imgPath = response.data().getProfileDetails_Q().profileImagePath();
                                    Log.i("profile image path", imgPath);
                                    DashboardActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            userName.setText(username);
                                            userEmail.setText(emaiId);
                                            Picasso.with(getApplicationContext()).load(imgPath).fit().into(profileImage);

                                        }
                                    });
                                } else if (status.equals("Failure")) {
                                    MyAppolloClient.getMyAppolloClient(GetNewRefreshToken.Authorization).query(GetRefreshToken.builder().
                                            refreshToken(refreshToken).grant_type("refresh_token").build()).enqueue(new ApolloCall.Callback<GetRefreshToken.Data>() {
                                        @Override
                                        public void onResponse(@Nonnull Response<GetRefreshToken.Data> response) {
                                            String status = response.data().getRefreshToken_Q().status();
                                            if (status.equals("Success")) {
                                                String accessToken = response.data().getRefreshToken_Q().accessToken();
                                                // String tokenExpiry = response.data().getRefreshToken_Q().accessTokenExpiresAt();
                                                String newRefreshToken = response.data().getRefreshToken_Q().RefreshToken();
                                                //String refreshTokenExpiry = response.data().getRefreshToken_Q().accessTokenExpiresAt();
                                                // String user = response.data().getRefreshToken_Q().user();
                                                // String userName = response.data().getRefreshToken_Q().name();
                                                Log.d("access Token", accessToken);
                                                String authToken = "Bearer" + " " + accessToken;
                                                Log.d("brarer token", authToken);
                                                SharedPreferences preferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, 0);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("authToken", authToken);
                                                editor.putString("refreshToken", newRefreshToken);
                                                editor.commit();
                                                sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                                if (sharedPreferences.contains("authToken")) {
                                                    String myToken = sharedPreferences.getString("authToken", "");
                                                    getProfileDetail(myToken);
                                                    //Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                                                    // }
                                                }
                                            }
                                        }


                                        @Override
                                        public void onFailure(@Nonnull ApolloException e) {

                                        }
                                    });
                                }

                            }

                        }
                    }
                        @Override
                        public void onFailure (@Nonnull ApolloException e){
                        }

                }
        );
    }



    private void subscribeToTopic(){

        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                      //  Toast.makeText(DashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_landD) {
            fragment = new LearningD();
            loadFragment(fragment);
            // Handle the camera action
        } else if (id == R.id.nav_holidayCalender) {
            fragment = new HolidayCalender();
            loadFragment(fragment);

        } else if (id == R.id.nav_facilities) {
           /* fragment = new Gallery();
            loadFragment(fragment);*/
            Intent intent = new Intent(getApplicationContext(),UserDetailsActivity.class);
            startActivity(intent);

        }   else if (id == R.id.nav_about) {
            fragment = new AboutUsFragment();
            loadFragment(fragment);

        }else if (id == R.id.nav_logout) {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_event:
                    Intent i=new Intent(getApplicationContext(),EventRegistrationDetailsActivity.class);
                    i.putExtra("location",location);
                    startActivity(i);
                    return true;
                case R.id.navigation_gallery:
                    fragment = new Gallery();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_profile:
                    fragment = new Profile();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override public boolean onCreateOptionsMenu(final Menu menu) {
        ImageView mImageLayoutView=null;
        TextView myTextView,myTextLayoutView;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard_toolbar, menu);
        final View actionView = menu.findItem(R.id.menu_item).getActionView();
        if(actionView!=null)
        {
            mImageLayoutView = actionView.findViewById(R.id.imageView);
            myTextLayoutView = actionView.findViewById(R.id.textView);
            ((View) actionView.findViewById(R.id.textView)).setVisibility(View.GONE);
            MyAppolloClient.getMyAppolloClient(myToken).query(
                    GetNotificationList.builder().userId(userId1)
                            .build()).enqueue(
                    new ApolloCall.Callback<GetNotificationList.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<GetNotificationList.Data> response) {
                           DashboardActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(response.data()!=null &&response.data().getUserNotification_Q()!=null && response.data().getUserNotification_Q().notifications()!=null)
                                    {
                                        Log.i("Run method notification", "Run method notification");
                                        notificationSize = response.data().getUserNotification_Q().notifications().size();
                                        Log.i("notification-size ", notificationSize+"");
                                        if(notificationSize>0) {
                                            ((View) actionView.findViewById(R.id.textView)).setVisibility(View.VISIBLE);
                                            myTextLayoutView.setText(notificationSize + "");
                                        }
                                    }
                                    else
                                    {
                                        Log.i("else responsedata", "inside else of  respnse");
                                        myTextLayoutView.setText("0");
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
        if(mImageLayoutView!=null) {
            mImageLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, notificationActivity.class);

                    intent.putExtra("sagar", "Sagar commented on your postmmmmmmmmmmmmmmmmmmmmmmmmmm");
                    intent.putExtra("sachin", "Sachin commented on your post");
                    intent.putExtra("sangaraj", "Sangaraj commented on your postllllllllllllllllllllllllllll");
                    startActivity(intent);
                    ((View) actionView.findViewById(R.id.textView)).setVisibility(View.GONE);

                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

}
