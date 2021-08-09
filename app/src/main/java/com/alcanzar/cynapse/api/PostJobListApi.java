package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PostJobListApi extends HeadApi {
    public PostJobListApi(Context context, JSONObject params){
        Log.e("postJobList",params.toString());
        postJsonApi(context, AppConstantClass.postJobList,params,"postJobList",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
