package com.attra.attralive.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attra.attralive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Facilities extends Fragment {


    public Facilities() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.facilities);
        return inflater.inflate(R.layout.fragment_facilities, container, false);
    }

}
