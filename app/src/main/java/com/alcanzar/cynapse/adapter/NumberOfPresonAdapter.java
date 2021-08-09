package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.AddPackageModal;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;

import java.util.ArrayList;

public class NumberOfPresonAdapter extends RecyclerView.Adapter<NumberOfPresonAdapter.MyViewHolder> {

    Context context;
    ArrayList<AddPackageModal> selectPersonModalList;

    public NumberOfPresonAdapter(Context context, ArrayList<AddPackageModal> selectPersonModalList) {
        this.context=context;
        this.selectPersonModalList=selectPersonModalList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view= LayoutInflater.from(viewGroup.getContext()).inflate(i,viewGroup,false);
       return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        String tagColor= AppCustomPreferenceClass.readString(context,AppCustomPreferenceClass.COLOR_CLICK_CHECK,"0");

        Log.e("textColorCheck",tagColor);
        if(i<=Integer.parseInt(tagColor)){
            myViewHolder.personNoText.setTextColor(Color.WHITE);
            myViewHolder.personNoText.setBackgroundResource(R.drawable.cust_no_person_click);

        }else {
            myViewHolder.personNoText.setTextColor(Color.parseColor("#4AB5B0"));
            myViewHolder.personNoText.setBackgroundResource(R.drawable.cust_no_person);
        }

        myViewHolder.personNoText.setText(selectPersonModalList.get(i).getPackageDetails());
        myViewHolder.personNoText.setTag(i);

//        myViewHolder.personNoText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AppCustomPreferenceClass.writeString(context,AppCustomPreferenceClass.COLOR_CLICK_CHECK, String.valueOf(myViewHolder.getAdapterPosition()));
//                notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return selectPersonModalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView personNoText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            personNoText=(TextView) itemView.findViewById(R.id.personNoText);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return R.layout.no_of_person_custview;

    }
}
