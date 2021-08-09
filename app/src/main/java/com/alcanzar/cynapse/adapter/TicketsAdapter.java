package com.alcanzar.cynapse.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.activity.MyConferenceDetailsActivity;
import com.alcanzar.cynapse.activity.MyConferencesActivity;
import com.alcanzar.cynapse.api.ChangeLikeApi;
import com.alcanzar.cynapse.api.StopConferenceBookingAPi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.PdfDelModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Constants;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder> {

    private Context context;
    private int rowLayout;
    private ArrayList<ConferenceDetailsModel> arrayList;
    boolean showOnclk;
    String[] x;
    String changeLike = "";
    static boolean showLike = false;
    private ArrayList<PdfDelModel> arrayListPdf;
    DatabaseHelper handler;
    HashMap<String, String> hashMap;

    public TicketsAdapter(Context context, int rowLayout, ArrayList<ConferenceDetailsModel> arrayList, boolean showOnclk) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        this.showOnclk = showOnclk;
        handler = new DatabaseHelper(context);
        hashMap = new HashMap<>();
    }


    public class TicketsViewHolder extends RecyclerView.ViewHolder {

        TextView title, address, date, time, txt_intertested, txt_views, txt_registered;
        ImageView share, img_tickimg, wishList;
        Button soldOutBtn;

        public TicketsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            address = itemView.findViewById(R.id.address);
            share = itemView.findViewById(R.id.share);
            wishList = itemView.findViewById(R.id.wishList);
            img_tickimg = itemView.findViewById(R.id.img_tickimg);
            txt_registered = itemView.findViewById(R.id.txt_registered);
            txt_intertested = itemView.findViewById(R.id.txt_intertested);
            txt_views = itemView.findViewById(R.id.txt_views);
            soldOutBtn = itemView.findViewById(R.id.soldOutBtn);



            if (showOnclk) {

//                wishList.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (showLike) {
//                            wishList.setImageResource(R.drawable.ic_like);
//                            showLike = false;
//                            changeLike = "1";
//                            hashMap.put("show_like", changeLike);
//
////                            Log.d("HASHMAPPPP", hashMap + "");
////
////                                ConferenceDetailsModel model = new ConferenceDetailsModel();
////
////                                model.setShow_like(changeLike);
////
////                                Log.d("CHAGNDELIKEE", arrayList.get(getAdapterPosition()).getInterested());
////
////                            if (!((MyConferencesActivity)context).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id())) {
////
////                                ((MyConferencesActivity)context).handler.AddMyConferenceDetails(model, true);
////
////                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
////                            } else {
////                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
////                                ((MyConferencesActivity) context).handler.AddMyConferenceDetails(model, false);
////                            }
////
//
//                            ((MyConferencesActivity) context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS);
//                            ((MyConferencesActivity) context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
//
//                            arrayList = ((MyConferencesActivity) context).handler.getMyConferenceDetails(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, " ");
////                              arrayList= ((MyConferencesActivity)context).handler.getMyConferenceDetailsId(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS,arrayList.get(getAdapterPosition()).getConference_id());
////                               Log.d("MYSHOWLIKEKE",arrayList.get(getAdapterPosition()).getShow_like());
//                        } else {
//
//                            wishList.setImageResource(R.drawable.heart_icon);
//                            showLike = true;
//                            changeLike = "0";
//                            hashMap.put("show_like", changeLike);
//
//                            // ConferenceDetailsModel model = new ConferenceDetailsModel();
////                            model.setId(arrayList.get(getAdapterPosition()).getId());
////                            model.setConference_id(arrayList.get(getAdapterPosition()).getConference_id());
////                            model.setConference_name(arrayList.get(getAdapterPosition()).getConference_name());
////                            model.setFrom_date(arrayList.get(getAdapterPosition()).getFrom_date());
////                            model.setTo_date(arrayList.get(getAdapterPosition()).getTo_date());
////                            model.setFrom_time(arrayList.get(getAdapterPosition()).getFrom_time());
////                            model.setTo_time(arrayList.get(getAdapterPosition()).getTo_time());
////                            model.setVenue(arrayList.get(getAdapterPosition()).getVenue());
////                            model.setEvent_host_name(arrayList.get(getAdapterPosition()).getEvent_host_name());
////                            model.setSpeciality(arrayList.get(getAdapterPosition()).getSpeciality());
////                            model.setContact(arrayList.get(getAdapterPosition()).getContact());
////                            model.setLocation("");
////                            model.setLatitude(arrayList.get(getAdapterPosition()).getLatitude());
////                            model.setLogitude(arrayList.get(getAdapterPosition()).getLogitude());
////                            model.setBrochuers_file(arrayList.get(getAdapterPosition()).getBrochuers_file());
////                            model.setBrochuers_charge(arrayList.get(getAdapterPosition()).getBrochuers_charge());
////                            model.setBrochuers_days(arrayList.get(getAdapterPosition()).getBrochuers_days());
////                            model.setRegistration_fee(arrayList.get(getAdapterPosition()).getRegistration_fee());
////                            model.setRegistration_days(arrayList.get(getAdapterPosition()).getRegistration_days());
////                            model.setEvent_sponcer(arrayList.get(getAdapterPosition()).getEvent_sponcer());
////                            model.setParticular_country_id(arrayList.get(getAdapterPosition()).getParticular_country_id());
////                            //model.setParticular_country_id("");
////                            model.setParticular_country_name(arrayList.get(getAdapterPosition()).getParticular_country_name());
////                            model.setParticular_state_id(arrayList.get(getAdapterPosition()).getParticular_state_id());
////                            model.setParticular_state_name(arrayList.get(getAdapterPosition()).getParticular_state_name());
////                            model.setStatus(arrayList.get(getAdapterPosition()).getStatus());
////                            model.setAdd_date(arrayList.get(getAdapterPosition()).getAdd_date());
////                            model.setModify_date(arrayList.get(getAdapterPosition()).getModify_date());
////                            model.setCost(arrayList.get(getAdapterPosition()).getCost());
////                            model.setDuration(arrayList.get(getAdapterPosition()).getDuration());
////                            model.setPayment_status("");
//                            //   model.setShow_like(changeLike);
////                            model.setParticular_city_id(arrayList.get(getAdapterPosition()).getParticular_city_id());
////                            model.setParticular_city_name(arrayList.get(getAdapterPosition()).getParticular_city_name());
////                            model.setViews(arrayList.get(getAdapterPosition()).getViews());
////                            model.setInterested(arrayList.get(getAdapterPosition()).getInterested());
////                            model.setRegistered(arrayList.get(getAdapterPosition()).getRegistered());
////                            model.setAddress_type(arrayList.get(getAdapterPosition()).getAddress_type());
////
////                            Log.d("CHAGNDELIKEE", arrayList.get(getAdapterPosition()).getInterested());
//////
////                            if (!((MyConferencesActivity)context).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id())) {
////
////                                ((MyConferencesActivity)context).handler.AddMyConferenceDetails(model, true);
////
////                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
////                            } else {
////                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
////                                ((MyConferencesActivity) context).handler.AddMyConferenceDetails(model, false);
////                            }
//                            ((MyConferencesActivity) context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS);
//                            ((MyConferencesActivity) context).handler.deleteData(arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id);
//
//                            arrayList = ((MyConferencesActivity) context).handler.getMyConferenceDetails(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, " ");
//                        }
//
//                        try {
//                            ChangeLikeApi1(arrayList.get(getAdapterPosition()).getConference_id(), changeLike, getAdapterPosition());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }


            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    sharingIntent.setType("text/plain");
//                    x = arrayList.get(getAdapterPosition()).getBrochuers_file().split(",");
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Conference Name: " + arrayList.get(getAdapterPosition()).getConference_name() + "\n" + "Conference Date:" + arrayList.get(getAdapterPosition()).getFrom_date()
//                            + "\n" + "Conference Time:" + arrayList.get(getAdapterPosition()).getFrom_time()
//                            + "\n" + "Conference Venue:" + arrayList.get(getAdapterPosition()).getVenue() + "\n"
//                            + "Conference Images:" + x[0]);
//                    context.startActivity(Intent.createC hooser(sharingIntent, "Share Text Using"));

                    shareDetails(getAdapterPosition());

                }
            });


            soldOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        if (arrayList.get(getAdapterPosition()).getBooking_stopped().equals("1")) {
                            Toast.makeText(context, "Already sold out", Toast.LENGTH_SHORT).show();
                        } else {
                            stopConferenceBooking(arrayList.get(getAdapterPosition()).getConference_id(), arrayList.get(getAdapterPosition()).getId(), soldOutBtn);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            if (showOnclk) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrayListPdf = handler.getPdfDel(DatabaseHelper.TABLE_DELETE_PDF, arrayList.get(getAdapterPosition()).getConference_id());
                        Log.d("ARLISSIZEEPDFF", arrayListPdf.size() + "");
//                        if(arrayListPdf.size()>0)
//                        {
//                            for(int i=0;i<a)
//
//                            String brouchurefile=arrayListPdf.get()
//                        }else {
//
//
//                        }


//                        Log.d("CONFIDIDDD11", arrayList.get(getAdapterPosition()).getInterested());
//
//////
////                        arrayList = handler.getMyConferenceDetailsId(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, arrayList.get(getAdapterPosition()).getConference_id());
////
////
//////
////                        if (arrayList.size() > 0) {
//
//                            ConferenceDetailsModel model = new ConferenceDetailsModel();
////                            model.setId(arrayList.get(0).getId());
////                            model.setConference_id(arrayList.get(0).getConference_id());
////                            model.setConference_name(arrayList.get(0).getConference_name());
////                            model.setFrom_date(arrayList.get(0).getFrom_date());
////                            model.setTo_date(arrayList.get(0).getTo_date());
////                            model.setFrom_time(arrayList.get(0).getFrom_time());
////                            model.setTo_time(arrayList.get(0).getTo_time());
////                            model.setVenue(arrayList.get(0).getVenue());
////                            model.setEvent_host_name(arrayList.get(0).getEvent_host_name());
////                            model.setSpeciality(arrayList.get(0).getSpeciality());
////                            model.setContact(arrayList.get(0).getContact());
////                            model.setLocation("");
////                            model.setLatitude(arrayList.get(0).getLatitude());
////                            model.setLogitude(arrayList.get(0).getLogitude());
////                            model.setBrochuers_file(arrayList.get(0).getBrochuers_file());
////                            model.setBrochuers_charge(arrayList.get(0).getBrochuers_charge());
////                            model.setBrochuers_days(arrayList.get(0).getBrochuers_days());
////                            model.setRegistration_fee(arrayList.get(0).getRegistration_fee());
////                            model.setRegistration_days(arrayList.get(0).getRegistration_days());
////                            model.setEvent_sponcer(arrayList.get(0).getEvent_sponcer());
////                            model.setParticular_country_id(arrayList.get(0).getParticular_country_id());
////                            //model.setParticular_country_id("");
////                            model.setParticular_country_name(arrayList.get(0).getParticular_country_name());
////                            model.setParticular_state_id(arrayList.get(0).getParticular_state_id());
////                            model.setParticular_state_name(arrayList.get(0).getParticular_state_name());
////                            model.setStatus(arrayList.get(0).getStatus());
////                            model.setAdd_date(arrayList.get(0).getAdd_date());
////                            model.setModify_date(arrayList.get(0).getModify_date());
////                            model.setCost(arrayList.get(0).getCost());
////                            model.setDuration(arrayList.get(0).getDuration());
////                            model.setPayment_status("");
//                              model.setShow_like(changeLike);
////                            model.setParticular_city_id(arrayList.get(0).getParticular_city_id());
////                            model.setParticular_city_name(arrayList.get(0).getParticular_city_name());
////                            model.setViews(arrayList.get(0).getViews());
////                            model.setInterested(arrayList.get(0).getInterested());
////                            model.setRegistered(arrayList.get(0).getRegistered());
////                            model.setAddress_type(arrayList.get(0).getAddress_type());
//
//                            Log.d("CHAGNDELIKEE", arrayList.get(0).getInterested());
//
//                            if (! ((MyConferencesActivity) context).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id, arrayList.get(0).getConference_id())) {
//
//                                ((MyConferencesActivity) context).handler.AddMyConferenceDetails(model, true);
//
//                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//                            } else {
//                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
//                                ((MyConferencesActivity) context).handler.AddMyConferenceDetails(model, false);
//                            }
//                            Intent intent = new Intent(context, MyConferenceDetailsActivity.class);
//                            intent.putExtra("conference_id", arrayList.get(0).getConference_id());
//
//                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                        }
                        Intent intent = new Intent(context, MyConferenceDetailsActivity.class);
//                        intent.putExtra("conference_id", arrayList.get(getAdapterPosition()).getConference_id());
//                        intent.putExtra("conference_name", arrayList.get(getAdapterPosition()).getConference_name());
//                        intent.putExtra("event_host_name", arrayList.get(getAdapterPosition()).getEvent_host_name());
//                        intent.putExtra("brochuers_file", arrayList.get(getAdapterPosition()).getBrochuers_file());
//
//                         intent.putExtra("credit_earnings", arrayList.get(getAdapterPosition()).getCredit_earnings());
////
////                        intent.putExtra("brochures_days", arrayList.get(getAdapterPosition()).getBrochuers_days());
//
//                        intent.putExtra("event_sponcer", arrayList.get(getAdapterPosition()).getEvent_sponcer());
//
//                        intent.putExtra("particular_country_id", arrayList.get(getAdapterPosition()).getParticular_country_id());
//
//                        intent.putExtra("particular_country_name", arrayList.get(getAdapterPosition()).getParticular_country_name());
//
//                        intent.putExtra("particular_state_id", arrayList.get(getAdapterPosition()).getParticular_state_id());
//
//                        intent.putExtra("particular_state_name", arrayList.get(getAdapterPosition()).getParticular_state_name());
//
//                        intent.putExtra("status", arrayList.get(getAdapterPosition()).getStatus());
//
//                        intent.putExtra("modify_date", arrayList.get(getAdapterPosition()).getModify_date());
//
//                        intent.putExtra("venue", arrayList.get(getAdapterPosition()).getVenue());
//                        intent.putExtra("speciality", arrayList.get(getAdapterPosition()).getSpeciality());
//                        intent.putExtra("add_date", arrayList.get(getAdapterPosition()).getAdd_date());
//                        intent.putExtra("contact", arrayList.get(getAdapterPosition()).getContact());
//                        intent.putExtra("from_date", arrayList.get(getAdapterPosition()).getFrom_date());
//                        intent.putExtra("from_time", arrayList.get(getAdapterPosition()).getFrom_time());
//                        intent.putExtra("to_date", arrayList.get(getAdapterPosition()).getTo_date());
//                        intent.putExtra("to_time", arrayList.get(getAdapterPosition()).getTo_time());
////                        intent.putExtra("registration_days", arrayList.get(getAdapterPosition()).getRegistration_days());
////                        intent.putExtra("registration_fee", arrayList.get(getAdapterPosition()).getRegistration_fee());
////                        intent.putExtra("cost", arrayList.get(getAdapterPosition()).getCost());
////                        intent.putExtra("duration", arrayList.get(getAdapterPosition()).getDuration());
//                        intent.putExtra("views", arrayList.get(getAdapterPosition()).getViews());
//                        intent.putExtra("interested", arrayList.get(getAdapterPosition()).getInterested());
//                        intent.putExtra("registered", arrayList.get(getAdapterPosition()).getRegistered());
//                        intent.putExtra("showLike", showLike);
//                        intent.putExtra("address_type", arrayList.get(getAdapterPosition()).getAddress_type());
//                        intent.putExtra("city_id", arrayList.get(getAdapterPosition()).getParticular_city_id());
//                        intent.putExtra("city_name", arrayList.get(getAdapterPosition()).getParticular_city_name());
//
//                        intent.putExtra("available_seat", arrayList.get(getAdapterPosition()).getAvailable_seat());
//                        intent.putExtra("conference_description", arrayList.get(getAdapterPosition()).getConference_description());
//                        intent.putExtra("conference_type_id", arrayList.get(getAdapterPosition()).getConference_type_id());
//                        intent.putExtra("credit_earnings", arrayList.get(getAdapterPosition()).getCredit_earnings());
//                        intent.putExtra("total_days_price", arrayList.get(getAdapterPosition()).getTotal_days_price());
//                        intent.putExtra("accomdation", arrayList.get(getAdapterPosition()).getAccomdation());
//                        intent.putExtra("member_concessions", arrayList.get(getAdapterPosition()).getMember_concessions());
//                        intent.putExtra("student_concessions", arrayList.get(getAdapterPosition()).getStudent_concessions());
//                        intent.putExtra("price_hike_after_date", arrayList.get(getAdapterPosition()).getPrice_hike_after_date());
//                        intent.putExtra("price_hike_after_percentage", arrayList.get(getAdapterPosition()).getPrice_hike_after_percentage());
//                        intent.putExtra("event_sponcer",arrayList.get(getAdapterPosition()).getEvent_sponcer());
//                        intent.putExtra("payment_mode",arrayList.get(getAdapterPosition()).getPayment_mode());
//                        intent.putExtra("medical_profile_id",arrayList.get(getAdapterPosition()).getMedical_profile_id());
//                        Log.d("SHOWLIKENPOSO",arrayList.get(getAdapterPosition()).getBrochuers_file());

                        intent.putExtra("conference_id", arrayList.get(getAdapterPosition()).getConference_id());
                        intent.putExtra("conference_name", arrayList.get(getAdapterPosition()).getConference_name());
                        intent.putExtra("event_host_name", arrayList.get(getAdapterPosition()).getEvent_host_name());
                        intent.putExtra("brochuers_file", arrayList.get(getAdapterPosition()).getBrochuers_file());
                        intent.putExtra("venue", arrayList.get(getAdapterPosition()).getVenue());
                        intent.putExtra("speciality", arrayList.get(getAdapterPosition()).getSpeciality());
                        intent.putExtra("add_date", arrayList.get(getAdapterPosition()).getAdd_date());
                        intent.putExtra("contact", arrayList.get(getAdapterPosition()).getContact());
                        intent.putExtra("from_date", arrayList.get(getAdapterPosition()).getFrom_date());
                        intent.putExtra("from_time", arrayList.get(getAdapterPosition()).getFrom_time());
                        intent.putExtra("conference_description", arrayList.get(getAdapterPosition()).getConference_description());
                        intent.putExtra("conference_type_id", arrayList.get(getAdapterPosition()).getConference_type_id());
                        intent.putExtra("credit_earnings", arrayList.get(getAdapterPosition()).getCredit_earnings());
                        intent.putExtra("total_days_price", arrayList.get(getAdapterPosition()).getTotal_days_price());
                        intent.putExtra("accomdation", arrayList.get(getAdapterPosition()).getAccomdation());
                        intent.putExtra("member_concessions", arrayList.get(getAdapterPosition()).getMember_concessions());
                        intent.putExtra("student_concessions", arrayList.get(getAdapterPosition()).getStudent_concessions());
                        intent.putExtra("price_hike_after_date", arrayList.get(getAdapterPosition()).getPrice_hike_after_date());
                        intent.putExtra("price_hike_after_percentage", arrayList.get(getAdapterPosition()).getPrice_hike_after_percentage());
                        intent.putExtra("available_seat", arrayList.get(getAdapterPosition()).getAvailable_seat());
                        intent.putExtra("to_date", arrayList.get(getAdapterPosition()).getTo_date());
                        intent.putExtra("to_time", arrayList.get(getAdapterPosition()).getTo_time());
                        intent.putExtra("event_sponcer", arrayList.get(getAdapterPosition()).getEvent_sponcer());
                        intent.putExtra("payment_mode", arrayList.get(getAdapterPosition()).getPayment_mode());
                        intent.putExtra("medical_profile_id", arrayList.get(getAdapterPosition()).getMedical_profile_id());
                        intent.putExtra("latitude", arrayList.get(getAdapterPosition()).getLatitude());
                        intent.putExtra("longitude", arrayList.get(getAdapterPosition()).getLogitude());
                        intent.putExtra("keynote_speakers", arrayList.get(getAdapterPosition()).getEvent_sponcer());
                        intent.putExtra("discount_percentage", arrayList.get(getAdapterPosition()).getDiscount_percentage());
                        intent.putExtra("discount_description", arrayList.get(getAdapterPosition()).getDiscount_description());
                        intent.putExtra("conference_type_name", arrayList.get(getAdapterPosition()).getConference_type_name());
                        intent.putExtra("gst", arrayList.get(getAdapterPosition()).getGst());
                        intent.putExtra("medical_profile_name", arrayList.get(getAdapterPosition()).getMedical_profile_name());
                        intent.putExtra("special_name", arrayList.get(getAdapterPosition()).getTarget_audience_speciality());
                        intent.putExtra("department_id", arrayList.get(getAdapterPosition()).getDepartment_id());
                        intent.putExtra("department_name", arrayList.get(getAdapterPosition()).getDepartment_name());

                        if (changeLike.equalsIgnoreCase("")) {
                            intent.putExtra("changeLike", arrayList.get(getAdapterPosition()).getShow_like());
                        } else {
                            intent.putExtra("changeLike", changeLike);
                        }
                        //intent.putExtra("like_status", arrayList.get(getAdapterPosition()).getShow_like());
                        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    }
                });
            }

        }
    }

    @Override
    public TicketsAdapter.TicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new TicketsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketsAdapter.TicketsViewHolder holder, int position) {

        holder.title.setText(arrayList.get(position).getConference_name());
        holder.address.setText(arrayList.get(position).getVenue());
        holder.date.setText(arrayList.get(position).getFrom_date());
        holder.time.setText(arrayList.get(position).getFrom_time());
        holder.txt_views.setText(arrayList.get(position).getViews() + " " + "Views");
        holder.txt_intertested.setText(arrayList.get(position).getInterested() + " " + "Interested");
        holder.txt_registered.setText(arrayList.get(position).getRegistered() + " " + "Registered");


        if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("1")) {
            holder.title.setText(arrayList.get(position).getConference_name() + "(" + "Conference" + ")");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("2")) {
            holder.title.setText(arrayList.get(position).getConference_name() + "(" + "Exhibition" + ")");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("3")) {
            holder.title.setText(arrayList.get(position).getConference_name() + "(" + "CME" + ")");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("4")) {
            holder.title.setText(arrayList.get(position).getConference_name() + "(" + "Training Courses" + ")");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("5")) {
            holder.title.setText(arrayList.get(position).getConference_name() + "(" + "Seminar" + ")");

        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("other_conference_type")) {

            holder.title.setText(arrayList.get(position).getConference_name() + "(" + "Other" + ")");

        }

        Log.d("showOnclkwillbedd", arrayList.get(position).getConference_type_id() + "");

        if (arrayList.get(position).getShow_like().equalsIgnoreCase("1")) {
            Log.d("OOPOPOP", "33333");
            holder.wishList.setImageResource(R.drawable.ic_like);
            //changeLike="1";
            showLike = false;
        } else {
            Log.d("OOPOPOP", "44444");
            holder.wishList.setImageResource(R.drawable.heart_icon);
            //changeLike="0";
            showLike = true;
        }

//        if (arrayList.get(position).getBooking_stopped().equals("1")) {
//            holder.soldOutBtn.setEnabled(false);
//        } else {
//            holder.soldOutBtn.setEnabled(true);
//        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void ChangeLikeApi1(String s, final String status, final int pos) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", status);


        header.put("Cynapse", params);
        new ChangeLikeApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSMYCONFYYY", response.toString());

                    if (res_code.equals("1")) {


//                        if (status.equalsIgnoreCase("1")) {
//                            Log.d("RESPONSMYCONFYYY", hashMap+"");
//                            handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(pos).getConference_id(), DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS);
//                            handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(pos).getConference_id(), DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
//                        } else {
//                            handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(pos).getConference_id(), DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS);
//                            handler.deleteData(arrayList.get(pos).getConference_id(), DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id);
//                        }


                        // MyToast.toastLong((Activity) context, res_msg);
                    } else {
                        // MyToast.toastLong(context, res_msg);
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

    private void ChangeLikeApi(String s, String status) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", status);


        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeLikeApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSMYCONFYYY", response.toString());
                    if (res_code.equals("1")) {

                        // MyToast.toastLong((Activity) context, res_msg);
                    } else {
                        // MyToast.toastLong(context, res_msg);
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


    private void stopConferenceBooking(String conference_id, final String id, final View view) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        header.put("Cynapse", params);

        new StopConferenceBookingAPi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSMYCONFYYY", response.toString());
                    if (res_code.equals("1")) {
                        MyToast.toastLong((Activity) context, res_msg);

                        handler.myConferenceEditBooingStop("0", id);

                        // arrayList = handler.getMyConferenceDetails(DatabaseHelper.TABLE_MY_CONFERENCES_SHOW_DETAILS, "3");
                        view.setEnabled(false);
                        // Log.d("bobob582",arrayList.toString());
                    } else {
                        MyToast.toastLong((Activity) context, res_msg);
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

    public void removeAt(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());
    }


    private void shareDetails(int getAdapterPosition) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Conference Name: " + arrayList.get(getAdapterPosition).getConference_name() +
                "\n\n" + "Conference Type:" + arrayList.get(getAdapterPosition).getConference_type_name()
                + "\n\n" + "Conference Date:" + arrayList.get(getAdapterPosition).getFrom_date() + "-" + arrayList.get(getAdapterPosition).getTo_date()
                + "\n\n" + "Conference Time:" + arrayList.get(getAdapterPosition).getFrom_time() + "-" + arrayList.get(getAdapterPosition).getTo_time()
                + "\n\n" + "Conference Venue:" + arrayList.get(getAdapterPosition).getVenue()
                + "\n\n" + "Keynotes Speakers:" + arrayList.get(getAdapterPosition).getEvent_sponcer() + "\n\n"
                + "App Download Links:" + "\n"
                + Constants.playStoreUrl + "\n\n" + Constants.appStoreUrl);
        context.startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));

    }
}
