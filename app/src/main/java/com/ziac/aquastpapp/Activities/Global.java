package com.ziac.aquastpapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ziac.aquastpapp.R;

import java.util.ArrayList;

import Models.CommonModelClass;
import Models.zList;

public class Global {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

//    Server url
//     public static String baseurl = "https://api.AquaSTP/";

    //    Local url
   // public static String baseurl="http://192.168.1.10/AquaSTP/Help";
    public static String baseurl="http://192.168.1.19:6968/";
    public static String userImageurl = baseurl+"WebsiteData/Users/";
    public static String urlUpdateprofileImage = baseurl +"api/Users/UpdateProfilePhoto";

    public static String urlGetStates = baseurl+"api/List/GetStates";

    public static String urlGetCities = baseurl+"api/List/GetCity?state_code=";
    public static String registration = baseurl+"api/Account/Register";

    public static String forgotpasswordurl = baseurl +"api/Account/ForgotPassword";
    public static String validateotpurl = baseurl +"api/Account/ValidateOTP";
    public static String resettpasswordurl = baseurl +"api/Account/ChangePassword";

    public static String getuserdetailsurl = baseurl + "api/account/getuserdetails";

    public static String getuserprofileurl = baseurl + "api/Users/GetUserProfile";
    public static String updateProfile = baseurl + "api/Users/UpdateProfile";

    public static String tokenurl = baseurl+"TOKEN";

    public static String urlmystock = baseurl + "api/Vehicles/GetMyStock";

    public static ArrayList<zList> statearraylist;
    public static ArrayList<zList> cityarraylist;

    public static ArrayList<CommonModelClass> equipmentNameslist;

    public static void  customtoast(Context context, LayoutInflater inflater, String msg){
        //LayoutInflater inflater = getLayoutInflater();
        View customToastView = inflater.inflate(R.layout.costom_toast, null);

        // Find and modify any elements inside the custom layout
        ImageView icon = customToastView.findViewById(R.id.toast_icon);
        TextView text = customToastView.findViewById(R.id.toast_text);
        text.setText(msg);
        // Create the Toast with the custom layout
        Toast customToast = new Toast(context);
        customToast.setDuration(Toast.LENGTH_LONG);
        customToast.setView(customToastView);
        customToast.show();
    }
}
