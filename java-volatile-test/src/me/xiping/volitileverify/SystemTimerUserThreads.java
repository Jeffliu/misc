package me.xiping.volitileverify;

public class SystemTimerUserThreads {

	private static final String[] VERSIONS = new String[]{"V1(volatile)","V2(Synchronized)","V3(AtomicLong)"};

	static Runnable timerConsumer(final String version, final int runTimes) {

		for (String v : VERSIONS)
			if (v.equalsIgnoreCase(version))
				return new Runnable() {
					@Override
					public void run() {
						for (int i=0; i <= runTimes;i++)
							if (VERSIONS[0].equalsIgnoreCase(version)) {
								SystemTimerV1.currentTimeMillis();
							} else if (VERSIONS[1].equalsIgnoreCase(version)) {
								SystemTimerV2.currentTimeMillis();
							} else if (VERSIONS[2].equalsIgnoreCase(version)) {
								SystemTimerV3.currentTimeMillis();
							}
					}
				};

		throw new IllegalArgumentException();
	}

	public static void main(String[] args) {

		int runTimes = args.length>0 ? Integer.parseInt(args[0]) : 1000000;

		int threadnum = Runtime.getRuntime().availableProcessors() + 1;
		for(String version : VERSIONS)
			System.out.println(version + ":" + runTest(version, threadnum, runTimes));

		System.exit(0);
	}

	private static long runTest(String version, int threadnum, int runTimes) {

		long start = System.currentTimeMillis();

		Thread[] bussThreads = new Thread[threadnum];
		for (int i = 0; i < threadnum; i++) {
			Thread thread = new Thread(timerConsumer(version, runTimes));
			bussThreads[i] = thread;
			thread.start();
		}

		for (Thread t : bussThreads)
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		return System.currentTimeMillis() - start;
	}
}