package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by alcanzar on 13/04/18.
 */

public class GetMyDealApi  extends HeadApi {
    public GetMyDealApi(Context context, JSONObject params,boolean bool){
        Log.e("getMyDeal",params.toString());
        postJsonApi(context, AppConstantClass.getMyDeal,params,"getMyDeal",bool);
    }
    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
}
