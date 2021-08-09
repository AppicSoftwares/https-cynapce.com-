package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.ConferenceBookApi;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewBookingActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO : header views
    TextView title, txt_conf_name, date, txt_reg_fee, txt_num_seat, txt_total, internet_total, txt_subtotal,
            time, address, day_date, day, txt_ticket, txt_charge, txt_handlingfee, email, mob, name, num_date, too_date, too_time;
    ImageView btnBack, titleIcon;
    // EditText name;
    Button btnPay;
    int sub_total;
    int total;
    String seat = "", conference_id = "", event_host_name = "", speciality = "", contact = "", registration_fee = "",
            location = "", conference_name = "", from_date = "", from_time = "", to_date = "", to_time = "",medical_profile_category_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_booking);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        title = findViewById(R.id.title);
        txt_conf_name = findViewById(R.id.txt_conf_name);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        address = findViewById(R.id.address);
        day_date = findViewById(R.id.day_date);
        day = findViewById(R.id.day);
        num_date = findViewById(R.id.num_date);
        txt_ticket = findViewById(R.id.txt_ticket);
    //    txt_charge = findViewById(R.id.txt_charge);
        txt_handlingfee = findViewById(R.id.txt_handlingfee);
        email = findViewById(R.id.email);
        mob = findViewById(R.id.mob);
        name = findViewById(R.id.name);
        too_date = findViewById(R.id.to_date);
        too_time = findViewById(R.id.to_time);


        txt_reg_fee = findViewById(R.id.txt_reg_fee);
        txt_num_seat = findViewById(R.id.txt_num_seat);
        txt_total = findViewById(R.id.txt_total);
        internet_total = findViewById(R.id.internet_total);
        txt_subtotal = findViewById(R.id.txt_subtotal);

        title.setText("Review Booking");
        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
        if (getIntent() != null) {
            seat = getIntent().getStringExtra("seat");
            conference_id = getIntent().getStringExtra("conference_id");
            event_host_name = getIntent().getStringExtra("event_host_name");
            speciality = getIntent().getStringExtra("speciality");
            contact = getIntent().getStringExtra("contact");
            registration_fee = getIntent().getStringExtra("registration_fee");
            conference_name = getIntent().getStringExtra("conference_name");
            location = getIntent().getStringExtra("location");
            from_date = getIntent().getStringExtra("from_date");
            from_time = getIntent().getStringExtra("from_time");
            to_date = getIntent().getStringExtra("to_date");
            to_time = getIntent().getStringExtra("to_time");
            Log.d("seatwillbe", seat);
        }
        medical_profile_category_name=AppCustomPreferenceClass.readString(ReviewBookingActivity.this, AppCustomPreferenceClass.medical_profile_category_name, "");
        Log.d("MEDICAL_CATEGORY_NAME",medical_profile_category_name);
        txt_ticket.setText(seat);
        txt_conf_name.setText(conference_name);
        date.setText(from_date);
        time.setText(from_time);
        address.setText(location);
        mob.setText(contact);
      //  txt_charge.setText(registration_fee);
        btnPay.setText("PAY");

        total = Integer.parseInt(registration_fee) * Integer.parseInt(seat);

        txt_reg_fee.setText(registration_fee);
        txt_num_seat.setText(seat);
        txt_total.setText(String.valueOf(total));
        sub_total = total + Integer.parseInt(internet_total.getText().toString());
        txt_subtotal.setText(String.valueOf(sub_total));

        too_time.setText(to_time);
        too_date.setText(to_date);
        email.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.email, ""));
        name.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.name, "")+"("+medical_profile_category_name+")");
        mob.setText(AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.phoneNumber, ""));

        try {
            String pattern = "MM-dd-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = simpleDateFormat.parse(from_date);
            SimpleDateFormat formatnew = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            System.out.println("dateofoutput" + formatnew.format(date));
            String dateSplit[] = formatnew.format(date).split(" ");
            day_date.setText(dateSplit[0]);
            num_date.setText(dateSplit[1]);
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
            day.setText(dayOfTheWeek);

        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnPay:
                //TODO : opening the payment activity here
                //  startActivity(new Intent(ReviewBookingActivity.this,TicketPaymentsActivity.class));
                try {
                    conferenceBookSeatApi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // finish();
                break;
        }
    }

    private void conferenceBookSeatApi() throws JSONException {
//                {
//                    "Cynapse": {
//                    "uuid":"S3994",
//                            "medical_profile_id":"1",
//                            "job_title":"Doctor",
//                            "job_specilization_id":"2",
//                            "sub_specilization":"test",
//                            "location":"lucknow",
//                            "skill_required":"operation expert",
//                            "preffered_for":"lko",
//                            "ctc":"40000",
//                            "no_of_vaccancies":"10",
//                            "job_description" :"It hiering for doctor requierment"
//
//                }
//                }
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ReviewBookingActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("seats", seat);
        params.put("amount", txt_subtotal.getText().toString());

        Log.d("POSTSEATFRAAG", params + "");
        header.put("Cynapse", params);
        new ConferenceBookApi(ReviewBookingActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    Log.d("RESPONSEBOOK", response.toString());
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        MyToast.toastLong(ReviewBookingActivity.this, res_msg);
                        // startActivity(new Intent(ReviewBookingActivity.this,TicketPaymentsActivity.class));
                        finish();
                    } else {
                        MyToast.toastLong(ReviewBookingActivity.this, res_msg);
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
}
