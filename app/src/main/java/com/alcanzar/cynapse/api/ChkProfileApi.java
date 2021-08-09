package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ChkProfileApi extends HeadApi {

    public ChkProfileApi(Context context, JSONObject params){
        Log.e("chkProfile",params.toString());
        postJsonApi(context, AppConstantClass.chkProfile,params,"chkProfile",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
