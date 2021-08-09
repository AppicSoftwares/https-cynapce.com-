package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ChangeLikeApi extends HeadApi {
    public ChangeLikeApi(Context context, JSONObject params){
        Log.e("likeConference",params.toString());
        postJsonApi(context, AppConstantClass.likeConference,params,"likeConference",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
