package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.MyConferenceModel.BookingDetail;


import java.util.ArrayList;

public class MyConferenceDetails3Adapter extends RecyclerView.Adapter<MyConferenceDetails3Adapter.MyConferenceDetails3AdapterVH> {

    private Context context;
    private ArrayList<BookingDetail> arrayList;

    public MyConferenceDetails3Adapter(Context context, ArrayList<BookingDetail> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyConferenceDetails3AdapterVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_conference_details3, viewGroup, false);
        return new MyConferenceDetails3AdapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyConferenceDetails3AdapterVH holder, int i) {

        holder.phoneNOTv.setText(arrayList.get(i).getPhoneNo());
        holder.userNameTv.setText(arrayList.get(i).getUsername());
        holder.emailTv.setText(arrayList.get(i).getEmail());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyConferenceDetails3AdapterVH extends RecyclerView.ViewHolder {
        TextView userNameTv;
        TextView phoneNOTv;
        TextView emailTv;

        public MyConferenceDetails3AdapterVH(@NonNull View itemView) {
            super(itemView);

            phoneNOTv = itemView.findViewById(R.id.phoneNOTv);
            userNameTv = itemView.findViewById(R.id.userNameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
        }
    }
}

