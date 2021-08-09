package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetConferenceTypeApi extends HeadApi {
    public GetConferenceTypeApi(Context context, JSONObject params){
        Log.e("getConferenceType",params.toString());
        postJsonApi(context, AppConstantClass.getConferenceType,params,"getCity",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
