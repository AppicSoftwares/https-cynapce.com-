package com.alcanzar.cynapse.fragments;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MyDealsActivity;
import com.alcanzar.cynapse.adapter.MyDealsAdapter;
import com.alcanzar.cynapse.api.GetMyDealApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.DashBoardModel;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyingProductsFragment extends Fragment {
    //TODO: layout views
    RecyclerView recycleView;
    TextView no_record_txt;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DashBoardProductModel> arrayList = new ArrayList<>();
    ArrayList<DashBoardModel> arrayList1 = new ArrayList<>();

    public BuyingProductsFragment() {
        // Required empty public constructor
        Log.d("ldldlww","ppdpd");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buying_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : this is used for the recycle view and
        recycleView = view.findViewById(R.id.recycleView);
        no_record_txt = view.findViewById(R.id.no_record_txt);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(linearLayoutManager);

        try {
            GetMyDealApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        arrayList = ((MyDealsActivity)getActivity()).handler.getSellBuyProducts(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER, "2");
//        if(arrayList.size() > 0)
//        {
//            setData();
//        }else
//        {
//            no_record_txt.setVisibility(View.VISIBLE);
//            no_record_txt.setText("No Buying Products Found");
//        }
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

    public void GetMyDealApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.UserId,""));
        //params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.deal_sync_time,""));
        params.put("sync_time", "");
        header.put("Cynapse",params);

        new GetMyDealApi(getActivity(),header,true){
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.deal_sync_time,sync_time);
                    Log.d("STATERESPONSE",response.toString());

                    if(res_code.equals("1")){
                        MyToast.toastShort(getActivity(),res_msg);
                        if(header.has("SellingProduct"))
                        {
                            JSONArray header2 = header.getJSONArray("SellingProduct");

                            for(int i = 0;i<header2.length();i++)
                            {
                                JSONObject item  = header2.getJSONObject(i);
                                DashBoardProductModel model = new DashBoardProductModel();
                                model.setId(item.getString("id"));
                                model.setProduct_id(item.getString("product_id"));
                                model.setProduct_name(item.getString("product_name"));
                                model.setCategory_id(item.getString("category_id"));
                                model.setCategory_name(item.getString("category_name"));
                                model.setPrice(item.getString("price"));
                               // model.setCondition_type(item.getString("condition_type"));
                               // model.setCondition(item.getString("condition"));
                                model.setAge(item.getString("age"));
                                model.setPractice_category_type(item.getString("practice_category_type"));
                                model.setPractice_category_name(item.getString("practice_category_name"));
                                model.setPractice_type(item.getString("practice_type"));
                                model.setPractice_type_name(item.getString("practice_type_name"));
                                model.setRooms(item.getString("rooms"));
                                model.setCountry_code(item.getString("country"));
                                model.setCountry_name(item.getString("country_name"));
                                model.setState_code(item.getString("state"));
                                model.setState_name(item.getString("state_name"));
                                model.setCity_id(item.getString("city"));
                                model.setCity_name(item.getString("city_name"));
                                model.setSpecific_locality(item.getString("specific_locality"));
                                model.setLand_length(item.getString("land_length"));
                                model.setLand_width(item.getString("land_width"));
                                model.setTotal_area(item.getString("total_area"));
                                model.setBuild_up_area(item.getString("build_up_area"));
                                model.setPrimary_type(item.getString("entity_name"));
                                model.setPrimary( item.getString("contact_person"));
                                model.setLicence(item.getString("contact_no"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                model.setSpecification(item.getString("hospital_category_name"));
                               // model.setDeal_type(item.getString("deal_type"));
                               // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("1");

                                if (!((MyDealsActivity)getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER,DatabaseHelper.id, item.getString("id")))
                                {
                                    ((MyDealsActivity)getActivity()).handler.AddSellBuyMaster(model, true);

                                } else {

                                    ((MyDealsActivity)getActivity()).handler.AddSellBuyMaster(model, false);
                                }
                            }

//                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId);
//                        Log.e("menulist.size();","<><><"+arrayList.size());
//                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(MyDealsActivity.this,R.layout.marketplacerow,arrayList);
//                        recycleView.setAdapter(myDealsAdapter);
                            // myDealsAdapter.notifyDataSetChanged();
                        }
                        if(header.has("BuyingProduct")) {
                            JSONArray header3 = header.getJSONArray("BuyingProduct");
                            for(int i = 0;i<header3.length();i++){
                                JSONObject item  = header3.getJSONObject(i);
                                DashBoardProductModel model = new DashBoardProductModel();
                                // model.setId(item.getString("id"));
                                model.setId(item.getString("buying_id"));
                                model.setProduct_id(item.getString("product_id"));
                                model.setProduct_name(item.getString("product_name"));
                                model.setCategory_id(item.getString("category_id"));
                                model.setCategory_name(item.getString("category_name"));
                                model.setPrice(item.getString("price"));
                                //model.setCondition_type(item.getString("condition_type"));
                               // model.setCondition(item.getString("condition"));
                                model.setAge(item.getString("age"));
                                model.setPractice_category_type(item.getString("practice_category_type"));
                                model.setPractice_category_name(item.getString("practice_category_name"));
                                model.setPractice_type(item.getString("practice_type"));
                                model.setPractice_type_name(item.getString("practice_type_name"));
                                model.setRooms(item.getString("rooms"));
                                model.setCountry_code(item.getString("country"));
                                model.setCountry_name(item.getString("country_name"));
                                model.setState_code(item.getString("state"));
                                model.setState_name(item.getString("state_name"));
                                model.setCity_id(item.getString("city"));
                                model.setCity_name(item.getString("city_name"));
                                model.setSpecific_locality(item.getString("specific_locality"));
                                model.setLand_length(item.getString("land_length"));
                                model.setLand_width(item.getString("land_width"));
                                model.setTotal_area(item.getString("total_area"));
                                model.setBuild_up_area(item.getString("build_up_area"));
                                model.setPrimary_type(item.getString("entity_name"));
                                model.setPrimary( item.getString("contact_person"));
                                model.setLicence(item.getString("contact_no"));
                                //model.setPrimary_type( item.getString("primary_type"));
                               // model.setPrimary( item.getString("primary"));
                                //model.setLicence(item.getString("licence"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                model.setSpecification(item.getString("hospital_category_name"));
                               // model.setDeal_type(item.getString("deal_type"));
                               // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("2");

                                if (!((MyDealsActivity)getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER,DatabaseHelper.id, item.getString("buying_id")))
                                {
                                    ((MyDealsActivity)getActivity()).handler.AddSellBuyMaster(model, true);
                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    ((MyDealsActivity)getActivity()).handler.AddSellBuyMaster(model, false);
                                }

                            }

                            setData();
//                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId);
//                        Log.e("menulist.size();","<><><"+arrayList.size());
//                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(MyDealsActivity.this,R.layout.marketplacerow,arrayList);
//                        recycleView.setAdapter(myDealsAdapter);
                            // myDealsAdapter.notifyDataSetChanged();
                        }

                    }else {
                        no_record_txt.setVisibility(View.VISIBLE);
                        no_record_txt.setText("No Buying Products Found");
                        // MyToast.toastLong(MyDealsActivity.this,res_msg);
                        }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
                MyToast.toastShort(getActivity(),error.getMessage());
            }
        };
    }


    /*public void GetMyDealApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.UserId,""));
        //params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.deal_sync_time,""));
        params.put("sync_time", "");
        //  params.put("sync_time", "");
        header.put("Cynapse",params);
        new GetAllMyProductsAPI(getActivity(),header){
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.deal_sync_time,sync_time);
                    Log.d("STATERESPONSE",response.toString());

                    if(res_code.equals("1"))
                    {
                        MyToast.toastShort(getActivity(),res_msg);
                        if(header.has("SellingProduct"))
                        {
                            JSONArray header2 = header.getJSONArray("SellingProduct");

                            for(int i = 0;i<header2.length();i++)
                            {
                                JSONObject item  = header2.getJSONObject(i);
                                DashBoardProductModel model = new DashBoardProductModel();
                                model.setId(item.getString("id"));
                                model.setProduct_id(item.getString("product_id"));
                                model.setProduct_name(item.getString("product_name"));
                                model.setCategory_id(item.getString("category_id"));
                                model.setCategory_name(item.getString("category_name"));
                                model.setPrice(item.getString("price"));
                               // model.setCondition_type(item.getString("condition_type"));
                               // model.setCondition(item.getString("condition"));
                                model.setAge(item.getString("age"));
                                model.setPractice_category_type(item.getString("practice_category_type"));
                                model.setPractice_category_name(item.getString("practice_category_name"));
                                model.setPractice_type(item.getString("practice_type"));
                                model.setPractice_type_name(item.getString("practice_type_name"));
                                model.setRooms(item.getString("rooms"));
                                model.setCountry_code(item.getString("country"));
                                model.setCountry_name(item.getString("country_name"));
                                model.setState_code(item.getString("state"));
                                model.setState_name(item.getString("state_name"));
                                model.setCity_id(item.getString("city"));
                                model.setCity_name(item.getString("city_name"));
                                model.setSpecific_locality(item.getString("specific_locality"));
                                model.setLand_length(item.getString("land_length"));
                                model.setLand_width(item.getString("land_width"));
                                model.setTotal_area(item.getString("total_area"));
                                model.setBuild_up_area(item.getString("build_up_area"));
                                model.setPrimary_type(item.getString("entity_name"));
                                model.setPrimary( item.getString("contact_person"));
                                model.setLicence(item.getString("contact_no"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                model.setSpecification(item.getString("hospital_category_name"));
                               // model.setDeal_type(item.getString("deal_type"));
                               // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("1");
                                if (!((MyDealsActivity)getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER,DatabaseHelper.id, item.getString("id")))
                                {

                                    ((MyDealsActivity)getActivity()).handler.AddSellBuyMaster(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    ((MyDealsActivity)getActivity()).handler.AddSellBuyMaster(model, false);
                                }
                            }
//                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId);
//                        Log.e("menulist.size();","<><><"+arrayList.size());
//                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(MyDealsActivity.this,R.layout.marketplacerow,arrayList);
//                        recycleView.setAdapter(myDealsAdapter);
                            // myDealsAdapter.notifyDataSetChanged();
                        }
                        if(header.has("BuyingProduct")) {
                            JSONArray header3 = header.getJSONArray("BuyingProduct");
                            for(int i = 0;i<header3.length();i++){
                                JSONObject item  = header3.getJSONObject(i);
                                DashBoardProductModel model = new DashBoardProductModel();
                                // model.setId(item.getString("id"));
                                model.setId(item.getString("buying_id"));
                                model.setProduct_id(item.getString("product_id"));
                                model.setProduct_name(item.getString("product_name"));
                                model.setCategory_id(item.getString("category_id"));
                                model.setCategory_name(item.getString("category_name"));
                                model.setPrice(item.getString("price"));
                                //model.setCondition_type(item.getString("condition_type"));
                               // model.setCondition(item.getString("condition"));
                                model.setAge(item.getString("age"));
                                model.setPractice_category_type(item.getString("practice_category_type"));
                                model.setPractice_category_name(item.getString("practice_category_name"));
                                model.setPractice_type(item.getString("practice_type"));
                                model.setPractice_type_name(item.getString("practice_type_name"));
                                model.setRooms(item.getString("rooms"));
                                model.setCountry_code(item.getString("country"));
                                model.setCountry_name(item.getString("country_name"));
                                model.setState_code(item.getString("state"));
                                model.setState_name(item.getString("state_name"));
                                model.setCity_id(item.getString("city"));
                                model.setCity_name(item.getString("city_name"));
                                model.setSpecific_locality(item.getString("specific_locality"));
                                model.setLand_length(item.getString("land_length"));
                                model.setLand_width(item.getString("land_width"));
                                model.setTotal_area(item.getString("total_area"));
                                model.setBuild_up_area(item.getString("build_up_area"));
                                model.setPrimary_type(item.getString("entity_name"));
                                model.setPrimary( item.getString("contact_person"));
                                model.setLicence(item.getString("contact_no"));
                                //model.setPrimary_type( item.getString("primary_type"));
                               // model.setPrimary( item.getString("primary"));
                                //model.setLicence(item.getString("licence"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                model.setSpecification(item.getString("hospital_category_name"));
                               // model.setDeal_type(item.getString("deal_type"));
                               // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("2");
                                if (!((MyDealsActivity)getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER,DatabaseHelper.id, item.getString("buying_id")))
                                {

                                    ((MyDealsActivity)getActivity()).handler.AddSellBuyMaster(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    ((MyDealsActivity)getActivity()).handler.AddSellBuyMaster(model, false);
                                }
                                setData();
                            }
//                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId);
//                        Log.e("menulist.size();","<><><"+arrayList.size());
//                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(MyDealsActivity.this,R.layout.marketplacerow,arrayList);
//                        recycleView.setAdapter(myDealsAdapter);
                            // myDealsAdapter.notifyDataSetChanged();
                        }

                    }else {

                        // MyToast.toastLong(MyDealsActivity.this,res_msg);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }
*/
    public  void setData()
    {
        arrayList = ((MyDealsActivity)getActivity()).handler.getSellBuyProducts(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER, "2");

        if(arrayList.size() == 0)
        {
            no_record_txt.setVisibility(View.VISIBLE);
            no_record_txt.setText("No Buying Products Found");
        }
        else
        {
            no_record_txt.setVisibility(View.GONE);
            MyDealsAdapter myDealsAdapter = new MyDealsAdapter(getActivity(),R.layout.marketplacerow,arrayList,"4");
            recycleView.setAdapter(myDealsAdapter);
        }

    }
}
