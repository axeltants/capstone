package com.example.capstone.redflow.Blood_history.donation_lists;

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

public class donationAdapter extends ArrayAdapter<donationGetSet> {
    private List<donationGetSet> donations;

    public donationAdapter(Context context, int resource, List<donationGetSet> objects) {
        super(context, resource, objects);
        donations = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.donation_history_list, parent, false);
        }

        donationGetSet donation = donations.get(position);

        TextView date = (TextView) convertView.findViewById(R.id.donation_date);
        date.setText(donation.getDate());

        return convertView;
    }
}
