package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MarketPlaceActivity;
import com.alcanzar.cynapse.activity.MessageActivity;
import com.alcanzar.cynapse.activity.MyDealsActivity;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.activity.PaymentConference;
import com.alcanzar.cynapse.activity.RecommendedJobsActivity;
import com.alcanzar.cynapse.api.ChangeNotificationStatusApi;
import com.alcanzar.cynapse.api.GetMyDealApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.model.NotificationDashBoardModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context context;
    private int rowLayout;
    DatabaseHelper handler;
    private ArrayList<NotificationDashBoardModel> arrayList;

    public  NotificationAdapter(Context context,int rowLayout,ArrayList<NotificationDashBoardModel> arrayList){
          this.context = context;
          this.rowLayout = rowLayout;
          this.arrayList = arrayList;
          handler = new DatabaseHelper(context);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView msg,time;
        LinearLayout top,new_lin_lay;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            top = itemView.findViewById(R.id.top);
            new_lin_lay = itemView.findViewById(R.id.new_lin_lay);
            time = itemView.findViewById(R.id.time);

            try {
                GetMyDealApi();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent it=new Intent(context, PaymentConference.class);
//                    it.putExtra("conference_id",arrayList.get(getAdapterPosition()).getProduct_id());
//                    context.startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                    if (arrayList.get(getAdapterPosition()).getStatus().equalsIgnoreCase("2")) {
                        try {
                            ChangeNotificationStatusApi(arrayList.get(getAdapterPosition()).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!arrayList.get(getAdapterPosition()).getMsg().contains("rejected")) {

                        if (arrayList.get(getAdapterPosition()).getMsgType().equalsIgnoreCase("5"))//Buy
                        {
                            Intent intent = new Intent(context, MarketPlaceActivity.class);
                            intent.putExtra("prod_id", arrayList.get(getAdapterPosition()).getProduct_id());
                            intent.putExtra("cat_id", arrayList.get(getAdapterPosition()).getProduct_category_id());
                            intent.putExtra("where", "NotificationAdapter");
                            intent.putExtra("from_noti", "true");
                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                        else if (arrayList.get(getAdapterPosition()).getMsgType().equalsIgnoreCase("2"))//Sell
                        {
                            Intent intent = new Intent(context, MyDealsActivity.class);
                            intent.putExtra("FromNoti", "2");
//                        intent.putExtra("prod_id",arrayList.get(getAdapterPosition()).getProduct_id());
//                        intent.putExtra("cat_id",arrayList.get(getAdapterPosition()).getProduct_category_id());
//                        intent.putExtra("from_noti","true");
                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                        else if (arrayList.get(getAdapterPosition()).getMsgType().equalsIgnoreCase("6")) {
                            Intent intent = new Intent(context, RecommendedJobsActivity.class);
                            intent.putExtra("recommend", "1");
                            intent.putExtra("job_id", arrayList.get(getAdapterPosition()).getProduct_id());
                            intent.putExtra("applicants_uuid", "");
                            Log.d("JOBIDDD11", "kdkdkdkdk");
//                        intent.putExtra("cat_id",arrayList.get(getAdapterPosition()).getProduct_category_id());
//                        intent.putExtra("from_noti","true");
                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                        else if (arrayList.get(getAdapterPosition()).getMsgType().equalsIgnoreCase("7")) {
                            Intent intent = new Intent(context, RecommendedJobsActivity.class);
                            intent.putExtra("recommend", "0");
                            intent.putExtra("job_id", arrayList.get(getAdapterPosition()).getProduct_id());
                            intent.putExtra("applicants_uuid", arrayList.get(getAdapterPosition()).getSender_id());
                            Log.d("JOBIDDD22", "kdkdkdkdk");
                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                        else if (arrayList.get(getAdapterPosition()).getMsgType().equalsIgnoreCase("12")) {
                            if (arrayList.get(getAdapterPosition()).getProduct_category_id().equalsIgnoreCase("5")) {
                                Intent intent = new Intent(context, MessageActivity.class);
                                intent.putExtra("sender_id", arrayList.get(getAdapterPosition()).getSender_id());
                                intent.putExtra("reciever_id", arrayList.get(getAdapterPosition()).getUuid());
                                intent.putExtra("prod_id", arrayList.get(getAdapterPosition()).getProduct_id());
                                intent.putExtra("chat_id", arrayList.get(getAdapterPosition()).getChat_id());
                                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }

                        } else if (arrayList.get(getAdapterPosition()).getMsgType().equalsIgnoreCase("13")) {
                            Intent intent = new Intent(context, MyProfileActivity.class);
                            intent.putExtra("edit_disable","false");
                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                        } else if (arrayList.get(getAdapterPosition()).getMsgType().equalsIgnoreCase("4")) {
                            Intent intent = new Intent(context, PaymentConference.class);
                            intent.putExtra("conference_id", arrayList.get(getAdapterPosition()).getProduct_id());
                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                }
            });
        }
    }
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout,parent,false);
        return new NotificationViewHolder(view);
    }
    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.msg.setText(arrayList.get(position).getMsg());
        Log.d("STAUTSNOTIF",arrayList.get(position).getStatus());
        if(arrayList.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.top.setBackgroundColor(ContextCompat.getColor(context,R.color.color_white));
            holder.new_lin_lay.setVisibility(View.GONE);
        }
        else
        {
            holder.top.setBackgroundColor(ContextCompat.getColor(context,R.color.noti_row_color));
            holder.new_lin_lay.setVisibility(View.VISIBLE);
        }
        holder.time.setText(arrayList.get(position).getTime());
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void ChangeNotificationStatusApi(String s) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("notification_id", s);

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeNotificationStatusApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
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
    public void GetMyDealApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context,AppCustomPreferenceClass.UserId,""));
        params.put("sync_time", AppCustomPreferenceClass.readString(context,AppCustomPreferenceClass.deal_sync_time,""));
        //  params.put("sync_time", "");
        header.put("Cynapse",params);
        new GetMyDealApi(context,header,false){
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(context,AppCustomPreferenceClass.deal_sync_time,sync_time);
                    Log.d("STATERESPONSE",response.toString());

                    if(res_code.equals("1")){
                        //MyToast.toastShort(this,res_msg);
                        if(header.has("SellingProduct"))
                        {
                            JSONArray header2 = header.getJSONArray("SellingProduct");
                            for(int i = 0;i<header2.length();i++){
                                JSONObject item  = header2.getJSONObject(i);
                                DashBoardProductModel model = new DashBoardProductModel();
                                model.setId(item.getString("id"));
                                model.setProduct_id(item.getString("product_id"));
                                model.setProduct_name(item.getString("product_name"));
                                model.setCategory_id(item.getString("category_id"));
                                model.setCategory_name(item.getString("category_name"));
                                model.setPrice(item.getString("price"));
                                // model.setCondition_type(item.getString("condition_type"));
                                // model.setCondition(item.getString("condition"));
                                model.setAge(item.getString("age"));
                                model.setPractice_category_type(item.getString("practice_category_type"));
                                model.setPractice_category_name(item.getString("practice_category_name"));
                                model.setPractice_type(item.getString("practice_type"));
                                model.setPractice_type_name(item.getString("practice_type_name"));
                                model.setRooms(item.getString("rooms"));
                                model.setCountry_code(item.getString("country"));
                                model.setCountry_name(item.getString("country_name"));
                                model.setState_code(item.getString("state"));
                                model.setState_name(item.getString("state_name"));
                                model.setCity_id(item.getString("city"));
                                model.setCity_name(item.getString("city_name"));
                                model.setSpecific_locality(item.getString("specific_locality"));
                                model.setLand_length(item.getString("land_length"));
                                model.setLand_width(item.getString("land_width"));
                                model.setTotal_area(item.getString("total_area"));
                                model.setBuild_up_area(item.getString("build_up_area"));
                                //model.setPrimary_type( item.getString("primary_type"));
                                //model.setPrimary( item.getString("primary"));
                                // model.setLicence(item.getString("licence"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                //model.setSpecification(item.getString("specification"));
                                //model.setDeal_type(item.getString("deal_type"));
                                // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("1");
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER,DatabaseHelper.id, item.getString("id")))
                                {

                                    handler.AddSellBuyMaster(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddSellBuyMaster(model, false);
                                }
                            }
//                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId);
//                        Log.e("menulist.size();","<><><"+arrayList.size());
//                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(MyDealsActivity.this,R.layout.marketplacerow,arrayList);
//                        recycleView.setAdapter(myDealsAdapter);
                            // myDealsAdapter.notifyDataSetChanged();
                        }
                        if(header.has("BuyingProduct")) {
                            JSONArray header3 = header.getJSONArray("BuyingProduct");
                            for(int i = 0;i<header3.length();i++){
                                JSONObject item  = header3.getJSONObject(i);
                                DashBoardProductModel model = new DashBoardProductModel();
                                // model.setId(item.getString("id"));
                                model.setId(item.getString("buying_id"));
                                model.setProduct_id(item.getString("product_id"));
                                model.setProduct_name(item.getString("product_name"));
                                model.setCategory_id(item.getString("category_id"));
                                model.setCategory_name(item.getString("category_name"));
                                model.setPrice(item.getString("price"));
                                // model.setCondition_type(item.getString("condition_type"));
                                // model.setCondition(item.getString("condition"));
                                model.setAge(item.getString("age"));
                                model.setPractice_category_type(item.getString("practice_category_type"));
                                model.setPractice_category_name(item.getString("practice_category_name"));
                                model.setPractice_type(item.getString("practice_type"));
                                model.setPractice_type_name(item.getString("practice_type_name"));
                                model.setRooms(item.getString("rooms"));
                                model.setCountry_code(item.getString("country"));
                                model.setCountry_name(item.getString("country_name"));
                                model.setState_code(item.getString("state"));
                                model.setState_name(item.getString("state_name"));
                                model.setCity_id(item.getString("city"));
                                model.setCity_name(item.getString("city_name"));
                                model.setSpecific_locality(item.getString("specific_locality"));
                                model.setLand_length(item.getString("land_length"));
                                model.setLand_width(item.getString("land_width"));
                                model.setTotal_area(item.getString("total_area"));
                                model.setBuild_up_area(item.getString("build_up_area"));
                                // model.setPrimary_type( item.getString("primary_type"));
                                // model.setPrimary( item.getString("primary"));
                                // model.setLicence(item.getString("licence"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                // model.setSpecification(item.getString("specification"));
                                // model.setDeal_type(item.getString("deal_type"));
                                // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("2");
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER,DatabaseHelper.id, item.getString("buying_id")))
                                {

                                    handler.AddSellBuyMaster(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddSellBuyMaster(model, false);
                                }

                            }
//                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId);
//                        Log.e("menulist.size();","<><><"+arrayList.size());
//                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(MyDealsActivity.this,R.layout.marketplacerow,arrayList);
//                        recycleView.setAdapter(myDealsAdapter);
                            // myDealsAdapter.notifyDataSetChanged();
                        }

                    }else {

                        // MyToast.toastLong(MyDealsActivity.this,res_msg);
                    }
                }
                catch (JSONException e){
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
