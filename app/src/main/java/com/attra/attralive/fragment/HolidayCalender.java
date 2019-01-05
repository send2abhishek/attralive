package com.attra.attralive.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.attra.attralive.R;
import com.attra.attralive.activity.UserDetailsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HolidayCalender extends Fragment {
    private static String[] locationList = {"Please select Location","Bangalore","Hydrabad", "Pune"};
    Spinner calenderLocation;

    public HolidayCalender() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        getActivity().setTitle(R.string.holiday_calender);
        View view= inflater.inflate(R.layout.fragment_holiday_calender, container, false);
        calenderLocation = view.findViewById(R.id.sp_calenderLocation);
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calenderLocation.setAdapter(locationAdapter);
        calenderLocation.setSelection(0);
        return view;
    }

}
