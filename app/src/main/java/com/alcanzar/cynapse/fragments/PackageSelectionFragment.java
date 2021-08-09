package com.alcanzar.cynapse.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.ReviewBookingNew;
import com.alcanzar.cynapse.activity.SelectPersonPackageActivity;
import com.alcanzar.cynapse.adapter.TicketsPackageAdapter;
import com.alcanzar.cynapse.adapter.TicketsPackageForgeinAdapter;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackageSelectionFragment extends Fragment {


    private String title;
    private ImageView btnBack;
    private int page;
    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String PAGE_NO = "PAGE_NO";
    private TextView textViewCheck;
    private DatabaseHelper handler;
    public ArrayList<ConferencePackageModel> conferencePackageModels = null;
    public ArrayList<ConferencePackageModelForgein> conferencePackageModelsForgein = null;
    private static String conferenceId;
    private RecyclerView recycleView;

    public static PackageSelectionFragment newInstance(int page, String conferenceID) {
        PackageSelectionFragment packageSelectionFragment = new PackageSelectionFragment();
        Bundle args = new Bundle();
//        args.putInt(PAGE_NO,page);
//        args.putString(TITLE_KEY,conferenceID);
        page = page;
        conferenceId = conferenceID;
        packageSelectionFragment.setArguments(args);
        return packageSelectionFragment;
    }

    public PackageSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_package_selection, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        page = getArguments().getInt(PAGE_NO);
//        title = getArguments().getString(TITLE_KEY);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        recycleView = view.findViewById(R.id.recycleView);
        btnBack = view.findViewById(R.id.btnBack);

        handler = new DatabaseHelper(getActivity());

        recycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycleView.setHasFixedSize(true);
        recycleView.setNestedScrollingEnabled(false);

        conferencePackageModels = handler.getNormalPackDetails(conferenceId);

        recycleView.setAdapter(new PackageAdapter(getActivity(), conferencePackageModels, false));


    }

    public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyViewHolder> {

        public Context context;
        private ArrayList<ConferencePackageModel> packageList;

        boolean flag;

        public PackageAdapter(Context context, ArrayList<ConferencePackageModel> packageList, boolean flag) {

            this.context = context;
            this.packageList = packageList;
            this.flag = flag;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tickects_packages_items, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PackageAdapter.MyViewHolder holder, int position) {

            holder.ticketsname.setText(packageList.get(position).getConference_pack_day());
            holder.ticketsprice.setText(packageList.get(position).getConference_pack_charge());

            if (flag) {
                holder.cardticketpackageitems.setBackgroundColor(Color.parseColor("#FFF0EDED"));
                holder.imgarrow.setVisibility(View.VISIBLE);
//            holder.ticketsprice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                holder.priceindicator.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return packageList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView ticketsname, ticketsprice;
            CardView cardticketpackageitems;
            ImageView imgarrow;
            ImageView priceindicator;

            public MyViewHolder(View view) {
                super(view);
                ticketsname = view.findViewById(R.id.tickectspackagename);
                ticketsprice = view.findViewById(R.id.tickectspackageprice);
                cardticketpackageitems = view.findViewById(R.id.cardticketpackageitems);
                imgarrow = view.findViewById(R.id.imgarrow);
                priceindicator = view.findViewById(R.id.priceindicator);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ReviewBookingNew.packageList.clear();
                        ReviewBookingNew.packageList.add(ticketsname.getText().toString());
                        ReviewBookingNew.packageList.add(ticketsprice.getText().toString());
                        ReviewBookingNew.packageList.add("1");

                        Intent intent = new Intent();
                        intent.putExtra("packageList", ReviewBookingNew.packageList.toString());
                        SelectPersonPackageActivity.activity.setResult(501, intent);
                        SelectPersonPackageActivity.activity.finish();
                    }
                });
            }
        }
    }
}
