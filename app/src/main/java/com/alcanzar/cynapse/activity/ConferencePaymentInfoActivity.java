package com.alcanzar.cynapse.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.adapter.CityListAdapter;
import com.alcanzar.cynapse.adapter.MedicalProfileAdapter;
import com.alcanzar.cynapse.adapter.StateListAdapter;
import com.alcanzar.cynapse.api.GetAllCountryApi;
import com.alcanzar.cynapse.api.GetCityApi;
import com.alcanzar.cynapse.api.GetInfoNotifyApi;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.api.PostPaymentApi;
import com.alcanzar.cynapse.api.PostReviewAmontApi;
import com.alcanzar.cynapse.api.PromoCodeReviewNotiApi;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.ServiceUtility;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.google.android.flexbox.FlexboxLayout;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipDeletedListener;

public class ConferencePaymentInfoActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, MedicalProfileInterface, MedicalTitleInterface, AdapterView.OnItemSelectedListener {

    RadioGroup radioGrp;
    RadioButton radio_with_noti, radio_without_noti;
    String medicalProfileId = "", conference_id = "", count = "", cost = "", payment_mode = "";
    String city_Id = "", state_Id = "", country_Str = "", conferenceType_Id = "", paymentMode_Id = "2", country_str = "", conferenceListDay = "", country_id = "", conferenceListCharges = "",
            medicalProfileID = "", targetAudienceSpeciality = "", targetAudienceSpecialityID = "", departmentID = "", departmentName;

    TextView txt_medical_select, title, txt_add_promocode, txt_noti_amount, txt_sum_details, txt_total_add, txt_add, txt_total_users, txt_noti_cost,
            txt_datepicker, txt_datepicker2, txt_datepicker3, promoCodeAmtTv, target_audience_speciality, target_audience_DepartmentTv2, resetDate1, resetDate2, resetDate3;

    ArrayList<MedicalProfileModel> medical_SpinnerList = new ArrayList<>();
    ArrayList<String> conferenceList = new ArrayList<>();
    ArrayList<String> medicalListId = new ArrayList<>();
    ArrayList<MedicalProfileModel> dept_SpinnerList = new ArrayList<>();
    AutoCompleteTextView txt_country, txt_state, txt_city;

    ArrayList<String> tempcountry = new ArrayList<>();
    ArrayList<String> countryName = new ArrayList<>();
    ArrayList<String> countrySpinner = new ArrayList<>();
    ArrayList<String> stateSpinner = new ArrayList<>();
    ArrayList<String> citySpinner = new ArrayList<>();
    ArrayList<CountryModel> countryList = new ArrayList<>();
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> stateId = new ArrayList<>();
    ArrayList<String> cityId = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();
    ArrayList<String> cityChipsNameList = new ArrayList<>();
    ArrayList<String> stateChipsNameList = new ArrayList<>();
    private ArrayList<String> tempCityListName = new ArrayList<>();
    private ArrayList<String> tempStateListName = new ArrayList<>();
    private ArrayList<String> cityListName = new ArrayList<>();
    private ArrayList<String> stateListName = new ArrayList<>();

    private ArrayList<String> stateIdAl = new ArrayList<>();
    private ArrayList<String> city_idAl = new ArrayList<>();

    ArrayAdapter country_adapter, state_adapter, city_adapter;
    Button btn_get_info;
    LinearLayout lnr_deli_pref, lnr_state, lnr_conf_payment, lnr_per_total, lnr_cost_per_noti, lnr_total_amnt, promoCodeAmtLl, lnr_amountpaid;
    ImageView btnBack;
    String curDate = "", strCurrentDateTo = "", strCurrentDatef = "", frmDate = "", todate = "";
    boolean dateSet = true;
    String prom_price = "", promocode_name = "", promocode_id = "", state_id = "", state_Str = "", city_str = "", city_id = "";
    private Calendar calendar;
    int hint = 0, startDayf, startDayt;
    private EditText accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;
    ArrayList<ArrayList<CityModel>> cityListMain = new ArrayList<>();
    double totalAmount = 0.0, total_amount = 0.0, with_total_amount = 0.0;
    ArrayList<String> tempstate = new ArrayList<>();
    ArrayList<String> tempcity = new ArrayList<>();

    int x = 0;

    private FlexboxLayout flexbox_City, flexbox_State;
    private ChipCloudConfig flexbox_CityConfig, flexbox_StateConfig;
    private ChipCloud flexbox_CityChipCloud, flexbox_StateChipCloud;

    private String dateSelection1 = "";
    private String dateSelection2 = "";
    private String dateSelection3 = "";
    private double totAmountCount = 0.0;

    private void setUpCityChips() {

        flexbox_CityConfig = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#4AB5B0"))
                .uncheckedTextColor(Color.parseColor("#ffffff"))
                .showClose(Color.parseColor("#ffffff"), 500);

        flexbox_CityChipCloud = new ChipCloud(this, flexbox_City, flexbox_CityConfig);


        flexbox_CityChipCloud.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {

                cityChipsNameList.remove(index);

                try {
                    city_idAl.remove(index);
                } catch (Exception e) {
                }

                Log.e("chipsDeletePosion", index + "," + label + "," + cityChipsNameList.size());
            }
        });
    }

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
                } catch (Exception e) {
                }

                Log.e("chipsDeletePosion", index + "," + label + "," + stateChipsNameList.size());
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
                flexbox_CityChipCloud.addChip(citys);
            } else {
                ifAlreadyAdded();
            }
        } else {
            Toast.makeText(this, "More Then 3 not select!", Toast.LENGTH_SHORT).show();
        }
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

        if (stateChipsNameList.size()>1)
        {
            clearCities();
        }
    }

    private void ifAlreadyAdded() {
        Toast.makeText(this, "Name already added!", Toast.LENGTH_SHORT).show();
    }

    private void clearCities()
    {
        cityChipsNameList.clear();
        city_idAl.clear();
        flexbox_City.removeAllViews();
        Log.e("chipsDeletePosion##",""+cityChipsNameList.size());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_payment_info);

        radioGrp = findViewById(R.id.radioGrppayment);

        lnr_per_total = findViewById(R.id.lnr_per_total);
        lnr_cost_per_noti = findViewById(R.id.lnr_cost_per_noti);
        lnr_conf_payment = findViewById(R.id.lnr_conf_payment);
        radio_with_noti = findViewById(R.id.radio_with_noti);
        lnr_total_amnt = findViewById(R.id.lnr_total_amnt);
        radio_without_noti = findViewById(R.id.radio_without_noti);
        txt_medical_select = findViewById(R.id.txt_medical_select);
        txt_total_add = findViewById(R.id.txt_total_add);
        txt_sum_details = findViewById(R.id.txt_sum_details);
        lnr_state = findViewById(R.id.lnr_state);
        txt_country = findViewById(R.id.txt_country);
        txt_total_users = findViewById(R.id.txt_total_users);
        txt_country.setOnClickListener(this);
        txt_state = findViewById(R.id.txt_state);
        txt_state.setOnClickListener(this);
        txt_country = findViewById(R.id.txt_country);
        txt_country.setOnClickListener(this);
        lnr_deli_pref = findViewById(R.id.lnr_deli_pref);
        btn_get_info = findViewById(R.id.btn_get_info);
        txt_add_promocode = findViewById(R.id.txt_add_promocode);
        txt_noti_amount = findViewById(R.id.txt_noti_amount);
        txt_datepicker = findViewById(R.id.txt_datepicker);
        txt_datepicker2 = findViewById(R.id.txt_datepicker2);
        txt_datepicker3 = findViewById(R.id.txt_datepicker3);
        promoCodeAmtLl = findViewById(R.id.promoCodeAmtLl);
        lnr_amountpaid = findViewById(R.id.lnr_amountpaid);
        promoCodeAmtTv = findViewById(R.id.promoCodeAmtTv);
        txt_datepicker.setOnClickListener(this);
        txt_datepicker2.setOnClickListener(this);
        txt_datepicker3.setOnClickListener(this);
        txt_add = findViewById(R.id.txt_add);
        btnBack = findViewById(R.id.btnBack);
        txt_city = findViewById(R.id.txt_city);
        txt_city.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        txt_sum_details.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("Review Notification");
        txt_noti_cost = findViewById(R.id.txt_noti_cost);
        txt_city.setOnClickListener(this);
        txt_state.setOnClickListener(this);
        accessCode = (EditText) findViewById(R.id.accessCode);
        merchantId = (EditText) findViewById(R.id.merchantId);
        orderId = (EditText) findViewById(R.id.orderId);
        currency = (EditText) findViewById(R.id.currency);
        amount = (EditText) findViewById(R.id.amount);
        rsaKeyUrl = (EditText) findViewById(R.id.rsaUrl);
        redirectUrl = (EditText) findViewById(R.id.redirectUrl);
        cancelUrl = (EditText) findViewById(R.id.cancelUrl);

        flexbox_City = findViewById(R.id.flexbox_City);
        flexbox_State = findViewById(R.id.flexbox_State);
        target_audience_speciality = findViewById(R.id.target_audience_speciality);
        target_audience_DepartmentTv2 = findViewById(R.id.target_audience_DepartmentTv2);
        resetDate1 = findViewById(R.id.resetDate1);
        resetDate2 = findViewById(R.id.resetDate2);
        resetDate3 = findViewById(R.id.resetDate3);


        calendar = Calendar.getInstance();


        setUpCityChips();
        setUpStateChips();

        if (getIntent() != null) {
            conference_id = getIntent().getStringExtra("conference_id");
            total_amount = Double.parseDouble(getIntent().getStringExtra("total_amount"));
            with_total_amount = Double.parseDouble(getIntent().getStringExtra("with_total_amount"));
            payment_mode = getIntent().getStringExtra("payment_mode");
            todate = getIntent().getStringExtra("toDate");
            Log.d("todate", todate);
            frmDate = getIntent().getStringExtra("frmDate");
            medicalProfileID = getIntent().getStringExtra("medicalProfileID");
            departmentID = getIntent().getStringExtra("departmentID");
            targetAudienceSpecialityID = getIntent().getStringExtra("targetAudienceSpecialityID");

            txt_medical_select.setText(getIntent().getStringExtra("medicalProfile").substring(1, getIntent().getStringExtra("medicalProfile").length() - 1));
            target_audience_speciality.setText(getIntent().getStringExtra("targetAudienceSpeciality").substring(1, getIntent().getStringExtra("targetAudienceSpeciality").length() - 1));
            target_audience_DepartmentTv2.setText(getIntent().getStringExtra("departmentName").substring(1, getIntent().getStringExtra("departmentName").length() - 1));
        }

        Log.d("PAYMENTMODE", payment_mode);


        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId.setText(randomNum.toString());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        final Date date = cal.getTime();
        final SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

//        txt_datepicker.setText(format1.format(date));
//        txt_datepicker2.setText(format1.format(date));
//        txt_datepicker3.setText(format1.format(date));

        if (radio_without_noti.isChecked()) {
            lnr_deli_pref.setVisibility(View.GONE);
            promoCodeAmtLl.setVisibility(View.GONE);
            lnr_conf_payment.setVisibility(View.VISIBLE);
            radio_with_noti.setChecked(false);
            txt_add_promocode.setText("");
            lnr_per_total.setVisibility(View.GONE);
            lnr_cost_per_noti.setVisibility(View.GONE);
            txt_noti_amount.setText(Util.form.format(total_amount));
            txt_total_add.setText(Util.form.format(total_amount));
            findViewById(R.id.tvt).setVisibility(View.GONE);
        } else if (radio_with_noti.isChecked()) {
            radio_without_noti.setChecked(false);
            lnr_deli_pref.setVisibility(View.VISIBLE);
            promoCodeAmtLl.setVisibility(View.GONE);
            txt_add_promocode.setText("");
            txt_noti_amount.setText("");
            txt_total_add.setText("");
        }

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_without_noti) {
                    paymentMode_Id = "2";
                    lnr_deli_pref.setVisibility(View.GONE);
                    lnr_conf_payment.setVisibility(View.VISIBLE);
                    radio_with_noti.setChecked(false);
                    txt_add_promocode.setText("");
                    lnr_per_total.setVisibility(View.GONE);
                    lnr_cost_per_noti.setVisibility(View.GONE);
                    Log.d("paymentidd11", paymentMode_Id);
                    txt_noti_amount.setText(Util.form.format(total_amount));
                    txt_total_add.setText(Util.form.format(total_amount));
                    findViewById(R.id.lnr_select_date).setVisibility(View.GONE);
                    lnr_amountpaid.setVisibility(View.VISIBLE);
                    promoCodeAmtLl.setVisibility(View.GONE);
                    promoCodeAmtTv.setText("");
                    findViewById(R.id.tvt).setVisibility(View.GONE);
                }
                else if (checkedId == R.id.radio_with_noti) {
                    promoCodeAmtLl.setVisibility(View.GONE);
                    findViewById(R.id.tvt).setVisibility(View.VISIBLE);
                    paymentMode_Id = "1";
                    promoCodeAmtTv.setText("");
                    radio_without_noti.setChecked(false);
                    lnr_deli_pref.setVisibility(View.VISIBLE);
                    txt_add_promocode.setText("");
                    Log.d("paymentidd22", paymentMode_Id);
                    txt_noti_amount.setText(Util.form.format(with_total_amount));
                    txt_total_add.setText("");
                    lnr_amountpaid.setVisibility(View.GONE);
                    findViewById(R.id.lnr_select_date).setVisibility(View.VISIBLE);
                }
            }
        });

        try {
            getMedicalProfileApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getCountyApi();
        } catch (Exception e) {
            e.printStackTrace();
        }


        txt_medical_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialogMedicalProfile();
            }
        });

        resetDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDate1.setVisibility(View.GONE);
                //txt_datepicker.setText(format1.format(date));
                txt_datepicker.setText(getString(R.string.select_date));
                removeDateSelectionAL(0);

                if (lnr_amountpaid.getVisibility()==View.VISIBLE) {
                    try {
                        GetInoNofiApi();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        resetDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDate2.setVisibility(View.GONE);
                //txt_datepicker2.setText(format1.format(date));
                txt_datepicker2.setText(getString(R.string.select_date));
                removeDateSelectionAL(1);

                if (lnr_amountpaid.getVisibility()==View.VISIBLE) {
                    try {
                        GetInoNofiApi();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        resetDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDate3.setVisibility(View.GONE);
                //txt_datepicker3.setText(format1.format(date));
                txt_datepicker3.setText(getString(R.string.select_date));
                removeDateSelectionAL(2);

                if (lnr_amountpaid.getVisibility()==View.VISIBLE) {
                    try {
                        GetInoNofiApi();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//
//        txt_state.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setStateListDilog();
//
//            }
//        });

        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (paymentMode_Id.equals("1")) {
                        if (lnr_amountpaid.getVisibility() == View.VISIBLE) {
                            GetPromocodeApi_(txt_add_promocode.getText().toString());
                        } else {
                            Toast.makeText(ConferencePaymentInfoActivity.this, "Amount is blank", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        GetPromocodeApi_(txt_add_promocode.getText().toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lnr_conf_payment.setVisibility(View.VISIBLE);
                //lnr_per_total.setVisibility(View.VISIBLE);
                // lnr_cost_per_noti.setVisibility(View.VISIBLE);

                if (txt_datepicker.getText().toString().equals(getString(R.string.select_date))
                && txt_datepicker2.getText().toString().equals(getString(R.string.select_date))
                && txt_datepicker3.getText().toString().equals(getString(R.string.select_date)))
                {
                    MyToast.toastLong(ConferencePaymentInfoActivity.this,"At least one date should be selected");
                    return;
                }

                try {
                    GetInoNofiApi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txt_country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    if (txt_country.getText().toString().equals("")) {
                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);
                    }
                    country_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcountry);
                    txt_country.setAdapter(country_adapter);
                    txt_country.showDropDown();

                } else {

                }
            }
        });

        txt_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country_str = txt_country.getText().toString();
                country_id = countryList.get(countryName.indexOf(country_str)).getCountry_code();
                Log.d("COUNTRYIDSF", country_id);

                try {
                    getStateApi(country_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("medicalId", country_id);
            }
        });

        txt_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().equals("")) {

                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);

                        country_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcountry);
                        txt_country.setAdapter(country_adapter);

                        //  crossC.setVisibility(View.GONE);
                    } else {
                        // crossC.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        txt_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tempstate.clear();
                    tempstate.addAll(stateName);
                    Log.e("focusState", cityListName.size() + "");
                    state_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_conference_payment_info, R.id.lbl_name, tempstate);
                    txt_state.setAdapter(state_adapter);
                    txt_state.showDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txt_state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    if (txt_state.getText().toString().equals("")) {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);
                    }
                    state_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_conference_payment_info, R.id.lbl_name, tempstate);
                    txt_state.setAdapter(state_adapter);
                    txt_state.showDropDown();

                } else {
                }
            }
        });

        txt_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                state_Str = txt_state.getText().toString();
                state_id = stateList.get(stateName.indexOf(state_Str)).getState_code();
                stateIdAl.add(state_id);
                country_id = stateList.get(stateName.indexOf(state_Str)).getCountry_code();

                addChipsData2(state_Str);

//                for (int i = 0; i < cityChipsNameList.size(); i++) {
//                    cityChipsNameList.remove(i);
//                }

                txt_state.setText("");
                city_idAl.clear();

                try {
                    getCityApi(country_id, state_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Countruy_IDDDD", country_id);
            }
        });

        txt_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().equals("")) {

                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);

                        state_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_conference_payment_info, R.id.lbl_name, tempstate);
                        txt_state.setAdapter(state_adapter);
                        // crossS.setVisibility(View.GONE);
                    } else {
                        // crossS.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txt_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMultiState()) {
                    try {
                        tempCityListName.clear();
                        tempCityListName.addAll(cityName);
                        Log.e("focusState", cityName.size() + "");
                        city_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_filter_conference, R.id.lbl_name, tempCityListName);
                        txt_city.setAdapter(city_adapter);
                        txt_city.showDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        txt_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (isMultiState()) {
                        Log.e("in ", "Country");
                        if (txt_city.getText().toString().equals("")) {
                            tempcity = new ArrayList<>();
                            try {
                                tempcity.addAll(cityName);
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            }
                        }
                        city_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_conference_payment_info, R.id.lbl_name, tempcity);
                        txt_city.setAdapter(city_adapter);
                        txt_city.showDropDown();
                    }
                } else {

                }
            }
        });

        txt_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city_str = txt_city.getText().toString();
                city_id = cityList.get(cityName.indexOf(city_str)).getCity_id();
                city_idAl.add(city_id.trim());
                txt_city.setText("");
                addChipsData(city_str);
                city_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_job_requirement_filter, R.id.lbl_name, tempCityListName);
                txt_city.setAdapter(city_adapter);
                txt_city.showDropDown();

                Log.d("city_id_of_state", city_id);
            }
        });


        txt_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().equals("")) {
                        tempcity = new ArrayList<>();
                        tempcity.addAll(cityName);

                        city_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_conference_payment_info, R.id.lbl_name, tempcity);
                        txt_city.setAdapter(city_adapter);
                        // crossCity.setVisibility(View.GONE);
                    } else {
                        // crossCity.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean isMultiState() {
        boolean b = false;

        if (stateChipsNameList.size() == 1) {
            b = true;
        } else {
            b = false;

            MyToast.toastLong(ConferencePaymentInfoActivity.this, "In multiple state scenario ,all cities of selected states are considered");
        }

        return b;
    }

    public void showDialogMedicalProfile() {
        final RecyclerView multi_city_sel_recycler;
        EditText loc_search;
        LinearLayoutManager linearLayoutManager;
        TextView cancel_txt, done_txt;
        final MedicalProfileAdapter menu_recycler_adapter;

        final Dialog dialog = new Dialog(ConferencePaymentInfoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multi_medical_selection_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        multi_city_sel_recycler = dialog.findViewById(R.id.multi_city_sel_recycler);
        cancel_txt = dialog.findViewById(R.id.cancel_txt);
        done_txt = dialog.findViewById(R.id.done_txt);
        loc_search = dialog.findViewById(R.id.loc_search);
        linearLayoutManager = new LinearLayoutManager(ConferencePaymentInfoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_city_sel_recycler.setLayoutManager(linearLayoutManager);


        menu_recycler_adapter = new MedicalProfileAdapter(medical_SpinnerList, ConferencePaymentInfoActivity.this, ConferencePaymentInfoActivity.this);
        multi_city_sel_recycler.setAdapter(menu_recycler_adapter);
        loc_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

                conferenceList.clear();
                medicalListId.clear();
//              medicalProfileId = " ";


                for (int i = 0; i < medical_SpinnerList.size(); i++)

                    if (medical_SpinnerList.get(i).getStatus()) {

                        conferenceList.add(medical_SpinnerList.get(i).getProfile_category_name());
                        medicalListId.add(medical_SpinnerList.get(i).getId().trim());

                    }


                medicalProfileId = medicalListId.toString().replace("[", "").replace("]", "").replaceAll(" ", "");
                medicalProfileId = medicalProfileId.trim();
                Log.d("EDICALPROFILEID", medicalProfileId + "");

                txt_medical_select.setText(conferenceList.toString().replace("[", "").replace("]", ""));

                dialog.dismiss();

            }
        });
    }

    private void getMedicalProfileApi() {
        new GetMedicalProfileApi(ConferencePaymentInfoActivity.this) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    Log.d("RESPONSEMEDICAL", response.toString());
                    if (res_code.equals("1")) {

                        dept_SpinnerList.clear();
                        //MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("ProfileCategoryMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            MedicalProfileModel medicalProfileModel = new MedicalProfileModel();
                            medicalProfileModel.setId(item.getString("id"));
                            medicalProfileModel.setProfile_category_name(item.getString("profile_category_name"));
                            dept_SpinnerList.add(medicalProfileModel);
                            medical_SpinnerList = getModel(false);
                            Log.d("CATEGORYNAMEEE", medical_SpinnerList.get(i).getId());

                        }
                        dept_SpinnerList.clear();

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

    private void getDatePicker(final TextView txt_datepicker, final int dateSelectionPos) {
        Calendar cs = Calendar.getInstance();
        int dayOfMonths = cs.get(Calendar.DAY_OF_MONTH);
        int monthOfYears = cs.get(Calendar.MONTH);
        int years = cs.get(Calendar.YEAR);
        dateSet = false;

        String parts3[] = todate.split("-");
        endDay = Integer.parseInt(parts3[0]);
        endMonth = Integer.parseInt(parts3[1]);
        endYear = Integer.parseInt(parts3[2]);

        //TODO: systemCurrent Date
        curDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
        final SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dialog1 = new DatePickerDialog(ConferencePaymentInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //TODO: calendar for comparison
                strCurrentDatef = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                String parts1[] = strCurrentDatef.split("-");
                String parts2[] = curDate.split("-");
                //String parts3[] = todate.split("-");
                //String parts4[] = frmDate.split("-");

                Log.e("date", curDate + "newDateee" + strCurrentDatef);

                //int endDay = Integer.parseInt(parts2[0]);
                //int endMonth = Integer.parseInt(parts2[1]);
                // int endYear = Integer.parseInt(parts2[2]);

                startDayf = Integer.parseInt(parts1[0]);
                int startMonth = Integer.parseInt(parts1[1]);
                int startYear = Integer.parseInt(parts1[2]);

//                int frmStartDay = Integer.parseInt(parts4[0]);
//                int frmStartMonth = Integer.parseInt(parts4[1]);
//                int frmStartYear = Integer.parseInt(parts4[2]);

//                 endDay = Integer.parseInt(parts3[0]);
//                 endMonth = Integer.parseInt(parts3[1]);
//                 endYear = Integer.parseInt(parts3[2]);

                Log.d("resbbb2", startDayf + "-" + startMonth + "-" + startYear);
                Log.d("resbbb", endDay + "-" + startMonth + "-" + endYear);

//============================================================================
//                        else {
//                            if (startYear == endYear) {
//                                if (startMonth < endMonth) {
//                                    Toast.makeText(ConferencePaymentInfoActivity.this, "Past date cannot be selected..", Toast.LENGTH_SHORT).show();
//                                    return;
//                                } else {
//                                    if (startMonth == endMonth) {
//                                        if (startDayf < endDay) {
//                                            Toast.makeText(ConferencePaymentInfoActivity.this, "Past date cannot be selected...", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//                                    }
//                                }
//                            }
//                        }
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                Date current = newDate.getTime();

                // Log.d("currentDate",current.toString());
                int diff = new Date().compareTo(current);
                txt_datepicker.setText(simpleDateFormats.format(newDate.getTime()));

                if (dateSelectionPos == 0) {
                    dateSelection1 = txt_datepicker.getText().toString();
                    resetDate1.setVisibility(View.VISIBLE);

                    if (lnr_amountpaid.getVisibility()==View.VISIBLE) {
                        try {
                            GetInoNofiApi();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (dateSelectionPos == 1) {
                    dateSelection2 = txt_datepicker.getText().toString();
                    resetDate2.setVisibility(View.VISIBLE);

                    if (lnr_amountpaid.getVisibility()==View.VISIBLE) {
                        try {
                            GetInoNofiApi();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                if (dateSelectionPos == 2) {
                    dateSelection3 = txt_datepicker.getText().toString();
                    resetDate3.setVisibility(View.VISIBLE);

                    if (lnr_amountpaid.getVisibility()==View.VISIBLE) {
                        try {
                            GetInoNofiApi();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                Utils.sop("dateSelectionAl-" + dateSelection1 + " " + dateSelection2 + " " + dateSelection3);

            }
        }, years, monthOfYears, dayOfMonths);

        Calendar c = Calendar.getInstance();
        c.set(endYear, endMonth-1, endDay);//Year,Mounth -1,Day
        dialog1.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog1.show();
    }

    private void removeDateSelectionAL(int dateSelectionPos) {

        if (dateSelectionPos == 0)
            dateSelection1 = "";
        if (dateSelectionPos == 1)
            dateSelection2 = "";
        if (dateSelectionPos == 2)
            dateSelection3 = "";

        Utils.sop("dateSelectionAl" + dateSelection1 + " " + dateSelection2 + " " + dateSelection3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_datepicker:
                getDatePicker(txt_datepicker, 0);
                break;
            case R.id.txt_datepicker2:
                getDatePicker(txt_datepicker2, 1);
                break;
            case R.id.txt_datepicker3:
                getDatePicker(txt_datepicker3, 2);
                break;
            case R.id.txt_sum_details:
                try {
                    if (!TextUtils.isEmpty(txt_total_add.getText().toString())) {
                        try {
                            postReviewAmountApi();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        MyToast.toastLong(ConferencePaymentInfoActivity.this, "Please select parameters to send notification as the amount should not be blank");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

//            case R.id.txt_city:
//                showAlertDialogCity();
//                break;

            case R.id.btnBack:
                finish();
                break;
            case R.id.txt_country:
                try {
                    if (txt_country.getText().toString().equals("")) {
                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);
                    }
                    country_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcountry);
                    txt_country.setAdapter(country_adapter);
                    txt_country.showDropDown();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void selectedTitles(ArrayList<String> titleList) {

    }

    private ArrayList<MedicalProfileModel> getModel(boolean isSelect) {
        ArrayList<MedicalProfileModel> list = new ArrayList<>();
        for (int i = 0; i < dept_SpinnerList.size(); i++) {

            MedicalProfileModel model = new MedicalProfileModel();
            model.setStatus(isSelect);
            model.setProfile_category_name(dept_SpinnerList.get(i).getProfile_category_name());
            model.setId(dept_SpinnerList.get(i).getId());
            list.add(model);
        }
        return list;
    }

    private void getCountyApi() {
        new GetAllCountryApi(ConferencePaymentInfoActivity.this) {
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

                        if (countryList.size() > 0) {

                            countryName = new ArrayList<>();
                            tempcountry = new ArrayList<>();
                            for (int j = 0; j < countryList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempcountry.add(countryList.get(j).getCountry_name());
                                countryName.add(countryList.get(j).getCountry_name());
                            }
                            Log.d("TEMCOUNTRYYYY", tempcountry + "");
                            country_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcountry);
                            txt_country.setAdapter(country_adapter);
                            txt_country.setThreshold(1);
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

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getStateApi(String countryId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", countryId);
        header.put("Cynapse", params);
        new GetStateApi(ConferencePaymentInfoActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSESTRING", response.toString());
                    if (res_code.equals("1")) {

                        stateSpinner.clear();
                        stateList.clear();
                        cityList.clear();
                        citySpinner.clear();
                        //MyToast.toastLong(getActivity(), res_msg);
                        JSONArray header2 = header.getJSONArray("State");
//                        for (int i = 0; i < header2.length(); i++) {
//                            JSONObject item = header2.getJSONObject(i);
//                            stateList.add(new StateModel(item.getString("country_code"), item.getString("state_code"), item.getString("state_name")));
//                        }
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

                            state_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_conference_payment_info, R.id.lbl_name, tempstate);
                            txt_state.setAdapter(state_adapter);
                            txt_state.setThreshold(1);
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

    public void setStateListDilog() {
        final RecyclerView multi_city_sel_recycler;

        LinearLayoutManager linearLayoutManager;
        TextView cancel_txt, done_txt;
        final StateListAdapter menu_recycler_adapter;

        final Dialog dialog = new Dialog(ConferencePaymentInfoActivity.this);
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

        linearLayoutManager = new LinearLayoutManager(ConferencePaymentInfoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_city_sel_recycler.setLayoutManager(linearLayoutManager);
//

        menu_recycler_adapter = new StateListAdapter(stateList, ConferencePaymentInfoActivity.this);
        multi_city_sel_recycler.setAdapter(menu_recycler_adapter);


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
                stateName.clear();
                stateId.clear();
                for (int i = 0; i < AppConstantClass.statelistMain.size(); i++) {
                    if (AppConstantClass.statelistMain.get(i).isSelectedCheck()) {
                        stateName.add(AppConstantClass.statelistMain.get(i).getState_name());
                        stateId.add(AppConstantClass.statelistMain.get(i).getState_code());
                    }
                }
                state_Id = stateId.toString().replace("[", "").replace("]", "");
                txt_state.setText(stateName.toString().replace("[", "").replace("]", ""));
                Log.d("STATEIDDDDD", state_Id);

//                medicalProfileId="";
//                for (int i = 0; i < medical_SpinnerList.size(); i++)
//
//                    if (medical_SpinnerList.get(i).getStatus()) {
//
//                        conferenceList.add(medical_SpinnerList.get(i).getProfile_category_name());
//                        medicalListId.add(medical_SpinnerList.get(i).getId());
//
//                    }
//
//                medicalProfileId = medicalListId.toString().replace("[", "").replace("]", "");
//                Log.d("EDICALPROFILEID", medicalProfileId + "");
//                txt_medical_select.setText(conferenceList.toString().replace("[", "").replace("]", ""));

                dialog.dismiss();

                try {
                    if (stateId.size() > 0) {
                        //  cityListMain = new ArrayList<>();
                        for (int i = 0; i < stateId.size(); i++) {

                            getCityApi(country_id, stateId.get(i));

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void GetPromocodeApi_(String promcode) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ConferencePaymentInfoActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("promo_code", promcode);
        params.put("conference_id", conference_id);

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new PromoCodeReviewNotiApi(ConferencePaymentInfoActivity.this, header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    Log.d("Promocodenew", response.toString());

                    if (res_code.equals("1")) {
                        x = 1;
                        Float promoCodePercnt = Float.parseFloat(header.getJSONObject("promocode").optString("percentage"));
                        promocode_id = header.getJSONObject("promocode").optString("promocode_id");
                        Float amt = Float.parseFloat(txt_noti_amount.getText().toString());
                        //Float couponDiscount = Float.parseFloat(coup.getText().toString());


                        Float totAmt = amt * promoCodePercnt / 100;
                        //couponDiscountTv.setText("" + totAmt);
                        Float finalAmt = amt - totAmt;
                        if (String.valueOf(finalAmt).contains("-")) {
                            Toast.makeText(ConferencePaymentInfoActivity.this, "Invalid Entry", Toast.LENGTH_LONG).show();
                        } else {
                            //txt_noti_amount.setText("" + finalAmt);
                            promoCodeAmtLl.setVisibility(View.VISIBLE);
                            promoCodeAmtTv.setText(Util.form.format(totAmt));
                            lnr_total_amnt.setVisibility(View.VISIBLE);
                            txt_total_add.setText(Util.form.format(finalAmt));
                        }
                    } else {
                        MyToast.toastLong(ConferencePaymentInfoActivity.this, res_msg);
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

    public void GetPromocodeApi(String promcode) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ConferencePaymentInfoActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("promo_code", promcode);
        params.put("conference_id", conference_id);

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new PromoCodeReviewNotiApi(ConferencePaymentInfoActivity.this, header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    Log.d("Promocodenew", response.toString());
                    if (res_code.equals("1")) {
                        x = 1;
                        //MyToast.toastShort(this,res_msg);
                        JSONObject jsonObject = header.getJSONObject("promocode");
                        // prom_price = jsonObject.getString("price");
                        promocode_name = jsonObject.getString("promocode_name");
                        promocode_id = jsonObject.getString("promocode_id");
                        lnr_total_amnt.setVisibility(View.VISIBLE);
                        Log.d("TOTALCTCAOMT3333", prom_price + "");

                        float tp = Float.parseFloat(txt_noti_amount.getText().toString());
                        float pp = Float.parseFloat(prom_price);
                        if (!(tp < pp)) {

                            //  txt_taxamnt.setText("RS " + prom_price);
                            tp = tp - pp;
                            txt_total_add.setText(tp + "");

                        } else {
                            txt_total_add.setText(String.valueOf(tp) + "");
                            Toast.makeText(ConferencePaymentInfoActivity.this, "Promocode cannot be applied as its price is greater than total amount", Toast.LENGTH_SHORT).show();
                        }
                        //txt_add.setText("");

                    } else {

                        MyToast.toastLong(ConferencePaymentInfoActivity.this, res_msg);
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
        cityList.clear();

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", stateCountryId);
        params.put("state_code", stateId);
        header.put("Cynapse", params);
        new GetCityApi(ConferencePaymentInfoActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("CITYRESPONSE", response.toString());
                    if (res_code.equals("1")) {
                        JSONArray header2 = header.getJSONArray("City");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            cityList.add(new CityModel(item.getString("city_id"), item.getString("country_code"), item.getString("state_code"), item.getString("city_name")));
                            cityName.add(item.getString("city_name"));
                            Log.d("CITYNAMEWIL", item.getString("city_name"));
                        }
                        if (cityList.size() > 0) {

                            cityName = new ArrayList<>();
                            tempcity = new ArrayList<>();

                            for (int j = 0; j < cityList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempcity.add(cityList.get(j).getCity_name());
                                cityName.add(cityList.get(j).getCity_name());
                            }

                            city_adapter = new Adapter_Filter(ConferencePaymentInfoActivity.this, R.layout.activity_conference_payment_info, R.id.lbl_name, tempcity);

                            txt_city.setAdapter(city_adapter);
                            txt_city.setThreshold(1);
                        }
//                        JSONArray header2 = header.getJSONArray("City");
//                        for (int i = 0; i < header2.length(); i++) {
//                            JSONObject item = header2.getJSONObject(i);
//                            cityList.add(new CityModel(item.getString("city_id"),
//                                    item.getString("country_code"),
//                                    item.getString("state_code"),
//                                    item.getString("city_name")));
//
//                            Log.d("CITYNAMEWIL", item.getString("city_name") + "," + cityListMain.size());
//                        }
//                        cityListMain.add(cityList);
//                        if (cityList.size() > 0) {
//
//                            cityName = new ArrayList<>();
//                            tempcity = new ArrayList<>();
//                            for (int j = 0; j < cityList.size(); j++) {
//                                // countryName.add(medicalList.get(j).getProfile_category_name());
//                                tempcity.add(cityList.get(j).getCity_name());
//                                cityName.add(cityList.get(j).getCity_name());
//                            }
//
//                            city_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcity);
//
//                            txt_city.setAdapter(city_adapter);
//                            txt_city.setThreshold(1);
//                        }
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

    private void showAlertDialogCity() {
        final RecyclerView multi_city_sel_recycler;

        LinearLayoutManager linearLayoutManager;
        TextView cancel_txt, done_txt;
        final StateListAdapter menu_recycler_adapter;

        final Dialog dialogA = new Dialog(ConferencePaymentInfoActivity.this);
        dialogA.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogA.setContentView(R.layout.multi_city_selection_dialog);
        //TODO: used to make the background transparent
        Window window = dialogA.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialogA.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        multi_city_sel_recycler = dialogA.findViewById(R.id.multi_city_sel_recycler);
        cancel_txt = dialogA.findViewById(R.id.cancel_txt);
        done_txt = dialogA.findViewById(R.id.done_txt);

        linearLayoutManager = new LinearLayoutManager(ConferencePaymentInfoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_city_sel_recycler.setLayoutManager(linearLayoutManager);

        if (cityListMain.size() == 0) {
            Toast.makeText(getApplicationContext(), "City list not found.", Toast.LENGTH_SHORT).show();
        } else {
            CityListAdapter menu_recycler_adaxpter = new CityListAdapter(ConferencePaymentInfoActivity.this, cityListMain, stateName);
            multi_city_sel_recycler.setAdapter(menu_recycler_adaxpter);

        }


        dialogA.show();
        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogA.dismiss();
                //TODO : finishing the activity
            }
        });

        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName.clear();
                cityId.clear();
                for (int i = 0; i < AppConstantClass.cityListFillData.size(); i++) {
                    if (AppConstantClass.cityListFillData.get(i).isSelectedCheck()) {
                        cityName.add(AppConstantClass.cityListFillData.get(i).getCity_name());
                        cityId.add(AppConstantClass.cityListFillData.get(i).getCity_id());

                    }
                }

                txt_city.setText(cityName.toString().replace("[", "").replace("]", ""));
                city_Id = cityId.toString().replace("[", "").replace("]", "");
                dialogA.dismiss();

            }
        });
    }

    private void GetInoNofiApi() throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ConferencePaymentInfoActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("notification_type", paymentMode_Id);
        params.put("medical_profile_id", medicalProfileID.replace("[", "").replace("]", "").replace(" ", ""));
        params.put("specialization_id", targetAudienceSpecialityID.replace("[", "").replace("]", "").replace(" ", ""));
        params.put("department_id", departmentID);
        params.put("country_id", country_id);

        if (dateSelection1.length() != 0 && dateSelection2.length() != 0 && dateSelection3.length() != 0) {
            dateSelection = dateSelection1 + "," + dateSelection2 + "," + dateSelection3;
            totAmountCount = 3;
        } else if (dateSelection1.length() != 0 && dateSelection2.length() != 0) {
            dateSelection = dateSelection1 + "," + dateSelection2;
            totAmountCount = 2;
        } else if (dateSelection3.length() != 0 && dateSelection1.length() != 0) {
            dateSelection = dateSelection1 + "," + dateSelection3;
            totAmountCount = 2;
        } else if (dateSelection3.length() != 0 && dateSelection2.length() != 0) {
            dateSelection = dateSelection2 + "," + dateSelection3;
            totAmountCount = 2;
        } else if (dateSelection3.length() != 0) {
            dateSelection = dateSelection3;
            totAmountCount = 1;
        } else if (dateSelection2.length() != 0) {
            dateSelection = dateSelection2;
            totAmountCount = 1;
        } else if (dateSelection1.length() != 0) {
            dateSelection = dateSelection1;
            totAmountCount = 1;
        }

        params.put("notification_date", dateSelection.replace(" ", ""));

//        params.put("state_id", state_id);
//        params.put("city_id", city_id);

        if (stateChipsNameList.size() == 0)
            stateIdAl.clear();

        if (cityChipsNameList.size() == 0)
            city_idAl.clear();

        params.put("state_id", stateIdAl.toString().replace("[", "").replace("]", "").replace(" ", ""));
        params.put("city_id", city_idAl.toString().replace("[", "").replace("]", "").replace(" ", ""));

        params.put("conference_id", conference_id);
        header.put("Cynapse", params);

        Log.d("resBob", header.toString());

        new GetInfoNotifyApi(ConferencePaymentInfoActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    Log.d("RESPONSWEGETNOTI", response.toString());
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    if (res_code.equals("1")) {
                        JSONObject jsonObject = header.getJSONObject("data");
                        count = jsonObject.getString("count");
                        txt_total_users.setText(count);
                        cost = jsonObject.getString("cost");

                        //txt_noti_cost.setText(cost);
                        txt_noti_cost.setText(Util.form.format(with_total_amount));

                        //totalAmount = Double.parseDouble(txt_total_users.getText().toString()) * Double.parseDouble(txt_noti_cost.getText().toString());

                        if (totAmountCount > 0)
                            totalAmount = Double.parseDouble(txt_total_users.getText().toString()) * (with_total_amount * totAmountCount);
                        else
                            totalAmount = Double.parseDouble(txt_total_users.getText().toString()) * with_total_amount;

                        Utils.sop("with_total_amount" + with_total_amount);
                        Utils.sop("totAmountCount" + totAmountCount);
                        Utils.sop("count--" + count);

                        txt_noti_amount.setText(Util.form.format(totalAmount+total_amount));
                        txt_total_add.setText(Util.form.format(totalAmount+total_amount));
                        lnr_amountpaid.setVisibility(View.VISIBLE);
                    } else {
                        MyToast.toastLong(ConferencePaymentInfoActivity.this, res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Log.d("exception", e.getMessage());
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
                //Log.d("exception", error.getMessage());
            }
        };
    }


    private void postReviewAmountApi() throws JSONException {
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
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));

        params.put("total_users", txt_total_users.getText().toString());
        params.put("amount_paid", txt_noti_amount.getText().toString());
        params.put("amount_after_promocode", txt_total_add.getText().toString());
        params.put("select_date_user", dateSelection.replace(" ", ""));
        params.put("conference_id", conference_id);

        header.put("Cynapse", params);
        Log.d("POSTJOBFRAAG", header + "");

        new PostReviewAmontApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSECOFPAY", response.toString());
                    if (res_code.equals("1")) {
                        JSONObject jsonObject = header.getJSONObject("data");
                        String order_id = jsonObject.getString("order_id");
                        Log.d("ORDERID_ID", order_id);

                        String tnoamnt = txt_noti_amount.getText().toString();
                        String totadd = txt_total_add.getText().toString();

                        Log.d("tmttnntnt", tnoamnt);

                        Log.d("totadddd", totadd);

                        if (x == 0) {
                            if (tnoamnt.equalsIgnoreCase("0.0") || tnoamnt.equalsIgnoreCase("0")) {
                                Log.d("kdkdkddd11", "pooo");
                                PostPaymentTicketApi("2", tnoamnt, order_id);
                                Intent intent1 = new Intent(ConferencePaymentInfoActivity.this, MainActivity.class);
                                intent1.putExtra("tabPress", "0");
                                startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                MyToast.toastLong(ConferencePaymentInfoActivity.this, "Payment successful , your conference is live now");
                                finish();
                            } else {
                                Intent it = new Intent(ConferencePaymentInfoActivity.this, WebViewActivity.class);
                                it.putExtra("totalAmount", tnoamnt);
                                it.putExtra("order_id", order_id);
                                it.putExtra("conference_id", conference_id);
                                it.putExtra("no_of_seats", paymentMode_Id);
                                it.putExtra("promocode_id", promocode_id);

                                if (paymentMode_Id.equals("1")) {
                                    it.putExtra("date_to_notify_user", txt_datepicker.getText().toString());
                                } else {
                                    it.putExtra("date_to_notify_user", "");
                                }

                                it.putExtra("uuid", "" + AppCustomPreferenceClass.readString(ConferencePaymentInfoActivity.this, AppCustomPreferenceClass.UserId, ""));
                                startActivity(it);
                                finish();
                            }
                        } else {
                            if (totadd.equalsIgnoreCase("0.0") || totadd.equalsIgnoreCase("0")) {
                                Log.d("kdkdkddd22", "pooo");
                                PostPaymentTicketApi("2", totadd, order_id);
                                Intent intent1 = new Intent(ConferencePaymentInfoActivity.this, MainActivity.class);
                                intent1.putExtra("tabPress", "0");
                                startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                MyToast.toastLong(ConferencePaymentInfoActivity.this, "Payment successful , your conference is live now");
                                finish();
                            } else {
                                Intent it = new Intent(ConferencePaymentInfoActivity.this, WebViewActivity.class);
                                it.putExtra("totalAmount", totadd);
                                it.putExtra("order_id", order_id);
                                it.putExtra("no_of_seats", paymentMode_Id);
                                it.putExtra("conference_id", conference_id);
                                it.putExtra("promocode_id", promocode_id);

                                if (paymentMode_Id.equals("1")) {
                                    it.putExtra("date_to_notify_user", dateSelection);
                                } else {
                                    it.putExtra("date_to_notify_user", "");
                                }

                                it.putExtra("uuid", "" + AppCustomPreferenceClass.readString(ConferencePaymentInfoActivity.this, AppCustomPreferenceClass.UserId, ""));
                                startActivity(it);
                                finish();
                            }

                        }
                        //finish();
                    } else {
                        MyToast.toastLong(ConferencePaymentInfoActivity.this, res_msg);
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

    private void PostPaymentTicketApi(String type_id, String amount, String orderID) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("no_of_seats", paymentMode_Id);
        params.put("total_amount", amount);
        params.put("order_id", orderID);
        params.put("payment_status", "1");
        params.put("type_id", type_id);
        params.put("user_details", "");
        params.put("promocode_id", promocode_id);

        if (paymentMode_Id.equals("1"))
            params.put("date_to_notify_user", txt_datepicker.getText().toString());
        else
            params.put("date_to_notify_user", "");

//        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
//        params.put("conference_id", conference_id);
//        params.put("amount", amount);
//        params.put("days", "0");
//        params.put("accomadation_charges", "");
//        params.put("extra_charges", "");
//        params.put("order_id", orderID);
//        params.put("type_id", type_id);

        //params.put("uuid", "S75f3");
        // params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.sync_time,""));
        //params.put("sync_time", "");
        header.put("Cynapse", params);
        new PostPaymentApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    //AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.sync_time,sync_time);
                    Log.d("JOBSPONSE", response.toString());

                    if (res_code.equals("1")) {
                        MyToast.toastLong(ConferencePaymentInfoActivity.this, res_msg);
                        Intent intent = new Intent(ConferencePaymentInfoActivity.this, MainActivity.class);
                        startActivity(intent);
                        ActivityCompat.finishAffinity(ConferencePaymentInfoActivity.this);

                    } else {
                        MyToast.toastLong(ConferencePaymentInfoActivity.this, res_msg);
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



    int endDay = 0;
    int endMonth = 0;
    int endYear = 0;
    private String dateSelection = "";

}
