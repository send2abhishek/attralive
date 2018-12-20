package com.attra.attralive.adapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attra.attralive.R;

import java.util.List;


    public class WidgetAdapter extends RecyclerView.Adapter<WidgetAdapter.MyView> {

        private List<String> list;

        public class MyView extends RecyclerView.ViewHolder {

            public TextView textView;

            public MyView(View view) {
                super(view);

                textView = view.findViewById(R.id.textview1);

            }
        }


        public WidgetAdapter(List<String> horizontalList) {
            this.list = horizontalList;
        }

        @Override
        public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.widgets_row, parent, false);

            return new MyView(itemView);
        }

        @Override
        public void onBindViewHolder(final MyView holder, final int position) {

            holder.textView.setText(list.get(position));

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

