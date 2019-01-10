package com.attra.attralive.Service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.attra.attralive.util.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
	private static final String TAG = "MyFirebaseIIDService";

	/**
	 * Called if InstanceID token is updated. This may occur if the security of
	 * the previous token had been compromised. Note that this is called when the InstanceID token
	 * is initially generated so this is where you would retrieve the token.
	 */
	@Override
	public void onTokenRefresh() {
		super.onTokenRefresh();
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();

		// Saving reg id to shared preferences
		storeRegIdInPref(refreshedToken);

		// sending reg id to your server
		sendRegistrationToServer(refreshedToken);

		// Notify UI that registration has completed, so the progress indicator can be hidden.
		Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
		registrationComplete.putExtra("token", refreshedToken);
		LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
	}

	private void sendRegistrationToServer(final String token) {
		// sending gcm token to server
		Log.e(TAG, "sendRegistrationToServer: " + token);
	}

	private void storeRegIdInPref(String token) {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("regId", token);
		editor.commit();
	}}