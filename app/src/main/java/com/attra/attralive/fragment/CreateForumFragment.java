package com.attra.attralive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.CreateForumActivity;
import com.attra.attralive.adapter.SearchResultAdapter;
import com.attra.attralive.model.SearchResult;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import graphqlandroid.SearchQuestionResult;

import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateForumFragment extends Fragment {


    public CreateForumFragment() {
        // Required empty public constructor
    }

    Button button,serachbtn;
    RecyclerView searchresultlist;
    ArrayList<SearchResult> serachlist;
    LinearLayoutManager linearLayoutManager;
    SearchResult searchResult;
    SearchResultAdapter searchAdapter;
    EditText searchquestion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_create_forum, container, false);

        final CheckBox checkBox=view.findViewById(R.id.forum_checkbox);
          button = (Button) view.findViewById(R.id.btn_createForum);
          searchquestion=view.findViewById(R.id.serch_forum_input);
          searchresultlist=view.findViewById(R.id.rv_searchresults);
          serachbtn=view.findViewById(R.id.forumSeacrhButton);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), VERTICAL);
        searchresultlist.addItemDecoration(decoration);
          serachlist=new ArrayList<SearchResult>();
          linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

          serachbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  searchresultlist.setVisibility(View.VISIBLE);
                  getsearchresult();

              }
          });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final  Intent intent = new Intent(getActivity(), CreateForumActivity.class);
                if(checkBox.isChecked()){
                    startActivity(intent);
                }else{
                    checkBox.setError("Please select Check Box");
                    checkBox.requestFocus();
                }


            }
        });
        return view;
    }
    private void getsearchresult()
    {
        MyAppolloClient.getMyAppolloClient().query(SearchQuestionResult.builder().question(searchquestion.getText().toString()).build()).enqueue(new ApolloCall.Callback<SearchQuestionResult.Data>() {
            @Override
            public void onResponse(@Nonnull Response<SearchQuestionResult.Data> response) {
                serachlist.clear();
                searchquestion.setText("");
                for(int loopVar=0;loopVar<response.data().searchQuestion().size();loopVar++)
                {
                    searchResult=new SearchResult(response.data().searchQuestion().get(loopVar).topic());
                    serachlist.add(searchResult);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchAdapter=new SearchResultAdapter(getActivity(),serachlist);
                        searchresultlist.setLayoutManager(linearLayoutManager);
                        searchresultlist.setAdapter(searchAdapter);
                    }
                });

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
        /*searchResult=new SearchResult("java not oo language");
        serachlist.add(searchResult);
        searchResult=new SearchResult("java not oo language");
        serachlist.add(searchResult);
        searchResult=new SearchResult("java not oo language");
        serachlist.add(searchResult);
        searchResult=new SearchResult("java not oo language");
        serachlist.add(searchResult);
        searchResult=new SearchResult("java not oo language");
        serachlist.add(searchResult);*/
    }

/*    @Override
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
    }*/

}
