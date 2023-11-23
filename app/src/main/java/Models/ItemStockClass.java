package Models;

public class ItemStockClass {
    public ItemStockClass() {

    }

    private String _Code ,_Name ,_Stock ,_Units;

    public String get_Code() {
        return _Code;
    }

    public void set_Code(String _Code) {
        this._Code = _Code;
    }

    public String get_Name() {
        return _Name;
    }

    public void set_Name(String _Name) {
        this._Name = _Name;
    }

    public String get_Stock() {
        return _Stock;
    }

    public void set_Stock(String _Stock) {
        this._Stock = _Stock;
    }

    public String get_Units() {
        return _Units;
    }

    public void set_Units(String _Units) {
        this._Units = _Units;
    }
}
