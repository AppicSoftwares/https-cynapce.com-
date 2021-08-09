package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class StopConferenceBookingAPi extends HeadApi {

    public StopConferenceBookingAPi(Context context, JSONObject params)
    {
        Log.e("ConferenceTypePayment",params.toString());
        postJsonApi(context, AppConstantClass.stopConferenceBooking,params,"stopConferenceBooking",true);
    }

    @Override
    public void responseApi(JSONObject response) {
    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
