package com.attra.attralive.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.squareup.picasso.Picasso;
import javax.annotation.Nonnull;
import graphqlandroid.GetProfileDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    TextView username, designation,DOB,gender,phone,email, submit,empId;
    Spinner workLocation,bu;
    private SharedPreferences sharedPreferences;
    String myToken,userId,userName;
    public static final String PREFS_AUTH = "my_auth";
    ImageView profilePic,qrCode;


    public Profile() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            userId = sharedPreferences.getString("userId","");
            userName = sharedPreferences.getString("userName","");
            Log.i("user id in userDtail",userId);

        }

        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.profile);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        empId = view.findViewById(R.id.et_empId);
       // username = view.findViewById(R.id.et_entername);
        designation=view.findViewById(R.id.et_designation);
        bu = view.findViewById(R.id.sp_selectbu);
        phone = view.findViewById(R.id.et_mobilenumber);
  //      email= view.findViewById(R.id.emailId);
        submit = view.findViewById(R.id.btn_submitDetails);
        workLocation = view.findViewById(R.id.sp_userWorkLocation);
        profilePic = view.findViewById(R.id.civ_profileimage);
        qrCode = view.findViewById(R.id.img_qrCode);
        getProfileDetail();
        return view;


    }

    private void getProfileDetail() {

        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetProfileDetails.builder().userId(userId)
                        .build()).enqueue(
                new ApolloCall.Callback<GetProfileDetails.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetProfileDetails.Data> response) {
                        Log.i("res", String.valueOf(response));
                        String message = response.data().getProfileDetails_Q().message();
                        String status = response.data().getProfileDetails_Q().status();
                        Log.i("message in profile", message);
                        Log.i("mstatus in profile", status);
                        if (response.data().getProfileDetails_Q().name() != null) {
                           /* String message = response.data().getProfileDetails_Q().message();
                            String status = response.data().getProfileDetails_Q().status();*/
                            if (status.equals("Success")) {
                                String username = response.data().getProfileDetails_Q().name();
                                String emaiId = response.data().getProfileDetails_Q().email();
                                String design = response.data().getProfileDetails_Q().designation();
                                String phoneNo= response.data().getProfileDetails_Q().mobileNumber();
                                String location = response.data().getProfileDetails_Q().location();
                                String businessUnit = response.data().getProfileDetails_Q().bu();
                                String imgPath = response.data().getProfileDetails_Q().profileImagePath();
                                String qrCodePath = response.data().getProfileDetails_Q().userQRCodeLink();
                                String emplyeeId = response.data().getProfileDetails_Q().empId();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(getActivity()).load(imgPath).fit().into(profilePic);
                                        Picasso.with(getActivity()).load(qrCodePath).fit().into(qrCode);
                                        designation.setText(design);
                                        phone.setText(phoneNo);
                                        empId.setText(emplyeeId);
                                        

                                    }
                                });
                            } else if (status.equals("Failure")) {

                            }

                        }


                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );
    }
}
