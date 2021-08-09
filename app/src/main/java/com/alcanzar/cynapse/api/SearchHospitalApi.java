package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class SearchHospitalApi extends HeadApi {


    public SearchHospitalApi(Context context, JSONObject params){
        Log.e("hospitalSearchFilter",params.toString());
        postJsonApi(context, AppConstantClass.hospitalSearchFilter,params,"hospitalSearchFilter",true);
    }


    @Override
    public void responseApi(JSONObject response) {
    }

    @Override
    public void errorApi(VolleyError error) {

    }


}
