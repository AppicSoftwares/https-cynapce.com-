package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.activity.TicketDetailsNew;
import com.alcanzar.cynapse.api.ChangeLikeApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ImageModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;

import com.alcanzar.cynapse.utils.Constants;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class TicketsConferenceAdapter extends RecyclerView.Adapter<TicketsConferenceAdapter.TicketsViewHolder> {

    private Context context;
    private int rowLayout;
    String[] x;
    static boolean showLike = true;
    static ArrayList<Boolean> showLikeAL = new ArrayList<>();
    String changeLike = "";
    private ArrayList<ConferenceDetailsModel> arrayList;
    private ArrayList<ImageModel> pdfList;
    HashMap<String, String> hashMap = new HashMap<>();
    DatabaseHelper handler;

    public TicketsConferenceAdapter(Context context, int rowLayout, ArrayList<ConferenceDetailsModel> arrayList) {

        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        handler = new DatabaseHelper(context);

    }

    public class TicketsViewHolder extends RecyclerView.ViewHolder {

        TextView title, address, date, time, soldOutTv;
        ImageView share, img_tickimg, wishList;
        RelativeLayout rel_dash;

        public TicketsViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            address = itemView.findViewById(R.id.address);
            share = itemView.findViewById(R.id.share);
            img_tickimg = itemView.findViewById(R.id.img_tickimg);
            wishList = itemView.findViewById(R.id.wishList);
            rel_dash = itemView.findViewById(R.id.rel_dash);
            soldOutTv = itemView.findViewById(R.id.soldOutTv);

//            rel_dash.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent inte =  new Intent(context, TicketDetailsNew.class);
//                    context.startActivity(inte);
//                }
//            });

            wishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("showlikevalu11", showLike + "");

                    if (showLikeAL.get(getAdapterPosition())) {
//                     wishList.setImageResource(R.drawable.heart_icon);
                        Log.d("111111", "plplp");
                        //showLike = false;
                        showLikeAL.set(getAdapterPosition(),false);
                        changeLike = "1";
//                        hashMap.put("show_like", changeLike);
//                        Log.d("showlikevalu22", showLike + "");
//
//                        ((MainActivity) context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);
//
//
//                        arrayList = ((MainActivity) context).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1");
                        try {
                            ChangeLikeApi(wishList, arrayList.get(getAdapterPosition()).getConference_id(), changeLike, showLike, getAdapterPosition());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        ConferenceDetailsModel model = new ConferenceDetailsModel();
//                        model.setShow_like(changeLike);
//                        if (!((MainActivity)context).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id())) {
//
//                            ((MainActivity)context).handler.AddConferenceDetails(model, true);
//
//                            //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//                        } else {
//                            //   Log.e("UPDATED", true + " " + model.getProduct_id());
//                            ((MainActivity) context).handler.AddConferenceDetails(model, false);
//                        }
                    } else {

//
                        Log.d("222222", "plplp");
                        //showLike = true;
                        changeLike = "1";
                        showLikeAL.set(getAdapterPosition(),true);
//                        Log.d("showlikevalu22", showLike + "");
//                        Log.d("conferenceid23", arrayList.get(getAdapterPosition()).getConference_id() + "");
//                        hashMap.put("show_like", changeLike);
//                        wishList.setImageResource(R.drawable.ic_like);
//                        ((MainActivity) context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);
//                        arrayList = ((MainActivity) context).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1");
                        try {
                            ChangeLikeApi(wishList, arrayList.get(getAdapterPosition()).getConference_id(), changeLike, showLike, getAdapterPosition());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // ((MainActivity)context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(getAdapterPosition()).getConference_id(), DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);

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
                public void onClick(View view) {
                    Log.d("showLikeitem", showLike + "");
                    Intent intent = new Intent(context, TicketDetailsNew.class);
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
                    intent.putExtra("like_status", arrayList.get(getAdapterPosition()).getShow_like());
                    intent.putExtra("gst", arrayList.get(getAdapterPosition()).getGst());
                    intent.putExtra("booking_stopped", arrayList.get(getAdapterPosition()).getBooking_stopped());
                    intent.putExtra("speciality_name", arrayList.get(getAdapterPosition()).getTarget_audience_speciality());
                    intent.putExtra("medical_profile_name", arrayList.get(getAdapterPosition()).getMedical_profile_name());
                    intent.putExtra("department_id", arrayList.get(getAdapterPosition()).getDepartment_id());
                    intent.putExtra("department_name", arrayList.get(getAdapterPosition()).getDepartment_name());


                    if (changeLike.equalsIgnoreCase("")) {
                        Log.d("SHOWLIKECONF", "11111");
                        intent.putExtra("changeLike", arrayList.get(getAdapterPosition()).getShow_like());
                    } else {
                        Log.d("SHOWLIKECONF", "222222");
                        intent.putExtra("changeLike", changeLike);
                    }

                    intent.putExtra("showLike", showLike);
                    Log.d("CHECKLIKE33", arrayList.get(getAdapterPosition()).getConference_id() + "");
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                  context.startActivity(new Intent(context, TicketDetails.class));
                }
            });


        }
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

    @Override
    public TicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new TicketsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketsViewHolder holder, int position) {


        holder.address.setText(arrayList.get(position).getVenue());
        holder.date.setText(arrayList.get(position).getFrom_date());
        holder.time.setText(arrayList.get(position).getFrom_time());

        if (arrayList.get(position).getBooking_stopped().equals("1")) {
            holder.soldOutTv.setVisibility(View.VISIBLE);
        } else if (Integer.parseInt(arrayList.get(position).getAvailable_seat()) < 1) {
            holder.soldOutTv.setVisibility(View.VISIBLE);
        }

        holder.time.setText(arrayList.get(position).getFrom_time());

        Log.d("OOPOPOP5656", arrayList.get(position).getConference_type_id());

        if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("1")) {
            holder.title.setText(arrayList.get(position).getConference_name() + " " + "(" + " " + "Conference" + " " + ")" + " ");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("2")) {
            holder.title.setText(arrayList.get(position).getConference_name() + " " + "(" + " " + "Exhibition" + " " + ")" + " ");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("3")) {
            holder.title.setText(arrayList.get(position).getConference_name() + " " + "(" + " " + "CME" + " " + ")" + " ");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("4")) {
            holder.title.setText(arrayList.get(position).getConference_name() + " " + "(" + " " + "Training Courses" + " " + ")" + " ");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("5")) {
            holder.title.setText(arrayList.get(position).getConference_name() + " " + "(" + " " + "Seminar" + " " + ")" + " ");
        } else if (arrayList.get(position).getConference_type_id().equalsIgnoreCase("other_conference_type")) {
            holder.title.setText(arrayList.get(position).getConference_name() + " " + "(" + " " + "Other" + " " + ")" + " ");
        }

        Log.d("okdkdkkksdfsdf", arrayList.get(position).getTotal_days_price());

        if (arrayList.get(position).getShow_like().equalsIgnoreCase("1")) {
            Log.d("OOPOPOP", "11111");
            //holder.wishList.setImageResource(R.drawable.ic_like);
            holder.wishList.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            //holder.wishList.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
            showLike = true;
            showLikeAL.add(true);
        } else {
            Log.d("OOPOPOP", "22222");
            holder.wishList.setImageResource(R.drawable.heart_icon);
            showLike = false;
            showLikeAL.add(false);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void ChangeLikeApi(final ImageView img, String s, final String status, final boolean boolLike, final int pos) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", "1");

        //params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeLikeApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOTRR", response.toString());
                    if (res_code.equals("1")) {

                        if (res_msg.equalsIgnoreCase("Conference Like Successfully")) {
                            Log.d("showlikevalu255", status + "");
                            Log.d("conferenceid23", arrayList.get(pos).getConference_id() + "");
                            hashMap.put("show_like", status);
                            //img.setImageResource(R.drawable.ic_like);
                            img.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                            ((MainActivity) context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(pos).getConference_id(), DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);
                            // arrayList = ((MainActivity) context).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1");
                        } else {
                            //img.setImageResource(R.drawable.heart_icon);
                            img.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                            hashMap.put("show_like", status);
                            Log.d("showlikevalu22", status + "");
                            ((MainActivity) context).handler.updateData(hashMap, DatabaseHelper.conference_id, arrayList.get(pos).getConference_id(), DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);
                            //arrayList = ((MainActivity) context).handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1");
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

}
