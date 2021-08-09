package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;

public class PrivacyPolicyActivity  extends AppCompatActivity implements View.OnClickListener {
    TextView title,priv_pol_txt,priv_pol_txt_second;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    WebView myWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy_layout);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        priv_pol_txt = findViewById(R.id.priv_pol_txt);
        priv_pol_txt_second = findViewById(R.id.priv_pol_txt_second);
        titleIcon.setImageResource(R.drawable.privacy_w);
        title = findViewById(R.id.title);
        title.setText(R.string.privacy_policy);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        priv_pol_txt.setText(R.string.privacy_policy_txt);
        priv_pol_txt_second.setText("");
        myWebView =  findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyBrowser());
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://cynapce.com/homes/privacy_policy");
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
