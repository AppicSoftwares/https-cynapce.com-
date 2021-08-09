package com.alcanzar.cynapse.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.RecommendedAppliedJobAdapter;
import com.alcanzar.cynapse.api.AppliedRequestJobListApi;
import com.alcanzar.cynapse.api.GetRecommendedJobListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecommendedJobsActivity  extends AppCompatActivity implements View.OnClickListener {
    //TODO : header vi ews
    TextView title;
    DatabaseHelper handler;
    ImageView btnBack, titleIcon;
    Button btnSearch;
    //TODO : recycle and listing views
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<JobMasterModel> arrayList = new ArrayList<>();
    String recommend="",job_id="",applicants_uuid ="";
    public static Activity recommend_;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        //TODO : initializing and setting the header views
        handler = new DatabaseHelper(this);
        recommend_= this;
        if(getIntent()!=null) {
            recommend = getIntent().getStringExtra("recommend");
            job_id = getIntent().getStringExtra("job_id");
            applicants_uuid = getIntent().getStringExtra("applicants_uuid");
        }
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.notification_white);
        title = findViewById(R.id.title);
        title.setText(R.string.jobs);
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

        if(recommend.equalsIgnoreCase("1"))
        {
            try {
                GetRecommendedJobListApi(job_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "1","","1");
            if (arrayList.size() > 0) {
                setArrayList();
            }
        }
        else
        {
            try {
                AppliedRequestJobListApi(job_id,applicants_uuid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "2","","1");
            if (arrayList.size() > 0) {
                setArrayList();
            }
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

    private void GetRecommendedJobListApi(String job_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this, AppCustomPreferenceClass.UserId, ""));
        // params.put("uuid","S75f3");
       // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.job_ra_sync_time,""));
        params.put("sync_time", "");
        params.put("jobs_id", job_id);
       // params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetRecommendedJobListApi(RecommendedJobsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(RecommendedJobsActivity.this,AppCustomPreferenceClass.job_ra_sync_time,sync_time);
                    Log.d("NOTIFICATIONYYYY", response.toString());
                    if (res_code.equals("1")) {
                        handler.deleteTableName(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER);
                        JSONArray jsonArray = header.getJSONArray("RecommendedJobList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            JobMasterModel model = new JobMasterModel();
                            model.setId(item.getString("id"));
                            model.setJob_id(item.getString("jobId"));
                            model.setMedical_profile_id(item.getString("medical_profile_id"));
                            model.setMedical_profile_name(item.getString("medical_profile_name"));
                            model.setJob_title(item.getString("job_title"));
                            model.setJob_title_id(item.getString("job_title_id"));
                            model.setJob_type(item.getString("job_type"));
                            model.setJob_type_name(item.getString("job_type_name"));
                            model.setSpecialization_id(item.getString("specialization_id"));
                            model.setSpecialization_name(item.getString("specialization_name"));
                            model.setSub_specialization_id(item.getString("sub_specialization_id"));
                            model.setSub_specialization_name(item.getString("sub_specialization_name"));
                            model.setYear_of_experience(item.getString("year_of_experience"));
                            model.setCurrent_ctc(item.getString("current_ctc"));
                            model.setExpected_ctc(item.getString("expected_ctc"));
                            model.setCurrent_employer(item.getString("current_employer"));
                            model.setLocation(item.getString("location"));
                            model.setPreferred_location(item.getString("preferred_location"));
                            model.setResume(item.getString("resume"));
                            model.setJob_description(item.getString("job_description"));
                            model.setSkills_required(item.getString("skills_required"));
                            model.setApplied_date(item.getString("applied_date"));
                            model.setApplied_status(item.getString("applied_status"));
                            model.setShortlist_status(item.getString("shortlist_status"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setModify_date(item.getString("modify_date"));
                            model.setStatus(item.getString("status"));
                            model.setDepartment_id(item.getString("department_id"));
                            model.setDepartment_name(item.getString("department_name"));
                            model.setPost_req_status("1");
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, DatabaseHelper.id, item.getString("id"))) {

                                handler.AddRecommendedAppliedJobMaster(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                handler.AddRecommendedAppliedJobMaster(model, false);
                            }
                        }
                        arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "1","","1");
                        setArrayList();
                        //adapter.notifyDataSetChanged();
                    } else {

                        // MyToast.toastLong(RecommendedJobsActivity.this, res_msg);
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

    private void AppliedRequestJobListApi(String job_id, String applicants_uuid) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this, AppCustomPreferenceClass.UserId, ""));
         params.put("job_id",job_id);
         params.put("applicants_uuid",applicants_uuid);
        //params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.app_job_ra_sync_time,""));
        params.put("sync_time", "");
       // params.put("sync_time", "");
        header.put("Cynapse", params);
        new AppliedRequestJobListApi(RecommendedJobsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(RecommendedJobsActivity.this,AppCustomPreferenceClass.app_job_ra_sync_time,sync_time);
                    Log.d("NOTIFICATIONNNN", response.toString());
                    if (res_code.equals("1")) {
                        handler.deleteTableName(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER);
                        JSONArray jsonArray = header.getJSONArray("AppliedRequestJobList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            JobMasterModel model = new JobMasterModel();
                            model.setId(item.getString("id"));
                            model.setJob_id(item.getString("jobId"));
                            model.setApp_jobId(item.getString("app_job_id"));
                            model.setMedical_profile_id(item.getString("medical_profile_id"));
                            model.setApp_medical_profile_id(item.getString("app_medical_profile_id"));
                            model.setMedical_profile_name(item.getString("medical_profile_name"));
                            model.setApp_medical_profile_name(item.getString("app_medical_profile_name"));
                            model.setJob_title(item.getString("job_title"));
                            model.setApp_job_title(item.getString("app_job_title"));
                            model.setJob_title_id(item.getString("job_title_id"));
                            model.setApp_job_title_id(item.getString("app_job_title_id"));
                            model.setJob_type(item.getString("job_type"));
                            model.setJob_type_name(item.getString("job_type_name"));
                            model.setSpecialization_id(item.getString("specialization_id"));
                            model.setApp_specialization_id(item.getString("app_specialization_id"));
                            model.setSpecialization_name(item.getString("specialization_name"));
                            model.setApp_specialization_name(item.getString("app_specialization_name"));
                            model.setSub_specialization_id(item.getString("sub_specialization_id"));
                            model.setApp_sub_specialization_id(item.getString("app_sub_specialization_id"));
                            model.setSub_specialization_name(item.getString("sub_specialization_name"));
                            model.setApp_sub_specialization_name(item.getString("app_sub_specialization_name"));
                            model.setYear_of_experience(item.getString("year_of_experience"));
                            model.setApp_year_of_experience(item.getString("app_year_of_experience"));
                            model.setCurrent_ctc(item.getString("current_ctc"));
                            model.setApp_current_ctc(item.getString("app_current_ctc"));
                            model.setApp_expected_ctc(item.getString("app_expected_ctc"));
                            model.setExpected_ctc(item.getString("expected_ctc"));
                            model.setCurrent_employer(item.getString("current_employer"));
                            model.setLocation(item.getString("location"));
                            model.setPreferred_location(item.getString("preffered_location"));
                            model.setResume(item.getString("resume"));
                            model.setResume_description(item.getString("resume_description"));
                            model.setJob_description(item.getString("job_description"));
                            model.setSkills_required(item.getString("skills_required"));
                            model.setApplied_date(item.getString("applied_date"));
                            model.setApplied_status(item.getString("applied_status"));
                            model.setShortlist_status(item.getString("shortlist_status"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setModify_date(item.getString("modify_date"));
                            model.setStatus(item.getString("status"));
                            model.setDepartment_id(item.getString("department_id"));
                            model.setDepartment_name(item.getString("department_name"));
                            model.setApp_department_id(item.getString("app_department_id"));
                            model.setApp_department_name(item.getString("app_department_name"));
                            model.setName(item.getString("name"));
                            model.setPost_req_status("2");
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, DatabaseHelper.id, item.getString("id"))) {

                                handler.AddRecommendedAppliedJobMaster(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                handler.AddRecommendedAppliedJobMaster(model, false);
                            }
                        }
                        arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "2","","1");
                        setArrayList();
                        //adapter.notifyDataSetChanged();
                    } else {

                        // MyToast.toastLong(RecommendedJobsActivity.this, res_msg);
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
    {//arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "1","");
        Log.e("menulist.size();","<><><"+arrayList.size());
        RecommendedAppliedJobAdapter requestPostJobAdapter = new RecommendedAppliedJobAdapter(RecommendedJobsActivity.this,R.layout.job_row,arrayList,recommend);
        recyclerView.setAdapter(requestPostJobAdapter);
    }
}
