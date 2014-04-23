package com.atlassian.eventmodel.thread;

import java.util.HashSet;
import java.util.Set;

public class ThreadLoop {

	public static ThreadPool threadPool = new ThreadPool();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("START");

		int emergencyBreak = 0;
		for (int i = 0; i < 40; i++) {

			if (emergencyBreak++ > 100) {
				System.out.println("EMERGENCY BREAK");
				break;
			}

			Thread t = threadPool.newThread(new Loop());
			if (t != null)
				t.start();

			try {
				Thread.sleep((int) (Math.random() * 200));
			} catch (InterruptedException e) {
			}

		}

		System.out.println("Interim pool size " + threadPool.getPoolSize());

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		System.out.println("(after 10 secs)Final pool size "
				+ threadPool.getPoolSize());
		threadPool.stop();
	}

	public static class Loop implements Runnable {

		public void run() {
			System.out.println("(" + Thread.currentThread().getId() + ") Thread Run initiated");
			System.out.println("(" + Thread.currentThread().getId() + ") DOING STUFF for random time");
			try {
				Thread.sleep((int) (Math.random() * 1000));
			} catch (InterruptedException e) {
			}
			System.out.println("(" + Thread.currentThread().getId() + ") Thread about to stop");
			Thread.currentThread().stop();
		}

	}

	public static class ThreadPool implements Runnable {

		private int maxPoolSize = 5;

		private boolean poolRunning = false;

		private Set<Thread> threadPool = new HashSet<Thread>();

		public ThreadPool() {
			poolRunning = true;

			// start cleaner thread
			Thread cleanerThread = new Thread(this);
			cleanerThread.start();
		}

		public Thread newThread(Runnable runnable) {
			Thread thread = null;
			if (!isPoolFull()) {
				thread = new Thread(runnable);
				addThread(thread);
			}
			return thread;
		}

		public void stop() {
			poolRunning = false;
		}

		public void addThread(Thread thread) {
			threadPool.add(thread);
		}

		public int getMaxPoolSize() {
			return maxPoolSize;
		}

		public void setMaxPoolSize(int maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
		}

		public Set<Thread> getThreadPool() {
			return threadPool;
		}

		public int getPoolSize() {
			return threadPool.size();
		}

		public boolean isPoolFull() {
			return threadPool.size() >= maxPoolSize;
		}

		public void removeThread(Thread thread) {
			threadPool.remove(thread);
		}

		public void cleanDeadThreads() {
			Set<Thread> markedForRemoval = new HashSet<Thread>();
			for (Thread thread : threadPool) {
				if (!thread.isAlive()) {
					System.out.println("(" + thread.getId()
							+ ") CLEAN UP - REMOVE FROM POOL");
					markedForRemoval.add(thread);
				} else {
					System.out.println("(" + thread.getId()
							+ ") CLEAN UP - STILL ALIVE KEEP");
				}
			}
			threadPool.removeAll(markedForRemoval);

		}

		/**
		 * To run pool cleaner thread
		 */
		public void run() {

			while (poolRunning) {
				try {
					Thread.sleep((int) (Math.random() * 1000));
				} catch (InterruptedException e) {
				}
				System.out.println("(" + Thread.currentThread().getId()
						+ ") Cleaner Thread *****");
				cleanDeadThreads();
			}

			cleanDeadThreads();

		}
	}

}
