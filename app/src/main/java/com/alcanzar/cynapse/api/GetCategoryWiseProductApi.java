package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by alcanzar on 10/04/18.
 */

public class GetCategoryWiseProductApi extends HeadApi {
    public GetCategoryWiseProductApi(Context context, JSONObject params){
        Log.e("getCategoryWiseProduct",params.toString());
        postJsonApi(context, AppConstantClass.getCategoryWiseProduct,params,"getCategoryWiseProduct",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
