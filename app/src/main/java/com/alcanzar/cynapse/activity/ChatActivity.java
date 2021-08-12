package com.alcanzar.cynapse.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ChatAdapter;
import com.alcanzar.cynapse.api.GetMessageApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.NotificationDashBoardModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatActivity extends Activity implements View.OnClickListener{
    //TODO : header views
    TextView title;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    DatabaseHelper handler;
    //TODO : recycle and listing views
    RecyclerView recycleView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<NotificationDashBoardModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        handler = new DatabaseHelper(this);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.chat);
        title = findViewById(R.id.title);
        title.setText(R.string.chat_title);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        //TODO : recycle and adapter control
        recycleView = findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(linearLayoutManager);
        try {
            GetMessageApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arrayList = handler.getChatNotification(DatabaseHelper.TABLE_CHAT_NOTIFICATION_MASTER, "");
        if (arrayList.size() > 0) {
            setArrayList();
        }
    }
    private void GetMessageApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("sender_uuid", AppCustomPreferenceClass.readString(ChatActivity.this, AppCustomPreferenceClass.UserId, ""));
        // params.put("uuid","S75f3");
        //params.put("sync_time", AppCustomPreferenceClass.readString(ChatActivity.this,AppCustomPreferenceClass.chat_noti_sync_time,""));
        header.put("Cynapse", params);
        new GetMessageApi(ChatActivity.this, header,false) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                   // AppCustomPreferenceClass.writeString(ChatActivity.this,AppCustomPreferenceClass.chat_noti_sync_time,sync_time);
                    Log.d("NOTIFICATION", response.toString());
                    if (res_code.equals("1")) {
                        JSONArray jsonArray = header.getJSONArray("Messages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            NotificationDashBoardModel model = new NotificationDashBoardModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            model.setId(jsonObject.getString("id"));
                           // model.setUuid(jsonObject.getString("uuid"));
                            model.setProduct_id(jsonObject.getString("product_id"));
                            model.setName(jsonObject.getString("name"));
                            model.setSender_id(jsonObject.getString("sender_id"));
                            model.setReciever_id(jsonObject.getString("reciever_id"));
                            model.setMsg(jsonObject.getString("message"));
                            model.setStatus(jsonObject.getString("status"));
                            model.setAdd_date(jsonObject.getString("add_date"));
                            model.setChat_id(jsonObject.getString("chat_id"));
                            model.setModify_date(jsonObject.getString("modify_date"));
                            model.setTime(Util.getDateNew(Long.parseLong(jsonObject.getString("modify_date"))*1000));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CHAT_NOTIFICATION_MASTER,DatabaseHelper.id, jsonObject.getString("id")))
                            {

                                handler.AddChatNotificationMaster(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                handler.AddChatNotificationMaster(model, false);
                            }
                            // long dateDiff = Long.parseLong(modifyDate) - Long.parseLong(addDate);

                            //  long secondsInMilli = 1000;
                            // long elapsedSeconds = dateDiff / secondsInMilli;
                            //   Log.d("ELSPSESECON",elapsedSeconds+"");
                            // arrayList.add(new NotificationDashBoardModel(message,id,String.valueOf(elapsedSeconds),msgType));
                            arrayList = handler.getChatNotification(DatabaseHelper.TABLE_CHAT_NOTIFICATION_MASTER, "");
                            setArrayList();


                        }

                        //adapter.notifyDataSetChanged();
                    } else {

                        // MyToast.toastLong(ChatActivity.this, res_msg);
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
        ChatAdapter chatAdapter =  new ChatAdapter(ChatActivity.this,R.layout.chat_row,arrayList);
        recycleView.setAdapter(chatAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
