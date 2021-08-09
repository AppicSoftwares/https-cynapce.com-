package com.alcanzar.cynapse.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.ReviewBookingNew;
import com.alcanzar.cynapse.activity.ReviewBookingNewPay;
import com.alcanzar.cynapse.activity.SelectPersonPackageActivity;
import com.alcanzar.cynapse.adapter.NumberOfPresonAdapter;
import com.alcanzar.cynapse.adapter.ViewPagerAdapter;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.AddPackageModal;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookTicketFragmentNew extends Fragment implements View.OnClickListener {

    private final int REQUEST_CODE = 501;
    //    PersonDetailsFormFragment
    static RecyclerView numberOfPersonRecyclerView;
    static ViewPager viewpager;
    private static int count = 0;
    private static int listSize = 0;
    private String conferenceID, discount_description;

    private static String discount_percentage;

    static NumberOfPresonAdapter numberOfPresonAdapter;
    private static Context context;
    ArrayList<AddPackageModal> selectPersonModalList;
    static private CheckBox checkBox;
    private static ScrollView scrollView;

    static TextView choosePackage, removePackage, packageNameTv, priceTv, discountTv, totalPriceTv, disPerTv, toDateTv, frmDateTv;

    private static Button btnAddConfer;
    DatabaseHelper handler;

    public static LinearLayout packageContainerLl, totAmtContainerLl;

    private String price_hike_after_date = "";
    private String price_hike_after_percentage = "";
    private String from_date = "", to_date = "";

    public BookTicketFragmentNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_ticket_fragment_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        context = getContext();

        numberOfPersonRecyclerView = view.findViewById(R.id.numberOfPersonRecyclerView);
        numberOfPersonRecyclerView.setHasFixedSize(true);
        numberOfPersonRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        listSize = Integer.parseInt(getArguments().getString("totalSits"));
        conferenceID = getArguments().getString("conferenceID");
        discount_percentage = getArguments().getString("discount_percentage");
        discount_description = getArguments().getString("discount_description");
        price_hike_after_date = getArguments().getString("price_hike_after_date");
        price_hike_after_percentage = getArguments().getString("price_hike_after_percentage");
        from_date = getArguments().getString("from_date");
        to_date = getArguments().getString("to_date");

        defaultAllValues(listSize);

        count = 0;

        setUpPersonListAdapter();
        setUpViewPager(view);

        choosePackage = view.findViewById(R.id.choosePackage);
        btnAddConfer = view.findViewById(R.id.btnAddConfer);
        removePackage = view.findViewById(R.id.removePackage);
        packageContainerLl = view.findViewById(R.id.packageContainerLl);
        packageNameTv = view.findViewById(R.id.packageNameTv);
        discountTv = view.findViewById(R.id.discountTv);
        totalPriceTv = view.findViewById(R.id.totalPriceTv);
        totAmtContainerLl = view.findViewById(R.id.totAmtContainerLl);
        priceTv = view.findViewById(R.id.priceTv);
        disPerTv = view.findViewById(R.id.disPerTv);
        checkBox = view.findViewById(R.id.checkBox);
        scrollView = view.findViewById(R.id.scrollView);
        toDateTv = view.findViewById(R.id.toDateTv);
        frmDateTv = view.findViewById(R.id.frmDateTv);


        if (TextUtils.isEmpty(discount_percentage)) {
            disPerTv.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
        } else {
            disPerTv.setText(discount_percentage + "% " + discount_description);
        }

        toDateTv.setText(to_date);
        frmDateTv.setText(from_date);

        totAmtContainerLl.setVisibility(View.GONE);
        packageContainerLl.setVisibility(View.GONE);

        implementClickListener();

        whichPAckage = "";

    }

    private Float calcutateDiscount(Float discount) {
        Float discount1 = Float.parseFloat(BookTicketFragmentNew.priceTv.getText().toString()) * discount;
        return discount1 / 100;
    }

    private static Float calcutateDiscount_(Float discount) {
        Float discount1 = Float.parseFloat(BookTicketFragmentNew.priceTv.getText().toString()) * discount;
        return discount1 / 100;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDones = false;
    }

    private void implementClickListener() {
        choosePackage.setOnClickListener(this);
        btnAddConfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, ReviewBookingNewPay.class);
                    intent.putExtra("namePrice", allUsersList.toString());
                    intent.putExtra("conference_name", getArguments().getString("conference_name"));
                    intent.putExtra("address", getArguments().getString("address"));
                    intent.putExtra("gst", getArguments().getString("gst"));
                    intent.putExtra("from_time", getArguments().getString("from_time"));
                    intent.putExtra("price_hike_after_percentage", price_hike_after_percentage);
                    intent.putExtra("price_hike_after_date", price_hike_after_date);
                    intent.putExtra("from_date", from_date);
                    intent.putExtra("to_date", to_date);
                    intent.putExtra("conference_id", conferenceID);
                    startActivity(intent);
                } catch (Exception e) {
                    //PersonDetailsFormFragment.savetodraft.performClick();
                }
            }
        });

        removePackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    clearAllPackageData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                enableDesableDoneBtn(R.color.disableBtn, false);

                packageContainerLl.setVisibility(View.GONE);
                totAmtContainerLl.setVisibility(View.GONE);
                whichPAckage = "";
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox.isChecked()) {
                    if (packageContainerLl.getVisibility() == View.GONE) {
                        checkBox.setChecked(false);
                        MyToast.toastShort(getActivity(), "Please choose package first!");
                        return;
                    }

                    try {
                        discountTv.setText(discount_percentage + "%");
                        Float price = Float.parseFloat(priceTv.getText().toString());
                        Float discount = calcutateDiscount(Float.parseFloat(discount_percentage));
                        Float totDis = price - discount;
                        totalPriceTv.setText("" + totDis);

                        try {
                            allUsersList.get(count).put("ischecked", "true");
                            allUsersList.get(count).put("totalPrice", totDis);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (NumberFormatException e) {
                        MyToast.toastShort(getActivity(), e.getMessage());
                    }

                    totAmtContainerLl.setVisibility(View.VISIBLE);

                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                } else {
                    totAmtContainerLl.setVisibility(View.GONE);

                    try {
                        allUsersList.get(count).put("ischecked", "");
                        allUsersList.get(count).put("totalPrice", allUsersList.get(count).getString("packagePrice"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    private static void enableDesableDoneBtn(int col, boolean b) {
        btnAddConfer.setBackgroundResource(col);
        btnAddConfer.setClickable(b);
        btnAddConfer.setEnabled(b);
    }

    private static void clearAllPackageData() throws JSONException {
        for (int i = 0; i < allUsersList.size(); i++) {
            allUsersList.get(i).put("package", "");
        }
    }

    private void setUpPersonListAdapter() {
        selectPersonModalList = new ArrayList<>();

        for (int i = 1; i <= listSize; i++) {
            AddPackageModal addPackageModal = new AddPackageModal();
            addPackageModal.setPackageDetails("Person " + i);
            if (i == 1) {
                AppCustomPreferenceClass.writeString(getContext(), AppCustomPreferenceClass.COLOR_CLICK_CHECK, "0");
            }

            selectPersonModalList.add(addPackageModal);

        }

        numberOfPresonAdapter = new NumberOfPresonAdapter(getContext(), selectPersonModalList);
        numberOfPersonRecyclerView.setAdapter(numberOfPresonAdapter);
        numberOfPresonAdapter.notifyDataSetChanged();
    }

    private void setUpViewPager(@NonNull View view) {
        viewpager = view.findViewById(R.id.viewpager);
        addTabs(viewpager);
    }

    public static void setIndexViewPager(String uname, String email, String phoneNO, Activity activity) {

        if (count < (listSize - 1)) {
            try {
                allUsersList.get(count).put("name", uname);
                allUsersList.get(count).put("email", email);
                allUsersList.get(count).put("phoneNO", phoneNO);
                Log.d("allUsersList", allUsersList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            count++;

            //==============hide/show done btn==============================
//            if (listSize - count == 1) {
//                enableDesableDoneBtn(R.drawable.header, true);
//            }

             if (isDones)
                 enableDesableDoneBtn(R.drawable.header, true);

            //==============end of hide/show done btn========================

            setPackageDetails(activity);

            Log.e("countCheck+++", count + " listSize " + listSize);

            AppCustomPreferenceClass.writeString(context, AppCustomPreferenceClass.COLOR_CLICK_CHECK, count + "");
            viewpager.setCurrentItem(count, true);
            numberOfPresonAdapter.notifyDataSetChanged();
            numberOfPersonRecyclerView.scrollToPosition(count);

        } else {
            try {
                allUsersList.get(count).put("name", uname);
                allUsersList.get(count).put("email", email);
                allUsersList.get(count).put("phoneNO", phoneNO);
                MyToast.toastShort(activity, "Successfully Saved to Draft");
                enableDesableDoneBtn(R.drawable.header, true);
                 isDones = true;
//                if (isClickedOnDoneBtn) {
//                    btnAddConfer.performClick();
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        isChecked();
    }

    private static void isChecked() {
        if (allUsersList.get(count).optString("ischecked").equals("true")) {
            try {
                discountTv.setText(discount_percentage + "%");
                Float price = Float.parseFloat(priceTv.getText().toString());
                Float discount = calcutateDiscount_(Float.parseFloat(discount_percentage));
                Float totDis = price - discount;
                totalPriceTv.setText("" + totDis);

            } catch (NumberFormatException e) {
                //MyToast.toastShort(getActivity(), e.getMessage());
            }

            totAmtContainerLl.setVisibility(View.VISIBLE);
            checkBox.setChecked(true);

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        } else {
            checkBox.setChecked(false);
            totAmtContainerLl.setVisibility(View.GONE);
        }

    }

    private static void setPackageDetails(Activity activity) {
        packageNameTv.setText(allUsersList.get(count).optString("package"));
        priceTv.setText(allUsersList.get(count).optString("packagePrice"));

        showAndHideWidgetAfterSavingData();

    }

    private static void showAndHideWidgetAfterSavingData() {
        Log.d("countBOB", "" + count);

        if (count == 0) {
            removePackage.setVisibility(View.VISIBLE);
        } else {
            removePackage.setVisibility(View.GONE);
        }

        if (priceTv.getText().toString().length() == 0) {
            totAmtContainerLl.setVisibility(View.GONE);
            packageContainerLl.setVisibility(View.GONE);
            checkBox.setChecked(false);
        } else if (checkBox.isChecked()) {
            totAmtContainerLl.setVisibility(View.VISIBLE);
            packageContainerLl.setVisibility(View.VISIBLE);
        } else {
            packageContainerLl.setVisibility(View.VISIBLE);
            totAmtContainerLl.setVisibility(View.GONE);
        }

        if (allUsersList.get(count).optString("package").equals("")) {
            packageContainerLl.setVisibility(View.GONE);
            totAmtContainerLl.setVisibility(View.GONE);
        }
    }

    private void defaultAllValues(int countUSers) {

        allUsersList.clear();

        for (int i = 0; i < countUSers; i++) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "");
                jsonObject.put("email", "");
                jsonObject.put("phoneNO", "");
                jsonObject.put("price", "");
                jsonObject.put("package", "");
                jsonObject.put("packagePrice", "");
                jsonObject.put("totalPrice", "");
                jsonObject.put("ischecked", "");
                allUsersList.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setIndexViewPagerDecrement(Activity activity) {
        if (count > 0) {
            count--;

            setPackageDetails(activity);

            Log.e("countCheck--", count + "");
            AppCustomPreferenceClass.writeString(context, AppCustomPreferenceClass.COLOR_CLICK_CHECK, count + "");
            viewpager.setCurrentItem(count, true);
            numberOfPresonAdapter.notifyDataSetChanged();
            numberOfPersonRecyclerView.scrollToPosition(count);
            isChecked();
        }


//        if (!isDones) {
//            enableDesableDoneBtn(R.color.disableBtn, false);
//        }

        enableDesableDoneBtn(R.color.disableBtn, false);
    }

    private void addTabs(ViewPager viewpager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        for (int i = 1; i <= listSize; i++) {
            if (i == 1) {
                viewPagerAdapter.addFrag(PersonDetailsFormFragment.getInstance(i + "", true, discount_percentage, discount_description));
            } else {
                viewPagerAdapter.addFrag(PersonDetailsFormFragment.getInstance(i + "", false, discount_percentage, discount_description));
            }
        }
        viewpager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.choosePackage:
                if (count == 0) {
                    if (packageContainerLl.getVisibility() == View.GONE) {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.alert_dialog);
                        TextView messageTv = dialog.findViewById(R.id.messageTv);
                        messageTv.setText("Only one type of package (either normal or Foreign) can be  booked at a time , for different packages book separately.");
                        Button okBtn = dialog.findViewById(R.id.okBtn);
                        okBtn.setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.btnContainerLl).setVisibility(View.GONE);

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startActivity();
                            }
                        });

                        dialog.show();

                    } else {
                        startActivity();
                    }
                } else {
                    startActivity();
                }

                break;
        }
    }

    private void startActivity() {
        Intent intent = new Intent(getActivity(), SelectPersonPackageActivity.class);
        intent.putExtra("conferenceID", conferenceID);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQUEST_CODE) {
            Log.d("packageList", data.getStringExtra("packageList"));

            //String[] packageList = ReviewBookingNew.packageList.toString().replace("[", "").replace("]", "").split(",");

            packageNameTv.setText("" + ReviewBookingNew.packageList.get(0));
            priceTv.setText("" + ReviewBookingNew.packageList.get(1));
            whichPAckage = ReviewBookingNew.packageList.get(2).toString();

            packageContainerLl.setVisibility(View.VISIBLE);

            try {
                allUsersList.get(count).put("package", ReviewBookingNew.packageList.get(0));
                allUsersList.get(count).put("packagePrice", ReviewBookingNew.packageList.get(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            isChecked();

            if (allUsersList.get(count).optString("ischecked").equals("")) {
                try {
                    allUsersList.get(count).put("totalPrice", allUsersList.get(count).getString("packagePrice"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Float price = Float.parseFloat(priceTv.getText().toString());
                    Float discount = calcutateDiscount_(Float.parseFloat(discount_percentage));
                    Float totDis = price - discount;
                    allUsersList.get(count).put("totalPrice", totDis);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        allUsersList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static ArrayList<JSONObject> allUsersList = new ArrayList<>();
    public static String whichPAckage = "";

    private static boolean isDones = false;
    private static boolean isClickedOnDoneBtn = false;
}
