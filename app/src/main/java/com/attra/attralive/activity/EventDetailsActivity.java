package com.attra.attralive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.attra.attralive.R;
import com.attra.attralive.fragment.EventDetailsFragment;
import com.attra.attralive.fragment.EventRegisteredDetailsFragment;
import com.attra.attralive.fragment.FAQFragment;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public LinearLayout ll1,ll2;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    ViewPagerAdapter adapter;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
      //  toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ll1=findViewById(R.id.Ll_first);
        ll2=findViewById(R.id.Ll_second);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventDetailsFragment(), "Details");
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
    public void openDtailedRegFragment(String location)
    {
       /* mFragmentList.remove(0);
        adapter.addFragment(new EventRegisteredDetailsFragment(),"details");
       // mFragmentList.add(new EventRegisteredDetailsFragment());
        adapter.notifyDataSetChanged()*/;
         intent=new Intent(getApplicationContext(),EventRegistrationDetailsActivity.class);
         intent.putExtra("location",location);
        startActivity(intent);
    }
    public void openr(ViewPager viewPager)
    {
        Toast.makeText(getApplicationContext(),"aaaas",Toast.LENGTH_LONG).show();
        mFragmentList.clear();
        mFragmentTitleList.clear();
        // adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventRegisteredDetailsFragment(), "Details");
        adapter.addFragment(new FAQFragment(), "FAQ");
        adapter.notifyDataSetChanged();

       // viewPager.setAdapter(adapter);
      // tabLayout.setupWithViewPager(viewPager);
    }
    }
