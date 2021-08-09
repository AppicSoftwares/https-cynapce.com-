package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.SaveConferenceDetailsImageDisplayAdapter;
import com.alcanzar.cynapse.api.BuyProductApi;
import com.alcanzar.cynapse.api.BuyingProductRequestApi;
import com.alcanzar.cynapse.api.GetProductDetailApi;
import com.alcanzar.cynapse.api.ImInterestedApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Constants;
import com.alcanzar.cynapse.utils.CustomPreference;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MarketPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper handler;
    String product_id = "", category_id = "", from_noti = "", deal_type = "";
    ImageView topImage, titleIcon, btnBack, share_in_row, share;
    TextView title, price, quantity, details, form, vegNon, usage, type, brok_unbrok_txt, title_in_row;
    Button btnBuy, btnBuyNow;
    ArrayList<DashBoardProductModel> arrayList = new ArrayList<>();
    private static ViewPager mPager;
    private static int currentPage = 0;
    ArrayList aList;
    public double latitude = 0.0, longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        showBannerAds();
        showInterstitialAds();

        handler = new DatabaseHelper(this);
        topImage = findViewById(R.id.topImage);
        share = findViewById(R.id.share);
        titleIcon = findViewById(R.id.titleIcon);
        share_in_row = findViewById(R.id.share_in_row);
        price = findViewById(R.id.price);
        title = findViewById(R.id.title);
        title_in_row = findViewById(R.id.title_in_row);
        brok_unbrok_txt = findViewById(R.id.brok_unbrok_txt);
        quantity = findViewById(R.id.quantity);
        details = findViewById(R.id.details);
        btnBack = findViewById(R.id.btnBack);
        btnBuy = findViewById(R.id.btnBuy);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        form = findViewById(R.id.form);
        vegNon = findViewById(R.id.vegNon);
        usage = findViewById(R.id.usage);
        type = findViewById(R.id.type);
        mPager = findViewById(R.id.pager);
        titleIcon.setVisibility(View.GONE);

        if (mMap == null) {

            ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMapAsync(this);
//            mapFragment.getMapAsync(this);
        }

        if (getIntent() != null) {
            product_id = getIntent().getStringExtra("prod_id");
            category_id = getIntent().getStringExtra("cat_id");
            from_noti = getIntent().getStringExtra("from_noti");

            Log.d("category_idBOb", category_id);

            arrayList = handler.getMarketProductsDetails(DatabaseHelper.TABLE_PRODUCT_DETAIL_MASTER, product_id);

            if (from_noti.equalsIgnoreCase("true")) {

                brok_unbrok_txt.setVisibility(View.VISIBLE);
                share_in_row.setVisibility(View.VISIBLE);
                btnBuyNow.setVisibility(View.VISIBLE);
                btnBuy.setVisibility(View.GONE);
                share.setVisibility(View.GONE);

                btnBuyNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Util.isVerifiedProfile(MarketPlaceActivity.this)) {

                            if (AppCustomPreferenceClass.readString(MarketPlaceActivity.this, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                                    AppCustomPreferenceClass.readString(MarketPlaceActivity.this, AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                                //b = false;
                                //showDialog(activity);
                                try {
                                    if (Util.isVerifiyEMailPHoneNO(MarketPlaceActivity.this)) {
                                        try {
                                            BuyingProductApi(product_id, category_id, arrayList.get(0).getPrice(), arrayList.get(0).getDeal_type());
                                        } catch (JSONException | IndexOutOfBoundsException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    BuyingProductApi(product_id, category_id, arrayList.get(0).getPrice(), arrayList.get(0).getDeal_type());
                                } catch (JSONException | IndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

            } else {
                brok_unbrok_txt.setVisibility(View.GONE);
                share_in_row.setVisibility(View.GONE);
                btnBuyNow.setVisibility(View.GONE);
            }
            try {
                GetProductDetailApi(category_id, product_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (arrayList.size() > 0) {
                    setArrayList(product_id);
                }
            } catch (IndexOutOfBoundsException iob) {
                iob.printStackTrace();
            }

            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Util.isVerifiedProfile(MarketPlaceActivity.this)) {

                        if (AppCustomPreferenceClass.readString(MarketPlaceActivity.this, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                                AppCustomPreferenceClass.readString(MarketPlaceActivity.this, AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                            //b = false;
                            //showDialog(activity);
                            try {
                                if (Util.isVerifiyEMailPHoneNO(MarketPlaceActivity.this)) {

                                    if (btnBuy.getText().toString().equalsIgnoreCase("BUY")) {
                                        try {
                                            BuyingProductRequestApi(product_id, "");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (btnBuy.getText().toString().equalsIgnoreCase("I'M INTERESTED")) {
                                        try {
                                            ImInterestedApi(product_id);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            BuyingProductRequestApi(product_id, "");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            //showDialogQty();
                            if (btnBuy.getText().toString().equalsIgnoreCase("BUY")) {
                                try {
                                    BuyingProductRequestApi(product_id, "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (btnBuy.getText().toString().equalsIgnoreCase("I'M INTERESTED")) {
                                try {
                                    ImInterestedApi(product_id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    BuyingProductRequestApi(product_id, "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            });

            if (getIntent().hasExtra("where") && getIntent().getStringExtra("where").equals("MyDeails"))
                btnBuy.setVisibility(View.GONE);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    if (arrayList.get(0).getPrimary_type().equalsIgnoreCase("1")) {
                        deal_type = "FOR SELL";
                    } else if (arrayList.get(0).getPrimary_type().equalsIgnoreCase("2")) {
                        deal_type = "FOR BUY";
                    } else {
                        deal_type = "FOR LEASE";
                    }
                    if (category_id.equalsIgnoreCase("1")) {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        //sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
//                  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, arrayList.get(0).getConference_name());
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "PRODUCT DETAILS"
                                + "\n\n" + "Product Name : " + arrayList.get(0).getType()
                                + "\n" + "Build Up Area : " + arrayList.get(0).getBuild_up_area()
                                + "\n" + "Total Area : " + arrayList.get(0).getTotal_area()
                                + "\n" + "No.Of Beds : " + arrayList.get(0).getNo_of_bed()
                                + "\n" + "₹  : " + arrayList.get(0).getPrice()
                                //+ "\n" + "Description  : " + arrayList.get(0).getDescription()
                                + "\n\n" + deal_type
                                + "\n\n" + "Description  : " + arrayList.get(0).getDescription()
                                + "\n" + "Click to download app : " + "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
                        startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));

                    } else if (category_id.equalsIgnoreCase("2")) {

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        //sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
//                  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, arrayList.get(0).getConference_name());
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "PRODUCT DETAILS"
                                + "\n\n" + "Product Name : " + arrayList.get(0).getProduct_name()
                                + "\n" + "Build Up Area : " + arrayList.get(0).getBuild_up_area()
                                + "\n" + "Total Area : " + arrayList.get(0).getTotal_area()
                                + "\n" + "Rooms : " + arrayList.get(0).getRooms()
                                + "\n" + "₹  : " + arrayList.get(0).getPrice()
                                + "\n\n" + deal_type
                                + "\n\n" + "Description  : " + arrayList.get(0).getDescription()
//                            + "\n" + "Product Image : " + arrayList.get(0).getProduct_image()
                                + "\n" + "Click to download app : " + "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
                        startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));


                    } else if (category_id.equalsIgnoreCase("3")) {

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        //sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
//                  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, arrayList.get(0).getConference_name());
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "PRODUCT DETAILS"
                                + "\n\n" + "Tie-Up Name : " + arrayList.get(0).getCondition()
//                            + "\n" + "Contact No. : " + arrayList.get(0).getSpecification()
//                            + "\n" + "Email  : " + arrayList.get(0).getCondition_type()
                                + "\n\nFOR TIE-UP"
                                + "\n\n" + "Description  : " + arrayList.get(0).getDescription()
                                + "\n" + "Click to download app : " + "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
                        startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
                        ;

                    } else if (category_id.equalsIgnoreCase("4")) {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        // sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
//                  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, arrayList.get(0).getConference_name());
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "PRODUCT DETAILS"
                                + "\n\n" + "Product Name : " + arrayList.get(0).getProduct_name()
                                + "\n" + "Age : " + arrayList.get(0).getAge()
                                + "\n" + "₹  : " + arrayList.get(0).getPrice()
                                + "\n\n" + deal_type
                                + "\n\n" + "Description  : " + arrayList.get(0).getDescription()
                                + "\n" + "Click to download app : " + "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
                        startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));


                    } else {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        // sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
//                  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, arrayList.get(0).getConference_name());
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "PRODUCT DETAILS"
                                + "\n\n" + "Product Name : " + arrayList.get(0).getProduct_name()
                                + "\n" + "Age : " + arrayList.get(0).getAge()
                                + "\n" + "₹  : " + arrayList.get(0).getPrice()
                                + "\n\n" + deal_type
                                + "\n\n" + "Description  : " + arrayList.get(0).getDescription()
                                + "\n" + "Click to download app : " + "https://play.google.com/store/apps/details?id=com.alcanzar.cynapse");
                        startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
                    }
                    details.setText(arrayList.get(0).getDescription());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        if (getIntent().hasExtra("where") && getIntent().getStringExtra("where").equals("NotificationAdapter")) {
            share_in_row.setVisibility(View.GONE);
        }

    }


    private void showBannerAds() {
        AdView mAdView;
        MobileAds.initialize(this,
                getResources().getString(R.string.googleAdsAppID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void showDialogQty() {

        Button yes;
        final EditText edit_qty;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.enter_quantity_dialog);
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        yes = dialog.findViewById(R.id.yes);
        edit_qty = dialog.findViewById(R.id.edit_qty);

        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edit_qty.getText().toString())) {

                    if (Integer.parseInt(edit_qty.getText().toString()) > Integer.parseInt(arrayList.get(0).getDeal_type())) {
                        MyToast.toastShort(MarketPlaceActivity.this, "Available quantity is " + arrayList.get(0).getDeal_type());
                    } else {
                        try {
                            BuyingProductRequestApi(product_id, edit_qty.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }

                } else {
                    MyToast.toastShort(MarketPlaceActivity.this, "Please enter Quantity");
                }


            }
        });
    }

    private void initSlider(final ArrayList<String> images) {

        mPager.setAdapter(new SaveConferenceDetailsImageDisplayAdapter(MarketPlaceActivity.this, images));

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, Constants.imgDelay, Constants.imgPeriod);
    }

    private void GetProductDetailApi(final String categoryId, final String product_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("product_id", product_id);
        params.put("category_id", categoryId);
        // params.put("sync_time", AppCustomPreferenceClass.readString(MarketPlaceActivity.this,AppCustomPreferenceClass.sync_time,""));
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetProductDetailApi(MarketPlaceActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MarketPlaceActivity.this, AppCustomPreferenceClass.sync_time, sync_time);

                    if (res_code.equals("1")) {
                        // MyToast.toastLong(MarketPlaceActivity.this,res_msg);
                        JSONObject item = header.getJSONObject("Product");
                        DashBoardProductModel model = new DashBoardProductModel();
                        model.setProduct_id(item.getString("product_id"));
                        model.setProduct_name(item.getString("product_name"));
                        model.setCategory_id(item.getString("category_id"));
                        model.setCategory_name(item.getString("category_name"));
                        model.setPrice(item.getString("price"));
                        model.setCondition_type(item.getString("contact_email"));
                        model.setCondition(item.getString("entity_name"));
                        model.setAge(item.getString("age"));
                        model.setRooms(item.getString("rooms"));
                        model.setSpecific_locality(item.getString("specific_locality"));
                        model.setLand_length(item.getString("land_length"));
                        model.setLand_width(item.getString("land_width"));
                        model.setTotal_area(item.getString("total_area"));
                        model.setBuild_up_area(item.getString("build_up_area"));
                        //model.setType(item.getString("type"));
                        model.setType(item.getString("hospital_category_name"));
                        model.setPrimary_type(item.getString("practice_category_type"));
                        model.setLicence(item.getString("contact_person"));
                        model.setNo_of_bed(item.getString("no_of_bed"));
                        model.setSpecification(item.getString("contact_no"));
                        model.setDeal_type(item.getString("quantity"));
                        model.setDescription(item.getString("description"));
                        model.setSeeking_name(item.getString("seeking_name"));
                        model.setAdd_date(item.getString("add_date"));
                        model.setModify_date(item.getString("modify_date"));
                        model.setStatus(item.getString("status"));


                        try {
                            if (item.has("longitude") && item.has("latitude")) {
                                longitude = Double.parseDouble(item.getString("longitude"));
                                latitude = Double.parseDouble(item.getString("latitude"));
                            }
                        } catch (NumberFormatException e) {
                            latitude = 0.0;
                            longitude = 0.0;
                        }

                        System.out.println("longitude "+longitude+" latitude "+latitude);


                        //=========add Images==============
                        if (item.optJSONArray("product_image").length() > 0) {
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (int i = 0; i < item.optJSONArray("product_image").length(); i++) {
                                arrayList.add(item.optJSONArray("product_image").optJSONObject(i).optString("image"));
                            }
                            model.setProduct_image(arrayList.toString());
                        } else {
                            model.setProduct_image("");
                        }
                        //=========end add Images==============

                        if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_PRODUCT_DETAIL_MASTER, DatabaseHelper.product_id, item.getString("product_id"))) {

                            handler.AddProductDetailMaster(model, true);

                            //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                        } else {
                            //   Log.e("UPDATED", true + " " + model.getProduct_id());
                            handler.AddProductDetailMaster(model, false);
                        }
                        try {
                            setArrayList(product_id);

                        } catch (IndexOutOfBoundsException iob) {
                            iob.printStackTrace();
                        }

                    } else {

                        // MyToast.toastLong(MarketPlaceActivity.this,res_msg);
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

    private void BuyingProductRequestApi(final String product_id, String s) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("product_id", product_id);
        params.put("quantity", s);
        // params.put("sync_time", AppCustomPreferenceClass.readString(MarketPlaceActivity.this,AppCustomPreferenceClass.sync_time,""));
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new BuyingProductRequestApi(MarketPlaceActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MarketPlaceActivity.this, AppCustomPreferenceClass.sync_time, sync_time);

                    if (res_code.equals("1")) {
                        // MyToast.toastLong(MarketPlaceActivity.this, res_msg);
                        JSONObject item = header.getJSONObject("BuyingProductRequest");
                        DashBoardProductModel model = new DashBoardProductModel();
                        model.setProduct_id(item.getString("uuid"));
                        model.setProduct_name(item.getString("type_id"));
                        model.setCategory_id(item.getString("product_id"));
                        model.setCategory_name(item.getString("status"));
                        model.setPrice(item.getString("add_date"));
                        model.setCondition_type(item.getString("modify_date"));
                        showDialog();
                    } else {

                        MyToast.toastLong(MarketPlaceActivity.this, res_msg);
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

    private void ImInterestedApi(final String product_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("interested_uuid", AppCustomPreferenceClass.readString(MarketPlaceActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("product_id", product_id);
        // params.put("sync_time", AppCustomPreferenceClass.readString(context,AppCustomPreferenceClass.sync_time,""));
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new ImInterestedApi(MarketPlaceActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    // AppCustomPreferenceClass.writeString(context,AppCustomPreferenceClass.sync_time,sync_time);

                    if (res_code.equals("1")) {
                        MyToast.toastLong((Activity) MarketPlaceActivity.this, res_msg);
                        JSONObject item = header.getJSONObject("Interested");
                        DashBoardProductModel model = new DashBoardProductModel();
                        model.setProduct_id(item.getString("uuid"));
                        model.setProduct_name(item.getString("message"));
                        model.setCategory_id(item.getString("product_id"));
                        model.setCategory_id(item.getString("msg_type"));
                        model.setCategory_name(item.getString("status"));
                        model.setPrice(item.getString("add_date"));
                        model.setCondition_type(item.getString("modify_date"));
                        //MyToast.toastLong((Activity) context,res_msg);
                        //showDialog();
                    } else {

                        MyToast.toastLong((Activity) MarketPlaceActivity.this, res_msg);
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

    InterstitialAd interstitialAd;

    private void showInterstitialAds() {

        Log.e("interstitialAddShow", "showAdd");
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        MobileAds.initialize(this, getString(R.string.googleAdsAppID));
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.googleAdsInterstitialAdId));
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
//                String keyClose= CustomPreference.readString(MarketPlaceActivity.this,CustomPreference.CheckAddKey,"");
                Log.e("vnkdnv", "sdgvbswdrbv");
                Intent i = new Intent(MarketPlaceActivity.this, MyDealsActivity.class);
                i.putExtra("FromNoti", "1");
                startActivity(i);
                finish();
//                if(keyClose.equals("1")){
//
//                    Intent  intent=new Intent(MarketPlaceActivity.this, AboutActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//
//                }

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();

            }
        });
//
    }


    private void BuyingProductApi(final String product_id, String category_id, String price, String deal_type) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("product_id", product_id);
        params.put("category_id", category_id);
        params.put("quantity", deal_type);
        params.put("price", price);
        params.put("payment_mode", "1");
        header.put("Cynapse", params);
        new BuyProductApi(MarketPlaceActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MarketPlaceActivity.this, AppCustomPreferenceClass.sync_time, sync_time);

                    if (res_code.equals("1")) {
                        MyToast.toastLong(MarketPlaceActivity.this, res_msg);
                        JSONObject item = header.getJSONObject("BuyingProduct");
                        DashBoardProductModel model = new DashBoardProductModel();
                        model.setProduct_id(item.getString("uuid"));
                        model.setProduct_name(item.getString("category_id"));
                        model.setCategory_id(item.getString("product_id"));
                        model.setCategory_id(item.getString("price"));
                        model.setCategory_id(item.getString("payment_mode"));
                        model.setCategory_name(item.getString("status"));
                        model.setPrice(item.getString("add_date"));
                        model.setCondition_type(item.getString("modify_date"));
//                        showDialog();
                        if (interstitialAd.isLoaded() || interstitialAd.isLoading()) {
                            interstitialAd.show();
                            showInterstitialAds();
                        } else {
                            Intent i = new Intent(MarketPlaceActivity.this, MyDealsActivity.class);
                            i.putExtra("FromNoti", "1");
                            startActivity(i);
                            finish();

                        }
                    } else {

                        MyToast.toastLong(MarketPlaceActivity.this, res_msg);
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

    public void showDialog() {
        final Dialog dialog = new Dialog(MarketPlaceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_msg_alert);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        TextView title = dialog.findViewById(R.id.title);
        TextView msg = dialog.findViewById(R.id.msg);
        ImageView close = dialog.findViewById(R.id.close);
        Button btnGotIt = dialog.findViewById(R.id.btnGotIt);
        //TODO :setting different views
        title.setText(R.string.sent);
        //msg.setText(R.string.request_sent);
        if (btnBuy.getText().toString().equalsIgnoreCase("BUY")) {
            msg.setText(R.string.buy_req);
        } else if (btnBuy.getText().toString().equalsIgnoreCase("LEASE")) {
            msg.setText(R.string.lease_req);
        } else {
            msg.setText(R.string.tieUp_req);
        }

        dialog.show();
        //TODO : dismiss the on btn click and close click
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO : finishing the activity
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
    }

    public void setArrayList(String product_id) {
        arrayList = handler.getMarketProductsDetails(DatabaseHelper.TABLE_PRODUCT_DETAIL_MASTER, product_id);
        Log.e("menulist();", "<><><" + arrayList.size());

//        Glide.with(this)
//                .load(arrayList.get(0).getProduct_image())
//                .placeholder(R.drawable.animation_loading_circle)
//                .error(R.drawable.no_img_placeholder)
//                .into(topImage);

        Picasso.with(this).load(arrayList.get(0).getProduct_image()).placeholder(R.drawable.animation_loading_circle).error(R.drawable.no_img_placeholder).into(topImage);

        Log.e("prod", "<><<" + arrayList.get(0).getProduct_name());
        if (arrayList.get(0).getPrimary_type().equalsIgnoreCase("1")) {
            btnBuy.setText("I'M INTERESTED TO SELL");
        } else if (arrayList.get(0).getPrimary_type().equalsIgnoreCase("2")) {
            btnBuy.setText("I'M INTERESTED TO BUY");
            btnBuyNow.setText("BUY");
        } else {
            btnBuy.setText("I'M INTERESTED TO LEASE");
            btnBuyNow.setText("LEASE");
        }
        if (category_id.equalsIgnoreCase("1")) {
            title.setText(arrayList.get(0).getType());
            title_in_row.setText(arrayList.get(0).getType());
            quantity.setText(String.format("%s sqft", arrayList.get(0).getTotal_area()));
            price.setText(String.format("₹ %s", arrayList.get(0).getPrice()));
            form.setText(String.format("Built Up Area : %s", arrayList.get(0).getBuild_up_area()));
            //vegNon.setText(String.format("Type : %s", arrayList.get(0).getType()));
            usage.setText(String.format("No.Of Beds : %s", arrayList.get(0).getNo_of_bed()));
            //type.setText(String.format("Quantity : %s", arrayList.get(0).getDeal_type()));
            brok_unbrok_txt.setText(arrayList.get(0).getDeal_type());
        } else if (category_id.equalsIgnoreCase("2")) {
            title.setText(arrayList.get(0).getProduct_name());
            title_in_row.setText(arrayList.get(0).getProduct_name());
            quantity.setText(String.format("%s sqft", arrayList.get(0).getTotal_area()));
            price.setText(String.format("₹ %s", arrayList.get(0).getPrice()));
            //form.setText(String.format("Type : %s", arrayList.get(0).getType()));
            vegNon.setText(String.format("Rooms : %s", arrayList.get(0).getRooms()));
            usage.setText(String.format("Built Up Area : %s", arrayList.get(0).getBuild_up_area()));
            // type.setText(String.format("Quantity : %s", arrayList.get(0).getDeal_type()));
            brok_unbrok_txt.setText(arrayList.get(0).getDeal_type());
        } else if (category_id.equalsIgnoreCase("3")) {
            title.setText(arrayList.get(0).getCondition());
            title_in_row.setText(arrayList.get(0).getCondition());
            quantity.setText(arrayList.get(0).getLicence());
            quantity.setVisibility(View.GONE);
            price.setText(arrayList.get(0).getSeeking_name());

            form.setText(String.format("Seeking For : %s", arrayList.get(0).getSeeking_name()));
            vegNon.setText("");

//            form.setText(String.format("Contact No. : %s", arrayList.get(0).getSpecification()));
//            vegNon.setText(String.format("Email : %s", arrayList.get(0).getCondition_type()));

            usage.setVisibility(View.GONE);
            btnBuy.setText("TIE-UP");
            btnBuyNow.setText("TIE-UP");
            //type.setText(String.format("Quantity : %s", arrayList.get(0).getDeal_type()));
            brok_unbrok_txt.setText(arrayList.get(0).getDeal_type());

        } else if (category_id.equalsIgnoreCase("4")) {
            title.setText(arrayList.get(0).getProduct_name());
            title_in_row.setText(arrayList.get(0).getProduct_name());
            //quantity.setText(arrayList.get(0).getSpecification());
            quantity.setText(arrayList.get(0).getLicence());
            price.setText(String.format("₹ %s", arrayList.get(0).getPrice()));
            //form.setText(String.format("Condition : %s", arrayList.get(0).getCondition()));
            vegNon.setText(String.format("Age : %s", arrayList.get(0).getAge()));
            usage.setVisibility(View.GONE);
            //type.setText(String.format("Quantity : %s", arrayList.get(0).getDeal_type()));
            brok_unbrok_txt.setText(arrayList.get(0).getDeal_type());
        } else {
            title.setText(arrayList.get(0).getProduct_name());
            title_in_row.setText(arrayList.get(0).getProduct_name());
            quantity.setText(arrayList.get(0).getLicence());
            //quantity.setText(arrayList.get(0).getSpecification());
            price.setText(String.format("₹ %s", arrayList.get(0).getPrice()));
            //form.setText(String.format("Condition : %s", arrayList.get(0).getSpecific_locality()));
            vegNon.setText(String.format("Age : %s", arrayList.get(0).getAge()));
            usage.setVisibility(View.GONE);
            // type.setText(String.format("Quantity : %s", arrayList.get(0).getDeal_type()));
            brok_unbrok_txt.setText(arrayList.get(0).getDeal_type());
        }

        details.setText(arrayList.get(0).getDescription());
        aList = new ArrayList(Arrays.asList(arrayList.get(0).getProduct_image().substring(1, arrayList.get(0).getProduct_image().length() - 1).replace(" ", "").replace("]", "").replace("[", "").split(",")));

        initSlider(aList);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mMap = googleMap;

                googleMap.getUiSettings().setScrollGesturesEnabled(false);

                if (ActivityCompat.checkSelfPermission(MarketPlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MarketPlaceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MarketPlaceActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            11);

                    return;
                }

                mMap.setMyLocationEnabled(true);
                LatLng user = new LatLng(latitude, longitude);
                Log.e("maplatlng", latitude + "" + longitude);
                googleMap.addMarker(new MarkerOptions().position(user));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
                CameraUpdate updateZoom = CameraUpdateFactory.newLatLngZoom(user, 15);
                mMap.animateCamera(updateZoom);
            }
        }, 3000);
    }
}
