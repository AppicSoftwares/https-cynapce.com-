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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.HelperClasses.GetCityClass;
import com.alcanzar.cynapse.HelperClasses.GetCountryClass;
import com.alcanzar.cynapse.HelperClasses.GetStateClass;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.OtherCategoryApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.OtherCategoryModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;
import com.google.android.flexbox.FlexboxLayout;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipDeletedListener;

public class MarketPlaceFilter extends AppCompatActivity implements View.OnClickListener {

    ImageView titleIcon;
    TextView title;
    ArrayList<String> list_categories;
    Spinner categories;
    ArrayList<String> countryListName, stateListName, cityListName, cityChipsNameList, specialityChipsNameList, specialityChipsId, departmentChipsNameList, departmentChipsId, titleChipsId, titleChipsNameList;
    ArrayList<String> tempCountryListName = new ArrayList<>();
    ArrayList<String> tempStateListName = new ArrayList<>();
    ArrayList<String> tempStateListCode = new ArrayList<>();
    ArrayList<String> tempCityListName = new ArrayList<>();
    ArrayList<String> cityChipsIdList = new ArrayList<>();
    AutoCompleteTextView country_auto, state_auto, city_auto, name_auto, medicalProfile_auto, departmentListAuto, Title_auto, Spealization_auto;
    private ArrayList<CountryModel> countryList;
    private ArrayList<StateModel> stateList;
    private ArrayList<CityModel> cityList = new ArrayList<>();
    ArrayAdapter tie_up_for_adapter, seeking_adapter, tie_up_with_adapter, country_adapter, state_adapter, hospital_adapter, name_adapter, reg_state_adapter, city_adapter, adapter_Yos, department_adapter, adapter_title, adapter_specialization;
    String country_id = "", distanceStr = "", title_Id = "", country_str = "", state_id = "", reg_state_id = "", state_Str = "", city_id = "", city_str = "", Yos_ID = "",
            yosStr = "", regStateStr = "", TieUpforStr = "", TieUpFor_id = "", edit_disable = "", medStr, medicalId = "", Cat_prac = "", Cat_PracID = "", splStr = "", specializationId, departmentId, HosStr = "", HosId = "", nameStr = "", nameId, Category_name = "", TieUp_withStr = "", TieUp_withId = "", SeekStr = "", SeekId = "";
    ChipCloud drawableWithCloseChipCloud, flexbox_multiSpecialityCloudChip;
    ChipCloudConfig drawableWithCloseConfig, flexbox_multiSpecialityConfig;
    FlexboxLayout flexboxDrawableWithClose, flexbox_multiSpeciality;
    ArrayList<OtherCategoryModel> OthercategoryList = new ArrayList<>();
    AutoCompleteTextView hospital_auto, seeking_auto, tie_up_with_auto, tieup_for_auto;
    BubbleSeekBar bubbleSeekBar2;
    TextView distanceText;
    ArrayList<String> tempHospital;
    ArrayList<String> HospitalName;
    DatabaseHelper handler;
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    ArrayList<String> specializationName;
    ArrayList<String> tempSplzation;

    ArrayList<DashBoardProductModel> arrayList = new ArrayList<>();
    ArrayList<String> tempNameList = new ArrayList<>();
    ArrayList<String> tempNameListId = new ArrayList<>();
    String marketType = "";
    ArrayList<String> tempTieUpFor;
    ArrayList<String> tempSeeking;
    ArrayList<String> TieUpForName;
    ArrayList<String> SeekingName;
    ArrayList<String> tempTieUpWith;
    ArrayList<String> TieUp_withName;
    ArrayList<OtherCategoryModel> TieUpcategoryList = new ArrayList<>();
    ArrayList<OtherCategoryModel> TypecategoryList = new ArrayList<>();
    RelativeLayout rel_lay_name, rel_lay_type, rel_lay_tie_up_with, rel_lay_hospital, rel_lay_cat, rel_lay_ti_up_for;
    Button filterMarketButton;
    EditText dealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_fragment_filter);

        initialisation();
        setNameData();
        setUpBubbleSeekbar();
        setUpClickListner();
        setUpSeekingFor();
        setUpTieUpWith();
        setHospitalArrayList();
        setUpHospitalListner();

        addItemsOnSpinnerCategories();
        getAllCountry();
        setUpChipsSpecialisation();
        statesetupListener();
        getProfileSpecialization("");
        setUpSpecialization();
        setUpCityListener();
        setUpChips();
    }

    private void setTieUpFor() {

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
                        tie_up_for_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
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
                try {
                    TieUpforStr = tieup_for_auto.getText().toString();
                    TieUpFor_id = OthercategoryList.get(TieUpForName.indexOf(TieUpforStr)).getId();

                    Log.d("TieUpFor_id", TieUpFor_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

                        tie_up_for_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
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
    }

    private void setUpTieUpWith() {

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
                        tie_up_with_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpWith);
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
//                edit_others_three.setText("");
//                if (TieUp_withId.equalsIgnoreCase("-4")) {
//                    other_three_rel_lay.setVisibility(View.VISIBLE);
//
//                } else {
//                    other_three_rel_lay.setVisibility(View.GONE);
//                }
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

                        tie_up_with_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpWith);
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
    }

    private void setUpSeekingFor() {
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
                        seeking_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempSeeking);
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
//                edit_others_two.setText("");
//                if (SeekId.equalsIgnoreCase("-3")) {
//                    other_two_rel_lay.setVisibility(View.VISIBLE);
//
//                } else {
//                    other_two_rel_lay.setVisibility(View.GONE);
//                }
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
                        seeking_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempSeeking);
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
    }

    private void initialisation() {

        if (getIntent() != null) {
            marketType = getIntent().getStringExtra("type");
            Log.e("MarketType", marketType);
        }


        handler = new DatabaseHelper(this);
        titleIcon = findViewById(R.id.titleIcon);
        title = findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        titleIcon.setImageDrawable(getResources().getDrawable(R.drawable.deals_white));

        categories = findViewById(R.id.categories);
        country_auto = findViewById(R.id.country_auto);
        state_auto = findViewById(R.id.state_auto);
        city_auto = findViewById(R.id.city_auto);
        cityChipsNameList = new ArrayList<>();
        bubbleSeekBar2 = findViewById(R.id.demo_4_seek_bar_2);
        flexboxDrawableWithClose = findViewById(R.id.flexbox_drawable_close);
        distanceText = findViewById(R.id.distanceText);
        hospital_auto = findViewById(R.id.hospital_auto);
        Spealization_auto = findViewById(R.id.Spealization_auto);
        flexbox_multiSpeciality = findViewById(R.id.flexbox_multiSpeciality);
        specialityChipsNameList = new ArrayList<>();
        specialityChipsId = new ArrayList<>();
        //Hospital Name

        rel_lay_name = findViewById(R.id.rel_lay_name);
        name_auto = findViewById(R.id.name_auto);
        seeking_auto = findViewById(R.id.seeking_auto);
        tie_up_with_auto = findViewById(R.id.tie_up_with_auto);
        rel_lay_ti_up_for = findViewById(R.id.rel_lay_ti_up_for);

        rel_lay_type = findViewById(R.id.rel_lay_type);
        rel_lay_tie_up_with = findViewById(R.id.rel_lay_tie_up_with);
        rel_lay_hospital = findViewById(R.id.rel_lay_hospital);
        rel_lay_cat = findViewById(R.id.rel_lay_cat);
        filterMarketButton = findViewById(R.id.filterMarketButton);
        tieup_for_auto = findViewById(R.id.tieup_for_auto);

        dealId = findViewById(R.id.dealId);
        try {
            if (!marketType.equals("2"))
                OtherCategoryApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setShowAndHideDataByType(marketType);

//        setColor(titleIcon);
    }

    private void setShowAndHideDataByType(String marketType) {
        int c = Integer.parseInt(marketType);
        switch (c) {

            case 0:
                OthercategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "1");
                Log.e("OthercategoryList", OthercategoryList.size() + "");
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "1", "1");
                title.setText("FILTER HOSPITAL");
                hospital_auto.setHint("Select Hospital Type");
                rel_lay_name.setVisibility(View.GONE);
                rel_lay_type.setVisibility(View.GONE);
                rel_lay_tie_up_with.setVisibility(View.GONE);
                rel_lay_ti_up_for.setVisibility(View.GONE);
                break;
            case 1:

                OthercategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "5");
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "2", "1");
                title.setText("FILTER PRACTICE");
                hospital_auto.setHint("Select Practice Type");
                rel_lay_ti_up_for.setVisibility(View.GONE);
                rel_lay_type.setVisibility(View.GONE);
                rel_lay_tie_up_with.setVisibility(View.GONE);

                break;
            case 2:
                OthercategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "2");
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "3", "1");
                TypecategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "3");
                TieUpcategoryList = handler.getOtherCategory(DatabaseHelper.TABLE_OTHER_CATEGORY_MASTER, "4");

                setTieUpFor();
                rel_lay_hospital.setVisibility(View.GONE);

                rel_lay_cat.setVisibility(View.GONE);

                if (OthercategoryList.size() > 0) {
                    setArrayList1();
                }
                if (TypecategoryList.size() > 0) {
                    setArrayList2();
                }
                if (TieUpcategoryList.size() > 0) {
                    setArrayList3();
                }

                //Log.e("asdf", arrayList.get(0).getCondition());
                title.setText("FILTER TIE UPS");
//                hospital_auto.setHint("Select Tieup for");
                break;
            case 3:
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "4", "1");
                Log.e("asdf", arrayList.get(0).getProduct_name() + "," + arrayList.size());
                title.setText("FILTER EQUIPMENT");
                rel_lay_type.setVisibility(View.GONE);
                rel_lay_tie_up_with.setVisibility(View.GONE);
                rel_lay_hospital.setVisibility(View.GONE);
                Spealization_auto.setHint("Category");
                rel_lay_ti_up_for.setVisibility(View.GONE);

                break;
            case 4:
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "5", "1");
                title.setText("FILTER BOOK & OTHER");
                rel_lay_type.setVisibility(View.GONE);
                rel_lay_tie_up_with.setVisibility(View.GONE);
                rel_lay_hospital.setVisibility(View.GONE);

                rel_lay_ti_up_for.setVisibility(View.GONE);
                break;
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

            tie_up_for_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
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

            seeking_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempSeeking);
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

            tie_up_with_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpWith);
            tie_up_with_auto.setAdapter(tie_up_with_adapter);
            tie_up_with_auto.setThreshold(1);
        }
    }

    private void setNameData() {
        tempNameList.clear();
        tempNameListId.clear();


        if (marketType.equals("1") || marketType.equals("2")) {
            for (int i = 0; i < arrayList.size(); i++) {

                tempNameList.add(arrayList.get(i).getProduct_name());

                Log.e("checkNameHospital", arrayList.get(0).getCondition());
            }
        } else if (marketType.equals("3")) {

            for (int i = 0; i < arrayList.size(); i++) {
                tempNameList.add(arrayList.get(i).getProduct_name());
                Log.e("checkNameHospital", arrayList.get(i).getProduct_name());
            }

        } else if (marketType.equals("0")) {
            for (int i = 0; i < arrayList.size(); i++) {
                tempNameList.add(arrayList.get(i).getFeatured_product());
//              tempNameListId.add(arrayList.get(i).getId());
                Log.e("checkNameHospital", arrayList.get(i).getFeatured_product());

            }
        } else {
            for (int i = 0; i < arrayList.size(); i++) {
                tempNameList.add(arrayList.get(i).getProduct_name());
//              tempNameListId.add(arrayList.get(i).getId());
                Log.e("checkNameHospital", arrayList.get(i).getProduct_name());
            }
        }


        name_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    if (name_auto.getText().toString().equals("")) {

                        try {
                            Log.d("tempplist", tempNameList.toString());

                            name_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempNameList);
                            name_auto.setAdapter(name_adapter);
                            name_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }

                    }


                } else {
                    if (name_auto.toString().equals("")) {
                    } else {
                    }

                }
            }
        });

        name_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nameStr = name_auto.getText().toString();
                nameId = arrayList.get(tempNameList.indexOf(nameStr)).getProduct_id();
//                Category_name = OthercategoryList.get(HospitalName.indexOf(HosStr)).getName();
                Log.d("nameId", nameId);
            }
        });

        name_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {

                        name_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempNameList);
                        name_auto.setAdapter(name_adapter);
                        name_auto.showDropDown();
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


    private void setUpHospitalListner() {


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
                            hospital_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
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
//                Category_name = OthercategoryList.get(HospitalName.indexOf(HosStr)).getName();
                Log.d("HosId", HosId);
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

                        hospital_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
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

    }

    public void setHospitalArrayList() {

        Log.e("checkType", OthercategoryList.size() + "");

        if (OthercategoryList.size() > 0) {

            HospitalName = new ArrayList<>();
            tempHospital = new ArrayList<>();
            for (int j = 0; j < (OthercategoryList.size()); j++) {
                // countryName.add(medicalList.get(j).getProfile_category_name());
                tempHospital.add(OthercategoryList.get(j).getName());
                HospitalName.add(OthercategoryList.get(j).getName());

            }
            tempHospital.remove(tempHospital.size() - 1);
            HospitalName.remove(HospitalName.size() - 1);

            hospital_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
            hospital_auto.setAdapter(hospital_adapter);
            hospital_auto.setThreshold(1);
        }
    }

    private void setUpBubbleSeekbar() {

        bubbleSeekBar2.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
//                String s = String.format(Locale.CHINA, "onChanged int:%d, float:%.1f", progress, progressFloat);
                distanceText.setText("Filter deals by distance (" + progress + ")");
                distanceStr = String.valueOf(progress);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
//                String s = String.format(Locale.CHINA, "onActionUp int:%d, float:%.1f", progress, progressFloat);
                distanceText.setText("Filter deals by distance (" + progress + ")");
                distanceStr = String.valueOf(progress);
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
//                String s = String.format(Locale.CHINA, "onFinally int:%d, float:%.1f", progress, progressFloat);
                distanceText.setText("Filter deals by distance (" + progress + ")");
                distanceStr = String.valueOf(progress);
            }
        });
    }

    private void setUpClickListner() {
        country_auto.setOnClickListener(this);
        state_auto.setOnClickListener(this);
        city_auto.setOnClickListener(this);
        hospital_auto.setOnClickListener(this);
        Spealization_auto.setOnClickListener(this);
        name_auto.setOnClickListener(this);
        seeking_auto.setOnClickListener(this);
        tie_up_with_auto.setOnClickListener(this);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        filterMarketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDataButton(marketType);
            }
        });

        tieup_for_auto.setOnClickListener(this);
    }


    public void addItemsOnSpinnerCategories() {


        list_categories = new ArrayList<>();
        if (marketType.equals("2")) {
            list_categories.add("Select Deal Type");
            list_categories.add("Tie Ups");
        } else {
            list_categories.add("Select Deal Type");
            list_categories.add("Buy");
            list_categories.add("Sell");
            list_categories.add("Lease");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(dataAdapter);

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cat_prac = list_categories.get(i);
                if (Cat_prac.equalsIgnoreCase("Buy")) {
                    Cat_PracID = "1";
                } else if (Cat_prac.equalsIgnoreCase("Sell")) {
                    Cat_PracID = "2";

                } else if (Cat_prac.equalsIgnoreCase("Lease")) {
                    Cat_PracID = "3";
                } else {
                    Cat_PracID = "";
                }
                Log.d("Cat_PracID", Cat_PracID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                        adapter_specialization = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
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

                        adapter_specialization = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
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
                } catch (Exception ao) {
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

    public void setColor(ImageView view) {
        final int newColor = getResources().getColor(R.color.white);
        view.setColorFilter(newColor);
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


                        country_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempCountryListName);
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
                Log.e("textCheck", s.toString());
                if (s.toString().equals("")) {
                    try {
                        Log.e("textCheck2", s.toString());

                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                        country_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempCountryListName);
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

    private void statesetupListener() {
        state_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    try {


                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                        Log.e("focusState", stateListName.size() + "");
                        state_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempStateListName);
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
                        state_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempStateListName);
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
        cityChipsIdList.clear();
        city_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    try {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                        Log.e("focusState", cityListName.size() + "");
                        city_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempCityListName);
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
                        addChipsData(city_str, city_id);
                        city_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempCityListName);
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
                        city_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempCityListName);
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

    private void addChipsData(String citys, String city_id) {
        if (cityChipsNameList.size() < 3) {

            boolean checkBool = true;
            for (int i = 0; i < cityChipsNameList.size(); i++) {
                if (cityChipsNameList.get(i).equals(citys.trim())) {
                    checkBool = false;

                }
            }
            if (checkBool) {
                cityChipsNameList.add(citys);
                cityChipsIdList.add(city_id);
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
                cityChipsIdList.remove(index);
                Log.e("chipsDeletePosion", index + "," + label + "," + cityChipsNameList.size());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.state_auto:
                try {

                    if (state_auto.getText().toString().equals("")) {
                        tempStateListName.clear();
                        tempStateListName.addAll(stateListName);
                    }
                    Log.e("focusState", stateListName.size() + "");
                    state_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempStateListName);
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
                    country_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempCountryListName);
                    country_auto.setAdapter(country_adapter);
                    country_auto.showDropDown();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.name_auto:
                try {
                    try {

                        name_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempNameList);
                        name_auto.setAdapter(name_adapter);
                        name_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }


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

                    city_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_market_place_fragment_filter, R.id.lbl_name, tempCityListName);
                    city_auto.setAdapter(city_adapter);
                    city_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.hospital_auto:
                try {
                    if (hospital_auto.getText().toString().equals("")) {
                        tempHospital = new ArrayList<>();
                        tempHospital.addAll(HospitalName);
                    }
                    hospital_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
                    hospital_auto.setAdapter(hospital_adapter);
                    hospital_auto.showDropDown();
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

                    adapter_specialization = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_my_profile, R.id.lbl_name, tempSplzation);
                    Spealization_auto.setAdapter(adapter_specialization);
                    Spealization_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            case R.id.tieup_for_auto:
                try {
                    if (tieup_for_auto.getText().toString().equals("")) {
                        tempTieUpFor = new ArrayList<>();
                        tempTieUpFor.addAll(TieUpForName);
                    }

                    tie_up_for_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_tie_ups, R.id.lbl_name, tempTieUpFor);
                    tieup_for_auto.setAdapter(tie_up_for_adapter);
                    tieup_for_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                break;
        }
    }


    private void filterDataButton(String marketType) {

        try {

            if (distanceStr.equals("0")) {
                distanceStr = "";
            }


            Log.e("clickFilter", "search");
            if (hospital_auto.getText().equals("")) {
                HosId = "";
            }
            if (name_auto.getText().equals("")) {
                nameId = "";
            }
            if (seeking_auto.getText().equals("")) {
                SeekId = "";
            }
            if (tie_up_with_auto.getText().equals("")) {
                TieUp_withId = "";
            }
            String cityString = cityChipsIdList.toString().replace("[", "").replace("]", "");
            String SpecializationString = specialityChipsId.toString().replace("[", "").replace("]", "");
//            Log.e("marketType",marketType);
//            Log.e("distanceStr",distanceStr);
//            Log.e("dealId",dealId.getText().toString().toString());
//
//            Log.e("nameId",""+nameId);
//            Log.e("cityString",cityString);
//            Log.e("SpecializationString",SpecializationString);
//            Log.e("Cat_PracID",Cat_PracID);

            Intent intent = new Intent();
            intent.putExtra("marketType", "" + marketType);
            intent.putExtra("distanceStr", "" + distanceStr);
            intent.putExtra("dealId", "" + dealId.getText().toString());
            if (marketType.equals(2)) {
                intent.putExtra("HosId", "" + TieUpFor_id);
            } else {
                intent.putExtra("HosId", "" + HosId);
            }
            intent.putExtra("SeekId", SeekId);
            intent.putExtra("TieUp_withId", TieUp_withId);
            intent.putExtra("nameStr", "" + nameStr);
            intent.putExtra("cityString", "" + cityString);
            intent.putExtra("SpecializationString", "" + SpecializationString);
            intent.putExtra("DealType", "" + Cat_PracID);

//            intent.putExtra("hospitalFilter",marketFilterModal);
            setResult(Activity.RESULT_OK, intent);
            finish();


        } catch (Exception e) {

        }
    }


    private void getProfileSpecialization(String id) {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        try {

            params.put("profile_category_id", id);
            header.put("Cynapse", params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetSpecializationApi(MarketPlaceFilter.this, header) {
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

                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MyProfileActivity.this, android.R.layout.simple_spinner_item
//                                , jobSpecializationSpinner);
//                        jobSpecialization.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(MyProfileActivity.this,res_msg);
//                        specializationList.add(new JobSpecializationModel("-3", "1", "Others"));

                        if (specializationList.size() > 0) {

                            specializationName = new ArrayList<>();
                            tempSplzation = new ArrayList<>();
                            for (int j = 0; j < specializationList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempSplzation.add(specializationList.get(j).getSpecialization_name());
                                specializationName.add(specializationList.get(j).getSpecialization_name());
                            }

                            adapter_specialization = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempSplzation);
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
                    AppCustomPreferenceClass.writeString(MarketPlaceFilter.this, AppCustomPreferenceClass.other_cat_sync_time, sync_time);
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

            hospital_adapter = new Adapter_Filter(MarketPlaceFilter.this, R.layout.activity_sell_product_hospital, R.id.lbl_name, tempHospital);
            hospital_auto.setAdapter(hospital_adapter);
            hospital_auto.setThreshold(1);
        }

    }
}
