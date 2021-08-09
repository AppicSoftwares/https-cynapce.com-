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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.ReviewBookingNew;
import com.alcanzar.cynapse.activity.SelectPersonPackageActivity;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForeignPackageSelectionFragment extends Fragment {


    private String title;
    private int page;
    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String PAGE_NO = "PAGE_NO";
    private TextView textViewCheck;
    private DatabaseHelper handler;
    public ArrayList<ConferencePackageModelForgein> conferencePackageModelsForgein = null;
    private static String conferenceId;
    private RecyclerView recycleView;


    public ForeignPackageSelectionFragment() {
        // Required empty public constructor
    }

    public static ForeignPackageSelectionFragment newInstance(int page, String conferenceID) {
        ForeignPackageSelectionFragment packageSelectionFragment = new ForeignPackageSelectionFragment();
        Bundle args = new Bundle();
//        args.putInt(PAGE_NO,page);
//        args.putString(TITLE_KEY,conferenceID);
        page = page;
        conferenceId = conferenceID;
        packageSelectionFragment.setArguments(args);
        return packageSelectionFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foreign_package_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recycleView = view.findViewById(R.id.recycleView);

        handler = new DatabaseHelper(getActivity());

        recycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycleView.setHasFixedSize(true);
        recycleView.setNestedScrollingEnabled(false);

        conferencePackageModelsForgein = handler.getForgeingPackDetails(conferenceId);

        recycleView.setAdapter(new PackageAdapterForgein(getActivity(), conferencePackageModelsForgein, false));
    }

    public class PackageAdapterForgein extends RecyclerView.Adapter<PackageAdapterForgein.MyViewHolder> {

        public Context context;
        private ArrayList<ConferencePackageModelForgein> packageList;
        boolean flag;

        public PackageAdapterForgein(Context context, ArrayList<ConferencePackageModelForgein> packageList, boolean flag) {

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
        public void onBindViewHolder(PackageAdapterForgein.MyViewHolder holder, int position) {

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

            TextView ticketsname, ticketsprice;
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
                        ReviewBookingNew.packageList.add("2");

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
