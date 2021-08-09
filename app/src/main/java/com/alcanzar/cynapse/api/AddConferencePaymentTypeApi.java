package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class AddConferencePaymentTypeApi extends HeadApi {

    public AddConferencePaymentTypeApi(Context context, JSONObject params){
        Log.e("ConferenceTypePayment",params.toString());
        postJsonApi(context, AppConstantClass.post_conference_pakages,params,"post_conference_pakages",true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
