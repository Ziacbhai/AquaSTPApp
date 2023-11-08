package Models;

public class CommonModelClass {
    private String Process_name ,Sstp2_code,Image_id,Manufacturer,EquipmentName,Specification,EquipmentNumber_Id,Rating_Capacity,
            Form_Factor,Phase,Cleaning_RunningFrequency_HRS;
    private int Card_view ;
    private  String Address_M ;

    public String getAddress_M() {
        return Address_M;
    }

    public void setAddress_M(String address_M) {
        Address_M = address_M;
    }

    public CommonModelClass() {

    }

    public String getProcess_name() {
        return Process_name;
    }

    public void setProcess_name(String process_name) {
        Process_name = process_name;
    }

    public String getSstp2_code() {
        return Sstp2_code;
    }

    public void setSstp2_code(String sstp2_code) {
        Sstp2_code = sstp2_code;
    }

    public String getImage() {
        return Image_id;
    }

    public void setImage(String image_id) {
        Image_id = image_id;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        EquipmentName = equipmentName;
    }

    public String getSpecification() {
        return Specification;
    }

    public void setSpecification(String specification) {
        Specification = specification;
    }

    public String getEquipmentNumber_Id() {
        return EquipmentNumber_Id;
    }

    public void setEquipmentNumber_Id(String equipmentNumber_Id) {
        EquipmentNumber_Id = equipmentNumber_Id;
    }

    public String getRating_Capacity() {
        return Rating_Capacity;
    }

    public void setRating_Capacity(String rating_Capacity) {
        Rating_Capacity = rating_Capacity;
    }

    public String getForm_Factor() {
        return Form_Factor;
    }

    public void setForm_Factor(String form_Factor) {
        Form_Factor = form_Factor;
    }

    public String getPhase() {
        return Phase;
    }

    public void setPhase(String phase) {
        Phase = phase;
    }

    public String getCleaning_RunningFrequency_HRS() {
        return Cleaning_RunningFrequency_HRS;
    }

    public void setCleaning_RunningFrequency_HRS(String cleaning_RunningFrequency_HRS) {
        Cleaning_RunningFrequency_HRS = cleaning_RunningFrequency_HRS;
    }

    public int getCard_view() {
        return Card_view;
    }

    public void setCard_view(int card_view) {
        Card_view = card_view;
    }
}
