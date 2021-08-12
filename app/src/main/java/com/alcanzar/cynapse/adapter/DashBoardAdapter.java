package com.alcanzar.cynapse.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.DashBoardModel;

import java.util.ArrayList;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.DashBoardViewHolder> {
    //TODO : context , list and row_layout for the recycler
    private Context context;
    private ArrayList<DashBoardModel> arrayList;
    private int rowLayout;
    //TODO : calling this from the other class by passing context n arrayList
    public DashBoardAdapter (Context context, ArrayList<DashBoardModel> arrayList,int rowLayout){
        this.context = context;
        this.arrayList=arrayList;
        this.rowLayout = rowLayout;
    }
    //TODO: this is used to contain all the views and here views initialization are done
    public class DashBoardViewHolder extends RecyclerView.ViewHolder {
        TextView msgText;
        public  DashBoardViewHolder(View view){
            super(view);
            msgText= view.findViewById(R.id.msgText);
        }
    }
    //TODO : this is used to assign the layout to the recycleView on create
    @Override
    public DashBoardAdapter.DashBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new DashBoardViewHolder(view);
    }
    //TODO : used to display the data for the specific views included in the layout
    @Override
    public void onBindViewHolder(DashBoardViewHolder holder, int position) {
       holder.msgText.setText(arrayList.get(position).getId());
    }
    //TODO : this will give the size of the list which is displayed
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}