package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class Get_all_casesApi  extends HeadApi {
    public Get_all_casesApi(Context context, JSONObject params){
        Log.e("get_all_cases",params.toString());
        postJsonApi(context, AppConstantClass.get_all_cases,params,"get_all_cases",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}

