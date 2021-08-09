package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by alcanzar on 13/04/18.
 */

public class PromoCodeApi extends HeadApi {
    public PromoCodeApi(Context context, JSONObject params, boolean bool){
        Log.e("validate_promocodeeeee",params.toString());
        postJsonApi(context, AppConstantClass.validate_promocode,params,"validate_promocode",bool);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
