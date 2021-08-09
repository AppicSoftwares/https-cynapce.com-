package com.alcanzar.cynapse.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MyArticlesAdapter;
import com.alcanzar.cynapse.model.DashBoardModel;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
ImageView btnBack,titleIcon;
TextView title,share_commnt,txt_vote,txt_view,txt_comment;
String vote,comment,view;
RecyclerView recycler_expert_view,recycler_other_view;
    LinearLayoutManager linearLayoutManager,layoutManager;
    ArrayList<DashBoardModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        btnBack=findViewById(R.id.btnBack);
        share_commnt=findViewById(R.id.share_commnt);
        recycler_other_view=findViewById(R.id.recycler_other_view);
        recycler_expert_view=findViewById(R.id.recycler_expert_view);
        txt_vote=findViewById(R.id.txt_vote);
        txt_view=findViewById(R.id.txt_view);
        titleIcon=findViewById(R.id.titleIcon);
        txt_comment=findViewById(R.id.txt_comment);
        title=findViewById(R.id.title);
        titleIcon.setVisibility(View.GONE);
        title.setVisibility(View.GONE);

        if(getIntent()!=null)
        {
         vote=getIntent().getStringExtra("vote");
         view=getIntent().getStringExtra("view");
         comment=getIntent().getStringExtra("comment");
        }

         //txt_vote.setText(vote);
         // txt_view.setText(view);
        //txt_comment.setText(comment);
        setTextViewDrawableColor(share_commnt,R.color.dark_grey);
        share_commnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

//                x = arrayList.get(getAdapterPosition()).getBrochuers_file().split(",");

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Comments Name: ");
                startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        for(int i = 0;i<=2;i++){
            arrayList.add(new DashBoardModel("This is demo message","id"));
        }
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_expert_view.setLayoutManager(linearLayoutManager);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_other_view.setLayoutManager(layoutManager);
        MyArticlesAdapter myArticlesAdapter = new MyArticlesAdapter(this,R.layout.comments_layout_row,arrayList);
        recycler_expert_view.setAdapter(myArticlesAdapter);
        MyArticlesAdapter myArticlesAdapter1 = new MyArticlesAdapter(this,R.layout.comments_layout_row,arrayList);
        recycler_other_view.setAdapter(myArticlesAdapter1);

    }
    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }
}
