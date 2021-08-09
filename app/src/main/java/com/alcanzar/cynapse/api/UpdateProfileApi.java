package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class UpdateProfileApi extends HeadApi {
    public UpdateProfileApi(Context context , JSONObject params){
        Log.e("updateProfileAPi",params.toString());
        postJsonApi(context, AppConstantClass.updateProfile,params,"updateProfile",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
