package com.alcanzar.cynapse.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.HelperClasses.GetCityClass;
import com.alcanzar.cynapse.HelperClasses.GetCountryClass;
import com.alcanzar.cynapse.HelperClasses.GetStateClass;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MetroCityInterface;
import com.alcanzar.cynapse.activity.RequestPostJobActivity;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.adapter.MetroCityAdapter;
import com.alcanzar.cynapse.api.GetDepartmentApi;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.api.GetMetroCityApi;
import com.alcanzar.cynapse.api.GetPerfferedDataApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.GetSubSpecializationApi;
import com.alcanzar.cynapse.api.GetTitleApi;
import com.alcanzar.cynapse.api.PostJobApi;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.model.MetroCityModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PostJobFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, MetroCityInterface {

    //TODO: different layout views
    Spinner medicalProfile, jobTitle, jobSpecialization, subSpecialization, dept_dd, preferFor_dd;
    String getProfileId = "";
    private String medicalId = "", specializationId = "", jobStr, subSpecializationStr = "", subspecializationId = "", dept_id = "", job_id = "", preferStr = "", prefer_Id = "", getProfileName = "";
    ArrayList<String> medicalProfileSpinner = new ArrayList<>();
    ArrayList<String> jobSpecializationSpinner = new ArrayList<>();
    ArrayList<MedicalProfileModel> medicalList = new ArrayList<>();
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    ArrayList<String> titleSpinner = new ArrayList<>();
    ArrayList<String> subSpecializationSpinner = new ArrayList<>();
    ArrayList<String> dept_Spinner = new ArrayList<>();
    ArrayList<MedicalProfileModel> subspecializationList = new ArrayList<>();
    ArrayList<MedicalProfileModel> PrefList = new ArrayList<>();
    ArrayList<MedicalProfileModel> dept_SpinnerList = new ArrayList<>();
    ArrayList<JobSpecializationModel> titleList = new ArrayList<>();
    EditText skillRequired, preferFor, ctc, vacancy, desc, edit_others, edit_pref_others,
            edit_title_others, edit_dept_others, edit_spl_others, edit_Sub_spl_others, experience, registration_no;
    Button btnCreate;
    RelativeLayout dept_rel_lay, Spl_rel_lay, subS_rel_lay, other_rel_lay, other_title_rel_lay, other_spl_rel_lay, other_pref_rel_lay,
            other_sub_spl_rel_lay, other_dept_rel_lay;
    View dept_view, spl_view, subS_view;
    TextView location;
    ArrayList<MetroCityModel> metrocityList = new ArrayList<>();
    ArrayList<MetroCityModel> metrocitySelectedList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();

    ArrayAdapter adapter_medical_profile, adapter_specialization, adapter_title, adapter_dept, adapter_subSpl, adapter_PreferredFor;
    AutoCompleteTextView medicalProfileauto, Spealization_auto, PreferredFor_auto, state_auto, city_auto, Title_auto, SubS_auto, dept_auto;
    ImageButton crossC, crossS, crossHd, crossCity, crossState, crossCount, crossTitle, cross_dept;
    private String medStr = "", splStr = "", hdStr = "", title_Id = "", titleStr = "", dept_Id = "", dept_Str = "", subSplStr = "", PrefStr = "", PrefId = "", subSpecializationId = "";
    ArrayList<String> medicalProfileName;
    ArrayList<String> specializationName;
    ArrayList<String> tempMedProf;
    ArrayList<String> tempSplzation;
    ArrayList<String> titleIdid;
    ArrayList<String> TitleName;

    ArrayList<String> PrefName;
    ArrayList<String> tempTitle;
    ArrayList<String> DeptName;
    ArrayList<String> tempDept;
    ArrayList<String> SubSName;
    ArrayList<String> tempSubS;
    ArrayList<String> tempPref;
    LinearLayout reg_lin_lay;
    String email = "", phoneNumber = "";

    //akash

    AutoCompleteTextView country_auto1, state_auto1, city_auto1;
    String state_id = "", state_Str = "";
    ArrayList<StateModel> stateList = new ArrayList<>();

    // akash


    //    akash commit changes

    ChipCloudConfig drawableWithCloseConfig, flexbox_multiSpecialityConfig, flexbox_multiDepartmentConfig, flexbox_multiTitleConfig;
    ChipCloud drawableWithCloseChipCloud;
    FlexboxLayout flexboxDrawableWithClose, flexbox_multiSpeciality, flexbox_multiDepartment, flexbox_multiTitle;


    private ArrayList<CountryModel> countryList;

    private ArrayList<CityModel> cityList1 = new ArrayList<>();

    String city_id = "";

    ArrayAdapter country_adapter, state_adapter1, city_adapter;

    String country_id = "", country_str = "", city_str = "",
            regStateStr = "", edit_disable = "";
    ArrayList<String> countryListName, stateListName, cityListName;

    ArrayList<String> tempCountryListName = new ArrayList<>();
    ArrayList<String> tempStateListName = new ArrayList<>();
    ArrayList<String> tempStateListCode = new ArrayList<>();
    ArrayList<String> tempCityListName = new ArrayList<>();
    ArrayList<String> cityChipsNameList = new ArrayList<>();


    //    akash commit changes

    public PostJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_job, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: initializing all of the views
        initializeView(view);
        //TODO: clearing the list data
        clearListData();
    }

    private void initializeView(View view) {
        //TODO: initializing the views
        btnCreate = view.findViewById(R.id.btnCreate);
        //TODO: spinners tab
        medicalProfile = view.findViewById(R.id.medicalProfile);
        jobTitle = view.findViewById(R.id.jobTitle);
        jobSpecialization = view.findViewById(R.id.jobSpecialization);
        subSpecialization = view.findViewById(R.id.subSpecialization);
        reg_lin_lay = view.findViewById(R.id.reg_lin_lay);
        other_pref_rel_lay = view.findViewById(R.id.other_pref_rel_lay);


        PreferredFor_auto = view.findViewById(R.id.PreferredFor_auto);
        medicalProfileauto = view.findViewById(R.id.medicalProfileauto);
        Spealization_auto = view.findViewById(R.id.Spealization_auto);
        Title_auto = view.findViewById(R.id.Title_auto);
        crossC = view.findViewById(R.id.crossC);
        crossS = view.findViewById(R.id.crossS);
        crossHd = view.findViewById(R.id.crossHd);
        SubS_auto = view.findViewById(R.id.SubS_auto);
        dept_auto = view.findViewById(R.id.dept_auto);
        other_rel_lay = view.findViewById(R.id.other_rel_lay);
        other_title_rel_lay = view.findViewById(R.id.other_title_rel_lay);
        other_spl_rel_lay = view.findViewById(R.id.other_spl_rel_lay);
        other_sub_spl_rel_lay = view.findViewById(R.id.other_sub_spl_rel_lay);
        other_dept_rel_lay = view.findViewById(R.id.other_dept_rel_lay);
        edit_others = view.findViewById(R.id.edit_others);
        edit_title_others = view.findViewById(R.id.edit_title_others);
        edit_dept_others = view.findViewById(R.id.edit_dept_others);
        edit_spl_others = view.findViewById(R.id.edit_spl_others);
        edit_Sub_spl_others = view.findViewById(R.id.edit_Sub_spl_others);

        crossC = view.findViewById(R.id.crossC);
        cross_dept = view.findViewById(R.id.cross_dept);
        crossTitle = view.findViewById(R.id.crossTitle);


        //TODO: this is used to setUpThe Spinners
        // setUpSpinner();
        edit_pref_others = view.findViewById(R.id.edit_pref_others);
        location = view.findViewById(R.id.location);
        experience = view.findViewById(R.id.experience);
        registration_no = view.findViewById(R.id.registration_no);
        skillRequired = view.findViewById(R.id.skillRequired);
        preferFor = view.findViewById(R.id.preferFor);
        preferFor_dd = view.findViewById(R.id.preferFor_dd);
        ctc = view.findViewById(R.id.ctc);
        vacancy = view.findViewById(R.id.vacancy);
        desc = view.findViewById(R.id.desc);
        btnCreate.setOnClickListener(this);
        dept_dd = view.findViewById(R.id.dept_dd);
        dept_rel_lay = view.findViewById(R.id.dept_rel_lay);
        Spl_rel_lay = view.findViewById(R.id.Spl_rel_lay);
        subS_rel_lay = view.findViewById(R.id.subS_rel_lay);
        dept_view = view.findViewById(R.id.dept_view);
        spl_view = view.findViewById(R.id.spl_view);
        subS_view = view.findViewById(R.id.subS_view);
        //TODO: making the spinner items selectable
        medicalProfile.setOnItemSelectedListener(this);
        jobTitle.setOnItemSelectedListener(this);
        jobSpecialization.setOnItemSelectedListener(this);
        subSpecialization.setOnItemSelectedListener(this);
        dept_dd.setOnItemSelectedListener(this);
        preferFor_dd.setOnItemSelectedListener(this);

        medicalProfileauto.setOnClickListener(this);
        Spealization_auto.setOnClickListener(this);
        SubS_auto.setOnClickListener(this);
        PreferredFor_auto.setOnClickListener(this);
        Title_auto.setOnClickListener(this);
        dept_auto.setOnClickListener(this);
        //TODO: listing data calling
        getMedicalProfileApi();
        setUpSpinnerPreferred();


        //akash commit changes


        country_auto1 = view.findViewById(R.id.country_auto);
        state_auto1 = view.findViewById(R.id.state_auto1);
        city_auto1 = view.findViewById(R.id.city_auto);
        flexboxDrawableWithClose = view.findViewById(R.id.flexbox_drawable_close);


        country_auto1.setOnClickListener(this);
        state_auto1.setOnClickListener(this);
        city_auto1.setOnClickListener(this);


        getAllCountry();
        statesetupListener();
        setUpCityListener();
        setUpChips();

//        akash commit changes

        try {
            GetProfileApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getProfileSpecialization("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getMetroCityApi();
        try {
            GetPerfferedDataApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        medicalProfileauto.requestFocus();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        medicalProfileauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((RequestPostJobActivity) getActivity()).medicalProfileautoclick = 1;

                    if (medicalProfileauto.getText().toString().equals("")) {
                        tempMedProf = new ArrayList<>();
                        tempMedProf.addAll(medicalProfileName);
                    }
                    adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempMedProf);
                    medicalProfileauto.setAdapter(adapter_medical_profile);
                    medicalProfileauto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }
        });

        medicalProfileauto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");
                    //   crossC.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (medicalProfileauto.getText().toString().equals("")) {
                            tempMedProf = new ArrayList<>();
                            tempMedProf.addAll(medicalProfileName);
                        }
                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempMedProf);
                        medicalProfileauto.setAdapter(adapter_medical_profile);

                        if (((RequestPostJobActivity) getActivity()).medicalProfileautoclick == 1)
                            medicalProfileauto.showDropDown();

                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }


                } else {
//                    if (medicalProfileauto.toString().equals("")) {
//                        crossC.setVisibility(View.GONE);
//                    } else{
//                        crossC.setVisibility(View.VISIBLE);
//                    }
                }
            }

        });

        medicalProfileauto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempMedProf = new ArrayList<>();
                        tempMedProf.addAll(medicalProfileName);

                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempMedProf);
                        medicalProfileauto.setAdapter(adapter_medical_profile);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                    // crossC.setVisibility(View.GONE);
                }

            }
        });

        medicalProfileauto.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                medStr = medicalProfileauto.getText().toString();
                medicalId = medicalList.get(medicalProfileName.indexOf(medStr)).getId();
                Log.e("svadbadbas",medicalId);
                Title_auto.setText("");
                Spealization_auto.setText("");
                SubS_auto.setText("");
                dept_auto.setText("");
                edit_others.setText("");
                edit_spl_others.setText("");
                edit_Sub_spl_others.setText("");
                edit_dept_others.setText("");
                edit_title_others.setText("");
                if (medicalId.equalsIgnoreCase("-1")) {
                    other_rel_lay.setVisibility(View.VISIBLE);
                    try {
                        Title_auto.setText("");
                        Spealization_auto.setText("");
                        SubS_auto.setText("");
                        dept_auto.setText("");
                        getTitle("13",0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    other_rel_lay.setVisibility(View.GONE);
                    try {
                        Title_auto.setText("");
                        Spealization_auto.setText("");
                        SubS_auto.setText("");
                        dept_auto.setText("");
                        if (medicalId.equalsIgnoreCase("59"))
                            getTitle("13",0);
                            getTitle(medicalId,0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("MEDICALIDZZ", medicalId);
//                Log.d("MEDICALIDZZ222",getProfileId);
//                if(medicalId.equals(""))
//                {
//                  medicalId=getProfileId;
//                }
//                if(medicalId.equalsIgnoreCase("1"))
//                {
//                    reg_lin_lay.setVisibility(View.VISIBLE);
//                }else
//                {
//                    reg_lin_lay.setVisibility(View.GONE);
//                }
                if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
                    try {
                        GetDepartmentApi(medicalId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dept_rel_lay.setVisibility(View.VISIBLE);
                    dept_view.setVisibility(View.VISIBLE);
                    Spl_rel_lay.setVisibility(View.GONE);
                    spl_view.setVisibility(View.GONE);
                    subS_rel_lay.setVisibility(View.GONE);
                    subS_view.setVisibility(View.GONE);
                    Spealization_auto.setVisibility(View.GONE);
                    SubS_auto.setVisibility(View.GONE);
                    dept_auto.setVisibility(View.VISIBLE);
                } else {
                    dept_rel_lay.setVisibility(View.GONE);
                    dept_view.setVisibility(View.GONE);
                    Spl_rel_lay.setVisibility(View.VISIBLE);
                    spl_view.setVisibility(View.VISIBLE);
                    subS_rel_lay.setVisibility(View.VISIBLE);
                    subS_view.setVisibility(View.VISIBLE);
                    Spealization_auto.setVisibility(View.VISIBLE);
                    SubS_auto.setVisibility(View.VISIBLE);
                    dept_auto.setVisibility(View.GONE);
                }
                Log.d("medicalId", medicalId);

            }
        });


        Title_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //if (Title_auto.getText().toString().equals("")) {

                    if (checkMedicalProfileType(medicalProfileauto.getText().toString())) {
                        getTitle("13",1);
                    } else {
                        tempTitle = new ArrayList<>();
                        try {
                            tempTitle.addAll(TitleName);

                            adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                            Title_auto.showDropDown();

                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }
                    //}

                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Title_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (checkMedicalProfileType(medicalProfileauto.getText().toString())) {
                        try {
                            getTitle("13",1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        tempTitle = new ArrayList<>();
                        try {
                            tempTitle.addAll(TitleName);
                            adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                            Title_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }
                    //}
//                    Log.e("in ", "Country");
//
//                    //   crossTitle.setVisibility(View.VISIBLE);
//                    // stateC.setVisibility(View.GONE);
//                    //  cityC.setVisibility(View.GONE);
//                    try {
//                        if (Title_auto.getText().toString().equals("")) {
//                            tempTitle = new ArrayList<>();
//                            try {
//                                tempTitle.addAll(TitleName);
//                            } catch (NullPointerException ne) {
//                                ne.printStackTrace();
//                            }
//
//                        }
//                        adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempTitle);
//                        Title_auto.setAdapter(adapter_title);
//                        Title_auto.showDropDown();
//                    } catch (NullPointerException ne) {
//                        ne.printStackTrace();
//                    }
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
                    try {
                        tempTitle = new ArrayList<>();
                        tempTitle.addAll(TitleName);
                        adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempTitle);
                        Title_auto.setAdapter(adapter_title);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                    // crossTitle.setVisibility(View.GONE);
                } else {
                    //  crossTitle.setVisibility(View.VISIBLE);
                }

            }
        });

        Title_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                try {
                    titleStr = Title_auto.getText().toString().trim();
                    title_Id = titleList.get(TitleName.indexOf(titleStr)).getSpecialization_id();
                    Log.e("adfsadv", titleStr + "####" + title_Id + ",##" + titleList.size());
                    if (title_Id.equalsIgnoreCase("-2")) {
                        other_title_rel_lay.setVisibility(View.VISIBLE);
                    } else {
                        other_title_rel_lay.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dept_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (dept_auto.getText().toString().equals("")) {
                        tempDept = new ArrayList<>();

                        try {
                            tempDept.addAll(DeptName);
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }
                    adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempDept);
                    dept_auto.setAdapter(adapter_dept);
                    dept_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }
        });

        dept_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // cross_dept.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    try {
                        if (dept_auto.getText().toString().equals("")) {
                            tempDept = new ArrayList<>();

                            try {
                                tempDept.addAll(DeptName);
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            }
                        }
                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                        dept_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                }
            }
        });

        dept_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {

                    try {
                        tempDept = new ArrayList<>();
                        tempDept.addAll(DeptName);

                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                    //  cross_dept.setVisibility(View.GONE);
                } else {
                    // cross_dept.setVisibility(View.VISIBLE);
                }

            }
        });
        dept_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                dept_Str = dept_auto.getText().toString();
                dept_Id = dept_SpinnerList.get(DeptName.indexOf(dept_Str)).getId();
                if (dept_Id.equalsIgnoreCase("-5")) {
                    other_dept_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_dept_rel_lay.setVisibility(View.GONE);
                }


            }
        });

        Spealization_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (Spealization_auto.getText().toString().equals("")) {
                        tempSplzation = new ArrayList<>();
                        try {
                            tempSplzation.addAll(specializationName);
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }
                    adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSplzation);
                    Spealization_auto.setAdapter(adapter_specialization);
                    Spealization_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }
        });

        Spealization_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    //  crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (Spealization_auto.getText().toString().equals("")) {
                            tempSplzation = new ArrayList<>();
                            try {
                                tempSplzation.addAll(specializationName);
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            }
                        }
                        adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                        Spealization_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
//                    if (Spealization_auto.toString().equals("")) {
//                        crossS.setVisibility(View.GONE);
//                    } else {
//                        crossS.setVisibility(View.VISIBLE);
//                    }

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

                        adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                    //crossS.setVisibility(View.GONE);
                }

            }
        });

        Spealization_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                splStr = Spealization_auto.getText().toString();
                specializationId = specializationList.get(specializationName.indexOf(splStr)).getSpecialization_id();
                try {
                    GetSubSpecializationApi(specializationId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (specializationId.equalsIgnoreCase("-3")) {
                    other_spl_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_spl_rel_lay.setVisibility(View.GONE);
                }

                Log.d("specializationId", specializationId);
            }
        });

        SubS_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (SubS_auto.getText().toString().equals("")) {
                        tempSubS = new ArrayList<>();
                        try {
                            tempSubS.addAll(SubSName);
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }
                    adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSubS);
                    SubS_auto.setAdapter(adapter_subSpl);
                    SubS_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }
        });

        SubS_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    try {
                        if (SubS_auto.getText().toString().equals("")) {
                            tempSubS = new ArrayList<>();
                            try {
                                tempSubS.addAll(SubSName);
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            }
                        }

                        adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSubS);
                        SubS_auto.setAdapter(adapter_subSpl);
                        SubS_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (SubS_auto.toString().equals("")) {
                        //  crossS.setVisibility(View.GONE);
                    } else {
                        //  crossS.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        SubS_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempSubS = new ArrayList<>();
                        tempSubS.addAll(SubSName);

                        adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSubS);
                        SubS_auto.setAdapter(adapter_subSpl);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                    //  crossS.setVisibility(View.GONE);
                } else {
                    // crossS.setVisibility(View.VISIBLE);
                }

            }
        });

        SubS_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                subSplStr = SubS_auto.getText().toString();
                try {
                    subSpecializationId = subspecializationList.get(SubSName.indexOf(subSplStr)).getId();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                if (subSpecializationId.equalsIgnoreCase("-4")) {
                    other_sub_spl_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_sub_spl_rel_lay.setVisibility(View.GONE);
                }

                Log.d("specializationId", specializationId);
            }
        });

        PreferredFor_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    try {
                        if (PreferredFor_auto.getText().toString().equals("")) {
                            tempPref = new ArrayList<>();
                            try {
                                tempPref.addAll(PrefName);
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            }

                        }
                        adapter_PreferredFor = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempPref);
                        PreferredFor_auto.setAdapter(adapter_PreferredFor);
                        PreferredFor_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }


                } else {
                    if (PreferredFor_auto.toString().equals("")) {
                        //  crossS.setVisibility(View.GONE);
                    } else {
                        //  crossS.setVisibility(View.VISIBLE);
                    }

                }


            }

        });

        PreferredFor_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempPref = new ArrayList<>();
                        tempPref.addAll(PrefName);

                        adapter_PreferredFor = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempPref);
                        PreferredFor_auto.setAdapter(adapter_PreferredFor);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                    //  crossS.setVisibility(View.GONE);
                } else {
                    // crossS.setVisibility(View.VISIBLE);
                }

            }
        });

        PreferredFor_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                PrefStr = PreferredFor_auto.getText().toString();

                Log.d("PrefName.indexOf",PrefStr);

                try {
                    PrefId = PrefList.get(PrefName.indexOf(PrefStr)).getId();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                Log.d("PrefName.indexOf1",PrefId);

                if (PrefId.equalsIgnoreCase("-6")) {
                    other_pref_rel_lay.setVisibility(View.VISIBLE);
                } else {
                    other_pref_rel_lay.setVisibility(View.GONE);
                }

                Log.d("specializationId", specializationId);
            }
        });
    }

    private void clearListData() {
        medicalList.clear();
        medicalList.clear();
        specializationList.clear();
        medicalProfileSpinner.clear();
        jobSpecializationSpinner.clear();
        subspecializationList.clear();
        subSpecializationSpinner.clear();
        titleList.clear();
        titleSpinner.clear();
        dept_Spinner.clear();
        dept_SpinnerList.clear();
    }

    private boolean checkMedicalProfileType(String str) {
        boolean b = false;

        if (str.equals("Doctor") || str.equals("Medical Student") || str.equals("Nurse") || str.equals("Paramedical") || str.equals("Administrative and Support")) {
            b = false;
        } else {
            b = true;
        }
        return b;
    }

    private void setUpSpinnerPreferred() {
        //TODO: for the prefer For
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getActivity(), R.array.preferForArray, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preferFor_dd.setAdapter(adapter4);
    }

    //TODO: api calling
    private void getMedicalProfileApi() {
        new GetMedicalProfileApi(getActivity()) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    if (res_code.equals("1")) {
                        medicalList.clear();
                        medicalProfileSpinner.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("ProfileCategoryMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            medicalList.add(new MedicalProfileModel(item.getString("id"), item.getString("profile_category_name")));
                            // medicalProfileSpinner.add(item.getString("profile_category_name"));
                            //Log.e("medicalListSize",String.valueOf(medicalProfileSpinner.size()));
                        }
                        medicalList.add(new MedicalProfileModel("-1", "Others"));
//                        try{
//                            medicalProfile.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, medicalProfileSpinner));
//                        }
//                        catch (NullPointerException ne)
//                        {
//                            ne.printStackTrace();
//                        }
                        if (medicalList.size() > 0) {

                            medicalProfileName = new ArrayList<>();
                            tempMedProf = new ArrayList<>();
                            for (int j = 0; j < medicalList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempMedProf.add(medicalList.get(j).getProfile_category_name());
                                medicalProfileName.add(medicalList.get(j).getProfile_category_name());
                            }

                            adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempMedProf);
                            medicalProfileauto.setAdapter(adapter_medical_profile);
                            medicalProfileauto.setThreshold(1);
                        }
                    } else {
                        medicalList.clear();
                        medicalProfileSpinner.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
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

        final Dialog dialog = new Dialog(getActivity());
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
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_city_sel_recycler.setLayoutManager(linearLayoutManager);

        menu_recycler_adapter = new MetroCityAdapter(metrocitySelectedList, getActivity(), PostJobFragment.this);
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
//            MetroCityAdapter  recycler_adapter = new MetroCityAdapter(metrocityList,getActivity(),RequestJobFragment.this);
//            multi_city_sel_recycler.setAdapter(recycler_adapter);
                cityList.clear();
                for (int i = 0; i < metrocitySelectedList.size(); i++)

                    if (metrocitySelectedList.get(i).getStatus()) {
                        // jobLocation.setText(metrocitySelectedList.toString().replace("[","").replace("]",""));
                        cityList.add(metrocitySelectedList.get(i).getProfile_category_name());
                        Log.d("modelArrayList", metrocityList.get(i).getProfile_category_name() + "<><<" + metrocityList.get(i).getStatus());
                    }
                location.setText(cityList.toString().replace("[", "").replace("]", ""));
                dialog.dismiss();
            }
        });
    }

    private void getProfileSpecialization(String id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("profile_category_id", id);
        header.put("Cynapse", params);
        new GetSpecializationApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        jobSpecializationSpinner.clear();
                        specializationList.clear();
                        JSONArray header2 = header.getJSONArray("ProfileSpecializationMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            specializationList.add(new JobSpecializationModel(item.getString("specialization_id"),
                                    item.getString("profile_category_id"), item.getString("specialization_name")));
                            //jobSpecializationSpinner.add(item.getString("specialization_name"));
                            Log.e("jobSpecializationSize", String.valueOf(jobSpecializationSpinner.size()));
                        }
                        specializationList.add(new JobSpecializationModel("-3", "1", "Others"));

                        if (specializationList.size() > 0) {

                            specializationName = new ArrayList<>();
                            tempSplzation = new ArrayList<>();
                            for (int j = 0; j < specializationList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempSplzation.add(specializationList.get(j).getSpecialization_name());
                                specializationName.add(specializationList.get(j).getSpecialization_name());
                            }

                            adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSplzation);
                            Spealization_auto.setAdapter(adapter_specialization);
                            Spealization_auto.setThreshold(1);

                        }
                        //MyToast.toastLong(getActivity(),res_msg);
                    } else {
                        jobSpecializationSpinner.clear();
                        specializationList.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
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

    private void getTitle(final String profile_category_id,final  int click) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("sync_time", "");
        params.put("profile_category_id", profile_category_id);
        header.put("Cynapse", params);
        new GetTitleApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        titleSpinner.clear();
                        titleList.clear();
                        JSONArray header2 = header.getJSONArray("Title");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            titleList.add(new JobSpecializationModel(item.getString("title_id"),
                                    item.getString("profile_category_id"), item.getString("title")));
                            // titleSpinner.add(item.getString("title"));
                            Log.e("titleSize", String.valueOf(titleSpinner.size()));
                        }
                        Log.e("titleSize", String.valueOf(titleSpinner.size()));
                        titleList.add(new JobSpecializationModel("-2", "Others", "Others"));
//                        ArrayAdapter<String> adapter =new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item
//                                ,titleSpinner);
//                        title_dd.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(getActivity(),res_msg);
                        if (titleList.size() > 0) {

                            TitleName = new ArrayList<>();
                            tempTitle = new ArrayList<>();
                            titleIdid = new ArrayList<>();

                            for (int j = 0; j < titleList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempTitle.add(titleList.get(j).getSpecialization_name().trim());
                                TitleName.add(titleList.get(j).getSpecialization_name().trim());
                                titleIdid.add(titleList.get(j).getSpecialization_id().trim());
                            }

                            adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                            Title_auto.setThreshold(1);

                            if (profile_category_id.equals("13") && !medicalId.equals("-1"))
                                Title_auto.showDropDown();

                            if(click==1)
                                Title_auto.showDropDown();
                        }

                    } else {
                        titleSpinner.clear();
                        titleList.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
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

    private void getMetroCityApi() {
        new GetMetroCityApi(getActivity()) {
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
                        //MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("MetroCity");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            MetroCityModel model = new MetroCityModel();
                            model.setId_city(item.getString("id"));
                            model.setProfile_category_name(item.getString("city_name"));
                            // model.setStatus(false);
                            metrocityList.add(model);
                            metrocitySelectedList = getModel(false);
//                            if (!((RequestPostJobActivity) Objects.requireNonNull(getActivity())).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_METRO_CITY_MASTER,DatabaseHelper.id_city,String.valueOf(model.getId_city())))
//                            {
//
//                                ((RequestPostJobActivity)getActivity()).handler.AddMetroCityMaster(model, true);
//
//                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//                            } else {
//                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
//                                ((RequestPostJobActivity)getActivity()).handler.AddMetroCityMaster(model, false);
//                            }
//                            medicalProfileSpinner.add(item.getString("city_name"));
                            //Log.e("medicalListSize",String.valueOf(medicalProfileSpinner.size()));
                        }
                        //  metrocityList = ((RequestPostJobActivity)getActivity()).handler.getMetroCity(DatabaseHelper.TABLE_METRO_CITY_MASTER);
                    } else {
                        metrocityList.clear();
                        // medicalProfileSpinner.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
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

    private void GetSubSpecializationApi(String specializationId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("specialization_id", specializationId);
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetSubSpecializationApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        subSpecializationSpinner.clear();
                        subspecializationList.clear();
                        JSONArray header2 = header.getJSONArray("SubSpecialization");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            subspecializationList.add(new MedicalProfileModel(item.getString("id"), item.getString("sub_specialization_name")));
                            //subSpecializationSpinner.add(item.getString("sub_specialization_name"));
                            Log.e("subSSize", String.valueOf(subSpecializationSpinner.size()));
                        }
                        subspecializationList.add(new MedicalProfileModel("-4", "Others"));
                        if (subspecializationList.size() > 0) {

                            SubSName = new ArrayList<>();
                            tempSubS = new ArrayList<>();
                            for (int j = 0; j < subspecializationList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempSubS.add(subspecializationList.get(j).getProfile_category_name());
                                SubSName.add(subspecializationList.get(j).getProfile_category_name());
                            }

                            adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.setThreshold(1);
                        }
                        //MyToast.toastLong(getActivity(),res_msg);
                    } else {
                        //subSpecializationSpinner.clear();
                        subspecializationList.clear();
                        subspecializationList.add(new MedicalProfileModel("-4", "Others"));
                        if (subspecializationList.size() > 0) {

                            SubSName = new ArrayList<>();
                            tempSubS = new ArrayList<>();
                            for (int j = 0; j < subspecializationList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempSubS.add(subspecializationList.get(j).getProfile_category_name());
                                SubSName.add(subspecializationList.get(j).getProfile_category_name());
                            }

                            adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.setThreshold(1);
                        }
                        //MyToast.toastLong(getActivity(),res_msg);
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

    private void GetDepartmentApi(String medicalId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("medical_profile_id", medicalId);
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetDepartmentApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        dept_Spinner.clear();
                        dept_SpinnerList.clear();
                        JSONArray header2 = header.getJSONArray("Department");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            dept_SpinnerList.add(new MedicalProfileModel(item.getString("id"), item.getString("department_name")));
                            //dept_Spinner.add(item.getString("department_name"));
                            Log.e("subSSize", String.valueOf(dept_Spinner.size()));
                        }
                        dept_SpinnerList.add(new MedicalProfileModel("-5", "Others"));
                        if (dept_SpinnerList.size() > 0) {

                            DeptName = new ArrayList<>();
                            tempDept = new ArrayList<>();
                            for (int j = 0; j < dept_SpinnerList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempDept.add(dept_SpinnerList.get(j).getProfile_category_name());
                                DeptName.add(dept_SpinnerList.get(j).getProfile_category_name());
                            }

                            adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempDept);
                            dept_auto.setAdapter(adapter_dept);
                            dept_auto.setThreshold(1);
                        }
                    } else {
                        dept_Spinner.clear();
                        dept_SpinnerList.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
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

    private void GetPerfferedDataApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetPerfferedDataApi(getActivity(), header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                Log.e("preferrdLock", response.toString());
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        PrefList.clear();
                        JSONArray header2 = header.getJSONArray("Preffered");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            PrefList.add(new MedicalProfileModel(item.getString("id"), item.getString("preffered_for")));
                            //dept_Spinner.add(item.getString("department_name"));
                            Log.e("subSSize", String.valueOf(dept_Spinner.size()));
                        }
                        PrefList.add(new MedicalProfileModel("-6", "Others"));
                        if (PrefList.size() > 0) {

                            PrefName = new ArrayList<>();
                            tempPref = new ArrayList<>();
                            for (int j = 0; j < PrefList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempPref.add(PrefList.get(j).getProfile_category_name());
                                PrefName.add(PrefList.get(j).getProfile_category_name());
                            }

                            adapter_PreferredFor = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempPref);
                            PreferredFor_auto.setAdapter(adapter_PreferredFor);
                            PreferredFor_auto.setThreshold(1);
                        }
                    } else {
                        PrefList.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
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

    private void postJobApi() throws JSONException {
//                {
//                    "Cynapse": {
//                    "uuid":"S3994",
//                            "medical_profile_id":"1",
//                            "job_title":"Doctor",
//                            "job_specilization_id":"2",
//                            "sub_specilization":"test",
//                            "location":"lucknow",
//                            "skill_required":"operation expert",
//                            "preffered_for":"lko",
//                            "ctc":"40000",
//                            "no_of_vaccancies":"10",
//                            "job_description" :"It hiering for doctor requierment"
//
//                }
//                }
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        //params.put("medical_profile_id",medicalId);


        if (medStr.equalsIgnoreCase("Others")) {
            params.put("medical_profile_id", "othermedicalprofile");
            params.put("others_medical_profile", edit_others.getText().toString());
        } else {

            params.put("medical_profile_id", medicalId);
            params.put("others_medical_profile", "");
        }
        if (titleStr.equalsIgnoreCase("Others")) {
            params.put("job_title", "othertitle");
            params.put("others_title", edit_title_others.getText().toString());
        } else {

            params.put("job_title", title_Id);
            params.put("others_title", "");
        }
        if (splStr.equalsIgnoreCase("Others")) {
            params.put("job_specialization_id", "otherspecialization");
            params.put("others_specialization", edit_spl_others.getText().toString());
        } else {

            params.put("job_specialization_id", specializationId);
            params.put("others_specialization", "");
        }
        if (subSplStr.equalsIgnoreCase("Others")) {
            params.put("sub_specialization", "othersubspecialization");
            params.put("others_sub_specialization", edit_Sub_spl_others.getText().toString());
        } else {

            params.put("sub_specialization", subSpecializationId);
            params.put("others_sub_specialization", "");
        }
        if (dept_Str.equalsIgnoreCase("Others")) {
            params.put("department_id", "otherdepartment");
            params.put("others_department", edit_dept_others.getText().toString());
        } else {

            params.put("department_id", dept_Id);
            params.put("others_department", "");
        }
        if (PrefStr.equalsIgnoreCase("Others")) {
            params.put("preffered_for", "othersprefferedfor");
            params.put("preffered_for_others", edit_pref_others.getText().toString());
        } else {

            params.put("preffered_for", PrefId);
            params.put("preffered_for_others", "");
        }
        params.put("registration_no", "");
        params.put("year_of_experience", experience.getText().toString());
        params.put("location", cityChipsNameList.toString().replace("[", "").replace("]", ""));
        params.put("skill_required", skillRequired.getText().toString());
        //params.put("preffered_for", prefer_Id);
        params.put("ctc", ctc.getText().toString());
        params.put("no_of_vaccancies", vacancy.getText().toString());
        params.put("job_description", desc.getText().toString());
        Log.d("POSTJOBREQUEST", params + "");
        header.put("Cynapse", params);
        new PostJobApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        MyToast.toastLong(getActivity(), res_msg);
                        getActivity().finish();
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
//    private void postJobApi() throws JSONException {
////                {
////                    "Cynapse": {
////                    "uuid":"S3994",
////                            "medical_profile_id":"1",
////                            "job_title":"Doctor",
////                            "job_specilization_id":"2",
////                            "sub_specilization":"test",
////                            "location":"lucknow",
////                            "skill_required":"operation expert",
////                            "preffered_for":"lko",
////                            "ctc":"40000",
////                            "no_of_vaccancies":"10",
////                            "job_description" :"It hiering for doctor requierment"
////
////                }
////                }
//        JSONObject header = new JSONObject();
//        JSONObject params = new JSONObject();
//        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
//        //params.put("medical_profile_id",medicalId);
//
//
//        if (medStr.equalsIgnoreCase("Others")) {
//            params.put("medical_profile_id", "othermedicalprofile");
//            params.put("others_medical_profile", edit_others.getText().toString());
//        } else {
//
//            params.put("medical_profile_id", medicalId);
//            params.put("others_medical_profile", "");
//        }
//        if (titleStr.equalsIgnoreCase("Others")) {
//            params.put("job_title", "othertitle");
//            params.put("others_title", edit_title_others.getText().toString());
//        } else {
//
//            params.put("job_title", title_Id);
//            params.put("others_title", "");
//        }
//        if (splStr.equalsIgnoreCase("Others")) {
//            params.put("job_specialization_id", "otherspecialization");
//            params.put("others_specialization", edit_spl_others.getText().toString());
//        } else {
//
//            params.put("job_specialization_id", specializationId);
//            params.put("others_specialization", "");
//        }
//        if (subSplStr.equalsIgnoreCase("Others")) {
//            params.put("sub_specialization", "othersubspecialization");
//            params.put("others_sub_specialization", edit_Sub_spl_others.getText().toString());
//        } else {
//
//            params.put("sub_specialization", subSpecializationId);
//            params.put("others_sub_specialization", "");
//        }
//        if (dept_Str.equalsIgnoreCase("Others")) {
//            params.put("department_id", "otherdepartment");
//            params.put("others_department", edit_dept_others.getText().toString());
//        } else {
//
//            params.put("department_id", dept_Id);
//            params.put("others_department", "");
//        }
//        params.put("registration_no", "");
//        params.put("location", location.getText().toString());
//        params.put("skill_required", skillRequired.getText().toString());
//        params.put("preffered_for", prefer_Id);
//        params.put("ctc", ctc.getText().toString());
//        params.put("no_of_vaccancies", vacancy.getText().toString());
//        params.put("job_description", desc.getText().toString());
//        Log.d("POSTJOBFRAAG",params+"");
//        header.put("Cynapse", params);
//        new PostJobApi(getActivity(), header) {
//            @Override
//            public void responseApi(JSONObject response) {
//                super.responseApi(response);
//                try {
//                    JSONObject header = response.getJSONObject("Cynapse");
//                    String res_msg = header.getString("res_msg");
//                    String res_code = header.getString("res_code");
//                    if (res_code.equals("1")) {
//                        MyToast.toastLong(getActivity(), res_msg);
//                        getActivity().finish();
//                    } else {
//                        MyToast.toastLong(getActivity(), res_msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void errorApi(VolleyError error) {
//                super.errorApi(error);
//            }
//        };
//    }

    private void setUpSpinner() {
        //TODO: for the medical profile
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.demoArray,android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        medicalProfile.setAdapter(adapter);
        //TODO: for the job title
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.jobTitleArray, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTitle.setAdapter(adapter1);
        //TODO: for the job specialization
//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.demoArray,android.R.layout.simple_spinner_item);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        jobSpecialization.setAdapter(adapter2);
        //TODO: for the sub specialization
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.SubSpecializationArray, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpecialization.setAdapter(adapter3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:

                if(Util.isVerifiedProfile(getActivity())) {

                    if (AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                        //b = false;
                        //showDialog(activity);
                        try {
                            if (Util.isVerifiyEMailPHoneNO((Activity) getActivity())) {

                                if (isValid()) {
                                    if (email.equalsIgnoreCase("") || phoneNumber.equalsIgnoreCase("")) {
                                        MyToast.toastLong(getActivity(), "Please update mobile number before posting a new Job");
                                    } else {
                                        try {
                                            postJobApi();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //TODO: first checking the validations then checking for the api process
                        if (isValid()) {
                            if (email.equalsIgnoreCase("") || phoneNumber.equalsIgnoreCase("")) {
                                MyToast.toastLong(getActivity(), "Please update mobile number before posting a new Job");
                            } else {
                                try {
                                    postJobApi();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                break;
            case R.id.medicalProfileauto:


                if (medicalProfileauto.getText().toString().equals("")) {

                    tempMedProf = new ArrayList<>();

                    Title_auto.setText("");
                    try {
                        tempMedProf.addAll(medicalProfileName);
                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempMedProf);
                        medicalProfileauto.setAdapter(adapter_medical_profile);
                        medicalProfileauto.showDropDown();

                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                }


                // getModels(false);
                //multi_title_sel_recycler.getAdapter().notifyDataSetChanged();

                break;

            case R.id.Spealization_auto:
                try {
                    if (Spealization_auto.getText().toString().equals("")) {
                        tempSplzation = new ArrayList<>();
                        tempSplzation.addAll(specializationName);
                    }

                    adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSplzation);
                    Spealization_auto.setAdapter(adapter_specialization);
                    Spealization_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.Title_auto:

                if (Title_auto.getText().toString().equals("")) {
                    tempTitle = new ArrayList<>();
                    try {
                        tempTitle.addAll(TitleName);
                        adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempTitle);
                        Title_auto.setAdapter(adapter_title);
                        Title_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                }
                break;
            case R.id.SubS_auto:
                if (SubS_auto.getText().toString().equals("")) {
                    tempSubS = new ArrayList<>();
                    try {
                        tempSubS.addAll(SubSName);
                        adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempSubS);
                        SubS_auto.setAdapter(adapter_subSpl);
                        SubS_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                }


                break;
            case R.id.PreferredFor_auto:
                if (PreferredFor_auto.getText().toString().equals("")) {
                    tempPref = new ArrayList<>();
                    try {
                        tempPref.addAll(PrefName);
                        adapter_PreferredFor = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempPref);
                        PreferredFor_auto.setAdapter(adapter_PreferredFor);
                        PreferredFor_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                }

                break;
            case R.id.dept_auto:
                if (dept_auto.getText().toString().equals("")) {
                    tempDept = new ArrayList<>();

                    try {
                        tempDept.addAll(DeptName);
                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_post_job, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                        dept_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                }
                break;

            case R.id.state_auto1:
                try {

                    if (state_auto1.getText().toString().equals("")) {
                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                    }
                    Log.e("focusState", stateListName.size() + "");
                    state_adapter1 = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempStateListName);
                    state_auto1.setAdapter(state_adapter1);
                    state_auto1.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.country_auto:
                try {
                    if (country_auto1.getText().toString().equals("")) {
                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                    }
                    country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCountryListName);
                    country_auto1.setAdapter(country_adapter);
                    country_auto1.showDropDown();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.city_auto:
                try {
                    if (city_auto1.getText().toString().equals("")) {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                    }

                    Log.d("cityarraylist", tempCityListName.toString());
                    city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCityListName);
                    city_auto1.setAdapter(city_adapter);
                    city_auto1.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
        }
    }

    private boolean isValid() {
        boolean isValid = false;
        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
            specializationId = "";
            subSpecializationId = "";
            if (medicalId.equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter Medical Profile");
                return false;
            } else if (medicalId.equalsIgnoreCase("-1") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Medical Profile cannot be blank!");
                return false;
            } else if (title_Id.equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter title");
            } else if (title_Id.equalsIgnoreCase("-2") && edit_title_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Title cannot be blank!");
                return false;
            } else if (dept_Id.equalsIgnoreCase("") || dept_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter Department");
                return false;
            } else if (dept_Id.equalsIgnoreCase("-5") && edit_dept_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Department cannot be blank!");
                return false;
            }
//            else if (TextUtils.isEmpty(location.getText().toString())) {
//                MyToast.toastLong(getActivity(), "Please provide Location!");
//                return false;
//            }

            else if (TextUtils.isEmpty(country_auto1.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Select Country!");
                return false;
            } else if (TextUtils.isEmpty(state_auto1.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Select State!");
                return false;
            } else if (cityChipsNameList.size() < 1) {
                MyToast.toastLong(getActivity(), "Please Select Atleast One City!");
                return false;
            } else if (TextUtils.isEmpty(experience.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Years of Experience!");
                return false;
            } else if (PrefId.equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Select Preferred for!");
                return false;
            } else if (PrefId.equalsIgnoreCase("-6") && edit_pref_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for preffered  for cannot be blank!");
                return false;
            } else if (TextUtils.isEmpty(ctc.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter CTC!");
                return false;
            } else if (TextUtils.isEmpty(vacancy.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter No. of Vaccancies!");
                return false;
            } else if (TextUtils.isEmpty(desc.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Job Description!");
                return false;
            } else {

                isValid = true;
            }
        } else {
            dept_Id = "";
            if (medicalId.equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter Medical Profile");
                return false;
            } else if (medicalId.equalsIgnoreCase("-1") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Medical Profile cannot be blank!");
                return false;
            } else if (title_Id.equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter title");
            } else if (title_Id.equalsIgnoreCase("-2") && edit_title_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Title cannot be blank!");
                return false;
            } else if (specializationId.equalsIgnoreCase("") || Spealization_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter Specialization");
            } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Specialization cannot be blank!");
                return false;
            } else if (subSpecializationId.equalsIgnoreCase("") || SubS_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter SubSpecialization");
            } else if (subSpecializationId.equalsIgnoreCase("-4") && edit_Sub_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Sub Specialization cannot be blank!");
                return false;
            }
//            else if (TextUtils.isEmpty(location.getText().toString())) {
//                MyToast.toastLong(getActivity(), "Please provide Location!");
//                return false;
//            }

            else if (TextUtils.isEmpty(country_auto1.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Select Country!");
                return false;
            } else if (TextUtils.isEmpty(state_auto1.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Select State!");
                return false;
            } else if (cityChipsNameList.size() < 1) {
                MyToast.toastLong(getActivity(), "Please Select Atleast One City!");
                return false;
            } else if (TextUtils.isEmpty(experience.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Years of Experience!");
                return false;
            } else if (PrefId.equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Select Preferred for!");
                return false;
            } else if (PrefId.equalsIgnoreCase("-6") && edit_pref_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for preffered for cannot be blank!");
                return false;
            } else if (TextUtils.isEmpty(ctc.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter CTC!");
                return false;
            } else if (TextUtils.isEmpty(vacancy.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter No. of Vaccancies!");
                return false;
            } else if (TextUtils.isEmpty(desc.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Job Description!");
                return false;
            } else {

                isValid = true;
            }

        }
        return isValid;
    }
    //TODO: validations check
//    private void isValid() {
//        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
//
//            if (!TextUtils.isEmpty(location.getText().toString()) && !medicalId.equalsIgnoreCase("") && !dept_Id.equalsIgnoreCase("")
//                    && !title_Id.equalsIgnoreCase("")
//                    && !TextUtils.isEmpty(skillRequired.getText().toString()) && !prefer_Id.equalsIgnoreCase("")
//                    && !TextUtils.isEmpty(ctc.getText().toString()) && !TextUtils.isEmpty(experience.getText().toString())
//                    && !TextUtils.isEmpty(vacancy.getText().toString()) && !TextUtils.isEmpty(desc.getText().toString())) {
//                //TODO: calling the PostApi
//                try {
//                    postJobApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                MyToast.toastLong(getActivity(), "All fields must be filled");
//            }
//        } else {
//            if (!TextUtils.isEmpty(location.getText().toString()) && !medicalId.equalsIgnoreCase("") && !title_Id.equalsIgnoreCase("")
//                    && !subSpecializationId.equalsIgnoreCase("")
//                    && !specializationId.equalsIgnoreCase("") && !TextUtils.isEmpty(skillRequired.getText().toString())
//                    && !prefer_Id.equalsIgnoreCase("")
//                    && !TextUtils.isEmpty(ctc.getText().toString()) && !TextUtils.isEmpty(experience.getText().toString())
//                    && !TextUtils.isEmpty(vacancy.getText().toString()) && !TextUtils.isEmpty(desc.getText().toString())) {
//                //TODO: calling the PostApi
////                && !TextUtils.isEmpty(registration_no.getText().toString())
//                try {
//                    postJobApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                MyToast.toastLong(getActivity(), "All fields must be filled");
//            }
//        }
////        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
////            if (!TextUtils.isEmpty(location.getText().toString()) && !medicalId.equalsIgnoreCase("") && !dept_Id.equalsIgnoreCase("") && !title_Id.equalsIgnoreCase("")
////                    && !TextUtils.isEmpty(skillRequired.getText().toString()) && !prefer_Id.equalsIgnoreCase("")
////                    && !TextUtils.isEmpty(ctc.getText().toString()) && !TextUtils.isEmpty(vacancy.getText().toString()) && !TextUtils.isEmpty(desc.getText().toString())) {
////                //TODO: calling the PostApi
////                try {
////                    postJobApi();
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////            } else {
////                MyToast.toastLong(getActivity(), "All fields must be filled");
////            }
////        } else {
////
////
////            System.out.println("ctc" +ctc);
////            System.out.println("vacancy" +vacancy);
////
////            if (!TextUtils.isEmpty(location.getText().toString()) && !medicalId.equalsIgnoreCase("") && !title_Id.equalsIgnoreCase("") && !subSpecializationId.equalsIgnoreCase("")
////                    && !specializationId.equalsIgnoreCase("") && !TextUtils.isEmpty(skillRequired.getText().toString()) && !prefer_Id.equalsIgnoreCase("")
////                    && !TextUtils.isEmpty(ctc.getText().toString()) && !TextUtils.isEmpty(vacancy.getText().toString()) && !TextUtils.isEmpty(desc.getText().toString())) {
////                //TODO: calling the PostApi
////                try {
////                    postJobApi();
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////            } else {
////                MyToast.toastLong(getActivity(), "All fields must be filled");
////            }
////        }
//
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.medicalProfile:
                //String medicalStr = parent.getItemAtPosition(position).toString();
                String medicalStr = medicalProfile.getItemAtPosition(medicalProfile.getSelectedItemPosition()).toString();
                medicalId = medicalList.get(position).getId();
                if (medicalId.equalsIgnoreCase("Select Medical Profile")) {
                    medicalId = "";
                }
                Log.d("Medical", medicalId);
                try {
                    getTitle(medicalId,0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
                    try {
                        GetDepartmentApi(medicalId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dept_rel_lay.setVisibility(View.VISIBLE);
                    dept_view.setVisibility(View.VISIBLE);
                    Spl_rel_lay.setVisibility(View.GONE);
                    spl_view.setVisibility(View.GONE);
                    subS_rel_lay.setVisibility(View.GONE);
                    subS_view.setVisibility(View.GONE);
                } else {
                    dept_rel_lay.setVisibility(View.GONE);
                    dept_view.setVisibility(View.GONE);
                    Spl_rel_lay.setVisibility(View.VISIBLE);
                    spl_view.setVisibility(View.VISIBLE);
                    subS_rel_lay.setVisibility(View.VISIBLE);
                    subS_view.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.jobTitle:
                jobStr = parent.getItemAtPosition(position).toString();
                job_id = titleList.get(position).getSpecialization_id();
                if (job_id.equalsIgnoreCase("Select Title")) {
                    job_id = "";
                }
                Log.d("JbSpinner", job_id);
                try {
                    getProfileSpecialization(medicalId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.jobSpecialization:
                String jobSpecializationStr = jobSpecialization.getItemAtPosition(jobSpecialization.getSelectedItemPosition()).toString();
                specializationId = specializationList.get(position).getSpecialization_id();
                if (specializationId.equalsIgnoreCase("Select Specialization")) {
                    specializationId = "";
                }
                Log.d("Job", specializationId);
                try {
                    GetSubSpecializationApi(specializationId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.dept_dd:

                String deptStr = dept_dd.getItemAtPosition(dept_dd.getSelectedItemPosition()).toString();
                dept_id = dept_SpinnerList.get(position).getId();
                if (dept_id.equalsIgnoreCase("Select Department")) {
                    dept_id = "";
                }
                Log.d("dept_id", dept_id);
                break;
            case R.id.preferFor_dd:
                preferStr = parent.getItemAtPosition(position).toString();
                Log.d("PreSpinner", preferStr);
                if (preferStr.equalsIgnoreCase("Hospital")) {
                    prefer_Id = "1";
                    other_pref_rel_lay.setVisibility(View.GONE);
                } else if (preferStr.equalsIgnoreCase("Nursing Home")) {
                    other_pref_rel_lay.setVisibility(View.GONE);
                    prefer_Id = "2";
                } else if (preferStr.equalsIgnoreCase("Corporate Hospitals")) {
                    other_pref_rel_lay.setVisibility(View.GONE);
                    prefer_Id = "3";
                } else if (preferStr.equalsIgnoreCase("Universities")) {
                    other_pref_rel_lay.setVisibility(View.GONE);
                    prefer_Id = "4";
                } else if (preferStr.equalsIgnoreCase("Others")) {
                    prefer_Id = "5";
                    other_pref_rel_lay.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.subSpecialization:
                subSpecializationStr = parent.getItemAtPosition(position).toString();
                subspecializationId = subspecializationList.get(position).getId();
                if (subspecializationId.equalsIgnoreCase("Select SubSpecialization")) {
                    subspecializationId = "";
                }
                Log.d("SubSpinner", subspecializationId);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

                        item.getString("medical_profile_category_id");
                        item.getString("profile_image");
//
                        for (int i = 0; i < medicalList.size(); i++) {
                            if (medicalList.get(i).getProfile_category_name().equalsIgnoreCase(item.getString("medical_profile_category_name"))) {
                                medicalProfile.setSelection(i);
                            }
                        }
                        //    AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.medical_profile_id,item.getString("medical_profile_category_id"));


                        getProfileName = item.getString("medical_profile_category_name");
                        // medicalProfileauto.setText(getProfileName);
                        // title_Id = item.getString("title_id");
                        //Title_auto.setText(item.getString("title_name"));
                        // specializationId = item.getString("specialization_id");
                        //Spealization_auto.setText(item.getString("specialization_name"));
                        // subSpecializationId = item.getString("sub_specialization_id");
                        // SubS_auto.setText(item.getString("sub_specialization_name"));
                        // dept_Id = item.getString("department_id");
                        // dept_auto.setText(item.getString("department_name"));
                        Log.d("MEDICLPROFILECATEGORYNM", getProfileName);
                        getProfileId = item.getString("medical_profile_category_id");
                        email = item.getString("email");
                        phoneNumber = item.getString("phone_number");
//                        if(getProfileId.equalsIgnoreCase("1"))
//                        {
//                            reg_lin_lay.setVisibility(View.VISIBLE);
//                        }else
//                        {
//                            reg_lin_lay.setVisibility(View.GONE);
//                        }
                        Log.d("MEDICLPROFILEIDD", getProfileId);
                        if (medicalId.equals("")) {
                            //medicalId = getProfileId;
                        }
                        Log.d("MEFIIDIDID", medicalId);


//                        if (getProfileId.equalsIgnoreCase("2") || getProfileId.equalsIgnoreCase("12") || getProfileId.equalsIgnoreCase("13")
//                                || getProfileId.equalsIgnoreCase("1") || getProfileId.equalsIgnoreCase("4")) {
//                            getTitle(getProfileId);
//                        } else {
//                            getTitle("13");
//                        }
//                        try {
//                            GetSubSpecializationApi(specializationId);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            GetDepartmentApi(medicalId);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if (getProfileId.equalsIgnoreCase("2") || getProfileId.equalsIgnoreCase("12") || getProfileId.equalsIgnoreCase("13")) {
//                            try {
//                                GetDepartmentApi(getProfileId);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            dept_rel_lay.setVisibility(View.VISIBLE);
//                            dept_view.setVisibility(View.VISIBLE);
//                            Spl_rel_lay.setVisibility(View.GONE);
//                            spl_view.setVisibility(View.GONE);
//                            subS_rel_lay.setVisibility(View.GONE);
//                            subS_view.setVisibility(View.GONE);
//                        } else {
//                            dept_rel_lay.setVisibility(View.GONE);
//                            dept_view.setVisibility(View.GONE);
//                            Spl_rel_lay.setVisibility(View.VISIBLE);
//                            spl_view.setVisibility(View.VISIBLE);
//                            subS_rel_lay.setVisibility(View.VISIBLE);
//                            subS_view.setVisibility(View.VISIBLE);
//                        }

                        // item.getString("medical_profile_category_name");


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

    @Override
    public void selectedCities(ArrayList<String> citylist) {

    }

    //    akash commit changes

    private void getAllCountry() {
        new GetCountryClass(getActivity()) {
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


        country_auto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.e("focusCountry", countryListName.size() + "");
                    if (country_auto1.getText().toString().equals("")) {
                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                    }


                    country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCountryListName);
                    country_auto1.setAdapter(country_adapter);
                    country_auto1.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        country_auto1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    try {
                        Log.e("focusCountry", countryListName.size() + "");
                        if (country_auto1.getText().toString().equals("")) {
                            tempCountryListName.clear();
                            tempCountryListName.addAll(countryListName);
                        }


                        country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCountryListName);
                        country_auto1.setAdapter(country_adapter);
                        country_auto1.showDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        country_auto1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country_str = country_auto1.getText().toString();
                try {
                    country_id = countryList.get(countryListName.indexOf(country_str)).getCountry_code();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                getAllState(country_id);
                state_auto1.setText("");
                clearCities();
                Log.d("countryID", country_id);
            }
        });

        country_auto1.addTextChangedListener(new TextWatcher() {
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
                        country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCountryListName);
                        country_auto1.setAdapter(country_adapter);
                        country_auto1.showDropDown();

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


    private void clearCities()
    {
        cityChipsNameList.clear();
        flexboxDrawableWithClose.removeAllViews();
        Log.e("chipsDeletePosion##", "" + cityChipsNameList.size());
    }


    private void statesetupListener() {

        state_auto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    tempStateListName.clear();
                    tempStateListName.addAll(stateListName);
                    Log.e("focusState", stateListName.size() + "");
                    state_adapter1 = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempStateListName);
                    state_auto1.setAdapter(state_adapter1);
                    state_auto1.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        state_auto1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    try {
                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                        Log.e("focusState", stateListName.size() + "");
                        state_adapter1 = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempStateListName);
                        state_auto1.setAdapter(state_adapter1);
                        state_auto1.showDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        state_auto1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state_Str = state_auto1.getText().toString();

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

                    clearCities();

                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                city_auto1.setText("");
                Log.d("Id", country_id);
                Log.d("Id", state_id);

                getAllCity(country_id, state_id);
                Log.d("stateId", state_id);
            }
        });

        state_auto1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                        state_adapter1 = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempStateListName);
                        state_auto1.setAdapter(state_adapter1);
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

    private void getAllCity(String country_id, String state_id) {
        new GetCityClass(getActivity(), country_id, state_id) {
            @Override
            protected void responseCity(ArrayList<CityModel> responseCity) {
                cityList1 = responseCity;
                Log.d("cities123", responseCity.toString());
                cityListName = new ArrayList<>();

                Iterator iterator = cityList1.iterator();

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

        city_auto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tempCityListName.clear();
                    tempCityListName.addAll(cityListName);
                    Log.e("focusCity", cityListName.size() + "");
                    city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCityListName);
                    city_auto1.setAdapter(city_adapter);
                    city_auto1.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        city_auto1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    try {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                        Log.e("focusCity", cityListName.size() + "");
                        city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCityListName);
                        city_auto1.setAdapter(city_adapter);
                        city_auto1.showDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        city_auto1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city_str = city_auto1.getText().toString();
                try {
                    try {

                        city_id = cityList1.get(cityListName.indexOf(city_str)).getCity_id();
                        city_auto1.setText("");
                        addChipsData(city_str);
                        city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCityListName);
                        city_auto1.setAdapter(city_adapter);
                        city_auto1.showDropDown();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                Log.d("city_id", city_id);


            }
        });

        city_auto1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {


                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                        city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCityListName);
                        city_auto1.setAdapter(city_adapter);
                        city_auto1.showDropDown();
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

        drawableWithCloseChipCloud = new ChipCloud(getActivity(), flexboxDrawableWithClose, drawableWithCloseConfig);

        drawableWithCloseChipCloud.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {

                cityChipsNameList.remove(index);
                Log.e("chipsDeletePosion", index + "," + label + "," + cityChipsNameList.size());
            }
        });
    }

    private void addChipsData(String citys) {
        if (cityChipsNameList.size() < 6) {

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
            Toast.makeText(getActivity(), "Maximum 6 cities can be selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void ifAlreadyAdded() {
        Toast.makeText(getActivity(), "City Name Already Exists!", Toast.LENGTH_SHORT).show();
    }


    private void getAllState(String country_id) {

        new GetStateClass(getActivity(), country_id) {
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

    //    akash commit changes
}
