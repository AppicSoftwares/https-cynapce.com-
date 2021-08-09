package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class Post_answersApi extends HeadApi {
    public Post_answersApi(Context context, JSONObject params){
        Log.e("post_answers",params.toString());
        postJsonApi(context, AppConstantClass.post_answers,params,"post_answers",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}

