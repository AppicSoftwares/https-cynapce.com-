package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PostDeviceDataApi extends HeadApi {
    public PostDeviceDataApi (Context context, JSONObject params){
        Log.e("post_device_data",params.toString());
        postJsonApi(context, AppConstantClass.post_device_data,params,"post_device_data",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
