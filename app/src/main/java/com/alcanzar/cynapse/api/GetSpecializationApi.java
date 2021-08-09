package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetSpecializationApi extends HeadApi {
    public GetSpecializationApi (Context context,JSONObject params){
        Log.e("getSpecialization",params.toString());
        postJsonApi(context, AppConstantClass.profileSpecialization,params,"getProfileSpecialization",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
