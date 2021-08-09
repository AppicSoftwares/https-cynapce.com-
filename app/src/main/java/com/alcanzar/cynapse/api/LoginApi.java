package com.alcanzar.cynapse.api;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.utils.AppConstantClass;
import com.android.volley.VolleyError;

import org.json.JSONObject;
public class LoginApi extends HeadApi
{
    public LoginApi (Context context,JSONObject params){
        Log.e("login",params.toString());
        postJsonApi(context, AppConstantClass.login,params,"login",true);
    }

    public LoginApi(Context context,JSONObject params,int i){
        Log.e("signUpNew",params.toString());
        postJsonApi(context, AppConstantClass.signUpNew,params,"signUpNew",true);
    }

    @Override
    public void responseApi(JSONObject response) {

    }
    @Override
    public void errorApi(VolleyError error) {

    }
//    81yitgg4o00v93

//            NX79xiiGYu1WAiu5
}
