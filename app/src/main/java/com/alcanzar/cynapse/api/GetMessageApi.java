package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetMessageApi  extends HeadApi {
    public GetMessageApi(Context context, JSONObject params, boolean bool){
        Log.e("getMessage",params.toString());
        postJsonApi(context, AppConstantClass.getMessage,params,"getMessage",bool);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}

