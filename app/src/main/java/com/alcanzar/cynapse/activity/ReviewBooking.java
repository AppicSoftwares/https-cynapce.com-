package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.fragments.BookTicketFragment;
import com.alcanzar.cynapse.utils.ServiceUtility;


public class ReviewBooking extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack, titleIcon;
    Fragment fragmentInstance;
    TextView title;

    String conference_id = "", total_days_price = "", accomdation = "",
            price_hike_after_date = "",
            price_hike_after_percentage = "", seat = "", medical_profile_id = "", member_concessions = "", student_concessions = "", payment_mode = "";

    // Button btnPay;
    boolean savebool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_open);
        btnBack = findViewById(R.id.btnBack);
        titleIcon = findViewById(R.id.titleIcon);
//        btnPay = findViewById(R.id.btnPay);
        title = findViewById(R.id.title);
        btnBack.setOnClickListener(this);
        titleIcon.setVisibility(View.GONE);
        //    btnPay.setOnClickListener(this);
        title.setText("Review Booking");
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;


        if (getIntent() != null) {
            conference_id = getIntent().getStringExtra("conference_id");
            total_days_price = getIntent().getStringExtra("total_days_price");
            accomdation = getIntent().getStringExtra("accomdation");
            price_hike_after_date = getIntent().getStringExtra("price_hike_after_date");
            price_hike_after_percentage = getIntent().getStringExtra("price_hike_after_percentage");
            seat = getIntent().getStringExtra("seat");
            savebool = getIntent().getBooleanExtra("savebool", false);
            medical_profile_id = getIntent().getStringExtra("medical_profile_id");
            member_concessions = getIntent().getStringExtra("member_concessions");
            student_concessions = getIntent().getStringExtra("student_concessions");
            payment_mode = getIntent().getStringExtra("payment_mode");
        }

//        Log.d("PRICHIKEAFTER11",medical_profile_id);
//        Log.d("PRICHIKEAFTER22",price_hike_after_percentage);
//        Log.d("PRICHIKEAFTER33",seat);

        Fragment fragment = new BookTicketFragment();
        Bundle b = new Bundle();
        b.putString("conference_id", conference_id);
        b.putString("total_days_price", total_days_price);
        b.putString("accomdation", accomdation);
        b.putString("price_hike_after_date", price_hike_after_date);
        b.putString("price_hike_after_percentage", price_hike_after_percentage);
        b.putString("seat", seat);
        b.putBoolean("savebool", savebool);
        b.putString("medical_profile_id", medical_profile_id);
        b.putString("member_concessions", member_concessions);
        b.putString("student_concessions", student_concessions);
        b.putString("payment_mode", payment_mode);

        fragment.setArguments(b);

        fragmentReplace(fragment);

    }

    public void fragmentReplace(Fragment fragment) {
        fragmentInstance = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentOpenList, fragment, "Fragment").commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
            default:
                break;
        }
    }
}
