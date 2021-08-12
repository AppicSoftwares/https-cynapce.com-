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
import com.alcanzar.cynapse.activity.PostedJobDetailsActivity;
import com.alcanzar.cynapse.activity.ShortlistedJobListActivity;
import com.alcanzar.cynapse.api.Delete_all_dataApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestPostJobAdapter extends RecyclerView.Adapter<RequestPostJobAdapter.RequestPostJobViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<JobMasterModel> arrayList;
    private String job_type;
    DatabaseHelper handler;
    Dialog dialogSociallog;

    public RequestPostJobAdapter(Context context, int rowLayout, ArrayList<JobMasterModel> arrayList, String job_type) {

        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        this.job_type = job_type;
        handler = new DatabaseHelper(context);
    }

    public class RequestPostJobViewHolder extends RecyclerView.ViewHolder {
        TextView title, hospitalName, years, address, tags ,timeAgo;
        ImageView delete_jobs;

        public RequestPostJobViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            years = itemView.findViewById(R.id.years);
            address = itemView.findViewById(R.id.address);
            tags = itemView.findViewById(R.id.tags);
            timeAgo = itemView.findViewById(R.id.timeAgo);

            delete_jobs = itemView.findViewById(R.id.delete_jobs);
            delete_jobs.setVisibility(View.VISIBLE);

            delete_jobs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSocialDialog(getAdapterPosition());
                }
            });

            if (job_type.equalsIgnoreCase("2")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ShortlistedJobListActivity.class);
                        i.putExtra("job_id", arrayList.get(getAdapterPosition()).getJob_id());
                        i.putExtra("specialization", arrayList.get(getAdapterPosition()).getSpecialization_name());
                        i.putExtra("subSpecialization", arrayList.get(getAdapterPosition()).getSub_specialization_name());
                        i.putExtra("department", arrayList.get(getAdapterPosition()).getDepartment_name());
                        i.putExtra("medical_profile_name", arrayList.get(getAdapterPosition()).getMedical_profile_name());
                        context.startActivity(i);
                    }
                });
            }

            if (job_type.equalsIgnoreCase("1")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, PostedJobDetailsActivity.class);
                        i.putExtra("job_id", arrayList.get(getAdapterPosition()).getJob_id());
                        i.putExtra("specialization", arrayList.get(getAdapterPosition()).getSpecialization_name());
                        i.putExtra("subSpecialization", arrayList.get(getAdapterPosition()).getSub_specialization_name());
                        i.putExtra("department", arrayList.get(getAdapterPosition()).getDepartment_name());
                        i.putExtra("medical_profile_name", arrayList.get(getAdapterPosition()).getMedical_profile_name());
                        i.putExtra("id", arrayList.get(getAdapterPosition()).getId());
                        i.putExtra("fromWhere", "RequestPostJobAdapter");
                        context.startActivity(i);
                    }
                });
            }
        }
    }

    @Override
    public RequestPostJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new RequestPostJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestPostJobViewHolder holder, int position) {
        if (arrayList.get(position).getMedical_profile_id().equalsIgnoreCase("1")) {
            holder.title.setText(String.format("%s/%s", arrayList.get(position).getSpecialization_name(), arrayList.get(position).getSub_specialization_name()));
        } else {
            holder.title.setText(arrayList.get(position).getSub_specialization_name());
        }

        holder.hospitalName.setText(arrayList.get(position).getJob_title());
        holder.years.setText(arrayList.get(position).getYear_of_experience());
        holder.address.setText(arrayList.get(position).getLocation());
        holder.tags.setText(arrayList.get(position).getJob_description());
        holder.timeAgo.setText(arrayList.get(position).getAdd_date());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void showSocialDialog(final int adapterPosition) {
        Button btn_Submit;
        ImageView cross_btn;
        final EditText edit_desc;
        TextView reason_txt;
        dialogSociallog = new Dialog(context);
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.delete_enter_reason_dialog);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // dialogSociallog.setCancelable(false);
        //cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        btn_Submit = dialogSociallog.findViewById(R.id.submit);
        edit_desc = dialogSociallog.findViewById(R.id.edit_desc);
        reason_txt = dialogSociallog.findViewById(R.id.reason_txt);
        reason_txt.setText(R.string.delete_reason);

        dialogSociallog.show();
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_desc.getText().toString())) {
                    MyToast.toastShort((Activity) context, context.getString(R.string.pls_enter_reason));
                } else if (edit_desc.getText().toString().length() < 10) {
                    MyToast.toastShort((Activity) context, "Reason is too short");
                } else {
                    try {
                        Delete_all_dataApi(arrayList.get(adapterPosition).getJob_id(), edit_desc.getText().toString(), adapterPosition);
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
        params.put("deleted_type", job_type);
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
                        try {
                            handler.deleteData(arrayList.get(adapterPosition).getJob_id(), DatabaseHelper.TABLE_JOBS_MASTER, DatabaseHelper.KEY_JOB_ID);

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
}
