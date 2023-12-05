package com.ziac.aquastpapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ziac.aquastpapp.R;

import java.util.ArrayList;

import Models.CommonModelClass;
import Models.ConsumablesClass;
import Models.IncidentsClass;
import Models.ItemStockClass;
import Models.LabTestClass;
import Models.RepairsClass;
import Models.StpModelClass;
import Models.zList;

public class Global {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

     //Server url
    // public static String baseurl = "https://api.AquaSTP/";

    //    Local url
   // public static String baseurl="http://192.168.1.10/AquaSTP/Help";
    public static String baseurl="http://192.168.1.12:6968/";

    public static String userImageurl = baseurl+"WebsiteData/Users/";

    public static String urlUpdateprofileImage = baseurl +"api/Users/UpdateProfilePhoto";

    public static String urlGetStates = baseurl+"api/List/GetStates";

    public static String urlGetCities = baseurl+"api/List/GetCity?state_code=";
    public static String registration = baseurl+"api/Account/Register";

    public static String forgotpasswordurl = baseurl +"api/Account/ForgotPassword";
    public static String validateotpurl = baseurl +"api/Account/ValidateOTP";
    public static String changetpasswordurl = baseurl +"api/Account/ChangePassword";

    public static String getuserdetailsurl = baseurl + "api/account/getuserdetails";

    public static String getuserprofileurl = baseurl + "api/Users/GetUserProfile";
    public static String getSearchSiteOrSTPByName = baseurl + "api/Users/SearchSiteOrSTPByName?";
    public static String updateProfile = baseurl + "api/Users/UpdateProfile";

    public static String tokenurl = baseurl+"TOKEN";

    public static String Equipment_Details_com_pumps = baseurl + "api/Masters/GetPumps?";

    public static String Equipment_Details_com_meters = baseurl + "api/Masters/GetMeters?";

    public static String GetItemStock = baseurl + "api/Masters/GetItemStock?";
    public static String GetRepairItems = baseurl + "api/Repairs/List?";
    public static String GetLab_Test_Items = baseurl + "api/Lab/List?";
    public static String Get_Incidents = baseurl + "api/Incidents/List?";
    public static String Get_Consumables= baseurl + "api/Consumables/List?";

    public static String Get_Repairs_Details = baseurl + "api/Repairs/Details?";
    public static String Get_Consumables_Details = baseurl + "api/Consumables/Details?";
    public static String Get_Lab_Details = baseurl + "api/Lab/Details?";
    public static String Get_Incidents_Details = baseurl + "api/Incidents/Details?";

   // public static String Get_Incidents_ImageUploud = baseurl + "api/Incidents/Details?";

    public static String Get_Incidents_delete = baseurl + "api/Incidents/DeleteImgOrDoc?";


    public static ArrayList<zList> statearraylist;
    public static ArrayList<zList> cityarraylist;

    public static ArrayList<StpModelClass> StpList;
    public static ArrayList<CommonModelClass> pumpdetails;
    public static ArrayList<ItemStockClass> Item_stock;
    public static ArrayList<RepairsClass> Repair_s;
    public static ArrayList<ConsumablesClass> Consumables_s;
    public static ArrayList<LabTestClass> Labtest_s;
    public static ArrayList<IncidentsClass> Incident_s;

    public static ArrayList<CommonModelClass> metersdetails;

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

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}