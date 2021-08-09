package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PostJobApi extends HeadApi {
    public PostJobApi(Context context, JSONObject params){
        Log.e("jobPost",params.toString());
        postJsonApi(context, AppConstantClass.jobPost,params,"jobPost",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
