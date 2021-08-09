package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetYearOfStudyApi extends HeadApi {
    public GetYearOfStudyApi (Context context, JSONObject params){
        Log.e("getYearOfStudy",params.toString());
        postJsonApi(context, AppConstantClass.getYearOfStudy,params,"getYearOfStudy",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
