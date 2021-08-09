package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetAllProductApi extends HeadApi {
    public GetAllProductApi(Context context ,JSONObject params){
        Log.e("getAllProduct",params.toString());
        postJsonApi(context, AppConstantClass.getAllProduct,params,"getAllProduct",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
