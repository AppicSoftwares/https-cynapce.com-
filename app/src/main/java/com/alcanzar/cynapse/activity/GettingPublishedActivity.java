package com.alcanzar.cynapse.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;

public class GettingPublishedActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO : header and other views
    TextView title;
    ImageView btnBack,titleIcon;
    Button btnSearch , btnPublish;
    RelativeLayout relOne,relTwo;
    EditText name,email,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_published);
        //TODO : initializing and setting the header views
        initializeViews();
    }
    //TODO: here the initialization of the views are done
    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.publishedeye);
        relOne = findViewById(R.id.relOne);
        relTwo = findViewById(R.id.relTwo);
        title = findViewById(R.id.title);
        title.setText(R.string.getting_published);
        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        //TODO: setting the text by default
        name.setText(AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.name,""));
        email.setText(AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.email,""));
        phone.setText(AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.phoneNumber,""));
        btnPublish.setOnClickListener(this);
        btnSearch.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnPublish:
                //TODO: moving to the next page of publish section
                if(btnPublish.getText().toString().equals("NEXT")){
                    btnPublish.setText(R.string.publish);
                    //TODO: switching to the next page
                    relOne.setVisibility(View.GONE);
                    relTwo.setVisibility(View.VISIBLE);
                }
                else {
                    //TODO: calling the PublishProfileApi
                    postPublishProfileApi();
                    //TODO :opening the success pop up
                    final Dialog dialog = new Dialog(this, Window.FEATURE_NO_TITLE);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_msg_alert);
                    //TODO: used to make the background transparent
                    Window window = dialog.getWindow();
                    window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO : initializing dfferent views for the dialog
                    TextView title = dialog.findViewById(R.id.title);
                    TextView msg = dialog.findViewById(R.id.msg);
                    ImageView close = dialog.findViewById(R.id.close);
                    Button btnGotIt= dialog.findViewById(R.id.btnGotIt);
                    //TODO :setting different views
                    title.setText(R.string.congrats);
                    msg.setText(R.string.publishedSuccessfully);
                    dialog.show();
                    //TODO : dismiss the on btn click and close click
                    btnGotIt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //TODO : finishing the activity
                            finish();
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
                break;
        }}

    private void postPublishProfileApi() {
//        {
//            "Cynapse": {
//            "uuid":"S3994",
//                    "name":"himanshu",
//                    "email":"himanshu.gupta@gmail.com",
//                    "phone_number":"8090657936",
//                    "specilization_id":"1",
//                    "state_id":"36",
//                    "city_id":"3",
//                    "country_id":"IN",
//                    "message":"It hiering for doctor requierment"
//
//        }
//        }

    }
}
