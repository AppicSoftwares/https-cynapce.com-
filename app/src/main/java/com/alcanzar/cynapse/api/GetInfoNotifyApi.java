package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetInfoNotifyApi extends HeadApi {
    public

    GetInfoNotifyApi(Context context, JSONObject params){
        Log.e("gtConfrenceUserInfowill",params.toString());
        postJsonApi(context, AppConstantClass.getConferenceUserInfo,params,"getConferenceUserInfo",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
