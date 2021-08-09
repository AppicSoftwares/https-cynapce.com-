package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MetroCityAdapter;
import com.alcanzar.cynapse.api.Check_Package_StatusApi;
import com.alcanzar.cynapse.api.GetMetroCityApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.RequestDirectJobPostApi;
import com.alcanzar.cynapse.api.applyForRequestJobApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.model.MetroCityModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.FilePath;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.PostImage;
import com.android.volley.VolleyError;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipDeletedListener;

public class ApplyJobFromPostedJobListActivity extends AppCompatActivity implements View.OnClickListener,MetroCityInterface {

    TextView medicalProfile,Title,Specialization,SubSpl,dept,jobLocation,uploadResume,title;
    EditText experience, currentCtc, expectedCtc, currentEmployer, desc,copy_paste_resume;
    ImageView resumeImg;
    DatabaseHelper handler;
    private static final int PICK_PDF_REQUEST = 11;
    private Uri fileUri;
    private String pdfPath,url = "", pdf_name = "",job_id ="" , id = "", recommend = "",email="",phoneNumber ="",medical_profile_id="";
    int exp=0,exp_lower =0 ,exp_upper = 0;
    ImageView btnBack, titleIcon;
    Button btnSearch, btnApply;
    ArrayList<MetroCityModel> metrocityList = new ArrayList<>();
    ArrayList<MetroCityModel> metrocitySelectedList = new ArrayList<>();
    ArrayList<JobMasterModel> arrayList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    RelativeLayout dept_rel_lay,Spl_rel_lay,subS_rel_lay;
    View subS_view,spl_view;

    //    akash commit changes


    ChipCloudConfig drawableWithCloseConfig,flexbox_multiSpecialityConfig,flexbox_multiDepartmentConfig,flexbox_multiTitleConfig;
    ChipCloud drawableWithCloseChipCloud;
    FlexboxLayout flexboxDrawableWithClose,flexbox_multiSpeciality,flexbox_multiDepartment,flexbox_multiTitle;
    String location="";
    String location_id="";
    ArrayAdapter locationAdapter;
    String  regStateStr = "", edit_disable = "";
    ArrayList<String>  cityChipsNameList = new ArrayList<>();

    //    akash commit changes

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job_layout);
        handler = new DatabaseHelper(this);
        if (getIntent() != null) {
//            id = getIntent().getStringExtra("id");
//            recommend = getIntent().getStringExtra("recommend");
//            job_title_id = getIntent().getStringExtra("job_title_id");
//            medical_profile_id = getIntent().getStringExtra("medical_profile_id");
            job_id = getIntent().getStringExtra("job_id");
        }
        arrayList = handler.getPostedJobList(DatabaseHelper.TABLE_POSTED_JOBS_MASTER, job_id,"");
        btnBack = findViewById(R.id.btnBack);

        dept_rel_lay=findViewById(R.id.dept_rel_lay);
        Spl_rel_lay=findViewById(R.id.Spl_rel_lay);
        subS_rel_lay=findViewById(R.id.subS_rel_lay);

        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.notification_white);
        title = findViewById(R.id.title);
        medicalProfile=findViewById(R.id.medicalProfile);
        Title=findViewById(R.id.Title);
        Specialization=findViewById(R.id.Specialization);
        SubSpl=findViewById(R.id.SubSpl);
        dept=findViewById(R.id.dept);
        experience = findViewById(R.id.experience);
        currentCtc = findViewById(R.id.currentCtc);
        expectedCtc = findViewById(R.id.expectedCtc);
        currentEmployer = findViewById(R.id.currentEmployer);
        jobLocation = findViewById(R.id.jobLocation);
        uploadResume = findViewById(R.id.uploadResume);
        copy_paste_resume = findViewById(R.id.copy_paste_resume);
        resumeImg = findViewById(R.id.resumeImg);
        btnApply = findViewById(R.id.btnApply);
        title.setText("Apply Job");
        btnSearch = findViewById(R.id.btnSearch);
        subS_view=findViewById(R.id.subS_view);
        spl_view=findViewById(R.id.spl_view);
        btnSearch.setVisibility(View.GONE);
        resumeImg.setOnClickListener(this);
        jobLocation.setOnClickListener(this);
        btnApply.setOnClickListener(this);
        getMetroCityApi();
        autosaveresume();

        try {
            GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        medical_profile_id = arrayList.get(0).getMedical_profile_id();

        medicalProfile.setText(arrayList.get(0).getMedical_profile_name());
        Title.setText(arrayList.get(0).getJob_title());
        Specialization.setText(arrayList.get(0).getSpecialization_name());
        SubSpl.setText(arrayList.get(0).getSub_specialization_name());
        dept.setText(arrayList.get(0).getDepartment_name());
        experience.setText(arrayList.get(0).getYear_of_experience());
        exp = Integer.parseInt(arrayList.get(0).getYear_of_experience());
        exp_lower =exp - 5;
        exp_upper =exp + 5;
        id = arrayList.get(0).getId();

        if(medical_profile_id.equalsIgnoreCase("1")|| medical_profile_id.equalsIgnoreCase("4")) {
            dept_rel_lay.setVisibility(View.GONE);
            Spl_rel_lay.setVisibility(View.VISIBLE);
            subS_rel_lay.setVisibility(View.VISIBLE);

        }else {
            dept_rel_lay.setVisibility(View.VISIBLE);
            Spl_rel_lay.setVisibility(View.GONE);
             subS_rel_lay.setVisibility(View.GONE);
             subS_view.setVisibility(View.GONE);
            spl_view.setVisibility(View.GONE);
        }

        //        akash commit changes
        flexboxDrawableWithClose = findViewById(R.id.flexbox_drawable_close);
        setUpChips();
        autosavelocationdata();
//        akash commit changes

    }



    public void setUpChips(){

        drawableWithCloseConfig = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#4AB5B0"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#4AB5B0"))
                .uncheckedTextColor(Color.parseColor("#ffffff"));


        drawableWithCloseChipCloud = new ChipCloud(ApplyJobFromPostedJobListActivity.this, flexboxDrawableWithClose, drawableWithCloseConfig);


        drawableWithCloseChipCloud.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {

//                cityChipsNameList.remove(index);
//                Log.e("chipsDeletePosion",index+","+label+","+cityChipsNameList.size());
            }
        });


    }
    private void addChipsData(String citys) {
        if(cityChipsNameList.size()<3){

            boolean checkBool=true;
            for(int i=0;i<cityChipsNameList.size();i++){
                if(cityChipsNameList.get(i).equals(citys.trim())){
                    checkBool=false;

                }
            }
            if(checkBool){
                cityChipsNameList.add(citys);
                drawableWithCloseChipCloud.addChip(citys);
            }else {
                ifAlreadyAdded();
            }


        }else {
            Toast.makeText(ApplyJobFromPostedJobListActivity.this, "Maximum 3 cities can be selected!", Toast.LENGTH_SHORT).show();
        }
    }
    private void ifAlreadyAdded() {
        Toast.makeText(ApplyJobFromPostedJobListActivity.this, "City Name Already Exists!", Toast.LENGTH_SHORT).show();
    }



    private void autosaveresume() {
//        AppCustomPreferenceClass.writeString(ResumeUpLoad.this,"pdf_name_code",resume_code);
//        AppCustomPreferenceClass.writeString(ResumeUpLoad.this,"pdf_name",resume_name);
////
//
        if(!(AppCustomPreferenceClass.readString(ApplyJobFromPostedJobListActivity.this,"pdf_name_code","").equals("") && AppCustomPreferenceClass.readString(ApplyJobFromPostedJobListActivity.this,"pdf_name","").equals(""))){

            uploadResume.setText(AppCustomPreferenceClass.readString(ApplyJobFromPostedJobListActivity.this,"pdf_name",""));
            uploadResume.setTextColor(Color.BLACK);


        }
    }

    private void autosavelocationdata(){


        location = arrayList.get(0).getLocation();
        Log.d("jobssss", location);
        String loc[] = location.split(",");
        for(int i =0;i<loc.length;i++){
            addChipsData(loc[i]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBack:
                finish();
                break;
            case R.id.resumeImg:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //TODO: enters here in case Permission is not granted
                    Log.d("entered", "here0");
                    //TODO: showing an explanation to user
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        MyToast.toastLong(this, "Application needs storage permission to upload resume");
                        Log.d("entered", "here1");
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PICK_PDF_REQUEST);
                        Log.d("entered", "here2");
                        // PICK_PDF_REQUEST is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    Log.d("entered", "here3");
                    openPdfIntent();
//                    //TODO : executed if the permission have already been granted
//                    //TODO : calling the upload resume method here
//                    if(uploadResume.getText().toString().equals("Upload Resume")){
//                        openPdfIntent();
//                    }
//                    else {
//                        //viewPdf();
//                    }
                break;
        }
            case R.id.jobLocation:

//                showDialog();

                break;
            case R.id.btnApply:

                if(isValid())
                {
                    if(email.equalsIgnoreCase("") || phoneNumber.equalsIgnoreCase(""))
                    {
                        MyToast.toastLong(this, "Please update mobile number before requesting a new Job");
                    }else
                    {
                        try {
                            RequestDirectJobPostApi(arrayList.get(0).getMedical_profile_id(),
                                    arrayList.get(0).getJob_title_id(),job_id,arrayList.get(0).getSpecialization_id(),
                                    arrayList.get(0).getSub_specialization_id(),arrayList.get(0).getDepartment_id());
                            // MyToast.toastShort(PostedJobDetailsActivity.this, "You apply for the job!");
                            //applyForRequestJobApi();
                            // btnApply.setClickable(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }


                break;
    }
    }
    private void openPdfIntent() {
        //TODO: calling the intent to open pdf
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
//        Intent intent = new Intent().setType("application/pdf").setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Pdf"),PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            Log.d("PickedPdfPath: ", fileUri.toString());
            //TODO: Calling the upload here
            if (!fileUri.equals(null)) {
                try{
                    getPdfPath();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    }
    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(this, header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(ApplyJobFromPostedJobListActivity.this, AppCustomPreferenceClass.sync_time, sync_time);

                    if (res_code.equals("1")) {
                        //   MyToast.toastLong(getActivity(), res_msg);
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");

                        item.getString("medical_profile_category_id");
                        item.getString("profile_image");
                        email = item.getString("email");
                        phoneNumber = item.getString("phone_number");

                    } else {
                        MyToast.toastLong(ApplyJobFromPostedJobListActivity.this, res_msg);
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
    private void Check_Package_StatusApi(final String medical_profile_id, final String job_title_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ApplyJobFromPostedJobListActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("plan_type", "1");
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new Check_Package_StatusApi(ApplyJobFromPostedJobListActivity.this, header) {
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
                        Intent intent = new Intent(ApplyJobFromPostedJobListActivity.this, Basic_Premimum_Activity.class);
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
    private boolean isValid() {
        boolean isValid = false;
      int  input_exp = Integer.parseInt(experience.getText().toString());
        if (TextUtils.isEmpty(experience.getText().toString())) {
                MyToast.toastLong(this, "Please Enter Years of Experience!");
                return false;
            } else if (!(input_exp >= exp_lower && input_exp <= exp_upper)) {
                MyToast.toastLong(this, "Please Input experience in between +5,-5 of the required experience for the job!");
                return false;
            } else if (TextUtils.isEmpty(currentCtc.getText().toString())) {
                MyToast.toastLong(this, "Please Enter Current CTC!");
                return false;
            } else if (TextUtils.isEmpty(expectedCtc.getText().toString())) {
                MyToast.toastLong(this, "Please Enter Expected CTC!");
                return false;
            } else if (TextUtils.isEmpty(currentEmployer.getText().toString())) {
                MyToast.toastLong(this, "Please Enter Current Employer!");
                return false;
            }
//            else if (TextUtils.isEmpty(jobLocation.getText().toString())) {
//
//            Log.e("cityChipsNameList",cityChipsNameList.toString().replace("[","").replace("]",""));
//                MyToast.toastLong(this, "Please provide Preferred Location!");
//                return false;
//            }
            else if (TextUtils.isEmpty(copy_paste_resume.getText().toString().trim()) && TextUtils.isEmpty(uploadResume.getText().toString().trim())) {
                MyToast.toastLong(this, "Please upload Resume OR write Cover letter!");
                return false;
            }
            else {

                isValid = true;
            }


        return isValid;
    }
    public void RequestDirectJobPostApi(String medicalId,String job_title,String job_id,String specializationId,String subspecializationId,String dept_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("medical_profile_id",medicalId);
        params.put("job_title",job_title);
        params.put("job_specialization_id",specializationId);
        params.put("sub_specialization_id",subspecializationId);
        params.put("department_id",dept_id);
        params.put("registration_no","");
        params.put("registration_state","");
        params.put("year_of_experience", experience.getText().toString());
        params.put("current_ctc", currentCtc.getText().toString());
        params.put("expected_ctc", expectedCtc.getText().toString());
        params.put("current_employer", currentEmployer.getText().toString());
//        params.put("preferred_job_location", jobLocation.getText().toString());
          params.put("preferred_job_location", location);
        params.put("preferred_for", "");
        params.put("post_jobs_id", job_id);
        params.put("resume_description", copy_paste_resume.getText().toString());
        if (pdf_name.trim().equals("")) {
            params.put("upload_resume", "sample.pdf");
        } else {
            params.put("upload_resume", pdf_name);
        }
        if(uploadResume.getText().toString().equals("")){
            Toast.makeText(ApplyJobFromPostedJobListActivity.this,"Please upload Resume",Toast.LENGTH_SHORT).show();
        }else{
            params.put("upload_resume", AppCustomPreferenceClass.readString(ApplyJobFromPostedJobListActivity.this,"pdf_name_code",""));
        }
        Log.d("RequestDirectJobPostApi", params + "");
        // params.put("job_description",desc.getText().toString());
        header.put("Cynapse", params);
        new RequestDirectJobPostApi(this,header)
        {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {

                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        Check_Package_StatusApi(arrayList.get(0).getMedical_profile_id(),arrayList.get(0).getJob_title_id());
                    } else {
                        MyToast.toastLong(ApplyJobFromPostedJobListActivity.this, res_msg);
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
    private void applyForRequestJobApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ApplyJobFromPostedJobListActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", job_id);
        params.put("medical_profile_id", arrayList.get(0).getMedical_profile_id());
        params.put("title_id", arrayList.get(0).getJob_title_id());
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        Log.d("applyForRequestJobApi",params+"");
        new applyForRequestJobApi(ApplyJobFromPostedJobListActivity.this, header) {
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
                        PostedJobDetailsActivity.fass.finish();
                        MyToast.toastLong(ApplyJobFromPostedJobListActivity.this, res_msg);
                    } else {

                        MyToast.toastLong(ApplyJobFromPostedJobListActivity.this, res_msg);
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
    public void showDialog() {
        final RecyclerView multi_city_sel_recycler;
        EditText loc_search;
        LinearLayoutManager linearLayoutManager;
        TextView cancel_txt, done_txt;
        final MetroCityAdapter menu_recycler_adapter;

        final Dialog dialog = new Dialog(ApplyJobFromPostedJobListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multi_city_selection_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        multi_city_sel_recycler = dialog.findViewById(R.id.multi_city_sel_recycler);
        cancel_txt = dialog.findViewById(R.id.cancel_txt);
        done_txt = dialog.findViewById(R.id.done_txt);
        loc_search = dialog.findViewById(R.id.loc_search);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_city_sel_recycler.setLayoutManager(linearLayoutManager);

        menu_recycler_adapter = new MetroCityAdapter(metrocitySelectedList, ApplyJobFromPostedJobListActivity.this, ApplyJobFromPostedJobListActivity.this);
        multi_city_sel_recycler.setAdapter(menu_recycler_adapter);
        loc_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (menu_recycler_adapter != null)
                    menu_recycler_adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();
        //TODO : dismiss the on btn click and close click
        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO : finishing the activity
            }
        });
        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            metrocityList = getModel(true);
//            Log.d("modelARRAYLIST",metrocityList+"");
//            MetroCityAdapter  recycler_adapter = new MetroCityAdapter(metrocityList,ApplyJobFromPostedJobListActivity.this,RequestJobFragment.this);
//            multi_city_sel_recycler.setAdapter(recycler_adapter);
                cityList.clear();
                for (int i = 0; i < metrocitySelectedList.size(); i++){

                    if (metrocitySelectedList.get(i).getStatus()) {
                        // jobLocation.setText(metrocitySelectedList.toString().replace("[","").replace("]",""));
                        cityList.add(metrocitySelectedList.get(i).getProfile_category_name());
                        Log.d("modelArrayList", metrocityList.get(i).getProfile_category_name() + "<><<" + metrocityList.get(i).getStatus());
                    }
//                jobLocation.setText(cityList.toString().replace("[", "").replace("]", ""));

                }

                for(int i=0;i<cityList.size();i++){
                    String listdata = cityList.get(i);
                    addChipsData(listdata);
                }
                dialog.dismiss();





            }
        });
    }

    private void getPdfPath() {
        //TODO: getting the pdf path and pdfFile name
        String pdfName;
        pdfPath = FilePath.getPath(ApplyJobFromPostedJobListActivity.this, fileUri);
        Log.d("pdfStringPath :", "<><<" + pdfPath);
        try {
            pdfName = pdfPath.substring(pdfPath.lastIndexOf("/") + 1);
            Log.d("pdfName :", pdfName);
            uploadResume.setText(pdfName);
            //TODO: calling the pdf upload here
            url = AppConstantClass.HOST+"fileUpload/resume";
            uploadFile(new File(pdfPath), url, pdfName, "file");
        } catch (NullPointerException ne) {
            //MyToast.toastLong(ApplyJobFromPostedJobListActivity.this, "You need to upload files which are stored locally!");
            ne.printStackTrace();
        }


    }

    //TODO: used to upload the pdf to the server
    void uploadFile(File file, final String url, String name, String type) {
        Log.e("file_name", ":" + file);
        PostImage post = new PostImage(file, url, name, ApplyJobFromPostedJobListActivity.this, type) {
            //            {
//                "Cynapse": {
//                "res_code": "1",
//                        "res_msg": "File Uploaded Successfuly.",
//                        "sync_time": 1521888468,
//                        "file_name": "0706444001521888468.pdf"
//            }
//            }
            @Override
            public void receiveData(String response) {
                try {
                    JSONObject response1 = new JSONObject(response);
                    JSONObject data = response1.getJSONObject("Cynapse");
                    MyToast.logMsg("jsonImage", data.toString());
                    String res = data.getString("res_code");
                    String res1 = data.getString("res_msg");
                    pdf_name = data.getString("file_name");
                    Log.e("pdfName", pdf_name);
                    if (res.equals("1")) {
                        MyToast.toastLong(ApplyJobFromPostedJobListActivity.this, "Your file have been uploaded successfully");
                    } else {
                        MyToast.toastLong(ApplyJobFromPostedJobListActivity.this, res1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void receiveError() {
                Log.e("PROFILE", "ERROR");
            }
        };

        post.execute(url, null, null);
    }


    private void getMetroCityApi() {
        new GetMetroCityApi(ApplyJobFromPostedJobListActivity.this) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    if (res_code.equals("1")) {
                        metrocityList.clear();
                        // medicalProfileSpinner.clear();
                        //MyToast.toastLong(ApplyJobFromPostedJobListActivity.this,res_msg);
                        JSONArray header2 = header.getJSONArray("MetroCity");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            MetroCityModel model = new MetroCityModel();
                            model.setId_city(item.getString("id"));
                            model.setProfile_category_name(item.getString("city_name"));
                            // model.setStatus(false);
                            metrocityList.add(model);
                            metrocitySelectedList = getModel(false);
//                            if (!((RequestPostJobActivity) Objects.requireNonNull(ApplyJobFromPostedJobListActivity.this)).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_METRO_CITY_MASTER,DatabaseHelper.id_city,String.valueOf(model.getId_city())))
//                            {
//
//                                ((RequestPostJobActivity)ApplyJobFromPostedJobListActivity.this).handler.AddMetroCityMaster(model, true);
//
//                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//                            } else {
//                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
//                                ((RequestPostJobActivity)ApplyJobFromPostedJobListActivity.this).handler.AddMetroCityMaster(model, false);
//                            }
//                            medicalProfileSpinner.add(item.getString("city_name"));
                            //Log.e("medicalListSize",String.valueOf(medicalProfileSpinner.size()));
                        }
                        //  metrocityList = ((RequestPostJobActivity)ApplyJobFromPostedJobListActivity.this).handler.getMetroCity(DatabaseHelper.TABLE_METRO_CITY_MASTER);
                    } else {
                        metrocityList.clear();
                        // medicalProfileSpinner.clear();
                        //MyToast.toastLong(ApplyJobFromPostedJobListActivity.this,res_msg);
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
    private ArrayList<MetroCityModel> getModel(boolean isSelect) {
        ArrayList<MetroCityModel> list = new ArrayList<>();
        for (int i = 0; i < metrocityList.size(); i++) {
            MetroCityModel model = new MetroCityModel();
            model.setStatus(isSelect);
            model.setId_city(metrocityList.get(i).getId_city());
            model.setProfile_category_name(metrocityList.get(i).getProfile_category_name());
            list.add(model);
        }
        return list;
    }

    @Override
    public void selectedCities(ArrayList<String> citylist) {

    }
}
