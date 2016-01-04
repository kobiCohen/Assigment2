package bgu.spl.app;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ShoeStoreRunner {

	
	private static Logger LOGGER = Logger.getGlobal();
	
	
	public static void main(String[] args) throws SecurityException, IOException {
		// Logger Init Block======================================================
		ConsoleHandler console = new ConsoleHandler();
		console.setFormatter(new Formatter() {
			public String format(LogRecord record){
				return "[" + record.getLevel() + "] "
						+ record.getMessage() + "\n";
			}
		});
		
		FileHandler txt = new FileHandler("ShoeStoreOutput.txt");
		txt.setFormatter(new Formatter() {
			public String format(LogRecord record){
				return "[" + record.getLevel() + "] "
						+ record.getMessage() + "\r\n";
			}
		});
		
		LOGGER.setLevel(Level.INFO);
		console.setLevel(Level.FINEST);
		txt.setLevel(Level.FINEST);
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(console);
		LOGGER.addHandler(txt);
		// Logger Init Block======================================================
		
		
		Parser parser = new Parser();
		Wrapper w = parser.parse(args[0]);
		
		
		// Store set up: START=====================================================
		Store str = Store.getInstance();
		LinkedList<Shoe> initialStorage = w.Shoes_Types;
		while(!initialStorage.isEmpty()){
			Shoe s = initialStorage.removeFirst();
			str.add(s.get_type(), s.get_amount());
		}
		
		str.print();
		// Store set up: END=======================================================
		
		
		
		// Services set up START===================================================
		
		// Building TimeService: START
		int speed = w.services.time._speed;
		int duration = w.services.time._duration;
		TimeService timeService = new TimeService("GlobalClock", speed, duration);
		// Building TimeService: END
		
		// Building ManagementService: START
		LinkedList<Discount> dlist = w.services.manager._discount;
		LinkedList<DiscountSchedule> discounts = new LinkedList<>();
		while(!dlist.isEmpty()){
			Discount dlistDisc = dlist.removeFirst();
			DiscountSchedule d = new DiscountSchedule(dlistDisc._type, dlistDisc._time, dlistDisc._amount);
			discounts.add(d);
		}
		ManagementService managementService = new ManagementService("Manager", discounts);
		// Building ManagementService: END
		
		// Building ShoeFactoryService: START
		LinkedList<ShoeFactoryService> factories = new LinkedList<>();
		for(int i=1; i<=w.services.factories; i++){
			ShoeFactoryService factory = new ShoeFactoryService("Factory " + i);
			factories.add(factory);
		}
		// Building ShoeFactoryService: END
		
		// Building SellingService: START
		LinkedList<SellingService> sellers = new LinkedList<>();
		for(int i=1; i<=w.services.sellers; i++){
			SellingService seller = new SellingService("Seller " + i);
			sellers.add(seller);
		}
		// Building SellingService: END
		
		// Building WebsiteClientService: START
		LinkedList<WebsiteClientService> customers = new LinkedList<>();
		LinkedList<Client> clients = w.services.clients;
		while(!clients.isEmpty()){
			Client client = clients.removeFirst();
			String name = client.name;
			LinkedList<String> wishList = client._wishList;
			LinkedList<Purches> purches = client._purchases;
			LinkedList<PurchaseSchedule> purchases = new LinkedList<>();
			while(!purches.isEmpty()){
				Purches pr = purches.removeFirst();
				PurchaseSchedule purchaseSchedule = new PurchaseSchedule(pr._type, pr._time);
				purchases.add(purchaseSchedule);
			}
			WebsiteClientService wcs = new WebsiteClientService(name, purchases, wishList);
			customers.add(wcs);
		}
		// Building WebsiteClientService: END
		
		// Services set up END=====================================================
		
		
		
		// Setting up threads START================================================
		LinkedList<Thread> threads = new LinkedList<>();
		
		Thread timeThread = new Thread(timeService);
		
		Thread managerThread = new Thread(managementService);
		threads.add(managerThread);
		
		for(ShoeFactoryService sfs: factories){
			Thread factoryThread = new Thread(sfs);
			threads.add(factoryThread);
		}
		
		for(SellingService ss: sellers){
			Thread sellerThread = new Thread(ss);
			threads.add(sellerThread);
		}
		

		for(WebsiteClientService wcs: customers){
			Thread clientThread = new Thread(wcs);
			threads.add(clientThread);
		}
		
		// Setting up threads END================================================
		
		
		LOGGER.info("PREAPARING TO RUN");
		
		// Running threads START=================================================
		
		for(Thread t: threads){
			t.start();
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		timeThread.start();
		
		
		try {
			timeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		str.print();
		System.out.println("should end");
		
	}

}
