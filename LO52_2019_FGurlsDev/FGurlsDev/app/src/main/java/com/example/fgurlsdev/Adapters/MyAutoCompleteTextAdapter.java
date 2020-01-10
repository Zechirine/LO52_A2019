package com.example.fgurlsdev.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.fgurlsdev.Entity.Group;
import com.example.fgurlsdev.Entity.Runner;
import com.example.fgurlsdev.R;

import java.util.ArrayList;
import java.util.List;

public class MyAutoCompleteTextAdapter<T> extends BaseAdapter implements Filterable {
    private Context context;
    //private List<Runner> runners;
    //private List<Runner> runnersFiltered;
    private List<T> entities;
    private List<T> entitiesFiltered;
    private Class entityClass;
    private final Object mLock = new Object();

    public MyAutoCompleteTextAdapter(Context context, Class entityClass, List<T> entities) {
        this.context = context;
        this.entityClass = entityClass;
        this.entities = entities;
    }

    @Override
    public int getCount() {
        return entitiesFiltered.size();
    }

    @Override
    public String getItem(int position) {
        if(entityClass.equals(Runner.class)){
            return ((Runner) entitiesFiltered.get(position)).getNom();
        }
        return ((Group) entitiesFiltered.get(position)).getGroupName();
    }

    @Override
    public long getItemId(int position) {
        if(entityClass.equals(Runner.class)){
            return ((Runner) entitiesFiltered.get(position)).getRunnerId();
        }
        return ((Group) entitiesFiltered.get(position)).getGroupId();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    synchronized (mLock) {
                        if (entityClass.equals(Runner.class)) {
                            ArrayList<Runner> newRunners = new ArrayList<>((List<Runner>)entities);
                            results.values = newRunners;
                            results.count = newRunners.size();
                        } else {
                            ArrayList<Group> newGroups = new ArrayList<>((List<Group>)entities);
                            results.values = newGroups;
                            results.count = newGroups.size();
                        }
                        return results;
                    }
                } else {
                    if (entityClass.equals(Runner.class)) {
                        final ArrayList<Runner> newValues = new ArrayList<>(entities.size());
                        for (T runner : entities) {
                            if (((Runner)runner).getNom().startsWith(constraint.toString())) {
                                newValues.add((Runner) runner);
                            }
                        }
                        results.values = newValues;
                        results.count = newValues.size();
                    } else {
                        final ArrayList<Group> newValues = new ArrayList<>(entities.size());
                        for (T group : entities) {
                            if (((Group)group).getGroupName().startsWith(constraint.toString())) {
                                newValues.add((Group) group);
                            }
                        }
                        results.values = newValues;
                        results.count = newValues.size();
                    }
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                entitiesFiltered = (List<T>) results.values;
                if(results.count > 0){
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    class MyViewHolder{
        TextView itemName;

        public MyViewHolder(View myView) {
            this.itemName = myView.findViewById(R.id.itemDrop);
            this.itemName.setTextColor(Color.BLACK);
            myView.setTag(this);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_drop, null);
            new MyViewHolder(convertView);
        }
        MyViewHolder myViewHolder = (MyViewHolder) convertView.getTag();
        myViewHolder.itemName.setText(getItem(position));

        return convertView;
    }
}
