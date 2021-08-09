package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alcanzar.cynapse.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyConferenceAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public MyConferenceAdapter(Context context, ArrayList<String> images) {

        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);

//                Glide.with(context).load(images.get(position))
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.whey_protien)
//                .into(myImage);

        Picasso.with(context).load(images.get(position)).error(R.drawable.whey_protien).into(myImage);

//
//                myImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, ShowImageTicket.class);
//                        intent.putExtra("image",images.get(position));
//                        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    }
//                });
//        Picasso.with(context)
//                .load(images.get(position))
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.placeholder)
//                .into(myImage);


        //myImage.setImageResource(Integer.parseInt(images.get(position)));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}