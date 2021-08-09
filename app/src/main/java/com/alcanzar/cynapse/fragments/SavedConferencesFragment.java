package com.alcanzar.cynapse.fragments;

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

import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.activity.MyConferencesActivity;
import com.alcanzar.cynapse.adapter.TicketsSaveAdapter;
import com.alcanzar.cynapse.api.ConferenceMyListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedConferencesFragment extends Fragment {

    //TODO: recycle and other views
    RecyclerView recycleView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ConferenceDetailsModel> arrayList = new ArrayList<>();
    boolean showOnclk = false;
    TextView txt_saveconf_message;
    DatabaseHelper handler;

    public SavedConferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_conferences, container, false);
        //return inflater.inflate(R.layout.activity_ticket_details_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: recycleView and other views listing
        recycleView = view.findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(linearLayoutManager);

        txt_saveconf_message = view.findViewById(R.id.txt_saveconf_message);
        handler = new DatabaseHelper(getActivity());

////
//        arrayList = ((MyConferencesActivity) getActivity()).handler.getSaveConferenceDetails(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, "1","1");
//        Log.d("arralylistshowconf", arrayList.size() + "");
//        if (arrayList.size() > 0) {
//            TicketsSaveAdapter ticketsAdapter = new TicketsSaveAdapter(getContext(), R.layout.tickets_row, arrayList, showOnclk);
//            ticketsAdapter.notifyDataSetChanged();
//            recycleView.setAdapter(ticketsAdapter);
//        }else {
//            txt_saveconf_message.setVisibility(View.VISIBLE);
//        }
//        //TODO: demo data
//        for (int i = 0; i < arrayList.size(); i++) {
//            Log.d("arralylist333333", arrayList.get(i).getShow_like() + "");
//        }
//        TicketsAdapter ticketsAdapter = new TicketsAdapter(getContext(),R.layout.tickets_row,arrayList);
//        recycleView.setAdapter(ticketsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Util.isNetConnected(getActivity())) {
                ConferenceAllMyList();
            } else {
                arrayList = handler.getSaveConferenceDetails(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, "1", "1");
                Log.d("arralylistshowconf", arrayList.size() + "");
                if (arrayList.size() > 0) {
                    TicketsSaveAdapter ticketsAdapter = new TicketsSaveAdapter(getContext(), R.layout.tickets_row, arrayList, showOnclk);
                    ticketsAdapter.notifyDataSetChanged();
                    recycleView.setAdapter(ticketsAdapter);
                } else {
                    txt_saveconf_message.setVisibility(View.VISIBLE);
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
                    arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    // AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sync_time_myconference, sync_time);
                    Log.d("JOBSPONSECONFEsavee", response.toString());

                    if (res_code.equals("1")) {
                        handler.deleteTableName(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
                        // handler.deleteTableName(DatabaseHelper.TABLE_MY_CONFERENCES_PACK_CHARGE);
                        // handler.deleteTableName(DatabaseHelper.TABLE_SAVE_CONFERENCES_PACK_CHARGE);
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

//                        if (header.has("BuyConference")) {
//                            Log.d("KDKDKDK111", "kDKKD111");
//                            JSONArray header2 = header.getJSONArray("BuyConference");
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
//                                    if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GOING_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {
//
//                                        handler.AddConferGoingPackCharge(conferencePackageModel, true);
//
//                                    } else {
//
//                                        handler.AddConferGoingPackCharge(conferencePackageModel, true);
//                                    }
//                                }
////                                model.setBrochuers_charge(item.getString("brochuers_charge"));
////                                model.setBrochuers_days(item.getString("brochuers_days"));
//                                model.setParticular_country_id(item.getString("particular_country_id"));
//                                model.setParticular_country_name(item.getString("particular_country_name"));
//                                model.setParticular_state_id(item.getString("particular_state_id"));
//                                model.setParticular_state_name(item.getString("particular_state_name"));
//                                model.setStatus(item.getString("status"));
//                                model.setAdd_date(item.getString("add_date"));
//                                model.setModify_date(item.getString("modify_date"));
//                                model.setPayment_status("1");
//                                model.setShow_like(item.getString("like_status"));
//                                model.setParticular_city_id(item.getString("particular_city_id"));
//                                //model.setParticular_city_id("");
//                                model.setParticular_city_name(item.getString("particular_city_name"));
//                                model.setAddress_type("");
//                                model.setAvailable_seat(item.getString("available_seat"));
//                                model.setConference_description(item.getString("conference_description"));
//                                Log.d("MODELOFLISTSTATUS22", item.getString("id") + "");
//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {
//
//                                    handler.AddGoingConferenceDetails(model, true);
//
//                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//                                } else {
//                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
//                                    handler.AddGoingConferenceDetails(model, false);
//                                }
//                            }
//                        }
                        if (header.has("LikeConference")) {
                            Log.d("KDKDKDK333", "kDKKD333");

                            JSONArray header2 = header.getJSONArray("LikeConference");
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
                                //model.setTotal_days_price(item.getString("total_days_price"));
                                model.setTotal_days_price("");
                                //model.setAccomdation(item.getString("accomdation"));
                                model.setAccomdation("");
                                //model.setMember_concessions(item.getString("member_concessions"));
                                model.setMember_concessions("");
                                //model.setStudent_concessions(item.getString("student_concessions"));
                                model.setStudent_concessions("");

                                model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
                                model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));
//                                model.setBrochuers_charge(item.getString("brochuers_charge"));
//                                model.setBrochuers_days(item.getString("brochuers_days"));
                                model.setLatitude(item.getString("latitude"));
                                model.setLogitude(item.getString("longitude"));
//                                model.setRegistration_fee(item.getString("registration_fee"));
//                                model.setRegistration_days(item.getString("registration_days"));
                                model.setEvent_sponcer(item.getString("keynote_speakers"));


                                //JSONArray dayArrayCharge = item.getJSONArray("packages");

//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
//
//                                    handler.AddMyConferImage(imageModel, true);
//
//                                } else {
//
//                                    handler.AddMyConferImage(imageModel, false);
//                                }

////
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
                                //model
                                //model.setParticular_country_id("");

                                model.setParticular_country_id(item.getString("particular_country_id"));
                                model.setParticular_country_name(item.getString("particular_country_name"));
                                model.setParticular_state_id(item.getString("particular_state_id"));
                                model.setParticular_state_name(item.getString("particular_state_name"));
                                model.setStatus(item.getString("status"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                //model.setPayment_status("2");
                                model.setPayment_status(item.getString("payment_mode"));
                                model.setShow_like(item.getString("like_status"));
                                model.setParticular_city_id(item.getString("particular_city_id"));
                                //model.setParticular_city_id("");
                                model.setParticular_city_name(item.getString("particular_city_name"));
                                model.setAddress_type("");
                                model.setAvailable_seat(item.getString("available_seat"));
                                //model.setAvailable_seat("");
                                model.setConference_description(item.getString("conference_description"));

                                model.setDiscount_percentage(item.getString("discount_percentage"));
                                model.setDiscount_description(item.getString("discount_description"));
                                //model.setConference_type_name(item.getString("conference_type_name"));
                                model.setConference_type_name("");
                                model.setGst(item.getString("gst"));

                                if (item.has("department_id"))
                                    model.setDepartment_id(item.getString("department_id"));
                                else
                                    model.setDepartment_id("");
                                model.setDepartment_name(item.getString("department_name"));
                                model.setTarget_audience_speciality(item.getString("speciality_name"));
                                model.setMedical_profile_name(item.getString("medical_profile_name"));

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

//                                /*for package*/
//                                JSONArray dayArrayCharge = item.getJSONArray("normal_packages");
//                                JSONArray foreign_packages = item.getJSONArray("foreign_packages");
//
//                                for (int k = 0; k < dayArrayCharge.length(); k++) {
//                                    Log.d("darraychangelength", dayArrayCharge.length() + "");
//                                    PackageSavedConferenceModel conferencePackageModel = new PackageSavedConferenceModel();
//                                    JSONObject confPack = dayArrayCharge.getJSONObject(k);
//                                    Log.d("conferecneidddd11", confPack.getString("price"));
//
//                                    conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
//                                    conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
//                                    conferencePackageModel.setConference_pack_day(confPack.getString("package_detail"));
//
////                                if (!((MainActivity) getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {
////                                    ((MainActivity) getActivity()).handler.AddConferPackCharge(conferencePackageModel, true);
////                                } else {
////                                    ((MainActivity) getActivity()).handler.AddConferPackCharge(conferencePackageModel, false);
////                                }
//
//                                    if (k == 0) {
//                                        handler.deleteAddPackageSavedConference(item.getString("conference_id"));
//                                    }
//                                    handler.AddPackageSavedConference(conferencePackageModel, true);
//                                }
                                /*foreign_packages*/
//                                for (int k = 0; k < foreign_packages.length(); k++) {
//                                    Log.d("darraychangelength", foreign_packages.length() + "");
//                                    ConferencePackageModelForgein conferencePackageModel = new ConferencePackageModelForgein();
//                                    JSONObject confPack = foreign_packages.getJSONObject(k);
//                                    Log.d("conferecneidddd11", confPack.getString("price"));
//
//                                    conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
//                                    conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
//                                    conferencePackageModel.setConference_pack_day(confPack.getString("package_detail"));
////                                if (!((MainActivity) getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CONFERENCES_PACK_CHARGE_FORGEIN, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {
////
////                                    ((MainActivity) getActivity()).handler.AddConferPackChargeForgein(conferencePackageModel, true);
////
////                                } else {
////                                    ((MainActivity) getActivity()).handler.AddConferPackChargeForgein(conferencePackageModel, false);
////                                }
//
//                                    if (k == 0) {
//                                        ((MainActivity) getActivity()).handler.deleteForgeinPAckage(item.getString("conference_id"));
//                                    }
//
//                                    ((MainActivity) getActivity()).handler.AddConferPackChargeForgein(conferencePackageModel, true);
//                                }

                                /*end of package*/
                            }

                        }

                        arrayList = handler.getSaveConferenceDetails(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, "1", "1");
                        Log.d("arralylistshowconf", arrayList.size() + "");
                        if (arrayList.size() > 0) {
                            TicketsSaveAdapter ticketsAdapter = new TicketsSaveAdapter(getContext(), R.layout.tickets_row, arrayList, showOnclk);
                            ticketsAdapter.notifyDataSetChanged();
                            recycleView.setAdapter(ticketsAdapter);
                        } else {
                            txt_saveconf_message.setVisibility(View.VISIBLE);
                        }
                    } else {
                        arrayList = handler.getSaveConferenceDetails(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, "1", "1");
                        Log.d("arralylistshowconf", arrayList.size() + "");
                        if (arrayList.size() > 0) {
                            TicketsSaveAdapter ticketsAdapter = new TicketsSaveAdapter(getContext(), R.layout.tickets_row, arrayList, showOnclk);
                            ticketsAdapter.notifyDataSetChanged();
                            recycleView.setAdapter(ticketsAdapter);
                        } else {
                            txt_saveconf_message.setVisibility(View.VISIBLE);
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
}
