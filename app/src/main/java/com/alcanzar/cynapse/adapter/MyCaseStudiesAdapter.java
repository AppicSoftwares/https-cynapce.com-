package com.alcanzar.cynapse.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.CaseStudyAnswerActivity;
import com.alcanzar.cynapse.activity.CaseStudyDetailsActivity;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.model.CaseStudyListModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCaseStudiesAdapter extends RecyclerView.Adapter<MyCaseStudiesAdapter.MyCaseStudiesViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<CaseStudyListModel> arrayList;
    private String from_attend = "";

    public MyCaseStudiesAdapter(Context context, int rowLayout, ArrayList<CaseStudyListModel> arrayList, String from_attend) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        this.from_attend = from_attend;
    }

    public class MyCaseStudiesViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView shareImage, verifiedImage;

        public MyCaseStudiesViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            shareImage = itemView.findViewById(R.id.shareImage);
            verifiedImage = itemView.findViewById(R.id.verifiedImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
//                        GetProfileShowDialogApi(getAdapterPosition());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    if (from_attend.equalsIgnoreCase("from_attend"))

                    {
                        Intent intent = new Intent(context, CaseStudyAnswerActivity.class);
                        intent.putExtra("id", arrayList.get(getAdapterPosition()).getId());
                        intent.putExtra("case_type", arrayList.get(getAdapterPosition()).getCase_type());
                        intent.putExtra("case_name", arrayList.get(getAdapterPosition()).getCase_name());
                        intent.putExtra("tot_ques", arrayList.get(getAdapterPosition()).getTotal_questions());
                        intent.putExtra("tot_attempt", arrayList.get(getAdapterPosition()).getTotal_attempted_ques());
                        context.startActivity(intent);
                    } else {
                        if (arrayList.get(getAdapterPosition()).getAttend_status().equalsIgnoreCase("0")) {
                            Intent intent = new Intent(context, CaseStudyDetailsActivity.class);
                            intent.putExtra("id", arrayList.get(getAdapterPosition()).getId());
                            context.startActivity(intent);
                        } else {
                            MyToast.toastShort((Activity) context, "You have already attended this Case.");
                        }

                    }

                }
            });
            shareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Case Name: " + arrayList.get(getAdapterPosition()).getCase_name()
                            + "\n" + "Case Sub Title:" + arrayList.get(getAdapterPosition()).getCase_sub_title()
                            + "\n" + "Case Description:" + arrayList.get(getAdapterPosition()).getDescription());
                    context.startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));

                }
            });
        }
    }

    @Override
    public MyCaseStudiesAdapter.MyCaseStudiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new MyCaseStudiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCaseStudiesAdapter.MyCaseStudiesViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getCase_name());
        holder.description.setText(arrayList.get(position).getCase_sub_title());
        if (arrayList.get(position).getAttend_status().equalsIgnoreCase("0")) {
            holder.verifiedImage.setImageResource(R.drawable.exclaimtionmark);

        } else {
            holder.verifiedImage.setImageResource(R.drawable.green_tick);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void GetProfileShowDialogApi(final int adapterPosition) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(context, header, false) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    if (res_code.equals("1")) {
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");
                        if (item.getString("medical_profile_category_id").equalsIgnoreCase("")
                                || item.getString("title_id").equalsIgnoreCase("")) {
                            showSocialDialog();
                        } else {

                            if (from_attend.equalsIgnoreCase("from_attend"))

                            {
                                Intent intent = new Intent(context, CaseStudyAnswerActivity.class);
                                intent.putExtra("id", arrayList.get(adapterPosition).getId());
                                intent.putExtra("case_type", arrayList.get(adapterPosition).getCase_type());
                                intent.putExtra("case_name", arrayList.get(adapterPosition).getCase_name());
                                intent.putExtra("tot_ques", arrayList.get(adapterPosition).getTotal_questions());
                                intent.putExtra("tot_attempt", arrayList.get(adapterPosition).getTotal_attempted_ques());
                                context.startActivity(intent);
                            } else {
                                if (arrayList.get(adapterPosition).getAttend_status().equalsIgnoreCase("0")) {
                                    Intent intent = new Intent(context, CaseStudyDetailsActivity.class);
                                    intent.putExtra("id", arrayList.get(adapterPosition).getId());
                                    context.startActivity(intent);
                                } else {
                                    MyToast.toastShort((Activity) context, "You have already attended this Case.");
                                }

                            }

                        }
                    } else {

                        //MyToast.toastLong(MainActivity.this,res_msg);
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

    public void showSocialDialog() {
        final Dialog dialogSociallog;
        Button btn_Submit;
        ImageView cross_btn;
        dialogSociallog = new Dialog(context);
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.dialog_social_login_details);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSociallog.setCancelable(false);

        cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        btn_Submit = dialogSociallog.findViewById(R.id.btn_Submit);
        dialogSociallog.show();
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                context.startActivity(intent);
                dialogSociallog.dismiss();

            }
        });

        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSociallog.dismiss();
            }
        });

    }
}
