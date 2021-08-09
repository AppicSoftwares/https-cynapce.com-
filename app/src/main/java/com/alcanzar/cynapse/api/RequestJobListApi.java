package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class RequestJobListApi extends HeadApi {
    public RequestJobListApi(Context context, JSONObject params){
        Log.e("requestJobList",params.toString());
        postJsonApi(context, AppConstantClass.requestJobList,params,"requestJobList",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
