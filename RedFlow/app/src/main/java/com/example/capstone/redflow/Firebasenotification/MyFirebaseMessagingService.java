package com.example.capstone.redflow.Firebasenotification;


import android.content.Intent;
import android.util.Log;

import com.example.capstone.redflow.notification_viewer.notification_normal;
import com.example.capstone.redflow.notification_viewer.notification_with_prelim;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by axel on 01/11/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String notifdate;

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            notifdate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

            //creating an intent for the notification
            Intent intent;
            if(title.equals("RedFlow: Good Day!")){
                intent = new Intent(getApplicationContext(), notification_with_prelim.class);
                intent.putExtra("title", "Good Day");
                intent.putExtra("content", message);
                intent.putExtra("date", notifdate);
            }else{
                intent = new Intent(getApplicationContext(), notification_normal.class);
                if(title.equals("RedFlow: Thanks!")){
                    intent.putExtra("title", "Thanks");
                    intent.putExtra("content", message);
                    intent.putExtra("date", notifdate);
                }else{
                    intent.putExtra("title", "Announcement");
                    intent.putExtra("content", message);
                    intent.putExtra("date", notifdate)
;                }
            }


            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}