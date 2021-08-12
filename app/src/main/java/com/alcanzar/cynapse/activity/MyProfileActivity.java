package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alcanzar.cynapse.GoogleOtp.AppSignatureHashHelper;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.adapter.PlaceAutocompleteAdapter;
import com.alcanzar.cynapse.api.ChangePassWordApi;
import com.alcanzar.cynapse.api.GetAllCountryApi;
import com.alcanzar.cynapse.api.GetCityApi;
import com.alcanzar.cynapse.api.GetDepartmentApi;
import com.alcanzar.cynapse.api.GetHighestDegreeApi;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.api.GetSubSpecializationApi;
import com.alcanzar.cynapse.api.GetTitleApi;
import com.alcanzar.cynapse.api.GetYearOfStudyApi;
import com.alcanzar.cynapse.api.ProfileUpdateRequestApi;
import com.alcanzar.cynapse.api.UpdateProfileApi;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.HighestDegreeModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.PostImage;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    //TODO: header views


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    AutoCompleteTextView address;
    RelativeLayout rl_country, rl_state, rl_city;


    CircleImageView profilePic;
    ImageView btnBack, titleIcon, cameraSelect;
    TextView title, dob;
    RelativeLayout dept_rel_lay, Spl_rel_lay, subS_rel_lay, other_rel_lay, other_title_rel_lay, other_spl_rel_lay, reg_state_rel_lay,
            other_sub_spl_rel_lay, other_hd_rel_lay, other_dept_rel_lay, reg_rel_lay, other_Yos_rel_lay, yos_rel_lay, hd_rel_lay;
    View dept_view, spl_view, subS_view;
    //TODO: Profile Views
    Spinner medicalProfile, jobSpecialization, highestDegree, country, state, city, title_dd, subSpecialization, dept_dd;
    private String medicalId = "", specializationId = "", highestDegreeId = "", countryId = "", stateCountryId = "", stateId = "",
            cityId = "", titleId = "", subSpecializationId = "", dept_id = "", getProfileId;
    EditText name, specializations, email, phone, occupation, edit_others, edit_Reg_no, edit_Cases, edit_CasesPub, edit_Reg_state,
            edit_title_others, edit_dept_others, edit_spl_others, edit_Sub_spl_others, edit_Hd_others, edit_yos_others;
    Button btnEdit, btnChgPassword;
    ImageButton slideOne, slideTwo;
    String social_id = "";
    private String profileImg="";
    private Bitmap bitmap;
    String profile_image = "";
    LinearLayout profileOne, profileTwo;
    //TODO: this is for the image upload
    private Bitmap currentImage;
    public final int GALLERY = 1;
    public final int CAMERA = 2;
    ArrayList<String> countrySpinner = new ArrayList<>();
    ArrayList<String> stateSpinner = new ArrayList<>();
    ArrayList<String> citySpinner = new ArrayList<>();
    private static final String IMAGE_DIRECTORY = "/cynapse";
    ArrayList<CountryModel> countryList = new ArrayList<>();
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();
    ArrayList<String> medicalProfileSpinner = new ArrayList<>();
    ArrayList<String> jobSpecializationSpinner = new ArrayList<>();
    ArrayList<String> highestDegreeSpinner = new ArrayList<>();
    ArrayList<String> titleSpinner = new ArrayList<>();
    ArrayList<String> subSpecializationSpinner = new ArrayList<>();
    ArrayList<String> dept_Spinner = new ArrayList<>();
    ArrayList<MedicalProfileModel> medicalList = new ArrayList<>();
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    ArrayList<HighestDegreeModel> highestDegreeList = new ArrayList<>();
    ArrayList<MedicalProfileModel> subspecializationList = new ArrayList<>();
    ArrayList<MedicalProfileModel> dept_SpinnerList = new ArrayList<>();
    ArrayList<JobSpecializationModel> titleList = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int PICK_CAMERA_REQUEST = 11;
    static String picturePath;
    private String medStr = "", splStr = "", hdStr = "", title_Id = "", titleStr = "", dept_Id = "", dept_Str = "", subSplStr = "";
    ArrayList<String> medicalProfileName;
    ArrayList<String> specializationName;
    ArrayList<String> HdName;

    ArrayList<String> tempMedProf;
    ArrayList<String> tempSplzation;
    ArrayList<String> tempHd;
    ArrayList<String> TitleName;
    ArrayList<String> tempTitle;
    ArrayList<String> DeptName;
    ArrayList<String> tempDept;
    ArrayList<String> SubSName;
    ArrayList<String> tempSubS;
    ArrayList<String> tempYos;
    ArrayList<String> YosName;
    ArrayAdapter adapter_medical_profile, adapter_specialization, adapter_Hd, adapter_title, adapter_dept, adapter_subSpl;
    AutoCompleteTextView medicalProfileauto, Spealization_auto, Hd_auto, country_auto, state_auto, reg_state_auto,
            city_auto, Title_auto, SubS_auto, dept_auto, Yos_auto;

    String country_id = "", country_str = "", state_id = "", reg_state_id = "", state_Str = "", city_id = "", city_str = "", Yos_ID = "",
            yosStr = "", regStateStr = "", edit_disable = "";
    ArrayList<String> countryName = new ArrayList<>();
    ArrayList<String> tempcountry = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> tempstate = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();
    ArrayList<String> tempcity = new ArrayList<>();
    ArrayList<MedicalProfileModel> YosList = new ArrayList<>();
    ArrayAdapter country_adapter, state_adapter, reg_state_adapter, city_adapter, adapter_Yos;
    private AppSignatureHashHelper appSignatureHashHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        if (getIntent() != null) {
            if (getIntent().hasExtra("edit_disable"))
                edit_disable = getIntent().getStringExtra("edit_disable");
        }

        // clearListData();
        getMedicalProfileApi();
        getCountyApi();
        try {
            GetYearOfStudyApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        try {
//            getTitle(AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.medical_profile_id,""));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            getProfileSpecialization(medicalId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            GetSubSpecializationApi();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        initializeViews();
        init();


        openGoogleLocatiion("<small>Search Address</small>");

        try {
            if (edit_disable.equalsIgnoreCase("true")) {
                editableData();
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    private void initializeViews() {

        appSignatureHashHelper = new AppSignatureHashHelper(this);

        //TODO: initializing the views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.profilemanagement);
        title = findViewById(R.id.title);
        title.setText(R.string.profile_management);
        btnEdit = findViewById(R.id.btnEdit);
        cameraSelect = findViewById(R.id.cameraSelect);
        profilePic = findViewById(R.id.profilePic);
        profileOne = findViewById(R.id.profileOne);
        btnChgPassword = findViewById(R.id.btnChgPassword);
        btnEdit.setVisibility(View.VISIBLE);
        //TODO: profile views
        name = findViewById(R.id.name);
        medicalProfile = findViewById(R.id.medicalProfile);
        jobSpecialization = findViewById(R.id.jobSpecialization);
        highestDegree = findViewById(R.id.highestDegree);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.mob_);

        occupation = findViewById(R.id.occupation);
        dob = findViewById(R.id.dob);
        country = findViewById(R.id.country);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        title_dd = findViewById(R.id.title_dd);
        subSpecialization = findViewById(R.id.subSpecialization);
        dept_dd = findViewById(R.id.dept_dd);
        dept_rel_lay = findViewById(R.id.dept_rel_lay);
        Spl_rel_lay = findViewById(R.id.Spl_rel_lay);
        subS_rel_lay = findViewById(R.id.subS_rel_lay);
        dept_view = findViewById(R.id.dept_view);
        spl_view = findViewById(R.id.spl_view);
        subS_view = findViewById(R.id.subS_view);

        hd_rel_lay = findViewById(R.id.hd_rel_lay);
        other_Yos_rel_lay = findViewById(R.id.other_Yos_rel_lay);
        yos_rel_lay = findViewById(R.id.yos_rel_lay);

        other_rel_lay = findViewById(R.id.other_rel_lay);
        other_title_rel_lay = findViewById(R.id.other_title_rel_lay);
        other_spl_rel_lay = findViewById(R.id.other_spl_rel_lay);
        other_sub_spl_rel_lay = findViewById(R.id.other_sub_spl_rel_lay);
        other_hd_rel_lay = findViewById(R.id.other_hd_rel_lay);
        other_dept_rel_lay = findViewById(R.id.other_dept_rel_lay);
        reg_rel_lay = findViewById(R.id.reg_rel_lay);
        reg_state_rel_lay = findViewById(R.id.reg_state_rel_lay);

        edit_others = findViewById(R.id.edit_others);
        edit_title_others = findViewById(R.id.edit_title_others);
        edit_dept_others = findViewById(R.id.edit_dept_others);
        edit_spl_others = findViewById(R.id.edit_spl_others);
        edit_Sub_spl_others = findViewById(R.id.edit_Sub_spl_others);
        edit_Hd_others = findViewById(R.id.edit_Hd_others);
        edit_yos_others = findViewById(R.id.edit_yos_others);

        edit_Reg_no = findViewById(R.id.edit_Reg_no);
        edit_Reg_state = findViewById(R.id.edit_Reg_state);
        edit_Cases = findViewById(R.id.edit_Cases);
        edit_CasesPub = findViewById(R.id.edit_CasesPub);
        Yos_auto = findViewById(R.id.Yos_auto);

        rl_country = findViewById(R.id.rl_country);
        rl_state = findViewById(R.id.rl_state);
        rl_city = findViewById(R.id.rl_city);

        Yos_auto.setOnClickListener(this);

        dob.setOnClickListener(this);
        country.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);
        city.setOnItemSelectedListener(this);
        medicalProfile.setOnItemSelectedListener(this);
        jobSpecialization.setOnItemSelectedListener(this);
        title_dd.setOnItemSelectedListener(this);
        dept_dd.setOnItemSelectedListener(this);
        subSpecialization.setOnItemSelectedListener(this);
        name.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.name, ""));
        address.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.address, ""));
        email.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.email, ""));
        phone.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.phoneNumber, ""));
        occupation.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.occupation, ""));
        dob.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.dob, ""));


        medicalProfileauto = findViewById(R.id.medicalProfileauto);
        Spealization_auto = findViewById(R.id.Spealization_auto);
        Title_auto = findViewById(R.id.Title_auto);
        Hd_auto = findViewById(R.id.Hd_auto);


        country_auto = findViewById(R.id.country_auto);
        state_auto = findViewById(R.id.state_auto);
        city_auto = findViewById(R.id.city_auto);
        SubS_auto = findViewById(R.id.SubS_auto);
        dept_auto = findViewById(R.id.dept_auto);
        reg_state_auto = findViewById(R.id.reg_state_auto);


        medicalProfileName = new ArrayList<>();
        specializationName = new ArrayList<>();
        HdName = new ArrayList<>();

        //TODO: spinners onItemSelectedListeners
        // medicalProfile.setOnItemSelectedListener(this);
        // specialization.setOnItemSelectedListener(this);
        //  highestDegree.setOnItemSelectedListener(this);
        medicalProfileauto.setOnClickListener(this);
        Spealization_auto.setOnClickListener(this);
        SubS_auto.setOnClickListener(this);
        Title_auto.setOnClickListener(this);
        country_auto.setOnClickListener(this);
        state_auto.setOnClickListener(this);
        city_auto.setOnClickListener(this);
        Hd_auto.setOnClickListener(this);
        dept_auto.setOnClickListener(this);
        Yos_auto.setOnClickListener(this);
        reg_state_auto.setOnClickListener(this);


        rl_country.setVisibility(View.VISIBLE);
        rl_state.setVisibility(View.VISIBLE);
        rl_city.setVisibility(View.VISIBLE);

        profileImg = AppCustomPreferenceClass.readString(MyProfileActivity.this,AppCustomPreferenceClass.profile_image,"");

        // medicalProfiletxt.setText(AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.medical_profile_id,""));
        try {
            GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            getProfileSpecialization("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        highestDegree.setOnItemSelectedListener(this);
        //TODO : click Listeners
        btnEdit.setOnClickListener(this);
        btnChgPassword.setOnClickListener(this);
        cameraSelect.setOnClickListener(this);

        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    dob.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dob, 0, R.drawable.green_tick, 0);
                } else if (s.length() == 0) {
                    dob.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dob, 0, 0, 0);
                } else {
                    dob.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dob, 0, R.drawable.exclaimtionmark, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

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


        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phonenumber, 0, R.drawable.green_tick, 0);
                } else if (s.length() == 0) {
                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phonenumber, 0, 0, 0);
                } else {
                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phonenumber, 0, R.drawable.exclaimtionmark, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 10) {
                    address.setCompoundDrawablesWithIntrinsicBounds(R.drawable.address, 0, R.drawable.green_tick, 0);
                } else if (s.length() == 0) {
                    address.setCompoundDrawablesWithIntrinsicBounds(R.drawable.address, 0, 0, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        occupation.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5) {
                    occupation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.occupation, 0, R.drawable.green_tick, 0);
                } else if (s.length() == 0) {
                    occupation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.occupation, 0, 0, 0);
                } else {
                    occupation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.occupation, 0, R.drawable.exclaimtionmark, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        medicalProfileauto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (medicalProfileauto.getText().toString().equals("")) {
                        tempMedProf = new ArrayList<>();
                        tempMedProf.addAll(medicalProfileName);
                    }
                    adapter_medical_profile = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempMedProf);
                    medicalProfileauto.setAdapter(adapter_medical_profile);
                    medicalProfileauto.showDropDown();

                } else {
                    if (medicalProfileauto.toString().equals("")) {

                    } else {

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

                    tempMedProf = new ArrayList<>();
                    tempMedProf.addAll(medicalProfileName);

                    adapter_medical_profile = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempMedProf);
                    medicalProfileauto.setAdapter(adapter_medical_profile);


                } else {

                }

            }
        });

        medicalProfileauto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                medStr = medicalProfileauto.getText().toString();
                try {
                    medicalId = medicalList.get(medicalProfileName.indexOf(medStr)).getId();
                    Log.d("otherstext", medicalId);
                    edit_others.setText("");
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                title_Id = "";
                specializationId = "";
                subSpecializationId = "";
                dept_Id = "";
                Yos_ID = "";
                highestDegreeId = "";
                Title_auto.setText("");
                Spealization_auto.setText("");
                SubS_auto.setText("");
                dept_auto.setText("");
                Hd_auto.setText("");
                Yos_auto.setText("");
                edit_Hd_others.setText("");
                edit_others.setText("");
                edit_spl_others.setText("");
                edit_Sub_spl_others.setText("");
                edit_yos_others.setText("");
                edit_dept_others.setText("");
                edit_title_others.setText("");
                other_title_rel_lay.setVisibility(View.GONE);
                other_dept_rel_lay.setVisibility(View.GONE);
                other_spl_rel_lay.setVisibility(View.GONE);
                other_sub_spl_rel_lay.setVisibility(View.GONE);
                other_hd_rel_lay.setVisibility(View.GONE);
                other_Yos_rel_lay.setVisibility(View.GONE);

                if (medicalId.equalsIgnoreCase("-1")) {
                    other_rel_lay.setVisibility(View.VISIBLE);
                    try {
                        getTitle("13", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    other_rel_lay.setVisibility(View.GONE);
                    try {
                        getTitle(medicalId, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (medicalId.equalsIgnoreCase("4")) {
                    yos_rel_lay.setVisibility(View.VISIBLE);
                    hd_rel_lay.setVisibility(View.GONE);

                } else {
                    yos_rel_lay.setVisibility(View.GONE);
                    hd_rel_lay.setVisibility(View.VISIBLE);
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
                try {
                    getHighestDegreeApi(medicalId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    getRegStateApi("IN");
                } catch (JSONException e) {
                    e.printStackTrace();
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
                        getTitle("13", 1);
                    } else {

                        Utils.sop("BOB5");
                        tempTitle = new ArrayList<>();
                        try {
                            tempTitle.addAll(TitleName);

                            adapter_title = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempTitle);
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
                if (hasFocus) {

                    Log.e("Title_auto", "Country");


                    if (checkMedicalProfileType(medicalProfileauto.getText().toString())) {
                        try {
                            getTitle("13", 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    else if (Title_auto.getText().toString().equals("")) {

                        Utils.sop("Title_auto");

                        tempTitle = new ArrayList<>();
                        try {
                            tempTitle.addAll(TitleName);
                            adapter_title = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                            Title_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }
                } else {
                    if (Title_auto.toString().equals("")) {
                        Utils.sop("Title_auto"+"null");
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

                    Utils.sop("BOB2");

                    tempTitle = new ArrayList<>();
                    try {
                        tempTitle.addAll(TitleName);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    adapter_title = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempTitle);
                    Title_auto.setAdapter(adapter_title);
                } else {
                }
            }
        });

        Title_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                titleStr = Title_auto.getText().toString();

                Utils.sop("BOB1");

                try {
                    title_Id = titleList.get(TitleName.indexOf(titleStr)).getSpecialization_id();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                edit_title_others.setText("");
                if (title_Id.equalsIgnoreCase("-2")) {
                    other_title_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_title_rel_lay.setVisibility(View.GONE);
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
                            adapter_dept = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempDept);
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

                        adapter_dept = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempDept);
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
                        adapter_specialization = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
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

                        adapter_specialization = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
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
                    Log.e("spl", specializationId);
                    if (specializationId.equalsIgnoreCase("-3")) {
                        other_spl_rel_lay.setVisibility(View.VISIBLE);
                    } else {
                        other_spl_rel_lay.setVisibility(View.GONE);
                    }
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
//                edit_spl_others.setText("");

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


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);


                    if (SubS_auto.getText().toString().equals("")) {
                        tempSubS = new ArrayList<>();
                        try {
                            tempSubS.addAll(SubSName);
                            adapter_subSpl = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }

                    }


                } else {
                    if (SubS_auto.toString().equals("")) {

                    } else {

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

                        adapter_subSpl = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSubS);
                        SubS_auto.setAdapter(adapter_subSpl);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

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
                edit_Sub_spl_others.setText("");
                if (subSpecializationId.equalsIgnoreCase("-4")) {
                    other_sub_spl_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_sub_spl_rel_lay.setVisibility(View.GONE);
                }

                Log.d("specializationId", specializationId);
            }
        });

        Hd_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (Hd_auto.getText().toString().equals("")) {
                            tempHd = new ArrayList<>();
                            tempHd.addAll(HdName);
                        }
                        adapter_Hd = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempHd);
                        Hd_auto.setAdapter(adapter_Hd);
                        Hd_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (Hd_auto.toString().equals("")) {

                    } else {

                    }

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

                        adapter_Hd = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempHd);
                        Hd_auto.setAdapter(adapter_Hd);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

                }

            }
        });
        Hd_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                hdStr = Hd_auto.getText().toString();
                edit_Hd_others.setText("");
                try {
                    highestDegreeId = highestDegreeList.get(HdName.indexOf(hdStr)).getDegree_id();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                if (highestDegreeId.equalsIgnoreCase("-6")) {
                    other_hd_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_hd_rel_lay.setVisibility(View.GONE);
                }

//                if(highestDegreeId.equalsIgnoreCase("-3"))
//                {
//                    other_hd_rel_lay.setVisibility(View.VISIBLE);
//
//                }
//                else
//                {
//                    other_hd_rel_lay.setVisibility(View.GONE);
//                }
                if (highestDegreeId.equalsIgnoreCase("Select Highest Degree")) {
                    highestDegreeId = "";
                }
                Log.d("highestDegreeId", highestDegreeId);

            }
        });

        country_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (country_auto.getText().toString().equals("")) {
                            tempcountry = new ArrayList<>();
                            tempcountry.addAll(countryName);
                        }
                        country_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempcountry);
                        country_auto.setAdapter(country_adapter);
                        country_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (country_auto.toString().equals("")) {

                    } else {

                    }

                }
            }
        });

        country_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country_str = country_auto.getText().toString();
                try {
                    country_id = countryList.get(countryName.indexOf(country_str)).getCountry_code();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                try {
                    getStateApi(country_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                state_auto.setText("");
                city_auto.setText("");
                Log.d("countryID", country_id);
            }
        });
        country_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    try {
                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);

                        country_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempcountry);
                        country_auto.setAdapter(country_adapter);
                        state_auto.setText("");
                        city_auto.setText("");
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
                    country_id = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                        state_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempstate);
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
                try {
                    state_id = stateList.get(stateName.indexOf(state_Str)).getState_code();
                    country_id = stateList.get(stateName.indexOf(state_Str)).getCountry_code();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                try {
                    getCityApi(country_id, state_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("medicalId", country_id);
            }
        });

        state_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    try {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);

                        state_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempstate);
                        state_auto.setAdapter(state_adapter);

                        city_auto.setText("");
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
                    state_id = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        city_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    try {
                        // stateC.setVisibility(View.GONE);
                        //  cityC.setVisibility(View.GONE);
                        if (city_auto.getText().toString().equals("")) {
                            tempcity = new ArrayList<>();
                            tempcity.addAll(cityName);
                        }
                        city_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempcity);
                        city_auto.setAdapter(city_adapter);
                        city_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (city_auto.toString().equals("")) {

                    } else {

                    }

                }
            }
        });

        city_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city_str = city_auto.getText().toString();
                try {
                    city_id = cityList.get(cityName.indexOf(city_str)).getCity_id();
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
                try {
                    if (count > 0) {
                        tempcity = new ArrayList<>();
                        tempcity.addAll(cityName);

                        city_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempcity);
                        city_auto.setAdapter(city_adapter);

                    } else {
                        city_id = "";
                    }
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                        adapter_Yos = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempYos);
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

                        adapter_Yos = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempYos);
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
                try {
                    Yos_ID = YosList.get(YosName.indexOf(yosStr)).getId();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                if (Yos_ID.equalsIgnoreCase("-7")) {
                    other_Yos_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_Yos_rel_lay.setVisibility(View.GONE);
                }

                Log.d("Yos_ID", Yos_ID);

            }
        });

        reg_state_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (reg_state_auto.getText().toString().equals("")) {
                            tempstate = new ArrayList<>();
                            tempstate.addAll(stateName);
                        }
                        reg_state_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempstate);
                        reg_state_auto.setAdapter(reg_state_adapter);
                        reg_state_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (reg_state_auto.toString().equals("")) {

                    } else {

                    }

                }
            }
        });

        reg_state_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    regStateStr = reg_state_auto.getText().toString();
                    reg_state_id = stateList.get(stateName.indexOf(regStateStr)).getState_code();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
            }
        });

        reg_state_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);

                        reg_state_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempstate);
                        reg_state_auto.setAdapter(reg_state_adapter);
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

//        String strHint = "bablu vishwakarma";
//        SpannableString span = new SpannableString(strHint);
//        span.setSpan(new RelativeSizeSpan(0.5f), 0, strHint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        phone.setHint(span);
    }

    private void init() {
        Log.d("SellHospitalActivity", "init: initializing");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(MyProfileActivity.this, mGoogleApiClient, LAT_LNG_BOUNDS, null);
        address.setAdapter(mPlaceAutocompleteAdapter);

        try {
            address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                        //execute our method for searching
                        geoLocate();
                    }

                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mGps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked gps icon");
//                getDeviceLocation();
//            }
//        });

//        hideSoftKeyboard();
    }

    private void openGoogleLocatiion(String sms)
    {
        PlacesClient placesClient;

        //String apiKey = "AIzaSyCUVWRZZji7uyu0xZdwYgC1q3xRJdReJ_Q";
        String apiKey = getString(R.string.googlePlaceAPI);
        Utils.sop("apiKey" + apiKey);

        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(MyProfileActivity.this, apiKey);
        }

        // placesClient = com.google.android.libraries.places.api.Places.createClient(AddConference.this);

        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.fragmen);
        autocompleteSupportFragment.setHint(Html.fromHtml(sms));
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME,Place.Field.ADDRESS);
        autocompleteSupportFragment.setPlaceFields(fields);


        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {
                final LatLng latLo = place.getLatLng();
                Utils.sop("latLo" + latLo + "==" + place);
                address.setText(place.getAddress());

                geoLocate();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autocompleteSupportFragment.setText("");
                    }
                },100);

                Utils.sop("latLo="+place.getName()+"====="+place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                Utils.sop("onError" + status.isSuccess());
            }
        });
    }

    private void geoLocate() {
        Log.d("SellHospitalActivity", "geoLocate: geolocating");

        String searchString = address.getText().toString();

        Geocoder geocoder = new Geocoder(MyProfileActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e("SellHospitalActivity", "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d("SellHospitalActivity", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

//           moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//                    address.getAddressLine(0));
        }
    }

    public void showDialog2(final Activity activity) {

        Button yes, yesBtn, noBtn;
        ImageView close;
        TextView msg;

        final Dialog dialoglog = new Dialog(activity);
        dialoglog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglog.setContentView(R.layout.alert_dialog_profile);
        Window window = dialoglog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialoglog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        yesBtn = dialoglog.findViewById(R.id.yesBtn);
        noBtn = dialoglog.findViewById(R.id.noBtn);

        yes = dialoglog.findViewById(R.id.btnGotIt);
        yes.setVisibility(View.GONE);

        close = dialoglog.findViewById(R.id.close);
        msg = dialoglog.findViewById(R.id.msg);

        dialoglog.findViewById(R.id.LL).setVisibility(View.VISIBLE);

        msg.setText("Do you want to save changes");
        dialoglog.show();

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidScreenOne())
                {
                    edit_disable = "true";
                    //btnEdit.setText("SAVE");
                    try {
                        callUpdateProfileApi();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dialoglog.dismiss();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoglog.dismiss();
                MyProfileActivity.this.finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoglog.dismiss();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                try {
                    if (btnEdit.getText().toString().equals("SAVE") && !isProfileDataChanged()) {
                        showDialog2(MyProfileActivity.this);
                    } else {
                        finish();
                    }
                }
                catch (Exception e)
                {
                    MyProfileActivity.this.finish();
                }
                break;
            case R.id.medicalProfileauto:
                try {
                    if (medicalProfileauto.getText().toString().equals("")) {
                        tempMedProf = new ArrayList<>();
                        tempMedProf.addAll(medicalProfileName);
                    }

                    adapter_medical_profile = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempMedProf);
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

                    adapter_specialization = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
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
                        adapter_title = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempTitle);
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
                        adapter_subSpl = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSubS);
                        SubS_auto.setAdapter(adapter_subSpl);
                        SubS_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                }


                break;
            case R.id.country_auto:
                try {
                    if (country_auto.getText().toString().equals("")) {
                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);
                    }
                    country_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempcountry);
                    country_auto.setAdapter(country_adapter);
                    country_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;
            case R.id.state_auto:
                try {
                    if (state_auto.getText().toString().equals("")) {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);
                    }
                    state_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempstate);
                    state_auto.setAdapter(state_adapter);
                    state_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.reg_state_auto:
                try {
                    if (reg_state_auto.getText().toString().equals("")) {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);
                    }
                    reg_state_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempstate);
                    reg_state_auto.setAdapter(reg_state_adapter);
                    reg_state_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.city_auto:
                try {
                    if (city_auto.getText().toString().equals("")) {
                        tempcity = new ArrayList<>();
                        tempcity.addAll(cityName);
                    }
                    city_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempcity);
                    city_auto.setAdapter(city_adapter);
                    city_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.dept_auto:
                if (dept_auto.getText().toString().equals("")) {
                    tempDept = new ArrayList<>();

                    try {
                        tempDept.addAll(DeptName);
                        adapter_dept = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                        dept_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                }
                break;
            case R.id.Yos_auto:
                try {
                    if (Yos_auto.getText().toString().equals("")) {
                        tempYos = new ArrayList<>();
                        tempYos.addAll(YosName);
                    }
                    adapter_Yos = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempYos);
                    Yos_auto.setAdapter(adapter_Yos);
                    Yos_auto.showDropDown();
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

                    adapter_Hd = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempHd);
                    Hd_auto.setAdapter(adapter_Hd);
                    Hd_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;

            case R.id.btnEdit:
                //TODO: making edit and save at this button
                if (btnEdit.getText().toString().equals("SAVE")) {
                    Log.d("TEST1", "TEST1");
                    if (isValidScreenOne())
                    {
                        edit_disable = "true";
                        /*name.setEnabled(false);
                        name.setInputType(InputType.TYPE_NULL);
                        name.setFocusable(false);

                        cameraSelect.setClickable(false);
                        medicalProfileauto.setEnabled(false);
                        medicalProfileauto.setFocusable(false);

                        Hd_auto.setEnabled(false);
                        Hd_auto.setFocusable(false);


                        Spealization_auto.setEnabled(false);
                        Spealization_auto.setFocusable(false);

                        country_auto.setEnabled(false);
                        country_auto.setFocusable(false);

                        city_auto.setEnabled(false);
                        city_auto.setFocusable(false);
                        dept_auto.setEnabled(false);
                        dept_auto.setFocusable(false);
                        state_auto.setEnabled(false);
                        state_auto.setFocusable(false);

                        address.setEnabled(false);
                        address.setInputType(InputType.TYPE_NULL);
                        address.setFocusable(false);

                        email.setEnabled(false);
                        email.setInputType(InputType.TYPE_NULL);
                        email.setFocusable(false);

                        phone.setEnabled(false);
                        phone.setInputType(InputType.TYPE_NULL);
                        phone.setFocusable(false);*/
                        try {
                            callUpdateProfileApi();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    btnEdit.setText("SAVE");
                   enableField();
                }
                break;
            case R.id.btnChgPassword:
                //TODO :opening the success pop up
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.changepassword_alert);
                //TODO: used to make the background transparent
                Window window = dialog.getWindow();
                window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //TODO : initializing different views for the dialog
                final EditText passWord = dialog.findViewById(R.id.passWord);
                final EditText confirmPassword = dialog.findViewById(R.id.confirmPassword);
                ImageView close = dialog.findViewById(R.id.close);
                Button btnSave = dialog.findViewById(R.id.btnSave);
                //TODO :setting different views
                dialog.show();
                //TODO : dismiss the on btn click and close click
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String s1 = passWord.getText().toString();
                        String s2 = confirmPassword.getText().toString();

                        if (TextUtils.isEmpty(passWord.getText().toString())) {

                            MyToast.toastShort(MyProfileActivity.this, "Please enter old password");
                        } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {

                            MyToast.toastShort(MyProfileActivity.this, "Please enter new password");
                        } else if (confirmPassword.getText().toString().trim().length() < 6) {
                            MyToast.toastLong(MyProfileActivity.this, "Password must contain atleast 6 characters");

                        } else if (!isValidPassword(confirmPassword.getText().toString())) {
                            MyToast.toastShort(MyProfileActivity.this, "Password should contain alphabets,numbers and special characters");

                        } else {
                            try {
                                changePassword(s1, s2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }


                        //TODO : finishing the activity
                        //finish();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //finish();
                    }
                });
                break;

            case R.id.dob:
                final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Calendar c = Calendar.getInstance();
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                int monthOfYear = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                //TODO: systemCurrent Date
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                DatePickerDialog dialogs = new DatePickerDialog(MyProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //TODO: calendar for comparison
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        // Log.d("newDate",newDate.toString());
                        Date current = newDate.getTime();
                        // Log.d("currentDate",current.toString());
                        int diff = new Date().compareTo(current);
                        //Log.d("diffDate", String.valueOf(diff));
                        if (diff < 0) {
                            MyToast.toastLong(MyProfileActivity.this, "Please Select a valid date");
                        } else {
                            dob.setText(simpleDateFormat.format(newDate.getTime()));
                        }
                    }
                }, year, monthOfYear, dayOfMonth);
                dialogs.show();
                break;


            case R.id.cameraSelect:
                chooserDialog("image");
                break;

        }
    }

    //TODO: whatsApp like dialog
    private void whatsAppDialog() {

    }

    @Override
    public void onBackPressed() {

        finish();

    }

    //    private boolean isValid() {
//        if (TextUtils.isEmpty(passWord.getText().toString())) {
//
//            MyToast.toastShort(getActivity(), "Please enter password");
//            return false;
//        }
//        else if (passWord.getText().toString().trim().length() < 6) {
//            MyToast.toastLong(getActivity(), "Password must contain atleast 6 characters");
//            return false;
//        }else if(!isValidPassword(passWord.getText().toString()))
//        {
//            MyToast.toastShort(getActivity(), "Password should contain alphabets,numbers and special characters");
//            return false;
//        }
//        return true;
//    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%&*()_+=|<>?{}\\\\[\\\\]~-])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    void enableField()
    {
        name.setEnabled(true);
        findViewById(R.id.ll).setVisibility(View.VISIBLE);
        name.setFocusableInTouchMode(true);
        name.setClickable(true);
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setVisibility(View.VISIBLE);

        medicalProfileauto.setEnabled(true);
        medicalProfileauto.setFocusableInTouchMode(true);
        medicalProfileauto.setClickable(true);

        if (AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("1")
                || AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("4")
                || AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("")) {
            Spealization_auto.setVisibility(View.VISIBLE);
            SubS_auto.setVisibility(View.VISIBLE);
            dept_auto.setVisibility(View.GONE);
        } else if (AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("2") ||
                AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("12") ||
                AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("13")) {
            Spealization_auto.setVisibility(View.GONE);
            SubS_auto.setVisibility(View.GONE);
            dept_auto.setVisibility(View.VISIBLE);
        } else {
            Spealization_auto.setVisibility(View.VISIBLE);
            SubS_auto.setVisibility(View.VISIBLE);
            dept_auto.setVisibility(View.GONE);
        }

        Hd_auto.setEnabled(true);
        Hd_auto.setFocusableInTouchMode(true);
        Hd_auto.setClickable(true);

        Yos_auto.setEnabled(true);
        Yos_auto.setFocusableInTouchMode(true);
        Yos_auto.setClickable(true);

        city_auto.setEnabled(true);
        city_auto.setFocusableInTouchMode(true);
        city_auto.setClickable(true);

        country_auto.setEnabled(true);
        country_auto.setFocusableInTouchMode(true);
        country_auto.setClickable(true);

        state_auto.setEnabled(true);
        state_auto.setFocusableInTouchMode(true);
        state_auto.setClickable(true);


        Spealization_auto.setEnabled(true);
        Spealization_auto.setFocusableInTouchMode(true);
        Spealization_auto.setClickable(true);

        SubS_auto.setEnabled(true);
        SubS_auto.setFocusableInTouchMode(true);
        SubS_auto.setClickable(true);

        Title_auto.setEnabled(true);
        Title_auto.setFocusableInTouchMode(true);
        Title_auto.setClickable(true);

        dept_auto.setEnabled(true);
        dept_auto.setFocusableInTouchMode(true);
        dept_auto.setClickable(true);

        cameraSelect.setClickable(true);
        address.setEnabled(true);
        address.setFocusableInTouchMode(true);
        address.setClickable(true);
        address.setInputType(InputType.TYPE_CLASS_TEXT);


        email.setEnabled(true);
        email.setFocusableInTouchMode(true);
        email.setClickable(true);
        email.setInputType(InputType.TYPE_CLASS_TEXT);


        phone.setEnabled(true);
        phone.setFocusableInTouchMode(true);
        phone.setClickable(true);
        phone.setInputType(InputType.TYPE_CLASS_NUMBER);

//                    occupation.setEnabled(true);
//                    occupation.setFocusableInTouchMode(true);
//                    occupation.setClickable(true);
//                    occupation.setInputType(InputType.TYPE_CLASS_TEXT);


        edit_others.setEnabled(true);
//                    edit_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_others.setFocusable(true);
//
        edit_title_others.setEnabled(true);
//                    edit_title_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_title_others.setFocusable(true);
//
        edit_dept_others.setEnabled(true);
//                    edit_dept_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_dept_others.setFocusable(true);
//
        edit_spl_others.setEnabled(true);
//                    edit_spl_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_spl_others.setFocusable(true);
//
        edit_Sub_spl_others.setEnabled(true);
//                    edit_Sub_spl_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_Sub_spl_others.setFocusable(true);
//
        edit_Hd_others.setEnabled(true);
//                    edit_Hd_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_Hd_others.setFocusable(true);
//
        edit_Reg_no.setEnabled(true);
        reg_state_auto.setEnabled(true);
//                    edit_Reg_no.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_Reg_no.setFocusable(true);
//
        edit_Cases.setEnabled(true);
//                    edit_Cases.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_Cases.setFocusable(true);
//
        edit_CasesPub.setEnabled(true);
//                    edit_CasesPub.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_CasesPub.setFocusable(true);


        dob.setClickable(true);

    }

    private boolean isValidScreenOne() {
        boolean isValid = false;
        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
            if (TextUtils.isEmpty(name.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter name");
            } else if (name.getText().toString().length() < 2) {
                MyToast.toastShort(MyProfileActivity.this, "Invalid Name");
            } else if (medicalId.equalsIgnoreCase("") || medicalProfileauto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Medical Profile cannot be blank!");
                return false;
            } else if (title_Id.equalsIgnoreCase("") || Title_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter title");
            } else if (title_Id.equalsIgnoreCase("-2") && edit_title_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Title cannot be blank!");
                return false;
            } else if (dept_Id.equalsIgnoreCase("") || dept_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Department");
            } else if (dept_Id.equalsIgnoreCase("-5") && edit_dept_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Department cannot be blank!");
                return false;
            } else if (highestDegreeId.equalsIgnoreCase("") || Hd_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Highest Degree");
            } else if (highestDegreeId.equalsIgnoreCase("-6") && edit_Hd_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Highest Degree cannot be blank!");
                return false;
            }

//            else if (TextUtils.isEmpty(address.getText().toString()))
//            {
//                MyToast.toastShort(MyProfileActivity.this, "Please enter Address");
//            }
//
            else if (TextUtils.isEmpty(email.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter valid Email");
            } else if (TextUtils.isEmpty(phone.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter Phone Number");
            } else if (phone.getText().length() > 0 && phone.getText().length() < 10) {
                MyToast.toastShort(MyProfileActivity.this, "Invalid Phone Number");
            } else if (country_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Country");
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter State");
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter City");
            } else {

                isValid = true;
            }
        } else if (medicalId.equalsIgnoreCase("1")) {
            if (TextUtils.isEmpty(name.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter name");
            } else if (name.getText().toString().length() < 2) {
                MyToast.toastShort(MyProfileActivity.this, "Invalid Name");
            } else if (medicalId.equalsIgnoreCase("") || medicalProfileauto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Medical Profile cannot be blank!");
                return false;
            } else if (title_Id.equalsIgnoreCase("") || Title_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter title");
            } else if (title_Id.equalsIgnoreCase("-2") && edit_title_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Title cannot be blank!");
                return false;
            } else if (specializationId.equalsIgnoreCase("") || Spealization_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Specialization");
            } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Specialization cannot be blank!");
                return false;
            } else if (subSpecializationId.equalsIgnoreCase("") || SubS_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter SubSpecialization");
            } else if (subSpecializationId.equalsIgnoreCase("-4") && edit_Sub_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Sub Specialization cannot be blank!");
                return false;
            } else if (highestDegreeId.equalsIgnoreCase("") || Hd_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Highest Degree");
            } else if (highestDegreeId.equalsIgnoreCase("-6") && edit_Hd_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Highest Degree cannot be blank!");
                return false;
            }

//            else if (TextUtils.isEmpty(address.getText().toString()))
//            {
//                MyToast.toastShort(MyProfileActivity.this, "Please enter Address");
//            }

            else if (TextUtils.isEmpty(email.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter valid Email");
            } else if (TextUtils.isEmpty(phone.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter Phone Number");
            } else if (phone.getText().length() > 0 && phone.getText().length() < 10) {
                MyToast.toastShort(MyProfileActivity.this, "Invalid Phone Number");
            } else if (country_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Country");
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter State");
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter City");
            } else {
                isValid = true;
            }
        } else if (medicalId.equalsIgnoreCase("4")) {
            if (TextUtils.isEmpty(name.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter name");
            } else if (name.getText().toString().length() < 2) {
                MyToast.toastShort(MyProfileActivity.this, "Invalid Name");
            } else if (medicalId.equalsIgnoreCase("") || medicalProfileauto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Medical Profile cannot be blank!");
                return false;
            } else if (title_Id.equalsIgnoreCase("") || Title_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter title");
            } else if (title_Id.equalsIgnoreCase("-2") && edit_title_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Title cannot be blank!");
                return false;
            } else if (specializationId.equalsIgnoreCase("") || Spealization_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Specialization");
            } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Specialization cannot be blank!");
                return false;
            } else if (subSpecializationId.equalsIgnoreCase("") || SubS_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter SubSpecialization");
            } else if (subSpecializationId.equalsIgnoreCase("-4") && edit_Sub_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Sub Specialization cannot be blank!");
                return false;
            } else if (Yos_ID.equalsIgnoreCase("") || Yos_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Year of Study");
            } else if (Yos_ID.equalsIgnoreCase("-7") && edit_yos_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Years of Study cannot be blank!");
                return false;
            }
//            else if (TextUtils.isEmpty(address.getText().toString()))
//            {
//                MyToast.toastShort(MyProfileActivity.this, "Please enter Address");
//            }
//
            else if (TextUtils.isEmpty(email.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter valid Email");
            } else if (TextUtils.isEmpty(phone.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter Phone Number");
            } else if (phone.getText().length() > 0 && phone.getText().length() < 10) {
                MyToast.toastShort(MyProfileActivity.this, "Invalid Phone Number");
            } else if (country_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Country");
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter State");
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter City");
            } else {
                isValid = true;
            }
        } else {
            if (TextUtils.isEmpty(name.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter name");
            } else if (name.getText().toString().length() < 2) {
                MyToast.toastShort(MyProfileActivity.this, "Invalid Name");
            } else if (medicalId.equalsIgnoreCase("") || medicalProfileauto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Medical Profile cannot be blank!");
                return false;
            } else if (title_Id.equalsIgnoreCase("") || Title_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter title");
            } else if (title_Id.equalsIgnoreCase("-2") && edit_title_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Title cannot be blank!");
                return false;
            } else if (specializationId.equalsIgnoreCase("") || Spealization_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Specialization");
            } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Specialization cannot be blank!");
                return false;
            } else if (subSpecializationId.equalsIgnoreCase("") || SubS_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter SubSpecialization");
            } else if (subSpecializationId.equalsIgnoreCase("-4") && edit_Sub_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Sub Specialization cannot be blank!");
                return false;
            } else if (highestDegreeId.equalsIgnoreCase("") || Hd_auto.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Highest Degree");
            } else if (highestDegreeId.equalsIgnoreCase("-6") && edit_Hd_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Highest Degree cannot be blank!");
                return false;
            }

//            else if (TextUtils.isEmpty(address.getText().toString()))
//            {
//                MyToast.toastShort(MyProfileActivity.this, "Please enter Address");
//            }

            else if (TextUtils.isEmpty(email.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter valid Email");
            } else if (TextUtils.isEmpty(phone.getText().toString())) {
                MyToast.toastShort(MyProfileActivity.this, "Please enter Phone Number");
            } else if (phone.getText().length() > 0 && phone.getText().length() < 10) {
                MyToast.toastShort(MyProfileActivity.this, "Invalid Phone Number");
            } else if (country_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter Country");
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter State");
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastShort(MyProfileActivity.this, "Please Enter City");
            } else {
                isValid = true;
            }
// else if (TextUtils.isEmpty(phone.getText().toString())) {
//                MyToast.toastShort(MyProfileActivity.this, "Please enter Phone Number");
//            } else if (phone.getText().length() < 10) {
//                MyToast.toastShort(MyProfileActivity.this, "Invalid Phone Number");
//            } else if (TextUtils.isEmpty(dob.getText().toString())) {
//                MyToast.toastShort(MyProfileActivity.this, "Please enter DOB");

        }

        return isValid;
    }

    void chooserDialog(final String type) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MyProfileActivity.this);
        dialog.setTitle(Html.fromHtml("<font color='#2FA49E'>Choose Photo From.</font>"));
        dialog.setMessage("");

        Log.e("dkd", "cropper_dialog");
        dialog.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.e("dkd", "cropper_dialog neg");
                dialog.dismiss();


//            if (ActivityCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(MyProfileActivity.this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
//                        PICK_CAMERA_REQUEST);
//            }

                if (ActivityCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (type.equalsIgnoreCase("image")) {
                        ActivityCompat.requestPermissions(MyProfileActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                PICK_CAMERA_REQUEST);
                    }
                    return;
                }

                if (type.equalsIgnoreCase("image")) {

//                if (ActivityCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                PICK_CAMERA_REQUEST);
//                    }
//                }
//
//
//                if (type.equalsIgnoreCase("image")) {
                    Util.openCamera(MyProfileActivity.this, PICK_CAMERA_REQUEST, "image");
                    // }

                }

            }
        });

        dialog.setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.e("dkd", "cropper_dialog pos");
                if (ActivityCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyProfileActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PICK_IMAGE_REQUEST);
                }
                if (type.equalsIgnoreCase("image")) {
                    Util.openGallery(MyProfileActivity.this, PICK_IMAGE_REQUEST, "image");
                }
            }
        });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("dkd", "onActivity pos");
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                Uri filePath = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(filePath, filePathColumn, null, null, null);

                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String yourRealPath = cursor.getString(columnIndex);
                    bitmap = BitmapFactory.decodeFile(yourRealPath);//  Util.customDecodeFile(yourRealPath, StaticItems.displayWidth, StaticItems.displayWidth);
                    Log.e("Bitmap", "wi " + bitmap.getWidth() + "  he" + bitmap.getHeight());
                    Util.imageFile = new File(yourRealPath);
                    //profile_image.setImageBitmap(bitmap);
                    showCropperDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Choose Anoother Pic", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
            if (requestCode == PICK_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Log.e("PICK_CAMERA_REQUEST", "PICK_CAMERA_REQUEST" + Util.imageFile);
                Log.e("PICK_CAMERA_REQUEST", "PICK_CAMERA_REQUEST" + Util.imageFile.getAbsolutePath());
                // bitmap = BitmapFactory.decodeFile(Util.imageFile.getAbsolutePath());//  Util.customDecodeFile(yourRealPath, StaticItems.displayWidth, StaticItems.displayWidth);
                bitmap = Util.customDecodeFile(Util.imageFile.getAbsolutePath(), 400, 400);
                //profile_image.setImageBitmap(bitmap);
                showCropperDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void showCropperDialog() {
        boolean wrapInScrollView = true;
        Log.e("resultCode", "resultCode12");

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.cropdialog, null, false);

        final CropImageView img = (CropImageView) view.findViewById(R.id.CropImageView);
        img.setAspectRatio(1, 1);
        img.setFixedAspectRatio(true);
        img.setGuidelines(1);
        img.setCropShape(CropImageView.CropShape.RECTANGLE);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        img.setImageBitmap(bitmap);

        new MaterialDialog.Builder(MyProfileActivity.this)
                .title(R.string.croppertitle)
                .customView(view, wrapInScrollView)
                .positiveText(R.string.crop)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        try {
                            bitmap = ThumbnailUtils.extractThumbnail(img.getCroppedImage(), 300, 300);
                            Util.saveImage(bitmap, MyProfileActivity.this, getString(R.string.app_name));
                            profilePic.setImageBitmap(bitmap);
                            saveImage(bitmap, MyProfileActivity.this, getString(R.string.app_name), "", false);
                            //uploadProfileImage();
                            //lin_lay_submit.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Log.e("Exp", e.toString());
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        //lin_lay_submit.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                })
                .show();
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

    public boolean saveImage(Bitmap save_bitmap, Context act, String rootDir, String body, boolean bool) {
        if (isStorageAvailable(act)) {

            String root = Environment.getExternalStorageDirectory().toString();
            File rootFile = new File(root, rootDir);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            String fname = generateUniqueName("pic") + ".jpg";
            File imageFile = new File(rootFile, fname);

            FileOutputStream f = null;
            try {
                f = new FileOutputStream(imageFile);
                save_bitmap.compress(Bitmap.CompressFormat.PNG, 90, f);
                f.flush();
                f.close();
                picturePath = imageFile.getAbsolutePath();
                uploadProfileImage(picturePath);

                //galleryAddPic(imageFile, act, body, bool);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private static String generateUniqueName(String filename) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        filename = filename + timeStamp;
        return filename;
    }

    public static boolean isStorageAvailable(Context con) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(con, "sd card is not writable", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(con, "SD is not available..!!", Toast.LENGTH_SHORT)
                    .show();
        }
        return false;
    }

    void uploadProfileImage(String picturePath) {
        final String fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
        // final String fileName = Util.imageFile.getName();
//        String uuid = CustomPreference.readString(this, CustomPreference.USER_ID, "");
        Log.e("profile_path_n", fileName);


        String url = AppConstantClass.HOST + "fileUpload/profileimage";

        Log.e("skfnaksdnvaiodkd", Util.imageFile + " " + fileName);

        @SuppressLint("StaticFieldLeak")
        PostImage post = new PostImage(Util.imageFile, url, fileName, MyProfileActivity.this, "image") {
            @Override
            public void receiveData(String response) {
//                getProfileImage(response);
                try {
                    Log.e("profileimage", " = " + response);
                    JSONObject response1 = new JSONObject(response);
                    JSONObject data = response1.getJSONObject("Cynapse");
                    MyToast.logMsg("jsonImage", data.toString());
                    String res = data.getString("res_code");
                    String res1 = data.getString("res_msg");
                    profile_image = data.getString("file_name");
                    AppCustomPreferenceClass.writeString(MyProfileActivity.this,AppCustomPreferenceClass.profile_image,profile_image);
                    //   JSONObject data1 = data.getJSONObject("pharma");
//                    String profile_image = "http://162.243.205.148/pharmacist_v2/assets/images/patient_images/" + data1.getString("profile_image");
//                    Log.d("PJKLFLKKLFKSDLF",profile_image+" ");
//                    Glide.with(getApplicationContext())
//                            .load(profile_image)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .error(R.drawable.default_category)
//                            .into(productImg)

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


    //TODO: My onActivity Result

    //TODO: used to recycle the bitmap and to free the memory to getOver of an exception (OutOfMemory) Exception
    @Override
    protected void onStop() {
        super.onStop();
        if (currentImage != null) {
            currentImage.recycle();
            currentImage = null;
            System.gc();
        }
    }

    private void clearListData() {
        medicalList.clear();
        specializationList.clear();
        highestDegreeList.clear();
        countryList.clear();
        stateList.clear();
        cityList.clear();
        medicalProfileSpinner.clear();
        jobSpecializationSpinner.clear();
        highestDegreeSpinner.clear();
        countrySpinner.clear();
        stateSpinner.clear();
        citySpinner.clear();
    }

    private void GetYearOfStudyApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("medical_profile_id", "4");
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetYearOfStudyApi(MyProfileActivity.this, header) {
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
                        YosList.add(new MedicalProfileModel("-7", "Others"));

                        if (YosList.size() > 0) {

                            YosName = new ArrayList<>();
                            tempYos = new ArrayList<>();
                            for (int j = 0; j < YosList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempYos.add(YosList.get(j).getProfile_category_name());
                                YosName.add(YosList.get(j).getProfile_category_name());
                            }

                            adapter_Yos = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempYos);
                            Yos_auto.setAdapter(adapter_Yos);
                            Yos_auto.setThreshold(1);

                        }


                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
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

    private void changePassword(String s1, String s2) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("old_password", s1);
        params.put("new_password", s2);
        params.put("uuid", AppCustomPreferenceClass.readString(MyProfileActivity.this, AppCustomPreferenceClass.UserId, ""));

        // params.put("device_id", getDeviceID(MyProfileActivity.this));
        header.put("Cynapse", params);
        new ChangePassWordApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSECHANGE", response.toString());
                    if (res_code.equals("1")) {
                        MyToast.toastLong(MyProfileActivity.this, res_msg);
//                        startActivity(new Intent(MyProfileActivity.this, MyProfileActivity.class));
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        ActivityCompat.finishAffinity(MyProfileActivity.this);
//                        finish();
                    } else {
                        MyToast.toastLong(MyProfileActivity.this, res_msg);
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

    private void callUpdateProfileApi() throws JSONException {
        //TODO : json Objects Creation
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        //TODO: putting all the parameters
        params.put("uuid", AppCustomPreferenceClass.readString(MyProfileActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("name", name.getText().toString());

        if (medStr.equalsIgnoreCase("Others")) {
            params.put("medical_profile_id", "othermedicalprofile");
            params.put("others_medical_profile", edit_others.getText().toString());
        } else {

            params.put("medical_profile_id", medicalId);
            params.put("others_medical_profile", "");
        }
        if (titleStr.equalsIgnoreCase("Others")) {
            params.put("title_id", "othertitle");
            params.put("others_title", edit_title_others.getText().toString());
        } else {

            params.put("title_id", title_Id);
            params.put("others_title", "");
        }
        if (splStr.equalsIgnoreCase("Others")) {
            params.put("specialization_id", "otherspecialization");
            params.put("others_specialization", edit_spl_others.getText().toString());
        } else {

            params.put("specialization_id", specializationId);
            params.put("others_specialization", "");
        }
        if (subSplStr.equalsIgnoreCase("Others")) {
            params.put("sub_specialization_id", "othersubspecialization");
            params.put("others_sub_specialization", edit_Sub_spl_others.getText().toString());
        } else {

            params.put("sub_specialization_id", subSpecializationId);
            params.put("others_sub_specialization", "");
        }
        if (dept_Str.equalsIgnoreCase("Others")) {
            params.put("department_id", "otherdepartment");
            params.put("others_department", edit_dept_others.getText().toString());
        } else {

            params.put("department_id", dept_Id);
            params.put("others_department", "");
        }
        if (hdStr.equalsIgnoreCase("Others")) {
            params.put("highest_degree", "otherdegree");
            params.put("others_highest_degree", edit_Hd_others.getText().toString());
        } else {

            params.put("highest_degree", highestDegreeId);
            params.put("others_highest_degree", "");
        }
        if (Yos_ID.equalsIgnoreCase("-7")) {
            params.put("year_of_study", "otheryearofstudy");
            params.put("others_year_of_study", edit_yos_others.getText().toString());
        } else {
            params.put("year_of_study", Yos_ID);
            params.put("others_year_of_study", "");
        }

        params.put("cases", edit_Cases.getText().toString());
        params.put("case_publication", edit_CasesPub.getText().toString());
        params.put("registration_no", edit_Reg_no.getText().toString());
        params.put("registration_state", reg_state_id);
        params.put("profile_image", profile_image);
        params.put("address", address.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phone_number", phone.getText().toString());
        // params.put("occupation", occupation.getText().toString());
        params.put("occupation", "");
        params.put("dob", dob.getText().toString());
        params.put("country_id", country_id);
        params.put("state_id", state_id);
        params.put("city_id", city_id);
        params.put("hash_key", appSignatureHashHelper.getAppSignatures().get(0));

        header.put("Cynapse", params);

        Log.d("PARAMSSMYPROFILE", params + "");
        new UpdateProfileApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    Log.d("RESPONSEUPDATEPROFILE", response.toString());

                    if (res_code.trim().equals("1")) {
                        MyToast.toastLong(MyProfileActivity.this, res_msg);
                        JSONObject item = header.getJSONObject("UpdateProfile");
                        //TODO: saving the SignUpDetails

                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.medical_profile_id, medicalId);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.name, name.getText().toString());
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.Country_id, country_id);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.state_id, state_id);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.city_id, city_id);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.address, address.getText().toString());
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.dob, dob.getText().toString());
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.occupation, occupation.getText().toString());
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.title_id, title_Id);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.specilization_id, specializationId);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.department_id, dept_Id);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.sub_specialization_id, subSpecializationId);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.
                                this, AppCustomPreferenceClass.highest_degree_id, highestDegreeId);
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.year_of_study, Yos_ID);


                        edit_disable = "false";
                        btnEdit.setText("EDIT");

                        try {
                            GetProfileApi();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.email_verified, item.getString("email_verified"));

                        if (!AppCustomPreferenceClass.readString(MyProfileActivity.this, AppCustomPreferenceClass.medical_profile_name, "")
                                .equalsIgnoreCase("")) {
                            if (!AppCustomPreferenceClass.readString(MyProfileActivity.this, AppCustomPreferenceClass.medical_profile_name, "")
                                    .equalsIgnoreCase(medicalProfileauto.getText().toString())) {
                                //ProfileUpdateRequestApi();
                                showDialog();
                            }
                        }

                        if (item.getString("email_verified").equalsIgnoreCase("0") && item.getString("otp").equalsIgnoreCase("")) {
                            MyToast.toastLong(MyProfileActivity.this, "Email verification link has been sent on your email id.");
                        }


                        if (!item.getString("otp").equalsIgnoreCase("")) {
                            Intent intent = new Intent(MyProfileActivity.this, OtpActivity.class);
                            intent.putExtra("phone_no", phone.getText().toString());
                            intent.putExtra("uuid", item.getString("uuid"));
                            intent.putExtra("from", "myprof");
                            startActivity(intent);
                            MyProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
//                        if (!AppCustomPreferenceClass.readString(MyProfileActivity.this, AppCustomPreferenceClass.phoneNumber, "")
//                                .equalsIgnoreCase("")) {
//                            if (!AppCustomPreferenceClass.readString(MyProfileActivity.this, AppCustomPreferenceClass.phoneNumber, "")
//                                    .equalsIgnoreCase(phone.getText().toString())) {
//                                //ProfileUpdateRequestApi();
//                                Intent intent = new Intent(MyProfileActivity.this,OtpActivity.class);
//                                intent.putExtra("phone_no",phone.getText().toString());
//                                startActivity(intent);
//                               MyProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//
//                            }
//                        }

//                        startActivity(new Intent(MyProfileActivity.this, MainActivity.class));
//                        MyProfileActivity.this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//                        ActivityCompat.finishAffinity(MyProfileActivity.this);
//                        MyProfileActivity.this.finish();
                    } else {
                        MyToast.toastLong(MyProfileActivity.this, res_msg);
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

    private void getCountyApi() {
        new GetAllCountryApi(MyProfileActivity.this) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSECOUNTRY", response.toString());
                    if (res_code.equals("1")) {
//                        countrySpinner.clear();
                        countryList.clear();
                        // MyToast.toastLong(MyProfileActivity.this, res_msg);
                        JSONArray header2 = header.getJSONArray("Country");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            countryList.add(new CountryModel(item.getString("country_code"), item.getString("country_name")));
                            //  countrySpinner.add(item.getString("country_name"));
                            Log.d("COUNTSPINNER", countrySpinner + "");
                        }
                        if (countryList.size() > 0) {

                            countryName = new ArrayList<>();
                            tempcountry = new ArrayList<>();
                            for (int j = 0; j < countryList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempcountry.add(countryList.get(j).getCountry_name());
                                countryName.add(countryList.get(j).getCountry_name());
                            }

                            country_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempcountry);
                            country_auto.setAdapter(country_adapter);
                            country_auto.setThreshold(1);
                        }

                        //  country.setAdapter(new ArrayAdapter<>(MyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, countrySpinner));
                    } else {
//                        countrySpinner.clear();
//                        countryList.clear();
                        // MyToast.toastLong(MyProfileActivity.this, res_msg);
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
        new GetStateApi(MyProfileActivity.this, header) {
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
                        // MyToast.toastLong(MyProfileActivity.this, res_msg);
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

                            state_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempstate);
                            state_auto.setAdapter(state_adapter);
                            state_auto.setThreshold(1);
                        }
                    } else {
//                        stateSpinner.clear();
//                        stateList.clear();
//                        cityList.clear();
//                        citySpinner.clear();
                        // MyToast.toastLong(MyProfileActivity.this, res_msg);
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

    private void getRegStateApi(String countryId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", countryId);
        header.put("Cynapse", params);
        new GetStateApi(MyProfileActivity.this, header) {
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
                        // MyToast.toastLong(MyProfileActivity.this, res_msg);
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

                            reg_state_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempstate);
                            reg_state_auto.setAdapter(reg_state_adapter);
                            reg_state_auto.setThreshold(1);
                        }
                    } else {
//                        stateSpinner.clear();
//                        stateList.clear();
//                        cityList.clear();
//                        citySpinner.clear();
                        // MyToast.toastLong(MyProfileActivity.this, res_msg);
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

    public void editableData() {
        btnEdit.setText("EDIT");
        name.setEnabled(true);
        name.setFocusableInTouchMode(true);
        name.setClickable(true);
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setVisibility(View.VISIBLE);

        medicalProfileauto.setEnabled(true);
        medicalProfileauto.setFocusableInTouchMode(true);
        medicalProfileauto.setClickable(true);
        findViewById(R.id.ll).setVisibility(View.VISIBLE);

        if (AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("1")
                || AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("4")
                || AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("")) {
            Spealization_auto.setVisibility(View.VISIBLE);
            SubS_auto.setVisibility(View.VISIBLE);
            dept_auto.setVisibility(View.GONE);
        } else if (AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("2") ||
                AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("12") ||
                AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("13")) {
            Spealization_auto.setVisibility(View.GONE);
            SubS_auto.setVisibility(View.GONE);
            dept_auto.setVisibility(View.VISIBLE);
        } else {
            Spealization_auto.setVisibility(View.VISIBLE);
            SubS_auto.setVisibility(View.VISIBLE);
            dept_auto.setVisibility(View.GONE);
        }

        Hd_auto.setEnabled(true);
        Hd_auto.setFocusableInTouchMode(true);
        Hd_auto.setClickable(true);

        Yos_auto.setEnabled(true);
        Yos_auto.setFocusableInTouchMode(true);
        Yos_auto.setClickable(true);

        city_auto.setEnabled(true);
        city_auto.setFocusableInTouchMode(true);
        city_auto.setClickable(true);

        country_auto.setEnabled(true);
        country_auto.setFocusableInTouchMode(true);
        country_auto.setClickable(true);

        state_auto.setEnabled(true);
        state_auto.setFocusableInTouchMode(true);
        state_auto.setClickable(true);


        Spealization_auto.setEnabled(true);
        Spealization_auto.setFocusableInTouchMode(true);
        Spealization_auto.setClickable(true);
        SubS_auto.setEnabled(true);
        SubS_auto.setFocusableInTouchMode(true);
        SubS_auto.setClickable(true);
        Title_auto.setEnabled(true);
        Title_auto.setFocusableInTouchMode(true);
        Title_auto.setClickable(true);

        dept_auto.setEnabled(true);
        dept_auto.setFocusableInTouchMode(true);
        dept_auto.setClickable(true);

        cameraSelect.setClickable(true);
        address.setEnabled(true);
        address.setFocusableInTouchMode(true);
        address.setClickable(true);
        address.setInputType(InputType.TYPE_CLASS_TEXT);


        email.setEnabled(true);
        email.setFocusableInTouchMode(true);
        email.setClickable(true);
        email.setInputType(InputType.TYPE_CLASS_TEXT);


        phone.setEnabled(true);
        phone.setFocusableInTouchMode(true);
        phone.setClickable(true);
        phone.setInputType(InputType.TYPE_CLASS_NUMBER);

//                    occupation.setEnabled(true);
//                    occupation.setFocusableInTouchMode(true);
//                    occupation.setClickable(true);
//                    occupation.setInputType(InputType.TYPE_CLASS_TEXT);


        edit_others.setEnabled(true);
//                    edit_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_others.setFocusable(true);
//
        edit_title_others.setEnabled(true);
//                    edit_title_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_title_others.setFocusable(true);
//
        edit_dept_others.setEnabled(true);
//                    edit_dept_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_dept_others.setFocusable(true);
//
        edit_spl_others.setEnabled(true);
//                    edit_spl_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_spl_others.setFocusable(true);
//
        edit_Sub_spl_others.setEnabled(true);
//                    edit_Sub_spl_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_Sub_spl_others.setFocusable(true);
//
        edit_Hd_others.setEnabled(true);
//                    edit_Hd_others.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_Hd_others.setFocusable(true);
//
        edit_Reg_no.setEnabled(true);
        reg_state_auto.setEnabled(true);
//                    edit_Reg_no.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_Reg_no.setFocusable(true);
//
        edit_Cases.setEnabled(true);
//                    edit_Cases.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_Cases.setFocusable(true);
//
        edit_CasesPub.setEnabled(true);
//                    edit_CasesPub.setInputType(InputType.TYPE_CLASS_TEXT);
//                    edit_CasesPub.setFocusable(true);


        dob.setClickable(true);
        Log.d("TEST2", "TEST2");
    }

    private void getCityApi(String stateCountryId, String stateId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", stateCountryId);
        params.put("state_code", stateId);
        header.put("Cynapse", params);
        Log.d("CITYHEADER", params.toString());
        new GetCityApi(MyProfileActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("CITYRESPONSE", response.toString());
                    if (res_code.equals("1")) {
//                        citySpinner.clear();
                        cityList.clear();
                        // MyToast.toastLong(MyProfileActivity.this, res_msg);
                        JSONArray header2 = header.getJSONArray("City");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            cityList.add(new CityModel(item.getString("city_id"), item.getString("country_code"), item.getString("state_code"), item.getString("city_name")));
                            //citySpinner.add(item.getString("city_name"));
                        }
                        if (cityList.size() > 0) {

                            cityName = new ArrayList<>();
                            tempcity = new ArrayList<>();
                            for (int j = 0; j < cityList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempcity.add(cityList.get(j).getCity_name());
                                cityName.add(cityList.get(j).getCity_name());
                            }

                            city_adapter = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempcity);
                            city_auto.setAdapter(city_adapter);
                            city_auto.setThreshold(1);
                        }
                    } else {
//                        citySpinner.clear();
//                        cityList.clear();
                        //  MyToast.toastLong(MyProfileActivity.this, res_msg);
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

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.medicalProfile:
                //String medicalStr = parent.getItemAtPosition(position).toString();
                String medicalStr = medicalProfile.getItemAtPosition(medicalProfile.getSelectedItemPosition()).toString();
                medicalId = medicalList.get(position).getId();
                Log.d("Medical", medicalId);
                //TODO: calling the getProfileSpecialization api here
                if (medicalId.equalsIgnoreCase("Select Medical Profile")) {
                    medicalId = "";
                } else {
                    try {
                        getTitle(medicalId, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                //TODO: calling the getProfileSpecialization api here
                break;
            case R.id.title_dd:
                String titleStr = title_dd.getItemAtPosition(title_dd.getSelectedItemPosition()).toString();
                titleId = titleList.get(position).getSpecialization_id();
                if (titleId.equalsIgnoreCase("Select Title")) {
                    titleId = "";
                }
                Log.d("Title", titleId);
                try {
                    getProfileSpecialization(medicalId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.subSpecialization:
                String subSpecializationStr = subSpecialization.getItemAtPosition(subSpecialization.getSelectedItemPosition()).toString();
                subSpecializationId = subspecializationList.get(position).getId();
                if (subSpecializationId.equalsIgnoreCase("Select SubSpecialization")) {
                    subSpecializationId = "";
                }
                Log.d("subSpecializationId", subSpecializationId);

                break;
            case R.id.dept_dd:

                String deptStr = dept_dd.getItemAtPosition(dept_dd.getSelectedItemPosition()).toString();
                dept_id = dept_SpinnerList.get(position).getId();
                if (dept_id.equalsIgnoreCase("Select Department")) {
                    dept_id = "";
                }
                Log.d("dept_id", dept_id);
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
            case R.id.highestDegree:
                String highestDegreeStr = highestDegree.getItemAtPosition(highestDegree.getSelectedItemPosition()).toString();
                highestDegreeId = highestDegreeList.get(position).getDegree_id();
                if (highestDegreeId.equalsIgnoreCase("Select Highest Degree")) {
                    highestDegreeId = "";
                }
                Log.d("highestDegree", highestDegreeId);
                break;
            case R.id.country:
                countryId = countryList.get(position).getCountry_code();
                Log.d("COUNTRYIDWIL", countryId);
                if (countryId.equalsIgnoreCase("Select Country")) {
                    countryId = "";
                }
                //TODO: calling the stateApi here
                try {
                    getStateApi(countryId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.state:
                stateCountryId = stateList.get(position).getCountry_code();
                stateId = stateList.get(position).getState_code();
                if (stateId.equalsIgnoreCase("Select Country")) {
                    stateId = "";
                }
                Log.d("stateId", stateId);
                Log.d("countryid", stateCountryId);
                try {
                    getCityApi(stateCountryId, stateId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.city:
                String cityStr = city.getItemAtPosition(city.getSelectedItemPosition()).toString();
                cityId = cityList.get(position).getCity_id();
                if (cityId.equalsIgnoreCase("Select City")) {
                    cityId = "";
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getMedicalProfileApi() {
        new GetMedicalProfileApi(MyProfileActivity.this) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    if (res_code.equals("1")) {
                        // medicalList.clear();
                        // medicalProfileSpinner.clear();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
                        JSONArray header2 = header.getJSONArray("ProfileCategoryMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            medicalList.add(new MedicalProfileModel(item.getString("id"), item.getString("profile_category_name")));
                            //  medicalProfileSpinner.add(item.getString("profile_category_name"));
                            //Log.e("medicalListSize",String.valueOf(medicalProfileSpinner.size()));
                        }
                        medicalList.add(new MedicalProfileModel("-1", "Others"));
//                        try{
//                            medicalProfile.setAdapter(new ArrayAdapter<>(MyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, medicalProfileSpinner));
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

                            adapter_medical_profile = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempMedProf);
                            medicalProfileauto.setAdapter(adapter_medical_profile);
                            medicalProfileauto.setThreshold(1);
                        }
                        //  medicalProfile.setAdapter(new ArrayAdapter<>(MyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, medicalProfileSpinner));
                    } else {
                        //medicalList.clear();
                        // medicalProfileSpinner.clear();
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

    private void getProfileSpecialization(String id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("profile_category_id", id);
        header.put("Cynapse", params);
        new GetSpecializationApi(MyProfileActivity.this, header) {
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

                            adapter_specialization = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
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

    private void getTitle(final String profile_category_id, final int i) throws JSONException {

        if (btnEdit.getText().toString().equals("SAVE")) {

            JSONObject header = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("sync_time", "");
            params.put("profile_category_id", profile_category_id);
            header.put("Cynapse", params);
            new GetTitleApi(MyProfileActivity.this, header) {
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
                                adapter_title = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempTitle);
                                Title_auto.setAdapter(adapter_title);
                                Title_auto.setThreshold(1);


                                if (profile_category_id.equals("13") && !medicalId.equals("-1"))
                                    Title_auto.showDropDown();

                                if (i == 1)
                                    Title_auto.showDropDown();
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
    }

    private void ProfileUpdateRequestApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("old_designation", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.medical_profile_id, ""));
        params.put("new_designation", medicalId);
        header.put("Cynapse", params);
        new ProfileUpdateRequestApi(MyProfileActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        //titleSpinner.clear();
                        final Dialog dialog = new Dialog(MyProfileActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_msg_alert);
                        //TODO: used to make the background transparent
                        Window window = dialog.getWindow();
                        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        //TODO : initializing different views for the dialog
                        TextView title = dialog.findViewById(R.id.title);
                        TextView msg = dialog.findViewById(R.id.msg);
                        ImageView close = dialog.findViewById(R.id.close);
                        Button btnGotIt = dialog.findViewById(R.id.btnGotIt);
                        //TODO :setting different views
                        title.setText(R.string.sent);
                        msg.setText(R.string.request_sent);
                        dialog.show();
                        //TODO : dismiss the on btn click and close click
                        btnGotIt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //TODO : finishing the activity

                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });
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

    public void showDialog() {
        final Dialog dialog = new Dialog(MyProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_msg_alert);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        TextView title = dialog.findViewById(R.id.title);
        TextView msg = dialog.findViewById(R.id.msg);
        ImageView close = dialog.findViewById(R.id.close);
        Button btnGotIt = dialog.findViewById(R.id.btnGotIt);
        //TODO :setting different views
        title.setText(R.string.sent);
        msg.setText(R.string.med_prof_req);
        dialog.show();
        //TODO : dismiss the on btn click and close click
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO : finishing the activity

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    private void GetSubSpecializationApi(String specializationId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("specialization_id", specializationId);
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetSubSpecializationApi(MyProfileActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
//                        subSpecializationSpinner.clear();
                        subspecializationList.clear();
                        JSONArray header2 = header.getJSONArray("SubSpecialization");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            subspecializationList.add(new MedicalProfileModel(item.getString("id"), item.getString("sub_specialization_name")));
                            // subSpecializationSpinner.add(item.getString("sub_specialization_name"));
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

                            adapter_subSpl = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.setThreshold(1);
                        }
//                        ArrayAdapter<String> adapter =new ArrayAdapter<>(MyProfileActivity.this,android.R.layout.simple_spinner_item
//                                ,subSpecializationSpinner);
//                        subSpecialization.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
                    } else {
//                        subSpecializationSpinner.clear();
//                        subspecializationList.clear();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
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

                            adapter_subSpl = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.setThreshold(1);
                        }
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
        new GetDepartmentApi(MyProfileActivity.this, header) {
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

                            adapter_dept = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempDept);
                            dept_auto.setAdapter(adapter_dept);
                            dept_auto.setThreshold(1);
                        }
//                        ArrayAdapter<String> adapter =new ArrayAdapter<>(MyProfileActivity.this,android.R.layout.simple_spinner_item
//                                ,dept_Spinner);
//                        dept_dd.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
                    } else {
                        // dept_Spinner.clear();
                        // dept_SpinnerList.clear();
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

    private void getHighestDegreeApi(String id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("medical_profile_id", id);
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetHighestDegreeApi(MyProfileActivity.this, header) {
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
//                        highestDegreeSpinner.clear();
                        JSONArray header2 = header.getJSONArray("Degree");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            highestDegreeList.add(new HighestDegreeModel(item.getString("degree_id"), item.getString("degree_name")));
                            //  highestDegreeSpinner.add(item.getString("degree_name"));
                            Log.d("highestDegreeList", String.valueOf(highestDegreeSpinner.size()));
                        }
                        // highestDegree.setAdapter(new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, highestDegreeSpinner));
                        // MyToast.toastLong(MyProfileActivity.this,res_msg);
                        highestDegreeList.add(new HighestDegreeModel("-6", "Others"));
                        if (highestDegreeList.size() > 0) {

                            HdName = new ArrayList<>();
                            tempHd = new ArrayList<>();
                            for (int j = 0; j < highestDegreeList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempHd.add(highestDegreeList.get(j).getDegree_name());
                                HdName.add(highestDegreeList.get(j).getDegree_name());
                            }

                            adapter_Hd = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempHd);
                            Hd_auto.setAdapter(adapter_Hd);
                        }
                    } else {
                        highestDegreeList.clear();
                        highestDegreeList.add(new HighestDegreeModel("-6", "Others"));
                        if (highestDegreeList.size() > 0) {

                            HdName = new ArrayList<>();
                            tempHd = new ArrayList<>();
                            for (int j = 0; j < highestDegreeList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempHd.add(highestDegreeList.get(j).getDegree_name());
                                HdName.add(highestDegreeList.get(j).getDegree_name());
                            }

                            adapter_Hd = new Adapter_Filter(MyProfileActivity.this, R.layout.activity_my_profile, R.id.lbl_name, tempHd);
                            Hd_auto.setAdapter(adapter_Hd);
                            Hd_auto.setThreshold(1);
                        }
//                        highestDegreeList.clear();
//                        highestDegreeSpinner.clear();
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

    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);

        new GetProfileApi(MyProfileActivity.this, header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.sync_time, sync_time);

                    if (res_code.equals("1")) {

                        if (!res_msg.equalsIgnoreCase("Profile Record.")) {
                            MyToast.toastLong(MyProfileActivity.this, res_msg);
                        }

                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");
                        name.setText(item.getString("name"));
                        item.getString("medical_profile_category_id");
                        profile_image = item.getString("profile_image");
                        social_id = item.getString("social_network_id");
                        if (social_id.isEmpty()) {
                            // btnChgPassword.setVisibility(View.VISIBLE);
                        } else {
                            btnChgPassword.setVisibility(View.GONE);
                        }

//                        Glide.with(getApplicationContext())
//                                .load(AppConstantClass.HOST_IMAGE + item.getString("profile_image"))
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .error(R.drawable.avatar).dontAnimate()
//                                .into(profilePic);

//                        Glide.with(getApplicationContext())
//                                .load(item.getString("profile_image"))
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .error(R.drawable.avatar).dontAnimate()
//                                .into(profilePic);

//                        if (TextUtils.isEmpty(item.getString("profile_image"))) {
//                            Picasso.with(getApplicationContext()).load(R.drawable.avatar).error(R.drawable.avatar).into(profilePic);
//                        } else {
//                            Picasso.with(getApplicationContext()).load(item.getString("profile_image")).error(R.drawable.avatar).into(profilePic);
//                        }

                        if (!profile_image.isEmpty())
                            Picasso.with(getApplicationContext()).load(profile_image).placeholder(R.drawable.avatar).into(profilePic);
                        else
                            Picasso.with(getApplicationContext()).load(R.drawable.avatar).error(R.drawable.avatar).into(profilePic);

                        for (int i = 0; i < medicalList.size(); i++) {
                            if (medicalList.get(i).getProfile_category_name().equalsIgnoreCase(item.getString("medical_profile_category_name"))) {
                                medicalProfile.setSelection(i);
                            }
                        }
                        for (int i = 0; i < titleList.size(); i++) {
                            if (titleList.get(i).getSpecialization_name().equalsIgnoreCase(item.getString("title_name"))) {
                                title_dd.setSelection(i);
                            }
                        }
                        for (int i = 0; i < countryList.size(); i++) {
                            if (countryList.get(i).getCountry_name().equalsIgnoreCase(item.getString("country_name"))) {
                                country.setSelection(i);
                            }
                        }

                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.medical_profile_id, item.getString("medical_profile_category_id"));
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.name, item.getString("name"));
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.medical_profile_name, item.getString("medical_profile_category_name"));
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.phoneNumber, item.getString("phone_number"));
                        //AppCustomPreferenceClass.writeString(MyProfileActivity.this, AppCustomPreferenceClass.profile_image, item.getString("profile_image"));


                        //akash changes
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, "phone_no1", item.getString("phone_number"));
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, "title_id1", item.getString("title_id"));
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, "medical_Id1", item.getString("medical_profile_category_id"));
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, "country_code1", item.getString("country_code"));
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, "state_id1", item.getString("state_id"));
                        AppCustomPreferenceClass.writeString(MyProfileActivity.this, "city_id1", item.getString("city_id"));


                        //medicalProfileauto.setVisibility(View.GONE);
                        medicalProfileauto.setText(item.getString("medical_profile_category_name"));
                        medicalId = item.getString("medical_profile_category_id");
                        title_Id = item.getString("title_id");
                        Title_auto.setText(item.getString("title_name"));
                        specializationId = item.getString("specialization_id");
                        Spealization_auto.setText(item.getString("specialization_name"));
                        subSpecializationId = item.getString("sub_specialization_id");
                        SubS_auto.setText(item.getString("sub_specialization_name"));
                        dept_Id = item.getString("department_id");
                        Yos_ID = item.getString("year_of_study");
                        getProfileId = item.getString("medical_profile_category_id");


                        Log.d("MEDICLPROFILEIDD", getProfileId);
                        if (medicalId.equals("")) {
                            medicalId = getProfileId;
                        }
                        Log.d("MEFIIDIDID", medicalId);
                        if (getProfileId.equalsIgnoreCase("2") || getProfileId.equalsIgnoreCase("12") || getProfileId.equalsIgnoreCase("13")
                                || getProfileId.equalsIgnoreCase("1") || getProfileId.equalsIgnoreCase("4")) {
                            if (!MyProfileActivity.this.isFinishing()) {
                                getTitle(getProfileId, 0);
                            }

                        } else {
                            if (!MyProfileActivity.this.isFinishing()) {
                                getTitle("13", 0);
                            }
                        }
                        if (!MyProfileActivity.this.isFinishing()) {
                            try {
                                getHighestDegreeApi(medicalId);
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                        }

                        if (getProfileId.equalsIgnoreCase("1")) {
                            reg_rel_lay.setVisibility(View.VISIBLE);
                        } else {
                            reg_rel_lay.setVisibility(View.GONE);
                        }
                        if (edit_disable.equalsIgnoreCase("true")) {
                            editableData();
                        } else {

                            if (btnEdit.getText().toString().equals("EDIT")) {
                                name.setEnabled(false);

                                name.setInputType(InputType.TYPE_NULL);
                                name.setFocusable(false);
                                medicalProfileauto.setEnabled(false);
                                medicalProfileauto.setFocusable(false);

                                Hd_auto.setEnabled(false);
                                Hd_auto.setFocusable(false);
                                findViewById(R.id.ll).setVisibility(View.GONE);
                                Yos_auto.setEnabled(false);
                                Yos_auto.setFocusable(false);

                                Spealization_auto.setEnabled(false);
                                Spealization_auto.setFocusable(false);
                                SubS_auto.setEnabled(false);
                                SubS_auto.setFocusable(false);

                                country_auto.setEnabled(false);
                                country_auto.setFocusable(false);
                                Title_auto.setEnabled(false);
                                Title_auto.setFocusable(false);

                                city_auto.setEnabled(false);
                                city_auto.setFocusable(false);

                                state_auto.setEnabled(false);
                                state_auto.setFocusable(false);
                                cameraSelect.setClickable(false);
                                address.setEnabled(false);
                                address.setInputType(InputType.TYPE_NULL);
                                address.setFocusable(false);

                                email.setEnabled(false);
                                email.setInputType(InputType.TYPE_NULL);
                                email.setFocusable(false);

                                phone.setEnabled(false);
                                phone.setInputType(InputType.TYPE_NULL);
                                phone.setFocusable(false);

                                occupation.setEnabled(false);
                                occupation.setInputType(InputType.TYPE_NULL);
                                occupation.setFocusable(false);

                                edit_others.setEnabled(false);
                                edit_others.setInputType(InputType.TYPE_NULL);
                                edit_others.setFocusable(false);
//
                                edit_title_others.setEnabled(false);
//                        edit_title_others.setInputType(InputType.TYPE_NULL);
//                        edit_title_others.setFocusable(false);
//
                                edit_dept_others.setEnabled(false);
//                        edit_dept_others.setInputType(InputType.TYPE_NULL);
//                        edit_dept_others.setFocusable(false);
//
                                edit_spl_others.setEnabled(false);
//                        edit_spl_others.setInputType(InputType.TYPE_NULL);
//                        edit_spl_others.setFocusable(false);
//
                                edit_Sub_spl_others.setEnabled(false);
//                        edit_Sub_spl_others.setInputType(InputType.TYPE_NULL);
//                        edit_Sub_spl_others.setFocusable(false);
//
                                edit_Hd_others.setEnabled(false);
//                        edit_Hd_others.setInputType(InputType.TYPE_NULL);
//                        edit_Hd_others.setFocusable(false);
//
                                edit_Reg_no.setEnabled(false);
                                reg_state_auto.setEnabled(false);
//                        edit_Reg_no.setInputType(InputType.TYPE_NULL);
//                        edit_Reg_no.setFocusable(false);
//
                                edit_Cases.setEnabled(false);
//                        edit_Cases.setInputType(InputType.TYPE_NULL);
//                        edit_Cases.setFocusable(false);
//
                                edit_CasesPub.setEnabled(false);
//                        edit_CasesPub.setInputType(InputType.TYPE_NULL);
//                        edit_CasesPub.setFocusable(false);


                                dob.setClickable(false);
                            }
                        }
                        if (item.getString("medical_profile_category_id").equalsIgnoreCase("2") ||
                                item.getString("medical_profile_category_id").equalsIgnoreCase("12") ||
                                item.getString("medical_profile_category_id").equalsIgnoreCase("13")) {
                            dept_auto.setText(item.getString("department_name"));
                            dept_auto.setEnabled(false);
                            dept_auto.setFocusable(false);
                            dept_rel_lay.setVisibility(View.VISIBLE);
                            dept_view.setVisibility(View.VISIBLE);
                            Spl_rel_lay.setVisibility(View.GONE);
                            spl_view.setVisibility(View.GONE);
                            subS_rel_lay.setVisibility(View.GONE);
                            subS_view.setVisibility(View.GONE);
                            yos_rel_lay.setVisibility(View.GONE);
                            hd_rel_lay.setVisibility(View.VISIBLE);
                            //dept_dd.setVisibility(View.GONE);
                        } else if (item.getString("medical_profile_category_id").equalsIgnoreCase("1")) {
                            // depttxt.setVisibility(View.GONE);
                            dept_rel_lay.setVisibility(View.GONE);
                            dept_view.setVisibility(View.GONE);
                            Spl_rel_lay.setVisibility(View.VISIBLE);
                            spl_view.setVisibility(View.VISIBLE);
                            subS_rel_lay.setVisibility(View.VISIBLE);
                            subS_view.setVisibility(View.VISIBLE);
                            yos_rel_lay.setVisibility(View.GONE);
                            hd_rel_lay.setVisibility(View.VISIBLE);
                        } else if (item.getString("medical_profile_category_id").equalsIgnoreCase("4")) {
                            dept_rel_lay.setVisibility(View.GONE);
                            dept_view.setVisibility(View.GONE);
                            Spl_rel_lay.setVisibility(View.VISIBLE);
                            spl_view.setVisibility(View.VISIBLE);
                            subS_rel_lay.setVisibility(View.VISIBLE);
                            subS_view.setVisibility(View.VISIBLE);
                            yos_rel_lay.setVisibility(View.VISIBLE);
                            hd_rel_lay.setVisibility(View.GONE);
                        } else {
                            dept_rel_lay.setVisibility(View.GONE);
                            dept_view.setVisibility(View.GONE);
                            Spl_rel_lay.setVisibility(View.VISIBLE);
                            spl_view.setVisibility(View.VISIBLE);
                            subS_rel_lay.setVisibility(View.VISIBLE);
                            subS_view.setVisibility(View.VISIBLE);
                            yos_rel_lay.setVisibility(View.GONE);
                            hd_rel_lay.setVisibility(View.VISIBLE);
                        }

                        highestDegreeId = item.getString("heighest_degree_id");
                        Hd_auto.setText(item.getString("heighest_degree_name"));

                        if (item.getString("address").equalsIgnoreCase("")) {
                            try {
                                address.setText(getCompleteAddressString(MyProfileActivity.this,
                                        Double.parseDouble(AppCustomPreferenceClass.readString(MyProfileActivity.this, AppCustomPreferenceClass.Latitude, "")),
                                        Double.parseDouble(AppCustomPreferenceClass.readString(MyProfileActivity.this, AppCustomPreferenceClass.Longitude, ""))));
                            } catch (NumberFormatException nf) {
                                nf.printStackTrace();
                            }
                        } else {
                            address.setText(item.getString("address"));
                        }
                        email.setText(item.getString("email"));
                        phone.setText(item.getString("phone_number"));
                        occupation.setText(item.getString("occupation"));
                        dob.setText(item.getString("dob"));
                        country_id = item.getString("country_code");
                        country_auto.setText(item.getString("country_name"));
                        // country_auto.setText(item.getString("year_of_study"));
                        Yos_auto.setText(item.getString("year_of_study_name"));

                        state_id = item.getString("state_id");
                        state_auto.setText(item.getString("state_name"));
                        city_id = item.getString("city_id");
                        city_auto.setText(item.getString("city_name"));
                        item.getString("add_date");
                        item.getString("modify_date");
                        item.getString("status");

                        /*Add Data in temp value to check whether data are modified on not*/
                        JSONObject jsonObject = new JSONObject();
                        arrayListObj.clear();
                        arrayListObj.add(jsonObject.put("name", name.getText().toString()));
                        arrayListObj.add(jsonObject.put("medical_profile_category_name", medicalProfileauto.getText().toString()));
                        arrayListObj.add(jsonObject.put("medical_profile_category_id", medicalId));
                        arrayListObj.add(jsonObject.put("title_id", title_Id));
                        arrayListObj.add(jsonObject.put("title_name", Title_auto.getText().toString()));
                        arrayListObj.add(jsonObject.put("specialization_id", specializationId));
                        arrayListObj.add(jsonObject.put("specialization_name", Spealization_auto.getText().toString()));
                        arrayListObj.add(jsonObject.put("sub_specialization_id", subSpecializationId));
                        arrayListObj.add(jsonObject.put("sub_specialization_name", SubS_auto.getText().toString()));
                        arrayListObj.add(jsonObject.put("department_id", dept_Id));
                        arrayListObj.add(jsonObject.put("year_of_study", Yos_ID));
                        arrayListObj.add(jsonObject.put("medical_profile_category_id", getProfileId));
                        arrayListObj.add(jsonObject.put("phone_number", phone.getText().toString()));
                        arrayListObj.add(jsonObject.put("email", email.getText().toString()));
                        arrayListObj.add(jsonObject.put("occupation", occupation.getText().toString()));
                        arrayListObj.add(jsonObject.put("dob", dob.getText().toString()));
                        arrayListObj.add(jsonObject.put("country_name", country_auto.getText().toString()));
                        arrayListObj.add(jsonObject.put("year_of_study_name", Yos_auto.getText().toString()));
                        arrayListObj.add(jsonObject.put("state_name", state_auto.getText().toString()));
                        arrayListObj.add(jsonObject.put("city_name", city_auto.getText().toString()));
                        arrayListObj.add(jsonObject.put("address", address.getText().toString()));
                        Log.i("arrayListObj", arrayListObj.toString());
                        /*end*/


                        if (item.getString("request_medical_profile_status").equalsIgnoreCase("1")) {
                            medicalProfileauto.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                        } else if (item.getString("request_medical_profile_status").equalsIgnoreCase("2")) {
                            medicalProfileauto.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.exclaimtionmark, 0);
                        } else {
                            medicalProfileauto.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                        if (!MyProfileActivity.this.isFinishing()) {
                            try {
                                getStateApi(country_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                        try {
//                            getStateApi("IN");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                            try {
                                getCityApi(country_id, state_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        //   TODO - For auto selecting text in spinner(IMP)

//                        for(int i = 0;i<stateList.size();i++)
//                        {
//                            if(stateList.get(i).getState_code().equalsIgnoreCase( item.getString("state_id")))
//                            {
//                                state.setSelection(i);
//                            }
//                        }
                    } else {

                        MyToast.toastLong(MyProfileActivity.this, res_msg);
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

    private boolean isProfileDataChanged() {
        boolean b = false;
        if (!arrayListObj.get(0).optString("name").equals(name.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("medical_profile_category_name").equals(medicalProfileauto.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("title_name").equals(Title_auto.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("specialization_name").equals(Spealization_auto.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("sub_specialization_name").equals(SubS_auto.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("department_id").equals(dept_Id)) {
            b = false;
        } else if (!arrayListObj.get(0).optString("medical_profile_category_id").equals(getProfileId)) {
            b = false;
        } else if (!arrayListObj.get(0).optString("phone_number").equals(phone.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("email").equals(email.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("occupation").equals(occupation.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("dob").equals(dob.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("country_name").equals(country_auto.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("year_of_study_name").equals(Yos_auto.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("state_name").equals(state_auto.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("city_name").equals(city_auto.getText().toString())) {
            b = false;
        } else if (!arrayListObj.get(0).optString("address").equals(address.getText().toString())) {
            b = false;
        } else {
            b = true;
        }

        return b;
    }

    private String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {

        String strAdd = "";
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//
//                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                }
//                strAdd = strReturnedAddress.toString();
//                Log.e("My Current loction", "" + strReturnedAddress.toString());
//            } else {
//                Log.e("My Current loction", "No Address returned!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("My Current loction", "Canont get Address!");
//        }
//        return strAdd;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            strAdd = address;
            //AddressStr = address;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return strAdd;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private ArrayList<JSONObject> arrayListObj = new ArrayList<>();
}


