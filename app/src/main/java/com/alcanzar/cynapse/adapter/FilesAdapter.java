package com.alcanzar.cynapse.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alcanzar.cynapse.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private Context context;
    ArrayList<JSONObject> jsonObjects;

    public FilesAdapter(Context context, ArrayList<JSONObject> jsonObjects) {
        this.context = context;
        this.jsonObjects = jsonObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.files_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

//        Glide.with(context)
//                .load(jsonObjects.get(position).optString(""))
//                .placeholder(R.drawable.animation_loading_circle)
//                .error(R.drawable.no_img_placeholder)
//                .into(viewHolder.img);

        Picasso.with(context).load(jsonObjects.get(position).optString("")).error(R.drawable.no_img_placeholder).placeholder(R.drawable.animation_loading_circle).into(viewHolder.img);
        String fileExt = getfilteExt(jsonObjects.get(position).optString(""));

        if (fileExt.equals(".pdf")) {
            Log.d("pdfBOB", "pdf");
        } else {
            Log.d("pdfBOB", "image");
        }

    }

    @Override
    public int getItemCount() {
        return jsonObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

    private String getfilteExt(String string) {
        String string1 = string.substring(string.length() - 3, string.length());
        return string1;
    }
}
