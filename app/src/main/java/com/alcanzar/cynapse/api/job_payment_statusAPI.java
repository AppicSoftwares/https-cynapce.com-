package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class job_payment_statusAPI extends HeadApi {
    public job_payment_statusAPI(Context context , JSONObject params){
        Log.e("PostJobPaymentData",params.toString());
        postJsonApi(context, AppConstantClass.PostJobPaymentData,params,"PostJobPaymentData",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}