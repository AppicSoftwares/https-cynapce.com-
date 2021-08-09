package com.alcanzar.cynapse.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.api.GetAllCountryApi;
import com.alcanzar.cynapse.model.CountryModel;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class GetCountryClass {

    private Context context;
    private ArrayList<CountryModel>  countryList = new ArrayList<>();

    protected GetCountryClass(Context context) {

        getCountyApi(context);
    }

    protected  abstract void responseCountry(ArrayList<CountryModel> countryNameList);
    public void getCountyApi(Context context) {
        new GetAllCountryApi(context) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSECOUNTRY", response.toString());
                    countryList.clear();
                    if (res_code.equals("1")) {


                        JSONArray header2 = header.getJSONArray("Country");
                        for (int i = 0; i < header2.length(); i++) {

                            JSONObject item = header2.getJSONObject(i);
                            countryList.add(new CountryModel(item.getString("country_code"), item.getString("country_name")));

                        }
                       responseCountry(countryList);

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
