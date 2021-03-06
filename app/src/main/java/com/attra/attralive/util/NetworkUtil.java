package com.attra.attralive.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NetworkUtil {
    public static boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.i("Networkinfo",""+activeNetworkInfo);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
            Log.i("isConnected wifi",""+connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState());
            Log.i("isConnected mobile data",""+connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()  );
        }
        else {
            connected = false;
        }
        return connected;
    }
    public static String versionRelease(){
        String VersionRelease = Build.VERSION.RELEASE;
        Log.i("VersionRelease" , VersionRelease);
        return VersionRelease;
    }
    public static String deviceId(Context context){
        String DeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("DeviceId" , DeviceId);
        return DeviceId;
    }
    public void hideKeyboard(Context context, View view)
    {
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@attra.com.au";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
