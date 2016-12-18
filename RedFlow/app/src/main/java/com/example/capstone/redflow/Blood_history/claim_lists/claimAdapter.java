package com.example.capstone.redflow.Blood_history.claim_lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.capstone.redflow.R;

import java.util.List;

/**
 * Created by axel on 12/18/16.
 */

public class claimAdapter extends ArrayAdapter<claimGetSet> {
    private List<claimGetSet> claims;

    public claimAdapter(Context context, int resource, List<claimGetSet> objects) {
        super(context, resource, objects);
        claims = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.claim_history_list, parent, false);
        }

        claimGetSet claim = claims.get(position);

        TextView date = (TextView) convertView.findViewById(R.id.claim_date);
        date.setText(claim.getDate());

        return convertView;
    }
}
