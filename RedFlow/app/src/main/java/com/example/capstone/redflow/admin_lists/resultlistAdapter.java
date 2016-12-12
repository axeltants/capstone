package com.example.capstone.redflow.admin_lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.capstone.redflow.R;

import java.util.List;

/**
 * Created by axel on 12/12/16.
 */

public class resultlistAdapter extends ArrayAdapter<resultGetSet>{
    private List<resultGetSet> results;

    public resultlistAdapter(Context context, int resource, List<resultGetSet> objects) {
        super(context, resource, objects);
        results = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.search_resultlist, parent, false);
        }

        resultGetSet result = results.get(position);

        TextView fullname = (TextView) convertView.findViewById(R.id.textview_fullname_list);
        fullname.setText(result.getFullname());
        TextView accstatus = (TextView) convertView.findViewById(R.id.textview_accountstatus_list);
        accstatus.setText(result.getAccstatuse());


        return convertView;
    }
}
