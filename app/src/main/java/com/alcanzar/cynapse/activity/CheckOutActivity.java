package com.alcanzar.cynapse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.PromoCodeApi;
import com.alcanzar.cynapse.api.ShortListofJobPostApi;
import com.alcanzar.cynapse.api.Update_Package_StatusAPI;
import com.alcanzar.cynapse.api.applyForRequestJobApi;
import com.alcanzar.cynapse.api.job_payment_statusAPI;
import com.alcanzar.cynapse.app.AppController;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.AppEnvironment;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.ServiceUtility;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

//import com.payumoney.core.PayUmoneyConfig;
//import com.payumoney.core.PayUmoneyConstants;
//import com.payumoney.core.PayUmoneySdkInitializer;
//import com.payumoney.core.entity.TransactionResponse;
//import com.payumoney.sdkui.ui.utils.PPConfig;
//import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
//import com.payumoney.sdkui.ui.utils.ResultModel;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener {

    TextView title, txt_applyjob, txt_basic, txt_sub_price, txt_taxamnt, txt_totamnt, txt_subscription, txt_apply, txt_percentage, txt_ctc, txt_ctcamont;
    ImageView btnBack, titleIcon;
    String jobApply = "", price = "", details = "", id = "", add_date = "", plan_type = "", percentage = "", ctc = "", prom_price = "",
            promocode_name = "", promocode_id = "", job_id = "", job_title_id = "", medical_profile_id = "";
    Button btn_amount_pay;
    String TAG = "CHECKOUT";
    static String prom_price1 = "";
    AppCustomPreferenceClass mAppPreference;
    double total = 0.0;
    double per=0.0;
    double totalctc = 0.0;
    LinearLayout lnr_4, lnr_2, lnr_3, lnr_7, lnr_6;
    View line_2, line_8;
    boolean checkout = false;
    EditText txt_promocode;
    //private ProgressDialog pDialog;
  //  private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    Integer randomNum = ServiceUtility.randInt(0, 9999999);
    boolean checkPromo = false;
    boolean checkAll = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        mAppPreference = new AppCustomPreferenceClass();
        ((AppController) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
        btnBack = findViewById(R.id.btnBack);
        titleIcon = findViewById(R.id.titleIcon);
        txt_applyjob = findViewById(R.id.txt_applyjob);
        txt_basic = findViewById(R.id.txt_basic);
        txt_sub_price = findViewById(R.id.txt_sub_price);
        txt_taxamnt = findViewById(R.id.txt_taxamnt);
        txt_totamnt = findViewById(R.id.txt_totamnt);
        btn_amount_pay = findViewById(R.id.btn_amount_pay);
        txt_subscription = findViewById(R.id.txt_subscription);
        txt_promocode = findViewById(R.id.txt_promocode);

        txt_percentage = findViewById(R.id.txt_percentage);
        txt_ctc = findViewById(R.id.txt_ctc);
        txt_ctcamont = findViewById(R.id.txt_ctcamont);

        txt_apply = findViewById(R.id.txt_apply);
        lnr_4 = findViewById(R.id.lnr_4);
        lnr_2 = findViewById(R.id.lnr_2);
        lnr_3 = findViewById(R.id.lnr_3);
        lnr_7 = findViewById(R.id.lnr_7);
        lnr_6 = findViewById(R.id.lnr_6);
        line_2 = findViewById(R.id.line_2);
        line_8 = findViewById(R.id.line_8);
        titleIcon.setVisibility(View.GONE);
        title = findViewById(R.id.title);
        title.setText(R.string.review_paymt);
        btnBack.setOnClickListener(this);
        btn_amount_pay.setOnClickListener(this);
        txt_apply.setOnClickListener(this);
        lnr_6.setVisibility(View.VISIBLE);
        AppCustomPreferenceClass.removeKey(CheckOutActivity.this, AppCustomPreferenceClass.promo_price);
        AppCustomPreferenceClass.removeKey(CheckOutActivity.this, AppCustomPreferenceClass.promo_code);

        checkout = getIntent().getBooleanExtra("checkout", false);
        if (checkout) {
            if (getIntent() != null) {
                id = getIntent().getStringExtra("id");
                price = getIntent().getStringExtra("price");
                jobApply = getIntent().getStringExtra("jobApply");
                details = getIntent().getStringExtra("job_shortlist");
                plan_type = getIntent().getStringExtra("plan_type");
                percentage = getIntent().getStringExtra("percentage");
                job_id = getIntent().getStringExtra("job_id");
                ctc = getIntent().getStringExtra("ctc");
            }
        } else {
            if (getIntent() != null) {
                jobApply = getIntent().getStringExtra("jobApply");
                price = getIntent().getStringExtra("price");
                details = getIntent().getStringExtra("details");
                plan_type = getIntent().getStringExtra("plan_type");
                job_id = getIntent().getStringExtra("job_id");
                job_title_id = getIntent().getStringExtra("job_title_id");
                medical_profile_id = getIntent().getStringExtra("medical_profile_id");
            }
        }

        Log.d("job_title_id", "<><<" + plan_type);
        Log.d("medical_profile_id", "<><<" + medical_profile_id);
//        Log.d("job_apllyes",jobApply);
//        Log.d("priceee",price);



        if (plan_type.equalsIgnoreCase("1")) {
            line_2.setVisibility(View.VISIBLE);
            lnr_2.setVisibility(View.VISIBLE);
            line_8.setVisibility(View.GONE);
            lnr_7.setVisibility(View.GONE);
            txt_applyjob.setText("Plan - " + " " + "Basic");
            // txt_taxamnt.setText("19.98");
            total = (Double.parseDouble(price));
            txt_totamnt.setText(String.valueOf(price));
            txt_subscription.setText(details + " " + "Job");
//            prom_price=AppCustomPreferenceClass.readString(getApplicationContext(),AppCustomPreferenceClass.promo_price,"");
//            if(!prom_price.equalsIgnoreCase(""))
//            {
//                if (total > Double.parseDouble(prom_price)) {
//                    total = total - Double.parseDouble(prom_price);
//                    Log.d("TOTALCTCAOM222", total + "");
//                    txt_totamnt.setText(String.valueOf(total));
//                    Log.d("AFTEROROCODE111", total + "");
//                }
//            }
        } else if (plan_type.equalsIgnoreCase("2")) {
            try{
            line_8.setVisibility(View.GONE);
            line_2.setVisibility(View.VISIBLE);
            lnr_7.setVisibility(View.GONE);
            lnr_2.setVisibility(View.VISIBLE);
            txt_applyjob.setText("Plan - " + " " + "Premium");
            // txt_taxamnt.setText("19.98");
            txt_totamnt.setText(String.valueOf(price));
            total = (Double.parseDouble(price));
            txt_subscription.setText(details + " " + "Job");
        }catch (Exception e){
            e.printStackTrace();
    }
//            prom_price=AppCustomPreferenceClass.readString(getApplicationContext(),AppCustomPreferenceClass.promo_price2,"");
//            if(!prom_price.equalsIgnoreCase(""))
//            {
//                if (total > Double.parseDouble(prom_price)) {
//                    total = total - Double.parseDouble(prom_price);
//                    Log.d("TOTALCTCAOM222", total + "");
//                    txt_totamnt.setText(String.valueOf(total));
//                    Log.d("AFTEROROCODE111", total + "");
//                }
//            }
        } else if (plan_type.equalsIgnoreCase("3")) {
            line_8.setVisibility(View.VISIBLE);
            line_2.setVisibility(View.GONE);
            totalctc = (Double.parseDouble(percentage) * Double.parseDouble(ctc)) / 100;
            Log.d("TOTALCTCAOMT", totalctc + "");
            txt_percentage.setText(percentage + "%");
            txt_ctc.setText("(" + ctc + ")");
            txt_ctcamont.setText("Rs " + String.valueOf(totalctc));
            lnr_7.setVisibility(View.VISIBLE);
            lnr_2.setVisibility(View.GONE);
            txt_applyjob.setText("Plan - " + " " + "HeadHunter");
            txt_totamnt.setText(String.valueOf(totalctc));
//            prom_price=AppCustomPreferenceClass.readString(getApplicationContext(),AppCustomPreferenceClass.promo_price3,"");
//            if(!prom_price.equalsIgnoreCase(""))
//            {
//                if (total > Double.parseDouble(prom_price)) {
//                    total = total - Double.parseDouble(prom_price);
//                    Log.d("TOTALCTCAOM222", total + "");
//                    txt_totamnt.setText(String.valueOf(total));
//                    Log.d("AFTEROROCODE111", total + "");
//                }
//            }
        } else {

        }

        Log.d("TOTALLLL", total + "");
        //txt_applyjob.setText("Plan - "+" "+details);
//        if (price.equalsIgnoreCase("")) {
//
//        } else {
//            total = Double.parseDouble(price);
//            if (prom_price.equalsIgnoreCase("")) {
//
//            } else {
//                total = total - Double.parseDouble(prom_price);
//                txt_totamnt.setText(String.valueOf(total));
//                Log.d("AFTEROROCODEHHH", total + "");
//            }
//
//            txt_basic.setText("RS "+ price);
//            txt_sub_price.setText(price);
//        }
        Log.d("PRICEEKDK11", price);
        txt_basic.setText("RS " + price);
        txt_sub_price.setText(price);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btn_amount_pay:
                Log.d("TOTOAAMOTYT", txt_totamnt.getText().toString());
//                pDialog = new ProgressDialog(CheckOutActivity.this, R.style.DialogTheme);
//                pDialog.setCancelable(false);
//                pDialog.show();
//                View v = LayoutInflater.from(CheckOutActivity.this).inflate(R.layout.custom_progress_view, null, false);
//                pDialog.setContentView(v);
                //launchPayUMoneyFlow(String.valueOf(txt_totamnt.getText().toString()));
                // btn_amount_pay.setClickable(false);
                if (plan_type.equalsIgnoreCase("3"))
                {
                    try {
                        ShortListofJobPostApi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    {
                        try
                        {
                            if (Float.parseFloat(txt_totamnt.getText().toString()) > 0)
                            {
                                try {
                                    Intent intent = new Intent(CheckOutActivity.this, WebViewActivity.class);
                                    intent.putExtra("order_id", randomNum.toString());
                                    intent.putExtra("job_id", job_id);
                                    intent.putExtra("job_bool", true);
                                    intent.putExtra("totalAmount", txt_totamnt.getText().toString());
                                    intent.putExtra("total_jobs", details);
                                    intent.putExtra("plan_type", jobApply);
                                    intent.putExtra("medical_profile_id", medical_profile_id);
                                    intent.putExtra("title_id", job_title_id);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {

                                    job_payment_api(job_id, jobApply, txt_totamnt.getText().toString(), "1", randomNum.toString());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    Update_Package_StatusAPI(details, jobApply, medical_profile_id, job_title_id);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Intent intent1 = new Intent(CheckOutActivity.this, MainActivity.class);
                                intent1.putExtra("tabPress", "2");
                                startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                            }

                        }
                        catch (NumberFormatException e)
                        {
                            MyToast.toastShort(CheckOutActivity.this, "Amount is not valid");
                        }
//                    if (jobApply.equalsIgnoreCase("1")) {
//                        try {
//                            Update_Package_StatusAPI(details, jobApply, medical_profile_id, job_title_id);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        // MyToast.logMsg(TAG,"Job Applied Successfully !");
//                    } else if (jobApply.equalsIgnoreCase("2")) {
//                        //  MyToast.logMsg(TAG,"Candidate Shortlisted Successfully !");
//                        try {
//                            Update_Package_StatusAPI(details, jobApply, medical_profile_id, job_title_id);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
                break;
            case R.id.txt_apply:
                if (!TextUtils.isEmpty(txt_promocode.getText().toString())) {
                    try {
                        GetPromocodeApi(txt_promocode.getText().toString());
//                        prom_price = AppCustomPreferenceClass.readString(CheckOutActivity.this, AppCustomPreferenceClass.promo_price, "");



//                    txt_taxamnt.setText("RS"+prom_price);
//                    Log.d("TOTALCTCAOMT3333",prom_price+"");
//                    Log.d("TOTALCTCAOMT444",prom_price1+"");
//                    if(prom_price.equalsIgnoreCase(""))
//                    {
//
//                    }else {
//                        totalctc=totalctc-Double.parseDouble(prom_price);
//                        txt_totamnt.setText("RS"+String.valueOf(totalctc));
//                        Log.d("AFTEROROCODE",totalctc+"");
//                    }
//
//
//                    if(prom_price.equalsIgnoreCase(""))
//                    {
//
//                    }else {
//                        total=total-Double.parseDouble(prom_price);
//                        txt_totamnt.setText("RS"+String.valueOf(total));
//                        Log.d("AFTEROROCODE",total+"");
//                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Please enter promocode", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    public void GetPromocodeApi(final String promcode) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("promo_code", promcode);
        params.put("job_id", job_id);

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new PromoCodeApi(this, header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    Log.d("Promocodeapiii", response.toString());
//                    AppCustomPreferenceClass.removeKey(CheckOutActivity.this, AppCustomPreferenceClass.promo_price);
//                    AppCustomPreferenceClass.removeKey(CheckOutActivity.this, AppCustomPreferenceClass.promo_code);
//                    AppCustomPreferenceClass.removeKey(CheckOutActivity.this, AppCustomPreferenceClass.promo_price3);
                    if (res_code.equals("1")) {


                        lnr_4.setVisibility(View.GONE);
//                        MyToast.toastShort(CheckOutActivity.this,res_msg);
                        JSONObject jsonObject = header.getJSONObject("promocode");

                        prom_price = jsonObject.getString("percentage");
                        promocode_name = jsonObject.getString("promocode_name");
                        promocode_id = jsonObject.getString("promocode_id");
                       // promo_per = jsonObject.getString("percentage");





//                        if(plan_type.equalsIgnoreCase("1"))
                        if(!checkPromo){
                            if (!AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.promo_code, "").equals("") && AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.promo_code, "").equals(promcode))
                            {
                                // ++++++*******************************************************************************************************************************************checkPromo=true;
                                AppCustomPreferenceClass.writeString(CheckOutActivity.this, AppCustomPreferenceClass.promo_price, prom_price);
                                AppCustomPreferenceClass.writeString(CheckOutActivity.this, AppCustomPreferenceClass.promo_code, promcode);
//                        }
//                        else if (!AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.promo_code, "").equals("") && !AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.promo_code, "").equals(promcode))
//                        {
//                            checkPromo=false;
//                            Toast.makeText(CheckOutActivity.this, "Only one promocode can be used at a time", Toast.LENGTH_SHORT).show();
                            }  else{
                                checkPromo=true;
                                AppCustomPreferenceClass.writeString(CheckOutActivity.this, AppCustomPreferenceClass.promo_price, prom_price);
                                AppCustomPreferenceClass.writeString(CheckOutActivity.this, AppCustomPreferenceClass.promo_code, promcode);
                            }
                        }else{
//                            checkPromo=false;
                            checkAll=true;
                            Toast.makeText(CheckOutActivity.this, "Promocode already applied", Toast.LENGTH_SHORT).show();
                        }


// if(plan_type.equalsIgnoreCase("2"))
//                            AppCustomPreferenceClass.writeString(CheckOutActivity.this, AppCustomPreferenceClass.promo_price2, prom_price);
//                        if(plan_type.equalsIgnoreCase("3"))
//                            AppCustomPreferenceClass.writeString(CheckOutActivity.this, AppCustomPreferenceClass.promo_price3, prom_price);


                        if(checkPromo && ! checkAll){

                            txt_taxamnt.setText("RS " + prom_price);
                            Log.d("TOTALCTCAOMT3333", prom_price + "");
                            Log.d("TOTALCTCAOMT444", totalctc + "");
                            if (plan_type.equalsIgnoreCase("1")) {
                                if (prom_price.equalsIgnoreCase("")) {

                                } else {
//                                    if (total > Double.parseDouble(prom_price)) {
                                    if(100>=Double.parseDouble(prom_price)){
                                        per =( total * Double.parseDouble(prom_price))/100;
                                        total=total-per;
                                        Log.d("TOTALCTCAOM222", total + "");
                                        txt_totamnt.setText(String.valueOf(total));
                                        Log.d("AFTEROROCODE111", total + "");
                                    } else {
                                        checkPromo = false;
                                        txt_totamnt.setText(String.valueOf(total));
                                        Toast.makeText(CheckOutActivity.this, "Promocode value can not exceed total value", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else if (plan_type.equalsIgnoreCase("2")) {
                                if (prom_price.equalsIgnoreCase("")) {

                                } else {
//                                    if (total > Double.parseDouble(prom_price)) {
                                    if(100>=Double.parseDouble(prom_price)){
                                        per =( total * Double.parseDouble(prom_price))/100;
                                        total = total-per;
                                        Log.d("TOTALCTCAOM999", total + "");
                                        txt_totamnt.setText(String.valueOf(total));
                                        Log.d("AFTEROROCODE222", total + "");
                                    } else {
                                        txt_totamnt.setText(String.valueOf(total));
                                        Toast.makeText(CheckOutActivity.this, "Promocode value can not exceed total value", Toast.LENGTH_SHORT).show();
                                        checkPromo = false;
                                    }
                                }
                            } else if (plan_type.equalsIgnoreCase("3")) {

                                if (prom_price.equalsIgnoreCase("")) {

                                } else {
//                                    if (total > Double.parseDouble(prom_price)) {
                                    if(100>=Double.parseDouble(prom_price)){
                                        per =( totalctc * Double.parseDouble(prom_price))/100;
                                        totalctc=totalctc-per;
                                        Log.d("TOTALCTCAOMT555", totalctc + "");
                                        txt_totamnt.setText(String.valueOf(totalctc));
                                        Log.d("AFTEROROCODE", totalctc + "");
                                    } else {
                                        checkPromo = false;
                                        txt_totamnt.setText(String.valueOf(totalctc));
                                        Toast.makeText(CheckOutActivity.this, "Promocode value can not exceed total value", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            txt_promocode.setText("");
                        }


                    } else {

//                        Log.d("PLanTYPEEE",plan_type);
//                        Log.d("PRICEEKDK22",price);
//                        Log.d("PRICEEKDK33",total+"");
//
//                        if(plan_type.equalsIgnoreCase("1"))
//                        {
//                            if (prom_price.equalsIgnoreCase("")) {
//
//                            } else {
//                                total = total - Double.parseDouble(prom_price);
//                                Log.d("TOTALCTCAOM222", total + "");
//                                txt_totamnt.setText(String.valueOf(total));
//                                Log.d("AFTEROROCODE111", total + "");
//                            }
//                        }else if(plan_type.equalsIgnoreCase("2"))
//                        {
//                            if (prom_price.equalsIgnoreCase("")) {
//
//                            } else {
//                                total = total - Double.parseDouble("20");
//                                Log.d("TOTALCTCAOM999", total + "");
//                                txt_totamnt.setText(String.valueOf(total));
//                                Log.d("AFTEROROCODE222", total + "");
//                            }
//                        }
                        Log.d("TOTALCTCAOMT", totalctc + "");
                        MyToast.toastLong(CheckOutActivity.this, res_msg);
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

    public void Update_Package_StatusAPI(String tot_jobs, final String plan_type_, String medical_profile_id, String job_title_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("total_jobs", tot_jobs);
        params.put("plan_type", plan_type_);
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new Update_Package_StatusAPI(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    Log.d("Promocodeapiii", response.toString());
                    Log.d("plan_type_", plan_type_);

                    if (res_code.equals("1")) {
                        if (plan_type_.equalsIgnoreCase("1")) {
                            applyForRequestJobApi();
                        } else {
                            ShortListofJobPostApi();
                        }


                    } else {

                        MyToast.toastLong(CheckOutActivity.this, res_msg);
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

    private void applyForRequestJobApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(CheckOutActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", job_id);
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new applyForRequestJobApi(CheckOutActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("appliedddd", response.toString());
                    if (res_code.equals("1")) {

                        // Intent intent = new Intent(CheckOutActivity.this, MyJobActivity.class);
//                        startActivity(intent);
                        MyToast.toastLong(CheckOutActivity.this, res_msg);
//                        finish();
                        //ActivityCompat.finishAffinity(CheckOutActivity.this);
                        try {
                            //   JobDetailsActivity.fas.finish();
                            ////   Basic_Premimum_Activity.fa.finish();
                            //JobDetailActivity.fass.finish();
                            //   NotificationsActivity.noti.finish();
                            //RecommendedJobsActivity.recommend_.finish();

                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }


                    } else {

                        MyToast.toastLong(CheckOutActivity.this, res_msg);
                        finish();
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

    private void ShortListofJobPostApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(CheckOutActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", job_id);
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new ShortListofJobPostApi(CheckOutActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("shortlisttttt", response.toString());
                    if (res_code.equals("1")) {

//                        Intent intent = new Intent(CheckOutActivity.this, MyJobActivity.class);
//                        startActivity(intent);
                        MyToast.toastLong(CheckOutActivity.this, res_msg);
////                        finish();
//                        try {
////                            JobDetailsActivity.fas.finish();
////                            Basic_Premimum_Activity.fa.finish();
////                          //  JobDetailActivity.fass.finish();
////                            NotificationsActivity.noti.finish();
////                            RecommendedJobsActivity.recommend_.finish();
//                            Intent intent = new Intent(CheckOutActivity.this, WebViewActivity.class);
//
//                            intent.putExtra("order_id",  randomNum.toString());
//                            intent.putExtra("job_id",job_id);
//                            intent.putExtra("job_bool",true);
//                            intent.putExtra("totalAmount",txt_totamnt.getText().toString());
//                            startActivity(intent);
//                            finish();
//                        } catch (NullPointerException ne) {
//                            ne.printStackTrace();
//                        }


                        // ActivityCompat.finishAffinity(CheckOutActivity.this);
                        finish();
                    } else {

                        MyToast.toastLong(CheckOutActivity.this, res_msg);
                        finish();
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

    public void job_payment_api(final String job_id, final String plan_type_, String amount, final String payment_status, String order_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", job_id);
        params.put("order_id", order_id);
        params.put("amount", amount);
        params.put("job_type", plan_type_);
        params.put("payment_status", payment_status);
        params.put("promocode",AppCustomPreferenceClass.readString(getApplicationContext(),AppCustomPreferenceClass.promo_code,""));
        params.put("promocode_price",AppCustomPreferenceClass.readString(getApplicationContext(),AppCustomPreferenceClass.promo_price,""));

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        Log.d("PAYMENTAPIREQUEST",params+"");
        new job_payment_statusAPI(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("Promo9899878", response.toString());
                    Log.d("plan_type_", plan_type_);

                    if (res_code.equals("1")) {
//                        if (plan_type_.equalsIgnoreCase("1")) {
//                            applyForRequestJobApi(job_id);
//                        } else {
//                            ShortListofJobPostApi(job_id);
//                        }

                        AppCustomPreferenceClass.removeKey(CheckOutActivity.this, AppCustomPreferenceClass.promo_price);
                        AppCustomPreferenceClass.removeKey(CheckOutActivity.this, AppCustomPreferenceClass.promo_code);
                        MyToast.toastLong(CheckOutActivity.this, res_msg+" successful");
//                        finish();
                    } else {

                        MyToast.toastLong(CheckOutActivity.this, res_msg);
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

//    private void selectProdEnv() {
//
//        new Handler(getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ((AppController) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
////                editor = settings.edit();
////                editor.putBoolean("is_prod_env", true);
////                editor.apply();
////
////                if (PayUmoneyFlowManager.isUserLoggedIn(getApplicationContext())) {
////                    logoutBtn.setVisibility(View.VISIBLE);
////                } else {
////                    logoutBtn.setVisibility(View.GONE);
////                }
//
//                setupCitrusConfigs();
//            }
//        }, AppCustomPreferenceClass.MENU_DELAY);
//    }
//    private void setupCitrusConfigs() {
//        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
//        if (appEnvironment == AppEnvironment.PRODUCTION) {
//            Toast.makeText(CheckOutActivity.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(CheckOutActivity.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
//        }
//    }
}
