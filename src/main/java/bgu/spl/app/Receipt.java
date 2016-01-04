package bgu.spl.app;

/**
 * An Immutable object representing a receipt that should be sent to a client
 * after buying a shoe (when the client’s PurchaseRequest completed). 
 * The receipt contains the following fields:
 * <ul>
 * <li><b>seller: string</b> - the name of the service which issued the receipt.</li>
 * <li><b>customer: string</b> - the name of the service this receipt issued to 
 * 				 (the client name or “store”).</li>
 * <li><b>shoeType: string</b> - the shoe type bought.</li>
 * <li><b>discount: boolean</b> - indicating if the shoe was sold at a discounted price.</li>
 * <li><b>issuedTick: int</b> - tick in which this receipt was issued (upon completing the 
 * 				   corresponding request).</li>
 * <li><b>requestTick: int</b> - tick in which the customer requested to buy the shoe.</li>
 * <li><b>amountSold: int</b> - the amount of shoes sold.</li>
 * </ul>
 * 
 * @author Avi
 *
 */
public final class Receipt {

	private final String seller;
	private final String customer;
	private final String shoeType;
	private final boolean discount;
	private final int issuedTick;
	private final int requestTick;
	private final int amountSold;
	
	/**
	 * The constructor. It gets as parameters the seller name, the customer name, 
	 * the requested shoe type, a boolean that says if the shoe was on discount,
	 * the tick in which the receipt was issued, the tick in which the request was 
	 * made, and the amount sold.
	 * @param seller <b>- String:</b> The name of the seller(some {@link SellingService}).
	 * @param customer <b>- String:</b> The name of the customer (some {@link WebsiteClientService}).
	 * @param shoeType <b>- String:</b> The type of the requested shoe.
	 * @param discount <b>- boolean:</b> Indicates if the requested shoe was bought on discount.
	 * @param issuedTick <b>- int:</b> The tick of the Receipt issue.
	 * @param requestTick <b>- int:</b> The tick in which the request was made.
	 * @param amountSold <b>- int:</b> The amount of sold shoes.
	 */
	public Receipt(String seller, String customer, String shoeType, boolean discount, int issuedTick, int requestTick,
			int amountSold) {
		this.seller = seller;
		this.customer = customer;
		this.shoeType = shoeType;
		this.discount = discount;
		this.issuedTick = issuedTick;
		this.requestTick = requestTick;
		this.amountSold = amountSold;
	}

	/**
	 * Gets the seller's name.
	 * @return {@link String}: The name of the seller (some {@link SellingService}).
	 */
	public String getSeller() {
		return seller;
	}

	/**
	 * Gets the customer name.
	 * @return {@link String}: The name of the seller (some {@link WebsiteClientService}).
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * Gets the type of the shoe.
	 * @return {@link String}: The type of the shoe.
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Indicates if the shoe was sold on discount.
	 * @return boolean: True if the shoe was sold on discount, else False.
	 */
	public boolean isDiscount() {
		return discount;
	}

	/**
	 * Gets the tick in which the receipt was issued.
	 * @return int: The tick in which the receipt was issued.
	 */
	public int getIssuedTick() {
		return issuedTick;
	}

	/**
	 * Gets the tick in which the request was made by the client.
	 * @return int: The tick in which the request was made by the client.
	 */
	public int getRequestTick() {
		return requestTick;
	}

	/**
	 * Gets the amount sold of shoes sold in this request.
	 * @return int: The amount of shoes sold in this request.
	 */
	public int getAmountSold() {
		return amountSold;
	}

	/**
	 * Returns a string description of that object in the following format:<br>
	 * <br>|seller: seller
	 * <br>|customer: customer
	 * <br>|shoeType: shoeType
	 * <br>|discount: discount
	 * <br>|issuedTick: issuedTick
	 * <br>|requestTick: requestTick
	 * <br>|amountSold: amountSold
	 */
	@Override
	public String toString() {
		return "\t|seller: " + seller + "\n\t|customer: " + customer + "\n\t|shoeType: " + shoeType + "\n\t|discount: "
				+ discount + "\n\t|issuedTick: " + issuedTick + "\n\t|requestTick: " + requestTick + "\n\t|amountSold: "
				+ amountSold + "\n";
	}
}
