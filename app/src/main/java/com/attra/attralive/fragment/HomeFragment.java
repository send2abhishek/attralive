package com.attra.attralive.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.VideoView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.DashboardActivity;
import com.attra.attralive.activity.NewsFeedPostActivity;
import com.attra.attralive.activity.OtpValidationActivity;
import com.attra.attralive.adapter.NewsFeedListAdapter;
import com.attra.attralive.adapter.SliderAdapter;
import com.attra.attralive.adapter.WidgetAdapter;
import com.attra.attralive.model.NewsFeed;
import com.attra.attralive.model.NewsFeedNew;
import com.attra.attralive.util.GetNewRefreshToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;

import graphqlandroid.GetPosts;

import static com.attra.attralive.activity.OtpValidationActivity.PREFS_AUTH;

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
    ImageView descImage;
    ArrayList<String> Number;
    String refreshToken,myToken,accesstoken,postId;

    SharedPreferences sharedPreferences;

    ViewPager viewPager;

    String images[] = {"https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546607539810-ic_launcher.png","https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546607539810-ic_launcher.png","https://dsd8ltrb0t82s.cloudfront.net/EventsQRCodes/Att_5c2353c4daea021e34431842.png"};

    //String[] images= new String[1];
    //String images[] = {"https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546607539810-ic_launcher.png","https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546607539810-ic_launcher.png"};
    SliderAdapter myCustomPagerAdapter;

    // imgview.setScaleType(ImageView.ScaleType.FIT_XY);

    private static int currentPage = 0;

   // private SharedPreferences sharedPreferences;
    String userId1,username;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



         sharedPreferences =this.getActivity().getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            userId1 = sharedPreferences.getString("userId", "");
            username = sharedPreferences.getString("userName","");
            postId = sharedPreferences.getString("postId","");
            refreshToken = sharedPreferences.getString("refreshToken", "");
            //      Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_LONG).show();
            Log.i("token in dashboard",myToken);
            Log.i("user id in dashboard",userId1);

        }

        postFeed = view.findViewById(R.id.tv_postThought);
        newsFeed = view.findViewById(R.id.rv_newsFeed);
        newsFeedArrayList = new ArrayList<NewsFeed>();
        Number = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        prepareNewsfeed();
        sharedPreferences = getActivity().getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            //callservice(myToken);
            Toast.makeText(getActivity(), myToken, Toast.LENGTH_LONG).show();

        }
        if (sharedPreferences.contains("refreshToken")) {
            refreshToken = sharedPreferences.getString("refreshToken", "");
            Toast.makeText(getActivity(), refreshToken, Toast.LENGTH_LONG).show();
            //callservice(myToken);
            //Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

        }




       // prepareNewsfeed();
        GetEventWidgetsFromService();

        prepareNewsfeed(myToken);

        System.out.println("After prepareNewsfeed");
        newsFeedListAdapter = new NewsFeedListAdapter(getActivity(), newsFeedArrayList);
        newsFeed.addItemDecoration(new DividerItemDecoration(newsFeed.getContext(), DividerItemDecoration.VERTICAL));
        newsFeed.setLayoutManager(linearLayoutManager);
        newsFeed.setAdapter(newsFeedListAdapter);


       // prepareNewsfeed();
        GetEventWidgetsFromService();
        System.out.println("Outside method "+images[0]);
       // prepareNewsfeed();

        System.out.println("After prepareNewsfeed");
//        newsFeedListAdapter = new NewsFeedListAdapter(getActivity(), newsFeedArrayList);
//        newsFeed.addItemDecoration(new DividerItemDecoration(newsFeed.getContext(), DividerItemDecoration.VERTICAL));
//        newsFeed.setLayoutManager(linearLayoutManager);
//        newsFeed.setAdapter(newsFeedListAdapter);


        viewPager = view.findViewById(R.id.viewPager);

        myCustomPagerAdapter = new SliderAdapter(getActivity(), images);
        viewPager.setAdapter(myCustomPagerAdapter);
        autoScroll();

        ImageView imageView = view.findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blogreadimage);




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

    private void GetEventWidgetsFromService()
    {
        System.out.println("Inside GetEvent method");

        MyAppolloClient.getMyAppolloClient(myToken).query(
               GetEventWidgets.builder().status("A").build()).enqueue(
               new ApolloCall.Callback<GetEventWidgets.Data>() {
                   @Override
                   public void onResponse(@Nonnull Response<GetEventWidgets.Data> response) {
                            Log.i("Inside getevent ","inside response method");
                       String eventWidgetPath = "";
                       //System.out.println("gg"+response.data().getEventWidget_Q().status());
                      // System.out.println("WW "+response.data().getEventWidget_Q().widget().get(0).event_widget_path());
                       //images[0] = eventWidgetPath;
                      // System.out.println("This is image"+images[0]);
                       if(response.data().getEventWidget_Q().status().equals("Success"))

                       {
                           for(int i =0;i<response.data().getEventWidget_Q().widget().size();i++)
                           {

                               String eventId = response.data().getEventWidget_Q().widget().get(i).event_id();
                               eventWidgetPath = response.data().getEventWidget_Q().widget().get(i).event_widget_path();
                               Log.i("Widget",eventWidgetPath);
                               images[i] = eventWidgetPath;

                          }

                       }
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               myCustomPagerAdapter = new SliderAdapter(getActivity(), images);
                               viewPager.setAdapter(myCustomPagerAdapter);

                           }
                       });




                   }

                   @Override
                   public void onFailure(@Nonnull ApolloException e) {

                   }
               }
       );


    }

    private void prepareNewsfeed(String myToken) {


       MyAppolloClient.getMyAppolloClient(myToken).query(
               GetPosts.builder().build()).enqueue(
               new ApolloCall.Callback<GetPosts.Data>() {
                   @Override
                   public void onResponse(@Nonnull Response<GetPosts.Data> response) {
                       String status = response.data().getPosts_Q().status();
                       String message=response.data().getPosts_Q().message();
                       Log.d("mesa",message);
                       if(status.equals("Success"))
                       {
                           Log.i("","inside success");

                           for(int i =0;i<response.data().getPosts_Q().posts().size();i++)
                           {
                               Log.i("Here",response.data().getPosts_Q().posts().get(i).userId()+
                                       response.data().getPosts_Q().posts().get(i).postId()
                                       +response.data().getPosts_Q().posts().get(i).profileImagePath()+
                                       response.data().getPosts_Q().posts().get(i).filePath()+
                                       response.data().getPosts_Q().posts().get(i).name()+
                                       response.data().getPosts_Q().posts().get(i).location()+
                                       response.data().getPosts_Q().posts().get(i).timeago()+
                                       response.data().getPosts_Q().posts().get(i).description()+
                                       response.data().getPosts_Q().posts().get(i).likesCount()+
                                       response.data().getPosts_Q().posts().get(i).commentsCount());

                              newsFeedList = new NewsFeed(
                                      response.data().getPosts_Q().posts().get(i).userId(),
                                      response.data().getPosts_Q().posts().get(i).postId(),
                                      response.data().getPosts_Q().posts().get(i).profileImagePath(),
                                      response.data().getPosts_Q().posts().get(i).filePath(),
                                      response.data().getPosts_Q().posts().get(i).name(),
                                      response.data().getPosts_Q().posts().get(i).location(),
                                      response.data().getPosts_Q().posts().get(i).timeago(),
                                      response.data().getPosts_Q().posts().get(i).description(),
                                      response.data().getPosts_Q().posts().get(i).likesCount(),
                                      response.data().getPosts_Q().posts().get(i).commentsCount());




                              newsFeedArrayList.add(newsFeedList);

                              getActivity().runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {

                                      newsFeedListAdapter = new NewsFeedListAdapter(getActivity(), newsFeedArrayList);

                                    //  newsFeed.addItemDecoration(new DividerItemDecoration(newsFeed.getContext(), DividerItemDecoration.VERTICAL));

                                     // newsFeed.addItemDecoration(new DividerItemDecoration(newsFeed.getContext(), DividerItemDecoration.VERTICAL));
                                      newsFeed.setLayoutManager(linearLayoutManager);
                                      newsFeed.setAdapter(newsFeedListAdapter);
                                  }
                              });
                           }
                       }
                       else
                           if (message.equals("Invalid token: access token is invalid")) {
                               Log.d("www",message);
                               GetNewRefreshToken.getRefreshtoken(refreshToken, getActivity());
                               sharedPreferences = getActivity().getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                               if (sharedPreferences.contains("authToken")) {
                                    accesstoken = sharedPreferences.getString("authToken", "");
                                   //callservice(myToken);
                                   //Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();
                               }
                               prepareNewsfeed(accesstoken);

                       }
                   }

                   @Override
                   public void onFailure(@Nonnull ApolloException e) {

                   }
               }
       );


    }

    public void SetDescPic() {


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(getActivity())
                        .load("https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546607539810-ic_launcher.png")
                        .into(descImage);
            }
        });
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

    public void autoScroll(){
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == images.length) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
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