package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.PostedJobDetailsActivity;
import com.alcanzar.cynapse.model.JobMasterModel;

import java.util.ArrayList;

public class PostedJobListAdapter  extends RecyclerView.Adapter<PostedJobListAdapter.MyViewHolder>implements Filterable
{
    Context mcontext;
    public ArrayList<JobMasterModel> courselist = new ArrayList<>();
    private ArrayList<JobMasterModel> mOriginalValues;
    TextView no_record_txts;

    public PostedJobListAdapter(ArrayList<JobMasterModel> courselist, Context mcontext, TextView no_record_txts)
    {
        this.courselist=courselist;
        this.mcontext=mcontext;
        this.no_record_txts=no_record_txts;
    }
    @Override
    public PostedJobListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.posted_jobs_row, parent, false);
        return new PostedJobListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PostedJobListAdapter.MyViewHolder holder, final int position) {

        Log.e("datasetSearch",courselist.get(position).getMedical_profile_name());
        holder.title.setText(courselist.get(position).getJob_title());
        holder.years.setText(String.format("Years of Experience : %s", courselist.get(position).getYear_of_experience()));
        holder.hospitalName.setText(courselist.get(position).getMedical_profile_name());
        holder.timeAgo.setText(String.format("Posted On : %s", courselist.get(position).getModify_date()));


        String person_name = courselist.get(position).getMedical_profile_name();
        String person_id = courselist.get(position).getMedical_profile_id();
        String Location = courselist.get(position).getLocation();
        Log.d("person_name",person_name);
        Log.d("person_name",person_id);
        Log.d("location",Location);

        if(person_id.equals("12") || person_id.equals("13") || person_id.equals("2")){
            holder.specialization.setText(courselist.get(position).getDepartment_name());
        }
        else{
            holder.specialization.setText(courselist.get(position).getSpecialization_name());
        }
        if(Location.contains(",")){
            holder.location.setText("Multiple Locations");
        }else{
            holder.location.setText(courselist.get(position).getLocation());
        }


    }

    @Override
    public int getItemCount() {
//        if(courselist.size()==0)
//        {
//            no_record_txts.setText(R.string.no_records);
//        }else {
//            no_record_txts.setText("");
//        }
        return courselist.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                courselist=(ArrayList<JobMasterModel>) results.values;
                notifyDataSetChanged(courselist);  // notifies the data with new filtered values
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<JobMasterModel> FilteredArrList = new ArrayList<JobMasterModel>();

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
                        String data = mOriginalValues.get(i).getJob_title();
                        String data1 = mOriginalValues.get(i).getMedical_profile_name();
                        if (data.toLowerCase().startsWith(constraint.toString())||data1.toLowerCase().startsWith(constraint.toString())) {
                            JobMasterModel setgetc = new JobMasterModel();
                            setgetc.setJob_id(mOriginalValues.get(i).getJob_id());
                            setgetc.setJob_title(mOriginalValues.get(i).getJob_title());
                            setgetc.setMedical_profile_name(mOriginalValues.get(i).getMedical_profile_name());
                            setgetc.setYear_of_experience(mOriginalValues.get(i).getYear_of_experience());
                            setgetc.setModify_date(mOriginalValues.get(i).getModify_date());
                            FilteredArrList.add(setgetc);
                        }
                        else {
                            final String prefixString = constraint.toString().toLowerCase();

                            final String[] words = data.split(" ");
                            for (String word : words) {
                                if (word.startsWith(prefixString)) {
                                    JobMasterModel setgetc = new JobMasterModel();
                                    setgetc.setJob_id(mOriginalValues.get(i).getJob_id());
                                    setgetc.setJob_title(mOriginalValues.get(i).getJob_title());
                                    setgetc.setMedical_profile_name(mOriginalValues.get(i).getMedical_profile_name());
                                    setgetc.setYear_of_experience(mOriginalValues.get(i).getYear_of_experience());
                                    setgetc.setModify_date(mOriginalValues.get(i).getModify_date());
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
    private void notifyDataSetChanged(ArrayList<JobMasterModel> courselist) {
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
        TextView title,hospitalName,years,timeAgo,specialization,location;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            years = itemView.findViewById(R.id.years);
            timeAgo = itemView.findViewById(R.id.timeAgo);
            specialization = itemView.findViewById(R.id.specialization);
            location = itemView.findViewById(R.id.location);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(mcontext, PostedJobDetailsActivity.class);
            i.putExtra("job_id",courselist.get(getAdapterPosition()).getJob_id());
            mcontext.startActivity(i);
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
