package com.attra.attralive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attra.attralive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventRegisteredDetailsFragment extends Fragment {
    TextView eventLocation;


    public EventRegisteredDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_event_registered_details, container, false);

        return view;
    }

}
