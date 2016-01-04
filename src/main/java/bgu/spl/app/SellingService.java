package bgu.spl.app;

import java.util.logging.Logger;
import bgu.spl.mics.MicroService;


/**
 * Created by kobi on 12/19/2015.
 */
public class SellingService extends MicroService {
	
	private static Logger LOGGER = Logger.getGlobal();
	
    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public SellingService(String name) {
        super(name);
    }

    /**
     * this method is called once when the event loop starts.
     * This method subscribes to 2 types of broadcasts: TickBroadcast and PurchaseOrderRequest.
     */
    @Override
    protected void initialize() {
    	/*
    	 * In this subscription the seller goes to the store, and tries to take the requested shoe,
    	 * with the information whether the client wished to take it only on discount.
    	 * the seller acts accordingly to the result of the taking action. 
    	 */
        subscribeRequest(PurchaseOrderRequest.class, v->{
            
        	Store store = Store.getInstance();
            PurchaseOrderRequest p = ((PurchaseOrderRequest)v);
            String client = p.getName();
            String shoeType = p.getShoeType();
            Boolean discount = p.isOnlyDiscount();
            
            BuyResult buyResult = store.take(shoeType, discount);
            LOGGER.info(this.getName() + ": " + client + ", Let me check for your shoe type: " + shoeType + ", requestedDiscount: " + discount);
            
            Receipt receipt = null;
            int requestTick = p.getRequestTick();
            switch (buyResult){
            	/*
            	 * Case that the shoe should have been sold, but it is out of stock:
            	 * In this case the seller sends a restock request to the manager, and awaits
            	 * the manager response.
            	 * If the manager says that there were no factories, the seller tells that to
            	 * the customer and returns him a null receipt.
            	 * If the manager says that he reserved a shoe of that type for the seller,
            	 * the seller sells the shoe to the customer, and files the receipt.
            	 */
                case NOT_IN_STOCK:
                	LOGGER.info(this.getName() + ": hey Manager get me shoes! " + "For " + client + ", " + p.getShoeType() + ", requestedDiscount: " + discount);
                	RestockRequest restock = new RestockRequest(this.getName(),shoeType,current_tick);
                    sendRequest(restock, wereThereFactories->{
                    	Receipt receipt2;
                    	if(wereThereFactories){
                    		LOGGER.info(this.getName() + ": yayy shoes!! :3" + "For " + client + ", " + p.getShoeType() + ", requestedDiscount: " + discount);
                    		receipt2 = new Receipt(this.getName(), p.getName(), p.getShoeType(),false,current_tick,requestTick,p.getAmount());
                    		store.file(receipt2);
                    	}else{
                    		LOGGER.info(this.getName() + ": sigh.. no shoes T_T " + "For " + client + ", " + p.getShoeType() + ", requestedDiscount: " + discount);
                    		receipt2 = null;
                    	}
                    	complete(p,receipt2);
                    });
                    break;
                /*
                 * Case that the client wanted to buy the shoe on discount, but there were no
                 * more shoes on discount.
                 * The seller will return a null receipt.
                 */
                case NOT_ON_DISCOUNT:
                	LOGGER.info(this.getName() + ": " + client + ", " + p.getShoeType() + " are NOT_ON_DISCOUNT");
                    complete(p,receipt);
                    break;
                /*
                 * Case that the client wanted to buy a shoe on regular price and there was such
                 * shoe - the seller gives a receipt to the client, and files it in the store.
                 */
                case REGULAR_PRICE:
                	LOGGER.info(this.getName() + ": " + client + ", " + p.getShoeType() + ", REGULAR_PRICE, request tick: " + requestTick);
                    receipt=new Receipt(this.getName(), p.getName(), p.getShoeType(),false,current_tick,requestTick,p.getAmount());
                    store.file(receipt);
                    complete(p,receipt);
                    break;
                /*
                 * Case that the requested shoe was on discount - the seller gives a discounted
                 * receipt to the client, and files the receipt.
                 */
                case DISCOUNTED_PRICE:
                	LOGGER.info(this.getName() + ": " + client + ", " + p.getShoeType() + ", DISOUNTED_PRICE, request tick: " + requestTick);
                    receipt=new Receipt(this.getName(), p.getName(), p.getShoeType(),true,current_tick,requestTick,p.getAmount());
                    store.file(receipt);
                    complete(p,receipt);
                    break;
            }

        });
        
        /*
         * In this subscription the seller awaits for the time tick to be 
         * the termination tick, and upon receiving it, the service terminates.
         */
        subscribeBroadcast(TickBroadcast.class, v->{
        	this.current_tick=((TickBroadcast)v).getTick();
			if(this.current_tick<=v.getTerminationTick()){
				LOGGER.finest(this.getName() + ": Current tick is: " + this.current_tick);
			}else{
				LOGGER.info(this.getName() + ": Terminating...");
				terminate();
			}
		});
    }
}
