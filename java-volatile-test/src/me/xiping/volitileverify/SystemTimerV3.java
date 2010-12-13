package me.xiping.volitileverify;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SystemTimerV3 {
	private final static ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor();
	private static final long tickUnit = Long.parseLong(System.getProperty(
			"notify.systimer.tick", "50"));
	private static AtomicLong time = new AtomicLong(System.currentTimeMillis());

	private static class TimerTicker implements Runnable {
		public void run() {
			time.set(System.currentTimeMillis());
		}
	}

	public static long currentTimeMillis() {
		return time.get();
	}

	static {
		executor.scheduleAtFixedRate(new TimerTicker(), tickUnit, tickUnit,
				TimeUnit.MILLISECONDS);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				executor.shutdown();
			}
		});
	}
}