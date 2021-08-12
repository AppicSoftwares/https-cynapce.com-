package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;

public class Terms_And_Condtions_Activity extends AppCompatActivity implements View.OnClickListener {
    TextView title,term_and_condn_txt,term_and_condn_txt_second,term_and_condn_txt_third;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_cond_layout);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        term_and_condn_txt = findViewById(R.id.term_and_condn_txt);
        term_and_condn_txt_second = findViewById(R.id.term_and_condn_txt_second);
        term_and_condn_txt_third = findViewById(R.id.term_and_condn_txt_third);
        titleIcon.setImageResource(R.drawable.terms_w);
        title = findViewById(R.id.title);
        title.setText(R.string.terms_and_conditions);
//        term_and_condn_txt.setText(R.string.term_cond_txt);
//        term_and_condn_txt_second.setText(R.string.term_cond_txt_scnd);
//        term_and_condn_txt_third.setText(R.string.term_cond_txt_third);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        myWebView =  findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyBrowser());
        myWebView.loadUrl("http://cynapce.com/homes/terms_condition");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.myWebView.canGoBack()) {
            this.myWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
