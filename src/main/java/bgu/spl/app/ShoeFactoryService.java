package bgu.spl.app;

import bgu.spl.mics.MicroService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Created by kobi on 12/19/2015.
 */
public class ShoeFactoryService extends MicroService {

	private static Logger LOGGER = Logger.getGlobal();

	/**
	 * A list of the orders that this factory has to complete
	 * Those orders are issued from the manager.
	 */
    private ConcurrentLinkedQueue <Order> orders= new ConcurrentLinkedQueue<>();
    
    /**
     * @param name the micro-service name
     */
    public ShoeFactoryService(String name) {
        super(name);
    }

    /**
     * this method is called once when the event loop starts.
     * it subscribes to tick broadcasts and to manufacture order requests.
     */
    @Override
    protected void initialize() {
    	
    	/*
    	 * In this subscription the factory takes the manufacturing request,
    	 * and uses its info to create an order, for future completion.
    	 */
        subscribeRequest(ManufacturingOrderRequest.class, v->{
        	LOGGER.info(this.getName() + ": SHOES HAVE BEEN ORDERED!");
         	Order order=new Order(v.getShoeType(),v.getAmount(),v);
            orders.add(order);
        });
        
        /*
    	 * In this subscription the factory subscribes to the time tick, 
    	 * and performers the actions as described below.
    	 */
        subscribeBroadcast(TickBroadcast.class, v->{
            /*
             * In this block the factory awaits for the termination tick, 
             * when it arrives the factory terminates.
             */
        	this.current_tick=v.getTick();
            if(v.getTick()<=v.getTerminationTick()){
				LOGGER.finest(this.getName() + ": Current tick is: " + this.current_tick);
			}else{
				LOGGER.info(this.getName() + ": Terminating...");
				terminate();
			}
            
            /*
             * In this block the factory goes to the current order (if there are any)
             * and continues to make shoes for that order.
             */
            Order order=orders.peek();
            if (order!= null) {
                LOGGER.info(this.getName() + ": Manufacturing shoe type " + order.getType() + ": " + (order.get_totalAmount()-order.get_amountLeft()) + "/" + order.get_totalAmount());
            	order.makeShoe();
                if (order.get_amountLeft() == 0) {
                	/*
                	 * If the order is complete, 
                	 * The factory, sends the delivery to the manager, and sends
                	 * him a receipt with the amount of shoes delivered.
                	 */
                    Receipt receipt = new Receipt(this.getName(), order.getManufacturingOrderRequest().getName(), order.getType(), false, current_tick, order.getManufacturingOrderRequest().getRequestTick(), order.get_totalAmount());
                    LOGGER.info(this.getName() + ": Finished manufacturing " + order.getType() + ", " + order.get_totalAmount() + " units.");
                    complete(order.getManufacturingOrderRequest(), receipt);
                    orders.remove(order);
                }
            }
        });
    }
}
