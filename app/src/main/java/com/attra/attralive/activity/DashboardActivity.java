package com.attra.attralive.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.fragment.Profile;
import com.attra.attralive.R;
import com.attra.attralive.fragment.AboutUsFragment;
import com.attra.attralive.fragment.Gallery;
import com.attra.attralive.fragment.HolidayCalender;
import com.attra.attralive.fragment.HomeFragment;
import com.attra.attralive.fragment.LearningD;
import com.attra.attralive.model.NewsFeed;
import com.attra.attralive.util.Config;
import com.attra.attralive.util.NotificationUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import javax.annotation.Nonnull;

import graphqlandroid.ForgotPassword;
import graphqlandroid.GetNotificationList;
import graphqlandroid.GetProfileDetails;
import graphqlandroid.Logout;

import static com.attra.attralive.activity.OtpValidationActivity.PREFS_AUTH;
import static com.attra.attralive.util.NetworkUtil.isNetworkAvailable;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    ArrayList<NewsFeed> notificationArrayList;
    LinearLayoutManager linearLayoutManager;
    ImageView profileImage;
    TextView userName,userEmail;
    String userId1,username;
    String myToken;
    int notificationSize=0;
    ArrayList<Notification> notificationList;
    private static final String TAG = "DashboardActivity";
    public static String  Authorization= "Basic YXBwbGljYXRpb246c2VjcmV0";

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

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };



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

        sharedPreferences = getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            userId1 = sharedPreferences.getString("userId", "");
            username = sharedPreferences.getString("userName","");

            Log.i("userid",userId1);

      //      Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_LONG).show();
            Log.i("token in dashboard",myToken);
            Log.i("user id in dashboard",userId1);
            Log.i("user name in dashboard",username);


        }

        /*
        getProfileDetail();*/
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            NotificationManager notificationManager = (NotificationManager)getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.drawable.icon_blog_filled,
                    "Message received", System.currentTimeMillis());
            // Hide the notification after its selected
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            //adding LED lights to notification
            notification.defaults |= Notification.DEFAULT_LIGHTS;

            Intent intent = new Intent("android.intent.action.VIEW",
                    Uri.parse("http://my.example.com/"));
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    intent, 0);
            notificationManager.notify(0, notification);
        }
*/


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

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
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

   /* private void getProfileDetail(){

        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetProfileDetails.builder().userId(userId1)
                        .build()).enqueue(
                new ApolloCall.Callback<GetProfileDetails.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetProfileDetails.Data> response) {
                        Log.i("res", String.valueOf(response));
                        String message = response.data().getProfileDetails_Q().message();
                        String status = response.data().getProfileDetails_Q().status();
                        Log.i("message in dashboard",message);
                        Log.i("mstatus in dashboard",status);
                        if(response.data().getProfileDetails_Q().name()!=null){
                           *//* String message = response.data().getProfileDetails_Q().message();
                            String status = response.data().getProfileDetails_Q().status();*//*
                            if(status.equals("Success")){
                                String username = response.data().getProfileDetails_Q().name();
                                String emaiId = response.data().getProfileDetails_Q().email();
                                String imgPath = response.data().getProfileDetails_Q().profileImagePath();
                                Log.i("profile image path",imgPath);
                                DashboardActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        userName.setText(username);
                                        userEmail.setText(emaiId);
                                        Picasso.with(getApplicationContext()).load(imgPath).fit().into(profileImage);

                                    }
                                });
                            }
                            else if(status.equals("Failure")){

                            }

                        }

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );
    }*/


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
                    Intent i=new Intent(getApplicationContext(),EventRegistrationDetailsActivity.class);
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

            notificationList= new ArrayList<Notification>();
            Log.i("Network availabiltiy",""+isNetworkAvailable(getApplicationContext()));
            if(isNetworkAvailable(getApplicationContext()) )
            {
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
                                                   /*notificationList.add(
                                                           new Notification(noti.postType(),"","","", noti.action(),"", noti.userName(),"", noti.postMessage(),"","","Y"));
                                                           Log.i("notifications", noti.action());*/
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
            }else
            {
                Toast.makeText(this, "Please check network connectivity", Toast.LENGTH_SHORT).show();
            }
        }
        if(mImageLayoutView!=null) {
            mImageLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, notificationActivity.class);
                    if(notificationSize>0 && notificationList!=null)
                        intent.putExtra("NOTIFICATION_LIST", notificationList);

                    startActivity(intent);
                    ((View) actionView.findViewById(R.id.textView)).setVisibility(View.GONE);

                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }
    private void logoutUser(){
        MyAppolloClient.getMyAppolloClient(Authorization).mutate(Logout.builder().userId(userId1).build()).enqueue(new ApolloCall.Callback<Logout.Data>() {
            @Override
            public void onResponse(@Nonnull Response<Logout.Data> response) {
                String status = response.data().userLogout_M().status();
                String message = response.data().userLogout_M().message();
                Log.i("logout status ",status);
                Log.i(" logout message",message);
                DashboardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (status.equals("Success")) {
                            Toast.makeText(DashboardActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();
                            Intent i=new Intent(DashboardActivity.this,LoginActivity.class);
                            startActivity(i);
                        } else if ((status.equals("Failure")) && (message.equals("Invalid Username or Password"))) {
                            Toast.makeText(DashboardActivity.this,"Please try again",Toast.LENGTH_LONG).show();
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
