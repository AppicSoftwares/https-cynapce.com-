package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by alcanzar on 13/04/18.
 */

public class PromoCodeReviewNotiApi extends HeadApi {
    public PromoCodeReviewNotiApi(Context context, JSONObject params, boolean bool){
        Log.e("valite_conf_promocode33",params.toString());
        postJsonApi(context, AppConstantClass.validate_conference_promocode,params,"validate_conference_promocode",bool);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
