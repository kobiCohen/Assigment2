package bgu.spl.mics.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;
import bgu.spl.mics.RequestCompleted;

public class MessageBusImpl implements MessageBus {
	/**
	 * a map connecting every request to the services subscribed to get that request type 
	 * <br>
	 * <br>
	 * <b> Class - </b> 	The class of the request (the request type)
	 * <br>
	 * <b> BlockingQueue&ltMicroService&gt - </b> 	the queue of services interested in receiving that request type
	 * 
	 */
	public ConcurrentHashMap<Class, BlockingQueue<MicroService>> _listeners = new ConcurrentHashMap<>();
	/**
	 * a map connecting every request to the service that sent it
	 * <br>
	 * <br>
	 * <b> Request - </b>	the sent request
	 * <br>
	 * <b> MicroService - </b>	the service that sent that request
	 */
	public ConcurrentHashMap<Request, MicroService> _requestToRequester = new ConcurrentHashMap<>();
	/**
	 * a map connecting every <b>registered</b> service to its message queue
	 * <br>
	 * <br>
	 * <b> MicroService - </b>	a registered service
	 * <br>
	 * <b> BlockingQueue&ltMessage&gt - </b>	the queue of messages waiting to be handked by this service
	 */
	public ConcurrentHashMap<MicroService, BlockingQueue<Message>> _services = new ConcurrentHashMap<>();

	// THIS IS THE THREAD-SAFE INITIALIZATION BLOCK: START

	private static class MessageBusImplHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {

	}

	public static MessageBusImpl getInstance() {
        return MessageBusImplHolder.instance;
    }
	// THIS IS THE THREAD-SAFE INITIALIZATION BLOCK: END
	
	@Override
	public void subscribeRequest(Class<? extends Request> type, MicroService m) {
		if(_listeners.get(type)==null){

			addMessageType(type);
		}
		try {
			_listeners.get(type).put(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (_listeners.get(type) == null) {
			addMessageType(type);
		}
		_listeners.get(type).offer(m);

	}
	/*
	 * (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#complete(bgu.spl.mics.Request, java.lang.Object)
	 * 
	 * Creating a 'RequestCompleted' request, and then using the hash map of _requestToRequester
	 * to find the original requester.
	 */

	@Override
	public <T> void complete(Request <T> r, T result) {
		// Making the 'RequestCompleted' request
		RequestCompleted<T> completed = new RequestCompleted<T>((Request)r, result);
		// Getting the original requester, and sending him the result
		MicroService _requester = _requestToRequester.get(r);

		if (_requester!=null) {
			/*
			 * checking if the requester still exists, if so, sending him a Request completed message
			 */
			BlockingQueue<Message> qu = _services.get(_requester);
			qu.add(completed);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		/*
		 * Checking if the message bus knows of this type of broadcast,
		 * if not - making a queue to deal with them
		 */
		if (_listeners.get(b.getClass())==null){
			addMessageType(b.getClass());
		}
		/*
		 * Sending the broadcast to all of the subscribers.
		 */
		BlockingQueue<MicroService> _subscribers_queue=_listeners.get(b.getClass());
		for (MicroService m:_subscribers_queue){
			_services.get(m).add(b);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#sendRequest(bgu.spl.mics.Request, bgu.spl.mics.MicroService)
	 * 
	 * Using a hash map of <Request, MicroService> to map every received request to the service
	 * that requested it. comes in handy later in the this.complete() method.
	 */
	@Override
	public boolean sendRequest(Request<?> r, MicroService requester) {
		/*
		 * Checking if the bus knows of this type of request. if not, making a queue to deal with them
		 */
		if (_listeners.get(r.getClass())==null){
			addMessageType(r.getClass());
		}
		/*
		 * Checking if there is somebody to receive it
		 */
		if (_listeners.get(r.getClass()).isEmpty()){
			return false;
		}
		/*
		 * if there are listeners, getting a listener in a round-robin way.
		 */
		BlockingQueue<MicroService> q=(_listeners.get(r.getClass()));
		MicroService m = null;
		try {
			m = q.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/*
		 * getting the queue of the microservice and enqueueing the request there
		 */
		BlockingQueue<Message> _to_send_to=_services.get(m);
			_to_send_to.add(r);
		q.add(m);
		/*
		 * connecting the request and the requester through the map.
		 */
		_requestToRequester.putIfAbsent(r, requester);
		return true;
	}

	@Override
	public void register(MicroService m) {
		/*
		 * making a queue for requests for this service, and adding it the the map
		 */
		LinkedBlockingQueue<Message> q = new LinkedBlockingQueue<>();
		_services.putIfAbsent(m,q);
	}

	@Override
	public void unregister(MicroService m) {
		/*
		 * removing the microservice's queue
		 */
		for (BlockingQueue<MicroService> value : _listeners.values())
		{
			value.remove(m);
		}
		/*
		 * removing the micro service from listening to all messages
		 */
		for (MicroService f: _requestToRequester.values())
		{
			if (f == m) {
				_requestToRequester.values().remove(m);
			}
		}
		_services.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		/*
		 * waiting for a message and returning it
		 */
		BlockingQueue <Message> q = _services.get(m);
		if(q==null)
			System.out.println("q is null!!!");
		Message k = q.take();
		return k;
	}

	public void addMessageType(Class <? extends Message> type){
		/*
		 * adding this type of message to the bus
		 */
		BlockingQueue<MicroService> q=new LinkedBlockingQueue<>();
		_listeners.putIfAbsent(type,q);
	}
}
