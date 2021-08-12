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
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
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
import com.alcanzar.cynapse.api.AddHospitalProductApi;
import com.alcanzar.cynapse.api.GetAllCountryApi;
import com.alcanzar.cynapse.api.GetCityApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.api.OtherCategoryApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.GetCategoryModel;
import com.alcanzar.cynapse.model.OtherCategoryModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.FilePath;
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

/**
 * Created by alcanzar on 05/04/18.
 */

public class SellHospitalActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    //TODO : header and other views

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    AutoCompleteTextView edit_street;


    TextView title, licenceJpg, feet_two_txt, feet_one_txt, sq_feet_txt;
    ImageView btnBack, titleIcon, productImg, upload;
    Button btnPublish;
    long area = 1;
    String categoryId = "", dealTyp = "", TypeId = "", length = "", width = "", type = "";
    ArrayList<String> productCategory = new ArrayList<>();
    ArrayList<GetCategoryModel> categoryList = new ArrayList<>();
    RelativeLayout other_rel_lay, rel_lay_loc;
    Spinner state, city, Type, country, hospital_dd, feet_yard_spin, categories;
    String profile_image = "";
    private Bitmap bitmap;
    private Uri fileUri;
    DatabaseHelper handler;
    private String pdfPath, url, pdf_name = "";
    List<String> list, length_list, list_categories, Type_list, list_type_feet;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int PICK_CAMERA_REQUEST = 11;
    static String picturePath;
    ArrayList<String> stateSpinner = new ArrayList<>();
    ArrayList<String> citySpinner = new ArrayList<>();

    EditText edit_Price, ed_age, ed_status, edit_loc, edit_tot_arae, edit_built_area, edit_cont_email, edit_contact_ph,
            no_of_beds, area_length, area_width, edit_others, left_licence, done_licence, edit_qty;
    String countryId = "", stateCountryId = "", stateId = "", cityId = "", CatId = "", Category_name = "", category = "", Cat_PracID = "", Cat_prac = "", HosStr = "", HosId = "",
            country_id = "", country_str = "", state_id = "", state_Str = "", city_id = "", city_str = "", AddressStr = "";

    ArrayList<String> countryName = new ArrayList<>();
    ArrayList<String> tempcountry = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> tempstate = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();
    ArrayList<String> tempcity = new ArrayList<>();
    ArrayList<CountryModel> countryList = new ArrayList<>();
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();
    private static final int PICK_PDF_REQUEST = 12;
    ArrayList<OtherCategoryModel> OthercategoryList = new ArrayList<>();
    ArrayList<String> OthercategoryNameList = new ArrayList<>();
    AutoCompleteTextView hospital_auto, country_auto, state_auto, city_auto;
    ArrayAdapter hospital_adapter, country_adapter, state_adapter, city_adapter;
    ArrayList<String> tempHospital;
    ArrayList<String> HospitalName;
    RadioGroup radioGrp;
    RadioButton radioMyAdd, radioCurrentAdd, radioEnterAdd;
    TextView my_address_txt, current_address_txt;

    String addressLat = "", addressLog = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product_hospital);
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        handler = new DatabaseHelper(this);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        edit_cont_email = findViewById(R.id.edit_cont_email);
        edit_contact_ph = findViewById(R.id.edit_contact_ph);
        titleIcon.setImageResource(R.drawable.deals_white);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        Type = findViewById(R.id.Type);
        sq_feet_txt = findViewById(R.id.sq_feet_txt);
        feet_two_txt = findViewById(R.id.feet_two_txt);
        feet_one_txt = findViewById(R.id.feet_one_txt);
        licenceJpg = findViewById(R.id.licenceJpg);
        productImg = findViewById(R.id.productImg);
        country = findViewById(R.id.country);
        hospital_dd = findViewById(R.id.hospital_dd);
        other_rel_lay = findViewById(R.id.other_rel_lay);
        rel_lay_loc = findViewById(R.id.rel_lay_loc);
        area_length = findViewById(R.id.area_length);
        area_width = findViewById(R.id.area_width);
        feet_yard_spin = findViewById(R.id.feet_yard_spin);

        categories = findViewById(R.id.categories);

        edit_street = findViewById(R.id.edit_street);
        edit_loc = findViewById(R.id.edit_loc);
        edit_tot_arae = findViewById(R.id.edit_tot_arae);
        edit_tot_arae.setEnabled(false);
        edit_built_area = findViewById(R.id.edit_built_area);
        upload = findViewById(R.id.upload);
        no_of_beds = findViewById(R.id.no_of_beds);
        edit_Price = findViewById(R.id.edit_Price);
        edit_others = findViewById(R.id.edit_others);
        left_licence = findViewById(R.id.left_licence);
        done_licence = findViewById(R.id.done_licence);

        edit_qty = findViewById(R.id.edit_qty);


        ed_age = findViewById(R.id.ed_age);

        radioGrp = findViewById(R.id.radioGrp);
        radioMyAdd = findViewById(R.id.radioMyAdd);
        radioCurrentAdd = findViewById(R.id.radioCurrentAdd);
        radioEnterAdd = findViewById(R.id.radioEnterAdd);
        my_address_txt = findViewById(R.id.my_address_txt);
        current_address_txt = findViewById(R.id.current_address_txt);

        ed_status = findViewById(R.id.ed_status);

        title = findViewById(R.id.title);
        title.setText(R.string.sell_prod_hos);
        btnPublish = findViewById(R.id.btnPublish);

        hospital_auto = findViewById(R.id.hospital_auto);
        country_auto = findViewById(R.id.country_auto);
        state_auto = findViewById(R.id.state_auto);
        city_auto = findViewById(R.id.city_auto);


        btnPublish.setOnClickListener(this);
        productImg.setOnClickListener(this);
        upload.setOnClickListener(this);
        country_auto.setOnClickListener(this);
        hospital_auto.setOnClickListener(this);
        city_auto.setOnClickListener(this);
        state_auto.setOnClickListener(this);
        getCountyApi();
        addItemsOnSpinnerFeet();
        addItemsOnSpinnerCategories();


        try {
            OtherCategoryApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        OthercategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER,"1");
//        if(OthercategoryList.size() > 0)
//        {
//            setArrayList();
//        }
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cat_prac = list_categories.get(i);
                if (Cat_prac.equalsIgnoreCase("Buy")) {
                    Cat_PracID = "1";
                } else if (Cat_prac.equalsIgnoreCase("Sell")) {
                    Cat_PracID = "2";

                } else {
                    Cat_PracID = "3";
                }
                Log.d("Cat_PracID", Cat_PracID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                        current_address_txt.setText(getCompleteAddressString(SellHospitalActivity.this,
                                Double.parseDouble(AppCustomPreferenceClass.readString(SellHospitalActivity.this, AppCustomPreferenceClass.Latitude, "")),
                                Double.parseDouble(AppCustomPreferenceClass.readString(SellHospitalActivity.this, AppCustomPreferenceClass.Longitude, ""))));
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

        feet_yard_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = list_type_feet.get(i);
                if (category.equalsIgnoreCase("Feet")) {
                    categoryId = "1";
                } else {
                    categoryId = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addItemsOnSpinnerType();
        Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = Type_list.get(i);
                if (type.equalsIgnoreCase("Regular")) {
                    TypeId = "1";
                } else {
                    TypeId = "2";
                }

                Log.d("type", type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        area_length.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                feet_one_txt.setText(category);
                feet_two_txt.setText(category);
                if (area_length.getText().toString().equalsIgnoreCase("")) {

                    edit_tot_arae.setText(String.format("%s square%s", String.valueOf(area), category));
                } else if (area_width.getText().toString().equalsIgnoreCase("")) {
                    area = Long.parseLong(area_length.getText().toString());
                    edit_tot_arae.setText(String.format("%s square%s", String.valueOf(area), category));
                } else {
                    area = Long.parseLong(area_width.getText().toString()) * Long.parseLong(area_length.getText().toString());
                    edit_tot_arae.setText(String.format("%s square%s", String.valueOf(area), category));

                }
                Log.d("area1", "length<><<<<<" + area);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        area_width.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                feet_two_txt.setText(category);
                feet_one_txt.setText(category);
                if (area_width.getText().toString().equalsIgnoreCase("")) {

                    edit_tot_arae.setText(String.format("%s square%s", String.valueOf(area), category));
                } else if (area_length.getText().toString().equalsIgnoreCase("")) {
                    area = Long.parseLong(area_width.getText().toString());
                    edit_tot_arae.setText(String.format("%s square%s", String.valueOf(area), category));
                } else {
                    area = Long.parseLong(area_width.getText().toString()) * Long.parseLong(area_length.getText().toString());
                    edit_tot_arae.setText(String.format("%s square%s", String.valueOf(area), category));
                }
                Log.d("area2", "width<><<<<<" + area);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_built_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sq_feet_txt.setText(String.format("square%s", category));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                countryId = countryList.get(position).getCountry_code();
                Log.d("COUNTRYIDWIL", countryId);
                //TODO: calling the stateApi here
                try {
                    getStateApi(countryId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateCountryId = stateList.get(i).getCountry_code();
                stateId = stateList.get(i).getState_code();
                try {
                    getCityApi(stateCountryId, stateId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityId = cityList.get(i).getCity_id();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        hospital_dd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CatId = OthercategoryList.get(i).getId();
                Category_name = OthercategoryList.get(i).getName();
                if (CatId.equalsIgnoreCase("-11")) {
                    other_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_rel_lay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        hospital_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (hospital_auto.getText().toString().equals("")) {
                        tempHospital = new ArrayList<>();
                        try {
                            tempHospital.addAll(HospitalName);
                            hospital_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
                            hospital_auto.setAdapter(hospital_adapter);
                            hospital_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }

                    }


                } else {
                    if (hospital_auto.toString().equals("")) {
                    } else {
                    }

                }
            }
        });
        hospital_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HosStr = hospital_auto.getText().toString();
                HosId = OthercategoryList.get(HospitalName.indexOf(HosStr)).getId();
                Category_name = OthercategoryList.get(HospitalName.indexOf(HosStr)).getName();
                edit_others.setText("");
                if (HosId.equalsIgnoreCase("-11")) {
                    other_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_rel_lay.setVisibility(View.GONE);
                }
                Log.d("medicalId", HosId);
            }
        });
        hospital_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempHospital = new ArrayList<>();
                        tempHospital.addAll(HospitalName);

                        hospital_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
                        hospital_auto.setAdapter(hospital_adapter);
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
        country_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (country_auto.getText().toString().equals("")) {
                        tempcountry = new ArrayList<>();

                        try {
                            tempcountry.addAll(countryName);
                            country_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempcountry);
                            country_auto.setAdapter(country_adapter);
                            country_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
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
                Log.d("iteem", country_str);
                if (country_str.equalsIgnoreCase("")) {
                    country_id = "";
                } else {
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

                        country_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempcountry);
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
                    if (state_auto.getText().toString().equals("")) {
                        tempstate = new ArrayList<>();
                        try {
                            tempstate.addAll(stateName);
                            state_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempstate);
                            state_auto.setAdapter(state_adapter);
                            state_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }

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
                if (count > 0) {
                    try {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);

                        state_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempstate);
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


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    if (city_auto.getText().toString().equals("")) {
                        tempcity = new ArrayList<>();


                        try {
                            tempcity.addAll(cityName);
                            city_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempcity);
                            city_auto.setAdapter(city_adapter);
                            city_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
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
                if (count > 0) {
                    try {
                        tempcity = new ArrayList<>();
                        tempcity.addAll(cityName);

                        city_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempcity);
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

        clearListData();
        init();

        openGoogleLocatiion("<small>Street, House No.</small>");
    }

    private void openGoogleLocatiion(String sms)
    {
        PlacesClient placesClient;

        //String apiKey = "AIzaSyCUVWRZZji7uyu0xZdwYgC1q3xRJdReJ_Q";
        String apiKey = getString(R.string.googlePlaceAPI);
        Utils.sop("apiKey" + apiKey);

        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(SellHospitalActivity.this, apiKey);
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

    public void addItemsOnSpinnerCategories() {


        list_categories = new ArrayList<>();
        list_categories.add("Buy");
        list_categories.add("Sell");
        list_categories.add("Lease");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();

                break;
            case R.id.productImg:
                chooserDialog("image");
                break;
            case R.id.upload:
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
                }
                break;

            case R.id.hospital_auto:
                try {
                    if (hospital_auto.getText().toString().equals("")) {
                        tempHospital = new ArrayList<>();
                        tempHospital.addAll(HospitalName);
                    }

                    hospital_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
                    hospital_auto.setAdapter(hospital_adapter);
                    hospital_auto.showDropDown();
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


                    country_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempcountry);
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
                    state_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempstate);
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
                    city_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempcity);
                    city_auto.setAdapter(city_adapter);
                    city_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }


                break;
            case R.id.btnPublish:
                //TODO: moving to the next page of publish section
//                if (btnPublish.getText().toString().equals("NEXT")) {
//                    if(isValidOne())
//                    {
//                        btnPublish.setText(R.string.publish);
//                        //TODO: switching to the next page
//                        relFirst.setVisibility(View.GONE);
//                        relSecond.setVisibility(View.VISIBLE);
//                    }
//
//                } else if(btnPublish.getText().toString().equals("PUBLISH")){
//                    //TODO : opening the success pop up
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
                //               }
                break;
        }
    }

    private void clearListData() {
        productCategory.clear();

    }

    public void addItemsOnSpinnerFeet() {


        list_type_feet = new ArrayList<String>();
        list_type_feet.add("Feet");
        list_type_feet.add("Meter");
        list_type_feet.add("Yards");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_type_feet);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feet_yard_spin.setAdapter(dataAdapter);
    }

    public void addItemsOnSpinnerType() {
        Type_list = new ArrayList<>();
        Type_list.add("Regular");
        Type_list.add("Featured");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Type_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Type.setAdapter(dataAdapter);
    }


    private void init() {
        Log.e("SellHospitalActivity", "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(SellHospitalActivity.this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        edit_street.setAdapter(mPlaceAutocompleteAdapter);
        try {

            edit_street.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("asdfsaf", "dvasd");

                    geoLocate();

                }
            });
            edit_street.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                        //execute our method for searching

                        Log.e("asdfsaf", textView.getText().toString());

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

    private void geoLocate()
    {
        Log.e("SellHospitalActivity", "geoLocate: geolocating");

        String searchString = "";

        if (rel_lay_loc.getVisibility()==View.GONE)
        {
            searchString = AddressStr;
        }
        else
        {
            searchString = edit_street.getText().toString();
        }

        Geocoder geocoder = new Geocoder(SellHospitalActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e("SellHospitalActivity", "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            addressLat = String.valueOf(address.getLatitude());
            addressLog = String.valueOf(address.getLongitude());
            Log.e("SellHospitalActivity", "geoLocate: found a location: " + addressLat + "," + addressLog);
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//           moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//                    address.getAddressLine(0));
        }
    }

    private boolean isValid() {
        boolean bool = false;
        String[] totarea = edit_tot_arae.getText().toString().split(" ");
        if (radioMyAdd.isChecked()) {
            for (int i = 0; i < OthercategoryList.size(); i++) {
                if (OthercategoryList.get(i).getName().contains(hospital_auto.getText().toString().trim())) {
                    bool = true;
                    Log.d("hos_auto333", "<><<<");
                }
                Log.d("list_auto", "<><<<" + OthercategoryList.get(i).getName());
            }

            if (!bool) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select from drop-down only!");
                return false;
            } else if (HosId.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter Hospital Category!");
                return false;
            } else if (HosStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Others for Hospital Category cannot be blank!");
                return false;
            } else if (country_id == null || country_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter country!");
                return false;
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter state!");
                return false;
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter city!");
                return false;
            } else if (AddressStr.trim().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please provide Address!");
                return false;
            } else if (area_length.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select length of land!");
                return false;
            } else if (area_width.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select width of land!");
                return false;
            } else if (TextUtils.isEmpty(edit_tot_arae.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Total area is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_built_area.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Built up area is required!");
                return false;
            } else if (Long.parseLong(edit_built_area.getText().toString()) > Long.parseLong(totarea[0])) {
                MyToast.toastLong(SellHospitalActivity.this, "Built up area should be less or equal to Total Area!");
                return false;
            } else if (type.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select type!");
                return false;
            } else if (TextUtils.isEmpty(no_of_beds.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "No. of beds required!");
                return false;
            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Please update email in profile");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
                MyToast.toastLong(SellHospitalActivity.this, "Please enter valid Contact Email!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Please update phone no. in profile");
                return false;
            } else if (edit_contact_ph.getText().length() < 10) {
                MyToast.toastShort(SellHospitalActivity.this, "Invalid Phone Number");
                return false;
            }
//            else if (TextUtils.isEmpty(edit_qty.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Quantity is required!");
//                return false;
//            }
        } else if (radioCurrentAdd.isChecked()) {
            for (int i = 0; i < OthercategoryList.size(); i++) {
                if (OthercategoryList.get(i).getName().contains(hospital_auto.getText().toString().trim())) {
                    bool = true;
                    Log.d("hos_auto333", "<><<<");
                }
                Log.d("list_auto", "<><<<" + OthercategoryList.get(i).getName());
            }

            if (!bool) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select from drop-down only!");
                return false;
            } else if (HosId.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter Hospital Category!");
                return false;
            } else if (HosStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Others for Hospital Category cannot be blank!");
                return false;
            } else if (country_id == null || country_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter country!");
                return false;
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter state!");
                return false;
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter city!");
                return false;
            } else if (AddressStr.trim().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please provide Address!");
                return false;
            } else if (area_length.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select length of land!");
                return false;
            } else if (area_width.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select width of land!");
                return false;
            } else if (TextUtils.isEmpty(edit_tot_arae.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Total area is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_built_area.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Built up area is required!");
                return false;
            } else if (Long.parseLong(edit_built_area.getText().toString()) > Long.parseLong(totarea[0])) {
                MyToast.toastLong(SellHospitalActivity.this, "Built up area should be less or equal to Total Area!");
                return false;
            } else if (type.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select type!");
                return false;
            } else if (TextUtils.isEmpty(no_of_beds.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "No. of beds required!");
                return false;
            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Please update email in profile");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
                MyToast.toastLong(SellHospitalActivity.this, "Please enter valid Contact Email!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Please update phone no. in profile");
                return false;
            } else if (edit_contact_ph.getText().length() < 10) {
                MyToast.toastShort(SellHospitalActivity.this, "Invalid Phone Number");
                return false;
            }
//            else if (TextUtils.isEmpty(edit_qty.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Quantity is required!");
//                return false;
//            }
        } else {
            for (int i = 0; i < OthercategoryList.size(); i++) {
                if (OthercategoryList.get(i).getName().contains(hospital_auto.getText().toString())) {
                    Log.d("hos_auto111", "<><<<");
                    bool = true;
                }
                Log.d("list_auto", "<><<<" + OthercategoryList.get(i).getName());
            }
            if (!bool) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select from drop-down only!");
                return false;
            } else if (HosId.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter Hospital Category!");
                return false;
            } else if (HosStr.equalsIgnoreCase("Others") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Others for Hospital Category cannot be blank!");
                return false;
            } else if (country_id == null || country_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter country!");
                return false;
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter state!");
                return false;
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter city!");
                return false;
            } else if (edit_street.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please Enter Street,House No.!");
                return false;
            } else if (area_length.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select length of land!");
                return false;
            } else if (area_width.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select width of land!");
                return false;
            } else if (TextUtils.isEmpty(edit_tot_arae.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Total area is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_built_area.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Built up area is required!");
                return false;
            } else if (Long.parseLong(edit_built_area.getText().toString()) > Long.parseLong(totarea[0])) {
                MyToast.toastLong(SellHospitalActivity.this, "Built up area should be less or equal to Total Area!");
                return false;
            } else if (type.equalsIgnoreCase("")) {
                MyToast.toastLong(SellHospitalActivity.this, "Please select type!");
                return false;
            } else if (TextUtils.isEmpty(no_of_beds.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "No. of beds required!");
                return false;
            } else if (TextUtils.isEmpty(edit_cont_email.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Please update email in profile");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_cont_email.getText().toString()).matches()) {
                MyToast.toastLong(SellHospitalActivity.this, "Please enter valid Contact Email!");
                return false;
            } else if (TextUtils.isEmpty(edit_contact_ph.getText().toString())) {
                MyToast.toastLong(SellHospitalActivity.this, "Please update phone no. in profile");
                return false;
            } else if (edit_contact_ph.getText().length() < 10) {
                MyToast.toastShort(SellHospitalActivity.this, "Invalid Phone Number");
                return false;
            }
//            else if (TextUtils.isEmpty(edit_qty.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Quantity is required!");
//                return false;
//            }
        }
        return true;
    }
//    private boolean isValidOne() {
//        boolean bool=false;
//        String[] totarea = edit_tot_arae.getText().toString().split(" ");
//        // Log.e("stateId","<><"+stateId);
//        if (AddressStr.equalsIgnoreCase("")) {
//            for(int i = 0;i < OthercategoryList.size();i++)
//            {
//                if(OthercategoryList.get(i).getName().contains(hospital_auto.getText().toString()))
//                {
//                    Log.d("hos_auto111","<><<<");
//                    bool=true;
//                }
//                Log.d("list_auto","<><<<"+OthercategoryList.get(i).getName());
//            }
//            if(!bool)
//            {
//                MyToast.toastLong(SellHospitalActivity.this, "Please select from drop-down only!");
//                return false;
//            }else if (country_id == null || country_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please Enter country!");
//                return false;
//            } else if (state_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please Enter state!");
//                return false;
//            } else if (city_id.equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please Enter city!");
//                return false;
//            } else if (area_length.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please select length of land!");
//                return false;
//            } else if (area_width.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please select width of land!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_tot_arae.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Total area is required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_built_area.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Built up area is required!");
//                return false;
//            } else if (Integer.parseInt(edit_built_area.getText().toString()) > Integer.parseInt(totarea[0])) {
//                MyToast.toastLong(SellHospitalActivity.this, "Built up area should be less or equal to Total Area!");
//                return false;
//            } else if (type.equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please select type!");
//                return false;
//            } else if (TextUtils.isEmpty(no_of_beds.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "No. of beds required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_qty.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Quantity is required!");
//                return false;
//            }
//        } else {
//            for(int i = 0;i < OthercategoryList.size();i++)
//            {
//                if(OthercategoryList.get(i).getName().contains(hospital_auto.getText().toString().trim()))
//                {
//                    bool=true;
//                    Log.d("hos_auto333","<><<<");
//                }
//                Log.d("list_auto","<><<<"+OthercategoryList.get(i).getName());
//            }
//
//            if(!bool)
//            {
//                MyToast.toastLong(SellHospitalActivity.this, "Please select from drop-down only!");
//                return false;
//            }else if (area_length.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please select length of land!");
//                return false;
//            } else if (area_width.getText().toString().equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please select width of land!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_tot_arae.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Total area is required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_built_area.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Built up area is required!");
//                return false;
//            } else if (Integer.parseInt(edit_built_area.getText().toString()) > Integer.parseInt(totarea[0])) {
//                MyToast.toastLong(SellHospitalActivity.this, "Built up area should be less or equal to Total Area!");
//                return false;
//            } else if (type.equalsIgnoreCase("")) {
//                MyToast.toastLong(SellHospitalActivity.this, "Please select type!");
//                return false;
//            } else if (TextUtils.isEmpty(no_of_beds.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "No. of beds required!");
//                return false;
//            } else if (TextUtils.isEmpty(edit_qty.getText().toString())) {
//                MyToast.toastLong(SellHospitalActivity.this, "Quantity is required!");
//                return false;
//            }
//        }
//
//        return true;
//    }

    //   private boolean isValidTwo() {
//        String[] totarea=edit_tot_arae.getText().toString().split(" ");
//         if (TextUtils.isEmpty(edit_built_area.getText().toString())){
//            MyToast.toastLong(SellHospitalActivity.this,"Built up area is required!");
//            return false;
//        }else if (Integer.parseInt(edit_built_area.getText().toString())>Integer.parseInt(totarea[0])){
//            MyToast.toastLong(SellHospitalActivity.this,"Built up area should be less or equal to Total Area!");
//            return false;
//        }
//        else if (type.equalsIgnoreCase("") ) {
//            MyToast.toastLong(SellHospitalActivity.this,"Please select type!");
//            return false;
//        }else if (TextUtils.isEmpty(no_of_beds.getText().toString())) {
//            MyToast.toastLong(SellHospitalActivity.this,"No. of beds required!");
//            return false;
//        }
//        return true;
//    }
    private void OtherCategoryApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        //params.put("sync_time", AppCustomPreferenceClass.readString(SellHospitalActivity.this, AppCustomPreferenceClass.other_cat_sync_time, ""));
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
                    AppCustomPreferenceClass.writeString(SellHospitalActivity.this, AppCustomPreferenceClass.other_cat_sync_time, sync_time);
                    Log.d("RESPONSECOUNTRY", response.toString());
                    if (res_code.equals("1")) {
                        // OthercategoryList.clear();
                        // OthercategoryNameList.clear();
                        // handler.deleteTableName(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER);
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
                        OtherCategoryModel model = new OtherCategoryModel();
                        model.setId(String.valueOf(-11));
                        model.setType_id(String.valueOf(1));
                        model.setName("Others");
                        model.setStatus("1");
                        if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, DatabaseHelper.id, String.valueOf(-11))) {

                            handler.AddOtherCategory(model, true);

                            //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                        } else {
                            //   Log.e("UPDATED", true + " " + model.getProduct_id());
                            handler.AddOtherCategory(model, false);
                        }


                        setArrayList();

                    } else {
                        // OthercategoryList.clear();
                        // MyToast.toastLong(SellHospitalActivity.this,res_msg);
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

                            country_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempcountry);
                            country_auto.setAdapter(country_adapter);
                            country_auto.setThreshold(1);
                        }
//                        try {
//                            country.setAdapter(new ArrayAdapter<>(SellHospitalActivity.this,android.R.layout.simple_spinner_dropdown_item,countrySpinner));
//                        }
//                        catch (NullPointerException ne)
//                        {
//                            ne.printStackTrace();
//                        }

                    } else {
                        // countrySpinner.clear();
                        //  countryList.clear();
                        // MyToast.toastLong(SellHospitalActivity.this,res_msg);
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
        new GetStateApi(SellHospitalActivity.this, header) {
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
                        // cityList.clear();
                        //   citySpinner.clear();
                        // MyToast.toastLong(SellHospitalActivity.this,res_msg);
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

                            state_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempstate);
                            state_auto.setAdapter(state_adapter);
                            state_auto.setThreshold(1);
                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SellHospitalActivity.this,android.R.layout.simple_spinner_dropdown_item,stateSpinner);
//                        state.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    } else {
                        // stateSpinner.clear();
                        // stateList.clear();
                        //  cityList.clear();
                        //  citySpinner.clear();
                        //  MyToast.toastLong(SellHospitalActivity.this,res_msg);
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
        new GetCityApi(SellHospitalActivity.this, header) {
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
                        // MyToast.toastLong(SellHospitalActivity.this,res_msg);
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

                            city_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempcity);
                            city_auto.setAdapter(city_adapter);
                            city_auto.setThreshold(1);
                        }
//                        ArrayAdapter<String> adapter =new ArrayAdapter<String>(SellHospitalActivity.this,android.R.layout.simple_spinner_dropdown_item,citySpinner);
//                        city.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    } else {
                        // citySpinner.clear();
                        //  cityList.clear();
                        // MyToast.toastLong(SellHospitalActivity.this,res_msg);
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
//    private void getAllProductCategoryApi() {
//        new GetProductCategoryApi(getApplicationContext()) {
//            @Override
//            public void responseApi(JSONObject response) {
//                super.responseApi(response);
//                try {
//                    JSONObject header = response.getJSONObject("Cynapse");
//                    String res_code = header.getString("res_code");
//                    String res_msg = header.getString("res_msg");
//                    Log.d("RESONCATE", response.toString());
//                    if (res_code.equals("1")) {
//                        productCategory.clear();
//                        categoryList.clear();
//
//                        //MyToast.toastLong(SellHospitalActivity.this,res_msg);
//                        JSONArray header2 = header.getJSONArray("Category");
//                        for (int i = 0; i < header2.length(); i++) {
//                            JSONObject item = header2.getJSONObject(i);
//                            categoryList.add(new GetCategoryModel(item.getString("category_id"), item.getString("category_name")));
//                            productCategory.add(item.getString("category_name"));
//                            Log.d("CATEGORIDDD", item.getString("category_id"));
//
//                            //Log.e("medicalListSize",String.valueOf(medicalProfileSpinner.size()));
//                        }
//                        categories.setAdapter(new ArrayAdapter<>(SellHospitalActivity.this, android.R.layout.simple_spinner_dropdown_item, productCategory));
//                    } else {
//                        productCategory.clear();
//
//                        MyToast.toastLong(SellHospitalActivity.this, res_msg);
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

    void chooserDialog(final String type) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(SellHospitalActivity.this);
        dialog.setTitle(Html.fromHtml("<font color='#2FA49E'>Choose Photo From.</font>"));
        dialog.setMessage("");

        Log.e("dkd", "cropper_dialog");
        dialog.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.e("dkd", "cropper_dialog neg");
                dialog.dismiss();


                if (ActivityCompat.checkSelfPermission(SellHospitalActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SellHospitalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SellHospitalActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PICK_CAMERA_REQUEST);
                    return;
                }

                if (type.equalsIgnoreCase("image")) {
                    try {
                        Util.openCamera(SellHospitalActivity.this, PICK_CAMERA_REQUEST, "image");


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
                if (ActivityCompat.checkSelfPermission(SellHospitalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SellHospitalActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SellHospitalActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PICK_IMAGE_REQUEST);
                }
                if (type.equalsIgnoreCase("image")) {
                    Util.openGallery(SellHospitalActivity.this, PICK_IMAGE_REQUEST, "image");
                }
            }
        });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Log.e("dkd", "onActivity pos");

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
            if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                fileUri = data.getData();
                Log.d("PickedPdfPath: ", fileUri.toString());
                //TODO: Calling the upload here
                if (!fileUri.equals(null)) {
                    getPdfPath();
                }
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

        new MaterialDialog.Builder(SellHospitalActivity.this)
                .title(R.string.croppertitle)
                .customView(view, wrapInScrollView)
                .positiveText(R.string.crop)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        try {
                            bitmap = ThumbnailUtils.extractThumbnail(img.getCroppedImage(), 300, 300);
                            Util.saveImage(bitmap, SellHospitalActivity.this, getString(R.string.app_name));
                            productImg.setImageBitmap(bitmap);
                            saveImage(bitmap, SellHospitalActivity.this, getString(R.string.app_name), "", false);
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
        PostImage post = new PostImage(Util.imageFile, url, fileName, SellHospitalActivity.this, "image") {
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
// "uuid":"S1dea",
//         "category_id":"1",
//         "hospital_category_id":"others_hospital",
//         "others_hospital_name":"test hospital",
//         "country":"IN",
//         "state":"36",
//         "city":"123456",
//         "specific_locality":"Hazarat Ganj",
//         "land_length":"400",
//         "land_width":"300",
//         "total_area":"700",
//         "build_up_area":"900",
//         "primary_type":"2",
//         "licence_left":"Testing",
//         "licence_done":"Testing",
//         "no_of_bed":"80",
//         "price":"101",
//         "description":"May Testing dsgsdg sdgds xdvd",
//         "product_image":"testing.jpg"


    private void addProductApi() throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(SellHospitalActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("category_id", "1");

        if (Category_name.equalsIgnoreCase("Others")) {
            params.put("hospital_category_id", "others_hospital");
            params.put("others_hospital_name", edit_others.getText().toString());
        } else {

            params.put("hospital_category_id", HosId);
            params.put("others_hospital_name", "");
        }
        params.put("practice_category", Cat_PracID);
        params.put("quantity", "");
        if (AddressStr.equalsIgnoreCase("")) {
            params.put("city", city_id);
            params.put("country", country_id);
            params.put("state", state_id);
            params.put("address", edit_street.getText().toString());
//            params.put("city", "");
//            params.put("country", "");
//            params.put("state", "");
//            params.put("address", "");
        } else {
            geoLocate();
            params.put("address", AddressStr);
            params.put("city", city_id);
            params.put("country", country_id);
            params.put("state", state_id);
        }

        params.put("specific_locality", "");
        params.put("contact_phone", edit_contact_ph.getText().toString());
        params.put("contact_email", edit_cont_email.getText().toString());
        params.put("house_street", edit_street.getText().toString());
        params.put("land_length", area_length.getText().toString());
        params.put("land_width", area_width.getText().toString());
        params.put("total_area", edit_tot_arae.getText().toString());
        params.put("build_up_area", edit_built_area.getText().toString() + " " + "square" + category);
        params.put("primary_type", TypeId);
        params.put("licence_left", left_licence.getText().toString());
        params.put("licence_done", done_licence.getText().toString());
        params.put("no_of_bed", no_of_beds.getText().toString());
        params.put("price", edit_Price.getText().toString());
        params.put("description", ed_status.getText().toString());
        params.put("latitude", addressLat);
        params.put("longitude", addressLog);

        // params.put("product_image", "");
        header.put("Cynapse", params);

        new AddHospitalProductApi(SellHospitalActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    if (res_code.equals("1")) {
                        final Dialog dialog = new Dialog(SellHospitalActivity.this);
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
                        //msg.setText(R.string.request_sent);
                        if (Cat_PracID.equalsIgnoreCase("1")) {
                            msg.setText(R.string.buy_req);
                        } else if (Cat_PracID.equalsIgnoreCase("2")) {
                            msg.setText(R.string.sell_req);

                        } else {
                            msg.setText(R.string.lease_req);
                        }
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
//                        MyToast.toastLong(SellHospitalActivity.this, res_msg);
//                        finish();
                    } else {
                        MyToast.toastLong(SellHospitalActivity.this, res_msg);
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

    private void getPdfPath() {
        //TODO: getting the pdf path and pdfFile name
        pdfPath = FilePath.getPath(SellHospitalActivity.this, fileUri);
        Log.d("pdfStringPath :", "<><<" + pdfPath);
        String pdfName = pdfPath.substring(pdfPath.lastIndexOf("/") + 1);
        Log.d("pdfName :", pdfName);
        licenceJpg.setText(pdfName);
        //TODO: calling the pdf upload here
        url = AppConstantClass.HOST + "fileUpload/licence";
        uploadFile(new File(pdfPath), url, pdfName, "file");
    }

    //TODO: used to upload the pdf to the server
    void uploadFile(File file, final String url, String name, String type) {
        Log.e("file_name", ":" + file);
        PostImage post = new PostImage(file, url, name, SellHospitalActivity.this, type) {
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
                        MyToast.toastLong(SellHospitalActivity.this, "Your file have been uploaded successfully");
                    } else {
                        MyToast.toastLong(SellHospitalActivity.this, res1);
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

    public void setArrayList() {
        OthercategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "1");

//        for(int i = 0;i<OthercategoryList.size();i++)
//        {
//            OthercategoryNameList.add(OthercategoryList.get(i).getName());
//
//        }
        // OthercategoryNameList.add("Others");
//        try {
//            hospital_dd.setAdapter(new ArrayAdapter<>(SellHospitalActivity.this,android.R.layout.simple_spinner_dropdown_item,OthercategoryNameList));
//        }
//        catch (NullPointerException ne)
//        {
//            ne.printStackTrace();
//        }
        if (OthercategoryList.size() > 0) {
            HospitalName = new ArrayList<>();
            tempHospital = new ArrayList<>();
            for (int j = 0; j < OthercategoryList.size(); j++) {
                // countryName.add(medicalList.get(j).getProfile_category_name());
                tempHospital.add(OthercategoryList.get(j).getName());
                HospitalName.add(OthercategoryList.get(j).getName());
            }

            hospital_adapter = new Adapter_Filter(SellHospitalActivity.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
            hospital_auto.setAdapter(hospital_adapter);
            hospital_auto.setThreshold(1);
        }

    }

    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(SellHospitalActivity.this, header, false) {
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
}
