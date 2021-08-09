package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PostFeedbackApi extends HeadApi {
    public PostFeedbackApi(Context context, JSONObject params){
        Log.e("addBooksProduct",params.toString());
        postJsonApi(context, AppConstantClass.postFeedbacks,params,"postFeedbacks",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}


