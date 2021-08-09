package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class JobSearchFilter extends HeadApi {

    public JobSearchFilter(Context context, JSONObject jsonObject) {
        Log.e("JobSearchFilter",jsonObject.toString());
        postJsonApi(context, AppConstantClass.jobSearchFilter,jsonObject,"JobSearchFilter",true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}

