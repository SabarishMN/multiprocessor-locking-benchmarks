package edu.vt.ece.hw4.locks;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueueLock implements Lock {
    private static class Node implements Comparable<Node> {
        private final Thread thread;
        private final int priority;
        private final long timestamp;

        public Node(Thread thread, int priority) {
            this.thread = thread;
            this.priority = priority;
            this.timestamp = System.nanoTime(); // For fairness within same priority
        }

        @Override
        public int compareTo(Node other) {
            if (this.priority == other.priority) {
                return Long.compare(this.timestamp, other.timestamp);
            }
            return Integer.compare(this.priority, other.priority);
        }
    }
    private final PriorityBlockingQueue<Node> queue = new PriorityBlockingQueue<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final long tryLockTimeout;

    public PriorityQueueLock(long tryLockTimeout) {
        this.tryLockTimeout = tryLockTimeout;
    }

    @Override
    public void lock() {
        Node node = new Node(Thread.currentThread(), getThreadPriority());
        queue.add(node);

        while (true) {
            // Check if this thread's node is at the head of the queue
            if (queue.peek() == node && lock.tryLock()) {
                queue.remove(node); // Remove from queue when acquiring lock
                break;
            }
            // Spin-wait until the lock can be acquired in priority order
        }
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    public boolean tryLock() {
        Node node = new Node(Thread.currentThread(), getThreadPriority());
        queue.add(node);

        try {
            long startTime = System.nanoTime();
            while (System.nanoTime() - startTime < TimeUnit.MILLISECONDS.toNanos(tryLockTimeout)) {
                if (queue.peek() == node && lock.tryLock()) {
                    queue.remove(node);
                    return true;
                }
            }
            queue.remove(node); // Remove if timeout occurs without acquiring lock
        } catch (Exception e) {
            queue.remove(node); // Ensure node is removed on exception
        }
        return false;
    }

    private int getThreadPriority() {
        return (int) (Thread.currentThread().getId() % 5 + 1);
    }

}