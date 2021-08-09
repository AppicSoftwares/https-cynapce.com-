package com.alcanzar.cynapse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MyArticlesAdapter;
import com.alcanzar.cynapse.model.DashBoardModel;
import com.alcanzar.cynapse.utils.RecyclerTouchListener;

import java.util.ArrayList;

public class MyArticlesActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO : header views
    TextView title;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    //TODO : recycle and listing views
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DashBoardModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_articles);
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.article_white);
        title = findViewById(R.id.title);
        title.setText(R.string.community_forum);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        //TODO : recycle and adapter control

        for(int i = 0;i<=10;i++){
            arrayList.add(new DashBoardModel("This is demo msg","id"));
        }

        recyclerView = findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyArticlesAdapter myArticlesAdapter = new MyArticlesAdapter(this,R.layout.timeline_row,arrayList);
        recyclerView.setAdapter(myArticlesAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //phone_no.setText(arrayList.get(position).getId());
                //amount.setText(lastTransactionSetterGetters.get(position).getWallet_amount());
                //Intent td = new Intent(getActivity(), TicketDetails.class);
                //td.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(new Intent(getApplicationContext(), MyArticleDetailsActivity.class));

            }
            @Override
            public void onLongClick(View view, int position) {

            }

        }));

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
