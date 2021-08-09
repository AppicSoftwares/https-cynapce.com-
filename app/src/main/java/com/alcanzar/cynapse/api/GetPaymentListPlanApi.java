package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by alcanzar on 13/04/18.
 */

public class GetPaymentListPlanApi extends HeadApi {
    public GetPaymentListPlanApi(Context context, JSONObject params, boolean bool){
        Log.e("getPaymentListPlan",params.toString());
        postJsonApi(context, AppConstantClass.get_jobs_packages,params,"get_jobs_packages",bool);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
