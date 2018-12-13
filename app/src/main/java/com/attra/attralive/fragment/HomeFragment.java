package com.attra.attralive.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.activity.NewsFeedPostActivity;
import com.attra.attralive.adapter.NewsFeedListAdapter;
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
        linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        prepareNewsfeed();
        newsFeedListAdapter =new NewsFeedListAdapter(getActivity(),newsFeedArrayList);
        newsFeed.addItemDecoration(new DividerItemDecoration(newsFeed.getContext(),DividerItemDecoration.VERTICAL));
        newsFeed.setLayoutManager(linearLayoutManager);
        newsFeed.setAdapter(newsFeedListAdapter);

        postFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),NewsFeedPostActivity.class);
                startActivity(intent);
            }
        });


        return  view;

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

}
