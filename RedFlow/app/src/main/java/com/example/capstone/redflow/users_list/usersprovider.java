package com.example.capstone.redflow.users_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by axel on 12/13/16.
 */

public class usersprovider {
    public static List<usersGetSet> userslist = new ArrayList<>();
    public static Map<String, usersGetSet> searchmap = new HashMap<>();

    static{
        adduser("1", "Kendall Jenner", "January 2 2016");
        adduser("2", "Liza Soberano", "October 7 2016");
        adduser("3", "Lily Collins", "November 23 2016");
        adduser("4", "Gal Gadot", "July 4 2015");
    }

    private static void adduser(String userID, String fullname, String history){
        usersGetSet item = new usersGetSet(userID, fullname, history);
        userslist.add(item);
        searchmap.put(userID, item);
    }

    public static List<String> getuserFullname(){
        List<String> list = new ArrayList<>();
        for(usersGetSet user : userslist){
            list.add(user.getFullname());
        }
        return list;
    }

    public static List<usersGetSet> getFilteredList(String searchstring){
        List<usersGetSet> filteredlist = new ArrayList<>();
        for(usersGetSet user : userslist){
            if(user.getuserID().contains(searchstring)){
                filteredlist.add(user);
            }
        }
        return filteredlist;
    }
}
