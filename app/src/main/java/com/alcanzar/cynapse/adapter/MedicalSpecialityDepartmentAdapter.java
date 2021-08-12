package com.alcanzar.cynapse.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MedicalDepartmentInterface;
import com.alcanzar.cynapse.model.GetTargetDepartmentModel;

import java.util.ArrayList;

public class MedicalSpecialityDepartmentAdapter extends RecyclerView.Adapter<MedicalSpecialityDepartmentAdapter.MyViewHolder> implements Filterable {

    Context mcontext;
    MedicalDepartmentInterface metroCityInterface;
    public static ArrayList<GetTargetDepartmentModel> courselist = new ArrayList<>();
    private ArrayList<String> citylist = new ArrayList<>();
    private ArrayList<GetTargetDepartmentModel> mOriginalValues;
    private boolean isAllChked = false;

    public MedicalSpecialityDepartmentAdapter(ArrayList<GetTargetDepartmentModel> courselist, Context mcontext, MedicalDepartmentInterface metroCityInterface1) {
        this.metroCityInterface = metroCityInterface1;
        this.courselist = courselist;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.multi_city_selection_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.city_name.setText(courselist.get(position).getDepartment_name());

        holder.checkbox.setChecked(courselist.get(position).isStatus());
        holder.checkbox.setTag(position);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Integer pos = (Integer) holder.checkbox.getTag();
                Integer pos = (Integer) holder.checkbox.getTag();
                // Toast.makeText(mcontext, courselist.get(pos).getProfile_category_name() + " clicked!", Toast.LENGTH_SHORT).show();

                if (courselist.get(pos).isStatus()) {
                    courselist.get(pos).setStatus(false);
                } else {
                    courselist.get(pos).setStatus(true);
                    citylist.add(courselist.get(pos).getDepartment_id());
                    metroCityInterface.selectedTitlesMedical(citylist);
                }
            }
        });

        if (courselist.get(position).isStatus())
            isAllChked = true;
        else
            isAllChked = false;

        if (position == 0) {
            holder.chkAll.setVisibility(View.VISIBLE);
            if (isAllChked)
                holder.chkAll.setChecked(true);
        } else
            holder.chkAll.setVisibility(View.GONE);


        holder.chkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < courselist.size(); i++) {
                        holder.checkbox.setTag(position);
                        courselist.get(i).setStatus(true);
                    }
                } else {
                    for (int i = 0; i < courselist.size(); i++) {
                        holder.checkbox.setTag(position);
                        courselist.get(i).setStatus(false);
                    }
                }

                notifyDataSetChanged(courselist);
            }

        });
    }

    private void notifyDataSetChanged(ArrayList<GetTargetDepartmentModel> courselist) {
        this.courselist = courselist;
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return courselist.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                courselist = (ArrayList<GetTargetDepartmentModel>) results.values;
                notifyDataSetChanged(courselist);  // notifies the data with new filtered values
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<GetTargetDepartmentModel> FilteredArrList = new ArrayList<GetTargetDepartmentModel>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(courselist); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getDepartment_name();
                        GetTargetDepartmentModel setgetc = new GetTargetDepartmentModel();
                        setgetc.setMedical_profile_name(mOriginalValues.get(i).getMedical_profile_name());
                        setgetc.setDepartment_id(mOriginalValues.get(i).getDepartment_id());
                        setgetc.setDepartment_name(mOriginalValues.get(i).getDepartment_name());
                        setgetc.setStatus(mOriginalValues.get(i).isStatus());
                        FilteredArrList.add(setgetc);

                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView city_name;
        CheckBox checkbox, chkAll;

        public MyViewHolder(View itemView) {
            super(itemView);
            city_name = itemView.findViewById(R.id.city_name);
            checkbox = itemView.findViewById(R.id.checkbox);
            chkAll = itemView.findViewById(R.id.chkAll);
//            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChecked)
//                    {
//                        citylist.add(courselist.get(getAdapterPosition()).getProfile_category_name());
//                        metroCityInterface.selectedCities(citylist);
//                        MetroCityModel model = new MetroCityModel();
//                        model.setId_city(courselist.get(getAdapterPosition()).getId_city());
//                        model.setProfile_category_name(courselist.get(getAdapterPosition()).getProfile_category_name());
//                        model.setStatus("1");
//                        if (!((RequestPostJobActivity) Objects.requireNonNull(mcontext)).handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_METRO_CITY_MASTER,DatabaseHelper.id_city,String.valueOf(model.getId_city())))
//                        {
//
//                            ((RequestPostJobActivity)mcontext).handler.AddMetroCityMaster(model, true);
//
//                                Log.e("ADDED_Sub_item", true + " " + model.getId_city());
//                        } else {
//                               Log.e("UPDATED", true + " " + model.getId_city());
//                            ((RequestPostJobActivity)mcontext).handler.AddMetroCityMaster(model, false);
//                        }
//                    }
//                }
//            });
//            checkbox.setChecked(true);
//            try
//            {
//                if(citylist.get(getAdapterPosition()).equalsIgnoreCase(courselist.get(getAdapterPosition()).getProfile_category_name()))
//                {
//                    checkbox.setChecked(true);
//                }
//            }
//            catch (ArrayIndexOutOfBoundsException ae)
//            {
//                ae.printStackTrace();
//            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //((MainActivity) mcontext).replaceFragment(new Menu_List(courselist,getAdapterPosition()));
//            Bundle b=new Bundle();
//            b.putSerializable("mylist",courselist);
//            b.putString("menu_id",courselist.get(getAdapterPosition()).getMenu_id());
//            courselist.get(getAdapterPosition()).setTouch(true);
//            Constant.Log("menu",";;"+courselist.get(getAdapterPosition()).getMenu_id());
//            Menu_List skillsDetails=new Menu_List();
//            skillsDetails.setArguments(b);
//            ((MainActivity) mcontext).replaceFragment(skillsDetails);

        }
    }
}
