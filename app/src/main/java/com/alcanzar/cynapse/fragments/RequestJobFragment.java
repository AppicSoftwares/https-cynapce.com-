package com.alcanzar.cynapse.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.HelperClasses.GetCityClass;
import com.alcanzar.cynapse.HelperClasses.GetCountryClass;
import com.alcanzar.cynapse.HelperClasses.GetStateClass;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MedicalTitleInterface;
import com.alcanzar.cynapse.activity.MetroCityInterface;
import com.alcanzar.cynapse.activity.ResumeUpLoad;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.adapter.MedicalTitleAdapter;
import com.alcanzar.cynapse.adapter.MetroCityAdapter;
import com.alcanzar.cynapse.api.GetDepartmentApi;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.api.GetMetroCityApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.api.GetSubSpecializationApi;
import com.alcanzar.cynapse.api.GetTitleApi;
import com.alcanzar.cynapse.api.RequestJobApi;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.model.MetroCityModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.FilePath;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.PostImage;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipDeletedListener;

import static android.app.Activity.RESULT_OK;
import static com.alcanzar.cynapse.utils.FilePath.isDownloadsDocument;
import static com.alcanzar.cynapse.utils.Util.getDriveFilePath;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestJobFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, MetroCityInterface, MedicalTitleInterface {
    //TODO:PDF REQUEST Variables
    private static final int PICK_PDF_REQUEST = 11;
    private Uri fileUri;
    private String pdfPath="";
    RelativeLayout dept_rel_lay, Spl_rel_lay, subS_rel_lay, other_rel_lay, other_title_rel_lay, other_spl_rel_lay,reg_rel_lay,
            other_sub_spl_rel_lay, other_dept_rel_lay;
    View dept_view, spl_view, subS_view;

    String profile_id;
    //TODO: different layout views
    String getProfileId = "", getProfileName = "";
    Spinner medicalProfile, jobTitle, jobSpecialization, subSpecialization, preferFor, dept_dd;
    private String medicalId = "", specializationId = "", subspecializationId = "", jobStr = "", job_id = "", subSpecializationStr = "", preferStr = "", url = AppConstantClass.HOST+"fileUpload/resume" , pdf_name = "", dept_id = "", medicalTitleId = "" , exists = "";
    ArrayList<String> medicalProfileSpinner = new ArrayList<>();
    ArrayList<String> jobSpecializationSpinner = new ArrayList<>();
    ArrayList<MedicalProfileModel> medicalList = new ArrayList<>();
    ArrayList<MetroCityModel> metrocityList = new ArrayList<>();
    ArrayList<MetroCityModel> metrocitySelectedList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> titleNewList = new ArrayList<>();
    ArrayList<String> titleNewListId = new ArrayList<>();
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    ArrayList<JobSpecializationModel> titleSelectedList = new ArrayList<>();
    ArrayList<String> titleSpinner = new ArrayList<>();
    ArrayList<String> subSpecializationSpinner = new ArrayList<>();
    ArrayList<String> dept_Spinner = new ArrayList<>();
    ArrayList<MedicalProfileModel> subspecializationList = new ArrayList<>();
    ArrayList<MedicalProfileModel> dept_SpinnerList = new ArrayList<>();
    ArrayList<JobSpecializationModel> titleList = new ArrayList<>();
    EditText experience, currentCtc, expectedCtc, currentEmployer, desc, edit_others,copy_paste_resume,edit_Reg_no, edit_Reg_state,
            edit_title_others, edit_dept_others, edit_spl_others, edit_Sub_spl_others;
    TextView uploadResume, jobLocation,Title_auto;
    Button btnCreate;
    ImageView resumeImg;
    ArrayAdapter  state_adapter;
    RecyclerView multi_title_sel_recycler;
    MedicalTitleAdapter titleAdapter_recycler_adapter;
    ArrayAdapter adapter_medical_profile, adapter_specialization, adapter_title, adapter_dept, adapter_subSpl;
    AutoCompleteTextView medicalProfileauto, Spealization_auto, country_auto1, state_auto, state_auto1,city_auto1, SubS_auto, dept_auto;
    ImageButton crossC, crossS, crossHd, crossCity, crossState, crossCount, crossTitle, cross_dept;
    private String medStr = "", splStr = "", hdStr = "", title_Id = "", titleStr = "", dept_Id = "", dept_Str = "",state_id = "", state_Str = "", subSplStr = "", subSpecializationId = "";
    ArrayList<String> medicalProfileName;
    ArrayList<String> specializationName;
    ArrayList<String> tempMedProf;
    ArrayList<String> tempSplzation;
    ArrayList<String> tempHd;
    ArrayList<String> TitleName;
    ArrayList<String> tempTitle;
    ArrayList<String> DeptName;
    ArrayList<String> tempDept;
    ArrayList<String> SubSName;
    ArrayList<String> tempSubS;
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<String> tempstate = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    String email = "",phoneNumber = "";

    String pdfName = "";
    String pdf_name_code = "";

//    akash commit changes


    ChipCloudConfig drawableWithCloseConfig,flexbox_multiSpecialityConfig,flexbox_multiDepartmentConfig,flexbox_multiTitleConfig;
    ChipCloud drawableWithCloseChipCloud;
    FlexboxLayout flexboxDrawableWithClose,flexbox_multiSpeciality,flexbox_multiDepartment,flexbox_multiTitle;

    private ArrayList<CountryModel> countryList;

    private  ArrayList<CityModel> cityList1 = new ArrayList<>();

    String city_id="";

    ArrayAdapter country_adapter, state_adapter1, city_adapter;

    String country_id = "", country_str = "", city_str = "",
            regStateStr = "", edit_disable = "";
    ArrayList<String> countryListName,stateListName,cityListName;

    ArrayList<String> tempCountryListName=new ArrayList<>();
    ArrayList<String> tempStateListName=new ArrayList<>();
    ArrayList<String> tempStateListCode=new ArrayList<>();
    ArrayList<String> tempCityListName=new ArrayList<>();
    ArrayList<String> cityChipsNameList=new ArrayList<>();


    String p1="",n1="";

    //    akash commit changes

    public RequestJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_job, container, false);
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
        preferFor = view.findViewById(R.id.preferFor);
        //TODO: this is used to setUpThe Spinners
        setUpSpinner();
        experience = view.findViewById(R.id.experience);
        state_auto = view.findViewById(R.id.state_auto);
        currentCtc = view.findViewById(R.id.currentCtc);
        expectedCtc = view.findViewById(R.id.expectedCtc);
        currentEmployer = view.findViewById(R.id.currentEmployer);
        jobLocation = view.findViewById(R.id.jobLocation);
        uploadResume = view.findViewById(R.id.uploadResume);
        copy_paste_resume = view.findViewById(R.id.copy_paste_resume);
        desc = view.findViewById(R.id.desc);
        resumeImg = view.findViewById(R.id.resumeImg);
        dept_dd = view.findViewById(R.id.dept_dd);
        dept_rel_lay = view.findViewById(R.id.dept_rel_lay);
        Spl_rel_lay = view.findViewById(R.id.Spl_rel_lay);
        subS_rel_lay = view.findViewById(R.id.subS_rel_lay);
        dept_view = view.findViewById(R.id.dept_view);
        spl_view = view.findViewById(R.id.spl_view);
        subS_view = view.findViewById(R.id.subS_view);

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
        reg_rel_lay = view.findViewById(R.id.reg_rel_lay);
        edit_others = view.findViewById(R.id.edit_others);
        edit_title_others = view.findViewById(R.id.edit_title_others);
        edit_dept_others = view.findViewById(R.id.edit_dept_others);
        edit_spl_others = view.findViewById(R.id.edit_spl_others);
        edit_Sub_spl_others = view.findViewById(R.id.edit_Sub_spl_others);
        edit_Reg_no = view.findViewById(R.id.edit_Reg_no);
        edit_Reg_state =view.findViewById(R.id.edit_Reg_state);
        crossC = view.findViewById(R.id.crossC);
        cross_dept = view.findViewById(R.id.cross_dept);
        crossTitle = view.findViewById(R.id.crossTitle);
        //TODO: making the btn clickable
        btnCreate.setOnClickListener(this);
        uploadResume.setOnClickListener(this);
        resumeImg.setOnClickListener(this);
        //TODO: making the spinner items selectable
        medicalProfile.setOnItemSelectedListener(this);
        jobTitle.setOnItemSelectedListener(this);
        jobSpecialization.setOnItemSelectedListener(this);
        subSpecialization.setOnItemSelectedListener(this);
        dept_dd.setOnItemSelectedListener(this);
        medicalProfileauto.setOnClickListener(this);
        state_auto.setOnClickListener(this);
        medicalProfileauto.setOnClickListener(this);
        Spealization_auto.setOnClickListener(this);
        SubS_auto.setOnClickListener(this);
        dept_auto.setOnClickListener(this);

//        p1=AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code","");
//        n1=AppCustomPreferenceClass.readString(getActivity(),"pdf_name","");
//        Log.e("siudfgdy",n1);
//        Log.e("siudfgdy",p1);

        autosaveresume();

//        akash commit changes

        country_auto1=view.findViewById(R.id.country_auto);
        state_auto1=view.findViewById(R.id.state_auto1);
        city_auto1=view.findViewById(R.id.city_auto);
        flexboxDrawableWithClose = view.findViewById(R.id.flexbox_drawable_close);

        country_auto1.setOnClickListener(this);
        state_auto1.setOnClickListener(this);
        city_auto1.setOnClickListener(this);


        getAllCountry();
        statesetupListener();
        setUpCityListener();
        setUpChips();

//      akash commit changes

        jobLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //preferFor.setOnItemSelectedListener(this);
        //TODO: listing data calling
        getMedicalProfileApi();
        getMetroCityApi();
        try {
            getStateApi("IN");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            titleList1 = new ArrayList<>();
            GetProfileApi();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getProfileSpecialization("");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //   medicalProfileauto.setText(getProfileName);
        medicalProfileauto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // crossC.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    // cityC.setVisibility(View.GONE);

                    if (medicalProfileauto.getText().toString().equals("")) {

                        tempMedProf = new ArrayList<>();
                        try {
                            tempMedProf.addAll(medicalProfileName);
                            adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempMedProf);
                            medicalProfileauto.setAdapter(adapter_medical_profile);
                            medicalProfileauto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
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

                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempMedProf);
                        medicalProfileauto.setAdapter(adapter_medical_profile);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    // crossC.setVisibility(View.GONE);
                } else {
                    // crossC.setVisibility(View.VISIBLE);

                }

            }
        });
        medicalProfileauto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                medStr = medicalProfileauto.getText().toString();

                medicalId = medicalList.get(medicalProfileName.indexOf(medStr)).getId();
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
                        getTitle("13");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    other_rel_lay.setVisibility(View.GONE);
                    try {


                        if(medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")
                                ||medicalId.equalsIgnoreCase("1")||medicalId.equalsIgnoreCase("4")){
                            getTitle(medicalId);
                        }else {
                            getTitle("13");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (medicalId.equalsIgnoreCase("1")) {
                    reg_rel_lay.setVisibility(View.VISIBLE);
                } else {
                    reg_rel_lay.setVisibility(View.GONE);
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

        state_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (state_auto.getText().toString().equals("")) {
                            tempstate = new ArrayList<>();
                            tempstate.addAll(stateName);
                        }
                        state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempstate);
                        state_auto.setAdapter(state_adapter);
                        state_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (state_auto.toString().equals("")) {

                    } else {

                    }

                }
            }
        });
        state_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state_Str = state_auto.getText().toString();
                state_id = stateList.get(stateName.indexOf(state_Str)).getState_code();
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
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);

                        state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempstate);
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
        Title_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTitle();
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
                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                        dept_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (dept_auto.toString().equals("")) {

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

                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

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
                        adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                        Spealization_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (Spealization_auto.toString().equals("")) {
                        Spealization_auto.setEnabled(true);
                    }
//                        crossS.setVisibility(View.GONE);
//                    } else{
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

                        adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    //  crossS.setVisibility(View.GONE);
                } else {
                    //  crossS.setVisibility(View.VISIBLE);
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
        SubS_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);


                    if (SubS_auto.getText().toString().equals("")) {
                        tempSubS = new ArrayList<>();
                        try {
                            tempSubS.addAll(SubSName);
                            adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }

                    }


                } else {
                    if (SubS_auto.toString().equals("")) {
                        SubS_auto.setEnabled(true);
                    }
                    //  crossS.setVisibility(View.GONE);
//                    } else{
//                        //crossS.setVisibility(View.VISIBLE);
//                    }

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

                        adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSubS);
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
                }catch (ArrayIndexOutOfBoundsException ao)
                {
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
    }

//    private void checkforpdf() {
//
//        if(AppCustomPreferenceClass.readString(getActivity(),"pdf_name","")!=""){
//
//            uploadResume.setText(pdf_name);
//
//        }
//    }

    private void autosaveresume() {
//        AppCustomPreferenceClass.writeString(ResumeUpLoad.this,"pdf_name_code",resume_code);
//        AppCustomPreferenceClass.writeString(ResumeUpLoad.this,"pdf_name",resume_name);
////
//
        //if(!(AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code","").equals("") && AppCustomPreferenceClass.readString(getActivity(),"pdf_name","").equals(""))){

        if(!AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code","").equals(""))
        {
            uploadResume.setText(AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code",""));
            uploadResume.setTextColor(Color.BLACK);
            exists = "1";
//            pdfName=AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code","");

            pdfName = AppCustomPreferenceClass.readString(getActivity(),"pdf_name","");
            pdf_name_code = AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code","");
            Log.d("pdfcode",pdf_name_code);
        }
    }

    private void clearListData() {
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

                            adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempMedProf);
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

                            adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSplzation);
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

    //    private void getTitle(String profile_category_id) throws JSONException {
//        JSONObject header = new JSONObject();
//        JSONObject params =  new JSONObject();
//        params.put("sync_time","");
//        params.put("profile_category_id",profile_category_id);
//        header.put("Cynapse",params);
//        new GetTitleApi(getActivity(),header){
//            @Override
//            public void responseApi(JSONObject response) {
//                super.responseApi(response);
//                try {
//                    JSONObject header  = response.getJSONObject("Cynapse");
//                    String res_msg = header.getString("res_msg");
//                    String res_code = header.getString("res_code");
//                    if(res_code.equals("1")){
//                        titleSpinner.clear();
//                        titleList.clear();
//                        JSONArray header2 = header.getJSONArray("Title");
//                        for(int i=0;i<header2.length();i++){
//                            JSONObject item = header2.getJSONObject(i);
//                            titleList.add(new JobSpecializationModel(item.getString("title_id"),
//                                    item.getString("profile_category_id"),item.getString("title")));
//                            // titleSpinner.add(item.getString("title"));
//                            Log.e("titleSize",String.valueOf(titleSpinner.size()));
//                        }
//                        Log.e("titleSize",String.valueOf(titleSpinner.size()));
//                        titleList.add(new JobSpecializationModel("-2","Others","Others"));
////                        ArrayAdapter<String> adapter =new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item
////                                ,titleSpinner);
////                        title_dd.setAdapter(adapter);
////                        adapter.notifyDataSetChanged();
//                        //MyToast.toastLong(getActivity(),res_msg);
//                        if (titleList.size() > 0) {
//
//                            TitleName = new ArrayList<>();
//                            tempTitle = new ArrayList<>();
//                            for (int j = 0; j < titleList.size(); j++) {
//                                // countryName.add(medicalList.get(j).getProfile_category_name());
//                                tempTitle.add(titleList.get(j).getSpecialization_name());
//                                TitleName.add(titleList.get(j).getSpecialization_name());
//                            }
//
//                            adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempTitle);
//                            Title_auto.setAdapter(adapter_title);
//                            Title_auto.setThreshold(1);
//                        }
//                    }
//                    else {
//                        titleSpinner.clear();
//                        titleList.clear();
//                        //MyToast.toastLong(getActivity(),res_msg);
//                    }
//                }
//                catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void errorApi(VolleyError error) {
//                super.errorApi(error);
//            }
//        };
//    }

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

                            adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.setThreshold(1);
                        }
                        //MyToast.toastLong(getActivity(),res_msg);
                    } else {
                        // subSpecializationSpinner.clear();
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

                            adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSubS);
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

                            adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempDept);
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

    private void requestPostApi() throws JSONException {
//        {
//            "Cynapse": {
//            "uuid":"S3994",
//                    "medical_profile_id":"1",
//                    "job_title":"Doctor",
//                    "job_specilization_id":"2",
//                    "sub_specilization":"test",
//                    "year_of_experience":"15",
//                    "current_ctc":"40000",
//                    "expected_ctc":"50000",
//                    "current_empoloyer":"pgi",
//                    "freferred_job_location":"lucknow",
//                    "freferred_for":"testing_prefe",
//                    "upload_resume":"https://www.w3schools.com/",
//                    "job_description" :"It hiering for doctor requierment"
//
//        }
//        }
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
//        params.put("medical_profile_id",medicalId);
//        params.put("job_title",job_id);
//        params.put("job_specialization_id",specializationId);
//        params.put("sub_specialization",subspecializationId);
//        params.put("department_id",dept_id);

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
            params.put("job_title", medicalTitleId);
            //  params.put("job_title", title_Id);
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

        params.put("registration_no", edit_Reg_no.getText().toString());
        params.put("registration_state", state_id);
        params.put("year_of_experience", experience.getText().toString());
        params.put("current_ctc", currentCtc.getText().toString());
        params.put("expected_ctc", expectedCtc.getText().toString());
        params.put("current_employer", currentEmployer.getText().toString());
        params.put("preferred_job_location", cityChipsNameList.toString().replace("[", "").replace("]", ""));
        params.put("preferred_for", preferStr);
        params.put("resume_description", copy_paste_resume.getText().toString());

        if (pdf_name_code.trim().equals("") && pdf_name.equals("")) {
            params.put("upload_resume", "");
            Log.i("upload_resume","1");
        }

        else if (!pdf_name.equals(""))
        {
            params.put("upload_resume", pdf_name);
            Log.i("upload_resume","2");
        }

        else if (!pdf_name_code.trim().equals(""))
        {
            params.put("upload_resume", pdf_name_code);
            Log.i("upload_resume","3");
        }

//        else {
//            params.put("upload_resume", AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code",""));
//        }
//        if(uploadResume.getText().toString().equals("")){
//            Toast.makeText(getActivity(),"Please upload Resume",Toast.LENGTH_SHORT).show();
//        }else{
//            params.put("upload_resume", AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code",""));
//        }
        //params.put("upload_resume", pdf_name);AppCustomPreferenceClass.readString(getActivity(),"pdf_name_code","")

        Log.d("REQUESTJOBREQUEST", params + "");
        // params.put("job_description",desc.getText().toString());
        header.put("Cynapse", params);
        new RequestJobApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                Log.d("resyetrqw",response.toString());
//                {
//                    "Cynapse": {
//                    "res_code": "1",
//                            "res_msg": "Request Jobposting Successfuly.",
//                            "sync_time": 1521805002,
//                            "RequestJobPosting": {
//                        "uuid": "S3994",
//                                "job_id": "J4ea2",
//                                "medical_profile_id": "1",
//                                "medical_profile_name": "Doctors",
//                                "job_title": "Doctor",
//                                "job_type": 1,
//                                "specialization": "2",
//                                "specialization_name": "Sr Residient",
//                                "year_exp": "15",
//                                "current_ctc": "40000",
//                                "expected_ctc": "50000",
//                                "current_employer": "pgi",
//                                "preferred_location": "lucknow",
//                                "resume": "https://www.w3schools.com/",
//                                "job_description": "It hiering for doctor requierment",
//                                "add_date": 1521805002,
//                                "modify_date": 1521805002,
//                                "status": 2
//                    }
//                }
//                }
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {

//                        if(cont==titleList1.size()){
                        MyToast.toastLong(getActivity(), res_msg);
                        getActivity().finish();
//                        }

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

    private void setUpSpinner() {
        //TODO: for the prefer For
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getActivity(), R.array.preferForArray, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preferFor.setAdapter(adapter4);
    }

    //TODO: these two are used to result the spinner selection values
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
                //TODO: calling the getProfileSpecialization api here
                try {
                    getTitle(medicalId);
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
            case R.id.subSpecialization:
                subSpecializationStr = parent.getItemAtPosition(position).toString();
                String subSpecializationStr = subSpecialization.getItemAtPosition(subSpecialization.getSelectedItemPosition()).toString();
                subspecializationId = subspecializationList.get(position).getId();
                if (subspecializationId.equalsIgnoreCase("Select SubSpecialization")) {
                    subspecializationId = "";
                }
                Log.d("SubSpinner", subspecializationId);
                break;
            case R.id.dept_dd:

                String deptStr = dept_dd.getItemAtPosition(dept_dd.getSelectedItemPosition()).toString();
                dept_id = dept_SpinnerList.get(position).getId();
                if (dept_id.equalsIgnoreCase("Select Department")) {
                    dept_id = "";
                }
                Log.d("dept_id", dept_id);
                break;
            case R.id.preferFor:
                preferStr = parent.getItemAtPosition(position).toString();
                Log.d("PreSpinner", preferStr);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                Log.d("MEDICALT@@@@@@@@", medicalTitleId);

                if(Util.isVerifiedProfile(getActivity()))
                {
                    if (AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                        //b = false;
                        //showDialog(activity);
                        try {
                            if (Util.isVerifiyEMailPHoneNO((Activity) getActivity())) {

                                    if (isValid()) {
                                        if (email.equalsIgnoreCase("") || phoneNumber.equalsIgnoreCase("")) {
                                            MyToast.toastLong(getActivity(), "Please update mobile number before requesting a new Job");
                                        } else {
                                            try {
//                            cont=0;
                                                if (titleList1.size() > 0) {
//                                for(int i=0;i<titleList1.size();i++)
//                                {

//                                    medicalTitleId=titleList1.get(i);
//                                    Log.e("RequestJobTitle",medicalTitleId);
//                                    cont++;
//                                                    if (pdfPath.equals("") && !TextUtils.isEmpty(uploadResume.getText().toString().trim()))
//                                                    {
//                                                        pdfPath = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.resumePath, "");
//                                                        Utils.sop("pdfPath@@"+pdfPath);
//                                                        uploadExistsResume(new File(pdfPath),url,pdfName,"file");
//
//                                                    }
//                                                    else
//                                                    {
//                                                        requestPostApi();
//                                                    }
                                                    requestPostApi();
                                                }
//                            }else{
//                                medicalTitleId=title_Id;
//                                requestPostApi();
//                                Log.e("RequestJobTitle1",medicalTitleId);
//                            }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    else {

                        if (isValid()) {
                            if (email.equalsIgnoreCase("") || phoneNumber.equalsIgnoreCase("")) {
                                MyToast.toastLong(getActivity(), "Please update mobile number before requesting a new Job");
                            } else {
                                try {
                                    if (titleList1.size() > 0) {
//                                for(int i=0;i<titleList1.size();i++)
//                                {

//                                    medicalTitleId=titleList1.get(i);
//                                    Log.e("RequestJobTitle",medicalTitleId);
//                                    cont++;
                                        requestPostApi();
                                    }
//                            }else{
//                                medicalTitleId=title_Id;
//                                requestPostApi();
//                                Log.e("RequestJobTitle1",medicalTitleId);
//                            }
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
                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempMedProf);
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

                    adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSplzation);
                    Spealization_auto.setAdapter(adapter_specialization);
                    Spealization_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.SubS_auto:
                if (SubS_auto.getText().toString().equals("")) {
                    tempSubS = new ArrayList<>();
                    try {
                        tempSubS.addAll(SubSName);
                        adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempSubS);
                        SubS_auto.setAdapter(adapter_subSpl);
                        SubS_auto.showDropDown();
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
                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                        dept_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                }
                break;

            case R.id.state_auto1:
                try {

                    if(state_auto1.getText().toString().equals("")){
                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                    }
                    Log.e("focusState",stateListName.size()+"");
                    state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempStateListName);
                    state_auto1.setAdapter(state_adapter);
                    state_auto1.showDropDown();
                }catch (Exception e){
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

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.city_auto:
                try {
                    if (city_auto1.getText().toString().equals("")) {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                    }

                    Log.d("cityarraylist",tempCityListName.toString());
                    city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCityListName);
                    city_auto1.setAdapter(city_adapter);
                    city_auto1.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.resumeImg:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //TODO: enters here in case Permission is not granted
                    Log.d("entered", "here0");
                    //TODO: showing an explanation to user
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        MyToast.toastLong(getActivity(), "Application needs storage permission to upload resume");
                        Log.d("entered", "here1");
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
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
                }
                break;
            case R.id.uploadResume:
//                if(uploadResume.getText().toString().contains(".pdf")){
//                    viewPdf();
//                }
//                else {
//                    MyToast.toastLong(getActivity(),"First Click the icon to upload resume !!");
//                }

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
            } else if (medicalTitleId.equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter title");
            } else if (dept_Id.equalsIgnoreCase("") || dept_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter Department");
                return false;
            }else if (dept_Id.equalsIgnoreCase("-5") && edit_dept_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Department cannot be blank!");
                return false;
            } else if (TextUtils.isEmpty(experience.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Years of Experience!");
                return false;
            } else if (TextUtils.isEmpty(currentCtc.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Current CTC!");
                return false;
            } else if (TextUtils.isEmpty(expectedCtc.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Expected CTC!");
                return false;
            } else if (TextUtils.isEmpty(currentEmployer.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Current Employer!");
                return false;
            }
//            else if (TextUtils.isEmpty(jobLocation.getText().toString())) {
//                MyToast.toastLong(getActivity(), "Please provide Prefferd Location!");
//                return false;
//            }
            else if (TextUtils.isEmpty(country_auto1.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Select Country!");
                return false;
            }
            else if (TextUtils.isEmpty(state_auto1.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Select State!");
                return false;
            }
            else if (cityChipsNameList.size()<1) {
                MyToast.toastLong(getActivity(), "Please Select Atleast One City!");
                return false;
            }

            else if (TextUtils.isEmpty(copy_paste_resume.getText().toString().trim()) && TextUtils.isEmpty(uploadResume.getText().toString().trim())) {
                MyToast.toastLong(getActivity(), "Please upload Resume OR write Cover letter!");
                return false;
            }
            else {
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
            }  else if (medicalTitleId.equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter title");
            } else if (specializationId.equalsIgnoreCase("") || Spealization_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter Specialization");
            } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Specialization cannot be blank!");
                return false;
            } else if (subSpecializationId.equalsIgnoreCase("")|| SubS_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Please Enter SubSpecialization");
            } else if (subSpecializationId.equalsIgnoreCase("-4") && edit_Sub_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Sub Specialization cannot be blank!");
                return false;
            } else if (TextUtils.isEmpty(experience.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Years of Experience!");
                return false;
            } else if (TextUtils.isEmpty(currentCtc.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Current CTC!");
                return false;
            } else if (TextUtils.isEmpty(expectedCtc.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Expected CTC!");
                return false;
            } else if (TextUtils.isEmpty(currentEmployer.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Enter Current Employer!");
                return false;
            }
//            else if (TextUtils.isEmpty(jobLocation.getText().toString())) {
//                MyToast.toastLong(getActivity(), "Please provide Prefferd Location!");
//                return false;
//            }
            else if (TextUtils.isEmpty(country_auto1.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Select Country!");
                return false;
            }
            else if (TextUtils.isEmpty(state_auto1.getText().toString())) {
                MyToast.toastLong(getActivity(), "Please Select State!");
                return false;
            }
            else if (cityChipsNameList.size()<1) {
                MyToast.toastLong(getActivity(), "Please Select Atleast One City!");
                return false;
            }
            else if (TextUtils.isEmpty(copy_paste_resume.getText().toString().trim()) && TextUtils.isEmpty(uploadResume.getText().toString().trim())) {
                MyToast.toastLong(getActivity(), "Please upload Resume OR write Cover letter!");
                return false;
            } else {
                isValid = true;
            }
        }
        return isValid;
    }
    //TODO: validations check
//    private void isValid() {
//
//        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
//            if (!TextUtils.isEmpty(experience.getText().toString()) && !medicalId.equalsIgnoreCase("") && !dept_Id.equalsIgnoreCase("")
//                    && !medicalTitleId.equalsIgnoreCase("") && !TextUtils.isEmpty(currentCtc.getText().toString())
//                    && !TextUtils.isEmpty(expectedCtc.getText().toString())
//                    && !TextUtils.isEmpty(currentEmployer.getText().toString()) && !TextUtils.isEmpty(jobLocation.getText().toString())) {
//                //TODO: calling the requestPostApi
//                try {
//                    requestPostApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                MyToast.toastLong(getActivity(), "All fields must be filled");
//            }
//        } else {
//            System.out.println("EXPERICEEEEE" + experience);
//            System.out.println("medicalId" + medicalId);
//            System.out.println("subSpecializationId" + subSpecializationId);
//            System.out.println("specializationId" + specializationId);
//            System.out.println("title_Id" + medicalTitleId);
//            System.out.println("currentCtc" + currentCtc);
//            System.out.println("currentEmployer" + currentEmployer);
//            System.out.println("jobLocation" + jobLocation);
//
//            if (!TextUtils.isEmpty(experience.getText().toString()) && !medicalId.equalsIgnoreCase("") && !subSpecializationId.equalsIgnoreCase("")
//                    && !specializationId.equalsIgnoreCase("") && !medicalTitleId.equalsIgnoreCase("") && !TextUtils.isEmpty(currentCtc.getText().toString()) && !TextUtils.isEmpty(expectedCtc.getText().toString())
//                    && !TextUtils.isEmpty(currentEmployer.getText().toString()) && !TextUtils.isEmpty(jobLocation.getText().toString())) {
//                //TODO: calling the requestPostApi
//                try {
//                    requestPostApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                MyToast.toastLong(getActivity(), "All fields must be filled");
//            }
//        }
//        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
//            if (!TextUtils.isEmpty(experience.getText().toString()) && !medicalId.equalsIgnoreCase("") && !dept_Id.equalsIgnoreCase("")
//                    && !title_Id.equalsIgnoreCase("") && !TextUtils.isEmpty(currentCtc.getText().toString()) && !TextUtils.isEmpty(expectedCtc.getText().toString())
//                    && !TextUtils.isEmpty(currentEmployer.getText().toString()) && !TextUtils.isEmpty(jobLocation.getText().toString())) {
//                //TODO: calling the requestPostApi
//                try {
//
//                    requestPostApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                MyToast.toastLong(getActivity(), "All fields must be filled");
//            }
//        } else {
//            if (!TextUtils.isEmpty(experience.getText().toString()) && !medicalId.equalsIgnoreCase("") && !subSpecializationId.equalsIgnoreCase("")
//                    && !specializationId.equalsIgnoreCase("") && !title_Id.equalsIgnoreCase("") && !TextUtils.isEmpty(currentCtc.getText().toString()) && !TextUtils.isEmpty(expectedCtc.getText().toString())
//                    && !TextUtils.isEmpty(currentEmployer.getText().toString()) && !TextUtils.isEmpty(jobLocation.getText().toString())) {
//                //TODO: calling the requestPostApi
//                try {
//                    requestPostApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                MyToast.toastLong(getActivity(), "All fields must be filled");
//            }
//        }

    //   }

    private void openPdfIntent() {
        //TODO: calling the intent to open pdf

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
//                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
//                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
//                        "text/plain",
                        "application/pdf",
//                        "application/zip"
                };


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            intent.setType("*/*");
            if (mimeTypes.length > 0) {
                //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
//        Intent intent = new Intent().setType("application/pdf").setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Pdf"),PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("data", String.valueOf(data));
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            Log.d("PickedPdfPath: ", fileUri.toString());
            //TODO: Calling the upload here
            if (fileUri!=null) {
                try{
                    getPdfPath();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }else {
                Log.e("asdvasdvb", fileUri.toString());
            }
//            Log.e("asdvasdvb",FilePath.getPath(getActivity(), fileUri);)
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getPdfPath() {
        //TODO: getting the pdf path and pdfFile name

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            if (Util.isGoogleDriveUri(fileUri)) {
                pdfPath = getDriveFilePath(fileUri,getActivity());
                //pdfPath = fileUri.getLastPathSegment();
                Log.d("pdfStringPath__", "<><<" + pdfPath);
            } else {
                if (isDownloadsDocument(fileUri))
                {
                    pdfPath = Util.getRealPathFromURI(fileUri, getActivity());
                    Log.d("pdfStringPath___+", "<><<" + pdfPath);
                }
                else {
                    pdfPath = fileUri.getPath();
                    Log.d("pdfStringPath___", "<><<" + pdfPath);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pdfPath = FilePath.getPath(getActivity(), fileUri);
            }
        }

        Log.d("pdfStringPath :", "<><<" + pdfPath);
        try {
            pdfName = pdfPath.substring(pdfPath.lastIndexOf("/") + 1);
            Log.d("pdfNamepdfName :", pdfName);
            //uploadResume.setText(pdfName);
            //TODO: calling the pdf upload here
            uploadFile(new File(pdfPath), url, pdfName, "file");
        } catch (NullPointerException ne) {
            //MyToast.toastLong(getActivity(), "You need to upload files which are stored locally!");
            ne.printStackTrace();
        }
    }

    //TODO: used to upload the pdf to the server
    void uploadFile(File file, final String url, String name, String type) {
        Log.e("file_name", ":" + file);
        PostImage post = new PostImage(file, url, name, getActivity(), type) {
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
                        pdf_name = data.getString("file_name");
                        Log.e("pdfName", pdf_name);
                        //AppCustomPreferenceClass.writeString(getActivity(),"pdf_name_code",pdf_name);
                        //AppCustomPreferenceClass.writeString(getActivity(),"pdf_name",pdfName);
                        uploadResume.setText(pdf_name);
                        //AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.resumePath,pdfPath);
                        MyToast.toastLong(getActivity(), "Your file have been uploaded successfully");
                    } else {
                        MyToast.toastLong(getActivity(), res1);
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

    private void getTitle(String profile_category_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("sync_time", "");
        params.put("profile_category_id", profile_category_id);


        header.put("Cynapse", params);
        Log.d("PARAMTITITL", params + "");
        new GetTitleApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    Log.d("TITLTERESPO", response.toString());
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        // titleSpinner.clear();
                        titleList.clear();
                        JSONArray header2 = header.getJSONArray("Title");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
//                            titleList.add(new JobSpecializationModel(item.getString("title_id"),
//                                    item.getString("profile_category_id"),item.getString("title")));
//                            // titleSpinner.add(item.getString("title"));
//                            Log.e("titleSize",String.valueOf(titleSpinner.size()));
//
//                        Log.e("titleSize",String.valueOf(titleSpinner.size()));

                            JobSpecializationModel model = new JobSpecializationModel();
                            model.setSpecialization_id(item.getString("title_id"));
                            model.setSpecialization_name(item.getString("title"));
                            //  titleList.add(new JobSpecializationModel("-2","Others","Others"));
                            titleList.add(model);
                            Log.d("TITLELISTWIL", titleList + "");
                            titleSelectedList = getModels(false);
//                        ArrayAdapter<String> adapter =new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item
//                                ,titleSpinner);
//                        title_dd.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                            //MyToast.toastLong(getActivity(),res_msg);
//                        if (titleList.size() > 0) {
//
//                            TitleName = new ArrayList<>();
//                            tempTitle = new ArrayList<>();
//                            for (int j = 0; j < titleList.size(); j++) {
//                                // countryName.add(medicalList.get(j).getProfile_category_name());
//                                tempTitle.add(titleList.get(j).getSpecialization_name());
//                                TitleName.add(titleList.get(j).getSpecialization_name());
//                            }
//
////                            adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempTitle);
////                            Title_auto.setAdapter(adapter_title);
////                            Title_auto.setThreshold(1);
                        }
                    } else {
                        //  titleSpinner.clear();
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

        menu_recycler_adapter = new MetroCityAdapter(metrocitySelectedList, getActivity(), RequestJobFragment.this);
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
                jobLocation.setText(cityList.toString().replace("[", "").replace("]", ""));
                dialog.dismiss();
            }
        });
    }

    public void showDialogTitle() {

        EditText loc_search;
        LinearLayoutManager linearLayoutManager;
        TextView cancel_txt, done_txt;


        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multi_title_selection_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        multi_title_sel_recycler = dialog.findViewById(R.id.multi_city_sel_recycler);
        cancel_txt = dialog.findViewById(R.id.cancel_txt);
        done_txt = dialog.findViewById(R.id.done_txt);
        loc_search = dialog.findViewById(R.id.loc_search);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_title_sel_recycler.setLayoutManager(linearLayoutManager);

        titleAdapter_recycler_adapter = new MedicalTitleAdapter(titleSelectedList, getActivity(), RequestJobFragment.this);
        multi_title_sel_recycler.setAdapter(titleAdapter_recycler_adapter);
        loc_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleAdapter_recycler_adapter != null)
                    titleAdapter_recycler_adapter.getFilter().filter(s);
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
//                titleNewList.clear();
//                titleNewListId.clear();
//                Title_auto.setText("");
                dialog.dismiss();
                //TODO : finishing the activity
            }
        });
//        done_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////            metrocityList = getModel(true);
////            Log.d("modelARRAYLIST",metrocityList+"");
////            MetroCityAdapter  recycler_adapter = new MetroCityAdapter(metrocityList,getActivity(),RequestJobFragment.this);
////            multi_city_sel_recycler.setAdapter(recycler_adapter);
//                titleNewList.clear();
//                for (int i = 0; i < titleSelectedList.size(); i++)
//
//                    if (titleSelectedList.get(i).getStatus()) {
//                        // jobLocation.setText(metrocitySelectedList.toString().replace("[","").replace("]",""));
//                        titleNewList.add(titleSelectedList.get(i).getSpecialization_name());
//                        Log.d("modelArrayList", titleList.get(i).getSpecialization_name() + "<><<" + titleList.get(i).getStatus());
//                    }
//                Title_auto.setText(titleNewList.toString().replace("[", "").replace("]", ""));
//                //Title_auto.setEnabled(false);
//                // dialog.dismiss();
//                if (titleNewList.size() > 3) {
//                    Toast toast = Toast.makeText(getActivity(), "Only 3 title can be select!", Toast.LENGTH_LONG);
//
//                    toast.show();
//                    dialog.show();
//                } else {
//                    dialog.dismiss();
//                }
//            }
//        });
        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            metrocityList = getModel(true);
//            Log.d("modelARRAYLIST",metrocityList+"");
//            MetroCityAdapter  recycler_adapter = new MetroCityAdapter(metrocityList,getActivity(),RequestJobFragment.this);
//            multi_city_sel_recycler.setAdapter(recycler_adapter);
                titleNewList.clear();
                titleNewListId.clear();
                for (int i = 0; i < titleSelectedList.size(); i++)

                    if (titleSelectedList.get(i).getStatus()) {
                        // jobLocation.setText(metrocitySelectedList.toString().replace("[","").replace("]",""));
                        titleNewList.add(titleSelectedList.get(i).getSpecialization_name());
                        titleNewListId.add(titleSelectedList.get(i).getSpecialization_id());
                        Log.d("modelArrayList", titleList.get(i).getSpecialization_name() + "<><<" + titleList.get(i).getStatus());
                    }

                Log.d("ISZEOFTITLESELLIS", titleNewList.size() + "");
                medicalTitleId = titleNewListId.toString().replace("[", "").replace("]", "");

                Log.d("MEDICALTITLEIDTDS", medicalTitleId);

                Title_auto.setText(titleNewList.toString().replace("[", "").replace("]", ""));
                if (titleNewList.size() > 3) {
                    Toast toast = Toast.makeText(getActivity(), "Only 3 titles can be selected!", Toast.LENGTH_LONG);
                    toast.show();
                    dialog.show();
                } else {
                    dialog.dismiss();
                }

            }
        });
    }

    private void viewPdf() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.cynapse.provider", new File(pdfPath));
            intent.setDataAndType(contentUri, "application/pdf");
            startActivity(intent);
        } else {
            try {
                intent.setDataAndType(Uri.fromFile(new File(pdfPath)), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                MyToast.toastLong(getActivity(), "No Application Available to View PDF");
            }
        }
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

    private ArrayList<JobSpecializationModel> getModels(boolean isSelect) {
        ArrayList<JobSpecializationModel> list = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {

            JobSpecializationModel model = new JobSpecializationModel();
            model.setStatus(isSelect);
            model.setSpecialization_id(titleList.get(i).getSpecialization_id());
            model.setSpecialization_name(titleList.get(i).getSpecialization_name());
            list.add(model);
        }
        return list;
    }

    ArrayList<String> titleList1;


    @Override
    public void selectedTitles(ArrayList<String> titleList) {
        Log.d("titleNewList", titleList + "<><");
//        titleList1=titleList;
        //jobLocation.setText(citylist.toString().replace("[","").replace("]",""));
//        for(int i = 0;i< titleList.size();i++)
//         jobLocation.setText(citylist.get(0)+","+citylist.get(1),);
//        medicalTitleId = title_Id;
        medicalTitleId = titleList.toString().replace("[", "").replace("]", "");

        Log.d("MEDICALTITLEID", medicalTitleId);
        //  }

    }

    @Override
    public void selectedCities(ArrayList<String> citylist) {
        Log.d("cityList", citylist + "<><");
        //jobLocation.setText(citylist.toString().replace("[","").replace("]",""));
//        for(int i = 0;i< citylist.size();i++)
//        {
//            jobLocation.setText(citylist.get(0)+","+citylist.get(1),);
//        }

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
                        //   MyToast.toastLong(getActivity(), res_msg);
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
                        medicalProfileauto.setText(getProfileName);
                        title_Id = item.getString("title_id");
                        medicalTitleId = item.getString("title_id");
                        Title_auto.setText(item.getString("title_name"));
                        titleList1.add(item.getString("title_id"));
                        specializationId = item.getString("specialization_id");
                        Spealization_auto.setText(item.getString("specialization_name"));
                        subSpecializationId = item.getString("sub_specialization_id");
                        SubS_auto.setText(item.getString("sub_specialization_name"));
                        dept_Id = item.getString("department_id");
                        dept_auto.setText(item.getString("department_name"));
                        Log.d("MEDICLPROFILECATEGORYNM", getProfileName);
                        getProfileId = item.getString("medical_profile_category_id");
                        email = item.getString("email");
                        phoneNumber = item.getString("phone_number");

                        Log.d("MEDICLPROFILEIDD", getProfileId);
                        if (medicalId.equals("")) {
                            medicalId = getProfileId;
                        }

                        Log.d("MEFIIDIDID", medicalId);
                        if(getProfileId.equalsIgnoreCase("2") || getProfileId.equalsIgnoreCase("12") || getProfileId.equalsIgnoreCase("13")
                                ||getProfileId.equalsIgnoreCase("1")||getProfileId.equalsIgnoreCase("4"))
                        {
                            getTitle(getProfileId);
                        }
                        else
                        {
                            getTitle("13");
                        }
                        try {
                            GetSubSpecializationApi(specializationId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            GetDepartmentApi(medicalId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(getProfileId.equalsIgnoreCase("1"))
                        {
                            reg_rel_lay.setVisibility(View.VISIBLE);
                        }else
                        {
                            reg_rel_lay.setVisibility(View.GONE);
                        }

                        if (getProfileId.equalsIgnoreCase("2") || getProfileId.equalsIgnoreCase("12") || getProfileId.equalsIgnoreCase("13")) {
                            try {
                                GetDepartmentApi(getProfileId);
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
    private void getStateApi(String countryId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", countryId);
        header.put("Cynapse", params);
        new GetStateApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("STATERESPONSE", response.toString());
                    if (res_code.equals("1")) {
//                        stateSpinner.clear();
                        stateList.clear();
//                        cityList.clear();
//                        citySpinner.clear();
                        // MyToast.toastLong(getActivity(), res_msg);
                        JSONArray header2 = header.getJSONArray("State");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            stateList.add(new StateModel(item.getString("country_code"), item.getString("state_code"), item.getString("state_name")));

                            // stateSpinner.add(item.getString("state_name"));
                        }
                        if (stateList.size() > 0) {

                            stateName = new ArrayList<>();
                            tempstate = new ArrayList<>();
                            for (int j = 0; j < stateList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempstate.add(stateList.get(j).getState_name());
                                stateName.add(stateList.get(j).getState_name());
                            }

                            state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempstate);
                            state_auto.setAdapter(state_adapter);
                            state_auto.setThreshold(1);
                        }
                    } else {
//                        stateSpinner.clear();
//                        stateList.clear();
//                        cityList.clear();
//                        citySpinner.clear();
                        // MyToast.toastLong(getActivity(), res_msg);
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


//    akash commit changes

    private void getAllCountry() {
        new GetCountryClass(getActivity()) {
            @Override
            protected void responseCountry(ArrayList<CountryModel> countryNameList) {
                countryList=countryNameList;
                countryListName=new ArrayList<>();


                Iterator iterator=countryList.iterator();

                while(iterator.hasNext()){
                    CountryModel countryModel=(CountryModel)iterator.next();
                    countryListName.add(countryModel.getCountry_name());

                }
            }
        };


        country_auto1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    try {


                        Log.e("focusCountry",countryListName.size()+"");
                        if (country_auto1.getText().toString().equals("")) {
                            tempCountryListName.clear();
                            tempCountryListName.addAll(countryListName);
                        }


                        country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCountryListName);
                        country_auto1.setAdapter(country_adapter);
                        country_auto1.showDropDown();}catch (Exception e){
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
                Log.e("textCheck",s.toString());
                if (s.toString().equals("")) {
                    try {
                        Log.e("textCheck2",s.toString());

                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                        country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCountryListName);
                        country_auto1.setAdapter(country_adapter);
                        country_auto1.showDropDown();

                    }catch (Exception e){
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

    private void statesetupListener() {

        state_auto1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    try{
                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                        Log.e("focusState",stateListName.size()+"");
                        state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempStateListName);
                        state_auto1.setAdapter(state_adapter);
                        state_auto1.showDropDown();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        state_auto1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state_Str = state_auto1.getText().toString();

                int num= stateListName.toString().indexOf(state_Str);
                for(int i=0;i<stateListName.size();i++){
                    if(stateListName.get(i).equals(state_Str)){
                        num=i;
                        break;
                    }
                }
                Log.e("focusStateId",stateList.size()+","+num+","+tempStateListCode.size()+","+state_Str);
                try {


                    state_id = stateList.get(num).getState_code();
                    country_id = stateList.get(num).getCountry_code();
                    Log.e("focusStateId",state_id);
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                //city_auto1.setText("");
                clearCities();
                Log.d("Id",country_id);
                Log.d("Id",state_id);

                getAllCity(country_id,state_id);
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
                        state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempStateListName);
                        state_auto1.setAdapter(state_adapter);
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
        new GetCityClass(getActivity(), country_id, state_id) {
            @Override
            protected void responseCity(ArrayList<CityModel> responseCity) {
                cityList1 = responseCity;
                Log.d("cities123",responseCity.toString());
                cityListName=new ArrayList<>();

                Iterator iterator=cityList1.iterator();

                while(iterator.hasNext()){
                    CityModel countryModel=(CityModel) iterator.next();
                    cityListName.add(countryModel.getCity_name());
                    Log.e("CityCount",cityListName.size()+"");

                }

            }
        };
    }

    private void setUpCityListener() {
        cityChipsNameList.clear();
        city_auto1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    try{
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                        Log.e("focusCity",cityListName.size()+"");
                        city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_request_job, R.id.lbl_name, tempCityListName);
                        city_auto1.setAdapter(city_adapter);
                        city_auto1.showDropDown();
                    }catch (Exception e){
                        e.printStackTrace();
                    } }
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

                    }catch (Exception e){
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
                Log.e("chipsDeletePosion",index+","+label+","+cityChipsNameList.size());
            }
        });
    }


    private void addChipsData(String citys) {
        if(cityChipsNameList.size()<6){

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
            Toast.makeText(getActivity(), "Maximum 6 cities can be selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void ifAlreadyAdded() {
        Toast.makeText(getActivity(), "City Name Already Exists!", Toast.LENGTH_SHORT).show();
    }

    private void getAllState(String country_id) {

        new GetStateClass(getActivity(),country_id) {
            @Override
            protected void responseState(ArrayList<StateModel> responseState) {
                stateList = responseState;
                stateListName=new ArrayList<>();

                Iterator iterator=stateList.iterator();

                while(iterator.hasNext()){
                    StateModel countryModel=(StateModel) iterator.next();
                    tempStateListCode.add(countryModel.getState_code());
                    stateListName.add(countryModel.getState_name());

                }

            }
        };


    }

//    akash commit changes
}

//========filter location done=============
//RequestJobFragment,JobRequirementFilter,PostJobFragment,FilterConferenceActivity