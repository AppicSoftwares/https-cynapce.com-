package com.alcanzar.cynapse.activity;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ViewPagerAdapter;
import com.alcanzar.cynapse.api.ConferenceMyListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.GoingConferencesFragment;
import com.alcanzar.cynapse.fragments.MyConferencesFragment;
import com.alcanzar.cynapse.fragments.SavedConferencesFragment;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ImageModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyConferencesActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO : header views
    TextView title;
    ImageView btnBack, titleIcon;
    Button btnSearch;
    String tabTitle[];
    String paymentsucess;
    // public EditText search_box;
    public DatabaseHelper handler;
    boolean showLike = false;
    ArrayList<ConferenceDetailsModel> arrayList = new ArrayList<>();
    TabLayout tabLayout;
    ViewPager viewPager;
    static boolean boolPdfDel = true;

    static String change_like_status = "";
    public boolean isFromNotification = false;
    public String conference_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_conferences);
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        title = findViewById(R.id.title);
        title.setText(R.string.my_conferences);
        btnSearch = findViewById(R.id.btnSearch);
        //search_box=findViewById(R.id.search_box);
        btnSearch.setVisibility(View.GONE);
        handler = new DatabaseHelper(this);
        //TODO: tabLayout and viewPager setUp
        viewPager = findViewById(R.id.viewPager);
        setUpViewPager(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //TODO: setting the tab titles
        tabTitle = new String[]{"\tFavourite\t", "\tBookings\t", "\tMy Events\t"};
        for (int i = 0; i < tabTitle.length; i++) {
            tabLayout.getTabAt(i).setText(tabTitle[i]);
        }

        if (getIntent() != null) {

            try {
                boolPdfDel = getIntent().getBooleanExtra("boolPdfDel", false);
                change_like_status = getIntent().getStringExtra("changeLike");
                System.out.println("tabPRERRRRR" + boolPdfDel);

                if (change_like_status == null) {
                    selectPage(0);
                    tabLayout.getTabAt(0).setText(tabTitle[0]);
                    tabLayout.getTabAt(1).setText(tabTitle[1]);
                    tabLayout.getTabAt(2).setText(tabTitle[2]);
                } else if (change_like_status.equalsIgnoreCase("1")) {
                    Log.d("2222222", "909090");
                    selectPage(2);
                    tabLayout.getTabAt(0).setText(tabTitle[0]);
                    tabLayout.getTabAt(1).setText(tabTitle[1]);
                    tabLayout.getTabAt(2).setText(tabTitle[2]);

                } else if (change_like_status.equalsIgnoreCase("2")) {
                    Log.d("2222222", "909090");
                    selectPage(1);
                    tabLayout.getTabAt(0).setText(tabTitle[0]);
                    tabLayout.getTabAt(1).setText(tabTitle[1]);
                    tabLayout.getTabAt(2).setText(tabTitle[2]);

                } else if (change_like_status.equalsIgnoreCase("3")) {
                    Log.d("2222222", "909090");
                    selectPage(0);
                    tabLayout.getTabAt(0).setText(tabTitle[0]);
                    tabLayout.getTabAt(1).setText(tabTitle[1]);
                    tabLayout.getTabAt(2).setText(tabTitle[2]);

                }
                paymentsucess = getIntent().getStringExtra("paymentsucess");

                if (getIntent().hasExtra("notificationBOB")) {
                    isFromNotification = true;
                    conference_id = getIntent().getStringExtra("conference_id");
                    selectPage(2);
                } else if (getIntent().hasExtra("notificationBOB1")) {
                    isFromNotification = true;
                    conference_id = getIntent().getStringExtra("conference_id");
                    selectPage(1);
                } else if (paymentsucess.equalsIgnoreCase("1")) {
                    selectPage(1);
                } else if (paymentsucess.equalsIgnoreCase("2")) {
                    selectPage(2);
                } else {
                    selectPage(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.d("UUUUU", "222222");

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ConferenceAllMyList(boolPdfDel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:

                if (!Utils.isActivityRunning(MainActivity.class, MyConferencesActivity.this)) {
                    startActivity(new Intent(MyConferencesActivity.this, MainActivity.class));
                }
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!Utils.isActivityRunning(MainActivity.class, MyConferencesActivity.this)) {
            startActivity(new Intent(MyConferencesActivity.this, MainActivity.class));
        }

        finish();
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new SavedConferencesFragment());
        viewPagerAdapter.addFrag(new GoingConferencesFragment());
        viewPagerAdapter.addFrag(new MyConferencesFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void ConferenceAllMyList(final boolean boolPdfDel) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(MyConferencesActivity.this, AppCustomPreferenceClass.UserId, ""));
        //params.put("uuid", "S75f3");
        //params.put("sync_time", AppCustomPreferenceClass.readString(MyConferencesActivity.this,AppCustomPreferenceClass.sync_time_myconference,""));
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new ConferenceMyListApi(MyConferencesActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MyConferencesActivity.this, AppCustomPreferenceClass.sync_time_myconference, sync_time);
                    Log.d("JOBSPONSECONFERrrrr", response.toString());

                    if (res_code.equals("1")) {

                        handler.deleteTableName(DatabaseHelper.TABLE_GOING_CONFERENCES_PACK_CHARGE);
                        handler.deleteTableName(DatabaseHelper.TABLE_MY_CONFERENCES_PACK_CHARGE);
                        handler.deleteTableName(DatabaseHelper.TABLE_SAVE_CONFERENCES_PACK_CHARGE);
                        handler.deleteTableName(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS);
                        handler.deleteTableName(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
                        handler.deleteTableName(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);

                        if (header.has("Conference")) {
                            JSONArray header2 = header.getJSONArray("Conference");
                            for (int i = 0; i < header2.length(); i++) {
                                JSONObject item = header2.getJSONObject(i);
                                ConferenceDetailsModel model = new ConferenceDetailsModel();
                                ImageModel imageModel = new ImageModel();
                                ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
                                model.setId(item.getString("id"));
                                model.setConference_id(item.getString("conference_id"));
                                model.setConference_name(item.getString("conference_name"));
                                model.setFrom_date(item.getString("from_date"));
                                model.setTo_date(item.getString("to_date"));
                                model.setFrom_time(item.getString("from_time"));
                                model.setTo_time(item.getString("to_time"));
                                model.setVenue(item.getString("venue"));
                                model.setEvent_host_name(item.getString("event_host_name"));
                                model.setSpeciality(item.getString("speciality"));
                                model.setContact(item.getString("contact"));

                                model.setConference_type_id(item.getString("conference_type_id"));
                                model.setMedical_profile_id(item.getString("medical_profile_id"));
                                model.setCredit_earnings(item.getString("credit_earnings"));
                                model.setPayment_mode(item.getString("payment_mode"));
                                model.setTotal_days_price(item.getString("total_days_price"));
                                model.setAccomdation(item.getString("accomdation"));
                                model.setMember_concessions(item.getString("member_concessions"));
                                model.setStudent_concessions(item.getString("student_concessions"));
                                model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
                                model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));
                                model.setLocation("");
//                                imageModel.setImage_name(item.getString("brochuers_file"));
//                                imageModel.setImage_id(item.getString("conference_id"));
                                model.setBrochuers_file(item.getString("brochuers_file"));
                                model.setInterested(item.getString("interested"));
                                model.setRegistered(item.getString("registered"));
                                model.setViews(item.getString("views"));
                                model.setLatitude(item.getString("latitude"));
                                model.setLogitude(item.getString("longitude"));
                                model.setParticular_city_id(item.getString("particular_city_id"));
                                model.setParticular_city_name(item.getString("particular_city_name"));
                                model.setParticular_country_id(item.getString("particular_country_id"));
                                //model.setParticular_country_id("");
                                model.setParticular_country_name(item.getString("particular_country_name"));
                                model.setParticular_state_id(item.getString("particular_state_id"));
                                model.setParticular_state_name(item.getString("particular_state_name"));
                                model.setEvent_sponcer(item.getString("keynote_speakers"));
//                                model.setPdf_url(String.valueOf(boolPdfDel));


                                JSONArray dayArrayCharge = item.getJSONArray("packages");

//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
//
//                                    handler.AddMyConferImage(imageModel, true);
//
//                                } else {
//
//                                    handler.AddMyConferImage(imageModel, false);
//                                }

                                int x = 150, y = 1;
                                for (int k = 0; k < dayArrayCharge.length(); k++) {
                                    JSONObject confPack = dayArrayCharge.getJSONObject(k);
                                    Log.d("conferecnepriceee", confPack.getString("price"));
                                    Log.d("conferecnedaysss", confPack.getString("days"));
                                    conferencePackageModel.setConference_pack_id(item.getString("conference_id"));

                                    conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                                    conferencePackageModel.setConference_pack_day(confPack.getString("days"));


                                    if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {

                                        handler.AddConferMyPackCharge(conferencePackageModel, true);

                                    } else {
                                        handler.AddConferMyPackCharge(conferencePackageModel, true);
                                    }
                                }

                                //model.setParticular_country_id("");
                                model.setParticular_country_id(item.getString("particular_country_id"));
                                model.setParticular_country_name(item.getString("particular_country_name"));
                                model.setParticular_state_id(item.getString("particular_state_id"));
                                model.setParticular_state_name(item.getString("particular_state_name"));
                                model.setStatus(item.getString("status"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
//                                model.setCost(item.getString("cost"));
//                                model.setDuration(item.getString("duration"));
                                model.setPayment_status("0");
                                model.setShow_like(item.getString("like_status"));
                                model.setParticular_city_id(item.getString("particular_city_id"));
                                model.setParticular_city_name(item.getString("particular_city_name"));
                                model.setAddress_type(item.getString("address_type"));
                                model.setViews(item.getString("views"));
                                model.setRegistered(item.getString("registered"));
                                model.setInterested(item.getString("interested"));
                                model.setAvailable_seat(item.getString("available_seat"));
                                model.setConference_description(item.getString("conference_description"));
                                Log.d("ContacTINNNNNN", item.getString("id"));


                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {

                                    handler.AddMyConferenceDetails(model, true);

                                } else {

                                    handler.AddMyConferenceDetails(model, false);
                                }
                            }
                        }

                        if (header.has("BuyConference")) {
                            Log.d("KDKDKDK111", "kDKKD111");
                            JSONArray header2 = header.getJSONArray("BuyConference");
                            for (int i = 0; i < header2.length(); i++) {
                                JSONObject item = header2.getJSONObject(i);
                                ConferenceDetailsModel model = new ConferenceDetailsModel();
                                ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
                                model.setId(item.getString("id"));
                                model.setConference_id(item.getString("conference_id"));
                                model.setConference_name(item.getString("conference_name"));
                                model.setFrom_date(item.getString("from_date"));
                                model.setTo_date(item.getString("to_date"));
                                model.setFrom_time(item.getString("from_time"));
                                model.setTo_time(item.getString("to_time"));
                                model.setVenue(item.getString("venue"));
                                model.setEvent_host_name(item.getString("event_host_name"));
                                model.setSpeciality(item.getString("speciality"));
                                model.setContact(item.getString("contact"));
                                model.setLocation("");
                                model.setBrochuers_file(item.getString("brochuers_file"));

                                model.setConference_type_id(item.getString("conference_type_id"));
                                model.setMedical_profile_id(item.getString("medical_profile_id"));
                                model.setCredit_earnings(item.getString("credit_earnings"));
                                model.setPayment_mode(item.getString("payment_mode"));
                                model.setTotal_days_price(item.getString("total_days_price"));
                                model.setAccomdation(item.getString("accomdation"));
                                model.setMember_concessions(item.getString("member_concessions"));
                                model.setStudent_concessions(item.getString("student_concessions"));
                                model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
                                model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));
//                                model.setBrochuers_charge(item.getString("brochuers_charge"));
//                                model.setBrochuers_days(item.getString("brochuers_days"));
                                model.setLatitude(item.getString("latitude"));
                                model.setLogitude(item.getString("longitude"));
//                                model.setRegistration_fee(item.getString("registration_fee"));
//                                model.setRegistration_days(item.getString("registration_days"));
                                model.setEvent_sponcer(item.getString("keynote_speakers"));


                                JSONArray dayArrayCharge = item.getJSONArray("packages");

//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
//
//                                    handler.AddMyConferImage(imageModel, true);
//
//                                } else {
//
//                                    handler.AddMyConferImage(imageModel, false);
//                                }


                                for (int k = 0; k < dayArrayCharge.length(); k++) {
                                    JSONObject confPack = dayArrayCharge.getJSONObject(k);
                                    Log.d("conferecneidddd11", confPack.getString("price"));
                                    Log.d("conferecneidddd22", confPack.getString("days"));
                                    conferencePackageModel.setConference_pack_id(item.getString("conference_id"));

                                    conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                                    conferencePackageModel.setConference_pack_day(confPack.getString("days"));


                                    if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GOING_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {

                                        handler.AddConferGoingPackCharge(conferencePackageModel, true);

                                    } else {

                                        handler.AddConferGoingPackCharge(conferencePackageModel, true);
                                    }
                                }
//                                model.setBrochuers_charge(item.getString("brochuers_charge"));
//                                model.setBrochuers_days(item.getString("brochuers_days"));
                                model.setParticular_country_id(item.getString("particular_country_id"));
                                model.setParticular_country_name(item.getString("particular_country_name"));
                                model.setParticular_state_id(item.getString("particular_state_id"));
                                model.setParticular_state_name(item.getString("particular_state_name"));
                                model.setStatus(item.getString("status"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setPayment_status("1");
                                model.setShow_like(item.getString("like_status"));
                                model.setParticular_city_id(item.getString("particular_city_id"));
                                //model.setParticular_city_id("");
                                model.setParticular_city_name(item.getString("particular_city_name"));
                                model.setAddress_type("");
                                model.setAvailable_seat(item.getString("available_seat"));
                                model.setConference_description(item.getString("conference_description"));
                                Log.d("MODELOFLISTSTATUS22", item.getString("id") + "");
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {

                                    handler.AddGoingConferenceDetails(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddGoingConferenceDetails(model, false);
                                }
                            }
                        }


                        if (header.has("LikeConference")) {

                            JSONArray header2 = header.getJSONArray("LikeConference");
                            for (int i = 0; i < header2.length(); i++) {
                                JSONObject item = header2.getJSONObject(i);
                                ConferenceDetailsModel model = new ConferenceDetailsModel();
                                ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
                                model.setId(item.getString("id"));
                                model.setConference_id(item.getString("conference_id"));
                                model.setConference_name(item.getString("conference_name"));
                                model.setFrom_date(item.getString("from_date"));
                                model.setTo_date(item.getString("to_date"));
                                model.setFrom_time(item.getString("from_time"));
                                model.setTo_time(item.getString("to_time"));
                                model.setVenue(item.getString("venue"));
                                model.setEvent_host_name(item.getString("event_host_name"));
                                model.setSpeciality(item.getString("speciality"));
                                model.setContact(item.getString("contact"));
                                model.setLocation("");
                                model.setBrochuers_file(item.getString("brochuers_file"));

                                model.setConference_type_id(item.getString("conference_type_id"));
                                model.setMedical_profile_id(item.getString("medical_profile_id"));
                                model.setCredit_earnings(item.getString("credit_earnings"));
                                model.setPayment_mode(item.getString("payment_mode"));
                                model.setTotal_days_price(item.getString("total_days_price"));
                                model.setAccomdation(item.getString("accomdation"));
                                model.setMember_concessions(item.getString("member_concessions"));
                                model.setStudent_concessions(item.getString("student_concessions"));
                                model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
                                model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));

                                model.setLatitude(item.getString("latitude"));
                                model.setLogitude(item.getString("longitude"));

                                model.setEvent_sponcer(item.getString("keynote_speakers"));

                                JSONArray dayArrayCharge = item.getJSONArray("packages");

//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
//
//                                    handler.AddMyConferImage(imageModel, true);
//
//                                } else {
//
//                                    handler.AddMyConferImage(imageModel, false);
//                                }


                                for (int k = 0; k < dayArrayCharge.length(); k++) {
                                    JSONObject confPack = dayArrayCharge.getJSONObject(k);

                                    Log.d("conferecneidddd11", confPack.getString("price"));

                                    conferencePackageModel.setConference_pack_id(item.getString("conference_id"));

                                    conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                                    conferencePackageModel.setConference_pack_day(confPack.getString("days"));


                                    if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SAVE_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {

                                        handler.AddConferSavePackCharge(conferencePackageModel, true);

                                    } else {

                                        handler.AddConferSavePackCharge(conferencePackageModel, true);
                                    }
                                }
                                //model
                                //model.setParticular_country_id("");
                                model.setParticular_country_id(item.getString("particular_country_id"));
                                model.setParticular_country_name(item.getString("particular_country_name"));
                                model.setParticular_state_id(item.getString("particular_state_id"));
                                model.setParticular_state_name(item.getString("particular_state_name"));
                                model.setStatus(item.getString("status"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setPayment_status("2");
                                model.setShow_like(item.getString("like_status"));
                                model.setParticular_city_id(item.getString("particular_city_id"));
                                //model.setParticular_city_id("");
                                model.setParticular_city_name(item.getString("particular_city_name"));
                                model.setAddress_type("");
                                model.setAvailable_seat(item.getString("available_seat"));
                                model.setConference_description(item.getString("conference_description"));


                                if (item.has("booking_stopped"))
                                    model.setBooking_stopped(item.getString("booking_stopped"));
                                else
                                    model.setBooking_stopped("");

                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {

                                    handler.AddSaveConferenceDetails(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddSaveConferenceDetails(model, false);
                                }
                            }
                        }

                    } else {

                        //  MyToast.toastLong(getActivity(),res_msg);
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

    void selectPage(int pageIndex) {
        //viewPager.setCurrentItem(pageIndex);
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
    }

//    public void setArrayList()
//    {
//
//        TicketsConferenceAdapter requestPostJobAdapter = new TicketsConferenceAdapter(MyConferencesActivity.this, R.layout.tickets_row, arrayList);
//        recycleView.setAdapter(requestPostJobAdapter);
//    }

}
