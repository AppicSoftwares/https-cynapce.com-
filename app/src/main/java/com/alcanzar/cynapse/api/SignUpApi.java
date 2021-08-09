package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class SignUpApi extends HeadApi {
    public SignUpApi(Context context , JSONObject params){
        Log.e("SignUpApi",params.toString());
        postJsonApi(context, AppConstantClass.signUp,params,"signUp",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
