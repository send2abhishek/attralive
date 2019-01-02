package com.attra.attralive.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.fragment.DatePickerFragment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import graphqlandroid.GetBusinessUnit;
import graphqlandroid.GetLocation;

import static com.attra.attralive.activity.RegistrationActivity.MY_PREFS_NAME;

public class UserDetailsActivity extends AppCompatActivity {
    Spinner designation,bu,location;
    Button continueBtn;
    private static final String[] designList = {"Please select","Software Engineer","Project Lead", "Project Lead", "Project manager"};
    List<String> buList = new ArrayList<String>();
    List<String> locationList = new ArrayList<String>();
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    String emailId,password,authToken;
    String empId;
    String accessToken;
    static String authheader;
    String name;
    String designationValue ;
    String locationValue ;
    String buValue ;
    String phoneNumValue;
    String dobValue;
    String gender;
    public static String  authHeader= "Basic YXBwbGljYXRpb246c2VjcmV0";
    private static final String URL = "http://192.168.1.100/graphql/";
    private static ApolloClient apolloClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        designation = findViewById(R.id.sp_userDesignation);
        bu = findViewById(R.id.sp_userBU);
        location = findViewById(R.id.sp_userWorkLocation);
        continueBtn = findViewById(R.id.btn_continue);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        getUserBU();
        getUserLocation();

        ArrayAdapter<String>designationAdapter = new ArrayAdapter<String>(UserDetailsActivity.this,
                android.R.layout.simple_spinner_item,designList);

        designationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designation.setAdapter(designationAdapter);
        designation.setSelection(0);



        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("==========================MAHA=====");
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);
                gender= radioSexButton.getText().toString();
                designationValue = designation.getSelectedItem().toString();
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
        });




    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void getUserLocation(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            accessToken= prefs.getString("access_token",null);
            Log.i("token in user detail",accessToken);
        }

        String token="Bearer"+" "+accessToken;
        Log.i("brarer token",token);
        MyAppolloClient.getMyAppolloClient("Bearer 6fb3e5a2fe8040aacaf8290af36ad5920748be81").query(
                GetLocation.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetLocation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetLocation.Data> response) {
                        Log.i("res", String.valueOf(response));
                        for(int loopVar= 0; loopVar<response.data().getLocations_Q().locations().size(); loopVar++){
                           String  locationData= response.data().getLocations_Q().locations().get(loopVar);
                           locationList.add(locationData);
                            Log.i("location",locationData);

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
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            accessToken= prefs.getString("access_token",null);
            Log.i("token in user detail",accessToken);
        }

        String token="Bearer"+" "+accessToken;
        Log.i("brarer token",token);
        MyAppolloClient.getMyAppolloClient("Bearer eb60aab61fc96a8e22960b468710088f04c2feab").query(
                GetBusinessUnit.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetBusinessUnit.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetBusinessUnit.Data> response) {
                        Log.i("res", String.valueOf(response));
                        for(int loopVar= 0; loopVar<response.data().getBusinessUnits_Q().businessUnits().size(); loopVar++){
                            String  businessUnitData= response.data().getBusinessUnits_Q().businessUnits().get(loopVar);
                            buList.add(businessUnitData);
                            Log.i("location",businessUnitData);

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


}