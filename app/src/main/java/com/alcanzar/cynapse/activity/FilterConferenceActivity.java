package com.alcanzar.cynapse.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.HelperClasses.GetCityClass;
import com.alcanzar.cynapse.HelperClasses.GetCountryClass;
import com.alcanzar.cynapse.HelperClasses.GetStateClass;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.api.ConferenceSearchAPI;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipDeletedListener;

public class FilterConferenceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView title;
    ImageView btnBack, titleIcon, img_auto, img_city, img_state, img_allindia, img_foreign;
    boolean setImg = false;
    TextView btnFilter;
    TextView btn_clear_Filter;
    String cityName = "", stateName = "";
    String y = "", conferenceType_Id="";
    static int pressCheck = 0;
    EditText txt_loc;
    boolean setCondition = false;

    private Spinner categorySpinner;

    private AutoCompleteTextView country_auto, state_auto, city_auto;

    private ArrayList<String> countryListName, cityListName, cityChipsNameList;
    private ArrayList<String> tempCountryListName = new ArrayList<>();
    private ArrayList<String> stateIdAl = new ArrayList<>();
    private ArrayList<String> city_idAl = new ArrayList<>();
    private ArrayList<String> tempStateListCode = new ArrayList<>();
    private ArrayList<String> stateListName = new ArrayList<>();
    private ArrayList<String> tempStateListName = new ArrayList<>();
    private ArrayList<String> tempCityListName = new ArrayList<>();

    private ArrayAdapter country_adapter, state_adapter, city_adapter;

    private ArrayList<CountryModel> countryList;
    private ArrayList<StateModel> stateList;
    private ArrayList<CityModel> cityList = new ArrayList<>();
    private ArrayList<String> stateChipsNameList = new ArrayList<>();

    private ChipCloud drawableWithCloseChipCloud;

    private FlexboxLayout flexboxDrawableWithClose, flexbox_multiSpeciality, flexbox_multiDepartment, flexbox_multiTitle;

    private ChipCloudConfig drawableWithCloseConfig, flexbox_multiSpecialityConfig, flexbox_multiDepartmentConfig, flexbox_multiTitleConfig;


    private FlexboxLayout flexbox_City, flexbox_State;
    private ChipCloudConfig flexbox_CityConfig, flexbox_StateConfig;
    private ChipCloud flexbox_CityChipCloud, flexbox_StateChipCloud;


    private void setUpStateChips() {

        flexbox_StateConfig = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#4AB5B0"))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .showClose(Color.parseColor("#ffffff"), 500);

        flexbox_StateChipCloud = new ChipCloud(this, flexbox_State, flexbox_StateConfig);


        flexbox_StateChipCloud.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {

                stateChipsNameList.remove(index);

                try {
                    stateIdAl.remove(index);
                }
                catch (Exception e)
                {
                }
                Log.e("chipsDeletePosion", index + "," + label + "," + stateChipsNameList.size());
            }
        });
    }


    private void addChipsData2(String citys) {

        if (stateChipsNameList.size() < 3) {
            boolean checkBool = true;
            for (int i = 0; i < stateChipsNameList.size(); i++) {
                if (stateChipsNameList.get(i).equals(citys.trim())) {
                    checkBool = false;
                }
            }
            if (checkBool) {
                stateChipsNameList.add(citys);
                flexbox_StateChipCloud.addChip(citys);
            } else {
                ifAlreadyAdded();
            }
        } else {
            Toast.makeText(this, "More Then 3 not select!", Toast.LENGTH_SHORT).show();
        }

//        if (stateChipsNameList.size()>1)
//        {
//            clearCities();
//        }

        clearCities();
    }

    private void clearCities()
    {
        cityChipsNameList.clear();
        city_idAl.clear();
        flexboxDrawableWithClose.removeAllViews();
        Log.e("chipsDeletePosion##",""+cityChipsNameList.size());
    }

    private void clearState()
    {
        stateChipsNameList.clear();
        stateIdAl.clear();
        flexbox_State.removeAllViews();
        Log.e("clearState##",""+stateChipsNameList.size());
    }


    private boolean isMultiState() {
        boolean b = false;

        if (stateChipsNameList.size() == 1) {
            b = true;
        } else {
            b = false;

            MyToast.toastLong(FilterConferenceActivity.this, "In multiple state scenario ,all cities of selected states are considered");
        }

        return b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_conference);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        img_auto = findViewById(R.id.img_auto);
        img_city = findViewById(R.id.img_city);
        img_state = findViewById(R.id.img_state);
        img_allindia = findViewById(R.id.img_allindia);
        img_foreign = findViewById(R.id.img_foreign);
        btnFilter = findViewById(R.id.btnFilter);
        btn_clear_Filter = findViewById(R.id.btn_clear_Filter);
        // txt_choosecategory = findViewById(R.id.txt_choosecategory);
        txt_loc = findViewById(R.id.txt_loc);
        title = findViewById(R.id.title);
        categorySpinner = findViewById(R.id.categorySpinner);
        country_auto = findViewById(R.id.country_auto);
        state_auto = findViewById(R.id.state_auto);
        city_auto = findViewById(R.id.city_auto);
        flexboxDrawableWithClose = findViewById(R.id.flexbox_drawable_close);
        cityChipsNameList = new ArrayList<>();
        flexbox_State = findViewById(R.id.flexbox_State);

        title.setText("FILTER");

        btnBack.setOnClickListener(this);
        img_auto.setOnClickListener(this);
        img_city.setOnClickListener(this);
        img_state.setOnClickListener(this);
        img_allindia.setOnClickListener(this);
        img_foreign.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btn_clear_Filter.setOnClickListener(this);

        categorySpinner.setOnItemSelectedListener(this);
        country_auto.setOnItemSelectedListener(this);
        state_auto.setOnItemSelectedListener(this);

        if (setImg) {
            img_auto.setImageResource(R.drawable.green_tick);
            img_city.setImageResource(R.drawable.green_tick);
            img_state.setImageResource(R.drawable.green_tick);
            img_allindia.setImageResource(R.drawable.green_tick);
            img_foreign.setImageResource(R.drawable.green_tick);


        } else {
            img_auto.setImageResource(R.drawable.ic_cirlceicn);
            img_city.setImageResource(R.drawable.ic_cirlceicn);
            img_state.setImageResource(R.drawable.ic_cirlceicn);
            img_allindia.setImageResource(R.drawable.ic_cirlceicn);
            img_foreign.setImageResource(R.drawable.ic_cirlceicn);
        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conference_type1, R.layout.conference_type);
        adapter.setDropDownViewResource(R.layout.conference_type);

        categorySpinner.setAdapter(adapter);

        getAllCountry();
        statesetupListener();
        setUpCityListener();
        setUpChips();
        setUpStateChips();
//
//        KeyboardVisibilityEvent.setEventListener(
//                FilterConferenceActivity.this,
//                new KeyboardVisibilityEventListener() {
//                    @Override
//                    public void onVisibilityChanged(boolean isOpen) {
//                        if (isOpen) {
//                            if (country_auto.getText().toString().length() == 0) {
//                                MyToast.toastLong(FilterConferenceActivity.this, "pressed country_auto");
//                            } else if (state_auto.getText().toString().length() == 0) {
//                                MyToast.toastLong(FilterConferenceActivity.this, "pressed state_auto");}
//                            else if (city_auto.getText().toString().length() == 0) {
//                                MyToast.toastLong(FilterConferenceActivity.this, "pressed city_auto");
//                            }
//                        }
//
//                        else
//                        {
//                            MyToast.toastLong(FilterConferenceActivity.this, "pressed city_auto------");
//                        }
//                    }
//
//                });

    }


    private void statesetupListener() {
//        state_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {

//                if (hasFocus) {
//                    try {
//                        tempStateListName.clear();
//                        tempStateListName.addAll(stateListName);
//                        Log.e("focusState", stateListName.size() + "");
//                        state_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_filter_conference, R.id.lbl_name, tempStateListName);
//                        state_auto.setAdapter(state_adapter);
//                        state_auto.showDropDown();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
        state_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tempStateListName.clear();
                    tempStateListName.addAll(stateListName);
                    Log.e("focusState", stateListName.size() + "");
                    state_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_filter_conference, R.id.lbl_name, tempStateListName);
                    state_auto.setAdapter(state_adapter);
                    state_auto.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
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
                    addChipsData2(state_Str);
                    stateIdAl.add(state_id);

                    Log.e("focusStateId", state_id);
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                city_idAl.clear();

                city_auto.setText("");
                state_auto.setText("");
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
                        state_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_filter_conference, R.id.lbl_name, tempStateListName);
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

    private void setUpCityListener() {
        cityChipsNameList.clear();
//        city_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if (hasFocus) {
//
//                    try {
//                        tempCityListName.clear();
//                        tempCityListName.addAll(cityListName);
//                        Log.e("focusState", cityListName.size() + "");
//                        city_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_filter_conference, R.id.lbl_name, tempCityListName);
//                        city_auto.setAdapter(city_adapter);
//                        city_auto.showDropDown();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        city_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMultiState()) {
                    try {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                        Log.e("focusState", cityListName.size() + "");
                        city_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_filter_conference, R.id.lbl_name, tempCityListName);
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
                        city_id_list.add(city_id);
                        city_idAl.add(city_id.trim());
                        city_auto.setText("");
                        addChipsData(city_str);
                        city_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCityListName);
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
                        city_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_filter_conference, R.id.lbl_name, tempCityListName);
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

                try {
                    city_idAl.remove(index);
                }
                catch (Exception e)
                {
                }
                Log.e("chipsDeletePosion", index + "," + label + "," + cityChipsNameList.size());
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

        country_auto.setFocusable(true);

//        country_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if (hasFocus) {
//                    try {
//                        Log.e("focusCountry", countryListName.size() + "");
//
//                        if (country_auto.getText().toString().equals("")) {
//                            tempCountryListName.clear();
//                            tempCountryListName.addAll(countryListName);
//                        }
//
//                        country_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCountryListName);
//                        country_auto.setAdapter(country_adapter);
//                        country_auto.showDropDown();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });

        country_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.e("focusCountry", countryListName.size() + "");

                    if (country_auto.getText().toString().equals("")) {
                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                    }

                    country_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCountryListName);
                    country_auto.setAdapter(country_adapter);
                    country_auto.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        country_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    country_id = countryList.get(countryListName.indexOf(country_auto.getText().toString())).getCountry_code();
                    clearState();
                    clearCities();
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
                Log.e("textCheck2", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("textCheck", s.toString());
                if (s.toString().equals("")) {
                    try {
                        Log.e("textCheck2", s.toString());

                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                        country_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCountryListName);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;

            case R.id.country_auto:
                try {
                    if (country_auto.getText().toString().equals("")) {
                        tempCountryListName.clear();
                        tempCountryListName.addAll(countryListName);
                    }
                    country_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCountryListName);
                    country_auto.setAdapter(country_adapter);
                    country_auto.showDropDown();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.img_auto:
                Log.d("IMDKFKDKDK", setImg + "");
                if (setImg == false) {
                    y = "1";
                    pressCheck = 1;
                    img_auto.setImageResource(R.drawable.green_tick);
                    setImg = true;
                } else {
                    y = "1";
                    img_auto.setImageResource(R.drawable.ic_cirlceicn);
                    setImg = false;
                    pressCheck = 0;
                }
                break;


            case R.id.img_city:

                if (setImg == false) {
                    y = "2";
                    img_city.setImageResource(R.drawable.green_tick);
                    setImg = true;
                    pressCheck = 2;
                    cityName = AppCustomPreferenceClass.readString(FilterConferenceActivity.this, AppCustomPreferenceClass.conference_city, "");
                } else {
                    y = "2";
                    img_city.setImageResource(R.drawable.ic_cirlceicn);
                    setImg = false;
                    pressCheck = 0;
                    cityName = AppCustomPreferenceClass.readString(FilterConferenceActivity.this, AppCustomPreferenceClass.conference_city, "");
                }
                break;

            case R.id.img_state:
                if (setImg == false) {
                    y = "3";
                    img_state.setImageResource(R.drawable.green_tick);
                    setImg = true;
                    pressCheck = 3;
                    stateName = AppCustomPreferenceClass.readString(FilterConferenceActivity.this, AppCustomPreferenceClass.conference_state, "");
                } else {
                    y = "3";
                    img_state.setImageResource(R.drawable.ic_cirlceicn);
                    setImg = false;
                    pressCheck = 0;
                    stateName = AppCustomPreferenceClass.readString(FilterConferenceActivity.this, AppCustomPreferenceClass.conference_state, "");
                }
                break;

            case R.id.img_allindia:
                if (setImg == false) {
                    y = "4";
                    pressCheck = 4;
                    img_allindia.setImageResource(R.drawable.green_tick);
                    setImg = true;
                } else {
                    y = "4";
                    pressCheck = 0;
                    img_allindia.setImageResource(R.drawable.ic_cirlceicn);
                    setImg = false;
                }
                break;

            case R.id.img_foreign:
                if (setImg == false) {
                    y = "5";
                    pressCheck = 5;
                    img_foreign.setImageResource(R.drawable.green_tick);
                    setImg = true;
                } else {
                    y = "5";
                    pressCheck = 0;
                    img_foreign.setImageResource(R.drawable.ic_cirlceicn);
                    setImg = false;
                }
                break;
            case R.id.btn_clear_Filter:
                Intent intent1 = new Intent(FilterConferenceActivity.this, MainActivity.class);
                intent1.putExtra("tabPress", "1");
                intent1.putExtra("filter_show", false);
                startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btnFilter:

                hitApi();

//                Intent intent = new Intent(FilterConferenceActivity.this, MainActivity.class);
//                intent.putExtra("tabPress", "1");
//                intent.putExtra("filter_type", y);
//                intent.putExtra("conference_loc", txt_loc.getText().toString());
//                //intent.putExtra("choosecategory", txt_choosecategory.getText().toString());
//                intent.putExtra("filter_show", true);
//                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                break;
            case R.id.city_auto:
                try {
                    if (city_auto.getText().toString().equals("")) {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityListName);
                    }

                    city_adapter = new Adapter_Filter(FilterConferenceActivity.this, R.layout.activity_filter_conference, R.id.lbl_name, tempCityListName);
                    city_auto.setAdapter(city_adapter);
                    city_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                break;
            default:
                break;
        }
        if (pressCheck == 0) {
            img_auto.setClickable(true);
            img_city.setClickable(true);
            img_allindia.setClickable(true);
            img_state.setClickable(true);
            img_foreign.setClickable(true);

        } else if (pressCheck > 0 && pressCheck <= 1) {
            img_auto.setClickable(true);
            img_city.setClickable(false);
            img_allindia.setClickable(false);
            img_state.setClickable(false);
            img_foreign.setClickable(false);
        } else if (pressCheck > 1 && pressCheck <= 2) {

            img_auto.setClickable(false);
            img_city.setClickable(true);
            img_state.setClickable(false);
            img_allindia.setClickable(false);
            img_foreign.setClickable(false);

        } else if (pressCheck > 2 && pressCheck <= 3) {

            img_auto.setClickable(false);

            img_city.setClickable(false);


            img_allindia.setClickable(false);
            img_state.setClickable(true);
            img_foreign.setClickable(false);

        } else if (pressCheck > 3 && pressCheck <= 4) {

            img_auto.setClickable(false);
            img_city.setClickable(false);

            img_state.setClickable(false);

            img_allindia.setClickable(true);


            img_foreign.setClickable(false);

        } else if (pressCheck > 4 && pressCheck <= 5) {

            img_auto.setClickable(false);

            img_city.setClickable(false);

            img_state.setClickable(false);

            img_allindia.setClickable(false);

            img_foreign.setClickable(true);

        }
        Log.d("VALUEOFAYYY", pressCheck + "");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        if (item.equalsIgnoreCase("Conference")) {
            conferenceType_Id = "1";
        } else if (item.equalsIgnoreCase("Exhibition")) {
            conferenceType_Id = "2";
        } else if (item.equalsIgnoreCase("CME")) {
            conferenceType_Id = "3";
        } else if (item.equalsIgnoreCase("Training Courses")) {
            conferenceType_Id = "4";
        } else if (item.equalsIgnoreCase("Seminar")) {
            conferenceType_Id = "5";
        } else if (item.equalsIgnoreCase("Other")) {
            conferenceType_Id = "other_conference_type";
        } else {

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void hitApi() {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        try {

            if (stateChipsNameList.size()==0)
                stateIdAl.clear();

            if (cityChipsNameList.size()==0)
                city_idAl.clear();

            params.put("uuid", AppCustomPreferenceClass.readString(FilterConferenceActivity.this, AppCustomPreferenceClass.UserId, ""));
            params.put("conference_type", conferenceType_Id);
            params.put("country", country_id);
            params.put("state", stateIdAl.toString().replace("[","").replace("]","").replace(" ",""));
            params.put("city", city_idAl.toString().replace("[","").replace("]","").replace(" ",""));
            //params.put("city", city_id_list.toString().replace("[","").replace("]","").replace(" ",""));
            header.put("Cynapse", params);

            //String str = header.getJSONObject("Cynapse").getString("city").replace(",",":");

            Log.d("resBOb", header.toString());

            new ConferenceSearchAPI(FilterConferenceActivity.this, header) {

                @Override
                public void responseApi(JSONObject response) {
                    super.responseApi(response);
                    JSONObject header = null;
                    try {
                        header = response.getJSONObject("Cynapse");
                        String res_msg = header.getString("res_msg");
                        String res_code = header.getString("res_code");
                        Log.d("RESPONSENOT", response.toString());

                        if (res_code.equals("1")) {
                            Intent intent = new Intent(FilterConferenceActivity.this, MainActivity.class);
                            intent.putExtra("tabPress", "1");
                            //intent.putExtra("filter_type", y);
                            //intent.putExtra("conference_loc", txt_loc.getText().toString());
                            intent.putExtra("fromWhere", "FilterConferenceActivity");
                            intent.putExtra("response", response.toString());
                            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            MyToast.toastLong(FilterConferenceActivity.this, res_msg);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String country_id = "", state_Str = "", state_id = "", city_str = "", city_id = "";
    private ArrayList<String> city_id_list = new ArrayList<>();
}
