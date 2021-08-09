package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class Update_Package_StatusAPI extends HeadApi {
    public Update_Package_StatusAPI(Context context , JSONObject params){
        Log.e("update_package_status",params.toString());
        postJsonApi(context, AppConstantClass.update_package_status,params,"update_package_status",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}