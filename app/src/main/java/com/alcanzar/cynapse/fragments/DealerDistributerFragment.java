package com.alcanzar.cynapse.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.DealersDistAdapters;
import com.alcanzar.cynapse.model.DashBoardModel;

import java.util.ArrayList;

public class DealerDistributerFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DashBoardModel> arrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.activity_dealer_distribute,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //TODO: Demo data
        for(int i =0;i<=20;i++){
            arrayList.add(new DashBoardModel("Demo Name",""));
        }
        DealersDistAdapters dealersDistAdapters = new DealersDistAdapters(getActivity(),R.layout.my_dealers_distributers_row,arrayList);
        recyclerView.setAdapter(dealersDistAdapters);
    }
}
