package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.adapter.AddMorePackageAdapter;
import com.alcanzar.cynapse.adapter.MedicalProfileAdapter;
import com.alcanzar.cynapse.adapter.MedicalSpecialityAdapter;
import com.alcanzar.cynapse.adapter.MedicalSpecialityDepartmentAdapter;
import com.alcanzar.cynapse.adapter.PlaceAutocompleteAdapter;
import com.alcanzar.cynapse.api.AddConferencePaymentTypeApi;
import com.alcanzar.cynapse.api.AddConferencePostApi;
import com.alcanzar.cynapse.api.AddMyConferencePostApi;
import com.alcanzar.cynapse.api.GetAllCountryApi;
import com.alcanzar.cynapse.api.GetCityApi;
import com.alcanzar.cynapse.api.GetConferenceTypeApi;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.GetStateApi;
import com.alcanzar.cynapse.api.GetTargetDepartment;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.AddPackageModal;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ConferenceTypeModel;
import com.alcanzar.cynapse.model.CountryModel;
import com.alcanzar.cynapse.model.GetTargetDepartmentModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.model.PackagesModel;
import com.alcanzar.cynapse.model.PdfModel;

import com.alcanzar.cynapse.model.RadioButtonStateManageModel;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.PostImage;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;


public class AddConference extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, MedicalProfileInterface, MedicalDepartmentInterface, MedicalTitleInterface, AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    ImageView btnBack, titleIcon, img_venue, img_datef, img_datet, img_broucher, img_add;
    int fl = 0;

    LinearLayout parentscrollLayout, normalPackLL, packageForgeinLL;

    TextView txt_venue, txt_datef, txt_datet, txt_timef, txt_timet, title, txt_total_day, txt_total_day_1, textview;

    public static TextView txt_uploadbrochures;

    EditText txt_conferenc_name, txt_event_host,
            txt_conctact, txt_brouchercharg, txt_cost,
            txt_brochdays, txt_registrationfee, txt_regdays, txt_eventsponcer, discount_details_edit,
            edit_spl_others, txt_duration, txt_conf_desc, txt_available_seta, txt_conference_type, edit_credit_earn, no_of_discount, edit_gst;

    AutoCompleteTextView txt_country, txt_state, txt_city;
    LinearLayout rel_lay_loc, lnr_upload_broch;
    RelativeLayout other_spl_rel_lay;
    RadioGroup radioGrp, radioGrppayment;
    RadioButton radioMyAdd, radioCurrentAdd, radioEnterAdd, radioonline, radioatvenue, radioFreeEvent;
    TextView my_address_txt, current_address_txt, txt_medical_select, txt_specially, txt_department;
    File imageFile;
    private Uri fileUri;
    Utils utilsObj;
    private String pdfPath;
    Button btnAddConfer;
    DatabaseHelper handler;
    private Bitmap bitmap;
    static String picturePath;
    String fileMultiNme = "", country_Id = "", country_Str = "", conferenceType_Id = "", paymentMode_Id = "", conferenceListDay = "", conferenceListCharges = "";

    double pack1Total = 0.0, pack2Total = 0.0;
    private static final int PICK_CAMERA_REQUEST = 11;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int PICK_PDF_REQUEST = 11;
    private static final String FILES_TO_UPLOAD = "upload";
    public static ArrayList<HashMap<String, String>> path_list_file = new ArrayList<HashMap<String, String>>();
    EditText txt_day1, txt_day2, txt_day3, txt_total, txt_package_name, txt_package_name_1, txt_day_1, txt_day_2, txt_day_3, txt_total_1;

    ArrayList<String> conferenceCharges;
    ArrayList<String> conferenceDay;
    ArrayList<String> countrySpinner = new ArrayList<>();
    ArrayList<String> stateSpinner = new ArrayList<>();
    ArrayList<String> citySpinner = new ArrayList<>();
    ArrayList<String> medicalListId = new ArrayList<>();
    ArrayList<String> titleNewListId = new ArrayList<>();
    ArrayList<String> titleNewListIdDepartment = new ArrayList<>();
    ArrayList<String> allpdfName = new ArrayList<>();
    ArrayList<CountryModel> countryList = new ArrayList<>();
    ArrayList<StateModel> stateList = new ArrayList<>();
    ArrayList<CityModel> cityList = new ArrayList<>();

    ArrayAdapter adapter_specialization;
    ArrayList<String> specializationName;
    private ArrayList<String> imagesPathList;
    ArrayList<PdfModel> arrayListFileName = new ArrayList<>();
    String addres_type = "";
    MedicalSpecialityAdapter titleAdapter_recycler_adapter;
    MedicalSpecialityDepartmentAdapter medicalSpecialityDepartmentAdapter;
    boolean dateSet = true;
    ArrayList<PackagesModel> arrayListPackage = new ArrayList<>();
    ArrayList<ConferenceDetailsModel> arrayList = new ArrayList<>();
    public static ArrayList<String> path_list = new ArrayList<String>();
    ArrayList<JobSpecializationModel> jobSpecializationSpinner = new ArrayList<>();
    ArrayList<JobSpecializationModel> specialList = new ArrayList<>();
    static ArrayList<String> conferenceTypeList = new ArrayList<>();
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    ArrayList<GetTargetDepartmentModel> medicalSpecialityDepartmentList = new ArrayList<>();
    ArrayList<MedicalProfileModel> dept_SpinnerList = new ArrayList<>();
    ArrayList<MedicalProfileModel> medical_SpinnerList = new ArrayList<>();
    ArrayList<String> titleNewList = new ArrayList<>();
    ArrayList<String> titleNewListDepartment = new ArrayList<>();

    ArrayList<String> conferenceList = new ArrayList<>();
    static int days1, mins1, days2, mins2;
    RecyclerView multi_title_sel_recycler;
    String updatefileName = "";
    String my_city = "", my_state = "", my_country = "", my_city_id = "", my_country_id = "", my_state_id = "", my_address = "";
    String country_id = "", specializationId = "", country_str = "", medicalSpecialityDepartmentID = "",
            state_id = "", state_Str = "", cost = "", duration = "",
            event_sponcer = "", address_type = "",
            city_id = "", medicalProfileId = "", all_pdf_name = "",

    city_str = "", getProfileName = "", city_name = "", available_seat = "", conference_description = "", brochuers_file = "", city_Id = "", city_Name = "",
            getProfileId = "", url = "",
            conference_id = "", conference_name = "", event_host_name = "", speciality = "", contact = "", registration_fee = "", location = "", AddressStr = "",
            from_date = "", from_time = "", venue = "", add_date = "",
            registration_days = "", particular_country_id = "", particular_country_name = "", particular_state_id = "", particular_state_name = "", status = "", modify_date = "", to_date = "", to_time = "", brochures_charge = "", brochures_days = "";
    ArrayList<String> countryName = new ArrayList<>();
    ArrayList<String> tempcountry = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> tempstate = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();
    ArrayList<String> tempcity = new ArrayList<>();
    ArrayList<String> conferenceTyeList = new ArrayList<>();
    ArrayList<ConferencePackageModel> conferencePackageList = new ArrayList<>();
    ArrayAdapter country_adapter, state_adapter, city_adapter;
    TimePickerDialog timePickerDialog;
    static String pdf_name = "";
    String fileName = "";
    ArrayList<JobSpecializationModel> titleList = new ArrayList<>();
    ArrayList<GetTargetDepartmentModel> titleListDepartment = new ArrayList<>();
    private Calendar calendar;
    String conf_timef = "", conf_timet = "", splStr = "";
    boolean timeY = false, show_details = false;
    String profile_image = "", filePath = "", filename = "";
    private final int PICK_IMAGE_MULTIPLE = 4;
    String multiFileName = "";
    String curDate = "", strCurrentDateTo = "";
    Spinner spinner;
    ArrayList<JobSpecializationModel> arrayList1 = new ArrayList<>();
    private final int PICK_FILE_MULTIPLE = 6;
    LinearLayout lnr_online, lnr_online_1;

    private ImageView buttonView, img_showdoc, img_decrse_pack;
    private LinearLayout parentLayout, lnr_add_pack;
    int hint = 0, startDayf, startDayt;
    TextView view, total_amount, txt_totl, txt_pice_date, txt_uploadbrochures_1, img_broucher_1;
    EditText total_amount_pack;
    LinearLayout ll, lnr_hor;
    double total = 0.0, acod_amt = 0.0, daysBetween = 0.0, bewdaybetween = 0.0;
    List<EditText> allEds;
    AutoCompleteTextView txt_addres;
    String streetAddress = "";
    EditText locationTv;

    //    String plsJustWork = "", memberdis = "";
//    String plsJustWork = "", memberdis = "";
    boolean dateSetpicker = true;
    RelativeLayout relativeLayout;
    EditText edittTxt, txt_acc_amount, ed_hike_per;
    String curpickDate = "", strings = "", strCurrentDatef = "",
            credit_earnings = "", total_days_price = "", accomdation = "", member_concessions = "",
            student_concessions = "", price_hike_after_date = "", price_hike_after_percentage = "", payment_mode = "";

    ArrayList<EditText> editTextList;
    RadioGroup radioGrpDis, radioGrpAccomad;
    //    RadioButton radiomem, radiostud, radioacmod;
    ArrayList<TextView> mTextViewList;
    boolean addbool = true;
    ArrayList aList = null;
    String showBrocherName = "";
    RadioButtonStateManageModel radioButtonStateManageModel = new RadioButtonStateManageModel();
    String currentDate2 = "", frmoDate2 = "";


    ///////#######################

    TextView addMorePackage, addMoreForeignPackage;

    ArrayList<AddPackageModal> addPackageModalArrayList = new ArrayList<>();
    ArrayList<AddPackageModal> addPackageForeignArrayList = new ArrayList<>();

    RecyclerView addPackageRecyclerView, addPackageForeignRecyclerView;
    TextView specialitylabel;


    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conference_1);
        txt_uploadbrochures_1 = findViewById(R.id.txt_uploadbrochures_1);
        spinner = findViewById(R.id.conferenceType);
        parentscrollLayout = findViewById(R.id.parentscrollLayout);

        normalPackLL = findViewById(R.id.normalPackLL);
        normalPackLL.setVisibility(View.GONE);

        txt_package_name = findViewById(R.id.txt_package_name);
        txt_package_name_1 = findViewById(R.id.txt_package_name_1);
        txt_total_1 = findViewById(R.id.txt_total_1);
        txt_total_day_1 = findViewById(R.id.txt_total_day_1);
        total_amount_pack = findViewById(R.id.total_amount_pack);
//        ed_consession_member = findViewById(R.id.ed_consession_member);
        addMorePackage = findViewById(R.id.addMorePackage);
        packageForgeinLL = findViewById(R.id.packageForgeinLL);
        packageForgeinLL.setVisibility(View.GONE);

        addPackageRecyclerView = findViewById(R.id.addPackageRecyclerView);
        addPackageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addPackageRecyclerView.setHasFixedSize(true);
        addPackageRecyclerView.setNestedScrollingEnabled(false);

        addPackageForeignRecyclerView = findViewById(R.id.addPackageForeignRecyclerView);
        addPackageForeignRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addPackageForeignRecyclerView.setHasFixedSize(true);
        addPackageForeignRecyclerView.setNestedScrollingEnabled(false);
        addMoreForeignPackage = findViewById(R.id.addMoreForeignPackage);

        specialitylabel = findViewById(R.id.specialitylabel);

        txt_addres = findViewById(R.id.txt_addres);

        getStreetNumber();

        try {
            GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String msgper = "Percentage cannot exceed than 100";
//        ed_consession_member.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                Log.e("focus_check",b+"");
//                if(!b){
//                    if(!ed_consession_member.getText().toString().trim().equals("")) {
//                        int asd = Integer.parseInt(ed_consession_member.getText().toString());
//                        if (asd > 100) {
//                            ed_consession_member.setFocusable(true);
//                            ed_consession_member.setText("");
//                            Toast.makeText(AddConference.this, msgper, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//        });
//        ed_consession_stud = findViewById(R.id.ed_consession_stud);
//        ed_consession_stud.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                Log.e("focus_check",b+"");
//                if(!b){
//                    if(!ed_consession_stud.getText().toString().trim().equals("")){
//                        int asd=Integer.parseInt(ed_consession_stud.getText().toString());
//                        if(asd>100){
//                            ed_consession_stud.setFocusable(true);
//                            ed_consession_stud.setText("");
//                            Toast.makeText(AddConference.this, msgper, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }
//            }
//        });

        ed_hike_per = findViewById(R.id.ed_hike_per);

//        ed_hike_per.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                Log.e("focus_check", b + "");
//                if (!b) {
//                    if (!ed_hike_per.getText().toString().trim().equals("")) {
//                        int asd = parseInt(ed_hike_per.getText().toString());
//                        if (asd > 100) {
//                            ed_hike_per.setFocusable(true);
//                            ed_hike_per.setText("");
//                            Toast.makeText(AddConference.this, msgper, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//        });

        txt_day_1 = findViewById(R.id.txt_day_1);
        txt_day_2 = findViewById(R.id.txt_day_2);
        txt_day_3 = findViewById(R.id.txt_day_3);
        txt_total = findViewById(R.id.txt_total);
        txt_total_day = findViewById(R.id.txt_total_day);
        txt_day1 = findViewById(R.id.txt_day1);
        txt_day2 = findViewById(R.id.txt_day2);
        txt_day3 = findViewById(R.id.txt_day3);
        lnr_online = findViewById(R.id.lnr_online);
        lnr_online_1 = findViewById(R.id.lnr_online_1);
        img_add = findViewById(R.id.img_add);
        edit_credit_earn = findViewById(R.id.edit_credit_earn);
        txt_conference_type = findViewById(R.id.txt_conference_type);
        txt_medical_select = findViewById(R.id.txt_medical_select);
        txt_medical_select.setOnClickListener(this);
        btnBack = findViewById(R.id.btnBack);
        titleIcon = findViewById(R.id.titleIcon);
        img_broucher = findViewById(R.id.img_broucher);
//      txt_venue = findViewById(R.id.txt_venue);
        other_spl_rel_lay = findViewById(R.id.other_spl_rel_lay);
        img_datef = findViewById(R.id.img_datef);
        txt_datef = findViewById(R.id.txt_datef);
        txt_timef = findViewById(R.id.txt_timef);
        txt_timet = findViewById(R.id.txt_timet);
        rel_lay_loc = findViewById(R.id.rel_lay_loc);
        btnAddConfer = findViewById(R.id.btnAddConfer);
        txt_specially = findViewById(R.id.txt_specially);
        txt_department = findViewById(R.id.txt_department);
        txt_event_host = findViewById(R.id.txt_event_host);
        txt_conferenc_name = findViewById(R.id.txt_conferenc_name);
        txt_uploadbrochures = findViewById(R.id.txt_uploadbrochures);
        txt_brouchercharg = findViewById(R.id.txt_brouchercharg);
        txt_registrationfee = findViewById(R.id.txt_registrationfee);
        txt_brochdays = findViewById(R.id.txt_brochdays);
        // txt_addres = findViewById(R.id.txt_addres);
        txt_regdays = findViewById(R.id.txt_regdays);
        txt_conctact = findViewById(R.id.txt_conctact);
        txt_country = findViewById(R.id.txt_country);
        txt_city = findViewById(R.id.txt_city);
        txt_cost = findViewById(R.id.txt_cost);
        txt_state = findViewById(R.id.txt_state);
        title = findViewById(R.id.title);
        txt_datet = findViewById(R.id.txt_datet);
        img_datet = findViewById(R.id.img_datet);
        txt_eventsponcer = findViewById(R.id.txt_eventsponcer);
        txt_duration = findViewById(R.id.txt_duration);
        radioGrppayment = findViewById(R.id.radioGrppayment);
        radioonline = findViewById(R.id.radioonline);
        radioatvenue = findViewById(R.id.radioatvenue);
        radioFreeEvent = findViewById(R.id.radioFreeEvent);
        radioGrp = findViewById(R.id.radioGrp);
        radioMyAdd = findViewById(R.id.radioMyAdd);
        txt_available_seta = findViewById(R.id.txt_available_seta);
        txt_conf_desc = findViewById(R.id.txt_conf_desc);
        radioCurrentAdd = findViewById(R.id.radioCurrentAdd);
        radioEnterAdd = findViewById(R.id.radioEnterAdd);
        my_address_txt = findViewById(R.id.my_address_txt);
//        my_address_txt.setVisibility(View.GONE);
        current_address_txt = findViewById(R.id.current_address_txt);
        edit_spl_others = findViewById(R.id.edit_spl_others);
        allEds = new ArrayList<EditText>();
        mTextViewList = new ArrayList<>();
        relativeLayout = (RelativeLayout) findViewById(R.id.rel_hor);
        lnr_add_pack = findViewById(R.id.lnr_add_pack);
         locationTv = findViewById(R.id.locationTv);
//        radioGrpAccomad = findViewById(R.id.radioGrpAccomad);
//        radioacmod = findViewById(R.id.radioacmod);
//        radioGrpDis = findViewById(R.id.radioGrpDis);
//        radiomem = findViewById(R.id.radiomem);
//        radiostud = findViewById(R.id.radiostud);
        ll = (LinearLayout) findViewById(R.id.LL);
        img_decrse_pack = findViewById(R.id.img_decrse_pack);
        buttonView = (ImageView) findViewById(R.id.buttonView);
        img_showdoc = findViewById(R.id.img_showdoc);
        total_amount = findViewById(R.id.total_amount);
        txt_totl = findViewById(R.id.txt_totl);
        txt_pice_date = findViewById(R.id.txt_pice_date);
        txt_acc_amount = findViewById(R.id.txt_acc_amount);

        discount_details_edit = findViewById(R.id.discount_details_edit);
        no_of_discount = findViewById(R.id.no_of_discount);
        edit_gst = findViewById(R.id.edit_gst);

        txt_pice_date.setOnClickListener(this);
        conferenceCharges = new ArrayList<>();
        conferenceDay = new ArrayList<>();
        txt_total_day_1.setOnClickListener(this);
        txt_total_day.setOnClickListener(this);
        img_add.setOnClickListener(this);
        txt_timef.setOnClickListener(this);
        txt_timet.setOnClickListener(this);
        img_datef.setOnClickListener(this);
        img_datet.setOnClickListener(this);
        img_broucher.setOnClickListener(this);
        btnAddConfer.setOnClickListener(this);
        txt_specially.setOnClickListener(this);
        txt_city.setOnClickListener(this);
        txt_datef.setOnClickListener(this);
        txt_datet.setOnClickListener(this);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        btnBack.setOnClickListener(this);


        utilsObj = new Utils();
        calendar = Calendar.getInstance();
        handler = new DatabaseHelper(this);
        frmoDate2 = getCurrentDate();
        strCurrentDatef = "";

        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true, false);

        addTextChangedListener();

//        clearListData();
        getCountyApi();



        if (getIntent() != null) {

            conference_id = getIntent().getStringExtra("conference_id");
            conference_name = getIntent().getStringExtra("conference_name");
            event_host_name = getIntent().getStringExtra("event_host_name");
            speciality = getIntent().getStringExtra("speciality");
            contact = getIntent().getStringExtra("contact");

            from_date = getIntent().getStringExtra("from_date");
            strCurrentDatef = from_date;
            from_time = getIntent().getStringExtra("from_time");
            venue = getIntent().getStringExtra("venue");
            add_date = getIntent().getStringExtra("add_date");
//            brochures_charge = getIntent().getStringExtra("brochures_charge");
//            brochures_days = getIntent().getStringExtra("brochures_days");
//            registration_days = getIntent().getStringExtra("registration_days");
            particular_country_id = getIntent().getStringExtra("particular_country_id");
            particular_country_name = getIntent().getStringExtra("particular_country_name");
            particular_state_id = getIntent().getStringExtra("particular_state_id");
            particular_state_name = getIntent().getStringExtra("particular_state_name");
            status = getIntent().getStringExtra("status");
            modify_date = getIntent().getStringExtra("modify_date");
            to_date = getIntent().getStringExtra("to_date");
            to_time = getIntent().getStringExtra("to_time");
            show_details = getIntent().getBooleanExtra("show_details", false);
//            cost = getIntent().getStringExtra("cost");
//            duration = getIntent().getStringExtra("duration");
            event_sponcer = getIntent().getStringExtra("event_sponcer");
            address_type = getIntent().getStringExtra("address_type");
            city_id = getIntent().getStringExtra("city_id");
            city_Name = getIntent().getStringExtra("city_name");
            available_seat = getIntent().getStringExtra("available_seat");
            conference_description = getIntent().getStringExtra("conference_description");
            brochuers_file = getIntent().getStringExtra("brochuers_file");

            conferenceType_Id = getIntent().getStringExtra("conference_type_id");
            medicalProfileId = getIntent().getStringExtra("medical_profile_id");
            credit_earnings = getIntent().getStringExtra("credit_earnings");
            total_days_price = getIntent().getStringExtra("total_days_price");
            accomdation = getIntent().getStringExtra("accomdation");
            member_concessions = getIntent().getStringExtra("member_concessions");
            student_concessions = getIntent().getStringExtra("student_concessions");
            price_hike_after_date = getIntent().getStringExtra("price_hike_after_date");
            price_hike_after_percentage = getIntent().getStringExtra("price_hike_after_percentage");
            paymentMode_Id = getIntent().getStringExtra("payment_mode");
        }

        openGoogleLocatiion("<small>Street, House No.</small>",txt_addres);

        try {
            getConferenceTypeApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conference_type, R.layout.conference_type);
        adapter.setDropDownViewResource(R.layout.conference_type);

        spinner.setAdapter(adapter);


        if (show_details) {

            if (address_type.equalsIgnoreCase("1")) {
                my_address_txt.setVisibility(View.VISIBLE);
                current_address_txt.setVisibility(View.GONE);
                my_address_txt.setText(venue);
                radioMyAdd.setChecked(true);

            } else if (address_type.equalsIgnoreCase("2")) {
                current_address_txt.setVisibility(View.VISIBLE);
                my_address_txt.setVisibility(View.GONE);
                current_address_txt.setText(venue);
                radioCurrentAdd.setChecked(true);
            } else if (address_type.equalsIgnoreCase("3")) {
                my_address_txt.setVisibility(View.GONE);
                current_address_txt.setVisibility(View.GONE);
                rel_lay_loc.setVisibility(View.VISIBLE);
                txt_addres.setText(venue);
                radioEnterAdd.setChecked(true);
            }

            aList = new ArrayList(Arrays.asList(brochuers_file.split(",")));

            for (int i = 0; i < aList.size(); i++) {
                showBrocherName = showBrocherName + aList.get(i).toString().replace("http://162.243.205.148/cynapce/app/webroot/files/brochures/", "") + ",";
            }

            showBrocherName = showBrocherName.substring(0, showBrocherName.length() - 1);
            txt_conferenc_name.setText(conference_name);
            txt_brouchercharg.setText(brochures_charge);
            txt_brochdays.setText(brochures_days);
            // txt_addres.setText(venue);
            // txt_venue.setText(venue);

            txt_datef.setText(from_date);
            txt_datet.setText(to_date);
            txt_timef.setText(from_time);
            txt_timet.setText(to_time);
            txt_event_host.setText(event_host_name);
            txt_specially.setText(speciality);
            specializationId = speciality;
            txt_regdays.setText(registration_days);
            txt_registrationfee.setText(registration_fee);
            txt_conctact.setText(contact);
            txt_cost.setText(cost);
            txt_country.setText(particular_country_name);
            txt_state.setText(particular_state_name);
            txt_eventsponcer.setText(event_sponcer);
            txt_duration.setText(duration);
            country_id = particular_country_id;
            state_id = particular_state_id;
            title.setText("Edit conference");
            btnAddConfer.setText("Update conference");

            txt_available_seta.setText(available_seat);
            txt_conf_desc.setText(conference_description);
            txt_city.setText(city_Name);
            edit_credit_earn.setText(credit_earnings);
            txt_medical_select.setText(medicalProfileId);
            img_broucher.setEnabled(true);
            Log.d("uploadimagebro", showBrocherName);
            txt_uploadbrochures.setText(showBrocherName);


//            if(showBrocherName.equalsIgnoreCase(""))
//            {
//
//            }else {
//                txt_uploadbrochures_1.setVisibility(View.VISIBLE);
//                txt_uploadbrochures_1.setText(showBrocherName);
//            }


            if (paymentMode_Id.equalsIgnoreCase("1")) {
                radioonline.setChecked(true);
                radioatvenue.setChecked(false);
                radioFreeEvent.setChecked(false);
            } else if (paymentMode_Id.equalsIgnoreCase("2")) {

                radioonline.setChecked(false);
                radioatvenue.setChecked(true);
                radioFreeEvent.setChecked(false);
            } else if (paymentMode_Id.equalsIgnoreCase("3")) {
                radioonline.setChecked(false);
                radioatvenue.setChecked(false);
                radioFreeEvent.setChecked(true);
            }


            txt_acc_amount.setText(accomdation);

            if (accomdation.length() > 0) {
                txt_acc_amount.setEnabled(true);
//                radioacmod.setChecked(true);
            } else {
                txt_acc_amount.setEnabled(false);
//                radioacmod.setChecked(false);
            }


            total_amount_pack.setText(total_days_price);

            if (member_concessions.length() > 0) {
                radioButtonStateManageModel.setButtonTwoCheckedState(true);
//                radiomem.setChecked(true);

            } else {
                radioButtonStateManageModel.setButtonTwoCheckedState(false);
//                radiomem.setChecked(false);

            }


//            ed_consession_member.setText(member_concessions);

            if (student_concessions.length() > 0) {
//                radiostud.setChecked(true);

            } else {

//                radiostud.setChecked(false);
            }
//            ed_consession_stud.setText(student_concessions);
            ed_hike_per.setText(price_hike_after_percentage);
            txt_pice_date.setText(price_hike_after_date);

            if (conferenceType_Id.equalsIgnoreCase("1")) {
                txt_conference_type.setVisibility(View.GONE);
                spinner.setSelection(0);

            } else if (conferenceType_Id.equalsIgnoreCase("2")) {
                txt_conference_type.setVisibility(View.GONE);
                spinner.setSelection(1);
            } else if (conferenceType_Id.equalsIgnoreCase("3")) {
                txt_conference_type.setVisibility(View.GONE);
                spinner.setSelection(2);
            } else if (conferenceType_Id.equalsIgnoreCase("4")) {
                txt_conference_type.setVisibility(View.GONE);
                spinner.setSelection(3);
            } else if (conferenceType_Id.equalsIgnoreCase("5")) {
                txt_conference_type.setVisibility(View.GONE);
                spinner.setSelection(4);
            } else if (conferenceType_Id.equalsIgnoreCase("other_conference_type")) {
                txt_conference_type.setVisibility(View.VISIBLE);
                spinner.setSelection(5);
            } else {
                txt_conference_type.setVisibility(View.GONE);
            }

            try {
                conferencePackageList = handler.getConfPackCharge(DatabaseHelper.TABLE_MY_CONFERENCES_PACK_CHARGE, conference_id);


            } catch (Exception e) {
                e.printStackTrace();

            }
            try {
                UpdateDateDiff(from_date, to_date, conferencePackageList);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            radioonline.setChecked(true);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            Date date = cal.getTime();
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            // AppConstantClass.currentDate1 = format1.format(date);
            txt_pice_date.setText("");
            ed_hike_per.setText("");
//            ed_consession_stud.setText("");
//            ed_consession_member.setText("");
            total_amount_pack.setText("");
            txt_acc_amount.setText("");
            txt_uploadbrochures.setText("");
            img_broucher.setEnabled(true);
            txt_medical_select.setText("");
            edit_credit_earn.setText("");
            txt_conferenc_name.setText("");
            txt_brouchercharg.setText("");
            txt_brochdays.setText("");
            txt_addres.setText("");
            //txt_venue.setText("");
            txt_datef.setText("");
            txt_datet.setText("");
            txt_timef.setText("");
            txt_timet.setText("");
            txt_event_host.setText("");
            txt_specially.setText("");
            txt_regdays.setText("");
            txt_registrationfee.setText("");
            txt_conctact.setText("");
            txt_cost.setText("");
            txt_country.setText("");
            txt_state.setText("");
            txt_eventsponcer.setText("");
            txt_duration.setText("");
            title.setText("Add Events");
            btnAddConfer.setText("Add Events");
        }

        try {
            GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            getMedicalProfileApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            txt_venue.setText(getCompleteAddressString(AddConference.this,
//                    Double.parseDouble(AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Latitude, "")),
//                    Double.parseDouble(AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Longitude, ""))));
//        } catch (NumberFormatException nf) {
//            nf.printStackTrace();
//        }

        try {
            getProfileSpecialization("");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            getTargetDepartment("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txt_medical_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogMedicalProfile();
            }
        });

        txt_specially.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTitle();
            }
        });

        txt_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDepartment();
            }
        });

//        radioacmod.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(radioacmod.isChecked()){
//                    Log.e("dvav","UnCheck"+ radioacmod.isChecked());
//
//                    txt_acc_amount.setEnabled(true);
//                    radioacmod.setChecked(true);
//                }else{
//
//
//                    txt_acc_amount.setEnabled(false);
//                    radioacmod.setChecked(false);
//                    Log.e("dvav","Check");
//                }
//            }
//        });


//        radioacmod.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (radioButtonStateManageModel.isButtonOneCheckedState()) {
//                    txt_acc_amount.setEnabled(false);
//                    radioButtonStateManageModel.setButtonOneCheckedState(false);
//                    txt_acc_amount.setText("");
//                    radioacmod.setChecked(false);
//                } else {
//                    radioButtonStateManageModel.setButtonOneCheckedState(true);
//                    txt_acc_amount.setEnabled(true);
//                    radioacmod.setChecked(true);
//                }
//            }
//        });

        txt_country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("in ", "Country");

                    if (txt_country.getText().toString().equals("")) {
                        tempcountry = new ArrayList<>();
                        tempcountry.addAll(countryName);
                    }

                    country_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcountry);
                    txt_country.setAdapter(country_adapter);
                    txt_country.showDropDown();

                } else {
//                    if (country_auto.toString().equals("")) {
//                        crossC.setVisibility(View.GONE);
//                    } else{
//                        crossC.setVisibility(View.VISIBLE);
//                    }
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

                        country_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcountry);
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

        txt_state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    if (txt_state.getText().toString().equals("")) {
                        tempstate = new ArrayList<>();
                        tempstate.addAll(stateName);
                    }
                    state_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempstate);
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
                country_id = stateList.get(stateName.indexOf(state_Str)).getCountry_code();
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

                        state_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempstate);
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

        txt_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    if (txt_city.getText().toString().equals("")) {
                        tempcity = new ArrayList<>();


                        try {
                            tempcity.addAll(cityName);
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }
                    city_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcity);
                    txt_city.setAdapter(city_adapter);
                    txt_city.showDropDown();

                } else {

                }
            }
        });

        txt_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    city_str = txt_city.getText().toString();
                    city_id = cityList.get(cityName.indexOf(city_str)).getCity_id();

                    Log.d("city_id_of_state", city_id);
                } catch (IndexOutOfBoundsException e) {
                    Log.d("city_id_of_state", e.getMessage());
                }
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

                        city_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcity);
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
        //DateDiff(strCurrentDatef, strCurrentDateTo);

        radioGrppayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioonline) {
                    paymentMode_Id = "1";
                    parentscrollLayout.setVisibility(View.VISIBLE);

//                    total_amount_pack.setText("");
//                    txt_acc_amount.setText("");
//                    ed_consession_member.setText("");
//                    ed_consession_stud.setText("");
//                    ed_hike_per.setText("");
                    radioFreeEvent.setChecked(false);
                    radioatvenue.setChecked(false);

                } else if (checkedId == R.id.radioatvenue) {

                    paymentMode_Id = "2";
                    parentscrollLayout.setVisibility(View.VISIBLE);
//                    total_amount_pack.setText("");
//                    txt_acc_amount.setText("");
//                    ed_consession_member.setText("");
//                    ed_consession_stud.setText("");
//                    ed_hike_per.setText("");
                    radioonline.setChecked(false);
                    radioFreeEvent.setChecked(false);

//                    if(txt_datef.length()>0 || txt_datet.length()>0)
//                    {
//
//                        Log.d("CHECKEONLIKNE22",strCurrentDatef);
//                        Log.d("CHECKEONLIKNE22",strCurrentDateTo);
//                        txt_datet.setText("");
//                        txt_datef.setText("");
//                        allEds.clear();
//                        mTextViewList.clear();
//                        strCurrentDateTo="";
//                        strCurrentDatef="";
//                    }

                } else {
                    paymentMode_Id = "3";
                    parentscrollLayout.setVisibility(View.VISIBLE);
                    radioonline.setChecked(false);
                    radioatvenue.setChecked(false);


                }

            }
        });


//        lnr_add_pack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                acod_amt = 0.0;
//                if (radioonline.isChecked()) {
//                    SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
//                    try {
//                        Date dateBefore = myFormat.parse(strCurrentDatef);
//                        Date dateAfter = myFormat.parse(strCurrentDateTo);
//                        long difference = dateAfter.getTime() - dateBefore.getTime();
//                        daysBetween = (difference / (1000 * 60 * 60 * 24));
//
//                        System.out.println("Number of Days between dates: " + daysBetween);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    Log.d("HINTOFPACKKK11", hint + "");
//
//                    if (daysBetween >= 0) {
//
//                        img_decrse_pack.setVisibility(View.VISIBLE);
//                        if (daysBetween >= hint) {
//
//                            Log.d("HINTOFPACKKK22", hint + "");
//                            //hint++;
//                            createEditTextView(hint, true);
//                            hint++;
//                        } else {
//                           // lnr_add_pack.setVisibility(View.GONE);
//
//                        }
//
//                    }
//
//                } else if (radioatvenue.isChecked()) {
//                    SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
//                    try {
//                        Date dateBefore = myFormat.parse(strCurrentDatef);
//                        Date dateAfter = myFormat.parse(strCurrentDateTo);
//                        long difference = dateAfter.getTime() - dateBefore.getTime();
//                        daysBetween = (difference / (1000 * 60 * 60 * 24));
//
//                        System.out.println("Number of Days between dates: " + daysBetween);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    if (daysBetween > 0) {
//                        img_decrse_pack.setVisibility(View.VISIBLE);
//                        if (daysBetween > hint) {
//                            hint++;
//                            createEditTextView(hint, true);
//                        } else {
//                            lnr_add_pack.setVisibility(View.GONE);
//
//                        }
//
//                    }
//                }
//            }
//        });

//        img_decrse_pack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addbool = false;
//                acod_amt = 0.0;
//                // total_amount.setText("");
//                //  txt_acc_amount.setText("");
//                Log.d("valueofhinttt", hint + "");
//                Log.d("STARTTTDAYY", startDayt + "");
//
//                Log.d("STARTFFDAYY", daysBetween + "");
//
//                if (daysBetween >= 0) {
//                    if (hint >= 0) {
//                        if (daysBetween >= hint - 1) {
//                            if (hint >= 0) {
//                                lnr_add_pack.setVisibility(View.VISIBLE);
//
//
//                                createEditTextView(hint, false);
//
//                                if (hint == 0) {
//                                    hint = 0;
//                                } else {
//                                    hint--;
//                                }
//                            }
//                        } else {
//
//                            Toast.makeText(getApplicationContext(), "Now Package can not add ", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//
//                        img_decrse_pack.setVisibility(View.VISIBLE);
//                        lnr_add_pack.setVisibility(View.VISIBLE);
//                    }
//
//
//                }
//            }
//        });


        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioMyAdd) {

                    current_address_txt.setVisibility(View.GONE);
                    rel_lay_loc.setVisibility(View.GONE);
                    AddressStr = my_address_txt.getText().toString();
                    Log.d("ADRESSSTRRR", AddressStr + "");
                    if (my_address_txt.getText().toString().length() == 0) {
                        MyToast.toastLong(AddConference.this, "First fill the address");
                    } else {
                        my_address_txt.setVisibility(View.VISIBLE);
                        AddressStr = my_address_txt.getText().toString();
                        addres_type = "1";
                        city_id = my_city_id;
                        state_id = my_state_id;
                        country_id = my_country_id;
                    }
                } else if (checkedId == R.id.radioCurrentAdd) {
                    current_address_txt.setVisibility(View.VISIBLE);
                    rel_lay_loc.setVisibility(View.GONE);
                    my_address_txt.setVisibility(View.GONE);
                    //AddressStr = current_address_txt.getText().toString();
                    addres_type = "2";
                    try {
                        current_address_txt.setText(getCompleteAddressString(AddConference.this,
                                Double.parseDouble(AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Latitude, "")),
                                Double.parseDouble(AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Longitude, ""))));

                        AddressStr = current_address_txt.getText().toString();

                    } catch (NumberFormatException nf) {
                        nf.printStackTrace();
                    }

                }

                else if (checkedId == R.id.radioEnterAdd)
                {
                    current_address_txt.setVisibility(View.GONE);
                    rel_lay_loc.setVisibility(View.VISIBLE);
                    my_address_txt.setVisibility(View.GONE);
                    //findViewById(R.id.tm_adres).setVisibility(View.GONE);
                    addres_type = "3";
                    AddressStr = txt_addres.getText().toString();
                    Log.d("yhyhyhy", AddressStr);
                }

                geoLocate();

            }
        });

//        radioGrpAccomad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.radioacmod) {
//
//                    txt_acc_amount.setEnabled(true);
//
//                }
//
//            }
//        });


//        txt_totl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    acod_amt = Double.parseDouble(txt_acc_amount.getText().toString());
//                    Log.d("doublevalueee", acod_amt + "");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (addbool) {
//                    total = 0.0;
//                    for (int i = 0; i < allEds.size(); i++) {
//                        strings = allEds.get(i).getText().toString();
//                        try {
//                            total = total + Double.parseDouble(strings);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    total = total + acod_amt;
//                    total_amount.setText(String.valueOf(total));
//
//                } else {
//                    total = 0.0;
//
//                    for (int i = 0; i < allEds.size(); i++) {
//                        strings = allEds.get(i).getText().toString();
//
//                        try {
//                            total = total + Double.parseDouble(strings);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    total = total + acod_amt;
//                    total_amount.setText(String.valueOf(total));
//
//                }
//            }
//        });
        img_showdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

//        radioGrpDis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.radiomem) {
//
//                    memberdis = "1";
//
//                } else if (checkedId == R.id.radiostud) {
//                    memberdis = "2";
//                }
//            }
//        });

//        radiomem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (radioButtonStateManageModel.isButtonTwoCheckedState()) {
//                    radioButtonStateManageModel.setButtonTwoCheckedState(false);
//
//                    radiomem.setChecked(false);
////                    ed_consession_member.setText("");
//
//                } else {
//                    radioButtonStateManageModel.setButtonTwoCheckedState(true);
//                    radiomem.setChecked(true);
//                }
//            }
//        });
//
//        radiostud.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (radioButtonStateManageModel.isButtonThreeCheckedState()) {
////                    radiomem.setChecked(false);
//                    radiostud.setChecked(false);
////                    ed_consession_stud.setText("");
//                    radioButtonStateManageModel.setButtonThreeCheckedState(false);
//
//                } else {
//                    radiostud.setChecked(true);
//                    radioButtonStateManageModel.setButtonThreeCheckedState(true);
//                }
////                radiomem.setChecked(false);
//
//
//            }
//        });

//        radioGrpDis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(radiomem.isChecked()){
//                    radiomem.setChecked(false);
//                }else if(radiostud.isChecked()){
//                    radiostud.setChecked(false);
//                }
//            }
//        });


        addMorePackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMorePackageDilog(true, -1);
            }
        });

        addMoreForeignPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMoreForeignPackageDialog(true, -1);
            }
        });


    }

    private void getStreetNumber()
    {
        Log.d("SellHospitalActivity", "init: initializing");

        try {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(AddConference.this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(AddConference.this, this)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(AddConference.this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        txt_addres.setAdapter(mPlaceAutocompleteAdapter);

        try
        {
            txt_addres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    AddressStr = txt_addres.getText().toString();
                    geoLocate();
                }
            });

            txt_addres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        AddressStr = txt_addres.getText().toString();
                        Log.d("AddressStr--", AddressStr);
                        geoLocate();
                    }
                }
            });

            txt_addres.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (TextUtils.isEmpty(txt_addres.getText().toString())) {
                        Toast.makeText(AddConference.this, "Enter Street, House No.", Toast.LENGTH_SHORT).show();
                    } else if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                        //execute our method for searching
                        AddressStr = txt_addres.getText().toString();
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


    private void geoLocate() {
        Log.d("SellHospitalActivity", "geoLocate: geolocating");

        //String searchString = txt_addres.getText().toString();
        String searchString = AddressStr;
        streetAddress = txt_addres.getText().toString();

        Log.d("dhsgfskgdsk", streetAddress);
        Geocoder geocoder = new Geocoder(AddConference.this);
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
            Log.d("SellHospitalActivity", "geoLocate: found a location: " + address.toString());
            Log.d("SellHospitalActivity", "geoLocate: found a location: " + addressLat + "  =  " + addressLog);
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

//           moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//                    address.getAddressLine(0));
        }
    }

    private void showAddMoreForeignPackageDialog(final boolean b, final int i) {
        final Dialog dialog = new Dialog(AddConference.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_more_package_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
//        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button dialogCancel = dialog.findViewById(R.id.dialogCancel);
        Button dialogSave = dialog.findViewById(R.id.dialogSave);

        final EditText packageDetailsEdit = dialog.findViewById(R.id.packageDetailsEdit);
        final EditText packagePriceEdit = dialog.findViewById(R.id.packagePriceEdit);


        if (!b) {
            packageDetailsEdit.setText(addPackageForeignArrayList.get(i).getPackageDetails());
            packagePriceEdit.setText(addPackageForeignArrayList.get(i).getPrice() + "");
        }

        dialogSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String packageDetailsStr = packageDetailsEdit.getText().toString();
                String packagePriceStr = packagePriceEdit.getText().toString();

                if (TextUtils.isEmpty(packageDetailsStr)) {
                    packageDetailsEdit.setError("Enter Details");
                    packageDetailsEdit.requestFocus();
                } else if (TextUtils.isEmpty(packagePriceStr)) {
                    packagePriceEdit.setError(" Enter Price");
                    packagePriceEdit.requestFocus();

                } else if (addPackageForeignArrayList.size() > 9 && b) {
                    Toast.makeText(AddConference.this, "Only 10 Packages can be add!", Toast.LENGTH_SHORT).show();
                } else {
                    AddPackageModal addPackageModal = new AddPackageModal();
                    addPackageModal.setPackageDetails(packageDetailsStr);
                    addPackageModal.setPrice(parseInt(packagePriceStr));
                    if (b) {
                        addPackageForeignArrayList.add(addPackageModal);
                    } else {
                        addPackageForeignArrayList.remove(i);
                        addPackageForeignArrayList.add(i, addPackageModal);
                    }

                    dialog.dismiss();
                    setAdapterAddMoreForeignPackage();
                    Toast.makeText(AddConference.this, "Package Save", Toast.LENGTH_SHORT).show();
                    addMoreForeignPackage.setText(getString(R.string.addMore));
                    packageForgeinLL.setVisibility(View.VISIBLE);
                }

            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            address = address.replaceAll(" ", "%20");

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();

            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            getLatLong(jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static boolean getLatLong(JSONObject jsonObject) {

        try {

            double longitute = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("getLatLong", longitute + " = " + latitude);


        } catch (JSONException e) {
            return false;

        }

        return true;
    }

    private void setAdapterAddMoreForeignPackage() {

        AddMorePackageAdapter addMorePackageAdapter = new AddMorePackageAdapter(AddConference.this, addPackageForeignArrayList, 2) {

            @Override
            public void setDataEditNotify(View view, int position) {
                showAddMoreForeignPackageDialog(false, position);
            }

            @Override
            public void setDataDeleteNotify(View view, final int position) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        addPackageModalArrayList.remove(position);
                        setAdapterAddMoreForeignPackage();

                        if (addPackageForeignArrayList.size() == 0) {
                            addMoreForeignPackage.setText(getString(R.string.add_package));
                            packageForgeinLL.setVisibility(View.GONE);
                        }
                    }
                }, 1500);

            }

        };
        addPackageForeignRecyclerView.setAdapter(addMorePackageAdapter);
    }

    private void showAddMorePackageDilog(final boolean b, final int i) {

        final Dialog dialog = new Dialog(AddConference.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_more_package_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
//        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button dialogCancel = dialog.findViewById(R.id.dialogCancel);
        Button dialogSave = dialog.findViewById(R.id.dialogSave);

        final EditText packageDetailsEdit = dialog.findViewById(R.id.packageDetailsEdit);
        final EditText packagePriceEdit = dialog.findViewById(R.id.packagePriceEdit);

        if (!b) {
            packageDetailsEdit.setText(addPackageModalArrayList.get(i).getPackageDetails());
            packagePriceEdit.setText(addPackageModalArrayList.get(i).getPrice() + "");
        }

        dialogSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String packageDetailsStr = packageDetailsEdit.getText().toString();
                String packagePriceStr = packagePriceEdit.getText().toString();

                if (TextUtils.isEmpty(packageDetailsStr)) {
                    packageDetailsEdit.setError("Enter Details");
                    packageDetailsEdit.requestFocus();
                } else if (TextUtils.isEmpty(packagePriceStr)) {
                    packagePriceEdit.setError(" Enter Price");
                    packagePriceEdit.requestFocus();

                } else if (addPackageModalArrayList.size() > 9 && b) {

                    Toast.makeText(AddConference.this, "Only 10 Packages can be add!", Toast.LENGTH_SHORT).show();

                } else {
                    AddPackageModal addPackageModal = new AddPackageModal();
                    addPackageModal.setPackageDetails(packageDetailsStr);
                    addPackageModal.setPrice(parseInt(packagePriceStr));

                    if (b) {

                        addPackageModalArrayList.add(addPackageModal);

                    } else {
                        addPackageModalArrayList.remove(i);
                        addPackageModalArrayList.add(i, addPackageModal);
                    }


                    dialog.dismiss();
                    setAdapterAddMorePackage();
                    Toast.makeText(AddConference.this, "Package Save", Toast.LENGTH_SHORT).show();
                    addMorePackage.setText(getString(R.string.addMore));
                    normalPackLL.setVisibility(View.VISIBLE);
                }
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void setAdapterAddMorePackage() {

        AddMorePackageAdapter addMorePackageAdapter = new AddMorePackageAdapter(AddConference.this, addPackageModalArrayList, 1) {

            @Override
            public void setDataEditNotify(View view, int position) {
                showAddMorePackageDilog(false, position);
            }

            @Override
            public void setDataDeleteNotify(View view, final int position) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        addPackageModalArrayList.remove(position);
                        setAdapterAddMorePackage();
                        if (addPackageModalArrayList.size() == 0) {
                            addMorePackage.setText(getString(R.string.add_package));
                            normalPackLL.setVisibility(View.GONE);
                        }
                    }
                }, 1500);
            }
        };

        addPackageRecyclerView.setAdapter(addMorePackageAdapter);
    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if (item.equalsIgnoreCase("Conference")) {
            txt_conference_type.setVisibility(View.GONE);
            conferenceType_Id = "1";
        } else if (item.equalsIgnoreCase("Exhibition")) {
            txt_conference_type.setVisibility(View.GONE);
            conferenceType_Id = "2";
        } else if (item.equalsIgnoreCase("CME")) {
            txt_conference_type.setVisibility(View.GONE);
            conferenceType_Id = "3";
        } else if (item.equalsIgnoreCase("Training Courses")) {
            txt_conference_type.setVisibility(View.GONE);
            conferenceType_Id = "4";
        } else if (item.equalsIgnoreCase("Seminar")) {
            txt_conference_type.setVisibility(View.GONE);
            conferenceType_Id = "5";
        } else if (item.equalsIgnoreCase("Other")) {
            txt_conference_type.setVisibility(View.VISIBLE);
            conferenceType_Id = "other_conference_type";
        } else {
            txt_conference_type.setVisibility(View.GONE);
        }
        // Showing selected spinner item
        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

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


    public void updateEditText(int hints, ArrayList<ConferencePackageModel> arrayList) {
        String dayList = "";
        edittTxt = new EditText(this);
        textview = new TextView(this);

        edittTxt.setTextAppearance(this, R.style.fontForNotificationLandingPage);
        edittTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        textview.setTextAppearance(this, R.style.fontForNotificationLandingPage);

        RelativeLayout.LayoutParams paramsA = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsA.setMargins(20, 5, 10, 5);
        textview.setText(" Day " + arrayList.get(hints).getConference_pack_day());

        mTextViewList.add(textview);
        for (int i = 0; i < mTextViewList.size(); i++) {
            dayList = mTextViewList.get(i).getText().toString().replace("[", "").replace("]", "");

        }
        conferenceDay.add(dayList);
        Log.d("DAYLISTTTT", dayList);
        Log.d("CONFERDAYLIST", conferenceDay + "");
        paramsA.addRule(RelativeLayout.RIGHT_OF, textview.getId());
        ll.addView(textview);

        edittTxt.setText(arrayList.get(hints).getConference_pack_charge());
        allEds.add(edittTxt);
        ll.addView(edittTxt);


    }


    protected void createEditTextView(int hints, boolean bool) {

        String dayList = "";
        if (bool) {

            edittTxt = new EditText(this);
            textview = new TextView(this);

            edittTxt.setTextAppearance(this, R.style.fontForNotificationLandingPage);
            edittTxt.setInputType(InputType.TYPE_CLASS_NUMBER);


            InputFilter filter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; ++i) {
                        if (!Pattern.compile("[1234567890]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                            return "";
                        }
                    }

                    return null;
                }
            };

            edittTxt.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(7)});

            textview.setTextAppearance(this, R.style.fontForNotificationLandingPage);

            RelativeLayout.LayoutParams paramsA = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsA.setMargins(20, 5, 10, 5);
            textview.setText(" Day " + hints);

            mTextViewList.add(textview);
            for (int i = 0; i < mTextViewList.size(); i++) {
                dayList = mTextViewList.get(i).getText().toString().replace("[", "").replace("]", "");

            }
            conferenceDay.add(dayList);
            Log.d("DAYLISTTTT", dayList);
            Log.d("CONFERDAYLIST", conferenceDay + "");
            paramsA.addRule(RelativeLayout.RIGHT_OF, textview.getId());
            ll.addView(textview);
            edittTxt.setHint("Price" + hints);
            allEds.add(edittTxt);
            ll.addView(edittTxt);


        } else {
            if (hints > 0) {
                ll.removeView(allEds.remove(hints - 1));
                ll.removeView(mTextViewList.remove(hints - 1));
            } else {
                Log.d("hintsvaluechang", hints + "");
                ll.removeView(textview);
                ll.removeView(edittTxt);
                allEds.clear();
                mTextViewList.clear();

            }
        }

    }


    public void showDialog() {

        TextView cancel_txt, conf_txt;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about_conf_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog

        cancel_txt = dialog.findViewById(R.id.txt_cancel);

        dialog.show();
        //TODO : dismiss the on btn click and close click
        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO : finishing the activity
            }
        });

    }

    public void showDialogMedicalProfile() {
        final RecyclerView multi_city_sel_recycler;
        EditText loc_search;
        LinearLayoutManager linearLayoutManager;
        TextView cancel_txt, done_txt;

        final MedicalProfileAdapter menu_recycler_adapter;

        final Dialog dialog = new Dialog(AddConference.this);
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

        linearLayoutManager = new LinearLayoutManager(AddConference.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_city_sel_recycler.setLayoutManager(linearLayoutManager);


        if (show_details) {
            try {
                String[] a = medicalProfileId.split(",");
                for (int j = 0; j < a.length; j++) {
                    Log.e("Afeff", a[j]);

                    for (int i = 0; i < medical_SpinnerList.size(); i++) {
                        Log.e("jabfba", medical_SpinnerList.get(i).getId() + ","
                                + medical_SpinnerList.get(i).getProfile_category_name() + ","
                                + medical_SpinnerList.get(i).getStatus() + ","
                                + medical_SpinnerList.get(i).getClass());
                        if (medical_SpinnerList.get(i).getId().equals(a[j].trim())) {
                            medical_SpinnerList.get(i).setStatus(true);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//


        menu_recycler_adapter = new MedicalProfileAdapter(medical_SpinnerList, AddConference.this, AddConference.this);
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
//                medicalProfileId="";


                for (int i = 0; i < medical_SpinnerList.size(); i++)

                    if (medical_SpinnerList.get(i).getStatus()) {
                        conferenceList.add(medical_SpinnerList.get(i).getProfile_category_name());
                        medicalListId.add(medical_SpinnerList.get(i).getId());
                    }

                medicalProfileId = medicalListId.toString().replace("[", "").replace("]", "");
                Log.d("EDICALPROFILEID", medicalProfileId + "");


                txt_medical_select.setText(conferenceList.toString().replace("[", "").replace("]", ""));

                dialog.dismiss();

            }
        });
    }

    private void getCountyApi() {
        new GetAllCountryApi(AddConference.this) {
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

                            country_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcountry);
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

    private void finishDialog() {
        final Dialog finishDialog = new Dialog(AddConference.this);
        finishDialog.setContentView(R.layout.alert_dialog);

        Button no = finishDialog.findViewById(R.id.noBtn);
        TextView messageTv = finishDialog.findViewById(R.id.messageTv);
        Button yes = finishDialog.findViewById(R.id.yesBtn);


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishDialog.dismiss();
                AddConference.this.finish();
            }
        });

        finishDialog.show();
    }

    private void finishDialog2() {
        final Dialog finishDialog = new Dialog(AddConference.this);
        finishDialog.setContentView(R.layout.alert_dialog);

        Button no = finishDialog.findViewById(R.id.noBtn);

        no.setText("Back");
        TextView messageTv = finishDialog.findViewById(R.id.messageTv);
        messageTv.setText("Please review your event form as it can't be changed once submitted.");
        Button yes = finishDialog.findViewById(R.id.yesBtn);
        yes.setText("Add");

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getLocationInfo(AddressStr);

                fileMultiNme = path_list.toString().replace("[", "").replace("]", "");
                Log.d("ARYLITDAYYY", conferenceDay + "");
                conferenceListDay = conferenceDay.toString().replace("[", "").replace("]", "");//day of conference
                for (int i = 0; i < allEds.size(); i++) {
                    String confrencePrice = allEds.get(i).getText().toString();
                    conferenceCharges.add(confrencePrice);
                    Log.d("prickekeke", confrencePrice);
                }

                conferenceListCharges = conferenceCharges.toString().replace("[", "").replace("]", "");//list charge of conference

                try {
                    if (show_details) {
                        if (isValid()) {
                            try {
                                Log.d("UPPDATFIELL", all_pdf_name);
                                if (all_pdf_name.trim().length() == 0) {
                                    addMyConferencePostApi(country_id, state_id, city_id, "");
                                } else {
                                    addMyConferencePostApi(country_id, state_id, city_id, all_pdf_name);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (isValid()) {
                            try {
                                if (radioonline.isChecked()) {
                                    paymentMode_Id = "1";
                                } else if (radioatvenue.isChecked()) {
                                    paymentMode_Id = "2";
                                } else if (radioFreeEvent.isChecked()) {
                                    paymentMode_Id = "3";
                                }
                                addConferencePostApi();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finishDialog.dismiss();
            }
        });

        finishDialog.show();
    }

    int coin = 0;

    @Override
    public void onBackPressed() {

        finishDialog();
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.txt_medical_select:
//             //   showDialogMedicalProfile();
//                break;
            case R.id.btnBack:

                finishDialog();

                break;
            case R.id.txt_city:
                if (txt_city.getText().toString().equals("")) {
                    tempcity = new ArrayList<>();

                    try {
                        tempcity.addAll(cityName);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                }

                city_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcity);
                txt_city.setAdapter(city_adapter);
                txt_city.showDropDown();
                break;

//            case R.id.txt_specially:
//
//                showDialogTitle();
//
//                break;
            case R.id.txt_datef:
                Calendar cs = Calendar.getInstance();
                int dayOfMonths = cs.get(Calendar.DAY_OF_MONTH);
                int monthOfYears = cs.get(Calendar.MONTH);
                int years = cs.get(Calendar.YEAR);
                dateSet = false;
                //TODO: systemCurrent Date
//                if(coin==0){
                curDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
                currentDate2 = curDate;

                final SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                DatePickerDialog dialog1 = new DatePickerDialog(AddConference.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //TODO: calendar for comparison
                        strCurrentDatef = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        String parts1[] = strCurrentDatef.split("-");
                        String parts2[] = curDate.split("-");

                        Log.e("date", curDate + "newDateee" + strCurrentDatef);

                        int endDay = parseInt(parts2[0]);
                        int endMonth = parseInt(parts2[1]);
                        int endYear = parseInt(parts2[2]);

                        startDayf = parseInt(parts1[0]);
                        int startMonth = parseInt(parts1[1]);
                        int startYear = parseInt(parts1[2]);

//                        if(coin!=0) {

//
//                            if (startYear < endYear) {
//                                Toast.makeText(AddConference.this, "Past date cannot be selected", Toast.LENGTH_SHORT).show();
//                                return;
//                            } else {
//                                if (startYear == endYear) {
//                                    if (startMonth < endMonth) {
//                                        Toast.makeText(AddConference.this, "Past date cannot be selected", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    } else {
//                                        if (startMonth == endMonth) {
//                                            if (startDayf < endDay) {
//                                                Toast.makeText(AddConference.this, "Past date cannot be selected", Toast.LENGTH_SHORT).show();
//                                                return;
//                                            }
//                                        }
//                                    }
//                                }
//                            }

//                            String parts3[] = currentDate2.split("-");
//                            int startdate2=Integer.parseInt(parts3[0]);
//                            int startMonth2=Integer.parseInt(parts3[1]);
//                            int startYear2=Integer.parseInt(parts3[2]);
//
//                            if (startYear2 > endYear && startYear < endYear) {
//                                Toast.makeText(AddConference.this, "Past date cannot be selected", Toast.LENGTH_SHORT).show();
//                                return;
//                            } else {
//                                if (startYear2 == endYear && startYear == endYear) {
//                                    if (startMonth2 > endMonth && startMonth < endMonth) {
//                                        Toast.makeText(AddConference.this, "Past date cannot be selected", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    } else {
//                                        if (startMonth2 == endMonth && startMonth == endMonth ){
//                                            if (startdate2 > endDay && startDayf < endDay) {
//                                                Toast.makeText(AddConference.this, "Past date cannot be selected", Toast.LENGTH_SHORT).show();
//                                                return;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            try {
//                                if(show_details){
//                                    UpdateDateDiff(strCurrentDatef, strCurrentDateTo,conferencePackageList);
//                                }else{
//                                    DateDiff(strCurrentDatef, strCurrentDateTo);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

//                        }

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        // Log.d("newDate",newDate.toString());
                        Date current = newDate.getTime();
                        // Log.d("currentDate",current.toString());
                        int diff = new Date().compareTo(current);


                        try {

                            txt_datef.setText(simpleDateFormats.format(newDate.getTime()));
                            txt_datet.setText("");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, years, monthOfYears, dayOfMonths);
                dialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog1.show();
                break;
            case R.id.txt_datet:
                Calendar css = Calendar.getInstance();
                int dayOfMonthss = css.get(Calendar.DAY_OF_MONTH);
                int monthOfYearss = css.get(Calendar.MONTH);
                int yearss = css.get(Calendar.YEAR);
                dateSet = true;
                //TODO: systemCurrent Date
                curDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
                final SimpleDateFormat simpleDateFormatss = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                DatePickerDialog dialog11 = new DatePickerDialog(AddConference.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //TODO: calendar for comparison
                        strCurrentDateTo = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        String parts1[] = strCurrentDateTo.split("-");


                        try {
                            if (!strCurrentDatef.equals("")) {
                                String parts2[] = strCurrentDatef.split("-");

                                Log.e("date", curDate + "newDateee" + strCurrentDateTo);

                                int endDay = parseInt(parts2[0]);
                                int endMonth = parseInt(parts2[1]);
                                int endYear = parseInt(parts2[2]);

                                startDayt = parseInt(parts1[0]);
                                int startMonth = parseInt(parts1[1]);
                                int startYear = parseInt(parts1[2]);

                                if (startYear < endYear) {
                                    Toast.makeText(AddConference.this, "Date cannot be selected as conference ending before this date", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    if (startYear == endYear) {
                                        if (startMonth < endMonth) {
                                            Toast.makeText(AddConference.this, "Date cannot be selected", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            if (startMonth == endMonth) {
                                                if (startDayt < endDay) {
                                                    Toast.makeText(AddConference.this, "Date cannot be selected", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                String msgg = "Please select \"from date\" first";
                                Toast.makeText(AddConference.this, msgg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String msgg = "Please select \"from date\" first";
                            Toast.makeText(AddConference.this, msgg, Toast.LENGTH_SHORT).show();
                        }
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        // Log.d("newDate",newDate.toString());
                        Date current = newDate.getTime();

                        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

                        try {

                            if (!strCurrentDatef.equals("")) {
                                Date dateBefore = myFormat.parse(strCurrentDatef);
                                Date dateAfter = myFormat.parse(strCurrentDateTo);
                                long difference = dateAfter.getTime() - dateBefore.getTime();
                                bewdaybetween = (difference / (1000 * 60 * 60 * 24));
                                if (bewdaybetween > 9) {
                                    Toast.makeText(AddConference.this, "can not select more than 10 days", Toast.LENGTH_SHORT).show();
                                    txt_datet.setText("");
                                } else {
                                    txt_datet.setText(simpleDateFormatss.format(newDate.getTime()));
                                    coin++;
                                    currentDate2 = txt_datet.getText().toString();
                                    try {
                                        if (show_details) {
                                            UpdateDateDiff1(strCurrentDatef, strCurrentDateTo);
                                        } else {
                                            DateDiff(strCurrentDatef, strCurrentDateTo);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                System.out.println("Number of Days between dates: " + bewdaybetween);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, yearss, monthOfYearss, dayOfMonthss);
                dialog11.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog11.show();
                break;

            case R.id.txt_timef:
                timeY = true;
                timePickerDialog.show(this.getSupportFragmentManager(), "Time Picker");
                break;
            case R.id.txt_timet:
                timeY = false;
                timePickerDialog.show(this.getSupportFragmentManager(), "Time Picker");
                break;
            case R.id.img_broucher:

                if (ActivityCompat.checkSelfPermission(AddConference.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&

                        ActivityCompat.checkSelfPermission(AddConference.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(AddConference.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PICK_CAMERA_REQUEST);
                }

                Intent chooseIntent = new Intent(AddConference.this, FileSelectionActivity.class);
                startActivityForResult(chooseIntent, PICK_FILE_MULTIPLE);

                break;
            case R.id.btnAddConfer:

                finishDialog2();

                // isValid();
                break;
            case R.id.img_add:
                lnr_online_1.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_pice_date:
                txt_pice_date.setText("");
                Calendar cs1 = Calendar.getInstance();
                int dayOfMonths1 = cs1.get(Calendar.DAY_OF_MONTH);
                int monthOfYears1 = cs1.get(Calendar.MONTH);
                int years1 = cs1.get(Calendar.YEAR);
                dateSetpicker = false;
                //TODO: systemCurrent Date

                if (currentDate2 != null) {

                    if (!currentDate2.equals("")) {
                        curpickDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
                        final SimpleDateFormat simpleDateFormats1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        DatePickerDialog dialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                try {
                                    //TODO: calendar for comparison
                                    String strCurrentDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                    String parts1[] = strCurrentDate.split("-");
                                    // String parts2[] = currentDate2.split("-");
                                    String parts2[] = strCurrentDatef.split("-");

                                    Log.e("datefromwill", currentDate2 + "newDateee" + strCurrentDate);

                                    int endDay = parseInt(parts2[0]);
                                    int endMonth = parseInt(parts2[1]);
                                    int endYear = parseInt(parts2[2]);

                                    int startDay = parseInt(parts1[0]);
                                    int startMonth = parseInt(parts1[1]);
                                    int startYear = parseInt(parts1[2]);

                                    if (startYear > endYear) {
                                        Toast.makeText(AddConference.this, invalidDateMsg(), Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        if (startYear == endYear) {
                                            if (startMonth > endMonth) {
                                                Toast.makeText(AddConference.this, invalidDateMsg(), Toast.LENGTH_SHORT).show();
                                                return;
                                            } else {
                                                if (startMonth == endMonth) {
                                                    if (startDay > endDay) {
                                                        Toast.makeText(AddConference.this, invalidDateMsg(), Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Calendar newDate = Calendar.getInstance();
                                    newDate.set(year, monthOfYear, dayOfMonth);
                                    // Log.d("newDate",newDate.toString());
                                    Date current = newDate.getTime();

                                    int diff = new Date().compareTo(current);
                                    txt_pice_date.setText(simpleDateFormats1.format(newDate.getTime()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, years1, monthOfYears1, dayOfMonths1);
                        dialog2.show();
                    } else {
                        Toast.makeText(this, "Please select conference date first", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please select conference date first", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }

    }

    private String invalidDateMsg() {
        return "Date cannot be selected, as conference starts on " + strCurrentDatef;
    }

    public void UpdateDateDiff1(String strtDate, String endDate) {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

        hint = 1;
        conferenceDay.clear();
        //        mTextViewList.clear();
        allEds.clear();
        try {

            Date dateBefore = myFormat.parse(strtDate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            daysBetween = (difference / (1000 * 60 * 60 * 24));

            System.out.println("Number of Days between datessss: " + daysBetween);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ll.removeAllViews();

        daysBetween = ++daysBetween;
        Log.e("Number_of_Days", daysBetween + "");
        if (daysBetween >= 0) {
            img_decrse_pack.setVisibility(View.GONE);

            do {
                createEditTextView(hint, true);
                hint++;

            } while (daysBetween >= hint);

        }

    }

    public void UpdateDateDiff(String strtDate, String endDate, ArrayList<ConferencePackageModel> arrayList) {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        hint = 0;
        conferenceDay.clear();
        //        mTextViewList.clear();
        allEds.clear();
        try {

            Date dateBefore = myFormat.parse(strtDate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            daysBetween = (difference / (1000 * 60 * 60 * 24));

            System.out.println("Number of Days between datessss: " + daysBetween);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ll.removeAllViews();

        daysBetween = ++daysBetween;
        Log.e("Number_of_Days", daysBetween + "");
        if (daysBetween >= 0) {
            img_decrse_pack.setVisibility(View.GONE);

            do {
                updateEditText(hint, arrayList);
                hint++;

            } while (daysBetween >= hint);

        }

    }

    public String getCurrentDate() {

        String cd;
        Calendar css = Calendar.getInstance();
        int dayOfMonthss = css.get(Calendar.DAY_OF_MONTH);
        int monthOfYearss = css.get(Calendar.MONTH);
        int yearss = css.get(Calendar.YEAR);
        dateSet = true;
        //TODO: systemCurrent Date
        cd = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
        return cd;
    }

    public void DateDiff(String strtDate, String endDate) {

        Log.e("asfg", strtDate + "," + endDate);
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        hint = 1;
        conferenceDay.clear();
//        mTextViewList.clear();
        allEds.clear();

        try {
            Date dateBefore = myFormat.parse(strtDate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            daysBetween = (difference / (1000 * 60 * 60 * 24));


        } catch (ParseException e) {
            e.printStackTrace();
        }

        ll.removeAllViews();

        daysBetween = ++daysBetween;
        Log.e("Number_of_Days", daysBetween + "");

        if (daysBetween >= 0) {
            img_decrse_pack.setVisibility(View.GONE);

            do {
                createEditTextView(hint, true);
                hint++;
            } while (daysBetween >= hint);

        }

    }

    private void openGoogleLocatiion(String sms, final  EditText locationTv)
    {
        PlacesClient placesClient;

        //String apiKey = "AIzaSyCUVWRZZji7uyu0xZdwYgC1q3xRJdReJ_Q";
        String apiKey = getString(R.string.googlePlaceAPI);
        Utils.sop("apiKey" + apiKey);

        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(AddConference.this, apiKey);
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
                locationTv.setText(place.getName()+", "+place.getAddress());
                AddressStr = place.getAddress();
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

    private void openPdfIntent() {
        //TODO: calling the intent to open pdf

        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            Log.d("PickedPdfPath: ", fileUri.toString());
            //TODO: Calling the upload here
            if (!fileUri.equals(null)) {
                // getPdfPath();
            }
        } else if (requestCode == PICK_FILE_MULTIPLE) {
            Log.d("FILESSSS", "PFPFPFPF" + "");
            path_list_file.clear();
            if (data != null) {
                ArrayList<File> Files = (ArrayList<File>) data.getSerializableExtra(FILES_TO_UPLOAD); //file array list
                Log.d("FILESSSS", Files + "");
                //string array
                int totCount = Files.size() + fl;

                if (totCount > 3) {
                    Toast.makeText(this, "Maximum 3 files can be uploaded.", Toast.LENGTH_SHORT).show();
                } else if (Files.size() <= 3) {
                    HashMap<String, String> map = new HashMap<String, String>();

                    for (File file : Files) {

                        filename = file.getName();
                        filePath = file.getPath();
                        Log.d("filepathaaa", filePath.toString());
                        Log.d("filnameeeeeeee", filename.toString());

                        updatefileName = updatefileName + filename + ",";

                        map.put("filePath", filePath);
                        map.put("filename", filename);
                        path_list_file.add(map);

                        getPdfPath(filePath, filename);
                    }

                } else {
                    Toast.makeText(this, "Max 3 file select.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void addTextChangedListener() {

        ed_hike_per.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!ed_hike_per.getText().toString().trim().equals("")) {
                    int asd = parseInt(ed_hike_per.getText().toString());
                    if (asd > 100) {
                        ed_hike_per.setFocusable(true);
                        ed_hike_per.setText("");
                        Toast.makeText(AddConference.this, "Discount should not exceed 100", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        edit_gst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edit_gst.getText().toString().trim().equals("")) {
                    int asd = parseInt(edit_gst.getText().toString());
                    if (asd > 50) {
                        edit_gst.setFocusable(true);
                        edit_gst.setText("");
                        Toast.makeText(AddConference.this, "GST % should not exceed 50", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        no_of_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!no_of_discount.getText().toString().trim().equals("")) {
                    int asd = parseInt(no_of_discount.getText().toString());
                    if (asd > 100) {
                        no_of_discount.setFocusable(true);
                        no_of_discount.setText("");
                        Toast.makeText(AddConference.this, "Discount should not exceed 100", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        //startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private boolean isValid() {

        boolean isValid = false;

        if (show_details) {
            if (TextUtils.isEmpty(conferenceType_Id)) {
                MyToast.toastLong(AddConference.this, "Conference Type is required!");
                return false;
            } else if (TextUtils.isEmpty(medicalProfileId)) {
                MyToast.toastLong(AddConference.this, "Medical Profile id is required!");
                return false;
            } else if (TextUtils.isEmpty(txt_conferenc_name.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Conference Name is required!");
                return false;
            } else if (TextUtils.isEmpty(txt_datef.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Conference Start Date is required!");
                return false;
            } else if (TextUtils.isEmpty(txt_datet.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Conference End Date is required!");
                return false;
            } else if (TextUtils.isEmpty(txt_timef.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Conference From Time is required!");
                return false;
            } else if (TextUtils.isEmpty(txt_timet.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Conference To Time is required!");
                return false;
            } else if (country_id == null || country_id.equalsIgnoreCase("")) {
                MyToast.toastLong(AddConference.this, "Please Enter country!");
                return false;
            } else if (state_id.equalsIgnoreCase("")) {
                MyToast.toastLong(AddConference.this, "Please Enter state!");
                return false;
            } else if (city_id.equalsIgnoreCase("")) {
                MyToast.toastLong(AddConference.this, "Please Enter city!");
                return false;
            } else if (rel_lay_loc.getVisibility() == View.VISIBLE) {
                if (txt_addres.getText().toString().length() == 0) {
                    MyToast.toastLong(AddConference.this, "Please Enter Street.House No.");
                    return false;
                } else if (TextUtils.isEmpty(txt_event_host.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Event Host is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_available_seta.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "No. Of Seat is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_conctact.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Contact Number is required!");
                    return false;
                } else if (TextUtils.isEmpty(specializationId)) {
                    MyToast.toastLong(AddConference.this, "Specialization id  is required!");
                    return false;
                } else if (TextUtils.isEmpty(medicalSpecialityDepartmentID)) {
                    MyToast.toastLong(AddConference.this, "Department id  is required!");
                    return false;
                } else if (TextUtils.isEmpty(edit_credit_earn.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Credit Earning is required!");
                } else if (TextUtils.isEmpty(txt_uploadbrochures.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Please upload brochure to continue!");
                    return false;
                } else if (TextUtils.isEmpty(txt_eventsponcer.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Keynote Speaker is required!");
                    return false;
                }
//            else if (TextUtils.isEmpty(total_amount_pack.getText().toString())) {
//                MyToast.toastLong(AddConference.this, "Total Amount is required!");
//                return false;
//            }

                else if (addPackageModalArrayList.size() < 1) {
                    MyToast.toastLong(AddConference.this, "Please add normal packages");
                    return false;
                }

//            else if (txt_pice_date.getText().toString().length() == 0) {
//                MyToast.toastLong(AddConference.this, "Please select price hike date");
//                return false;
//            }

//                else if (ed_hike_per.getText().toString().length() == 0) {
//                    MyToast.toastLong(AddConference.this, "Please select price hike %");
//                    return false;
//                }
//
                else if (TextUtils.isEmpty(txt_conf_desc.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference Description is required!");
                    return false;
                } else {
                    isValid = true;
                }
            } else if (TextUtils.isEmpty(txt_event_host.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Event Host is required!");
                return false;
            } else if (TextUtils.isEmpty(txt_available_seta.getText().toString())) {
                MyToast.toastLong(AddConference.this, "No. Of Seat is required!");
                return false;
            } else if (TextUtils.isEmpty(txt_conctact.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Contact Number is required!");
                return false;
            } else if (TextUtils.isEmpty(specializationId)) {
                MyToast.toastLong(AddConference.this, "Specialization id  is required!");
                return false;
            } else if (TextUtils.isEmpty(medicalSpecialityDepartmentID)) {
                MyToast.toastLong(AddConference.this, "Department id  is required!");
                return false;
            } else if (TextUtils.isEmpty(edit_credit_earn.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Credit Earning is required!");
            } else if (TextUtils.isEmpty(txt_uploadbrochures.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Please upload brochure to continue!");
                return false;
            } else if (TextUtils.isEmpty(txt_eventsponcer.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Keynote Speaker is required!");
                return false;
            }
//            else if (TextUtils.isEmpty(total_amount_pack.getText().toString())) {
//                MyToast.toastLong(AddConference.this, "Total Amount is required!");
//                return false;
//            }

            else if (addPackageModalArrayList.size() < 1) {
                MyToast.toastLong(AddConference.this, "Please add normal packages");
                return false;
            }

//            else if (txt_pice_date.getText().toString().length() == 0) {
//                MyToast.toastLong(AddConference.this, "Please select price hike date");
//                return false;
//            }

//            else if (ed_hike_per.getText().toString().length() == 0) {
//                MyToast.toastLong(AddConference.this, "Please select price hike %");
//                return false;
//            }
//
            else if (TextUtils.isEmpty(txt_conf_desc.getText().toString())) {
                MyToast.toastLong(AddConference.this, "Conference Description is required!");
                return false;
            } else {
                isValid = true;
            }
        } else {
            if (txt_addres.getText().toString().length() > 0) {
                if (TextUtils.isEmpty(medicalProfileId)) {
                    MyToast.toastLong(AddConference.this, "Medical Profile id is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_conferenc_name.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference Name is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_datef.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference Start Date is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_datet.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference End Date is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_timef.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference From Time is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_timet.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference To Time is required!");
                    return false;
                } else if (country_id == null || country_id.equalsIgnoreCase("")) {
                    MyToast.toastLong(AddConference.this, "Please Enter country!");
                    return false;
                } else if (state_id.equalsIgnoreCase("")) {
                    MyToast.toastLong(AddConference.this, "Please Enter state!");
                    return false;
                } else if (city_id.equalsIgnoreCase("")) {
                    MyToast.toastLong(AddConference.this, "Please Enter city!");
                    return false;
                } else if (rel_lay_loc.getVisibility() == View.VISIBLE) {
                    if (txt_addres.getText().toString().length() == 0) {
                        MyToast.toastLong(AddConference.this, "Please Enter Street.House No.");
                        return false;
                    } else {
                        if (TextUtils.isEmpty(txt_available_seta.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "No. Of Seat is required!");
                            return false;
                        } else if (TextUtils.isEmpty(txt_event_host.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Event Host is required!");
                            return false;
                        } else if (TextUtils.isEmpty(txt_conctact.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Contact Number is required!");
                            return false;
                        } else if (TextUtils.isEmpty(edit_credit_earn.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Credit Earning is required!");
                            return false;
                        } else if (TextUtils.isEmpty(specializationId)) {
                            MyToast.toastLong(AddConference.this, "Specialization id  is required!");
                            return false;
                        } else if (TextUtils.isEmpty(medicalSpecialityDepartmentID)) {
                            MyToast.toastLong(AddConference.this, "Department id  is required!");
                            return false;
                        } else if (TextUtils.isEmpty(txt_uploadbrochures.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Please upload brochure to continue!");
                            return false;
                        } else if (TextUtils.isEmpty(txt_eventsponcer.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Keynote Speaker is required!");
                            return false;
                        }
//                else if (TextUtils.isEmpty(total_amount_pack.getText().toString())) {
//                    MyToast.toastLong(AddConference.this, "Total Amount is required!");
//                    return false;
//                }

                        else if (addPackageModalArrayList.size() < 1) {
                            MyToast.toastLong(AddConference.this, "Please add normal packages");
                            return false;
                        }

//                else if (txt_pice_date.getText().toString().length() == 0) {
//                    MyToast.toastLong(AddConference.this, "Please select price hike date");
//                    return false;
//                }
//                        else if (ed_hike_per.getText().toString().length() == 0) {
//                            MyToast.toastLong(AddConference.this, "Please select price hike %");
//                            return false;
//                        }
//
                        else if (TextUtils.isEmpty(txt_conf_desc.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Conference Description is required!");
                            return false;
                        } else {
                            isValid = true;
                        }
                    }

                } else if (TextUtils.isEmpty(txt_available_seta.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "No. Of Seat is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_event_host.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Event Host is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_conctact.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Contact Number is required!");
                    return false;
                } else if (TextUtils.isEmpty(edit_credit_earn.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Credit Earning is required!");
                    return false;
                } else if (TextUtils.isEmpty(specializationId)) {
                    MyToast.toastLong(AddConference.this, "Specialization id  is required!");
                    return false;
                } else if (TextUtils.isEmpty(medicalSpecialityDepartmentID)) {
                    MyToast.toastLong(AddConference.this, "Department id  is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_uploadbrochures.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Please upload brochure to continue!");
                    return false;
                } else if (TextUtils.isEmpty(txt_eventsponcer.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Keynote Speaker is required!");
                    return false;
                }
//                else if (TextUtils.isEmpty(total_amount_pack.getText().toString())) {
//                    MyToast.toastLong(AddConference.this, "Total Amount is required!");
//                    return false;
//                }

                else if (addPackageModalArrayList.size() < 1) {
                    MyToast.toastLong(AddConference.this, "Please add normal packages");
                    return false;
                }

//                else if (txt_pice_date.getText().toString().length() == 0) {
//                    MyToast.toastLong(AddConference.this, "Please select price hike date");
//                    return false;
//                }
//                else if (ed_hike_per.getText().toString().length() == 0) {
//                    MyToast.toastLong(AddConference.this, "Please select price hike %");
//                    return false;
//                }

                else if (TextUtils.isEmpty(txt_conf_desc.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference Description is required!");
                    return false;
                } else {
                    isValid = true;
                }
            } else {
                if (TextUtils.isEmpty(medicalProfileId)) {
                    MyToast.toastLong(AddConference.this, "Please select target audience!");
                    return false;
                } else if (TextUtils.isEmpty(txt_conferenc_name.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference Name is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_datef.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference Start Date is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_datet.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference End Date is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_timef.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference From Time is required!");
                    return false;
                } else if (TextUtils.isEmpty(txt_timet.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference To Time is required!");
                    return false;
                } else if (country_id == null || country_id.equalsIgnoreCase("")) {
                    MyToast.toastLong(AddConference.this, "Please Enter country!");
                    return false;
                } else if (txt_state.getText().toString().length() == 0) {
                    MyToast.toastLong(AddConference.this, "Please Enter state!");
                    return false;
                } else if (txt_city.getText().toString().length() == 0) {
                    MyToast.toastLong(AddConference.this, "Please Enter city!");
                    return false;
                } else if (rel_lay_loc.getVisibility() == View.VISIBLE) {
                    if (txt_addres.getText().toString().length() == 0) {
                        MyToast.toastLong(AddConference.this, "Please Enter Street.House No.");
                        return false;
                    } else {
                        if (TextUtils.isEmpty(txt_available_seta.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "No. Of Seat is required!");
                            txt_available_seta.requestFocus();
                            return false;
                        } else if (TextUtils.isEmpty(txt_event_host.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Event Host is required!");
                            return false;
                        } else if (titleNewList.size() < 1) {
                            MyToast.toastLong(AddConference.this, "Select target Audience Speciality");
                            return false;
                        } else if (TextUtils.isEmpty(txt_conctact.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Contact Number is required!");
                            return false;
                        } else if (TextUtils.isEmpty(specializationId)) {
                            MyToast.toastLong(AddConference.this, "Specialization id  is required!");
                            return false;
                        } else if (TextUtils.isEmpty(medicalSpecialityDepartmentID)) {
                            MyToast.toastLong(AddConference.this, "Department id  is required!");
                            return false;
                        } else if (TextUtils.isEmpty(edit_credit_earn.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Credit Earning is required!");
                            return false;
//                } else if (TextUtils.isEmpty(edit_gst.getText().toString())) {
//                    MyToast.toastLong(AddConference.this, "GST is required");
//                    edit_gst.requestFocus();
//                    return false;
                        } else if (TextUtils.isEmpty(txt_uploadbrochures.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Please upload brochure to continue!");
                            return false;
                        } else if (TextUtils.isEmpty(txt_eventsponcer.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "KeyNote Speaker is required!");
                            return false;
                        } else if (addPackageModalArrayList.size() < 1) {
                            MyToast.toastLong(AddConference.this, "Please add normal packages");
                            return false;
                        }
//                else if (txt_pice_date.getText().toString().length() == 0) {
//                    MyToast.toastLong(AddConference.this, "Please select price hike date");
//                    return false;
//                }

//                        else if (ed_hike_per.getText().toString().length() == 0) {
//                            MyToast.toastLong(AddConference.this, "Please select price hike %");
//                            return false;
//                        }
                        //else if (TextUtils.isEmpty(total_amount_pack.getText().toString())) {
//                    MyToast.toastLong(AddConference.this, "Total Amount is required!");
//                    return false;
                        else if (TextUtils.isEmpty(txt_conf_desc.getText().toString())) {
                            MyToast.toastLong(AddConference.this, "Conference Description is required!");
                            return false;
                        } else {
                            isValid = true;
                        }
                    }
                } else if (TextUtils.isEmpty(txt_available_seta.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "No. Of Seat is required!");
                    txt_available_seta.requestFocus();
                    return false;
                } else if (TextUtils.isEmpty(txt_event_host.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Event Host is required!");
                    return false;
                } else if (titleNewList.size() < 1) {
                    MyToast.toastLong(AddConference.this, "Select target Audience Speciality");
                    return false;
                } else if (TextUtils.isEmpty(txt_conctact.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Contact Number is required!");
                    return false;
                } else if (TextUtils.isEmpty(specializationId)) {
                    MyToast.toastLong(AddConference.this, "Specialization id  is required!");
                    return false;
                } else if (TextUtils.isEmpty(medicalSpecialityDepartmentID)) {
                    MyToast.toastLong(AddConference.this, "Department id  is required!");
                    return false;
                } else if (TextUtils.isEmpty(edit_credit_earn.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Credit Earning is required!");
                    return false;
//                } else if (TextUtils.isEmpty(edit_gst.getText().toString())) {
//                    MyToast.toastLong(AddConference.this, "GST is required");
//                    edit_gst.requestFocus();
//                    return false;
                } else if (TextUtils.isEmpty(txt_uploadbrochures.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Please upload brochure to continue!");
                    return false;
                } else if (TextUtils.isEmpty(txt_eventsponcer.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "KeyNote Speaker is required!");
                    return false;
                } else if (addPackageModalArrayList.size() < 1) {
                    MyToast.toastLong(AddConference.this, "Please add normal packages");
                    return false;
                }

//                else if (txt_pice_date.getText().toString().length() == 0) {
//                    MyToast.toastLong(AddConference.this, "Please select price hike date");
//                    return false;
//                }

//                else if (ed_hike_per.getText().toString().length() == 0) {
//                    MyToast.toastLong(AddConference.this, "Please select price hike %");
//                    return false;
//                }
                //else if (TextUtils.isEmpty(total_amount_pack.getText().toString())) {
//                    MyToast.toastLong(AddConference.this, "Total Amount is required!");
//                    return false;
                else if (TextUtils.isEmpty(txt_conf_desc.getText().toString())) {
                    MyToast.toastLong(AddConference.this, "Conference Description is required!");
                    return false;
                } else {

                    isValid = true;
                }
            }
        }
        return isValid;
    }

    ArrayList<Integer> t4 = new ArrayList<Integer>();


    private void getPdfPath(String pdf_path, String pdfName) {
        //TODO: getting the pdf path and pdfFile name

        try {

            Log.d("pdfName :", pdfName);
            Log.d("pdfPath :", pdf_path);

            //  url = "http://162.243.205.148/cynapce/apis/fileUpload/brochures";
            url = AppConstantClass.HOST + "fileUpload/brochures";

            try {
                uploadFile(new File(pdf_path), url, pdfName, "file");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (NullPointerException ne) {
            //MyToast.toastLong(getActivity(), "You need to upload files which are stored locally!");
            ne.printStackTrace();
        }
    }

    //TODO: used to upload the pdf to the server
    void uploadFile(File file, final String url, String name, String type) {
        Log.e("file_name", ":" + file);
        PostImage post = new PostImage(file, url, name, AddConference.this, type) {
            //     {
//                "Cynapse": {
//                "res_code": "1",
//                        "res_msg": "File Uploaded Successfuly.",
//                        "sync_time": 1521888468,
//                        "file_name": "0706444001521888468.pdf"
//                           }
//                  }
            @Override
            public void receiveData(String response) {
                try {
                    JSONObject response1 = new JSONObject(response);
                    JSONObject data = response1.getJSONObject("Cynapse");
                    MyToast.logMsg("jsonImage", data.toString());
                    String res = data.getString("res_code");
                    String res1 = data.getString("res_msg");


                    if (res.equals("1")) {
                        pdf_name = data.getString("file_name");
                        AppCustomPreferenceClass.writeString(getApplicationContext(), AppCustomPreferenceClass.all_pdf_list, pdf_name);

                        allpdfName.add(AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.all_pdf_list, ""));

                        all_pdf_name = allpdfName.toString().replace("[", "").replace("]", "");

                        txt_uploadbrochures.setText(all_pdf_name);
                        MyToast.toastLong(AddConference.this, "Your file have been uploaded successfully");
                        fl++;
                    } else {
                        MyToast.toastLong(AddConference.this, res1);
                    }
                } catch (Exception e) {
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


    private String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {

        String strAdd = "";

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
            // AddressStr =address;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return strAdd;
    }

    private void clearListData() {

        specializationList.clear();
        medical_SpinnerList.clear();
        titleList.clear();
    }

    private void getMedicalProfileApi() {
        new GetMedicalProfileApi(AddConference.this) {
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

                        JSONArray header2 = header.getJSONArray("ProfileCategoryMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            MedicalProfileModel medicalProfileModel = new MedicalProfileModel();
                            medicalProfileModel.setId(item.getString("id"));
                            medicalProfileModel.setProfile_category_name(item.getString("profile_category_name"));
                            dept_SpinnerList.add(medicalProfileModel);
                            medical_SpinnerList = getModel(false);
                        }


                        if (show_details) {
                            String[] a = medicalProfileId.split(",");
                            String b[] = new String[a.length];

                            for (int j = 0; j < a.length; j++) {
                                Log.e("Afeff", a[j]);

                                for (int k = 0; k < medical_SpinnerList.size(); k++) {

                                    if (medical_SpinnerList.get(k).getId().equals(a[j].trim())) {
                                        b[j] = medical_SpinnerList.get(k).getProfile_category_name();
                                    }
                                }
                            }
                            txt_medical_select.setText(TextUtils.join(",", b));
                        }
                    } else {

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

    private void getProfileSpecialization(String id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("profile_category_id", id);
        header.put("Cynapse", params);
        new GetSpecializationApi(AddConference.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSESPECIAL", response.toString());
                    if (res_code.equals("1")) {

                        handler.deleteTableName(DatabaseHelper.TABLE_SPECIALITY_DETAILS);
                        titleList.clear();
                        JSONArray header2 = header.getJSONArray("ProfileSpecializationMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            JobSpecializationModel model = new JobSpecializationModel();
                            model.setProfile_category_id(item.getString("profile_category_id"));
                            model.setSpecialization_id(item.getString("specialization_id"));
                            model.setSpecialization_name(item.getString("specialization_name"));


                            titleList.add(model);
                            specializationList = getModels(false);

                            if (show_details) {

                                String[] a = speciality.split(",");
                                String b[] = new String[a.length];

                                for (int j = 0; j < a.length; j++) {
                                    Log.e("Afeff", a[j]);

                                    for (int k = 0; k < specializationList.size(); k++) {
                                        Log.e("jabfba", specializationList.get(i).getSpecialization_id() + ",");
                                        if (specializationList.get(k).getSpecialization_id().equals(a[j].trim())) {
                                            b[j] = specializationList.get(k).getSpecialization_name();
                                        }
                                    }
                                }
                                txt_specially.setText(TextUtils.join(",", b));
                            }

                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SPECIALITY_DETAILS, DatabaseHelper.specialization_id, item.getString("specialization_id"))) {

                                handler.AddSpeciality(model, true);

                            } else {

                                handler.AddSpeciality(model, false);
                            }

                        }
                        arrayList1 = handler.getSpeciality(DatabaseHelper.TABLE_SPECIALITY_DETAILS, "7");
                    } else {
                        titleList.clear();
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

    private void getTargetDepartment(String id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("profile_category_id", id);
        header.put("Cynapse", params);
        new GetTargetDepartment(AddConference.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSESPECIAL", response.toString());

                    if (res_code.equals("1")) {

                        JSONArray header2 = header.getJSONArray("Department");

                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            GetTargetDepartmentModel model = new GetTargetDepartmentModel();
                            model.setDepartment_id(item.getString("department_id"));
                            model.setDepartment_name(item.getString("department_name"));
                            model.setMedical_profile_id(item.getString("medical_profile_id"));
                            model.setMedical_profile_name(item.getString("medical_profile_name"));
                            model.setType(item.getString("type"));
                            model.setStatus(Boolean.parseBoolean(item.getString("status")));

                            titleListDepartment.add(model);
                            medicalSpecialityDepartmentList = getModelsDepartment(false);
                        }

                    } else {

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


    private void addMyConferencePostApi(String country_ID, String state_ID, String city_ID, String updatFile) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("total_days_price", total_amount_pack.getText().toString());
        params.put("accomdation", txt_acc_amount.getText().toString());
        params.put("member_concessions", "");
        params.put("student_concessions", "");
//        params.put("member_concessions", ed_consession_member.getText().toString());
//        params.put("student_concessions", ed_consession_stud.getText().toString());
        params.put("price_hike_after_percentage", ed_hike_per.getText().toString());
        params.put("price_hike_after_date", txt_pice_date.getText().toString());
        params.put("conference_name", txt_conferenc_name.getText().toString());
        params.put("from_date", txt_datef.getText().toString());
        params.put("to_date", txt_datet.getText().toString());
        params.put("from_time", txt_timef.getText().toString());
        params.put("to_time", txt_timet.getText().toString());

        params.put("venue", venue);
        params.put("address_type", address_type);
        params.put("event_host", txt_event_host.getText().toString());

        params.put("speciality", specializationId);
        params.put("contact", txt_conctact.getText().toString());
        params.put("available_seat", txt_available_seta.getText().toString());
        params.put("conference_description", txt_conf_desc.getText().toString());
        params.put("medical_profile_id", medicalProfileId);
        params.put("conference_type_id", conferenceType_Id);
        params.put("otherconferencetype", txt_conference_type.getText().toString());
        params.put("keynote_speakers", txt_eventsponcer.getText().toString());
        params.put("payment_mode", paymentMode_Id);
        params.put("credit_earnings", edit_credit_earn.getText().toString());
        params.put("perticular_for_country", country_ID);
        params.put("perticular_for_state", state_ID);
        params.put("perticular_for_city", city_ID);

        // params.put("cost", txt_cost.getText().toString());
        // params.put("duration", txt_duration.getText().toString());

        params.put("latitude", AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Latitude, ""));

        params.put("longitude", AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Longitude, ""));
        params.put("upload_brochures", updatFile);
//
//        if (txt_uploadbrochures.length() > 0) {
//            Log.d("KDKDKDKDKD",txt_uploadbrochures.getText().toString());
//            params.put("upload_brochures", updatFile);
//
//        } else {
//            Log.d("KDKDKDKDKD","222222");
//            params.put("upload_brochures", brochuers_file);
//        }

        header.put("Cynapse", params);

        new AddMyConferencePostApi(AddConference.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    Log.d("ADCONFERESPON", response.toString());
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {

                        String res = header.getString("UpdateConference");
                        JSONObject jsonObject = new JSONObject(res);
                        String conference_id = jsonObject.getString("conference_id");

                        addConfPackPostApi(conference_id);

                        Intent intent = new Intent(getApplicationContext(), MyConferencesActivity.class);
                        intent.putExtra("changeLike", "1");
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else {
                        MyToast.toastLong(AddConference.this, res_msg);
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


    /*----*/
    private void addConferencePostApi() throws JSONException {

        System.out.println("CITYIDDDADDCONF" + paymentMode_Id);
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();

        params.put("uuid", AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.UserId, ""));
//        params.put("total_days_price", total_amount_pack.getText().toString());
//        params.put("accomdation", txt_acc_amount.getText().toString());
//        params.put("member_concessions", "");
//        params.put("member_concessions", ed_consession_member.getText().toString());
//        params.put("student_concessions","");
//        params.put("student_concessions", ed_consession_stud.getText().toString());
        params.put("price_hike_after_percentage", ed_hike_per.getText().toString());
        params.put("price_hike_after_date", txt_pice_date.getText().toString());
        params.put("conference_name", txt_conferenc_name.getText().toString());
        params.put("from_date", txt_datef.getText().toString());
        params.put("to_date", txt_datet.getText().toString());
        params.put("from_time", txt_timef.getText().toString());
        params.put("to_time", txt_timet.getText().toString());

        if (txt_addres.getText().toString().length() > 0) {
            params.put("venue", txt_addres.getText().toString());
        } else {
            params.put("venue", AddressStr);
        }
        if (radioMyAdd.isChecked()) {
            addres_type = "1";
            params.put("address_type", addres_type);
        } else if (radioCurrentAdd.isChecked()) {
            addres_type = "2";
            params.put("address_type", addres_type);
        } else if (radioEnterAdd.isChecked()) {
            addres_type = "3";
            params.put("address_type", addres_type);
        }

        params.put("perticular_for_city", city_id);
        params.put("conference_description", txt_conf_desc.getText().toString());
        params.put("available_seat", txt_available_seta.getText().toString());
        params.put("event_host", txt_event_host.getText().toString());
        params.put("speciality", specializationId);
        // params.put("contact", txt_conctact.getText().toString());
        params.put("perticular_for_country", country_id);
        params.put("perticular_for_state", state_id);
        params.put("medical_profile_id", medicalProfileId);
        params.put("conference_type_id", conferenceType_Id);
        params.put("otherconferencetype", txt_conference_type.getText().toString());
        params.put("keynote_speakers", txt_eventsponcer.getText().toString());
        params.put("payment_mode", paymentMode_Id);
        params.put("credit_earnings", edit_credit_earn.getText().toString());

//        params.put("latitude", AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Latitude, ""));
//        params.put("longitude", AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Longitude, ""));

        params.put("latitude", addressLat);
        params.put("longitude", addressLog);
        params.put("upload_brochures", all_pdf_name);

        /*bablu 12-04-19*/
        params.put("discount_detail", discount_details_edit.getText().toString().trim());
        params.put("discount_percentage", no_of_discount.getText().toString().trim());
        params.put("gst", edit_gst.getText().toString().trim());
        params.put("department_id", medicalSpecialityDepartmentID.replace(" ", ""));
        /*end*/

        Log.d("REQUETPARAMCONFER", params + "");
        header.put("Cynapse", params);


        new AddConferencePostApi(AddConference.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
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
//                      }
//                    }
//                }
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    Log.d("ADCONFERESPON", response.toString());
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        try {
                            String res = header.getString("AddConference");
                            JSONObject jsonObject = new JSONObject(res);
                            String conference_id = jsonObject.getString("conference_id");

                            addConfPackPostApi(conference_id);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //MyToast.toastLong(AddConference.this, " Conference created successfully, You will be notified soon");

                    } else {
                        MyToast.toastLong(AddConference.this, res_msg);
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


    private void addConfPackPostApi(String conf_id) throws JSONException {


        JSONArray arrayPackage = new JSONArray();
        for (int f = 0; f < addPackageModalArrayList.size(); f++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("packageDetails", addPackageModalArrayList.get(f).getPackageDetails());
            jsonObject.put("price", addPackageModalArrayList.get(f).getPrice());
            arrayPackage.put(jsonObject);
        }

        JSONArray arrayPackageForeign = new JSONArray();
        for (int j = 0; j < addPackageForeignArrayList.size(); j++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("packageDetails", addPackageForeignArrayList.get(j).getPackageDetails());
            jsonObject.put("price", addPackageForeignArrayList.get(j).getPrice());
            arrayPackageForeign.put(jsonObject);
        }

        Log.e("addpackageNormal", arrayPackage.toString());
        Log.e("addPackageForeign", arrayPackageForeign.toString());


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conf_id);

//        JSONArray array = new JSONArray();
//        double pricePack = 0.0;
//
//        Log.e("ajsjlbcvajsb",conferenceDay.toString()+","+allEds.size());
//
//        for (int i = 0; i < conferenceDay.size(); i++) {
//            try {
//
//                JSONObject obj = new JSONObject();
//
//                obj.put("day", conferenceDay.get(i).replace("Day", "").trim());
//
//
////                if (txt_acc_amount.getText().toString().length() == 0) {
////                } else {
////                    for (int l = 0; l < allEds.size(); l++) {
//                String confrencePrices = allEds.get(i).getText().toString();
//
//                obj.put("price", confrencePrices);
//                Log.e("afdsddw",obj.toString());
////                    }
//
//                array.put(obj);
////                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }

        params.put("addPackageNormal", arrayPackage);
        params.put("addPackageForeign", arrayPackageForeign);
        header.put("Cynapse", params);

        new AddConferencePaymentTypeApi(AddConference.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
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
//                      }
//                    }
//                }
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("kdkdkkddkdkdkdkd", response.toString());
                    if (res_code.equals("1")) {
                        //MyToast.toastLong(AddConference.this, res_msg);
                        // finish();
                        final Dialog finishDialog = new Dialog(AddConference.this);
                        finishDialog.setContentView(R.layout.alert_dialog);
                        finishDialog.setCanceledOnTouchOutside(false);
                        Button no = finishDialog.findViewById(R.id.noBtn);
                        no.setVisibility(View.GONE);
                        Button okBtn = finishDialog.findViewById(R.id.okBtn);
                        okBtn.setVisibility(View.VISIBLE);
                        Button yes = finishDialog.findViewById(R.id.yesBtn);
                        yes.setVisibility(View.GONE);
                        TextView messageTv = finishDialog.findViewById(R.id.messageTv);
                        messageTv.setText("Your Event request has been sent for admin approval , Once approved please click on the notification received to pay and make your event live.");

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                finishDialog.dismiss();
                                AddConference.this.finish();
                            }
                        });

                        finishDialog.show();

                    } else {
                        // MyToast.toastLong(AddConference.this, res_msg);
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

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Log.d(">>>>minuts", minute + "");
        Log.d(">>>>hourofDayy", hourOfDay + "");

        if (timeY) {
            temp = Calendar.getInstance();
            temp.set(Calendar.HOUR_OF_DAY, hourOfDay);
            temp.set(Calendar.MINUTE, minute);
        } else {
            temp1 = Calendar.getInstance();
            temp1.set(Calendar.HOUR_OF_DAY, hourOfDay);
            temp1.set(Calendar.MINUTE, minute);
        }


        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        if ((minute >= 0) && (minute <= 9)) {
            switch (minute) {
                case 0: {
                    if (timeY) {
                        String min = minute + "0";
                        conf_timef = hourOfDay + ":" + min;
                        days1 = hourOfDay;
                        mins1 = minute;
                    } else {
                        String min = minute + "0";
                        conf_timet = hourOfDay + ":" + min;
                        days2 = hourOfDay;
                        mins2 = minute;
                    }
                    break;
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9: {
                    if (timeY) {
                        String min = "0" + minute;
                        conf_timef = hourOfDay + ":" + min;
                        days1 = hourOfDay;
                        mins1 = minute;
                    } else {
                        String min = "0" + minute;
                        conf_timet = hourOfDay + ":" + min;
                        days2 = hourOfDay;
                        mins2 = minute;
                        Log.d("PPPPPPWWWW", "4444444");
                    }
                    break;
                }
            }
        } else {
            if (timeY) {
                String min = "" + minute;
                conf_timef = hourOfDay + ":" + min;
                days1 = hourOfDay;
                mins1 = minute;
                //  txt_timef.setText(hourOfDay + ":" + minute);
                Log.d("PPPPPPWWWW", "555555");
            } else {
                String min = "" + minute;
                conf_timet = hourOfDay + ":" + min;
                days2 = hourOfDay;
                mins2 = minute;
            }
        }
        String zo = "";
        if (hourOfDay >= 12)
            zo = "PM";
        else
            zo = "AM";
        //txt_timef.setText(conf_timef + " " + zo);
        txt_timef.setText(conf_timef);
        try {
            int difftime = days2 - days1;
            int mindiff = mins2 - mins1;
            Log.d("TIMEDIFFFF1", fromTime + "");
            Log.d("TIMEDIFFFF", toTime + "");

            //if (difftime > 0 && mindiff >= 0) {
            //txt_timet.setText("");
            //if (temp1.getTimeInMillis() > temp.getTimeInMillis()) {
                String zo2 = "";
                if (hourOfDay >= 12)
                    zo = "PM";
                else
                    zo = "AM";
                //txt_timet.setText(conf_timet + " " + zo);
                txt_timet.setText(conf_timet);
            //}
            //else if (difftime == 0 && mindiff == 0) {
//                txt_timet.setText("");
//                //MyToast.toastShort(AddConference.this, "Invalid Time Selection");
//            } else {
//                txt_timet.setText("");
//                //MyToast.toastShort(AddConference.this, "Invalid Time Selection");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStateApi(String countryId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", countryId);
        header.put("Cynapse", params);
        new GetStateApi(AddConference.this, header) {
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

                            txt_state.setText("");
                            txt_city.setText("");

                            state_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempstate);
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

    private void getCityApi(String stateCountryId, String stateId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("country_code", stateCountryId);
        params.put("state_code", stateId);
        header.put("Cynapse", params);
        Log.d("CITYHEADER", params.toString());
        new GetCityApi(AddConference.this, header) {
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
                        //  cityList.clear();
                        // MyToast.toastLong(SellHospitalActivity.this,res_msg);
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

                            city_adapter = new Adapter_Filter(AddConference.this, R.layout.activity_add_conference_1, R.id.lbl_name, tempcity);
                            txt_city.setText("");
                            txt_city.setAdapter(city_adapter);
                            txt_city.setThreshold(1);
                        }

                    } else {

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

    private void getConferenceTypeApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.UserId, ""));
        params.put("sync_time", "");
        header.put("Cynapse", params);

        new GetConferenceTypeApi(AddConference.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("ConferenceType", response.toString());
                    if (res_code.equals("1")) {
                        JSONArray header2 = header.getJSONArray("ConferenceType");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            ConferenceTypeModel model = new ConferenceTypeModel();
                            model.setConference_type_id(item.getString("name"));

                            model.setConference_type_name(item.getString("type_id"));

                            conferenceTypeList.add(item.getString("name"));
                        }


                    } else {

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

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
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

        new MaterialDialog.Builder(AddConference.this)
                .title(R.string.croppertitle)
                .customView(view, wrapInScrollView)
                .positiveText(R.string.crop)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        try {
                            bitmap = ThumbnailUtils.extractThumbnail(img.getCroppedImage(), 300, 300);
                            saveImage(bitmap, AddConference.this, getString(R.string.app_name), "", false);


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
            imageFile = new File(rootFile, fname);

            FileOutputStream f = null;
            try {
                f = new FileOutputStream(imageFile);
                save_bitmap.compress(Bitmap.CompressFormat.PNG, 70, f);
                f.flush();
                f.close();
                picturePath = imageFile.getAbsolutePath();
                Log.d("PICTUREPATHRERER", picturePath);
                //   uploadProfileImage(picturePath);

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

    void uploadProfileImage(String mulfileName) {
        // final String fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
        // final String fileName = Util.imageFile.getName();
//        String uuid = CustomPreference.readString(this, CustomPreference.USER_ID, "");
        Log.d("FILENAMEIMAG", mulfileName + "");

        String url = AppConstantClass.HOST + "fileUpload/brochures";
        PostImage post = new PostImage(Util.imageFile, url, mulfileName, AddConference.this, "image") {
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
                    Log.d("PROFILEIMAGE", profile_image);
                    path_list.add(profile_image);

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
                } finally {
                    {
                        Log.d("FOROKDILJDF", fileName);
                    }
                }
            }

            @Override
            public void receiveError() {
                Log.e("PROFILE", "ERROR");
            }
        };
        post.execute(url, null, null);
    }

    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(AddConference.this, header, false) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("RESPONSEPROFILe", response.toString());

                    if (res_code.equals("1")) {
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");
                        Log.d("ADRESSSSSCON", item.getString("address") + "," + item.getString("city_name") + "," + item.getString("state_name") + "," + item.getString("country_name"));
                        my_city = item.getString("city_name");
                        my_state = item.getString("state_name");
                        my_country = item.getString("country_name");
                        my_city_id = item.getString("city_id");
                        my_country_id = item.getString("country_code");
                        my_state_id = item.getString("state_id");
                        my_address = item.getString("address");

                        // my_address_txt.setText(item.getString("address") + "," + item.getString("city_name") + "," + item.getString("state_name") + "," + item.getString("country_name"));
                        //my_address_txt.setText(item.getString("address") + " " + item.getString("city_name") + " " + item.getString("state_name") + " " + item.getString("country_name"));
                        // AddressStr = my_address_txt.getText().toString();

                        /*bablu*/
                        if (item.getString("address").equalsIgnoreCase("")) {
                            try {
                                my_address_txt.setText(getCompleteAddressString(AddConference.this,
                                        Double.parseDouble(AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Latitude, "")),
                                        Double.parseDouble(AppCustomPreferenceClass.readString(AddConference.this, AppCustomPreferenceClass.Longitude, ""))));
                            } catch (NumberFormatException nf) {
                                nf.printStackTrace();
                            }
                        } else {
                            my_address_txt.setText(item.getString("address"));
                        }

                        AddressStr = my_address_txt.getText().toString();
                        geoLocate();
                        /*end*/

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

    public void showDialogTitle() {

        EditText loc_search;
        LinearLayoutManager linearLayoutManager;
        TextView cancel_txt, done_txt;


        final Dialog dialog = new Dialog(AddConference.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multi_speciality_selection_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        multi_title_sel_recycler = dialog.findViewById(R.id.multi_city_sel_recycler);
        cancel_txt = dialog.findViewById(R.id.cancel_txt);
        done_txt = dialog.findViewById(R.id.done_txt);
        loc_search = dialog.findViewById(R.id.loc_search);
        linearLayoutManager = new LinearLayoutManager(AddConference.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_title_sel_recycler.setLayoutManager(linearLayoutManager);

        String[] a = specializationId.split(",");
        for (int j = 0; j < a.length; j++) {
            Log.e("Afeff", a[j]);

            for (int i = 0; i < specializationList.size(); i++) {
                Log.e("jabfba", specializationList.get(i).getSpecialization_id() + ",");
                if (specializationList.get(i).getSpecialization_id().equals(a[j].trim())) {
                    specializationList.get(i).setStatus(true);
                }
            }
        }

        titleAdapter_recycler_adapter = new MedicalSpecialityAdapter(specializationList, AddConference.this, AddConference.this);
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
                dialog.dismiss();
                //TODO : finishing the activity
            }
        });

        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                titleNewList.clear();

                titleNewListId.clear();
                for (int i = 0; i < specializationList.size(); i++) {

                    if (specializationList.get(i).getStatus()) {
                        // jobLocation.setText(metrocitySelectedList.toString().replace("[","").replace("]",""));
                        titleNewList.add(specializationList.get(i).getSpecialization_name());
                        titleNewListId.add(specializationList.get(i).getSpecialization_id());
                        Log.d("modelArrayList", titleList.get(i).getSpecialization_name() + "<><<" + titleList.get(i).getSpecialization_id());
                    }
                }

                Log.d("ISZEOFTITLESELLIS", titleNewList.size() + "");
                specializationId = titleNewListId.toString().replace("[", "").replace("]", "");
                Log.d("specializationIddDDDD", specializationId);
                txt_specially.setText(titleNewList.toString().replace("[", "").replace("]", ""));
                dialog.dismiss();
            }
        });
        specialitylabel.setVisibility(View.VISIBLE);
    }

    public void showDialogDepartment() {

        EditText loc_search;
        LinearLayoutManager linearLayoutManager;
        TextView cancel_txt, done_txt;

        final Dialog dialog = new Dialog(AddConference.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multi_speciality_selection_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        multi_title_sel_recycler = dialog.findViewById(R.id.multi_city_sel_recycler);
        cancel_txt = dialog.findViewById(R.id.cancel_txt);
        done_txt = dialog.findViewById(R.id.done_txt);
        loc_search = dialog.findViewById(R.id.loc_search);
        linearLayoutManager = new LinearLayoutManager(AddConference.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        multi_title_sel_recycler.setLayoutManager(linearLayoutManager);


        String[] a = medicalSpecialityDepartmentID.split(",");
        for (int j = 0; j < a.length; j++) {
            Log.e("Afeff", a[j]);

            for (int i = 0; i < medicalSpecialityDepartmentList.size(); i++) {
                Log.e("jabfba", medicalSpecialityDepartmentList.get(i).getDepartment_id() + ",");
                if (medicalSpecialityDepartmentList.get(i).getDepartment_id().equals(a[j].trim())) {
                    medicalSpecialityDepartmentList.get(i).setStatus(true);
                }
            }
        }


        medicalSpecialityDepartmentAdapter = new MedicalSpecialityDepartmentAdapter(medicalSpecialityDepartmentList, AddConference.this, AddConference.this);
        multi_title_sel_recycler.setAdapter(medicalSpecialityDepartmentAdapter);

        loc_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (medicalSpecialityDepartmentAdapter != null)
                    medicalSpecialityDepartmentAdapter.getFilter().filter(s);
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
                titleNewListDepartment.clear();
                titleNewListIdDepartment.clear();

                for (int i = 0; i < medicalSpecialityDepartmentList.size(); i++) {

                    if (medicalSpecialityDepartmentList.get(i).isStatus()) {
                        // jobLocation.setText(metrocitySelectedList.toString().replace("[","").replace("]",""));
                        titleNewListDepartment.add(medicalSpecialityDepartmentList.get(i).getDepartment_name());
                        titleNewListIdDepartment.add(medicalSpecialityDepartmentList.get(i).getDepartment_id());
                        Log.d("modelArrayList", titleListDepartment.get(i).getDepartment_id() + "<><<" + titleListDepartment.get(i).getDepartment_name());
                    }
                }

                Log.d("ISZEOFTITLESELLIS", titleNewListDepartment.size() + "");
                medicalSpecialityDepartmentID = titleNewListIdDepartment.toString().replace("[", "").replace("]", "");

                Log.d("specializationIddDDDD", medicalSpecialityDepartmentID);

                txt_department.setText(titleNewListDepartment.toString().replace("[", "").replace("]", ""));

                dialog.dismiss();

            }
        });

        specialitylabel.setVisibility(View.VISIBLE);
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

    private ArrayList<GetTargetDepartmentModel> getModelsDepartment(boolean isSelect) {
        ArrayList<GetTargetDepartmentModel> list = new ArrayList<>();
        for (int i = 0; i < titleListDepartment.size(); i++) {

            GetTargetDepartmentModel model = new GetTargetDepartmentModel();
            model.setStatus(isSelect);
            model.setDepartment_id(titleListDepartment.get(i).getDepartment_id());
            model.setDepartment_name(titleListDepartment.get(i).getDepartment_name());
            model.setStatus(titleListDepartment.get(i).isStatus());
            list.add(model);
        }
        return list;
    }

    @Override
    public void selectedTitles(ArrayList<String> titleList) {

        specializationId = titleList.toString().replace("[", "").replace("]", "");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void selectedTitlesMedical(ArrayList<String> titleList) {
        medicalSpecialityDepartmentID = titleList.toString().replace("[", "").replace("]", "");
    }

    private String addressLat = "";
    private String addressLog = "";
    private int fromTime = 0;
    private int fromTimeSec = 0;
    private int toTime = 0;
    private int toTimeSec = 0;
    Calendar temp = null;
    Calendar temp1 = null;
}
