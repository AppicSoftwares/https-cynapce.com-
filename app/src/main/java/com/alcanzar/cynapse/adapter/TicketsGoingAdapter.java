package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.GoingConferenceDetailsActivity;
import com.alcanzar.cynapse.activity.MyConferencesActivity;
import com.alcanzar.cynapse.api.ChangeLikeApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Constants;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketsGoingAdapter extends RecyclerView.Adapter<TicketsGoingAdapter.TicketsViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<ConferenceDetailsModel> arrayList;
    boolean showOnclk;
    String[] x;
    String changeLike = "";
    static boolean showLike = true;

    DatabaseHelper handler;
    HashMap<String, String> hashMap1, hashMap2;

    public TicketsGoingAdapter(Context context, int rowLayout, ArrayList<ConferenceDetailsModel> arrayList) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        handler = new DatabaseHelper(context);
        hashMap1 = new HashMap<>();
        hashMap2 = new HashMap<>();
    }


    public class TicketsViewHolder extends RecyclerView.ViewHolder {

        TextView title, address, date, time, txt_intertested, txt_views, txt_registered;
        ImageView share, img_tickimg, wishList,wishListRed;

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
            wishListRed = itemView.findViewById(R.id.wishListRed);


            wishList.setVisibility(View.GONE);
            wishListRed.setVisibility(View.GONE);

            wishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("SHOWLIKEVAL",showLike+"");
                    if (showLike) {
                        Log.d("111111", "plplp");

                        showLike = false;
                        changeLike = "0";
//                        hashMap1.put("show_like", changeLike);
//                        Log.d("HASHMAPPPP", hashMap1 + "");
//                        wishList.setImageResource(R.drawable.ic_like);
//
//                        ((MyConferencesActivity) context).handler.updateData(hashMap1, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);
////                            ((MyConferencesActivity)context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
//
//                        arrayList = ((MyConferencesActivity) context).handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1");

                        try {
                            ChangeLikeApi1(wishList,wishListRed,arrayList.get(getAdapterPosition()).getConference_id(), changeLike, getAdapterPosition());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        hashMap1.put("conference_id", arrayList.get(getAdapterPosition()).getConference_id());
//                        hashMap1.put("conference_name", arrayList.get(getAdapterPosition()).getConference_name());
//                        hashMap1.put("event_host_name", arrayList.get(getAdapterPosition()).getEvent_host_name());
//                        hashMap1.put("brochuers_file", arrayList.get(getAdapterPosition()).getBrochuers_file());
//
//                        hashMap1.put("brochuers_charge", arrayList.get(getAdapterPosition()).getBrochuers_charge());
//
//                        hashMap1.put("brochuers_days", arrayList.get(getAdapterPosition()).getBrochuers_days());
//
//                        hashMap1.put("event_sponcer", arrayList.get(getAdapterPosition()).getEvent_sponcer());
//
//                        hashMap1.put("particular_country_id", arrayList.get(getAdapterPosition()).getParticular_country_id());
//
//                        hashMap1.put("particular_country_name", arrayList.get(getAdapterPosition()).getParticular_country_name());
//
//                        hashMap1.put("particular_state_id", arrayList.get(getAdapterPosition()).getParticular_state_id());
//
//                        hashMap1.put("particular_state_name", arrayList.get(getAdapterPosition()).getParticular_state_name());
//
//                        hashMap1.put("status", arrayList.get(getAdapterPosition()).getStatus());
//
//                        hashMap1.put("modify_date", arrayList.get(getAdapterPosition()).getModify_date());
//
//
//                        hashMap1.put("venue", arrayList.get(getAdapterPosition()).getVenue());
//                        hashMap1.put("speciality", arrayList.get(getAdapterPosition()).getSpeciality());
//                        hashMap1.put("add_date", arrayList.get(getAdapterPosition()).getAdd_date());
//                        hashMap1.put("contact", arrayList.get(getAdapterPosition()).getContact());
//                        hashMap1.put("from_date", arrayList.get(getAdapterPosition()).getFrom_date());
//                        hashMap1.put("from_time", arrayList.get(getAdapterPosition()).getFrom_time());
//                        hashMap1.put("to_date", arrayList.get(getAdapterPosition()).getTo_date());
//                        hashMap1.put("to_time", arrayList.get(getAdapterPosition()).getTo_time());
//                        hashMap1.put("registration_days", arrayList.get(getAdapterPosition()).getRegistration_days());
//                        hashMap1.put("registration_fee", arrayList.get(getAdapterPosition()).getRegistration_fee());
//
//                        hashMap1.put("latitude", arrayList.get(getAdapterPosition()).getLatitude());
//                        hashMap1.put("logitude", arrayList.get(getAdapterPosition()).getLogitude());
//                        hashMap1.put("show_like", changeLike);
//                        hashMap1.put("payment_status", "2");
//                        if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id())) {
//
//                            ((MyConferencesActivity) context).handler.insert(hashMap1, DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
//
//                            //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//                        } else {
//                            //   Log.e("UPDATED", true + " " + model.getProduct_id());
//                            ((MyConferencesActivity)context).handler.updateData(hashMap1, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
//
//                        }
//
//
//
//
//                        arrayList1 = ((MyConferencesActivity) context).handler.getSaveConferenceDetails(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, "1");
//                        for (int j = 0; j < arrayList1.size(); j++) {
//                            Log.d("SABECONFUPDATDETLS11", arrayList1.get(j).getConference_name());
//                        }

                    } else {
                        showLike = true;
                        changeLike = "1";

//                        wishList.setImageResource(R.drawable.heart_icon);
//                        showLike = true;
//                        changeLike = "0";
//                        hashMap1.put("show_like", changeLike);
//
//                        ((MyConferencesActivity) context).handler.updateData(hashMap1, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);
//
//                        arrayList = ((MyConferencesActivity) context).handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1");
//                        ((MyConferencesActivity) context).handler.deleteData(arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id);
//
//                        ArrayList<ConferenceDetailsModel> arrayList1 = new ArrayList<ConferenceDetailsModel>();
//                        arrayList1 = ((MyConferencesActivity) context).handler.getSaveConferenceDetails(DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS, "1");
//                        for (int j = 0; j < arrayList1.size(); j++) {
//                            Log.d("SABECONFUPDATDETLS22", arrayList1.get(j).getConference_name());
//                        }


                        try {
                            ChangeLikeApi1(wishList,wishListRed,arrayList.get(getAdapterPosition()).getConference_id(), changeLike, getAdapterPosition());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    //   arrayList= ((MyConferencesActivity)context).handler.getGoingConferenceDetailsId(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS,arrayList.get(getAdapterPosition()).getConference_id());


                }
            });


            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                    sharingIntent.setType("text/plain");
//                    x = arrayList.get(getAdapterPosition()).getBrochuers_file().split(",");
//                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Conference Name: " + arrayList.get(getAdapterPosition()).getConference_name() + "\n" + "Conference Date:" + arrayList.get(getAdapterPosition()).getFrom_date()
//                            + "\n" + "Conference Time:" + arrayList.get(getAdapterPosition()).getFrom_time()
//                            + "\n" + "Conference Venue:" + arrayList.get(getAdapterPosition()).getVenue() + "\n"
//                            + "Conference Images:" + x[0]);
//                    context.startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));

                        shareDetails(getAdapterPosition());
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //                    intent.putExtra("conference_id", arrayList.get(getAdapterPosition()).getConference_id());
//                    intent.putExtra("conference_name", arrayList.get(getAdapterPosition()).getConference_name());
//                    intent.putExtra("event_host_name", arrayList.get(getAdapterPosition()).getEvent_host_name());
//                    intent.putExtra("brochuers_file", arrayList.get(getAdapterPosition()).getBrochuers_file());
//
//                    intent.putExtra("credit_earnings", arrayList.get(getAdapterPosition()).getCredit_earnings());
////
////                        intent.putExtra("brochures_days", arrayList.get(getAdapterPosition()).getBrochuers_days());
//
//                    intent.putExtra("event_sponcer", arrayList.get(getAdapterPosition()).getEvent_sponcer());
//
//                    intent.putExtra("particular_country_id", arrayList.get(getAdapterPosition()).getParticular_country_id());
//
//                    intent.putExtra("particular_country_name", arrayList.get(getAdapterPosition()).getParticular_country_name());
//
//                    intent.putExtra("particular_state_id", arrayList.get(getAdapterPosition()).getParticular_state_id());
//
//                    intent.putExtra("particular_state_name", arrayList.get(getAdapterPosition()).getParticular_state_name());
//
//                    intent.putExtra("status", arrayList.get(getAdapterPosition()).getStatus());
//
//                    intent.putExtra("modify_date", arrayList.get(getAdapterPosition()).getModify_date());
//
//                    intent.putExtra("venue", arrayList.get(getAdapterPosition()).getVenue());
//                    intent.putExtra("speciality", arrayList.get(getAdapterPosition()).getSpeciality());
//                    intent.putExtra("add_date", arrayList.get(getAdapterPosition()).getAdd_date());
//                    intent.putExtra("contact", arrayList.get(getAdapterPosition()).getContact());
//                    intent.putExtra("from_date", arrayList.get(getAdapterPosition()).getFrom_date());
//                    intent.putExtra("from_time", arrayList.get(getAdapterPosition()).getFrom_time());
//                    intent.putExtra("to_date", arrayList.get(getAdapterPosition()).getTo_date());
//                    intent.putExtra("to_time", arrayList.get(getAdapterPosition()).getTo_time());
////                    intent.putExtra("registration_days", arrayList.get(getAdapterPosition()).getRegistration_days());
////                    intent.putExtra("registration_fee", arrayList.get(getAdapterPosition()).getRegistration_fee());
////                    intent.putExtra("cost", arrayList.get(getAdapterPosition()).getCost());
////                    intent.putExtra("duration", arrayList.get(getAdapterPosition()).getDuration());
//
//                    intent.putExtra("address_type", arrayList.get(getAdapterPosition()).getAddress_type());
//                    intent.putExtra("city_id", arrayList.get(getAdapterPosition()).getParticular_city_id());
//                    intent.putExtra("city_name", arrayList.get(getAdapterPosition()).getParticular_city_name());
//
//                    intent.putExtra("available_seat", arrayList.get(getAdapterPosition()).getAvailable_seat());
//                    intent.putExtra("conference_description", arrayList.get(getAdapterPosition()).getConference_description());
//                    intent.putExtra("conference_type_id", arrayList.get(getAdapterPosition()).getConference_type_id());
//                    intent.putExtra("credit_earnings", arrayList.get(getAdapterPosition()).getCredit_earnings());
//                    intent.putExtra("total_days_price", arrayList.get(getAdapterPosition()).getTotal_days_price());
//                    intent.putExtra("accomdation", arrayList.get(getAdapterPosition()).getAccomdation());
//                    intent.putExtra("member_concessions", arrayList.get(getAdapterPosition()).getMember_concessions());
//                    intent.putExtra("student_concessions", arrayList.get(getAdapterPosition()).getStudent_concessions());
//                    intent.putExtra("price_hike_after_date", arrayList.get(getAdapterPosition()).getPrice_hike_after_date());
//                    intent.putExtra("price_hike_after_percentage", arrayList.get(getAdapterPosition()).getPrice_hike_after_percentage());
//                    intent.putExtra("event_sponcer",arrayList.get(getAdapterPosition()).getEvent_sponcer());
//                    intent.putExtra("payment_mode",arrayList.get(getAdapterPosition()).getPayment_mode());
//                    intent.putExtra("medical_profile_id",arrayList.get(getAdapterPosition()).getMedical_profile_id());
//
//                    if (changeLike.equalsIgnoreCase("")) {
//                        intent.putExtra("changeLike", arrayList.get(getAdapterPosition()).getShow_like());
//                    } else {
//                        intent.putExtra("changeLike", changeLike);
//                    }
//                    intent.putExtra("showLike", showLike);
                    Intent intent = new Intent(context, GoingConferenceDetailsActivity.class);
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
                        intent.putExtra("like_status", arrayList.get(getAdapterPosition()).getShow_like());
                    } else {
                        intent.putExtra("like_status", changeLike);
                    }

                    Log.d("SHOLIKEGOING", showLike + "");
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP ));
                }
            });
        }


    }

    @Override
    public TicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new TicketsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketsViewHolder holder, int position) {
        Log.d("showOnclkwillbe", arrayList.get(position).getShow_like() + "");

        if(arrayList.get(position).getConference_type_id().equalsIgnoreCase("1"))
        {
            holder.title.setText(arrayList.get(position).getConference_name()+"("+"Conference"+")");
        }else if(arrayList.get(position).getConference_type_id().equalsIgnoreCase("2"))
        {
            holder.title.setText(arrayList.get(position).getConference_name()+"("+"Exhibition"+")");
        }else if(arrayList.get(position).getConference_type_id().equalsIgnoreCase("3"))
        {
            holder.title.setText(arrayList.get(position).getConference_name()+"("+"CME"+")");
        }else if(arrayList.get(position).getConference_type_id().equalsIgnoreCase("4"))
        {
            holder.title.setText(arrayList.get(position).getConference_name()+"("+"Training Courses"+")");
        }else if(arrayList.get(position).getConference_type_id().equalsIgnoreCase("5"))
        {
            holder.title.setText(arrayList.get(position).getConference_name()+"("+"Seminar"+")");

        }else if(arrayList.get(position).getConference_type_id().equalsIgnoreCase("other_conference_type"))
        {

            holder.title.setText(arrayList.get(position).getConference_name()+"("+"Other"+")");

        }
        holder.address.setText(arrayList.get(position).getVenue());
        holder.date.setText(arrayList.get(position).getFrom_date());
        holder.time.setText(arrayList.get(position).getFrom_time());

        if (arrayList.get(position).getShow_like().equalsIgnoreCase("1")) {
            Log.d("OOPOPOP", "33333");
            //holder.wishList.setImageResource(R.drawable.ic_like);
//            holder.wishList.setVisibility(View.GONE);
//            holder.wishListRed.setVisibility(View.VISIBLE);
             showLike = true;
        } else {
            Log.d("OOPOPOP", "44444");
            //holder.wishList.setImageResource(R.drawable.heart_icon);
//            holder.wishList.setVisibility(View.VISIBLE);
//            holder.wishListRed.setVisibility(View.GONE);
             showLike = false;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void ChangeLikeApi1(final ImageView img, final ImageView imgRed, String s, final String status, final int pos) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", "1");

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
                        if (res_msg.equalsIgnoreCase("Conference Like Successfully"))
                        {
                            hashMap1.put("show_like", status);
                            Log.d("HASHMAPPP111", hashMap1 + "");
                            //img.setImageResource(R.drawable.ic_like);

//                            img.setVisibility(View.GONE);
//                            imgRed.setVisibility(View.VISIBLE);

                            ((MyConferencesActivity) context).handler.updateData(hashMap1, DatabaseHelper.conference_id, arrayList.get(pos).getConference_id(), DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);
                            arrayList = ((MyConferencesActivity) context).handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1");

                        }else {

                            hashMap1.put("show_like", status);
                            Log.d("HASHMAPPP222", hashMap1 + "");
                            //img.setImageResource(R.drawable.heart_icon);

//                            img.setVisibility(View.VISIBLE);
//                            imgRed.setVisibility(View.GONE);

                            ((MyConferencesActivity) context).handler.updateData(hashMap1, DatabaseHelper.conference_id, arrayList.get(pos).getConference_id(), DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);
                            arrayList = ((MyConferencesActivity) context).handler.getGoingConferenceDetails(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, "1");

                        }
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
                +"\n\n" + "Conference Date:" + arrayList.get(getAdapterPosition).getFrom_date()+"-"+arrayList.get(getAdapterPosition).getTo_date()
                + "\n\n" + "Conference Time:" + arrayList.get(getAdapterPosition).getFrom_time()+"-"+arrayList.get(getAdapterPosition).getTo_time()
                + "\n\n" + "Conference Venue:" + arrayList.get(getAdapterPosition).getVenue()
                + "\n\n" + "Keynotes Speakers:" + arrayList.get(getAdapterPosition).getEvent_sponcer()+"\n\n"
                +"App Download Links:"+"\n"
                + Constants.playStoreUrl+"\n\n"+Constants.appStoreUrl);
        context.startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));

    }
}
