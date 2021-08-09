package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class RequestDirectJobPostApi extends HeadApi {
    public RequestDirectJobPostApi(Context context,JSONObject jsonObject)
    {
        String url= AppConstantClass.requestDirectJobPost;
        Log.e("requestJobPost",jsonObject.toString());
        postJsonApi(context,url,jsonObject,"requestDirectJobPost",true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
