package com.attra.attralive.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.attra.attralive.R;
import com.attra.attralive.activity.EventDetailsActivity;
import com.attra.attralive.activity.UserDetailsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {


    public EventDetailsFragment() {
        // Required empty public constructor
    }
CardView register;
TextView details;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

               View v= inflater.inflate(R.layout.fragment_event_details, container, false);
               register=v.findViewById(R.id.crd_regbutton);
               details=v.findViewById(R.id.tv_location);
               register.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       ((EventDetailsActivity)getActivity()).openDtailedRegFragment(details.getText().toString());
                   }
               });
        return v;
    }

}
