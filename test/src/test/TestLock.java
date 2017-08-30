package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TestLock {
	public static void testCounter() {
		final Counter c = new Counter();
		Thread t1 = new Thread() {
			public void run() {
				int i = 0;
				while (i++ < 10)
					System.out.println("1 " + c.inc2());
			}
		};

		Thread t2 = new Thread() {
			public void run() {
				int i = 0;
				while (i++ < 10)
					System.out.println("2 " + c.inc2());
			}
		};

		t1.start();
		t2.start();
	}
}

class Counter {

	private int count = 0;

	public int inc() {
		synchronized (this) {
			return ++count;
		}
	}

	Lock lockInc2 = new MyLock();

	public int inc2() {
		System.out.println(Thread.currentThread().getName() + " trying to take lock count = " + count);
		lockInc2.lock();
		int newCount = ++count;
		System.out.println(Thread.currentThread().getName() + " goig to sleep count = " + count);
		sleep(100);
		lockInc2.unlock();
		System.out.println(Thread.currentThread().getName() + " unlocked count = " + count);
		return newCount;
	}

	private void sleep(long i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class MyLock implements Lock {

	private boolean isLocked = false;

	@Override
	public synchronized void lock() {
		while (isLocked)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		isLocked = true;
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized void unlock() {
		isLocked = false;
		notify();
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
}

class MyReEnterantLock {
	private boolean isLocked = false;
	private Thread owner;
	private int count = 0;

	public synchronized void lock() {
		while (isLocked && !Thread.currentThread().equals(owner)) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		isLocked = true;
		owner = Thread.currentThread();
		count++;
	}

	public synchronized void unlock() {
		if(owner != Thread.currentThread())
			return;
		isLocked = false;		
		count--;
		if (count == 0) {
			owner = null;
			notify();
		}
			
		if (count < 0)
			throw new RuntimeException("can't notify when dont have lock");
	}
}