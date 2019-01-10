package com.attra.attralive.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attra.attralive.R;
import com.attra.attralive.adapter.FAQAdapter;
import com.attra.attralive.model.FAQ;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {


    public FAQFragment() {
        // Required empty public constructor
    }

RecyclerView recyclerView;
    ArrayList<FAQ> arrayList;
    FAQ faq;
    FAQAdapter faqAdapter;
    String question,answer;
    int FAQCount;
    LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_faq, container, false);
        recyclerView=v.findViewById(R.id.rv_FAQ);
        arrayList=new ArrayList<FAQ>();
        FAQCount=this.getArguments().getInt("FAQcount");
        for(int i=0;i<FAQCount;i++) {
            question = this.getArguments().getString("Question" + i);
            answer = this.getArguments().getString("Answer" + i);
            faq=new FAQ(question,answer);
            arrayList.add(faq);
        }
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        faqAdapter=new FAQAdapter(getActivity(),arrayList);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(faqAdapter);
        return v;
    }

}
