package com.alcanzar.cynapse.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.CityModel;

import java.util.ArrayList;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ArrayList<CityModel>> cityListMain;
    ArrayList<String> stateName;

    public CityListAdapter(Context context, ArrayList<ArrayList<CityModel>> cityListMain, ArrayList<String> stateName) {
        this.context = context;
        this.cityListMain = cityListMain;
        this.stateName = stateName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_city_dialog, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            ArrayList<CityModel> cityList = cityListMain.get(position);
            holder.stateNameId.setText(stateName.get(position));
            holder.recyclerViewRow.setNestedScrollingEnabled(false);
            holder.recyclerViewRow.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerViewRow.setHasFixedSize(true);
            Log.e("Afwevsc", cityList.get(position).getCity_name());
            SubCityListAdapter subCityListAdapter = new SubCityListAdapter(context, cityList);
            holder.recyclerViewRow.setAdapter(subCityListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return cityListMain.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView stateNameId;
        RecyclerView recyclerViewRow;

        public MyViewHolder(View itemView) {
            super(itemView);
            stateNameId = itemView.findViewById(R.id.stateNameId);
            recyclerViewRow = itemView.findViewById(R.id.recyclerViewRow);

        }
    }
}
