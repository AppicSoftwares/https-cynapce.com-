package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetNotificationApi extends HeadApi {
    public GetNotificationApi(Context context, JSONObject params){
        Log.e("notification",params.toString());
        postJsonApi(context, AppConstantClass.getNotification,params,"notification",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
