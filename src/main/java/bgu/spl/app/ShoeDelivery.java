package bgu.spl.app;

import java.util.LinkedList;
import java.util.logging.Logger;

public class ShoeDelivery {

	private static Logger LOGGER = Logger.getGlobal();
	private int numOfOrderedShoes;
	private int numOfReservedShoes;
	private LinkedList<RestockRequest> reservedRestockRequests; 
	
	public ShoeDelivery(int orderedShoes){
		this.numOfOrderedShoes = orderedShoes;
		this.numOfReservedShoes = 0;
		this.reservedRestockRequests = new LinkedList<>();
	}
	
	/**
	 * Tries to reserve a shoe from this delivery for {@link RestockRequest} r.
	 * 
	 * @param r - The {@link RestockRequest} to add to this delivery waiting list.
	 * @return <b>True</b> if there was a shoe from this delivery to reserve for this request.<br>
	 * <b>False</b> if there were no free shoes from this delivery to reserve for this request.
	 */
	public boolean addRestockRequest(RestockRequest r){
		if(this.numOfOrderedShoes == this.numOfReservedShoes){
			LOGGER.fine("NO MORE FREE SHOES IN SHOEDELIVERY");
			return false;
		}else{
			this.reservedRestockRequests.add(r);
			this.numOfReservedShoes++;
			return true;
		}
	}
	
	/**
	 * Gets the list of {@link RestockRequest}'s that a shoe from this delivery is
	 * reserved for.
	 * @return {@link LinkedList<{@link RestockRequest}>} the list of reservedRestockRequests.
	 */
	public  LinkedList<RestockRequest> getReservedRestockRequests(){
		return this.reservedRestockRequests;
	}
	
	/**
	 * Returns the number of free shoes (not reserved shoes)
	 * @return the number of free shoes (not reserved shoes)
	 */
	public int getNotReservedShoes(){
		return this.numOfOrderedShoes-this.numOfReservedShoes;
	}
	
}
