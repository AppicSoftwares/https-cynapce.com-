package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class AddTieUpsProductApi  extends HeadApi {
    public AddTieUpsProductApi(Context context, JSONObject params){
        Log.e("addTieUpsProduct",params.toString());
        postJsonApi(context, AppConstantClass.addTieUpsProduct,params,"addTieUpsProduct",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
