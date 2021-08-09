package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;
public class RequestJobApi extends HeadApi {
    public RequestJobApi(Context context, JSONObject params){
        Log.e("requestJobPost",params.toString());
        postJsonApi(context, AppConstantClass.requestJobPost,params,"requestJobPost",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
