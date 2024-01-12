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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ziac.aquastpapp.R;

import java.util.ArrayList;

import Models.CommonModelClass;
import Models.ConsumptionClass;

import Models.ConsumptionClass2;
import Models.EquipmentClassRepairBreakUp;
import Models.EquipmentListClassConsumption;
import Models.EquipmentRepairListClass;
import Models.IncidentsClass;
import Models.ItemListClassConsumption;
import Models.ItemListClassRepair_BreakUp;
import Models.ItemStockClass;
import Models.LabTestClass;
import Models.RepairClass2;
import Models.RepairClass1;
import Models.RepairClass3;
import Models.RepairClass4;
import Models.StpModelClass;
import Models.zList;

public class Global {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private static Picasso picassoInstance;

    //Server url
    //public static String baseurl = "http://aquastp.ziaconline.com/";


    //Local url
    // public static String baseurl="http://192.168.1.10/AquaSTP/Help";

    public static String baseurl = "http://192.168.1.17:9396/";

    public static String userImageurl = baseurl + "WebsiteData/Users/";
    public static String incident_image = baseurl + "WebsiteData/IncidentReportDocs/";


    /* public static String repair_images = baseurl+"WebsiteData/RepairDocs/";*/
    public static String repair_images = baseurl;

    public static String incident_UploadImage = baseurl + "api/Incidents/UploadImage";
    public static String Repair_UploadImage = baseurl + "api/Repairs/UploadImage";

    public static String Repair_two_Imagelist = baseurl + "api/Repairs/RepairImageList?";

    public static String Incident_UploadDocuments = baseurl + "api/Incidents/UploadDoc";

    public static String urlUpdateprofileImage = baseurl + "api/Users/UpdateProfilePhoto";

    public static String urlGetStates = baseurl + "api/List/GetStates";

    public static String urlGetCities = baseurl + "api/List/GetCity?state_code=";
    public static String registration = baseurl + "api/Account/Register";

    public static String forgotpasswordurl = baseurl + "api/Account/ForgotPassword";
    public static String validateotpurl = baseurl + "api/Account/ValidateOTP";
    public static String changetpasswordurl = baseurl + "api/Account/ChangePassword";

    public static String getuserdetailsurl = baseurl + "api/account/getuserdetails";

    public static String getuserprofileurl = baseurl + "api/Users/GetUserProfile";
    public static String getSearchSiteOrSTPByName = baseurl + "api/Users/SearchSiteOrSTPByName?";
    public static String updateProfile = baseurl + "api/Users/UpdateProfile";
    public static String updateConsumption = baseurl + "api/Consumables/AddEditConsumables";

    public static String updateIncidents = baseurl + "api/Incidents/IncidentsAddUpdate";
    public static String updateDConsumption = baseurl + "api/Consumables/AddEditConsumablesDetails";
    public static String GetRepairItems = baseurl + "api/Repairs/List?";
    public static String updateRepairAddUpdate = baseurl + "api/Repairs/RepairAddUpdate";
    public static String updateRepairDAddUpdate = baseurl + "api/Repairs/RepairDetailsAddUPdate";
    public static String RepairsRepairBreakUp = baseurl + "api/Repairs/RepairBreakUp?";
    public static String Repair_BreakUp_update = baseurl + "api/Repairs/RepairBreakUpAddUPdate";
    public static String tokenurl = baseurl + "TOKEN";

    public static String Equipment_Details_com_pumps = baseurl + "api/Masters/GetPumps?";

    public static String Equipment_Details_com_meters = baseurl + "api/Masters/GetMeters?";

    public static String GetItemStock = baseurl + "api/Masters/GetItemStock?";


    public static String GetLab_Test_Items = baseurl + "api/Lab/List?";

    public static String Lab_Test_Update = baseurl + "api/Lab/AddUpdate";
    public static String Get_Incidents = baseurl + "api/Incidents/List?";
    public static String Get_Consumables = baseurl + "api/Consumables/List?";

    public static String Get_Repairs_Details = baseurl + "api/Repairs/Details?";
    public static String Get_Consumables_Details = baseurl + "api/Consumables/Details?";
    public static String Get_Lab_Details = baseurl + "api/Lab/Details?";
    public static String Get_Incidents_Details = baseurl + "api/Incidents/Details?";//

    public static String Get_Incidents_delete = baseurl + "api/Incidents/DeleteImgOrDoc?";
    public static String api_List_Get_Equipments = baseurl + "api/List/GetEquipments?";
    public static String api_List_Get_Item = baseurl + "api/List/GetProducts?";
    public static String api_Repair_List_Get_Equipments = baseurl + "api/List/GetRepairItems?";
    public static String api_Repair_List_Get_Units = baseurl + "api/List/GetUnits?";
    public static ArrayList<zList> statearraylist;
    public static ArrayList<zList> cityarraylist;
    public static ArrayList<StpModelClass> StpList;
    public static ArrayList<CommonModelClass> pumpdetails;
    public static ArrayList<ItemStockClass> Item_stock;
    public static ArrayList<RepairClass1> repair1list;
    public static ArrayList<RepairClass2> repair2list;
    public static ArrayList<RepairClass3> repair3list;
    public static ArrayList<RepairClass4> repair4list;
    public static ConsumptionClass ConsumptionClass;
    public static ConsumptionClass2 consumptionClass2;
    public static RepairClass1 repairClass1;
    public static RepairClass2 repairClass2;
    public static RepairClass3 repairClass3;
    public static RepairClass4 repairClass4;
    public static LabTestClass labTestClass1;
    public static IncidentsClass incidentsClass;
    public static ArrayList<ConsumptionClass> Consumption1list;
    public static ArrayList<ConsumptionClass2> Consumption2list;
    public static ArrayList<EquipmentListClassConsumption> Consumption_equipment;
    public static ArrayList<ItemListClassConsumption> Consumption_item;
    public static ArrayList<EquipmentRepairListClass> Repair_equipment;
    public static ArrayList<ItemListClassRepair_BreakUp> Repair_Item_Breakup;
    public static ArrayList<EquipmentClassRepairBreakUp> Repair_Equipment_Breakup;
    public static ArrayList<LabTestClass> LabTest_Class;
    public static ArrayList<IncidentsClass> Incident_Class;
    public static ArrayList<CommonModelClass> metersdetails;

    public static void customtoast(Context context, LayoutInflater inflater, String msg) {
        View customToastView = inflater.inflate(R.layout.costom_toast, null);
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

    public static Picasso getPicassoInstance(Context context) {
        if (picassoInstance == null) {
            picassoInstance = new Picasso.Builder(context.getApplicationContext()).build();
        }
        return picassoInstance;
    }

    public static void loadWithPicasso(Context context, ImageView imageView, String imageUrl) {
        Picasso picasso = getPicassoInstance(context);
        picasso.load(imageUrl)
                .error(R.drawable.no_image_available_icon)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imageView);
    }
}