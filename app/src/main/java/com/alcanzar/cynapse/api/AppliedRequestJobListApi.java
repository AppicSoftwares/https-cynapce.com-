package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class AppliedRequestJobListApi extends HeadApi {
    public AppliedRequestJobListApi(Context context, JSONObject params){
        Log.e("appliedRequestJobList",params.toString());
        postJsonApi(context, AppConstantClass.appliedRequestJobList,params,"appliedRequestJobList",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}

