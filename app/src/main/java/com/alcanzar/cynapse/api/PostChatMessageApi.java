package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PostChatMessageApi extends HeadApi {
    public PostChatMessageApi (Context context, JSONObject params){
        Log.e("postChatMessage",params.toString());
        postJsonApi(context, AppConstantClass.postChatMessage,params,"postChatMessage",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
