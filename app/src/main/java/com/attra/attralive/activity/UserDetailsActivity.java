package com.attra.attralive.activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Nonnull;
import graphqlandroid.GetBusinessUnit;
import graphqlandroid.GetLocation;

public class UserDetailsActivity extends AppCompatActivity {
    Spinner bu, location;
    CardView continueBtn;

    TextView dob;
      List<String> buList = new ArrayList<String>();
      List<String> locationList = new ArrayList<String>();
    private RadioGroup radioGroup;
    private RadioButton radioButton;



    String emailId, password;
    EditText empId, phNo, userDesign;
    String buValue,userName,userId;
    private static ApolloClient apolloClient;
    public static final String PREFS_AUTH = "my_auth";
    public static String Authorization = "Basic YXBwbGljYXRpb246c2VjcmV0";
    private SharedPreferences sharedPreferences;String myToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        userDesign = findViewById(R.id.et_designation);
        bu = findViewById(R.id.sp_selectbu);
        location = findViewById(R.id.sp_userWorkLocation);
        continueBtn = findViewById(R.id.crd_continuebutton);
        Intent intent = getIntent();
        emailId = intent.getStringExtra("emailId");
        password = intent.getStringExtra("password");
        userName=intent.getStringExtra("username");
        userId=intent.getStringExtra("userId");
        empId = findViewById(R.id.et_entername);
        phNo = findViewById(R.id.et_mobilenumber);


        sharedPreferences = getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
      //      Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

        }

        getUserBU();
        getUserLocation();
        /*sendDeviceToken();*/
        continueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String designation = userDesign.getText().toString();
                String workLoc = location.getSelectedItem().toString();
                String userBu = bu.getSelectedItem().toString();
                String mobile = phNo.getText().toString();
                String employeeId = empId.getText().toString();
                String imagePath = "wqeqeqweqe";

                int sid=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(sid);
                String gender = radioButton.getText().toString();


                if (employeeId.trim().equals("")) {
                    empId.setError("Employee Id is required");
                    empId.requestFocus();
                } else if (designation.trim().equals("")) {
                    userDesign.setError("Designation is required");
                    userDesign.requestFocus();
                } else if (workLoc.trim().equals("")) {
                    ((TextView) location.getSelectedView()).setError("Select Location");
                    ((TextView) location.getSelectedView()).requestFocus();
                }  else if (userBu.trim().equals("")) {
                    ((TextView) bu.getSelectedView()).setError("Select BU");
                    ((TextView) bu.getSelectedView()).requestFocus();
                } else if (mobile.length() < 10) {
                    phNo.setError("Enter valid Contact Number");
                    phNo.requestFocus();
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent1);
                    /*MyAppolloClient.getMyAppolloClient(myToken).mutate(
                            UserDetailsUpdate.builder().userId(userId).userName(userName).gender(gender).bu(buValue).designation(designation).dob(dobValue).empId(employeeId).location(workLoc)
                                    .bu(userBu).mobileNumber(mobile).profileImagePath(imagePath)
                                    .build()).enqueue(
                            new ApolloCall.Callback<UserDetailsUpdate.Data>() {
                                @Override
                                public void onResponse(@Nonnull Response<UserDetailsUpdate.Data> response) {
//                                                String message= response.data().otpValidation_M().otpstatus();
                                    String status = response.data().updateUserDetails_M().status();
                                    final String message = response.data().updateUserDetails_M().message();
                                    Log.d("res_message", message);
                                   // Log.d("res_status userDetails", status);
                                    Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivity(intent1);
                                    *//*UserDetailsActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (status.equals("Success")) {
                                                Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                                                startActivity(intent1);
                                            }
                                        }
                                    });*//*
                                }

                                @Override
                                public void onFailure(@Nonnull ApolloException e) {
                                }
                            }
                    );*/
                }


            }
        });


       /* continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("==========================MAHA=====");
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);
                gender= radioSexButton.getText().toString();
                locationValue = location.getSelectedItem().toString();
                buValue = bu.getSelectedItem().toString();

                Toast.makeText(UserDetailsActivity.this, ""+gender+"="+designationValue+"="+locationValue+"="+buValue, Toast.LENGTH_SHORT).show();
                System.out.println("===========================NANDI===="+gender+"="+designationValue+"="+locationValue+"="+buValue);
                if(gender==null){
                    Toast.makeText(UserDetailsActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }else if(designationValue.equals("Please select")){
                    Toast.makeText(UserDetailsActivity.this, "Please Select Designation", Toast.LENGTH_SHORT).show();
                }else if(locationValue.equals("Please select")){
                    Toast.makeText(UserDetailsActivity.this, "Please Select Location", Toast.LENGTH_SHORT).show();
                }else if(buValue.equals("Please select")){
                    Toast.makeText(UserDetailsActivity.this, "Please Select BU", Toast.LENGTH_SHORT).show();
                }else if(designationValue==null){
                    Toast.makeText(UserDetailsActivity.this, "Please Select Designation", Toast.LENGTH_SHORT).show();
                }else if(designationValue==null){
                    Toast.makeText(UserDetailsActivity.this, "Please Select Designation", Toast.LENGTH_SHORT).show();
                }
                else {


                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                }
            }
        });*/


    }


    private void getUserLocation(){
        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetLocation.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetLocation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetLocation.Data> response) {
                        Log.i("res", String.valueOf(response));
                        if(response.data().getLocations_Q().locations()!=null){
                        for(int loopVar= 0; loopVar<response.data().getLocations_Q().locations().size(); loopVar++) {
                            String locationData = response.data().getLocations_Q().locations().get(loopVar);
                            locationList.add(locationData);
                            Log.i("location", locationData);
                        }
                        }

                        UserDetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(UserDetailsActivity.this,
                                        android.R.layout.simple_spinner_item,locationList);
                                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                location.setAdapter(locationAdapter);
                                location.setSelection(0);
                            }
                        });

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }

     private void getUserBU(){


        Log.i("token in user details",myToken);
        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetBusinessUnit.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetBusinessUnit.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetBusinessUnit.Data> response) {
                        Log.i("res", String.valueOf(response));
                        if(response.data().getBusinessUnits_Q().businessUnits()!=null) {
                            for (int loopVar = 0; loopVar < response.data().getBusinessUnits_Q().businessUnits().size(); loopVar++) {
                                String businessUnitData = response.data().getBusinessUnits_Q().businessUnits().get(loopVar);
                                buList.add(businessUnitData);
                                Log.i("location", businessUnitData);

                            }
                        }
                        UserDetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> userBuAdapter = new ArrayAdapter<>(UserDetailsActivity.this,
                                        android.R.layout.simple_spinner_item,buList);

                                userBuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                bu.setAdapter(userBuAdapter);
                                bu.setSelection(0);
                            }
                        });

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }


}