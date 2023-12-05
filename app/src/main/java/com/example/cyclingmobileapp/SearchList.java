package com.example.cyclingmobileapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchList extends ArrayAdapter<String> {

    private final String delimeter;
    List<String> results;
    List<String> resultTypes;
    List<String> resultsAndTypes;
    List<String> ogResultsAndTypes;

    public SearchList(Context context, int resource, int textViewResourceId, List<String> results, List<String> resultTypes) {
        super(context, resource, textViewResourceId, results);
        this.results = results;
        this.resultTypes = resultTypes;
        resultsAndTypes = new ArrayList<>();
        ogResultsAndTypes = new ArrayList<>();
        delimeter = getSaltString();
        for (int i = 0; i < Math.min(results.size(), resultTypes.size()); i++) {
            String combined = (results.get(i) + delimeter + resultTypes.get(i));
            resultsAndTypes.add(combined);
            ogResultsAndTypes.add(combined);
        }
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }

    @Override
    public int getCount() {
        return resultsAndTypes.size();
    }

    @Override
    public String getItem(int i) {
        return resultsAndTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View retView = super.getView(i, view, viewGroup);
        TextView text1 = (TextView) retView.findViewById(android.R.id.text1);
        TextView text2 = (TextView) retView.findViewById(android.R.id.text2);

        String combined = resultsAndTypes.get(i);
        String[] parts = combined.split(delimeter);
        text1.setText(parts[0]);
        text2.setText(parts[1]);
        return retView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<String> filteredList = new ArrayList<>();

                if (ogResultsAndTypes == null) {
                    ogResultsAndTypes = new ArrayList<String>(resultsAndTypes);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = ogResultsAndTypes.size();
                    results.values = ogResultsAndTypes;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (String data : ogResultsAndTypes) {
                        if (data.toLowerCase().contains(charSequence)) {
                            filteredList.add(data);
                        }
                    }
                    results.count = filteredList.size();
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                resultsAndTypes = (List<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
