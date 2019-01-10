package com.attra.attralive.activity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
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
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import android.graphics.Bitmap;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.attra.attralive.util.GetNewRefreshToken;
import com.google.gson.Gson;

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

    TextView dob;
    List<String> buList = new ArrayList<String>();
    List<String> locationList = new ArrayList<String>();
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    ApiService apiService;

    OkHttpClient client;



    Fragment fragment = null;


    Uri picUri,outputFileUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    private final static int PIC_CROP = 2;



    EditText postDescription;

    String status, message, path, description,myToken,username,userId,refreshToken;
    CardView uploadimage;

    ImageView fabCamera, capturedImage,upload;
    Bitmap mBitmap;
    TextView successMsg, Description;
    Button post;



    String emailId, password,userBu,designation,workLoc,mobile,employeeId;
    EditText empId, phNo, userDesign;
    String buValue,userName;
    private static ApolloClient apolloClient;
    //
    public static String Authorization = "Basic YXBwbGljYXRpb246c2VjcmV0";

    private SharedPreferences sharedPreferences;



   /* private SharedPreferences sharedPreferences;
    String myToken;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        userDesign = findViewById(R.id.et_designation);
        bu = findViewById(R.id.sp_selectbu);
        location = findViewById(R.id.sp_userWorkLocation);
        continueBtn = findViewById(R.id.crd_continuebutton);
        // Intent intent = getIntent();
        // emailId = intent.getStringExtra("emailId");
        // password = intent.getStringExtra("password");

        empId = findViewById(R.id.et_empId);

        // userName=intent.getStringExtra("username");
        // userId=intent.getStringExtra("userId");
        //empId = findViewById(R.id.et_entername);
        phNo = findViewById(R.id.et_mobilenumber);

        uploadimage=findViewById(R.id.crd_upload);

        upload = findViewById(R.id.im_profileimage);



        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            refreshToken=sharedPreferences.getString("refreshToken","");
            userId = sharedPreferences.getString("userId","");
            userName = sharedPreferences.getString("userName","");
            Log.i("user id in userDtail",userId);
            Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

        }

       getUserBU(myToken);
      getUserLocation(myToken);
        /*sendDeviceToken();*/

        //postDescription = findViewById(R.id.descText);


        // capturedImage = findViewById(R.id.capturedImage);
//        fabCamera = findViewById(R.id.openCameraOptions);
//        post = findViewById(R.id.btn_postnewsFeed);
        //fabCamera.setOnClickListener(this);
        // post.setOnClickListener(this);


        askPermissions();
        initRetrofitClient();
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadProfileImage();
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                // multipartImageUpload();




            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                designation = userDesign.getText().toString();
               // workLoc = location.getSelectedItem().toString();
               // userBu = bu.getSelectedItem().toString();
                mobile = phNo.getText().toString();
                employeeId = empId.getText().toString();

                /*int sid=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(sid);
                String gender = radioButton.getText().toString();*/


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
                    if(mBitmap!=null)
                        multipartImageUpload();
                    else
                    {
                        path="https://dsd8ltrb0t82s.cloudfront.net/ProfilePictures/1546848719271-image.jpeg";
                        CallSubmitDataService(myToken);
                    }
                    // Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                    // startActivity(intent1);

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


    private void getUserLocation(String accesstoken){
        MyAppolloClient.getMyAppolloClient(accesstoken).query(
                GetLocation.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetLocation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetLocation.Data> response) {
                        Log.i("res", String.valueOf(response));
                        String Status=response.data().getLocations_Q().status();
                        String Message=response.data().getLocations_Q().message();
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
                        if(Status.equals("Failure")){

                            if(Message.equals("Invalid token: access token is invalid")){

                                GetNewRefreshToken.getRefreshtoken(refreshToken,UserDetailsActivity.this);
                                UserDetailsActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                        if (sharedPreferences.contains("authToken")) {
                                            String myToken = sharedPreferences.getString("authToken", "");
                                            getUserLocation(myToken);
                                            Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }

    private void getUserBU(String accesstoken){


        //Log.i("token in user details",myToken);
        MyAppolloClient.getMyAppolloClient(accesstoken).query(
                GetBusinessUnit.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetBusinessUnit.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetBusinessUnit.Data> response) {
                        Log.i("res", String.valueOf(response));
                        String Status=response.data().getBusinessUnits_Q().status();
                        String Message=response.data().getBusinessUnits_Q().message();
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
                         if(Status.equals("Failure")){
                            if(Message.equals("Invalid token: access token is invalid")){

                                GetNewRefreshToken.getRefreshtoken(refreshToken,UserDetailsActivity.this);
                                UserDetailsActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                        if (sharedPreferences.contains("authToken")) {
                                            String myToken = sharedPreferences.getString("authToken", "");
                                            getUserBU(myToken);
                                            Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }
                        }
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

        apiService = new Retrofit.Builder().baseUrl("http://10.200.44.25:4001").client(client).build().create(ApiService.class);
    }
    public Intent getPickImageChooserIntent() {

         outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery)
        {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }
    private Uri
    getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.jpeg"));
        }
        return outputFileUri;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {

            //capturedImage = findViewById(R.id.im_profileimage);

            if (requestCode == IMAGE_RESULT) {


                String filePath = getImageFilePath(data);
                //cropImage();
                System.out.println("file path"+filePath);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    upload.setImageDrawable(null);
                    upload.setImageBitmap(mBitmap);

                }
            }
            else if(requestCode==PIC_CROP)
            {
               Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");

                /*String filePath = getImageFilePath(data);
                System.out.println("file path"+filePath);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    upload.setImageDrawable(null);
                    upload.setImageBitmap(mBitmap);

                }*/
            }

        }

    }
    private void cropImage()
    {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(outputFileUri, "image/*");

            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        picUri = savedInstanceState.getParcelable("pic_uri");
    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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


            RequestBody usrId = createPartFromString(userId);
            RequestBody type = createPartFromString("profilePicture");
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "profilePicture");
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("userId", usrId);
            map.put("type", type);
            Call<ResponseBody> req = apiService.postImage(map, body);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    System.out.println("Image response"+ response);

                    if (response.code() == 200) {
//                        successMsg.setText("Uploaded Successfully!");
//                        successMsg.setTextColor(Color.BLUE);
//
                        System.out.println("Image response"+ response);

                        /*org.json.simple.JSONObject jsonObj = null;
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        try {
                            String data = response.body().string();
                            System.out.println(data);

                            Map jsonJavaRootObject = new Gson().fromJson(data, Map.class);
                            //System.out.println(jsonJavaRootObject.get("status"));
                            status = jsonJavaRootObject.get("status").toString();
                            message = jsonJavaRootObject.get("message").toString();
                            path = jsonJavaRootObject.get("path").toString();

                            System.out.println(status+" " + message+" " + path);
                            CallSubmitDataService(myToken);


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
    private void CallSubmitDataService(String accesstoken)
    {
       // Log.d("accesstoken",accesstoken);
        MyAppolloClient.getMyAppolloClient(accesstoken).mutate(
                UserDetailsUpdate.builder().userId(userId).name(userName).designation(designation).empId(employeeId).location(workLoc)
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
                            intent1.putExtra("location",workLoc);
                            startActivity(intent1);
                        } else if(status.equals("Failure")){
                               if(message.equals("Invalid token: access token is invalid")){

                            GetNewRefreshToken.getRefreshtoken(refreshToken,UserDetailsActivity.this);
                            UserDetailsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                    if (sharedPreferences.contains("authToken")) {
                                        String myToken = sharedPreferences.getString("authToken", "");
                                        CallSubmitDataService(myToken);
                                        Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
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