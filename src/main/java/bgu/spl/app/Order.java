package bgu.spl.app;

/**
 * Created by kobi on 12/21/2015.


 */
public class Order {
    private int _amountLeft;
    private int _totalAmount;
    private String type;
    private ManufacturingOrderRequest manufacturingOrderRequest;

    public Order(String type, int _totalAmount, ManufacturingOrderRequest manufacturingOrderRequest){
        this.type=type;
        this._totalAmount=_totalAmount;
        this._amountLeft=_totalAmount;
        this.manufacturingOrderRequest=manufacturingOrderRequest;
    }
    public void makeShoe(){_amountLeft--;}
    public ManufacturingOrderRequest getManufacturingOrderRequest() {return manufacturingOrderRequest;}
    public int get_amountLeft() {return _amountLeft;}
    public int get_totalAmount() {return _totalAmount;}
    public String getType() {return type;}
}
