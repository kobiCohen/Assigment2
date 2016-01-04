package bgu.spl.app;

/**
 * This is an object which describes a schedule of a <b>single</b> client-purchase 
 * at a specific tick. It contains the fields:
 * <ul>
 * <li><b>shoeType: string</b> - the type of shoe to purchase.</li>
 * <li><b>tick: int</b> - the tick number to send the PurchaseOrderRequest at.</li>
 * </ul>
 * @author Avi
 *
 */
public final class PurchaseSchedule {

	private final String shoeType;
	private final int tick;
	
	/**
	 * The constructor. It gets as parameters the type of shoe to purchase, and 
	 * the tick number to send the PurchaseOrderRequest at.
	 * @param shoeType <b>- String:</b> the type of shoe to purchase.
	 * @param tick <b>- int:</b> the tick number to send the PurchaseOrderRequest at.
	 */
	public PurchaseSchedule(String shoeType, int tick) {
		this.shoeType = shoeType;
		this.tick = tick;
	}

	/**
	 * Gets the shoe type
	 * @return {@link String}: The shoe type.
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Gets the tick number to send the PurchaseOrderRequest at.
	 * @return <b>int</b>: the tick number to send the PurchaseOrderRequest at.
	 */
	public int getTick() {
		return tick;
	}
}
