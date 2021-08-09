package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetAllMyPlansApi extends HeadApi {
    public GetAllMyPlansApi(Context context, JSONObject params){
        Log.e("getAllMyPlans",params.toString());
        postJsonApi(context, AppConstantClass.getAllMyPlans,params,"getAllMyPlans",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
