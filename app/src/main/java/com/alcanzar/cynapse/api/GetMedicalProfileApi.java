package com.alcanzar.cynapse.api;

import android.content.Context;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetMedicalProfileApi extends HeadApi {
    public GetMedicalProfileApi (Context context ){
        postJsonApiGet(context, AppConstantClass.medicalProfile,"medicalProfile",true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
