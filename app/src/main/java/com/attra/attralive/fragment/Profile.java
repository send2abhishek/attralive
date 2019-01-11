package com.attra.attralive.fragment;

import android.app.Activity;
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
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.ApiService;
import com.attra.attralive.Service.MyAppolloClient;
import com.attra.attralive.activity.DashboardActivity;
import com.attra.attralive.activity.UserDetailsActivity;
import com.attra.attralive.util.GetNewRefreshToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    TextView username, userDesign,DOB,gender,phone,email, submit,empId;
    Spinner location,bu;
    private SharedPreferences sharedPreferences;
    String myToken,userId,userName;
    public static final String PREFS_AUTH = "my_auth";
    ImageView profilePic,qrCode;

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
    Bitmap mBitmap;
    ImageView upload;
    CardView submitdata;
    String status,message,path,refreshToken;
    String emailId, password,userBu,workLoc,mobile,employeeId,designation;
    EditText  phNo;
    String buValue;

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
            refreshToken=sharedPreferences.getString("refreshToken","");
            Log.i("user id in userDtail",userId);

        }

        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.profile);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        empId = view.findViewById(R.id.et_empId);
       // username = view.findViewById(R.id.et_entername);
        userDesign=view.findViewById(R.id.et_designation);
        bu = view.findViewById(R.id.sp_selectbu);
        phone = view.findViewById(R.id.et_mobilenumber);
  //      email= view.findViewById(R.id.emailId);
        submit = view.findViewById(R.id.btn_submitDetails);
        location = view.findViewById(R.id.sp_userWorkLocation);
        profilePic = view.findViewById(R.id.civ_profileimage);
        submitdata=view.findViewById(R.id.crd_continuebutton);
        qrCode = view.findViewById(R.id.img_qrCode);
        getProfileDetail();
        askPermissions();
        initRetrofitClient();

profilePic.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
    }
});
submitdata.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        designation = userDesign.getText().toString();
         workLoc = location.getSelectedItem().toString();
        userBu = bu.getSelectedItem().toString();
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
                callSubmiteditData(myToken);
            }
            // Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
            // startActivity(intent1);

        }

    }
});
        return view;
    }
    private void callSubmiteditData(String accesstoken)
    {
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
                            Intent intent1 = new Intent(getActivity(), DashboardActivity.class);
                            intent1.putExtra("location",workLoc);
                            startActivity(intent1);
                        } else if(status.equals("Failure")){
                            if(message.equals("Invalid token: access token is invalid")){

                                GetNewRefreshToken.getRefreshtoken(refreshToken,getActivity());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sharedPreferences = getActivity().getSharedPreferences(GetNewRefreshToken.PREFS_AUTH, Context.MODE_PRIVATE);
                                        if (sharedPreferences.contains("authToken")) {
                                            String myToken = sharedPreferences.getString("authToken", "");
                                            callSubmiteditData(myToken);
                                            Toast.makeText(getActivity(), myToken, Toast.LENGTH_LONG).show();

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
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
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
        PackageManager packageManager = getActivity().getPackageManager();

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
        File getImage = getActivity().getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.jpeg"));
        }
        return outputFileUri;
    }
    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picUri = savedInstanceState.getParcelable("pic_uri");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            //capturedImage = findViewById(R.id.im_profileimage);

            if (requestCode == IMAGE_RESULT) {


                String filePath = getImageFilePath(data);
                //cropImage();
                System.out.println("file path"+filePath);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    profilePic.setImageDrawable(null);
                    profilePic.setImageBitmap(mBitmap);

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
    private void multipartImageUpload() {

        try {
            //description = postDescription.getText().toString();

            File filesDir = getActivity().getFilesDir();
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
                            //CallSubmitDataService(myToken);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    Toast.makeText(getActivity(), "Successfully updated" + " ", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("","Failure body");
//                    successMsg.setText("Uploaded Failed!");
//                    successMsg.setTextColor(Color.RED);
                    Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_LONG).show();
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
                                        userDesign.setText(design);
                                        Picasso.with(getActivity()).load(qrCodePath).fit().into(qrCode);
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
