package bgu.spl.app;

import bgu.spl.mics.Request;

/**
 * This is a request that is sent by the selling service to the store manager so that he
 * will know that he need to order new shoes from a factory. Its response type expected to be a
 * boolean where the result: true means that the order is complete and the shoe is reserved for
 * the selling service and the result: false means that the shoe cannot be ordered (because there
 * were no factories available).<br>
 * The RestockRequest contains the following fields:
 * <ul>
 * <li><b>name: String</b> - The name of the selling service.</li>
 * <li><b>shoeType: String</b> - The type of the requested shoe.</li>
 * <li><b>requestTick: int</b> - The tick in which the selling service requested to restock a shoe.</li>
 * </ul>
 * @author Avi
 *
 */
public final class RestockRequest implements Request<Boolean> {

	private final String name;
	private final String shoeType;
	private final int requestTick;
	
	/**
	 * The constructor. Receives as parameters the name of the selling service requesting
	 * to restock, the shoe type to be restocked, and the tick in which this request was made.
	 * @param name <b>- String:</b> the name of the selling sevice. 
	 * @param shoeType <b>- String:</b> the type of the requested shoe.
	 * @param requestTick <b>- int:</b> The tick in which the selling service requested to restock a shoe.
	 */
	public RestockRequest(String name, String shoeType, int requestTick) {
		super();
		this.name = name;
		this.shoeType = shoeType;
		this.requestTick = requestTick;
	}
	
	/**
	 * Gets the sale service name.
	 * @return {@link String}: The sale service name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the requested shoe type.
	 * @return {@link String}: The requested shoe type.
	 */
	public String getShoeType() {
		return shoeType;
	}
	
	/**
	 * Gets the tick in which the sale service requested to restock the shoe type.
	 * @return int: The tick in which the sale service requested to restock the shoe type.
	 */
	public int getRequestTick() {
		return requestTick;
	}
	
}
