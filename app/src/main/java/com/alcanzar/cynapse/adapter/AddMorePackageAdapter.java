package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.AddPackageModal;

import java.util.ArrayList;
import java.util.zip.Inflater;

public abstract class AddMorePackageAdapter extends RecyclerView.Adapter<AddMorePackageAdapter.MyViewHolder> {

    public abstract void setDataEditNotify(View view,int position);
    public abstract void setDataDeleteNotify(View view,int position);
    Context context;
    ArrayList<AddPackageModal> addPackageModalArrayList;
    int what;

    public AddMorePackageAdapter(Context context, ArrayList<AddPackageModal> addPackageModalArrayList,int what) {
        this.context = context;
        this.addPackageModalArrayList = addPackageModalArrayList;
        this.what = what;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_package_details_cust_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {


        AddPackageModal addPackageModal = addPackageModalArrayList.get(position);

        myViewHolder.packageDetails.setText(addPackageModal.getPackageDetails());

        if (what==1) {
            myViewHolder.packagePrice.setText(context.getString(R.string.Rs) + " " + addPackageModal.getPrice());
        }
        else
        {
            myViewHolder.packagePrice.setText("$"+" "+addPackageModal.getPrice());
        }

        myViewHolder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(v,addPackageModalArrayList.get(myViewHolder.getAdapterPosition()),myViewHolder.getAdapterPosition());
            }
        });

        myViewHolder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editItem(v,addPackageModalArrayList.get(myViewHolder.getAdapterPosition()),myViewHolder.getAdapterPosition());
            }
        });

    }

    private void editItem(View v, AddPackageModal addPackageModal, int adapterPosition) {
        setDataEditNotify(v,adapterPosition);
    }

    private void deleteItem(View v, AddPackageModal addPackageModal, int adapterPosition) {
        addPackageModalArrayList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        setDataDeleteNotify(v,adapterPosition);
    }


    @Override
    public int getItemCount() {
        return addPackageModalArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  packageDetails;
        TextView  packagePrice;
        ImageButton delete_button,edit_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            packageDetails=itemView.findViewById(R.id.packageDetails);
            packagePrice=itemView.findViewById(R.id.packagePrice);
            delete_button=itemView.findViewById(R.id.delete_button);
            edit_button=itemView.findViewById(R.id.edit_button);
        }
    }


}
