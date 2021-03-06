package com.attra.attralive.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.EventRegistrationDetailsActivity;
import com.attra.attralive.activity.MapsActivity;
import com.attra.attralive.util.GetNewRefreshToken;
import com.squareup.picasso.Picasso;

import javax.annotation.Nonnull;

import graphqlandroid.EventRegistration;
import graphqlandroid.GetEventDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventRegisteredDetailsFragment extends Fragment {
    TextView eventLocation;


    public EventRegisteredDetailsFragment() {
        // Required empty public constructor
    }

    String eventtitle,venue,startdate,enddate,starttime,endtime,description,status,message,eventId;
    Boolean isRegistered;
    TextView eventvenue,eventdate,eventtime,map;
    Button register;
    TextView regdetails;
    ImageView qrcode;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences;
    String loc,myToken,refreshToken,name,userId,reguserId,emailId,Qrcodelink,regId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_event_registered_details, container, false);

        eventvenue=view.findViewById(R.id.tv_eventvenue);
        eventdate=view.findViewById(R.id.tv_eventdate);
        eventtime=view.findViewById(R.id.tv_eventtime);
        register=view.findViewById(R.id.bt_regbutton);
        regdetails=view.findViewById(R.id.tv_regid);
        qrcode=view.findViewById(R.id.im_qrcode);
        map = view.findViewById(R.id.tv_viewMap);
        linearLayout=view.findViewById(R.id.ll_qrcode);
        System.out.println("fragment");
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(),MapsActivity.class);
                startActivity(intent);
            }
        });
        sharedPreferences = getActivity().getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            refreshToken = sharedPreferences.getString("refreshToken", "");
            userId = sharedPreferences.getString("userId", "");
            name = sharedPreferences.getString("userName", "");
            emailId=sharedPreferences.getString("emailId","");
            Log.i("user id in userDtail", userId);
            Log.i("refresh id in userDtail", refreshToken);
        }

         loc=this.getArguments().getString("location");
        venue=this.getArguments().getString("Venue");
        startdate=this.getArguments().getString("StartDate");
        enddate=this.getArguments().getString("EndDate");
        starttime=this.getArguments().getString("StartTime");
        endtime=this.getArguments().getString("EndTime");
        eventId=this.getArguments().getString("EventId");
        isRegistered=this.getArguments().getBoolean("isregisterd");
        System.out.println("isreddfg"+isRegistered);
        if(isRegistered==true)
        {
            register.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            regId=this.getArguments().getString("registeredId");
            Qrcodelink=this.getArguments().getString("Qrcode");
            regdetails.setText("RegistrationId\n"+regId);
            Log.d("Qrcode in fragment",Qrcodelink);
            Picasso.with(getActivity()).load(Qrcodelink).into(qrcode);

        }
        eventvenue.setText(venue);
        eventdate.setText(startdate+":"+enddate);
        eventtime.setText(starttime + ":" + endtime);
        System.out.println("loc"+loc);
    //    getEventDetails();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                registerForEvent(myToken);
            }
        });
        return view;
    }
    private void registerForEvent(String accesstoken)
    {
        MyAppolloClient.getMyAppolloClient(accesstoken).mutate(EventRegistration.builder().email(emailId).userId(userId).
                eventID(eventId).build()).enqueue(new ApolloCall.Callback<EventRegistration.Data>() {
            @Override
            public void onResponse(@Nonnull Response<EventRegistration.Data> response) {
                status=response.data().eventRegistrartion_M().status();
                message=response.data().eventRegistrartion_M().message();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        regdetails.setText("RegistrationId\n"+response.data().eventRegistrartion_M().registrationId());
                        Picasso.with(getActivity()).load(response.data().eventRegistrartion_M().QRCodeLink()).into(qrcode);

                    }
                });
                if(status.equals("Failure")){
                    if(message.equals("Invalid token: access token is invalid")){

                        GetNewRefreshToken.getRefreshtoken(refreshToken,getActivity());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sharedPreferences = getActivity().getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                if (sharedPreferences.contains("authToken")) {
                                    String myToken = sharedPreferences.getString("authToken", "");
                                    registerForEvent(myToken);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }


}
