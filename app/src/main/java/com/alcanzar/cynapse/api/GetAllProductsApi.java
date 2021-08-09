package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetAllProductsApi extends HeadApi {
    public GetAllProductsApi(Context context, JSONObject params){
        Log.e("getAllProducts",params.toString());
        postJsonApi(context, AppConstantClass.getAllProducts,params,"getAllProducts",true);

    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
