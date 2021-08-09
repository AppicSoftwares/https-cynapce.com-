package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ConferenceBookApi extends HeadApi {
    public ConferenceBookApi(Context context, JSONObject params){
        Log.e("jobConfBookPost",params.toString());
        postJsonApi(context, AppConstantClass.conferenceSeatBooking,params,"conferenceSeatBooking",false);
    }
    public ConferenceBookApi(Context context, JSONObject params,boolean b){
        Log.e("jobConfBookPost",params.toString());
        postJsonApi(context, AppConstantClass.conferenceSeatBooking,params,"conferenceSeatBooking",b);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
