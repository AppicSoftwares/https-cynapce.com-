package com.alcanzar.cynapse.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class GetMedicalProfileClass {
    private Context context;
    private ArrayList<MedicalProfileModel> medicalList = new ArrayList<>();

    public GetMedicalProfileClass(Context context) {
        this.context = context;
        getMedicalProfileApi(context);
    }

    protected abstract void responseMedicalProfile(ArrayList<MedicalProfileModel> medicalList);

    private void getMedicalProfileApi(Context context) {
        new GetMedicalProfileApi(context) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    if (res_code.equals("1")) {

                        //MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("ProfileCategoryMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            medicalList.add(new MedicalProfileModel(item.getString("id"), item.getString("profile_category_name")));
                        }
                        medicalList.add(new MedicalProfileModel("-1", "Others"));

                        Log.e("zdvadv",medicalList.size()+"");
                            responseMedicalProfile(medicalList);
                    } else {
                        medicalList.clear();

                        //MyToast.toastLong(getActivity(),res_msg);
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
