package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by alcanzar on 13/04/18.
 */

public class BuyProductApi  extends HeadApi {
    public BuyProductApi(Context context, JSONObject params){
        Log.e("buyProduct",params.toString());
        postJsonApi(context, AppConstantClass.buyProduct,params,"buyProduct",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}

