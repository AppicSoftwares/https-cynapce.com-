package com.alcanzar.cynapse.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.utils.AppConstantClass;

import java.util.ArrayList;

public class SubCityListAdapter extends RecyclerView.Adapter<SubCityListAdapter.MyViewHolder> {
    Context context;
    ArrayList<CityModel> cityList;


    public SubCityListAdapter(Context context, ArrayList<CityModel> cityList) {
        this.context = context;
        this.cityList = cityList;

    }
//
//    @Override
//    public int getCount() {
//        Log.e("ASDvwv",cityList.size()+"");
//        return cityList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return cityList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//
//    public class Holder
//    {
//        TextView stateNameId,city_name;
//        CheckBox checkbox;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_city_row_diloag,parent,false);
//         Holder holder=new Holder();
//         holder.city_name=convertView.findViewById(R.id.city_name);
//
//         holder.city_name.setText(cityList.get(position).getCity_name());
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_city_row_diloag,parent,false);
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//        holder.city_name.setText(cityList.get(position).getCity_name());
//    }
//
//    @Override
//    public int getItemCount() {
//        return cityList.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView stateNameId,city_name;
//        CheckBox checkbox;
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            city_name=itemView.findViewById(R.id.city_name);
//            checkbox=itemView.findViewById(R.id.checkbox);
//        }
//    }
////            checkbox=itemView.findViewById(R.id.checkbox);
//
//        return convertView;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_city_row_diloag, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.city_name.setText(cityList.get(position).getCity_name());
        Log.d("CITYIDWILLLLLL111", cityList.get(position).getCity_id());
        if(cityList.get(position).isSelectedCheck())
            holder.checkbox.setChecked(true);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Fbdvsbvfrb",cityList.get(position).getCity_name()+""+cityList.get(position).getCity_id());


                if((cityList.get(position).isSelectedCheck()))
                {
                    cityList.get(position).setSelectedCheck(false);
                    holder.checkbox.setChecked(false);
                    AppConstantClass.cityListFillData=cityList;
                    Log.e("dvadvadvddv", AppConstantClass.cityListFillData.get(position).isSelectedCheck()+"");
                }
                else
                {
                    cityList.get(position).setSelectedCheck(true);
                    holder.checkbox.setChecked(true);
                    AppConstantClass.cityListFillData=cityList;
                    Log.e("dadgdvadsvva", AppConstantClass.cityListFillData.get(position).isSelectedCheck()+"");

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView stateNameId, city_name;
        CheckBox checkbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            city_name = itemView.findViewById(R.id.city_name);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
