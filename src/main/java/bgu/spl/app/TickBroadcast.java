package bgu.spl.app;

import bgu.spl.mics.Broadcast;

/**
 * This is a broadcast message that is sent at every passed clock tick.
 * The TickBroadcast contains the following field:
 * <ul>
 * <li><b>tick: terminationTick</b> - The termination time tick.</li>
 * <li><b>tick: int</b> - The current time tick.</li>
 * </ul>
 * @author Avi
 *
 */
public final class TickBroadcast implements Broadcast {

	private final int terminationTick;
	private final int tick;
	
	/**
	 * The constructor. Receives as parameter this broadcast's time tick.
	 * @param tick <b>- int:</b> This broadcast's tick.
	 * @param terminationTick <b>- int:</b> The termination tick.
	 */
	public TickBroadcast(int tick, int terminationTick) {
		this.tick = tick;
		this.terminationTick = terminationTick;
	}

	/**
	 * Gets the time tick of this broadcast.
	 * @return int: This broadcast's tick.
	 */
	public int getTick() {
		return tick;
	}
	
	/**
	 * Gets the termination time tick.
	 * @return int: The termination time tick.
	 */
	public int getTerminationTick() {
		return terminationTick;
	}
	

}
