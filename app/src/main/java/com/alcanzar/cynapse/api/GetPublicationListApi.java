package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetPublicationListApi extends HeadApi {
    public GetPublicationListApi(Context context, JSONObject params){
        Log.e("getPublicationList",params.toString());
        postJsonApi(context, AppConstantClass.getPublicationList,params,"getPublicationList",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}