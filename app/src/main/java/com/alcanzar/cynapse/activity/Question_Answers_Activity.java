package com.alcanzar.cynapse.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.Post_answersApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CaseStudyListModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Question_Answers_Activity extends AppCompatActivity implements View.OnClickListener {
    TextView title, question, optionOne, optionTwo, optionThree, optionFour, timeRemain, quesLeftNo, quesSkipNo;
    ImageView btnBack, titleIcon;
    EditText editOne;
    DatabaseHelper handler;
    Button btnNext, btnPrev, btnSubmit;
    int tot_ques = 0;
    int count = 1;
    int skip_count = 0;
    String case_id = "", chrono_time = "";
    long diff;
    String timeinMin = "0", timeinSec = "0",time_in_min_sec="0";
    boolean skip_bool = false, click_bool = false;
    CountDownTimer countDownTimer;
    LinearLayout edit_txt_lin_lay, option_lin_lay;
    ArrayList<CaseStudyListModel> arrayList = new ArrayList<>();
    ArrayList<CaseStudyListModel> arrayListOptions = new ArrayList<>();
    ArrayList<CaseStudyListModel> arrayList_ALL = new ArrayList<>();
    ArrayList<CaseStudyListModel> arrayList_Ans = new ArrayList<>();
    ArrayList<CaseStudyListModel> arrayList_AnsFinal = new ArrayList<>();
    ArrayList<CaseStudyListModel> arrayList_selected = new ArrayList<>();
    Chronometer chronometer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_study_details);
        handler = new DatabaseHelper(this);
        if (getIntent() != null) {
            case_id = getIntent().getStringExtra("id");
        }
        init_view();
    }

    public void init_view() {
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.case_studies_white);
        title = findViewById(R.id.title);
        btnBack = findViewById(R.id.btnBack);
        question = findViewById(R.id.question);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnSubmit = findViewById(R.id.btnSubmit);
        optionOne = findViewById(R.id.optionOne);
        optionTwo = findViewById(R.id.optionTwo);
        optionThree = findViewById(R.id.optionThree);
        optionFour = findViewById(R.id.optionFour);
        timeRemain = findViewById(R.id.timeRemain);
        quesLeftNo = findViewById(R.id.quesLeftNo);
        quesSkipNo = findViewById(R.id.quesSkipNo);
        chronometer = findViewById(R.id.chronometer);
        editOne = findViewById(R.id.editOne);
        edit_txt_lin_lay = findViewById(R.id.edit_txt_lin_lay);
        option_lin_lay = findViewById(R.id.option_lin_lay);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        optionOne.setOnClickListener(this);
        optionTwo.setOnClickListener(this);
        optionThree.setOnClickListener(this);
        optionFour.setOnClickListener(this);
        fetchData();
        arrayList_ALL = handler.getCaseStudyQuestion(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_MASTER, "");
        arrayList_Ans = handler.getCaseStudyAnswers(DatabaseHelper.TABLE_CASE_STUDY_ANSWER_MASTER);
        quesLeftNo.setText(String.valueOf(Integer.parseInt(arrayList_ALL.get(0).getTotal_questions()) - 1));
        quesSkipNo.setText(String.valueOf(arrayList_Ans.size()));
        tot_ques = Integer.parseInt(arrayList_ALL.get(0).getTotal_questions()) - 1;
        // timer();
        chrono_timer();
        if (count == 1) {
            btnPrev.setVisibility(View.GONE);
        } else {
            btnPrev.setVisibility(View.VISIBLE);
        }
        if (count == Integer.parseInt(arrayList_ALL.get(0).getTotal_questions())) {
            btnNext.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);

        }

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnPrev:
                if (tot_ques < Integer.parseInt(arrayList_ALL.get(0).getTotal_questions()) - 1) {
                    tot_ques++;
                    quesLeftNo.setText(String.valueOf(tot_ques));
                }
                if (count > 1) {
                    count--;
                    fetchData();
                    if (skip_count > 0) {
                        if (click_bool) {
                            click_bool = false;
                            skip_bool = true;
                        } else {
                            skip_bool = false;
                        }
                        if (!skip_bool) {
                            --skip_count;
                            quesSkipNo.setText(String.valueOf((skip_count)));
                        }
                    }

                    Log.e("list_sizePRev", "size" + arrayList.size() + "count" + count);
                }
                if (count == 1) {
                    btnPrev.setVisibility(View.GONE);
                } else {
                    btnPrev.setVisibility(View.VISIBLE);
                }
                btnNext.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
                optionOne.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionTwo.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionThree.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionFour.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                break;
            case R.id.btnNext:
                btnPrev.setVisibility(View.VISIBLE);
                if (tot_ques > 0) {
                    tot_ques--;
                    quesLeftNo.setText(String.valueOf(tot_ques));
                }

                if (count < Integer.parseInt(arrayList_ALL.get(0).getTotal_questions())) {
                    count++;
                    Log.e("list_sizeNExt", "size" + arrayList.size() + "count" + count);
                    fetchData();
                    if (skip_count < Integer.parseInt(arrayList_ALL.get(0).getTotal_questions())) {
                        if (click_bool) {
                            skip_bool = true;
                            click_bool = false;
                        } else {
                            skip_bool = false;
                        }
                        if (!skip_bool) {
                            ++skip_count;
                            quesSkipNo.setText(String.valueOf((skip_count)));
                        }
                    }


                }
                if (count == Integer.parseInt(arrayList_ALL.get(0).getTotal_questions())) {
                    btnNext.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                } else {
                    btnNext.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);

                }
                editOne.setText("");
                optionOne.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionTwo.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionThree.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionFour.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                break;
            case R.id.btnSubmit:
                arrayList_AnsFinal = handler.getCaseStudyAnswers(DatabaseHelper.TABLE_CASE_STUDY_ANSWER_MASTER);
//                timeinMin = String.format("%d",
//                        TimeUnit.MILLISECONDS.toMinutes(diff)
//                );
                try {
                    Post_answersApi(arrayList_AnsFinal, chrono_time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.optionOne:
                optionOne.setBackgroundColor(ContextCompat.getColor(this, R.color.color_light_Sky_blue));
                optionTwo.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionThree.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionFour.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                addAnswerData(0);

                click_bool = true;
                break;
            case R.id.optionTwo:
                optionTwo.setBackgroundColor(ContextCompat.getColor(this, R.color.color_light_Sky_blue));
                optionOne.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionThree.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionFour.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                addAnswerData(1);

                click_bool = true;
                break;
            case R.id.optionThree:
                optionThree.setBackgroundColor(ContextCompat.getColor(this, R.color.color_light_Sky_blue));
                optionOne.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionTwo.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionFour.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                addAnswerData(2);

                click_bool = true;
                break;
            case R.id.optionFour:
                optionFour.setBackgroundColor(ContextCompat.getColor(this, R.color.color_light_Sky_blue));
                optionOne.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionTwo.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                optionThree.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
                addAnswerData(3);

                click_bool = true;
                break;
            default:
                break;
        }
    }

    public void fetchData() {
        arrayList = handler.getCaseStudyQuestion(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_MASTER, String.valueOf(count));
        try {
            arrayListOptions = handler.getCaseStudyOptions(DatabaseHelper.TABLE_CASE_STUDY_OPTION_MASTER, arrayList.get(0).getQues_id());
            if (arrayList.size() > 0) {
                Log.e("arrayOPSize();", "<><><" + arrayListOptions.size());
                if (arrayList.get(0).getCase_type().equalsIgnoreCase("1")) {
                    option_lin_lay.setVisibility(View.VISIBLE);
                    edit_txt_lin_lay.setVisibility(View.GONE);
                } else {
                    option_lin_lay.setVisibility(View.GONE);
                    edit_txt_lin_lay.setVisibility(View.VISIBLE);
                    editOne.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!editOne.getText().toString().equalsIgnoreCase("")) {
                                addAnswerInputdata();
                                click_bool = true;
                            }
                        }
                    });
                }
                if (arrayList.size() > 0) {
                    question.setText(arrayList.get(0).getQues_name());
                    title.setText(arrayList.get(0).getCase_name());
                }
                if (arrayListOptions.size() > 0) {
                    Log.e("arrayOP();", "<><><" + arrayListOptions.get(0).getOption_name());
                    optionOne.setText(arrayListOptions.get(0).getOption_name());
                    optionTwo.setText(arrayListOptions.get(1).getOption_name());
                    optionThree.setText(arrayListOptions.get(2).getOption_name());
                    optionFour.setText(arrayListOptions.get(3).getOption_name());
                }
            }
        } catch (IndexOutOfBoundsException ib) {
            ib.printStackTrace();
        }

    }

    public void addAnswerData(int i) {
        CaseStudyListModel model_anss = new CaseStudyListModel();
        model_anss.setOption_id(arrayListOptions.get(i).getOption_id());
        model_anss.setQues_id(arrayListOptions.get(i).getQues_id());
        model_anss.setOption_name("");
        model_anss.setAnswer("");

        if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CASE_STUDY_ANSWER_MASTER, DatabaseHelper.ques_id, arrayListOptions.get(i).getQues_id())) {

            handler.AddCaseStudyAnswerList(model_anss, true, false);

            Log.e("ADDED_OPAns", true + " " + model_anss.getQues_id());
        } else {
            Log.e("UPDATED_OPAns", true + " " + model_anss.getQues_id());
            handler.AddCaseStudyAnswerList(model_anss, false, false);
        }
    }

    public void addAnswerInputdata() {
        arrayList = handler.getCaseStudyQuestion(DatabaseHelper.TABLE_CASE_STUDY_QUESTION_MASTER, String.valueOf(count));
        CaseStudyListModel model_ans = new CaseStudyListModel();
        model_ans.setOption_id("");
        model_ans.setQues_id(arrayList.get(0).getQues_id());
        model_ans.setOption_name("");
        model_ans.setAnswer(editOne.getText().toString());

        if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CASE_STUDY_ANSWER_MASTER, DatabaseHelper.ques_id, arrayList.get(0).getQues_id())) {

            handler.AddCaseStudyAnswerList(model_ans, true, true);

            Log.e("ADDED_EDAns", true + " " + model_ans.getQues_id());
        } else {
            Log.e("UPDATED_EDAns", true + " " + model_ans.getQues_id());
            handler.AddCaseStudyAnswerList(model_ans, false, true);
        }
        arrayList_AnsFinal = handler.getCaseStudyAnswers(DatabaseHelper.TABLE_CASE_STUDY_ANSWER_MASTER);
        Log.e("ADDED_Ans", arrayList_AnsFinal.get(0).getAnswer());
    }

    private void Post_answersApi(ArrayList<CaseStudyListModel> arrayList_AnsFinal, String chrono_time) throws JSONException {
        // String timeRem= timeRemain.getText().toString();
        int skip_ques = Integer.parseInt(arrayList_ALL.get(0).getTotal_questions()) - arrayList_AnsFinal.size();

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(Question_Answers_Activity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("case_id", case_id);
        params.put("case_type", arrayList_ALL.get(0).getCase_type());
        params.put("total_questions", arrayList_ALL.get(0).getTotal_questions());
        params.put("attempt_questions", String.valueOf(arrayList_AnsFinal.size()));
        params.put("skipped_questions", String.valueOf(skip_ques));
        params.put("time_durations", chrono_time);
        // params.put("product_image", "");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < arrayList_AnsFinal.size(); i++) {
            Log.e("ListAns", "<><><" + arrayList_AnsFinal.get(i).getAnswer() + "???" + arrayList_AnsFinal.get(i).getQues_id());
            JSONObject param = new JSONObject();
            param.put("question_id", arrayList_AnsFinal.get(i).getQues_id());
            param.put("options_id", arrayList_AnsFinal.get(i).getOption_id());
            param.put("input_answer", arrayList_AnsFinal.get(i).getAnswer());
            jsonArray.put(param);
        }
        params.put("Answers", jsonArray);
        header.put("Cynapse", params);

        new Post_answersApi(Question_Answers_Activity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    if (res_code.equals("1")) {
                        handler.deleteTableName(DatabaseHelper.TABLE_CASE_STUDY_ANSWER_MASTER);
                        finish();
                        CaseStudyDetailsActivity.fa.finish();
                        if (Integer.parseInt(timeinMin) > 0) {

                            MyToast.toastLong(Question_Answers_Activity.this, res_msg + "You took " + timeinMin + " minutes and  "+time_in_min_sec+" seconds extra than the alloted time!");

                        } else if (Integer.parseInt(timeinSec) > 0) {
                            MyToast.toastLong(Question_Answers_Activity.this, res_msg + "You took " + timeinSec + " seconds extra than the alloted time!");

                        } else {
                            MyToast.toastLong(Question_Answers_Activity.this, res_msg);
                        }

                    } else {
                        MyToast.toastLong(Question_Answers_Activity.this, res_msg);
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

    public void timer() {
        String givenDateString = arrayList.get(0).getTime_durations();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mma");
//        try {
//            Date mDate = sdf.parse(givenDateString);
//            timeInMilliseconds = mDate.getTime();
//            System.out.println("Date in milli :: " + timeInMilliseconds);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        final long timeinmillis = TimeUnit.MINUTES.toMillis(Long.parseLong(givenDateString));

        Log.d("timeinmillis", "value in millisecinds " + timeinmillis);
        countDownTimer = new CountDownTimer(timeinmillis, 100) {
            public void onTick(long ms) {
                String text = String.format(Locale.getDefault(), " %02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(ms) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(ms) % 60);
                //txt = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
                timeRemain.setText(text);
                //Log.d("txtVal",">>>"+txt);
                Log.d("textVal", "<<<" + text);
                diff = timeinmillis - ms;
                Log.d("Diffrence in time", "<<<" + diff);
            }

            public void onFinish() {
                //timer_txt.setText("0");

            }
        }.start();
    }

    void chrono_timer() {
        // final String givenDateString = arrayList.get(0).getTime_durations();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                //  cArg.setText(hh+":"+mm+":"+ss);
                cArg.setText(mm + ":" + ss);
                chrono_time = mm + ":" + ss;
//                if(mm.equalsIgnoreCase("01") && ss.equalsIgnoreCase("00"))
//                {
//                    chronometer.stop();
//                }
                if (time > Long.parseLong(arrayList.get(0).getTime_durations()) * 60000) {
                    diff = time - (Long.parseLong(arrayList.get(0).getTime_durations()) * 60000);
                    if (diff > 60000) {
                        timeinMin = String.format("%d",
                                TimeUnit.MILLISECONDS.toMinutes(diff)
                        );
//                        time_in_min_sec = String.format("%d",
//                                TimeUnit.MILLISECONDS.toMinutes(diff) % 60
//                        );
                        time_in_min_sec = String.valueOf(s);
                        timeinSec = "0";
                    } else {
                        timeinSec = String.format("%d",
                                TimeUnit.MILLISECONDS.toSeconds(diff)
                        );
                        timeinMin = "0";
                    }
                }

                Log.d("Time_durations", "<<<" + Long.parseLong(arrayList.get(0).getTime_durations()) * 60000);
                Log.d("Diffrence in time", "<<<" + time);
                Log.d("Diffrence in chronotime", "<<<" + diff);

            }
        });
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();


    }
    public void getSelected(String option)
    {
        CaseStudyListModel model_ans = new CaseStudyListModel();
        model_ans.setId(String.valueOf(count));
        model_ans.setOption_id(option);
        arrayList_selected.add(model_ans);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
//            countDownTimer.cancel();
            handler.deleteTableName(DatabaseHelper.TABLE_CASE_STUDY_ANSWER_MASTER);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }

    }
}
