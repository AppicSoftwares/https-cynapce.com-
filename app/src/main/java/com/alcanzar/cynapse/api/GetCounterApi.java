package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetCounterApi extends HeadApi {
    public GetCounterApi(Context context, JSONObject params){
        Log.e("getCounter",params.toString());
        postJsonApi(context, AppConstantClass.getCounter,params,"getCounter",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
