package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetRecommendedJobListApi extends HeadApi {
    public GetRecommendedJobListApi(Context context, JSONObject params){
        Log.e("getRecommendedJobList",params.toString());
        postJsonApi(context, AppConstantClass.getRecommendedJobList,params,"getRecommendedJobList",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}

