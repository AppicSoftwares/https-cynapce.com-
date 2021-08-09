package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by alcanzar on 12/04/18.
 */

public class BuyingProductRequestApi extends HeadApi {
    public BuyingProductRequestApi(Context context, JSONObject params){
            Log.e("buyingProductRequest",params.toString());
            postJsonApi(context, AppConstantClass.buyingProductRequest,params,"buyingProductRequest",true);
        }
        @Override
        public void responseApi(JSONObject response) {

        }

        @Override
        public void errorApi(VolleyError error) {

        }
    }
