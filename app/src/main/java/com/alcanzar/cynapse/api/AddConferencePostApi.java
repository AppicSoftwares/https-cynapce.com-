package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class AddConferencePostApi extends HeadApi {
    public AddConferencePostApi(Context context, JSONObject params){
        Log.e("addConferencePost",params.toString());
        postJsonApi(context, AppConstantClass.addConference,params,"addConference",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
