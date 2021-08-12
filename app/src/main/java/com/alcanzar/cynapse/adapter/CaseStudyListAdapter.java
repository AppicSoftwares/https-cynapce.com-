package com.alcanzar.cynapse.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.JobMasterModel;

import java.util.ArrayList;

public class CaseStudyListAdapter extends RecyclerView.Adapter<CaseStudyListAdapter.RequestPostJobViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<JobMasterModel> arrayList;
    public CaseStudyListAdapter(Context context,int rowLayout,ArrayList<JobMasterModel> arrayList){
        this.context =context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
    }
    public class RequestPostJobViewHolder extends RecyclerView.ViewHolder{
        TextView title,hospitalName,years,address,tags;
        public RequestPostJobViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            years = itemView.findViewById(R.id.years);
            address = itemView.findViewById(R.id.address);
            tags = itemView.findViewById(R.id.tags);
        }
    }
    @Override
    public CaseStudyListAdapter.RequestPostJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout,parent,false);
        return new CaseStudyListAdapter.RequestPostJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CaseStudyListAdapter.RequestPostJobViewHolder holder, int position) {
        if(arrayList.get(position).getMedical_profile_id().equalsIgnoreCase("1"))
        {
            holder.title.setText(String.format("%s/%s", arrayList.get(position).getSpecialization_name(), arrayList.get(position).getSub_specialization_name()));
        }
        else
        {
            holder.title.setText(arrayList.get(position).getSub_specialization_name());
        }

        holder.hospitalName.setText(arrayList.get(position).getJob_title());
        holder.years.setText(arrayList.get(position).getYear_of_experience());
        holder.address.setText(arrayList.get(position).getPreferred_location());
        holder.tags.setText(arrayList.get(position).getJob_description());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

