package test;

public class TestWait {
	public static void testWait() {
		Task t = new Task();
		MyTask t1 = new MyTask(t);
		MyTask t2 = new MyTask(t);
		new Thread(t1).start();
		new Thread(t2).start();
	}
}

class MyTask implements Runnable {
	Task t;

	MyTask(Task t) {
		this.t = t;
	}

	public void run() {
		t.execute();
	}
}

class Task {
	int c = 0;

	public synchronized void execute() {
		System.out.println("");
		int i = 0;
		while (i++ < 10) {
			System.out.println(Thread.currentThread().getName() + " c = " + c++);
			try {
				System.out.println(Thread.currentThread().getName() + " going to notify  c = " + c);
				notify();
				System.out.println(Thread.currentThread().getName() + " going to wait  c = " + c);
				wait();
				System.out.println(Thread.currentThread().getName() + "end of wait  c = " + c);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
