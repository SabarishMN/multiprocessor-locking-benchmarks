package edu.vt.ece.hw4.barriers;
import edu.vt.ece.spin.TTASLock;

public class TTASBarrier implements Barrier {
    private final int n;
    private final TTASLock lock = new TTASLock();
    private int counter = 0;

    public TTASBarrier(int n) {
        this.n = n;
    }
    public void enter() {
        lock.lock();
        counter++;
        lock.unlock();

        while (counter < n) {
            // Busy-wait until all threads have incremented the counter
        }
    }
}
