package bgu.spl.app;

/**
 * This class defines an object that represents information about a <b>single</b>
 * type of shoe in the store(e.q, red-Sneakers, Blue-Sandals, etc.). It contains
 * the following fields:<br><br>
 * <ul>
 * <li><b>shoeType: String</b> - the type of the shoe (e.g., red-sneakers, blue-sandals, etc.)
 * 				 that this storage info regards.</li>
 * <li><b>amountOnStorage: int</b> - the number of shoes of shoeType currently on the storage</li>
 * <li><b>discountedAmount: int</b> - amount of shoes in this storage that can be sale in a 
 * 						 discounted price. (i.e., if amountOnStorage is 3 and 
 * 						 discountedAmount is 1 it means that 1 out of the 3 shoes
 *						 have a discount, after selling this one shoe the discount 
 *						 will end)</li>
 * </ul>
 * 
 * It may also contain different methods that allow modifying this values by the store.
 * <br>
 * @author Avi
 *
 */
public class ShoeStorageInfo {

	private final String shoeType;
	private int amountOnStorage;
	private int discountedAmount;
	
	/**
	 * The constructor. It gets as parameters a shoe type, amount of shoes, and
	 * amount of shoes on discount.
	 * @param shoeType <b>- String:</b> the type of the shoes.
	 * @param amountOnStorage - <b>int:</b> the amount of shoes in the storage.
	 * @param discountedAmount - <b>int:</b> the amount of the shoes that are on discount.
	 */
	public ShoeStorageInfo(String shoeType, int amountOnStorage, int discountedAmount) {
		super();
		this.shoeType = shoeType;
		this.amountOnStorage = amountOnStorage;
		this.discountedAmount = discountedAmount;
	}

	/**
	 * Getter that returns the type of the shoe.
	 * @return {@link String}: the type of the shoe.
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Getter that returns the amount of shoes of that type.
	 * @return int: the number of shoes of that type in the storage.
	 */
	public int getAmountOnStorage() {
		return amountOnStorage;
	}

	/**
	 * Setter that sets the amount of shoes of that type.
	 * @param amountOnStorage - int: the number of shoes of that type to be set.
	 */
	public void setAmountOnStorage(int amountOnStorage) {
		this.amountOnStorage = amountOnStorage;
	}

	/**
	 * Getter that returns the amount of shoes on discount of that type.
	 * @return int: the amount of shoes of that type on discount.
	 */
	public int getDiscountedAmount() {
		return discountedAmount;
	}

	/**
	 * Setter that sets the amount of shoes on discount of that type.
	 * @param discountedAmount - int: the amount of shoes of that type to be 
	 * 		  discounted.
	 */
	public void setDiscountedAmount(int discountedAmount) {
		this.discountedAmount = discountedAmount;
	}

	/**
	 * Returns a string description of that object in th following format:
	 * {shoeType: shoeType, amount: amountOnStorage, dicountedAmount: discountedAmount}
	 */
	@Override
	public String toString() {
		return "{shoeType: " + shoeType + ", amount: " + amountOnStorage + ", dicountedAmount: " + discountedAmount + "}\n";
	}
	
}
