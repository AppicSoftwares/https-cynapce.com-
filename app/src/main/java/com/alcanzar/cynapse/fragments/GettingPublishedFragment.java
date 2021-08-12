package com.alcanzar.cynapse.fragments;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.adapter.PlaceAutocompleteAdapter;
import com.alcanzar.cynapse.api.GetAllCountryApi;
import com.alcanzar.cynapse.api.GetCityApi;
import com.alcanzar.cynapse.api.GetDepartmentApi;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.api.GetSubSpecializationApi;
import com.alcanzar.cynapse.api.GetTitleApi;
import com.alcanzar.cynapse.api.PublishProfileApi;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.CustomPreference;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static io.fabric.sdk.android.services.concurrency.AsyncTask.init;

/**
 * A simple {@link Fragment} subclass.
 */
public class GettingPublishedFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener ,GoogleApiClient.OnConnectionFailedListener{
    //TODO: getting the views
    EditText name, email, phone, desc, edit_others,
            edit_title_others, edit_dept_others, edit_spl_others, edit_Sub_spl_others;
    Spinner medicalProfile, jobSpecialization, jobTitle, subSpecialization, country, state, city, dept_dd;
    private String medicalId = "", specializationId = "", countryId = "", stateCountryId = "", stateId = "", cityId = "", subspecializationId = "", jobStr = "", job_id = "", subSpecializationStr = "", dept_id = "";
    ArrayList<String> medicalProfileSpinner = new ArrayList<>();
    ArrayList<String> jobSpecializationSpinner = new ArrayList<>();
    ArrayList<String> countrySpinner = new ArrayList<>();
    ArrayList<String> stateSpinner = new ArrayList<>();
    ArrayList<String> citySpinner = new ArrayList<>();
    ArrayList<MedicalProfileModel> medicalList = new ArrayList<>();
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    ArrayList<CountryModel> countryList = new ArrayList<>();
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();
    ArrayList<String> titleSpinner = new ArrayList<>();
    ArrayList<String> subSpecializationSpinner = new ArrayList<>();
    ArrayList<String> dept_Spinner = new ArrayList<>();
    ArrayList<MedicalProfileModel> subspecializationList = new ArrayList<>();
    ArrayList<MedicalProfileModel> dept_SpinnerList = new ArrayList<>();
    ArrayList<JobSpecializationModel> titleList = new ArrayList<>();
    Button btnPublish;
    ImageView btnBack;
    RelativeLayout dept_rel_lay, Spl_rel_lay, subS_rel_lay, other_rel_lay, other_title_rel_lay, other_spl_rel_lay,
            other_sub_spl_rel_lay, other_dept_rel_lay;
    ;
    View dept_view, spl_view, subS_view;
    ArrayAdapter adapter_medical_profile, adapter_specialization, adapter_title, adapter_dept, adapter_subSpl;
    AutoCompleteTextView medicalProfileauto, Spealization_auto, country_auto, state_auto, city_auto, Title_auto, SubS_auto, dept_auto;
    ImageButton crossC, crossS, crossHd, crossCity, crossState, crossCount, crossTitle, cross_dept;
    private String medStr = "", splStr = "", hdStr = "", title_Id = "", titleStr = "", dept_Id = "", dept_Str = "", subSplStr = "", subSpecializationId = "";
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
    String country_id = "", country_str = "", state_id = "", state_Str = "", city_id = "", city_str = "", getProfileName = "", getProfileId = "",AddressStr="";
    ArrayList<String> countryName = new ArrayList<>();
    ArrayList<String> tempcountry = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> tempstate = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();
    ArrayList<String> tempcity = new ArrayList<>();
    ArrayAdapter country_adapter, state_adapter, city_adapter;
    RadioGroup radioGrp;
    RadioButton radioMyAdd, radioCurrentAdd, radioEnterAdd;
    TextView my_address_txt, current_address_txt;
    RelativeLayout rel_lay_loc,rl_addresss;

    AutoCompleteTextView address;
    String streetAddress="";

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
   // private TextInputLayout tiL;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    public GettingPublishedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_getting_published, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : initializing and setting the header views
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        initializeViews(view);
        //TODO: clearing the list data
        clearListData();
        getStreetNumber();
        openGoogleLocatiion("<small>Street, House No.</small>");
    }

    private void openGoogleLocatiion(String sms)
    {
        PlacesClient placesClient;

        //String apiKey = "AIzaSyCUVWRZZji7uyu0xZdwYgC1q3xRJdReJ_Q";
        String apiKey = getString(R.string.googlePlaceAPI);
        Utils.sop("apiKey" + apiKey);

        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(getActivity(), apiKey);
        }

        // placesClient = com.google.android.libraries.places.api.Places.createClient(AddConference.this);
        final  AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.fragmen);
        //final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmen);
        autocompleteSupportFragment.setHint(Html.fromHtml(sms));
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME,Place.Field.ADDRESS);
        autocompleteSupportFragment.setPlaceFields(fields);

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {
                final LatLng latLo = place.getLatLng();
                Utils.sop("latLo" + latLo + "==" + place);
                address.setText(place.getAddress());
               // tiL.setVisibility(View.VISIBLE);
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
                Utils.sop("onError" + status.getStatusMessage());
            }
        });
    }


    private void getStreetNumber()
    {
        Log.d("SellHospitalActivity", "init: initializing");

        try{
             mGoogleApiClient = new GoogleApiClient
            .Builder(getActivity())
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API)
            .enableAutoManage(getActivity(), this)
            .build();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        address.setAdapter(mPlaceAutocompleteAdapter);
        try{
            address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    geoLocate();
                }
            });
            address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                        //execute our method for searching
                        geoLocate();
                    }

                    return false;
                }
            });
        }catch (Exception e){
            e.printStackTrace(); }
//        mGps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked gps icon");
//                getDeviceLocation();
//            }
//        });

//        hideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d("SellHospitalActivity", "geoLocate: geolocating");

        String searchString = address.getText().toString();
        streetAddress = address.getText().toString();

        Log.d("dhsgfskgdsk",streetAddress);
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e("SellHospitalActivity", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d("SellHospitalActivity", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

//           moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//                    address.getAddressLine(0));
        }
    }



    private void clearListData() {
        medicalList.clear();
        specializationList.clear();
        countryList.clear();
        stateList.clear();
        cityList.clear();
        medicalProfileSpinner.clear();
        jobSpecializationSpinner.clear();
        countrySpinner.clear();
        stateSpinner.clear();
        citySpinner.clear();
        subspecializationList.clear();
        subSpecializationSpinner.clear();
        titleList.clear();
        titleSpinner.clear();
        dept_Spinner.clear();
        dept_SpinnerList.clear();
    }

    //TODO: here the initialization of the views are done
    private void initializeViews(View v) {
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        phone = v.findViewById(R.id.phone);
        // btnBack = v.findViewById(R.id.btnBack);
        medicalProfile = v.findViewById(R.id.medicalProfile);
        jobSpecialization = v.findViewById(R.id.jobSpecialization);

        jobTitle = v.findViewById(R.id.jobTitle);
        subSpecialization = v.findViewById(R.id.subSpecialization);
        dept_dd = v.findViewById(R.id.dept_dd);

        country = v.findViewById(R.id.country);
        state = v.findViewById(R.id.state);
        city = v.findViewById(R.id.city);
        desc = v.findViewById(R.id.desc);
        dept_dd = v.findViewById(R.id.dept_dd);
        dept_rel_lay = v.findViewById(R.id.dept_rel_lay);
        Spl_rel_lay = v.findViewById(R.id.Spl_rel_lay);
        subS_rel_lay = v.findViewById(R.id.subS_rel_lay);
        dept_view = v.findViewById(R.id.dept_view);
        spl_view = v.findViewById(R.id.spl_view);
        subS_view = v.findViewById(R.id.subS_view);
        rel_lay_loc=v.findViewById(R.id.rel_lay_loc);
        rl_addresss=v.findViewById(R.id.rl_addresss);
        radioGrp = v.findViewById(R.id.radioGrp);

        radioMyAdd = v.findViewById(R.id.radioMyAdd);
        radioCurrentAdd =v.findViewById(R.id.radioCurrentAdd);
        radioEnterAdd = v.findViewById(R.id.radioEnterAdd);
        my_address_txt = v.findViewById(R.id.my_address_txt);
        current_address_txt =v.findViewById(R.id.current_address_txt);
        country_auto = v.findViewById(R.id.country_auto);
        state_auto = v.findViewById(R.id.state_auto);
        city_auto = v.findViewById(R.id.city_auto);
        medicalProfileauto = v.findViewById(R.id.medicalProfileauto);
        Spealization_auto = v.findViewById(R.id.Spealization_auto);
        Title_auto = v.findViewById(R.id.Title_auto);
        crossC = v.findViewById(R.id.crossC);
        crossS = v.findViewById(R.id.crossS);
        crossHd = v.findViewById(R.id.crossHd);
        SubS_auto = v.findViewById(R.id.SubS_auto);
        dept_auto = v.findViewById(R.id.dept_auto);
        other_rel_lay = v.findViewById(R.id.other_rel_lay);
        other_title_rel_lay = v.findViewById(R.id.other_title_rel_lay);
        other_spl_rel_lay = v.findViewById(R.id.other_spl_rel_lay);
        other_sub_spl_rel_lay = v.findViewById(R.id.other_sub_spl_rel_lay);
        other_dept_rel_lay = v.findViewById(R.id.other_dept_rel_lay);
        edit_others = v.findViewById(R.id.edit_others);
        edit_title_others = v.findViewById(R.id.edit_title_others);
        edit_dept_others = v.findViewById(R.id.edit_dept_others);
        edit_spl_others = v.findViewById(R.id.edit_spl_others);
        edit_Sub_spl_others = v.findViewById(R.id.edit_Sub_spl_others);

        crossC = v.findViewById(R.id.crossC);
        cross_dept = v.findViewById(R.id.cross_dept);
        crossTitle = v.findViewById(R.id.crossTitle);
        crossS = v.findViewById(R.id.crossS);
        crossCity = v.findViewById(R.id.crossCity);

        address= v.findViewById(R.id.address);
        //tiL= v.findViewById(R.id.tiL);


        // btnBack.setOnClickListener(this);
        btnPublish = v.findViewById(R.id.btnPublish);
        //TODO: setting the text by default
//        name.setText(AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.name, ""));
//        email.setText(AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.email, ""));
//        phone.setText(AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.phoneNumber, ""));
        btnPublish.setOnClickListener(this);
        //TODO: spinners onItemSelectedListeners
        medicalProfile.setOnItemSelectedListener(this);
        jobSpecialization.setOnItemSelectedListener(this);
        jobTitle.setOnItemSelectedListener(this);
        subSpecialization.setOnItemSelectedListener(this);
        dept_dd.setOnItemSelectedListener(this);
        country.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);
        city.setOnItemSelectedListener(this);

        medicalProfileauto.setOnClickListener(this);
        Spealization_auto.setOnClickListener(this);
        SubS_auto.setOnClickListener(this);
        Title_auto.setOnClickListener(this);
        country_auto.setOnClickListener(this);
        state_auto.setOnClickListener(this);
        city_auto.setOnClickListener(this);
        dept_auto.setOnClickListener(this);
        showInterstitialAds();
        //TODO: calling the getMedicalProfileApi
        getMedicalProfileApi();
        getCountyApi();
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
                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempMedProf);
                        medicalProfileauto.setAdapter(adapter_medical_profile);
                        medicalProfileauto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
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
                        adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempMedProf);
                        medicalProfileauto.setAdapter(adapter_medical_profile);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    //   crossC.setVisibility(View.GONE);
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
                        getTitle(medicalId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("MEDICALGETID", medicalId);

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
        Title_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    //    crossTitle.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    // cityC.setVisibility(View.GONE);

                    if (Title_auto.getText().toString().equals("")) {
                        tempTitle = new ArrayList<>();
                        try {
                            tempTitle.addAll(TitleName);
                            adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                            Title_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }
                }
            }


        });

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMyAdd) {
                    my_address_txt.setVisibility(View.VISIBLE);
                    current_address_txt.setVisibility(View.GONE);
                    rel_lay_loc.setVisibility(View.GONE);
                    rl_addresss.setVisibility(View.GONE);
                   // tiL.setVisibility(View.GONE);
                    AddressStr = my_address_txt.getText().toString();
                } else if (checkedId == R.id.radioCurrentAdd) {
                    current_address_txt.setVisibility(View.VISIBLE);
                    rel_lay_loc.setVisibility(View.GONE);
                    my_address_txt.setVisibility(View.GONE);
                    rl_addresss.setVisibility(View.GONE);
                    //tiL.setVisibility(View.GONE);
                    AddressStr = current_address_txt.getText().toString();
                    try {
                        current_address_txt.setText(getCompleteAddressString(getActivity(),
                                Double.parseDouble(AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.Latitude, "")),
                                Double.parseDouble(AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.Longitude, ""))));
                    } catch (NumberFormatException nf) {
                        nf.printStackTrace();
                    }
                } else {
                    current_address_txt.setVisibility(View.GONE);
                    rel_lay_loc.setVisibility(View.GONE);
                    rl_addresss.setVisibility(View.VISIBLE);
                    my_address_txt.setVisibility(View.GONE);
                    AddressStr = "";
                }

            }
        });

        Title_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().equals("")) {
                        try {
                            tempTitle = new ArrayList<>();
                            tempTitle.addAll(TitleName);

                            adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                        //  crossTitle.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        Title_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                titleStr = Title_auto.getText().toString();
                try{
                    title_Id = titleList.get(TitleName.indexOf(titleStr)).getSpecialization_id();
                }catch (IndexOutOfBoundsException ao)
                {
                    ao.printStackTrace();
                }

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

                    //  cross_dept.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (dept_auto.getText().toString().equals("")) {
                        tempDept = new ArrayList<>();

                        try {
                            tempDept.addAll(DeptName);
                            adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempDept);
                            dept_auto.setAdapter(adapter_dept);
                            dept_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
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

                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }


                    //   cross_dept.setVisibility(View.GONE);
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
        Spealization_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (Spealization_auto.getText().toString().equals("")) {
                        tempSplzation = new ArrayList<>();
                        try {
                            tempSplzation.addAll(specializationName);
                            adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSplzation);
                            Spealization_auto.setAdapter(adapter_specialization);
                            Spealization_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
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

                        adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                    } catch (Exception ne) {
                        ne.printStackTrace();
                    }
                    // crossS.setVisibility(View.GONE);
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

                    //  crossS.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);


                    if (SubS_auto.getText().toString().equals("")) {
                        tempSubS = new ArrayList<>();
                        try {
                            tempSubS.addAll(SubSName);
                            adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }

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

                    tempSubS = new ArrayList<>();
                    try {
                        tempSubS.addAll(SubSName);
                        adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSubS);
                        SubS_auto.setAdapter(adapter_subSpl);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }


                    // crossS.setVisibility(View.GONE);
                } else {
                    //  crossS.setVisibility(View.VISIBLE);
                }

            }
        });
        SubS_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                subSplStr = SubS_auto.getText().toString();
                subSpecializationId = subspecializationList.get(SubSName.indexOf(subSplStr)).getId();
                if (subSpecializationId.equalsIgnoreCase("-4")) {
                    other_sub_spl_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_sub_spl_rel_lay.setVisibility(View.GONE);
                }

                Log.d("specializationId", specializationId);
            }
        });
        country_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    //   crossC.setVisibility(View.VISIBLE);
                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (country_auto.getText().toString().equals("")) {
                        try {
                            tempcountry = new ArrayList<>();
                            tempcountry.addAll(countryName);
                            country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempcountry);
                            country_auto.setAdapter(country_adapter);
                            country_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }


                }
            }
        });
        country_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country_str = country_auto.getText().toString();
                country_id = countryList.get(countryName.indexOf(country_str)).getCountry_code();
                try {
                    getStateApi(country_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                state_auto.setText("");
                city_auto.setText("");
                Log.d("medicalId", country_id);
            }
        });
        country_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);

                        country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempcountry);
                        country_auto.setAdapter(country_adapter);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    //  crossC.setVisibility(View.GONE);
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
                    try {
                        //  crossS.setVisibility(View.VISIBLE);
                        // stateC.setVisibility(View.GONE);
                        //  cityC.setVisibility(View.GONE);
                        if (state_auto.getText().toString().equals("")) {
                            tempstate = new ArrayList<>();
                            tempstate.addAll(stateName);
                        }
                        state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempstate);
                        state_auto.setAdapter(state_adapter);
                        state_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
//                    if (state_auto.toString().equals("")) {
//                        crossS.setVisibility(View.GONE);
//                    } else{
//                        crossS.setVisibility(View.VISIBLE);
//                    }

                }
            }
        });
        state_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state_Str = state_auto.getText().toString();
                state_id = stateList.get(stateName.indexOf(state_Str)).getState_code();
                country_id = stateList.get(stateName.indexOf(state_Str)).getCountry_code();
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
                if (s.toString().equals("")) {
                    try {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);

                        state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempstate);
                        state_auto.setAdapter(state_adapter);

                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    // crossS.setVisibility(View.GONE);
                } else {
                    // crossS.setVisibility(View.VISIBLE);
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
                        //  crossCity.setVisibility(View.VISIBLE);
                        // stateC.setVisibility(View.GONE);
                        //  cityC.setVisibility(View.GONE);
                        if (city_auto.getText().toString().equals("")) {
                            tempcity = new ArrayList<>();
                            tempcity.addAll(cityName);
                        }
                        city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempcity);
                        city_auto.setAdapter(city_adapter);
                        city_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
//                    if (city_auto.toString().equals("")) {
//                        crossCity.setVisibility(View.GONE);
//                    } else{
//                        crossCity.setVisibility(View.VISIBLE);
//                    }

                }
            }
        });

        city_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city_str = city_auto.getText().toString();
                city_id = cityList.get(cityName.indexOf(city_str)).getCity_id();

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
                        tempcity = new ArrayList<>();
                        tempcity.addAll(cityName);

                        city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempcity);
                        city_auto.setAdapter(city_adapter);

                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    //crossCity.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

                            adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempMedProf);
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

                            adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSplzation);
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

    private void getTitle(String profile_category_id) throws JSONException {
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
                            for (int j = 0; j < titleList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempTitle.add(titleList.get(j).getSpecialization_name());
                                TitleName.add(titleList.get(j).getSpecialization_name());
                            }

                            adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempTitle);
                            Title_auto.setAdapter(adapter_title);
                            Title_auto.setThreshold(1);
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

                            adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSubS);
                            SubS_auto.setAdapter(adapter_subSpl);
                            SubS_auto.setThreshold(1);
                        }
                        //MyToast.toastLong(getActivity(),res_msg);
                    } else {
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

                            adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSubS);
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

                            adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempDept);
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

    private void getCountyApi() {
        new GetAllCountryApi(getActivity()) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        countrySpinner.clear();
                        countryList.clear();
                        // MyToast.toastLong(getActivity(), res_msg);
                        JSONArray header2 = header.getJSONArray("Country");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            countryList.add(new CountryModel(item.getString("country_code"), item.getString("country_name")));
                            countrySpinner.add(item.getString("country_name"));
                        }
                        // country.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countrySpinner));

                        if (countryList.size() > 0) {

                            countryName = new ArrayList<>();
                            tempcountry = new ArrayList<>();
                            for (int j = 0; j < countryList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempcountry.add(countryList.get(j).getCountry_name());
                                countryName.add(countryList.get(j).getCountry_name());
                            }

                            country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempcountry);
                            country_auto.setAdapter(country_adapter);
                            country_auto.setThreshold(1);
                        }


                    } else {
                        countrySpinner.clear();
                        countryList.clear();
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
                    if (res_code.equals("1")) {
                        stateSpinner.clear();
                        stateList.clear();
                        cityList.clear();
                        citySpinner.clear();
                        //MyToast.toastLong(getActivity(), res_msg);
                        JSONArray header2 = header.getJSONArray("State");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            stateList.add(new StateModel(item.getString("country_code"), item.getString("state_code"), item.getString("state_name")));
                            stateSpinner.add(item.getString("state_name"));
                        }
                        if (stateList.size() > 0) {

                            stateName = new ArrayList<>();
                            tempstate = new ArrayList<>();
                            for (int j = 0; j < stateList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempstate.add(stateList.get(j).getState_name());
                                stateName.add(stateList.get(j).getState_name());
                            }

                            state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempstate);
                            state_auto.setAdapter(state_adapter);
                            state_auto.setThreshold(1);
                        }
                    } else {
                        stateSpinner.clear();
                        stateList.clear();
                        cityList.clear();
                        citySpinner.clear();
                        //MyToast.toastLong(getActivity(), res_msg);
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

    private void getCityApi(String stateCountryId, String stateId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", stateCountryId);
        params.put("state_code", stateId);
        header.put("Cynapse", params);
        new GetCityApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        citySpinner.clear();
                        cityList.clear();
                        //  MyToast.toastLong(getActivity(), res_msg);
                        JSONArray header2 = header.getJSONArray("City");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            cityList.add(new CityModel(item.getString("city_id"), item.getString("country_code"), item.getString("state_code"), item.getString("city_name")));
                            citySpinner.add(item.getString("city_name"));
                        }
                        if (cityList.size() > 0) {

                            cityName = new ArrayList<>();
                            tempcity = new ArrayList<>();
                            for (int j = 0; j < cityList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempcity.add(cityList.get(j).getCity_name());
                                cityName.add(cityList.get(j).getCity_name());
                            }

                            city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempcity);
                            city_auto.setAdapter(city_adapter);
                            city_auto.setThreshold(1);
                        }
                    } else {
                        citySpinner.clear();
                        cityList.clear();
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

    private void postPublishProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        params.put("name", name.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phone_number", phone.getText().toString());


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
        if (AddressStr.equalsIgnoreCase("")) {
            params.put("city_id", "");
            params.put("country_id", "");
            params.put("state_id", "");
            params.put("address", streetAddress);
        } else {
            params.put("address", AddressStr);
            params.put("city_id", "");
            params.put("country_id", "");
            params.put("state_id", "");
        }
        Log.d("hsdfds",streetAddress);
        params.put("message", desc.getText().toString());
        Log.d("paramsval", params + "");
        header.put("Cynapse", params);
        new PublishProfileApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    Log.d("RESPONSPUBLISHPROFILE", response.toString());
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {

                       // MyToast.toastLong(getActivity(), res_msg);
                        showDialog();
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
            //               "Cynapse": {
//                "res_code": "1",
//                        "res_msg": "Published Profile Posted Successfully.",
//                        "sync_time": 1521872153,
        };
    }

    InterstitialAd interstitialAd;

    private void showInterstitialAds() {

        Log.e("interstitialAddShow","showAdd");
        AdRequest adRequest=new AdRequest.Builder()
                .build();

        MobileAds.initialize(getActivity(),getString(R.string.googleAdsAppID));
        interstitialAd=new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getString(R.string.googleAdsInterstitialAdId));
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                String keyClose= CustomPreference.readString(getActivity(),CustomPreference.CheckAddKey,"");
                Log.e("vnkdnv","sdgvbswdrbv");
//                if(keyClose.equals("2")){

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    ActivityCompat.finishAffinity(getActivity());
                    getActivity().finish();

//                }

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();

            }
        });


//
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPublish:
                //TODO: checking valid
                if (Util.isVerifiedProfile(getActivity()))
                {
                    if (AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                        //b = false;
                        //showDialog(activity);
                        try {
                            if (Util.isVerifiyEMailPHoneNO(getActivity())) {
                                if (isValid()) {
                                    try {
                                        postPublishProfileApi();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    else
                    {
                        if (isValid()) {
                            try {
                                postPublishProfileApi();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                break;
            case R.id.medicalProfileauto:
                try {
                    if (medicalProfileauto.getText().toString().equals("")) {
                        tempMedProf = new ArrayList<>();
                        tempMedProf.addAll(medicalProfileName);
                    }

                    adapter_medical_profile = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempMedProf);
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

                    adapter_specialization = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSplzation);
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
                        adapter_title = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempTitle);
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
                        adapter_subSpl = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempSubS);
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
                    country_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempcountry);
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
                    state_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempstate);
                    state_auto.setAdapter(state_adapter);
                    state_auto.showDropDown();
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
                    city_adapter = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempcity);
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
                        adapter_dept = new Adapter_Filter(getActivity(), R.layout.fragment_getting_published, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                        dept_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                }
                break;
//            case R.id.btnBack:
//                break;
        }
    }
    private boolean isValid() {
        boolean isValid = false;
        if (radioMyAdd.isChecked()) {
            if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {

                if (TextUtils.isEmpty(name.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Name is required!");
                    return false;
                }else if (TextUtils.isEmpty(email.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Email is required!");
                    return false;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    MyToast.toastLong(getActivity(), "Please enter valid Contact Email!");
                    return false;
                }else if (TextUtils.isEmpty(phone.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Phone No. is required!");
                    return false;
                }else if (phone.getText().length() < 10) {
                    MyToast.toastShort(getActivity(), "Invalid Phone Number");
                    return false;
                } else if (AddressStr.trim().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please provide Address!");
                    return false;
                } else if (medicalId.equalsIgnoreCase("")) {
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
                } else if (dept_Id.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter Department");
                    return false;
                } else if (dept_Id.equalsIgnoreCase("-5") && edit_dept_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Department cannot be blank!");
                    return false;
                } else if (TextUtils.isEmpty(desc.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Please Enter Message!");
                    return false;
                } else {

                    isValid = true;
                }
            } else {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Name is required!");
                    return false;
                }else if (TextUtils.isEmpty(email.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Email is required!");
                    return false;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    MyToast.toastLong(getActivity(), "Please enter valid Contact Email!");
                    return false;
                }else if (TextUtils.isEmpty(phone.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Phone No. is required!");
                    return false;
                }else if (phone.getText().length() < 10) {
                    MyToast.toastShort(getActivity(), "Invalid Phone Number");
                    return false;
                } else if (AddressStr.trim().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please provide Address!");
                    return false;
                } else if (medicalId.equalsIgnoreCase("")) {
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
                } else if (specializationId.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter Specialization");
                } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Specialization cannot be blank!");
                    return false;
                } else if (subSpecializationId.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter SubSpecialization");
                } else if (subSpecializationId.equalsIgnoreCase("-4") && edit_Sub_spl_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Sub Specialization cannot be blank!");
                    return false;
                } else if (TextUtils.isEmpty(desc.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Please Enter Message!");
                    return false;
                } else {

                    isValid = true;
                }
            }
            } else if (radioCurrentAdd.isChecked()) {
            if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {

                if (TextUtils.isEmpty(name.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Name is required!");
                    return false;
                }else if (TextUtils.isEmpty(email.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Email is required!");
                    return false;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    MyToast.toastLong(getActivity(), "Please enter valid Contact Email!");
                    return false;
                }else if (TextUtils.isEmpty(phone.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Phone No. is required!");
                    return false;
                }else if (phone.getText().length() < 10) {
                    MyToast.toastShort(getActivity(), "Invalid Phone Number");
                    return false;
                } else if (AddressStr.trim().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please provide Address!");
                    return false;
                } else if (medicalId.equalsIgnoreCase("")) {
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
                } else if (dept_Id.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter Department");
                    return false;
                } else if (dept_Id.equalsIgnoreCase("-5") && edit_dept_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Department cannot be blank!");
                    return false;
                }  else if (TextUtils.isEmpty(desc.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Please Enter Message!");
                    return false;
                } else {

                    isValid = true;
                }
            } else {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Name is required!");
                    return false;
                }else if (TextUtils.isEmpty(email.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Email is required!");
                    return false;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    MyToast.toastLong(getActivity(), "Please enter valid Contact Email!");
                    return false;
                }else if (TextUtils.isEmpty(phone.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Phone No. is required!");
                    return false;
                }else if (phone.getText().length() < 10) {
                    MyToast.toastShort(getActivity(), "Invalid Phone Number");
                    return false;
                } else if (AddressStr.trim().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please provide Address!");
                    return false;
                } else if (medicalId.equalsIgnoreCase("")) {
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
                } else if (specializationId.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter Specialization");
                } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Specialization cannot be blank!");
                    return false;
                } else if (subSpecializationId.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter SubSpecialization");
                } else if (subSpecializationId.equalsIgnoreCase("-4") && edit_Sub_spl_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Sub Specialization cannot be blank!");
                    return false;
                }  else if (TextUtils.isEmpty(desc.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Please Enter Message!");
                    return false;
                } else {

                    isValid = true;
                }
            }
        } else {
            if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {

                if (TextUtils.isEmpty(name.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Name is required!");
                    return false;
                }else if (TextUtils.isEmpty(email.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Email is required!");
                    return false;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    MyToast.toastLong(getActivity(), "Please enter valid Contact Email!");
                    return false;
                }else if (TextUtils.isEmpty(phone.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Phone No. is required!");
                    return false;
                }else if (phone.getText().length() < 10) {
                    MyToast.toastShort(getActivity(), "Invalid Phone Number");
                    return false;
//                } else if (country_id == null || country_id.equalsIgnoreCase("")) {
//                    MyToast.toastLong(getActivity(), "Please Enter country!");
//                    return false;
//                } else if (state_id.equalsIgnoreCase("")) {
//                    MyToast.toastLong(getActivity(), "Please Enter state!");
//                    return false;
//                } else if (city_id.equalsIgnoreCase("")) {
//                    MyToast.toastLong(getActivity(), "Please Enter city!");
//                    return false;

                } else if(streetAddress.equalsIgnoreCase("")){
                    MyToast.toastLong(getActivity(), "Please Enter Street Name");
                    return false;
                }
                else if (medicalId.equalsIgnoreCase("")) {
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
                } else if (dept_Id.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter Department");
                    return false;
                } else if (dept_Id.equalsIgnoreCase("-5") && edit_dept_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Department cannot be blank!");
                    return false;
                }else if (TextUtils.isEmpty(desc.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Please Enter Message!");
                    return false;
                } else {

                    isValid = true;
                }
            } else {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Name is required!");
                    return false;
                }else if (TextUtils.isEmpty(email.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Email is required!");
                    return false;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    MyToast.toastLong(getActivity(), "Please enter valid Contact Email!");
                    return false;
                }else if (TextUtils.isEmpty(phone.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Phone No. is required!");
                    return false;
                }else if (phone.getText().length() < 10) {
                    MyToast.toastShort(getActivity(), "Invalid Phone Number");
                    return false;
 //               }
//                  else if (country_id == null || country_id.equalsIgnoreCase("")) {
//                    MyToast.toastLong(getActivity(), "Please Enter country!");
//                    return false;
//                } else if (state_id.equalsIgnoreCase("")) {
//                    MyToast.toastLong(getActivity(), "Please Enter state!");
//                    return false;
//                } else if (city_id.equalsIgnoreCase("")) {
//                    MyToast.toastLong(getActivity(), "Please Enter city!");
//                    return false;
                }else if(streetAddress.equalsIgnoreCase("")){
                    MyToast.toastLong(getActivity(), "Please Enter Street Name");
                    return false;
                }
                else if (medicalId.equalsIgnoreCase("")) {
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
                } else if (specializationId.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter Specialization");
                } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Specialization cannot be blank!");
                    return false;
                } else if (subSpecializationId.equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Please Enter SubSpecialization");
                } else if (subSpecializationId.equalsIgnoreCase("-4") && edit_Sub_spl_others.getText().toString().equalsIgnoreCase("")) {
                    MyToast.toastLong(getActivity(), "Others for Sub Specialization cannot be blank!");
                    return false;
                }  else if (TextUtils.isEmpty(desc.getText().toString())) {
                    MyToast.toastLong(getActivity(), "Please Enter Message!");
                    return false;
                } else {

                    isValid = true;
                }
            }
//            if (country_id == null || country_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(BooksActivity.this, "Please Enter country!");
//                return false;
//            } else if (state_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(BooksActivity.this, "Please Enter state!");
//                return false;
//            } else if (city_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(BooksActivity.this, "Please Enter city!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_name.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Name is required!");
//                return false;
//            }
////            else if (specializationId.equalsIgnoreCase("")) {
////                MyToast.toastLong(BooksActivity.this, "Please Enter Speciality!");
////                return false;
////            } else if (splStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
////                MyToast.toastLong(BooksActivity.this, "Others for Speciality cannot be blank!");
////                return false;
////            }
//            else if (TextUtils.isEmpty(edit_special.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Specialization is required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_age.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Age is required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_price.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Price is required!");
//                return false;
//            }  else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Email is required!");
//                return false;
//            }else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
//                MyToast.toastLong(BooksActivity.this, "Please enter valid Contact Email!");
//                return false;
//            }else if (TextUtils.isEmpty(edit_contact_no.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Phone No. is required!");
//                return false;
//            }  else if (TextUtils.isEmpty(ed_status.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Description is required!");
//                return false;
//            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_name.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Description cannot be similar to name!");
//                return false;
//            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_contact_no.getText().toString())) {
//                MyToast.toastLong(BooksActivity.this, "Description cannot be similar to name of contact number!");
//                return false;
//            }
        }
        return isValid;
    }
    private void issValid() {
        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
            Log.d("kdfksks", "pikfksdfk");
            if (!TextUtils.isEmpty(name.getText().toString()) && !medicalId.equalsIgnoreCase("") && !dept_Id.equalsIgnoreCase("")
                    && !title_Id.equalsIgnoreCase("") && !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(phone.getText().toString())
                    && !TextUtils.isEmpty(desc.getText().toString())) {
                //TODO: calling the PublishProfileApi
                try {
                    postPublishProfileApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MyToast.toastLong(getActivity(), "All fields must be filled");
            }
        } else {

            if (!TextUtils.isEmpty(name.getText().toString()) && !medicalId.equalsIgnoreCase("") && !subSpecializationId.equalsIgnoreCase("")
                    && !specializationId.equalsIgnoreCase("") && !title_Id.equalsIgnoreCase("") && !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(phone.getText().toString())
                    && !TextUtils.isEmpty(desc.getText().toString())) {
                Log.d("kdfksks111", "pikfksdfk22");
                //TODO: calling the PublishProfileApi
                try {
                    postPublishProfileApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MyToast.toastLong(getActivity(), "All fields must be filled");
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.medicalProfile:
                //String medicalStr = parent.getItemAtPosition(position).toString();
                String medicalStr = medicalProfile.getItemAtPosition(medicalProfile.getSelectedItemPosition()).toString();
                medicalId = medicalList.get(position).getId();
                Log.d("Medical", medicalId);
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
            case R.id.country:
                String countryStr = country.getItemAtPosition(country.getSelectedItemPosition()).toString();
                countryId = countryList.get(position).getCountry_code();
                //TODO: calling the stateApi here
                try {
                    getStateApi(countryId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.state:
                String stateStr = state.getItemAtPosition(state.getSelectedItemPosition()).toString();
                stateCountryId = stateList.get(position).getCountry_code();
                stateId = stateList.get(position).getState_code();
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
//                        for (int i = 0; i < medicalList.size(); i++) {
//                            if (medicalList.get(i).getProfile_category_name().equalsIgnoreCase(item.getString("medical_profile_category_name"))) {
//                                medicalProfile.setSelection(i);
//                            }
//                        }
                        //    AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.medical_profile_id,item.getString("medical_profile_category_id"));
                        name.setText(item.getString("name"));
                        email.setText(item.getString("email"));
                        phone.setText(item.getString("phone_number"));
                        getProfileName = item.getString("medical_profile_category_name");
                        medicalProfileauto.setText(getProfileName);
                        Title_auto.setText(item.getString("title_name"));
try {


                        medicalProfileauto.setText(item.getString("medical_profile_category_name"));
                        medicalId = item.getString("medical_profile_category_id");
                        title_Id = item.getString("title_id");
                        Title_auto.setText(item.getString("title_name"));
                        specializationId = item.getString("specialization_id");
                        Spealization_auto.setText(item.getString("specialization_name"));
                        subSpecializationId = item.getString("sub_specialization_id");
                        SubS_auto.setText(item.getString("sub_specialization_name"));
                        dept_Id = item.getString("department_id");

}catch (Exception e){

}

                        Log.d("MEDICLPROFILECATEGORYNM", getProfileName);
                        getProfileId = item.getString("medical_profile_category_id");
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
                        if (getProfileId.equalsIgnoreCase("2") || getProfileId.equalsIgnoreCase("12") || getProfileId.equalsIgnoreCase("13")) {
                            try {
                                GetDepartmentApi(getProfileId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dept_auto.setText(item.getString("department_name"));
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
                        my_address_txt.setText(item.getString("address") + " " + item.getString("city_name") + " " + item.getString("state_name") + " " + item.getString("country_name"));
                        AddressStr = my_address_txt.getText().toString();

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
            AddressStr = address;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return strAdd;
    }
    public void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
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
        msg.setText(R.string.publication_req);
        dialog.show();
        dialog.setCancelable(false);
        //TODO : dismiss the on btn click and close click
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(interstitialAd.isLoaded()|| interstitialAd.isLoading()){
                    interstitialAd.show();
//                    Intent  intent=new Intent(this, AboutActivity.class);
                    showInterstitialAds();
                }else {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    ActivityCompat.finishAffinity(getActivity());
                    getActivity().finish();

                }

                //TODO : finishing the activity
                //finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(interstitialAd.isLoaded()|| interstitialAd.isLoading()){
                    interstitialAd.show();
//                    Intent  intent=new Intent(this, AboutActivity.class);
                    showInterstitialAds();
                }else {

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    ActivityCompat.finishAffinity(getActivity());
                    getActivity().finish();


                }


                //finish();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
