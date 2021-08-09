package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetProfileApi  extends HeadApi {
    public GetProfileApi(Context context, JSONObject params,boolean boolvalue){
        Log.e("getProfile",params.toString());
        postJsonApi(context, AppConstantClass.getProfile,params,"getProfile",boolvalue);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
