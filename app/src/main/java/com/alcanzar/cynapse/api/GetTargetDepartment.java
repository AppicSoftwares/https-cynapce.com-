package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetTargetDepartment extends HeadApi {

    public GetTargetDepartment(Context context, JSONObject params)
    {
        Log.e("getState",params.toString());
        postJsonApi(context, AppConstantClass.getTargetDepartment,params,"getState",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
