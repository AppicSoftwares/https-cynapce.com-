package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PostRSAApi extends HeadApi {
    public PostRSAApi(Context context, JSONObject params){
        Log.e("getCcevnue",params.toString());
        postJsonApi(context, AppConstantClass.getCcevnue,params,"getCcevnue",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
