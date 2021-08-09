package com.alcanzar.cynapse.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.api.GetAllCountryApi;
import com.alcanzar.cynapse.api.GetCityApi;
import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.StateModel;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class GetCityClass {

    private Context context;
    private ArrayList<CityModel> cityList=new ArrayList<>();
    String countryId;
    String stateId;

    public GetCityClass(Context context, String countryId, String stateId) {
        this.context = context;
        this.countryId = countryId;
        this.stateId = stateId;
        try {
            getCityApi(context,countryId,stateId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected  abstract void responseCity(ArrayList<CityModel> cityNameList);


    public void getCityApi(Context context,String stateCountryId, String stateId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", stateCountryId);
        params.put("state_code", stateId);
        header.put("Cynapse", params);
        Log.d("CITYHEADER", params.toString());
        new GetCityApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("CITYRESPONSE", response.toString());
                    if (res_code.equals("1")) {
                        JSONArray header2 = header.getJSONArray("City");
                        for (int i = 0; i < header2.length(); i++) {

                            JSONObject item = header2.getJSONObject(i);
                            cityList.add(new CityModel(item.getString("city_id"), item.getString("country_code"), item.getString("state_code"), item.getString("city_name")));

                        }
                        responseCity(cityList);

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
