package com.example.capstone.redflow.Blood_history.donation_lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by axel on 12/18/16.
 */

public class donationprovider {
    public static List<donationGetSet> donationlist = new ArrayList<>();
    public static Map<String, donationGetSet> donationmap = new HashMap<>();

    static{
        adddonation("1", "February 2 2016");
        adddonation("2", "April 14 2016");
    }

    private static void adddonation(String donationID, String donationdate){
        donationGetSet item = new donationGetSet(donationID, donationdate);
        donationlist.add(item);
        donationmap.put(donationID, item);
    }

    public static List<String> getsearchFullname(){
        List<String> list = new ArrayList<>();
        for(donationGetSet donation : donationlist){
            list.add(donation.getDate());
        }
        return list;
    }

    public static List<donationGetSet> getFilteredList(String searchstring){
        List<donationGetSet> filteredlist = new ArrayList<>();
        for(donationGetSet donation : donationlist){
            if(donation.getdonationID().contains(searchstring)){
                filteredlist.add(donation);
            }
        }
        return filteredlist;
    }
}
