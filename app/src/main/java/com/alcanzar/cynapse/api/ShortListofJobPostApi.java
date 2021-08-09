package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ShortListofJobPostApi  extends HeadApi {
    public ShortListofJobPostApi(Context context, JSONObject params){
        Log.e("shortListofJobPost",params.toString());
        postJsonApi(context, AppConstantClass.shortListofJobPost,params,"shortListofJobPost",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
