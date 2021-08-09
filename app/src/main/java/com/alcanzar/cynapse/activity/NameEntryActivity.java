package com.alcanzar.cynapse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.ResendOTPApi;
import com.alcanzar.cynapse.api.UpdateUsernameAPI;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

public class NameEntryActivity extends AppCompatActivity {

    private EditText userName;
    private Button nextBtn;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_entry_fragment);

        initV();
        setOnClickListener();
    }

    private void initV() {
        userName = findViewById(R.id.userName);
        nextBtn = findViewById(R.id.nextBtn);

        activity = NameEntryActivity.this;

        if (getIntent() != null) {
            uuid = getIntent().getStringExtra("uuid");
        }
    }

    private void setOnClickListener() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (userName.getText().toString().isEmpty())
                        MyToast.toastLong(activity,"Enter your Name");
                     else
                    hitApi(uuid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void hitApi(String uuid) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("name", userName.getText().toString().trim());

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new UpdateUsernameAPI(activity, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
                    if (res_code.equals("1")) {
                        MyToast.toastLong(activity, res_msg);
                        startActivity(new Intent(activity, MainActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        AppCustomPreferenceClass.writeString(activity, AppCustomPreferenceClass.name, userName.getText().toString().trim());
                        ActivityCompat.finishAffinity(activity);
                    } else {
                        MyToast.toastLong(activity, res_msg);
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                    MyToast.toastLong(activity, error.getMessage());
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
                MyToast.toastLong(activity, error.getMessage());
            }
        };
    }

    private String uuid = "";
}

