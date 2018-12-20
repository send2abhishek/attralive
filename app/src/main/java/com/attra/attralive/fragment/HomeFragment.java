package com.attra.attralive.fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.attra.attralive.R;
import com.attra.attralive.activity.NewsFeedPostActivity;
import com.attra.attralive.adapter.NewsFeedListAdapter;
import com.attra.attralive.adapter.WidgetAdapter;
import com.attra.attralive.model.NewsFeed;

import java.util.ArrayList;

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


    RecyclerView recyclerView;
    ArrayList<String> Number;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    WidgetAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout ;
    View ChildView ;
    int RecyclerViewItemPosition ;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        postFeed = view.findViewById(R.id.tv_postThought);
        newsFeed=view.findViewById(R.id.rv_newsFeed);
        newsFeedArrayList=new ArrayList<NewsFeed>();
        Number = new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        prepareNewsfeed();
        newsFeedListAdapter =new NewsFeedListAdapter(getActivity(),newsFeedArrayList);
        newsFeed.addItemDecoration(new DividerItemDecoration(newsFeed.getContext(),DividerItemDecoration.VERTICAL));
        newsFeed.setLayoutManager(linearLayoutManager);
        newsFeed.setAdapter(newsFeedListAdapter);


        recyclerView = view.findViewById(R.id.recyclerview1);

        RecyclerViewLayoutManager = new LinearLayoutManager(getActivity(),LinearLayout.HORIZONTAL,false);

        recyclerView.setLayoutManager(RecyclerViewLayoutManager);


        // Adding items to RecyclerView.
        AddItemsToRecyclerViewArrayList();

        RecyclerViewHorizontalAdapter = new WidgetAdapter(Number);

        HorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);

        autoScroll();


        // Adding on item click listener to RecyclerView.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting clicked value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    // Showing clicked item value on screen using toast message.
                    Toast.makeText(getActivity(), Number.get(RecyclerViewItemPosition), Toast.LENGTH_LONG).show();

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



        postFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),NewsFeedPostActivity.class);
                startActivity(intent);
            }
        });


        return  view;

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

    private void prepareNewsfeed()
    {
        newsFeedList =new NewsFeed(R.drawable.ic_behance,R.drawable.blogreadimage,"Sangraj",
                "This is tmy first post","1 day ago","Blockchain Technology","12","3");
        newsFeedArrayList.add(newsFeedList);
        newsFeedList =new NewsFeed(R.drawable.ic_behance,R.drawable.blogreadimage,"Sangraj",
                "This is tmy first post","1 day ago","Blockchain Technology","12","3");
        newsFeedArrayList.add(newsFeedList);
        newsFeedList =new NewsFeed(R.drawable.ic_behance,R.drawable.blogreadimage,"Sangraj",
                "This is tmy first post","1 day ago","Blockchain Technology","12","3");
        newsFeedArrayList.add(newsFeedList);
        newsFeedList =new NewsFeed(R.drawable.ic_behance,R.drawable.blogreadimage,"Sangraj",
                "This is tmy first post","1 day ago","Blockchain Technology","12","3");
        newsFeedArrayList.add(newsFeedList);
        newsFeedList =new NewsFeed(R.drawable.ic_behance,R.drawable.blogreadimage,"Sangraj",
                "This is tmy first post","1 day ago","Blockchain Technology","12","3");
        newsFeedArrayList.add(newsFeedList);
        newsFeedList =new NewsFeed(R.drawable.ic_behance,R.drawable.blogreadimage,"Sangraj",
                "This is tmy first post","1 day ago","Blockchain Technology","12","3");
        newsFeedArrayList.add(newsFeedList);
        newsFeedList =new NewsFeed(R.drawable.ic_behance,R.drawable.blogreadimage,"Sangraj",
                "This is tmy first post","1 day ago","Blockchain Technology","12","3");
        newsFeedArrayList.add(newsFeedList);
    }

    public void AddItemsToRecyclerViewArrayList(){

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
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollBy(2, 0);
                handler.postDelayed(this, 10);
            }
        };
        handler.postDelayed(runnable, 0);
    }

}

