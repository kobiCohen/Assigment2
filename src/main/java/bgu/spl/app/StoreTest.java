package bgu.spl.app;

import junit.framework.TestCase;

/**
 * Created by kobi on 12/30/2015.
 */
public class StoreTest extends TestCase {

    public void testGetInstance() throws Exception {
        Store store=Store.getInstance();
        assertEquals(store, Store.getInstance());

    }

    public void testTake() throws Exception {
        
         Store mockStore =Store.getInstance();
        /*
         *testing normal sale.
         */
       // mockStore.add("crocks", 1);
        BuyResult buyResult= mockStore.take("crocks", false);
        assertEquals(BuyResult.REGULAR_PRICE,buyResult);
        /*
         *Testing not in stock situation
         */
        buyResult=mockStore.take("crocks", false);
        assertEquals(BuyResult.NOT_IN_STOCK,  buyResult  );
        /*
         *Testing discount sale
         */
        mockStore.add("crocks", 2);
        mockStore.addDiscount("crocks", 1);
        buyResult= mockStore.take("crocks", true);
        assertEquals(BuyResult.DISCOUNTED_PRICE, buyResult);
        /*
         *Testing not in discount situation
         */
        buyResult= mockStore.take("crocks", true);
        assertEquals(BuyResult.NOT_ON_DISCOUNT, buyResult);
    }

    public void testAdd() throws Exception {
        /**
         * Checking if the shoe was added
         */
        Store mockStore=Store.getInstance();
        mockStore.add("crocks", 1);
        BuyResult buyResult= mockStore.take("crocks", false);
        assertEquals(BuyResult.REGULAR_PRICE, buyResult);


    }

    public void testAddDiscount() throws Exception {
        /**
         * Checking if discount was added
         */
        Store mockStore=Store.getInstance();   mockStore.add("crocks", 2);
        mockStore.addDiscount("crocks", 1);
        BuyResult buyResult= mockStore.take("crocks", true);
        assertEquals(BuyResult.DISCOUNTED_PRICE, buyResult);
    }
}