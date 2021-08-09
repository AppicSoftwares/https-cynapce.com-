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
import com.alcanzar.cynapse.api.applyForRequestJobApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostedJobDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    String id = "", recommend = "", medical_profile_id = "", job_title_id = "", jobType = "", job_id = "", job_type_title = "", jobApply = "";
    TextView location_job, yearOfExp_job, title,
            ctc, job_desc_txt, medical_profile_job, job_title_job, specialization_job, sub_specialization_job, department_job;
    DatabaseHelper handler;
    LinearLayout applicant_lin_lay, job_det_lin_lay;
    ImageView btnBack, titleIcon, share;
    Button btnSearch, btnApply;
    public static Activity fass;
    String getID = "", percentage = "", price = "", shortlist = "", plan_type = "", getctc = "";
    ArrayList<JobMasterModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details);
        handler = new DatabaseHelper(this);
        fass = this;

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.notification_white);

        yearOfExp_job = findViewById(R.id.yearOfExp_job);
        location_job = findViewById(R.id.location_job);
        medical_profile_job = findViewById(R.id.medical_profile_job);
        job_title_job = findViewById(R.id.job_title_job);
        specialization_job = findViewById(R.id.specialization_job);
        sub_specialization_job = findViewById(R.id.sub_specialization_job);
        department_job = findViewById(R.id.department_job);
        ctc = findViewById(R.id.ctc);
        applicant_lin_lay = findViewById(R.id.applicant_lin_lay);
        job_det_lin_lay = findViewById(R.id.job_det_lin_lay);

        job_desc_txt = findViewById(R.id.job_desc_det_txt);
        title = findViewById(R.id.title);

        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);


        title.setText(R.string.job_details);
        btnSearch = findViewById(R.id.btnSearch);
        share = findViewById(R.id.share);
        share.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.GONE);
        applicant_lin_lay.setVisibility(View.GONE);
        job_det_lin_lay.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);


        if (getIntent() != null) {
//            id = getIntent().getStringExtra("id");
//            recommend = getIntent().getStringExtra("recommend");
//            job_title_id = getIntent().getStringExtra("job_title_id");
//            medical_profile_id = getIntent().getStringExtra("medical_profile_id");

            if (getIntent().hasExtra("fromWhere")) {
                if (getIntent().getStringExtra("fromWhere").equals("RequestPostJobAdapter")) {
                    job_id = getIntent().getStringExtra("id");
                    arrayList = handler.getJobList(DatabaseHelper.TABLE_JOBS_MASTER, job_id);
                    btnApply.setVisibility(View.GONE);
                } else {
                    job_id = getIntent().getStringExtra("job_id");
                    arrayList = handler.getPostedJobList(DatabaseHelper.TABLE_POSTED_JOBS_MASTER, job_id, "");
                }
            } else {
                job_id = getIntent().getStringExtra("job_id");
                arrayList = handler.getPostedJobList(DatabaseHelper.TABLE_POSTED_JOBS_MASTER, job_id, "");
            }
        }

        Log.e("advsdvwsdfbvfr", "id= " + job_id + "," + arrayList.size());

//        if (recommend.equalsIgnoreCase("1")) {
//            jobApply = "1";
//            arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "1", id,"1");
//        Log.d("GETRESUMECASE",arrayList.get(0).getResume().toString());
        if (arrayList.get(0).getResume().equalsIgnoreCase("1")) {
            btnApply.setText(R.string.applied_);

        } else {
            btnApply.setText(R.string.proceed_);
        }
//        } else {
//            jobApply = "2";
//            arrayList = handler.getRecommendedAppliedJobList(DatabaseHelper.TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, "2", id,"1");
//
//            if (arrayList.get(0).getShortlist_status().equalsIgnoreCase("1")) {
//                btnApply.setText(R.string.shortlisted_);
//
//            } else {
//                btnApply.setText(R.string.shortlist_);
//            }
//        }

        medical_profile_id = arrayList.get(0).getMedical_profile_id();
        job_title_id = arrayList.get(0).getJob_title_id();
        id = arrayList.get(0).getId();


        job_title_job.setText("Job Title : " + arrayList.get(0).getJob_title());
        medical_profile_job.setText("Medical Profile : " + arrayList.get(0).getMedical_profile_name());
        specialization_job.setText("Specialization : " + arrayList.get(0).getSpecialization_name());
        sub_specialization_job.setText("Sub Specialization : " + arrayList.get(0).getSub_specialization_name());
        ctc.setText("CTC : " + arrayList.get(0).getCurrent_ctc());
        yearOfExp_job.setText("Experience Required : " + arrayList.get(0).getYear_of_experience());
        location_job.setText("LOCATION : " + arrayList.get(0).getLocation());
        job_desc_txt.setText("JOB DESCRIPTION : " + arrayList.get(0).getJob_description());
        department_job.setText("DEPARTMENT : " + arrayList.get(0).getDepartment_name());


//        specialization.setText(String.format("%s %s", arrayList.get(0).getSpecialization_name(), arrayList.get(0).getDepartment_name()));
//        yearOfExp.setText(String.format(" Years of Experience : %s", arrayList.get(0).getYear_of_experience()));
//        location.setText(String.format(" Location : %s", arrayList.get(0).getLocation()));
        //if (medical_profile_id.equalsIgnoreCase("1") || medical_profile_id.equalsIgnoreCase("4")) {
        if (!arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("nurse") &&
                !arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("Administrative and support")&&
                !arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("paramedical"))
        {
            department_job.setVisibility(View.GONE);
            specialization_job.setVisibility(View.VISIBLE);
            sub_specialization_job.setVisibility(View.VISIBLE);
        } else {
            department_job.setVisibility(View.VISIBLE);
            specialization_job.setVisibility(View.GONE);
            sub_specialization_job.setVisibility(View.GONE);
        }

        // if (recommend.equalsIgnoreCase("1"))


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
            case R.id.share:
                if (medical_profile_id.equalsIgnoreCase("1") || medical_profile_id.equalsIgnoreCase("4")) {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
//                  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, arrayList.get(0).getConference_name());
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "JOB DETAILS"
                            + "\n\n" + "Medical Profile : " + arrayList.get(0).getMedical_profile_name()
                            + "\n" + "Job Title : " + arrayList.get(0).getJob_title()
                            + "\n" + "Specialization : " + arrayList.get(0).getSpecialization_name()
                            + "\n" + "Sub Specialization : " + arrayList.get(0).getSub_specialization_name()
                            + "\n" + "Experience Required : " + arrayList.get(0).getYear_of_experience()
                            + "\n" + "Location : " + arrayList.get(0).getLocation()
                            + "\n" + "CTC : " + arrayList.get(0).getCurrent_ctc() + "\n"
                            + "Job Description : " + arrayList.get(0).getJob_description()
                            + "\n" + "Click to download app : " + "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
                    startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
//                  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, arrayList.get(0).getConference_name());
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "JOB DETAILS"
                            + "\n\n" + "Medical Profile : " + arrayList.get(0).getMedical_profile_name()
                            + "\n" + "Job Title : " + arrayList.get(0).getJob_title()
                            + "\n" + "Department : " + arrayList.get(0).getDepartment_name()
                            + "\n" + "Experience Required : " + arrayList.get(0).getYear_of_experience()
                            + "\n" + "Location : " + arrayList.get(0).getLocation()
                            + "\n" + "CTC : " + arrayList.get(0).getCurrent_ctc()
                            + "\n" + "Job Description : " + arrayList.get(0).getJob_description()
                            + "\n" + "Click to download app : " + "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
                    startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
                }


                break;
            case R.id.btnApply:

                if (Util.isVerifiedProfile(PostedJobDetailsActivity.this)) {



                    if (AppCustomPreferenceClass.readString(PostedJobDetailsActivity.this, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(PostedJobDetailsActivity.this, AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                        //b = false;
                        //showDialog(activity);
                        try {
                            if (Util.isVerifiyEMailPHoneNO((Activity) PostedJobDetailsActivity.this)) {


                                if (btnApply.getText().toString().equals("PROCEED")) {
                                    if (arrayList.get(0).getMedical_profile_name().equalsIgnoreCase(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_name, ""))) {
                                        Intent i = new Intent(this, ApplyJobFromPostedJobListActivity.class);
                                        Log.d("JOBID",job_id);
                                        i.putExtra("job_id", job_id);
                                        startActivity(i);
                                    } else {
                                        String medProf = AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_name, "");
                                        MyToast.toastShort(PostedJobDetailsActivity.this, "Your medical profile is " + medProf + " you cannot apply for medical profile other than yours");
                                    }
//                        try {
//                            Check_Package_StatusApi();
//                           // MyToast.toastShort(PostedJobDetailsActivity.this, "You apply for the job!");
//                            //applyForRequestJobApi();
//                           // btnApply.setClickable(false);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                                } else {
                                    MyToast.toastLong(PostedJobDetailsActivity.this, "You have already applied for the job!");
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    else {
                        //if (recommend.equalsIgnoreCase("1")) {
                        if (btnApply.getText().toString().equals("PROCEED")) {
                            if (arrayList.get(0).getMedical_profile_name().equalsIgnoreCase(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_name, ""))) {
                                Intent i = new Intent(this, ApplyJobFromPostedJobListActivity.class);
                                i.putExtra("job_id", job_id);
                                startActivity(i);
                            } else {
                                String medProf = AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_name, "");
                                MyToast.toastShort(PostedJobDetailsActivity.this, "Your medical profile is " + medProf + " you cannot apply for medical profile other than yours");
                            }
//                        try {
//                            Check_Package_StatusApi();
//                           // MyToast.toastShort(PostedJobDetailsActivity.this, "You apply for the job!");
//                            //applyForRequestJobApi();
//                           // btnApply.setClickable(false);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        } else {
                            MyToast.toastLong(PostedJobDetailsActivity.this, "You have already applied for the job!");
                        }
                    }
                }
                // }

                break;
        }
    }

    private void Check_Package_StatusApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(PostedJobDetailsActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("plan_type", "1");
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new Check_Package_StatusApi(PostedJobDetailsActivity.this, header) {
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
//                        if (!isFinishing()) {
//                            applyForRequestJobApi();
//                        }
                        applyForRequestJobApi();
                        //MyToast.toastLong(PostedJobDetailsActivity.this, res_msg);
                    } else {

                        //                       if (plan_type_.equalsIgnoreCase("1")) {
//                            if (job_type_title.equalsIgnoreCase("3")) {
//                                Intent intent = new Intent(PostedJobDetailsActivity.this, CheckOutActivity.class);
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
                        Intent intent = new Intent(PostedJobDetailsActivity.this, Basic_Premimum_Activity.class);
                        intent.putExtra("jobApply", "1");
                        intent.putExtra("job_title_id", job_title_id);
                        intent.putExtra("medical_profile_id", medical_profile_id);
                        intent.putExtra("job_id", job_id);
                        intent.putExtra("detail_id", id);
                        intent.putExtra("recommend", recommend);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        //  }

//                        } else {
//                            if (job_type_title.equalsIgnoreCase("3")) {
//                                Intent intent = new Intent(PostedJobDetailsActivity.this, CheckOutActivity.class);
//                                intent.putExtra("id", getID);
//                                intent.putExtra("price", price);
//                                intent.putExtra("jobApply", plan_type_);
//                                intent.putExtra("job_id", job_id);
//                                intent.putExtra("job_shortlist", shortlist);
//                                intent.putExtra("plan_type", plan_type);
//                                intent.putExtra("percentage", percentage);
//                                intent.putExtra("ctc", getctc);
//                                intent.putExtra("checkout", true);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//
//                                finish();
//                            } else {
//                                try {
//                                    Intent intent = new Intent(PostedJobDetailsActivity.this, Basic_Premimum_Activity.class);
//                                    intent.putExtra("jobApply", plan_type_);
//                                    intent.putExtra("job_title_id", job_title_id);
//                                    intent.putExtra("medical_profile_id", medical_profile_id);
//                                    intent.putExtra("job_id", job_id);
//                                    intent.putExtra("detail_id", id);
//                                    intent.putExtra("recommend", recommend);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);
//                                    finish();
//                                    //ShortListofJobPostApi();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }

                    }


                    // MyToast.toastLong(PostedJobDetailsActivity.this, res_msg);
                    finish();
                    //      }
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

    private void applyForRequestJobApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(PostedJobDetailsActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", arrayList.get(0).getJob_id());
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new applyForRequestJobApi(PostedJobDetailsActivity.this, header) {
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
                        MyToast.toastLong(PostedJobDetailsActivity.this, res_msg);
                    } else {

                        MyToast.toastLong(PostedJobDetailsActivity.this, res_msg);
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

}
