package com.alcanzar.cynapse.api;

import android.content.Context;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetProductCategoryApi extends HeadApi {
    public GetProductCategoryApi(Context context){

        postJsonApiGet(context, AppConstantClass.getAllProductCategory,"getAllCategory",false);

    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
