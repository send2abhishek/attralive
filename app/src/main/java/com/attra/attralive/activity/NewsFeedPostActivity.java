package com.attra.attralive.activity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLogTags;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.attra.attralive.R;
import com.attra.attralive.Service.ApiService;
import com.attra.attralive.Service.MyAppolloClient;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import graphqlandroid.GetProfileDetails;
import graphqlandroid.PostThought;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.HTTP;
import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class NewsFeedPostActivity extends AppCompatActivity implements View.OnClickListener {
    ApiService apiService;
    OkHttpClient client;
    Fragment fragment = null;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    ImageView fabCamera, capturedImage;

    Bitmap mBitmap;
    Intent CropIntent;
    Uri outputFileUri;
    TextView successMsg;
    EditText postDescription;
    TextView Etusername, tvlocation;
    String refreshToken, worklocation, profileimage;
    ImageView imageView;
    Button post;
    String status, message, path, description="",myToken,username,userID,location;
    public static final String PREFS_AUTH ="my_auth";
    private SharedPreferences sharedPreferences;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_news_feed_post);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        OkHttpClient httpclient=new OkHttpClient();
        Picasso picasso = new Picasso.Builder(NewsFeedPostActivity.this)
                .downloader(new OkHttp3Downloader(httpclient))
                .build();
        sharedPreferences = getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("authToken")) {
            myToken = sharedPreferences.getString("authToken", "");
            username = sharedPreferences.getString("userName","");
            userID = sharedPreferences.getString("userId","");
            refreshToken=sharedPreferences.getString("refreshToken","");
            worklocation=sharedPreferences.getString("location","");
            profileimage=sharedPreferences.getString("profileImagePath","");
            //username = sharedPreferences.getString("username","");
        }
            userID = sharedPreferences.getString("userId","");

            Log.i("rrrrrrrrrrrrrrrrrrr",userID);
            Log.i("tok",myToken);

            //getNameandLocation();


        Etusername = findViewById(R.id.et_username);
       tvlocation=findViewById(R.id.tv_title);
       imageView = findViewById(R.id.img_userImage);

       Picasso.with(NewsFeedPostActivity.this)
               .load(profileimage).
               memoryPolicy(MemoryPolicy.NO_CACHE)
               .into(imageView);

       Etusername.setText(username);
       tvlocation.setText(worklocation);



        System.out.println(username);


        postDescription = findViewById(R.id.descText);
       // capturedImage = findViewById(R.id.capturedImage);
        fabCamera = findViewById(R.id.openCameraOptions);
        post = findViewById(R.id.btn_postnewsFeed);
        fabCamera.setOnClickListener(this);
        post.setOnClickListener(this);

        askPermissions();
        initRetrofitClient();




    }

    public void getNameandLocation()
    {
        MyAppolloClient.getMyAppolloClient(myToken).query(
                GetProfileDetails.builder().userId(userID)
                        .build()).enqueue(
                new ApolloCall.Callback<GetProfileDetails.Data>() {
                    @Override
                    public void onResponse(@Nonnull com.apollographql.apollo.api.Response<GetProfileDetails.Data> response) {
                        Log.i("res", String.valueOf(response));
                        String status = response.data().getProfileDetails_Q().status();
                        Log.i("mstatus in profile", status);
                        if (response.data().getProfileDetails_Q().name() != null) {
                            if (status.equals("Success")) {
                                username = response.data().getProfileDetails_Q().name();
                                 location = response.data().getProfileDetails_Q().location();
                                Log.i("location in newsfeed",location);

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

    private void initRetrofitClient() {
client         = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl("http://10.200.44.20:4001").client(client).build().create(ApiService.class);
    }

    public Intent CallGetVideoMethod()
    {
        videoView = findViewById(R.id.img_video);
         int VIDEO_CAPTURE = 101;

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_CAPTURE);

        return intent;
    }






    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.jpeg"));
        }
        return outputFileUri;
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
                // ((ImageView) findViewById(R.id.im_profileimage)).setImageURI(result.getUri());
                ((ImageView) findViewById(R.id.capturedImage)).setImageURI(result.getUri());

                Toast.makeText(this, "Cropping successful, Sample: " + result.getUri().toString(), Toast.LENGTH_LONG).show();

                Log.i("uri",result.getUri().toString());
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),result.getUri());
//                    mBitmap = BitmapFactory.decodeFile(result.getUri().toString());
//                    capturedImage.setImageBitmap(mBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
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

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void multipartImageUpload() {

        try {


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


            RequestBody userId = createPartFromString(userID);
            RequestBody type = createPartFromString("postPicture");
           // RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "postPicture");

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("userId", userId);
            map.put("type", type);

            Call<ResponseBody> req = apiService.postImage(map, body);

            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println("homescreenimage response"+ response);

                    if (response.code() == 200) {
//                        successMsg.setText("Uploaded Successfully!");
//                        successMsg.setTextColor(Color.BLUE);
//
                        System.out.println("homescreenimage response"+ response);


                        try {
                            String data = response.body().string();
                            System.out.println(data);

                            Map jsonJavaRootObject = new Gson().fromJson(data, Map.class);
                            //System.out.println(jsonJavaRootObject.get("status"));
                            status = jsonJavaRootObject.get("status").toString();
                            message = jsonJavaRootObject.get("message").toString();
                            path = jsonJavaRootObject.get("path").toString();

                           // CallPostService();

                            System.out.println(status+" " + message+" " + path);
                            CallPostService();



                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    Toast.makeText(getApplicationContext(), "Successfully updated" + " ", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(NewsFeedPostActivity.this,DashboardActivity.class);
                    startActivity(i);
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
        public void CallPostService()

        {

        MyAppolloClient.getMyAppolloClient(myToken).mutate(
                PostThought.builder().userId(userID).description(description).filePath(path).build()).enqueue(
                new ApolloCall.Callback<PostThought.Data>() {
                    @Override
                    public void onResponse(@Nonnull com.apollographql.apollo.api.Response<PostThought.Data> response) {
                        Log.i("","inside callpostmethod");
                        Log.i("",description);
                        String status = response.data().addPost_M().status();
                        String message = response.data().addPost_M().message();

                       // String status = response.data().addPost_M().userId();
                        NewsFeedPostActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewsFeedPostActivity.this, "Successfully Posted", Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                }
        );


        Intent i = new Intent(getApplication(),DashboardActivity.class);
        startActivity(i);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openCameraOptions:
//                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                onSelectImageClick(view);
                break;


            case R.id.img_video:

                CallGetVideoMethod();


            case R.id.btn_postnewsFeed:

               description = postDescription.getText().toString();

               if(description.equals("") && mBitmap==null)
               {
                   Toast.makeText(NewsFeedPostActivity.this,"Please Enter description or image",Toast.LENGTH_LONG).show();
               }
                else {
                   if (mBitmap != null)
                       multipartImageUpload();
                   else {
                       CallPostService();
                   }
               }
                break;

        }
    }

}

