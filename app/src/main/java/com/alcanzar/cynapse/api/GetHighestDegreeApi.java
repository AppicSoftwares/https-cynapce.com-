package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;
public class GetHighestDegreeApi extends HeadApi {
    public GetHighestDegreeApi(Context context,JSONObject params){
        Log.e("highestDegree",params.toString());
        postJsonApi(context, AppConstantClass.highestDegree,params,"highestDegree",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
