package com.attra.attralive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RatingBar;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.MainBlogCategoryActivity;
import com.attra.attralive.adapter.BlogListAdapter;
import com.attra.attralive.model.BlogListModel;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nonnull;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogListFragment extends Fragment {
    RecyclerView blogList_RV;
    public ArrayList<BlogListModel> blogArrayList;
    BlogListAdapter myAdapter;
    LinearLayoutManager linearLayoutManager;
    LayoutAnimationController controller=null;
    public BlogListModel blogListModel;
    Toolbar toolbar;
    RatingBar blogRating;
    public String username,blogtitle;
    public BlogListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_blog_list, container, false);
        blogList_RV=view.findViewById(R.id.Rv_Blog);
        blogRating = view.findViewById(R.id.ratingbar);
        //toolbar=view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);

       // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Blogs");
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), MainBlogCategoryActivity.class);
                startActivity(intent);


            }
        });


        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
       /* ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Blog's");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);*/

        blogArrayList=new ArrayList<BlogListModel>();
        linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        prepareBlogList();



        return  view;
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                System.out.print("hai");
                getActivity().onBackPressed();
                return true;
            case R.id.action_settings:
                Toast.makeText(getActivity(),"welcome",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/


    private void prepareBlogList()
    {
        getBlogListFromDB();

       /* blogListModel=new BlogListModel(2.0f,R.drawable.ic_behance,"Flutter With Material Design",
                "Sagar,EC2,Bangalore","1 day ago");
        blogArrayList.add(blogListModel);
        blogListModel=new BlogListModel(3.0f,R.drawable.ic_dropbox,"India's Top Finance and Mutual Funds",
                "Sendil,AMR,Bangalore","2 day ago");
        blogArrayList.add(blogListModel);
        blogListModel=new BlogListModel(5.0f,R.drawable.ic_foursquare,"Tips For Healthy Life ",
                "Anand,EC2,Bangalore","1 Hour ago");
        blogArrayList.add(blogListModel);
        blogListModel=new BlogListModel(1.0f,R.drawable.ic_man,"Daily Life of a Sports man ",
                "Siva,EC2,Bangalore","3 Hour ago");
        blogArrayList.add(blogListModel);
        blogListModel=new BlogListModel(5.0f,R.drawable.ic_myspace,"Blockchain Technology and Bitcoin",
                "Tom,EC2,Bangalore","7 Hour ago");
        blogArrayList.add(blogListModel);
        blogListModel=new BlogListModel(3.0f,R.drawable.ic_yelp,"RRP and UI Path",
                "Harry,Australia","1 Hour ago");
        blogArrayList.add(blogListModel);
        blogListModel=new BlogListModel(4.0f,R.drawable.ic_path,"Trends  in Technology ",
                "Rose,EC2,Bangalore","3 Hour ago");
        blogArrayList.add(blogListModel);*/
    }
    public void getBlogListFromDB()
    {
        MyAppolloClient.getMyAppolloClient().query(graphqlandroid.BlogList.builder().build()).enqueue(new ApolloCall.Callback<graphqlandroid.BlogList.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<graphqlandroid.BlogList.Data> response) {
                username=response.data().Blogs().get(0).user();
                blogtitle=response.data().Blogs().get(0).title();
                String rating = String.valueOf((response.data().Blogs().get(0).overallRating()).floatValue());
                final float  blogRatingData = (response.data().Blogs().get(0).overallRating()).floatValue();
                Log.i("blog rating", rating);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            Date date1=new Date() ;

                        for(int loopVar=0;loopVar<response.data().Blogs().size();loopVar++){
                            String datefromDB=response.data().Blogs().get(loopVar).date();
                            String datenew[]=datefromDB.split(" ");

                        blogListModel =new BlogListModel(R.drawable.ic_behance,blogRatingData,response.data().Blogs().get(loopVar).title(),
                               response.data().Blogs().get(loopVar).user()+" ,EC2,Bangalore",datenew[0],response.data().Blogs().get(loopVar).id());
                        blogArrayList.add( blogListModel);}
                        controller=AnimationUtils.loadLayoutAnimation(blogList_RV.getContext(),R.anim.layout_file);
                        myAdapter=new BlogListAdapter(getActivity(),blogArrayList);
                        blogList_RV.addItemDecoration(new DividerItemDecoration(blogList_RV.getContext(),DividerItemDecoration.VERTICAL));
                        blogList_RV.setLayoutManager(linearLayoutManager);
                        blogList_RV.setAdapter(myAdapter);
                        blogList_RV.setLayoutAnimation(controller);
                        blogList_RV.scheduleLayoutAnimation();
                        Calendar cal=Calendar.getInstance();
                        DateFormat df=new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
                        System.out.print("Dateformat"+df.format(cal.getTime()));
                       Toast.makeText(getActivity(),"user"+df.format(cal.getTime()),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });


    }

}
