package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PublishProfileApi extends HeadApi{
    public PublishProfileApi(Context context, JSONObject params){
        Log.e("Post_Published_Profile",params.toString());
        postJsonApi(context, AppConstantClass.Post_Published_Profile,params,"Post_Published_Profile",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
