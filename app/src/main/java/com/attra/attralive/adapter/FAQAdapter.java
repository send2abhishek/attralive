package com.attra.attralive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.model.FAQ;

import java.util.ArrayList;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.MyViewHolder> {
    Context context;
    ArrayList<FAQ> arrayList;
    FAQ faq;
    public FAQAdapter(Context activity, ArrayList<FAQ> arrayList) {
        context=activity;
        arrayList=this.arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.faq_row,viewGroup,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
faq=arrayList.get(i);
myViewHolder.question.setText(faq.getQuestion());
myViewHolder.answer.setText(faq.getAnswer());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question,answer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.tv_question);
            answer=itemView.findViewById(R.id.tv_answer);
        }
    }
}
