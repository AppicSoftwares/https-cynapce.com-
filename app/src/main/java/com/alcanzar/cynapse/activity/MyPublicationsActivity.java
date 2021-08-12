package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MyPublicationAdapter;
import com.alcanzar.cynapse.api.GetPublicationListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.MedicalWritingModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPublicationsActivity extends AppCompatActivity implements View.OnClickListener{
    //TODO : header views
    TextView title;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    TextView no_record_txt;
    //TODO : used for the listing purpose
    RecyclerView recyclerView;
    DatabaseHelper handler;
    LinearLayoutManager linearLayoutManager;
    ArrayList<MedicalWritingModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publications);
        //TODO : initializing and setting the header views
        handler = new DatabaseHelper(this);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.publication_white);
        title = findViewById(R.id.title);
        title.setText(R.string.medical_writing);
        no_record_txt = findViewById(R.id.no_record_txt);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
       // no_record_txt.setVisibility(View.VISIBLE);
       // no_record_txt.setText("Nothing To Show");
        //TODO : adapter and recycle control
        //demo arrayListValues
//        for (int i = 0;i<=20;i++){
//            arrayList.add(new DashBoardModel(getString(R.string.book_name),"id"));
//        }
        recyclerView = findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        try {
            GetPublicationList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setData();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }}
    private void GetPublicationList() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("sync_time", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.sync_time_publish_list, ""));
        header.put("Cynapse", params);
        new GetPublicationListApi(MyPublicationsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MyPublicationsActivity.this, AppCustomPreferenceClass.sync_time_publish_list, sync_time);
                    if (res_code.equals("1")) {
                        JSONArray header2 = header.getJSONArray("publication");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            MedicalWritingModel model = new MedicalWritingModel();
                            model.setId(item.getString("id"));
                            model.setTitle(item.getString("title"));
                            model.setImage(item.getString("image"));
                            model.setPublished_year(item.getString("published_year"));
                            model.setUrl(item.getString("url"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setModify_date(item.getString("modify_date"));
                            model.setStatus(item.getString("status"));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GETTING_PUBLISH_LIST, DatabaseHelper.id, item.getString("id"))) {

                                handler.AddGetPubListMaster(model, true);

                                Log.e("ADDED_Sub_item", true + " " + model.getId());
                            } else {
                                Log.e("UPDATED", true + " " + model.getId());
                                handler.AddGetPubListMaster(model, false);
                            }
                        }
                        setData();
                    } else {

                        //MyToast.toastLong(MainActivity.this,res_msg);
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
        arrayList = handler.getPublicationList(DatabaseHelper.TABLE_GETTING_PUBLISH_LIST, "1");
        Log.e("menulist.size();", "<><><" + arrayList.size());
        if (arrayList.size() == 0) {
            no_record_txt.setVisibility(View.VISIBLE);
            no_record_txt.setText(R.string.nothing_to_show);
        } else {
            no_record_txt.setVisibility(View.GONE);
        }
        MyPublicationAdapter myPublicationAdapter = new MyPublicationAdapter(MyPublicationsActivity.this,R.layout.my_publications_row,arrayList);
        recyclerView.setAdapter(myPublicationAdapter);
    }
    }
