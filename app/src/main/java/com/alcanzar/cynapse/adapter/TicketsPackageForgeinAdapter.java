package com.alcanzar.cynapse.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;


import java.util.ArrayList;

public class TicketsPackageForgeinAdapter extends RecyclerView.Adapter<TicketsPackageForgeinAdapter.MyViewHolder> {

    public Context context;
    private ArrayList<ConferencePackageModelForgein> packageList;
    boolean flag;
    boolean isTrue  = false;

    public TicketsPackageForgeinAdapter(Context context, ArrayList<ConferencePackageModelForgein> packageList, boolean flag) {
        this.context = context;
        this.packageList = packageList;
        this.flag = flag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tickects_packages_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketsPackageForgeinAdapter.MyViewHolder holder, int position) {


        holder.ticketsname.setText(packageList.get(position).getConference_pack_day());
        holder.ticketsprice.setText("$"+packageList.get(position).getConference_pack_charge());
        holder.priceindicator.setVisibility(View.GONE);
        if (flag) {
            holder.cardticketpackageitems.setBackgroundColor(Color.parseColor("#FFF0EDED"));
            holder.imgarrow.setVisibility(View.VISIBLE);
//            holder.ticketsprice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.priceindicator.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView ticketsname, ticketsprice;
        CardView cardticketpackageitems;
        ImageView imgarrow;
        ImageView priceindicator;

        public MyViewHolder(View view) {
            super(view);
            ticketsname = view.findViewById(R.id.tickectspackagename);
            ticketsprice = view.findViewById(R.id.tickectspackageprice);
            cardticketpackageitems = view.findViewById(R.id.cardticketpackageitems);
            imgarrow = view.findViewById(R.id.imgarrow);
            priceindicator = view.findViewById(R.id.priceindicator);

        }
    }
}
