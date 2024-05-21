package Models;

public class PumpMotorBlowerLogModel {

    String equip_name,running_time,start_time,end_time,tstp2_code,running_status,tstp5_code;


    public String getTstp5_code() {
        return tstp5_code;
    }

    public void setTstp5_code(String tstp5_code) {
        this.tstp5_code = tstp5_code;
    }

    public String getRunning_status() {
        return running_status;
    }

    public void setRunning_status(String running_status) {
        this.running_status = running_status;
    }

    public String getEquip_name() {
        return equip_name;
    }

    public void setEquip_name(String equip_name) {
        this.equip_name = equip_name;
    }

    public String getRunning_time() {
        return running_time;
    }

    public void setRunning_time(String running_time) {
        this.running_time = running_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String get_tstp2_code() {
        return tstp2_code;
    }

    public void set_tstp2_code(String start_tstp2_code) {
        tstp2_code = start_tstp2_code;
    }

}
