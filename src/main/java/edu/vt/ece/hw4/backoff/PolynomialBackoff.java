package edu.vt.ece.hw4.backoff;

public class PolynomialBackoff implements Backoff {
    private int attempts = 0;
    private final int exponent;

    public PolynomialBackoff(int exponent) {
        this.exponent = exponent;
    }
    @Override
    public void backoff() throws InterruptedException {
        int delay = (int) Math.pow(attempts, exponent);
        attempts++;
        Thread.sleep(delay);
    }
}
