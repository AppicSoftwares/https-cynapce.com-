package com.alcanzar.cynapse.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ShortlistedJobListAdapter;
import com.alcanzar.cynapse.api.ShorlistedCandidatesApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShortlistedJobListActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    DatabaseHelper handler;
    ImageView btnBack, titleIcon;
    Button btnSearch;
    String job_id = "";
    //TODO : recycle and listing views
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<JobMasterModel> arrayList = new ArrayList<>() ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        if(getIntent()!=null)
        {
            job_id = getIntent().getStringExtra("job_id");
        }
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.job_white);
        title = findViewById(R.id.title);
        title.setText("Shortlisted Applicants");
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        //TODO : recycle and adapter control
//        for (int i = 0; i <= 10; i++) {
//            arrayList.add(new DashBoardModel("This is the new notification", "id"));
//        }
        recyclerView = findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        try {
            shorlistedCandidatesAPI(job_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
    public void shorlistedCandidatesAPI(String job_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.UserId,""));
        params.put("job_id",job_id);
        header.put("Cynapse",params);
        new ShorlistedCandidatesApi(this,header){
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    if(res_code.equals("1"))
                    {
                        MyToast.toastLong(ShortlistedJobListActivity.this, res_msg);
                        JSONArray jsonArray = header.getJSONArray("shorlistedCandidates");
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject item  = jsonArray.getJSONObject(i);
                            JobMasterModel model = new JobMasterModel();
                            model.setId(item.getString("id"));
                            model.setJob_id(item.getString("jobId"));
                            model.setMedical_profile_id(item.getString("medical_profile_id"));
                            model.setMedical_profile_name(item.getString("medical_profile_name"));

                            model.setMobileNo(item.getString("mobile_number"));
                            model.setEmailId(item.getString("email"));
                            model.setAddress(item.getString("address"));


                            model.setName(item.getString("name"));
                            model.setDepartment_name(item.getString("department_name"));
                            model.setJob_title(item.getString("job_title"));
                            model.setJob_type(item.getString("job_type"));
                            model.setJob_type_name(item.getString("job_type_name"));
                            model.setSpecialization_id(item.getString("specialization_id"));
                            model.setSpecialization_name(item.getString("specialization_name"));
                            model.setSub_specialization_id(item.getString("sub_specialization_id"));
                            model.setSub_specialization_name(item.getString("sub_specialization_name"));
                            model.setCurrent_ctc(item.getString("current_ctc"));
                            model.setYear_of_experience(item.getString("year_of_experience"));
                            model.setExpected_ctc(item.getString("expected_ctc"));
                            model.setCurrent_employer(item.getString("current_employer"));
                            model.setLocation(item.getString("location"));
                            model.setExpected_ctc(item.getString("expected_ctc"));
                            model.setPreferred_location(item.getString("preferred_location"));
                            model.setResume(item.getString("resume"));
                            model.setResume_description(item.getString("resume_description"));
                            model.setJob_description(item.getString("job_description"));
                            model.setSkills_required(item.getString("skills_required"));
                            model.setApplied_date(item.getString("applied_date"));
                            model.setApplied_status(item.getString("applied_status"));
                            model.setShortlist_status(item.getString("shortlist_status"));
                            model.setShortlist_status(item.getString("shortlist_status"));
                            model.setAdd_date(item.getString("add_date"));
                           // model.setModify_date(item.getString("modify_date"));
                            model.setModify_date(Util.getDateNew_(Long.parseLong(item.getString("modify_date"))*1000));
                            model.setStatus(item.getString("status"));
                            arrayList.add(model);


                        }
                        setArrayList();
                        saveArrayList(arrayList,"shortlist_data");

                    }else
                    {
                        MyToast.toastLong(ShortlistedJobListActivity.this, res_msg);
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

    public void setArrayList()
    {
        ShortlistedJobListAdapter notificationAdapter = new ShortlistedJobListAdapter(this, R.layout.shortlisted_job_list_row, arrayList);
        recyclerView.setAdapter(notificationAdapter);
    }

    public void saveArrayList(ArrayList<JobMasterModel> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ShortlistedJobListActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }
}
