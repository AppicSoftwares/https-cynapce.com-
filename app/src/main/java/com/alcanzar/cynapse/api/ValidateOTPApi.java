package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ValidateOTPApi extends HeadApi {
    public ValidateOTPApi(Context context , JSONObject params){
        Log.e("validateOTP",params.toString());
        postJsonApi(context, AppConstantClass.validateOTP,params,"validateOTP",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
