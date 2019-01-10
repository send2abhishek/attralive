package com.attra.attralive.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    Intent intent;
    ImageView eventposter;
    String eventtitle,venue,startdate,enddate,starttime,endtime,description,status,message,eventpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration_details);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        eventposter=findViewById(R.id.im_eventposter);
        getEventDetails();
        setupViewPager(viewPager);
      //  intent=getIntent();

//        Toast.makeText(getApplicationContext(),intent.getStringExtra("location").toString(),Toast.LENGTH_LONG).show();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
private void getEventDetails()
{
    MyAppolloClient.getMyAppolloClient("Bearer 850085dbbf7f2448476d143a36acf6b7a2f983a4").
            query(GetEventDetails.builder().status("A").build()).enqueue(new ApolloCall.Callback<GetEventDetails.Data>() {
        @Override
        public void onResponse(@Nonnull Response<GetEventDetails.Data> response) {
          //  eventpath=response.data().getEventDetails_Q().event_image_path();
            EventRegistrationDetailsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(EventRegistrationDetailsActivity.this).load(eventpath).into(eventposter);
                }
            });
            /*eventtitle=response.data().getEventDetails_Q().event_title();
            description=response.data().getEventDetails_Q().Description();
            venue=response.data().getEventDetails_Q().venue();
            startdate=response.data().getEventDetails_Q().Schedule().get(0).start_date();
            starttime=response.data().getEventDetails_Q().Schedule().get(0).start_date();
            startdate=response.data().getEventDetails_Q().Schedule().get(0).start_date();
            startdate=response.data().getEventDetails_Q().Schedule().get(0).start_date();*/

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {

        }
    });
}
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragment=new EventRegisteredDetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putString("location","bangalore");
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "Details");
        adapter.addFragment(new FAQFragment(), "FAQ");
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
