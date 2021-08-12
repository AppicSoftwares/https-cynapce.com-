package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.DealersDistAdapters;
import com.alcanzar.cynapse.model.DashBoardModel;

import java.util.ArrayList;

public class DealerDistributeActivity extends AppCompatActivity implements View.OnClickListener{
    //TODO : header views
    TextView title;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    //TODO: recyler and other views
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DashBoardModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_distribute);
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.dealers_distrbuters_white);
        title = findViewById(R.id.title);
        title.setText(R.string.dealers_and_distributors);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.VISIBLE);
        //TODO: recycler and adapter initialization
        recyclerView = findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //TODO: Demo data
        for(int i =0;i<=20;i++){
            arrayList.add(new DashBoardModel("Demo Name",""));
        }
        DealersDistAdapters dealersDistAdapters = new DealersDistAdapters(getApplicationContext(),R.layout.my_dealers_distributers_row,arrayList);
        recyclerView.setAdapter(dealersDistAdapters);
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
