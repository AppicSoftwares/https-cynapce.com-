package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetAllMyProductsAPI  extends HeadApi{

    public GetAllMyProductsAPI(Context context,JSONObject params)
    {
        Log.e("getAllMyProducts",params.toString());
        postJsonApi(context, AppConstantClass.getAllMyProducts,params,"getAllMyProducts",true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
