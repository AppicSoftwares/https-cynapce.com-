package com.alcanzar.cynapse.fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.DashBoardAdapter;
import com.alcanzar.cynapse.model.DashBoardModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    //TODO : this is used for the recycler listing purpose
    RecyclerView recycleView;
    ArrayList<DashBoardModel> modelArrayList = new ArrayList<>();
    DashBoardAdapter dashBoardAdapter;
    LinearLayoutManager mLayoutManager;
    public DashBoardFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash_board, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        //TODO : calling the dashboard adapter here
        for(int i=0;i<10;i++){
           // Log.e("loop","add dummy data");
            modelArrayList.add(new DashBoardModel("This is demo message","img"));
        }
        mLayoutManager=new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView = view.findViewById(R.id.recycleView);
        recycleView.setLayoutManager(mLayoutManager);
        dashBoardAdapter = new DashBoardAdapter(getContext(),modelArrayList,R.layout.timeline_row);
        recycleView.setAdapter(dashBoardAdapter);
    }
}
