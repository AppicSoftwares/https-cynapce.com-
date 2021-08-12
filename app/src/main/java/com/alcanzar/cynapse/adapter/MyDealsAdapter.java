package com.alcanzar.cynapse.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MarketPlaceActivity;
import com.alcanzar.cynapse.api.Delete_all_dataApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyDealsAdapter extends RecyclerView.Adapter<MyDealsAdapter.MyDealsViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<DashBoardProductModel> arrayList;
    String deal_type;
    DatabaseHelper handler;
    Dialog dialogSociallog;

    public MyDealsAdapter(Context context, int rowLayout, ArrayList<DashBoardProductModel> arrayList, String deal_type) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        this.deal_type = deal_type;
        handler = new DatabaseHelper(context);
    }

    public class MyDealsViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, quantity, brok_unbrok_txt;
        Button btnBuy;
        ImageView topImage, delete_deals;

        public MyDealsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            topImage = itemView.findViewById(R.id.topImage);
            delete_deals = itemView.findViewById(R.id.delete_deals);
            quantity = itemView.findViewById(R.id.quantity);
            brok_unbrok_txt = itemView.findViewById(R.id.brok_unbrok_txt);
            btnBuy = itemView.findViewById(R.id.btnBuy);
            btnBuy.setVisibility(View.GONE);
            brok_unbrok_txt.setVisibility(View.VISIBLE);

            delete_deals.setVisibility(View.VISIBLE);

            delete_deals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSocialDialog(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MarketPlaceActivity.class);
                    intent.putExtra("prod_id", arrayList.get(getAdapterPosition()).getProduct_id());
                    intent.putExtra("cat_id", arrayList.get(getAdapterPosition()).getCategory_id());
                    intent.putExtra("where", "MyDeails");
                    intent.putExtra("from_noti", "false");
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public MyDealsAdapter.MyDealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return new MyDealsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyDealsAdapter.MyDealsViewHolder holder, int position) {

        if (arrayList.get(position).getCategory_id().equalsIgnoreCase("1")) {
            holder.title.setText(arrayList.get(position).getSpecification());
            holder.brok_unbrok_txt.setText(arrayList.get(position).getCategory_name());
            holder.quantity.setText(String.format("%s sqft", arrayList.get(position).getTotal_area()));
            holder.price.setText(String.format("₹ %s", arrayList.get(position).getPrice()));
        } else if (arrayList.get(position).getCategory_id().equalsIgnoreCase("2")) {
            //holder.title.setText(arrayList.get(position).getSpecific_locality());
            holder.title.setText(arrayList.get(position).getProduct_name());
            holder.brok_unbrok_txt.setText(arrayList.get(position).getCategory_name());
            holder.quantity.setText(String.format("%s sqft", arrayList.get(position).getTotal_area()));
            holder.price.setText(String.format("₹ %s", arrayList.get(position).getPrice()));
        } else if (arrayList.get(position).getCategory_id().equalsIgnoreCase("3")) {
            holder.title.setText(arrayList.get(position).getProduct_name());
            //holder.title.setText(arrayList.get(position).getPrimary_type());
            holder.brok_unbrok_txt.setText(arrayList.get(position).getCategory_name());
            holder.quantity.setText(arrayList.get(position).getPrimary());
            holder.price.setText(arrayList.get(position).getLicence());
        } else if (arrayList.get(position).getCategory_id().equalsIgnoreCase("4")) {
            holder.title.setText(arrayList.get(position).getProduct_name());
            holder.brok_unbrok_txt.setText(arrayList.get(position).getCategory_name());
            holder.quantity.setText(arrayList.get(position).getSpecification());
            holder.price.setText(String.format("₹ %s", arrayList.get(position).getPrice()));
        } else {
            holder.title.setText(arrayList.get(position).getProduct_name());
            holder.brok_unbrok_txt.setText(arrayList.get(position).getCategory_name());
            holder.quantity.setText(arrayList.get(position).getSpecification());
            holder.price.setText(String.format("₹ %s", arrayList.get(position).getPrice()));
        }

//        Glide.with(context)
//                .load(arrayList.get(position).getProduct_image())
//                .placeholder(R.drawable.animation_loading_circle)
//                .error(R.drawable.no_img_placeholder)
//                .into(holder.topImage);

        Picasso.with(context).load(arrayList.get(position).getProduct_image()).error(R.drawable.no_img_placeholder).placeholder(R.drawable.animation_loading_circle).into(holder.topImage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void showSocialDialog(final int adapterPosition) {

        Button btn_Submit;
        TextView reason_txt;
        ImageView cross_btn;
        final EditText edit_desc;
        dialogSociallog = new Dialog(context);
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.delete_enter_reason_dialog);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // dialogSociallog.setCancelable(false);
        //cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        edit_desc = dialogSociallog.findViewById(R.id.edit_desc);
        btn_Submit = dialogSociallog.findViewById(R.id.submit);
        reason_txt = dialogSociallog.findViewById(R.id.reason_txt);
        reason_txt.setText("Please enter the reason for deleting this deal");
        dialogSociallog.show();
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_desc.getText().toString())) {
                    MyToast.toastShort((Activity) context, "Please enter Reason");
                } else if (edit_desc.getText().toString().length() < 10) {
                    MyToast.toastShort((Activity) context, "Reason is too short");
                } else {
                    try {
                        Delete_all_dataApi(arrayList.get(adapterPosition).getProduct_id(), edit_desc.getText().toString(), adapterPosition);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//                dialogSociallog.dismiss();

            }
        });

//        cross_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogSociallog.dismiss();
//            }
//        });

    }

    private void Delete_all_dataApi(String deleted_id, String deleted_reason, final int adapterPosition) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("deleted_type", deal_type);
        params.put("deleted_id", deleted_id);
        params.put("deleted_reason", deleted_reason);

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new Delete_all_dataApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSEFORGOT", response.toString());
                    if (res_code.equals("1")) {
                        dialogSociallog.dismiss();
                        handler.deleteData(arrayList.get(adapterPosition).getProduct_id(), DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER, DatabaseHelper.product_id);
                        try {
                            arrayList.remove(adapterPosition);
//                notifyDataSetChanged();
                            notifyItemRemoved(adapterPosition);
                        } catch (IndexOutOfBoundsException iob) {
                            iob.printStackTrace();
                        }
                        MyToast.toastLong((Activity) context, res_msg);

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

    @Override
    public int getItemViewType(int position) {

        Log.e("CheckImageData", "checkUrl=" + arrayList.get(position).getProduct_image().indexOf(".", 20));
        if (arrayList.get(position).getProduct_image().indexOf(".", 20) != -1) {
            return rowLayout;

        } else {
            return R.layout.marketplacerow_noimage;
        }

    }
}
