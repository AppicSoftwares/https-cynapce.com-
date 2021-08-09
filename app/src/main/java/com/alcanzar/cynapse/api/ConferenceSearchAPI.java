package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ConferenceSearchAPI extends HeadApi {

    public ConferenceSearchAPI(Context context, JSONObject params) {
        Log.e("conference_search", params.toString());
        postJsonApi(context, AppConstantClass.conference_search, params, "conference_search", true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
