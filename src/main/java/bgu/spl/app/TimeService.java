package bgu.spl.app;


import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import bgu.spl.mics.MicroService;

public class TimeService extends MicroService {

	private static Logger LOGGER = Logger.getGlobal();
	
	/**
	 * The timer used to count the ticks, according to predefined speed.
	 */
	private Timer timer;
	
	/**
	 * The predefined speed
	 */
	private int speed;
	
	/**
	 * The termination tick
	 */
	private int duration;
	
	/**
	 * The current tick (this is also a service that needs to store the current tick
	 */
	private int tick;
	
	/**
	 * The constructor.
	 * @param name - The name of the service (probably timeSomething)
	 * @param speed - The speed of the tick count.
	 * @param duration - The termination tick.
	 */
	public TimeService(String name, int speed, int duration) {
		super(name);
		this.timer = new Timer();
		this.speed = speed;
		this.duration = duration;
		this.tick = 0;
	}

	/**
	 * The timer only subscribes to tick broadcasts, since it needs to know when to
	 * Terminate.
	 */
	@Override
	protected void initialize() {
		/*
         * In this Subscription the timer awaits for the time tick to be 
         * the termination tick, and upon receiving it, the service terminates.
         */
		subscribeBroadcast(TickBroadcast.class, v->{
			this.current_tick=((TickBroadcast)v).getTick();
			if(current_tick<=v.getTerminationTick()){
				LOGGER.finest(this.getName() + ": Current tick is: " + this.current_tick);
			}else{
				// cancels the timer object.
				timer.cancel();
				LOGGER.info(this.getName() + ": Finished counting, terminating...");
				terminate();
			}
		});
		
		/*
		 * In this block the time service initializes and starts its timer object,
		 * using the predefined parameters.
		 */
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				tick++;
				LOGGER.info("Tick number: " + tick);
				sendBroadcast(new TickBroadcast(tick, duration));
				if(tick > duration){
					LOGGER.info("TimerTask tick is the last one, Terminating TimerTask...");
					this.cancel();
				}
			}
		};
		
		timer.schedule(task, 0, this.speed);
	}
}


