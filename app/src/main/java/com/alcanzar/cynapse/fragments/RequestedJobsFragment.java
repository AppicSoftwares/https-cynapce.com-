package com.alcanzar.cynapse.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MyJobActivity;
import com.alcanzar.cynapse.adapter.RequestPostJobAdapter;
import com.alcanzar.cynapse.api.RequestJobListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestedJobsFragment extends Fragment {
    //TODO: recycleView and other Views
    RecyclerView recyclerView;
    TextView no_record_txt;
    LinearLayoutManager linearLayoutManager;
    ArrayList<JobMasterModel> arrayList = new ArrayList<>();
    ArrayList revarrayList2=new ArrayList();



    public RequestedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requested_jobs, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: recycle view and listings
        no_record_txt = view.findViewById(R.id.no_record_txt);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(linearLayoutManager);
        try {
            RequestJobListApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setData();
//        for(int i = 0;i<=10;i++){
//            arrayList.add(new DashBoardModel(getResources().getString(R.string.senior_resident_rmo),""));
//        }

    }
    private void RequestJobListApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
         params.put("uuid", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.UserId,""));
        // params.put("uuid", "S75f3");
        // params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.sync_time,""));
        params.put("sync_time", "");
        header.put("Cynapse",params);
        new RequestJobListApi(getActivity(),header){
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    //AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.sync_time,sync_time);
                    Log.d("JOBSPONSE",response.toString());

                    if(res_code.equals("1")){
                      //  MyToast.toastLong(getActivity(),res_msg);
                        JSONArray header2 = header.getJSONArray("RequestJobList");
                        for(int i = header2.length()-1;i>=0;i--){
                            JSONObject item  = header2.getJSONObject(i);
                            JobMasterModel model = new JobMasterModel();
                            model.setId(item.getString("id"));
                            model.setJob_id(item.getString("jobId"));
                            model.setMedical_profile_id(item.getString("medical_profile_id"));
                            model.setMedical_profile_name(item.getString("medical_profile_name"));
                            model.setJob_title(item.getString("job_title"));
                            model.setJob_type(item.getString("job_type"));
                            model.setJob_type_name(item.getString("job_type_name"));
                            model.setSpecialization_id(item.getString("specialization_id"));
                            model.setSpecialization_name(item.getString("specialization_name"));
                            model.setSub_specialization_id(item.getString("sub_specialization_id"));
                            model.setSub_specialization_name(item.getString("sub_specialization_name"));
                            model.setYear_of_experience(item.getString("year_of_experience"));
                            model.setCurrent_ctc(item.getString("current_ctc"));
                            model.setExpected_ctc(item.getString("expected_ctc"));
                            model.setCurrent_employer(item.getString("current_employer"));
                            //model.setLocation(item.getString("location"));
                            model.setLocation(item.getString("preferred_location"));
                            model.setPreferred_location(item.getString("preferred_location"));
                            model.setResume( item.getString("resume"));
                            //model.setJob_description(item.getString("job_description"));
                            model.setJob_description(item.getString("resume_description"));
                            model.setSkills_required(item.getString("skills_required"));
                            model.setAdd_date(Util.getDateNew(Long.parseLong(item.getString("add_date"))* 1000));
                            model.setModify_date(item.getString("modify_date"));
                            model.setStatus(item.getString("status"));
                            model.setDepartment_id(item.getString("department_id"));
                            model.setDepartment_name(item.getString("department_name"));
                            model.setPost_req_status("1");
                            if (!((MyJobActivity)getActivity()).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_JOBS_MASTER,DatabaseHelper.id, item.getString("id")))
                            {

                                ((MyJobActivity)getActivity()).handler.AddJobMaster(model, true);

                                //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                            } else {
                                //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                ((MyJobActivity)getActivity()).handler.AddJobMaster(model, false);
                            }
                        }

                        setData();

                        // myDealsAdapter.notifyDataSetChanged();
                    }else {

                       // MyToast.toastLong(getActivity(),res_msg);
                    }
                }
                catch (JSONException e){
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
        arrayList = ((MyJobActivity)getActivity()).handler.getJobList(DatabaseHelper.TABLE_JOBS_MASTER, "1","1");
        Collections.reverse(arrayList);

        if(arrayList.size() == 0)
        {
            no_record_txt.setVisibility(View.VISIBLE);
            no_record_txt.setText("No Requested Jobs Found");
        }
        else
        {
            no_record_txt.setVisibility(View.GONE);
            Log.e("menulist.size();","<><><"+arrayList.size());

            RequestPostJobAdapter requestPostJobAdapter = new RequestPostJobAdapter(getActivity(),R.layout.job_row,arrayList, "1");
            recyclerView.setAdapter(requestPostJobAdapter);
        }
    }
}
