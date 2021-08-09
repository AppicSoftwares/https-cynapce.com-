package com.alcanzar.cynapse.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.NotificationAdapter;
import com.alcanzar.cynapse.api.GetNotificationApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.NotificationDashBoardModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO : header views
    TextView title;
    DatabaseHelper handler;
    ImageView btnBack, titleIcon;
    Button btnSearch;
    //TODO : recycle and listing views
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<NotificationDashBoardModel> arrayList = new ArrayList<>();
    public static Activity noti;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        //TODO : initializing and setting the header views
        handler = new DatabaseHelper(this);
        noti = this;
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.notification_white);
        title = findViewById(R.id.title);
        title.setText(R.string.notify);
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
            getNotificationApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrayList = handler.getNotification(DatabaseHelper.TABLE_NOTIFICATION_MASTER, "");

        if (arrayList.size() > 0) {
            setArrayList();
        }
//        NotificationAdapter notificationAdapter = new NotificationAdapter(getApplicationContext(), R.layout.notification_row, arrayList);
//        recyclerView.setAdapter(notificationAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getNotificationApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNotificationApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(NotificationsActivity.this, AppCustomPreferenceClass.UserId, ""));
       // params.put("uuid","S75f3");
        //params.put("sync_time","");
        params.put("sync_time", AppCustomPreferenceClass.readString(NotificationsActivity.this,AppCustomPreferenceClass.noti_sync_time,""));
        header.put("Cynapse", params);
        new GetNotificationApi(NotificationsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(NotificationsActivity.this,AppCustomPreferenceClass.noti_sync_time,sync_time);
                    Log.d("NOTIFICATIONAAA", response.toString());
                    if (res_code.equals("1")) {
                        JSONArray jsonArray = header.getJSONArray("Notification");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            NotificationDashBoardModel model = new NotificationDashBoardModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            model.setId(jsonObject.getString("id"));
                            model.setUuid(jsonObject.getString("uuid"));
                            model.setProduct_id(jsonObject.getString("product_id"));
                            model.setSender_id(jsonObject.getString("sender_id"));
                            model.setChat_id(jsonObject.getString("chat_id"));
                            if(jsonObject.has("product_category_id"))
                            {
                                model.setProduct_category_id(jsonObject.getString("product_category_id"));
                            }
                            model.setMsg(jsonObject.getString("message"));
                            model.setStatus(jsonObject.getString("status"));
                            model.setAdd_date(jsonObject.getString("add_date"));
                            model.setModify_date(jsonObject.getString("modify_date"));
                            model.setMsgType(jsonObject.getString("msg_type"));
                            model.setTime(Util.getDateNew(Long.parseLong(jsonObject.getString("modify_date"))*1000));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_NOTIFICATION_MASTER,DatabaseHelper.id, jsonObject.getString("id")))
                            {

                                handler.AddNotificationMaster(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                handler.AddNotificationMaster(model, false);
                            }
                            // long dateDiff = Long.parseLong(modifyDate) - Long.parseLong(addDate);

                            //  long secondsInMilli = 1000;
                           // long elapsedSeconds = dateDiff / secondsInMilli;
                         //   Log.d("ELSPSESECON",elapsedSeconds+"");
                           // arrayList.add(new NotificationDashBoardModel(message,id,String.valueOf(elapsedSeconds),msgType));
                            arrayList = handler.getNotification(DatabaseHelper.TABLE_NOTIFICATION_MASTER, "");
                            setArrayList();


                        }

                        //adapter.notifyDataSetChanged();
                    } else {

                       // MyToast.toastLong(NotificationsActivity.this, res_msg);
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

    public void setArrayList()
    {
        NotificationAdapter notificationAdapter = new NotificationAdapter(getApplicationContext(), R.layout.notification_row, arrayList);
        recyclerView.setAdapter(notificationAdapter);
    }
}
