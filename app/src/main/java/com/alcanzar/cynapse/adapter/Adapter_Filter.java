package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.alcanzar.cynapse.R;

import java.util.ArrayList;
import java.util.List;


public class Adapter_Filter extends ArrayAdapter<String> {
    Context context;
    int resource, textViewResourceId;
    List<String> items, tempItems, suggestions;

    public Adapter_Filter(Context context, int resource, int textViewResourceId, List<String> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;

        try {
            tempItems = new ArrayList<String>(items); // this makes the difference. error**
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }

        suggestions = new ArrayList<String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.filter_row, parent, false);
        }
        String people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people);
        }
        return view;
    }


    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {


        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((String) resultValue);
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null && !constraint.equals("")) {
                suggestions.clear();

                for (String people : tempItems) {
                    if (people == null) {
                        Log.d("hii", "hello");
                    }

                    if (people.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {

                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                filterResults.count = items.size();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filterList = (ArrayList<String>) results.values;
            if (results != null && results.count > 0) {
                clear();
                try {
                   for(String people : filterList) {
                        add(people);
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };

    //error occur
//    java.lang.NullPointerException: collection == null
//    at java.util.ArrayList.<init>(ArrayList.java:94)
//    at com.alcanzar.eventapp.adapter.Adapter_Filter.<init>(Adapter_Filter.java:31)

}
