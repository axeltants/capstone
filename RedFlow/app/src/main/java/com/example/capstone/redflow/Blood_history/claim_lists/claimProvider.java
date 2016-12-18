package com.example.capstone.redflow.Blood_history.claim_lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by axel on 12/18/16.
 */

public class claimProvider {
    public static List<claimGetSet> claimlist = new ArrayList<>();
    public static Map<String, claimGetSet> claimmap = new HashMap<>();

    static{
        addclaim("1", "April 5  2016");
    }

    private static void addclaim(String claimID, String claimdate){
        claimGetSet item = new claimGetSet(claimID, claimdate);
        claimlist.add(item);
        claimmap.put(claimID, item);
    }

    public static List<String> getsearchclaim(){
        List<String> list = new ArrayList<>();
        for(claimGetSet claim : claimlist){
            list.add(claim.getDate());
        }
        return list;
    }

    public static List<claimGetSet> getFilteredList(String searchstring){
        List<claimGetSet> filteredlist = new ArrayList<>();
        for(claimGetSet claim : claimlist){
            if(claim.getclaimID().contains(searchstring)){
                filteredlist.add(claim);
            }
        }
        return filteredlist;
    }
}
