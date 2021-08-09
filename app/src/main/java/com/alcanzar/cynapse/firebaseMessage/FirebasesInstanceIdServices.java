package com.alcanzar.cynapse.firebaseMessage;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FirebasesInstanceIdServices extends FirebaseInstanceIdService {

    String TAG = this.getClass().getName();
    @Override
    public void onTokenRefresh() {
        Log.e(TAG+"_id_firebase", FirebaseInstanceId.getInstance().getId());
    }
}
