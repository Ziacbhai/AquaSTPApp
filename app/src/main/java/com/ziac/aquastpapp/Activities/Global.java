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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import Models.CommonModelClass;
import Models.ConsumptionModel;
import Models.ConsumptionModel2;
import Models.DailyLogModel;
import Models.EquipmentClassRepairBreakUp;
import Models.EquipmentListClassConsumption;
import Models.EquipmentRepairListClass;
import Models.FiltersModel;
import Models.IncidentsModelClass;
import Models.ItemListConsumptionModel;
import Models.ItemListRepair_BreakUpModel;
import Models.ItemStockModel;
import Models.LabTestModel;
import Models.MetersDailyLogModel;
import Models.PumpMotorBlowerLogModel;
import Models.RepairModel2;
import Models.RepairModel1;
import Models.RepairModel3;
import Models.RepairModel4;
import Models.SensorsModelClass;
import Models.StpModelClass;
import Models.zList;

public class Global {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private static Picasso picassoInstance;
    //Server url
    public static String baseurl = "http://aquastp.ziaconline.com/";
    //Local url
    //public static String baseurl = "http://192.168.1.20:9396/";
    //Logs
    public static String GetDailyLogIndex = baseurl + "api/DailyLog/DailyLogIndex?";
    //Pump_Motor
    public static String GetDailyLogPumpMotor = baseurl + "api/DailyLog/GetPumps?";
    public static String StartMotorPumpsUrl = baseurl + "api/DailyLog/StartMotorPumps?";
    public static String StopMotorPumpsUrl = baseurl + "api/DailyLog/StopMotorPumps?";
    public static String RolloverMotorPumpsUrl = baseurl + "api/DailyLog/RolloverMotorPumps?";
    //Blower
    public static String GetDailyLogBlowers = baseurl + "api/DailyLog/GetBlowers?";
    public static String StartBlowerUrl = baseurl + "api/DailyLog/StartBlower?";
    public static String StopBlowerUrl = baseurl + "api/DailyLog/StopBlower?";
    public static String RolloverBlowerUrl = baseurl + "api/DailyLog/RolloverBlower?";
    //Meter
    public static String GetDailyLogMeter = baseurl + "api/DailyLog/GetMeters?";
    public static String DailyLogUpdateMeterReadings = baseurl + "api/DailyLog/UpdateMeterReadings";
    //Filters
    public static String GetDailyLogFilters = baseurl + "api/DailyLog/GetFilters?";
    /* public static String FilterDaily_log_image = baseurl + "WebsiteData/DailyLog/Filters/";*/
    public static String GetDailyLogFilterImageUpload = baseurl + "api/DailyLog/FilterImageUpload";
    //Sensors
    public static String GetDailyLogSensors = baseurl + "api/DailyLog/GetSensors?";
    public static String DailyLogUpdateSensorsReadings = baseurl + "api/DailyLog/UpdateSensorsReadings";
    public static String DailyLogSensorsImageUpload = baseurl + "api/DailyLog/SensorsImageUpload";
    //Handover Remarks;
    public static String DailyLogUpdateHandOverRemark = baseurl + "api/DailyLog/UpdateHandOverRemarks?";
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
    public static ArrayList<DailyLogModel> dailyLogModelArrayList;

    public static ArrayList<zList> cityarraylist;
    public static ArrayList<StpModelClass> StpList;
    public static ArrayList<CommonModelClass> pumpdetails;
    public static ArrayList<ItemStockModel> Item_stock;
    public static ArrayList<RepairModel1> repair1list;
    public static ArrayList<RepairModel2> repair2list;
    public static ArrayList<RepairModel3> repair3list;
    public static ArrayList<RepairModel4> repair4list;
    public static ConsumptionModel ConsumptionModel;
    public static ConsumptionModel2 consumptionModel2;
    public static RepairModel1 repairModel1;
    public static RepairModel2 repairModel2;
    public static RepairModel3 repairModel3;
    public static DailyLogModel dailyLogModel;
    public static RepairModel4 repairModel4;
    public static LabTestModel labTestModel1;
    public static IncidentsModelClass incidentsModelClass;
    public static FiltersModel filtersModel;

    public static MetersDailyLogModel MetersDailyLogModel;

    public static SensorsModelClass sensorclass;
    public static ArrayList<ConsumptionModel> Consumption1list;
    public static ArrayList<ConsumptionModel2> Consumption2list;
    public static ArrayList<EquipmentListClassConsumption> Consumption_equipment;
    public static ArrayList<ItemListConsumptionModel> Consumption_item;
    public static ArrayList<EquipmentRepairListClass> Repair_equipment;
    public static ArrayList<ItemListRepair_BreakUpModel> Repair_Item_Breakup;
    public static ArrayList<EquipmentClassRepairBreakUp> Repair_Equipment_Breakup;
    public static ArrayList<LabTestModel> LabTest_Class;
    public static ArrayList<IncidentsModelClass> Incident_Class;
    public static ArrayList<PumpMotorBlowerLogModel> RunningPumpsMotors_LogClass;
    public static ArrayList<PumpMotorBlowerLogModel> Blower_LogClass;
    //public static ArrayList<PumpMotorBlower_LogClass> Stop_Blower_LogClass;
    public static ArrayList<PumpMotorBlowerLogModel> StoppedPumpsMotors_LogClass;
    public static ArrayList<FiltersModel> Filter_LogClass;
    public static ArrayList<SensorsModelClass> Sensors_Class;
    public static ArrayList<MetersDailyLogModel> Meters_Class;
    public static SensorsModelClass sensorsClass;
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
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.no_image_available_icon)
                .into(imageView);
    }

    public static void loadstoppedpumps(JSONObject response) throws JSONException {
        /*stopped pumps*/
        Global.StoppedPumpsMotors_LogClass = new ArrayList<PumpMotorBlowerLogModel>();
        JSONArray jarray;
        jarray = response.getJSONArray("pumps2");
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            try {
                e = jarray.getJSONObject(i);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            PumpMotorBlowerLogModel pumpMotorClass = new PumpMotorBlowerLogModel();
            try {
                pumpMotorClass.setEquip_name(e.getString("equip_name"));
                pumpMotorClass.setRunning_time(e.getString("running_time"));
                pumpMotorClass.setEnd_time(e.getString("endtime"));
                pumpMotorClass.set_tstp2_code(e.getString("tstp2_code"));
                pumpMotorClass.setRunning_status(e.getString("running_status"));
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            Global.StoppedPumpsMotors_LogClass.add(pumpMotorClass);
        }
    }

    public static void loadrunningpumps(JSONObject response) throws JSONException {
        /*running pumps*/
        Global.RunningPumpsMotors_LogClass = new ArrayList<PumpMotorBlowerLogModel>();
        JSONArray jarray;
        jarray = response.getJSONArray("pumps1");
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            try {
                e = jarray.getJSONObject(i);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            PumpMotorBlowerLogModel pumpMotorClass = new PumpMotorBlowerLogModel();
            try {
                pumpMotorClass.setEquip_name(e.getString("equip_name"));
                pumpMotorClass.setStart_time(e.getString("starttime"));
                pumpMotorClass.setRunning_time(e.getString("running_time"));
                pumpMotorClass.set_tstp2_code(e.getString("tstp2_code"));
                pumpMotorClass.setRunning_status(e.getString("running_status"));

            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            Global.RunningPumpsMotors_LogClass.add(pumpMotorClass);
        }
    }

    public static void loadmeter1(JSONObject response) throws JSONException {
        Global.Meters_Class = new ArrayList<MetersDailyLogModel>();
        JSONArray jarray;
        jarray = response.getJSONArray("meters1");

        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            try {
                e = jarray.getJSONObject(i);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            MetersDailyLogModel = new MetersDailyLogModel();
            try {
                MetersDailyLogModel.setMeters_equip_name(e.getString("equip_name"));
                MetersDailyLogModel.setTstp3_code(e.getString("tstp3_code"));
                MetersDailyLogModel.setMeters_reading_edit(e.getString("reading_value"));
                MetersDailyLogModel.setMeter_status(e.getString("status"));

            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            Global.Meters_Class.add(MetersDailyLogModel);

        }
    }

    public static void loadmeter2(JSONObject response) throws JSONException {
        Global.Meters_Class = new ArrayList<MetersDailyLogModel>();
        JSONArray jarray;
        jarray = response.getJSONArray("meters2");

        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            try {
                e = jarray.getJSONObject(i);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            MetersDailyLogModel = new MetersDailyLogModel();
            try {
                MetersDailyLogModel.setMeters_equip_name(e.getString("equip_name"));
                MetersDailyLogModel.setMeters_reading_edit(e.getString("reading_value"));
                MetersDailyLogModel.setMeters_reading_time(e.getString("readingtime"));
                MetersDailyLogModel.setMeters_total(e.getString("final_value"));
                MetersDailyLogModel.setTstp3_code(e.getString("tstp3_code"));
                MetersDailyLogModel.setMeter_status(e.getString("status"));

            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
            Global.Meters_Class.add(MetersDailyLogModel);

        }
    }
}