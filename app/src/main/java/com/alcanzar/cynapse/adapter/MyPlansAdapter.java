package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.JobMasterModel;

import java.util.ArrayList;

public class MyPlansAdapter extends RecyclerView.Adapter<MyPlansAdapter.MyViewHolder> {

    Context context;
    ArrayList<JobMasterModel> cityListMain;


    public MyPlansAdapter(Context context, ArrayList<JobMasterModel> cityListMain) {
        this.context = context;
        this.cityListMain = cityListMain;

    }

    @NonNull
    @Override
    public MyPlansAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_plans_row, parent, false);
        return new MyPlansAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlansAdapter.MyViewHolder holder, int position) {


            holder.medical_prof.setText("Medical Profile : "+cityListMain.get(position).getMedical_profile_name());
            holder.title.setText("Title : "+cityListMain.get(position).getJob_title());
            holder.total_jobs.setText("Jobs Left : "+cityListMain.get(position).getJob_type());
            if(cityListMain.get(position).getJob_type_name().equalsIgnoreCase("1"))
            {
                holder.package_type.setText("Package Type : "+"Applicant");
            }else
            {
                holder.package_type.setText("Package Type : "+"Recruiter");
            }





    }

    @Override
    public int getItemCount() {

        return cityListMain.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView medical_prof,title,total_jobs,package_type;


        public MyViewHolder(View itemView) {
            super(itemView);
            medical_prof = itemView.findViewById(R.id.medical_prof);
            title = itemView.findViewById(R.id.title);
            total_jobs = itemView.findViewById(R.id.total_jobs);
            package_type = itemView.findViewById(R.id.package_type);

        }
    }
}
