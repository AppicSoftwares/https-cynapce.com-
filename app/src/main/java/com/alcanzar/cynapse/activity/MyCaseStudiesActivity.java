package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MyCaseStudiesAdapter;
import com.alcanzar.cynapse.api.Get_all_attend_casesApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CaseStudyListModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCaseStudiesActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO : header views
    TextView title;
    ImageView btnBack, titleIcon;
    Button btnSearch;
    TextView no_record_txt;
    DatabaseHelper handler;
    FloatingActionButton btnAdd;
    //TODO : recycle and other views
    RecyclerView recycleView;
    LinearLayoutManager linearLayoutManager;
    String case_id="";
    ArrayList<CaseStudyListModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_case_studies);
        handler = new DatabaseHelper(this);
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        no_record_txt = findViewById(R.id.no_record_txt);
        titleIcon.setImageResource(R.drawable.case_studies_white);
        title = findViewById(R.id.title);
        title.setText(R.string.my_case_studies);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.VISIBLE);
        btnAdd = findViewById(R.id.btnAdd);
        //TODO: recycle and other initialization
        recycleView = findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(linearLayoutManager);
        try {
            Get_all_attend_casesApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO: Demo data
//        for (int i = 0; i <= 20; i++)
//        {
//            arrayList.add(new DashBoardModel("CASE 1", "id"));
//        }
//        MyCaseStudiesAdapter myCaseStudiesAdapter = new MyCaseStudiesAdapter(getApplicationContext(), R.layout.my_case_studies_row, arrayList);
//        recycleView.setAdapter(myCaseStudiesAdapter);
        arrayList = handler.getCaseStudyAttendList(DatabaseHelper.TABLE_CASE_STUDY_ATTEND_LIST_MASTER, "1");
        if(arrayList.size() > 0)
        {
            MyCaseStudiesAdapter myCaseStudiesAdapter = new MyCaseStudiesAdapter(MyCaseStudiesActivity.this, R.layout.my_case_studies_row, arrayList, "from_attend");
            recycleView.setAdapter(myCaseStudiesAdapter);
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
    private void Get_all_attend_casesApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(MyCaseStudiesActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("sync_time", AppCustomPreferenceClass.readString(MyCaseStudiesActivity.this, AppCustomPreferenceClass.sync_time_attend_cases, ""));
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new Get_all_attend_casesApi(MyCaseStudiesActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MyCaseStudiesActivity.this, AppCustomPreferenceClass.sync_time_attend_cases, sync_time);
                    Log.d("response", response.toString());

                    if (res_code.equals("1")) {
                        // MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("attendcases");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            CaseStudyListModel model = new CaseStudyListModel();
                            model.setId(item.getString("case_id"));
                            model.setCase_name(item.getString("case_name"));
                            model.setCase_sub_title(item.getString("case_sub_title"));
                            model.setCase_type(item.getString("case_type"));
                            model.setTotal_questions(item.getString("total_questions"));
                            model.setTotal_attempted_ques(item.getString("total_attempted_ques"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setModify_date(item.getString("modify_date"));
                            model.setStatus(item.getString("status"));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CASE_STUDY_ATTEND_LIST_MASTER, DatabaseHelper.id, item.getString("case_id"))) {

                                handler.AddCaseAttendList(model, true);

                                Log.e("ADDED_Sub_item", true + " " + model.getId());
                            } else {
                                Log.e("UPDATED", true + " " + model.getId());
                                handler.AddCaseAttendList(model, false);
                            }
                        }
                        arrayList = handler.getCaseStudyAttendList(DatabaseHelper.TABLE_CASE_STUDY_ATTEND_LIST_MASTER, "1");
                        Log.e("menulist.size();", "<><><" + arrayList.size());
                        if (arrayList.size() == 0) {
                            no_record_txt.setVisibility(View.VISIBLE);
                            no_record_txt.setText("No Case Studies");
                        } else {
                            no_record_txt.setVisibility(View.GONE);
                        }
                        MyCaseStudiesAdapter myCaseStudiesAdapter = new MyCaseStudiesAdapter(MyCaseStudiesActivity.this, R.layout.my_case_studies_row, arrayList,"from_attend");
                        recycleView.setAdapter(myCaseStudiesAdapter);
                        // myDealsAdapter.notifyDataSetChanged();
                    } else {

                        //MyToast.toastLong(getActivity(),res_msg);
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
