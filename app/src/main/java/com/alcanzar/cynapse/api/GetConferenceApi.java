package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetConferenceApi extends HeadApi {
    public GetConferenceApi(Context context, JSONObject params){
        Log.e("getCity",params.toString());
        postJsonApi(context, AppConstantClass.getConference,params,"getConference",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
