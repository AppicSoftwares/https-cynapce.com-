package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class PostedJobListApi extends HeadApi {
    public PostedJobListApi(Context context, JSONObject params){
        Log.e("postedJobList",params.toString());
        postJsonApi(context, AppConstantClass.postedJobList,params,"postedJobList",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
