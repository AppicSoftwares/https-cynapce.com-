package com.alcanzar.cynapse.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.alcanzar.cynapse.GoogleOtp.AppSignatureHashHelper;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.LoginSignUpActivity;
import com.alcanzar.cynapse.activity.OtpActivity;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.api.GetDepartmentApi;
import com.alcanzar.cynapse.api.GetHighestDegreeApi;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.GetYearOfStudyApi;
import com.alcanzar.cynapse.api.SignUpApi;
import com.alcanzar.cynapse.model.HighestDegreeModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.GPSTracker;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment implements  View.OnClickListener, AdapterView.OnItemSelectedListener {
    ImageButton btnOne, btnTwo;
    Button btnNext;
    LinearLayout relOne, relTwo;
    RelativeLayout other_rel_lay, other_spl_rel_lay, other_hd_rel_lay, other_Yos_rel_lay, yos_rel_lay, hd_rel_lay, other_dept_rel_lay, dept_rel_lay, Spl_rel_lay;
    View dept_view, spl_view;
    //TODO: Sign Up Fields
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    ArrayList<HighestDegreeModel> highestDegreeList = new ArrayList<>();
    EditText email, phone, password, confirmPassword, edit_medical_others, edit_spl_others, edit_hd_others, name, edit_yos_others, edit_dept_others;
    Spinner medicalProfile, specialization, highestDegree;
    private String medicalId = "", specializationId = "", highestDegreeId = "", medStr = "", splStr = "", hdStr = "", Yos_ID = "", yosStr = "", dept_Id = "", dept_Str = "";
    ArrayList<String> medicalProfileName;
    ArrayList<String> specializationName;
    ArrayList<String> YosName;
    ArrayList<String> HdName;
    ArrayList<String> tempMedProf;
    ArrayList<String> tempSplzation;
    ArrayList<String> DeptName;
    ArrayList<String> tempDept;
    ArrayList<String> tempHd;
    ArrayList<String> tempYos;
    ArrayList<MedicalProfileModel> dept_SpinnerList = new ArrayList<>();
    ArrayList<MedicalProfileModel> medicalList = new ArrayList<>();
    ArrayList<MedicalProfileModel> YosList = new ArrayList<>();
    ArrayAdapter adapter_medical_profile, adapter_specialization, adapter_Hd, adapter_Yos, adapter_dept;
    AutoCompleteTextView medicalProfileauto, Spealization_auto, Hd_auto, Yos_auto, dept_auto;
    ImageButton crossC, crossS, crossHd;
    public GPSTracker tracker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: initialize views

        initializeViews(view);
        //TODO: clearing the list data
        clearListData();
        tracker = new GPSTracker(getActivity()) {
            @Override
            public void setLoc() {

            }
        };

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                //TODO: android matcher
                if (Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                    email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, R.drawable.green_tick, 0);
                }
                //TODO: CustomMatcher
//                if((str.matches(emailStrOne) || str.matches(emailStrTwo)) && s.length() > 0){
//                    email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
//                }
                else if (s.length() == 0) {
                    email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
                } else {
                    email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, R.drawable.exclaimtionmark, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5) {
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.green_tick, 0);
                } else if (s.length() == 0) {
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
                } else {
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.exclaimtionmark, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Log.e("pass_match ", "<><" + s.toString() + ",.,<<" + password.getText().toString().trim());
                if (s.toString().equals(password.getText().toString().trim())) {
                    confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.green_tick, 0);
                } else if (s.length() == 0) {
                    confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
                } else {
                    confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.exclaimtionmark, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        //confirmPassword.addTextChangedListener(watcher);
    }

    private void clearListData() {
        // medicalList.clear();
        // medicalProfileSpinner.clear();

    }




    //TODO: here the initialization of the views are done
    private void initializeViews(View view) {

        appSignature = new AppSignatureHashHelper(getActivity());

        medicalProfile = view.findViewById(R.id.medicalProfile);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        password = view.findViewById(R.id.password);
        name = view.findViewById(R.id.name);
        edit_medical_others = view.findViewById(R.id.edit_medical_others);
        edit_spl_others = view.findViewById(R.id.edit_spl_others);
        edit_hd_others = view.findViewById(R.id.edit_hd_others);
        edit_yos_others = view.findViewById(R.id.edit_yos_others);
        edit_dept_others = view.findViewById(R.id.edit_dept_others);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        highestDegree = view.findViewById(R.id.highestDegree);
        specialization = view.findViewById(R.id.specialization);
        other_rel_lay = view.findViewById(R.id.other_rel_lay);
        other_spl_rel_lay = view.findViewById(R.id.other_spl_rel_lay);
        other_hd_rel_lay = view.findViewById(R.id.other_hd_rel_lay);
        hd_rel_lay = view.findViewById(R.id.hd_rel_lay);
        Spl_rel_lay = view.findViewById(R.id.Spl_rel_lay);
        other_Yos_rel_lay = view.findViewById(R.id.other_Yos_rel_lay);
        dept_rel_lay = view.findViewById(R.id.dept_rel_lay);
        other_dept_rel_lay = view.findViewById(R.id.other_dept_rel_lay);
        yos_rel_lay = view.findViewById(R.id.yos_rel_lay);
        medicalProfileauto = view.findViewById(R.id.medicalProfileauto);
        Spealization_auto = view.findViewById(R.id.Spealization_auto);
        dept_auto = view.findViewById(R.id.dept_auto);
        Hd_auto = view.findViewById(R.id.Hd_auto);
        Yos_auto = view.findViewById(R.id.Yos_auto);
        crossC = view.findViewById(R.id.crossC);
        crossS = view.findViewById(R.id.crossS);
        crossHd = view.findViewById(R.id.crossHd);
        btnNext = view.findViewById(R.id.btnNext);

        dept_view = view.findViewById(R.id.dept_view);
        spl_view = view.findViewById(R.id.spl_view);

        medicalProfileName = new ArrayList<>();
        specializationName = new ArrayList<>();
        HdName = new ArrayList<>();
        btnNext.setOnClickListener(this);
        //TODO: spinners onItemSelectedListeners
        // medicalProfile.setOnItemSelectedListener(this);
        // specialization.setOnItemSelectedListener(this);
        //  highestDegree.setOnItemSelectedListener(this);
        medicalProfileauto.setOnClickListener(this);
        Spealization_auto.setOnClickListener(this);
        dept_auto.setOnClickListener(this);
        Hd_auto.setOnClickListener(this);
        Yos_auto.setOnClickListener(this);
        //TODO: here changing the text color of the signUp login during call
        ((LoginSignUpActivity) getActivity()).btnLogIn.setTextColor(Color.parseColor("#DEE0E4"));
        ((LoginSignUpActivity) getActivity()).btnSignUp.setTextColor(Color.parseColor("#4B4B4B"));
        //demo spinner get
        //TODO: calling the getMedicalProfileApi

        medicalProfileauto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // crossC.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    try {
                        if (medicalProfileauto.getText().toString().equals("")) {
                            tempMedProf = new ArrayList<>();
                            tempMedProf.addAll(medicalProfileName);
                        }
                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempMedProf);
                        medicalProfileauto.setAdapter(adapter_medical_profile);
                        medicalProfileauto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (medicalProfileauto.toString().equals("")) {
                        //crossC.setVisibility(View.GONE);
                    } else {
                        // crossC.setVisibility(View.VISIBLE);
                    }

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

                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempMedProf);
                        medicalProfileauto.setAdapter(adapter_medical_profile);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    // crossC.setVisibility(View.GONE);
                } else {
                    //crossC.setVisibility(View.VISIBLE);
                }

            }
        });
        medicalProfileauto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                medStr = medicalProfileauto.getText().toString();
                medicalId = medicalList.get(medicalProfileName.indexOf(medStr)).getId();
                edit_medical_others.setText("");
                if (medicalId.equalsIgnoreCase("-1")) {
                    other_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_rel_lay.setVisibility(View.GONE);
                }
                if (medicalId.equalsIgnoreCase("4")) {
                    yos_rel_lay.setVisibility(View.VISIBLE);
                    hd_rel_lay.setVisibility(View.GONE);

                } else {
                    yos_rel_lay.setVisibility(View.GONE);
                    hd_rel_lay.setVisibility(View.VISIBLE);
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
                    Spealization_auto.setVisibility(View.GONE);
                    dept_auto.setVisibility(View.VISIBLE);
                } else {
                    dept_rel_lay.setVisibility(View.GONE);
                    dept_view.setVisibility(View.GONE);
                    Spl_rel_lay.setVisibility(View.VISIBLE);
                    spl_view.setVisibility(View.VISIBLE);
                    Spealization_auto.setVisibility(View.VISIBLE);
                    dept_auto.setVisibility(View.GONE);
                }
                try {
                    getHighestDegreeApi(medicalId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    getProfileSpecialization("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (medicalId.equalsIgnoreCase("Select Medical Profile")) {
                    medicalId = "";
                }

                Spealization_auto.setText("");

                dept_auto.setText("");
                Hd_auto.setText("");
                Yos_auto.setText("");
                edit_hd_others.setText("");
                edit_medical_others.setText("");
                edit_spl_others.setText("");
                edit_yos_others.setText("");
                edit_dept_others.setText("");

                other_dept_rel_lay.setVisibility(View.GONE);
                other_spl_rel_lay.setVisibility(View.GONE);
                other_hd_rel_lay.setVisibility(View.GONE);
                other_Yos_rel_lay.setVisibility(View.GONE);
                Log.d("medicalId", medicalId);

            }
        });
        Spealization_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    //crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (Spealization_auto.getText().toString().equals("")) {
                            tempSplzation = new ArrayList<>();
                            tempSplzation.addAll(specializationName);
                        }
                        adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                        Spealization_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (Spealization_auto.toString().equals("")) {
                        // crossS.setVisibility(View.GONE);
                    } else {
                        // crossS.setVisibility(View.VISIBLE);
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

                        adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    // crossS.setVisibility(View.GONE);
                } else {
                    //crossS.setVisibility(View.VISIBLE);
                }

            }
        });
        Spealization_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                splStr = Spealization_auto.getText().toString();
                specializationId = specializationList.get(specializationName.indexOf(splStr)).getSpecialization_id();
                edit_spl_others.setText("");
                if (specializationId.equalsIgnoreCase("-2")) {
                    other_spl_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_spl_rel_lay.setVisibility(View.GONE);
                }
                if (specializationId.equalsIgnoreCase("Select Specialization")) {
                    specializationId = "";
                }
                Log.d("specializationId", specializationId);


            }
        });
        Hd_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (Hd_auto.getText().toString().equals("")) {
                            tempHd = new ArrayList<>();
                            tempHd.addAll(HdName);
                        }
                        adapter_Hd = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempHd);
                        Hd_auto.setAdapter(adapter_Hd);
                        Hd_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (Hd_auto.toString().equals("")) {
                        // crossS.setVisibility(View.GONE);
                    } else {
                        //  crossS.setVisibility(View.VISIBLE);
                    }

                }


            }


        });
        dept_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (dept_auto.getText().toString().equals("")) {
                        tempDept = new ArrayList<>();

                        try {
                            tempDept.addAll(DeptName);
                            adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempDept);
                            dept_auto.setAdapter(adapter_dept);
                            dept_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }


                } else {
                    if (dept_auto.toString().equals("")) {

                    } else {

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

                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempDept);
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

                try {
                    dept_Id = dept_SpinnerList.get(DeptName.indexOf(dept_Str)).getId();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                edit_dept_others.setText("");
                if (dept_Id.equalsIgnoreCase("-5")) {
                    other_dept_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_dept_rel_lay.setVisibility(View.GONE);
                }
            }
        });

        Hd_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempHd = new ArrayList<>();
                        tempHd.addAll(HdName);

                        adapter_Hd = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempHd);
                        Hd_auto.setAdapter(adapter_Hd);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    //  crossS.setVisibility(View.GONE);
                } else {
                    // crossS.setVisibility(View.VISIBLE);
                }

            }
        });
        Hd_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                hdStr = Hd_auto.getText().toString();
                Log.d("hdStr", hdStr);
                try {
                    highestDegreeId = highestDegreeList.get(HdName.indexOf(hdStr)).getDegree_id();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                edit_hd_others.setText("");
                if (highestDegreeId.equalsIgnoreCase("-3")) {
                    other_hd_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_hd_rel_lay.setVisibility(View.GONE);
                }
                if (highestDegreeId.equalsIgnoreCase("Select Highest Degree")) {
                    highestDegreeId = "";
                }
                Log.d("highestDegreeId", highestDegreeId);


            }
        });
        Yos_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (Yos_auto.getText().toString().equals("")) {
                            tempYos = new ArrayList<>();
                            tempYos.addAll(YosName);
                        }
                        adapter_Yos = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempYos);
                        Yos_auto.setAdapter(adapter_Yos);
                        Yos_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (Yos_auto.toString().equals("")) {
                        // crossS.setVisibility(View.GONE);
                    } else {
                        //  crossS.setVisibility(View.VISIBLE);
                    }

                }


            }


        });
        Yos_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempYos = new ArrayList<>();
                        tempYos.addAll(YosName);

                        adapter_Yos = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempYos);
                        Yos_auto.setAdapter(adapter_Yos);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    //  crossS.setVisibility(View.GONE);
                } else {
                    // crossS.setVisibility(View.VISIBLE);
                }

            }
        });
        Yos_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                yosStr = Yos_auto.getText().toString();
                Yos_ID = YosList.get(YosName.indexOf(yosStr)).getId();
                edit_yos_others.setText("");
                if (Yos_ID.equalsIgnoreCase("-4")) {
                    other_Yos_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_Yos_rel_lay.setVisibility(View.GONE);
                }

                Log.d("Yos_ID", Yos_ID);

            }
        });
        getMedicalProfileApi();


        try {
            GetYearOfStudyApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (isValidScreenOne()) {
                    try {
                        callSignUpApi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.medicalProfileauto:
                try {
                    if (medicalProfileauto.getText().toString().equals("")) {
                        tempMedProf = new ArrayList<>();
                        tempMedProf.addAll(medicalProfileName);
                    }

                    adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempMedProf);
                    medicalProfileauto.setAdapter(adapter_medical_profile);
                    medicalProfileauto.showDropDown();
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

                    adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempSplzation);
                    Spealization_auto.setAdapter(adapter_specialization);
                    Spealization_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;
            case R.id.Hd_auto:
                try {
                    if (Hd_auto.getText().toString().equals("")) {
                        tempHd = new ArrayList<>();
                        tempHd.addAll(HdName);
                    }

                    adapter_Hd = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempHd);
                    Hd_auto.setAdapter(adapter_Hd);
                    Hd_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;
            case R.id.Yos_auto:
                try {
                    if (Yos_auto.getText().toString().equals("")) {
                        tempYos = new ArrayList<>();
                        tempYos.addAll(YosName);
                    }

                    adapter_Yos = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempYos);
                    Yos_auto.setAdapter(adapter_Yos);
                    Yos_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;

        }
    }

    private boolean isValidScreenOne() {
        boolean isValid = false;
        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
            if (TextUtils.isEmpty(name.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter name");
            } else if (TextUtils.isEmpty(email.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                MyToast.toastShort(getActivity(), "Please enter valid Email");
                return false;
            } else if (TextUtils.isEmpty(phone.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Phone Number");
            } else if (phone.getText().length() < 10) {
                MyToast.toastShort(getActivity(), "Invalid Phone Number");
            } else if (TextUtils.isEmpty(password.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Password");
            } else if (password.getText().length() < 6) {
                MyToast.toastShort(getActivity(), "Password should be of atleast 6 characters");
            } else if (!isValidPassword(password.getText().toString())) {
                MyToast.toastShort(getActivity(), "Password should contain alphabets,numbers and special characters");
            } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Confirm Password");
            } else if (!confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                MyToast.toastLong(getActivity(), "Password and Confirm Password must be same");
            } else if (medicalId.equalsIgnoreCase("")) {

                MyToast.toastShort(getActivity(), "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_medical_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Medical Profile cannot be blank!");
                return false;
            } else if (dept_Id.equalsIgnoreCase("")) {
                MyToast.toastShort(getActivity(), "Please Enter Department");
                return false;
            } else if (dept_Id.equalsIgnoreCase("-5") && edit_dept_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Department cannot be blank!");
                return false;
            } else if (highestDegreeId.equalsIgnoreCase("")) {
                MyToast.toastShort(getActivity(), "Please Enter Highest Degree");
            } else if (highestDegreeId.equalsIgnoreCase("-3") && edit_hd_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Highest Degree cannot be blank!");
                return false;
            } else {
                isValid = true;
            }
        } else if (medicalId.equalsIgnoreCase("4")) {
            if (TextUtils.isEmpty(name.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter name");
            } else if (TextUtils.isEmpty(email.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                MyToast.toastShort(getActivity(), "Please enter valid Email");
            } else if (TextUtils.isEmpty(phone.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Phone Number");
            } else if (phone.getText().length() < 10) {
                MyToast.toastShort(getActivity(), "Invalid Phone Number");
            } else if (TextUtils.isEmpty(password.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Password");
            } else if (password.getText().length() < 6) {
                MyToast.toastShort(getActivity(), "Password should be of atleast 6 characters");
            } else if (!isValidPassword(password.getText().toString())) {
                MyToast.toastShort(getActivity(), "Password should contain alphabets,numbers and special characters");
            } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Confirm Password");
            } else if (!confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                MyToast.toastLong(getActivity(), "Password and Confirm Password must be same");
            } else if (medicalId.equalsIgnoreCase("")) {

                MyToast.toastShort(getActivity(), "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_medical_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Medical Profile cannot be blank!");
                return false;
            } else if (specializationId.equalsIgnoreCase("")) {
                MyToast.toastShort(getActivity(), "Please Enter Specialization");
            } else if (specializationId.equalsIgnoreCase("-2") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Specialization cannot be blank!");
                return false;
            } else if (Yos_ID.equalsIgnoreCase("")) {
                MyToast.toastShort(getActivity(), "Please Enter Years of Study");
            } else if (Yos_ID.equalsIgnoreCase("-4") && edit_yos_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Years of Study cannot be blank!");
                return false;
            } else {
                isValid = true;
            }
        } else {
            if (TextUtils.isEmpty(name.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter name");
            } else if (TextUtils.isEmpty(email.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                MyToast.toastShort(getActivity(), "Please enter valid Email");
            } else if (TextUtils.isEmpty(phone.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Phone Number");
            } else if (phone.getText().length() < 10) {
                MyToast.toastShort(getActivity(), "Invalid Phone Number");
            } else if (TextUtils.isEmpty(password.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Password");
            } else if (password.getText().length() < 6) {
                MyToast.toastShort(getActivity(), "Password should be of atleast 6 characters");
            } else if (!isValidPassword(password.getText().toString())) {
                MyToast.toastShort(getActivity(), "Password should contain alphabets,numbers and special characters");
            } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
                MyToast.toastShort(getActivity(), "Please enter Confirm Password");
            } else if (!confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                MyToast.toastLong(getActivity(), "Password and Confirm Password must be same");
            } else if (medicalId.equalsIgnoreCase("")) {

                MyToast.toastShort(getActivity(), "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_medical_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Medical Profile cannot be blank!");
                return false;
            } else if (specializationId.equalsIgnoreCase("")) {
                MyToast.toastShort(getActivity(), "Please Enter Specialization");
            } else if (specializationId.equalsIgnoreCase("-2") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Specialization cannot be blank!");
                return false;
            } else if (highestDegreeId.equalsIgnoreCase("")) {
                MyToast.toastShort(getActivity(), "Please Enter Highest Degree");
            } else if (highestDegreeId.equalsIgnoreCase("-3") && edit_hd_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(getActivity(), "Others for Highest Degree cannot be blank!");
                return false;
            } else {
                isValid = true;
            }
        }


        return isValid;
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%&*()_+=|<>?{}\\\\[\\\\]~-])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static String getDeviceID(Context ctx) {
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //TODO: API Calling
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
                        //medicalProfileSpinner.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("ProfileCategoryMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            medicalList.add(new MedicalProfileModel(item.getString("id"), item.getString("profile_category_name")));
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

                            adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempMedProf);
                            medicalProfileauto.setAdapter(adapter_medical_profile);
                            medicalProfileauto.setThreshold(1);
                        }

                    } else {
                        //  medicalList.clear();
                        //  medicalProfileSpinner.clear();
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
                        // jobSpecializationSpinner.clear();
                        specializationList.clear();
                        JSONArray header2 = header.getJSONArray("ProfileSpecializationMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            specializationList.add(new JobSpecializationModel(item.getString("specialization_id"),
                                    item.getString("profile_category_id"), item.getString("specialization_name")));
                            //  jobSpecializationSpinner.add(item.getString("specialization_name"));
                            //   Log.e("jobSpecializationSize", String.valueOf(jobSpecializationSpinner.size()));
                        }
                        specializationList.add(new JobSpecializationModel("-2", "1", "Others"));
                        //jobSpecializationSpinner.add("Others");
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item
//                                , jobSpecializationSpinner);
//                        specialization.setAdapter(adapter);
                        //  adapter.notifyDataSetChanged();


                        if (specializationList.size() > 0) {

                            specializationName = new ArrayList<>();
                            tempSplzation = new ArrayList<>();
                            for (int j = 0; j < specializationList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempSplzation.add(specializationList.get(j).getSpecialization_name());
                                specializationName.add(specializationList.get(j).getSpecialization_name());
                            }

                            adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempSplzation);
                            Spealization_auto.setAdapter(adapter_specialization);
                            Spealization_auto.setThreshold(1);

                        }


                        //MyToast.toastLong(getActivity(),res_msg);
                    } else {
                        // jobSpecializationSpinner.clear();
                        // specializationList.clear();
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

    private void getHighestDegreeApi(String id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("medical_profile_id", id);
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetHighestDegreeApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
//                {
//                    "Cynapse": {
//                    "res_code": "1",
//                            "res_msg": "Highest Degree.",
//                            "sync_time": 1521715148,
//                            "Degree": [
//                    {
//                        "degree_id": "1",
//                            "degree_name": "MMBBS",
//                            "status": "1",
//                            "add_date": "1521712599",
//                            "modify_date": "1521712599"
//                    },
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        highestDegreeList.clear();
                        //  highestDegreeSpinner.clear();
                        JSONArray header2 = header.getJSONArray("Degree");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            highestDegreeList.add(new HighestDegreeModel(item.getString("degree_id"), item.getString("degree_name")));
                            // highestDegreeSpinner.add(item.getString("degree_name"));
                            // Log.d("highestDegreeList", String.valueOf(highestDegreeSpinner.size()));
                        }
                        highestDegreeList.add(new HighestDegreeModel("-3", "Others"));
                        if (highestDegreeList.size() > 0) {

                            HdName = new ArrayList<>();
                            tempHd = new ArrayList<>();
                            for (int j = 0; j < highestDegreeList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempHd.add(highestDegreeList.get(j).getDegree_name());
                                HdName.add(highestDegreeList.get(j).getDegree_name());
                            }

                            adapter_Hd = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempHd);
                            Hd_auto.setAdapter(adapter_Hd);
                            Hd_auto.setThreshold(1);
                        }
                        // highestDegree.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, highestDegreeSpinner));
                        // MyToast.toastLong(getActivity(),res_msg);
                    } else {
                        highestDegreeList.clear();
                        highestDegreeList.add(new HighestDegreeModel("-3", "Others"));
                        if (highestDegreeList.size() > 0) {

                            HdName = new ArrayList<>();
                            tempHd = new ArrayList<>();
                            for (int j = 0; j < highestDegreeList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempHd.add(highestDegreeList.get(j).getDegree_name());
                                HdName.add(highestDegreeList.get(j).getDegree_name());
                            }

                            adapter_Hd = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempHd);
                            Hd_auto.setAdapter(adapter_Hd);
                            Hd_auto.setThreshold(1);
                        }
                        //  highestDegreeList.clear();
                        //  highestDegreeSpinner.clear();
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

    private void GetYearOfStudyApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("medical_profile_id", "4");
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetYearOfStudyApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        // jobSpecializationSpinner.clear();
                        // specializationList.clear();
                        JSONArray header2 = header.getJSONArray("YearOfStudy");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            YosList.add(new MedicalProfileModel(item.getString("id"), item.getString("year_of_study_name")));
                            //  jobSpecializationSpinner.add(item.getString("specialization_name"));
                            //   Log.e("jobSpecializationSize", String.valueOf(jobSpecializationSpinner.size()));
                        }
                        YosList.add(new MedicalProfileModel("-4", "Others"));

                        if (YosList.size() > 0) {

                            YosName = new ArrayList<>();
                            tempYos = new ArrayList<>();
                            for (int j = 0; j < YosList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempYos.add(YosList.get(j).getId());
                                YosName.add(YosList.get(j).getProfile_category_name());
                            }

                            adapter_Yos = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempYos);
                            Yos_auto.setAdapter(adapter_Yos);
                            Yos_auto.setThreshold(1);

                        }


                        //MyToast.toastLong(getActivity(),res_msg);
                    } else {
                        // jobSpecializationSpinner.clear();
                        // specializationList.clear();
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

    private void callSignUpApi() throws JSONException {
        //TODO : json Objects Creation
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        //TODO: putting all the parameters


        if (medicalId.equalsIgnoreCase("-1")) {
            params.put("medical_profile_id", "othermedicalprofile");
            params.put("others_medical_profile", edit_medical_others.getText().toString());
        } else {
            params.put("medical_profile_id", medicalId);
            params.put("others_medical_profile", "");
        }
        if (specializationId.equalsIgnoreCase("-2")) {
            params.put("specialization_id", "otherspecialization");
            params.put("others_specialization", edit_spl_others.getText().toString());
        } else {
            params.put("specialization_id", specializationId);
            params.put("others_specialization", "");
        }
        if (highestDegreeId.equalsIgnoreCase("-3")) {
            params.put("highest_degree", "otherdegree");
            params.put("others_highest_degree", edit_hd_others.getText().toString());
        } else {
            params.put("highest_degree", highestDegreeId);
            params.put("others_highest_degree", "");
        }
        if (dept_Str.equalsIgnoreCase("Others")) {
            params.put("department_id", "otherdepartment");
            params.put("others_department", edit_dept_others.getText().toString());
        } else {

            params.put("department_id", dept_Id);
            params.put("others_department", "");
        }
        if (Yos_ID.equalsIgnoreCase("-4")) {
            params.put("year_of_study", "otheryearofstudy");
            params.put("others_year_of_study", edit_yos_others.getText().toString());
        } else {
            params.put("year_of_study", Yos_ID);
            params.put("others_year_of_study", "");
        }
        params.put("name", name.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phone_number", phone.getText().toString());
        params.put("password", password.getText().toString());
        params.put("device_type", "android-mobile");
        params.put("platform_type", "Android");
        params.put("device_id", getDeviceID(getActivity()));

        String appSing = appSignature.getAppSignatures().get(0);

        params.put("hash_key", appSing);
        header.put("Cynapse", params);
        new SignUpApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    if (res_code.trim().equals("1")) {
                        // MyToast.toastLong(getActivity(), res_msg);
                        JSONObject item = header.getJSONObject("SignUp");
                        //TODO: saving the SignUpDetails
                        //AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.UserId, item.getString("uuid"));
                        // AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.authToken, item.getString("auth_token"));
//                        AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.name,item.getString("name"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email, item.getString("email"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.phoneNumber, item.getString("phone_number"));
                        //Log.e("uuid",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.UserId,""));
                        //Log.e("authToken",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.authToken,""));
//                        startActivity(new Intent(getActivity(), MainActivity.class));
//                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        ActivityCompat.finishAffinity(getActivity());
//                        getActivity().finish();

                        Intent intent = new Intent(getActivity(), OtpActivity.class);
                        intent.putExtra("phone_no", phone.getText().toString());
                        intent.putExtra("uuid", item.getString("uuid"));
                        intent.putExtra("from", "signup");
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        ActivityCompat.finishAffinity(getActivity());
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
                        //  dept_Spinner.clear();
                        dept_SpinnerList.clear();
                        JSONArray header2 = header.getJSONArray("Department");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            dept_SpinnerList.add(new MedicalProfileModel(item.getString("id"), item.getString("department_name")));
                            // dept_Spinner.add(item.getString("department_name"));
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

                            adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_sign_up, R.id.lbl_name, tempDept);
                            dept_auto.setAdapter(adapter_dept);
                            dept_auto.setThreshold(1);
                        }
//                        ArrayAdapter<String> adapter =new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item
//                                ,dept_Spinner);
//                        dept_dd.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(getActivity(),res_msg);
                    } else {
                        // dept_Spinner.clear();
                        // dept_SpinnerList.clear();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //TODO: spinner item on selectedListeners
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
//               try {
//                   getProfileSpecialization(medicalId);
//               } catch (JSONException e) {
//                   e.printStackTrace();
//               }
                break;
            case R.id.jobSpecialization:
                String jobSpecializationStr = specialization.getItemAtPosition(specialization.getSelectedItemPosition()).toString();
                specializationId = specializationList.get(position).getSpecialization_id();
                if (specializationId.equalsIgnoreCase("Select Specialization")) {
                    specializationId = "";
                }
                Log.d("Job", specializationId);
                break;
            case R.id.highestDegree:
                String highestDegreeStr = highestDegree.getItemAtPosition(highestDegree.getSelectedItemPosition()).toString();
                highestDegreeId = highestDegreeList.get(position).getDegree_id();
                if (highestDegreeId.equalsIgnoreCase("Select Highest Degree")) {
                    highestDegreeId = "";
                }
                Log.d("highestDegree", highestDegreeId);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    AppSignatureHashHelper appSignature;
}
