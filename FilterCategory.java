package com.example.bookproject.adapter;

import android.widget.Filter;

import com.example.bookproject.model.Category;

import java.util.ArrayList;

public class FilterCategory extends Filter {
    ArrayList<Category> filterList;
    AdapterCategory adapterCategory;

    public FilterCategory(ArrayList<Category> filterList, AdapterCategory adapterCategory) {
        this.filterList = filterList;
        this.adapterCategory = adapterCategory;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if(charSequence != null && charSequence.length() > 0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Category> filterModels = new ArrayList<>();
            for(int i = 0;i<filterList.size();i++){
                if(filterList.get(i).getCategory().toUpperCase().contains(charSequence)){
                    filterModels.add(filterList.get(i));
                }
            }
            results.count = filterModels.size();
            results.values = filterModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterCategory.categoryArrayList = (ArrayList<Category>)filterResults.values;
        adapterCategory.notifyDataSetChanged();
    }
}


