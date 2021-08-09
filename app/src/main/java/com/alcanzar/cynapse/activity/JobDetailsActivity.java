package com.alcanzar.cynapse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;

public class JobDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_checkout;
    TextView title,txt_jobpay,txt_basic;
    ImageView btnBack,titleIcon;
    String jobApply="",price="",details="",plan_type="",job_id="",medical_profile_id = "", job_title_id = "";
    public static Activity fas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        btn_checkout=findViewById(R.id.btn_checkout);
        btnBack = findViewById(R.id.btnBack);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setVisibility(View.GONE);
        title=findViewById(R.id.title);
        txt_jobpay=findViewById(R.id.txt_jobpay);
        txt_basic=findViewById(R.id.txt_basic);
//        btnBack.setOnClickListener(this);
        fas = this;
        if(getIntent()!=null)
        {
          jobApply=getIntent().getStringExtra("jobApply");
          price=getIntent().getStringExtra("price");
          details=getIntent().getStringExtra("details");
          plan_type = getIntent().getStringExtra("plan_type");
            job_id = getIntent().getStringExtra("job_id");
            job_title_id = getIntent().getStringExtra("job_title_id");
            medical_profile_id = getIntent().getStringExtra("medical_profile_id");

        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(JobDetailsActivity.this,JobDetailActivity.class);
//                intent.putExtra("jobApply",jobApply);
//                intent.putExtra("job_title_id",AppConstantClass.detial_arrayList.get(0).getJob_title_id());
//                intent.putExtra("medical_profile_id", AppConstantClass.detial_arrayList.get(0).getMedical_profile_id());
//                intent.putExtra("job_id",AppConstantClass.detial_arrayList.get(0).getJob_id());
//                intent.putExtra("recommend",AppConstantClass.detial_arrayList.get(0).getRecommend());
//                intent.putExtra("id", AppConstantClass.detial_arrayList.get(0).getDetail_id());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        });
        if(plan_type.equalsIgnoreCase("1"))
        {

            txt_basic.setText("BASIC");
        }else {


            txt_basic.setText("PREMIUM");
        }
        if(jobApply.equalsIgnoreCase("1"))
        {
            title.setText("APPLYING");
        }else
        {
            title.setText("SHORTLIST");
        }


        txt_jobpay.setText(" Rs "+price);
Log.d("plan_type","<><"+plan_type);
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(JobDetailsActivity.this,CheckOutActivity.class);
                intent.putExtra("jobApply",jobApply);
                intent.putExtra("price",price);
                intent.putExtra("details",details);
                intent.putExtra("plan_type",plan_type);
                intent.putExtra("job_id",job_id);
                intent.putExtra("job_title_id",job_title_id);
                intent.putExtra("medical_profile_id",medical_profile_id);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:
                finish();
                break;
        }
    }
    public void onBackPressed(){
        super.onBackPressed();
//        Intent intent=new Intent(JobDetailsActivity.this,JobDetailActivity.class);
//        intent.putExtra("jobApply",jobApply);
//        intent.putExtra("job_title_id",AppConstantClass.detial_arrayList.get(0).getJob_title_id());
//        intent.putExtra("medical_profile_id",AppConstantClass.detial_arrayList.get(0).getMedical_profile_id());
//        intent.putExtra("job_id",AppConstantClass.detial_arrayList.get(0).getJob_id());
//        intent.putExtra("recommend",AppConstantClass.detial_arrayList.get(0).getRecommend());
//        intent.putExtra("id", AppConstantClass.detial_arrayList.get(0).getDetail_id());
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
    }
}
