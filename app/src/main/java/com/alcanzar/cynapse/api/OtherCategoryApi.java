package com.alcanzar.cynapse.api;

import android.content.Context;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class OtherCategoryApi  extends HeadApi {
    public OtherCategoryApi (Context context, JSONObject params){
        postJsonApi(context, AppConstantClass.otherCategory,params,"otherCategory",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}

