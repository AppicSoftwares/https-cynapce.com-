package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.DashBoardModel;

import java.util.ArrayList;



public class DealersDistAdapters extends RecyclerView.Adapter<DealersDistAdapters.DealersDistViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<DashBoardModel> arrayList;
    public DealersDistAdapters(Context context, int rowLayout, ArrayList<DashBoardModel> arrayList){
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
    }
    public class DealersDistViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        public DealersDistViewHolder(View itemView) {
            super(itemView);
            userName =itemView.findViewById(R.id.userName);
        }
    }
    @Override
    public DealersDistAdapters.DealersDistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout,parent,false);
        return new DealersDistViewHolder(view);
    }
    @Override
    public void onBindViewHolder(DealersDistAdapters.DealersDistViewHolder holder, int position) {
          holder.userName.setText(arrayList.get(position).getId());
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
