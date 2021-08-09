package com.alcanzar.cynapse.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MyCaseStudiesAdapter;
import com.alcanzar.cynapse.api.Get_all_casesApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.CaseStudyListModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseStudiesFragment extends Fragment {
    //TODO : recycle and other views
    RecyclerView recycleView;
    DatabaseHelper handler;
    TextView no_record_txt;
    LinearLayoutManager linearLayoutManager;
    ArrayList<CaseStudyListModel> arrayList = new ArrayList<>();

    public CaseStudiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_case_studies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: recycle and other initialization
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        handler = new DatabaseHelper(getActivity());
        recycleView = view.findViewById(R.id.recycleView);
        no_record_txt = view.findViewById(R.id.no_record_txt);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(linearLayoutManager);
        try {
            Get_all_casesApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //TODO: Demo data
//        for(int i = 0;i<= 20;i++){
//            arrayList.add(new DashBoardModel("CASE 1","id"));
//        }
//        MyCaseStudiesAdapter myCaseStudiesAdapter = new MyCaseStudiesAdapter(getActivity(),R.layout.my_case_studies_row,arrayList);
//        recycleView.setAdapter(myCaseStudiesAdapter);
        arrayList = handler.getCaseStudyList(DatabaseHelper.TABLE_CASE_STUDY_LIST_MASTER, "1");
        if(arrayList.size() > 0)
        {
            MyCaseStudiesAdapter myCaseStudiesAdapter = new MyCaseStudiesAdapter(getActivity(), R.layout.my_case_studies_row, arrayList, "from_case");
            recycleView.setAdapter(myCaseStudiesAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Get_all_casesApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Get_all_casesApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.sync_time_cases, ""));
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new Get_all_casesApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sync_time_cases, sync_time);
                    Log.d("STATERESPONSE", response.toString());

                    if (res_code.equals("1")) {
                        // MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("case");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            CaseStudyListModel model = new CaseStudyListModel();
                            model.setId(item.getString("case_id"));
                            model.setCase_name(item.getString("case_name"));
                            model.setCase_sub_title(item.getString("case_sub_title"));
                            model.setAdd_date(item.getString("add_date"));
                            model.setModify_date(item.getString("modify_date"));
                            model.setCase_type(item.getString("case_type"));
                            model.setDescription(item.getString("description"));
                            model.setAttend_status(item.getString("attend_status"));
                            model.setStatus(item.getString("status"));
                            if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_CASE_STUDY_LIST_MASTER, DatabaseHelper.id, item.getString("case_id"))) {

                                handler.AddCaseList(model, true);

                                    Log.e("ADDED_Sub_item", true + " " + model.getId());
                            } else {
                                   Log.e("UPDATED", true + " " + model.getId());
                                handler.AddCaseList(model, false);
                            }
                        }
                        arrayList = handler.getCaseStudyList(DatabaseHelper.TABLE_CASE_STUDY_LIST_MASTER, "1");
                        Log.e("menulist.size();", "<><><" + arrayList.size());
                        if (arrayList.size() == 0) {
                            no_record_txt.setVisibility(View.VISIBLE);
                            no_record_txt.setText("No Case Studies");
                        } else {
                            no_record_txt.setVisibility(View.GONE);
                        }
                        MyCaseStudiesAdapter myCaseStudiesAdapter = new MyCaseStudiesAdapter(getActivity(), R.layout.my_case_studies_row, arrayList, "from_case");
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
