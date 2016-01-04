package bgu.spl.app;

import bgu.spl.mics.Request;

/**
 * This is a request that is sent when a client wishes to buy a shoe.
 * Its result type is expected to be a {@link Receipt}. In case that the purchase 
 * was not completed successfully, null should be returned as the request result.<br>
 * The PurchaseOrderRequest contains the following fields:
 * <ul>
 * <li><b>name: String</b> - The name of the client.</li>
 * <li><b>shoeType: String</b> - The type of the requested shoe.</li>
 * <li><b>onlyDiscount: boolean</b> - True if the client wishes to buy only if the shoe is 
 * 		  on discount.</li>
 * <li><b>requestTick: int</b> - The tick in which the client requested to buy the shoe.</li>
 * <li><b>amount: int</b> - The amount of shoes to buy.</li>
 * </ul>
 * @author Avi
 *
 */
public final class PurchaseOrderRequest implements Request<Receipt> {

	private final String name;
	private final String shoeType;
	private final boolean onlyDiscount;
	private final int requestTick;
	private final int amount;
	
	/**
	 * The constructor. Receives as parameters the name of the client, the type of the
	 * requested shoe, boolean indicating if the client is interested in discounted purchase only,
	 * the tick in which the client requested to buy the shoe, and the amount of shoes to buy.
	 * @param name <b>- String:</b> the name of the client.
	 * @param shoeType <b>- String:</b> the type of the requested shoe.
	 * @param onlyDiscount <b>- boolean:</b> True if the client wishes to buy only if the shoe is 
	 * 		  on discount.
	 * @param requestTick <b>- int:</b> tick in which the client requested to buy the shoe.
	 * @param amount <b>- int:</b> the amount of shoes to buy.
	 */
	public PurchaseOrderRequest(String name, String shoeType, boolean onlyDiscount, int requestTick, int amount) {
		super();
		this.name = name;
		this.shoeType = shoeType;
		this.onlyDiscount = onlyDiscount;
		this.requestTick = requestTick;
		this.amount = amount;
	}

	/**
	 * Gets the client name.
	 * @return {@link String}: The client name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the desired shoe type.
	 * @return {@link String}: The desired shoe type.
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Gets the indicator whether the client wishes to buy only if the shoe is on discount.
	 * @return boolean: An indicator whether the client wishes to buy only if the shoe is on discount.
	 */
	public boolean isOnlyDiscount() {
		return onlyDiscount;
	}

	/**
	 * Gets the tick in which the client requested to buy the shoe.
	 * @return int: The tick in which the client requested to buy the shoe.
	 */
	public int getRequestTick() {
		return requestTick;
	}

	/**
	 * Gets the amount of shoes of the desired type to buy.
	 * @return int: The amount of shoes of the desired type to buy.
	 */
	public int getAmount() {
		return amount;
	}
	
	
}
