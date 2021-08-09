package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetPerfferedDataApi extends HeadApi {
    public GetPerfferedDataApi(Context context, JSONObject params, boolean bool){
        Log.e("getPerfferedData",params.toString());
        postJsonApi(context, AppConstantClass.getPerfferedData,params,"getPerfferedData",bool);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
