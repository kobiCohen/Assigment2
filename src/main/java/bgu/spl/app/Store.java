package bgu.spl.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The store object -  as a thread safe singleton
 * 
 * @author Avi
 *
 */


public class Store{
	
	private int client_receit_count;
	private int manager_receit_count;
	private int client_request_count;

	/**
	 * a map containing {@link ShoeStorageInfo} for every shoe type that is in the store
	 * <br>
	 * <br>
	 * <b> String - </b> 	The type of the shoe
	 * <br>
	 * <b> ShoeStorageInfo - </b> 	the {@link ShoeStorageInfo} for that shoe type
	 * 
	 */
	private ConcurrentHashMap<String, ShoeStorageInfo> _shoesList;
	/**
	 * a queue-represented list of the receipts that the store issued 
	 * <br>
	 * <br>
	 * <b> Receipt - </b> 	The receipt
	 */
	private ConcurrentLinkedQueue<Receipt> _receiptList;
	
	
	// THIS IS THE THREAD-SAFE INITIALIZATION BLOCK: START

	private static class StoreHolder {
		private static Store instance = new Store();
	}

	private Store() {
		_shoesList = new ConcurrentHashMap<>();
		_receiptList = new ConcurrentLinkedQueue<>();
	}

	public static Store getInstance() {
        return StoreHolder.instance;
    }
	// THIS IS THE THREAD-SAFE INITIALIZATION BLOCK: END
		
	/**
	 * This load function iterates over the {@code shoesList} and stores every
	 * {@link ShoeStorageInfo} in the {@code _shoesList} map.
	 * It also Instantiates a new {@link ConcurrentLinkedQueue} for the receipts.
	 * @param storage - the array of {@link ShoeStorageInfo}'s to be inserted into {@code _shoesList}.
	 */
	public void load(ShoeStorageInfo[] storage) {
		for(ShoeStorageInfo sInfo: storage){
			_shoesList.putIfAbsent(sInfo.getShoeType(), sInfo);
		}
		_receiptList = new ConcurrentLinkedQueue<Receipt>();
	}
	
	/**
	 * This method will attempt to take a single {@code shoeType} from the store.
	 * It receives the {@code shoeType} to take and a boolean - 
	 * {@code onlyDiscount} which indicates that the caller 
	 * <b>(some sale service)</b> wishes to take the item only if it is on
	 * discount.
	 * <br><br>
	 * Its result is an {@link Enum} which has the following values:
	 * <br>
	 * NOT_IN_STOCK - the shoe isn't even in the stock<br>
	 * NOT_ON_DISCOUNT - there are no discounted shoes of the requested type<br>
	 * REGULAR_PRICE - a shoe of the requested type was taken on normal price<br>
	 * DISCOUNTED_PRICE - a shoe of the requested type was taken on discounted price<br>
	 * 
	 * @param shoeType - the desired shoe to be taken.
	 * @param onlyDiscount - true if that caller (sellservice) is interested
	 * 		  to take the shoeType only if it is on discount.
	 * @return an {@link Enum} as described above.
	 */
	public synchronized BuyResult take(String shoeType, boolean onlyDiscount){
		/*
		 * buyres is the Enum to be returned - see javadoc.
		 */
		this.client_request_count++;
		BuyResult buyres;
		ShoeStorageInfo desiredShoe = _shoesList.get(shoeType);
		try{	
			if(onlyDiscount){
				// If the client wants a discount
				if(desiredShoe.getDiscountedAmount()>0){
					// If there are shoes on discount
					synchronized(desiredShoe){
						// Makes sure that no other seller will try to take from this specific shoe
						// type while the current thread is taking from it
						if(desiredShoe.getAmountOnStorage()>0){
							// If there are shoes in the storage
							desiredShoe.setAmountOnStorage(desiredShoe.getAmountOnStorage()-1);
							desiredShoe.setDiscountedAmount(desiredShoe.getDiscountedAmount()-1);
							buyres = BuyResult.DISCOUNTED_PRICE;
						}else{
							// If there aren't shoes in the storage
							buyres = BuyResult.NOT_IN_STOCK; // DONCARE CASE
						}
					}
				}else{
					// If there aren't shoes on discount
					buyres = BuyResult.NOT_ON_DISCOUNT;
				}
			}else{
				// If the client doesn't care about a discount
				if(desiredShoe.getDiscountedAmount()>0){
					// If there are shoes on discount
					synchronized(desiredShoe){
						// Makes sure that no other seller will try to take from this specific shoe
						// type while the current thread is taking from it
						if(desiredShoe.getAmountOnStorage()>0){
							// If there are shoes in the storage
							desiredShoe.setAmountOnStorage(desiredShoe.getAmountOnStorage()-1);
							desiredShoe.setDiscountedAmount(desiredShoe.getDiscountedAmount()-1);
							buyres = BuyResult.DISCOUNTED_PRICE;
						}else{
							// If there aren't shoes in the storage
							buyres = BuyResult.NOT_IN_STOCK; // DONTCARE CASE
						}
					}
				}else{
					// If there aren't shoes on discount
					synchronized(desiredShoe){
						// Makes sure that no other seller will try to take from this specific shoe
						// type while the current thread is taking from it
						if(desiredShoe.getAmountOnStorage()>0){
							// If there are shoes in the storage
							desiredShoe.setAmountOnStorage(desiredShoe.getAmountOnStorage()-1);
							buyres = BuyResult.REGULAR_PRICE;
						}else{
							// If there aren't shoes in the storage
							buyres = BuyResult.NOT_IN_STOCK;
						}
					}
				}
			}
		}catch(NullPointerException e){
			// In case that the shoe is not even offered in the store, the seller is just going to
			// reply with a null as a request result - just like in the case of NOT_ON_DISCOUNT
			buyres = BuyResult.NOT_IN_STOCK;
		}
		//returns the value of buyres - according to the outcome of the take choice tree
		return buyres;
	}
	
	/**
	 * This method adds the given {@code amount} to the {@link ShoeStorageInfo}
	 * of the given {@code shoeType}.
	 * @param shoeType - the given {@code shoeType} to add the given {@code amount} to.
	 * @param amount - the given {@code amount} to add to the given {@code shoeType}.
	 */
	public void add(String shoeType, int amount){
		if(!_shoesList.containsKey(shoeType)){
			/* cares of the case that the manager decided to increase the
			 * variety of shoes by adding a new shoeType to the ShoeStorageInfo.
			 */
			addNewShoeType(shoeType, amount, 0);
		}else{
			// the shoeType is in the store, just increase it's quantity
			ShoeStorageInfo shoeT = _shoesList.get(shoeType);
			int oldAmount = shoeT.getAmountOnStorage();
			int newAmount = oldAmount+amount;
			shoeT.setAmountOnStorage(newAmount);
		}
	}
	
	/**
	 * This method adds the given {@code amount} of discounts to the 
	 * {@link ShoeStorageInfo} of the given {@code shoeType}.<br>
	 * If the shoe type that the manager sends a discount for is not in the store,
	 * the by logic, a manager will not declare a discount on a shoe type that he doesn't
	 * have. So, obviously, the manages has added to the store a new shoe type,
	 * and issued a discount on it.
	 * @param shoeType - the given {@code shoeType} to add the given {@code amount}
	 * 		  of discounts to.
	 * @param amount - the given {@code amount} of discounts to add to the given 
	 * {@code shoeType}.
	 */
	public void addDiscount(String shoeType, int amount) {
		if(!_shoesList.containsKey(shoeType)){
			/* cares of the case that the manager decided to add a discount on
			 * a type of shoes that is not in the storage.
			 */
			addNewShoeType(shoeType, amount, amount);
		}else{
			// the shoeType is in the store, just increase it's amount on discount
			
			// Important - the invariant says that at every moment, the amount of shoes on
			// discount will not exceed the amount of shoes in the storage
			ShoeStorageInfo shoeT = _shoesList.get(shoeType);
			int oldDiscountAmount = shoeT.getDiscountedAmount();
			
			// Important - the invariant says that at every moment, the amount of shoes on
			// discount will not exceed the amount of shoes in the storage
			int newDiscountAmount = Math.min(oldDiscountAmount+amount, shoeT.getAmountOnStorage());
			
			shoeT.setDiscountedAmount(newDiscountAmount);
		}
	}
	
	/**
	 * In some cases, there may be a request to add an amount of a shoeType that is
	 * not in the storage, or to add a discount on such shoeType.
	 * In such case, this function will be called, and it will create a new shoeType
	 * in the storage.
	 * @param shoeType - the name of the shoe type to add
	 * @param amount - the amount of shoes of that type to add
	 * @param discountedAmount - the amount of discounted shoes of that amount to add.
	 */
	private void addNewShoeType(String shoeType, int amount, int discountedAmount){
		ShoeStorageInfo newShoeType = new ShoeStorageInfo(shoeType, amount, discountedAmount);
		_shoesList.putIfAbsent(shoeType, newShoeType);
	}
	
	/**
	 * Saves the given {@link Receipt} in the store
	 * @param receipt - the given {@link Receipt} to save 
	 */
	public void file(Receipt receipt){
		// This is Thread - Safe
		synchronized(this._receiptList){
			if(receipt.getCustomer()=="Manager"){
				this.manager_receit_count++;
			}else{
				this.client_receit_count++;
			}
			_receiptList.add(receipt);
		}
	}
	
	/**
	 * This method prints to the standard output the following information:<br>
	 * * For each item on stock - its name, amount, and discounted amount.<br>
	 * * For each receipt filed in the store - all of its fields.<br>
	 */
	public void print(){
		String output = "===================Store daily summary: HEAD==================";
		output += "\n\n";
		output += "Stock summary:\n";
		for(ShoeStorageInfo sho: _shoesList.values()){
			output += sho.toString();
		}
		output += "\nReceipts:\n";
		int receiptNumber = 1;
		for(Receipt rec: _receiptList){
			output += "Receipt Number " + receiptNumber + ":\n" + rec.toString() + "\n\n";
			receiptNumber++;
		}
		output += "Manager receipts: " + this.manager_receit_count + ", Client receipts: " + this.client_receit_count + ", Client requests: " + this.client_request_count + "\n\n";
		output += "==================Store daily summary: BOTTOM=================";
		System.out.println(output);
		
	}
	
	
}
