package com.attra.attralive.activity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Build;
import android.support.v4.app.Fragment;

import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.attra.attralive.Service.ApiService;
import com.attra.attralive.Service.MyAppolloClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import graphqlandroid.GetBusinessUnit;
import graphqlandroid.GetLocation;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import graphqlandroid.UserDetailsUpdate;


public class UserDetailsActivity extends AppCompatActivity {
    Spinner bu, location;
    CardView continueBtn;
      List<String> buList = new ArrayList<String>();
      List<String> locationList = new ArrayList<String>();

    Fragment fragment = null;

    ApiService apiService;

    OkHttpClient client;

    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;

    EditText postDescription;

    String status, message, path, description,myToken,username,userId;
    CardView uploadimage;

    ImageView fabCamera, capturedImage,upload;
    Bitmap mBitmap;
    TextView successMsg, Description;
    Button post;



    String emailId, password,userBu,designation,workLoc,mobile,employeeId;
    EditText empId, phNo, userDesign;
    String buValue,userName;
    private static ApolloClient apolloClient;
    public static final String PREFS_AUTH = "my_auth";
    public static String Authorization = "Basic YXBwbGljYXRpb246c2VjcmV0";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        userDesign = findViewById(R.id.et_designation);
        bu = findViewById(R.id.sp_selectbu);
        location = findViewById(R.id.sp_userWorkLocation);
        continueBtn = findViewById(R.id.crd_continuebutton);

        empId = findViewById(R.id.et_empId);

        phNo = findViewById(R.id.et_mobilenumber);
        uploadimage = findViewById(R.id.crd_upload);

        phNo = findViewById(R.id.et_mobilenumber);

        uploadimage=findViewById(R.id.crd_upload);

        upload = findViewById(R.id.im_profileimage);


        sharedPreferences = getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");

            userId = sharedPreferences.getString("userId", "");
            userName = sharedPreferences.getString("userName", "");

        }

        getUserBU();
        getUserLocation();


        askPermissions();
        initRetrofitClient();
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadProfileImage();

                onSelectImageClick(v);
                // multipartImageUpload();




            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = "Awnish";
                //String userId = "asd";

                String designation = userDesign.getText().toString();
                String workLoc = location.getSelectedItem().toString();
                String userBu = bu.getSelectedItem().toString();
                String mobile = phNo.getText().toString();
                String employeeId = empId.getText().toString();

                if (employeeId.trim().equals("")) {
                    empId.setError("Employee Id is required");
                    empId.requestFocus();
                } else if (designation.trim().equals("")) {
                    userDesign.setError("Designation is required");
                    userDesign.requestFocus();
                } else if (workLoc.trim().equals("")) {
                    ((TextView) location.getSelectedView()).setError("Select Location");
                    ((TextView) location.getSelectedView()).requestFocus();
                } else if (userBu.trim().equals("")) {
                    ((TextView) bu.getSelectedView()).setError("Select BU");
                    ((TextView) bu.getSelectedView()).requestFocus();
                } else if (mobile.length() < 10) {
                    phNo.setError("Enter valid Contact Number");
                    phNo.requestFocus();
                } else {
                    if(mBitmap!=null)
                        multipartImageUpload();
                    else
                    {
                        path="https://dsd8ltrb0t82s.cloudfront.net/ProfilePictures/1546848719271-image.jpeg";
                        CallSubmitDataService();
                    }
                    // Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                    // startActivity(intent1);


/*
                    Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent1);*/
                    MyAppolloClient.getMyAppolloClient(myToken).mutate(
                            UserDetailsUpdate.builder().userId(userId).name(userName).designation(designation).empId(employeeId).location(workLoc)
                                    .bu(userBu).mobileNumber(mobile).profileImagePath("asdasd")
                                    .build()).enqueue(
                            new ApolloCall.Callback<UserDetailsUpdate.Data>() {
                                @Override
                                public void onResponse(@Nonnull Response<UserDetailsUpdate.Data> response) {
//                                                String message= response.data().otpValidation_M().otpstatus();
                                    System.out.println("res_message in User" + response);
                                    String status = response.data().updateUserDetails_M().status();
                                    final String message = response.data().updateUserDetails_M().message();
                                    Log.d("res_message in User", message);
                                    // Log.d("res_status userDetails", status);
                                    if (status.equals("Success")) {
                                        Log.d("res_message in User", message);
                                        if(workLoc.equals("Bangalore")){
                                            subscribeToTopic(workLoc);
                                        }

                                        Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                                        startActivity(intent1);
                                    } else if (status.equals("Failure")) {
                                        // if(message.equals("")){
                                        Log.d("res_message in User ", message);

                                        // }
                                    }

                                }

                                @Override
                                public void onFailure(@Nonnull ApolloException e) {
                                }
                            }
                    );

                }


            }
        });
    }
    private void subscribeToTopic(String location){

        FirebaseMessaging.getInstance().subscribeToTopic(location)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                            Log.i("subscribed to topic"+""+location,msg);
                        }
                        //  Toast.makeText(DashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                picUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.iv_qrCode)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void getUserLocation(){
        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetLocation.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetLocation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetLocation.Data> response) {
                        Log.i("res", String.valueOf(response));
                        if(response.data().getLocations_Q().locations()!=null)
                        {
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


        //Log.i("token in user details",myToken);
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
    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    private void initRetrofitClient() {
        client         = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl("http://10.200.44.20:4001").client(client).build().create(ApiService.class);
    }









    private void multipartImageUpload() {

        try {
            //description = postDescription.getText().toString();

            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".jpeg");
            Log.i("multipartImageUpload","Inside this method");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), reqFile);
            // MultipartBody.Part body = MultipartBody.Part.createFormData("postPicture", file.getName(), reqFile);
            Log.i("",file.getName());


            RequestBody userId = createPartFromString("5c31e8f07db2e805e077c037");
            RequestBody type = createPartFromString("profilePicture");
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "profilePicture");
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("userId", userId);
            map.put("type", type);
            Call<ResponseBody> req = apiService.postImage(map, body);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    System.out.println("Image response"+ response);

                    if (response.code() == 200) {
                        System.out.println("Image response"+ response);

                        try {
                            String data = response.body().string();
                            System.out.println(data);

                            Map jsonJavaRootObject = new Gson().fromJson(data, Map.class);
                            //System.out.println(jsonJavaRootObject.get("status"));
                            status = jsonJavaRootObject.get("status").toString();
                            message = jsonJavaRootObject.get("message").toString();
                            path = jsonJavaRootObject.get("path").toString();

                            System.out.println(status+" " + message+" " + path);
                            CallSubmitDataService();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    Toast.makeText(getApplicationContext(), "Successfully updated" + " ", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("","Failure body");
//                    successMsg.setText("Uploaded Failed!");
//                    successMsg.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private RequestBody createPartFromString(String data) {
        return RequestBody.create(MultipartBody.FORM,data);
    }
    private void CallSubmitDataService()
    {
        MyAppolloClient.getMyAppolloClient("Bearer ae108cc309a817e3a05d8b7215c2e6242461eb78").mutate(
                UserDetailsUpdate.builder().userId("5c31e8f07db2e805e077c037").name(userName).designation(designation).empId(employeeId).location(workLoc)
                        .bu(userBu).mobileNumber(mobile).profileImagePath(path)
                        .build()).enqueue(
                new ApolloCall.Callback<UserDetailsUpdate.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<UserDetailsUpdate.Data> response) {
//                                                String message= response.data().otpValidation_M().otpstatus();
                        System.out.println("res_message in User"+ response);
                        String status = response.data().updateUserDetails_M().status();
                        final String message = response.data().updateUserDetails_M().message();
                        Log.d("res_message in User",message);
                        // Log.d("res_status userDetails", status);
                        if(status.equals("Success")){
                            Log.d("res_message in User", message);
                            Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(intent1);
                        } else if(status.equals("Failure")){
                            // if(message.equals("")){
                            Log.d("res_message in User ", message);

                            // }
                        }

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );
    }




}