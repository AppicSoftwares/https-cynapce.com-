package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ChatMessageAdapter;
import com.alcanzar.cynapse.api.GetChatMessageApi;
import com.alcanzar.cynapse.api.PostChatMessageApi;
import com.alcanzar.cynapse.model.ChatMessege_Setter_Getter;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    //TODO : header views
    TextView title;
    EditText msgText;
    ImageView btnBack,titleIcon;
    Button btnSearch,btnSend;
    String sender_id="",prod_id="",reciever_id="",chat_id="";
    //TODO : recycle and listing views
    RecyclerView recycleView_chat;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ChatMessege_Setter_Getter> chatMessege_setter_getters = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        //TODO : initializing and setting the header views
        if(getIntent()!=null) {
            sender_id = getIntent().getStringExtra("sender_id");
            reciever_id = getIntent().getStringExtra("reciever_id");
            prod_id = getIntent().getStringExtra("prod_id");
            chat_id = getIntent().getStringExtra("chat_id");
        }

        btnBack = findViewById(R.id.btnBack);
        msgText = findViewById(R.id.msgText);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.chat);
        title = findViewById(R.id.title);
        title.setText(R.string.chat_title);
        btnSearch = findViewById(R.id.btnSearch);
        btnSend = findViewById(R.id.btnSend);
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnSend.setVisibility(View.VISIBLE);
        //TODO : recycle and adapter control
        recycleView_chat = findViewById(R.id.recycleView_chat);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView_chat.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSend:
                if (Util.isEmpty(msgText)) {
                    btnSend.setEnabled(false);

                } else {
                    btnSend.setEnabled(true);
                    try {
                        postChatMsgApi(sender_id,prod_id,msgText.getText().toString(),reciever_id,chat_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    Handler handle = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try
            {
                try {
                    getChatMessageApi(sender_id,prod_id,reciever_id,chat_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (NullPointerException ne)
            {
                ne.printStackTrace();
            }

        }
    };
    @Override
    public void onPause() {
        handle.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.e("OnResume====", "=");
        handle.postDelayed(runnable, 0);
        super.onResume();
    }

    private void getChatMessageApi(final String sender_id, String prod_id, String reciever_id, String chat_id) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("sender_uuid", AppCustomPreferenceClass.readString(MessageActivity.this, AppCustomPreferenceClass.UserId, ""));
       // params.put("reciever_uuid", reciever_id);
        params.put("product_id", prod_id);
        params.put("chat_id", chat_id);
        params.put("sync_time","");
        header.put("Cynapse", params);

        new GetChatMessageApi(MessageActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    if (res_code.equals("1")) {

                        handle.postDelayed(runnable, 5000);
                        chatMessege_setter_getters = new ArrayList<>();
                        JSONArray items = header.getJSONArray("getChat");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject obj1 = items.getJSONObject(i);
                            ChatMessege_Setter_Getter event = new ChatMessege_Setter_Getter();
                            event.setId(obj1.getString("id"));
                            event.setMessage(obj1.getString("chatmessage"));
                            event.setProduct_id(obj1.getString("product_id"));
                           // event.setDate(Util.getDate(obj1.getString("date")));
                           //event.setTime(obj1.getString("time"));
                            event.setSender_id(obj1.getString("sender_uuid"));
                           // event.setReciever_id(obj1.getString("reciever_uuid"));
                            Log.d("sender_id","");
                            event.setAdd_date(Util.getTimeNew(Long.parseLong(obj1.getString("add_date"))*1000));
                            if (AppCustomPreferenceClass.readString(MessageActivity.this, AppCustomPreferenceClass.UserId, "")
                                    .equals(obj1.getString("sender_uuid"))) {
                                if (!obj1.getString("chatmessage").equals("")) {
                                    event.setView_type(1);
                                }
                            } else {
                                if (!obj1.getString("chatmessage").equals("")) {
                                    event.setView_type(2);
                                }


                            }


                            chatMessege_setter_getters.add(event);


                        }
                        ChatMessageAdapter friendAdapter = new ChatMessageAdapter(MessageActivity.this, chatMessege_setter_getters/*,userIdMyFriend*/);
                        recycleView_chat.setAdapter(friendAdapter);
                        recycleView_chat.scrollToPosition(chatMessege_setter_getters.size() - 1);


                    } else {
                       // MyToast.toastLong(MessageActivity.this, res_msg);
                        handle.postDelayed(runnable, 5000);
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
    private void postChatMsgApi(final String sender_id, String prod_id, String msg, String reciever_id, String chat_id) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("sender_uuid", AppCustomPreferenceClass.readString(MessageActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("reciever_uuid", sender_id);
        params.put("product_id", prod_id);
        params.put("chat_id", chat_id);
        params.put("chatmessage",msg);
        header.put("Cynapse", params);
        msgText.setText("");
        new PostChatMessageApi(MessageActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    if (res_code.equals("1")) {

                       // handle.postDelayed(runnable, 5000);
                       // chatMessege_setter_getters = new ArrayList<>();
//                        JSONArray items = header.getJSONArray("PostChat");
//                        for (int i = 0; i < items.length(); i++) {
//                            JSONObject obj1 = items.getJSONObject(i);
//                            ChatMessege_Setter_Getter event = new ChatMessege_Setter_Getter();
//                            event.setId(obj1.getString("id"));
//                            event.setMessage(obj1.getString("chatmessage"));
//                            event.setProduct_id(obj1.getString("product_id"));
//                           // event.setDate(Util.getDate(obj1.getString("date")));
//                           //event.setTime(obj1.getString("time"));
//                          //  event.setSender_id(obj1.getString("sender_uuid"));
//                           // event.setReciever_id(obj1.getString("reciever_uuid"));
////                            if (sender_id.equals(obj1.getString("sender_uuid"))) {
////                                if (!obj1.getString("chatmessage").equals("")) {
////                                    event.setView_type(2);
////                                }
////                            } else {
////                                if (!obj1.getString("chatmessage").equals("")) {
////                                    event.setView_type(1);
////                                }
////
////
////                            }
//
//
//                            chatMessege_setter_getters.add(event);
//
//
//                        }
//                        ChatMessageAdapter friendAdapter = new ChatMessageAdapter(MessageActivity.this, chatMessege_setter_getters/*,userIdMyFriend*/);
//                        recycleView_chat.setAdapter(friendAdapter);
//                        recycleView_chat.scrollToPosition(chatMessege_setter_getters.size() - 1);


                    } else {
                       // MyToast.toastLong(MessageActivity.this, res_msg);
                        handle.postDelayed(runnable, 5000);
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
