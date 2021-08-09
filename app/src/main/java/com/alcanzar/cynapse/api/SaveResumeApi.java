package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class SaveResumeApi extends HeadApi {


    public SaveResumeApi(Context context, JSONObject params){
        Log.e("SaveResumeApi",params.toString());
        postJsonApi(context, AppConstantClass.uploadResume,params,"save resume",true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
