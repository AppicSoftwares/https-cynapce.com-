package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetDepartmentApi extends HeadApi {
    public GetDepartmentApi (Context context, JSONObject params){
        Log.e("getDepartment",params.toString());
        postJsonApi(context, AppConstantClass.getDepartment,params,"getDepartment",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}