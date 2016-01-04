package bgu.spl.app;
import bgu.spl.mics.MicroService;
import bgu.spl.app.PurchaseSchedule;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by kobi on 12/19/2015.
 */
public class WebsiteClientService extends MicroService {
	
	private static Logger LOGGER = Logger.getGlobal();
	
	/**
	 * A list of the purchases that the client needs to make
	 */
    private LinkedList <PurchaseSchedule> purchases;
    
    /**
     * A list of the desired shoes that the client wishes to have
     */
    private LinkedList <String> wishList;
    
    /**
     * @param name the micro-service name, the purchase list and the wish list
     */
    public WebsiteClientService(String name, LinkedList<PurchaseSchedule> purchases, LinkedList<String> wishList) {
        super(name);
        this.purchases = purchases;
        this.wishList = wishList;
    }
    
    /**
     * this method is called once when the event loop starts.
     * This service subscribes to tick broadcasts and to discount broadcasts.
     */
    @Override
    protected void initialize() {
    	/*
    	 * In this subscription the client subscribes to the time tick, 
    	 * and performers the actions as described below.
    	 */
    	subscribeBroadcast(TickBroadcast.class, v-> {
    		/*
             * In this block the client awaits for the time tick to be 
             * the termination tick, and upon receiving it, the service terminates.
             */
    		this.current_tick=v.getTick();
	        if(current_tick<=v.getTerminationTick()){
	        	LOGGER.finest(this.getName() + ": Current tick is: " + this.current_tick);
			}else{
				LOGGER.info(this.getName() + ": Terminating...");
				terminate();
			}
	        
	        /*
	         * In this block the client checks whether he has a purchase that he wants to make in
	         * this tick. If he does, he sends a request with the purchase details.
	         * At the end of the block, if the client has purchased all of his stuff, 
	         * he logs off.
	         */
	        purchases.forEach((k)-> {
	        	if (k.getTick()==current_tick){
	        		
	        		/*
	        		 * In the callback function, the client removes the shoe that
	        		 * he bought from the wishlist and from the purchase list, and then prints the receipt.
	        		 */
                    boolean success = sendRequest(new PurchaseOrderRequest(getName() ,k.getShoeType(), false,current_tick,1),c->
                    {
                        Receipt r= (Receipt) c;
                        purchases.remove(k);
                        wishList.remove(r.getShoeType());
                        System.out.println(r);
                        if(wishList.isEmpty()&&purchases.isEmpty()){
                        	LOGGER.info(this.getName() + ": Finished shopping for today, terminating...");
                        	terminate();
                        }
                    });
                    
                    // Logging commands used for debugging
                    if (success) {
                        LOGGER.info(getName() + ": Waits to get the following shoe:" + k.getShoeType() + ", Requesting discount sale: False");
                    }else {
                    	LOGGER.info(getName() + ": Disappointed to find out that there will be no shoes of type:" + k.getShoeType());
                    }
                }
            });
        });
    	
    	/*
    	 * In this subscription the client checks whether the discounted shoe type is on
    	 * his wish list, and if it is, he tries to buy it.
    	 * In the callback function he removes the item from his wish list.
    	 * At the end of the block, if the client has purchased all of his stuff, 
	     * he logs off.
    	 */
    	subscribeBroadcast(NewDiscountBroadcast.class,v-> {
    		NewDiscountBroadcast newDiscountBroadcast=(NewDiscountBroadcast)v;
            if (wishList.contains(newDiscountBroadcast.getShoeType())){
            	LOGGER.info(this.getName() + ": A discount on " + newDiscountBroadcast.getShoeType() + "????????????????? ME WANT IT!!!!!!!!!");
            	LOGGER.info(getName() + ": Waits to get the following shoe:" + newDiscountBroadcast.getShoeType() + ", Requesting discount sale: True");
            	this.sendRequest(new PurchaseOrderRequest(getName(),newDiscountBroadcast.getShoeType(),true,this.current_tick,1), c->{
                    Receipt r= (Receipt) c;
                    System.out.println(r);
                    wishList.remove(v.getShoeType());
                    if(wishList.isEmpty()&&purchases.isEmpty()){
                    	LOGGER.info(this.getName() + ": Finished shopping for today, terminating...");
                    	terminate();
                    }
                });
            }
        });
    }
}
