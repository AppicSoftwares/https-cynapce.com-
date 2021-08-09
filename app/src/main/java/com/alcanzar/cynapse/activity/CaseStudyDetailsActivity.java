package com.alcanzar.cynapse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.Get_all_questionsApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CaseStudyListModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CaseStudyDetailsActivity extends AppCompatActivity{
    Button btnAttendQues;
    DatabaseHelper handler;
    TextView question,heading,time_alotted_ans;
    TextView title;
    ImageView btnBack, titleIcon;
    String case_id = "";
   static   AppCompatActivity fa;
    ArrayList<CaseStudyListModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_questions_layout);
        handler = new DatabaseHelper(this);
        btnAttendQues = findViewById(R.id.btnAttendQues);
        time_alotted_ans = findViewById(R.id.time_alotted_ans);
        question = findViewById(R.id.question);
        heading = findViewById(R.id.heading);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.case_studies_white);
        title = findViewById(R.id.title);
        btnBack = findViewById(R.id.btnBack);
        fa = this;
        if(getIntent()!=null)
        {
            case_id = getIntent().getStringExtra("id");
        }
        try {
            Get_all_questionsApi(case_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setData();
        btnAttendQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseStudyDetailsActivity.this,Question_Answers_Activity.class);
                intent.putExtra("id",case_id);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Get_all_questionsApi(String case_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(CaseStudyDetailsActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("case_id", case_id);
        header.put("Cynapse", params);
        new Get_all_questionsApi(CaseStudyDetailsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    handler.deleteTableName(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_MASTER);
                    handler.deleteTableName(DatabaseHelper.TABLE_CASE_STUDY_ANSWER_MASTER);
                    handler.deleteTableName(DatabaseHelper.TABLE_CASE_STUDY_OPTION_MASTER);
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("STATERESPONSE", response.toString());

                    if (res_code.equals("1")) {
                        // MyToast.toastLong(MyCaseStudiesActivity.this,res_msg);
                        JSONArray header2 = header.getJSONArray("questions");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            CaseStudyListModel model = new CaseStudyListModel();
                            model.setId(String.valueOf(i+1));
                            model.setQues_id(item.getString("question_id"));
                            model.setQues_name(item.getString("question"));
                            model.setCase_name(header.getString("case_name"));
                            model.setCase_heading(header.getString("case_heading"));
                            model.setDescription(header.getString("description"));
                            model.setCase_type(header.getString("case_type"));
                            model.setTotal_questions(header.getString("total_questions"));
                            model.setTime_durations(header.getString("time_durations"));
                            model.setQues_status("");
                            JSONArray ans_arr = item.getJSONArray("answers");
                            for(int j = 0 ; j < ans_arr.length();j++)
                            {
                                JSONObject jsonObject = ans_arr.getJSONObject(j);
                                CaseStudyListModel model_ans = new CaseStudyListModel();
                                model_ans.setOption_id(jsonObject.getString("options_id"));
                                model_ans.setQues_id(item.getString("question_id"));
                                model_ans.setOption_name(jsonObject.getString("choice"));
                                model_ans.setIs_correct(jsonObject.getString("is_correct"));
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CASE_STUDY_OPTION_MASTER, DatabaseHelper.option_id, jsonObject.getString("options_id"))) {

                                    handler.AddCaseStudyOptionList(model_ans, true);

                                    Log.e("ADDED_OPtion_item", true + " " + model_ans.getOption_id());
                                } else {
                                    Log.e("UPDATED_OP", true + " " + model_ans.getOption_id());
                                    handler.AddCaseStudyOptionList(model_ans, false);
                                }
                            }
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_MASTER, DatabaseHelper.id, model.getId())) {

                                handler.AddCaseStudyQuesList(model, true);

                                Log.e("ADDED_Ques_item", true + " " + model.getId());
                            } else {
                                Log.e("UPDATED", true + " " + model.getId());
                                handler.AddCaseStudyQuesList(model, false);
                            }
                        }

                        setData();
                    } else {

                        //MyToast.toastLong(MyCaseStudiesActivity.this,res_msg);
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
    public void setData()
    {
        arrayList = handler.getCaseStudyQuestion(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_MASTER, "");
        Log.e("array.size();", "<><><" + arrayList.size());
        if(arrayList.size() > 0)
        {
            question.setText(arrayList.get(0).getDescription());
            heading.setText(arrayList.get(0).getCase_heading());
            title.setText(arrayList.get(0).getCase_name());
            time_alotted_ans.setText(arrayList.get(0).getTime_durations()+"Min");
        }

    }
}
