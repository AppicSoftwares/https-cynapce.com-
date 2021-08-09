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

public class MyArticlesAdapter extends RecyclerView.Adapter<MyArticlesAdapter.MyArticlesViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<DashBoardModel>  arrayList;
    //TODO : this is the adapter constructor class
    public MyArticlesAdapter(Context context, int rowLayout, ArrayList<DashBoardModel> arrayList){
        this.context =context;
        this.rowLayout =rowLayout;
        this.arrayList =arrayList;
    }
    //TODO : this is used to contain all the views and here views initialization are done
    public class MyArticlesViewHolder extends RecyclerView.ViewHolder{
        TextView msgText;
        public MyArticlesViewHolder(View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.msgText);
        }
    }
    //TODO :  this is used to assign the layout to the recycleView on create
    @Override
    public MyArticlesAdapter.MyArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout,parent,false);
        return new MyArticlesViewHolder(view);
    }
    //TODO : used to display the data for the specific views included in the layout
    @Override
    public void onBindViewHolder(MyArticlesAdapter.MyArticlesViewHolder holder, int position) {

        holder.msgText.setText(arrayList.get(position).getId());
    }
    //TODO : this will give the size of the list which is displayed
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
