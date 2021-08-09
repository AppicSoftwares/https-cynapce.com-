package com.alcanzar.cynapse.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MyConferenceDetails2Adapter;
import com.alcanzar.cynapse.adapter.MyConferenceDetails3Adapter;
import com.alcanzar.cynapse.api.ConferenceSearchAPI;
import com.alcanzar.cynapse.api.GetbookedConferenceAPI;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.MyConferenceDetails2Model;
import com.alcanzar.cynapse.model.MyConferenceModel.BookingDetail;
import com.alcanzar.cynapse.model.MyConferenceModel.MyConferenceDetailsModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyConferenceDetails2 extends AppCompatActivity {

    private MyConferenceDetails2Adapter adapter;
    private Activity activity;
    private RecyclerView recycleView;

    private TextView title;
    private ImageView btnBack;
    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_conference_details2);
        initView();
        setOnClickListener();

        if (!TextUtils.isEmpty(getIntent().getStringExtra("conference_id")))
        hitApi(getIntent().getStringExtra("conference_id"));
    }

    private void initView() {
        activity = MyConferenceDetails2.this;

        title = findViewById(R.id.title);
        btnBack = findViewById(R.id.btnBack);
        recycleView = findViewById(R.id.recycleView);

        title.setText("Registered Person");

        databaseHelper = new DatabaseHelper(this);
        //adapter = new MyConferenceDetails2Adapter(activity, getTicketBookingList);

        recycleView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        //recycleView.setAdapter(adapter);

        findViewById(R.id.titleIcon).setVisibility(View.GONE);

    }

    private void hitApi(String conferenceType_Id) {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            params.put("conference_id", conferenceType_Id);
            header.put("Cynapse", params);
            Log.d("resBOb", header.toString());

            new GetbookedConferenceAPI(activity, header) {
                @Override
                public void responseApi(JSONObject response) {
                    super.responseApi(response);
                    JSONObject header = null;
                    try {
                        header = response.getJSONObject("Cynapse");
                        String res_msg = header.getString("res_msg");
                        String res_code = header.getString("res_code");
                        Log.d("RESPONSENOT", response.toString());

                        if (res_code.equals("1")) {

                            for (int i = 0; i < header.optJSONArray("ticket_booking").length(); i++) {
                                JSONObject jsonObject = header.optJSONArray("ticket_booking").optJSONObject(i);
                                JSONArray jsonArray = jsonObject.optJSONArray("booking_detail");

                                MyConferenceDetailsModel model = new MyConferenceDetailsModel();

                                model.setBookingConferenceId(jsonObject.optString("booking_conference_id"));
                                model.setConferenceId(jsonObject.optString("conference_id"));
                                model.setName(jsonObject.optString("name"));
                                model.setNoOfSeats(jsonObject.optString("no_of_seats"));
                                model.setOrderId(jsonObject.optString("order_id"));
                                model.setPaymentStatus(jsonObject.optString("payment_status"));
                                model.setStatus(jsonObject.optString("status"));
                                model.setTotalAmount(jsonObject.optString("total_amount"));

                                if (!databaseHelper.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_TICKET_BOOKING, DatabaseHelper.booking_conference_id, model.getBookingConferenceId())) {
                                    databaseHelper.ADD_TICKET_BOOKING(model, true);

                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        bookingDetailModel = new BookingDetail();
                                        bookingDetailModel.setBookingConferenceId(model.getBookingConferenceId());
                                        bookingDetailModel.setEmail(jsonArray.optJSONObject(j).optString("email"));
                                        bookingDetailModel.setUsername(jsonArray.optJSONObject(j).optString("username"));
                                        bookingDetailModel.setPhoneNo(jsonArray.optJSONObject(j).optString("phone_no"));
                                        databaseHelper.ADD_BOOKING_DETAILS(bookingDetailModel, true);
                                    }

                                } else {
                                    databaseHelper.ADD_TICKET_BOOKING(model, false);

                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        bookingDetailModel = new BookingDetail();
                                        bookingDetailModel.setBookingConferenceId(model.getBookingConferenceId());
                                        bookingDetailModel.setEmail(jsonArray.optJSONObject(j).optString("email"));
                                        bookingDetailModel.setUsername(jsonArray.optJSONObject(j).optString("username"));
                                        bookingDetailModel.setPhoneNo(jsonArray.optJSONObject(j).optString("phone_no"));
                                        databaseHelper.ADD_BOOKING_DETAILS(bookingDetailModel, false);
                                    }
                                }
                            }

                            getTicketBookingList = databaseHelper.getTicketBookingList(DatabaseHelper.TABLE_TICKET_BOOKING);

                            adapter = new MyConferenceDetails2Adapter(activity, getTicketBookingList,bookingDetailList);
                            recycleView.setAdapter(adapter);

                        } else {
                            MyToast.toastLong(activity, res_msg);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setOnClickListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private ArrayList<MyConferenceDetailsModel> getTicketBookingList = new ArrayList<>();
    private ArrayList<BookingDetail> bookingDetailList = new ArrayList<>();
    private BookingDetail bookingDetailModel;


}
