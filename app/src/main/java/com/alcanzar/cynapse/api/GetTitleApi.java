package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetTitleApi  extends HeadApi {
    public GetTitleApi (Context context, JSONObject params){
        Log.e("getTitle",params.toString());
        postJsonApi(context, AppConstantClass.getTitle,params,"getTitle",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
