package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ShorlistedCandidatesApi extends HeadApi {
    public ShorlistedCandidatesApi(Context context,JSONObject params)
    {
        Log.e("shorlistedCandidates",params.toString());
        postJsonApi(context, AppConstantClass.shorlistedCandidates,params,"shorlistedCandidates",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
