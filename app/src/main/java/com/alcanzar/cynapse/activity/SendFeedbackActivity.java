package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.PostFeedbackApi;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class SendFeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    ImageView btnBack,titleIcon;
    Button btnSearch,btnSubmit_;
    EditText feedback_edit;
    ConstraintLayout constraint_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        init_views();
    }
    private void init_views()
    {
        btnBack = findViewById(R.id.btnBack);
        constraint_layout = findViewById(R.id.constraint_layout);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        btnSubmit_ = findViewById(R.id.btnSubmit_);
        feedback_edit = findViewById(R.id.feedback_edit);
        titleIcon.setImageResource(R.drawable.terms_w);
        title = findViewById(R.id.title);
        title.setText(R.string.send_feedback);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        btnSubmit_.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSubmit_:
                if(!(TextUtils.isEmpty(feedback_edit.getText().toString())))
                {
                    try {
                        PostFeedbackApi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                {
                    Snackbar snackbar1 = Snackbar.make(constraint_layout, "Feedback cannot be blank!", Snackbar.LENGTH_LONG);

                    View sbView = snackbar1.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                   // textView.setTextColor(Color.YELLOW);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    snackbar1.show();
                }
                break;
        }
    }
    private void PostFeedbackApi() throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(SendFeedbackActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("feedbacks", feedback_edit.getText().toString());
        header.put("Cynapse", params);

        new PostFeedbackApi(SendFeedbackActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    if (res_code.equals("1")) {

                        MyToast.toastLong(SendFeedbackActivity.this, res_msg);
                        finish();
                    } else {
                        MyToast.toastLong(SendFeedbackActivity.this, res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }

}
