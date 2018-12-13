package com.attra.attralive.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.adapter.ForumListAdapter;
import com.attra.attralive.model.ForumQuestionListModel;
import com.attra.attralive.model.SearchResult1;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nonnull;

import graphqlandroid.ForumQuestionList;
import graphqlandroid.SearchQuestionResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public ArrayList<ForumQuestionListModel> forumQuestionListModels;
    public ForumQuestionListModel model;
    LayoutAnimationController controller=null;
    ForumListAdapter forumListAdapter=null;
    SearchResult1 searchResult1;
    ArrayList<SearchResult1> list;
    public ForumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = view.findViewById(R.id.rv_forum);

      /*  ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Forum's");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);*/

        forumQuestionListModels = new ArrayList<ForumQuestionListModel>();
        linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        prepareForumList();
       // setHasOptionsMenu(true);
        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent  = new Intent(getActivity(), CreateForumFragment.class);
                startActivity(intent);*/
               Fragment fragment = new CreateForumFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame_container,fragment );
                transaction.addToBackStack("forum");
                transaction.commit();

            }
        });
        return view;
    }

    public void prepareForumList()
    {
        MyAppolloClient.getMyAppolloClient().query(graphqlandroid.ForumQuestionList.builder().build()).enqueue(new ApolloCall.Callback<ForumQuestionList.Data>() {

            @Override
            public void onResponse(@Nonnull final Response<ForumQuestionList.Data> response) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Date date1 = new Date();

                        for (int loopVar = 0; loopVar < response.data().Questions().size() ;loopVar++) {
                            //      String datefromDB=response.data().Questions().get(loopVar).();

                            model = new ForumQuestionListModel(response.data().Questions().get(loopVar).id(), response.data().Questions().get(loopVar).id(), response.data().Questions().get(loopVar).topic(),
                                    response.data().Questions().get(loopVar).watch_count());
                            forumQuestionListModels.add(model);
                        }
                            controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_file);
                            forumListAdapter = new ForumListAdapter(getActivity(), forumQuestionListModels);
                            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(forumListAdapter);
                            recyclerView.setLayoutAnimation(controller);
                            recyclerView.scheduleLayoutAnimation();
                            Calendar cal = Calendar.getInstance();
                            DateFormat df = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
                            System.out.print("Dateformat" + df.format(cal.getTime()));
                            Toast.makeText(getActivity(), "user" + df.format(cal.getTime()), Toast.LENGTH_LONG).show();
                        }

                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });


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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.toolbar_action_items, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_favorite);

        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override

            public boolean onQueryTextSubmit(String query) {


                searchViewAndroidActionBar.clearFocus();
                getSearchResult(query);

                return true;

            }



            @Override

            public boolean onQueryTextChange(String newText) {



                return false;

            }

        });
      //  super.onCreateOptionsMenu(menu, inflater);
       // return true;
    }

    private void getSearchResult(final String question)
    {
        MyAppolloClient.getMyAppolloClient().query(SearchQuestionResult.builder().question(question).build()).enqueue(new ApolloCall.Callback<SearchQuestionResult.Data>() {
            @Override
            public void onResponse(@Nonnull Response<SearchQuestionResult.Data> response) {
                System.out.println("gayathryashwin"+question);
                //searchResult1=new SearchResult1(response.data().searchQuestion().get(0).topic(),1,2);
               // list.add(searchResult1);
                for (int loopVar = 0; loopVar < response.data().searchQuestion().size() ;loopVar++) {
                    //      String datefromDB=response.data().Questions().get(loopVar).();

                    model = new ForumQuestionListModel(response.data().searchQuestion().get(loopVar).topic(), "2","e",
                            1);
                    forumQuestionListModels.add(model);
                }
                controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_file);
                forumListAdapter = new ForumListAdapter(getActivity(), forumQuestionListModels);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(forumListAdapter);
                recyclerView.setLayoutAnimation(controller);
                recyclerView.scheduleLayoutAnimation();
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }
}
