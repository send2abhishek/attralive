package com.attra.attralive.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.ApiService;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.EditProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import de.hdodenhof.circleimageview.CircleImageView;
import graphqlandroid.GetProfileDetails;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    TextView username_view, password_view, DOB, gender, phone, email, submit, empId, location, bu,welcomeUserName;
    private SharedPreferences sharedPreferences;
    String myToken, userId, userName;
    public static final String PREFS_AUTH = "my_auth";
    CircleImageView profilePic;
    ImageView qrCode;
    List<String> buList = new ArrayList<String>();
    List<String> locationList = new ArrayList<String>();
    ApiService apiService;
    ArrayAdapter<String> locationAdapter;
    ArrayAdapter<String> userBuAdapter;
    OkHttpClient client;
    Fragment fragment = null;
    Uri picUri, outputFileUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    private final static int PIC_CROP = 2;
    Bitmap mBitmap;
    ImageView upload;
    Button submitdata;
    String status, message, path, refreshToken;
    String emailId, password, userBu, workLoc, mobile, employeeId;
    EditText phNo;

    public Profile() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            userId = sharedPreferences.getString("userId", "");
            userName = sharedPreferences.getString("userName", "");
            refreshToken = sharedPreferences.getString("refreshToken", "");
            Log.i("user id in userDtail", userId);

        }

        getActivity().setTitle(R.string.profile);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        empId = view.findViewById(R.id.et_empId);
        bu = view.findViewById(R.id.tv_userBu);
        phone = view.findViewById(R.id.et_mobilenumber);

        location = view.findViewById(R.id.tv_userWorkLocation);
        profilePic = view.findViewById(R.id.profileImage);
        submitdata = view.findViewById(R.id.editDetailsBtn);
        username_view = view.findViewById(R.id.et_userName);
        password_view = view.findViewById(R.id.et_password);
        welcomeUserName=view.findViewById(R.id.WelcomeUserName);
        getProfileDetail();
        empId.setEnabled(false);
        bu.setEnabled(false);
        phone.setEnabled(false);
        location.setEnabled(false);
        profilePic.setEnabled(false);
        username_view.setEnabled(false);
        password_view.setEnabled(false);
        submitdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getContext(), EditProfile.class);
                startActivity(intent1);
            }
        });

        return view;
    }


    private void getProfileDetail() {

        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetProfileDetails.builder().userId(userId)
                        .build()).enqueue(
                new ApolloCall.Callback<GetProfileDetails.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetProfileDetails.Data> response) {
                        if (response.data() != null && response.data().getProfileDetails_Q() != null) {
                            Log.i("res", String.valueOf(response));
                            String message = response.data().getProfileDetails_Q().message();
                            String status = response.data().getProfileDetails_Q().status();
                            Log.i("message in profile", message);
                            Log.i("mstatus in profile", status);
                            if (response.data().getProfileDetails_Q().name() != null) {
                                if (status.equals("Success")) {
                                    String username = response.data().getProfileDetails_Q().name();
                                    Log.i("username", username);
                                     String emaiId = response.data().getProfileDetails_Q().email();

                                    final String password = "*********";
                                    final String useNname = userName;
                                    String phoneNo = response.data().getProfileDetails_Q().mobileNumber();
                                    String loc = response.data().getProfileDetails_Q().location();
                                    String businessUnit = response.data().getProfileDetails_Q().bu();
                                    String imgPath = response.data().getProfileDetails_Q().profileImagePath();
                                    String emplyeeId = response.data().getProfileDetails_Q().empId();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Picasso.with(getActivity()).load(imgPath).fit().into(profilePic);
                                            if(userName!=null) {
                                                welcomeUserName.setText(userName);
                                            }else
                                            {
                                                welcomeUserName.setText("User!!");
                                            }
                                            if (emaiId != null) {
                                                username_view.setText(emaiId);
                                            }

                                                password_view.setText("********");
                                            phone.setText(phoneNo);
                                            empId.setText(emplyeeId);
                                            bu.setText(businessUnit);
                                            location.setText(loc);
                                            if (userBuAdapter != null && businessUnit != null) {
                                                bu.setText(businessUnit);

                                            }
                                            if (locationAdapter != null && loc != null) {
                                                location.setText(loc);
                                                Log.i("profile==>loc", "profile==>getprofiledetails==>loc" + loc + "loc position  " + locationAdapter.getPosition(loc));

                                            }

                                        }
                                    });
                                } else if (status.equals("Failure")) {

                                }

                            }


                        } else {
                            Log.i("Profile", "Profile==>getProfileDetails()==>onResponse null");
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }

                }
        );
    }
}