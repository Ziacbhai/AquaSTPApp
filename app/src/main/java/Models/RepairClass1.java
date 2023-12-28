package Models;

public class RepairClass1 {

    String Repair_code,REPNo,Repair_Amount,Repair_Date,Remark,R_createdby;

    public String getR_createdby() {
        return R_createdby;
    }

    public void setR_createdby(String r_createdby) {
        R_createdby = r_createdby;
    }

    private boolean isRepaired;

    public boolean isRepaired() {
        return isRepaired;
    }

    public void setRepaired(boolean repaired) {
        isRepaired = repaired;
    }



    public String getRepair_code() {
        return Repair_code;
    }

    public void setRepair_code(String repair_code) {
        Repair_code = repair_code;
    }

    public String getREPNo() {
        return REPNo;
    }

    public void setREPNo(String REPNo) {
        this.REPNo = REPNo;
    }

    public String getRepair_Amount() {
        return Repair_Amount;
    }

    public void setRepair_Amount(String repair_Amount) {
        Repair_Amount = repair_Amount;
    }

    public String getRepair_Date() {
        return Repair_Date;
    }

    public void setRepair_Date(String repair_Date) {
        Repair_Date = repair_Date;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }



}
