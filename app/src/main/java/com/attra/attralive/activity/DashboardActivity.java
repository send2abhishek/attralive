package com.attra.attralive.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.fragment.AboutUsFragment;
import com.attra.attralive.fragment.Gallery;
import com.attra.attralive.fragment.HolidayCalender;
import com.attra.attralive.fragment.HomeFragment;
import com.attra.attralive.fragment.LearningD;
import com.attra.attralive.fragment.Profile;
import com.attra.attralive.model.NewsFeed;
import com.attra.attralive.util.Config;
import com.attra.attralive.util.GetNewRefreshToken;
import com.attra.attralive.util.NotificationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.GetNotificationList;
import graphqlandroid.GetProfileDetails;
import graphqlandroid.Logout;

import static com.attra.attralive.util.NetworkUtil.isNetworkAvailable;
//import static com.attra.attralive.activity.OtpValidationActivity.PREFS_AUTH;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    ArrayList<NewsFeed> notificationArrayList;
    LinearLayoutManager linearLayoutManager;
    ImageView profileImage;

    TextView userName, userEmail;
    String userId1, username, location;
    String myToken, refreshToken;
    int notificationSize = 0;
    ArrayList<com.attra.attralive.model.Notification> notificationList;

    private static final String TAG = "DashboardActivity";
    Intent intent;
    public static String Authorization = "Basic YXBwbGljYXRpb246c2VjcmV0";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        intent = getIntent();
        location = intent.getStringExtra("location");
        //subscribeToTopic();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        //txtMessage.setText(message);

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);

            }
        }


        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("goBack").commit();
        notificationArrayList = new ArrayList<NewsFeed>();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
        Menu menuNav=navigationView.getMenu();
        MenuItem nav_facilities = menuNav.findItem(R.id.nav_facilities);
        MenuItem nav_landD = menuNav.findItem(R.id.nav_landD);
        nav_facilities.setEnabled(false);
        nav_landD.setEnabled(false);
        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.tv_menuUserName);
        userEmail = headerView.findViewById(R.id.tv_email);
        profileImage = headerView.findViewById(R.id.civ_profilePic);

        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            userId1 = sharedPreferences.getString("userId", "");

            username = sharedPreferences.getString("userName","");
            refreshToken=sharedPreferences.getString("refreshToken","");
            Log.i("token in dashboard",myToken);
            Log.i("user id in dashboard",userId1);
            Log.i("user name in dashboard",username);

            userName.setText(username);
        }
        getProfileDetail(myToken);
    }


    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
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

    private void getProfileDetail(String accesstoken) {

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
                                    String loc = response.data().getProfileDetails_Q().location();
                                    Log.i("this user name", username);

                                    Log.i("profile image path", imgPath);
                                    Log.i("username in dashbard", username);
                                    sharedPreferences = getApplicationContext().getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("location", loc);
                                    editor.putString("profileImagePath", imgPath);
                                    editor.putString("userName", username);
                                    editor.commit();
                                    DashboardActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // userName.setText(username);
                                            userEmail.setText(emaiId);
                                            Picasso.with(getApplicationContext()).load(imgPath).fit().into(profileImage);

                                        }
                                    });
                                } else if (status.equals("Failure")) {
                                    if (message.equals("Invalid token: access token is invalid")) {

                                        GetNewRefreshToken.getRefreshtoken(refreshToken, DashboardActivity.this);
                                        DashboardActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                                if (sharedPreferences.contains("authToken")) {
                                                    String myToken = sharedPreferences.getString("authToken", "");
                                                    getProfileDetail(myToken);
                                                    //Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                    }
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                }
        );
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
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "mobile.appsupport@attra.com.au" });
            Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));
            return true;

        } else if (id == R.id.nav_giveRating) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }

        } else if (id == R.id.nav_about) {
            fragment = new AboutUsFragment();
            loadFragment(fragment);

        } else if (id == R.id.nav_logout) {
            logoutUser();

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
                    Intent i = new Intent(getApplicationContext(), EventRegistrationDetailsActivity.class);
                    i.putExtra("location", location);
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


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        ImageView mImageLayoutView = null;
        TextView myTextView, myTextLayoutView;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard_toolbar, menu);
        final View actionView = menu.findItem(R.id.menu_item).getActionView();
        if (actionView != null) {
            mImageLayoutView = actionView.findViewById(R.id.imageView);
            myTextLayoutView = actionView.findViewById(R.id.textView);
            ((View) actionView.findViewById(R.id.textView)).setVisibility(View.GONE);


            notificationList = new ArrayList<com.attra.attralive.model.Notification>();
            Log.i("Network availabiltiy", "" + isNetworkAvailable(getApplicationContext()));
            if (isNetworkAvailable(getApplicationContext()))

                MyAppolloClient.getMyAppolloClient(myToken).query(
                        GetNotificationList.builder().userId(userId1)
                                .build()).enqueue(
                        new ApolloCall.Callback<GetNotificationList.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<GetNotificationList.Data> response) {
                                DashboardActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (response.data() != null && response.data().getUserNotification_Q() != null && response.data().getUserNotification_Q().notifications() != null) {
                                            Log.i("Run method notification", "Run method notification");
                                            notificationSize = response.data().getUserNotification_Q().notifications().size();
                                            Log.i("notification-size ", notificationSize + "");
                                            if (notificationSize > 0) {
                                                if (((View) actionView.findViewById(R.id.textView)) != null)
                                                    ((View) actionView.findViewById(R.id.textView)).setVisibility(View.VISIBLE);
                                                myTextLayoutView.setText(Integer.toString(notificationSize));

                                                for (GetNotificationList.Notification noti : response.data().getUserNotification_Q().notifications()) {
                                                    com.attra.attralive.model.Notification noitification = new com.attra.attralive.model.Notification(noti.postType(), noti.postId(), noti.ownerId(), noti.action(), noti.userId(), noti.userName(), noti
                                                            .time(), noti.postMessage(), noti.userImage(), noti.readStatus());
                                                    notificationList.add(
                                                            noitification);
                                                    // new Notification(noti.postType(), "", "", "", noti.action(), "", noti.userName(), "", "Y"));
                                                    Log.i("notifications", noti.action());
                                                }
                                            }
                                        } else {
                                            Log.i("else responsedata", "inside else of  respnse");
                                            myTextLayoutView.setText("0");
                                        }

                                    }
                                });

                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {
                                Log.i("Failure", "OnFailure method   " + e.getMessage());
                            }
                        }
                );
        } else {
            Toast.makeText(this, "Please check network connectivity", Toast.LENGTH_SHORT).show();
        }
        if (mImageLayoutView != null) {
            mImageLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
                    if (notificationSize > 0 && notificationList != null)
                        intent.putExtra("NOTIFICATION_LIST", notificationList);

                    startActivity(intent);
                    ((View) actionView.findViewById(R.id.textView)).setVisibility(View.GONE);

                }
            });
        }
        return super.onCreateOptionsMenu(menu);

    }

    private void logoutUser() {
        MyAppolloClient.getMyAppolloClient(Authorization).mutate(Logout.builder().userId(userId1).build()).enqueue(new ApolloCall.Callback<Logout.Data>() {
            @Override
            public void onResponse(@Nonnull Response<Logout.Data> response) {
                String status = response.data().userLogout_M().status();
                String message = response.data().userLogout_M().message();
                Log.i("logout status ", status);
                Log.i(" logout message", message);
                DashboardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (status.equals("Success")) {
                            Toast.makeText(DashboardActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                            startActivity(i);
                        } else if ((status.equals("Failure")) && (message.equals("Invalid Username or Password"))) {
                            Toast.makeText(DashboardActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

}
