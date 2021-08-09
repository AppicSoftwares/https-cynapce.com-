package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class UpdateSocialMediaProfileApi extends HeadApi {
    public UpdateSocialMediaProfileApi(Context context , JSONObject params){
        Log.e("updateSocialMediaProf",params.toString());
        postJsonApi(context, AppConstantClass.updateSocialMediaProfile,params,"updateSocialMediaProfile",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}

