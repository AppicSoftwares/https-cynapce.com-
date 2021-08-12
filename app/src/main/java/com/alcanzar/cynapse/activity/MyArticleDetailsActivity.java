package com.alcanzar.cynapse.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.alcanzar.cynapse.R;

public class MyArticleDetailsActivity extends AppCompatActivity implements View.OnClickListener {
LinearLayout comment_lin_lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_forum_details);
        init_view();
    }
    private void init_view()
    {
        comment_lin_lay = findViewById(R.id.comment_lin_lay);
        comment_lin_lay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.comment_lin_lay:
                startActivity(new Intent(MyArticleDetailsActivity.this, CommentActivity.class));
                break;
        }
    }
}
