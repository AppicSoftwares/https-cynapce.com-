package com.alcanzar.cynapse.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.StateModel;
import com.alcanzar.cynapse.utils.AppConstantClass;

import java.util.ArrayList;

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.MyViewHolder> {
    Context mcontext;


    public static ArrayList<StateModel> courselist = new ArrayList<>();

    public StateListAdapter(ArrayList<StateModel> courselist, Context mcontext) {

        //this.stateListInterface = stateListInterface;
        this.courselist = courselist;
        this.mcontext = mcontext;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_city_selection_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.city_name.setText(courselist.get(position).getState_name());
//        if ((courselist.get(position).isSelectedCheck())) {
//            courselist.get(position).setSelectedCheck(true);
//
//        }
        holder.checkbox.setChecked(courselist.get(position).isSelectedCheck());
        holder.checkbox.setTag(position);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.checkbox.getTag();

                if ((courselist.get(pos).isSelectedCheck())) {
                    courselist.get(pos).setSelectedCheck(false);

                    AppConstantClass.statelistMain = courselist;
                    Log.e("dvadvadv", AppConstantClass.statelistMain.get(pos).isSelectedCheck() + "");
                } else {
                    courselist.get(pos).setSelectedCheck(true);
                    AppConstantClass.statelistMain = courselist;
                    Log.e("dadgva", AppConstantClass.statelistMain.get(pos).isSelectedCheck() + "");

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return courselist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView city_name;
        CheckBox checkbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            city_name = itemView.findViewById(R.id.city_name);
            checkbox = itemView.findViewById(R.id.checkbox);

        }
    }
}
