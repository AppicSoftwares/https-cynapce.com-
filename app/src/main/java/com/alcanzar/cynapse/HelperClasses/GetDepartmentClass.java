package com.alcanzar.cynapse.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.api.GetDepartmentApi;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class GetDepartmentClass {

    private Context context;
    private ArrayList<MedicalProfileModel> dept_SpinnerList;
private String medicalId;

    public GetDepartmentClass(Context context, String medicalId) {
        this.context = context;
        this.medicalId = medicalId;
        dept_SpinnerList=new ArrayList<>();

        try {
            GetDepartmentApi(context,medicalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected abstract void responseDepartment(ArrayList<MedicalProfileModel> dept_SpinnerList);

    private void GetDepartmentApi(Context context,String medicalId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("medical_profile_id", medicalId);
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetDepartmentApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        //  dept_Spinner.clear();

                        JSONArray header2 = header.getJSONArray("Department");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            dept_SpinnerList.add(new MedicalProfileModel(item.getString("id"), item.getString("department_name")));
                            // dept_Spinner.add(item.getString("department_name"));
                            Log.e("subSSize", String.valueOf(dept_SpinnerList.size()));
                        }

                        responseDepartment(dept_SpinnerList);


//                        ArrayAdapter<String> adapter =new ArrayAdapter<>(MyProfileActivity.this,android.R.layout.simple_spinner_item
//                                ,dept_Spinner);
//                        dept_dd.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
                    } else {
                        // dept_Spinner.clear();
                        // dept_SpinnerList.clear();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
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
