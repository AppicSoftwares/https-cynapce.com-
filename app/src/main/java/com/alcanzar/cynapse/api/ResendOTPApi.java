package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ResendOTPApi extends HeadApi {
    public ResendOTPApi(Context context , JSONObject params){
        Log.e("resendOTP",params.toString());
        postJsonApi(context, AppConstantClass.resendOTP,params,"resendOTP",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
