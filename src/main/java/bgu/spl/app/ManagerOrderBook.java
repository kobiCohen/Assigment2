package bgu.spl.app;

import java.util.HashMap;
import java.util.logging.Logger;

public class ManagerOrderBook {

	private static Logger LOGGER = Logger.getGlobal();
	private HashMap<ManufacturingOrderRequest, ShoeDelivery> orders;
	private HashMap<String, ShoeDelivery> latestDelivery;
	
	public ManagerOrderBook(){
		this.orders = new HashMap<>();
		this.latestDelivery = new HashMap<>();
	}
	
	/**
	 * This method tries to insert the given {@link RestockRequest} to the last delivery
	 * of the requested shoe type.<br><br>
	 * 1. If all of the shoes in the last delivery are all reserved,
	 * the method creates a new delivery, puts the restock request there as the first request
	 * with a reserved shoe, and returns the new {@link ManufacturingOrderRequest} to the caller
	 * (the manager).<br><br>
	 * 2. If there weren't any deliveries of this shoe type ordered, It registers a
	 * new shoe type in the book, does the specified in paragraph 1, and returns the created 
	 * ManufacturingOrderRequest.<br><br>
	 * 3. If the last ordered delivery of that shoe type had a free shoe to reserve, it puts
	 * the restock request in the reservation list for that delivery, and returns null.
	 * @param manager - the name of the caller (probably some manager).
	 * @param restock - the restock request.
	 * @return {@link ManufacturingOrderRequest} or null, as specified above.
	 */
	public ManufacturingOrderRequest insertRestockRequest(String manager, RestockRequest restock){
		String shoeType = restock.getShoeType();
		int requestTick = restock.getRequestTick();
		ShoeDelivery sd;
		if(this.latestDelivery.containsKey(shoeType)){
			// If there is this shoe type in the book
			sd = this.latestDelivery.get(shoeType);
			boolean restockAdded = sd.addRestockRequest(restock);
			if(!restockAdded){
				// If the last delivery was all reserved
				LOGGER.fine("NEED TO ADD A NEW ORDER TO THE BOOK");
				ManufacturingOrderRequest newMOR = new ManufacturingOrderRequest(manager, shoeType, requestTick, (requestTick%5)+1);
				sd = new ShoeDelivery((requestTick%5)+1);
				sd.addRestockRequest(restock);
				this.latestDelivery.put(shoeType, sd);
				this.orders.put(newMOR, sd);
				return newMOR;
			}else{
				// If there were free shoes for reservation in the last delivery
				LOGGER.fine("RESTOCK REQUEST INSERTED TO EXISTING DELIVERY");
				return null;
			}
		}else{
			// If the shoe type is not in the book
			ManufacturingOrderRequest newMOR = new ManufacturingOrderRequest(manager, shoeType, requestTick, (requestTick%5)+1);
			sd = new ShoeDelivery((requestTick%5)+1);
			sd.addRestockRequest(restock);
			this.latestDelivery.put(shoeType, sd);
			this.orders.put(newMOR, sd);
			return newMOR;
		}
	}
	
	/**
	 * This method finds the {@link ShoeDelivery} that is mapped to the given 
	 * {@link ManufacturingOrderRequest} mor, and prepares to return it.
	 * It also checks if the key with the shoe type of the given mor maps to the same ShoeDelivery
	 * that it prepared, and if so, it removes that key from the latestDelivery map.
	 * @param mor - The given ManufacturingOrderRequest.
	 * @return A delivery of type {@link ShoeDelivery}
	 */
	public ShoeDelivery getCompletedDelivery(ManufacturingOrderRequest mor){
		ShoeDelivery retSD = this.orders.remove(mor);
		if(this.latestDelivery.get(mor.getShoeType())==retSD){
			this.latestDelivery.remove(mor.getShoeType());
		}
		return retSD;
	}
	
}
