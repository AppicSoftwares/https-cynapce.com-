package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.JobDetailsActivity;
import com.alcanzar.cynapse.model.JobPaymentModel;

import java.util.ArrayList;

public class PaymentJobAdapter extends RecyclerView.Adapter<PaymentJobAdapter.RequestPostJobViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<JobPaymentModel> arrayList;
    private String jobApply="",jobPlan="",job_title_id="",medical_profile_id="";

    public PaymentJobAdapter(Context context, int rowLayout, ArrayList<JobPaymentModel> arrayList, String jobApply, String jobPlan, String job_title_id, String medical_profile_id) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        this.jobPlan=jobPlan;
        this.job_title_id=job_title_id;
        this.medical_profile_id=medical_profile_id;
        Log.d("SADIROKLMD",job_title_id+"<><"+medical_profile_id);
        this.jobApply = jobApply;
    }

    public class RequestPostJobViewHolder extends RecyclerView.ViewHolder {
        TextView title, txt_jobpay, jobdetails, txt_applyjob;

        public RequestPostJobViewHolder(View itemView) {
            super(itemView);
            // title = itemView.findViewById(R.id.title);
            txt_jobpay = itemView.findViewById(R.id.txt_jobpay);
            jobdetails = itemView.findViewById(R.id.jobdetails);
            txt_applyjob = itemView.findViewById(R.id.txt_applyjob);


            Log.d("JIDOKKIKD444",job_title_id+"<><<"+medical_profile_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, JobDetailsActivity.class);
                    intent.putExtra("jobApply",jobPlan);
                    intent.putExtra("price",arrayList.get(getAdapterPosition()).getJob_pay());
                    intent.putExtra("job_id",arrayList.get(getAdapterPosition()).getJob_id_());
                    intent.putExtra("details",arrayList.get(getAdapterPosition()).getJob_details());
                    intent.putExtra("plan_type",arrayList.get(getAdapterPosition()).getJob_type());
                    intent.putExtra("job_title_id", job_title_id);
                    intent.putExtra("medical_profile_id", medical_profile_id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public PaymentJobAdapter.RequestPostJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new RequestPostJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentJobAdapter.RequestPostJobViewHolder holder, int position) {

        Log.d("JIDOKKIKDK22",arrayList.get(position).getJob_details());

        Log.d("JIDOKKIKDK333",arrayList.get(position).getJob_pay());
        Log.d("JIDOKKIKD444",jobApply);
        if (jobPlan.equalsIgnoreCase("1")) {
            holder.txt_applyjob.setText(" Apply " + arrayList.get(position).getJob_details() + " "+"job(s)");
        } else {
            holder.txt_applyjob.setText(" Shortlist " + arrayList.get(position).getJob_details() + " "+ "job(s)");

        }

        holder.txt_jobpay.setText(" Rs "+""+arrayList.get(position).getJob_pay());
//       holder.years.setText(arrayList.get(position).getYear_of_experience());
//       holder.address.setText(arrayList.get(position).getPreferred_location());
//       holder.tags.setText(arrayList.get(position).getJob_description());
    }

    @Override
    public int getItemCount() {


        return arrayList.size();
    }
}
