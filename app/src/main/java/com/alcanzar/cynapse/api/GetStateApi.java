package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;
public class GetStateApi extends HeadApi {
    public GetStateApi(Context context, JSONObject params){
        Log.e("getState",params.toString());
        postJsonApi(context, AppConstantClass.getState,params,"getState",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
