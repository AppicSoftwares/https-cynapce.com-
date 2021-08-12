package com.alcanzar.cynapse.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.PaymentJobAdapter;
import com.alcanzar.cynapse.api.GetPaymentListPlanApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobDetailsModel;
import com.alcanzar.cynapse.model.JobPaymentModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Basic_Premimum_Activity extends AppCompatActivity implements View.OnClickListener
{
    String jobApply = "", medical_profile_id = "", job_title_id = "", jobType = "1", job_id="", detail_id="", recommend="", jobPlan="";
    ArrayList<JobPaymentModel> arrayList = new ArrayList<>();

    RecyclerView recycleView;
    Button btn_basic, btn_prmium;
    DatabaseHelper handler;
    TextView txt_plan_msg;
    LinearLayoutManager linearLayoutManager;
    TextView title;
    ImageView btnBack, titleIcon;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic__premimum);
        fa = this;
        linearLayoutManager = new LinearLayoutManager(Basic_Premimum_Activity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(linearLayoutManager);
        btn_prmium = findViewById(R.id.btn_prmium);
        btn_basic = findViewById(R.id.btn_basic);
        btnBack = findViewById(R.id.btnBack);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setVisibility(View.GONE);
        title = findViewById(R.id.title);
        title.setText(R.string.our_plans);
        btnBack.setOnClickListener(this);
        handler = new DatabaseHelper(this);
        txt_plan_msg = findViewById(R.id.txt_plan_msg);
        if (getIntent() != null) {
            jobApply = getIntent().getStringExtra("jobApply");
            job_title_id = getIntent().getStringExtra("job_title_id");
            medical_profile_id = getIntent().getStringExtra("medical_profile_id");
            recommend = getIntent().getStringExtra("recommend");
            job_id = getIntent().getStringExtra("job_id");
            detail_id = getIntent().getStringExtra("detail_id");

            Log.d("JOBAPPLYY", jobApply);
            Log.d("medical_profile_id", medical_profile_id);
            Log.d("job_title_id_fffff", job_title_id);
            Log.d("job_id_dddddd", job_id);

        }
        JobDetailsModel model = new JobDetailsModel();
        model.setDetail_id(detail_id);
        model.setJob_title_id(job_title_id);
        model.setRecommend(recommend);
        model.setJob_id(job_id);
        model.setMedical_profile_id(medical_profile_id);
        AppConstantClass.detial_arrayList.add(model);

        try {
           GetMyPlansApi();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
//        setData();

        btn_prmium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jobType.equalsIgnoreCase("1")) {
                    jobType = "2";
                    btn_basic.setBackgroundResource(R.drawable.button_1);
                    btn_prmium.setBackgroundResource(R.drawable.button_2);
                } else {
                    btn_basic.setBackgroundResource(R.drawable.button_3);
                    btn_prmium.setBackgroundResource(R.drawable.button_4);
                    jobType = "1";
                }

                arrayList = handler.getJobPlanDetails(DatabaseHelper.TABLE_PAYMENT_PLAN, "2",job_title_id);
                Log.d("SIZEOFARILIST11", arrayList.size() + "");
                if (arrayList.size() > 0) {
                    txt_plan_msg.setVisibility(View.GONE);
                    recycleView.setVisibility(View.VISIBLE);
                    PaymentJobAdapter requestPostJobAdapter = new PaymentJobAdapter(Basic_Premimum_Activity.this, R.layout.job_payment, arrayList, "2",jobApply,job_title_id,medical_profile_id);
                    recycleView.setAdapter(requestPostJobAdapter);
                } else {
                    PaymentJobAdapter requestPostJobAdapter = new PaymentJobAdapter(Basic_Premimum_Activity.this, R.layout.job_payment, arrayList, "2",jobApply,job_title_id,medical_profile_id);
                    arrayList.clear();
                    recycleView.setVisibility(View.GONE);
                    requestPostJobAdapter.notifyDataSetChanged();
                    txt_plan_msg.setVisibility(View.VISIBLE);
                }


            }
        });

        btn_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jobType.equalsIgnoreCase("1")) {
                    jobType = "2";
                    btn_basic.setBackgroundResource(R.drawable.button_1);
                    btn_prmium.setBackgroundResource(R.drawable.button_2);
                } else {
                    btn_basic.setBackgroundResource(R.drawable.button_3);
                    btn_prmium.setBackgroundResource(R.drawable.button_4);
                    jobType = "1";
                }
                arrayList = handler.getJobPlanDetails(DatabaseHelper.TABLE_PAYMENT_PLAN, "1",job_title_id);
                Log.d("SIZEOFARILIST222", arrayList.size() + "");

                if (arrayList.size() > 0) {
                    txt_plan_msg.setVisibility(View.GONE);
                    recycleView.setVisibility(View.VISIBLE);
                    PaymentJobAdapter requestPostJobAdapter = new PaymentJobAdapter(Basic_Premimum_Activity.this, R.layout.job_payment, arrayList, "1",jobApply,job_title_id,medical_profile_id);
                    recycleView.setAdapter(requestPostJobAdapter);
                } else {
                    PaymentJobAdapter requestPostJobAdapter = new PaymentJobAdapter(Basic_Premimum_Activity.this, R.layout.job_payment, arrayList, "1",jobApply,job_title_id,medical_profile_id);
                    recycleView.setVisibility(View.GONE);
                    arrayList.clear();
                    requestPostJobAdapter.notifyDataSetChanged();
                    txt_plan_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        if (jobType.equalsIgnoreCase("1")) {

            btn_basic.setBackgroundResource(R.drawable.button_3);
            btn_prmium.setBackgroundResource(R.drawable.button_4);
        } else {
            btn_basic.setBackgroundResource(R.drawable.button_1);
            btn_prmium.setBackgroundResource(R.drawable.button_2);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

//        try {
//            GetMyPlansApi();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
                    handler.deleteTableName(DatabaseHelper.TABLE_PAYMENT_PLAN);
                     AppCustomPreferenceClass.writeString(Basic_Premimum_Activity.this,AppCustomPreferenceClass.pay_plan_sync_time,sync_time);
                    Log.d("Paymentplannnn", response.toString());

                    if (res_code.equals("1")) {
                        //MyToast.toastShort(this,res_msg);

                        JSONArray header2 = header.getJSONArray("jobs_packages");
                        Log.d("KLDKLFLKDLFL",header2.length()+"");
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
                            model.setJob_title_id(job_title_id);
                            model.setJob_id_(job_id);

                            // arrayList.add(model);

                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_PAYMENT_PLAN, DatabaseHelper.id, item.getString("id"))) {

                                handler.AddPaymentPlan(model, true);

                            } else {

                                handler.AddPaymentPlan(model, false);
                            }




                        }

                        setData();
                        //Log.d("ARLRILISTT333", arrayList.get(0).getJob_type());



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

    public void setData()
    {
        arrayList = handler.getJobPlanDetails(DatabaseHelper.TABLE_PAYMENT_PLAN, "1",job_title_id);
        if (arrayList.size() > 0) {
            Log.d("ARLRILISTT22233", arrayList.size() + "");
            PaymentJobAdapter requestPostJobAdapter = new PaymentJobAdapter(Basic_Premimum_Activity.this, R.layout.job_payment, arrayList, "1",jobApply,job_title_id,medical_profile_id);
            recycleView.setAdapter(requestPostJobAdapter);
        } else {
            txt_plan_msg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
