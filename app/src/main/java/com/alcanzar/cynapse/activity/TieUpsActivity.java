package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Activity;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.adapter.PlaceAutocompleteAdapter;
import com.alcanzar.cynapse.api.AddTieUpsProductApi;
import com.alcanzar.cynapse.api.GetAllCountryApi;
import com.alcanzar.cynapse.api.GetCityApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.api.OtherCategoryApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.OtherCategoryModel;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TieUpsActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    //TODO : header and other views



    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    AutoCompleteTextView edit_street;


    TextView title;
    ImageView btnBack, titleIcon, productImg,productImg2,productImg3;
    Button btnPublish;
    ArrayList<String> productCategory = new ArrayList<>();
    ArrayList<OtherCategoryModel> OthercategoryList = new ArrayList<>();
    ArrayList<OtherCategoryModel> TypecategoryList = new ArrayList<>();
    ArrayList<OtherCategoryModel> TieUpcategoryList = new ArrayList<>();
    RelativeLayout other_rel_lay, other_two_rel_lay, other_three_rel_lay, rel_lay_loc;
    Spinner categories, type, tie_up_with;
    String profile_image = "", CatId = "", TieUpId = "", Type_Id = "", Category_name = "", TieUp_name = "", Type_name, TieUpFor_id = "", TieUpforStr = "",
            SeekId = "", SeekStr = "", TieUp_withId = "", TieUp_withStr = "", country_id = "", country_str = "", state_id = "", state_Str = "", city_id = "", city_str = "", AddressStr = "";
    private Bitmap bitmap;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int PICK_CAMERA_REQUEST = 11;
    static String picturePath;
    DatabaseHelper handler;
    EditText edit_name, ed_status, edit_others, edit_others_two, edit_others_three, edit_cont_email, edit_contact_person, edit_contact_ph;

    RadioGroup radioGrp;
    RadioButton radioMyAdd, radioCurrentAdd, radioEnterAdd;
    TextView my_address_txt, current_address_txt;
    AutoCompleteTextView country_auto, state_auto, city_auto;

    AutoCompleteTextView tieup_for_auto, seeking_auto, tie_up_with_auto;
    ArrayAdapter tie_up_for_adapter, seeking_adapter, tie_up_with_adapter, country_adapter, adapter_specialization, state_adapter, city_adapter;
    ;
    ArrayList<String> tempTieUpFor;
    ArrayList<String> tempSeeking;
    ArrayList<String> TieUpForName;
    ArrayList<String> SeekingName;
    ArrayList<String> tempTieUpWith;
    ArrayList<String> TieUp_withName;

    ArrayList<String> countryName = new ArrayList<>();
    ArrayList<String> tempcountry = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> tempstate = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();
    ArrayList<String> tempcity = new ArrayList<>();
    ArrayList<CountryModel> countryList = new ArrayList<>();
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();
    String addressLat="",addressLog="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tie_ups);
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        handler = new DatabaseHelper(this);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.deals_white);
        categories = findViewById(R.id.categories);
        tie_up_with = findViewById(R.id.tie_up_with);
        type = findViewById(R.id.type);
        productImg = findViewById(R.id.productImg);
        productImg2 = findViewById(R.id.productImg2);
        productImg3 = findViewById(R.id.productImg3);
        other_rel_lay = findViewById(R.id.other_rel_lay);
        other_two_rel_lay = findViewById(R.id.other_two_rel_lay);
        other_three_rel_lay = findViewById(R.id.other_three_rel_lay);

        edit_cont_email = findViewById(R.id.edit_cont_email);
        edit_contact_person = findViewById(R.id.edit_contact_person);
        edit_contact_ph = findViewById(R.id.edit_contact_ph);
        edit_name = findViewById(R.id.edit_name);

        tieup_for_auto = findViewById(R.id.tieup_for_auto);
        seeking_auto = findViewById(R.id.seeking_auto);
        tie_up_with_auto = findViewById(R.id.tie_up_with_auto);


        ed_status = findViewById(R.id.ed_status);
        edit_street = findViewById(R.id.edit_street);
        edit_others = findViewById(R.id.edit_others);
        edit_others_two = findViewById(R.id.edit_others_two);
        edit_others_three = findViewById(R.id.edit_others_three);

        title = findViewById(R.id.title);
        title.setText(R.string.sell_buy_tie_ups);
        btnPublish = findViewById(R.id.btnPublish);
        btnPublish.setOnClickListener(this);

        productImg.setOnClickListener(this);
        productImg2.setOnClickListener(this);
        productImg3.setOnClickListener(this);

        productImg2.setVisibility(View.GONE);
        productImg3.setVisibility(View.GONE);


        tieup_for_auto.setOnClickListener(this);

        seeking_auto.setOnClickListener(this);


        tie_up_with_auto.setOnClickListener(this);


        radioGrp = findViewById(R.id.radioGrp);
        radioMyAdd = findViewById(R.id.radioMyAdd);
        radioCurrentAdd = findViewById(R.id.radioCurrentAdd);
        radioEnterAdd = findViewById(R.id.radioEnterAdd);
        my_address_txt = findViewById(R.id.my_address_txt);
        current_address_txt = findViewById(R.id.current_address_txt);
        country_auto = findViewById(R.id.country_auto);
        state_auto = findViewById(R.id.state_auto);
        city_auto = findViewById(R.id.city_auto);
        rel_lay_loc = findViewById(R.id.rel_lay_loc);

        country_auto.setOnClickListener(this);
        city_auto.setOnClickListener(this);
        state_auto.setOnClickListener(this);

        // getAllProductCategoryApi();
        // addItemsOnSpinnerCategories();
        // addItemsOnSpinnerType();
        try {
            OtherCategoryApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getCountyApi();
        clearListData();
        try {
            GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        init();
        OthercategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "2");
        TypecategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "3");
        TieUpcategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "4");

        if (OthercategoryList.size() > 0) {
            setArrayList1();
        }
        if (TypecategoryList.size() > 0) {
            setArrayList2();
        }
        if (TieUpcategoryList.size() > 0) {
            setArrayList3();
        }

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMyAdd) {
                    my_address_txt.setVisibility(View.VISIBLE);
                    current_address_txt.setVisibility(View.GONE);
                    rel_lay_loc.setVisibility(View.GONE);
                    AddressStr = my_address_txt.getText().toString();
                } else if (checkedId == R.id.radioCurrentAdd) {
                    current_address_txt.setVisibility(View.VISIBLE);
                    rel_lay_loc.setVisibility(View.GONE);
                    my_address_txt.setVisibility(View.GONE);
                    AddressStr = current_address_txt.getText().toString();
                    try {
                        current_address_txt.setText(getCompleteAddressString(TieUpsActivity.this,
                                Double.parseDouble(AppCustomPreferenceClass.readString(TieUpsActivity.this, AppCustomPreferenceClass.Latitude, "")),
                                Double.parseDouble(AppCustomPreferenceClass.readString(TieUpsActivity.this, AppCustomPreferenceClass.Longitude, ""))));
                    } catch (NumberFormatException nf) {
                        nf.printStackTrace();
                    }
                } else {
                    current_address_txt.setVisibility(View.GONE);
                    rel_lay_loc.setVisibility(View.VISIBLE);
                    my_address_txt.setVisibility(View.GONE);
                    AddressStr = "";
                }

            }
        });
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CatId = OthercategoryList.get(i).getId();
                Category_name = OthercategoryList.get(i).getName();
                if (CatId.equalsIgnoreCase("-2")) {
                    other_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_rel_lay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                        country_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempcountry);
                        country_auto.setAdapter(country_adapter);
                        country_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
                    if (country_auto.toString().equals("")) {

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
                if (count>0) {
                    try {
                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);

                        country_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempcountry);
                        country_auto.setAdapter(country_adapter);
                        state_auto.setText("");
                        city_auto.setText("");
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                }else{
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

                    try {
                        // stateC.setVisibility(View.GONE);
                        //  cityC.setVisibility(View.GONE);
                        if (state_auto.getText().toString().equals("")) {
                            tempstate = new ArrayList<>();
                            tempstate.addAll(stateName);
                        }
                        state_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempstate);
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
                if (count>0) {
                    try {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);

                        state_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempstate);
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
                        city_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempcity);
                        city_auto.setAdapter(city_adapter);
                        city_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
                    if (city_auto.toString().equals("")) {

                    }

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
                if (count>0) {
                    try {
                        tempcity = new ArrayList<>();
                        tempcity.addAll(cityName);

                        city_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempcity);
                        city_auto.setAdapter(city_adapter);


                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    city_id = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Type_Id = TypecategoryList.get(i).getId();
                Type_name = TypecategoryList.get(i).getName();
                if (Type_Id.equalsIgnoreCase("-3")) {
                    other_two_rel_lay.setVisibility(View.VISIBLE);
                } else {
                    other_two_rel_lay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tie_up_with.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TieUpId = TieUpcategoryList.get(i).getId();
                TieUp_name = TieUpcategoryList.get(i).getName();
                if (TieUpId.equalsIgnoreCase("-4")) {
                    other_three_rel_lay.setVisibility(View.VISIBLE);
                } else {
                    other_three_rel_lay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tieup_for_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (tieup_for_auto.getText().toString().equals("")) {
                            tempTieUpFor = new ArrayList<>();
                            tempTieUpFor.addAll(TieUpForName);
                        }
                        tie_up_for_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
                        tieup_for_auto.setAdapter(tie_up_for_adapter);
                        tieup_for_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
                    if (tieup_for_auto.toString().equals("")) {

                    } else {

                    }

                }
            }
        });
        tieup_for_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TieUpforStr = tieup_for_auto.getText().toString();
                TieUpFor_id = OthercategoryList.get(TieUpForName.indexOf(TieUpforStr)).getId();
                edit_others.setText("");
                if (TieUpFor_id.equalsIgnoreCase("-2")) {
                    other_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_rel_lay.setVisibility(View.GONE);
                }
                Log.d("medicalId", TieUpFor_id);
            }
        });
        tieup_for_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempTieUpFor = new ArrayList<>();
                        tempTieUpFor.addAll(TieUpForName);

                        tie_up_for_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
                        tieup_for_auto.setAdapter(tie_up_for_adapter);
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
        seeking_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (seeking_auto.getText().toString().equals("")) {
                            tempSeeking = new ArrayList<>();
                            tempSeeking.addAll(SeekingName);
                        }
                        seeking_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempSeeking);
                        seeking_auto.setAdapter(seeking_adapter);
                        seeking_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
                    if (seeking_auto.toString().equals("")) {

                    } else {

                    }

                }
            }
        });
        seeking_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeekStr = seeking_auto.getText().toString();
                SeekId = TypecategoryList.get(SeekingName.indexOf(SeekStr)).getId();
                edit_others_two.setText("");
                if (SeekId.equalsIgnoreCase("-3")) {
                    other_two_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_two_rel_lay.setVisibility(View.GONE);
                }
                Log.d("medicalId", SeekId);
            }
        });
        seeking_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempSeeking = new ArrayList<>();
                        tempSeeking.addAll(SeekingName);
                        seeking_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempSeeking);
                        seeking_auto.setAdapter(seeking_adapter);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tie_up_with_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (tie_up_with_auto.getText().toString().equals("")) {
                            tempTieUpWith = new ArrayList<>();
                            tempTieUpWith.addAll(TieUp_withName);
                        }
                        tie_up_with_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpWith);
                        tie_up_with_auto.setAdapter(tie_up_with_adapter);
                        tie_up_with_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    if (tie_up_with_auto.toString().equals("")) {

                    } else {

                    }

                }
            }
        });
        tie_up_with_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TieUp_withStr = tie_up_with_auto.getText().toString();
                TieUp_withId = TieUpcategoryList.get(TieUp_withName.indexOf(TieUp_withStr)).getId();
                edit_others_three.setText("");
                if (TieUp_withId.equalsIgnoreCase("-4")) {
                    other_three_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_three_rel_lay.setVisibility(View.GONE);
                }
                Log.d("medicalId", TieUp_withId);
            }
        });
        tie_up_with_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {

                    try {
                        tempTieUpWith = new ArrayList<>();
                        tempTieUpWith.addAll(TieUp_withName);

                        tie_up_with_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpWith);
                        tie_up_with_auto.setAdapter(tie_up_with_adapter);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        openGoogleLocatiion("<small>Street, House No.</small>");
    }

    private void openGoogleLocatiion(String sms)
    {
        PlacesClient placesClient;

        //String apiKey = "AIzaSyCUVWRZZji7uyu0xZdwYgC1q3xRJdReJ_Q";
        String apiKey = getString(R.string.googlePlaceAPI);
        Utils.sop("apiKey" + apiKey);

        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(TieUpsActivity.this, apiKey);
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
                edit_street.setText(place.getAddress());

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

    private void init(){
        Log.d("SellHospitalActivity", "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(TieUpsActivity.this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        edit_street.setAdapter(mPlaceAutocompleteAdapter);
        try{


            edit_street.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    geoLocate();
                }
            });



            edit_street.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                        //execute our method for searching

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

        String searchString = "";

        if (rel_lay_loc.getVisibility()==View.GONE)
        {
            searchString = AddressStr;
        }
        else
        {
            searchString = edit_street.getText().toString();
        }

        Geocoder geocoder = new Geocoder(TieUpsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e("SellHospitalActivity", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            addressLat = String.valueOf(address.getLatitude());
            addressLog = String.valueOf(address.getLongitude());

            Log.d("SellHospitalActivity", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//           moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//                    address.getAddressLine(0));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;

            case R.id.productImg:
                productImgs=0;
                chooserDialog("image");
                break;

                case R.id.productImg2:
                    productImgs=1;
                chooserDialog("image");
                break;

                case R.id.productImg3:
                    productImgs=2;
                chooserDialog("image");
                break;

            case R.id.tieup_for_auto:
                try {
                    if (tieup_for_auto.getText().toString().equals("")) {
                        tempTieUpFor = new ArrayList<>();
                        tempTieUpFor.addAll(TieUpForName);
                    }

                    tie_up_for_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
                    tieup_for_auto.setAdapter(tie_up_for_adapter);
                    tieup_for_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;

            case R.id.seeking_auto:
                try {
                    if (seeking_auto.getText().toString().equals("")) {
                        tempSeeking = new ArrayList<>();
                        tempSeeking.addAll(SeekingName);
                    }
                    seeking_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempSeeking);
                    seeking_auto.setAdapter(seeking_adapter);
                    seeking_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;

            case R.id.tie_up_with_auto:
                try {
                    if (tie_up_with_auto.getText().toString().equals("")) {
                        tempTieUpWith = new ArrayList<>();
                        tempTieUpWith.addAll(TieUp_withName);
                    }
                    tie_up_with_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpWith);
                    tie_up_with_auto.setAdapter(tie_up_with_adapter);
                    tie_up_with_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;
            case R.id.country_auto:
                try {
                    if (country_auto.getText().toString().equals("")) {
                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);
                    }


                    country_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempcountry);
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
                    state_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempstate);
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
                    city_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempcity);
                    city_auto.setAdapter(city_adapter);
                    city_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }


                break;

            case R.id.btnPublish:
                //TODO: moving to the next page of publish section

                if (isValid()) {
                    try {
                        addProductApi();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                    final Dialog dialog = new Dialog(this);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.dialog_msg_alert);
//                    //TODO: used to make the background transparent
//                    Window window = dialog.getWindow();
//                    window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    //TODO : initializing different views for the dialog
//                    TextView title = dialog.findViewById(R.id.title);
//                    TextView msg = dialog.findViewById(R.id.msg);
//                    ImageView close = dialog.findViewById(R.id.close);
//                    Button btnGotIt = dialog.findViewById(R.id.btnGotIt);
//                    //TODO :setting different views
//                    title.setText(R.string.congrats);
//                    msg.setText(R.string.publishedSuccessfully);
//                    dialog.show();
//                    //TODO : dismiss the on btn click and close click
//                    btnGotIt.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                            //TODO : finishing the activity
//                            finish();
//                        }
//                    });
//                    close.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                            finish();
//                        }
//                    });
                //    }
                break;
        }
    }

    private void clearListData() {
        productCategory.clear();

    }

    void chooserDialog(final String type) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(TieUpsActivity.this);
        dialog.setTitle(Html.fromHtml("<font color='#2FA49E'>Choose Photo From.</font>"));
        dialog.setMessage("");

        Log.e("dkd", "cropper_dialog");
        dialog.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.e("dkd", "cropper_dialog neg");
                dialog.dismiss();

                if (ActivityCompat.checkSelfPermission(TieUpsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(TieUpsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TieUpsActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PICK_CAMERA_REQUEST);
                    return;
                }

                if (type.equalsIgnoreCase("image")) {

                    try {
                        Util.openCamera(TieUpsActivity.this, PICK_CAMERA_REQUEST, "image");
                    } catch (SecurityException se) {
                        Toast.makeText(getApplicationContext(), "Camera permission needed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.e("dkd", "cropper_dialog pos");
                if (ActivityCompat.checkSelfPermission(TieUpsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(TieUpsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TieUpsActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PICK_IMAGE_REQUEST);
                }
                if (type.equalsIgnoreCase("image")) {
                    Util.openGallery(TieUpsActivity.this, PICK_IMAGE_REQUEST, "image");
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

    @Override
    public void onBackPressed() {
        finish();

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

        new MaterialDialog.Builder(TieUpsActivity.this)
                .title(R.string.croppertitle)
                .customView(view, wrapInScrollView)
                .positiveText(R.string.crop)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        try {
                            bitmap = ThumbnailUtils.extractThumbnail(img.getCroppedImage(), 300, 300);
                            Util.saveImage(bitmap, TieUpsActivity.this, getString(R.string.app_name));

                            if (productImgs==0)
                            productImg.setImageBitmap(bitmap);
                            if (productImgs==1)
                                productImg2.setImageBitmap(bitmap);
                            if (productImgs==2)
                                productImg3.setImageBitmap(bitmap);

                            saveImage(bitmap, TieUpsActivity.this, getString(R.string.app_name), "", false);
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

        String url = AppConstantClass.HOST + "fileUpload/productimage";
        PostImage post = new PostImage(Util.imageFile, url, fileName, TieUpsActivity.this, "image") {
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


                    try {
                        profile_imageAl.set(productImgs, profile_image);
                    }
                    catch (Exception e)
                    {
                        profile_imageAl.add(profile_image);
                    }


                    if(productImgs==0)
                        productImg2.setVisibility(View.VISIBLE);
                    else productImg3.setVisibility(View.VISIBLE);
                    System.out.println("multipleImagesAl"+profile_imageAl.toString());


                    //   JSONObject data1 = data.getJSONObject("pharma");
//                    String profile_image = "http://162.243.205.148/pharmacist_v2/assets/images/patient_images/" + data1.getString("profile_image");
//                    Log.d("PJKLFLKKLFKSDLF",profile_image+" ");
//                    Glide.with(getApplicationContext())
//                            .load(profile_image)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .error(R.drawable.default_category)
//                            .into(productImg);


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

    private boolean isValid() {
        if (radioMyAdd.isChecked()) {
            if (country_id == null || country_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter country!");
                return false;
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter state!");
                return false;
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter city!");
                return false;
            }else if (TieUpFor_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up for!");
                return false;
            } else if (TieUpforStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up cannot be blank!");
                return false;
            } else if (SeekId.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Seeking!");
                return false;
            } else if (SeekStr.equalsIgnoreCase("Others") && edit_others_two.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Seeking cannot be blank!");
                return false;
            } else if (TieUp_withId.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up with!");
                return false;
            } else if (TieUp_withStr.equalsIgnoreCase("Others") && edit_others_three.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up with cannot be blank!");
                return false;
            } else if (AddressStr.trim().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please provide Address!");
                return false;
            } else if (edit_name.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Name of Entity is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_person.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Contact Person is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Contact Email is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Please update email in profile");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
                MyToast.toastLong(TieUpsActivity.this, "Please enter valid Contact Email!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Please update phone no. in profile");
                return false;
            } else if (edit_contact_ph.getText().length() < 10) {
                MyToast.toastShort(TieUpsActivity.this, "Invalid Phone Number");
                return false;
            } else if (TextUtils.isEmpty(ed_status.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description is required!");
                return false;
            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_name.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of entity!");
                return false;
            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_contact_person.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of contact person!");
                return false;
            }
        } else if (radioCurrentAdd.isChecked()) {
            if (country_id == null || country_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter country!");
                return false;
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter state!");
                return false;
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter city!");
                return false;
            }else if (TieUpFor_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up for!");
                return false;
            } else if (TieUpforStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up cannot be blank!");
                return false;
            } else if (SeekId.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Seeking!");
                return false;
            } else if (SeekStr.equalsIgnoreCase("Others") && edit_others_two.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Seeking cannot be blank!");
                return false;
            } else if (TieUp_withId.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up with!");
                return false;
            } else if (TieUp_withStr.equalsIgnoreCase("Others") && edit_others_three.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up with cannot be blank!");
                return false;
            } else if (AddressStr.trim().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please provide Address!");
                return false;
            } else if (edit_name.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Name of Entity is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_person.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Contact Person is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Please update email in profile");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
                MyToast.toastLong(TieUpsActivity.this, "Please enter valid Email!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Please update phone no. in profile");
                return false;
            } else if (edit_contact_ph.getText().length() < 10) {
                MyToast.toastShort(TieUpsActivity.this, "Invalid Phone Number");
                return false;
            } else if (TextUtils.isEmpty(ed_status.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description is required!");
                return false;
            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_name.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of entity!");
                return false;
            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_contact_person.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of contact person!");
                return false;
            }
        } else {
            if (country_id == null || country_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter country!");
                return false;
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter state!");
                return false;
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter city!");
                return false;
            } else if (edit_street.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Street,House No.!");
                return false;
            } else if (TieUpFor_id.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up for!");
                return false;
            } else if (TieUpforStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up cannot be blank!");
                return false;
            } else if (SeekId.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Seeking!");
                return false;
            } else if (SeekStr.equalsIgnoreCase("Others") && edit_others_two.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Seeking cannot be blank!");
                return false;
            } else if (TieUp_withId.equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up with!");
                return false;
            } else if (TieUp_withStr.equalsIgnoreCase("Others") && edit_others_three.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up with cannot be blank!");
                return false;
            } else if (edit_name.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(TieUpsActivity.this, "Name of Entity is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_person.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Contact Person is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Contact Email is required!");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
                MyToast.toastLong(TieUpsActivity.this, "Please enter valid Email!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Please update phone no. in profile");
                return false;
            } else if (edit_contact_ph.getText().length() < 10) {
                MyToast.toastShort(TieUpsActivity.this, "Invalid Phone Number");
                return false;
            } else if (TextUtils.isEmpty(ed_status.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description is required!");
                return false;
            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_name.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of entity!");
                return false;
            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_contact_person.getText().toString())) {
                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of contact person!");
                return false;
            }
        }
        return true;
    }
//    private boolean isValidOne() {
//        if (AddressStr.equalsIgnoreCase("")) {
//            if (country_id == null || country_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter country!");
//                return false;
//            } else if (state_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter state!");
//                return false;
//            } else if (city_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter city!");
//                return false;
//            } else if (TieUpFor_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up for!");
//                return false;
//            } else if (TieUpforStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up cannot be blank!");
//                return false;
//            } else if (SeekId.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter Seeking!");
//                return false;
//            } else if (SeekStr.equalsIgnoreCase("Others") && edit_others_two.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Others for Seeking cannot be blank!");
//                return false;
//            } else if (TieUp_withId.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up with!");
//                return false;
//            } else if (TieUp_withStr.equalsIgnoreCase("Others") && edit_others_three.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up with cannot be blank!");
//                return false;
//            } else if (edit_name.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Name of Entity is required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_contact_person.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Contact Person is required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Contact Email is required!");
//                return false;
//            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
//                MyToast.toastLong(TieUpsActivity.this, "Please enter valid Contact Email!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Contact Phone is required!");
//                return false;
//            } else if (TextUtils.isEmpty(ed_status.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Description is required!");
//                return false;
//            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_name.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of entity!");
//                return false;
//            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_contact_person.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of contact person!");
//                return false;
//            }
//        } else {
//            if (TieUpFor_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up for!");
//                return false;
//            } else if (TieUpforStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up cannot be blank!");
//                return false;
//            } else if (SeekId.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter Seeking!");
//                return false;
//            } else if (SeekStr.equalsIgnoreCase("Others") && edit_others_two.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Others for Seeking cannot be blank!");
//                return false;
//            } else if (TieUp_withId.equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Please Enter Tie Up with!");
//                return false;
//            } else if (TieUp_withStr.equalsIgnoreCase("Others") && edit_others_three.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Others for Tie Up with cannot be blank!");
//                return false;
//            } else if (edit_name.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(TieUpsActivity.this, "Name of Entity is required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_contact_person.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Contact Person is required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Contact Email is required!");
//                return false;
//            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
//                MyToast.toastLong(TieUpsActivity.this, "Please enter valid Contact Email!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Contact Phone is required!");
//                return false;
//            } else if (TextUtils.isEmpty(ed_status.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Description is required!");
//                return false;
//            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_name.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of entity!");
//                return false;
//            } else if (ed_status.getText().toString().equalsIgnoreCase(edit_contact_person.getText().toString())) {
//                MyToast.toastLong(TieUpsActivity.this, "Description cannot be similar to name of contact person!");
//                return false;
//            }
//        }
//
//        return true;
//    }

    //    private boolean isValidTwo() {
//         if (TextUtils.isEmpty(edit_loc.getText().toString())) {
//            MyToast.toastLong(TieUpsActivity.this,"Address is required!");
//            return false;
//        }else if (TextUtils.isEmpty(edit_contact_person.getText().toString())) {
//            MyToast.toastLong(TieUpsActivity.this,"Contact Person is required!");
//            return false;
//        }else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
//            MyToast.toastLong(TieUpsActivity.this,"Contact Email is required!");
//            return false;
//        }else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
//            MyToast.toastLong(TieUpsActivity.this,"Please enter valid Contact Email!");
//            return false;
//        }else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
//            MyToast.toastLong(TieUpsActivity.this,"Contact Phone is required!");
//            return false;
//        }else if (TextUtils.isEmpty(ed_status.getText().toString())) {
//            MyToast.toastLong(TieUpsActivity.this,"Description is required!");
//            return false;
//        }else if (ed_status.getText().toString().equalsIgnoreCase(edit_name.getText().toString())) {
//            MyToast.toastLong(TieUpsActivity.this,"Description cannot be similar to name of entity!");
//            return false;
//        }else if (ed_status.getText().toString().equalsIgnoreCase(edit_contact_person.getText().toString())) {
//            MyToast.toastLong(TieUpsActivity.this,"Description cannot be similar to name of contact person!");
//            return false;
//        }
//        return true;
//    }
    private void OtherCategoryApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        //params.put("sync_time", AppCustomPreferenceClass.readString(TieUpsActivity.this, AppCustomPreferenceClass.other_cat_sync_time, ""));
        params.put("sync_time", "");

        header.put("Cynapse", params);
        new OtherCategoryApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    // handler.deleteTableName(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER);
                    AppCustomPreferenceClass.writeString(TieUpsActivity.this, AppCustomPreferenceClass.other_cat_sync_time, sync_time);
                    Log.d("RESPONSECOUNTRY", response.toString());
                    if (res_code.equals("1")) {
                        // OthercategoryList.clear();
                        //  OthercategoryNameList.clear();
                        //TypecategoryList.clear();
                        // TypeNameList.clear();
                        // TieUpcategoryList.clear();
                        //  TieUpNameList.clear();

                        //  MyToast.toastLong(this,res_msg);
                        JSONArray header2 = header.getJSONArray("OtherCategory");
                        for (int i = 0; i < header2.length(); i++) {
                            OtherCategoryModel model = new OtherCategoryModel();
                            JSONObject item = header2.getJSONObject(i);

                            model.setId(item.getString("id"));
                            model.setType_id(item.getString("type_id"));
                            model.setName(item.getString("name"));
                            model.setStatus(item.getString("status"));
                            //countrySpinner.add(item.getString("country_name"));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, DatabaseHelper.id, item.getString("id"))) {

                                handler.AddOtherCategory(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                handler.AddOtherCategory(model, false);
                            }
                        }
                        for (int i = 1; i < 4; i++) {
                            OtherCategoryModel model = new OtherCategoryModel();
                            model.setId(String.valueOf(-i - 1));
                            model.setType_id(String.valueOf(i + 1));
                            model.setName("Others");
                            model.setStatus("1");
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, DatabaseHelper.id, String.valueOf(-i - 1))) {

                                handler.AddOtherCategory(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                handler.AddOtherCategory(model, false);
                            }
                        }


                        setArrayList1();
                        setArrayList2();
                        setArrayList3();

                    } else {
                        // OthercategoryList.clear();
                        // MyToast.toastLong(TieUpsActivity.this,res_msg);
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
        new GetAllCountryApi(this) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSECOUNTRY", response.toString());
                    if (res_code.equals("1")) {
                        // countrySpinner.clear();
                        //countryList.clear();
                        //  MyToast.toastLong(this,res_msg);
                        JSONArray header2 = header.getJSONArray("Country");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            countryList.add(new CountryModel(item.getString("country_code"), item.getString("country_name")));
                            countryName.add(item.getString("country_name"));
                        }
                        if (countryList.size() > 0) {

                            countryName = new ArrayList<>();
                            tempcountry = new ArrayList<>();
                            for (int j = 0; j < countryList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempcountry.add(countryList.get(j).getCountry_name());
                                countryName.add(countryList.get(j).getCountry_name());
                            }

                            country_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempcountry);
                            country_auto.setAdapter(country_adapter);
                            country_auto.setThreshold(1);
                        }
//                        try {
//                            country.setAdapter(new ArrayAdapter<>(TieUpsActivity.this,android.R.layout.simple_spinner_dropdown_item,countrySpinner));
//                        }
//                        catch (NullPointerException ne)
//                        {
//                            ne.printStackTrace();
//                        }

                    } else {
                        // countrySpinner.clear();
                        //  countryList.clear();
                        // MyToast.toastLong(TieUpsActivity.this,res_msg);
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
        new GetStateApi(TieUpsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("STATERESPONSE", response.toString());
                    if (res_code.equals("1")) {
                        // stateSpinner.clear();
                        stateList.clear();
                        //  cityList.clear();
                        //   citySpinner.clear();
                        // MyToast.toastLong(TieUpsActivity.this,res_msg);
                        JSONArray header2 = header.getJSONArray("State");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            stateList.add(new StateModel(item.getString("country_code"), item.getString("state_code"), item.getString("state_name")));

                            stateName.add(item.getString("state_name"));
                        }
                        if (stateList.size() > 0) {

                            stateName = new ArrayList<>();
                            tempstate = new ArrayList<>();
                            for (int j = 0; j < stateList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempstate.add(stateList.get(j).getState_name());
                                stateName.add(stateList.get(j).getState_name());
                            }

                            state_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempstate);
                            state_auto.setAdapter(state_adapter);
                            state_auto.setThreshold(1);
                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TieUpsActivity.this,android.R.layout.simple_spinner_dropdown_item,stateSpinner);
//                        state.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    } else {
                        // stateSpinner.clear();
                        // stateList.clear();
                        //  cityList.clear();
                        //  citySpinner.clear();
                        //  MyToast.toastLong(TieUpsActivity.this,res_msg);
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
        Log.d("CITYHEADER", params.toString());
        new GetCityApi(TieUpsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("CITYRESPONSE", response.toString());
                    if (res_code.equals("1")) {
                        // citySpinner.clear();
                        cityList.clear();
                        // MyToast.toastLong(TieUpsActivity.this,res_msg);
                        JSONArray header2 = header.getJSONArray("City");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            cityList.add(new CityModel(item.getString("city_id"), item.getString("country_code"), item.getString("state_code"), item.getString("city_name")));
                            cityName.add(item.getString("city_name"));
                        }
                        if (cityList.size() > 0) {

                            cityName = new ArrayList<>();
                            tempcity = new ArrayList<>();
                            for (int j = 0; j < cityList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempcity.add(cityList.get(j).getCity_name());
                                cityName.add(cityList.get(j).getCity_name());
                            }

                            city_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempcity);
                            city_auto.setAdapter(city_adapter);
                            city_auto.setThreshold(1);
                        }
//                        ArrayAdapter<String> adapter =new ArrayAdapter<String>(TieUpsActivity.this,android.R.layout.simple_spinner_dropdown_item,citySpinner);
//                        city.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    } else {
                        // citySpinner.clear();
                        //  cityList.clear();
                        // MyToast.toastLong(TieUpsActivity.this,res_msg);
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
//     '0'  => 'uuid',
//             '1'  => 'category_id',
//             '2'  => 'tie_up_for',
//             '3'  => 'others_tie_up_for_name',
//             '4'  => 'seeking',
//             '5'  => 'others_seeking_name',
//             '6'  => 'tie_up_with',
//             '7'  => 'others_tie_up_with_name',
//             '8'  => 'name_of_entity',
//             '9'  => 'contact_person',
//             '10' => 'contact_no',
//             '11' => 'contact_email',
//             '12' => 'address',
//             '13' => 'description',
//             '14' => 'product_image'

    private void addProductApi() throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(TieUpsActivity.this, AppCustomPreferenceClass.UserId, ""));
        if (TieUpforStr.equalsIgnoreCase("Others")) {
            params.put("tie_up_for", "others_tie_up_for");
            params.put("others_tie_up_for_name", edit_others.getText().toString());
        } else {
            params.put("tie_up_for", TieUpFor_id);
            params.put("others_tie_up_for_name", "");
        }
        if (SeekStr.equalsIgnoreCase("Others")) {
            params.put("seeking", "others_seeking");
            params.put("others_seeking_name", edit_others_two.getText().toString());
        } else {
            params.put("seeking", SeekId);
            params.put("others_seeking_name", "");
        }
        if (TieUp_withStr.equalsIgnoreCase("Others")) {
            params.put("tie_up_with", "others_tie_up_with");
            params.put("others_tie_up_with_name", edit_others_three.getText().toString());
        } else {
            params.put("tie_up_with", TieUp_withId);
            params.put("others_tie_up_with_name", "");
        }

        if (AddressStr.equalsIgnoreCase("")) {
            params.put("city", city_id);
            params.put("country", country_id);
            params.put("state", state_id);
//
//            params.put("city", "");
//            params.put("country", "");
//            params.put("state", "");
            params.put("address",  edit_street.getText().toString());
        } else {
            geoLocate();
            params.put("address", AddressStr);
            params.put("city", city_id);
            params.put("country", country_id);
            params.put("state", state_id);
        }
        // params.put("contact_phone", edit_contact_ph.getText().toString());
        // params.put("contact_email", edit_cont_email.getText().toString());
        params.put("house_street", edit_street.getText().toString());
        params.put("category_id", "3");
        params.put("name_of_entity", edit_name.getText().toString());
        params.put("contact_person", edit_contact_person.getText().toString());
        params.put("contact_no", edit_contact_ph.getText().toString());
        params.put("contact_email", edit_cont_email.getText().toString());
        params.put("description", ed_status.getText().toString());


        if (profile_imageAl.size()==0)
        params.put("product_image","");
        else
            params.put("product_image", profile_imageAl.toString().substring(1,profile_imageAl.toString().length()-1).replace(" ",""));

        params.put("latitude",addressLat);
        params.put("longitude",addressLog);

        header.put("Cynapse", params);

        new AddTieUpsProductApi(TieUpsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    if (res_code.equals("1")) {
                        final Dialog dialog = new Dialog(TieUpsActivity.this);
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
//                        title.setText(R.string.congrats);
//                        msg.setText(R.string.publishedSuccessfully);
                        title.setText(R.string.sent);
                        //msg.setText(R.string.request_sent);
                        msg.setText(R.string.tieUp_req);
                        dialog.show();
                        //TODO : dismiss the on btn click and close click
                        btnGotIt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //TODO : finishing the activity
                                finish();
                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        // MyToast.toastLong(TieUpsActivity.this, res_msg);
//                        finish();
                    } else {
                        MyToast.toastLong(TieUpsActivity.this, res_msg);
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

    // ---------Fetching data from database ------- //
    public void setArrayList() {
        OthercategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "2");

//        for(int i = 0;i<OthercategoryList.size();i++)
//        {
//            OthercategoryNameList.add(OthercategoryList.get(i).getName());
//
//        }
//       // OthercategoryNameList.add("Others");
//        try {
//            categories.setAdapter(new ArrayAdapter<>(TieUpsActivity.this,android.R.layout.simple_spinner_dropdown_item,OthercategoryNameList));
//        }
//        catch (NullPointerException ne)
//        {
//            ne.printStackTrace();
//        }
        if (OthercategoryList.size() > 0) {

            TieUpForName = new ArrayList<>();
            tempTieUpFor = new ArrayList<>();
            for (int j = 0; j < OthercategoryList.size(); j++) {
                // countryName.add(medicalList.get(j).getProfile_category_name());
                tempTieUpFor.add(OthercategoryList.get(j).getName());
                TieUpForName.add(OthercategoryList.get(j).getName());
            }

            tie_up_for_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
            tieup_for_auto.setAdapter(tie_up_for_adapter);
            tieup_for_auto.setThreshold(1);
        }

        TypecategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "3");

//        for(int i = 0;i<TypecategoryList.size();i++)
//        {
//            TypeNameList.add(TypecategoryList.get(i).getName());
//        }
//       // Collections.reverse(TypeNameList);
//       // TypeNameList.add("Others");
//        try {
//            type.setAdapter(new ArrayAdapter<>(TieUpsActivity.this,android.R.layout.simple_spinner_dropdown_item,TypeNameList));
//        }
//        catch (NullPointerException ne)
//        {
//            ne.printStackTrace();
//        }
        if (TypecategoryList.size() > 0) {

            SeekingName = new ArrayList<>();
            tempSeeking = new ArrayList<>();
            for (int j = 0; j < TypecategoryList.size(); j++) {
                // countryName.add(medicalList.get(j).getProfile_category_name());
                tempSeeking.add(TypecategoryList.get(j).getName());
                SeekingName.add(TypecategoryList.get(j).getName());
            }

            seeking_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempSeeking);
            seeking_auto.setAdapter(seeking_adapter);
            seeking_auto.setThreshold(1);
        }

        TieUpcategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "4");

//        for(int i = 0;i<TieUpcategoryList.size();i++)
//        {
//            TieUpNameList.add(TieUpcategoryList.get(i).getName());
//        }
//       // TieUpNameList.add("Others");
//        try {
//            tie_up_with.setAdapter(new ArrayAdapter<>(TieUpsActivity.this,android.R.layout.simple_spinner_dropdown_item,TieUpNameList));
//        }
//        catch (NullPointerException ne)
//        {
//            ne.printStackTrace();
//        }
        if (TieUpcategoryList.size() > 0) {

            TieUp_withName = new ArrayList<>();
            tempTieUpWith = new ArrayList<>();
            for (int j = 0; j < TieUpcategoryList.size(); j++) {
                // countryName.add(medicalList.get(j).getProfile_category_name());
                tempTieUpWith.add(TieUpcategoryList.get(j).getName());
                TieUp_withName.add(TieUpcategoryList.get(j).getName());
            }

            tie_up_with_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpWith);
            tie_up_with_auto.setAdapter(tie_up_with_adapter);
            tie_up_with_auto.showDropDown();
        }

    }

    public void setArrayList1() {
        OthercategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "2");
        if (OthercategoryList.size() > 0) {

            TieUpForName = new ArrayList<>();
            tempTieUpFor = new ArrayList<>();
            for (int j = 0; j < OthercategoryList.size(); j++) {
                // countryName.add(medicalList.get(j).getProfile_category_name());

                tempTieUpFor.add(OthercategoryList.get(j).getName());
                TieUpForName.add(OthercategoryList.get(j).getName());
            }

            tie_up_for_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
            tieup_for_auto.setAdapter(tie_up_for_adapter);
            tieup_for_auto.setThreshold(1);
        }
    }

    public void setArrayList2() {
        TypecategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "3");

        if (TypecategoryList.size() > 0) {

            SeekingName = new ArrayList<>();
            tempSeeking = new ArrayList<>();
            for (int j = 0; j < TypecategoryList.size(); j++) {
                // countryName.add(medicalList.get(j).getProfile_category_name());
                tempSeeking.add(TypecategoryList.get(j).getName());
                SeekingName.add(TypecategoryList.get(j).getName());
            }

            seeking_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempSeeking);
            seeking_auto.setAdapter(seeking_adapter);
            seeking_auto.setThreshold(1);
        }

    }

    public void setArrayList3() {
        TieUpcategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "4");
        if (TieUpcategoryList.size() > 0) {

            TieUp_withName = new ArrayList<>();
            tempTieUpWith = new ArrayList<>();
            for (int j = 0; j < TieUpcategoryList.size(); j++) {
                // countryName.add(medicalList.get(j).getProfile_category_name());
                tempTieUpWith.add(TieUpcategoryList.get(j).getName());
                TieUp_withName.add(TieUpcategoryList.get(j).getName());
            }

            tie_up_with_adapter = new Adapter_Filter(TieUpsActivity.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpWith);
            tie_up_with_auto.setAdapter(tie_up_with_adapter);
            tie_up_with_auto.setThreshold(1);
        }
    }

    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(TieUpsActivity.this, header, false) {
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
                        edit_contact_person.setText(item.getString("name"));
                        edit_cont_email.setText(item.getString("email"));
                        edit_contact_ph.setText(item.getString("phone_number"));
                        my_address_txt.setText(item.getString("address") + " " + item.getString("city_name") + " " + item.getString("state_name") + " " + item.getString("country_name"));
                        AddressStr = my_address_txt.getText().toString();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private int productImgs = 0;

    ArrayList<String> profile_imageAl  = new ArrayList<>();
}
