package com.alcanzar.cynapse.activity;

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
import com.alcanzar.cynapse.api.GetMyDealApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.BuyingProductsFragment;
import com.alcanzar.cynapse.fragments.SellingProductFragment;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyDealsActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO : header views
    TextView title;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    String FromNoti="";
   public DatabaseHelper handler;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    //TODO: configure titles at the different tabs
    private String[] titleStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deals);
        handler = new DatabaseHelper(this);

        handler.deleteTableName(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER);

        showBannerAds();
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.deals_white);
        title = findViewById(R.id.title);
        title.setText(R.string.my_deals);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        //TODO: initialization of the tabLayout and the viewpager

        viewPager = findViewById(R.id.viewPager);
        setUpViewPager(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //TODO: setting the tab titles
        titleStr = new String[]{getResources().getString(R.string.buy_product),getResources().getString(R.string.sell_product)};
        for(int i = 0;i<titleStr.length;i++){
            tabLayout.getTabAt(i).setText(titleStr[i]);
        }
        if(getIntent()!=null) {
            FromNoti = getIntent().getStringExtra("FromNoti");
            if(FromNoti.equalsIgnoreCase("1"))
            {
                selectPage(0);
            }else
            {
                selectPage(1);
            }
        }
//        try {
//            GetMyDealApi();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
    private void showBannerAds() {
        AdView mAdView;
        MobileAds.initialize(this,
                getResources().getString(R.string.googleAdsAppID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }}

       //TODO: this is used for the dynamic view pager implementation
      private void setUpViewPager(ViewPager viewPager){
           viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
          viewPagerAdapter.addFrag(new BuyingProductsFragment());
          viewPagerAdapter.addFrag(new SellingProductFragment());
          viewPager.setAdapter(viewPagerAdapter);
      }
    void selectPage(int pageIndex){
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }

    public void GetMyDealApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.UserId,""));
        params.put("sync_time", AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.deal_sync_time,""));
        //  params.put("sync_time", "");
        header.put("Cynapse",params);
        new GetMyDealApi(this,header,true){
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MyDealsActivity.this,AppCustomPreferenceClass.deal_sync_time,sync_time);
                    Log.d("STATERESPONSE",response.toString());

                    if(res_code.equals("1")){
                        //MyToast.toastShort(this,res_msg);
                        if(header.has("SellingProduct"))
                        {
                            JSONArray header2 = header.getJSONArray("SellingProduct");
                            for(int i = 0;i<header2.length();i++){
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
                                //model.setPrimary_type( item.getString("primary_type"));
                                //model.setPrimary( item.getString("primary"));
                                // model.setLicence(item.getString("licence"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                //model.setSpecification(item.getString("specification"));
                                //model.setDeal_type(item.getString("deal_type"));
                                // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("1");
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER,DatabaseHelper.id, item.getString("id")))
                                {

                                    handler.AddSellBuyMaster(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddSellBuyMaster(model, false);
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
                                // model.setPrimary_type( item.getString("primary_type"));
                                // model.setPrimary( item.getString("primary"));
                                // model.setLicence(item.getString("licence"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                // model.setSpecification(item.getString("specification"));
                                // model.setDeal_type(item.getString("deal_type"));
                                // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("2");
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER,DatabaseHelper.id, item.getString("buying_id")))
                                {

                                    handler.AddSellBuyMaster(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddSellBuyMaster(model, false);
                                }

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

}
