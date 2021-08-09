package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class AddPracticeProductApi  extends HeadApi {
    public AddPracticeProductApi(Context context, JSONObject params){
        Log.e("addPracticeProduct",params.toString());
        postJsonApi(context, AppConstantClass.addPracticeProduct,params,"addPracticeProduct",true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
