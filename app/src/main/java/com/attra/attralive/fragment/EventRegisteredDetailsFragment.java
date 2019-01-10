package com.attra.attralive.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.EventRegistrationDetailsActivity;
import com.squareup.picasso.Picasso;

import javax.annotation.Nonnull;

import graphqlandroid.GetEventDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventRegisteredDetailsFragment extends Fragment {


    public EventRegisteredDetailsFragment() {
        // Required empty public constructor
    }

    String eventtitle,venue,startdate,enddate,starttime,endtime,description,status,message,userid;
    TextView eventvenue,eventdate,eventtime;
    CardView register;
    TextView regdetails;
    ImageView qrcode;
    LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_event_registered_details, container, false);
        eventvenue=v.findViewById(R.id.tv_eventvenue);
        eventdate=v.findViewById(R.id.tv_eventdate);
        eventtime=v.findViewById(R.id.tv_eventtime);
        register=v.findViewById(R.id.crd_regevent);
        regdetails=v.findViewById(R.id.tv_regid);
        qrcode=v.findViewById(R.id.im_qrcode);
        linearLayout=v.findViewById(R.id.ll_qrcode);
        getEventDetails();
        String loc=this.getArguments().getString("location");
        System.out.println("loc"+loc);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        return v;
    }
    private void getEventDetails()
    {
        MyAppolloClient.getMyAppolloClient("Bearer 90f57fce635b44bf4a58733f69f9d24da5f3e6e2").
                query(GetEventDetails.builder().status("A").build()).enqueue(new ApolloCall.Callback<GetEventDetails.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetEventDetails.Data> response) {

//            eventtitle=response.data().getEventDetails_Q().event_title();
//            description=response.data().getEventDetails_Q().Description();
//            venue=response.data().getEventDetails_Q().venue();
//            startdate=response.data().getEventDetails_Q().Schedule().get(0).start_date();
//            starttime=response.data().getEventDetails_Q().Schedule().get(0).start_time();
//            enddate=response.data().getEventDetails_Q().Schedule().get(0).end_date();
//            endtime=response.data().getEventDetails_Q().Schedule().get(0).end_time();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    eventvenue.setText(venue);
                    eventdate.setText(startdate);
                    eventtime.setText(starttime+":"+endtime);
                }
            });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

}
