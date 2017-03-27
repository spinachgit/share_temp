package com.tag.framework.core.pool;

import java.util.LinkedList;

public class MyThreadPool {
	private LinkedList<Runnable> queue = new LinkedList();
	private int size;

	public MyThreadPool(int size) {
		this.size = size;
		for (int i = 0; i < size; i++)
			new WorkThread().start();
	}

	public synchronized Runnable getTask() {
		Runnable task = (Runnable) this.queue.poll();
		if (task == null) {
			try {
				System.out.println("wait...");
				wait();
				System.out.println("wait over...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return task;
	}

	public synchronized void excute(Runnable task) {
		System.out.println("excute...");
		this.queue.add(task);
		notifyAll();
	}

	public static void main(String[] args) {
		MyThreadPool threadPool = new MyThreadPool(4);
		for (int i = 0; i < 100; i++) {
			threadPool.excute(new MyRunnable());
		}
	}

	private static class MyRunnable implements Runnable {
		public static int i;

		public void run() {
			i += 1;
			try {
				Thread.sleep(250L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("i=====" + i);
		}
	}

	private class WorkThread extends Thread {
		private WorkThread() {
		}

		public void run() {
			while (true) {
				Runnable task = MyThreadPool.this.getTask();
				if (task != null)
					task.run();
			}
		}
	}
}