package com.alcanzar.cynapse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.Check_Package_StatusApi;
import com.alcanzar.cynapse.api.GetPaymentListPlanApi;
import com.alcanzar.cynapse.api.ShortListofJobPostApi;
import com.alcanzar.cynapse.api.applyForRequestJobApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.model.JobPaymentModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JobDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String id = "", recommend = "", medical_profile_id = "", job_title_id = "", jobType = "", job_id = "", job_type_title = "", jobApply = "";
    TextView title, job_title, specialization, yearOfExp, location, sub_specialization, ctc, department, currentctc, expectedctc, medical_profile_job,
            job_title_job, specialization_job, department_job, sub_specialization_job, yearOfExp_job, vacancy_job, location_job, job_desc_det_txt,
            job_desc_txt, vacancy, timeAgo, medical_profile, applicant_name;
    DatabaseHelper handler;
    ImageView btnBack, titleIcon;
    Button btnSearch, btnApply;
    public static Activity fass;
    LinearLayout job_det_lin_lay, applicant_lin_lay;
    String getID = "", percentage = "", price = "", shortlist = "", plan_type = "", getctc = "";
    ArrayList<JobMasterModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details);

        handler = new DatabaseHelper(this);
        fass = this;

        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            recommend = getIntent().getStringExtra("recommend");
            job_title_id = getIntent().getStringExtra("job_title_id");
            medical_profile_id = getIntent().getStringExtra("medical_profile_id");
            job_id = getIntent().getStringExtra("job_id");
        }

        Log.d("recommenddd", recommend);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        applicant_lin_lay = findViewById(R.id.applicant_lin_lay);
        job_det_lin_lay = findViewById(R.id.job_det_lin_lay);
        medical_profile = findViewById(R.id.medical_profile);
        applicant_name = findViewById(R.id.applicant_name);
        medical_profile_job = findViewById(R.id.medical_profile_job);
        yearOfExp_job = findViewById(R.id.yearOfExp_job);
        vacancy_job = findViewById(R.id.vacancy_job);
        job_title_job = findViewById(R.id.job_title_job);
        department = findViewById(R.id.department);
        currentctc = findViewById(R.id.currentctc);
        expectedctc = findViewById(R.id.expectedctc);
        specialization_job = findViewById(R.id.specialization_job);
        sub_specialization_job = findViewById(R.id.sub_specialization_job);
        department_job = findViewById(R.id.department_job);
        location_job = findViewById(R.id.location_job);
        job_desc_det_txt = findViewById(R.id.job_desc_det_txt);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.notification_white);
        title = findViewById(R.id.title);
        job_title = findViewById(R.id.job_title);
        specialization = findViewById(R.id.specialization);
        sub_specialization = findViewById(R.id.sub_specialization);
        location = findViewById(R.id.location);
        yearOfExp = findViewById(R.id.yearOfExp);
        //functional_area_txt = findViewById(R.id.functional_area_txt);
        job_desc_txt = findViewById(R.id.job_desc_txt);
        //  industry_txt = findViewById(R.id.industry_txt);
        timeAgo = findViewById(R.id.timeAgo);
        vacancy = findViewById(R.id.vacancy);
        //  role_txt = findViewById(R.id.role_txt);
        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);
        ctc = findViewById(R.id.ctc);
        title.setText(R.string.jobs);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);

        if (recommend.equalsIgnoreCase("1")) {
            jobApply = "1";
            applicant_lin_lay.setVisibility(View.GONE);
            job_det_lin_lay.setVisibility(View.VISIBLE);

            arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "1", id, "1");
            Log.d("stautsstab", arrayList.get(0).getApplied_status());
            if (arrayList.get(0).getApplied_status().equalsIgnoreCase("1")) {
                btnApply.setText(R.string.applied_);

            } else {
                btnApply.setText(R.string.apply_);
            }
        } else {
            jobApply = "2";
            applicant_lin_lay.setVisibility(View.VISIBLE);
            job_det_lin_lay.setVisibility(View.VISIBLE);
            arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "2", id, "1");

            if (arrayList.get(0).getShortlist_status().equalsIgnoreCase("1")) {
                btnApply.setText(R.string.shortlisted_);
            } else {
                btnApply.setText(R.string.shortlist_);
                applicant_name.setVisibility(View.GONE);
            }
        }

        try {
            GetMyPlansApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

        applicant_name.setText("Applicant Name : " + arrayList.get(0).getName());
        job_title.setText("Job Title : " + arrayList.get(0).getJob_title());
        medical_profile.setText("Medical Profile : " + arrayList.get(0).getApp_medical_profile_name());
        specialization.setText("Specialization : " + arrayList.get(0).getApp_specialization_name());
        sub_specialization.setText("Sub Specialization : " + arrayList.get(0).getApp_sub_specialization_name());
        expectedctc.setText("Expected CTC : " + arrayList.get(0).getApp_expected_ctc());
        currentctc.setText("Current CTC : " + arrayList.get(0).getApp_current_ctc());
        location.setText("Preferred Location : " + arrayList.get(0).getPreferred_location());
        job_desc_txt.setText("Cover letter : " + arrayList.get(0).getResume_description());
        yearOfExp.setText("Years of Experience : " + arrayList.get(0).getApp_year_of_experience());
        department.setText("DEPARTMENT : " + arrayList.get(0).getApp_department_name());

        job_title_job.setText("Job Title : " + arrayList.get(0).getJob_title());
        medical_profile_job.setText("Medical Profile : " + arrayList.get(0).getMedical_profile_name());
        specialization_job.setText("Specialization : " + arrayList.get(0).getSpecialization_name());
        sub_specialization_job.setText("Sub Specialization : " + arrayList.get(0).getSub_specialization_name());
        ctc.setText("CTC : " + arrayList.get(0).getCurrent_ctc());
        yearOfExp_job.setText("Experience Required : " + arrayList.get(0).getYear_of_experience());
        location_job.setText("LOCATION : " + arrayList.get(0).getLocation());
        job_desc_det_txt.setText("JOB DESCRIPTION : " + arrayList.get(0).getJob_description());
        department_job.setText("DEPARTMENT : " + arrayList.get(0).getDepartment_name());


//        specialization.setText(String.format("%s %s", arrayList.get(0).getSpecialization_name(), arrayList.get(0).getDepartment_name()));
//        yearOfExp.setText(String.format(" Years of Experience : %s", arrayList.get(0).getYear_of_experience()));
//        location.setText(String.format(" Location : %s", arrayList.get(0).getLocation()));


        //if(medical_profile_id.equalsIgnoreCase("1")|| medical_profile_id.equalsIgnoreCase("4"))
        if (!arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("nurse") &&
                !arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("paramedical") &&
                !arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("Administrative and Support")) {
            department.setVisibility(View.GONE);
            department_job.setVisibility(View.GONE);
            specialization_job.setVisibility(View.VISIBLE);
            specialization.setVisibility(View.VISIBLE);
            sub_specialization.setVisibility(View.VISIBLE);
            sub_specialization_job.setVisibility(View.VISIBLE);
        } else {
            department.setVisibility(View.VISIBLE);
            department_job.setVisibility(View.VISIBLE);
            specialization_job.setVisibility(View.GONE);
            specialization.setVisibility(View.GONE);
            sub_specialization.setVisibility(View.GONE);
            sub_specialization_job.setVisibility(View.GONE);
        }
        // job_desc_txt.setText(arrayList.get(0).getJob_description());
        // role_txt.setText(arrayList.get(0).getSpecialization_name());
        // functional_area_txt.setText(arrayList.get(0).getMedical_profile_name());
        //industry_txt.setText(String.format("%s %s", arrayList.get(0).getSpecialization_name(), arrayList.get(0).getDepartment_name()));
        // vacancy.setText(String.format(" Skills Required : %s", arrayList.get(0).getSkills_required()));

//        if (recommend.equalsIgnoreCase("1")) {
//
//            ctc.setText(String.format(" CTC : %s", arrayList.get(0).getCurrent_ctc()));
//            timeAgo.setVisibility(View.GONE);
//
//
//        } else {
//            ctc.setText(String.format(" Current CTC : %s", arrayList.get(0).getCurrent_ctc()));
//            timeAgo.setText(String.format(" Expected CTC : %s", arrayList.get(0).getExpected_ctc()));
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnApply:
                if (recommend.equalsIgnoreCase("1")) {
                    // AppCustomPreferenceClass.removeKey(getApplicationContext(),AppCustomPreferenceClass.promo_price);
//                    AppCustomPreferenceClass.removeKey(getApplicationContext(),AppCustomPreferenceClass.promo_plan_type);
                    if (btnApply.getText().toString().equalsIgnoreCase("APPLY")) {
                        try {
                            Check_Package_StatusApi("1");

                            //applyForRequestJobApi();
                            btnApply.setClickable(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        MyToast.toastLong(JobDetailActivity.this, "You have already applied for the job!");
                    }
                } else {
                    if (btnApply.getText().toString().equalsIgnoreCase("SHORTLIST")) {

                        try {
                            Check_Package_StatusApi("2");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        btnApply.setClickable(false);

                    } else {
                        MyToast.toastLong(JobDetailActivity.this, "You are already shortlisted for the job!");
                    }
                }
                break;
        }
    }

    private void applyForRequestJobApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(JobDetailActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", arrayList.get(0).getJob_id());
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new applyForRequestJobApi(JobDetailActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("applied", response.toString());
                    if (res_code.equals("1")) {
                        finish();
                        MyToast.toastLong(JobDetailActivity.this, res_msg);
                    } else {

                        MyToast.toastLong(JobDetailActivity.this, res_msg);
                        finish();
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

    private void Check_Package_StatusApi(final String plan_type_) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(JobDetailActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("plan_type", plan_type_);
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);
        //params.put("uuid","S75f3");
        //params.put("sync_time", "");
        header.put("Cynapse", params);
        new Check_Package_StatusApi(JobDetailActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("applieddddd", response.toString());
                    if (res_code.equals("1")) {
                        if (plan_type_.equalsIgnoreCase("1")) {
                            applyForRequestJobApi();
                        } else {
                            ShortListofJobPostApi();
                        }


                        //MyToast.toastLong(JobDetailActivity.this, res_msg);
                    } else {

                        if (plan_type_.equalsIgnoreCase("1")) {
//                            if (job_type_title.equalsIgnoreCase("3")) {
//                                Intent intent = new Intent(JobDetailActivity.this, CheckOutActivity.class);
//                                intent.putExtra("id", getID);
//                                intent.putExtra("price", price);
//                                intent.putExtra("job_id", job_id);
//                                intent.putExtra("job_shortlist", shortlist);
//                                intent.putExtra("plan_type", plan_type);
//                                intent.putExtra("percentage", percentage);
//                                intent.putExtra("ctc", getctc);
//                                intent.putExtra("checkout", true);
//                                startActivity(intent);
//                            } else {
                            Intent intent = new Intent(JobDetailActivity.this, Basic_Premimum_Activity.class);
                            intent.putExtra("jobApply", plan_type_);
                            intent.putExtra("job_title_id", job_title_id);
                            intent.putExtra("medical_profile_id", medical_profile_id);
                            intent.putExtra("job_id", job_id);
                            intent.putExtra("detail_id", id);
                            intent.putExtra("recommend", recommend);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            //  }

                        } else {
                            if (job_type_title.equalsIgnoreCase("3")) {
                                Intent intent = new Intent(JobDetailActivity.this, CheckOutActivity.class);
                                intent.putExtra("id", getID);
                                intent.putExtra("price", price);
                                intent.putExtra("jobApply", plan_type_);
                                intent.putExtra("job_id", job_id);
                                intent.putExtra("job_shortlist", shortlist);
                                intent.putExtra("plan_type", plan_type);
                                intent.putExtra("percentage", percentage);
                                intent.putExtra("ctc", getctc);
                                intent.putExtra("checkout", true);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                finish();
                            } else {
                                try {
                                    Intent intent = new Intent(JobDetailActivity.this, Basic_Premimum_Activity.class);
                                    intent.putExtra("jobApply", plan_type_);
                                    intent.putExtra("job_title_id", job_title_id);
                                    intent.putExtra("medical_profile_id", medical_profile_id);
                                    intent.putExtra("job_id", job_id);
                                    intent.putExtra("detail_id", id);
                                    intent.putExtra("recommend", recommend);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    //ShortListofJobPostApi();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }


                        // MyToast.toastLong(JobDetailActivity.this, res_msg);
                        finish();
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

    private void ShortListofJobPostApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(JobDetailActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", arrayList.get(0).getJob_id());
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new ShortListofJobPostApi(JobDetailActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("applied", response.toString());
                    if (res_code.equals("1")) {
                        finish();
                        MyToast.toastLong(JobDetailActivity.this, res_msg);
                    } else {

                        MyToast.toastLong(JobDetailActivity.this, res_msg);
                        finish();
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

    public void GetMyPlansApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_type", jobApply);
        params.put("medical_profile", medical_profile_id);
        params.put("title", job_title_id);
        params.put("job_id", job_id);
        params.put("sync_time", "");
        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new GetPaymentListPlanApi(this, header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    // AppCustomPreferenceClass.writeString(Basic_Premimum_Activity.this,AppCustomPreferenceClass.pay_plan_sync_time,sync_time);
                    Log.d("Paymentplannnn", response.toString());

                    if (res_code.equals("1")) {
                        //MyToast.toastShort(this,res_msg);

                        JSONArray header2 = header.getJSONArray("jobs_packages");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            JobPaymentModel model = new JobPaymentModel();
                            model.setJob_id(item.getString("id"));
                            model.setJob_pay(item.getString("price"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setJob_details(item.getString("job_shortlist"));
                            model.setJob_type(item.getString("plan_type"));
                            model.setPercentage(item.getString("percentage"));
                            model.setCtc(item.getString("ctc"));
                            job_type_title = item.getString("plan_type");
                            plan_type = item.getString("plan_type");
                            getctc = item.getString("ctc");
                            shortlist = item.getString("job_shortlist");
                            price = item.getString("price");
                            percentage = item.getString("percentage");
                            getID = item.getString("id");

                            // arrayList.add(model);

//                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_PAYMENT_PLAN, DatabaseHelper.id, item.getString("id"))) {
//
//                                handler.AddPaymentPlan(model, true);
//
//                            } else {
//
//                                handler.AddPaymentPlan(model, false);
//                            }
                            //  Log.d("PLANTYOOEOOEP_PE", item.getString("plan_type"));
//                            if (item.getString("plan_type").equalsIgnoreCase("3")) {
//
//
//                                Intent intent = new Intent(JobDetailActivity.this, CheckOutActivity.class);
//                                intent.putExtra("id", item.getString("id"));
//                                intent.putExtra("price", item.getString("price"));
//                                intent.putExtra("add_date", item.getString("add_date"));
//                                intent.putExtra("job_shortlist", item.getString("job_shortlist"));
//                                intent.putExtra("plan_type", item.getString("plan_type"));
//                                intent.putExtra("percentage", item.getString("percentage"));
//                                intent.putExtra("ctc", item.getString("ctc"));
//                                intent.putExtra("checkout", true);
//                                startActivity(intent);

                            //   }


                        }

//                        arrayList = handler.getJobPlanDetails(DatabaseHelper.TABLE_PAYMENT_PLAN, "1");
//                        PaymentJobAdapter requestPostJobAdapter = new PaymentJobAdapter(Basic_Premimum_Activity.this,R.layout.job_payment,arrayList,jobApply);
//                        recycleView.setAdapter(requestPostJobAdapter);

                    } else {

                        // MyToast.toastLong(MyDealsActivity.this,res_msg);
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
