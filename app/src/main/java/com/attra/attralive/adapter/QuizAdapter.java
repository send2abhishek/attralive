package com.attra.attralive.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;


import com.attra.attralive.R;
import com.attra.attralive.model.DigitalQuiz;

import java.util.ArrayList;

/**
 * Created by sirisha.kalluri on 1/12/2018.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder>{
    private Context mContext;
    private Activity mActivity;
    ArrayList<DigitalQuiz> list = null;

    public QuizAdapter(Context context, FragmentActivity activity, ArrayList<DigitalQuiz> digitalQuizList) {
        this.mContext = context;
        this.mActivity = activity;
        this.list = digitalQuizList;
    }

    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.quizlist, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuizAdapter.ViewHolder holder, int position) {
        for (DigitalQuiz digitalQuiz:list) {
            digitalQuiz = list.get(position);
            holder.mQidQname.setText("Q: "+digitalQuiz.getQuestionName());
            holder.mOption1.setText(digitalQuiz.getOption1());
            holder.mOption2.setText(digitalQuiz.getOption2());
            holder.mOption3.setText(digitalQuiz.getOption3());
            holder.mOption4.setText(digitalQuiz.getOption4());
            System.out.println("position: " +position);
            System.out.println("in loop: " +digitalQuiz.getQuestionId()+": "+digitalQuiz.getQuestionName()+" "+
                    digitalQuiz.getOption1()+ " " +digitalQuiz.getOption2()+" "+digitalQuiz.getOption3()+" "+ digitalQuiz.getOption4());
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("list.size: " +list.size());
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mQidQname;
        public RadioButton mOption1, mOption2, mOption3, mOption4;

        public ViewHolder(View itemView) {
            super(itemView);
            mQidQname = itemView.findViewById(R.id.qid_qname);
            mOption1 = itemView.findViewById(R.id.option1);
            mOption2 = itemView.findViewById(R.id.option2);
            mOption3 = itemView.findViewById(R.id.option3);
            mOption4 = itemView.findViewById(R.id.option4);

        }
    }
}
