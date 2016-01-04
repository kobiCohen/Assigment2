package bgu.spl.app;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by kobi on 12/18/2015.
 */
public class Parser {

    public Wrapper parse(String f){
	    Gson gson=new Gson();
	    Reader reader = null;
	    String file = f;
	    Scanner sc = new Scanner(System.in);
	    
	    boolean readSuccess=false;
	    
	    while(!readSuccess){
		    try {
				reader = new FileReader(file);
				readSuccess = true;
			} catch (FileNotFoundException e) {
				System.out.println("File not found - please enter new file name:");
				file = sc.nextLine();
			}
	    }
	    
	    sc.close();
	    
	    Wrapper p = gson.fromJson(reader, Wrapper.class);
	    System.out.println("i'm here for the laughs");
	    System.out.println(p.toString());
	    return p;
    }


}
class Wrapper{
    @SerializedName("initialStorage")
    public LinkedList<Shoe> Shoes_Types;
    @SerializedName("services")
    public Services services;
    public String toString(){
    	String sho = "";
    	for(Shoe s: Shoes_Types){
    		sho += "\n\t\t" + s.toString();
    	}
    	return "Wrapper:\n\t" + "InitialStorage: [" + sho + "\n\t],\n\t" + services.toString();
    }
}
class Services{
	@SerializedName("time")
	public Time time;
    @SerializedName("manager")
    public Manager manager;
    @SerializedName("factories")
    public int factories;
    @SerializedName("sellers")
    public int sellers;
    @SerializedName("customers")
    public LinkedList<Client> clients;
    public String toString(){
    	String cli = "";
    	for(Client s: clients){
    		cli += "\n\t\t" + s.toString();
    	}
    	if(!clients.isEmpty()) cli = cli.substring(0, cli.length()-1);
    	return "Services: {\n\t\t" + time.toString() + manager.toString() + "\n\t\tFactories: "
    			+ factories + "," + "\n\t\tSellers: " + sellers + "," + "\n\t\tCustomers: ["
    			+ cli + "\n\t\t]\n\t}";
    }

}

class Time {
	@SerializedName("speed")
    public int _speed;
    @SerializedName("duration")
    public  int _duration;
    public String toString(){
    	return "Time: {\n\t\t\t" + "speed:" + _speed + "\n\t\t\tduration:" + _duration + "\n\t\t}," ;
    }
}

class Shoe {
    @SerializedName("shoeType")
    public String _type;
    @SerializedName("amount")
    public  int _amount;
    public int get_amount(){return _amount;}
    public String get_type(){return _type;}
    public String toString(){
    	return "{shoeType: \"" + _type + "\", amount: " + _amount + "}";
    }
}
class Manager{
    @SerializedName("discountSchedule")
    public LinkedList<Discount> _discount;
    public String toString(){
    	String dc = "";
    	for(Discount s: _discount){
    		dc += "\n\t\t\t\t" + s.toString();
    	}
    	return "\n\t\t" + "Manager: {" + "\n\t\t\tDiscoutSchedule: [" + dc + "\n\t\t\t]\n\t\t},";
    }
}

class Client{
    @SerializedName("name")
    public String name;
    @SerializedName("wishList")
    public LinkedList<String> _wishList;
    @SerializedName("purchaseSchedule")
    public LinkedList<Purches> _purchases;
    public String toString(){
    	String wis = "";
    	for(String s: _wishList){
    		wis += "\"" + s.toString() + "\", ";
    	}
    	if(!_wishList.isEmpty()) wis = wis.substring(0, wis.length()-2);
    	wis += "]";
    	
    	String pur = "";
    	for(Purches s: _purchases){
    		pur += "\n\t\t\t\t\t" + s.toString() + ",";
    	}
    	if(!_purchases.isEmpty()) pur = pur.substring(0, pur.length()-1);
    	pur += "\n\t\t\t\t]";
    	
    	return "\t{\n\t\t\t\tname: \"" + name + "\"," + "\n\t\t\t\twishList: [" + wis
    			+ "\n\t\t\t\tpurchaseSchedule: [" + pur + "\n\t\t\t},";
    }
}
class Purches{
    @SerializedName("tick")
    public int _time;
    @SerializedName("shoeType")
    public String _type;
    public String get_type() {return _type;}
    public int get_time() {return _time;}
    public String toString(){
    	return "{shoeType: \"" + _type + "\", tick: " + _time + "}" ;
    }
}
class Discount{
    @SerializedName("shoeType")
    public String _type;
    @SerializedName("tick")
    public int _time;
    @SerializedName("amount")
    public int _amount;
    public String toString(){
    	return "{ShoeTyepe: \"" + _type + "\", amount: " + _amount + ", tick: " + _time + "}" ;
    }
}