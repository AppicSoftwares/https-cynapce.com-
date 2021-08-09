package com.alcanzar.cynapse.api;

import android.content.Context;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetMetroCityApi extends HeadApi {
    public GetMetroCityApi (Context context ){
        postJsonApiGet(context, AppConstantClass.getMetroCity ,"getMetroCity ",false);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}