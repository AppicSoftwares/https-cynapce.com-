package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetbookedConferenceAPI extends HeadApi {

    public GetbookedConferenceAPI(Context context, JSONObject params) {
        Log.e("getbookedConference", params.toString());
        postJsonApi(context, AppConstantClass.getbookedConference, params, "getbookedConference", true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
