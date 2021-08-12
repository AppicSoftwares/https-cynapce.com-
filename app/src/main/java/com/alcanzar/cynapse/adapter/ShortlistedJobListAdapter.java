package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.ShortlistedCandidateDetailsActivity;
import com.alcanzar.cynapse.model.JobMasterModel;

import java.util.ArrayList;

public class ShortlistedJobListAdapter extends RecyclerView.Adapter<ShortlistedJobListAdapter.ShortlistedJobViewHolder> {

   private int Rowlayout;
   private Context context;
   private ArrayList<JobMasterModel> arrayList;

    public ShortlistedJobListAdapter(Context context, int Rowlayout, ArrayList<JobMasterModel> arrayList)
    {
        this.context = context;
        this.Rowlayout = Rowlayout;
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ShortlistedJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(Rowlayout,parent,false);
        return new ShortlistedJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShortlistedJobViewHolder holder, int position) {
   // holder.name.setText(arrayList.get(position).getSpecialization());
            //or
        holder.name.setText(arrayList.get(position).getName());
    holder.title.setText(arrayList.get(position).getJob_title());
    holder.medical_profile.setText(arrayList.get(position).getMedical_profile_name());
    holder.posted_on.setText(String.format("Posted On : %s", arrayList.get(position).getModify_date()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ShortlistedJobViewHolder extends RecyclerView.ViewHolder {
    TextView name,title,medical_profile,posted_on;
        ShortlistedJobViewHolder(View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.name);
            title =itemView.findViewById(R.id.title);
            medical_profile =itemView.findViewById(R.id.medical_profile);
            posted_on =itemView.findViewById(R.id.posted_on);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ShortlistedCandidateDetailsActivity.class);
                    context.startActivity(i);
                }
            });
        }
    }
}
