package bgu.spl.app;

import bgu.spl.mics.Request;

/**
 * This is a request that is sent when the the store manager want that a shoe factory
 * will manufacture a shoe for the store. Its result type expected to be a {@link Receipt}.
 * On the case the manufacture was not completed successfully null should be returned as the
 * request result.
 * The PurchaseOrderRequest contains the following fields:
 * <ul>
 * <li><b>name: String</b> - the name of the store manager.</li>
 * <li><b>shoeType: String</b> - the type of the requested shoes.</li>
 * <li><b>requestTick: int</b> - tick in which the manager requested to order the shoes.</li>
 * <li><b>amount: int</b> - the amount of shoes to order.</li>
 * @author Avi
 *
 */
public final class ManufacturingOrderRequest implements Request<Receipt> {

	private final String name;
	private final String shoeType;
	private final int requestTick;
	private final int amount;
	
	/**
	 * The constructor. Receives as parameters the name of the manager, the type of the
	 * requested shoe, the tick in which the manager requested to order the shoe, and the amount 
	 * of shoes to order.
	 * @param name <b>- String:</b> the name of the manager.
	 * @param shoeType <b>- String:</b> the type of the requested shoes.
	 * @param requestTick <b>- int:</b> tick in which the manager requested to order the shoe.
	 * @param amount <b>- int:</b> the amount of shoes to order.
	 */
	public ManufacturingOrderRequest(String name, String shoeType, int requestTick, int amount) {
		super();
		this.name = name;
		this.shoeType = shoeType;
		this.requestTick = requestTick;
		this.amount = amount;
	}

	/**
	 * Gets the manager name.
	 * @return {@link String}: The manager name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the desired shoes type.
	 * @return {@link String}: The desired shoes type.
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Gets the tick in which the manager requested to order the shoe.
	 * @return int: The tick in which the manager requested to order the shoe.
	 */
	public int getRequestTick() {
		return requestTick;
	}

	/**
	 * Gets the amount of shoes of the desired type to order.
	 * @return int: The amount of shoes of the desired type to order.
	 */
	public int getAmount() {
		return amount;
	}
	
}
