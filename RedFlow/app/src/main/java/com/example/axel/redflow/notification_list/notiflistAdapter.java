package com.example.axel.redflow.notification_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.axel.redflow.R;

import java.util.List;

public class notiflistAdapter extends ArrayAdapter<notificationGetSet> {

    private List<notificationGetSet> notifications;

    public notiflistAdapter(Context context, int resource, List<notificationGetSet> objects) {
        super(context, resource, objects);
        notifications = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_notif, parent, false);
        }

        notificationGetSet notification = notifications.get(position);

        TextView mssge = (TextView) convertView.findViewById(R.id.edittext_notifmssge);
        mssge.setText(notification.getMssge());
        TextView date = (TextView) convertView.findViewById(R.id.edittext_notifdate);
        date.setText(notification.getDate());


        return convertView;
    }
}
