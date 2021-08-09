package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetChatMessageApi extends HeadApi {
    public GetChatMessageApi(Context context, JSONObject params){
        Log.e("getChatMessage",params.toString());
        postJsonApi(context, AppConstantClass.getChatMessage,params,"getChatMessage",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
