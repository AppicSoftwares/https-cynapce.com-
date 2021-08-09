package com.alcanzar.cynapse.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
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
import com.alcanzar.cynapse.api.BuyingProductRequestApi;
import com.alcanzar.cynapse.api.ImInterestedApi;
import com.alcanzar.cynapse.fragments.MarketPlaceFragment;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alcanzar on 10/04/18.
 */

public class MarketPlaceProductAdapter extends RecyclerView.Adapter<MarketPlaceProductAdapter.MyDealsViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<DashBoardProductModel> arrayList;

    private static int count = 0;


    public MarketPlaceProductAdapter(Context context, int rowLayout, ArrayList<DashBoardProductModel> arrayList) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
    }

    class MyDealsViewHolder extends RecyclerView.ViewHolder {
        TextView title, brok_unbrok_txt, price, quantity, category_type, cate_spaslization;
        ImageView topImage;
        Button btnBuy, btn_im_intersted, btnRent;

        MyDealsViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            brok_unbrok_txt = itemView.findViewById(R.id.brok_unbrok_txt);
            topImage = itemView.findViewById(R.id.topImage);
            quantity = itemView.findViewById(R.id.quantity);
            cate_spaslization = itemView.findViewById(R.id.cate_spaslization);
            category_type = itemView.findViewById(R.id.category_type);
            btnBuy = itemView.findViewById(R.id.btnBuy);
            btn_im_intersted = itemView.findViewById(R.id.btn_im_intersted);
            btnRent = itemView.findViewById(R.id.btnRent);


            if (MarketPlaceFragment.type ==2)
                quantity.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MarketPlaceActivity.class);
                    intent.putExtra("prod_id", arrayList.get(getAdapterPosition()).getProduct_id());
                    intent.putExtra("cat_id", arrayList.get(getAdapterPosition()).getCategory_id());
                    intent.putExtra("from_noti", "false");
                    context.startActivity(intent);
                }
            });

            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(btnBuy.getText().toString().equalsIgnoreCase("TIE-UP"))
//                    {

                    if (Util.isVerifiedProfile((Activity) context)) {
                        if (AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                                AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                            //b = false;
                            //showDialog(activity);
                            try {
                                if (Util.isVerifiyEMailPHoneNO((Activity) context)) {


                                    try {
                                        BuyingProductRequestApi(btnBuy, btn_im_intersted, btnRent, arrayList.get(getAdapterPosition()).getProduct_id(), "");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                BuyingProductRequestApi(btnBuy, btn_im_intersted, btnRent, arrayList.get(getAdapterPosition()).getProduct_id(), "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                    }else
//                    {
//                        showDialogQty(arrayList.get(getAdapterPosition()).getProduct_id(),arrayList.get(getAdapterPosition()).getDeal_type());
//                    }
                    }

                }
            });

            btnRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(btnRent.getText().toString().equalsIgnoreCase("TIE-UP"))
//                    {


                    if (Util.isVerifiedProfile((Activity) context)) {

                        if (AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                                AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                            //b = false;
                            //showDialog(activity);
                            try {
                                if (Util.isVerifiyEMailPHoneNO((Activity) context)) {


                                    try {
                                        BuyingProductRequestApi(btnBuy, btn_im_intersted, btnRent, arrayList.get(getAdapterPosition()).getProduct_id(), "");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        else {

                            try {
                                BuyingProductRequestApi(btnBuy, btn_im_intersted, btnRent, arrayList.get(getAdapterPosition()).getProduct_id(), "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }


//                    }else
//                    {
//                        showDialogQty(arrayList.get(getAdapterPosition()).getProduct_id(),arrayList.get(getAdapterPosition()).getDeal_type());
//                    }

                }
            });
            btn_im_intersted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Util.isVerifiedProfile((Activity) context)) {

                        if (AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                                AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                            //b = false;
                            //showDialog(activity);
                            try {
                                if (Util.isVerifiyEMailPHoneNO((Activity) context)) {


                                    try {
                                        ImInterestedApi(arrayList.get(getAdapterPosition()).getProduct_id());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                ImInterestedApi(arrayList.get(getAdapterPosition()).getProduct_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            });
        }
    }

    @Override
    public MyDealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(viewType, parent, false);

        return new MyDealsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyDealsViewHolder holder, int position) {
        if (arrayList.get(position).getCategory_id().equalsIgnoreCase("1")) {
            holder.category_type.setText(arrayList.get(position).getCategory_name());
            holder.title.setText(arrayList.get(position).getFeatured_product());
            holder.brok_unbrok_txt.setText(arrayList.get(position).getDeal_type());
            holder.quantity.setText(String.format("%s sqft", arrayList.get(position).getTotal_area()));
            holder.price.setText(String.format("₹ %s", arrayList.get(position).getPrice()));
            holder.cate_spaslization.setText(arrayList.get(position).getSpecialization_name());
            if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("1")) {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.VISIBLE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("2")) {
                holder.btnBuy.setVisibility(View.VISIBLE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.VISIBLE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            }

        } else if (arrayList.get(position).getCategory_id().equalsIgnoreCase("2")) {
            holder.title.setText(arrayList.get(position).getProduct_name());
            holder.category_type.setText(arrayList.get(position).getCategory_name());
            holder.brok_unbrok_txt.setText(arrayList.get(position).getDeal_type());
            holder.quantity.setText(String.format("%s sqft", arrayList.get(position).getTotal_area()));
            holder.price.setText(String.format("₹ %s", arrayList.get(position).getPrice()));
            holder.cate_spaslization.setText(arrayList.get(position).getSpecialization_name());
            if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("1")) {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.VISIBLE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("2")) {
                holder.btnBuy.setVisibility(View.VISIBLE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.VISIBLE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            }
        } else if (arrayList.get(position).getCategory_id().equalsIgnoreCase("3")) {
            holder.title.setText(arrayList.get(position).getCondition());
            holder.category_type.setText(arrayList.get(position).getCategory_name());
            // holder.brok_unbrok_txt.setText(arrayList.get(position).getDeal_type());
//            holder.quantity.setText(arrayList.get(position).getCondition_type());
            holder.price.setText(arrayList.get(position).getFeatured_product_type());
            holder.cate_spaslization.setText(arrayList.get(position).getSpecialization_name());
            if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("1")) {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.VISIBLE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("2")) {
                holder.btnBuy.setVisibility(View.VISIBLE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.VISIBLE);
                holder.btnBuy.setText(R.string.tie_up);
                holder.btn_im_intersted.setText(R.string.tie_up);
                holder.btnRent.setText(R.string.tie_up);
            }

        } else if (arrayList.get(position).getCategory_id().equalsIgnoreCase("4")) {
            holder.title.setText(arrayList.get(position).getProduct_name());
            holder.category_type.setText(arrayList.get(position).getCategory_name());
            holder.brok_unbrok_txt.setText(arrayList.get(position).getDeal_type());
            holder.quantity.setText(arrayList.get(position).getSpecification());
            holder.price.setText(String.format("₹ %s", arrayList.get(position).getPrice()));
            holder.cate_spaslization.setText(arrayList.get(position).getSpecialization_name());
            if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("1")) {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.VISIBLE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("2")) {
                holder.btnBuy.setVisibility(View.VISIBLE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.VISIBLE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            }
        } else {
            holder.title.setText(arrayList.get(position).getProduct_name());
            holder.category_type.setText("Books & Others");
            holder.brok_unbrok_txt.setText(arrayList.get(position).getDeal_type());
            holder.quantity.setText(arrayList.get(position).getSpecification());
            holder.price.setText(String.format("₹ %s", arrayList.get(position).getPrice()));
            holder.cate_spaslization.setText(arrayList.get(position).getSpecialization_name());
            if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("1")) {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.VISIBLE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("2")) {
                holder.btnBuy.setVisibility(View.VISIBLE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.GONE);
                holder.btnBuy.setText("I'M INTERESTED TO BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            } else {
                holder.btnBuy.setVisibility(View.GONE);
                holder.btn_im_intersted.setVisibility(View.GONE);
                holder.btnRent.setVisibility(View.VISIBLE);
                holder.btnBuy.setText("BUY");
                holder.btn_im_intersted.setText("I'M INTERESTED TO SELL");
                holder.btnRent.setText("I'M INTERESTED TO LEASE");
            }
        }
//        if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("1")) {
//            holder.btnBuy.setVisibility(View.GONE);
//            holder.btn_im_intersted.setVisibility(View.VISIBLE);
//            holder.btnRent.setVisibility(View.GONE);
//            holder.btnBuy.setText("BUY");
//            holder.btn_im_intersted.setText("I'M INTERESTED");
//            holder.btnRent.setText("LEASE");
//        } else if (arrayList.get(position).getPractice_category_type().equalsIgnoreCase("2")) {
//            holder.btnBuy.setVisibility(View.VISIBLE);
//            holder.btn_im_intersted.setVisibility(View.GONE);
//            holder.btnRent.setVisibility(View.GONE);
//            holder.btnBuy.setText("BUY");
//            holder.btn_im_intersted.setText("I'M INTERESTED");
//            holder.btnRent.setText("LEASE");
//        } else {
//            holder.btnBuy.setVisibility(View.GONE);
//            holder.btn_im_intersted.setVisibility(View.GONE);
//            holder.btnRent.setVisibility(View.VISIBLE);
//            holder.btnBuy.setText("BUY");
//            holder.btn_im_intersted.setText("I'M INTERESTED");
//            holder.btnRent.setText("LEASE");
//        }


//        Glide.with(context)
//                .load(arrayList.get(position).getProduct_image())
//                .placeholder(R.drawable.animation_loading_circle)
//                .error(R.drawable.no_img_placeholder)
//                .into(holder.topImage);

        Picasso.with(context).load(arrayList.get(position).getProduct_image()).placeholder(R.drawable.animation_loading_circle).error(R.drawable.no_img_placeholder).into(holder.topImage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void showDialogQty(final String product_id, final String qty) {

        Button yes;
        final EditText edit_qty;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.enter_quantity_dialog);
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        yes = dialog.findViewById(R.id.yes);
        edit_qty = dialog.findViewById(R.id.edit_qty);

        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edit_qty.getText().toString())) {

                    if (Integer.parseInt(edit_qty.getText().toString()) > Integer.parseInt(qty)) {
                        MyToast.toastShort((Activity) context, "Available quantity is " + qty + " !");
                    } else {
//                        try {
//                            BuyingProductRequestApi(btnBuy, btn_im_intersted, btnRent, product_id, edit_qty.getText().toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        dialog.dismiss();
                    }

                } else {
                    MyToast.toastShort((Activity) context, "Please enter Quantity");
                }


            }
        });
    }

    private void BuyingProductRequestApi(final Button btnBuy, final Button btn_im_intersted, final Button btnRent, final String product_id, String s) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("product_id", product_id);
        params.put("quantity", s);
        // params.put("sync_time", AppCustomPreferenceClass.readString(context,AppCustomPreferenceClass.sync_time,""));
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new BuyingProductRequestApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(context, AppCustomPreferenceClass.sync_time, sync_time);

                    if (res_code.equals("1")) {
                        // MyToast.toastLong((Activity) context,res_msg);
                        JSONObject item = header.getJSONObject("BuyingProductRequest");
                        DashBoardProductModel model = new DashBoardProductModel();
                        model.setProduct_id(item.getString("uuid"));
                        model.setProduct_name(item.getString("type_id"));
                        model.setCategory_id(item.getString("product_id"));
                        model.setCategory_name(item.getString("status"));
                        model.setPrice(item.getString("add_date"));
                        model.setCondition_type(item.getString("modify_date"));
                        showDialog(btnBuy, btn_im_intersted, btnRent);
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

    private void ImInterestedApi(final String product_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("interested_uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("product_id", product_id);
        // params.put("sync_time", AppCustomPreferenceClass.readString(context,AppCustomPreferenceClass.sync_time,""));
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new ImInterestedApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    // AppCustomPreferenceClass.writeString(context,AppCustomPreferenceClass.sync_time,sync_time);

                    if (res_code.equals("1")) {
                        MyToast.toastLong((Activity) context, res_msg);
                        JSONObject item = header.getJSONObject("Interested");
                        DashBoardProductModel model = new DashBoardProductModel();
                        model.setProduct_id(item.getString("uuid"));
                        model.setProduct_name(item.getString("message"));
                        model.setCategory_id(item.getString("product_id"));
                        model.setCategory_id(item.getString("msg_type"));
                        model.setCategory_name(item.getString("status"));
                        model.setPrice(item.getString("add_date"));
                        model.setCondition_type(item.getString("modify_date"));
                        //MyToast.toastLong((Activity) context,res_msg);
                        //showDialog();
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

    public void showDialog(Button buy, Button btn_im_intersted, Button btnRent) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_msg_alert);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing different views for the dialog
        TextView title = dialog.findViewById(R.id.title);
        TextView msg = dialog.findViewById(R.id.msg);
        ImageView close = dialog.findViewById(R.id.close);
        Button btnGotIt = dialog.findViewById(R.id.btnGotIt);
        //TODO :setting different views
        title.setText(R.string.sent);
        //msg.setText(R.string.request_sent);
        if (buy.getVisibility() == View.VISIBLE) {
            msg.setText(R.string.buy_req);

        } else if (btn_im_intersted.getVisibility() == View.VISIBLE) {
            msg.setText(R.string.buy_req);
        } else {
            if (btnRent.getText().toString().equalsIgnoreCase("LEASE")) {
                msg.setText(R.string.lease_req);
            } else {
                msg.setText(R.string.tieUp_req);
            }

        }

        dialog.show();
        //TODO : dismiss the on btn click and close click
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO : finishing the activity
                //finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //finish();
            }
        });
    }
}
