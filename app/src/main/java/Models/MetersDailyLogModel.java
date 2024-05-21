package Models;

public class MetersDailyLogModel {

    String meters_equip_name,meters_reading_edit,meters_reading_time,meters_total,tstp3_code_m2,meter_status,tstp3_code;

    public String getTstp3_code_m2() {
        return tstp3_code_m2;
    }

    public void setTstp3_code_m2(String tstp3_code_m2) {
        this.tstp3_code_m2 = tstp3_code_m2;
    }

    public String getMeter_status() {
        return meter_status;
    }

    public void setMeter_status(String meter_status) {
        this.meter_status = meter_status;
    }

    public String getTstp3_code() {
        return tstp3_code;
    }

    public void setTstp3_code(String tstp3_code) {
        this.tstp3_code = tstp3_code;
    }

    public String getMeters_equip_name() {
        return meters_equip_name;
    }

    public void setMeters_equip_name(String meters_equip_name) {
        this.meters_equip_name = meters_equip_name;
    }

    public String getMeters_reading_edit() {
        return meters_reading_edit;
    }

    public void setMeters_reading_edit(String meters_reading_edit) {
        this.meters_reading_edit = meters_reading_edit;
    }

    public String getMeters_reading_time() {
        return meters_reading_time;
    }

    public void setMeters_reading_time(String meters_reading_time) {
        this.meters_reading_time = meters_reading_time;
    }

    public String getMeters_total() {
        return meters_total;
    }

    public void setMeters_total(String meters_total) {
        this.meters_total = meters_total;
    }
}
