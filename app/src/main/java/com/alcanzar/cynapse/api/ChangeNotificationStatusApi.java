package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ChangeNotificationStatusApi  extends HeadApi {
    public ChangeNotificationStatusApi(Context context, JSONObject params){
        Log.e("changeNotiStatus",params.toString());
        postJsonApi(context, AppConstantClass.changeNotificationStatus,params,"changeNotificationStatus",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
