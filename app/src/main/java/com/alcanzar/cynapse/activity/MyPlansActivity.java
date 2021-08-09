package com.alcanzar.cynapse.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MyPlansAdapter;
import com.alcanzar.cynapse.api.GetAllMyPlansApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPlansActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    DatabaseHelper handler;
    ImageView btnBack, titleIcon;
    Button btnSearch;
    //TODO : recycle and listing views
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<JobMasterModel> arrayList = new ArrayList<>();
    public static Activity noti;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.terms_w);
        title = findViewById(R.id.title);
        title.setText("MY Plans");
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        //TODO : recycle and adapter control
//        for (int i = 0; i <= 10; i++) {
//            arrayList.add(new DashBoardModel("This is the new notification", "id"));
//        }
        recyclerView = findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        try {
            getMyPlans();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
    private void getMyPlans() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(MyPlansActivity.this, AppCustomPreferenceClass.UserId, ""));
        // params.put("uuid","S75f3");
        //params.put("sync_time","");
        header.put("Cynapse", params);
        new GetAllMyPlansApi(MyPlansActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("NOTIFICATIONAAA", response.toString());
                    if (res_code.equals("1")) {
                        JSONArray jsonArray = header.getJSONArray("PackageData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JobMasterModel model = new JobMasterModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            model.setJob_type(jsonObject.getString("total_jobs"));
                            model.setMedical_profile_name(jsonObject.getString("medical_profile_name"));
                            model.setJob_title(jsonObject.getString("title_name"));
                            model.setJob_type_name(jsonObject.getString("package_type"));
                            arrayList.add(model);
                        }
                         MyPlansAdapter requestPostJobAdapter = new MyPlansAdapter(MyPlansActivity.this,arrayList);
                        recyclerView.setAdapter(requestPostJobAdapter);
                        //adapter.notifyDataSetChanged();
                    } else {

                        // MyToast.toastLong(MyPlansActivity.this, res_msg);
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
