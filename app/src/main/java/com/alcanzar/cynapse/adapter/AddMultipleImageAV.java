package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alcanzar.cynapse.R;

import java.util.ArrayList;

public class AddMultipleImageAV extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private ArrayList<Bitmap> multipleImagesAl;

    public AddMultipleImageAV(Context context,ArrayList<Bitmap> multipleImagesAl)
    {
        this.context = context;
        this.multipleImagesAl  = multipleImagesAl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_multiple_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return multipleImagesAl.size();
    }

    public void notifyData(ArrayList<Bitmap> multipleImagesAl1)
    {
        multipleImagesAl.clear();
        multipleImagesAl.addAll(multipleImagesAl1);
        notifyDataSetChanged();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {

    private ImageView img_add;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        img_add = itemView.findViewById(R.id.img_add);
    }
}
