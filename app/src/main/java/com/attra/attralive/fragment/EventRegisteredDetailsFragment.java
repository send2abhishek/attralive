package com.attra.attralive.fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    String eventtitle,venue,startdate,enddate,starttime,endtime,description,status,message,eventId,isRegistered;
    TextView eventvenue,eventdate,eventtime;
    CardView register;
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
        register=view.findViewById(R.id.crd_regevent);
        regdetails=view.findViewById(R.id.tv_regid);
        qrcode=view.findViewById(R.id.im_qrcode);
        linearLayout=view.findViewById(R.id.ll_qrcode);
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
        isRegistered=this.getArguments().getString("isRegistered");
        if(isRegistered.equals("true"))
        {
            register.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            regId=this.getArguments().getString("RegistrationId");
            Qrcodelink=this.getArguments().getString("QRcodelink");
            regdetails.setText("RegistrationId\n"+regId);
            Picasso.with(getActivity()).load(Qrcodelink).into(qrcode);
        }
        System.out.println("loc"+loc);
        getEventDetails();
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
    private void getEventDetails()
    {
        MyAppolloClient.getMyAppolloClient("Bearer d3b928d44fcacd522f26e829d3fbdb6e483f2d9e").
                query(GetEventDetails.builder().status("A").location(loc).build()).enqueue(new ApolloCall.Callback<GetEventDetails.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetEventDetails.Data> response) {

            /*eventtitle=response.data().getEventDetails_Q().eventD().get(0).event_title();
            description=response.data().getEventDetails_Q().eventD().get(0).Description();
            venue=response.data().getEventDetails_Q().eventD().get(0).venue();
            startdate=response.data().getEventDetails_Q().eventD().get(0).Schedule().start_date();
            starttime=response.data().getEventDetails_Q().eventD().get(0).Schedule().start_time();
            enddate=response.data().getEventDetails_Q().eventD().get(0).Schedule().end_date();
            endtime=response.data().getEventDetails_Q().eventD().get(0).Schedule().end_time();*/
            /*
            }*/
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for(int i=0;i<response.data().getEventDetails_Q().eventD().get(0).registeredUsers().size();i++) {
                        reguserId = response.data().getEventDetails_Q().eventD().get(0).registeredUsers().get(i).userId();
                        if (reguserId.equals(userId)) {
                            register.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            //regdetails.setText(response.data().getEventDetails_Q().eventD().get(0).registeredUsers().get(i).
                            //registrationId());
                            regdetails.setText("rrrr");
                           // Picasso.with(getActivity()).load("https://dsd8ltrb0t82s.cloudfront.net/NewsFeedsPictures/1546607539810-ic_launcher.png").into(qrcode);
                        }
                   }
                    eventvenue.setText("asd");
                    eventdate.setText("gg");
                    eventtime.setText("oo" + ":" + "nn");
                }
            });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

}
