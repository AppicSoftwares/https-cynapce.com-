package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ImInterestedApi extends HeadApi {
    public ImInterestedApi (Context context, JSONObject params){
        Log.e("imInterested",params.toString());
        postJsonApi(context, AppConstantClass.imInterested,params,"imInterested",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
