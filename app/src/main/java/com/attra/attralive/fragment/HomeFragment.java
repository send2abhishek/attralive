package com.attra.attralive.fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.NewsFeedPostActivity;
import com.attra.attralive.adapter.NewsFeedListAdapter;
import com.attra.attralive.adapter.SliderAdapter;
import com.attra.attralive.adapter.WidgetAdapter;
import com.attra.attralive.model.NewsFeed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;

import graphqlandroid.GetPosts;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    RecyclerView newsFeed;
    ArrayList<NewsFeed> newsFeedArrayList;
    NewsFeedListAdapter newsFeedListAdapter;
    LinearLayoutManager linearLayoutManager;
    NewsFeed newsFeedList;
    TextView postFeed;
    ImageView iv;


    RecyclerView recyclerView;
    ArrayList<String> Number;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    WidgetAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int RecyclerViewItemPosition;
    ViewPager viewPager;
    String images[] = {"https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546592733408-29a82067b71bd9e3df95e1c0ba5c4daf--fantasy-art-avatar-jake-sully.jpg","https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546592733408-29a82067b71bd9e3df95e1c0ba5c4daf--fantasy-art-avatar-jake-sully.jpg"};
    SliderAdapter myCustomPagerAdapter;

    // imgview.setScaleType(ImageView.ScaleType.FIT_XY);

    private static int currentPage = 0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        postFeed = view.findViewById(R.id.tv_postThought);
        newsFeed = view.findViewById(R.id.rv_newsFeed);
        newsFeedArrayList = new ArrayList<NewsFeed>();
        Number = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        prepareNewsfeed();
        newsFeedListAdapter = new NewsFeedListAdapter(getActivity(), newsFeedArrayList);
        newsFeed.addItemDecoration(new DividerItemDecoration(newsFeed.getContext(), DividerItemDecoration.VERTICAL));
        newsFeed.setLayoutManager(linearLayoutManager);
        newsFeed.setAdapter(newsFeedListAdapter);

        viewPager = view.findViewById(R.id.viewPager);
        ImageView example = view.findViewById(R.id.imageView);
        iv = view.findViewById(R.id.img_descImage);

//        Picasso.with(getActivity())
//                .load("https://attralive.s3.ap-south-1.amazonaws.com/NewsFeedsPictures/1546237731525-launcher.jpeg")
//                .into(example);

        myCustomPagerAdapter = new SliderAdapter(getActivity(), images);
        viewPager.setAdapter(myCustomPagerAdapter);

        ImageView imageView = view.findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blogreadimage);


        autoScroll();


        // Adding on item click listener to RecyclerView.


        postFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewsFeedPostActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }

    private void prepareNewsfeed() {

       MyAppolloClient.getMyAppolloClient("Bearer a2fba7c054a979eb63a22186ca142a14e08706f2").query(
               GetPosts.builder().build()).enqueue(
               new ApolloCall.Callback<GetPosts.Data>() {
                   @Override
                   public void onResponse(@Nonnull Response<GetPosts.Data> response) {
                       String status = response.data().getPosts_Q().status();

                       if(status.equals("Success"))
                       {
                           Log.i("","inside success");

                           for(int i =0;i<response.data().getPosts_Q().posts().size();i++)
                           {

                           //   String data = response.data().getPosts_Q().posts().get(i).description();
                               Log.i("",response.data().getPosts_Q().posts().get(i).description());
                              newsFeedList = new NewsFeed("",response.data().getPosts_Q().posts().get(i).filePath(),"","",
                              "",response.data().getPosts_Q().posts().get(i).description(),"","");
                             // Log.i("",response.data().getPosts_Q().posts().get(i).description());
                               Log.i("",response.data().getPosts_Q().posts().get(i).filePath());
                               Picasso.with(getActivity())
                                 .load(response.data().getPosts_Q().posts().get(i).filePath())
                                 .into(iv);
                              newsFeedArrayList.add(newsFeedList);

                           }
                       }
                   }

                   @Override
                   public void onFailure(@Nonnull ApolloException e) {

                   }
               }
       );

//        newsFeedList = new NewsFeed(R.drawable.ic_behance, R.drawable.blogreadimage, "Sangraj",
//                "This is tmy first post", "1 day ago", "Blockchain Technology", "12", "3");
//        newsFeedArrayList.add(newsFeedList);
//        newsFeedList = new NewsFeed(R.drawable.ic_behance, R.drawable.blogreadimage, "Sangraj",
//                "This is tmy first post", "1 day ago", "Blockchain Technology", "12", "3");
//        newsFeedArrayList.add(newsFeedList);
//        newsFeedList = new NewsFeed(R.drawable.ic_behance, R.drawable.blogreadimage, "Sangraj",
//                "This is tmy first post", "1 day ago", "Blockchain Technology", "12", "3");
//        newsFeedArrayList.add(newsFeedList);
//        newsFeedList = new NewsFeed(R.drawable.ic_behance, R.drawable.blogreadimage, "Sangraj",
//                "This is tmy first post", "1 day ago", "Blockchain Technology", "12", "3");
//        newsFeedArrayList.add(newsFeedList);
//        newsFeedList = new NewsFeed(R.drawable.ic_behance, R.drawable.blogreadimage, "Sangraj",
//                "This is tmy first post", "1 day ago", "Blockchain Technology", "12", "3");
//        newsFeedArrayList.add(newsFeedList);
//        newsFeedList = new NewsFeed(R.drawable.ic_behance, R.drawable.blogreadimage, "Sangraj",
//                "This is tmy first post", "1 day ago", "Blockchain Technology", "12", "3");
//        newsFeedArrayList.add(newsFeedList);
//        newsFeedList = new NewsFeed(R.drawable.ic_behance, R.drawable.blogreadimage, "Sangraj",
//                "This is tmy first post", "1 day ago", "Blockchain Technology", "12", "3");
//        newsFeedArrayList.add(newsFeedList);
    }

    public void AddItemsToRecyclerViewArrayList() {

        Number = new ArrayList<>();
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");
        Number.add("ATTRACTION'19");

    }

    public void autoScroll() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }
}