package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ForgotPassWordApi extends HeadApi {
    public ForgotPassWordApi(Context context, JSONObject params){
        Log.e("forgotPassword",params.toString());
        postJsonApi(context, AppConstantClass.forgotPassword,params,"forgotPassword",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
