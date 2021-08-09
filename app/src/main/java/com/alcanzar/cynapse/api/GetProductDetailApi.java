package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by alcanzar on 11/04/18.
 */

public class GetProductDetailApi extends HeadApi {
    public GetProductDetailApi(Context context, JSONObject params){
        Log.e("GetProductDetailApi",params.toString());
        postJsonApi(context, AppConstantClass.getProductDetail,params,"GetProductDetailApi",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}

