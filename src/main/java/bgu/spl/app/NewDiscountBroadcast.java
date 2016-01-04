package bgu.spl.app;

import bgu.spl.mics.Broadcast;
/**
 * This is a broadcast message that is sent when the manager of the store
 * decides to have a sale on a specific shoe.
 * The NewDiscountBroadcast contains the following fields:
 * <ul>
 * <li><b>shoeType: String</b> - The shoe type on which the manager makes discount.</li>
 * <li><b>amount: int</b> - The amount of shoes that the manager decides to discount.</li>
 * <li><b>tick: int</b> - The time tick of this broadcast.</li>
 * </ul>
 * @author Avi
 *
 */
public final class NewDiscountBroadcast implements Broadcast {

	private final String shoeType;
	private final int amount;
	private final int tick;
	
	/**
	 * The constructor. Receives as parameters the shoe type on which there will be a discount,
	 * the amount of shoes that are going to be on discount, and this broadcast's time tick.
	 * @param shoeType <b>- String:</b> The shoe type on which the manager makes discount.
	 * @param amount <b>- int:</b> The amount of shoes that the manager decides to discount.
	 * @param tick <b>- int:</b> This broadcast's tick.
	 */
	public NewDiscountBroadcast(String shoeType, int amount, int tick) {
		this.shoeType = shoeType;
		this.amount = amount;
		this.tick = tick;
	}

	/**
	 * Gets the shoe type on which the manager makes discount.
	 * @return {@link String}: The shoe type on which the manager makes discount.
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Gets the amount of shoes that the manager decides to discount.
	 * @return int: The amount of shoes that the manager decides to discount.
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Gets the time tick of this broadcast.
	 * @return int: This broadcast's tick.
	 */
	public int getTick() {
		return tick;
	}

}
