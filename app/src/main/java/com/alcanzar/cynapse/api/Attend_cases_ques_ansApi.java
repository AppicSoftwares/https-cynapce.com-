package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class Attend_cases_ques_ansApi  extends HeadApi {
    public Attend_cases_ques_ansApi(Context context, JSONObject params){
        Log.e("attend_cases_ques_ans",params.toString());
        postJsonApi(context, AppConstantClass.attend_cases_ques_ans,params,"attend_cases_ques_ans",true);
    }
    @Override
    public void responseApi(JSONObject response) {

    }

    @Override
    public void errorApi(VolleyError error) {

    }
}
