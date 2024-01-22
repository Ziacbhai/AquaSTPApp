package Models;

public class PumpMotor_Blower_DailyLogClass {

    String equip_name,running_time,start_time,end_time,Start_tstp2_code,Stop_tstp2_code;

    private int logType;

    private boolean isStartViewVisible;

    public boolean isStartViewVisible() {
        return isStartViewVisible;
    }

    public void setStartViewVisible(boolean startViewVisible) {
        isStartViewVisible = startViewVisible;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    private boolean hideViews;

    public boolean isHideViews() {
        return hideViews;
    }

    public void setHideViews(boolean hideViews) {
        this.hideViews = hideViews;
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

    public String getStart_tstp2_code() {
        return Start_tstp2_code;
    }

    public void setStart_tstp2_code(String start_tstp2_code) {
        Start_tstp2_code = start_tstp2_code;
    }

    public String getStop_tstp2_code() {
        return Stop_tstp2_code;
    }

    public void setStop_tstp2_code(String stop_tstp2_code) {
        Stop_tstp2_code = stop_tstp2_code;
    }
}
