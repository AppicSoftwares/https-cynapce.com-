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
import com.alcanzar.cynapse.activity.JobDetailActivity;
import com.alcanzar.cynapse.model.JobMasterModel;

import java.util.ArrayList;

public class RecommendedAppliedJobAdapter extends RecyclerView.Adapter<RecommendedAppliedJobAdapter.RequestPostJobViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<JobMasterModel> arrayList;
    private String recommend = "";

    public RecommendedAppliedJobAdapter(Context context, int rowLayout, ArrayList<JobMasterModel> arrayList, String recommend) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        this.recommend = recommend;
    }

    public class RequestPostJobViewHolder extends RecyclerView.ViewHolder {
        TextView title, hospitalName, years, address, tags;
        ImageView share_in_recommended;

        public RequestPostJobViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            years = itemView.findViewById(R.id.years);
            address = itemView.findViewById(R.id.address);
            tags = itemView.findViewById(R.id.tags);
            share_in_recommended = itemView.findViewById(R.id.share_in_recommended);
            share_in_recommended.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetailActivity.class);
                    intent.putExtra("id", arrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("recommend", recommend);
                   // intent.putExtra("id", arrayList.get(getAdapterPosition()).getId());
                   // intent.putExtra("recommend", recommend);
                    intent.putExtra("medical_profile_id", arrayList.get(getAdapterPosition()).getMedical_profile_id());
                    intent.putExtra("job_title_id", arrayList.get(getAdapterPosition()).getJob_title_id());
                    intent.putExtra("job_id", arrayList.get(getAdapterPosition()).getJob_id());
                    Log.d("JOGITITLLTLTED",arrayList.get(getAdapterPosition()).getJob_title_id());
//                        intent.putExtra("cat_id",arrayList.get(getAdapterPosition()).getProduct_category_id());
//                        intent.putExtra("from_noti","true");
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }

    @Override
    public RequestPostJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new RequestPostJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestPostJobViewHolder holder, int position) {
        if (arrayList.get(position).getMedical_profile_id().equalsIgnoreCase("1") || arrayList.get(position).getMedical_profile_id().equalsIgnoreCase("4")) {
            holder.title.setText(String.format("%s/%s", arrayList.get(position).getSpecialization_name(), arrayList.get(position).getSub_specialization_name()));
        } else {
            holder.title.setText(arrayList.get(position).getDepartment_name());
        }

        holder.hospitalName.setText(arrayList.get(position).getJob_title());
        holder.years.setText(String.format(" Years of Experience : %s", arrayList.get(position).getYear_of_experience()));
        holder.address.setText(String.format(" Address : %s", arrayList.get(position).getLocation()));
        holder.tags.setText(String.format(" Job Description : %s", arrayList.get(position).getJob_description()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
