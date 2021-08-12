package com.alcanzar.cynapse.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.FilterClasses.MarketPlaceFilter;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.BooksActivity;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.activity.SellHospitalActivity;
import com.alcanzar.cynapse.activity.SellProductActivity;
import com.alcanzar.cynapse.activity.Sell_Buy_Practice_Activity;
import com.alcanzar.cynapse.activity.TieUpsActivity;
import com.alcanzar.cynapse.adapter.MarketPlaceProductAdapter;
import com.alcanzar.cynapse.api.GetAllProductsApi;
import com.alcanzar.cynapse.api.GetCategoryWiseProductApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.SearchHospitalApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketPlaceFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_FILTER = 504;
    ImageView hospitals, practice, tieUps, equipments, books;
    //TODO: recycle and listing views
    RecyclerView recycleView, recycleView_all;
    LinearLayoutManager linearLayoutManager, all_lay_mgr;
    ArrayList<DashBoardProductModel> arrayList = new ArrayList<>();
    FloatingActionButton btnAdd, btnFilter;
    public static int type = 0;
    DatabaseHelper handler;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView no_record_txt;
    String medicalId = "", phoneNumber = "", title_Id = "";

    boolean checkRequestBoolean = true;

    public MarketPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market_place, container, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        handler = new DatabaseHelper(getActivity());
        btnAdd = view.findViewById(R.id.btnAdd);
        btnFilter = view.findViewById(R.id.btnFilter);
        setColor(btnFilter);
        hospitals = view.findViewById(R.id.hospitals);
        practice = view.findViewById(R.id.practice);
        tieUps = view.findViewById(R.id.tieUps);
        equipments = view.findViewById(R.id.equipments);
        books = view.findViewById(R.id.books);
        no_record_txt = view.findViewById(R.id.no_record_txt);
        hospitals.setOnClickListener(this);
        practice.setOnClickListener(this);
        tieUps.setOnClickListener(this);
        equipments.setOnClickListener(this);
        books.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnFilter.setOnClickListener(this);

        //TODO : this is used for the recycle view and listing purpose
        recycleView = view.findViewById(R.id.recycleView);
        recycleView_all = view.findViewById(R.id.recycleView_all);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(linearLayoutManager);

        all_lay_mgr = new LinearLayoutManager(getActivity());
        all_lay_mgr.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView_all.setLayoutManager(all_lay_mgr);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        Log.e("menulist.size();", "<><><" + arrayList.size());

        try {
            handler.deleteTableName(DatabaseHelper.TABLE_PRODUCT_MASTER);
            GetAllProductsApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnAdd.setVisibility(View.GONE);
        btnFilter.setVisibility(View.GONE);
        recycleView_all.setVisibility(View.VISIBLE);
        recycleView.setVisibility(View.GONE);
        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "", "1");
        if (arrayList.size() > 0) {
            MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
            recycleView_all.setAdapter(myDealsAdapter);
        }
//        try {
//            GetCategoryWiseProductApi("1");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    handler.deleteTableName(DatabaseHelper.TABLE_PRODUCT_MASTER);
                    GetAllProductsApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);
//                try {
//                    GetCategoryWiseProductApi("1");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("jksdbvkjsndf", dx + "," + dy);
                if (dy > 1 && btnAdd.getVisibility() == View.VISIBLE) {
                    btnAdd.hide();
                    btnFilter.hide();
                } else if (dy < 0 && btnAdd.getVisibility() != View.VISIBLE) {
                    btnAdd.show();
                    btnFilter.show();
                } else {
                    btnAdd.show();
                    btnFilter.show();
                }
            }
        });


    }

    public void setColor(FloatingActionButton view) {
        final int newColor = getResources().getColor(R.color.white);
        view.setColorFilter(newColor);
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

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (checkRequestBoolean)
                GetProfileApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFilter:

                Intent intent = new Intent(getActivity(), MarketPlaceFilter.class);
                Log.e("cvasdv", String.valueOf(type));
                intent.putExtra("type", String.valueOf(type));
                startActivityForResult(intent, REQUEST_CODE_FILTER);

                break;
            case R.id.btnAdd:

                //TODO: opening the sell product activity
//                    try {
//                        GetProfileShowDialogApi();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                if (Util.isVerifiedProfile(getActivity())) {
                    if (AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                        //b = false;
                        //showDialog(activity);
                        try {
                            if (Util.isVerifiyEMailPHoneNO(getActivity())) {
                                if (type == 3) {
                                    startActivity(new Intent(getActivity(), SellProductActivity.class));
                                } else if (type == 0) {
                                    startActivity(new Intent(getActivity(), SellHospitalActivity.class));
                                } else if (type == 1) {
                                    startActivity(new Intent(getActivity(), Sell_Buy_Practice_Activity.class));
                                } else if (type == 2) {
                                    startActivity(new Intent(getActivity(), TieUpsActivity.class));
                                } else {
                                    startActivity(new Intent(getActivity(), BooksActivity.class));
                                }
//                                if (phoneNumber.equalsIgnoreCase("") || medicalId.equalsIgnoreCase("") || title_Id.equalsIgnoreCase("")) {
//                                    showDialog();
//                                } else {
//                                    if (type == 3) {
//                                        startActivity(new Intent(getActivity(), SellProductActivity.class));
//                                    } else if (type == 0) {
//                                        startActivity(new Intent(getActivity(), SellHospitalActivity.class));
//                                    } else if (type == 1) {
//                                        startActivity(new Intent(getActivity(), Sell_Buy_Practice_Activity.class));
//                                    } else if (type == 2) {
//                                        startActivity(new Intent(getActivity(), TieUpsActivity.class));
//                                    } else {
//                                        startActivity(new Intent(getActivity(), BooksActivity.class));
//                                    }
//                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
//                        if (phoneNumber.equalsIgnoreCase("") || medicalId.equalsIgnoreCase("") || title_Id.equalsIgnoreCase(""))
//                        {
//                            showDialog();
//                        }
//                        else {
//                            if (type == 3) {
//                                startActivity(new Intent(getActivity(), SellProductActivity.class));
//                            } else if (type == 0) {
//                                startActivity(new Intent(getActivity(), SellHospitalActivity.class));
//                            } else if (type == 1) {
//                                startActivity(new Intent(getActivity(), Sell_Buy_Practice_Activity.class));
//                            } else if (type == 2) {
//                                startActivity(new Intent(getActivity(), TieUpsActivity.class));
//                            } else {
//                                startActivity(new Intent(getActivity(), BooksActivity.class));
//                            }
//                        }
                        if (type == 3) {
                            startActivity(new Intent(getActivity(), SellProductActivity.class));
                        } else if (type == 0) {
                            startActivity(new Intent(getActivity(), SellHospitalActivity.class));
                        } else if (type == 1) {
                            startActivity(new Intent(getActivity(), Sell_Buy_Practice_Activity.class));
                        } else if (type == 2) {
                            startActivity(new Intent(getActivity(), TieUpsActivity.class));
                        } else {
                            startActivity(new Intent(getActivity(), BooksActivity.class));
                        }
                    }
                }
                break;
            case R.id.hospitals:
                type = 0;
                selectHospitals();
                btnAdd.setVisibility(View.VISIBLE);
                btnFilter.setVisibility(View.VISIBLE);
                recycleView.setVisibility(View.VISIBLE);
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "1", "1");
                Log.e("menulist.size();", "<><><" + arrayList.size());
                if (arrayList.size() == 0) {
                    no_record_txt.setVisibility(View.VISIBLE);
                    no_record_txt.setText("No Hospitals to Buy");
                } else {
                    no_record_txt.setVisibility(View.GONE);
                }
                recycleView_all.setVisibility(View.GONE);
                MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
                recycleView.setAdapter(myDealsAdapter);
                try {
                    GetCategoryWiseProductApi("1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.practice:
                recycleView.setVisibility(View.VISIBLE);
                recycleView_all.setVisibility(View.GONE);
                type = 1;
                btnAdd.setVisibility(View.VISIBLE);
                btnFilter.setVisibility(View.VISIBLE);
                selectPractice();
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "2", "1");
                Log.e("menulist.size();", "<><><" + arrayList.size());
                if (arrayList.size() == 0) {
                    no_record_txt.setVisibility(View.VISIBLE);
                    no_record_txt.setText("No Practice to Buy");
                } else {
                    no_record_txt.setVisibility(View.GONE);
                }
                MarketPlaceProductAdapter myDealAdapter = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
                recycleView.setAdapter(myDealAdapter);
                try {
                    GetCategoryWiseProductApi("2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tieUps:
                recycleView.setVisibility(View.VISIBLE);
                recycleView_all.setVisibility(View.GONE);
                type = 2;
                btnAdd.setVisibility(View.VISIBLE);
                btnFilter.setVisibility(View.VISIBLE);
                selectTieUps();
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "3", "1");
                Log.e("menulist.size();", "<><><" + arrayList.size());
                if (arrayList.size() == 0) {
                    no_record_txt.setVisibility(View.VISIBLE);
                    no_record_txt.setText("No TieUps");
                } else {
                    no_record_txt.setVisibility(View.GONE);
                }
                MarketPlaceProductAdapter myAdapter = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
                recycleView.setAdapter(myAdapter);
                try {
                    GetCategoryWiseProductApi("3");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.equipments:
                recycleView.setVisibility(View.VISIBLE);
                recycleView_all.setVisibility(View.GONE);
                type = 3;
                btnAdd.setVisibility(View.VISIBLE);
                btnFilter.setVisibility(View.VISIBLE);
                selectEquipments();
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "4", "1");
                Log.e("menulist.size();", "<><><" + arrayList.size());
                if (arrayList.size() == 0) {
                    no_record_txt.setVisibility(View.VISIBLE);
                    no_record_txt.setText("No Equipment to Buy");
                } else {
                    no_record_txt.setVisibility(View.GONE);
                }
                MarketPlaceProductAdapter myDAdapter = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
                recycleView.setAdapter(myDAdapter);
                try {
                    GetCategoryWiseProductApi("4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.books:
                recycleView.setVisibility(View.VISIBLE);
                recycleView_all.setVisibility(View.GONE);
                type = 4;
                btnAdd.setVisibility(View.VISIBLE);
                btnFilter.setVisibility(View.VISIBLE);
                selectBooks();
                arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "5", "1");
                Log.e("menulist.size();", "<><><" + arrayList.size());
                if (arrayList.size() == 0) {
                    no_record_txt.setVisibility(View.VISIBLE);
                    no_record_txt.setText("No Books to Buy");
                } else {
                    no_record_txt.setVisibility(View.GONE);
                }
                MarketPlaceProductAdapter myDealsAdap = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
                recycleView.setAdapter(myDealsAdap);
                try {
                    GetCategoryWiseProductApi("5");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showDialog()
    {
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
        disp_txt.setText("To post deals please fill mandatory fields in profile");
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
                            if (type == 3) {
                                startActivity(new Intent(getActivity(), SellProductActivity.class));
                            } else if (type == 0) {
                                startActivity(new Intent(getActivity(), SellHospitalActivity.class));
                            } else if (type == 1) {
                                startActivity(new Intent(getActivity(), Sell_Buy_Practice_Activity.class));
                            } else if (type == 2) {
                                startActivity(new Intent(getActivity(), TieUpsActivity.class));
                            } else {
                                startActivity(new Intent(getActivity(), BooksActivity.class));
                            }
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

    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(getActivity(), header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sync_time, sync_time);

                    if (res_code.equals("1")) {
                        //MyToast.toastLong(getActivity(), res_msg);
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");
                        //
//                        for (int i = 0; i < medicalList.size(); i++) {
//                            if (medicalList.get(i).getProfile_category_name().equalsIgnoreCase(item.getString("medical_profile_category_name"))) {
//                                medicalProfile.setSelection(i);
//                            }
//                        }
                        //    AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.medical_profile_id,item.getString("medical_profile_category_id"));
                        medicalId = item.getString("medical_profile_category_id");
                        title_Id = item.getString("title_id");
                        phoneNumber = item.getString("phone_number");
                        Log.d("MEFIIDIDID", medicalId);

                        // item.getString("medical_profile_category_name");
                        //setData();

                    } else {

                        MyToast.toastLong(getActivity(), res_msg);
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

//    @Override
//    public void onResume() {
//        super.onResume();
//        try {
//            GetAllProductsApi();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

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

    private void selectBooks() {
        //TODO: making the specific view Selected
        books.setImageResource(R.drawable.book_selected);
        hospitals.setImageResource(R.drawable.land);
        practice.setImageResource(R.drawable.practice);
        tieUps.setImageResource(R.drawable.tie_ups);
        equipments.setImageResource(R.drawable.equipment);
    }

    private void selectEquipments() {
        equipments.setImageResource(R.drawable.equipment_selected);
        hospitals.setImageResource(R.drawable.land);
        practice.setImageResource(R.drawable.practice);
        tieUps.setImageResource(R.drawable.tie_ups);
        books.setImageResource(R.drawable.book);
    }

    private void selectTieUps() {
        tieUps.setImageResource(R.drawable.tieups_selected);
        hospitals.setImageResource(R.drawable.land);
        practice.setImageResource(R.drawable.practice);
        equipments.setImageResource(R.drawable.equipment);
        books.setImageResource(R.drawable.book);
    }

    private void selectPractice() {
        practice.setImageResource(R.drawable.practice_selected);
        hospitals.setImageResource(R.drawable.land);
        tieUps.setImageResource(R.drawable.tie_ups);
        equipments.setImageResource(R.drawable.equipment);
        books.setImageResource(R.drawable.book);
    }

    private void selectHospitals() {
        hospitals.setImageResource(R.drawable.hospital_selected);
        practice.setImageResource(R.drawable.practice);
        tieUps.setImageResource(R.drawable.tie_ups);
        equipments.setImageResource(R.drawable.equipment);
        books.setImageResource(R.drawable.book);
    }

    private void GetCategoryWiseProductApi(final String categoryId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("category_id", categoryId);
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        // params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.sync_time,""));
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetCategoryWiseProductApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    // AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.sync_time,sync_time);
                    Log.d("STATERESPONSE", response.toString());

                    if (res_code.equals("1")) {
                        // mSwipeRefreshLayout.setRefreshing(false);
                        // MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("CategoryProduct");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            DashBoardProductModel model = new DashBoardProductModel();
                            model.setProduct_id(item.getString("product_id"));
                            model.setProduct_name(item.getString("product_name"));
                            model.setCategory_id(item.getString("category_id"));
                            model.setCategory_name(item.getString("category_name"));
                            model.setPrice(item.getString("price"));
                            model.setCondition_type(item.getString("contact_email"));
                            model.setCondition(item.getString("entity_name"));
                            model.setAge(item.getString("age"));
                            model.setDeal_type(item.getString("quantity"));
                            model.setPractice_category_type(item.getString("practice_category_type"));
                            model.setPractice_category_name(item.getString("practice_category_name"));
                            model.setPractice_type(item.getString("practice_type"));
                            model.setPractice_type_name(item.getString("practice_type_name"));
                            model.setSpecific_locality(item.getString("specific_locality"));
                            model.setLand_length(item.getString("land_length"));
                            model.setLand_width(item.getString("land_width"));
                            model.setTotal_area(item.getString("total_area"));
                            model.setBuild_up_area(item.getString("build_up_area"));
                            model.setType(item.getString("type"));
                            model.setPrimary_type(item.getString("primary_type"));
                            model.setLicence(item.getString("contact_person"));
                            model.setNo_of_bed(item.getString("no_of_bed"));
                            model.setSpecification(item.getString("tie_up_for_name"));
                            // model.setDeal_type(item.getString("deal_type"));
                            model.setDescription(item.getString("description"));
                            model.setProduct_image(item.getString("product_image"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setModify_date(item.getString("modify_date"));
                            model.setStatus(item.getString("status"));
                            // model.setFeatured_product(item.getString("featured_product"));

                            model.setFeatured_product(item.getString("hospital_category_name"));
                            model.setSpecialization_name(item.getString("specialization_name"));
                            // model.setFeatured_product_type(item.getString("featured_product_type"));
                            model.setFeatured_product_type(item.getString("seeking_name"));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_PRODUCT_MASTER, DatabaseHelper.product_id, item.getString("product_id"))) {

                                handler.AddProductMaster(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                handler.AddProductMaster(model, false);
                            }
                        }
                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId, "1");
                        Log.e("menulist.size();", "<><><" + arrayList.size());
                        if (arrayList.size() == 0) {
                            if (Objects.equals(categoryId, "1")) {
                                no_record_txt.setVisibility(View.VISIBLE);
                                no_record_txt.setText("No Hospitals to Buy");
                            } else if (Objects.equals(categoryId, "2")) {
                                no_record_txt.setVisibility(View.VISIBLE);
                                no_record_txt.setText("No Practice to Buy");
                            } else if (Objects.equals(categoryId, "3")) {
                                no_record_txt.setVisibility(View.VISIBLE);
                                no_record_txt.setText("No TieUps");
                            } else if (Objects.equals(categoryId, "4")) {
                                no_record_txt.setVisibility(View.VISIBLE);
                                no_record_txt.setText("No Equipment to Buy");
                            } else if (Objects.equals(categoryId, "5")) {
                                no_record_txt.setVisibility(View.VISIBLE);
                                no_record_txt.setText("No Books to Buy");
                            }

                        } else {
                            no_record_txt.setVisibility(View.GONE);
                        }
                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
                        recycleView.setAdapter(myDealsAdapter);
                        // myDealsAdapter.notifyDataSetChanged();

                    } else {
                        // mSwipeRefreshLayout.setRefreshing(false);
                        //MyToast.toastLong(getActivity(),res_msg);
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

    private void GetAllProductsApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        // params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.sync_time,""));
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetAllProductsApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    // AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.sync_time,sync_time);
                    Log.d("STATERESPONSE", response.toString());

                    if (res_code.equals("1")) {
                        //mSwipeRefreshLayout.setRefreshing(false);
                        // MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("CategoryProduct");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            DashBoardProductModel model = new DashBoardProductModel();
                            model.setProduct_id(item.getString("product_id"));
                            model.setProduct_name(item.getString("product_name"));
                            model.setCategory_id(item.getString("category_id"));
                            model.setCategory_name(item.getString("category_name"));
                            model.setPrice(item.getString("price"));
                            model.setCondition_type(item.getString("contact_email"));
                            model.setCondition(item.getString("entity_name"));
                            model.setAge(item.getString("age"));
                            model.setDeal_type(item.getString("quantity"));
                            model.setPractice_category_type(item.getString("practice_category_type"));
                            model.setPractice_category_name(item.getString("practice_category_name"));
                            model.setPractice_type(item.getString("practice_type"));
                            model.setPractice_type_name(item.getString("practice_type_name"));
                            model.setSpecific_locality(item.getString("specific_locality"));
                            model.setLand_length(item.getString("land_length"));
                            model.setLand_width(item.getString("land_width"));
                            model.setTotal_area(item.getString("total_area"));
                            model.setBuild_up_area(item.getString("build_up_area"));
                            model.setType(item.getString("type"));
                            model.setPrimary_type(item.getString("primary_type"));
                            model.setLicence(item.getString("contact_person"));
                            model.setNo_of_bed(item.getString("no_of_bed"));
                            model.setSpecification(item.getString("tie_up_for_name"));
                            // model.setDeal_type(item.getString("deal_type"));
                            model.setDescription(item.getString("description"));
                            model.setProduct_image(item.getString("product_image"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setModify_date(item.getString("modify_date"));
                            model.setStatus(item.getString("status"));
                            // model.setFeatured_product(item.getString("featured_product"));
                            model.setFeatured_product(item.getString("hospital_category_name"));
                            //model.setFeatured_product_type(item.getString("featured_product_type"));
                            model.setSpecialization_name(item.getString("specialization_name"));
                            model.setFeatured_product_type(item.getString("seeking_name"));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_PRODUCT_MASTER, DatabaseHelper.product_id, item.getString("product_id"))) {

                                handler.AddProductMaster(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                handler.AddProductMaster(model, false);
                            }
                        }

                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, "", "1");

                        Log.e("menulist.size();", "<><><" + arrayList.size());

                        if (arrayList.size() == 0) {
                            no_record_txt.setVisibility(View.VISIBLE);
                            no_record_txt.setText("No Products Available");

                        } else {
                            no_record_txt.setVisibility(View.GONE);
                        }

                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
                        recycleView_all.setAdapter(myDealsAdapter);

                        // myDealsAdapter.notifyDataSetChanged();
                    } else {
                        // mSwipeRefreshLayout.setRefreshing(false);
                        //MyToast.toastLong(getActivity(),res_msg);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILTER && resultCode == Activity.RESULT_OK) {

            if (data != null) {

                checkRequestBoolean = false;
//                MarketFilterModal filterModals=

                String latitudeText = AppCustomPreferenceClass.readString(getContext(), AppCustomPreferenceClass.Latitude, "");
                String longitudeText = AppCustomPreferenceClass.readString(getContext(), AppCustomPreferenceClass.Longitude, "");

                Log.e("latAndLog=", latitudeText + "," + longitudeText);

//                MarketFilterModal marketFilterModal= (MarketFilterModal) data.getSerializableExtra("hospitalFilter");
                JSONObject header = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                int c = Integer.parseInt(data.getStringExtra("marketType")) + 1;
                try {

//                    Log.e("fadgvdvad",marketFilterModal.getHospitalType()+","+filterModals.toString());


                    jsonObject.put("category_type_id", String.valueOf(c));
                    jsonObject.put("deal_id", data.getStringExtra("dealId"));
                    if (c == 1) {
                        jsonObject.put("hospital_type", data.getStringExtra("HosId"));
                        jsonObject.put("practice_type", "");
                        jsonObject.put("tieup_for", "");
                        jsonObject.put("product_name", "");
                        jsonObject.put("tieup_name", "");
                    } else if (c == 2) {
                        jsonObject.put("hospital_type", "");
                        jsonObject.put("practice_type", data.getStringExtra("HosId"));
                        jsonObject.put("tieup_for", "");
                        jsonObject.put("product_name", data.getStringExtra("nameStr"));
                        jsonObject.put("tieup_name", "");
                    } else if (c == 3) {
                        jsonObject.put("hospital_type", "");
                        jsonObject.put("practice_type", "");
                        jsonObject.put("tieup_for", data.getStringExtra("HosId"));
                        jsonObject.put("product_name", "");
                        jsonObject.put("tieup_name", data.getStringExtra("nameStr"));
                    } else {
                        jsonObject.put("hospital_type", "");
                        jsonObject.put("practice_type", "");
                        jsonObject.put("tieup_for", "");
                        jsonObject.put("product_name", data.getStringExtra("nameStr"));
                        jsonObject.put("tieup_name", "");
                    }

                    if (c == 3) {
                        jsonObject.put("tieup_seeking", data.getStringExtra("SeekId"));
                        jsonObject.put("tieup_with", data.getStringExtra("TieUp_withId"));
                    } else {
                        jsonObject.put("tieup_seeking", "");
                        jsonObject.put("tieup_with", "");
                    }

                    jsonObject.put("city", data.getStringExtra("cityString").replace(" ",""));
                    jsonObject.put("specialization", data.getStringExtra("SpecializationString"));
                    if (c == 3) {
                        jsonObject.put("deal_type", "");
                    } else {
                        jsonObject.put("deal_type", data.getStringExtra("DealType"));
                    }

                    jsonObject.put("distance", data.getStringExtra("distanceStr"));


                    jsonObject.put("current_longitude", longitudeText);
                    jsonObject.put("current_latitude", latitudeText);


                    header.put("Cynapse", jsonObject);
                    searchDataMarket(header, String.valueOf(c));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void searchDataMarket(JSONObject header, final String categoryId) {

        arrayList.clear();
        new SearchHospitalApi(getContext(), header) {
            @Override
            public void responseApi(JSONObject response) {

                Log.e("MarketResponseFil", response.toString());

                try {

                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    // AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.sync_time,sync_time);

                    if (res_code.equals("1")) {
                        // mSwipeRefreshLayout.setRefreshing(false);
                        // MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("CategoryProduct");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            DashBoardProductModel model = new DashBoardProductModel();
                            model.setProduct_id(item.getString("product_id"));
                            model.setProduct_name(item.getString("product_name"));
                            model.setCategory_id(item.getString("category_id"));
                            model.setCategory_name(item.getString("category_name"));
                            model.setPrice(item.getString("price"));
                            model.setCondition_type(item.getString("contact_email"));
                            model.setCondition(item.getString("entity_name"));
                            model.setAge(item.getString("age"));
                            model.setDeal_type(item.getString("quantity"));
                            model.setPractice_category_type(item.getString("practice_category_type"));
                            model.setPractice_category_name(item.getString("practice_category_name"));
                            model.setPractice_type(item.getString("practice_type"));
                            model.setPractice_type_name(item.getString("practice_type_name"));
                            model.setSpecific_locality(item.getString("specific_locality"));
                            model.setLand_length(item.getString("land_length"));
                            model.setLand_width(item.getString("land_width"));
                            model.setTotal_area(item.getString("total_area"));
                            model.setBuild_up_area(item.getString("build_up_area"));
                            model.setType(item.getString("type"));
                            model.setPrimary_type(item.getString("primary_type"));
                            model.setLicence(item.getString("contact_person"));
                            model.setNo_of_bed(item.getString("no_of_bed"));
                            model.setSpecification(item.getString("tie_up_for_name"));
                            // model.setDeal_type(item.getString("deal_type"));
                            model.setDescription(item.getString("description"));
                            model.setProduct_image(item.getString("product_image"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setModify_date(item.getString("modify_date"));
                            model.setStatus(item.getString("status"));
                            // model.setFeatured_product(item.getString("featured_product"));

                            model.setFeatured_product(item.getString("hospital_category_name"));
                            model.setSpecialization_name(item.getString("specialization_name"));
                            // model.setFeatured_product_type(item.getString("featured_product_type"));
                            model.setFeatured_product_type(item.getString("seeking_name"));
                            arrayList.add(model);
                        }
                    }
                    Log.e("menulist.size();", "<><><" + arrayList.size());
                    if (arrayList.size() == 0) {
                        if (Objects.equals(categoryId, "1")) {
                            no_record_txt.setVisibility(View.VISIBLE);
                            no_record_txt.setText("No Hospitals to Buy");
                        } else if (Objects.equals(categoryId, "2")) {
                            no_record_txt.setVisibility(View.VISIBLE);
                            no_record_txt.setText("No Practice to Buy");
                        } else if (Objects.equals(categoryId, "3")) {
                            no_record_txt.setVisibility(View.VISIBLE);
                            no_record_txt.setText("No TieUps");
                        } else if (Objects.equals(categoryId, "4")) {
                            no_record_txt.setVisibility(View.VISIBLE);
                            no_record_txt.setText("No Equipment to Buy");
                        } else if (Objects.equals(categoryId, "5")) {
                            no_record_txt.setVisibility(View.VISIBLE);
                            no_record_txt.setText("No Books to Buy");
                        }

                    } else {
                        no_record_txt.setVisibility(View.GONE);
                    }
                    MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(getActivity(), R.layout.marketplacerow, arrayList);
                    recycleView.setAdapter(myDealsAdapter);


                } catch (Exception e) {
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
