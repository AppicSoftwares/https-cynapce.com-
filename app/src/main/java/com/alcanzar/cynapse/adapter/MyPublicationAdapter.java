package com.alcanzar.cynapse.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.MedicalWritingModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPublicationAdapter extends RecyclerView.Adapter<MyPublicationAdapter.MyPublicationViewHolder>{
    private Context context;
    private int rowLayout;
    private ArrayList<MedicalWritingModel> arrayList;

    public MyPublicationAdapter(Context context,int rowLayout,ArrayList<MedicalWritingModel> arrayList){
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
    }

    public class MyPublicationViewHolder extends RecyclerView.ViewHolder{
        TextView bookName,publishYear;
        ImageView bookImg;
        String url="";
        public MyPublicationViewHolder(View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            bookImg = itemView.findViewById(R.id.bookImg);
            publishYear = itemView.findViewById(R.id.publishYear);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        url = arrayList.get(getAdapterPosition()).getUrl();
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                    catch (ActivityNotFoundException aof)
                    {
                        aof.printStackTrace();
                    }

                }
            });
        }
    }


    @Override
    public MyPublicationAdapter.MyPublicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout,parent,false);
        return new MyPublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyPublicationAdapter.MyPublicationViewHolder holder, int position) {

        holder.bookName.setText(arrayList.get(position).getTitle());
        holder.publishYear.setText("Published("+arrayList.get(position).getPublished_year()+")");

//        Glide.with(context)
//                .load(arrayList.get(position).getImage())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.book_mypub)
//                .dontAnimate()
//                .into(holder.bookImg);

        Picasso.with(context).load(arrayList.get(position).getImage()).error(R.drawable.book_mypub).into(holder.bookImg);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
