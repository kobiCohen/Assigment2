package bgu.spl.mics.impl;

import junit.framework.TestCase;

/**
 * Created by kobi on 12/30/2015.
 */
public class MessageBusImplTest extends TestCase {

    public void testGetInstance() throws Exception {
        /*
         * Checking if there is not more than one instance
         */
        MessageBusImpl messageBus=MessageBusImpl.getInstance();
        MessageBusImpl messageBus1=MessageBusImpl.getInstance();
        assertEquals(messageBus, messageBus1);
    }
}