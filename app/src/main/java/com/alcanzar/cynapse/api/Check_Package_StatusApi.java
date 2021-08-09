package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class Check_Package_StatusApi extends HeadApi {
    public Check_Package_StatusApi(Context context, JSONObject params){
        Log.e("check_package_status",params.toString());
        postJsonApi(context, AppConstantClass.check_package_status,params,"check_package_status",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
