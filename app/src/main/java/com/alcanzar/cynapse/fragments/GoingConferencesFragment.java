package com.alcanzar.cynapse.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.activity.GoingConferenceDetailsActivity;
import com.alcanzar.cynapse.activity.MyConferenceDetailsActivity;
import com.alcanzar.cynapse.activity.MyConferencesActivity;
import com.alcanzar.cynapse.adapter.TicketsGoingAdapter;
import com.alcanzar.cynapse.api.ConferenceMyListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.AddForgeinPackageMYConferenceModel;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;
import com.alcanzar.cynapse.model.PackageSavedConferenceModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoingConferencesFragment extends Fragment {
    //TODO: recycleView and listing
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ConferenceDetailsModel> arrayList = new ArrayList<>();
    boolean showOnclk = false;
    TextView txt_goingconf_message;
    DatabaseHelper handler;

    public GoingConferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_going_conferences, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(linearLayoutManager);
        txt_goingconf_message = view.findViewById(R.id.txt_goingconf_message);
        handler = new DatabaseHelper(getActivity());

//        arrayList = ((MyConferencesActivity) getActivity()).handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1");
//        Log.d("arralylistGoingconf", arrayList.size() + "");
//        if (arrayList.size() > 0) {
//            TicketsGoingAdapter ticketsAdapter = new TicketsGoingAdapter(getContext(), R.layout.tickets_row, arrayList);
//            ticketsAdapter.notifyDataSetChanged();
//            recyclerView.setAdapter(ticketsAdapter);
//        } else {
//            txt_goingconf_message.setVisibility(View.VISIBLE);
//        }
        //TODO: demo data
//        for(int i= 0;i<=2;i++){
//            arrayList.add(new DashBoardModel(getResources().getString(R.string.health_care_conference),"id"));
//        }
//        TicketsAdapter ticketsAdapter = new TicketsAdapter(getContext(),R.layout.tickets_row,arrayList);
//        recyclerView.setAdapter(ticketsAdapter);
    }

    //
    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Util.isNetConnected(getActivity())) {
                ConferenceAllMyList();
            } else {
                arrayList = ((MyConferencesActivity) getActivity()).handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1");
                Log.d("arralylistGoingconf", arrayList.size() + "");
                if (arrayList.size() > 0) {
                    TicketsGoingAdapter ticketsAdapter = new TicketsGoingAdapter(getContext(), R.layout.tickets_row, arrayList);
                    recyclerView.setAdapter(ticketsAdapter);
                } else {

                    txt_goingconf_message.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ConferenceAllMyList() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        //params.put("uuid", "S75f3");
        //params.put("sync_time", AppCustomPreferenceClass.readString(MyConferencesActivity.this,AppCustomPreferenceClass.sync_time_myconference,""));
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new ConferenceMyListApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
//                    AppCustomPreferenceClass.writeString(MyConferencesActivity.this, AppCustomPreferenceClass.sync_time_myconference, sync_time);
                    Log.d("JOBSPOGOINGONFERrrrr", response.toString());

                    if (res_code.equals("1")) {
                        handler.deleteTableName(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);
//                        handler.deleteTableName(DatabaseHelper.TABLE_MY_CONFERENCES_PACK_CHARGE);
//                        handler.deleteTableName(DatabaseHelper.TABLE_SAVE_CONFERENCES_PACK_CHARGE);
//                        if (header.has("Conference")) {
//                            Log.d("MYConference", "kDKKD");
//                            JSONArray header2 = header.getJSONArray("Conference");
//                            for (int i = 0; i < header2.length(); i++) {
//                                JSONObject item = header2.getJSONObject(i);
//                                ConferenceDetailsModel model = new ConferenceDetailsModel();
//                                ImageModel imageModel = new ImageModel();
//                                ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
//                                model.setId(item.getString("id"));
//                                model.setConference_id(item.getString("conference_id"));
//                                model.setConference_name(item.getString("conference_name"));
//                                model.setFrom_date(item.getString("from_date"));
//                                model.setTo_date(item.getString("to_date"));
//                                model.setFrom_time(item.getString("from_time"));
//                                model.setTo_time(item.getString("to_time"));
//                                model.setVenue(item.getString("venue"));
//                                model.setEvent_host_name(item.getString("event_host_name"));
//                                model.setSpeciality(item.getString("speciality"));
//                                model.setContact(item.getString("contact"));
//
//                                model.setConference_type_id(item.getString("conference_type_id"));
//                                model.setMedical_profile_id(item.getString("medical_profile_id"));
//                                model.setCredit_earnings(item.getString("credit_earnings"));
//                                model.setPayment_mode(item.getString("payment_mode"));
//                                model.setTotal_days_price(item.getString("total_days_price"));
//                                model.setAccomdation(item.getString("accomdation"));
//                                model.setMember_concessions(item.getString("member_concessions"));
//                                model.setStudent_concessions(item.getString("student_concessions"));
//                                model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
//                                model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));
//                                model.setLocation("");
//                                imageModel.setImage_name(item.getString("brochuers_file"));
//                                imageModel.setImage_id(item.getString("conference_id"));
//                                model.setBrochuers_file(item.getString("brochuers_file"));
//                                model.setInterested(item.getString("interested"));
//                                model.setRegistered(item.getString("registered"));
//                                model.setViews(item.getString("views"));
//                                model.setLatitude(item.getString("latitude"));
//                                model.setLogitude(item.getString("longitude"));
//                                model.setParticular_city_id(item.getString("particular_city_id"));
//                                model.setParticular_city_name(item.getString("particular_city_name"));
//                                model.setParticular_country_id(item.getString("particular_country_id"));
//                                //model.setParticular_country_id("");
//                                model.setParticular_country_name(item.getString("particular_country_name"));
//                                model.setParticular_state_id(item.getString("particular_state_id"));
//                                model.setParticular_state_name(item.getString("particular_state_name"));
//                                model.setEvent_sponcer(item.getString("keynote_speakers"));
//
//
//                                JSONArray dayArrayCharge = item.getJSONArray("packages");
//
////                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
////
////                                    handler.AddMyConferImage(imageModel, true);
////
////                                } else {
////
////                                    handler.AddMyConferImage(imageModel, false);
////                                }
//
//                                int x = 150, y = 1;
//                                for (int k = 0; k < dayArrayCharge.length(); k++) {
//                                    JSONObject confPack = dayArrayCharge.getJSONObject(k);
//                                    Log.d("conferecneidddd11", confPack.getString("price"));
//                                    Log.d("conferecneidddd22", confPack.getString("days"));
//                                    conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
//
//                                    conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
//                                    conferencePackageModel.setConference_pack_day(confPack.getString("days"));
//
//
//                                    if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {
//
//                                        handler.AddConferMyPackCharge(conferencePackageModel, true);
//
//                                    } else {
//
//                                        handler.AddConferMyPackCharge(conferencePackageModel, true);
//                                    }
//                                }
//                                //model.setParticular_country_id("");
//                                model.setParticular_country_id(item.getString("particular_country_id"));
//                                model.setParticular_country_name(item.getString("particular_country_name"));
//                                model.setParticular_state_id(item.getString("particular_state_id"));
//                                model.setParticular_state_name(item.getString("particular_state_name"));
//                                model.setStatus(item.getString("status"));
//                                model.setAdd_date(item.getString("add_date"));
//                                model.setModify_date(item.getString("modify_date"));
////                                model.setCost(item.getString("cost"));
////                                model.setDuration(item.getString("duration"));
//                                model.setPayment_status("0");
//                                model.setShow_like(item.getString("like_status"));
//                                model.setParticular_city_id(item.getString("particular_city_id"));
//                                model.setParticular_city_name(item.getString("particular_city_name"));
//                                model.setAddress_type(item.getString("address_type"));
//                                model.setViews(item.getString("views"));
//                                model.setRegistered(item.getString("registered"));
//                                model.setInterested(item.getString("interested"));
//                                model.setAvailable_seat(item.getString("available_seat"));
//                                model.setConference_description(item.getString("conference_description"));
//                                Log.d("ContacTINNNNNN", item.getString("id"));
//                                Log.d("MODELOFLISTSTATUS11", item.getString("total_days_price") + "");
//
//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {
//
//                                    handler.AddMyConferenceDetails(model, true);
//
//                                } else {
//
//                                    handler.AddMyConferenceDetails(model, false);
//                                }
//                            }
//                        }
                        if (header.has("BuyConference"))
                        {
                            normal_packagesArr = new JSONArray();
                            foreign_packagesArr = new JSONArray();

                            Log.d("KDKDKDK111444", "kDKKD111");
                            JSONArray header2 = header.getJSONArray("BuyConference");

                            for (int i = 0; i < header2.length(); i++) {
                                JSONObject item = header2.getJSONObject(i);
                                ConferenceDetailsModel model = new ConferenceDetailsModel();
                                //ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
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

                                if (item.has("total_days_price")) {
                                    model.setTotal_days_price(item.getString("total_days_price"));
                                } else {
                                    model.setTotal_days_price("");
                                }

                                if (item.has("accomdation")) {
                                    model.setAccomdation(item.getString("accomdation"));
                                } else {
                                    model.setAccomdation("");
                                }

                                if (item.has("member_concessions")) {
                                    model.setMember_concessions(item.getString("member_concessions"));
                                } else {
                                    model.setMember_concessions("");
                                }

                                if (item.has("student_concessions")) {
                                    model.setStudent_concessions(item.getString("student_concessions"));
                                } else {
                                    model.setStudent_concessions("");
                                }

                                model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
                                model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));
//                                model.setBrochuers_charge(item.getString("brochuers_charge"));
//                                model.setBrochuers_days(item.getString("brochuers_days"));
                                model.setLatitude(item.getString("latitude"));
                                model.setLogitude(item.getString("longitude"));
//                                model.setRegistration_fee(item.getString("registration_fee"));
//                                model.setRegistration_days(item.getString("registration_days"));
                                model.setEvent_sponcer(item.getString("keynote_speakers"));

//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
//
//                                    handler.AddMyConferImage(imageModel, true);
//
//                                } else {
//
//                                    handler.AddMyConferImage(imageModel, false);
//                                }


//                                for (int k = 0; k < dayArrayCharge.length(); k++) {
//                                    JSONObject confPack = dayArrayCharge.getJSONObject(k);
//                                    Log.d("conferecneidddd11", confPack.getString("price"));
//                                    Log.d("conferecneidddd22", confPack.getString("days"));
//                                    conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
//
//                                    conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
//                                    conferencePackageModel.setConference_pack_day(confPack.getString("days"));
//
//
//                                    if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GOING_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {
//
//                                        handler.AddConferGoingPackCharge(conferencePackageModel, true);
//
//                                    } else {
//
//                                        handler.AddConferGoingPackCharge(conferencePackageModel, true);
//                                    }
//                                }
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

                                if (item.has("department_id"))
                                    model.setDepartment_id(item.getString("department_id"));
                                else
                                    model.setDepartment_id("");
                                model.setDepartment_name(item.getString("department_name"));
                                model.setTarget_audience_speciality(item.getString("speciality_name"));
                                model.setMedical_profile_name(item.getString("medical_profile_name"));
                                model.setDiscount_percentage(item.getString("discount_percentage"));
                                model.setDiscount_description(item.getString("discount_description"));

                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {

                                    handler.AddGoingConferenceDetails(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddGoingConferenceDetails(model, false);
                                }


                                normal_packagesArr = item.getJSONArray("packages");
                                foreign_packagesArr = item.getJSONArray("foreign_packages");

                                ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
                                ConferencePackageModelForgein addForgeinPackageMYConferenceModel = new ConferencePackageModelForgein();

                                /*for normal package*/
                                if (normal_packagesArr.length() > 0) {
                                    for (int k = 0; k < normal_packagesArr.length(); k++) {
                                        JSONObject confPack = normal_packagesArr.getJSONObject(k);
                                        conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
                                        conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                                        conferencePackageModel.setConference_pack_day(confPack.getString("package_detail"));

                                        if (k == 0) {
                                            try {
                                                handler.deleteAddPackageGoingConference(item.getString("conference_id"));
                                            } catch (SQLiteException e) {
                                                //e.printStackTrace();
                                            }
                                        }

                                        handler.AddPackageGoingConference(conferencePackageModel);
                                    }
                                }

                                /*for forgein package*/
                                if (foreign_packagesArr.length() > 0) {
                                    for (int k = 0; k < foreign_packagesArr.length(); k++) {
                                        JSONObject confPack = normal_packagesArr.getJSONObject(k);
                                        addForgeinPackageMYConferenceModel.setConference_pack_id(item.getString("conference_id"));
                                        addForgeinPackageMYConferenceModel.setConference_pack_charge(confPack.getString("price"));
                                        addForgeinPackageMYConferenceModel.setConference_pack_day(confPack.getString("package_detail"));

                                        if (k == 0) {
                                            handler.deleteAddForgeinPackageGoingConference(item.getString("conference_id"));
                                        }

                                        handler.AddForgeinPackageGoingConference(addForgeinPackageMYConferenceModel);
                                    }
                                }
                                /*end of forgein package*/

                            }
                        }

//
//                        if (header.has("LikeConference")) {
//                            Log.d("KDKDKDK333", "kDKKD333");
//
//                            JSONArray header2 = header.getJSONArray("LikeConference");
//                            for (int i = 0; i < header2.length(); i++) {
//                                JSONObject item = header2.getJSONObject(i);
//                                ConferenceDetailsModel model = new ConferenceDetailsModel();
//                                ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
//                                model.setId(item.getString("id"));
//                                model.setConference_id(item.getString("conference_id"));
//                                model.setConference_name(item.getString("conference_name"));
//                                model.setFrom_date(item.getString("from_date"));
//                                model.setTo_date(item.getString("to_date"));
//                                model.setFrom_time(item.getString("from_time"));
//                                model.setTo_time(item.getString("to_time"));
//                                model.setVenue(item.getString("venue"));
//                                model.setEvent_host_name(item.getString("event_host_name"));
//                                model.setSpeciality(item.getString("speciality"));
//                                model.setContact(item.getString("contact"));
//                                model.setLocation("");
//                                model.setBrochuers_file(item.getString("brochuers_file"));
//
//                                model.setConference_type_id(item.getString("conference_type_id"));
//                                model.setMedical_profile_id(item.getString("medical_profile_id"));
//                                model.setCredit_earnings(item.getString("credit_earnings"));
//                                model.setPayment_mode(item.getString("payment_mode"));
//                                model.setTotal_days_price(item.getString("total_days_price"));
//                                model.setAccomdation(item.getString("accomdation"));
//                                model.setMember_concessions(item.getString("member_concessions"));
//                                model.setStudent_concessions(item.getString("student_concessions"));
//                                model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
//                                model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));
////                                model.setBrochuers_charge(item.getString("brochuers_charge"));
////                                model.setBrochuers_days(item.getString("brochuers_days"));
//                                model.setLatitude(item.getString("latitude"));
//                                model.setLogitude(item.getString("longitude"));
////                                model.setRegistration_fee(item.getString("registration_fee"));
////                                model.setRegistration_days(item.getString("registration_days"));
//                                model.setEvent_sponcer(item.getString("keynote_speakers"));
//
//
//                                JSONArray dayArrayCharge = item.getJSONArray("packages");
//
////                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
////
////                                    handler.AddMyConferImage(imageModel, true);
////
////                                } else {
////
////                                    handler.AddMyConferImage(imageModel, false);
////                                }
//
//
//                                for (int k = 0; k < dayArrayCharge.length(); k++) {
//                                    JSONObject confPack = dayArrayCharge.getJSONObject(k);
//                                    Log.d("conferecneidddd11", confPack.getString("price"));
//                                    Log.d("conferecneidddd22", confPack.getString("days"));
//                                    conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
//
//                                    conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
//                                    conferencePackageModel.setConference_pack_day(confPack.getString("days"));
//
//
//                                    if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SAVE_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {
//
//                                        handler.AddConferSavePackCharge(conferencePackageModel, true);
//
//                                    } else {
//
//                                        handler.AddConferSavePackCharge(conferencePackageModel, true);
//                                    }
//                                }
//                                //model
//                                //model.setParticular_country_id("");
//                                model.setParticular_country_id(item.getString("particular_country_id"));
//                                model.setParticular_country_name(item.getString("particular_country_name"));
//                                model.setParticular_state_id(item.getString("particular_state_id"));
//                                model.setParticular_state_name(item.getString("particular_state_name"));
//                                model.setStatus(item.getString("status"));
//                                model.setAdd_date(item.getString("add_date"));
//                                model.setModify_date(item.getString("modify_date"));
//                                model.setPayment_status("2");
//                                model.setShow_like(item.getString("like_status"));
//                                model.setParticular_city_id(item.getString("particular_city_id"));
//                                //model.setParticular_city_id("");
//                                model.setParticular_city_name(item.getString("particular_city_name"));
//                                model.setAddress_type("");
//                                model.setAvailable_seat(item.getString("available_seat"));
//                                model.setConference_description(item.getString("conference_description"));
//                                Log.d("MODELOFLISTSTATUS22", item.getString("id") + "");
//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {
//
//                                    handler.AddSaveConferenceDetails(model, true);
//
//                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//                                } else {
//                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
//                                    handler.AddSaveConferenceDetails(model, false);
//                                }
//                            }
//                        }

                        arrayList = handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1");
                        Log.d("arralylistGoingconf", arrayList.size() + "");
                        if (arrayList.size() > 0) {
                            TicketsGoingAdapter ticketsAdapter = new TicketsGoingAdapter(getContext(), R.layout.tickets_row, arrayList);
                            //ticketsAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(ticketsAdapter);

                            if (((MyConferencesActivity) getActivity()).isFromNotification) {

                                ArrayList<ConferenceDetailsModel> arrayList2 = handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1", ((MyConferencesActivity) getActivity()).conference_id);

                                if (arrayList2.size() > 0) {
                                    Intent intent = new Intent(getActivity(), GoingConferenceDetailsActivity.class);
                                    intent.putExtra("conference_id", arrayList2.get(0).getConference_id());
                                    intent.putExtra("conference_name", arrayList2.get(0).getConference_name());
                                    intent.putExtra("event_host_name", arrayList2.get(0).getEvent_host_name());
                                    intent.putExtra("brochuers_file", arrayList2.get(0).getBrochuers_file());
                                    intent.putExtra("venue", arrayList2.get(0).getVenue());
                                    intent.putExtra("speciality", arrayList2.get(0).getSpeciality());
                                    intent.putExtra("add_date", arrayList2.get(0).getAdd_date());
                                    intent.putExtra("contact", arrayList2.get(0).getContact());
                                    intent.putExtra("from_date", arrayList2.get(0).getFrom_date());
                                    intent.putExtra("from_time", arrayList2.get(0).getFrom_time());
                                    intent.putExtra("conference_description", arrayList2.get(0).getConference_description());
                                    intent.putExtra("conference_type_id", arrayList2.get(0).getConference_type_id());
                                    intent.putExtra("credit_earnings", arrayList2.get(0).getCredit_earnings());
                                    intent.putExtra("total_days_price", arrayList2.get(0).getTotal_days_price());
                                    intent.putExtra("accomdation", arrayList2.get(0).getAccomdation());
                                    intent.putExtra("member_concessions", arrayList2.get(0).getMember_concessions());
                                    intent.putExtra("student_concessions", arrayList2.get(0).getStudent_concessions());
                                    intent.putExtra("price_hike_after_date", arrayList2.get(0).getPrice_hike_after_date());
                                    intent.putExtra("price_hike_after_percentage", arrayList2.get(0).getPrice_hike_after_percentage());
                                    intent.putExtra("available_seat", arrayList2.get(0).getAvailable_seat());
                                    intent.putExtra("to_date", arrayList2.get(0).getTo_date());
                                    intent.putExtra("to_time", arrayList2.get(0).getTo_time());
                                    intent.putExtra("event_sponcer", arrayList2.get(0).getEvent_sponcer());
                                    intent.putExtra("payment_mode", arrayList2.get(0).getPayment_mode());
                                    intent.putExtra("medical_profile_id", arrayList2.get(0).getMedical_profile_id());
                                    intent.putExtra("latitude", arrayList2.get(0).getLatitude());
                                    intent.putExtra("longitude", arrayList2.get(0).getLogitude());
                                    intent.putExtra("keynote_speakers", arrayList2.get(0).getEvent_sponcer());
                                    intent.putExtra("discount_percentage", arrayList2.get(0).getDiscount_percentage());
                                    intent.putExtra("discount_description", arrayList2.get(0).getDiscount_description());
                                    intent.putExtra("conference_type_name", arrayList2.get(0).getConference_type_name());
                                    intent.putExtra("gst", arrayList2.get(0).getGst());
                                    intent.putExtra("medical_profile_name", arrayList2.get(0).getMedical_profile_name());
                                    intent.putExtra("special_name", arrayList2.get(0).getTarget_audience_speciality());
                                    intent.putExtra("department_id", arrayList2.get(0).getDepartment_id());
                                    intent.putExtra("department_name", arrayList2.get(0).getDepartment_name());
                                    intent.putExtra("like_status", arrayList2.get(0).getShow_like());
                                    getActivity().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }

                                ((MyConferencesActivity) getActivity()).isFromNotification = false;
                                ((MyConferencesActivity) getActivity()).conference_id = "";
                            }
                        } else {
                            txt_goingconf_message.setVisibility(View.VISIBLE);
                        }
                    } else {
                        arrayList = handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1");
                        Log.d("arralylistGoingconf", arrayList.size() + "");
                        if (arrayList.size() > 0) {
                            TicketsGoingAdapter ticketsAdapter = new TicketsGoingAdapter(getContext(), R.layout.tickets_row, arrayList);
                            ticketsAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(ticketsAdapter);
                        } else {
                            txt_goingconf_message.setVisibility(View.VISIBLE);
                        }
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }

        }
    }


    JSONArray normal_packagesArr = null;
    JSONArray foreign_packagesArr = null;
}
