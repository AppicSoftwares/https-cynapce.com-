package com.alcanzar.cynapse.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.AddConference;
import com.alcanzar.cynapse.activity.FilterConferenceActivity;
import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.adapter.TicketsConferenceAdapter;
import com.alcanzar.cynapse.api.ConferenceAllListApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;
import com.alcanzar.cynapse.model.ImageModel;
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

public class TicketsFragment extends Fragment {

    //TODO: used for the adapter and recycle case
    ArrayList<ConferenceDetailsModel> arrayList = new ArrayList<>();
    String medicalId = "", phoneNumber = "", title_Id = "";

    LinearLayoutManager linearLayoutManager;
    RecyclerView recycleView;
    ImageView btnAdd, btnFilter;
    TextView txt_conf_message;
    String showTab = "", filter_type = "", getStateName = "", getConfLoc = "", getConfCateg = "";
    static String cityName = "", stateName = "", countryName = "", change_like_status = "";
    boolean filter_show = false;
    String filter_show_type = "";
    ArrayList<String> pdfList = new ArrayList<String>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String res = "";

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: initialization of the different views

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recycleView = view.findViewById(R.id.recycleView);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setHasFixedSize(true);

        btnAdd = view.findViewById(R.id.btnAdd);
        btnFilter = view.findViewById(R.id.btnFilter);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        txt_conf_message = view.findViewById(R.id.txt_conf_message);

        try {
            GetProfileApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("TABVARIALBLE", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.tabPress, ""));
        Log.d("TACONFLOCC", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.conference_loc, ""));
        Log.d("FILTER_TYPEFFF", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.filter_type, ""));
        System.out.println("FILTER_TYPEFFF3333" + AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.filter_type, ""));
        Log.d("CHOSE_CATEG", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.conference_categ, ""));
        filter_type = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.filter_type, "");
        getConfLoc = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.conference_loc, "");
        getConfCateg = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.conference_categ, "");
        change_like_status = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.change_like_status, "");
        Log.d("TACONFplokijjk11", getConfCateg);

        try {
            if (getArguments() == null) {
                ConferenceAllList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (getArguments() != null)
                        getArguments().clear();
                    ConferenceAllList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && btnAdd.getVisibility() == View.VISIBLE) {
                    btnAdd.setVisibility(View.GONE);
                    btnFilter.setVisibility(View.GONE);
                } else if (dy < 0 && btnAdd.getVisibility() != View.VISIBLE) {
                    btnAdd.setVisibility(View.VISIBLE);
                    btnFilter.setVisibility(View.VISIBLE);
                }
            }
        });

        filter_show_type = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.filter_show, "");
        filter_show = Boolean.parseBoolean(filter_show_type);
        Log.d("TACONFplokijjk", filter_show + "");

        if (filter_show) {

            AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.filter_show);
            if (getConfCateg.equalsIgnoreCase("")) {

                if (getConfLoc.equalsIgnoreCase("")) {
                    if (filter_type.equalsIgnoreCase("1")) {

                        arrayList = ((MainActivity) getActivity()).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1","1");
                        if (arrayList.size() > 0) {
                            setArrayList();
                        } else {
                            txt_conf_message.setVisibility(View.VISIBLE);
                            txt_conf_message.setText("No  Matching result Found");
                        }

                    } else if (filter_type.equalsIgnoreCase("2")) {
                        Log.d("GETCONFCITY", cityName);
                        //AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.filter_type);
                        try {
                            arrayList = ((MainActivity) getActivity()).handler.getConferenceCity(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, cityName);
                            Log.d("arilistcitysize====", arrayList.size() + "");
                            if (arrayList.size() > 0) {
                                setArrayList();
                            } else {
                                txt_conf_message.setVisibility(View.VISIBLE);
                                txt_conf_message.setText("No  Matching result Found");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else if (filter_type.equalsIgnoreCase("3")) {

                        Log.d("GETCONFSTAE", stateName);
                        //AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.filter_type);
                        arrayList = ((MainActivity) getActivity()).handler.getConferenceState(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, stateName);
                        if (arrayList.size() > 0) {
                            setArrayList();
                        } else {
                            txt_conf_message.setVisibility(View.VISIBLE);
                            txt_conf_message.setText("No  Matching result Found");
                        }


                    } else if (filter_type.equalsIgnoreCase("4")) {

                        Log.d("GETCONFSTAECOUNTRY", countryName);
                        arrayList = ((MainActivity) getActivity()).handler.getConferenceCountry(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, countryName);
                        Log.d("GETCONFSTAECOUNTRY1", arrayList.size() + "");
                        if (arrayList.size() > 0) {
                            setArrayList();
                        } else {
                            txt_conf_message.setVisibility(View.VISIBLE);
                            txt_conf_message.setText("No  Matching result Found");
                        }


                    } else if (filter_type.equalsIgnoreCase("5")) {

                    } else if (filter_type.equalsIgnoreCase("")) {
                        Log.d("GETCONNULL", "GETNULLLL");

                        arrayList = ((MainActivity) getActivity()).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1","1");
                        if (arrayList.size() > 0) {
                            setArrayList();
                        } else {
//                        txt_conf_message.setVisibility(View.VISIBLE);
//                        txt_conf_message.setText("No  Matching result Found");
                        }
                    }

                } else {

                    Log.d("GETLOCAIONWILL", getConfLoc);
                    AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.conference_loc);
                    arrayList = ((MainActivity) getActivity()).handler.getConferenceLoaction(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, getConfLoc);

                    if (arrayList.size() > 0) {
                        setArrayList();
                    } else {
                        txt_conf_message.setVisibility(View.VISIBLE);
                        txt_conf_message.setText("No  Matching result Found");
                    }
                }

            } else {

                if (filter_type.equalsIgnoreCase("")) {

                    Log.d("kkkkkkk", "ppppppp");
                    Log.d("GETCONDETAILASS", getConfCateg);
                    AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.conference_categ);
                    arrayList = ((MainActivity) getActivity()).handler.getConferenceCategory1(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, getConfCateg);
                    Log.d("GETCONDETAILASS", arrayList.size() + "");
                    if (arrayList.size() > 0) {
                        setArrayList();
                    } else {
                        txt_conf_message.setVisibility(View.VISIBLE);
                        txt_conf_message.setText("No  Matching result Found");
                    }


                } else if (filter_type.equalsIgnoreCase("1")) {

                    arrayList = ((MainActivity) getActivity()).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1","1");
                    if (arrayList.size() > 0) {
                        setArrayList();
                    } else {
                        txt_conf_message.setVisibility(View.VISIBLE);
                        txt_conf_message.setText("No  Matching result Found");
                    }

                } else if (filter_type.equalsIgnoreCase("2")) {
                    Log.d("GETCONFCITY", cityName);
                    //AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.filter_type);
                    AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.conference_categ);
                    arrayList = ((MainActivity) getActivity()).handler.getConferenceCategory(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, getConfCateg, cityName, filter_type);
                    if (arrayList.size() > 0) {
                        setArrayList();
                    } else {
                        txt_conf_message.setVisibility(View.VISIBLE);
                        txt_conf_message.setText("No  Matching result Found");
                    }

                } else if (filter_type.equalsIgnoreCase("3")) {

                    Log.d("GETCONFSTAE", stateName);
                    //AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.filter_type);
                    AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.conference_categ);
                    arrayList = ((MainActivity) getActivity()).handler.getConferenceCategory(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, getConfCateg, stateName, filter_type);
                    if (arrayList.size() > 0) {
                        setArrayList();
                    } else {
                        txt_conf_message.setVisibility(View.VISIBLE);
                        txt_conf_message.setText("No  Matching result Found");
                    }


                } else if (filter_type.equalsIgnoreCase("4")) {

                    Log.d("GETCONFSTAECOUNTRY", countryName);
                    AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.conference_categ);
                    arrayList = ((MainActivity) getActivity()).handler.getConferenceCategory(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, getConfCateg, countryName, filter_type);
                    Log.d("GETCONFSTAECOUNTRY1", arrayList.size() + "");
                    if (arrayList.size() > 0) {
                        setArrayList();
                    } else {
                        txt_conf_message.setVisibility(View.VISIBLE);
                        txt_conf_message.setText("No  Matching result Found");
                    }
                }
                Log.d("GETREFCATEGOFILTER", filter_type);
            }

        } else {
            Log.d("909090", "00000000");
            AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.filter_show);
            try {
                if (getArguments() == null) {
                    ConferenceAllList();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.filter_type);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(getActivity(), ReviewBookingNewPay.class);

                Intent intent = new Intent(getActivity(), FilterConferenceActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (phoneNumber.equalsIgnoreCase("") || medicalId.equalsIgnoreCase("") || title_Id.equalsIgnoreCase("")) {
//                    showDialog();
//                } else {
//                    AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.all_pdf_list);
//                    Intent intent1 = new Intent(getActivity(), AddConference.class);
//                    startActivity(intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                }

                if (Util.isVerifiedProfile(getActivity())) {
                    if (AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                        //b = false;
                        //showDialog(activity);
                        try {
                            if (Util.isVerifiyEMailPHoneNO(getActivity())) {
                                AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.all_pdf_list);
                                Intent intent1 = new Intent(getActivity(), AddConference.class);
                                startActivity(intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        AppCustomPreferenceClass.removeKey(getActivity(), AppCustomPreferenceClass.all_pdf_list);
                        Intent intent1 = new Intent(getActivity(), AddConference.class);
                        startActivity(intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }

//                try {
//                    GetProfileShowDialogApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });


        if (getArguments() != null) {
            res = getArguments().getString("response");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        fetchDataFromFilter(new JSONObject(res));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, 0);
        }
    }


    //
//    @Override
//    public void onResume() {
//        super.onResume();
//        try {
//            if(Util.isNetConnected(getActivity()))
//            {
//                ConferenceAllList();
//            }else {
//
//                arrayList = ((MainActivity) getActivity()).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1");
//                Log.e("menulistsizewilllbee", "<><><" + arrayList.size());
//
//                if (arrayList.size() > 0) {
//
//                    setArrayList();
//                } else {
//                    txt_conf_message.setVisibility(View.VISIBLE);
//                    txt_conf_message.setText("No Conferences Founds.");
//                }
//
//
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private void showDialog() {
        final Dialog dialogSociallog;
        Button btn_Submit;
        ImageView cross_btn;
        TextView disp_txt;
        dialogSociallog = new Dialog(getActivity());
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.dialog_social_login_details);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSociallog.setCancelable(false);
        cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        disp_txt = dialogSociallog.findViewById(R.id.disp_txt);
        disp_txt.setText("To add Events please fill mandatory fields in profile");
        btn_Submit = dialogSociallog.findViewById(R.id.btn_Submit);
        dialogSociallog.show();
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                intent.putExtra("edit_disable", "true");
                startActivity(intent);
                dialogSociallog.dismiss();

            }
        });

        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSociallog.dismiss();
            }
        });

    }

    private void ConferenceAllList() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        //params.put("uuid", "S75f3");
        //params.put("sync_time", "");
        // params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.sync_time_allconfer, ""));
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new ConferenceAllListApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                fetchData(response);
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }

    private void fetchData(JSONObject response) {

        arrayList.clear();
        JSONObject header = null;

        try
        {
            header = response.getJSONObject("Cynapse");
            String res_msg = header.getString("res_msg");
            String res_code = header.getString("res_code");
            String sync_time = header.getString("sync_time");
            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sync_time_allconfer, sync_time);
            Log.d("JOBSPONSECONFER", response.toString());

            if (res_code.equals("1")) {
                ((MainActivity) getActivity()).handler.deleteTableName(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);
                ((MainActivity) getActivity()).handler.deleteTableName(DatabaseHelper.TABLE_CONFERENCES_PACK_CHARGE);
                //  MyToast.toastLong(getActivity(),res_msg);
                JSONArray header2 = header.getJSONArray("Conference");

                for (int i = 0; i < header2.length(); i++)
                {
                    JSONObject item = header2.getJSONObject(i);
                    ConferenceDetailsModel model = new ConferenceDetailsModel();

                    ImageModel imageModel = new ImageModel();
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

                    if (item.has("contact")) {
                        model.setContact(item.getString("contact"));
                    } else {
                        model.setContact("");
                    }

                    model.setConference_type_id(item.getString("conference_type_id"));
                    model.setMedical_profile_id(item.getString("medical_profile_id"));

                    if (item.has("conference_type_name")) {
                        model.setConference_type_name(item.getString("conference_type_name"));
                    } else {
                        model.setConference_type_name("null");
                    }

                    model.setCredit_earnings(item.getString("credit_earnings"));
                    model.setPayment_mode(item.getString("payment_mode"));
                    //model.setTotal_days_price(iem.getString("total_days_price"));
                    model.setTotal_days_price("");
                    //model.setAccomdation(item.getString("accomdation"));
                    model.setAccomdation("");
                    //model.setMember_concessions(item.getString("member_concessions"));
                    model.setMember_concessions("");
                    //model.setStudent_concessions(item.getString("student_concessions"));
                    model.setStudent_concessions("");
                    model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
                    model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));
                    model.setLocation("");
                    model.setDiscount_percentage(item.getString("discount_percentage"));
                    model.setDiscount_description(item.getString("discount_description"));
                    imageModel.setImage_name(item.getString("brochuers_file"));
                    imageModel.setImage_id(item.getString("conference_id"));

                    JSONArray dayArrayCharge = item.getJSONArray("normal_packages");
                    JSONArray foreign_packages = item.getJSONArray("foreign_packages");

//                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
//
//                                    handler.AddMyConferImage(imageModel, true);
//
//                                } else {
//
//                                    handler.AddMyConferImage(imageModel, false);
//                                }

                    for (int k = 0; k < dayArrayCharge.length(); k++) {
                        Log.d("darraychangelength", dayArrayCharge.length() + "");
                        ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
                        JSONObject confPack = dayArrayCharge.getJSONObject(k);
                        Log.d("conferecneidddd11", confPack.getString("price"));

                        conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
                        conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                        conferencePackageModel.setConference_pack_day(confPack.getString("package_detail"));
//                                if (!((MainActivity) getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CONFERENCES_PACK_CHARGE, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {
//                                    ((MainActivity) getActivity()).handler.AddConferPackCharge(conferencePackageModel, true);
//                                } else {
//                                    ((MainActivity) getActivity()).handler.AddConferPackCharge(conferencePackageModel, false);
//                                }


                        if (k == 0) {
                            ((MainActivity) getActivity()).handler.deleteNormalPAckage(item.getString("conference_id"));
                        }

                        ((MainActivity) getActivity()).handler.AddConferPackCharge(conferencePackageModel, true);
                    }

                    /*foreign_packages*/

                    for (int k = 0; k < foreign_packages.length(); k++) {
                        Log.d("darraychangelength", foreign_packages.length() + "");
                        ConferencePackageModelForgein conferencePackageModel = new ConferencePackageModelForgein();
                        JSONObject confPack = foreign_packages.getJSONObject(k);
                        Log.d("conferecneidddd11", confPack.getString("price"));

                        conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
                        conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                        conferencePackageModel.setConference_pack_day(confPack.getString("package_detail"));
//                                if (!((MainActivity) getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CONFERENCES_PACK_CHARGE_FORGEIN, DatabaseHelper.conference_pack_id, item.getString("conference_id"))) {
//
//                                    ((MainActivity) getActivity()).handler.AddConferPackChargeForgein(conferencePackageModel, true);
//
//                                } else {
//                                    ((MainActivity) getActivity()).handler.AddConferPackChargeForgein(conferencePackageModel, false);
//                                }

                        if (k == 0) {
                            ((MainActivity) getActivity()).handler.deleteForgeinPAckage(item.getString("conference_id"));
                        }

                        ((MainActivity) getActivity()).handler.AddConferPackChargeForgein(conferencePackageModel, true);
                    }


                    model.setBrochuers_file(item.getString("brochuers_file"));
                    model.setPayment_mode(item.getString("payment_mode"));
                    //  model.setBrochuers_days(item.getString("brochuers_days"));
                    model.setLatitude(item.getString("latitude"));
                    model.setLogitude(item.getString("longitude"));
                    model.setParticular_city_id(item.getString("particular_city_id"));
                    model.setParticular_city_name(item.getString("particular_city_name"));
                    model.setEvent_sponcer(item.getString("keynote_speakers"));
                    model.setParticular_country_id(item.getString("particular_country_id"));
                    //model.setParticular_country_id("");
                    model.setParticular_country_name(item.getString("particular_country_name"));
                    //  model.setParticular_state_id(item.getString("particular_state_id"));
                    // model.setParticular_state_name(item.getString("particular_state_name"));
                    model.setStatus(item.getString("status"));
                    model.setActivate_status(item.getString("activate_status"));
                    model.setAdd_date(item.getString("add_date"));
                    model.setModify_date(item.getString("modify_date"));
                    model.setPayment_status("");
                    model.setShow_like(item.getString("like_status"));
//                            model.setParticular_city_id(item.getString("particular_city_id"));
//                            model.setParticular_city_name(item.getString("particular_city_name"));
                    model.setAvailable_seat(item.getString("available_seat"));
                    model.setConference_description(item.getString("conference_description"));
                    model.setGst(item.getString("gst"));
                    model.setBooking_stopped(item.getString("booking_stopped"));


                    if (item.has("department_id"))
                        model.setDepartment_id(item.getString("department_id"));
                    else
                        model.setDepartment_id("");

                    model.setDepartment_name(item.getString("department_name"));
                    model.setTarget_audience_speciality(item.getString("speciality_name"));
                    model.setMedical_profile_name(item.getString("medical_profile_name"));

                    //model.setEvent_sponcer(item.getString("keynote_speakers"));
                    Log.d("MODELOFLIST", item.getString("payment_mode") + "");//event_sponcer


                    if (!((MainActivity) getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CONFERENCES_SHOW_IMAGES, DatabaseHelper.image_id, item.getString("conference_id"))) {
                        ((MainActivity) getActivity()).handler.AddConferImage(imageModel, true);
                    } else {
                        ((MainActivity) getActivity()).handler.AddConferImage(imageModel, false);
                    }

                    if (!((MainActivity) getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.id, item.getString("id"))) {
                        ((MainActivity) getActivity()).handler.AddConferenceDetails(model, true);
                    } else {
                        ((MainActivity) getActivity()).handler.AddConferenceDetails(model, false);
                    }

                }

                /*bablu*/
                //arrayList = ((MainActivity) getActivity()).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1");
                arrayList = ((MainActivity) getActivity()).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1","1");

                /*end*/

                if (arrayList.size() > 0) {
                    txt_conf_message.setVisibility(View.GONE);
                    setArrayList();
                } else {
                    txt_conf_message.setVisibility(View.VISIBLE);
                    txt_conf_message.setText(res_msg);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            } else {

//                        arrayList = ((MainActivity) getActivity()).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1");
//                        Log.e("menulistsizewilllbee", "<><><" + arrayList.size());
//
//                        if (arrayList.size() > 0) {
//                            setArrayList();
//                        } else {
//                            txt_conf_message.setVisibility(View.VISIBLE);
//                            // txt_conf_message.setText("No Conferences Founds.");
//                        }
//
                //MyToast.toastLong(getActivity(), res_msg);
                txt_conf_message.setVisibility(View.VISIBLE);
                txt_conf_message.setText(res_msg);
                mSwipeRefreshLayout.setRefreshing(false);
//                        mSwipeRefreshLayout.setRefreshing(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchDataFromFilter(JSONObject response) {
        // arrayList.clear();
        JSONObject header = null;
        arrayList.clear();

        try {
            header = response.getJSONObject("Cynapse");
            String res_msg = header.getString("res_msg");
            String res_code = header.getString("res_code");
            String sync_time = header.getString("sync_time");
            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sync_time_allconfer, sync_time);
            Log.d("JOBSPONSECONFER", response.toString());

            if (res_code.equals("1"))
            {
                conferencePackageModelAl = new ArrayList<>();

                //  MyToast.toastLong(getActivity(),res_msg);
                JSONArray header2 = header.getJSONArray("Conference");

                for (int i = 0; i < header2.length(); i++) {
                    JSONObject item = header2.getJSONObject(i);
                    ConferenceDetailsModel model = new ConferenceDetailsModel();

                    ImageModel imageModel = new ImageModel();
                    model.setId(item.getString("id"));
                    model.setConference_id(item.getString("conference_id"));
                    model.setConference_name(item.getString("conference_name"));
                    model.setFrom_date(item.getString("from_date"));
                    model.setTo_date(item.getString("to_date"));
                    model.setFrom_time(item.getString("from_time"));
                    model.setTo_time(item.getString("to_time"));
                    model.setFrom_time(item.getString("from_time"));
                    model.setTo_time(item.getString("to_time"));
                    model.setVenue(item.getString("venue"));
                    model.setEvent_host_name(item.getString("event_host_name"));
                    model.setSpeciality(item.getString("speciality"));

                    if (item.has("contact")) {
                        model.setContact(item.getString("contact"));
                    } else {
                        model.setContact("");
                    }

                    model.setConference_type_id(item.getString("conference_type_id"));
                    model.setMedical_profile_id(item.getString("medical_profile_id"));

                    if (item.has("conference_type_name")) {
                        model.setConference_type_name(item.getString("conference_type_name"));
                    } else {
                        model.setConference_type_name("null");
                    }

                    model.setCredit_earnings(item.getString("credit_earnings"));
                    model.setPayment_mode(item.getString("payment_mode"));
                    //model.setTotal_days_price(iem.getString("total_days_price"));
                    model.setTotal_days_price("");
                    //model.setAccomdation(item.getString("accomdation"));
                    model.setAccomdation("");
                    //model.setMember_concessions(item.getString("member_concessions"));
                    model.setMember_concessions("");
                    //model.setStudent_concessions(item.getString("student_concessions"));
                    model.setStudent_concessions("");
                    model.setPrice_hike_after_date(item.getString("price_hike_after_date"));
                    model.setPrice_hike_after_percentage(item.getString("price_hike_after_percentage"));
                    model.setLocation("");
                    model.setDiscount_percentage(item.getString("discount_percentage"));
                    model.setDiscount_description(item.getString("discount_description"));
                    imageModel.setImage_name(item.getString("brochuers_file"));
                    imageModel.setImage_id(item.getString("conference_id"));

                    JSONArray dayArrayCharge = item.getJSONArray("normal_packages");
                    JSONArray foreign_packages = item.getJSONArray("foreign_packages");

                    for (int k = 0; k < dayArrayCharge.length(); k++) {
                        Log.d("darraychangelength", dayArrayCharge.length() + "");
                        ConferencePackageModel conferencePackageModel = new ConferencePackageModel();
                        JSONObject confPack = dayArrayCharge.getJSONObject(k);
                        Log.d("conferecneidddd11", confPack.getString("price"));

                        conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
                        conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                        conferencePackageModel.setConference_pack_day(confPack.getString("package_detail"));
                    }

                    /*foreign_packages*/
                    for (int k = 0; k < foreign_packages.length(); k++) {
                        Log.d("darraychangelength", foreign_packages.length() + "");
                        ConferencePackageModelForgein conferencePackageModel = new ConferencePackageModelForgein();
                        JSONObject confPack = foreign_packages.getJSONObject(k);
                        Log.d("conferecneidddd11", confPack.getString("price"));

                        conferencePackageModel.setConference_pack_id(item.getString("conference_id"));
                        conferencePackageModel.setConference_pack_charge(confPack.getString("price"));
                        conferencePackageModel.setConference_pack_day(confPack.getString("package_detail"));

                        conferencePackageModelAl.add(conferencePackageModel);
                    }

                    model.setBrochuers_file(item.getString("brochuers_file"));
                    model.setPayment_mode(item.getString("payment_mode"));
                    //  model.setBrochuers_days(item.getString("brochuers_days"));
                    model.setLatitude(item.getString("latitude"));
                    model.setLogitude(item.getString("longitude"));
                    model.setParticular_city_id(item.getString("particular_city_id"));
                    model.setParticular_city_name(item.getString("particular_city_name"));
                    model.setEvent_sponcer(item.getString("keynote_speakers"));

                    if (item.has("particular_country_id")) {
                        model.setParticular_country_id(item.getString("particular_country_id"));
                    } else {
                        model.setParticular_country_id("");
                    }

                    if (item.has("particular_country_name")) {
                        model.setParticular_country_name(item.getString("particular_country_name"));
                    } else {
                        model.setParticular_country_name("");
                    }

                    //model.setParticular_country_id("");
                    //  model.setParticular_state_id(item.getString("particular_state_id"));
                    // model.setParticular_state_name(item.getString("particular_state_name"));
                    model.setStatus(item.getString("status"));
                    model.setAdd_date(item.getString("add_date"));
                    model.setModify_date(item.getString("modify_date"));
                    model.setPayment_status("");
                    model.setShow_like(item.getString("like_status"));
                    model.setAvailable_seat(item.getString("available_seat"));
                    model.setConference_description(item.getString("conference_description"));
                    model.setGst(item.getString("gst"));
                    model.setBooking_stopped(item.getString("booking_stopped"));
                    Log.d("MODELOFLIST", item.getString("payment_mode") + "");//event_sponcer

                    arrayList.add(model);
                }

                if (arrayList.size() > 0) {
                    setArrayList();
                    txt_conf_message.setVisibility(View.GONE);
                } else {
                    txt_conf_message.setVisibility(View.VISIBLE);
                    txt_conf_message.setText(res_msg);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                //MyToast.toastLong(getActivity(), res_msg);

                txt_conf_message.setVisibility(View.VISIBLE);
                txt_conf_message.setText(res_msg);
                mSwipeRefreshLayout.setRefreshing(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setArrayList() {
        if (arrayList.size() > 0) {
            TicketsConferenceAdapter requestPostJobAdapter = new TicketsConferenceAdapter(getContext(), R.layout.tickets_row, arrayList);
            recycleView.setAdapter(requestPostJobAdapter);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            GetProfileApi();
            ConferenceAllList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);

        new GetProfileApi(getActivity(), header, false) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("RSPONPROFIL", response.toString());
                    //AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.conf_sync_time, sync_time);

                    if (res_code.equals("1")) {
                        //
                        // MyToast.toastLong(getActivity(), res_msg);
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");

                        item.getString("heighest_degree_id");

                        item.getString("country_code");
                        countryName = item.getString("country_name");
                        Log.d("GETCONFCounty", countryName);

                        item.getString("state_id");
                        stateName = item.getString("state_name");
                        Log.d("GETCONFSTAE3333", stateName);

                        item.getString("city_id");
                        //  cityName = item.getString("city_name");
                        cityName = item.getString("city_name");
                        Log.d("GETCONCITY66666", cityName);
                        medicalId = item.getString("medical_profile_category_id");
                        title_Id = item.getString("title_id");
                        phoneNumber = item.getString("phone_number");
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.medical_profile_category_name, item.getString("medical_profile_category_name"));
                    } else {
                        //  MyToast.toastLong(getActivity(), res_msg);


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

    private void GetProfileShowDialogApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(getActivity(), header, false) {
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
                        if (item.getString("medical_profile_category_id").equalsIgnoreCase("")
                                || item.getString("title_id").equalsIgnoreCase("")) {
                            showSocialDialog();
                        } else {
                            Intent intent1 = new Intent(getActivity(), AddConference.class);
                            startActivity(intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
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

    public void showSocialDialog() {
        final Dialog dialogSociallog;
        Button btn_Submit;
        ImageView cross_btn;
        dialogSociallog = new Dialog(getActivity());
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.dialog_social_login_details);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSociallog.setCancelable(false);

        cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        btn_Submit = dialogSociallog.findViewById(R.id.btn_Submit);
        dialogSociallog.show();

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
                dialogSociallog.dismiss();

            }
        });

        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSociallog.dismiss();
            }
        });
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            // Refresh your fragment here
//            if (getFragmentManager() != null) {
//                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//            }
//
//        }
//    }

    ArrayList<ConferencePackageModelForgein> conferencePackageModelAl;
}
