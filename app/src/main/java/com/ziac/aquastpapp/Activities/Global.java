package com.ziac.aquastpapp.Activities;

import android.content.SharedPreferences;

import java.util.ArrayList;

import Models.zList;

public class Global {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static String baseurl="http://192.168.1.13/AquaSTP/Help/Api/POST-api-Account-Register";

    public static String urlGetStates = baseurl + "api-List-GetStates";
    public static String urlGetCities = baseurl +"api/list/POSTcity";
    public static ArrayList<zList> statearraylist;
    public static ArrayList<zList> cityarraylist;
}
