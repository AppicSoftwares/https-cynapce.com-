package com.alcanzar.cynapse.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MyConferencesActivity;
import com.alcanzar.cynapse.activity.WebViewActivity;
import com.alcanzar.cynapse.adapter.BookTicketPriceAdapter;
import com.alcanzar.cynapse.api.ConferenceBookApi;
import com.alcanzar.cynapse.api.PostPaymentApi;
import com.alcanzar.cynapse.api.PromoCodeReviewNotiApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.BookTicketModal;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.RecyclerTouchListener;
import com.alcanzar.cynapse.utils.ServiceUtility;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class BookTicketFragment extends Fragment implements View.OnClickListener {
    LinearLayout linearLayout1, linearLayout2, lnr_41, lnr_consession, lnr_acc_chr, lnr_afer_promoprice,lnr_afer_promoprice_1;
    Integer randomNum = ServiceUtility.randInt(0, 9999999);
    EditText txt_promocode;
    TextView txt_apply, txt_taxamnt, txt_total_amount;
    String prom_price = "", promocode_name = "", promocode_id = "", days = "", order_id = "";
    boolean checkPromo =false;
    boolean checkAll=false;
    //    String change = "";
    View itemView;
    double total = 0.0,no_of_seat_db =0.0,final_tot = 0.0;
    double per=0.0;
    TextView txt_amnt_conces, txt_aft_concess, txt_amnt_rupe, txt_no_seat,charg_extra,textTotalPriceFinal_1;
    RecyclerView recyclerViewPrice;
    ArrayList<BookTicketModal> modalArrayList;
    DatabaseHelper helper;
    RadioButton radioAny, checkYesOrNoRadio;
    TextView textTotalPrice1, textPerPrice, textTotalPriceFinal, accommodationPrice, adapterTotalPrice, txt_available_date, dateTextView;
    RadioGroup radioGroupData;
    ArrayList<ConferencePackageModel> conferencePackageList = new ArrayList<>();
    Button btnPay;
    String conference_id = "", total_days_price = "", accomdation = "",
            price_hike_after_date = "",
            price_hike_after_percentage = "", seat = "", medical_profile_id = "", member_concessions = "", student_concessions = "", payment_mode = "";
    boolean savebool = false;
    ArrayList aList;
    float tp = 0f, pp = 0f, no_of_seat = 0f, amu = 0f;
    int y=0;

    BookTicketPriceAdapter bookTicketPriceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_book_ticket, container, false);
        helper = new DatabaseHelper(getActivity());
//        AppConstantClass.prom_price_1="";
        helper.deleteTableName(DatabaseHelper.BOOK_TICKET_TABLE);
        initialise();
        return itemView;
    }

    private void initialise() {
        recyclerViewPrice = itemView.findViewById(R.id.recyclerViewPrice);
        radioAny = itemView.findViewById(R.id.radioAny);
        btnPay = itemView.findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
        lnr_afer_promoprice_1=itemView.findViewById(R.id.lnr_afer_promoprice_1);
        charg_extra=itemView.findViewById(R.id.charg_extra);
        lnr_consession = itemView.findViewById(R.id.lnr_consession);
        txt_amnt_conces = itemView.findViewById(R.id.txt_amnt_conces);
        txt_aft_concess = itemView.findViewById(R.id.txt_aft_concess);
        txt_amnt_rupe = itemView.findViewById(R.id.txt_amnt_rupe);
        lnr_acc_chr = itemView.findViewById(R.id.lnr_acc_chr);
        txt_no_seat = itemView.findViewById(R.id.txt_no_seat);
        txt_taxamnt = itemView.findViewById(R.id.txt_taxamnt);
        txt_total_amount = itemView.findViewById(R.id.txt_total_amount);
        textTotalPrice1 = itemView.findViewById(R.id.textTotalPrice1);
        textPerPrice = itemView.findViewById(R.id.textPerPrice);
        textTotalPriceFinal = itemView.findViewById(R.id.textTotalPriceFinal);
        textTotalPriceFinal_1=itemView.findViewById(R.id.textTotalPriceFinal_1);
        radioGroupData = itemView.findViewById(R.id.radioGroupData);
        accommodationPrice = itemView.findViewById(R.id.accommodationPrice);
        adapterTotalPrice = itemView.findViewById(R.id.adapterTotalPrice);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        linearLayout1 = itemView.findViewById(R.id.dateLayout);
        linearLayout2 = itemView.findViewById(R.id.chargeper);
        lnr_41 = itemView.findViewById(R.id.lnr_41);
        lnr_41.setVisibility(View.GONE);
        lnr_afer_promoprice = itemView.findViewById(R.id.lnr_afer_promoprice);
        lnr_afer_promoprice_1.setVisibility(View.GONE);
        // lnr_afer_promoprice.setVisibility(View.GONE);
        txt_apply = itemView.findViewById(R.id.txt_apply);
        txt_promocode = itemView.findViewById(R.id.txt_promocode);
        txt_apply.setOnClickListener(this);
        if (getArguments() != null) {

            conference_id = getArguments().getString("conference_id");
            total_days_price = getArguments().getString("total_days_price");
            accomdation = getArguments().getString("accomdation");
            seat = getArguments().getString("seat");
            price_hike_after_date = getArguments().getString("price_hike_after_date");
            price_hike_after_percentage = getArguments().getString("price_hike_after_percentage");
            savebool = getArguments().getBoolean("savebool");
            medical_profile_id = getArguments().getString("medical_profile_id");
            member_concessions = getArguments().getString("member_concessions");
            student_concessions = getArguments().getString("student_concessions");
            payment_mode = getArguments().getString("payment_mode");
        }
        // Log.d("PAMENTMODE",payment_mode);

        no_of_seat = Float.parseFloat(seat);
        no_of_seat_db = Double.parseDouble(seat);

        txt_no_seat.setText(seat);
        textTotalPrice1.setText(total_days_price);

        textPerPrice.setText(price_hike_after_percentage);
        if (accomdation.equalsIgnoreCase("")) {
            lnr_acc_chr.setVisibility(View.GONE);
            accommodationPrice.setText(accomdation);
        } else {
            lnr_acc_chr.setVisibility(View.VISIBLE);
            accommodationPrice.setText(accomdation);
        }

        dateTextView.setText(price_hike_after_date);
        AppConstantClass.dataTest = price_hike_after_date;
        btnPay.setText("PAY");

        aList = new ArrayList(Arrays.asList(medical_profile_id.split(",")));


        Log.e("dfasdvvvvv", getArguments().getBoolean("savebool") + ","
                + getArguments().getString("total_days_price") + ","
                + getArguments().getString("member_concessions") + ","
                + getArguments().getString("seat") + ","
                + getArguments().getString("student_concessions") + ","
                + getArguments().getString("medical_profile_id"));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        AppConstantClass.currentDate1 = format1.format(date);

        Log.e("sncjvkndv", AppConstantClass.currentDate1 + "");

        Date inActiveDate = null;
        try {
            inActiveDate = format1.parse(AppConstantClass.currentDate1);
            Log.e("sncjvkndv", inActiveDate + "");
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        if (savebool) {
            conferencePackageList = helper.getConfPackCharge(DatabaseHelper.TABLE_SAVE_CONFERENCES_PACK_CHARGE, conference_id);

        } else {
            conferencePackageList = helper.getConfPackCharge(DatabaseHelper.TABLE_CONFERENCES_PACK_CHARGE, conference_id);

        }


        for (int i = 0; i < conferencePackageList.size(); i++) {

            Log.d("daysskdjkkdkdkkd111", conferencePackageList.get(i).getConference_pack_day());
            Log.d("daysskdjkkdkdkkd222", conferencePackageList.get(i).getConference_pack_charge());
        }


        modalArrayList = new ArrayList<>();

        modalArrayList.clear();
        modalArrayList = helper.getBookTicketData(DatabaseHelper.BOOK_TICKET_TABLE);
        AppConstantClass.notyListModal.clear();
        AppConstantClass.notyListModal.addAll(conferencePackageList);
        bookTicketPriceAdapter = new BookTicketPriceAdapter(getActivity(), conferencePackageList, itemView, aList, member_concessions, student_concessions, no_of_seat);
        recyclerViewPrice.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPrice.setHasFixedSize(true);
        recyclerViewPrice.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPrice.setAdapter(bookTicketPriceAdapter);
        recyclerViewPrice.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewPrice, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d("dayofclickckkc", conferencePackageList.get(position).getConference_pack_day());

                days = conferencePackageList.get(position).getConference_pack_day();
//                change = "0";
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        //    radioAny.setOnClickListener(this);
        //  }
        radioAny.setOnClickListener(this);

        radioGroupData.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroupData.getCheckedRadioButtonId();
//                checkYesOrNoRadio = itemView.findViewById(selectedId);
                Log.e("selecterIdRadio", selectedId + "");
                lnr_41.setVisibility(View.GONE);

                boolean butCheck=true;
                for (int i = 0; i < conferencePackageList.size(); i++) {
                    String c = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.checkSelectStateAdapter + i, "0");
                    if (Integer.parseInt(c) == 1) {
                        butCheck=false;

                        Log.e("asdf6u7567",butCheck+"");

                        break;
                    }
                }
//                if(radioAny.isChecked()){

                try {
//                    if (R.id.rButton1 == selectedId) {

                    alldayDataCheck(butCheck);
//                        float a = Float.parseFloat(textTotalPriceFinal.getText() + "");
//                        float b = Float.parseFloat(accommodationPrice.getText() + "");
//                        a = a + b;
//                        float c = Float.parseFloat(charg_extra.getText() + "");
//                        float d= b+c;
//                        charg_extra.setText(d+"");
//                        textTotalPriceFinal.setText(a + "");
//                        textTotalPriceFinal_1.setText(a + "");
//                        tp = Float.parseFloat(textTotalPriceFinal_1.getText().toString());
//                        if (!prom_price.equals("")) {
//
////                            tp = Float.parseFloat(textTotalPriceFinal.getText().toString());
//                            pp = Float.parseFloat(prom_price);
//                            if ((tp > pp)) {
//
//                                lnr_41.setVisibility(View.VISIBLE);
//
//                                tp = tp - pp;
//                                txt_total_amount.setText((tp * no_of_seat) + "");
////                                txt_total_amount.setText(tp + "");
//                            } else {
//                                Toast.makeText(getActivity(), "Not Apply !promo price greater then total price", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        txt_total_amount.setText((tp * no_of_seat) + "");


                    Log.d("TIKTKTKTKTK11", textTotalPriceFinal.getText().toString());

//                    } else {
//                        float a = Float.parseFloat(textTotalPriceFinal.getText() + "");
//                        float b = Float.parseFloat(accommodationPrice.getText() + "");
//
//                        float c = Float.parseFloat(charg_extra.getText() + "");
//                        float d= c-b;
//                        charg_extra.setText(d+"");
//
//                        a = a - b;
//                        textTotalPriceFinal.setText(a + "");
//                        textTotalPriceFinal_1.setText(a + "");
//                        tp = Float.parseFloat(textTotalPriceFinal_1.getText().toString());

//                        if (!prom_price.equals("")) {
//
//
//                            pp = Float.parseFloat(prom_price);
//                            if ((tp > pp)) {
//
//                                lnr_41.setVisibility(View.VISIBLE);
//
//                                tp = tp - pp;
//                                txt_total_amount.setText((tp * no_of_seat) + "");
////                                txt_total_amount.setText(tp + "");
//                            } else {
//                                Toast.makeText(getActivity(), "Not Apply !promo price greater then total price", Toast.LENGTH_SHORT).show();
//                            }
                    //   }
//                        Log.d("TIKTKTKTKTK22", textTotalPriceFinal.getText().toString());
//                        txt_total_amount.setText((tp * no_of_seat) + "");
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.txt_apply:
                if (!TextUtils.isEmpty(txt_promocode.getText().toString())) {


                    try {
                        int cou = 0;
                        for (int i = 0; i < conferencePackageList.size(); i++) {
                            String c = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.checkSelectStateAdapter + i, "0");
                            if (Integer.parseInt(c) == 1) {

                                cou++;
                                break;
                            }
                        }
                        if (cou != 0 || radioAny.isChecked()) {
                            try {
                                GetPromocodeApi(txt_promocode.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Please select package to precede! ", Toast.LENGTH_SHORT).show();
                        }
                        //checkDataSelectedForPromo();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "Please enter promocode", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.radioAny:
                txt_promocode.setText("");
                lnr_41.setVisibility(View.GONE);
                txt_taxamnt.setText("");
                AppConstantClass.change = "1";

                alldayDataCheck(true);
                break;

            case R.id.btnPay:
                try {
                    int cou = 0;
                    for (int i = 0; i < conferencePackageList.size(); i++) {
                        String c = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.checkSelectStateAdapter + i, "0");
                        if (Integer.parseInt(c) == 1) {
                            cou++;
                            break;
                        }
                    }
                    if (cou != 0 || radioAny.isChecked()) {
//                        conferenceBookSeatApi();
                        if (payment_mode.equals("2")) {

                            MyToast.toastLong(getActivity(), "Payment will be collected at venue");
                            getActivity().finish();
                        } else {
                            //PostPaymentTicketApi();

                            if (txt_total_amount.getText().toString().equalsIgnoreCase("0") || txt_total_amount.getText().toString().equalsIgnoreCase("0.0")) {
                                PostPaymentTicketApi("1");
                                conferenceBookSeatApi();

                            } else {


                                Intent it = new Intent(getActivity(), WebViewActivity.class);
                                it.putExtra("totalAmount", txt_total_amount.getText().toString());
                                it.putExtra("seat", seat);
                                it.putExtra("conference_id", conference_id);
                                it.putExtra("book_ticket", true);
                                it.putExtra("order_id", randomNum.toString());
                                it.putExtra("extra_charges", textPerPrice.getText().toString());
                                it.putExtra("accomadation_charges", accommodationPrice.getText().toString());
                                if (AppConstantClass.change.equals("0")) {
                                    it.putExtra("days", days);
                                } else {
                                    it.putExtra("days", "0");
                                }

                                startActivity(it);
                                getActivity().finish();
                            }

                        }

                    } else {
                        Toast.makeText(getActivity(), "Please select package to precede! ", Toast.LENGTH_SHORT).show();
                    }
//
//
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

//    private void checkDataSelectedForPromo() {
//
//        int cou = 0;
//        for (int i = 0; i < conferencePackageList.size(); i++) {
//            String c = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.checkSelectStateAdapter + i, "0");
//            if (Integer.parseInt(c) == 1) {
//                cou++;
//                break;
//            }
//        }
//        try {
//
//            if (cou == 0) {
//
//                if (radioAny.isChecked()) {
//                    Log.d("PROMOVO",txt_promocode.getText().toString());
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                GetPromocodeApi(txt_promocode.getText().toString());
//                            }catch (Exception e)
//                            {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }, 300);
//
//                    Log.d("PROMOPPRICE111", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.Price,""));
//
//                } else {
//                    Toast.makeText(getActivity(), "at Least one day select!", Toast.LENGTH_SHORT).show();
//                }
//
//            } else {
//                Log.d("PROMOPPRICE222", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.Price,""));
//
//                GetPromocodeApi(txt_promocode.getText().toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        int selectedId = radioGroupData.getCheckedRadioButtonId();
//        if (selectedId == R.id.rButton1) {
//            float t = Float.parseFloat(textTotalPriceFinal.getText() + "");
//            float a = Float.parseFloat(accommodationPrice.getText() + "");
//            t = t + a;
//            textTotalPriceFinal.setText(t + "");
//            textTotalPriceFinal_1.setText(t + "");
//            if (!prom_price.equals("")) {
//                tp = Float.parseFloat(textTotalPriceFinal.getText().toString());
//                pp = Float.parseFloat(prom_price);
//                if ((tp > pp)) {
//
//                    lnr_41.setVisibility(View.VISIBLE);
//
//                    tp = tp - pp;
////                                txt_total_amount.setText((tp * no_of_seat) + "");
//                    txt_total_amount.setText(tp + "");
//                } else {
//                    Toast.makeText(getActivity(), "Not Apply !promo price granthem total price", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//        } else {
//            textTotalPriceFinal.setText(textTotalPrice1.getText());
//            textTotalPriceFinal_1.setText(textTotalPrice1.getText());
////            tp = Float.parseFloat(textTotalPriceFinal.getText().toString());
////            pp = Float.parseFloat(prom_price);
//
//            if (!prom_price.equals("")) {
//                tp = Float.parseFloat(textTotalPriceFinal.getText().toString());
//                pp = Float.parseFloat(prom_price);
//                if ((tp > pp)) {
//
//                    lnr_41.setVisibility(View.VISIBLE);
//
//                    tp = tp - pp;
////                                txt_total_amount.setText((tp * no_of_seat) + "");
//                    txt_total_amount.setText(tp + "");
//                } else {
//                    Toast.makeText(getActivity(), "Not Apply !promo price granthem total price", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//    }


    private void alldayDataCheck(boolean checkBut) {
        lnr_afer_promoprice_1.setVisibility(View.GONE);

//        int cou = 0;
//        for (int i = 0; i < conferencePackageList.size(); i++) {
//            String c = AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.checkSelectStateAdapter + i, "0");
//            if (Integer.parseInt(c) == 1) {
//                cou++;
//                break;
//            }
//        }
        try {
//            if (cou == 0) {

            if(checkBut){
                bookTicketPriceAdapter.dataUpdate();
                radioAny.setChecked(true);
                checkConcession();
                adapterTotalPrice.setText("0");
                Log.e("Call_That", "dsfd");
//                    textTotalPriceFinal.setText(textTotalPrice1.getText());

            }else{
                Log.e("Call_That_Else", "dsfd");
                checkConcession();
//                    textTotalPriceFinal.setText(adapterTotalPrice.getText());
            }
            checkDateCondition();
            int selectedId = radioGroupData.getCheckedRadioButtonId();
            if (selectedId == R.id.rButton1) {

                float a = Float.parseFloat(textTotalPriceFinal.getText() + "");
                float b = Float.parseFloat(accommodationPrice.getText() + "");
                a = a + b;
                textTotalPriceFinal.setText(Util.decimalNumberRound(a));
                textTotalPriceFinal_1.setText(Util.decimalNumberRound(a));
//                    tp = Float.parseFloat(textTotalPriceFinal.getText().toString());
//                    if (!prom_price.equals("")) {
//
////                            tp = Float.parseFloat(textTotalPriceFinal.getText().toString());
//                        pp = Float.parseFloat(prom_price);
//                        if ((tp > pp)) {
//
//                            lnr_41.setVisibility(View.VISIBLE);
//
//                            tp = tp - pp;
////                                txt_total_amount.setText((tp * no_of_seat) + "");
//                            txt_total_amount.setText(tp + "");
//                        } else {
//                            Toast.makeText(getActivity(), "Not Apply !promo price greater then total price", Toast.LENGTH_SHORT).show();
//                        }
//                    }

                a=a * no_of_seat;
                txt_total_amount.setText(Util.decimalNumberRound(a ));
                Log.e("kjgasdas",a+","+no_of_seat);
//                    textTotalPriceFinal.setText((Float.parseFloat(textTotalPrice1.getText() + "") + Float.parseFloat(accommodationPrice.getText() + "")) + "");
            } else {
//                    textTotalPriceFinal.setText(textTotalPrice1.getText());
//                    textTotalPriceFinal_1.setText(textTotalPrice1.getText());
                tp = Float.parseFloat(textTotalPriceFinal.getText().toString().trim());
                Log.e("kjgasdas_Asd",tp+"");
                txt_total_amount.setText(Util.decimalNumberRound((tp * no_of_seat)));

//                    if (!prom_price.equals("")) {
//
////                            tp = Float.parseFloat(textTotalPriceFinal.getText().toString());
//                        pp = Float.parseFloat(prom_price);
//                        if ((tp > pp)) {
//
//                            lnr_41.setVisibility(View.VISIBLE);
//
//                            tp = tp - pp;
////                                txt_total_amount.setText((tp * no_of_seat) + "");
//                            txt_total_amount.setText(tp + "");
//                        } else {
//                            Toast.makeText(getActivity(), "Not Apply !promo price greater then total price", Toast.LENGTH_SHORT).show();
//                        }
//                    }
            }
            // Log.d("totalfinalamount",textTotalPriceFinal.getText().toString());
            // txt_total_amount.setText("Rs."+" "+String.valueOf(no_of_seat*Float.parseFloat(textTotalPriceFinal.getText().toString())));





//            } else {
//                Toast.makeText(getActivity(), "Already Selected Days", Toast.LENGTH_SHORT).show();
//                radioAny.setChecked(false);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkConcession(){
        float amu1=0f;
        if(radioAny.isChecked()) {
            amu1 = Float.parseFloat(textTotalPrice1.getText() + "");
        }else{
            amu1 = Float.parseFloat(adapterTotalPrice.getText() + "");
        }

        if (aList.contains("4")) {
            Log.d("kdkdkdkdskdsk111", "iiii"+amu1);
            if(student_concessions.equals("")){
                student_concessions="0";
            }
            amu1 = amu1 - (Float.parseFloat(student_concessions) * amu1) / 100;
            lnr_consession.setVisibility(View.VISIBLE);
            txt_amnt_rupe.setVisibility(View.VISIBLE);
            txt_amnt_conces.setText("Amount after the student concession" + " " + student_concessions + "%" + " " + "is");
            txt_aft_concess.setText(amu1 + "");

        } else {
            Log.d("kdkdkdkdskdsk222", "iiii"+amu1);
            if(member_concessions.equals("")){
                member_concessions="0";
            }
            amu1 = amu1 - (Float.parseFloat(member_concessions) * amu1) / 100;
            lnr_consession.setVisibility(View.VISIBLE);
            txt_amnt_rupe.setVisibility(View.VISIBLE);
            txt_amnt_conces.setText("Amount after the member concession" + " " + member_concessions + "%" + " " + "is");
            txt_aft_concess.setText(amu1 + "");

        }

        textTotalPriceFinal.setText(amu1 + "");
        textTotalPriceFinal_1.setText(amu1 + "");
        txt_total_amount.setText(String.valueOf(no_of_seat * amu1));

    }

    public void checkDateCondition() {
        try {

            String[] c_date1 = AppConstantClass.currentDate1.split("-"),
                    c_date2 = AppConstantClass.dataTest.split("-");

            int x = Integer.parseInt(c_date1[0]) - Integer.parseInt(c_date2[0]);
            int y = Integer.parseInt(c_date1[1]) - Integer.parseInt(c_date2[1]);
            int z = Integer.parseInt(c_date1[2]) - Integer.parseInt(c_date2[2]);
            Log.e("jabdsjc", x + "," + y + "," + z + "=" + AppConstantClass.currentDate1 + "," + AppConstantClass.dataTest);
            if (x >= 0 && y >= 0 || z > 0) {

                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                setPerDataInAmount();

            } else {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void setPerDataInAmount() {
        try {
            float amu;
            float percent = Float.parseFloat(textPerPrice.getText() + "");
//            if (radioAny.isChecked()) {
            amu = Float.parseFloat(textTotalPriceFinal.getText() + "");
//            }else {
//                amu = Float.parseFloat(textTotalPriceFinal.getText() + "");
//            }


//                if (aList.contains("4")) {
//                    Log.d("kdkdkdkdskdsk111", "iiii");
//                    amu = amu - (Float.parseFloat(student_concessions) * amu) / 100;
//                    lnr_consession.setVisibility(View.VISIBLE);
//                    txt_amnt_rupe.setVisibility(View.VISIBLE);
//                    txt_amnt_conces.setText("Amount after the student concession" + " " + student_concessions + "%" + " " + "is");
//                    txt_aft_concess.setText(amu + "");
//
//                } else {
//                    Log.d("kdkdkdkdskdsk222", "iiii");
//                    amu = amu - (Float.parseFloat(member_concessions) * amu) / 100;
//                    lnr_consession.setVisibility(View.VISIBLE);
//                    txt_amnt_rupe.setVisibility(View.VISIBLE);
//                    txt_amnt_conces.setText("Amount after the member concession" + " " + member_concessions + "%" + " " + "is");
//                    txt_aft_concess.setText(amu + "");
//
//                }
            Log.e("Aefqefddddd", amu + "");
//                if (R.id.rButton1 == radioGroupData.getCheckedRadioButtonId()) {
//                    amu = amu + Float.parseFloat(accommodationPrice.getText() + "");
//                }
//                Log.d("amuldlld111", amu + "");
            float b = amu * percent / 100;
            amu = amu + b;
            charg_extra.setText(Util.decimalNumberRound(amu));
            textTotalPriceFinal.setText(Util.decimalNumberRound(amu));
            textTotalPriceFinal_1.setText(Util.decimalNumberRound(amu));
            Log.d("kdskdkdeee", amu + "");
            Log.d("kdskdkdffff", no_of_seat + "");
            txt_total_amount.setText(Util.decimalNumberRound((no_of_seat * amu)));
//                if(!AppConstantClass.prom_price_1.equals("")) {
//                    amu=amu-Float.parseFloat(AppConstantClass.prom_price_1);
//                }



        } catch (Exception e) {
            e.printStackTrace();
        }


//        else {
//
//            float amu = Float.parseFloat(adapterTotalPrice.getText() + "");
//            Log.e("Aefqef4466", amu + "");
//            Log.d("amuldlld555", (Float.parseFloat(student_concessions) * amu) / 100 + "");
//            if(aList.contains("4"))
//            {
//                amu = amu - (Float.parseFloat(student_concessions) * amu) / 100;

//
//            }else {
//                amu = amu - (Float.parseFloat(member_concessions) * amu) / 100;
//
//            }
//            Log.e("Aefqef44488", amu + "");
//            float b = (amu * percent) / 100;
//            amu = amu + b;
//            Log.e("Aefqef", amu + "");
//            if (R.id.rButton1 == radioGroupData.getCheckedRadioButtonId()) {
//                amu = amu + Float.parseFloat(accommodationPrice.getText() + "");
//            }
//            textTotalPriceFinal.setText(amu + "");
//        }
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
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("seats", seat);
        params.put("amount", txt_total_amount.getText().toString());
        //params.put("amount", txt_subtotal.getText().toString());

        Log.d("POSTSEATFRAAG", params + "");
        header.put("Cynapse", params);
        new ConferenceBookApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    Log.d("RESPONSEBOOKBEFORE", response.toString());
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        MyToast.toastLong(getActivity(), res_msg);
                        Intent intent = new Intent(getActivity(), MyConferencesActivity.class);
                        intent.putExtra("paymentsucess", "1");
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        MyToast.toastLong(getActivity(), res_msg);
                        getActivity().finish();
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


    public void GetPromocodeApi(final String promcode) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        params.put("promo_code", promcode);
        params.put("conference_id", conference_id);

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new PromoCodeReviewNotiApi(getActivity(), header,true) {
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
                        y=1;

                        //MyToast.toastShort(this,res_msg);
                        JSONObject jsonObject = header.getJSONObject("promocode");
                        prom_price = jsonObject.getString("price");
                        promocode_name = jsonObject.getString("promocode_name");
                        promocode_id = jsonObject.getString("promocode_id");
                        txt_taxamnt.setText(Util.decimalNumberRound(Float.parseFloat(prom_price))+" % ");
                        Log.d("TOTALCTCAOMT3333", prom_price + "");
                        // AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.Price , prom_price);

//                        AppConstantClass.prom_price_1=prom_price;
//                        txt_total_amount.setText("");
                        if(!checkPromo){
                            if (!AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.promo_code_conf, "").equals("") && AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.promo_code, "").equals(promcode))
                            {
                                checkPromo=true;
                                AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.promo_price_conf, prom_price);
                                AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.promo_code_conf, promcode);
//                        }
//                        else if (!AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.promo_code, "").equals("") && !AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.promo_code, "").equals(promcode))
//                        {
//                            checkPromo=false;
//                            Toast.makeText(getActivity(), "Only one promocode can be used at a time", Toast.LENGTH_SHORT).show();
                            }  else{
                                checkPromo=true;
                                AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.promo_price_conf, prom_price);
                                AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.promo_code_conf, promcode);
                            }
                        }else{
//                            checkPromo=false;
                            checkAll=true;
                            Toast.makeText(getActivity(), "Promocode already applied", Toast.LENGTH_SHORT).show();
                        }

                        lnr_afer_promoprice_1.setVisibility(View.VISIBLE);
                        //Log.d("KDKKDKDK999",Float.parseFloat(textTotalPriceFinal_1.getText().toString())+"");
                       // Log.d("KDKKDKD8888",Float.parseFloat(prom_price)+"");
                        try {
                           // tp = Float.parseFloat(textTotalPriceFinal_1.getText().toString());
                            total = Double.parseDouble(textTotalPriceFinal_1.getText().toString());
                           // pp = Float.parseFloat(prom_price);
                            if(checkPromo && ! checkAll) {
                                if (prom_price.equalsIgnoreCase("")) {

                                } else {
//                                    if (total > Double.parseDouble(prom_price)) {
                                    if (100 >= Double.parseDouble(prom_price)) {
                                        lnr_41.setVisibility(View.VISIBLE);
                                        per = (total * Double.parseDouble(prom_price)) / 100;
                                        total = total - per;
                                        Log.d("TOTALCTCAOM222", total + "");
                                        final_tot = total*no_of_seat_db;
                                        txt_total_amount.setText(String.valueOf(final_tot));
                                        textTotalPriceFinal_1.setText(String.valueOf(total));
                                        Log.d("AFTEROROCODE111", total + "");
                                    } else {
                                        //checkPromo = false;
                                        txt_total_amount.setText(String.valueOf(total));
                                        Toast.makeText(getActivity(), "Promocode value can not exceed total value", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
//                            if (tp>pp) {
////
//                                lnr_41.setVisibility(View.VISIBLE);
//
//                                tp = tp - pp;
////                                txt_total_amount.setText((tp * no_of_seat) + "");
//                                Log.d("KDKKDKD7777",no_of_seat+"");
////                                Log.d("KDKKDKD333444",tp*2+"");
//                                textTotalPriceFinal_1.setText(tp+"");
//
//                                tp=tp * no_of_seat;
//                                Log.d("KDKKDKD333444",tp+"");
////                                txt_total_amount.setText(tp  + "");
//                                txt_total_amount.setText(Util.decimalNumberRound(tp));
//                            } else {
//                                //Toast.makeText(getActivity(), "Not Apply !promo price greater then total price", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "Promocode value can not exceed total value", Toast.LENGTH_SHORT).show();
//                            }

                            txt_promocode.setText("");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {

//                        Log.d("TOTALCTCAOMT", totalctc + "");
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

    private void PostPaymentTicketApi(String x) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("amount", txt_total_amount.getText().toString());
        params.put("promocode",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.promo_code_conf,""));
        params.put("promocode_price",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.promo_price_conf,""));

        if (AppConstantClass.change.equals("0")) {
            params.put("days", days);
        } else {
            params.put("days", "0");
        }

        params.put("accomadation_charges", accommodationPrice.getText().toString());
        params.put("extra_charges", textPerPrice.getText().toString());
        params.put("order_id", randomNum.toString());
        params.put("type_id",x);
        //params.put("uuid", "S75f3");
        // params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.sync_time,""));
        //params.put("sync_time", "");
        header.put("Cynapse", params);
        new PostPaymentApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    //AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.sync_time,sync_time);
                    Log.d("JOBSPONSE", response.toString());

                    if (res_code.equals("1")) {
                        //MyToast.toastLong(getActivity(),res_msg);
                        order_id = header.getString("order_id");

//                        Intent it = new Intent(getActivity(), WebViewActivity.class);
//                        it.putExtra("totalAmount", textTotalPriceFinal.getText().toString());
//                        it.putExtra("seat", seat);
//                        it.putExtra("conference_id", conference_id);
//                        it.putExtra("book_ticket", true);
//                        it.putExtra("order_id", order_id);
//                        startActivity(it);
//                        Log.d("ORDEid", order_id);

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
}
