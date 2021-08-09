package com.alcanzar.cynapse.fragments;


import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.utils.MyToast;

import static com.alcanzar.cynapse.fragments.BookTicketFragmentNew.packageContainerLl;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonDetailsFormFragment extends Fragment implements View.OnClickListener {

    private static final String PersonId = "PersonId";
    private static final String nextButBool = "PersonId";
    private int count = 0;
    public static Button previousButton, savetodraft;

    private EditText name, email, mob_nom;
    private CheckBox checkBox;
    private TextView disPerTv;
    private static String discount_percentage1, discount_description1;


    public static PersonDetailsFormFragment getInstance(String id, boolean btn_next_show, String discount_percentage, String discount_description) {

        // Required empty public constructor

        discount_percentage1 = discount_percentage;
        discount_description1 = discount_description;

        PersonDetailsFormFragment personDetailsFormFragment = new PersonDetailsFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PersonId, id);
        bundle.putBoolean(nextButBool, btn_next_show);
        personDetailsFormFragment.setArguments(bundle);
        return personDetailsFormFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_details_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        previousButton = view.findViewById(R.id.previousButton);
        savetodraft = view.findViewById(R.id.savetodraft);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        mob_nom = view.findViewById(R.id.mob_);
        checkBox = view.findViewById(R.id.checkBox);
        disPerTv = view.findViewById(R.id.disPerTv);

        disPerTv.setText(discount_percentage1 + "% " + discount_description1);

        if (getArguments().getBoolean(nextButBool)) {
            previousButton.setEnabled(false);
            previousButton.setVisibility(View.GONE);
        }

        setUpClickListener();

    }

    private void setUpClickListener() {

        savetodraft.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        checkBox.setOnClickListener(this);
        disPerTv.setOnClickListener(this);
    }

    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onLifeCycle", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onLifeCycle", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onLifeCycle", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("onLifeCycle", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onLifeCycle", "onDestroy");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.savetodraft:

                if (isValid()) {

                    nameStr = name.getText().toString();
                    emailStr = email.getText().toString();
                    phoneNOStr = mob_nom.getText().toString();

                    if (packageContainerLl.getVisibility() == View.GONE) {
                        MyToast.toastShort(getActivity(), "Please Select Package first!");
                    } else {
                        BookTicketFragmentNew.setIndexViewPager(nameStr,emailStr,phoneNOStr, getActivity());
                    }
                }

                break;
            case R.id.previousButton:
                BookTicketFragmentNew.setIndexViewPagerDecrement(getActivity());
                break;

            case R.id.checkBox:
//                if (checkBox.isChecked())
//                {
//                    //BookTicketFragmentNew.priceTv.setText();
//                } else {
//                    BookTicketFragmentNew.priceTv.setText("12");
//                }
                break;

            case R.id.disPerTv:
                break;
        }
    }

    private Float calcutateDiscount(Float discount) {
        Float discount1 = Float.parseFloat(BookTicketFragmentNew.priceTv.getText().toString()) * discount;
        return discount1 / 100;
    }

    private boolean isValid() {

        if (TextUtils.isEmpty(name.getText())) {
            name.setError("Enter Name");
            name.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(email.getText()) || (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())) {
            email.setError("Enter Valid Email Id");
            email.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(mob_nom.getText())) {
            mob_nom.setError("Enter Mobile No.");
            mob_nom.requestFocus();
            return false;

        }else if (mob_nom.getText().toString().length()<10) {
            mob_nom.setError("Enter Valid Mobile No.");
            mob_nom.requestFocus();
            return false;

        } else {
            return true;
        }

    }

    public static String nameStr,emailStr,phoneNOStr;
}
