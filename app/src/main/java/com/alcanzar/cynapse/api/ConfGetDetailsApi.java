package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;


public class ConfGetDetailsApi extends HeadApi {
    public ConfGetDetailsApi(Context context, JSONObject params){
        Log.e("getConferenceDetail",params.toString());
        postJsonApi(context, AppConstantClass.getConferenceDetail,params,"getConferenceDetail",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}

