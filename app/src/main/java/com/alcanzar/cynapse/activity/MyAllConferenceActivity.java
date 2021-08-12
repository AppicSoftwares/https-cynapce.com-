package com.alcanzar.cynapse.activity;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.ConferenceMyListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.GoingConferencesFragment;
import com.alcanzar.cynapse.fragments.MyConferencesFragment;
import com.alcanzar.cynapse.fragments.SavedConferencesFragment;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ImageModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAllConferenceActivity extends AppCompatActivity {

    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;
    public DatabaseHelper handler;
    static String change_like_status = "";
    ImageView btnBack, titleIcon;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new DatabaseHelper(this);
        setContentView(R.layout.activity_my_all_conference);
        btnBack = findViewById(R.id.btnBack);

        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        title = findViewById(R.id.title);
        title.setText(R.string.my_conferences);
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        // Create a new Tab named "First"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("My Events"); // set the Text for the first Tab

        //firstTab.setIcon(R.drawable.ic_launcher); // set an icon for the
        // first tab
        firstTab.select();
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout

        // Create a new Tab named "Second"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Going"); // set the Text for the second Tab
        //secondTab.setIcon(R.drawable.ic_launcher); // set an icon for the second tab
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
        // Create a new Tab named "Third"
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Saved"); // set the Text for the first Tab
        // thirdTab.setIcon(R.drawable.ic_launcher); // set an icon for the first tab
        tabLayout.addTab(thirdTab); // add  the tab at in the TabLayout

        if (getIntent() != null) {
            try {
                change_like_status = getIntent().getStringExtra("changeLike");
                System.out.println("tabPRERRRRR" + change_like_status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //perform setOnTabSelectedListener event on TabLayout

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new MyConferencesFragment();
                        break;
                    case 1:
                        fragment = new GoingConferencesFragment();
                        break;
                    case 2:
                        fragment = new SavedConferencesFragment();

                        break;
                }

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ConferenceAllMyList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ConferenceAllMyList() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(MyAllConferenceActivity.this, AppCustomPreferenceClass.UserId, ""));
        //params.put("uuid", "S75f3");
        //params.put("sync_time", AppCustomPreferenceClass.readString(MyConferencesActivity.this,AppCustomPreferenceClass.sync_time_myconference,""));
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new ConferenceMyListApi(MyAllConferenceActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MyAllConferenceActivity.this, AppCustomPreferenceClass.sync_time_myconference, sync_time);
                    Log.d("JOBSPONSECONFERrrrr", response.toString());

                    if (res_code.equals("1")) {
                        handler.deleteTableName(DatabaseHelper.TABLE_GOING_CONFERENCES_PACK_CHARGE);
                        handler.deleteTableName(DatabaseHelper.TABLE_MY_CONFERENCES_PACK_CHARGE);
                        handler.deleteTableName(DatabaseHelper.TABLE_SAVE_CONFERENCES_PACK_CHARGE);
                        if (header.has("Conference")) {
                            Log.d("MYConference", "kDKKD");
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
                                imageModel.setImage_name(item.getString("brochuers_file"));
                                imageModel.setImage_id(item.getString("conference_id"));
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
                                        Log.d("conferecneidddd11", confPack.getString("price"));

                                        conferencePackageModel.setConference_pack_id(item.getString("conference_id"));

                                        conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                                        conferencePackageModel.setConference_pack_day(confPack.getString("days"));


                                        if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {

                                            handler.AddConferMyPackCharge(conferencePackageModel, true);

                                        } else {

                                            handler.AddConferMyPackCharge(conferencePackageModel, false);
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
                                Log.d("MODELOFLISTSTATUS11", item.getString("total_days_price") + "");

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
                            Log.d("KDKDKDK333", "kDKKD333");

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

                                Log.d("MODELOFLISTSTATUS22", item.getString("id") + "");
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {

                                    handler.AddSaveConferenceDetails(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddSaveConferenceDetails(model, false);
                                }
                            }
                        }
                        // mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        //  mSwipeRefreshLayout.setRefreshing(false);
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

}
