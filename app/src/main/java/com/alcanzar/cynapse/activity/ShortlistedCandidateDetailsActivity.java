package com.alcanzar.cynapse.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShortlistedCandidateDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView nameF,emailF,contactF,addressF,title, job_title, cover_letter_txt,title_app,spl_app,current_employer,sub_spl_app
            , yearEXp, job_desc_txt,medical_prof_app,dept_app,pref_loc,current_ctc,expected_ctc;
    DatabaseHelper handler;
    ImageView btnBack, titleIcon;
    Button btnSearch, btnApply;
    ArrayList<JobMasterModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortlist_candidate_details);
        arrayList = getArrayList("shortlist_data");
        Log.e("list_size","<><<"+arrayList.size());
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.job_white);
        title = findViewById(R.id.title);
        cover_letter_txt = findViewById(R.id.cover_letter_txt);
        pref_loc = findViewById(R.id.pref_loc);
        current_ctc = findViewById(R.id.current_ctc);
        expected_ctc = findViewById(R.id.expected_ctc);

        job_desc_txt = findViewById(R.id.job_desc_txt);
        medical_prof_app = findViewById(R.id.medical_prof_app);
        spl_app = findViewById(R.id.spl_app);
        title_app = findViewById(R.id.title_app);
        sub_spl_app = findViewById(R.id.sub_spl_app);
        dept_app = findViewById(R.id.dept_app);
        yearEXp = findViewById(R.id.yearEXp);
        current_employer = findViewById(R.id.current_employer);

        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);
        nameF=findViewById(R.id.nameF);
        emailF=findViewById(R.id.emailF);
        contactF=findViewById(R.id.contactF);
        addressF=findViewById(R.id.addressF);
        title.setText("Applicant Details");
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);


        if (!arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("nurse") &&
                !arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("Administrative and support")&&
                !arrayList.get(0).getMedical_profile_name().equalsIgnoreCase("paramedical"))
        {
            dept_app.setVisibility(View.GONE);
            spl_app.setVisibility(View.VISIBLE);
            sub_spl_app.setVisibility(View.VISIBLE);
        } else {
            dept_app.setVisibility(View.VISIBLE);
            spl_app.setVisibility(View.GONE);
            sub_spl_app.setVisibility(View.GONE);
        }

//        if(arrayList.get(0).getMedical_profile_id().equalsIgnoreCase("1")|| arrayList.get(0).getMedical_profile_id().equalsIgnoreCase("13"))
//        {
//            dept_app.setVisibility(View.GONE);
//            spl_app.setVisibility(View.VISIBLE);
//            sub_spl_app.setVisibility(View.VISIBLE);
//        }else
//        {
//            dept_app.setVisibility(View.VISIBLE);
//            spl_app.setVisibility(View.GONE);
//            sub_spl_app.setVisibility(View.GONE);
//        }


//        String sourceString = "<b>" + id + "</b> " + name;
//        mytextview.setText(Html.fromHtml(sourceString));
        nameF.setText("Name : "+arrayList.get(0).getName());
        emailF.setText("Email : "+arrayList.get(0).getEmailId());
        contactF.setText("Contact No. : "+arrayList.get(0).getMobileNo());
        addressF.setText("Address : "+arrayList.get(0).getAddress());
        cover_letter_txt.setText("Cover Letter : "+arrayList.get(0).getResume_description());
        current_ctc.setText(String.format("Current CTC : %s", arrayList.get(0).getCurrent_ctc()));
        expected_ctc.setText(String.format("Expected CTC : %s", arrayList.get(0).getExpected_ctc()));
        spl_app.setText(String.format("Specialization : %s", arrayList.get(0).getSpecialization_name()));
        sub_spl_app.setText(String.format("Sub Specialization : %s", arrayList.get(0).getSub_specialization_name()));
        dept_app.setText(String.format("Department : %s", arrayList.get(0).getDepartment_name()));
        medical_prof_app.setText(String.format("Medical Profile : %s", arrayList.get(0).getMedical_profile_name()));
        title_app.setText(String.format("Job Title : %s", arrayList.get(0).getJob_title()));
        pref_loc.setText(String.format("Preferred Location : %s", arrayList.get(0).getPreferred_location()));
        current_employer.setText(String.format("Current Employer : %s", arrayList.get(0).getCurrent_employer()));
        yearEXp.setText(String.format("Years of Experience : %s", arrayList.get(0).getYear_of_experience()));

        if(arrayList.get(0).getResume().equalsIgnoreCase(""))
        {
            job_desc_txt.setVisibility(View.GONE);
            findViewById(R.id.uploadResumeTv).setVisibility(View.VISIBLE);
        }
        else
        {
            findViewById(R.id.uploadResumeTv).setVisibility(View.GONE);
            job_desc_txt.setVisibility(View.VISIBLE);
            job_desc_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList.get(0).getResume().contains(".pdf")) {
                        Intent intent = new Intent(ShortlistedCandidateDetailsActivity.this, PdfActivity.class);
                        intent.putExtra("pdfurl", arrayList.get(0).getResume());
                        startActivity(intent);
                    }
                    else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(0).getResume())));
                    }
                }
            });
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
    public ArrayList<JobMasterModel> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ShortlistedCandidateDetailsActivity.this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<JobMasterModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
