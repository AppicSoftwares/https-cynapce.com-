package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;
public class GetCityApi extends HeadApi {
    public GetCityApi(Context context, JSONObject params){
        Log.e("getCity",params.toString());
        postJsonApi(context, AppConstantClass.getCity,params,"getCity",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
