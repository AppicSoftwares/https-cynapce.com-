package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class UpdateUsernameAPI extends HeadApi {

    public UpdateUsernameAPI(Context context, JSONObject params) {
        Log.e("updateUsername", params.toString());
        postJsonApi(context, AppConstantClass.updateUsername, params, "updateUsername", true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
