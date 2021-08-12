package com.alcanzar.cynapse.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.ConfGetDetailsApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentConference extends AppCompatActivity implements View.OnClickListener {

    LinearLayout lnr_payment_detail;
    ImageView btnBack;
    TextView conferenceTitle, txt_frm_date, txt_fm_time, txt_t_date, txt_t_time, locationAdres, title, txt_conf_message;
    String confercneType = "CME";
    String conference_id = "", total_amount = "", payment_mode = "", with_total_amount;
    DatabaseHelper handler;
    RelativeLayout rel_conf_pay;
    ArrayList<ConferenceDetailsModel> arrayList = new ArrayList<>();
    String medicalProfile = "", targetAudienceSpeciality = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_conference);
        lnr_payment_detail = findViewById(R.id.lnr_payment_detail);
        lnr_payment_detail.setOnClickListener(this);
        conferenceTitle = findViewById(R.id.conferenceTitle);
        txt_conf_message = findViewById(R.id.txt_conf_message);
        txt_frm_date = findViewById(R.id.txt_frm_date);
        txt_fm_time = findViewById(R.id.txt_fm_time);
        txt_t_date = findViewById(R.id.txt_t_date);
        txt_t_time = findViewById(R.id.txt_t_time);
        locationAdres = findViewById(R.id.locationAdres);
        title = findViewById(R.id.title);
        //conferenceTitle.setText("Health Care Conference" + "(" + confercneType + ")");
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        title.setText("Review Notification");
        handler = new DatabaseHelper(this);
        if (getIntent() != null) {
            conference_id = getIntent().getStringExtra("conference_id");
        }
        Log.d("cnfrnce_payment_id", conference_id);
        try {
            ConfGetDeatils(conference_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        lnr_payment_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Log.d("oidkkdkd","999999");
//                    Intent it = new Intent(PaymentConference.this, ConferencePaymentInfoActivity.class);
//                    it.putExtra("total_amount", total_amount);
//                    it.putExtra("conference_id", conference_id);
//                    startActivity(it);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnBack:
                finish();

                break;
            case R.id.lnr_payment_detail:
                try {
                    //   Log.d("oidkkdkd",arrayList.get(0).getPayment_mode());
                    Intent it = new Intent(PaymentConference.this, ConferencePaymentInfoActivity.class);
                    it.putExtra("total_amount", total_amount);
                    it.putExtra("with_total_amount", with_total_amount);
                    it.putExtra("conference_id", conference_id);
                    it.putExtra("payment_mode", arrayList.get(0).getPayment_mode());
                    it.putExtra("toDate", txt_t_date.getText().toString());
                    it.putExtra("frmDate", txt_frm_date.getText().toString());
                    it.putExtra("medicalProfile", medical_profileAl.toString());
                    it.putExtra("medicalProfileID", medical_profileID);
                    it.putExtra("targetAudienceSpeciality", Target_audience_specialityAl.toString());
                    it.putExtra("targetAudienceSpecialityID", Target_audience_specialityALID);
                    it.putExtra("departmentID", departmentID);
                    it.putExtra("departmentName", departmentAL.toString());
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void ConfGetDeatils(String conf_id) throws JSONException {

        JSONObject header = new JSONObject();
        final JSONObject params = new JSONObject();
        params.put("conference_id", conf_id);
        header.put("Cynapse", params);

        new ConfGetDetailsApi(PaymentConference.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONCONFPAY", response + "");

                    if (res_code.equals("1")) {

                        JSONObject item = header.getJSONObject("Conference");
                        ConferenceDetailsModel model = new ConferenceDetailsModel();
                        model.setId(item.getString("id"));
                        model.setConference_id(item.getString("conference_id"));
                        model.setConference_name(item.getString("conference_name"));
                        model.setFrom_date(item.getString("from_date"));
                        model.setTo_date(item.getString("to_date"));
                        model.setFrom_time(item.getString("from_time"));
                        model.setTo_time(item.getString("to_time"));
                        model.setVenue(item.getString("venue"));
                        model.setConference_type_id(item.getString("conference_type_id"));
                        model.setTotal_days_price(item.getString("pay_for_add_conference"));
                        model.setPayment_mode(item.getString("payment_mode"));
                        model.setWith_notification_cost(item.getString("with_notification_cost"));
                        model.setWithout_notification_cost(item.getString("without_notification_cost"));
                        model.setMedical_profile_id(item.getString("medical_profile_id"));

                        medical_profileID = item.getString("medical_profile_id");
                        Target_audience_specialityALID = item.getString("speciality");
                        departmentID = item.getString("department_id");

                        //==================get Medical Profile name======================
                        medical_profileAl.clear();
                        for (int i = 0; i < item.optJSONArray("medical_profile").length(); i++) {
                            medical_profileAl.add(item.optJSONArray("medical_profile").optJSONObject(i).optString("profile_category_name"));
                        }
                        model.setMedical_profile_name(medical_profileAl.toString());
                        //==================end Medical Profile name======================

                        // ==================get Target_audience_speciality======================
                        Target_audience_specialityAl.clear();
                        for (int i = 0; i < item.optJSONArray("specialization").length(); i++) {
                            Target_audience_specialityAl.add(item.optJSONArray("specialization").optJSONObject(i).optString("specialization_name"));
                        }
                        model.setTarget_audience_speciality(Target_audience_specialityAl.toString());
                        //==================end Target_audience_speciality======================

                        // ==================get Depatment======================
                        if (item.has("department")) {
                            departmentAL.clear();
                            for (int i = 0; i < item.optJSONArray("department").length(); i++) {
                                departmentAL.add(item.optJSONArray("department").optJSONObject(i).optString("department_name"));
                            }
                        }
                        // model.setTarget_audience_speciality(departmentAL.toString());
                        //==================end Depatment======================

                        Log.d("modkelelel", model.getConference_type_id());

                        if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_PAYMENT_CONFERENCE_DETAILS, DatabaseHelper.id, item.getString("id"))) {
                            handler.AddConferencePaymentDetails(model, true);
                        } else {
                            handler.AddConferencePaymentDetails(model, false);
                        }

                        arrayList = handler.getPaymentConferenceDetails(DatabaseHelper.TABLE_PAYMENT_CONFERENCE_DETAILS, item.getString("conference_id"));
                        Log.d("afrayolsitks", arrayList.size() + "");

                        if (arrayList.size() > 0) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                if (arrayList.get(i).getConference_type_id().equalsIgnoreCase("1")) {
                                    conferenceTitle.setText(arrayList.get(i).getConference_name() + "(" + "" + "Conference" + "" + ")");
                                } else if (arrayList.get(i).getConference_type_id().equalsIgnoreCase("2")) {
                                    conferenceTitle.setText(arrayList.get(i).getConference_name() + "(" + "" + "Exhibition" + "" + ")");
                                } else if (arrayList.get(i).getConference_type_id().equalsIgnoreCase("3")) {
                                    conferenceTitle.setText(arrayList.get(i).getConference_name() + "(" + "" + "CME" + "" + ")");
                                } else if (arrayList.get(i).getConference_type_id().equalsIgnoreCase("4")) {
                                    conferenceTitle.setText(arrayList.get(i).getConference_name() + "(" + "" + "Training Courses" + "" + ")");
                                } else if (arrayList.get(i).getConference_type_id().equalsIgnoreCase("5")) {
                                    conferenceTitle.setText(arrayList.get(i).getConference_name() + "(" + "" + "Seminar" + "" + ")");
                                } else if (arrayList.get(i).getConference_type_id().equalsIgnoreCase("other_conference_type")) {
                                    conferenceTitle.setText(arrayList.get(i).getConference_name() + "(" + "" + "Other Conference" + "" + ")");
                                }

                                if (TextUtils.isEmpty(arrayList.get(i).getFrom_date()))
                                    txt_frm_date.setVisibility(View.GONE);
                                else txt_frm_date.setText(arrayList.get(i).getFrom_date());

                                if (TextUtils.isEmpty(arrayList.get(i).getTo_date()))
                                    txt_t_date.setVisibility(View.GONE);
                                else
                                txt_t_date.setText(arrayList.get(i).getTo_date());

                                if (TextUtils.isEmpty(arrayList.get(i).getFrom_time()))
                                    txt_fm_time.setVisibility(View.GONE);
                                else
                                txt_fm_time.setText(arrayList.get(i).getFrom_time());

                                if (TextUtils.isEmpty(arrayList.get(i).getTo_time()))
                                    txt_t_time.setVisibility(View.GONE);
                                else
                                    txt_t_time.setText(arrayList.get(i).getTo_time());

                                if (TextUtils.isEmpty(arrayList.get(i).getVenue()))
                                    locationAdres.setVisibility(View.GONE);
                                else
                                    locationAdres.setText(arrayList.get(i).getVenue());


                                //total_amount = arrayList.get(0).getTotal_days_price();
                                total_amount = arrayList.get(0).getWithout_notification_cost();
                                with_total_amount = arrayList.get(0).getWith_notification_cost();
                                payment_mode = arrayList.get(0).getPayment_mode();
                                Log.d("P*JMEYUJD", payment_mode);
                            }
                        } else {
                            txt_conf_message.setVisibility(View.VISIBLE);
                            //txt_conf_message.setText("No Conferences Founds.");
                        }
                    } else {
                        //   MyToast.toastLong(PaymentConference.this, res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    findViewById(R.id.lnr_payment_detail).setVisibility(View.GONE);
                    findViewById(R.id.txt_conf_message).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
                findViewById(R.id.lnr_payment_detail).setVisibility(View.GONE);
                findViewById(R.id.txt_conf_message).setVisibility(View.VISIBLE);
            }
        };
    }


    ArrayList<String> medical_profileAl = new ArrayList<>();
    String medical_profileID = "";
    ArrayList<String> Target_audience_specialityAl = new ArrayList<>();
    String Target_audience_specialityALID = "";
    String departmentID = "";
    ArrayList<String> departmentAL = new ArrayList<>();
}
