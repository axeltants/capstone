package com.example.capstone.redflow.notification_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by axel on 11/30/16.
 */

public class notifprovider {
    public static List<notificationGetSet> notiflist = new ArrayList<>();
    public static Map<String, notificationGetSet> notifmap = new HashMap<>();

    static{
        addnotif("1", "Good day, Someone needs your blood.", "nov. 30 2016 8:35pm");
        addnotif("2", "Your donated blood saves someone's life thank you!", "dec. 2 2016 8:35pm");
        addnotif("3", "You are now a verified donor user", "dec. 3 2016 8:35pm");
        addnotif("4", "did you know bla bla bla", "dec. 4 2016 8:35pm");
    }

    private static void addnotif(String notifID, String mssge, String date){
        notificationGetSet item = new notificationGetSet(notifID, mssge, date);
        notiflist.add(item);
        notifmap.put(notifID, item);
    }

    public static List<String> getnotifmssge(){
        List<String> list = new ArrayList<>();
        for(notificationGetSet notification : notiflist){
            list.add(notification.getMssge());
        }
        return list;
    }

    public static List<notificationGetSet> getFilteredList(String searchstring){
        List<notificationGetSet> filteredlist = new ArrayList<>();
        for(notificationGetSet notification : notiflist){
            if(notification.getNotifID().contains(searchstring)){
                filteredlist.add(notification);
            }
        }
        return filteredlist;
    }
}
