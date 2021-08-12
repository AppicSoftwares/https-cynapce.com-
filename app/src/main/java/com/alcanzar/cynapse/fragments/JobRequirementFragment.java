package com.alcanzar.cynapse.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.FilterClasses.JobRequirementFilter;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.activity.RequestPostJobActivity;
import com.alcanzar.cynapse.adapter.PostedJobListAdapter;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.JobSearchFilter;
import com.alcanzar.cynapse.api.PostedJobListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobRequirementFragment extends Fragment {
    Button btnCreate;
    RecyclerView recycleView_jobs;
    LinearLayoutManager job_lay_mgr;
    EditText edit_search_job;
    TextView no_record_txt, no_records_txt;
    String medicalId = "", phoneNumber = "", title_Id = "";
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<JobMasterModel> arrayList = new ArrayList<>();

    private final int REQUEST_CODE_FILTER = 1000;
    FloatingActionButton fabFilter;

    public JobRequirementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_requirement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCreate = view.findViewById(R.id.btnCreate);
        recycleView_jobs = view.findViewById(R.id.recycleView_jobs);
        edit_search_job = view.findViewById(R.id.edit_search_job);
        no_record_txt = view.findViewById(R.id.no_record_txt);
        no_records_txt = view.findViewById(R.id.no_records_txt);
        fabFilter = view.findViewById(R.id.fabFilter);
        job_lay_mgr = new LinearLayoutManager(getActivity());
        recycleView_jobs.setLayoutManager(job_lay_mgr);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        job_lay_mgr.setOrientation(LinearLayoutManager.VERTICAL);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        setColor(fabFilter);

        try {
            GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    GetProfileApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        arrayList = ((MainActivity)getActivity()).handler.getPostedJobList(DatabaseHelper.TABLE_POSTED_JOBS_MASTER, "","1");
//        if(arrayList.size() > 0) {
//            setData();
        // }


        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), JobRequirementFilter.class);
                startActivityForResult(intent, REQUEST_CODE_FILTER);

            }
        });

    }
//        try {
//            PostedJobListApi();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILTER) {

            if (data != null) {
                String JobId = data.getStringExtra("JobId");
                String medicalProfile = data.getStringExtra("medicalProfileId");
                String specialityChipsId = data.getStringExtra("specialityChipsId");
                String departmentChipsNameList = data.getStringExtra("departmentChipsNameList");
                String titleChipsNameList = data.getStringExtra("titleChipsNameList");
                String city_auto = data.getStringExtra("city_auto");
                String experience = data.getStringExtra("experience");
                String salary = data.getStringExtra("salary");
                getFilterData(JobId, medicalProfile, specialityChipsId
                        , departmentChipsNameList, titleChipsNameList
                        , city_auto, experience
                        , salary);
//                    Toast.makeText(getActivity(), data.getStringExtra("data"), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void getFilterData(String jobId, String medicalProfile, String specialityChipsId, String departmentChipsNameList, String titleChipsNameList, String city_auto, String experience, String salary) {

        JSONObject header = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("job_id", jobId);
            jsonObject.put("medical_profile_id", medicalProfile);
            jsonObject.put("others_medical_profile", "");
            jsonObject.put("specialization_id", specialityChipsId);
            jsonObject.put("department_id", departmentChipsNameList);
            jsonObject.put("title", titleChipsNameList);
            jsonObject.put("city", city_auto);
            jsonObject.put("work_experience", experience);
            jsonObject.put("min_salary", salary);
            header.put("Cynapse", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new JobSearchFilter(getContext(), header) {
            @Override
            public void responseApi(JSONObject response) {
                Log.e("responeSearch", response.toString());

                try {

                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sync_time_posted_jobs, sync_time);
                    Log.d("JOBSPONSE", response.toString());
                    arrayList.clear();

///// check commit
                    if (res_code.equals("1")) {
                        //  MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("PostJobList");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
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
                            model.setResume(item.getString("applied_status"));
                            model.setJob_description(item.getString("job_description"));
                            model.setSkills_required(item.getString("skills_required"));
//                            model.setAdd_date(item.getString("add_date"));
                            model.setAdd_date(item.getString("modify_date"));
                            model.setModify_date(Util.getDateNew_(Long.parseLong(item.getString("modify_date")) * 1000));
                            model.setStatus(item.getString("status"));
                            model.setDepartment_id(item.getString("department_id"));
                            model.setDepartment_name(item.getString("department_name"));
                            model.setPost_req_status("2");

                            if (!((MainActivity) getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_POSTED_JOBS_MASTER, DatabaseHelper.id, item.getString("id"))) {

                                ((MainActivity) getActivity()).handler.AddPostedJobMaster(model, true);

                                Log.e("ADDED_Sub_item", true + " " + model.getId());
                            } else {
                                Log.e("UPDATED", true + " " + model.getId());
                                ((MainActivity) getActivity()).handler.AddPostedJobMaster(model, false);
                            }

                            if (model.getStatus().equals("1"))
                                arrayList.add(model);
                            Log.e("acvdvad", arrayList.size() + "");

                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        setFilterData();

                    } else {
                        Toast.makeText(getContext(), res_msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };

    }

    private void setFilterData() {
        if (arrayList.size() == 0) {
            no_record_txt.setVisibility(View.VISIBLE);
            no_record_txt.setText("No Posted Jobs List Found");
            // no_record_txt.setVisibility(View.GONE);
        } else {
            no_record_txt.setVisibility(View.GONE);
            Log.e("menulistSize;", "<><><" + arrayList.size());
            final PostedJobListAdapter requestPostJobAdapter = new PostedJobListAdapter(arrayList, getActivity(), no_records_txt);
            recycleView_jobs.setAdapter(requestPostJobAdapter);

        }

    }

    public void setColor(FloatingActionButton view) {
        final int newColor = getResources().getColor(R.color.white);
        view.setColorFilter(newColor);
    }

    @Override
    public void onResume() {
        super.onResume();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                //akash changes
//                String phone= AppCustomPreferenceClass.readString(getActivity(),"phone_no1","");
//                String title =AppCustomPreferenceClass.readString(getActivity(),"title_id1","");
//                String medicalid= AppCustomPreferenceClass.readString(getActivity(),"medical_Id1","");
//                String countryid= AppCustomPreferenceClass.readString(getActivity(),"country_code1","");
//                String stateid= AppCustomPreferenceClass.readString(getActivity(),"state_id1","");
//                String cityid= AppCustomPreferenceClass.readString(getActivity(),"city_id1","");
//
//                Log.d("values1234",phone+"/"+title+"/"+medicalid+"/"+countryid+"/"+stateid+"/"+cityid);
//
////                title_Id=AppCustomPreferenceClass.readString(getActivity(),"titleid","");
//                if(phone.equalsIgnoreCase("")||medicalid.equalsIgnoreCase("") || title.equalsIgnoreCase("")|| countryid.equalsIgnoreCase("")||
//                        stateid.equalsIgnoreCase("") || cityid.equalsIgnoreCase(""))
//                {
//                    showDialog();
//                }else{
//                    startActivity(new Intent(getActivity(), RequestPostJobActivity.class));
//                }
                if (Util.isVerifiedProfile(getActivity())) {
                    if (AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                        try {
                            if (Util.isVerifiyEMailPHoneNO(getActivity())) {
                                startActivity(new Intent(getActivity(), RequestPostJobActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        startActivity(new Intent(getActivity(), RequestPostJobActivity.class));
                    }
                }
//                try {
//                    GetProfileShowDialogApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
//        try {
////            GetProfileApi();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void showDialog() {
        final Dialog dialogSociallog;
        Button btn_Submit;
        ImageView cross_btn;
        TextView disp_txt;
        dialogSociallog = new Dialog(getActivity());
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.dialog_social_login_details);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSociallog.setCancelable(false);
        cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        disp_txt = dialogSociallog.findViewById(R.id.disp_txt);
        disp_txt.setText("To continue posting or requesting jobs please fill mandatory fields in profile");
        btn_Submit = dialogSociallog.findViewById(R.id.btn_Submit);
        dialogSociallog.show();

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                intent.putExtra("edit_disable", "true");
                startActivity(intent);
                dialogSociallog.dismiss();
            }
        });

        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSociallog.dismiss();
            }
        });

    }

    private void GetProfileShowDialogApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(getActivity(), header, false) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    if (res_code.equals("1")) {
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");
                        if (item.getString("medical_profile_category_id").equalsIgnoreCase("")
                                || item.getString("title_id").equalsIgnoreCase("")) {
                            showSocialDialog();
                        } else {

                            startActivity(new Intent(getActivity(), RequestPostJobActivity.class));
                        }
                    } else {

                        //MyToast.toastLong(MainActivity.this,res_msg);
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

    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(getActivity(), header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sync_time, sync_time);

                    if (res_code.equals("1")) {
                        //MyToast.toastLong(getActivity(), res_msg);
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");
                        //
//                        for (int i = 0; i < medicalList.size(); i++) {
//                            if (medicalList.get(i).getProfile_category_name().equalsIgnoreCase(item.getString("medical_profile_category_name"))) {
//                                medicalProfile.setSelection(i);
//                            }
//                        }
                        //    AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.medical_profile_id,item.getString("medical_profile_category_id"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.medical_profile_name, item.getString("medical_profile_category_name"));
                        medicalId = item.getString("medical_profile_category_id");
                        title_Id = item.getString("title_id");
                        phoneNumber = item.getString("phone_number");
                        Log.d("MEFIIDIDID", medicalId);

                        // item.getString("medical_profile_category_name");
                        try {
                            PostedJobListApi(medicalId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //setData();

                    } else {

                        MyToast.toastLong(getActivity(), res_msg);
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

    public void PostedJobListApi(String medicalId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        //params.put("uuid", "S75f3");
        params.put("sync_time", "");
        params.put("medical_profile_id", medicalId);
        //params.put("sync_time", "");
        header.put("Cynapse", params);
        new PostedJobListApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sync_time_posted_jobs, sync_time);
                    Log.d("JOBSPONSEa", response.toString());

                    if (res_code.equals("1")) {
                        //  MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("PostJobList");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
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
                            model.setResume(item.getString("applied_status"));
                            model.setJob_description(item.getString("job_description"));
                            model.setSkills_required(item.getString("skills_required"));
                            model.setAdd_date(item.getString("add_date"));
                            //model.setAdd_date(item.getString("modify_date"));
                            model.setModify_date(Util.getDateNew_(Long.parseLong(item.getString("add_date")) * 1000));
                            model.setStatus(item.getString("status"));
                            model.setDepartment_id(item.getString("department_id"));
                            model.setDepartment_name(item.getString("department_name"));
                            model.setPost_req_status("2");

                            if (!((MainActivity) getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_POSTED_JOBS_MASTER, DatabaseHelper.id, item.getString("id"))) {

                                ((MainActivity) getActivity()).handler.AddPostedJobMaster(model, true);

                                Log.e("ADDED_Sub_item", true + " " + model.getId());
                            } else {
                                Log.e("UPDATED", true + " " + model.getId());
                                ((MainActivity) getActivity()).handler.AddPostedJobMaster(model, false);
                            }

                            //arrayList.add(model);
                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                        arrayList.clear();
                        setData();
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                        //setData();
                        no_record_txt.setVisibility(View.VISIBLE);
                        no_record_txt.setText("No Posted Jobs List Found");
                        //  MyToast.toastLong(getActivity(),res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyToast.toastLong(getActivity(), e.getMessage());

                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
                MyToast.toastLong(getActivity(), error.getMessage());
            }
        };
    }

    public void setData() {
        arrayList = ((MainActivity) getActivity()).handler.getPostedJobList(DatabaseHelper.TABLE_POSTED_JOBS_MASTER, "", "1");

        if (arrayList.size() == 0) {
            no_record_txt.setVisibility(View.VISIBLE);
            no_record_txt.setText("No Posted Jobs List Found");
            // no_record_txt.setVisibility(View.GONE);
        } else {
            no_record_txt.setVisibility(View.GONE);
            Log.e("menulist.size();", "<><><" + arrayList.size());
            final PostedJobListAdapter requestPostJobAdapter = new PostedJobListAdapter(arrayList, getActivity(), no_records_txt);
            recycleView_jobs.setAdapter(requestPostJobAdapter);
            edit_search_job.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (requestPostJobAdapter != null)
                        requestPostJobAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }


    }

    public void showSocialDialog() {
        final Dialog dialogSociallog;
        Button btn_Submit;
        ImageView cross_btn;
        dialogSociallog = new Dialog(getActivity());
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.dialog_social_login_details);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSociallog.setCancelable(false);

        cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        btn_Submit = dialogSociallog.findViewById(R.id.btn_Submit);
        dialogSociallog.show();
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
                dialogSociallog.dismiss();

            }
        });

        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSociallog.dismiss();
            }
        });

    }
}
