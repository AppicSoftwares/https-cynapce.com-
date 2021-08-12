package com.alcanzar.cynapse.adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.ConferencePackageModel;

import java.util.ArrayList;

public class ConferenceChargeAdapter extends RecyclerView.Adapter<ConferenceChargeAdapter.ConferenceChargeViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<ConferencePackageModel>  arrayList;
    //TODO : this is the adapter constructor class
    public ConferenceChargeAdapter(Context context, int rowLayout, ArrayList<ConferencePackageModel> arrayList){
        this.context =context;
        this.rowLayout =rowLayout;
        this.arrayList =arrayList;
    }
    //TODO : this is used to contain all the views and here views initialization are done
    public class ConferenceChargeViewHolder extends RecyclerView.ViewHolder{
        TextView txt_pack_day,txt_pack_charg;

        public ConferenceChargeViewHolder(View itemView) {
            super(itemView);
            txt_pack_day = itemView.findViewById(R.id.txt_pack_day);
            txt_pack_charg = itemView.findViewById(R.id.txt_pack_charg);
        }
    }
    //TODO :  this is used to assign the layout to the recycleView on create
    @Override
    public ConferenceChargeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout,parent,false);
        return new ConferenceChargeViewHolder(view);
    }
    //TODO : used to display the data for the specific views included in the layout
    @Override
    public void onBindViewHolder(ConferenceChargeViewHolder holder, int position) {

       holder.txt_pack_day.setText(arrayList.get(position).getConference_pack_day());
       holder.txt_pack_charg.setText(arrayList.get(position).getConference_pack_charge());
    }
    //TODO : this will give the size of the list which is displayed
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
