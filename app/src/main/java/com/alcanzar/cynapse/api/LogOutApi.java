package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;
public class LogOutApi extends HeadApi {
    public LogOutApi(Context context, JSONObject params){
       Log.e("logout",params.toString());
       postJsonApi(context, AppConstantClass.logout,params,"logout",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
