package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.Attend_cases_ques_ansApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CaseStudyListModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CaseStudyAnswerActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ans_txt_lin_lay,option_lin_lay;
    DatabaseHelper handler;
    String case_id = "",case_type="",case_name="",tot_ques="",tot_attempt="";
    Button btnPrev,btnNext,btnDone;
    TextView question,optionOne,optionTwo,title,tot_ques_,ques_attempt,answer;
    ImageView titleIcon,btnBack;
    int count = 1;
    ArrayList<CaseStudyListModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_details_theory);
        handler = new DatabaseHelper(this);
        if(getIntent()!=null)
        {
            case_id = getIntent().getStringExtra("id");
            case_type = getIntent().getStringExtra("case_type");
            case_name = getIntent().getStringExtra("case_name");
            tot_ques = getIntent().getStringExtra("tot_ques");
            tot_attempt = getIntent().getStringExtra("tot_attempt");
            Log.d("tot_QUES", tot_attempt +"<><<"+tot_ques);
        }
        btnBack = findViewById(R.id.btnBack);
        tot_ques_ = findViewById(R.id.tot_ques_);
        answer = findViewById(R.id.answer);
        ques_attempt = findViewById(R.id.ques_attempt);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.case_studies_white);
        title = findViewById(R.id.title);
        title.setText(case_name);
        ques_attempt.setText(tot_attempt);
        tot_ques_.setText(tot_ques);
        ans_txt_lin_lay = findViewById(R.id.ans_txt_lin_lay);
        option_lin_lay = findViewById(R.id.option_lin_lay);
        optionOne = findViewById(R.id.optionOne);
        optionTwo = findViewById(R.id.optionTwo);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnDone = findViewById(R.id.btnDone);
        question = findViewById(R.id.question);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        try {
            Attend_cases_ques_ansApi(case_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(count == 1)
        {
            btnPrev.setVisibility(View.GONE);
        }else
        {
            btnPrev.setVisibility(View.VISIBLE);
        }
        if(count == Integer.parseInt(tot_attempt))
        {
            btnNext.setVisibility(View.GONE);
            btnDone.setVisibility(View.VISIBLE);
        }else
        {
            btnNext.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.GONE);

        }
       // setData();
    }
    private void Attend_cases_ques_ansApi(String case_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(CaseStudyAnswerActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("case_id", case_id);
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new Attend_cases_ques_ansApi(CaseStudyAnswerActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    handler.deleteTableName(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_ANS_MASTER);
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("STATERESPONSE", response.toString());
                    //AppCustomPreferenceClass.writeString(CaseStudyAnswerActivity.this, AppCustomPreferenceClass.sync_time_attend_ans_cases, sync_time);
                    if (res_code.equals("1")) {
                        // MyToast.toastLong(CaseStudyAnswerActivity.this,res_msg);
                        JSONArray header2 = header.getJSONArray("attendquestions");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            CaseStudyListModel model = new CaseStudyListModel();
                            model.setId(String.valueOf(i+1));
                            model.setQues_name(item.getString("question"));
                            model.setOption_name(item.getString("options"));
                            model.setIs_correct(item.getString("is_correct"));
                            model.setCorrect_answer(item.getString("correct_answer"));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_ANS_MASTER, DatabaseHelper.id, model.getId())) {

                                handler.AddCaseStudyQuesAnsList(model, true);

                                Log.e("ADDED_Ques_item", true + " " + model.getId());
                            } else {
                                Log.e("UPDATED", true + " " + model.getId());
                                handler.AddCaseStudyQuesAnsList(model, false);
                            }
                        }

                        setData();
                    } else {

                        //MyToast.toastLong(CaseStudyAnswerActivity.this,res_msg);
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


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnPrev:
                if(count > 1)
                {
                    count--;
                    setData();
                    Log.e("list_sizePRev","size"+arrayList.size()+"count"+count);
                }
                if(count == 1)
                {
                    btnPrev.setVisibility(View.GONE);
                }else
                {
                    btnPrev.setVisibility(View.VISIBLE);
                }
                btnNext.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.GONE);
                break;
            case R.id.btnNext:
                btnPrev.setVisibility(View.VISIBLE);
                if(count < Integer.parseInt(tot_attempt))
                {
                    count++;
                    Log.e("list_sizeNExt","size"+arrayList.size()+"count"+count);
                    setData();

                }
                if(count == Integer.parseInt(tot_attempt))
                {
                    btnNext.setVisibility(View.GONE);
                    btnDone.setVisibility(View.VISIBLE);
                }else
                {
                    btnNext.setVisibility(View.VISIBLE);
                    btnDone.setVisibility(View.GONE);

                }
                break;
            case R.id.btnDone:
                finish();
                break;
        }
    }
    public void setData()
    {
        arrayList = handler.getCaseStudyQuestionAns(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_ANS_MASTER, String.valueOf(count));
//        Log.e("array.size();", "<><><" + arrayList.size());

        if(arrayList.size() > 0)
        {
            question.setText(arrayList.get(0).getQues_name());


            if(case_type.equalsIgnoreCase("1"))
            {
                option_lin_lay.setVisibility(View.VISIBLE);
                ans_txt_lin_lay.setVisibility(View.GONE);
                optionOne.setText(arrayList.get(0).getOption_name());
                if(arrayList.get(0).getOption_name().equalsIgnoreCase(""))
                {
                    optionOne.setText("No Answer");
                }
                else
                {
                    optionOne.setText(arrayList.get(0).getOption_name());
                }
                optionTwo.setText(arrayList.get(0).getCorrect_answer());
            }
            else {
                option_lin_lay.setVisibility(View.GONE);
                ans_txt_lin_lay.setVisibility(View.VISIBLE);
                if(arrayList.get(0).getOption_name().equalsIgnoreCase(""))
                {
                    answer.setText("No Answer");
                }
                else
                {
                    answer.setText(arrayList.get(0).getOption_name());
                }

            }
            if(arrayList.get(0).getIs_correct().equalsIgnoreCase("Y"))
            {
                optionOne.setBackgroundColor(ContextCompat.getColor(this,R.color.green));
                optionOne.setTextColor(ContextCompat.getColor(this,R.color.white));
                optionTwo.setVisibility(View.GONE);
            }
            else
            {
                optionOne.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
                optionOne.setTextColor(ContextCompat.getColor(this,R.color.white));
                optionTwo.setVisibility(View.VISIBLE);
                optionTwo.setBackgroundColor(ContextCompat.getColor(this,R.color.green));
                optionTwo.setTextColor(ContextCompat.getColor(this,R.color.white));
            }
        }

    }
}
