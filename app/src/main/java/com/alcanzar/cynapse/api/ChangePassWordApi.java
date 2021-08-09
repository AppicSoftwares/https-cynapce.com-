package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ChangePassWordApi extends HeadApi {
    public ChangePassWordApi(Context context, JSONObject params){
        Log.e("changePassword",params.toString());
        postJsonApi(context, AppConstantClass.changePassword,params,"changePassword",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
