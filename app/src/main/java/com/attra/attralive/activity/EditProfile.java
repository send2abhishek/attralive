package com.attra.attralive.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.attra.attralive.util.GetNewRefreshToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import fr.ganfra.materialspinner.MaterialSpinner;
import graphqlandroid.GetBusinessUnit;
import graphqlandroid.GetLocation;
import graphqlandroid.GetProfileDetails;
import graphqlandroid.UserDetailsUpdate;
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

public class EditProfile extends AppCompatActivity {
    MaterialSpinner location, bu;
    Button continueBtn, cancelButton;

    TextView dob, welcomeUserName;
    List<String> buList = new ArrayList<String>();
    List<String> locationList = new ArrayList<String>();
    ArrayAdapter<String> locationAdapter;
    ArrayAdapter<String> userBuAdapter;
    ApiService apiService;
    OkHttpClient client;
    TextView userNameView, passwordView, changePassword;
    Fragment fragment = null;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    String status, message, path, description, myToken, username, userId;
    ImageView fabCamera, capturedImage, upload;
    Bitmap mBitmap;
    String password, userBu, designation, workLoc, mobile, employeeId;
    EditText empId, phNo, userDesign;
    String userName;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_detail);
        userDesign = findViewById(R.id.et_designation);
        bu = findViewById(R.id.sp_selectbu);
        location = findViewById(R.id.sp_userWorkLocation);
        continueBtn = findViewById(R.id.updateBtn);
        empId = findViewById(R.id.et_empId);
        phNo = findViewById(R.id.et_mobilenumber);
        upload = findViewById(R.id.profileImage);
        userNameView = findViewById(R.id.et_username);
        passwordView = findViewById(R.id.et_password);
        changePassword = findViewById(R.id.changePassword);
        cancelButton = findViewById(R.id.cancelBtn);
        welcomeUserName = findViewById(R.id.WelcomeUserName);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        getSupportActionBar().setTitle(R.string.update_profile);

        sharedPreferences = getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            Toast.makeText(this, "Shared pref val " + "sharedPreferences.getString(\"authToken\", \"\")", Toast.LENGTH_SHORT).show();
            myToken = sharedPreferences.getString("authToken", "");
            userId = sharedPreferences.getString("userId", "");
            userName = sharedPreferences.getString("userName", "");
            Log.i("user id in userDtail", userId);
            Toast.makeText(getApplicationContext(), myToken, Toast.LENGTH_LONG).show();
            String username = sharedPreferences.getString("userName", "");
        }
        if (userName != null) {
            userNameView.setText(userName);
        } else {
            Log.i("userId in shared pref", "UserID in shared pref is null");
        }
        //empId.setText("322356");
        passwordView.setText("**********************");
        userNameView.setEnabled(false);
        empId.setEnabled(false);
        getUserBU("0");
          getUserLocation("0");
        getProfileDetail();

        askPermissions();
        initRetrofitClient();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                onSelectImageClick(v);


            }
        });

        if (changePassword != null) {
            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(this, "changePassword is null", Toast.LENGTH_SHORT).show();
        }
        if (cancelButton != null) {
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent intent = new Intent(getApplicationContext(), Profile.class);
                    startActivity(intent);*/

                    finish();
                }
            });
        }
        if (continueBtn != null)
            continueBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    designation = userDesign.getText().toString();
                    workLoc = location.getSelectedItem().toString();
                    userBu = bu.getSelectedItem().toString();
                    mobile = phNo.getText().toString();
                    employeeId = empId.getText().toString();


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
                        if (mBitmap != null) {
                            Log.i("mBitmap", mBitmap + "");
                            multipartImageUpload();
                        } else {
                            path = "https://dsd8ltrb0t82s.cloudfront.net/ProfilePictures/1546848719271-image.jpeg";
                            CallSubmitDataService();
                        }

                    }


                }
            });
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
                                    String design = response.data().getProfileDetails_Q().designation();
                                    String phoneNo = response.data().getProfileDetails_Q().mobileNumber();
                                    String loc = response.data().getProfileDetails_Q().location();
                                    String businessUnit = response.data().getProfileDetails_Q().bu();
                                    String imgPath = response.data().getProfileDetails_Q().profileImagePath();
                                    //  String qrCodePath = response.data().getProfileDetails_Q().userQRCodeLink();
                                    String emailId = response.data().getProfileDetails_Q().email();
                                    EditProfile.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Picasso.with(getApplicationContext()).load(imgPath).fit().into(upload);
                                            userDesign.setText(design);
                                            //   Picasso.with(getActivity()).load(qrCodePath).fit().into(qrCode);
                                            // String split=emailId.spli
                                            /*if(userName!=null)
                                            welcomeUserName.setText(userName);
                                            else
                                                welcomeUserName.setText("User!!!!");*/
                                            welcomeUserName.setText("Awnish Kumar");
                                            if (emailId != null) {
                                                userNameView.setText(emailId);
                                            }
                                            if (password != null) {
                                                passwordView.setText("**********");
                                            }
                                            phNo.setText(phoneNo);
                                            if (response.data().getProfileDetails_Q().empId() != null) {
                                                String emplyeeId = response.data().getProfileDetails_Q().empId();
                                                empId.setText(emplyeeId);
                                            } else {
                                                Log.i("Employee id", "Profile==>getProfileDetails==>Employee id is null");
                                            }

                                            /*if(userBuAdapter!=null &&businessUnit!=null) {
                                                Log.i("BU spinner","EditProfile==>getprofileDetails="+businessUnit);
                                                 bu.setAdapter(userBuAdapter);
                                                 bu.setSelection( userBuAdapter.getPosition(businessUnit));
                                            }
                                            else
                                            {
                                                Log.i("Bu Spinner","EditProfile==>getprofileDetails= either bu or adpater is null");
                                            }
                                            if(locationAdapter!=null && loc!=null)
                                            {
                                                location.setAdapter(locationAdapter);
                                               location.setSelection( locationAdapter.getPosition(loc));
                                                Log.i("Location spinner","EditProfile==>getprofileDetails="+loc);
                                            }else
                                            {
                                                Log.i("Location Spinner","EditProfile==>getprofileDetails= either loc or adpater is null");
                                            }*/
                                            //location.setText(loc);
                                           /* if (userBuAdapter != null && businessUnit != null) {
                                                bu.setText(businessUnit);
                                                // bu.setSelection(userBuAdapter.getPosition(businessUnit));
                                                Toast.makeText(getContext(), "location position is ==" + userBuAdapter.getPosition(loc), Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(getContext(), "user bu null == ", Toast.LENGTH_SHORT).show();
                                            //  bu.setSelection(1);
                                            if (locationAdapter != null && loc != null) {
                                                location.setText(loc);
                                                Log.i("profile==>loc","profile==>getprofiledetails==>loc"+loc+ "loc position  "+locationAdapter.getPosition(loc));
                                                // location.setSelection(locationAdapter.getPosition(loc));
                                                Toast.makeText(getContext(), "location position is ==" + locationAdapter.getPosition(loc), Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(getContext(), "user location null == ", Toast.LENGTH_SHORT).show();*/
                                            if (loc != null) {
                                                getUserLocation(loc);
                                            } else {
                                                Log.i("Location edit_ptofile", "location is null");
                                            }
                                            if (businessUnit != null) {
                                                getUserBU(businessUnit);
                                            } else {
                                                Log.i("bu edit_ptofile", "bu is null");
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

    public void onSelectImageClick(View view) {
        Log.i("onselectimageclick", "onselectimageclick");
        CropImage.startPickImageActivity(this);
    }

    private void startCropImageActivity(Uri imageUri) {
        Log.i("startCropImageActivity", "startCropImageActivity");
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


    private void getUserLocation(String loc) {
        Log.i("locaion", "getUserLocation==>loc " + loc);
        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetLocation.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetLocation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetLocation.Data> response) {
                        Log.i("res", String.valueOf(response));
                        if (response.data().getLocations_Q().locations() != null) {
                            for (int loopVar = 0; loopVar < response.data().getLocations_Q().locations().size(); loopVar++) {
                                String locationData = response.data().getLocations_Q().locations().get(loopVar);
                                locationList.add(locationData);
                                Log.i("location", locationData);
                            }
                        }

                        EditProfile.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                locationAdapter = new ArrayAdapter<>(EditProfile.this,
                                        android.R.layout.simple_spinner_item, locationList);
                                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                if(loc.equals("0"))
                                {
                                    location.setAdapter(locationAdapter);
                                    location.setSelection(Integer.parseInt(loc));
                                    location.setEnableFloatingLabel(true);
                                }
                                location.setAdapter(locationAdapter);
                                Log.i("loc position", "" + locationAdapter.getPosition(loc));
                                int locPos = locationAdapter.getPosition(loc) + 1;
                                location.setSelection(locPos);
                                location.setEnableFloatingLabel(true);
                            }
                        });

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                    }
                }
        );

    }

    private void getUserBU(String businessUnit) {


        Log.i("businessUnit", "getUserBU==>businessUnit " + businessUnit);
        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetBusinessUnit.builder()
                        .build()).enqueue(
                new ApolloCall.Callback<GetBusinessUnit.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetBusinessUnit.Data> response) {
                        Log.i("res", String.valueOf(response));
                        if (response.data().getBusinessUnits_Q().businessUnits() != null) {
                            for (int loopVar = 0; loopVar < response.data().getBusinessUnits_Q().businessUnits().size(); loopVar++) {
                                String businessUnitData = response.data().getBusinessUnits_Q().businessUnits().get(loopVar);
                                buList.add(businessUnitData);
                                Log.i("location", businessUnitData);

                            }
                        }
                        userBuAdapter = new ArrayAdapter<>(EditProfile.this,
                                android.R.layout.simple_spinner_item, buList);

                        userBuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        EditProfile.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (businessUnit.equals("0")) {
                                    bu.setAdapter(userBuAdapter);
                                    bu.setSelection(Integer.parseInt(businessUnit));
                                    location.setEnableFloatingLabel(true);
                                } else {
                                    int buPos = userBuAdapter.getPosition(businessUnit) + 1;
                                    Log.i("bu position", "" + userBuAdapter.getPosition(businessUnit));
                                    bu.setAdapter(userBuAdapter);
                                    bu.setSelection(buPos);
                                    /*  location.setHint("Select an item");*/
                                    location.setEnableFloatingLabel(true);
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
        Log.i("initRetrofitClient", "initRetrofitClient");
        client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl("http://13.232.225.201:80").client(client).build().create(ApiService.class);
    }

    //    public Intent getPickImageChooserIntent() {
//        Log.i("getPickImageChooser","getPickImageChooserIntent");
//        Uri outputFileUri = getCaptureImageOutputUri();
//
//        List<Intent> allIntents = new ArrayList<>();
//        PackageManager packageManager = getPackageManager();
//
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }
//
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//        for (ResolveInfo res : listGallery) {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            allIntents.add(intent);
//        }
//
//        Intent mainIntent = allIntents.get(allIntents.size() - 1);
//        for (Intent intent : allIntents) {
//            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
//                mainIntent = intent;
//                break;
//            }
//        }
//        allIntents.remove(mainIntent);
//
//        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
//
//        return chooserIntent;
//    }
//    @RequiresApi(api = Build.VERSION_CODES.FROYO)
//    private Uri getCaptureImageOutputUri() {
//        Log.i("getCaptureImage","getCaptureImageOutputUri");
//        Uri outputFileUri = null;
//        File getImage = getExternalFilesDir("");
//        if (getImage != null) {
//            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.jpeg"));
//        }
//        return outputFileUri;
//    }
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", "onActivityResult");
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
                ((ImageView) findViewById(R.id.profileImage)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public String getImageFilePath(Intent data) {

        Log.i("getImageFilePath", "getImageFilePath");
        return getImageFromFilePath(data);
    }

    private String getImageFromFilePath(Intent data) {
        Log.i("getImageFromFilePath", "getImageFromFilePath");
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.jpeg"));
        }
        return outputFileUri;
    }


    private String getPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    private void multipartImageUpload() {
        Log.i("multipartImageUpload", "multipartImageUpload");
        try {
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".jpeg");
            Log.i("file in multipart", file + "");
            Log.i("multipartImageUpload", "Inside this method");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), reqFile);
            Log.i("file.getName()", file.getName());


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
                    System.out.println("Image response" + response);

                    if (response.code() == 200) {
                        System.out.println("Image response" + response);

                        try {
                            String data = response.body().string();
                            System.out.println(data);

                            Map jsonJavaRootObject = new Gson().fromJson(data, Map.class);
                            //System.out.println(jsonJavaRootObject.get("status"));
                            status = jsonJavaRootObject.get("status").toString();
                            message = jsonJavaRootObject.get("message").toString();
                            path = jsonJavaRootObject.get("path").toString();

                            System.out.println(status + " " + message + " " + path);
                            CallSubmitDataService();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    Toast.makeText(getApplicationContext(), "Successfully updated" + " ", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("", "Failure body");
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
        Log.i("createPartFromString", "createPartFromString");
        return RequestBody.create(MultipartBody.FORM, data);
    }

    private void CallSubmitDataService() {
        Log.i("CallSubmitDataService", "CallSubmitDataService" + "  ====  " + path + "   token" + myToken);
        MyAppolloClient.getMyAppolloClient(myToken).mutate(
                UserDetailsUpdate.builder().userId(userId).name(userName).designation(designation).empId(employeeId).location(workLoc)
                        .bu(userBu).mobileNumber(mobile).profileImagePath(path)
                        .build()).enqueue(
                new ApolloCall.Callback<UserDetailsUpdate.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<UserDetailsUpdate.Data> response) {
                        System.out.println("res_message in User" + response);
                        String status = response.data().updateUserDetails_M().status();
                        final String message = response.data().updateUserDetails_M().message();
                        Log.d("res_message in User", message);
                        // Log.d("res_status userDetails", status);
                        if (status.equals("Success")) {
                            Log.d("res_message in User", message);
                         /*   if(workLoc.equals("Bangalore")){

                            }else if(workLoc.equals("Hydrabad")){

                            }*/
                            subscribeToTopic(workLoc);
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

    public void subscribeToTopic(String location) {
        FirebaseMessaging.getInstance().subscribeToTopic(location)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = location;
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("topic subscription", msg);
                        //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
