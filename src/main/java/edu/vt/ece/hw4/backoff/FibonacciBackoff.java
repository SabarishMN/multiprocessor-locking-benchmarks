package edu.vt.ece.hw4.backoff;

public class FibonacciBackoff implements Backoff {
    private int attempts = 0;
    private int prev = 0, curr = 1;

    @Override
    public void backoff() throws InterruptedException {
        int delay = curr;
        int next = prev + curr;
        prev = curr;
        curr = next;
        attempts++;
        Thread.sleep(delay);

    }
}
