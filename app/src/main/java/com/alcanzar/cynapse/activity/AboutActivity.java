package com.alcanzar.cynapse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.BuildConfig;
import com.alcanzar.cynapse.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title,version_txt;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    LinearLayout lin_lay_terms,lin_lay_privacy,lin_lay_send_feed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init_view();
    }
    private void init_view()
    {
        btnBack = findViewById(R.id.btnBack);
        lin_lay_terms = findViewById(R.id.lin_lay_terms);
        lin_lay_privacy = findViewById(R.id.lin_lay_privacy);
        lin_lay_send_feed = findViewById(R.id.lin_lay_send_feed);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.about_icon_w);
        title = findViewById(R.id.title);
        version_txt = findViewById(R.id.version_txt);
        title.setText(R.string.about);
        title.setTextSize(14);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        version_txt.setText(versionName);
        lin_lay_terms.setOnClickListener(this);
        lin_lay_privacy.setOnClickListener(this);
        lin_lay_send_feed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.lin_lay_terms:
                startActivity(new Intent(this, Terms_And_Condtions_Activity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.lin_lay_privacy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.lin_lay_send_feed:
                startActivity(new Intent(this, SendFeedbackActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }
}
