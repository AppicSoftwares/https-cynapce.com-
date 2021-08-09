package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ProfileUpdateRequestApi  extends HeadApi {
    public ProfileUpdateRequestApi(Context context, JSONObject params){
        Log.e("profileUpdateRequest",params.toString());
        postJsonApi(context, AppConstantClass.profileUpdateRequest,params,"profileUpdateRequest",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
