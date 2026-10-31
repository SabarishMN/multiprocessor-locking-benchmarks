package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpinSleepLock implements Lock {
    private final AtomicBoolean state = new AtomicBoolean(false);
    private final AtomicInteger spinCount = new AtomicInteger(0);
    private final int maxSpin;

    public SpinSleepLock(int maxSpin) {
        this.maxSpin = maxSpin;
    }

    @Override
    public void lock() {
        while (true) {
            // If the lock is free, try to acquire it
            if (!state.getAndSet(true)) {
                return; // Acquired lock
            }

            // If the maximum spin limit hasn't been reached, increment spin count and spin
            if (spinCount.get() < maxSpin) {
                spinCount.incrementAndGet();
                while (state.get()) {
                    // Spin
                }
                spinCount.decrementAndGet();
            } else {
                // If the spin limit is reached, go to sleep
                synchronized (this) {
                    try {
                        while (state.get()) {
                            wait(); // Sleep until notified
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    @Override
    public void unlock() {
        state.set(false);
        synchronized (this) {
            notify(); // Wake up one sleeping thread, if any
        }
    }

}