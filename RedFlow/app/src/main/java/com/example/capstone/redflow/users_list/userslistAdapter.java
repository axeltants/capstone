package com.example.capstone.redflow.users_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.capstone.redflow.R;

import java.util.List;

/**
 * Created by axel on 12/13/16.
 */

public class userslistAdapter extends ArrayAdapter<usersGetSet> {
    private List<usersGetSet> users;

    public userslistAdapter(Context context, int resource, List<usersGetSet> objects) {
        super(context, resource, objects);
        users = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.registered_userslist, parent, false);
        }

        usersGetSet user = users.get(position);

        TextView fullname = (TextView) convertView.findViewById(R.id.textview_fullname_list);
        fullname.setText(user.getFullname());
        TextView history = (TextView) convertView.findViewById(R.id.textview_history_list);
        history.setText(user.gethistory());


        return convertView;
    }
}
