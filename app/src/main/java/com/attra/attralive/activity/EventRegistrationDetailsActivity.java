package com.attra.attralive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.fragment.EventDetailsFragment;
import com.attra.attralive.fragment.EventRegisteredDetailsFragment;
import com.attra.attralive.fragment.FAQFragment;
import com.attra.attralive.util.GetNewRefreshToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import graphqlandroid.GetEventDetails;

public class EventRegistrationDetailsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public LinearLayout ll1,ll2;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    ViewPagerAdapter adapter;
    EventRegisteredDetailsFragment fragment;
    FAQFragment faqFragment;
    Intent intent;
    ImageView eventposter;
    String eventId,reguserId;
    SharedPreferences sharedPreferences;
    String location,eventtitle,venue,startdate,enddate,starttime,endtime,description,
            status,message,eventpath,refreshToken,userId,name,myToken,registeredId,Qrcode;
    Bundle bundle,bundle1;
    String question[],answer[];
    Boolean isregisterd=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration_details);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        eventposter=findViewById(R.id.im_eventposter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attraction 2019");
        intent=getIntent();
        location=intent.getStringExtra("location");
//       Toast.makeText(getApplicationContext(),intent.getStringExtra("location").toString(),Toast.LENGTH_LONG).show();
       /* getEventDetails();*/
       // setupViewPager(viewPager);
      //  intent=getIntent();

//        Toast.makeText(getApplicationContext(),intent.getStringExtra("location").toString(),Toast.LENGTH_LONG).show();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            refreshToken = sharedPreferences.getString("refreshToken", "");
            Toast.makeText(getApplicationContext(), "refr" + refreshToken, Toast.LENGTH_LONG).show();
            userId = sharedPreferences.getString("userId", "");
            name = sharedPreferences.getString("userName", "");
            Log.i("user id in userDtail", userId);
            Log.i("refresh id in userDtail", refreshToken);
            Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();
        }
        getEventDetails(myToken);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            Intent intent=new Intent(EventRegistrationDetailsActivity.this,DashboardActivity.class);
            startActivity(intent);
        }
        return true;

        //return super.onOptionsItemSelected(item);
    }

    private void getEventDetails(String accesstoken)
{
    MyAppolloClient.getMyAppolloClient(accesstoken).
            query(GetEventDetails.builder().status("A").location("Bangalore").build()).enqueue(new ApolloCall.Callback<GetEventDetails.Data>() {
        @Override
        public void onResponse(@Nonnull Response<GetEventDetails.Data> response) {
            if(response.data().getEventDetails_Q().eventD()!=null){
                Log.d("succes","succes");
            String Status = response.data().getEventDetails_Q().eventD().get(0).status();
            String Message = response.data().getEventDetails_Q().eventD().get(0).message();
            eventpath = response.data().getEventDetails_Q().eventD().get(0).event_image_path();
            eventtitle = response.data().getEventDetails_Q().eventD().get(0).event_title();
            description = response.data().getEventDetails_Q().eventD().get(0).Description();
            venue = response.data().getEventDetails_Q().eventD().get(0).venue();
            startdate = response.data().getEventDetails_Q().eventD().get(0).Schedule().start_date();
            starttime = response.data().getEventDetails_Q().eventD().get(0).Schedule().start_time();
            enddate = response.data().getEventDetails_Q().eventD().get(0).Schedule().end_date();
            endtime = response.data().getEventDetails_Q().eventD().get(0).Schedule().end_time();
            eventId = response.data().getEventDetails_Q().eventD().get(0).id();
            int count=response.data().getEventDetails_Q().eventD().get(0).registeredUsers().size();
                for (int i = 0; i < count; i++) {
                    reguserId = response.data().getEventDetails_Q().eventD().get(0).registeredUsers().get(i).userId();
                    if (reguserId.equals(userId)) {
                            registeredId= response.data().getEventDetails_Q().eventD().
                                    get(0).registeredUsers().get(i).registrationId();
                            Qrcode=response.data().getEventDetails_Q().eventD().
                                    get(0).registeredUsers().get(i).QRCodeLink();
                            Log.d("QRCODE",Qrcode);
                        isregisterd=true;
                        break;
                    }
                }
            System.out.println("count"+count);

            EventRegistrationDetailsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(EventRegistrationDetailsActivity.this).load(eventpath).into(eventposter);


                    System.out.println("REGISTERED"+venue);
                    question=new String[response.data().getEventDetails_Q().eventD().get(0).FAQs().size()];
                    answer=new String[response.data().getEventDetails_Q().eventD().get(0).FAQs().size()];
                   for (int j = 0; j < response.data().getEventDetails_Q().eventD().get(0).FAQs().size(); j++) {
                        question[j]= response.data().getEventDetails_Q().eventD().get(0).FAQs().get(j).Question();
                        answer[j]= response.data().getEventDetails_Q().eventD().get(0).FAQs().get(j).Answer();
                    }
//                    bundle1.putInt("FAQcount", response.data().getEventDetails_Q().eventD().get(0).FAQs().size());
                    setupViewPager(viewPager,venue,startdate,enddate,starttime,endtime,eventId,location,question,answer,isregisterd,registeredId,Qrcode);

                }
            });

            if (Status.equals("Failure")) {
                if (Message.equals("Invalid token: access token is invalid")) {

                    GetNewRefreshToken.getRefreshtoken(refreshToken, EventRegistrationDetailsActivity.this);
                    EventRegistrationDetailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                            if (sharedPreferences.contains("authToken")) {
                                String myToken = sharedPreferences.getString("authToken", "");
                                getEventDetails(myToken);
                                Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        }
        else
            Log.d("fail","fail");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {

        }
    });
}
    private void setupViewPager(ViewPager viewPager,String venue,String startdate,String starttime,String enddate,String endtime,String eventId,String location,
                                String [] question,String [] answer,Boolean isregisterd,String registeredId,String Qrcode) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragment=new EventRegisteredDetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putString("Venue", venue);
        bundle.putString("StartDate", startdate);
        bundle.putString("EndDate", enddate);
        bundle.putString("StartTime", starttime);
        bundle.putString("EndTime", endtime);
        bundle.putString("EventId", eventId);
        bundle.putString("Location", location);
        bundle.putString("registeredId", registeredId);
        bundle.putString("Qrcode", Qrcode);
        bundle.putBoolean("isregisterd",isregisterd);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "Details");
        faqFragment=new FAQFragment();
        bundle1=new Bundle();
        System.out.println(question[0]);
        bundle1.putStringArray("question",question);
        bundle1.putStringArray("answer",answer);
        faqFragment.setArguments(bundle1);
        adapter.addFragment(faqFragment, "FAQ");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager manager) {

            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
