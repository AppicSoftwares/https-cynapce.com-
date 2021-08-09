package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PostPaymentApi extends HeadApi
{
    public PostPaymentApi(Context context, JSONObject params){
        Log.e("postpaymentapi",params.toString());
        postJsonApi(context, AppConstantClass.conferenceOrder,params,"postpaymentapi",false);
    }

    public PostPaymentApi(Context context, JSONObject params,boolean bol){
        Log.e("postpaymentapi",params.toString());
        postJsonApi(context, AppConstantClass.conferenceOrder,params,"postpaymentapi",bol);
    }

    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
