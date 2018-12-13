package com.attra.attralive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.SearchResult;

import java.util.ArrayList;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {
    Context context;
    ArrayList<SearchResult> list;
    SearchResult searchResult;
    public SearchResultAdapter(Context context, ArrayList<SearchResult> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_row,viewGroup,false);
        SearchViewHolder searchViewHolder=new SearchViewHolder(view);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
    searchResult=list.get(i);
    searchViewHolder.serachresult.setText(searchResult.getSerachresult());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView serachresult;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            serachresult=itemView.findViewById(R.id.tv_searchresult);
        }
    }
}
