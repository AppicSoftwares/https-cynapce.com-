package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ConferenceAllListApi extends HeadApi {
    public ConferenceAllListApi(Context context, JSONObject params){
        Log.e("conferencListdsfss",params.toString());
        postJsonApi(context, AppConstantClass.getAllConference,params,"getAllConference",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
