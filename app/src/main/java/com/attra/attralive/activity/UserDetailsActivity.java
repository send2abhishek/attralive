package com.attra.attralive.activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nonnull;

import graphqlandroid.GetBusinessUnit;
import graphqlandroid.GetLocation;
import graphqlandroid.OtpValidation;
import graphqlandroid.SendDeviceToken;
import graphqlandroid.UserDetailsUpdate;


public class UserDetailsActivity extends AppCompatActivity {
    Spinner bu,location;
    CardView continueBtn;
    TextView dob;
  /*  List<String> buList = new ArrayList<String>();
    List<String> locationList = new ArrayList<String>();*/
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Button submitDetails;
    String emailId,password,authToken;
    EditText empId,phNo,userDesign;
    String accessToken;
    static String authheader;
    String name;
    String designationValue ;
    String locationValue ;
    String buValue ;
    String phoneNumValue;
    String dobValue;
    String gender;
    String token="";
    String refreshToken;
    public static String  authHeader= "Basic YXBwbGljYXRpb246c2VjcmV0";
    private static final String URL = "http://192.168.1.100/graphql/";
    private static ApolloClient apolloClient;
    public static final String PREFS_AUTH ="my_auth";
    public static String  Authorization= "Basic YXBwbGljYXRpb246c2VjcmV0";
    private static final String[] buList = {"Please select","Synchrony","Practice", "APAC", "Corporate"};
    private static final String[] locationList = {"Please select","Banalore","Practice", "APAC", "Corporate"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        userDesign = findViewById(R.id.et_designation);
        bu = findViewById(R.id.sp_selectbu);
        location = findViewById(R.id.sp_userWorkLocation);
        continueBtn = findViewById(R.id.crd_regbutton);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        dob=findViewById(R.id.tv_userDob);
        Intent intent = getIntent();
        emailId = intent.getStringExtra("emailId");
        password = intent.getStringExtra("password");
        empId = findViewById(R.id.et_entername);
        phNo = findViewById(R.id.et_mobilenumber);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(UserDetailsActivity.this,
                android.R.layout.simple_spinner_item,locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(locationAdapter);
        location.setSelection(0);

        ArrayAdapter<String> userBuAdapter = new ArrayAdapter<>(UserDetailsActivity.this,
                android.R.layout.simple_spinner_item,buList);

        userBuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bu.setAdapter(userBuAdapter);
        bu.setSelection(0);


      /*  SharedPreferences sp = getSharedPreferences(PREFS_AUTH,Context.MODE_PRIVATE);
        token = sp.getString(authToken,"");
        Log.i("token in UserDetails",token);*/
     /*   getUserBU();
        getUserLocation();
*/
        continueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName="Awnish";
                String userId = "asd";
                String designation = userDesign.getText().toString();
                String dobValue = dob.getText().toString();
                String workLoc = location.getSelectedItem().toString();
                String userBu = bu.getSelectedItem().toString();
                String mobile = phNo.getText().toString();
                String employeeId = empId.getText().toString();
                String imagePath = "wqeqeqweqe";
                String gender = "Male";

                if(employeeId.trim().equals("")){
                    empId.setError("Employee Id is required");
                    empId.requestFocus();
                }
                else if(designation.trim().equals(""))
                {
                    userDesign.setError("Designation is required");
                    userDesign.requestFocus();
                }
                else if (workLoc.trim().equals("")) {
                    ((TextView)location.getSelectedView()).setError("Select Location");
                    ((TextView)location.getSelectedView()).requestFocus();
                }
                else if (dobValue.trim().equals("")){
                    dob.setError("Dob is required");
                    dob.requestFocus();
                }
                else if (userBu.trim().equals("")) {
                    ((TextView)bu.getSelectedView()).setError("Select BU");
                    ((TextView)bu.getSelectedView()).requestFocus();
                }
                else if(mobile.length()<10)
                {
                    phNo.setError("Enter valid Contact Number");
                    phNo.requestFocus();
                }
                else
                {
                    MyAppolloClient.getMyAppolloClient("Bearer ae56400f9ab034a2b21366eb86bc0561e1a3739c").mutate(
                            UserDetailsUpdate.builder().userId(userId).userName(userName).gender(gender).bu(buValue).designation(designation).dob(dobValue).empId(employeeId).location(workLoc)
                                    .bu(userBu).mobileNumber(mobile).profileImagePath(imagePath)
                                    .build()).enqueue(
                            new ApolloCall.Callback<UserDetailsUpdate.Data>() {
                                @Override
                                public void onResponse(@Nonnull Response<UserDetailsUpdate.Data> response) {
//                                                String message= response.data().otpValidation_M().otpstatus();
                                    final String status = response.data().updateUserDetails_M().status();
                                    final String message = response.data().updateUserDetails_M().message();
                                    Log.i("res_message",message);
                                    Log.i("res_status userDetails",status);

                                    UserDetailsActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(status.equals("Success")){
                                             Intent intent1 = new Intent(getApplicationContext(),DashboardActivity.class);
                                             startActivity(intent1);
                                            }
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
    public void showDatePickerDialog(View v) {
        /*DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        */
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(UserDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                dob.setText(date);
               // tfDate.setText(date);
            }
        }, yy, mm, dd);
        datePicker.show();
    }

   /* private void getUserLocation(){
        MyAppolloClient.getMyAppolloClient("ae56400f9ab034a2b21366eb86bc0561e1a3739c").query(
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

    }*/

   /* private void getUserBU(){
       *//* SharedPreferences sp = getSharedPreferences(PREFS_AUTH,Context.MODE_PRIVATE);
        String token = sp.getString(authToken,"");
*//*
        Log.i("token in user details",token);
        MyAppolloClient.getMyAppolloClient("ae56400f9ab034a2b21366eb86bc0561e1a3739c").query(
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

*/
/*  public static ApolloClient getToken(){
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

      OkHttpClient okHttpClient = new OkHttpClient.Builder()
              .addInterceptor(chain -> {
                  Request original = chain.request();
                  Request.Builder builder = original.newBuilder().method(original.method(), original.body());
                  builder.header("Authorization", "JWT" + " " +  authheader);
                  Log.d("AUTH_TAG", authheader);
                  return chain.proceed(builder.build());
              })
              .build();

      apolloClient = ApolloClient.builder()
              .serverUrl(URL)
              .okHttpClient(okHttpClient)
              .build();

      return apolloClient;

    }*/


    private void sendDeviceToken(){

        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        MyAppolloClient.getMyAppolloClient(token).mutate(
                SendDeviceToken.builder().userId(emailId)
                        .deviceId(deviceToken)
                        .build()).enqueue(
                new ApolloCall.Callback<SendDeviceToken.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<SendDeviceToken.Data> response) {
                        String message= response.data().registerDeviceId_M().message();
                        final String status = response.data().registerDeviceId_M().status();
                        Log.i("res_message",message);

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }

   /* public void getToken(){

        MyAppolloClient.getMyAppolloClient(Authorization).query(
                UserLoginAuth.builder().username(emailId).password(password)
                        .build()).enqueue(
                new ApolloCall.Callback<UserLoginAuth.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<UserLoginAuth.Data> response) {
                        accessToken= response.data().userLoginAuth_Q().accessToken();
                        String tokenExpiry = response.data().userLoginAuth_Q().accessTokenExpiresAt();
                        refreshToken = response.data().userLoginAuth_Q().accessToken();
                        String refreshTokenExpiry = response.data().userLoginAuth_Q().accessTokenExpiresAt();
                        String user = response.data().userLoginAuth_Q().user();
                        String message = response.data().userLoginAuth_Q().message();
                        String userName = response.data().userLoginAuth_Q().name();
                        String status = response.data().userLoginAuth_Q().status();
                        Log.i("access Token",accessToken);
                        authToken="Bearer"+" "+accessToken;
                        Log.i("brarer token",authToken);
                        if(status.equals("success")){

                            SharedPreferences  preferences = getApplicationContext().getSharedPreferences(PREFS_AUTH, 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("authToken",authToken);
                            editor.commit();

                        }else if(status.equals("Failure")){
                            if(message.equals("Invalid token: access token has expired")){
                                getNewRefreshToken(refreshToken);
                            }

                        }

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }


    private void getNewRefreshToken(String refreshToken){
        MyAppolloClient.getMyAppolloClient(Authorization).query(
                GetRefreshToken.builder().refreshToken(refreshToken).grant_type("refresh_token")
                        .build()).enqueue(
                new ApolloCall.Callback<GetRefreshToken.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetRefreshToken.Data> response) {
                        String message = response.data().userLoginAuth_Q().message();
                        String status = response.data().userLoginAuth_Q().status();
                        if(status.equals("success")){
                            accessToken= response.data().userLoginAuth_Q().accessToken();
                            String tokenExpiry = response.data().userLoginAuth_Q().accessTokenExpiresAt();
                            String newRefreshToken = response.data().userLoginAuth_Q().accessToken();
                            String refreshTokenExpiry = response.data().userLoginAuth_Q().accessTokenExpiresAt();
                            String user = response.data().userLoginAuth_Q().user();
                            String userName = response.data().userLoginAuth_Q().name();
                            Log.i("access Token",accessToken);
                            authToken="Bearer"+" "+accessToken;
                            Log.i("brarer token",authToken);
                            SharedPreferences sp = getSharedPreferences("your_shared_pref_name", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("access_token",accessToken);
                            editor.putString("refreshToken",newRefreshToken);
                            editor.putString("emailId",emailId);
                            editor.putString("password",password);
                            editor.apply();

                        }


                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }*/
}