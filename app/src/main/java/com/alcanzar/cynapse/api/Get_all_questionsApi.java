package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class Get_all_questionsApi extends HeadApi {
    public Get_all_questionsApi(Context context, JSONObject params){
        Log.e("get_all_questions",params.toString());
        postJsonApi(context, AppConstantClass.get_all_questions,params,"get_all_questions",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}

