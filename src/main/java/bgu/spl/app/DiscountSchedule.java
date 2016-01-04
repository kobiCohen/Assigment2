package bgu.spl.app;

/**
 * This is an object which describes a schedule of a single discount that the 
 * manager will add to a specific shoe at a specific tick. It contains the fields:
 * <ul>
 * <li><b>shoeType: string</b> - the type of shoe to add discount to.</li>
 * <li><b>tick: int</b> - the tick number to send the add the discount at.</li>
 * <li><b>amount: int</b> - the amount of items to put on discount (i.e., if the amount
 * 	      is 3 than after selling 3 items the discount should be over).</li>
 * </ul>
 * @author Avi
 *
 */
public final class DiscountSchedule {

	private final String shoeType;
	private final int tick;
	private final int amount;
	
	/**
	 * The constructor. It gets as parameters the shoe type, the tick number 
	 * to add a discount at, and the amount of items to put on a discount.
	 * @param shoeType <b>- String:</b> The shoe type
	 * @param tick <b>- int:</b> The tick number to add a discount at.
	 * @param amount <b>- int:</b> The amount of items to put on a discount.
	 */
	public DiscountSchedule(String shoeType, int tick, int amount) {
		this.shoeType = shoeType;
		this.tick = tick;
		this.amount = amount;
	}

	/**
	 * Gets the shoe type.
	 * @return {@link String}: The type of the shoe.
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Gets the tick number to add a discount at.
	 * @return int: The tick number to add a discount at.
	 */
	public int getTick() {
		return tick;
	}

	/**
	 * Gets the amount of items to put on a discount.
	 * @return int: The amount of items to put on a discount.
	 */
	public int getAmount() {
		return amount;
	}
}
