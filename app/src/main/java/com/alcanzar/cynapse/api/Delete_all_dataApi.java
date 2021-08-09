package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class Delete_all_dataApi extends HeadApi {
    public Delete_all_dataApi(Context context, JSONObject params){
        Log.e("delete_all_data",params.toString());
        postJsonApi(context, AppConstantClass.delete_all_data,params,"delete_all_data",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
