package bgu.spl.app;

import bgu.spl.mics.MicroService;
import bgu.spl.app.DiscountSchedule;

import java.util.LinkedList;
import java.util.logging.Logger;


/**
 * Created by kobi on 12/19/2015.
 */
public class ManagementService extends MicroService {
	
	private static Logger LOGGER = Logger.getGlobal();
	
	/**
	 * The lists of discounts that the manager wishes to hand out.
	 */
    private LinkedList <DiscountSchedule> _discounts;
    
    /**
     * The orders book of the manager.
     */
    private ManagerOrderBook orderBook;
    
    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public ManagementService(String name, LinkedList<DiscountSchedule> lz) {
        super(name);
        _discounts=lz;
        orderBook = new ManagerOrderBook();
    }
    /**
     * this method is called once when the event loop starts.
     * The manager subscribes to tick broadcasts and to restock requests.
     */
    @Override
    protected void initialize() {
    	/*
    	 * In this subscription the manager subscribes to the time tick, 
    	 * and performers the actions as described below.
    	 */
        subscribeBroadcast(TickBroadcast.class, v->{
            this.current_tick=((TickBroadcast)v).getTick();
            
            /*
             * In this block the manager checks his discount list for a discount that he needs
             * to hand out at this tick, and if there is, the manager hands it out.
             */
            if(_discounts!=null){
	            _discounts.forEach(k->{
	                if (k.getTick()==current_tick){
	                    Store store=Store.getInstance();
	                    store.addDiscount(k.getShoeType(),k.getAmount());
	                    LOGGER.info(this.getName() + ": +++++DISCOUNT!+++++ on shoe type: " + k.getShoeType() + ". Discounted amount: " + k.getAmount());
	                    sendBroadcast( new NewDiscountBroadcast(k.getShoeType(),k.getAmount(),k.getTick()));
	                }
	            });
            }
            
            /*
             * In this block the manager awaits for the time tick to be 
             * the termination tick, and upon receiving it, the service terminates.
             */
            if(this.current_tick<=v.getTerminationTick()){
				LOGGER.finest(this.getName() + ": Current tick is: " + this.current_tick);
			}else{
				LOGGER.info(this.getName() + ": Terminating...");
				terminate();
			}
        });
        
        /*
         * In this subscription the manager gets a restock request, and puts the
         * request in his book as described below
         */
        subscribeRequest(RestockRequest.class, v->{
        	/*
        	 * Puts the RestockRequest into the book, expecting to get
        	 * a manufacture order or null.
        	 */
        	ManufacturingOrderRequest manOrder = orderBook.insertRestockRequest(this.getName(), v);
        	if(manOrder!=null){
        		/*
        		 * If the manager got a manufacture order that means that there were no
        		 * free shoes for reservation from previous manufacture orders.
        		 * He then sends this manufacture order to a factory.
        		 */
        		LOGGER.info(this.getName() + ": ORDERING SHOES: " + manOrder.getShoeType() + ", Number of shoes: " + manOrder.getAmount());
        		boolean existFactory = sendRequest(manOrder, t->{
        			/*
        			 * The callback function gets the receipt that the factory issued.
        			 * It then gets from the order book the completed delivery, and 
        			 * distributes its reserved shoes to the sellers. The manager then puts
        			 * the rest of the shoes from that delivery to the store.
        			 * Finally if files the receipt.
        			 */
        			ShoeDelivery completedDelivery = orderBook.getCompletedDelivery(manOrder);
        			Store store = Store.getInstance();
        			store.add(v.getShoeType(), completedDelivery.getNotReservedShoes());
        			store.file(t);
        			System.out.println(t);
        			LinkedList<RestockRequest> reserves = completedDelivery.getReservedRestockRequests();
        			while(!reserves.isEmpty()){
        				RestockRequest restReq = reserves.removeFirst();
        				complete(restReq, true);
        			}
        			
        		});
        		
        		/*
        		 * For debugging reasons - prints out whether there were available factories.
        		 */
        		if(existFactory){
        			LOGGER.info(this.getName() + ": A new manufacuting order was created for shoe type: " + v.getShoeType());
        		}else{
        			LOGGER.info(this.getName() + ": There were no factories avilable");
        		}
        	}else{
        		/*
        		 * This is the case that the manufacture order returned from the book was null
        		 * It means that there was a previous delivery of that shoe type that still
        		 * had free shoes to reserve, so there was no need to send another manufacture
        		 * request.
        		 */
        		LOGGER.info(this.getName() + ": SHOES ARE ALREDY ORDERED: " + v.getShoeType());
        	}
        });
    }
}
