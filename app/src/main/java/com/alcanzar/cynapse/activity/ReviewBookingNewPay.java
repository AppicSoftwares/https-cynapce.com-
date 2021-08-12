package com.alcanzar.cynapse.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.ConferenceBookApi;
import com.alcanzar.cynapse.api.PostPaymentApi;
import com.alcanzar.cynapse.api.PromoCodeReviewNotiApi;
import com.alcanzar.cynapse.model.TicketsPackageModal;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.ServiceUtility;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.alcanzar.cynapse.activity.ReviewBookingNew.reviewBookingNew;
import static com.alcanzar.cynapse.fragments.BookTicketFragmentNew.allUsersList;
import static java.lang.Integer.parseInt;

public class ReviewBookingNewPay extends AppCompatActivity {

    private ImageView titleIcon;
    private TextView title, tot_packag, confrenceNameTv, timeTv, addressTv, applyTv, totalTv, gstTv, couponDiscountTv, grandTotalTv, uploadresume, subTotalTv, priceHikePerTv, hikeAmtTv;
    private RecyclerView recycle_reviewbooking;
    private ImageView arrowindicator;
    private EditText promocode;

    private ArrayList<TicketsPackageModal> reviewbookingdetails;
    private LinearLayout payLl, priceHikeContainerLl;
    private String promocode_id = "";

    private String price_hike_after_percentage = "0.0";
    private String price_hike_after_date = "";
    private String from_date = "", to_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_booking_new_pay);
        initialize();
    }

    public void initialize() {

        reviewbookingdetails = new ArrayList<>();

        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        title = findViewById(R.id.title);
        tot_packag = findViewById(R.id.tot_packag);
        confrenceNameTv = findViewById(R.id.confrenceNameTv);
        addressTv = findViewById(R.id.addressTv);
        timeTv = findViewById(R.id.timeTv);

        applyTv = findViewById(R.id.applyTv);
        totalTv = findViewById(R.id.totalTv);
        gstTv = findViewById(R.id.gstTv);
        couponDiscountTv = findViewById(R.id.couponDiscountTv);
        uploadresume = findViewById(R.id.uploadresume);
        promocode = findViewById(R.id.promocode);
        grandTotalTv = findViewById(R.id.grandTotalTv);

        subTotalTv = findViewById(R.id.subTotalTv);
        hikeAmtTv = findViewById(R.id.hikeAmtTv);
        priceHikePerTv = findViewById(R.id.priceHikePerTv);
        recycle_reviewbooking = findViewById(R.id.recycle_reviewbooking);
        priceHikeContainerLl = findViewById(R.id.priceHikeContainerLl);

        payLl = findViewById(R.id.payLl);

        title.setText("Review Booking");

        confrenceNameTv.setText(getIntent().getStringExtra("conference_name"));
        addressTv.setText(getIntent().getStringExtra("address"));
        timeTv.setText(getIntent().getStringExtra("from_time"));
        price_hike_after_percentage = getIntent().getStringExtra("price_hike_after_percentage");
        price_hike_after_date = getIntent().getStringExtra("price_hike_after_date");
        from_date = getIntent().getStringExtra("from_date");
        to_date = getIntent().getStringExtra("to_date");

        if (getIntent().hasExtra("gst")) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("gst")))
                gst = Float.parseFloat(getIntent().getStringExtra("gst"));
        }

        if (getIntent().hasExtra("conference_id")) {
            conference_id = getIntent().getStringExtra("conference_id").trim();
        }

        priceHikePerTv.setText("*You are booking this ticket after date " + price_hike_after_date + "\n" + "So you will be charged " + price_hike_after_percentage + "%" + " extra");

//        arrowindicator = findViewById(R.id.imgarrow);
//        arrowindicator.setVisibility(View.VISIBLE);

        recycle_reviewbooking.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycle_reviewbooking.setHasFixedSize(true);
        recycle_reviewbooking.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycle_reviewbooking.getContext(), DividerItemDecoration.HORIZONTAL);
        recycle_reviewbooking.addItemDecoration(dividerItemDecoration);
        recycle_reviewbooking.setItemAnimator(new DefaultItemAnimator());

        tot_packag.setText("" + allUsersList.size() + " Tickets");

        for (int i = 0; i < allUsersList.size(); i++) {
            try {
                TicketsPackageModal ticketsPackageModal = new TicketsPackageModal();
                ticketsPackageModal.setTicketname(allUsersList.get(i).optString("name"));
                ticketsPackageModal.setTicketprice(allUsersList.get(i).optString("totalPrice"));
                totaPrice = totaPrice + Float.parseFloat(allUsersList.get(i).optString("totalPrice"));
                reviewbookingdetails.add(ticketsPackageModal);
            } catch (Exception e) {
                ReviewBookingNewPay.this.finish();
                //MyToast.toastLong(ReviewBookingNewPay.this, "Invalid Price");
            }
        }

        totalTv.setText("" + totaPrice);
        gstTv.setText("" + gst);

        if (!checkDate() && TextUtils.isEmpty(price_hike_after_percentage)) {
            priceHikeContainerLl.setVisibility(View.GONE);
            grandTotalTv.setText(Util.form.format(calcGrandAmt()));
            uploadresume.setText(Util.form.format(calcGrandAmt()));
        } else {
            subTotalTv.setText(Util.form.format(calcGrandAmt()));
            hikeAmtTv.setText(Util.form.format(calculateHikedAmount()));
            grandTotalTv.setText(Util.form.format(calcGrandAmt() + calculateHikedAmount()));
            uploadresume.setText(Util.form.format(calcGrandAmt() + calculateHikedAmount()));
        }

        Log.d("allUsersList", allUsersList.toString());

        TicketsPackageAdapter1 reviewbookingadapter = new TicketsPackageAdapter1(getApplicationContext(), reviewbookingdetails, true);
        recycle_reviewbooking.setAdapter(reviewbookingadapter);

        payLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payLl.setClickable(false);
                try
                {
                    if (uploadresume.getText().toString().contains("-")) {
                        MyToast.toastLong(ReviewBookingNewPay.this, "Payable Amount should not be Negative");
                        payLl.setClickable(true);
                    } else if (isNegativeAmount) {
                        MyToast.toastLong(ReviewBookingNewPay.this, "Payable Amount should not be Negative");
                        payLl.setClickable(true);
                    } else if (uploadresume.getText().toString().equals("0.0") || uploadresume.getText().toString().equals("0")) {
                        //hit payment SuccessAPI;
                        PostPaymentTicketApi("1", "4");
                    } else {
                        Intent it = new Intent(ReviewBookingNewPay.this, WebViewActivity.class);
                        it.putExtra("totalAmount", uploadresume.getText().toString());
                        it.putExtra("order_id", "" + randomNum);
                        it.putExtra("uuid", AppCustomPreferenceClass.readString(ReviewBookingNewPay.this, AppCustomPreferenceClass.UserId, ""));
                        it.putExtra("conference_id", conference_id);
                        it.putExtra("AllUserList", allUsersList.toString());
                        it.putExtra("noOFSeats", "" + allUsersList.size());
                        it.putExtra("promocode_id", promocode_id);
                        it.putExtra("date_to_notify_user", "");
                        it.putExtra("ReviewBookingNewPay", true);
                        startActivity(it);

                        if (reviewBookingNew != null)
                            reviewBookingNew.finish();

                        finish();

                        //ActivityCompat.finishAffinity(ReviewBookingNewPay.this);
                    }
                } catch (Exception e) {
                    payLl.setClickable(true);
                }
            }
        });

        applyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (promocode.getText().toString().length() == 0) {
                    MyToast.toastShort(ReviewBookingNewPay.this, "Please Enter Promocode first!");
                    return;
                }
                if (Float.parseFloat(grandTotalTv.getText().toString()) < 0) {
                    MyToast.toastLong(ReviewBookingNewPay.this, "Payble Amount should not be Negative");
                    return;
                }
                if (isNegativeAmount) {
                    MyToast.toastLong(ReviewBookingNewPay.this, "Payble Amount should not be Negative");
                    return;
                }

                try {
                    GetPromocodeApi(promocode.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                couponDiscountTv.setText(""+couponDiscount);
//                uploadresume.setText("Pay "+calcGrandAmt());
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void conferenceBookSeatApi(String conference_id, String seat, String total_amount) throws JSONException {
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
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("seats", seat);
        params.put("amount", total_amount);
        //params.put("amount", txt_subtotal.getText().toString());

        Log.d("POSTSEATFRAAG", params + "");
        header.put("Cynapse", params);
        new ConferenceBookApi(this, header,true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    Log.d("RESPONSEBOOK", response.toString());
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        MyToast.toastLong(ReviewBookingNewPay.this, res_msg);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ReviewBookingNewPay.this, MainActivity.class);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(ReviewBookingNewPay.this);
                            }
                        }, 2000);
                        //  startActivity(new Intent(WebViewActivity.this,MyConferencesActivity.class));
                        //finish();
                    } else {
                        MyToast.toastLong(ReviewBookingNewPay.this, res_msg);
                        // finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    payLl.setClickable(true);
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
                payLl.setClickable(true);
            }
        };
    }

    private boolean checkDate() {

        boolean b = false;

//        from_date = "23-6-19";
//        price_hike_after_date = "23-6-19";

        try {
            if (TextUtils.isEmpty(from_date) && TextUtils.isEmpty(price_hike_after_date)) {
                b = false;
            } else {
                String parts1[] = from_date.split("-");
                String parts2[] = price_hike_after_date.split("-");

                int endDay = parseInt(parts2[0]);
                int endMonth = parseInt(parts2[1]);
                int endYear = parseInt(parts2[2]);

                int startDay = parseInt(parts1[0]);
                int startMonth = parseInt(parts1[1]);
                int startYear = parseInt(parts1[2]);

                if (endDay <= startDay) {
                    b = true;
                } else {
                    b = false;
                }
            }
        } catch (Exception e) {
            b = false;
        }

        return b;
    }

    private Float calculateHikedAmount() {
        Float hikedAmt = calcGrandAmt() * Float.parseFloat(price_hike_after_percentage) / 100;
        return hikedAmt;
    }

    private Float calcGrandAmt() {

        Float calcGrantAmt = 0.0f;

        Float amtPer = (totaPrice * gst) / 100;
        Float totPerAmt = totaPrice + amtPer;

        if (String.valueOf(amtPer).contains("-")) {
            isNegativeAmount = true;
        } else if (totPerAmt < couponDiscount) {
            isNegativeAmount = true;
        } else {
            calcGrantAmt = totPerAmt - couponDiscount;
        }
        return calcGrantAmt;
    }

    public class TicketsPackageAdapter1 extends RecyclerView.Adapter<TicketsPackageAdapter1.MyViewHolder> {

        public Context context;
        private ArrayList<TicketsPackageModal> packageList;


        boolean flag;

        public TicketsPackageAdapter1(Context context, ArrayList<TicketsPackageModal> packageList, boolean flag) {

            this.context = context;
            this.packageList = packageList;
            this.flag = flag;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tickects_packages_items, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.ticketsname.setText(packageList.get(position).getTicketname());
            holder.ticketsprice.setText("" + Float.parseFloat(packageList.get(position).getTicketprice()));


            //  holder.ticketsprice.setTextColor(R.color.grey_color);

//            if (flag) {
//                holder.cardticketpackageitems.setBackgroundColor(Color.parseColor("#FFF0EDED"));
//                holder.imgarrow.setVisibility(View.VISIBLE);
////            holder.ticketsprice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                holder.priceindicator.setVisibility(View.GONE);
//            }
//

            holder.imgarrow.setVisibility(View.VISIBLE);

        }

        @Override
        public int getItemCount() {
            return packageList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView ticketsname, ticketsprice;
            CardView cardticketpackageitems;
            ImageView imgarrow;
            ImageView priceindicator;

            public MyViewHolder(View view) {
                super(view);
                ticketsname = view.findViewById(R.id.tickectspackagename);
                ticketsprice = view.findViewById(R.id.tickectspackageprice);
                cardticketpackageitems = view.findViewById(R.id.cardticketpackageitems);
                imgarrow = view.findViewById(R.id.imgarrow);
                priceindicator = view.findViewById(R.id.priceindicator);

            }
        }
    }

    public void GetPromocodeApi(String promcode) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ReviewBookingNewPay.this, AppCustomPreferenceClass.UserId, ""));
        params.put("promo_code", promcode);
        params.put("conference_id", conference_id);

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new PromoCodeReviewNotiApi(ReviewBookingNewPay.this, header, true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    Log.d("Promocodenew", response.toString());

                    if (res_code.equals("1")) {
                        Float promoCodePercnt = Float.parseFloat(header.getJSONObject("promocode").optString("percentage"));
                        promocode_id = header.getJSONObject("promocode").optString("promocode_id");
                        Float grandTotal = Float.parseFloat(grandTotalTv.getText().toString());
                        Float couponDiscount = Float.parseFloat(couponDiscountTv.getText().toString());
                        if (grandTotal < couponDiscount) {
                            MyToast.toastLong(ReviewBookingNewPay.this, "Invalid Amount");
                        } else {
                            Float totAmt = grandTotal * promoCodePercnt / 100;
                            couponDiscountTv.setText(Util.form.format(totAmt));
                            Float finalAmt = grandTotal - totAmt;
                            uploadresume.setText(Util.form.format(finalAmt));
                        }
                    } else {
                        MyToast.toastLong(ReviewBookingNewPay.this, res_msg);
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

    private void PostPaymentTicketApi(String type_id, String paymentStatus) throws JSONException {

        JSONArray array = new JSONArray();

        for (int i = 0; i < allUsersList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", allUsersList.get(i).optString("name"));
            jsonObject.put("email", allUsersList.get(i).optString("email"));
            jsonObject.put("phone_no", allUsersList.get(i).optString("phoneNO"));
            array.put(jsonObject);
        }

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ReviewBookingNewPay.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("no_of_seats", allUsersList.size());
        params.put("total_amount", "0.00");
        params.put("order_id", randomNum);
        params.put("payment_status", paymentStatus);
        params.put("type_id", type_id);
        params.put("user_details", array);
        params.put("promocode_id", promocode_id);
        params.put("date_to_notify_user", "");
        header.put("Cynapse", params);
        Log.d("headerBOB", header + "");

        new PostPaymentApi(this, header,true) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("JOBSPONSE", response.toString());

                    if (res_code.equals("1")) {
                        MyToast.toastLong(ReviewBookingNewPay.this, res_msg);
                        conferenceBookSeatApi(conference_id, String.valueOf(allUsersList.size()), "0.00");
                    } else {
                        MyToast.toastLong(ReviewBookingNewPay.this, res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    payLl.setClickable(true);
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
                payLl.setClickable(true);
            }
        };
    }

    private Float totaPrice = 0.0f;
    private Float gst = 0.0f;
    private Float couponDiscount = 0.0f;
    private String conference_id = "";
    private static boolean isNegativeAmount = false;
    private Integer randomNum = ServiceUtility.randInt(0, 9999999);

}
