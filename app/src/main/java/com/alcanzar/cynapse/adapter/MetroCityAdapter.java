package com.alcanzar.cynapse.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.MetroCityInterface;
import com.alcanzar.cynapse.model.MetroCityModel;

import java.util.ArrayList;

public class MetroCityAdapter  extends RecyclerView.Adapter<MetroCityAdapter.MyViewHolder>implements Filterable
{
    Context mcontext;
    MetroCityInterface metroCityInterface;
    public static ArrayList<MetroCityModel> courselist = new ArrayList<>();
    private ArrayList<String> citylist = new ArrayList<>();
    private ArrayList<MetroCityModel> mOriginalValues;

    public MetroCityAdapter(ArrayList<MetroCityModel> courselist, Context mcontext,MetroCityInterface metroCityInterface1)
    {
        this.metroCityInterface=metroCityInterface1;
        this.courselist=courselist;
        this.mcontext=mcontext;


    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_city_selection_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.city_name.setText(courselist.get(position).getProfile_category_name());

//        if((courselist.get(position).getStatus()).equalsIgnoreCase("0"))
//        {
//            holder.checkbox.setChecked(false);
//        }
//        else
//        {
//            holder.checkbox.setChecked(true);
//        }
      //  citylist.clear();
//       holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked)
//                {
//                    citylist.add(courselist.get(position).getProfile_category_name());
//                    metroCityInterface.selectedCities(citylist);
//                }
//            }
//        });
       // citylist.clear();
        holder.checkbox.setChecked(courselist.get(position).getStatus());
        holder.checkbox.setTag(position);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Integer pos = (Integer) holder.checkbox.getTag();
                Integer pos = (Integer) holder.checkbox.getTag();
                // Toast.makeText(mcontext, courselist.get(pos).getProfile_category_name() + " clicked!", Toast.LENGTH_SHORT).show();

                if (courselist.get(pos).getStatus()) {
                    courselist.get(pos).setStatus(false);
                } else {
                    courselist.get(pos).setStatus(true);
                    citylist.add(courselist.get(pos).getProfile_category_name());
                    metroCityInterface.selectedCities(citylist);
                }
            }
        });


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
                courselist=(ArrayList<MetroCityModel>) results.values;
                notifyDataSetChanged(courselist);  // notifies the data with new filtered values
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<MetroCityModel> FilteredArrList = new ArrayList<MetroCityModel>();

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
                        String data = mOriginalValues.get(i).getProfile_category_name();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            MetroCityModel setgetc = new MetroCityModel();
                            setgetc.setProfile_category_name(mOriginalValues.get(i).getProfile_category_name());
                            setgetc.setId_city(mOriginalValues.get(i).getId_city());
                            setgetc.setStatus(mOriginalValues.get(i).getStatus());
                            FilteredArrList.add(setgetc);
                        }
                        else {
                            final String prefixString = constraint.toString().toLowerCase();

                            final String[] words = data.split(" ");
                            for (String word : words) {
                                if (word.startsWith(prefixString)) {
                                    MetroCityModel setgetc = new MetroCityModel();
                                    setgetc.setProfile_category_name(mOriginalValues.get(i).getProfile_category_name());
                                    setgetc.setId_city(mOriginalValues.get(i).getId_city());
                                    setgetc.setStatus(mOriginalValues.get(i).getStatus());
                                    FilteredArrList.add(setgetc);
                                    break;
                                }
                            }
                        }

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
    private void notifyDataSetChanged(ArrayList<MetroCityModel> courselist) {
        this.courselist = courselist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView city_name;
        CheckBox checkbox;
        public MyViewHolder(View itemView) {
            super(itemView);
            city_name = itemView.findViewById(R.id.city_name);
            checkbox = itemView.findViewById(R.id.checkbox);
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
