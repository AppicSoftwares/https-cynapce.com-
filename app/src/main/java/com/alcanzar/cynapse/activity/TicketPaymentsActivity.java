package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;

public class TicketPaymentsActivity extends AppCompatActivity implements View.OnClickListener{
    //TODO : header views
    TextView title;
    ImageView btnBack,titleIcon;
    TextView debit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_payments);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.conference_white);
        title = findViewById(R.id.title);
        title.setText("Review Booking");
        debit = findViewById(R.id.debit);
        debit.findViewById(R.id.debit);
        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: calling payments here
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
