package Models;

public class ConsumptionModel {
    String Remark,Date,Amount,Sstp1_code,Con1_code,Created_by,Con_no;

    public String getCon_no() {
        return Con_no;
    }

    public void setCon_no(String con_no) {
        Con_no = con_no;
    }

    public String getCreated_by() {
        return Created_by;
    }

    public void setCreated_by(String created_by) {
        Created_by = created_by;
    }

    public String getCon1_code() {
        return Con1_code;
    }

    public void setCon1_code(String con1_code) {
        Con1_code = con1_code;
    }

    public String getSstp1_code() {
        return Sstp1_code;
    }

    public void setSstp1_code(String sstp1_code) {
        Sstp1_code = sstp1_code;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }




}
