package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetSubSpecializationApi  extends HeadApi {
    public GetSubSpecializationApi (Context context, JSONObject params){
        Log.e("getSubSpecialization",params.toString());
        postJsonApi(context, AppConstantClass.getSubSpecialization,params,"getSubSpecialization",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
