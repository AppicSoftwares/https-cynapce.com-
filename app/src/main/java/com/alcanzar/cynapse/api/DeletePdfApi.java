package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class DeletePdfApi extends HeadApi {
    public DeletePdfApi(Context context, JSONObject params){
        Log.e("deletePdfApi",params.toString());
        postJsonApi(context, AppConstantClass.deleteBroushers,params,"deletePdfApi",false);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
