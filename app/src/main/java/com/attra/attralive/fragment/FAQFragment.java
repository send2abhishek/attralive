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
    String [] question,answer;
    int FAQCount;
    LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_faq, container, false);
        recyclerView=v.findViewById(R.id.rv_FAQ);
        question = this.getArguments().getStringArray("question");
        answer = this.getArguments().getStringArray("answer");
        arrayList=new ArrayList<FAQ>();
        FAQCount=question.length;
        for(int i=0;i<FAQCount;i++) {

            faq=new FAQ(question[i],answer[i]);
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
