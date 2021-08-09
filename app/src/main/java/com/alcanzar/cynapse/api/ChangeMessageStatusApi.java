package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ChangeMessageStatusApi  extends HeadApi {
    public ChangeMessageStatusApi(Context context, JSONObject params){
        Log.e("changeMessageStatus",params.toString());
        postJsonApi(context, AppConstantClass.changeMessageStatus,params,"changeMessageStatus",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}