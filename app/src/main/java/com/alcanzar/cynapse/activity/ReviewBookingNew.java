package com.alcanzar.cynapse.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.fragments.BookTicketFragmentNew;

import java.util.ArrayList;

public class ReviewBookingNew extends AppCompatActivity {

    public static ArrayList packageList = new ArrayList<String>();

    public static ReviewBookingNew reviewBookingNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_booking_new);

        reviewBookingNew =  ReviewBookingNew.this;

        findViewById(R.id.titleIcon).setVisibility(View.GONE);
        TextView title = findViewById(R.id.title);

        title.setText("Add Details");

        BookTicketFragmentNew bookTicketFragmentNew = new BookTicketFragmentNew();
        Bundle bundle = new Bundle();
        bundle.putString("totalSits",getIntent().getStringExtra("totalSits"));
        bundle.putString("conferenceID",getIntent().getStringExtra("conferenceID"));
        bundle.putString("discount_percentage",getIntent().getStringExtra("discount_percentage"));
        bundle.putString("discount_description",getIntent().getStringExtra("discount_description"));
        bundle.putString("conference_name",getIntent().getStringExtra("conference_name"));
        bundle.putString("address",getIntent().getStringExtra("address"));
        bundle.putString("gst",getIntent().getStringExtra("gst"));
        bundle.putString("from_date",getIntent().getStringExtra("from_date"));
        bundle.putString("to_date",getIntent().getStringExtra("to_date"));
        bundle.putString("from_time",getIntent().getStringExtra("from_time"));
        bundle.putString("price_hike_after_date",getIntent().getStringExtra("price_hike_after_date"));
        bundle.putString("price_hike_after_percentage",getIntent().getStringExtra("price_hike_after_percentage"));

        bookTicketFragmentNew.setArguments(bundle);
        setFragment(bookTicketFragmentNew);


        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentOpenList,fragment,"My_Fragment").commit();
    }


}
