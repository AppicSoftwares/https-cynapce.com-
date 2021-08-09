package com.alcanzar.cynapse.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.StateModel;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class GetStateClass {

    private Context context;
    private ArrayList<StateModel>  stateList = new ArrayList<>();


    protected GetStateClass(Context context, String countryId) {

        try {
            getStateApi(context,countryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected  abstract void responseState(ArrayList<StateModel> stateNameList);
    public void getStateApi(Context context, String countryId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", countryId);
        header.put("Cynapse", params);
        new GetStateApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("STATERESPONSE", response.toString());
                    if (res_code.equals("1")) {

                        JSONArray header2 = header.getJSONArray("State");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            stateList.add(new StateModel(item.getString("country_code"), item.getString("state_code"), item.getString("state_name")));

                            Log.e("CheckCount",stateList.size()+"");
                        }
                        responseState(stateList);


                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }

}
