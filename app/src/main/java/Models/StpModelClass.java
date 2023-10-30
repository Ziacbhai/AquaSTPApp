package Models;

public class StpModelClass {

    String sucode ,stpname;

    public StpModelClass(String sucode, String stpname) {
        this.sucode = sucode;
        this.stpname = stpname;
        //this.selectstp = selectstp;
    }

    public StpModelClass() {

    }

    public String getSuCode() {
        return sucode;
    }

    public void setSuCode(String code) {
        this.sucode = code;
    }

    public String getSTPName() {
        return stpname;
    }

    public void setSTPName(String stpname) {
        this.stpname = stpname;
    }


}
