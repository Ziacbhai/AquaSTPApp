package Models;

public class SensorsModelClass {

    String equip_name,reading,reading_time,sensor_total,sensor_image,sensor_tstp6_code,sensor_status;

    public String getSensor_status() {
        return sensor_status;
    }

    public void setSensor_status(String sensor_status) {
        this.sensor_status = sensor_status;
    }

    public String getSensor_tstp6_code() {
        return sensor_tstp6_code;
    }

    public void setSensor_tstp6_code(String sensor_tstp6_code) {
        this.sensor_tstp6_code = sensor_tstp6_code;
    }

    public String getEquip_name() {
        return equip_name;
    }

    public void setEquip_name(String equip_name) {
        this.equip_name = equip_name;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getReading_time() {
        return reading_time;
    }

    public void setReading_time(String reading_time) {
        this.reading_time = reading_time;
    }

    public String getSensor_total() {
        return sensor_total;
    }

    public void setSensor_total(String sensor_total) {
        this.sensor_total = sensor_total;
    }

    public String getSensor_image() {
        return sensor_image;
    }

    public void setSensor_image(String sensor_image) {
        this.sensor_image = sensor_image;
    }
}
