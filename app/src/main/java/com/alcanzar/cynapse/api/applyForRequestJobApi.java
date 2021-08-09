package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class applyForRequestJobApi  extends HeadApi {
    public applyForRequestJobApi(Context context, JSONObject params){
        Log.e("applyForRequestJob",params.toString());
        postJsonApi(context, AppConstantClass.applyForRequestJob,params,"applyForRequestJob",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}

