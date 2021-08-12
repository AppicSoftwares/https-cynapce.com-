package com.alcanzar.cynapse.FilterClasses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.HelperClasses.GetCityClass;
import com.alcanzar.cynapse.HelperClasses.GetCountryClass;
import com.alcanzar.cynapse.HelperClasses.GetDepartmentClass;
import com.alcanzar.cynapse.HelperClasses.GetMedicalProfileClass;
import com.alcanzar.cynapse.HelperClasses.GetStateClass;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.GetTitleApi;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.model.StateModel;
import com.android.volley.VolleyError;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipDeletedListener;

public class JobRequirementFilter extends AppCompatActivity implements View.OnClickListener {

    ImageView titleIcon;
    TextView title;
    AutoCompleteTextView country_auto, state_auto, city_auto, medicalProfile_auto, departmentListAuto, Title_auto, Spealization_auto;
    private ArrayList<CountryModel> countryList;
    private ArrayList<StateModel> stateList;
    private ArrayList<CityModel> cityList = new ArrayList<>();
    private ArrayList<MedicalProfileModel> medicalList;
    private ArrayList<MedicalProfileModel> departmentList;

    private ArrayList<String> medicalProfileList, departmentNameList;
    private ArrayList<String> tempMedicalProfileList = new ArrayList<>();
    ArrayList<String> countryListName, stateListName, cityListName, cityChipsNameList, specialityChipsNameList, specialityChipsId, departmentChipsNameList, departmentChipsId, titleChipsId, titleChipsNameList;
    ArrayList<String> tempCountryListName = new ArrayList<>();
    ArrayList<String> tempStateListName = new ArrayList<>();
    ArrayList<String> tempStateListCode = new ArrayList<>();
    ArrayList<String> tempCityListName = new ArrayList<>();

    ArrayList<String> tempDepartmentNameList = new ArrayList<>();
    ArrayList<JobSpecializationModel> titleList = new ArrayList<>();

    ArrayList<String> jobSpecializationSpinner = new ArrayList<>();
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    ArrayList<String> specializationName;
    ArrayList<String> tempSplzation;
    ArrayList<String> TitleName;
    ArrayList<String> tempTitle;
    Button filterJobButtton;
    EditText jobId, experience, salary;

    ArrayAdapter country_adapter, state_adapter, medical_profile_adapter, reg_state_adapter, city_adapter, adapter_Yos, department_adapter, adapter_title, adapter_specialization;
    String country_id = "", titleStr = "", title_Id = "", country_str = "", state_id = "", reg_state_id = "", state_Str = "", city_id = "", city_str = "", Yos_ID = "",
            yosStr = "", regStateStr = "", edit_disable = "", medStr, medicalId = "", splStr, specializationId, departmentId;

    FlexboxLayout flexboxDrawableWithClose, flexbox_multiSpeciality, flexbox_multiDepartment, flexbox_multiTitle;
    ChipCloudConfig drawableWithCloseConfig, flexbox_multiSpecialityConfig, flexbox_multiDepartmentConfig, flexbox_multiTitleConfig;
    ChipCloud drawableWithCloseChipCloud, flexbox_multiSpecialityCloudChip, flexbox_multiDepartmentCloudChip, flexbox_multiTitleCloudChip;
    RelativeLayout departmentAutoLayout, SpealizationLayout, titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_requirement_filter);
        initialisation();

        getMedicalProfileList();
        getAllCountry();
        statesetupListener();
        setUpCityListener();
        setUpChips();

        setUpChipsSpecialisation();
        setUpChipsDepartment();
        setUpChipsTitle();
        setUpOnClickLister();

        try {
            getProfileSpecialization("");
            setUpSpecialization();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpOnClickLister() {
        country_auto.setOnClickListener(this);
        state_auto.setOnClickListener(this);
        city_auto.setOnClickListener(this);
        medicalProfile_auto.setOnClickListener(this);
        departmentListAuto.setOnClickListener(this);
        Title_auto.setOnClickListener(this);
        Spealization_auto.setOnClickListener(this);
        country_auto.setOnClickListener(this);
        state_auto.setOnClickListener(this);
        filterJobButtton.setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initialisation() {
        titleIcon = findViewById(R.id.titleIcon);
        title = findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        titleIcon.setImageDrawable(getResources().getDrawable(R.drawable.find_job_icon_));
        title.setText("JOB SEARCH");
        setColor(titleIcon);
        flexboxDrawableWithClose = findViewById(R.id.flexbox_drawable_close);
        flexbox_multiSpeciality = findViewById(R.id.flexbox_multiSpeciality);
        flexbox_multiDepartment = findViewById(R.id.flexbox_multiDepartment);
        flexbox_multiTitle = findViewById(R.id.flexbox_multiTitle);

        cityChipsNameList = new ArrayList<>();
        specialityChipsNameList = new ArrayList<>();
        specialityChipsId = new ArrayList<>();

        departmentChipsNameList = new ArrayList<>();
        departmentChipsId = new ArrayList<>();
        titleChipsNameList = new ArrayList<>();
        titleChipsId = new ArrayList<>();
        country_auto = findViewById(R.id.country_auto);
        state_auto = findViewById(R.id.state_auto);
        city_auto = findViewById(R.id.city_auto);
        departmentAutoLayout = findViewById(R.id.departmentAutoLayout);
        SpealizationLayout = findViewById(R.id.SpealizationLayout);
        titleLayout = findViewById(R.id.titleLayout);
        medicalProfile_auto = findViewById(R.id.medicalProfile_auto);

        departmentListAuto = findViewById(R.id.departmentListAuto);
        Title_auto = findViewById(R.id.Title_auto);
        Spealization_auto = findViewById(R.id.Spealization_auto);
        filterJobButtton = findViewById(R.id.filterJobButtton);
        jobId = findViewById(R.id.jobId);
        experience = findViewById(R.id.experience);
        salary = findViewById(R.id.salary);


    }

    private void setUpSpecialization() {

        Spealization_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (Spealization_auto.getText().toString().equals("")) {
                            tempSplzation = new ArrayList<>();
                            tempSplzation.addAll(specializationName);
                        }
                        adapter_specialization = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                        Spealization_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
                    if (Spealization_auto.toString().equals("")) {

                    } else {

                    }

                }

            }

        });

        Spealization_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempSplzation = new ArrayList<>();
                        tempSplzation.addAll(specializationName);

                        adapter_specialization = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

                }

            }
        });

        Spealization_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                splStr = Spealization_auto.getText().toString();
                try {
                    specializationId = specializationList.get(specializationName.indexOf(splStr)).getSpecialization_id();
                    addChipsSpecialityData(splStr, specializationId);
                    Spealization_auto.setText("");
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

//
//                try {
//                    GetSubSpecializationApi(specializationId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (specializationId.equalsIgnoreCase("-3")) {
//                    other_spl_rel_lay.setVisibility(View.VISIBLE);
//
//                } else {
//                    other_spl_rel_lay.setVisibility(View.GONE);
//                }

                Log.d("specializationId", specializationId);
            }
        });
    }

    public void setColor(ImageView view) {
        final int newColor = getResources().getColor(R.color.white);
        view.setColorFilter(newColor);
    }

    private void getMedicalProfileList() {
        new GetMedicalProfileClass(this) {
            @Override
            protected void responseMedicalProfile(ArrayList<MedicalProfileModel> medicalNameList) {
                medicalList = medicalNameList;
                medicalProfileList = new ArrayList<>();
                Iterator iterator = medicalList.iterator();
                while (iterator.hasNext()) {
                    MedicalProfileModel medicalProfileModel = (MedicalProfileModel) iterator.next();
                    medicalProfileList.add(medicalProfileModel.getProfile_category_name());
                    Log.e("medicalCount", medicalProfileList.size() + ",");
                }
            }
        };

        medicalProfile_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tempMedicalProfileList.clear();
                    tempMedicalProfileList.addAll(medicalProfileList);
                    Log.e("focusState", medicalProfileList.size() + "");
                    medical_profile_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempMedicalProfileList);
                    medicalProfile_auto.setAdapter(medical_profile_adapter);
                    medicalProfile_auto.showDropDown();
                }
            }
        });

        medicalProfile_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                medStr = medicalProfile_auto.getText().toString();
                try {
                    medicalId = medicalList.get(medicalProfileList.indexOf(medStr)).getId();

                    Log.e("asdfads", medicalId);
                    if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
                        departmentAutoLayout.setVisibility(View.VISIBLE);
                        getAllDepartment(medicalId);
                        departmentAutoLayout.setVisibility(View.VISIBLE);
                        SpealizationLayout.setVisibility(View.GONE);


                    } else {
                        departmentAutoLayout.setVisibility(View.GONE);
                        SpealizationLayout.setVisibility(View.VISIBLE);
                    }
                    setupTitle();

                    if (medicalId.equalsIgnoreCase("-1")) {

                        try {
                            getTitle("13");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            getTitle(medicalId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    titleLayout.setVisibility(View.VISIBLE);

                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
            }
        });

        medicalProfile_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {

                        tempMedicalProfileList.clear();
                        tempMedicalProfileList.addAll(medicalProfileList);
                        Log.e("focusState", medicalProfileList.size() + "");
                        medical_profile_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempMedicalProfileList);
                        medicalProfile_auto.setAdapter(medical_profile_adapter);
                        medicalProfile_auto.showDropDown();

                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setupTitle() {
        Title_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (Title_auto.getText().toString().equals("")) {
                        tempTitle = new ArrayList<>();
                        try {
                            tempTitle.addAll(TitleName);
                            adapter_title = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                            Title_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }

                    }


                } else {
                    if (Title_auto.toString().equals("")) {

                    } else {

                    }

                }


            }
        });

        Title_auto.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {

                    tempTitle = new ArrayList<>();
                    try {
                        tempTitle.addAll(TitleName);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                    adapter_title = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempTitle);
                    Title_auto.setAdapter(adapter_title);


                } else {

                }

            }
        });

        Title_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                titleStr = Title_auto.getText().toString();

                try {
                    title_Id = titleList.get(TitleName.indexOf(titleStr)).getSpecialization_id();
                    addChipsTitle(titleStr, title_Id);
                    Title_auto.setText("");
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
//                edit_title_others.setText("");
//                if (title_Id.equalsIgnoreCase("-2")) {
//                    other_title_rel_lay.setVisibility(View.VISIBLE);
//
//                } else {
//                    other_title_rel_lay.setVisibility(View.GONE);
//                }


            }
        });
    }

    private void getAllDepartment(String medicalId) {
        new GetDepartmentClass(this, medicalId) {
            @Override
            protected void responseDepartment(ArrayList<MedicalProfileModel> dept_SpinnerList) {

                departmentList = dept_SpinnerList;
                departmentNameList = new ArrayList<>();

                Iterator iterator = departmentList.iterator();

                while (iterator.hasNext()) {
                    MedicalProfileModel departmentModal = (MedicalProfileModel) iterator.next();
                    departmentNameList.add(departmentModal.getProfile_category_name());
                }
            }
        };


        departmentListAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    tempDepartmentNameList.clear();
                    tempDepartmentNameList.addAll(departmentNameList);
                    Log.e("focusState", departmentNameList.size() + "");
                    department_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempDepartmentNameList);
                    departmentListAuto.setAdapter(department_adapter);
                    departmentListAuto.showDropDown();
                }
            }
        });

        departmentListAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                medStr = departmentListAuto.getText().toString();
                try {
                    departmentId = departmentList.get(departmentNameList.indexOf(medStr)).getId();
                    Log.e("sdhbsd", medStr);
                    addChipsDepartment(medStr, departmentId);
                    departmentListAuto.setText("");
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
            }
        });

        departmentListAuto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {

                        tempDepartmentNameList.clear();
                        tempDepartmentNameList.addAll(departmentNameList);
                        Log.e("focusState", departmentNameList.size() + "");
                        department_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempDepartmentNameList);
                        departmentListAuto.setAdapter(department_adapter);
                        departmentListAuto.showDropDown();

                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void getTitle(String profile_category_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("sync_time", "");
        params.put("profile_category_id", profile_category_id);
        header.put("Cynapse", params);
        new GetTitleApi(JobRequirementFilter.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        //titleSpinner.clear();
                        titleList.clear();
                        JSONArray header2 = header.getJSONArray("Title");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            titleList.add(new JobSpecializationModel(item.getString("title_id"),
                                    item.getString("profile_category_id"), item.getString("title")));
                            //  titleSpinner.add(item.getString("title"));

                        }
                        titleList.add(new JobSpecializationModel("-2", "Others", "Others"));
//                        Log.e("titleSize",String.valueOf(titleSpinner.size()));
//                        ArrayAdapter<String> adapter =new ArrayAdapter<>(MyProfileActivity.this,android.R.layout.simple_spinner_item
//                                ,titleSpinner);
//                        title_dd.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
                        if (titleList.size() > 0) {

                            TitleName = new ArrayList<>();
                            tempTitle = new ArrayList<>();
                            for (int j = 0; j < titleList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempTitle.add(titleList.get(j).getSpecialization_name());
                                TitleName.add(titleList.get(j).getSpecialization_name());
                            }

                            adapter_title = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                            Title_auto.setThreshold(1);
                        }

                    } else {
                        // titleSpinner.clear();
                        //  titleList.clear();
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

    private void getAllCountry() {
        new GetCountryClass(this) {
            @Override
            protected void responseCountry(ArrayList<CountryModel> countryNameList) {
                countryList = countryNameList;
                countryListName = new ArrayList<>();


                Iterator iterator = countryList.iterator();

                while (iterator.hasNext()) {
                    CountryModel countryModel = (CountryModel) iterator.next();
                    countryListName.add(countryModel.getCountry_name());

                }
            }
        };


        country_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    try {
                        Log.e("focusCountry", countryListName.size() + "");
                        if (country_auto.getText().toString().equals("")) {
                            tempCountryListName.clear();
                            tempCountryListName.addAll(countryListName);
                        }


                        country_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCountryListName);
                        country_auto.setAdapter(country_adapter);
                        country_auto.showDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        country_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country_str = country_auto.getText().toString();
                try {
                    country_id = countryList.get(countryListName.indexOf(country_str)).getCountry_code();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                getAllState(country_id);
                state_auto.setText("");
                clearCities();
                Log.d("countryID", country_id);
            }
        });

        country_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("textCheck", s.toString());
                if (s.toString().equals("")) {
                    try {
                        Log.e("textCheck2", s.toString());

                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                        country_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCountryListName);
                        country_auto.setAdapter(country_adapter);
                        country_auto.showDropDown();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void statesetupListener()
    {
        state_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    try {
                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                        Log.e("focusState", stateListName.size() + "");
                        state_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempStateListName);
                        state_auto.setAdapter(state_adapter);
                        state_auto.showDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        state_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state_Str = state_auto.getText().toString();
                int num = stateListName.toString().indexOf(state_Str);

                for (int i = 0; i < stateListName.size(); i++) {
                    if (stateListName.get(i).equals(state_Str)) {
                        num = i;
                        break;
                    }
                }
                Log.e("focusStateId", stateList.size() + "," + num + "," + tempStateListCode.size() + "," + state_Str);
                try {
                    state_id = stateList.get(num).getState_code();
                    country_id = stateList.get(num).getCountry_code();
                    Log.e("focusStateId", state_id);
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                city_auto.setText("");
                clearCities();
                getAllCity(country_id, state_id);
                Log.d("stateId", state_id);
            }
        });

        state_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {

                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                        state_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempStateListName);
                        state_auto.setAdapter(state_adapter);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void clearCities()
    {
            cityChipsNameList.clear();
            flexboxDrawableWithClose.removeAllViews();
            Log.e("chipsDeletePosion##", "" + cityChipsNameList.size());
    }

    private void getAllCity(String country_id, String state_id) {
        new GetCityClass(this, country_id, state_id) {
            @Override
            protected void responseCity(ArrayList<CityModel> responseCity) {
                cityList = responseCity;
                cityListName = new ArrayList<>();

                Iterator iterator = cityList.iterator();

                while (iterator.hasNext()) {
                    CityModel countryModel = (CityModel) iterator.next();
                    cityListName.add(countryModel.getCity_name());
                    Log.e("CityCount", cityListName.size() + "");

                }

            }
        };


    }

    private void setUpCityListener() {
        cityChipsNameList.clear();
        city_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    try {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                        Log.e("focusState", cityListName.size() + "");
                        city_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCityListName);
                        city_auto.setAdapter(city_adapter);
                        city_auto.showDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        city_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city_str = city_auto.getText().toString();
                try {
                    try {

                        city_id = cityList.get(cityListName.indexOf(city_str)).getCity_id();
                        city_auto.setText("");
                        addChipsData(city_str);
                        city_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCityListName);
                        city_auto.setAdapter(city_adapter);
                        city_auto.showDropDown();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                Log.d("city_id", city_id);


            }
        });

        city_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                        city_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCityListName);
                        city_auto.setAdapter(city_adapter);
                        city_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpChips() {

        drawableWithCloseConfig = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#4AB5B0"))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .showClose(Color.parseColor("#ffffff"), 500);

        drawableWithCloseChipCloud = new ChipCloud(this, flexboxDrawableWithClose, drawableWithCloseConfig);


        drawableWithCloseChipCloud.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {

                cityChipsNameList.remove(index);
                Log.e("chipsDeletePosion", index + "," + label + "," + cityChipsNameList.size());
            }
        });
    }

    private void setUpChipsSpecialisation() {

        flexbox_multiSpecialityConfig = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#4AB5B0"))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .showClose(Color.parseColor("#ffffff"), 500);

        flexbox_multiSpecialityCloudChip = new ChipCloud(this, flexbox_multiSpeciality, flexbox_multiSpecialityConfig);


        flexbox_multiSpecialityCloudChip.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {
                specialityChipsNameList.remove(index);
                specialityChipsId.remove(index);
                Log.e("chipsDeletePosion", index + "," + label + "," + specialityChipsNameList.size());
            }
        });
    }

    private void setUpChipsDepartment() {

        flexbox_multiDepartmentConfig = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#4AB5B0"))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .showClose(Color.parseColor("#ffffff"), 500);

        flexbox_multiDepartmentCloudChip = new ChipCloud(this, flexbox_multiDepartment, flexbox_multiDepartmentConfig);


        flexbox_multiDepartmentCloudChip.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {

                departmentChipsNameList.remove(index);
                departmentChipsId.remove(index);
                Log.e("chipsDeletePosion", index + "," + label + "," + departmentChipsNameList.size());
            }
        });
    }

    private void setUpChipsTitle() {

        flexbox_multiTitleConfig = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#4AB5B0"))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .showClose(Color.parseColor("#ffffff"), 500);

        flexbox_multiTitleCloudChip = new ChipCloud(this, flexbox_multiTitle, flexbox_multiTitleConfig);


        flexbox_multiTitleCloudChip.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {

                titleChipsNameList.remove(index);
                titleChipsId.remove(index);
                Log.e("chipsDeletePosion", index + "," + label + "," + titleChipsNameList.size());
            }
        });
    }

    private void addChipsTitle(String title, String title_Id) {
        if (titleChipsNameList.size() < 3) {
            boolean checkBool = true;
            for (int i = 0; i < titleChipsNameList.size(); i++) {
                if (titleChipsNameList.get(i).equals(title)) {
                    checkBool = false;
                }
            }
            if (checkBool) {
                titleChipsNameList.add(title);
                titleChipsId.add(title_Id);
                flexbox_multiTitleCloudChip.addChip(title);
            } else {
                ifAlreadyAdded();
            }
        }
    }

    private void addChipsDepartment(String depat, String departmentId) {

        if (departmentChipsNameList.size() < 3) {
            boolean checkBool = true;
            for (int i = 0; i < departmentChipsNameList.size(); i++) {
                if (departmentChipsNameList.get(i).equals(depat)) {
                    checkBool = false;

                }
            }
            if (checkBool) {
                departmentChipsNameList.add(depat);
                departmentChipsId.add(departmentId);
                flexbox_multiDepartmentCloudChip.addChip(depat);
            } else {
                ifAlreadyAdded();
            }

        } else {
            Toast.makeText(this, "More Then 3 not select!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addChipsSpecialityData(String splStr, String specialityId) {
        if (specialityChipsNameList.size() < 3) {
            boolean checkBool = true;
            for (int i = 0; i < specialityChipsNameList.size(); i++) {
                if (specialityChipsNameList.get(i).equals(splStr)) {
                    checkBool = false;
                }
            }
            if (checkBool) {
                specialityChipsNameList.add(splStr);
                specialityChipsId.add(specialityId);
                flexbox_multiSpecialityCloudChip.addChip(splStr);
            } else {
                ifAlreadyAdded();
            }

        } else {
            Toast.makeText(this, "More Then 3 not select!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addChipsData(String citys) {

        if (cityChipsNameList.size() < 3) {

            boolean checkBool = true;
            for (int i = 0; i < cityChipsNameList.size(); i++) {
                if (cityChipsNameList.get(i).equals(citys.trim())) {
                    checkBool = false;

                }
            }
            if (checkBool) {
                cityChipsNameList.add(citys);
                drawableWithCloseChipCloud.addChip(citys);
            } else {
                ifAlreadyAdded();
            }


        } else {
            Toast.makeText(this, "More Then 3 not select!", Toast.LENGTH_SHORT).show();
        }
    }

    private void ifAlreadyAdded() {
        Toast.makeText(this, "Name already added!", Toast.LENGTH_SHORT).show();
    }

    private void getAllState(String country_id) {

        new GetStateClass(this, country_id) {
            @Override
            protected void responseState(ArrayList<StateModel> responseState) {
                stateList = responseState;
                stateListName = new ArrayList<>();

                Iterator iterator = stateList.iterator();

                while (iterator.hasNext()) {
                    StateModel countryModel = (StateModel) iterator.next();
                    tempStateListCode.add(countryModel.getState_code());
                    stateListName.add(countryModel.getState_name());

                }

            }
        };
    }

    private void getProfileSpecialization(String id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("profile_category_id", id);
        header.put("Cynapse", params);
        new GetSpecializationApi(JobRequirementFilter.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        //jobSpecializationSpinner.clear();
                        specializationList.clear();
                        JSONArray header2 = header.getJSONArray("ProfileSpecializationMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            specializationList.add(new JobSpecializationModel(item.getString("specialization_id"),
                                    item.getString("profile_category_id"), item.getString("specialization_name")));
                            //     jobSpecializationSpinner.add(item.getString("specialization_name"));
                            Log.e("jobSpecializationSize", String.valueOf(jobSpecializationSpinner.size()));
                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MyProfileActivity.this, android.R.layout.simple_spinner_item
//                                , jobSpecializationSpinner);
//                        jobSpecialization.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
                        specializationList.add(new JobSpecializationModel("-3", "1", "Others"));

                        if (specializationList.size() > 0) {

                            specializationName = new ArrayList<>();
                            tempSplzation = new ArrayList<>();
                            for (int j = 0; j < specializationList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempSplzation.add(specializationList.get(j).getSpecialization_name());
                                specializationName.add(specializationList.get(j).getSpecialization_name());
                            }

                            adapter_specialization = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempSplzation);
                            Spealization_auto.setAdapter(adapter_specialization);
                            Spealization_auto.setThreshold(1);

                        }
                    } else {
                        // jobSpecializationSpinner.clear();
                        // specializationList.clear();
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.medicalProfile_auto:
                try {
                    if (medicalProfile_auto.getText().toString().equals("")) {
                        tempMedicalProfileList.clear();
                        tempMedicalProfileList.addAll(medicalProfileList);
                    }
                    Log.e("focusState", medicalProfileList.size() + "");
                    medical_profile_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempMedicalProfileList);
                    medicalProfile_auto.setAdapter(medical_profile_adapter);
                    medicalProfile_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;
            case R.id.Spealization_auto:
                try {
                    if (Spealization_auto.getText().toString().equals("")) {
                        tempSplzation = new ArrayList<>();
                        tempSplzation.addAll(specializationName);
                    }

                    adapter_specialization = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
                    Spealization_auto.setAdapter(adapter_specialization);
                    Spealization_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.Title_auto:
                if (Title_auto.getText().toString().equals("")) {
                    tempTitle.clear();
                    tempTitle.addAll(TitleName);
                }
                try {
                    tempTitle.addAll(TitleName);
                    adapter_title = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_my_profile, R.id.lbl_name, tempTitle);
                    Title_auto.setAdapter(adapter_title);
                    Title_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }


                break;
            case R.id.state_auto:
                try {

                    if (state_auto.getText().toString().equals("")) {
                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                    }
                    Log.e("focusState", stateListName.size() + "");
                    state_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempStateListName);
                    state_auto.setAdapter(state_adapter);
                    state_auto.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.country_auto:
                try {
                    if (country_auto.getText().toString().equals("")) {
                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                    }
                    country_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCountryListName);
                    country_auto.setAdapter(country_adapter);
                    country_auto.showDropDown();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.city_auto:
                try {
                    if (city_auto.getText().toString().equals("")) {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                    }

                    city_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCityListName);
                    city_auto.setAdapter(city_adapter);
                    city_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.departmentListAuto:
                try {

                    if (departmentListAuto.getText().toString().equals("")) {
                        tempDepartmentNameList.clear();
                        tempDepartmentNameList.addAll(departmentNameList);
                    }

                    Log.e("focusState", departmentNameList.size() + "");
                    department_adapter = new Adapter_Filter(JobRequirementFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempDepartmentNameList);
                    departmentListAuto.setAdapter(department_adapter);
                    departmentListAuto.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.filterJobButtton:
                try {

                    Log.e("JobId", jobId.getText().toString() + "");
                    Log.e("medicalProfileId", medicalId + "");
                    Log.e("specialityChipsId", "" + specialityChipsId.toString().replace("[", "").replace("]", ""));
                    Log.e("departmentChipsNameList", "" + departmentChipsId.toString().replace("[", "").replace("]", ""));
                    Log.e("titleChipsNameList", "" + titleChipsId.toString().replace("[", "").replace("]", ""));
                    Log.e("city_auto", "" + cityChipsNameList.toString().replace("[", "").replace("]", ""));
                    Log.e("experience", "" + experience.getText().toString());
                    Log.e("salary", "" + salary.getText().toString());
                    Intent intent = new Intent();

                    intent.putExtra("JobId", "" + jobId.getText().toString());
                    intent.putExtra("medicalProfileId", "" + medicalId);
                    intent.putExtra("specialityChipsId", "" + specialityChipsId.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("departmentChipsNameList", "" + departmentChipsId.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("titleChipsNameList", "" + titleChipsId.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("city_auto", "" + cityChipsNameList.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("experience", "" + experience.getText().toString());
                    intent.putExtra("salary", "" + salary.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }


//    public void toastCheck(View view) {
//
//        Intent intent=new Intent();
//        intent.putExtra("data","Success");
//        setResult(Activity.RESULT_OK,intent);
//        finish();
//
//    }
}
