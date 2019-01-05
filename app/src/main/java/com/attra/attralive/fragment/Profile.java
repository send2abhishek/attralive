package com.attra.attralive.fragment;


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
public class Profile extends Fragment {
    TextView username, designation,DOB,gender,BU,phone,email, submit;


    public Profile() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.profile);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        username = view.findViewById(R.id.et_entername);
        designation=view.findViewById(R.id.et_designation);
        BU = view.findViewById(R.id.BUType);
        phone = view.findViewById(R.id.et_mobilenumber);
        email= view.findViewById(R.id.emailid);
        submit = view.findViewById(R.id.btn_submitDetails);
        return view;
    }

}
