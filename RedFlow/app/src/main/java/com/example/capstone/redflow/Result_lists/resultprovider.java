package com.example.capstone.redflow.Result_lists;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by axel on 12/12/16.
 */

public class resultprovider {
    public static List<resultGetSet> searchlist = new ArrayList<>();
    public static Map<String, resultGetSet> searchmap = new HashMap<>();

    static{
        addresult("1", "Kendall Jenner", "Unverified");
        addresult("2", "Liza Soberano", "Verified");
        addresult("3", "Lily Collins", "Verified");
        addresult("4", "Gal Gadot", "Unverified");
    }

    private static void addresult(String searchID, String fullname, String accstatus){
        resultGetSet item = new resultGetSet(searchID, fullname, accstatus);
        searchlist.add(item);
        searchmap.put(searchID, item);
    }

    public static List<String> getsearchFullname(){
        List<String> list = new ArrayList<>();
        for(resultGetSet result : searchlist){
            list.add(result.getFullname());
        }
        return list;
    }

    public static List<resultGetSet> getFilteredList(String searchstring){
        List<resultGetSet> filteredlist = new ArrayList<>();
        for(resultGetSet result : searchlist){
            if(result.getResultID().contains(searchstring)){
                filteredlist.add(result);
            }
        }
        return filteredlist;
    }
}
