package edu.vt.ece.hw4.backoff;

public class ExponentialBackoff implements Backoff {
    private int attempts = 0;

    @Override
    public void backoff() throws InterruptedException {
        int delay = (int) Math.pow(2, attempts);
        attempts++;
        Thread.sleep(delay);
    }

}
